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


/*
 * Created on May 11, 2005
 */

package org.lamsfoundation.lams.tool.noticeboard;

import org.lamsfoundation.lams.AbstractLamsTestCase;
import org.lamsfoundation.lams.tool.noticeboard.dao.hibernate.NoticeboardContentDAO;
import org.lamsfoundation.lams.tool.noticeboard.dao.hibernate.NoticeboardSessionDAO;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;
import java.util.Date;
import java.util.TreeSet;

/**
 * @author mtruong
 */
public class NbDataAccessTestCase extends AbstractLamsTestCase
{

    //---------------------------------------------------------------------
    // DAO instances for initializing data
    //---------------------------------------------------------------------
    private NoticeboardContentDAO noticeboardDAO;
    private NoticeboardSessionDAO nbSessionDAO;
   
    //---------------------------------------------------------------------
    // Domain Object instances
    //---------------------------------------------------------------------
    protected NoticeboardContent nbContent;
    protected NoticeboardSession nbSession;
    
    //---------------------------------------------------------------------
    // Constant data used for Testing
    //---------------------------------------------------------------------
    
    protected final long ONE_DAY = 60 * 60 * 1000 * 24;
    
    protected final Long TEST_NB_ID = new Long(1500);
    protected final Long TEST_COPYNB_ID = new Long(3500);
    
    protected final String TEST_TITLE = "Test Title";
    protected final String TEST_CONTENT = "Welcome! We hope you enjoy the activities that are set out.";
	protected final String TEST_ONLINE_INSTRUCTIONS = "Put your online instructions here";
	protected final String TEST_OFFLINE_INSTRUCTIONS = "Put your offline instructions here";
	protected final boolean TEST_DEFINE_LATER = false;
	protected final boolean TEST_FORCE_OFFLINE = false;
	protected final Date TEST_DATE_CREATED = new Date(System.currentTimeMillis());
	protected final Date TEST_DATE_UPDATED = new Date();
	protected final Long TEST_CREATOR_USER_ID = new Long(1300);
	
	protected final Long TEST_SESSION_ID = new Long("1400");
	protected final Date TEST_SESSION_START_DATE = new Date(System.currentTimeMillis());
	protected final Date TEST_SESSION_END_DATE = new Date(System.currentTimeMillis() + ONE_DAY);
	protected final String TEST_SESSION_STATUS = "Active";
	
	
	
	/** Default Constructor */
	public NbDataAccessTestCase(String name)
	{
		super(name);
	}
	
	//---------------------------------------------------------------------
    // Inherited Methods
    //---------------------------------------------------------------------
	
    protected void setUp() throws Exception {
        super.setUp();
        noticeboardDAO = (NoticeboardContentDAO) this.context.getBean("nbContentDAO");
        nbSessionDAO = (NoticeboardSessionDAO) this.context.getBean("nbSessionDAO");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

	 /** Define the context files. Overrides method in AbstractLamsTestCase */
    protected String[] getContextConfigLocation() {
    	return new String[] {
    	       "org/lamsfoundation/lams/tool/noticeboard/testApplicationContext.xml"};
    }
    
    /** Define the sessionFactory bean name located in testApplication.xml. */
    protected String getHibernateSessionFactoryName()
    {
    	return "nbSessionFactory";
    }
    
    protected void initNbContentData()
    {
    	//noticeboardDAO = (NoticeboardContentDao) this.context.getBean("nbContentDAO");
    	/* Check to see if data exist or not, if not then add to database */
    	
    	
	    	nbContent = new NoticeboardContent(TEST_NB_ID,	
														TEST_TITLE,
														TEST_CONTENT,
														TEST_ONLINE_INSTRUCTIONS,
														TEST_OFFLINE_INSTRUCTIONS,
														TEST_DEFINE_LATER,
														TEST_FORCE_OFFLINE,
														TEST_CREATOR_USER_ID,
														TEST_DATE_CREATED,
														TEST_DATE_UPDATED,
														new TreeSet()
					);
	    	
	    	noticeboardDAO.saveNbContent(nbContent);
	 
	}
    
    protected void cleanNbContentData()
    {
    	noticeboardDAO.removeNoticeboard(TEST_NB_ID);
    }
    
    protected Long getTestNoticeboardId()
    {
        return this.TEST_NB_ID;
    }
    
    protected void initNbSessionContent()
    {

    	NoticeboardContent nb = noticeboardDAO.getNbContentById(TEST_NB_ID);
    	
    	nbSession = new NoticeboardSession(TEST_SESSION_ID,
    									   nb,
										   TEST_SESSION_START_DATE,
										   TEST_SESSION_END_DATE,
										   TEST_SESSION_STATUS);
    		
    	nbSessionDAO.saveNbSession(nbSession);
    	
    	//associate the session with the content
    	nb.getNbSessions().add(nbSession);
    	
    }
    
    protected void cleanNbSessionContent()
    {
    	nbSessionDAO.removeNbSession(TEST_SESSION_ID);
    }
    
    protected void cleanNbCopiedContent()
    {
    	noticeboardDAO.removeNoticeboard(TEST_COPYNB_ID);
    }
    
    protected void initAllData()
    {
    	initNbContentData();
    	initNbSessionContent();    	
    }
    
    protected void cleanAllData()
    {
    	cleanNbSessionContent();
    	cleanNbContentData();
    	
    }
    
    /*
     * 
     * @author mtruong
     *
     * TODO write a test case to test the getter and setter methods 
     * for the POJO
     */
    
}
