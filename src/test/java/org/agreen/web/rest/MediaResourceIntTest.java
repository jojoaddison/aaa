package org.agreen.web.rest;

import org.agreen.AgreenApp;
import org.agreen.domain.Media;
import org.agreen.repository.MediaRepository;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the MediaResource REST controller.
 *
 * @see MediaResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AgreenApp.class)
@WebAppConfiguration
@IntegrationTest
public class MediaResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_URL = "AAAAA";
    private static final String UPDATED_URL = "BBBBB";
    private static final String DEFAULT_THUMB = "AAAAA";
    private static final String UPDATED_THUMB = "BBBBB";
    private static final String DEFAULT_TYPE = "AAAAA";
    private static final String UPDATED_TYPE = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final Long DEFAULT_SIZE = 1L;
    private static final Long UPDATED_SIZE = 2L;

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATED_DATE_STR = dateTimeFormatter.format(DEFAULT_CREATED_DATE);

    private static final ZonedDateTime DEFAULT_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_MODIFIED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_MODIFIED_DATE_STR = dateTimeFormatter.format(DEFAULT_MODIFIED_DATE);

    @Inject
    private MediaRepository mediaRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMediaMockMvc;

    private Media media;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MediaResource mediaResource = new MediaResource();
        ReflectionTestUtils.setField(mediaResource, "mediaRepository", mediaRepository);
        this.restMediaMockMvc = MockMvcBuilders.standaloneSetup(mediaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        mediaRepository.deleteAll();
        media = new Media();
        media.setName(DEFAULT_NAME);
        media.setUrl(DEFAULT_URL);
        media.setThumb(DEFAULT_THUMB);
        media.setType(DEFAULT_TYPE);
        media.setDescription(DEFAULT_DESCRIPTION);
        media.setSize(DEFAULT_SIZE);
        media.setCreatedDate(DEFAULT_CREATED_DATE);
        media.setModifiedDate(DEFAULT_MODIFIED_DATE);
    }

    @Test
    public void createMedia() throws Exception {
        int databaseSizeBeforeCreate = mediaRepository.findAll().size();

        // Create the Media

        restMediaMockMvc.perform(post("/api/media")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(media)))
                .andExpect(status().isCreated());

        // Validate the Media in the database
        List<Media> media = mediaRepository.findAll();
        assertThat(media).hasSize(databaseSizeBeforeCreate + 1);
        Media testMedia = media.get(media.size() - 1);
        assertThat(testMedia.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMedia.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testMedia.getThumb()).isEqualTo(DEFAULT_THUMB);
        assertThat(testMedia.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testMedia.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMedia.getSize()).isEqualTo(DEFAULT_SIZE);
        assertThat(testMedia.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testMedia.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
    }

    @Test
    public void getAllMedia() throws Exception {
        // Initialize the database
        mediaRepository.save(media);

        // Get all the media
        restMediaMockMvc.perform(get("/api/media?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(media.getId())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
                .andExpect(jsonPath("$.[*].thumb").value(hasItem(DEFAULT_THUMB.toString())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE.intValue())))
                .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE_STR)))
                .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE_STR)));
    }

    @Test
    public void getMedia() throws Exception {
        // Initialize the database
        mediaRepository.save(media);

        // Get the media
        restMediaMockMvc.perform(get("/api/media/{id}", media.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(media.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.thumb").value(DEFAULT_THUMB.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.size").value(DEFAULT_SIZE.intValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE_STR))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE_STR));
    }

    @Test
    public void getNonExistingMedia() throws Exception {
        // Get the media
        restMediaMockMvc.perform(get("/api/media/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateMedia() throws Exception {
        // Initialize the database
        mediaRepository.save(media);
        int databaseSizeBeforeUpdate = mediaRepository.findAll().size();

        // Update the media
        Media updatedMedia = new Media();
        updatedMedia.setId(media.getId());
        updatedMedia.setName(UPDATED_NAME);
        updatedMedia.setUrl(UPDATED_URL);
        updatedMedia.setThumb(UPDATED_THUMB);
        updatedMedia.setType(UPDATED_TYPE);
        updatedMedia.setDescription(UPDATED_DESCRIPTION);
        updatedMedia.setSize(UPDATED_SIZE);
        updatedMedia.setCreatedDate(UPDATED_CREATED_DATE);
        updatedMedia.setModifiedDate(UPDATED_MODIFIED_DATE);

        restMediaMockMvc.perform(put("/api/media")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedMedia)))
                .andExpect(status().isOk());

        // Validate the Media in the database
        List<Media> media = mediaRepository.findAll();
        assertThat(media).hasSize(databaseSizeBeforeUpdate);
        Media testMedia = media.get(media.size() - 1);
        assertThat(testMedia.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMedia.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testMedia.getThumb()).isEqualTo(UPDATED_THUMB);
        assertThat(testMedia.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testMedia.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMedia.getSize()).isEqualTo(UPDATED_SIZE);
        assertThat(testMedia.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testMedia.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    public void deleteMedia() throws Exception {
        // Initialize the database
        mediaRepository.save(media);
        int databaseSizeBeforeDelete = mediaRepository.findAll().size();

        // Get the media
        restMediaMockMvc.perform(delete("/api/media/{id}", media.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Media> media = mediaRepository.findAll();
        assertThat(media).hasSize(databaseSizeBeforeDelete - 1);
    }
}
