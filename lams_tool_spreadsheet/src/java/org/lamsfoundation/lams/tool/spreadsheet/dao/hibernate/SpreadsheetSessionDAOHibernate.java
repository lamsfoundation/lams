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

package org.lamsfoundation.lams.tool.spreadsheet.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.spreadsheet.dao.SpreadsheetSessionDAO;
import org.lamsfoundation.lams.tool.spreadsheet.model.SpreadsheetSession;
import org.springframework.stereotype.Repository;

@Repository
public class SpreadsheetSessionDAOHibernate extends LAMSBaseDAO implements SpreadsheetSessionDAO {

    private static final String FIND_BY_SESSION_ID = "from " + SpreadsheetSession.class.getName()
	    + " as p where p.sessionId=?";
    private static final String FIND_BY_CONTENT_ID = "from " + SpreadsheetSession.class.getName()
	    + " as p where p.spreadsheet.contentId=?";

    @Override
    public SpreadsheetSession getSessionBySessionId(Long sessionId) {
	List list = doFindCacheable(FIND_BY_SESSION_ID, sessionId);
	if (list == null || list.size() == 0) {
	    return null;
	}
	return (SpreadsheetSession) list.get(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<SpreadsheetSession> getByContentId(Long toolContentId) {
	return doFindCacheable(FIND_BY_CONTENT_ID, toolContentId);
    }

    @Override
    public void delete(SpreadsheetSession session) {
	getSession().delete(session);
    }

    @Override
    public void deleteBySessionId(Long toolSessionId) {
	this.removeObject(SpreadsheetSession.class, toolSessionId);
    }

}
