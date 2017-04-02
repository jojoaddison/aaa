package org.agreen.web.rest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.agreen.domain.Document;
import org.agreen.domain.DocumentMetadata;
import org.agreen.service.interfaces.IDocumentService;
import org.apache.log4j.Logger;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * REST controller for managing Album.
 */
@RestController
@RequestMapping("/api")
public class DocumentResource {

    private static final Logger LOG = Logger.getLogger(DocumentResource.class);
    
    @Inject
    private IDocumentService documentService;

    /**
     * Adds a document to the archive.
     * 
     * Url: /api/upload?file={file}&category={category}&date={date} [POST]
     * 
     * @param file A file posted in a multipart request
     * @param category The name of the uploading category
     * @param date The date of the document
     * @return The meta data of the added document
     */
    @RequestMapping(value = "/documents/upload", method = RequestMethod.POST)
    public @ResponseBody List<DocumentMetadata> handleFileUpload(
            @RequestParam(value="files", required=true) MultipartFile[] files ,
            @RequestParam(value="category", required=true) String category) {
        List<DocumentMetadata> results = new ArrayList<>();
        LocalDate date = LocalDate.now();
        		
        try {
        	for(MultipartFile file: files){
                Document document = new Document(file.getBytes(), file.getContentType(), file.getOriginalFilename(), date, category );
                documentService.save(document);
                results.add(document.getMetadata()); 
        	}           
            return results;
        } catch (RuntimeException e) {
            LOG.error("Error while uploading.", e);
            throw e;
        } catch (Exception e) {
            LOG.error("Error while uploading.", e);
            throw new RuntimeException(e);
        }      
    }
    

    /**
     * Adds a document to the archive.
     * 
     * Url: /api/upload?file={file}&category={category}&date={date} [POST]
     * 
     * @param file A file posted in a multipart request
     * @param category The name of the uploading category
     * @param date The date of the document
     * @return The meta data of the added document
     */
    @RequestMapping(value = "/document/upload", method = RequestMethod.POST)
    public @ResponseBody DocumentMetadata handleFileUpload(
            @RequestParam(value="file", required=true) MultipartFile file ,
            @RequestParam(value="category", required=true) String category) {
        	LocalDate date = LocalDate.now();
        try {
            Document document = new Document(file.getBytes(), file.getOriginalFilename(), file.getContentType(), date, category );
            documentService.save(document);
            return document.getMetadata();
        } catch (RuntimeException e) {
            LOG.error("Error while uploading.", e);
            throw e;
        } catch (Exception e) {
            LOG.error("Error while uploading.", e);
            throw new RuntimeException(e);
        }      
    }
    
    /**
     * Finds document in the archive. Returns a list of document meta data 
     * which does not include the file data. Use getDocument to get the file.
     * Returns an empty list if no document was found.
     * 
     * Url: /api/documents/by-category-date/{category}/{date} [GET]
     * 
     * @param category The name of the uploading category
     * @param date The date of the document
     * @return A list of document meta data
     */
    @RequestMapping(value = "/documents/by-category-date/{category}/{date}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DocumentMetadata>> findDocument(
    		@PathVariable("category") String category,
            @PathVariable("date") @DateTimeFormat(pattern="yyyyMMdd") LocalDate date) {
        HttpHeaders httpHeaders = new HttpHeaders();
        return new ResponseEntity<List<DocumentMetadata>>(documentService.findDocuments(category,date), httpHeaders,HttpStatus.OK);
    }

    /**
     * Finds document in the archive. Returns a list of document meta data 
     * which does not include the file data. Use getDocument to get the file.
     * Returns an empty list if no document was found.
     * 
     * Url: /api/documents/by-category/{category}
     * 
     * @param category The name of the uploading category
     * @param date The date of the document
     * @return A list of document meta data
     */
    @RequestMapping(value = "/documents/by-category/{category}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DocumentMetadata>> findDocumentByCategory(
    		@PathVariable("category") String category) {
        HttpHeaders httpHeaders = new HttpHeaders();
        return new ResponseEntity<List<DocumentMetadata>>(documentService.findDocuments(category), httpHeaders,HttpStatus.OK);
    }

    /**
     * Finds document in the archive. Returns a list of document meta data 
     * which does not include the file data. Use getDocument to get the file.
     * Returns an empty list if no document was found.
     * 
     * Url: /api/documents/by-date/{date} [GET]
     * 
     * @param category The name of the uploading category
     * @param date The date of the document
     * @return A list of document meta data
     */
    @RequestMapping(value = "/documents/by-date/{category}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DocumentMetadata>> findDocumentByDate(
    		@PathVariable("date") @DateTimeFormat(pattern="yyyyMMdd") LocalDate date) {
        HttpHeaders httpHeaders = new HttpHeaders();
        return new ResponseEntity<List<DocumentMetadata>>(documentService.findDocumentsByDate(date), httpHeaders,HttpStatus.OK);
    }
    /**
     * Returns all documents.
     * 
     * Url: /api/documents [GET]
     * 
     * @param id The UUID of a document
     * @return The document file
     */
    @RequestMapping(value = "/documents", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
    public List<DocumentMetadata> getDocuments() {         
        // send it back to the client
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.IMAGE_JPEG);
        List<DocumentMetadata> documents = documentService.findDocuments();
        return documents;
    }

    /**
     * Returns the document file from the archive with the given UUID.
     * 
     * Url: /api/document/{id} [GET]
     * 
     * @param id The UUID of a document
     * @return The document file
     */
    @RequestMapping(value = "/document/{id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getDocument(@PathVariable String id) {         
        // send it back to the client
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.IMAGE_JPEG);
        Document document = documentService.getDocument(id);
        return Optional.ofNullable(document)
                .map(result -> new ResponseEntity<byte[]>(
                    document.getFileData(),
                    httpHeaders,
                    HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
