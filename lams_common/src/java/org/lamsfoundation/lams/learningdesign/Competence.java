package org.lamsfoundation.lams.learningdesign;

/**
 *
 *
 */
public class Competence {

    private Long competenceId;
    private LearningDesign learningDesign;
    private String title;
    private String description;

    public Competence() {
    }

    /**
     *
     *
     */
    public Long getCompetenceId() {
	return this.competenceId;
    }

    public void setCompetenceId(Long competenceId) {
	this.competenceId = competenceId;
    }

    /**
     *
     */
    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    /**
     *
     */
    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    /**
     *
     *
     *
     */
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
