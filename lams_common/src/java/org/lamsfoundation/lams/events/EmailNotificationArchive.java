package org.lamsfoundation.lams.events;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * An archived lesson or organisation notification sent by email.
 *
 * @author Marcin Cieslak
 *
 */
public class EmailNotificationArchive implements Serializable {
    private static final long serialVersionUID = 3394158938976463492L;

    private Long uid;
    private Integer organisationId;
    private Long lessonId;
    private Integer searchType;
    private Date sentOn;
    private String body;
    private Set<Integer> recipients;

    public EmailNotificationArchive() {
    }

    public EmailNotificationArchive(Integer organisationId, Long lessonId, Integer searchType, Date sentOn, String body,
	    Set<Integer> recipients) {
	this.organisationId = organisationId;
	this.lessonId = lessonId;
	this.searchType = searchType;
	this.sentOn = sentOn == null ? new Date() : sentOn;
	this.body = body;
	this.recipients = recipients;
    }

    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public Integer getOrganisationId() {
	return organisationId;
    }

    public void setOrganisationId(Integer organisationId) {
	this.organisationId = organisationId;
    }

    public Long getLessonId() {
	return lessonId;
    }

    public void setLessonId(Long lessonId) {
	this.lessonId = lessonId;
    }

    public Integer getSearchType() {
	return searchType;
    }

    public void setSearchType(Integer searchType) {
	this.searchType = searchType;
    }

    public Date getSentOn() {
	return sentOn;
    }

    public void setSentOn(Date sentOn) {
	this.sentOn = sentOn;
    }

    public String getBody() {
	return body;
    }

    public void setBody(String body) {
	this.body = body;
    }

    public Set<Integer> getRecipients() {
	return recipients;
    }

    public void setRecipients(Set<Integer> recipients) {
	this.recipients = recipients;
    }
}