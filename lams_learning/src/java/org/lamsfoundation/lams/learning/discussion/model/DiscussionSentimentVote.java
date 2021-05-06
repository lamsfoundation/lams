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

    @Column(name = "tool_content_id")
    private Long toolContentId;

    @Column(name = "burning_question_uid")
    private Long burningQuestionUid;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "selected_option")
    private Integer selectedOption;

    public DiscussionSentimentVote() {
    }

    public DiscussionSentimentVote(Long lessonId, Long toolContentId) {
	this.lessonId = lessonId;
	this.toolContentId = toolContentId;
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

    public Long getToolContentId() {
	return toolContentId;
    }

    public void setToolContentId(Long toolContentId) {
	this.toolContentId = toolContentId;
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