package org.lamsfoundation.lams.learningdesign.dao.hibernate;

import java.util.Set;

import org.hibernate.Query;
import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.learningdesign.Competence;
import org.lamsfoundation.lams.learningdesign.CompetenceMapping;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.dao.ICompetenceMappingDAO;

public class CompetenceMappingDAO extends BaseDAO implements ICompetenceMappingDAO {

    private static final String LOAD_COMPETENCE_MAPPING_BY_ACTIVITY_AND_COMPETENCE = "from lams_competence_mapping in class "
	    + CompetenceMapping.class.getName() + " where competence_id=? AND activity_id=?";

    @Override
    public void saveOrUpdate(CompetenceMapping competenceMapping) {
	this.getHibernateTemplate().saveOrUpdate(competenceMapping);
    }

    @Override
    public CompetenceMapping getCompetenceMapping(ToolActivity toolActivity, Competence competence) {
	if (toolActivity != null && competence != null) {
	    Long activityId = toolActivity.getActivityId();
	    Long competenceId = competence.getCompetenceId();
	    Query query = this.getSession().createQuery(LOAD_COMPETENCE_MAPPING_BY_ACTIVITY_AND_COMPETENCE);
	    query.setLong(0, competenceId);
	    query.setLong(1, activityId.longValue());
	    return (CompetenceMapping) query.uniqueResult();
	}
	return null;
    }

    @Override
    public void delete(CompetenceMapping competenceMapping) {
	this.getHibernateTemplate().delete(competenceMapping);
    }

    @Override
    public void deleteAll(Set<CompetenceMapping> competenceMappings) {
	this.getHibernateTemplate().deleteAll(competenceMappings);
    }
}
