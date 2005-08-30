/*
 * Created on May 20, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.sbmt.service;

import org.lamsfoundation.lams.test.AbstractLamsTestCase;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesContent;
import org.lamsfoundation.lams.tool.sbmt.dao.ISubmissionDetailsDAO;
import org.lamsfoundation.lams.tool.sbmt.dao.ISubmitFilesContentDAO;

/**
 * @author Manpreet Minhas
 */
public class TestSubmitFilesService extends AbstractLamsTestCase {
	
	protected ISubmitFilesService submitFilesService;
	protected ISubmitFilesContentDAO submitFilesContentDAO;
	protected ISubmissionDetailsDAO submissionDetailsDAO;
	
	protected SubmitFilesContent submitFilesContent;
	
	public TestSubmitFilesService(String name){
		super(name);
	}

	/**
	 *(non-Javadoc)
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
		submitFilesService = (ISubmitFilesService)context.getBean("submitFilesService");
		submitFilesContentDAO = (ISubmitFilesContentDAO)context.getBean("submitFilesContentDAO");
		submissionDetailsDAO = (ISubmissionDetailsDAO)context.getBean("submissionDetailsDAO");
	}
	
	/*public void testAddSubmitFilesContent(){
		submitFilesService.addSubmitFilesContent(new Long(1),"Trial Title Submit Files", "Trial Instructions Submit Files");
		assertNotNull(submitFilesContentDAO.getContentByID(new Long(1)));
	}
	public void testUploadFile(){
		String filePath = "c:" + File.separator + "mminhas.txt";
		submitFilesService.uploadFile(new Long(1),filePath,"Trial Content File Description", new Long(1));
		submitFilesService.uploadFile(new Long(1),filePath,"Trial Content File Description", new Long(1));
		submitFilesService.uploadFile(new Long(1),filePath,"Trial Content File Description", new Long(2));
		submitFilesService.uploadFile(new Long(1),filePath,"Trial Content File Description", new Long(3));
		submitFilesService.uploadFile(new Long(1),filePath,"Trial Content File Description", new Long(1));
		submitFilesService.uploadFile(new Long(1),filePath,"Trial Content File Description", new Long(2));
		assertNotNull(submissionDetailsDAO.getSubmissionDetailsByID(new Long(1)));
	}
	public void testRemoveToolContent(){
		submitFilesService.removeToolContent(new Long(1));
		try{
			submitFilesContentDAO.getContentByID(new Long(1));
			fail("Exception should be raised because this object has already been deleted");
		}catch(HibernateObjectRetrievalFailureException he){
			assertTrue(true);
		}
	}
	public void testGenerateReport(){
		Hashtable table = submitFilesService.generateReport(new Long(1));
		assertEquals(table.size(),3);
	}*/

}
 