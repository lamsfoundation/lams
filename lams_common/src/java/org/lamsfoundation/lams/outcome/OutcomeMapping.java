package org.lamsfoundation.lams.outcome;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Entity
@Table(name = "lams_outcome_mapping")
public class OutcomeMapping implements Serializable {
    private static final long serialVersionUID = -2195345501533401085L;

    @Id
    @Column(name = "mapping_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mappingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "outcome_id")
    private Outcome outcome;

    @Column(name = "lesson_id")
    private Long lessonId;

    @Column(name = "tool_content_id")
    private Long toolContentId;

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "qb_question_id")
    private Integer qbQuestionId;

    public Long getMappingId() {
	return mappingId;
    }

    public void setMappingId(Long mappingId) {
	this.mappingId = mappingId;
    }

    public Outcome getOutcome() {
	return outcome;
    }

    public void setOutcome(Outcome outcome) {
	this.outcome = outcome;
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

    public Long getItemId() {
	return itemId;
    }

    public void setItemId(Long itemId) {
	this.itemId = itemId;
    }

    public Integer getQbQuestionId() {
	return qbQuestionId;
    }

    public void setQbQuestionId(Integer qbQuestionId) {
	this.qbQuestionId = qbQuestionId;
    }

    @Override
    public boolean equals(Object o) {
	OutcomeMapping other = (OutcomeMapping) o;
	return new EqualsBuilder().append(mappingId, other.mappingId).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(mappingId).toHashCode();
    }
}