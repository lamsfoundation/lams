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



package org.lamsfoundation.lams.tool.sbmt.dao;

import java.util.List;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.tool.sbmt.model.SubmissionDetails;
import org.lamsfoundation.lams.tool.sbmt.model.SubmitFilesSession;

/**
 * @author Manpreet Minhas
 */
public interface ISubmissionDetailsDAO extends IBaseDAO {

    /**
     * Returns a <code>SubmissionDetails</code> object
     * corresponding to given <code>submissionID</code>
     * 
     * @param submissionID
     *            The submission_id to be looked up
     * @return SubmissionDetails The required populated object
     */
    public SubmissionDetails getSubmissionDetailsByID(Long submissionID);

    /**
     * Save or update the given <code>SubmitFilesSession</code> value.
     * 
     * @param session
     */
    public void saveOrUpdate(SubmitFilesSession session);
    
    public void save(SubmissionDetails submissionDetails);

    /**
     * @param sessionID
     * @return
     */
    public List<SubmissionDetails> getSubmissionDetailsBySession(Long sessionID);

    public List<SubmissionDetails> getBySessionAndLearner(Long sessionID, Integer userID);
}
