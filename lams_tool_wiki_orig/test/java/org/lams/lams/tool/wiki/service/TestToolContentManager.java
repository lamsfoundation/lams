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
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lams.lams.tool.wiki.service.IWikiService;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.SessionDataExistsException;

import org.lams.lams.tool.wiki.WikiConstants;

/**
 * @author mtruong
 *
 *TODO: the default content id is now not hard coded and taken from the database, ensure the test cases stillw ork
 */
public class TestToolContentManager extends WikiDataAccessTestCase {
	
	private ToolContentManager wikiContentManager = null;
	//private IWikiContentDAO wikiContentDAO = null;
	//private WikiContent wiki;
	private IWikiService wikiService = null;
	
	private boolean cleanContentData = true;
	private boolean cleanCopyContent = false;
	
	
	public TestToolContentManager(String name)
	{
		super(name);
	}
	
	protected void setUp() throws Exception
	{
		super.setUp();
		//setup some data
		this.wikiContent = null;
		this.wikiSession = null;
		wikiContentManager = (ToolContentManager)this.context.getBean("wikiService");
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
       
	     
		 if(cleanCopyContent)
	     {
	     	cleanWikiContentData(TEST_COPYNB_ID);
	     }
     }
	
	/*
	 * Normal Use case
	 */
	public void testcopyToolContent() throws ToolException
	{
		//ensure that the copied data is deleted after use
		cleanCopyContent = true;
		
		wikiContentManager.copyToolContent(TEST_NB_ID, TEST_COPYNB_ID);
		
		wikiContent = wikiService.retrieveWiki(TEST_COPYNB_ID);
		
		// check whether this new object contains the right content
		assertEquals(wikiContent.getWikiContentId(), TEST_COPYNB_ID);
	    	
		assertContentEqualsTestData(wikiContent);
	} 
	
	/*
	 * Error case: toContentId not supplied
	 */
	
	public void testCopyToolContentWithToContentIdNull() throws ToolException
	{
	    cleanCopyContent = true;
	    try
	    {
	        wikiContentManager.copyToolContent(TEST_NB_ID, null);
	       fail("A ToolException should have been raised as toContentId is missing");
	    }
	    catch(ToolException e)
	    {
	        assertTrue(true);
	    }
	}
	
	/*
	 * Alternative case: the fromContentId supplied is null, default content is used.
	 */
	public void testCopyToolContentWithFromContentIdNull() throws ToolException
	{
	    cleanCopyContent = true;
	    try
	    {
	        wikiContentManager.copyToolContent(null, TEST_COPYNB_ID);
	        
	        wikiContent = wikiService.retrieveWiki(TEST_COPYNB_ID);
	        assertEquals(wikiContent.getWikiContentId(), TEST_COPYNB_ID);
	        assertContentEqualsDefaultData(wikiContent);
	        
	        
	    }
	    catch(ToolException e)
	    {
	        assertFalse(false); //an exception should not have been thrown since default content is there
	    }
	}
	
	/*
	 * Alternative case: the fromContentId supplied does not exist in the db, default content is used.
	 */
	public void testCopyToolContentWithInvalidFromContentId() throws ToolException
	{
	    cleanCopyContent = true;
	    try
	    {
	        wikiContentManager.copyToolContent(new Long(76), TEST_COPYNB_ID);
	        
	        wikiContent = wikiService.retrieveWiki(TEST_COPYNB_ID);
	        assertEquals(wikiContent.getWikiContentId(), TEST_COPYNB_ID);
	        assertContentEqualsDefaultData(wikiContent);
	    }
	    catch(ToolException e)
	    {
	        assertFalse(true); //an exception should not have been thrown since default content is there
	    }
	}
	
	/*
	 * Error case: Default content is missing, throws DataMissingException
	 */
	public void testCopyToolContentWithDefaultContentMissing() throws ToolException
	{
	   // cleanCopyContent = true;
		Long defaultContentId = wikiService.getToolDefaultContentIdBySignature(WikiConstants.TOOL_SIGNATURE);
			    
	    //remove the default content so it will trigger the exception
	    wikiService.removeWiki(defaultContentId);
	    
	    try
	    {
	        wikiContentManager.copyToolContent(null, TEST_COPYNB_ID);
	        fail("A ToolException should have been raised as the fromContentId is missing" +
	        		" and default content is missing.");
	     
	    }
	    catch(ToolException e)
	    {
	        assertTrue(true); //an exception should not have been thrown since default content is there
	    }
	    
	    restoreDefaultContent(defaultContentId);
	} 
	
	/*
	 * Normal case: 
	 */
	
	public void testSetAsDefineLater() throws ToolException, DataMissingException
	{
	    wikiContentManager.setAsDefineLater(TEST_NB_ID);
		
		wikiContent = wikiService.retrieveWiki(TEST_NB_ID);
		
		assertTrue(wikiContent.isDefineLater());
	}
	
	/*
	 * Error case: supplied parameter toolContentId is null, ToolException is thrown
	 */
	public void testSetAsDefineLaterWithIdNull() throws ToolException, DataMissingException
	{
	    try
	    {
	        wikiContentManager.setAsDefineLater(null);
	        fail("A ToolException should have been raised as the toolContentId is null");
	    }
	    catch(ToolException e)
	    {
	        assertTrue(true);
	    }
	}
	
	/*
	 * Error case: The tool content is missing for the supplied toolContentId, throws DataMissingException
	 */
	public void testSetAsDefineLaterWithInvalidId() throws ToolException, DataMissingException
	{
	    try
	    {
	        Long idWithNoContent = new Long(8767);
	        wikiContentManager.setAsDefineLater(idWithNoContent);
	        fail("A ToolException should have been raised as the tool content is missing for the supplied toolContentId: "+idWithNoContent);
	    }
	    catch(DataMissingException e)
	    {
	        assertTrue(true);
	    }
	}
	
	/*
	 * Normal case for: setAsRunOffline(Long toolContentId)
	 */
	
	public void testsetAsRunOffline() throws DataMissingException, ToolException
	{
		wikiContentManager.setAsRunOffline(TEST_NB_ID);
		wikiContent = wikiService.retrieveWiki(TEST_NB_ID);
		assertTrue(wikiContent.isForceOffline());
	}
	
	/*
	 * Error case: supplied parameter toolContentId is null, ToolException is thrown
	 */
	public void testSetAsRunOfflineWithIdNull() throws ToolException, DataMissingException
	{
	    try
	    {
	        wikiContentManager.setAsRunOffline(null);
	        fail("A ToolException should have been raised as the toolContentId is null");
	    }
	    catch(ToolException e)
	    {
	        assertTrue(true);
	    }
	}
	/*
	 * Error case: The tool content is missing for the supplied toolContentId, throws DataMissingException
	 */
	
	public void testSetAsRunOfflineWithInvalidId() throws ToolException, DataMissingException
	{
	    try
	    {
	        Long idWithNoContent = new Long(8767);
	        wikiContentManager.setAsRunOffline(idWithNoContent);
	        fail("A ToolException should have been raised as the tool content is missing for the supplied toolContentId: "+idWithNoContent);
	    }
	    catch(DataMissingException e)
	    {
	        assertTrue(true);
	    }
	}
	
	/*
	 * Normal case:
	 * @author mtruong
	 */
	
	public void testRemoveToolContent() throws ToolException, SessionDataExistsException
	{
	    cleanContentData = false;
	    
		wikiContentManager.removeToolContent(TEST_NB_ID, true);		
		wikiContent = wikiService.retrieveWiki(TEST_NB_ID);
		assertNull(wikiContent);
	}
	
	/*
	 * Error case: toolContentId is null, throws ToolException
	 */
	public void testRemoveToolContentWithIdNull() throws ToolException, SessionDataExistsException
	{
	    cleanContentData = true;
	    try
	    {
	        wikiContentManager.removeToolContent(null, true);
	        fail("An exception should be raised as the supplied toolContentId is null");
	    }
	    catch (ToolException e)
	    {
	        assertTrue(true);
	    }
	}
	
	/*
	 * Error case: toolContentId is invalid (no content is defined for that id), throws DataMissingException
	 */
	public void testRemoveToolContentWithInvalidId() throws ToolException, SessionDataExistsException
	{
	    cleanContentData = true;
	    try
	    {
	        Long nonExistentId = new Long(89879);
	        wikiContentManager.removeToolContent(nonExistentId, true);
	        fail("An exception should be raised as the supplied toolContentId does not have content data");
	    }
	    catch (DataMissingException e)
	    {
	        assertTrue(true);
	    }
	}
	
	/*
	 * Exception case: session data exists, and removeSessionData flag is set to false, throws SessionDataExistsException
	 */
	
	public void testRemoveToolContentWithFlagSetToFalseAndSessionDataExists() throws ToolException, SessionDataExistsException
	{
	    cleanContentData = true;
	    try
	    {
	        wikiContentManager.removeToolContent(TEST_NB_ID, false);
	        fail("An exception should be raised as session data exists, but removeSessionData is false. Cannot Continue to remove tool content");
	    }
	    catch(SessionDataExistsException e)
	    {
	        assertTrue(true);
	    }
	}
	
	/*
	 * Alternative case: the flag removeSessionData=false, but no session data exists, return without throwing exception
	 */
	public void testRemoveToolContentWithFlagSetToFalseAndNoSessionData() throws ToolException, SessionDataExistsException
	{
	    cleanContentData = false;
	    
	    //remove session data
	    wikiContent = wikiService.retrieveWiki(TEST_NB_ID);
	    wikiContent.getWikiSessions().clear();
	    wikiService.saveWiki(wikiContent);
	    
	    wikiContentManager.removeToolContent(TEST_NB_ID, false);
	    
	    wikiContent = wikiService.retrieveWiki(TEST_NB_ID);
		assertNull(wikiContent);
  
	}
	
}
