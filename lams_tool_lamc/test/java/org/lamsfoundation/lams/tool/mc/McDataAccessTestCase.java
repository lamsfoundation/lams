/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.mc;

import java.util.HashSet;

import org.lamsfoundation.lams.test.AbstractLamsTestCase;
import org.lamsfoundation.lams.tool.mc.dao.hibernate.McContentDAO;
import org.lamsfoundation.lams.tool.mc.dao.hibernate.McOptionsContentDAO;
import org.lamsfoundation.lams.tool.mc.dao.hibernate.McQueContentDAO;
import org.lamsfoundation.lams.tool.mc.dao.hibernate.McSessionDAO;
import org.lamsfoundation.lams.tool.mc.dao.hibernate.McUserDAO;
import org.lamsfoundation.lams.tool.mc.dao.hibernate.McUsrAttemptDAO;
import org.lamsfoundation.lams.tool.mc.pojos.McContent;
import org.lamsfoundation.lams.tool.mc.pojos.McOptsContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueContent;
import org.lamsfoundation.lams.tool.mc.pojos.McSession;

/**
 * @author Ozgur Demirtas
 */
public class McDataAccessTestCase extends AbstractLamsTestCase
{
	//These both refer to the same entry in the db.
	protected final Long DEFAULT_CONTENT_ID = new Long(10);
	protected final Long TEST_CONTENT_ID = new Long(2);
	protected final Long TEST_CONTENT_ID_OTHER = new Long(3);
	
	protected final Long TEST_SESSION_ID = new Long(20);
	protected final Long TEST_SESSION_ID_OTHER = new Long(21);
	
	protected final Long TEST_QUE_ID1 = new Long(1);
	protected final Long TEST_QUE_OPTION_ID1 = new Long(1);
	protected final Long TEST_QUE_OPTION_ID2 = new Long(2);
	protected final Long TEST_QUE_OPTION_ID3 = new Long(3);
	
	protected final Long TEST_NEW_USER_ID = new Long(100);
	protected final Long TEST_MY_USER_ID = new Long(77);
		
    protected final long ONE_DAY = 60 * 60 * 1000 * 24;
    
    public final String NOT_ATTEMPTED = "NOT_ATTEMPTED";
    public final String INCOMPLETE = "INCOMPLETE";
    public static String COMPLETED = "COMPLETED";
	
	protected McContentDAO mcContentDAO;
	protected McSessionDAO mcSessionDAO;
	protected McUserDAO    mcUserDAO;
	protected McQueContentDAO mcQueContentDAO;
	protected McOptionsContentDAO mcOptionsContentDAO;
	protected McUsrAttemptDAO  mcUsrAttemptDAO;
	
	
	protected McSession mcSession;
	protected McQueContent mcQueContent;
	
	
	public McDataAccessTestCase(String name)
    {
        super(name);
    }

    protected void setUp() throws Exception
    {
        super.setUp();
        mcContentDAO = (McContentDAO) this.context.getBean("mcContentDAO");
        mcSessionDAO = (McSessionDAO) this.context.getBean("mcSessionDAO");
        mcUserDAO = (McUserDAO) this.context.getBean("mcUserDAO");
        mcQueContentDAO = (McQueContentDAO) this.context.getBean("mcQueContentDAO");
        mcOptionsContentDAO =(McOptionsContentDAO) this.context.getBean("mcOptionsContentDAO");
        mcUsrAttemptDAO = (McUsrAttemptDAO) this.context.getBean("mcUsrAttemptDAO");
    }

    protected String[] getContextConfigLocation()
    {
    	return new String[] {"/org/lamsfoundation/lams/tool/mc/testmcApplicationContext.xml" };
    }
    
    protected String getHibernateSessionFactoryName()
    {
    	return "mcSessionFactory";
    }
    
    protected void tearDown() throws Exception
    {
    	super.tearDown();
    }
    

    public void testInitDB()
    {
    	//create new mc content
    	McContent mc = new McContent();
		mc.setMcContentId(DEFAULT_CONTENT_ID);
		mc.setTitle("title...");
		mc.setInstructions("instructions...");
		mc.setQuestionsSequenced(false);
		mc.setUsernameVisible(false);
		mc.setCreatedBy(0);
		mc.setMonitoringReportTitle("Monitoring Report");
		mc.setReportTitle("Report");
		mc.setRunOffline(false);
	    mc.setDefineLater(false);
	    mc.setSynchInMonitor(false);
	    mc.setOnlineInstructions("online instructions...");
	    mc.setOfflineInstructions("offline instructions...");
	    mc.setEndLearningMessage("Thanks.");
	    mc.setContentInUse(false);
	    mc.setRetries(false);
	    mc.setShowFeedback(false);
	    mc.setMcQueContents(new HashSet());
	    mc.setMcSessions(new HashSet());
	    mcContentDAO.saveMcContent(mc);
	    
	    
    	McContent mcContent = mcContentDAO.findMcContentById(DEFAULT_CONTENT_ID);
    	McQueContent mcQueContent=  new McQueContent("A sample question",
													 new Integer(1),
													 mcContent,
													 new HashSet(),
													 new HashSet()
    												);
    	mcQueContentDAO.saveOrUpdateMcQueContent(mcQueContent);
    	 
    	McQueContent mcQueContent1 = mcQueContentDAO.getMcQueContentByUID(new Long(1));
     	McOptsContent mcOptionsContent= new McOptsContent( true, "sample answer 1", mcQueContent1, new HashSet());
     	mcOptionsContentDAO.saveMcOptionsContent(mcOptionsContent);
     	
     	McOptsContent mcOptionsContent2= new McOptsContent(false, "sample answer 2", mcQueContent1, new HashSet());
     	mcOptionsContentDAO.saveMcOptionsContent(mcOptionsContent2);
     	
     	McOptsContent mcOptionsContent3= new McOptsContent(false, "sample answer 3", mcQueContent1, new HashSet());
     	mcOptionsContentDAO.saveMcOptionsContent(mcOptionsContent3);
    }
    
}
    
