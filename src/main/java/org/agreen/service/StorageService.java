package org.agreen.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.agreen.domain.Document;
import org.agreen.domain.DocumentMetadata;
import org.agreen.service.interfaces.IStorageService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author Kojo Ampia - Addison
 */
@Component("storageService")
public class StorageService implements IStorageService {

    private static final Logger LOG = Logger.getLogger(StorageService.class);
    
    public static String STORAGE_ROOT;

    @Inject
    private Environment env;
    
    public static final String DOCUMENT_PROPERTIES = "document.properties";
    
    @PostConstruct
    public void init() {
    	STORAGE_ROOT = env.getProperty("client.root-directory");
		if(env.getProperty("os.name").toLowerCase().contains("window")){
			STORAGE_ROOT = env.getProperty("storage.root");
		}
    	LOG.debug("##### STORAGE_ROOT ####");
    	LOG.debug(STORAGE_ROOT);
    	LOG.debug("##### STORAGE_ROOT ####");
        createDirectory(STORAGE_ROOT);
    }
    
    /**
     * Inserts a document to the archive by creating a folder with the UUID
     * of the document. In the folder the document is saved and a properties file
     * with the meta data of the document. 
     * 
     * @see org.IStorageService.archive.dao.IDocumentDao#insert(org.murygin.archive.service.Document)
     */
    @Override
    public void insert(Document document) {
        try {
            createDirectory(document);
            saveFileData(document);
            saveMetaData(document);
        } catch (IOException e) {
            String message = "Error while inserting document";
            LOG.error(message, e);
            throw new RuntimeException(message, e);
        }
    }
    
    /**
     * Finds documents in the data store matching the given parameter.
     * To find a document all document meta data sets are iterated to check if they match
     * the parameter.
     * 
     * @see org.IStorageService.archive.dao.IDocumentDao#findByPersonNameDate(java.lang.String, java.util.LocalDate)
     */
    @Override
    public List<DocumentMetadata> findByCategoryDateCreated(String category, LocalDate date) {
        try {
            return findInFileSystem(category,date);
        } catch (IOException e) {
            String message = "Error while finding document, category name: " + category + ", date:" + date;
            LOG.error(message, e);
            throw new RuntimeException(message, e);
        }
    }

    /**
     * Finds documents in the data store matching the given parameter.
     * To find a document all document meta data sets are iterated to check if they match
     * the parameter.
     * 
     * @see org.IStorageService.archive.dao.IDocumentDao#findByPersonNameDate(java.lang.String, java.util.LocalDate)
     */
    @Override
    public List<DocumentMetadata> findByCategory(String category) {
        try {
            return findInFileSystem(category, null);
        } catch (IOException e) {
            String message = "Error while finding document, category name: " + category;
            LOG.error(message, e);
            throw new RuntimeException(message, e);
        }
    }
    /**
     * Finds documents in the data store matching the given parameter.
     * To find a document all document meta data sets are iterated to check if they match
     * the parameter.
     * 
     * @see org.IStorageService.archive.dao.IDocumentDao#findByPersonNameDate(java.lang.String, java.util.LocalDate)
     */
    @Override
    public List<DocumentMetadata> findAll() {
        try {
            return findInFileSystem(null, null);
        } catch (IOException e) {
            String message = "Error while finding documents";
            LOG.error(message, e);
            throw new RuntimeException(message, e);
        }
    }
    /**
     * Returns the document from the data store with the given UUID.
     * 
     * @see org.IStorageService.archive.dao.IDocumentDao#load(java.lang.String)
     */
    @Override
    public Document load(String uuid) {
        try {
            return loadFromFileSystem(uuid);
        } catch (IOException e) {
            String message = "Error while loading document with id: " + uuid;
            LOG.error(message, e);
            throw new RuntimeException(message, e);
        }
        
    }
    

    private List<DocumentMetadata> findInFileSystem(String category, LocalDate date) throws IOException  {
        List<String> uuidList = getUuidList();
        List<DocumentMetadata> metadataList = new ArrayList<DocumentMetadata>(uuidList.size());
        for (String uuid : uuidList) {
            DocumentMetadata metadata = loadMetadataFromFileSystem(uuid);         
            if(isMatched(metadata, category, date)) {
                metadataList.add(metadata);
            }
        }
        return metadataList;
    }

    private boolean isMatched(DocumentMetadata metadata, String category, LocalDate date) {
        if(metadata==null) {
            return false;
        }
        boolean match = true;
        if(category!=null) {
            match = (category.equals(metadata.getCategory()));
        }
        if(match && date!=null) {
            match = (date.equals(metadata.getDateCreated()));
        }
        return match;
    }

    private DocumentMetadata loadMetadataFromFileSystem(String uuid) throws IOException {
        DocumentMetadata document = null;
        String dirPath = getDirectoryPath(uuid);
        File file = new File(dirPath);
        if(file.exists()) {
            Properties properties = readProperties(uuid);
            document = new DocumentMetadata(properties);
            
        } 
        return document;
    }
    
    private Document loadFromFileSystem(String uuid) throws IOException {
       DocumentMetadata metadata = loadMetadataFromFileSystem(uuid);
       if(metadata==null) {
           return null;
       }
       Path path = Paths.get(getFilePath(metadata));
       Document document = new Document(metadata);
       document.setFileData(Files.readAllBytes(path));
       return document;
    }

    private String getFilePath(DocumentMetadata metadata) {
        String dirPath = getDirectoryPath(metadata.getUuid());
        StringBuilder sb = new StringBuilder();
        sb.append(dirPath).append(File.separator).append(metadata.getFileName());
        return sb.toString();
    }
    
    private void saveFileData(Document document) throws IOException {
        String path = getDirectoryPath(document);
        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(new File(path), document.getFileName())));
        stream.write(document.getFileData());
        stream.close();
    }
    
    public void saveMetaData(Document document) throws IOException {
            String path = getDirectoryPath(document);
            Properties props = document.createProperties();
            File f = new File(new File(path), DOCUMENT_PROPERTIES);
            OutputStream out = new FileOutputStream( f );
            props.store(out, "Document meta data");       
    }
    
    private List<String> getUuidList() {
        File file = new File(STORAGE_ROOT);
        String[] directories = file.list(new FilenameFilter() {
          @Override
          public boolean accept(File current, String name) {
            return new File(current, name).isDirectory();
          }
        });
        return Arrays.asList(directories);
    }
    
    private Properties readProperties(String uuid) throws IOException {
        Properties prop = new Properties();
        InputStream input = null;     
        try {
            input = new FileInputStream(new File(getDirectoryPath(uuid),DOCUMENT_PROPERTIES));
            prop.load(input);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return prop;
    }
    
    private String createDirectory(Document document) {
        String path = getDirectoryPath(document);
        createDirectory(path);
        return path;
    }

    private String getDirectoryPath(Document document) {
       return getDirectoryPath(document.getUuid());
    }
    
    private String getDirectoryPath(String uuid) {
        StringBuilder sb = new StringBuilder();
        sb.append(STORAGE_ROOT).append(File.separator).append(uuid);
        String path = sb.toString();
        return path;
    }

    private void createDirectory(String path) {
        File file = new File(path);
        file.mkdirs();
    }

}
