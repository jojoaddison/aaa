package org.agreen.service;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.inject.Inject;

import org.agreen.domain.Document;
import org.agreen.domain.DocumentMetadata;
import org.agreen.service.interfaces.IDocumentService;
import org.agreen.service.interfaces.IStorageService;
import org.springframework.stereotype.Service;

/**
 * 
 * @author Kojo Ampia - Addison
 * 
 */
@Service
public class DocumentService implements IDocumentService, Serializable {

    
    /**
	 * 
	 */
	private static final long serialVersionUID = 3815737393612262431L;

	@Inject
    private IStorageService storageService;


    /**
     * Saves a document in the archive.
     * @see org.IDocumentService.archive.service.IArchiveService#save(org.murygin.archive.service.Document)
     */
    @Override
    public DocumentMetadata save(Document document) {
        getstorageService().insert(document); 
        return document.getMetadata();
    }
    
    /**
     * Finds document in the archive
     * @see org.IDocumentService.archive.service.IArchiveService#findDocuments(java.lang.String, java.util.LocalDate)
     */
    @Override
    public List<DocumentMetadata> findDocuments(String category, LocalDate date) {
        return getstorageService().findByCategoryDateCreated(category, date);
    }

    /**
     * Finds document in the archive
     * @see org.IDocumentService.archive.service.IArchiveService#findDocuments(java.lang.String, java.util.LocalDate)
     */
    @Override
    public List<DocumentMetadata> findDocumentsByDate(LocalDate date) {
        return getstorageService().findByCategoryDateCreated(null, date);
    }

    /**
     * Finds document in the archive
     * @see org.IDocumentService.archive.service.IArchiveService#findDocuments(java.lang.String, java.util.LocalDate)
     */
    @Override
    public List<DocumentMetadata> findDocuments(String category) {
        return getstorageService().findByCategory(category);
    }

    /**
     * Finds document in the archive
     * @see org.IDocumentService.archive.service.IArchiveService#findDocuments(java.lang.String, java.util.LocalDate)
     */
    @Override
    public List<DocumentMetadata> findDocuments() {
        return getstorageService().findAll();
    }
    
    /**
     * Returns the document file from the archive
     * @see org.IDocumentService.archive.service.IArchiveService#getDocumentFile(java.lang.String)
     */
    @Override
    public byte[] getDocumentFile(String id) {
        Document document = getstorageService().load(id);
        if(document!=null) {
            return document.getFileData();
        } else {
            return null;
        }
    }

    /**
     * Returns the document file from the archive
     * @see org.IDocumentService.archive.service.IArchiveService#getDocumentFile(java.lang.String)
     */
    @Override
    public Document getDocument(String id) {
        return getstorageService().load(id);
    }

    public IStorageService getstorageService() {
        return storageService;
    }

    public void setstorageService(IStorageService documentDao) {
        storageService = documentDao;
    }


}
