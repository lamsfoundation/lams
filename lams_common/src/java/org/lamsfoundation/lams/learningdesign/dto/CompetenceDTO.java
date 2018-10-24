package org.lamsfoundation.lams.learningdesign.dto;

import org.lamsfoundation.lams.learningdesign.Competence;

public class CompetenceDTO {
    private Long competenceId;
    private Long learningDesignID;
    private String title;
    private String description;

    public CompetenceDTO() {
    }

    public CompetenceDTO(Competence competence) {
	this.competenceId = competence.getCompetenceId();
	this.learningDesignID = competence.getLearningDesign().getLearningDesignId();
	this.description = competence.getDescription();
	this.title = competence.getTitle();
    }

    public Long getCompetenceId() {
	return competenceId;
    }

    public void setCompetenceId(Long competenceId) {
	this.competenceId = competenceId;
    }

    public Long getLearningDesignID() {
	return learningDesignID;
    }

    public void setLearningDesignID(Long learningDesignID) {
	this.learningDesignID = learningDesignID;
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
}
