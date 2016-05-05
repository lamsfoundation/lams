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

/* $$Id$$ */

package org.lamsfoundation.lams.tool.sbmt.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesReport;
import org.lamsfoundation.lams.tool.sbmt.dao.ISubmitFilesReportDAO;

/**
 * @author Manpreet Minhas
 */
public class SubmitFilesReportDAO extends BaseDAO implements ISubmitFilesReportDAO {

    private static final String FIND_BY_SUBMISSION = "from " + SubmitFilesReport.class.getName()
	    + " where submission_id=?";

    /**
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.sbmt.dao.ISubmitFilesReportDAO#getReportByID(java.lang.Long)
     */
    @Override
    public SubmitFilesReport getReportByID(Long reportID) {
	return (SubmitFilesReport) super.find(SubmitFilesReport.class, reportID);
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.sbmt.dao.ISubmitFilesReportDAO#getReportBySubmissionID(java.lang.Long)
     */
    @Override
    public SubmitFilesReport getReportBySubmissionID(Long submissionID) {

	List list = this.getHibernateTemplate().find(FIND_BY_SUBMISSION, submissionID);
	if (list != null) {
	    return (SubmitFilesReport) list.get(0);
	} else {
	    return null;
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.sbmt.dao.ISubmitFilesReportDAO#updateReport(org.lamsfoundation.lams.tool.sbmt.
     * SubmitFilesReport)
     */
    @Override
    public void updateReport(SubmitFilesReport report) {
	getHibernateTemplate().update(report);
    }
}
