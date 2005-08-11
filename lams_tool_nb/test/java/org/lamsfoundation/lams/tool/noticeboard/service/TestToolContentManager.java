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
 * Created on May 24, 2005
 *
 */
package org.lamsfoundation.lams.tool.noticeboard.service;

import org.lamsfoundation.lams.tool.noticeboard.NbDataAccessTestCase;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.SessionDataExistsException;

import org.lamsfoundation.lams.tool.noticeboard.NoticeboardConstants;

/**
 * @author mtruong
 *
 *TODO: the default content id is now not hard coded and taken from the database, ensure the test cases stillw ork
 */
public class TestToolContentManager extends NbDataAccessTestCase {
	
	private ToolContentManager nbContentManager = null;
	//private INoticeboardContentDAO nbContentDAO = null;
	//private NoticeboardContent nb;
	private INoticeboardService nbService = null;
	
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
		this.nbContent = null;
		this.nbSession = null;
		nbContentManager = (ToolContentManager)this.context.getBean("nbService");
		nbService = (INoticeboardService)this.context.getBean("nbService");
		this.initNbContentData();
	    this.initNbSessionContent();
	}
	
	protected void tearDown() throws Exception
	{
		super.tearDown();
		
		//delete data
		if(cleanContentData)
        {
        	super.cleanNbContentData(TEST_NB_ID);
        }
       
	     
		 if(cleanCopyContent)
	     {
	     	super.cleanNbContentData(TEST_COPYNB_ID);
	     }
     }
	/*
	 * Normal Use case
	 */
	public void testcopyToolContent() throws ToolException
	{
		//ensure that the copied data is deleted after use
		cleanCopyContent = true;
		
		nbContentManager.copyToolContent(TEST_NB_ID, TEST_COPYNB_ID);
		
		nbContent = nbService.retrieveNoticeboard(TEST_COPYNB_ID);
		
		// check whether this new object contains the right content
		assertEquals(nbContent.getNbContentId(), TEST_COPYNB_ID);
	    	
		assertContentEqualsTestData(nbContent);
	} 
	
	/*
	 * Error case: toContentId not supplied
	 */
	
	public void testCopyToolContentWithToContentIdNull() throws ToolException
	{
	    cleanCopyContent = true;
	    try
	    {
	        nbContentManager.copyToolContent(TEST_NB_ID, null);
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
	        nbContentManager.copyToolContent(null, TEST_COPYNB_ID);
	        
	        nbContent = nbService.retrieveNoticeboard(TEST_COPYNB_ID);
	        assertEquals(nbContent.getNbContentId(), TEST_COPYNB_ID);
	        assertContentEqualsDefaultData(nbContent);
	        
	        
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
	        nbContentManager.copyToolContent(new Long(76), TEST_COPYNB_ID);
	        
	        nbContent = nbService.retrieveNoticeboard(TEST_COPYNB_ID);
	        assertEquals(nbContent.getNbContentId(), TEST_COPYNB_ID);
	        assertContentEqualsDefaultData(nbContent);
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
	    
	    //remove the default content so it will trigger the exception
	    nbService.removeNoticeboard(nbService.getToolDefaultContentIdBySignature(NoticeboardConstants.TOOL_SIGNATURE));
	    
	    try
	    {
	        nbContentManager.copyToolContent(null, TEST_COPYNB_ID);
	        fail("A ToolException should have been raised as the fromContentId is missing" +
	        		" and default content is missing.");
	     
	    }
	    catch(ToolException e)
	    {
	        assertTrue(true); //an exception should not have been thrown since default content is there
	    }
	    
	    this.restoreDefaultContent();
	}
	
	/*
	 * Normal case: 
	 */
	
	public void testSetAsDefineLater() throws ToolException, DataMissingException
	{
	    nbContentManager.setAsDefineLater(TEST_NB_ID);
		
		nbContent = nbService.retrieveNoticeboard(TEST_NB_ID);
		
		assertTrue(nbContent.isDefineLater());
	}
	
	/*
	 * Error case: supplied parameter toolContentId is null, ToolException is thrown
	 */
	public void testSetAsDefineLaterWithIdNull() throws ToolException, DataMissingException
	{
	    try
	    {
	        nbContentManager.setAsDefineLater(null);
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
	        nbContentManager.setAsDefineLater(idWithNoContent);
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
		nbContentManager.setAsRunOffline(TEST_NB_ID);
		nbContent = nbService.retrieveNoticeboard(TEST_NB_ID);
		assertTrue(nbContent.isForceOffline());
	}
	
	/*
	 * Error case: supplied parameter toolContentId is null, ToolException is thrown
	 */
	public void testSetAsRunOfflineWithIdNull() throws ToolException, DataMissingException
	{
	    try
	    {
	        nbContentManager.setAsRunOffline(null);
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
	        nbContentManager.setAsRunOffline(idWithNoContent);
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
	    
		nbContentManager.removeToolContent(TEST_NB_ID, true);		
		nbContent = nbService.retrieveNoticeboard(TEST_NB_ID);
		assertNull(nbContent);
	}
	
	/*
	 * Error case: toolContentId is null, throws ToolException
	 */
	public void testRemoveToolContentWithIdNull() throws ToolException, SessionDataExistsException
	{
	    cleanContentData = true;
	    try
	    {
	        nbContentManager.removeToolContent(null, true);
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
	        nbContentManager.removeToolContent(nonExistentId, true);
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
	        nbContentManager.removeToolContent(TEST_NB_ID, false);
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
	    nbContent = nbService.retrieveNoticeboard(TEST_NB_ID);
	    nbContent.getNbSessions().clear();
	    nbService.updateNoticeboard(nbContent);
	    
	    nbContentManager.removeToolContent(TEST_NB_ID, false);
	    
	    nbContent = nbService.retrieveNoticeboard(TEST_NB_ID);
		assertNull(nbContent);
  
	}
	
	
}
