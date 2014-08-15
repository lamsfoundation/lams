/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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
package org.lamsfoundation.lams.tool.survey.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.tool.survey.dao.SurveyAnswerDAO;
import org.lamsfoundation.lams.tool.survey.model.SurveyAnswer;

public class SurveyAnswerDAOHibernate extends BaseDAOHibernate implements SurveyAnswerDAO {
	private static final String GET_LEARNER_ANSWER = "from "+SurveyAnswer.class.getName()+" as a where a.surveyQuestion.uid=? and a.user.uid=?";
	private static final String GET_SESSION_ANSWER = "from "+SurveyAnswer.class.getName()+" as a " +
							" where a.user.session.sessionId=? and a.surveyQuestion.uid=?";
	private static final String GET_BY_TOOL_CONTENT_ID_AND_USER_ID = "from "+SurveyAnswer.class.getName()+" as a " +
		" where a.user.session.survey.contentId = ? and a.user.userId = ?";
	
	public SurveyAnswer getAnswer(Long questionUid, Long userUid) {
		List list = getHibernateTemplate().find(GET_LEARNER_ANSWER,new Object[]{questionUid,userUid});
		if(list.size() > 0)
			return (SurveyAnswer) list.get(0);
		else
			return null;
	}

	@SuppressWarnings("unchecked")
	public List<SurveyAnswer> getSessionAnswer(Long sessionId, Long questionUid) {
		return (List<SurveyAnswer>) getHibernateTemplate().find(GET_SESSION_ANSWER,new Object[]{sessionId,questionUid});
	}
	
    @SuppressWarnings("unchecked")
    public List<SurveyAnswer> getByToolContentIdAndUserId(Long toolContentId, Long userId) {
	return (List<SurveyAnswer>) getHibernateTemplate().find(GET_BY_TOOL_CONTENT_ID_AND_USER_ID, new Object[] { toolContentId, userId });
    }
}
