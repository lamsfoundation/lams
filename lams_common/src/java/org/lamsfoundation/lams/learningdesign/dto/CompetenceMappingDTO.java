package org.lamsfoundation.lams.learningdesign.dto;

import org.lamsfoundation.lams.learningdesign.CompetenceMapping;

public class CompetenceMappingDTO {
    private Long competenceMappingId;
    private String competenceTitle;
    private Integer toolActivityUIID;

    public CompetenceMappingDTO() {
    }

    public CompetenceMappingDTO(CompetenceMapping competenceMapping) {
	this.competenceMappingId = competenceMapping.getCompetenceMappingId();
	this.competenceTitle = competenceMapping.getCompetence().getTitle();
	this.toolActivityUIID = competenceMapping.getToolActivity().getActivityUIID();

    }

    public Long getCompetenceMappingId() {
	return competenceMappingId;
    }

    public void setCompetenceMappingId(Long competenceMappingId) {
	this.competenceMappingId = competenceMappingId;
    }

    public String getCompetenceTitle() {
	return competenceTitle;
    }

    public void setCompetenceTitle(String competenceTitle) {
	this.competenceTitle = competenceTitle;
    }

    public Integer getToolActivityUIID() {
	return toolActivityUIID;
    }

    public void setToolActivityUIID(Integer toolActivityUIID) {
	this.toolActivityUIID = toolActivityUIID;
    }
}
