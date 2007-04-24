/******************************************************************************
 * LamstwoItem.java - created by Sakai App Builder -AZ
 * 
 * Copyright (c) 2007 LAMS Foundation
 * Licensed under the Educational Community License version 1.0
 * 
 * A copy of the Educational Community License has been included in this 
 * distribution and is available at: http://www.opensource.org/licenses/ecl1.php
 * 
 *****************************************************************************/

package org.lamsfoundation.lams.integrations.sakai.model;

import java.util.Date;

/**
 * This is a sample POJO (data storage object) 
 * @author Sakai App Builder -AZ
 */
public class LamstwoItem {
	
	private Long id;
	private String title;
	private String introduction;
	private Long sequenceId;
	private Long lessonId; 
	private String ownerId; // Sakai userId
	private String siteId; // Sakai siteId
	private Boolean hidden; // only visible to owner if true
	private Boolean schedule;
	private Date startDate;
	private Date dateCreated;

	/**
	 * Default constructor
	 */
	public LamstwoItem() {
	}

	/**
	 * Minimal constructor
	 */
	public LamstwoItem(String title, String introduction,
			String ownerId, String siteId) {
		this.title = title;
		this.introduction = introduction;
		this.ownerId = ownerId;
		this.siteId = siteId;
	}

	/**
	 * Full constructor
	 */
	public LamstwoItem(String title, String introduction,
			Long sequenceId, Long lessonId, String ownerId, String siteId,
			Boolean hidden, Boolean schedule, Date startDate,
			Date dateCreated) {
		this.title = title;
		this.introduction = introduction;
		this.sequenceId = sequenceId;
		this.lessonId = lessonId;
		this.ownerId = ownerId;
		this.siteId = siteId;
		this.hidden = hidden;
		this.schedule = schedule;
		this.startDate = startDate;
		this.dateCreated = dateCreated;
	}

	/**
	 * Getters and Setters
	 */
	public Boolean getHidden() {
		return hidden;
	}
	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getIntroduction() {
		return this.introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public Long getSequenceId() {
		return sequenceId;
	}
	public void setSequenceId(Long sequenceId) {
		this.sequenceId = sequenceId;
	}
	public Long getLessonId() {
		return lessonId;
	}
	public void setLessonId(Long lessonId) {
		this.lessonId = lessonId;
	}
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	public String getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	public String getSiteId() {
		return siteId;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	public Boolean getSchedule() {
		return schedule;
	}
	public void setSchedule(Boolean schedule) {
		this.schedule = schedule;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
}
