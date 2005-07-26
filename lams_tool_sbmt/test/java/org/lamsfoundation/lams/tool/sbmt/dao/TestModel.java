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
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.TestCase;

import org.lamsfoundation.lams.tool.sbmt.SubmissionDetails;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesContent;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesReport;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesSession;

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
	//session data
	private static Long sessA = new Long(100);
	private static Integer statA = new Integer(100);
	private static Long sessB = new Long(200);
	private static Integer statB = new Integer(200);
	//detail data
	private String filePathA = "fpA";
	private String filePathB = "fpB";
	
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
	 * Class under test for SubmitFilesSession clone()
	 */
	public void testSessionClone() {
		fillSessionA(session);
		SubmitFilesSession sessionC = (SubmitFilesSession) session.clone();
		fillSessionB(session);
		
		assertEquals(sessionC.getSessionID(),sessA);
		assertEquals(sessionC.getStatus(),statA);
		//test SubmissionDetails Set 
		Iterator iter = sessionC.getSubmissionDetails().iterator();
		SubmissionDetails detail = new SubmissionDetails();
		fillDetailA(detail);
		assertEquals(iter.next(),detail);
		//test SubmissionDetails Set 
		iter = session.getSubmissionDetails().iterator();
		detail = new SubmissionDetails();
		fillDetailB(detail);
		assertEquals(iter.next(),detail);

	}	
	/*
	 * Class under test for SubmitFilesSession clone()
	 */
	public void testDetailClone() {
		fillDetailA(detail);
		SubmissionDetails detailC = (SubmissionDetails) detail.clone();
		fillDetailB(detail);
		
		assertEquals(detailC.getFilePath(),filePathA);
		SubmitFilesReport report = new SubmitFilesReport(); 
		fillReportA(report);
		assertEquals(detailC.getReport(),report);
		
	}	

	
	/*
	 * Class under test for SubmitFilesContent clone()
	 */
	public void testContentClone() {
		fillContentA(content);
		SubmitFilesContent contentC = (SubmitFilesContent) content.clone();
		fillContentB(content);
		
		assertEquals(contentC.getInstruction(),insA);
		assertEquals(contentC.getTitle(),titA);
		assertEquals(contentC.isDefineLater(),deA);
		assertEquals(contentC.isRunOffline(),offA);
		//test SubmitFilesSession l Set 
		Iterator iter = contentC.getToolSession().iterator();
		SubmitFilesSession session = new SubmitFilesSession();
		fillSessionA(session);
		assertEquals(iter.next(),session);
		
		//do more test to ensure origial object keep desired values "B"
		assertEquals(content.getInstruction(),insB);
		assertEquals(content.getTitle(),titB);
		assertEquals(content.isDefineLater(),deB);
		assertEquals(content.isRunOffline(),offB);
		//test SubmitFilesSession l Set 
		iter = content.getToolSession().iterator();
		session = new SubmitFilesSession();
		fillSessionB(session);
		assertEquals(iter.next(),session);
		
	}
	//================Fill init data for model object==========
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
		content.setInstruction(insA);
		content.setRunOffline(offA);
		content.setTitle(titA);
		//fill sessions
		SubmitFilesSession session = new SubmitFilesSession();
		fillSessionA(session);
		Set sessions = new TreeSet();
		sessions.add(session);
		content.setToolSession(sessions);
	}
	private void fillContentB(SubmitFilesContent content){
		content.setDefineLater(deB);
		content.setInstruction(insB);
		content.setRunOffline(offB);
		content.setTitle(titB);

		//fill sessions
		SubmitFilesSession session = new SubmitFilesSession();
		fillSessionB(session);
		Set sessions = new TreeSet();
		sessions.add(session);
		content.setToolSession(sessions);
	}
	private void fillSessionA(SubmitFilesSession session){
		session.setSessionID(sessA);
		session.setStatus(statA);
		//fill details
		SubmissionDetails detail = new SubmissionDetails();
		fillDetailA(detail);
		Set details = new TreeSet();
		details.add(detail);
		session.setSubmissionDetails(details);

	}
	private void fillSessionB(SubmitFilesSession session){
		session.setSessionID(sessB);
		session.setStatus(statB);
		//fill details
		SubmissionDetails detail = new SubmissionDetails();
		fillDetailB(detail);
		Set details = new TreeSet();
		details.add(detail);
		session.setSubmissionDetails(details);
	}
	
	/**
	 * @param detail2
	 */
	private void fillDetailA(SubmissionDetails detail) {
		detail.setFilePath(filePathA);
		SubmitFilesReport report = new SubmitFilesReport(); 
		fillReportA(report);
		detail.setReport(report);
	}
	/**
	 * @param detail2
	 */
	private void fillDetailB(SubmissionDetails detail) {
		detail.setFilePath(filePathB);
		SubmitFilesReport report = new SubmitFilesReport(); 
		fillReportB(report);
		detail.setReport(report);
	}
}
