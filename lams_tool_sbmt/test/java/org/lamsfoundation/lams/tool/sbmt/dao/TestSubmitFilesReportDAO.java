/*
 * Created on May 30, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.sbmt.dao;

import org.lamsfoundation.lams.AbstractLamsTestCase;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesContent;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesReport;
import org.lamsfoundation.lams.tool.sbmt.dao.hibernate.SubmitFilesContentDAO;
import org.lamsfoundation.lams.tool.sbmt.dao.hibernate.SubmitFilesReportDAO;

/**
 * @author Manpreet Minhas
 */
public class TestSubmitFilesReportDAO extends AbstractLamsTestCase {
	
	protected SubmitFilesReport submitFilesReport;
	protected SubmitFilesContent submitFilesContent;
	protected ISubmitFilesContentDAO submitFilesContentDAO;
	protected ISubmitFilesReportDAO submitFilesReportDAO;
	protected ISubmissionDetailsDAO submissionDetailsDAO;
	
	public TestSubmitFilesReportDAO(String name){
		super(name);
	}
	
	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.AbstractLamsTestCase#getContextConfigLocation()
	 */
	protected String[] getContextConfigLocation() {
		return new String[] {"org/lamsfoundation/lams/tool/sbmt/submitFilesApplicationContext.xml"};
	}

	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.AbstractLamsTestCase#getHibernateSessionFactoryName()
	 */
	protected String getHibernateSessionFactoryName() {
		return "sbmtSessionFactory";
	}
	public void setUp()throws Exception{
		super.setUp();
		submitFilesReportDAO = (SubmitFilesReportDAO)context.getBean("submitFilesReportDAO");
		submitFilesContentDAO = (SubmitFilesContentDAO)context.getBean("submitFilesContentDAO");
		submissionDetailsDAO = (ISubmissionDetailsDAO)context.getBean("submissionDetailsDAO");
	}
	public void testAddSubmitFilesReport(){
		submitFilesContent = submitFilesContentDAO.getContentByID(new Long(1));
		
		submitFilesReport = new SubmitFilesReport(submissionDetailsDAO.getSubmissionDetailsByID(new Long(1)));
		submitFilesReportDAO.insert(submitFilesReport);
		assertNotNull(submitFilesReport.getReportID());
	}	
}
