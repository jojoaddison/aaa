package org.agreen.service.interfaces;

import java.time.LocalDate;
import java.util.List;

import org.agreen.domain.Document;
import org.agreen.domain.DocumentMetadata;


/**
 * A service to save, find and get documents from an archive. 
 * 
 * @author Daniel Murygin <daniel.murygin[at]gmail[dot]com>
 */
public interface IDocumentService {
    
    /**
     * Saves a document in the archive.
     * 
     * @param document A document
     * @return DocumentMetadata The meta data of the saved document
     */
    DocumentMetadata save(Document document);
    
    /**
     * Finds document in the archive matching the given parameter.
     * A list of document meta data which does not include the file data.
     * Use getDocumentFile and the id from the meta data to get the file.
     * Returns an empty list if no document was found.
     * 
     * @param personName The name of a person, may be null
     * @param date The date of a document, may be null
     * @return A list of document meta data
     */
    List<DocumentMetadata> findDocuments(String category, LocalDate date);

    /**
     * Finds document in the archive matching the given parameter.
     * A list of document meta data which does not include the file data.
     * Use getDocumentFile and the id from the meta data to get the file.
     * Returns an empty list if no document was found.
     * 
     * @param personName The name of a person, may be null
     * @param date The date of a document, may be null
     * @return A list of document meta data
     */
	List<DocumentMetadata> findDocumentsByDate(LocalDate date);

    /**
     * Finds document in the archive matching the given parameter.
     * A list of document meta data which does not include the file data.
     * Use getDocumentFile and the id from the meta data to get the file.
     * Returns an empty list if no document was found.
     * 
     * @param personName The name of a person, may be null
     * @param date The date of a document, may be null
     * @return A list of document meta data
     */
    List<DocumentMetadata> findDocuments(String category);


    /**
     * Finds document in the archive matching the given parameter.
     * A list of document meta data which does not include the file data.
     * Use getDocumentFile and the id from the meta data to get the file.
     * Returns an empty list if no document was found.
     * 
     * @param personName The name of a person, may be null
     * @param date The date of a document, may be null
     * @return A list of document meta data
     */
    List<DocumentMetadata> findDocuments();
    
    /**
     * Returns the document file from the archive with the given id.
     * Returns null if no document was found.
     * 
     * @param id The id of a document
     * @return A document file
     */
    byte[] getDocumentFile(String id);
    /**
     * Returns the document from the archive with the given id.
     * Returns null if no document was found.
     * 
     * @param id The id of a document
     * @return A document 
     */
	Document getDocument(String id);

}
