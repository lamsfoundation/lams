/*
 * Created on May 24, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.sbmt.dao;

import java.util.Date;
import java.util.List;

import org.lamsfoundation.lams.AbstractLamsTestCase;
import org.lamsfoundation.lams.tool.sbmt.SubmissionDetails;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesContent;


/**
 * @author Manpreet Minhas
 */
public class TestSubmissionDetailsDAO extends AbstractLamsTestCase {
	
	protected ISubmissionDetailsDAO submissionDetailsDAO;
	protected ISubmitFilesContentDAO submitFilesContentDAO;
	
	public TestSubmissionDetailsDAO(String name){
		super(name);
	}

	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.AbstractLamsTestCase#getContextConfigLocation()
	 */
	protected String[] getContextConfigLocation() {
		return new String[] {"org/lamsfoundation/lams/applicationContext.xml",
				 "org/lamsfoundation/lams/workspace/workspaceApplicationContext.xml",
				 "org/lamsfoundation/lams/authoring/authoringApplicationContext.xml",
				 "org/lamsfoundation/lams/tool/sbmt/submitFilesApplicationContext.xml"};		
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
		submissionDetailsDAO = (ISubmissionDetailsDAO)context.getBean("submissionDetailsDAO");
		submitFilesContentDAO = (ISubmitFilesContentDAO)context.getBean("submitFilesContentDAO");
	}
	
	public void testAddDetails(){	
		SubmitFilesContent content = submitFilesContentDAO.getContentByID(new Long(1));
		SubmissionDetails details = new SubmissionDetails("filePath","fileDescription",new Date(),
															  new Long(1),new Long(1),new Long(1),
															  content);															  
		submissionDetailsDAO.insert(details);
		assertNotNull(details.getSubmissionID());
	}
	public void testGetDistinctUser(){
		List list = submissionDetailsDAO.getUsersForContent(new Long(1));
		assertEquals(list.size(),3);
	}

}
