package org.lamsfoundation.lams.learningdesign;

/**
 *
 * @hibernate.class table="lams_competence"
 */
public class Competence {

    private Long competenceId;
    private LearningDesign learningDesign;
    private String title;
    private String description;

    public Competence() {
    }

    /**
     * @hibernate.id generator-class="native" type="java.lang.Long"
     *               column="competence_id"
     */
    public Long getCompetenceId() {
	return this.competenceId;
    }

    public void setCompetenceId(Long competenceId) {
	this.competenceId = competenceId;
    }

    /**
     * @hibernate.property column="title" length="255" not-null="true"
     */
    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    /**
     * @hibernate.property column="description" length="65535" not-null="false"
     */
    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    /**
     * @hibernate.many-to-one not-null="true"
     * @hibernate.column name="learning_design_id"
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
