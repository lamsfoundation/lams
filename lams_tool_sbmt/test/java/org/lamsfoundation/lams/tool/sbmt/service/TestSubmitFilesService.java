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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $$Id$$ */	

package org.lamsfoundation.lams.tool.sbmt.service;

import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.SessionDataExistsException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.sbmt.SbmtBaseTestCase;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesContent;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesSession;
import org.lamsfoundation.lams.tool.sbmt.dao.ISubmissionDetailsDAO;
import org.lamsfoundation.lams.tool.sbmt.dao.ISubmitFilesContentDAO;
import org.lamsfoundation.lams.tool.sbmt.dao.ISubmitFilesSessionDAO;

/**
 * Note: this test sets up tool content ids and tool session ids that 
 * are invalid with respect to the core tables.
 * 
 * @author Manpreet Minhas, Fiona Malikoff
 */
public class TestSubmitFilesService extends SbmtBaseTestCase {
	
	protected ISubmitFilesService submitFilesService;
	protected ToolContentManager submitFilesToolContentManager;
	protected ToolSessionManager submitFilesToolSessionManager;
	
	protected ISubmitFilesSessionDAO submitFilesSessionDAO  = null;
	protected ISubmitFilesContentDAO submitFilesContentDAO  = null;
	protected ISubmissionDetailsDAO submissionDetailsDAO;
	
	protected SubmitFilesContent submitFilesContent;
	
	public TestSubmitFilesService(String name){
		super(name);
	}

	public void setUp()throws Exception{
		super.setUp();
		submitFilesService = (ISubmitFilesService)context.getBean("submitFilesService");
		submitFilesToolContentManager = (ToolContentManager) submitFilesService;
		submitFilesToolSessionManager = (ToolSessionManager) submitFilesService;

		submitFilesSessionDAO = (ISubmitFilesSessionDAO)context.getBean("submitFilesSessionDAO");
		submitFilesContentDAO = (ISubmitFilesContentDAO)context.getBean("submitFilesContentDAO");
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
	}*/
	
	public void testCopyContent(){
		Long id = copyTestContent();
	}
	
	private Long copyTestContent() {
		
		Long newContentId = new Long(getMaxContentId() + 1);
		try {
			submitFilesToolContentManager.copyToolContent(TEST_CONTENT_ID, newContentId);
		} catch (ToolException e) {
			e.printStackTrace();
			fail("Tool exception thrown copying the content");
		}
		return newContentId;
	}

	public void testCreateSession() {
		createSession(copyTestContent());
	}
	
	private SubmitFilesSession createSession(Long toolContentId) {
		Long toolSessionId = new Long(getMaxSessionId()+1);

		try {
			submitFilesToolSessionManager.createToolSession(toolSessionId,"sessionName",toolContentId);
		} catch (ToolException e) {
			e.printStackTrace();
			fail("Tool exception thrown while creating session");
		}
		SubmitFilesSession submitFilesSession = submitFilesSessionDAO.getSessionByID(toolSessionId);
		assertNotNull("submitFilesSession", submitFilesSession);
		assertEquals(toolSessionId, submitFilesSession.getSessionID());
		return submitFilesSession;
	}
	
	/** Tests removing content where session data exists */ 
	public void testRemoveToolContentSessionDataExists(){
		
		Long toolContentId = copyTestContent();
		SubmitFilesSession submitFilesSession = createSession(toolContentId);
		Long sessionContentId = submitFilesSession.getSessionID();

		// Remove without removing the session data - should fail. 
		try {
			submitFilesToolContentManager.removeToolContent(toolContentId, false);
			fail("Expected SessionDataExistsException to be thrown, toolSessionId = "
					+sessionContentId);
		} catch (SessionDataExistsException e) {
			assertTrue("SessionDataExistsException thrown as expected", true);
		} catch (ToolException e) {
			e.printStackTrace();
			fail("Tool exception thrown deleting the content toolContentId="+toolContentId);
		}

		// Records should still exist
		SubmitFilesContent content = submitFilesContentDAO.getContentByID(toolContentId);
		assertNotNull("Tool content data", content);

		SubmitFilesSession session = submitFilesSessionDAO.getSessionByID(sessionContentId);
		assertNotNull("Tool session data", session);

		// This time remove the session data too! 
		try {
			submitFilesToolContentManager.removeToolContent(toolContentId, true);
		} catch (SessionDataExistsException e) {
			e.printStackTrace();
			fail("SessionDataExistsException thrown deleting the content toolContentId="+toolContentId);
		} catch (ToolException e) {
			e.printStackTrace();
			fail("Tool exception thrown deleting the content toolContentId="+toolContentId);
		}

		SubmitFilesContent content2 = submitFilesContentDAO.getContentByID(toolContentId);
		assertNull("Tool Content has been deleted as expected", content2);

		SubmitFilesSession session2 = submitFilesSessionDAO.getSessionByID(sessionContentId);
		assertNull("Tool session has been deleted as expected", session2);
		
	}

	/** Tests removing content where session data does not exist */ 
	public void testRemoveToolContentNoSessionData(){
		
		Long toolContentId = copyTestContent();

		// Remove without removing the session data - should work. 
		try {
			submitFilesToolContentManager.removeToolContent(toolContentId, true);
		} catch (SessionDataExistsException e) {
			e.printStackTrace();
			fail("SessionDataExistsException thrown deleting the content toolContentId="+toolContentId);
		} catch (ToolException e) {
			e.printStackTrace();
			fail("Tool exception thrown deleting the content toolContentId="+toolContentId);
		}

		SubmitFilesContent content2 = submitFilesContentDAO.getContentByID(toolContentId);
		assertNull("Tool Content has been deleted as expected", content2);

	}

	/*	public void testGenerateReport(){
		Hashtable table = submitFilesService.generateReport(new Long(1));
		assertEquals(table.size(),3);
	}*/

}
 