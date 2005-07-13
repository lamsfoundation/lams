/*
 * Created on May 30, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.sbmt.dao;

import org.lamsfoundation.lams.AbstractLamsTestCase;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesContent;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesSession;
import org.lamsfoundation.lams.tool.sbmt.dao.hibernate.SubmitFilesContentDAO;
import org.lamsfoundation.lams.tool.sbmt.dao.hibernate.SubmitFilesSessionDAO;

/**
 * @author Manpreet Minhas
 */
public class TestSubmitFilesSession extends AbstractLamsTestCase {
	
	protected SubmitFilesSession submitFilesSession;
	protected SubmitFilesContent submitFilesContent;
	
	protected ISubmitFilesContentDAO submitFilesContentDAO;
	protected ISubmitFilesSessionDAO submitFilesSessionDAO;
	
	public TestSubmitFilesSession(String name){
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
	 * (non-Jsavadoc)
	 * @see org.lamsfoundation.lams.AbstractLamsTestCase#getHibernateSessionFactoryName()
	 */
	protected String getHibernateSessionFactoryName() {
		return "sbmtSessionFactory";
	}
	
	public void setUp() throws Exception{
		super.setUp();
		submitFilesSessionDAO = (SubmitFilesSessionDAO)context.getBean("submitFilesSessionDAO");
		submitFilesContentDAO = (SubmitFilesContentDAO)context.getBean("submitFilesContentDAO");
	}
	public void testAddSubmitFilesSession(){
		submitFilesContent = submitFilesContentDAO.getContentByID(new Long(1));
		submitFilesSession = new SubmitFilesSession(new Long(1),SubmitFilesSession.INCOMPLETE);
		submitFilesSessionDAO.insert(submitFilesSession);
		assertNotNull(submitFilesSession.getSessionID());
	}
	public void testGetSessionByID(){
		submitFilesSession = submitFilesSessionDAO.getSessionByID(new Long(1));
		assertEquals(submitFilesSession.getStatus(), new Integer(1));
	}

}
