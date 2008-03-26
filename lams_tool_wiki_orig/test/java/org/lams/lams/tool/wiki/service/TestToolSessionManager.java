/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0 
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $$Id$$ */
package org.lams.lams.tool.wiki.service;
import org.lams.lams.tool.wiki.WikiDataAccessTestCase;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lams.lams.tool.wiki.service.IWikiService;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.SessionDataExistsException;

import org.lams.lams.tool.wiki.WikiConstants;

/**
 * @author mtruong
 *
 * *TODO: the default content id is now not hard coded and taken from the database, ensure the test cases stillw ork
 */
public class TestToolSessionManager extends WikiDataAccessTestCase {
    
    private ToolSessionManager wikiSessionManager = null;
    private IWikiService wikiService = null;
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
		this.wikiContent = null;
		this.wikiSession = null;
		wikiSessionManager = (ToolSessionManager)this.context.getBean("wikiService");
		wikiService = (IWikiService)this.context.getBean("wikiService");
		initWikiContentData();
	    initWikiSessionContent();
	}
	
	protected void tearDown() throws Exception
	{
		//delete data
		if(cleanContentData)
        {
        	cleanWikiContentData(TEST_NB_ID);
        }
	
     }
	
	/* Normal case: with valid toolSessionId and toolContentId */
	
	public void testCreateToolSession() throws ToolException
	{
	    try
	    {
	        wikiSessionManager.createToolSession(NEW_SESSION_ID,NEW_SESSION_NAME, TEST_NB_ID);
		    
		    wikiSession = wikiService.retrieveWikiSession(NEW_SESSION_ID);
		    
		    assertEquals(wikiSession.getWikiSessionId(), NEW_SESSION_ID);
		    assertEquals(wikiSession.getWikiContent().getWikiContentId(), TEST_NB_ID);
	        
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
	        wikiSessionManager.createToolSession(null, NEW_SESSION_NAME,TEST_NB_ID);
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
	        wikiSessionManager.createToolSession(NEW_SESSION_ID,NEW_SESSION_NAME,  null);
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
	        wikiSessionManager.createToolSession(NEW_SESSION_ID, NEW_SESSION_NAME, invalidId);	    
		    
	        wikiSession = wikiService.retrieveWikiSession(NEW_SESSION_ID);
		    
		    assertEquals("validating session id:", wikiSession.getWikiSessionId(), NEW_SESSION_ID);
		    assertEquals("Validating content Id:", wikiSession.getWikiContent().getWikiContentId(), wikiService.getToolDefaultContentIdBySignature(WikiConstants.TOOL_SIGNATURE));
		    assertContentEqualsDefaultData(wikiSession.getWikiContent());
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
		Long defaultContentId = wikiService.getToolDefaultContentIdBySignature(WikiConstants.TOOL_SIGNATURE);
	    wikiService.removeWiki(defaultContentId);
	    
	    try
	    {
	        wikiSessionManager.createToolSession(NEW_SESSION_ID, NEW_SESSION_NAME, null);
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
	        wikiSessionManager.removeToolSession(TEST_SESSION_ID);
	        
	        wikiSession = wikiService.retrieveWikiSession(TEST_SESSION_ID);
	        assertNull(wikiSession);
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
	        wikiSessionManager.removeToolSession(null);
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
	        wikiSessionManager.removeToolSession(invalidId);
	        fail("An exception should have been raised as there is no corresponding session data");
	    }
	    catch(DataMissingException e)
	    {
	        assertTrue(true);
	    }
	}
}
