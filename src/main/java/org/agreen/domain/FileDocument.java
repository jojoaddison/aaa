package org.agreen.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A FileDocument.
 */

@Document(collection = "file_document")
public class FileDocument implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("category")
    private String category;

    @Field("date_created")
    private LocalDate dateCreated;

    @Field("file_type")
    private String fileType;

    @Field("file_data")
    private byte[] fileData;

    @Field("file_name")
    private String fileName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FileDocument fileDocument = (FileDocument) o;
        if(fileDocument.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, fileDocument.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "FileDocument{" +
            "id=" + id +
            ", category='" + category + "'" +
            ", dateCreated='" + dateCreated + "'" +
            ", fileType='" + fileType + "'" +
            ", fileData='" + fileData + "'" +
            ", fileName='" + fileName + "'" +
            '}';
    }
}
