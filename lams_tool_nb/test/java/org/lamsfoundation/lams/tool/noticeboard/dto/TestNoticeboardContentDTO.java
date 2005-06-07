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
 * Created on Jun 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.noticeboard.dto;

import java.util.TreeSet;

import org.lamsfoundation.lams.tool.noticeboard.NbDataAccessTestCase;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;
import org.lamsfoundation.lams.tool.noticeboard.dto.NoticeboardContentDTO;

/**
 * @author mtruong
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestNoticeboardContentDTO extends NbDataAccessTestCase {

	public TestNoticeboardContentDTO(String name)
	{
		super(name);
	}
	
	public void testFullConstructor()
	{
		NoticeboardContentDTO nbDTO = new NoticeboardContentDTO(TEST_NB_ID,
																TEST_TITLE,
																TEST_CONTENT,
																TEST_ONLINE_INSTRUCTIONS,
																TEST_OFFLINE_INSTRUCTIONS);
		
		assertEquals(nbDTO.getContentId(), TEST_NB_ID);
		assertEquals(nbDTO.getTitle(), TEST_TITLE);
		assertEquals(nbDTO.getContent(), TEST_CONTENT);
		assertEquals(nbDTO.getOnlineInstructions(), TEST_ONLINE_INSTRUCTIONS);
		assertEquals(nbDTO.getOfflineInstructions(), TEST_OFFLINE_INSTRUCTIONS);
	}
	
	public void testOtherConstructor()
	{
		NoticeboardContent nbContent = new NoticeboardContent(TEST_NB_ID,	
				TEST_TITLE,
				TEST_CONTENT,
				TEST_ONLINE_INSTRUCTIONS,
				TEST_OFFLINE_INSTRUCTIONS,
				TEST_DEFINE_LATER,
				TEST_FORCE_OFFLINE,
				TEST_CREATOR_USER_ID,
				TEST_DATE_CREATED,
				TEST_DATE_UPDATED,
				new TreeSet());
		
		NoticeboardContentDTO nbDTO = new NoticeboardContentDTO(nbContent);
		
		assertEquals(nbDTO.getContentId(), TEST_NB_ID);
		assertEquals(nbDTO.getTitle(), TEST_TITLE);
		assertEquals(nbDTO.getContent(), TEST_CONTENT);
		assertEquals(nbDTO.getOnlineInstructions(), TEST_ONLINE_INSTRUCTIONS);
		assertEquals(nbDTO.getOfflineInstructions(), TEST_OFFLINE_INSTRUCTIONS);
	}
}
