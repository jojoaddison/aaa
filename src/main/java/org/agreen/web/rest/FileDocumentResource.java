package org.agreen.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.agreen.domain.FileDocument;
import org.agreen.repository.FileDocumentRepository;
import org.agreen.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing FileDocument.
 */
@RestController
@RequestMapping("/api")
public class FileDocumentResource {

    private final Logger log = LoggerFactory.getLogger(FileDocumentResource.class);
        
    @Inject
    private FileDocumentRepository fileDocumentRepository;
    
    /**
     * POST  /file-documents : Create a new fileDocument.
     *
     * @param fileDocument the fileDocument to create
     * @return the ResponseEntity with status 201 (Created) and with body the new fileDocument, or with status 400 (Bad Request) if the fileDocument has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/file-documents",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FileDocument> createFileDocument(@RequestBody FileDocument fileDocument) throws URISyntaxException {
        log.debug("REST request to save FileDocument : {}", fileDocument);
        if (fileDocument.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("fileDocument", "idexists", "A new fileDocument cannot already have an ID")).body(null);
        }
        FileDocument result = fileDocumentRepository.save(fileDocument);
        return ResponseEntity.created(new URI("/api/file-documents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("fileDocument", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /file-documents : Updates an existing fileDocument.
     *
     * @param fileDocument the fileDocument to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated fileDocument,
     * or with status 400 (Bad Request) if the fileDocument is not valid,
     * or with status 500 (Internal Server Error) if the fileDocument couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/file-documents",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FileDocument> updateFileDocument(@RequestBody FileDocument fileDocument) throws URISyntaxException {
        log.debug("REST request to update FileDocument : {}", fileDocument);
        if (fileDocument.getId() == null) {
            return createFileDocument(fileDocument);
        }
        FileDocument result = fileDocumentRepository.save(fileDocument);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("fileDocument", fileDocument.getId().toString()))
            .body(result);
    }

    /**
     * GET  /file-documents : get all the fileDocuments.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of fileDocuments in body
     */
    @RequestMapping(value = "/file-documents",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<FileDocument> getAllFileDocuments() {
        log.debug("REST request to get all FileDocuments");
        List<FileDocument> fileDocuments = fileDocumentRepository.findAll();
        return fileDocuments;
    }

    /**
     * GET  /file-documents/:id : get the "id" fileDocument.
     *
     * @param id the id of the fileDocument to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fileDocument, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/file-documents/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FileDocument> getFileDocument(@PathVariable String id) {
        log.debug("REST request to get FileDocument : {}", id);
        FileDocument fileDocument = fileDocumentRepository.findOne(id);
        return Optional.ofNullable(fileDocument)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /file-documents/:id : delete the "id" fileDocument.
     *
     * @param id the id of the fileDocument to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/file-documents/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFileDocument(@PathVariable String id) {
        log.debug("REST request to delete FileDocument : {}", id);
        fileDocumentRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("fileDocument", id.toString())).build();
    }

}
