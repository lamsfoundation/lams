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
package org.lamsfoundation.lams.tool.scratchie.dao.hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.lamsfoundation.lams.tool.scratchie.dao.ScratchieSessionDAO;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieSession;
import org.lamsfoundation.lams.tool.scratchie.util.ScratchieSessionComparator;

public class ScratchieSessionDAOHibernate extends BaseDAOHibernate implements ScratchieSessionDAO {

    private static final String FIND_BY_SESSION_ID = "from " + ScratchieSession.class.getName()
	    + " as p where p.sessionId=?";
    private static final String FIND_BY_CONTENT_ID = "from " + ScratchieSession.class.getName()
	    + " as p where p.scratchie.contentId=? order by p.sessionName asc";

    public ScratchieSession getSessionBySessionId(Long sessionId) {
	List list = getHibernateTemplate().find(FIND_BY_SESSION_ID, sessionId);
	if (list == null || list.size() == 0)
	    return null;
	return (ScratchieSession) list.get(0);
    }

    public List<ScratchieSession> getByContentId(Long toolContentId) {
	List<ScratchieSession> sessions = getHibernateTemplate().find(FIND_BY_CONTENT_ID, toolContentId);
	
	Set<ScratchieSession> sortedSessions = new TreeSet<ScratchieSession>(new ScratchieSessionComparator());
	sortedSessions.addAll(sessions);
	
	return new ArrayList<ScratchieSession>(sortedSessions);
    }

    public void delete(ScratchieSession session) {
	this.getHibernateTemplate().delete(session);
    }

    public void deleteBySessionId(Long toolSessionId) {
	this.removeObject(ScratchieSession.class, toolSessionId);
    }

}
