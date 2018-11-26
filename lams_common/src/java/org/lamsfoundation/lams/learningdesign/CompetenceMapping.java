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
@Table(name = "lams_competence_mapping")
public class CompetenceMapping {

    @Id
    @Column(name = "competence_mapping_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long competenceMappingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "competence_id")
    private Competence competence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id")
    private ToolActivity toolActivity;

    public Long getCompetenceMappingId() {
	return competenceMappingId;
    }

    public void setCompetenceMappingId(Long competenceMappingId) {
	this.competenceMappingId = competenceMappingId;
    }

    public Competence getCompetence() {
	return competence;
    }

    public void setCompetence(Competence competence) {
	this.competence = competence;
    }

    public ToolActivity getToolActivity() {
	return this.toolActivity;
    }

    public void setToolActivity(ToolActivity toolActivity) {
	this.toolActivity = toolActivity;
    }
}