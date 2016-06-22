package org.agreen.web.rest;

import org.agreen.AgreenApp;
import org.agreen.domain.Page;
import org.agreen.repository.PageRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the PageResource REST controller.
 *
 * @see PageResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AgreenApp.class)
@WebAppConfiguration
@IntegrationTest
public class PageResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";
    private static final String DEFAULT_CONTENT = "AAAAA";
    private static final String UPDATED_CONTENT = "BBBBB";
    private static final String DEFAULT_LINK = "AAAAA";
    private static final String UPDATED_LINK = "BBBBB";

    @Inject
    private PageRepository pageRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPageMockMvc;

    private Page page;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PageResource pageResource = new PageResource();
        ReflectionTestUtils.setField(pageResource, "pageRepository", pageRepository);
        this.restPageMockMvc = MockMvcBuilders.standaloneSetup(pageResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        pageRepository.deleteAll();
        page = new Page();
        page.setTitle(DEFAULT_TITLE);
        page.setContent(DEFAULT_CONTENT);
        page.setLink(DEFAULT_LINK);
    }

    @Test
    public void createPage() throws Exception {
        int databaseSizeBeforeCreate = pageRepository.findAll().size();

        // Create the Page

        restPageMockMvc.perform(post("/api/pages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(page)))
                .andExpect(status().isCreated());

        // Validate the Page in the database
        List<Page> pages = pageRepository.findAll();
        assertThat(pages).hasSize(databaseSizeBeforeCreate + 1);
        Page testPage = pages.get(pages.size() - 1);
        assertThat(testPage.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testPage.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testPage.getLink()).isEqualTo(DEFAULT_LINK);
    }

    @Test
    public void getAllPages() throws Exception {
        // Initialize the database
        pageRepository.save(page);

        // Get all the pages
        restPageMockMvc.perform(get("/api/pages?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(page.getId())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
                .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK.toString())));
    }

    @Test
    public void getPage() throws Exception {
        // Initialize the database
        pageRepository.save(page);

        // Get the page
        restPageMockMvc.perform(get("/api/pages/{id}", page.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(page.getId()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.link").value(DEFAULT_LINK.toString()));
    }

    @Test
    public void getNonExistingPage() throws Exception {
        // Get the page
        restPageMockMvc.perform(get("/api/pages/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updatePage() throws Exception {
        // Initialize the database
        pageRepository.save(page);
        int databaseSizeBeforeUpdate = pageRepository.findAll().size();

        // Update the page
        Page updatedPage = new Page();
        updatedPage.setId(page.getId());
        updatedPage.setTitle(UPDATED_TITLE);
        updatedPage.setContent(UPDATED_CONTENT);
        updatedPage.setLink(UPDATED_LINK);

        restPageMockMvc.perform(put("/api/pages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPage)))
                .andExpect(status().isOk());

        // Validate the Page in the database
        List<Page> pages = pageRepository.findAll();
        assertThat(pages).hasSize(databaseSizeBeforeUpdate);
        Page testPage = pages.get(pages.size() - 1);
        assertThat(testPage.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testPage.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testPage.getLink()).isEqualTo(UPDATED_LINK);
    }

    @Test
    public void deletePage() throws Exception {
        // Initialize the database
        pageRepository.save(page);
        int databaseSizeBeforeDelete = pageRepository.findAll().size();

        // Get the page
        restPageMockMvc.perform(delete("/api/pages/{id}", page.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Page> pages = pageRepository.findAll();
        assertThat(pages).hasSize(databaseSizeBeforeDelete - 1);
    }
}
