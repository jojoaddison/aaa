package org.agreen.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Properties;

/**
 * A document from an archive managed by {@link IDocumentService}.
 * 
 * @author Kojo Ampia Addison
 */
public class Document extends DocumentMetadata implements Serializable {

    private static final long serialVersionUID = 2004955454853853315L;
    
    private byte[] fileData;
    
    public Document( byte[] fileData, String fileName, String type, LocalDate dateCreated, String categoryName) {
        super(fileName, type, dateCreated, categoryName);
        this.fileData = fileData;
    }

    public Document(Properties properties) {
        super(properties);
    }
    
    public Document(DocumentMetadata metadata) {
        super(metadata.getUuid(), metadata.getFileName(), metadata.getFileType(), metadata.getDateCreated(), metadata.getCategory());
    }

    public byte[] getFileData() {
        return fileData;
    }
    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }
    
    public DocumentMetadata getMetadata() {
        return new DocumentMetadata(getUuid(), getFileName(), getFileType(), getDateCreated(), getCategory());
    }
    
}
