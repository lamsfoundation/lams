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

package org.lamsfoundation.lams.tool.sbmt.dao;

import org.lamsfoundation.lams.tool.sbmt.SbmtBaseTestCase;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesContent;

/**
 * @author Manpreet Minhas
 */
public class TestSubmitFilesContentDAO extends SbmtBaseTestCase {
	
	protected SubmitFilesContent submitFilesContent;
	protected ISubmitFilesContentDAO submitFilesContentDAO;
	
	public TestSubmitFilesContentDAO(String name){
		super(name);
	}

	public void setUp()throws Exception{
		super.setUp();
		submitFilesContentDAO = (ISubmitFilesContentDAO)context.getBean("submitFilesContentDAO");
	}
	
	public void testAddSubmitFilesContent() throws Exception {
		long newId = getMaxContentId() + 1;
		String title = "Trial Content";
		String instructions = "Trial Instructions";
		submitFilesContent = new SubmitFilesContent(new Long(newId),title,instructions);
		submitFilesContentDAO.insert(submitFilesContent);
		Long contentId = submitFilesContent.getContentID();
		assertNotNull(contentId);
		assertEquals(contentId.longValue(), newId);
	}

	public void testAddGetContentByID() throws Exception {
		submitFilesContent = submitFilesContentDAO.getContentByID(TEST_CONTENT_ID);
		assertEquals(submitFilesContent.getTitle(), TEST_CONTENT_TITLE);
		assertEquals(submitFilesContent.getInstruction(), TEST_CONTENT_INSTRUCTIONS);
	}

}
