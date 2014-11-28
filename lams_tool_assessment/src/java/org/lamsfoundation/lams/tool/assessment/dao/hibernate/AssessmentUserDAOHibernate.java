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
package org.lamsfoundation.lams.tool.assessment.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.assessment.dao.AssessmentUserDAO;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentUser;
import org.springframework.stereotype.Repository;

@Repository
public class AssessmentUserDAOHibernate extends LAMSBaseDAO implements AssessmentUserDAO {

    private static final String FIND_BY_USER_ID_CONTENT_ID = "from " + AssessmentUser.class.getName()
	    + " as u where u.userId =? and u.assessment.contentId=?";
    private static final String FIND_BY_USER_ID_SESSION_ID = "from " + AssessmentUser.class.getName()
	    + " as u where u.userId =? and u.session.sessionId=?";
    private static final String FIND_BY_SESSION_ID = "from " + AssessmentUser.class.getName()
	    + " as u where u.session.sessionId=?";

	public AssessmentUser getUserByUserIDAndSessionID(Long userID, Long sessionId) {
		List list = doFind(FIND_BY_USER_ID_SESSION_ID, new Object[] { userID, sessionId });
		if (list == null || list.size() == 0)
			return null;
		return (AssessmentUser) list.get(0);
	}

	public AssessmentUser getUserByUserIDAndContentID(Long userId, Long contentId) {
		List list = doFind(FIND_BY_USER_ID_CONTENT_ID, new Object[] { userId, contentId });
		if (list == null || list.size() == 0)
			return null;
		return (AssessmentUser) list.get(0);
	}

	@SuppressWarnings("unchecked")
	public List<AssessmentUser> getBySessionID(Long sessionId) {
		return (List<AssessmentUser>) this.doFind(FIND_BY_SESSION_ID, sessionId);
	}

}
