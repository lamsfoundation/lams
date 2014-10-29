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
/* $$Id$$ */
package org.lamsfoundation.lams.tool.survey.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.tool.survey.dao.SurveyUserDAO;
import org.lamsfoundation.lams.tool.survey.model.SurveyUser;

public class SurveyUserDAOHibernate extends BaseDAOHibernate implements SurveyUserDAO {

    private static final String FIND_BY_USER_ID_CONTENT_ID = "FROM " + SurveyUser.class.getName()
	    + " AS u WHERE u.userId =? AND u.survey.contentId=?";
    private static final String FIND_BY_USER_ID_SESSION_ID = "FROM " + SurveyUser.class.getName()
	    + " AS u WHERE u.userId =? AND u.session.sessionId=?";
    private static final String FIND_BY_SESSION_ID = "FROM " + SurveyUser.class.getName()
	    + " AS u WHERE u.session.sessionId=?";
    private static final String GET_COUNT_FINISHED_USERS_FOR_SESSION = "SELECT COUNT(*) FROM "
	    + SurveyUser.class.getName()
	    + " AS u WHERE u.session.sessionId=? AND (u.sessionFinished is true OR u.responseFinalized is true)";

    public SurveyUser getUserByUserIDAndSessionID(Long userID, Long sessionId) {
	List list = this.getHibernateTemplate().find(FIND_BY_USER_ID_SESSION_ID, new Object[] { userID, sessionId });
	if (list == null || list.size() == 0)
	    return null;
	return (SurveyUser) list.get(0);
    }

    public SurveyUser getUserByUserIDAndContentID(Long userId, Long contentId) {
	List list = this.getHibernateTemplate().find(FIND_BY_USER_ID_CONTENT_ID, new Object[] { userId, contentId });
	if (list == null || list.size() == 0)
	    return null;
	return (SurveyUser) list.get(0);
    }

    @SuppressWarnings("unchecked")
    public List<SurveyUser> getBySessionID(Long sessionId) {
	return (List<SurveyUser>) this.getHibernateTemplate().find(FIND_BY_SESSION_ID, sessionId);
    }

    public int getCountFinishedUsers(Long sessionId) {
	List list = getHibernateTemplate().find(GET_COUNT_FINISHED_USERS_FOR_SESSION, new Object[] { sessionId });
	if (list == null || list.size() == 0) {
	    return 0;
	}
	return ((Number) list.get(0)).intValue();
    }

}
