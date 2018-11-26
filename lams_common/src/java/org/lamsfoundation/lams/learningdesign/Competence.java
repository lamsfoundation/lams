package org.lamsfoundation.lams.learningdesign;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "lams_competence")
public class Competence {

    @Id
    @Column(name = "competence_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long competenceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "learning_design_id")
    private LearningDesign learningDesign;

    @Column
    private String title;

    @Column
    private String description;

    public Competence() {
    }

    public Long getCompetenceId() {
	return this.competenceId;
    }

    public void setCompetenceId(Long competenceId) {
	this.competenceId = competenceId;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public org.lamsfoundation.lams.learningdesign.LearningDesign getLearningDesign() {
	return this.learningDesign;
    }

    public void setLearningDesign(org.lamsfoundation.lams.learningdesign.LearningDesign learningDesign) {
	this.learningDesign = learningDesign;
    }

    public Competence createCopy(Competence originalCompetence) {
	Competence newCompetence = new Competence();
	newCompetence.setTitle(originalCompetence.getTitle());
	newCompetence.setDescription(originalCompetence.getDescription());
	return newCompetence;
    }
}