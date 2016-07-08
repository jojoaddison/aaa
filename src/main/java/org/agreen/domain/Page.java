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
 * A Page.
 */

@Document(collection = "page")
public class Page implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("pid")
    private String pid;
    
    @Field("name")
    private String name;

    @Field("lang")
    private String lang;
    
    @Field("title")
    private String title;

    @Field("content")
    private String content;

    @Field("links")
    private Set<String> links = new HashSet<>();
    
    @Field("created")
    private ZonedDateTime createdDate = ZonedDateTime.now();

    @Field("modified")
    private ZonedDateTime modifiedDate = ZonedDateTime.now();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Set<String> getLink() {
        return links;
    }

    public void setLink(Set<String> link) {
        this.links = link;
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
        Page page = (Page) o;
        if(page.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, page.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Page{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", content='" + content + "'" +
            ", links=" + links + 
            '}';
    }
}
