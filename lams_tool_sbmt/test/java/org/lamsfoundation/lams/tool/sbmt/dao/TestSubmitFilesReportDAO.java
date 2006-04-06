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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $$Id$$ */	

package org.lamsfoundation.lams.tool.sbmt.dao;

import org.lamsfoundation.lams.tool.sbmt.SbmtBaseTestCase;
import org.lamsfoundation.lams.tool.sbmt.SubmissionDetails;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesContent;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesReport;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesSession;
import org.lamsfoundation.lams.tool.sbmt.dao.hibernate.SubmitFilesContentDAO;
import org.lamsfoundation.lams.tool.sbmt.dao.hibernate.SubmitFilesReportDAO;

/**
 * @author Manpreet Minhas
 */
public class TestSubmitFilesReportDAO extends SbmtBaseTestCase {
	
	protected SubmitFilesReport submitFilesReport;
	protected SubmitFilesContent submitFilesContent;
	protected ISubmitFilesContentDAO submitFilesContentDAO;
	protected ISubmitFilesReportDAO submitFilesReportDAO;
	protected ISubmissionDetailsDAO submissionDetailsDAO;
	
	public TestSubmitFilesReportDAO(String name){
		super(name);
	}
	
	public void setUp()throws Exception{
		super.setUp();
		submitFilesReportDAO = (SubmitFilesReportDAO)context.getBean("submitFilesReportDAO");
		submitFilesContentDAO = (SubmitFilesContentDAO)context.getBean("submitFilesContentDAO");
		submissionDetailsDAO = (ISubmissionDetailsDAO)context.getBean("submissionDetailsDAO");
	}
	
	public void testGetReportByID() {
		SubmitFilesReport report = submitFilesReportDAO.getReportByID(TEST_REPORT_ID);
		assertNotNull("report",report);
		assertEquals(report.getComments(),TEST_REPORT_COMMENT);
	}
	public void testAddSubmitFilesReport(){

		SubmissionDetails details = submissionDetailsDAO.getSubmissionDetailsByID(TEST_SUBMISSION_ID);
		assertNotNull("details", details);

		submitFilesReport = details.getReport();
		assertNotNull("submitFilesReport", submitFilesReport);

		SubmitFilesReport clone = (SubmitFilesReport) submitFilesReport.clone();
		submitFilesReportDAO.insert(clone);
		assertNotNull(clone.getReportID());
		assertNotSame("Clone has a different id", clone.getReportID(), submitFilesReport.getReportID());

	}	
}
