package org.lamsfoundation.lams.learningdesign;

/**
 *
 *
 */
public class CompetenceMapping {

    private Long competenceMappingId;
    private Competence competence;
    private ToolActivity toolActivity;

    public CompetenceMapping(Long competenceMappingId, Competence competence, ToolActivity toolActivity) {
	super();
	this.competenceMappingId = competenceMappingId;
	this.competence = competence;
	this.toolActivity = toolActivity;
    }

    public CompetenceMapping() {
	super();
	// TODO Auto-generated constructor stub
    }

    /** 
    *
    *
    */
    public Long getCompetenceMappingId() {
	return competenceMappingId;
    }

    public void setCompetenceMappingId(Long competenceMappingId) {
	this.competenceMappingId = competenceMappingId;
    }

    /** 
    * 
    *
    *
    */
    public Competence getCompetence() {
	return competence;
    }

    public void setCompetence(Competence competence) {
	this.competence = competence;
    }

    /** 
    * 
    *
    *
    */
    public ToolActivity getToolActivity() {
	return this.toolActivity;
    }

    public void setToolActivity(ToolActivity toolActivity) {
	this.toolActivity = toolActivity;
    }
}
