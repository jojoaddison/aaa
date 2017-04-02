package org.agreen.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Album.
 */

@Document(collection = "album")
public class Album implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("url")
    private String url;

    @Field("description")
    private String description;

    @Field("media")
    private Set<Media> media = new HashSet<>();

    @Field("is_default")
    private boolean isDefault = false;

    @Field("created")
    private ZonedDateTime createdDate;

    @Field("modified")
    private ZonedDateTime modifiedDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Media> getMedia() {
        return media;
    }

    public void setPhotos(Set<Media> photos) {
        this.media = photos;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime created) {
        this.createdDate = created;
    }

    public ZonedDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(ZonedDateTime modified) {
        this.modifiedDate = modified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Album album = (Album) o;
        if(album.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, album.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Album{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", url='" + url + "'" +
            ", description='" + description + "'" +
            ", media='" + media + "'" +
            ", isDefault='" + isDefault + "'" +
            ", created='" + createdDate + "'" +
            ", modified='" + modifiedDate + "'" +
            '}';
    }
}
