package org.agreen.domain;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "articles")
public class Article {
	@Id
	private String id;
	
	@Field("pid")
	private String pid;

	@Field("type")
	private String type;
	
	@Field("pages")
	private Set<Page> pages = new HashSet<>();
	
	@Field("media")
	private Set<Media> media = new HashSet<>();
	
    @Field("created_date")
    private ZonedDateTime createdDate = ZonedDateTime.now();

    @Field("modified_date")
    private ZonedDateTime modifiedDate = ZonedDateTime.now();
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public Set<Page> getPages() {
		return pages;
	}
	public void setPages(Set<Page> pages) {
		this.pages = pages;
	}
	public Set<Media> getMedia() {
		return media;
	}
	public void setMedia(Set<Media> media) {
		this.media = media;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public ZonedDateTime getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(ZonedDateTime createdDate) {
		this.createdDate = createdDate;
	}
	public ZonedDateTime getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(ZonedDateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Article article = (Article) o;
        if(article.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
