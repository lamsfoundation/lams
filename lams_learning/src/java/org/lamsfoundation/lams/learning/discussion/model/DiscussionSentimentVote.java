package org.lamsfoundation.lams.learning.discussion.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "lams_discussion_sentiment")
public class DiscussionSentimentVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name = "lesson_id")
    private Long lessonId;

    @Column(name = "tool_question_uid")
    private Long toolQuestionUid;

    @Column(name = "burning_question_uid")
    private Long burningQuestionUid;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "selected_option")
    private Integer selectedOption;

    public DiscussionSentimentVote() {
    }

    public DiscussionSentimentVote(Long lessonId, Long toolQuestionUid, Long burningQuestionUid) {
	this.lessonId = lessonId;
	this.toolQuestionUid = toolQuestionUid;
	this.burningQuestionUid = burningQuestionUid;
    }

    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public Long getLessonId() {
	return lessonId;
    }

    public void setLessonId(Long lessonId) {
	this.lessonId = lessonId;
    }

    public Long getToolQuestionUid() {
	return toolQuestionUid;
    }

    public void setToolQuestionUid(Long toolQuestionUid) {
	this.toolQuestionUid = toolQuestionUid;
    }

    public Long getBurningQuestionUid() {
	return burningQuestionUid;
    }

    public void setBurningQuestionUid(Long burningQuestionUid) {
	this.burningQuestionUid = burningQuestionUid;
    }

    public Integer getUserId() {
	return userId;
    }

    public void setUserId(Integer userId) {
	this.userId = userId;
    }

    public Integer getSelectedOption() {
	return selectedOption;
    }

    public void setSelectedOption(Integer selectedOption) {
	this.selectedOption = selectedOption;
    }
}