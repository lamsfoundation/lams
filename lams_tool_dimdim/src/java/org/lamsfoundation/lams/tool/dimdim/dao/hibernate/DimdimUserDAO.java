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
/* $Id$ */

package org.lamsfoundation.lams.tool.dimdim.dao.hibernate;

import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.tool.dimdim.dao.IDimdimUserDAO;
import org.lamsfoundation.lams.tool.dimdim.model.DimdimUser;

/**
 * DAO for accessing the DimdimUser objects - Hibernate specific code.
 */
public class DimdimUserDAO extends BaseDAO implements IDimdimUserDAO {

	private static final String FIND_BY_USER_ID_SESSION_ID = "from "
			+ DimdimUser.class.getName()
			+ " as dimdimUser where dimdimUser.userId=:userId and dimdimUser.dimdimSession.sessionId=:sessionId";

	public DimdimUser getByUserIdAndSessionId(Long userId, Long toolSessionId) {
		return (DimdimUser) getSession()
				.createQuery(FIND_BY_USER_ID_SESSION_ID).setLong("userId",
						userId).setLong("sessionId", toolSessionId)
				.uniqueResult();
	}
}
