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

import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.tool.sbmt.SubmissionDetails;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesSession;
import org.lamsfoundation.lams.tool.sbmt.dao.ISubmissionDetailsDAO;

/**
 * @author Manpreet Minhas
 */
public class SubmissionDetailsDAO extends BaseDAO implements ISubmissionDetailsDAO {

    private static final String FIND_BY_SESSION = "from " + SubmissionDetails.class.getName()
	    + " as d where d.submitFileSession.sessionID=? ";
    private static final String FIND_BY_SESSION_LEARNER = "from " + SubmissionDetails.class.getName()
	    + " as d where d.submitFileSession.sessionID=? and d.learner.userID=?";

    /**
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.sbmt.dao.ISubmissionDetailsDAO#getSubmissionDetailsByID(java.lang.Long)
     */
    @Override
    public SubmissionDetails getSubmissionDetailsByID(Long submissionID) {
	return (SubmissionDetails) this.getHibernateTemplate().get(SubmissionDetails.class, submissionID);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.sbmt.dao.ISubmissionDetailsDAO#saveOrUpdate(org.lamsfoundation.lams.tool.sbmt.
     * SubmitFilesSession)
     */
    @Override
    public void saveOrUpdate(SubmitFilesSession session) {
	this.getHibernateTemplate().saveOrUpdate(session);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.sbmt.dao.ISubmissionDetailsDAO#getSubmissionDetailsBySession(java.lang.Long)
     */
    @Override
    public List getSubmissionDetailsBySession(Long sessionID) {
	if (sessionID != null) {
	    return this.getSession().createQuery(FIND_BY_SESSION).setLong(0, sessionID.longValue()).list();
	}
	return null;
    }

    @Override
    public List<SubmissionDetails> getBySessionAndLearner(Long sessionID, Integer userID) {

	return this.getHibernateTemplate().find(FIND_BY_SESSION_LEARNER, new Object[] { sessionID, userID });

    }
}
