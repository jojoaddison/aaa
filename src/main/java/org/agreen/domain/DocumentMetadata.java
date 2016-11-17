package org.agreen.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.UUID;

/**
 * Document Metadata 
 * @see {@link IDocumentService}.
 * @author Kojo Ampia - Addison
 */
public class DocumentMetadata implements Serializable {    

    /**
	 * 
	 */
	private static final long serialVersionUID = -8851123055190207973L;
    
    public static final String UUID_KEY = "uuid";
    public static final String CATEGORY_KEY = "category";
    public static final String FILE_KEY = "filename";
    public static final String DATE_KEY = "date";
    public static final String FILE_TYPE = "type";
    public static final String DATE_FORMATTER = "yyyyMMdd";
    
    protected String uuid;
    protected String fileName;
    protected LocalDate dateCreated;
    protected String category;
    protected String type;
    
    public DocumentMetadata() {
        super();
    }

    public DocumentMetadata(String fileName, String type, LocalDate dateCreated, String category) {
        this(UUID.randomUUID().toString(), fileName, type, dateCreated,category);
    }
    
    public DocumentMetadata(String uuid, String fileName, String type, LocalDate dateCreated, String category) {
        super();
        this.uuid = uuid;
        this.fileName = fileName;
        this.dateCreated = dateCreated;
        this.category = category;
        this.type = type;
    }
    
    public DocumentMetadata(Properties properties) {
    	
        this(properties.getProperty(UUID_KEY),
             properties.getProperty(FILE_KEY),
             properties.getProperty(FILE_TYPE),
             null,
             properties.getProperty(CATEGORY_KEY));
        
        String dateString = properties.getProperty(DATE_KEY);
        
        if(dateString!=null) {
            this.dateCreated = LocalDate.parse(dateString);
        }    
        
    }

    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public void setFileType(String type){
    	this.type = type;
    }
    
    public String getFileType(){
    	return type;
    }
    
    public LocalDate getDateCreated() {
        return dateCreated;
    }
    public void setDocumentDate(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }
    
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    
    public Properties createProperties() {
        Properties props = new Properties();
        props.setProperty(UUID_KEY, getUuid());
        props.setProperty(FILE_KEY, getFileName());
        props.setProperty(FILE_TYPE, getFileType());
        props.setProperty(CATEGORY_KEY, getCategory());
        props.setProperty(DATE_KEY, getDateCreated().format(DateTimeFormatter.ISO_LOCAL_DATE));
        return props;
    }
    
    
}
