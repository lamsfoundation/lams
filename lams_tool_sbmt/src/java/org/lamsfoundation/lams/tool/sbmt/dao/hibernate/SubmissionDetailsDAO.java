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
import org.lamsfoundation.lams.tool.sbmt.dao.ISubmissionDetailsDAO;
import org.lamsfoundation.lams.tool.sbmt.model.SubmissionDetails;
import org.lamsfoundation.lams.tool.sbmt.model.SubmitFilesSession;
import org.springframework.stereotype.Repository;

/**
 * @author Manpreet Minhas
 */
@Repository
public class SubmissionDetailsDAO extends LAMSBaseDAO implements ISubmissionDetailsDAO {

    private static final String FIND_BY_SESSION = "from " + SubmissionDetails.class.getName()
	    + " as d where d.submitFileSession.sessionID=:sessionID";
    private static final String FIND_BY_SESSION_LEARNER = "from " + SubmissionDetails.class.getName()
	    + " as d where d.submitFileSession.sessionID=? and d.learner.userID=?";

    @Override
    public SubmissionDetails getSubmissionDetailsByID(Long submissionID) {
	return (SubmissionDetails) getSession().get(SubmissionDetails.class, submissionID);
    }

    @Override
    public void saveOrUpdate(SubmitFilesSession session) {
	getSession().saveOrUpdate(session);

    }
    
    @Override
    public void save(SubmissionDetails submissionDetails) {
	getSession().save(submissionDetails);
    }

    @Override
    public List<SubmissionDetails> getSubmissionDetailsBySession(Long sessionID) {
	if (sessionID != null) {
	    return getSession().createQuery(FIND_BY_SESSION, SubmissionDetails.class)
		    .setParameter("sessionID", sessionID.longValue()).list();
	}
	return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<SubmissionDetails> getBySessionAndLearner(Long sessionID, Integer userID) {
	return (List<SubmissionDetails>) doFind(FIND_BY_SESSION_LEARNER, new Object[] { sessionID, userID });
    }
}
