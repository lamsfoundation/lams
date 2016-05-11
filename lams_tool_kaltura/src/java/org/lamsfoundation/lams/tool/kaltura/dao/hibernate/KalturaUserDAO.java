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


package org.lamsfoundation.lams.tool.kaltura.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.kaltura.dao.IKalturaUserDAO;
import org.lamsfoundation.lams.tool.kaltura.model.KalturaUser;
import org.springframework.stereotype.Repository;

/**
 * DAO for accessing the KalturaUser objects - Hibernate specific code.
 */
@Repository
public class KalturaUserDAO extends LAMSBaseDAO implements IKalturaUserDAO {

    public static final String SQL_QUERY_FIND_BY_USER_ID_SESSION_ID = "from " + KalturaUser.class.getName() + " as f"
	    + " where user_id=? and f.session.sessionId=?";

    public static final String SQL_QUERY_FIND_BY_LOGIN_NAME_SESSION_ID = "from " + KalturaUser.class.getName()
	    + " as f where login_name=? and f.session.sessionId=?";

    private static final String SQL_QUERY_FIND_BY_UID = "from " + KalturaUser.class.getName() + " where uid=?";

    private static final String FIND_BY_USER_ID_CONTENT_ID = "from " + KalturaUser.class.getName()
	    + " as u where u.userId =? and u.kaltura.toolContentId=?";

    @Override
    public KalturaUser getByUserIdAndSessionId(Long userId, Long toolSessionId) {
	List list = this.doFind(SQL_QUERY_FIND_BY_USER_ID_SESSION_ID, new Object[] { userId, toolSessionId });

	if (list == null || list.isEmpty()) {
	    return null;
	}

	return (KalturaUser) list.get(0);
    }

    @Override
    public KalturaUser getByLoginNameAndSessionId(String loginName, Long toolSessionId) {

	List list = this.doFind(SQL_QUERY_FIND_BY_LOGIN_NAME_SESSION_ID, new Object[] { loginName, toolSessionId });

	if (list == null || list.isEmpty()) {
	    return null;
	}

	return (KalturaUser) list.get(0);

    }

    @Override
    public KalturaUser getByUserIdAndContentId(Long userId, Long contentId) {
	List list = this.doFind(FIND_BY_USER_ID_CONTENT_ID, new Object[] { userId, contentId });
	if (list == null || list.size() == 0) {
	    return null;
	}
	return (KalturaUser) list.get(0);
    }

    @Override
    public void saveOrUpdate(KalturaUser kalturaUser) {
	getSession().saveOrUpdate(kalturaUser);
	getSession().flush();
    }

    @Override
    public KalturaUser getByUid(Long uid) {
	List list = this.doFind(SQL_QUERY_FIND_BY_UID, new Object[] { uid });

	if (list == null || list.isEmpty()) {
	    return null;
	}

	return (KalturaUser) list.get(0);
    }
}
