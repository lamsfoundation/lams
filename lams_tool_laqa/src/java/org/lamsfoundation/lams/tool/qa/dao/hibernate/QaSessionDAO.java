/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */
package org.lamsfoundation.lams.tool.qa.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaSession;
import org.lamsfoundation.lams.tool.qa.dao.IQaSessionDAO;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;



/**
 * @author Ozgur Demirtas
 *
 */
public class QaSessionDAO extends HibernateDaoSupport implements
                                                         IQaSessionDAO
{
	private static final String COUNT_SESSION_INCOMPLITE 	= "select qaSession.session_status from QaSession qaSession where qaSession.session_status='INCOMPLETE' and qaSession.qaContentId = :qa";
	private static final String COUNT_SESSION_ACTIVITY   	= "select qaSession.session_status from QaSession qaSession where qaSession.qaContentId = :qa";
	private static final String GET_SESSION_IDS_FOR_CONTENT = "select qaSession.qaSessionId    from QaSession qaSession where qaSession.qaContentId = :qa";
	
	
	public int countIncompleteSession(QaContent qa)
    {
	   return (getHibernateTemplate().findByNamedParam(COUNT_SESSION_INCOMPLITE,
            "qa",
            qa)).size();
    }
	
    public int studentActivityOccurred(QaContent qa)
    {
    	  return (getHibernateTemplate().findByNamedParam(COUNT_SESSION_ACTIVITY,
                "qa",
                qa)).size();
    }
	
    
    public List getToolSessionsForContent(QaContent qa)
    {
    	   
		  List lisToolSessionIds=(getHibernateTemplate().findByNamedParam(GET_SESSION_IDS_FOR_CONTENT,
                "qa",
                qa));
		  return lisToolSessionIds;
    }
	
    /**
     * @see org.lamsfoundation.lams.tool.survey.dao.interfaces.ISurveySessionDAO#getSurveySessionById(long)
     */
    public QaSession getQaSessionById(long qaSessionId)
    {
        return (QaSession)this.getHibernateTemplate().load(QaSession.class,new Long(qaSessionId));
    }
    
    public QaSession getQaSessionOrNullById(long qaSessionId)
    {
        return (QaSession)this.getHibernateTemplate().get(QaSession.class,new Long(qaSessionId));
    }

    /**
     * @see org.lamsfoundation.lams.tool.survey.dao.interfaces.ISurveySessionDAO#CreateSurveySession(com.lamsinternational.tool.survey.domain.SurveySession)
     */
    public void CreateQaSession(QaSession session)
    {
        this.getHibernateTemplate().save(session);
    }

    /**
     * @see org.lamsfoundation.lams.tool.survey.dao.interfaces.ISurveySessionDAO#UpdateSurveySession(com.lamsinternational.tool.survey.domain.SurveySession)
     */
    public void UpdateQaSession(QaSession session)
    {
        this.getHibernateTemplate().update(session);
    }

    /**
     * @see org.lamsfoundation.lams.tool.survey.dao.interfaces.ISurveySessionDAO#deleteSurveySession(com.lamsinternational.tool.survey.domain.SurveySession)
     */
    public void deleteQaSession(QaSession qaSession)
    {
        this.getHibernateTemplate().delete(qaSession);
    }

}
