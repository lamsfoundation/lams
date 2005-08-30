package org.lamsfoundation.lams.tool.sbmt.dao;

import org.lamsfoundation.lams.test.AbstractLamsTestCase;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesContent;
import org.lamsfoundation.lams.tool.sbmt.dao.ISubmitFilesContentDAO;
/*
 * Created on May 30, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author Manpreet Minhas
 */
public class TestSubmitFilesContentDAO extends AbstractLamsTestCase {
	
	protected SubmitFilesContent submitFilesContent;
	protected ISubmitFilesContentDAO submitFilesContentDAO;
	
	public TestSubmitFilesContentDAO(String name){
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
		submitFilesContentDAO = (ISubmitFilesContentDAO)context.getBean("submitFilesContentDAO");
	}
	public void testAddSubmitFilesContent(){
		submitFilesContent = new SubmitFilesContent(new Long(1),"Trial Content","Trial Instructions");
		submitFilesContentDAO.insert(submitFilesContent);
		assertNotNull(submitFilesContent.getContentID());
	}
	public void testGetContentByID(){
		submitFilesContent = submitFilesContentDAO.getContentByID(new Long(1));
		assertEquals(submitFilesContent.getTitle(), new String("Trial Content"));
	}

}
