/**
 * Homework 04
 * A Yang
 * Ajay Vijayakumaran Nair
 * Nachiket Doke
 * 
 */
package com.example.homework04;

import java.io.Serializable;
import java.util.Date;

public class Story implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String title;
	private String storyUrl;
	private Date pubDate;
	private String imageUrl;
	private String teaser;
	private String reporterName;
	private Date airedDate;
	private String mp3url;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getPubDate() {
		return pubDate;
	}
	public void setPubDate(Date pubDate) {
		this.pubDate = pubDate;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getTeaser() {
		return teaser;
	}
	public void setTeaser(String teaser) {
		this.teaser = teaser;
	}
	public String getReporterName() {
		return reporterName;
	}
	public void setReporterName(String reporterName) {
		this.reporterName = reporterName;
	}
	public Date getAiredDate() {
		return airedDate;
	}
	public void setAiredDate(Date airedDate) {
		this.airedDate = airedDate;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getMp3url() {
		return mp3url;
	}
	public void setMp3url(String mp3url) {
		this.mp3url = mp3url;
	}
	public String getStoryUrl() {
		return storyUrl;
	}
	public void setStoryUrl(String storyUrl) {
		this.storyUrl = storyUrl;
	}
	
}
