package org.lamsfoundation.lams.events;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;

/**
 * An archived lesson or organisation notification sent by email.
 *
 * @author Marcin Cieslak
 *
 */
@Entity
@Table(name = "lams_email_notification_archive")
public class EmailNotificationArchive implements Serializable {
    private static final long serialVersionUID = 3394158938976463492L;

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name = "organisation_id")
    private Integer organisationId;

    @Column(name = "lesson_id")
    private Long lessonId;

    @Column(name = "search_type")
    private Integer searchType;

    @Column(name = "sent_on")
    private Date sentOn;
    @Column
    private String body;

    @ElementCollection
    @JoinTable(name = "lams_email_notification_recipient_archive", joinColumns = @JoinColumn(name = "email_notification_uid"))
    @Column(name = "user_id")
    private Set<Integer> recipients = new TreeSet<Integer>();

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