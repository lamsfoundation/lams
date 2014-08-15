/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0 
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.learningdesign.dao.hibernate;

import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.lamsfoundation.lams.learningdesign.dao.ICompetenceDAO;
import org.lamsfoundation.lams.learningdesign.Competence;
import org.lamsfoundation.lams.learningdesign.CompetenceMapping;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.springframework.orm.hibernate4.HibernateCallback;

public class CompetenceDAO extends BaseDAO implements ICompetenceDAO
{
	private static final String LOAD_COMPETENCE_BY_LDID_AND_TITLE= "from lams_competence in class " + Competence.class.getName() 
		+ " where title=? AND learning_design_id=?";
	
	
    /**
     * @see org.lamsfoundation.lams.competence.dao.ICompetenceDAO#saveOrUpdate()
     */
 
    public void saveOrUpdate(Competence competence)
    {
    	this.getHibernateTemplate().saveOrUpdate(competence);
    }
    
    
    
     /**
     * @see org.lamsfoundation.lams.competence.dao.ICompetencelDAO#getCompetence()
     */
    public Competence getCompetence(LearningDesign design, String competenceTitle)
    {
   		if ( design != null && competenceTitle != null ) {
			Long designID = design.getLearningDesignId();
			Query query = getSessionFactory().getCurrentSession().createQuery(LOAD_COMPETENCE_BY_LDID_AND_TITLE);
			query.setString(0,competenceTitle);
			query.setLong(1,designID.longValue());
			return (Competence) query.uniqueResult();
		}
		return null;
	}
    
    public void delete(Competence competence)
    {
    	this.getHibernateTemplate().delete(competence);
    }
    
    public void deleteAll(Set<Competence> competences)
	{
		this.getHibernateTemplate().deleteAll(competences);
	}
}
