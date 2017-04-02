package org.agreen.web.rest;

import org.agreen.AgreenApp;
import org.agreen.domain.FileDocument;
import org.agreen.repository.FileDocumentRepository;

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

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the FileDocumentResource REST controller.
 *
 * @see FileDocumentResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AgreenApp.class)
@WebAppConfiguration
@IntegrationTest
public class FileDocumentResourceIntTest {

    private static final String DEFAULT_CATEGORY = "AAAAA";
    private static final String UPDATED_CATEGORY = "BBBBB";
    private static final LocalDate DEFAULT_DATE_CREATED = LocalDate.now();
    private static final LocalDate UPDATED_DATE_CREATED = LocalDate.now();
    private static final String DEFAULT_FILE_TYPE = "AAAAA";
    private static final String UPDATED_FILE_TYPE = "BBBBB";
    private static final byte[] DEFAULT_FILE_DATA = ("AAAAA").getBytes();
    private static final byte[] UPDATED_FILE_DATA = ("BBBBB").getBytes();
    private static final String DEFAULT_FILE_NAME = "AAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBB";

    @Inject
    private FileDocumentRepository fileDocumentRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restFileDocumentMockMvc;

    private FileDocument fileDocument;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FileDocumentResource fileDocumentResource = new FileDocumentResource();
        ReflectionTestUtils.setField(fileDocumentResource, "fileDocumentRepository", fileDocumentRepository);
        this.restFileDocumentMockMvc = MockMvcBuilders.standaloneSetup(fileDocumentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        fileDocumentRepository.deleteAll();
        fileDocument = new FileDocument();
        fileDocument.setCategory(DEFAULT_CATEGORY);
        fileDocument.setDateCreated(DEFAULT_DATE_CREATED);
        fileDocument.setFileType(DEFAULT_FILE_TYPE);
        fileDocument.setFileData(DEFAULT_FILE_DATA);
        fileDocument.setFileName(DEFAULT_FILE_NAME);
    }

    @Test
    public void createFileDocument() throws Exception {
        int databaseSizeBeforeCreate = fileDocumentRepository.findAll().size();

        // Create the FileDocument

        restFileDocumentMockMvc.perform(post("/api/file-documents")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fileDocument)))
                .andExpect(status().isCreated());

        // Validate the FileDocument in the database
        List<FileDocument> fileDocuments = fileDocumentRepository.findAll();
        assertThat(fileDocuments).hasSize(databaseSizeBeforeCreate + 1);
        FileDocument testFileDocument = fileDocuments.get(fileDocuments.size() - 1);
        assertThat(testFileDocument.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testFileDocument.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testFileDocument.getFileType()).isEqualTo(DEFAULT_FILE_TYPE);
        assertThat(testFileDocument.getFileData()).isEqualTo(DEFAULT_FILE_DATA);
        assertThat(testFileDocument.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
    }

    @Test
    public void getAllFileDocuments() throws Exception {
        // Initialize the database
        fileDocumentRepository.save(fileDocument);

        // Get all the fileDocuments
        restFileDocumentMockMvc.perform(get("/api/file-documents?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(fileDocument.getId())))
                .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
                .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
                .andExpect(jsonPath("$.[*].fileType").value(hasItem(DEFAULT_FILE_TYPE.toString())))
                .andExpect(jsonPath("$.[*].fileData").value(hasItem(DEFAULT_FILE_DATA.toString())))
                .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME.toString())));
    }

    @Test
    public void getFileDocument() throws Exception {
        // Initialize the database
        fileDocumentRepository.save(fileDocument);

        // Get the fileDocument
        restFileDocumentMockMvc.perform(get("/api/file-documents/{id}", fileDocument.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(fileDocument.getId()))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.fileType").value(DEFAULT_FILE_TYPE.toString()))
            .andExpect(jsonPath("$.fileData").value(DEFAULT_FILE_DATA.toString()))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME.toString()));
    }

    @Test
    public void getNonExistingFileDocument() throws Exception {
        // Get the fileDocument
        restFileDocumentMockMvc.perform(get("/api/file-documents/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateFileDocument() throws Exception {
        // Initialize the database
        fileDocumentRepository.save(fileDocument);
        int databaseSizeBeforeUpdate = fileDocumentRepository.findAll().size();

        // Update the fileDocument
        FileDocument updatedFileDocument = new FileDocument();
        updatedFileDocument.setId(fileDocument.getId());
        updatedFileDocument.setCategory(UPDATED_CATEGORY);
        updatedFileDocument.setDateCreated(UPDATED_DATE_CREATED);
        updatedFileDocument.setFileType(UPDATED_FILE_TYPE);
        updatedFileDocument.setFileData(UPDATED_FILE_DATA);
        updatedFileDocument.setFileName(UPDATED_FILE_NAME);

        restFileDocumentMockMvc.perform(put("/api/file-documents")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedFileDocument)))
                .andExpect(status().isOk());

        // Validate the FileDocument in the database
        List<FileDocument> fileDocuments = fileDocumentRepository.findAll();
        assertThat(fileDocuments).hasSize(databaseSizeBeforeUpdate);
        FileDocument testFileDocument = fileDocuments.get(fileDocuments.size() - 1);
        assertThat(testFileDocument.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testFileDocument.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testFileDocument.getFileType()).isEqualTo(UPDATED_FILE_TYPE);
        assertThat(testFileDocument.getFileData()).isEqualTo(UPDATED_FILE_DATA);
        assertThat(testFileDocument.getFileName()).isEqualTo(UPDATED_FILE_NAME);
    }

    @Test
    public void deleteFileDocument() throws Exception {
        // Initialize the database
        fileDocumentRepository.save(fileDocument);
        int databaseSizeBeforeDelete = fileDocumentRepository.findAll().size();

        // Get the fileDocument
        restFileDocumentMockMvc.perform(delete("/api/file-documents/{id}", fileDocument.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<FileDocument> fileDocuments = fileDocumentRepository.findAll();
        assertThat(fileDocuments).hasSize(databaseSizeBeforeDelete - 1);
    }
}
