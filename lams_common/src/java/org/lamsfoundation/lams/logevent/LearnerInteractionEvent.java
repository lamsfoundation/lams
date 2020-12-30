package org.lamsfoundation.lams.logevent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "lams_learner_interaction_event")
public class LearnerInteractionEvent {

    public static final int RADIO_BUTTON_SELECTED = 1;
    public static final int CHECKBOX_CHECKED = 2;
    public static final int CHECKBOX_UNCHECKED = 3;

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name = "event_type")
    private int eventType;

    @Column(name = "occurred_date_time")
    private LocalDateTime occuredDateTime;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "qb_tool_question_uid")
    private Long qbToolQuestionUid;

    // For MCQ questions this is a reference to QbOption UID
    // For true/false questions 1 = true, 2 = false
    @Column(name = "option_uid")
    private Long optionUid;

    public LearnerInteractionEvent() {
    }

    public LearnerInteractionEvent(int eventType, int userId) {
	this.eventType = eventType;
	this.userId = userId;
	this.occuredDateTime = LocalDateTime.now();
    }

    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public int getEventType() {
	return eventType;
    }

    public void setEventType(int type) {
	this.eventType = type;
    }

    public LocalDateTime getOccuredDateTime() {
	return occuredDateTime;
    }

    public void setOccuredDateTime(LocalDateTime occuredDateTime) {
	this.occuredDateTime = occuredDateTime;
    }

    public String getFormattedDate() {
	return this.occuredDateTime.format(DATE_FORMATTER);
    }

    public int getUserId() {
	return userId;
    }

    public void setUserId(int userId) {
	this.userId = userId;
    }

    public Long getQbToolQuestionUid() {
	return qbToolQuestionUid;
    }

    public void setQbToolQuestionUid(Long toolQuestionUid) {
	this.qbToolQuestionUid = toolQuestionUid;
    }

    public Long getOptionUid() {
	return optionUid;
    }

    public void setOptionUid(Long qbOptionUid) {
	this.optionUid = qbOptionUid;
    }
}