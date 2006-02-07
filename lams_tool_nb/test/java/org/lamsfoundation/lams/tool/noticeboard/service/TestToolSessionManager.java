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
 * Created on Jul 8, 2005
 *
 */
package org.lamsfoundation.lams.tool.noticeboard.service;
import org.lamsfoundation.lams.tool.noticeboard.NbDataAccessTestCase;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.SessionDataExistsException;

import org.lamsfoundation.lams.tool.noticeboard.NoticeboardConstants;

/**
 * @author mtruong
 *
 * *TODO: the default content id is now not hard coded and taken from the database, ensure the test cases stillw ork
 */
public class TestToolSessionManager extends NbDataAccessTestCase {
    
    private ToolSessionManager nbSessionManager = null;
    private INoticeboardService nbService = null;
    private boolean cleanContentData = true;
    private static final Long NEW_SESSION_ID = new Long(3400);
    private static final String NEW_SESSION_NAME = "SessionName";
    
    public TestToolSessionManager(String name)
	{
		super(name);
	}
    
   protected void setUp() throws Exception
	{
		super.setUp();
		//setup some data
		this.nbContent = null;
		this.nbSession = null;
		nbSessionManager = (ToolSessionManager)this.context.getBean("nbService");
		nbService = (INoticeboardService)this.context.getBean("nbService");
		initNbContentData();
	    initNbSessionContent();
	}
	
	protected void tearDown() throws Exception
	{
		//delete data
		if(cleanContentData)
        {
        	cleanNbContentData(TEST_NB_ID);
        }
	
     }
	
	/* Normal case: with valid toolSessionId and toolContentId */
	
	public void testCreateToolSession() throws ToolException
	{
	    try
	    {
	        nbSessionManager.createToolSession(NEW_SESSION_ID,NEW_SESSION_NAME, TEST_NB_ID);
		    
		    nbSession = nbService.retrieveNoticeboardSession(NEW_SESSION_ID);
		    
		    assertEquals(nbSession.getNbSessionId(), NEW_SESSION_ID);
		    assertEquals(nbSession.getNbContent().getNbContentId(), TEST_NB_ID);
	        
	    }
	    catch (ToolException e)
	    {
	        fail("An exception should not have been thrown");
	        assertTrue(false);
	    }
	   
	}

	/*
	 * Error case: the supplied toolSessionId is null
	 */
	public void testCreateToolSessionWithNullSessionId() throws ToolException
	{
	    try
	    {
	        nbSessionManager.createToolSession(null, NEW_SESSION_NAME,TEST_NB_ID);
	        fail("An exception should be raised as the toolSessionId is null");
	    }
	    catch(ToolException e)
	    {
	        assertTrue(true);
	    }
	}
	
	/*
	 * Error case: the supplied toolContentId is null
	 */
	public void testCreateToolSessionWithNullContentId() throws ToolException
	{
	    try
	    {
	        nbSessionManager.createToolSession(NEW_SESSION_ID,NEW_SESSION_NAME,  null);
	        fail("An exception should be raised as the toolContentId is null");
	    }
	    catch(ToolException e)
	    {
	        assertTrue(true);
	    }
	}
	
	/*
	 * Alternative case: the supplied toolSessionId is invalid (has no content)
	 */
	
	public void testCreateToolSessionWithInvalidContentId() throws ToolException
	{
	    Long invalidId = new Long(8968);
	    try
	    {
	        nbSessionManager.createToolSession(NEW_SESSION_ID, NEW_SESSION_NAME, invalidId);	    
		    
	        nbSession = nbService.retrieveNoticeboardSession(NEW_SESSION_ID);
		    
		    assertEquals("validating session id:", nbSession.getNbSessionId(), NEW_SESSION_ID);
		    assertEquals("Validating content Id:", nbSession.getNbContent().getNbContentId(), nbService.getToolDefaultContentIdBySignature(NoticeboardConstants.TOOL_SIGNATURE));
		    assertContentEqualsDefaultData(nbSession.getNbContent());
	    }
	    catch (ToolException e)
	    {
	        fail("An exception should not have been thrown");
	        assertTrue(false);
	    }
	} 
	/*
	 * Error case: the default content is missing, throws ToolException
	 */
	
	public void testCreateToolSessionDefaultContentMissing() throws ToolException
	{
		Long defaultContentId = nbService.getToolDefaultContentIdBySignature(NoticeboardConstants.TOOL_SIGNATURE);
	    nbService.removeNoticeboard(defaultContentId);
	    
	    try
	    {
	        nbSessionManager.createToolSession(NEW_SESSION_ID, NEW_SESSION_NAME, null);
	        fail("An exception should be raised since the toolContentId"
	                + "is null and the defaultContent is missing");
	    }
	    catch (ToolException e)
	    {
	        assertTrue(true);
	    }
	    
	    restoreDefaultContent(defaultContentId);
	} 
	
	/*
	 * Normal case: 
	 */
	public void testRemoveToolSession() throws ToolException, DataMissingException
	{
	    try
	    {
	        nbSessionManager.removeToolSession(TEST_SESSION_ID);
	        
	        nbSession = nbService.retrieveNoticeboardSession(TEST_SESSION_ID);
	        assertNull(nbSession);
	    }
	    catch(DataMissingException e)
	    {
	        fail("An exception should not have been thrown");
	        assertTrue(false);
	    }
	    catch(ToolException e)
	    {
	        fail("An exception should not have been thrown");
	        assertTrue(false);
	    }
	}
	
	public void testRemoveToolSessionWithSessionIdNull() throws ToolException, DataMissingException
	{
	    try
	    {
	        nbSessionManager.removeToolSession(null);
	        fail("An exception should have been raised as the session id is null");
	    }
	    catch(ToolException e)
	    {
	        assertTrue(true);
	    }
	}
	
	public void testRemoveToolSessionWithInvalidId() throws ToolException, DataMissingException//no session data
	{
	    try
	    {
	        Long invalidId = new Long(878);
	        nbSessionManager.removeToolSession(invalidId);
	        fail("An exception should have been raised as there is no corresponding session data");
	    }
	    catch(DataMissingException e)
	    {
	        assertTrue(true);
	    }
	}
}
