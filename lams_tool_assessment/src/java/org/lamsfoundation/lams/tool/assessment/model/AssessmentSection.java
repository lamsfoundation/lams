package org.lamsfoundation.lams.tool.assessment.model;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tl_laasse10_section")
public class AssessmentSection implements Serializable, Comparable<AssessmentSection> {

    private static final long serialVersionUID = -8252609064125180392L;

    public static final Comparator<AssessmentSection> COMPARATOR = Comparator
	    .comparing(AssessmentSection::getDisplayOrder);

    // it makes sense to put comparator here as an internal class, so we do not need to look for it in other classes
    public static class AssessmentSectionComparator implements Comparator<AssessmentSection> {
	@Override
	public int compare(AssessmentSection o1, AssessmentSection o2) {
	    return COMPARATOR.compare(o1, o2);
	}
    }

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name = "display_order")
    int displayOrder;

    @Column
    String name;

    @Column(name = "question_count")
    int questionCount;

    public AssessmentSection() {
	this.displayOrder = 1;
    }

    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public int getDisplayOrder() {
	return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
	this.displayOrder = displayOrder;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public int getQuestionCount() {
	return questionCount;
    }

    public void setQuestionCount(int questionCount) {
	this.questionCount = questionCount;
    }

    @Override
    public int compareTo(AssessmentSection o) {
	return COMPARATOR.compare(this, o);
    }

    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append("AssessmentSection [uid=").append(uid).append(", displayOrder=").append(displayOrder)
		.append(", name=").append(name).append(", questionCount=").append(questionCount).append("]");
	return builder.toString();
    }

    @Override
    public int hashCode() {
	return Objects.hash(uid);
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (!(obj instanceof AssessmentSection)) {
	    return false;
	}
	AssessmentSection other = (AssessmentSection) obj;
	return Objects.equals(uid, other.uid);
    }
}