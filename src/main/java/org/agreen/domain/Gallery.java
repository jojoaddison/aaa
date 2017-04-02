package org.agreen.domain;

import java.util.HashSet;
import java.util.Set;

public class Gallery {
	private Set<Media> mediaGroup = new HashSet<>();
	private String title;
	
	public Gallery(Set<Media> mediaGroup){
		setMediaGroup(mediaGroup);
	}
	
	public Set<Media> getMediaGroup(){
		return mediaGroup;
	}
	
	public void setMediaGroup(Set<Media> mediaGroup){
		this.mediaGroup = mediaGroup;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
