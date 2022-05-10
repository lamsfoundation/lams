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

package org.lamsfoundation.lams.tool.sbmt.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.sbmt.dao.ISubmitFilesSessionDAO;
import org.lamsfoundation.lams.tool.sbmt.model.SubmitFilesSession;
import org.springframework.stereotype.Repository;

/**
 * @author Manpreet Minhas
 */
@Repository
public class SubmitFilesSessionDAO extends LAMSBaseDAO implements ISubmitFilesSessionDAO {

    private static final String FIND_LEARNER_BY_CONTENT_ID = " from " + SubmitFilesSession.class.getName()
	    + " as session where session.content.contentID = :contentID";

    @Override
    public SubmitFilesSession getSessionByID(Long sessionID) {
	return super.find(SubmitFilesSession.class, sessionID);
    }

    @Override
    public void createSession(SubmitFilesSession submitSession) {
	getSession().save(submitSession);
    }

    @Override
    public List<SubmitFilesSession> getSubmitFilesSessionByContentID(Long contentID) {
	if (contentID != null) {
	    return getSession().createQuery(FIND_LEARNER_BY_CONTENT_ID, SubmitFilesSession.class)
		    .setParameter("contentID", contentID).setCacheable(true).list();
	}
	return null;
    }

}
