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

package org.lamsfoundation.lams.tool.scratchie.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.scratchie.dao.ScratchieUserDAO;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieUser;
import org.springframework.stereotype.Repository;

@Repository
public class ScratchieUserDAOHibernate extends LAMSBaseDAO implements ScratchieUserDAO {

    private static final String FIND_BY_USER_ID_CONTENT_ID = "from " + ScratchieUser.class.getName()
	    + " as u where u.userId =? and u.session.scratchie.contentId=?";
    private static final String FIND_BY_USER_ID_SESSION_ID = "from " + ScratchieUser.class.getName()
	    + " as u where u.userId =? and u.session.sessionId=?";
    private static final String FIND_BY_SESSION_ID = "from " + ScratchieUser.class.getName()
	    + " as u where u.session.sessionId=?";

    @Override
    public ScratchieUser getUserByUserIDAndSessionID(Long userID, Long sessionId) {
	List list = this.doFind(FIND_BY_USER_ID_SESSION_ID, new Object[] { userID, sessionId });
	if (list == null || list.size() == 0) {
	    return null;
	}
	return (ScratchieUser) list.get(0);
    }

    @Override
    public ScratchieUser getUserByUserIDAndContentID(Long userId, Long contentId) {
	List list = this.doFind(FIND_BY_USER_ID_CONTENT_ID, new Object[] { userId, contentId });
	if (list == null || list.size() == 0) {
	    return null;
	}
	return (ScratchieUser) list.get(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ScratchieUser> getBySessionID(Long sessionId) {
	return (List<ScratchieUser>) this.doFind(FIND_BY_SESSION_ID, sessionId);
    }
    
    @Override
    public int countUsersByContentId(Long contentId) {
	final String COUNT_USERS_BY_CONTENT_ID = "SELECT COUNT(*) FROM " + ScratchieUser.class.getName()
		+ " as user WHERE user.session.scratchie.contentId = :contentId ";
	    
	List list = getSession().createQuery(COUNT_USERS_BY_CONTENT_ID).setParameter("contentId", contentId).list();
	if (list == null || list.size() == 0) {
	    return 0;
	} else {
	    return ((Number) list.get(0)).intValue();
	}
    }
}
