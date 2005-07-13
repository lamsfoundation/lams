/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */
package org.lamsfoundation.lams.tool.sbmt.dao;

import java.util.Calendar;
import java.util.Date;

import org.lamsfoundation.lams.tool.sbmt.SubmissionDetails;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesContent;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesReport;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesSession;

import junit.framework.TestCase;

/**
 * @author Steve.Ni
 * 
 * $version$
 */
public class TestModel extends TestCase {
	//report data
	private static String commA ="commA";
	private static Long markA = new Long(100);
	private static Long repA = new Long(100);
	private static int dayA = 10;
	private static int mthA = 9;
	private static int yearA = 2003;
	
	private static String commB ="commB";
	private static Long markB = new Long(201);
	private static Long repB = new Long(201);
	private static int dayB = 11;
	private static int mthB = 8;
	private static int yearB = 2004;

	//content data
	private static String insA = "InstructionsA";
	private static String insB = "InstructionsB";
	private static String titA = "titleA";
	private static String titB = "titleB";
	private static boolean deA = false;
	private static boolean deB = true;
	private static boolean offA = false;
	private static boolean offB = false;
	
	private SubmitFilesContent content;
	private SubmitFilesReport report;
	private SubmitFilesSession session;
	private SubmissionDetails detail;
	
	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		content = new SubmitFilesContent();
		
		content.setContentID(new Long(1));
		session = new SubmitFilesSession();
		detail = new SubmissionDetails();
		report = new SubmitFilesReport();
	}
	/*
	 * Class under test for SubmitFilesReport clone()
	 */
	public void testReportClone(){
		fillReportA(report);
		SubmitFilesReport reportC = (SubmitFilesReport) report.clone();
		fillReportB(report);
		
		assertEquals(reportC.getComments(),commA);
		Calendar cal = Calendar.getInstance();
		cal.set(yearA,mthA,dayA);
		assertEquals(reportC.getDateMarksReleased(),cal.getTime());
		assertEquals(reportC.getMarks(),markA);
		assertEquals(reportC.getReportID(),repA);
				
	}
	/*
	 * Class under test for SubmitFilesContent clone()
	 */
	public void testContentClone() {
		fillContentA(content);
		SubmitFilesContent contentC = (SubmitFilesContent) content.clone();
		fillContentB(content);
		
		assertEquals(contentC.getInstructions(),insA);
		assertEquals(contentC.getTitle(),titA);
		assertEquals(contentC.isDefineLater(),deA);
		assertEquals(contentC.isRunOffline(),offA);
//		assertEquals(contentC.getSubmissionDetails(),insA);
//		assertEquals(contentC.getToolSession(),insA);
		
	}
	
	private void fillReportA(SubmitFilesReport report){
		report.setComments(commA);
		Calendar cal = Calendar.getInstance();
		cal.set(yearA,mthA,dayA);
		report.setDateMarksReleased(cal.getTime());
		report.setMarks(markA);
		report.setReportID(repA);
	}
	private void fillReportB(SubmitFilesReport report){
		report.setComments(commB);
		Calendar cal = Calendar.getInstance();
		cal.set(yearB,mthB,dayB);
		report.setDateMarksReleased(cal.getTime());
		report.setMarks(markB);
		report.setReportID(repB);
	}
	private void fillContentA(SubmitFilesContent content){
		content.setDefineLater(deA);
		content.setInstructions(insA);
		content.setRunOffline(offA);
		content.setTitle(titA);
//		content.setSubmissionDetails();
//		content.setToolSession();
	}
	private void fillContentB(SubmitFilesContent content){
		content.setDefineLater(deB);
		content.setInstructions(insB);
		content.setRunOffline(offB);
		content.setTitle(titB);
//		content.setSubmissionDetails();
//		content.setToolSession();
	}
	
}
