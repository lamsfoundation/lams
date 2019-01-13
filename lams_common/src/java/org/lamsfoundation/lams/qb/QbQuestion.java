package org.lamsfoundation.lams.qb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

/**
 * A question in Question Bank.
 *
 * @author Marcin Cieslak
 */
@Entity
@Table(name = "lams_qb_question")
public class QbQuestion implements Serializable, Cloneable {
    private static final long serialVersionUID = -6287273838239262151L;

    // questions can be of different type
    // not all tools can produce/consume all question types
    public static final int TYPE_MULTIPLE_CHOICE_SINGLE_ANSWER = 1;

    // primary key
    // another candidate is questionId + version, but single uid can fe searched faster
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    // is the question searchable in Question Bank
    // or rathe it is a tool's private data
    @Column
    private Boolean local = true;

    // one of question types
    @Column
    private Integer type;

    // "tracking ID" for a question
    // multiple versions can share the same question ID so their stats are aggregated
    @Column(name = "question_id")
    private Integer questionId;

    // the same question can have multiple versions
    @Column
    private Integer version = 1;

    // text of the question
    @Column
    private String name;

    @Column
    private Integer mark;

    @Column
    private String feedback;

    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<QbOption> options = new ArrayList<>();

    // question state when it was loaded from DB
    @Transient
    private QbQuestion initialContent;

    // runs when the question gets loaded from DB
    @PostLoad
    private void setInitialContent() throws CloneNotSupportedException {
	initialContent = this.clone();
    }

    // compares if current question state and the state when it was loaded from DB
    // it detects if question is the same or should another question/version be created
    public boolean isModified() {
	return !equals(initialContent);
    }

    // checks if important parts of another question are the same as current question's
    @Override
    public boolean equals(Object o) {
	QbQuestion other = (QbQuestion) o;
	return other != null && StringUtils.equals(other.name, name) && StringUtils.equals(other.feedback, feedback)
		&& (mark == null ? other.mark == null : mark.equals(other.mark));
    }

    @Override
    public QbQuestion clone() {
	QbQuestion clone = null;
	try {
	    clone = (QbQuestion) super.clone();
	    clone.uid = null;
	} catch (Exception e) {
	    // this will never happen
	    e.printStackTrace();
	}
	return clone;

    }

    public Long getUid() {
	return uid;
    }

    public Boolean getLocal() {
	return local;
    }

    public void setLocal(Boolean local) {
	this.local = local;
    }

    public Integer getType() {
	return type;
    }

    public void setType(Integer type) {
	this.type = type;
    }

    public Integer getQuestionId() {
	return questionId;
    }

    public void setQuestionId(Integer questionId) {
	this.questionId = questionId;
    }

    public Integer getVersion() {
	return version;
    }

    public void setVersion(Integer version) {
	this.version = version;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = StringUtils.isBlank(name) ? null : name.trim();
    }

    public Integer getMark() {
	return mark;
    }

    public void setMark(Integer mark) {
	this.mark = mark;
    }

    public String getFeedback() {
	return feedback;
    }

    public void setFeedback(String feedback) {
	this.feedback = StringUtils.isBlank(feedback) ? null : feedback.trim();
    }

    public List<QbOption> getOptions() {
	return options;
    }

    public void setOptions(List<QbOption> options) {
	this.options = options;
    }
}