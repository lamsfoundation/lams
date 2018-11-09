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
import org.lamsfoundation.lams.tool.sbmt.dao.ISubmitFilesReportDAO;
import org.lamsfoundation.lams.tool.sbmt.model.SubmitFilesReport;
import org.springframework.stereotype.Repository;

/**
 * @author Manpreet Minhas
 */
@Repository
public class SubmitFilesReportDAO extends LAMSBaseDAO implements ISubmitFilesReportDAO {

    private static final String FIND_BY_SUBMISSION = "from " + SubmitFilesReport.class.getName()
	    + " where submission_id=?";

    @Override
    public SubmitFilesReport getReportByID(Long reportID) {
	return (SubmitFilesReport) super.find(SubmitFilesReport.class, reportID);
    }

    @Override
    public SubmitFilesReport getReportBySubmissionID(Long submissionID) {

	List list = doFind(FIND_BY_SUBMISSION, submissionID);
	if (list != null) {
	    return (SubmitFilesReport) list.get(0);
	} else {
	    return null;
	}
    }

    @Override
    public void updateReport(SubmitFilesReport report) {
	getSession().update(report);
    }
}
