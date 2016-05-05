package org.lamsfoundation.lams.learningdesign;

/**
 *
 * @hibernate.class table="lams_competence_mapping"
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
     * @hibernate.id generator-class="native" type="java.lang.Long"
     *               column="competence_mapping_id"
     */
    public Long getCompetenceMappingId() {
	return competenceMappingId;
    }

    public void setCompetenceMappingId(Long competenceMappingId) {
	this.competenceMappingId = competenceMappingId;
    }

    /**
     *
     * @hibernate.many-to-one not-null="true"
     * @hibernate.column name="competence_id"
     */
    public Competence getCompetence() {
	return competence;
    }

    public void setCompetence(Competence competence) {
	this.competence = competence;
    }

    /**
     *
     * @hibernate.many-to-one not-null="true"
     * @hibernate.column name="activity_id"
     */
    public ToolActivity getToolActivity() {
	return this.toolActivity;
    }

    public void setToolActivity(ToolActivity toolActivity) {
	this.toolActivity = toolActivity;
    }
}
