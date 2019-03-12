package org.lamsfoundation.lams.qb.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

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
    public static final int TYPE_MATCHING_PAIRS = 2;
    public static final int TYPE_SHORT_ANSWER = 3;
    public static final int TYPE_NUMERICAL = 4;
    public static final int TYPE_TRUE_FALSE = 5;
    public static final int TYPE_ESSAY = 6;
    public static final int TYPE_ORDERING = 7;
    public static final int TYPE_MARK_HEDGING = 8;

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

    @Column(name = "create_date")
    private Date createDate = new Date();

    // text of the question
    @Column
    private String name;

    @Column
    private String description;

    @Column
    private Integer mark;

    @Column
    private String feedback;

    @OneToMany(mappedBy = "qbQuestion", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QbOption> qbOptions = new ArrayList<>();

    // compares if current question data and the other one (probably modified with new data) are the same
    // it detects if question is the same or should another question/version be created
    public boolean isModified(QbQuestion modifiedQuestion) {
	return !equals(modifiedQuestion);
    }

    // checks if important parts of another question are the same as current question's
    @Override
    public boolean equals(Object o) {
	QbQuestion other = (QbQuestion) o;
	// options are also checked if they are equal
	return new EqualsBuilder().append(name, other.name).append(description, other.description)
		.append(feedback, other.feedback).append(mark, other.mark)
		.append(qbOptions.toArray(), other.getQbOptions().toArray()).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(name).append(description).append(feedback).append(mark).toHashCode();
    }

    @Override
    public QbQuestion clone() {
	QbQuestion clone = null;
	try {
	    clone = (QbQuestion) super.clone();
	} catch (CloneNotSupportedException e) {
	    // it should never happen
	    e.printStackTrace();
	}
	// make a deep copy of options
	List<QbOption> optionsClone = new ArrayList<>(qbOptions.size());
	clone.setQbOptions(optionsClone);
	for (QbOption option : qbOptions) {
	    QbOption optionClone = option.clone();
	    optionClone.setQbQuestion(clone);
	    optionsClone.add(optionClone);
	}
	return clone;
    }

    public void clearID() {
	this.uid = null;
	for (QbOption option : qbOptions) {
	    option.uid = null;
	}
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

    public Date getCreateDate() {
	return createDate;
    }

    public void setCreateDate(Date createDate) {
	this.createDate = createDate;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = StringUtils.isBlank(name) ? null : name.trim();
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
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

    public List<QbOption> getQbOptions() {
	return qbOptions;
    }

    public void setQbOptions(List<QbOption> options) {
	this.qbOptions = options;
    }
}