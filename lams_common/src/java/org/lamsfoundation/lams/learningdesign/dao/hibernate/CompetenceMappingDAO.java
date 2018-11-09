package org.lamsfoundation.lams.learningdesign.dao.hibernate;

import java.util.Set;

import org.hibernate.Query;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.learningdesign.Competence;
import org.lamsfoundation.lams.learningdesign.CompetenceMapping;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.dao.ICompetenceMappingDAO;
import org.springframework.stereotype.Repository;

@Repository
public class CompetenceMappingDAO extends LAMSBaseDAO implements ICompetenceMappingDAO {

    private static final String LOAD_COMPETENCE_MAPPING_BY_ACTIVITY_AND_COMPETENCE = "from lams_competence_mapping in class "
	    + CompetenceMapping.class.getName() + " where competence_id=:competence_id AND activity_id=:activity_id";

    @Override
    public void saveOrUpdate(CompetenceMapping competenceMapping) {
	this.getSession().saveOrUpdate(competenceMapping);
    }

    @Override
    public CompetenceMapping getCompetenceMapping(ToolActivity toolActivity, Competence competence) {
	if (toolActivity != null && competence != null) {
	    Long activityId = toolActivity.getActivityId();
	    Long competenceId = competence.getCompetenceId();
	    Query query = getSession().createQuery(LOAD_COMPETENCE_MAPPING_BY_ACTIVITY_AND_COMPETENCE);
	    query.setLong("competence_id", competenceId);
	    query.setLong("activity_id", activityId.longValue());
	    return (CompetenceMapping) query.uniqueResult();
	}
	return null;
    }

    @Override
    public void delete(CompetenceMapping competenceMapping) {
	this.getSession().delete(competenceMapping);
    }

    @Override
    public void deleteAll(Set<CompetenceMapping> competenceMappings) {
	this.doDeleteAll(competenceMappings);
    }

    @Override
    public void insert(CompetenceMapping competenceMapping) {
	super.insert(competenceMapping);
    }

}
