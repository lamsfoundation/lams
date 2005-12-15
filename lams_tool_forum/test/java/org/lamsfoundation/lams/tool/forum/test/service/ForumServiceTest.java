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
package org.lamsfoundation.lams.tool.forum.test.service;

import java.util.List;

import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.forum.persistence.Forum;
import org.lamsfoundation.lams.tool.forum.persistence.ForumToolSession;
import org.lamsfoundation.lams.tool.forum.persistence.ForumUser;
import org.lamsfoundation.lams.tool.forum.persistence.Message;
import org.lamsfoundation.lams.tool.forum.service.IForumService;
import org.lamsfoundation.lams.tool.forum.test.BaseTest;
import org.lamsfoundation.lams.tool.forum.test.TestUtils;

public class ForumServiceTest extends BaseTest{
	private IForumService forumService;
	private ToolContentManager contentManager;
	private ToolSessionManager sessionManager;
	
	public ForumServiceTest(String name) {
		super(name);
	}
	public void setUp()throws Exception{
		super.setUp();
		forumService = (IForumService)context.getBean("forumService");
		contentManager = (ToolContentManager) forumService;
		sessionManager = (ToolSessionManager) forumService;
	}
	public void testUpdateForum(){
		Forum forum = TestUtils.getForumA();
		ForumUser user = forum.getCreatedBy();
		
		forumService.updateForum(forum);
		//get back
		Forum tForum = forumService.getForum(new Long(1));
		assertEquals(tForum.getContentId(),new Long(1));
		assertEquals(tForum.getCreatedBy(),user);
	}
	public void testCreateRootTopic(){
		Message msg = TestUtils.getMessageA();
		forumService.createRootTopic(new Long(1),new Long(1),msg);
		Message tMsg = forumService.getMessage(msg.getUid());
		assertFalse(tMsg.getUpdated().equals(msg.getUpdated()));
		tMsg.setUpdated(msg.getUpdated());
		assertEquals(tMsg,msg);
	}
	public void testUpdateTopic(){
		
		Message msg = TestUtils.getMessageA();
		Message tMsg = forumService.updateTopic(msg);
		//update date will be different
		assertFalse(tMsg.getUpdated().equals(msg.getUpdated()));
		tMsg.setUpdated(msg.getUpdated());
		assertEquals(tMsg,msg);
		
		//remove test data
		forumService.deleteTopic(msg.getUid());
	}
	public void testReplyTopic(){
		Message parent = TestUtils.getMessageA();
		forumService.updateTopic(parent);
		
		Message msg = TestUtils.getMessageB();
		ForumToolSession session = TestUtils.getSessionA();
		forumService.updateSession(session);
		Message tMsg = forumService.replyTopic(parent.getUid(),session.getUid(),msg);
		
		//update date will be different
		assertFalse(tMsg.getUpdated().equals(msg.getUpdated()));
		tMsg.setUpdated(msg.getUpdated());
		assertEquals(tMsg,msg);
		
//		remove test data
		forumService.deleteTopic(parent.getUid());
	}
	public void testDeleteTopic(){
		Message parent = TestUtils.getMessageA();
		forumService.updateTopic(parent);
		
		Message msg = TestUtils.getMessageB();
		ForumToolSession session = TestUtils.getSessionA();
		forumService.updateSession(session);
		forumService.replyTopic(parent.getUid(),session.getUid(),msg);
		
		//delete parent and its children.
		forumService.deleteTopic(parent.getUid());
		
		assertNull(forumService.getMessage(parent.getUid()));
		assertNull(forumService.getMessage(msg.getUid()));
		
	}
	public void testGetTopicThread(){
		Message parent = TestUtils.getMessageA();
		ForumToolSession sessionA = TestUtils.getSessionA();
		forumService.updateSession(sessionA);
		parent.setToolSession(sessionA);
		forumService.updateTopic(parent);
		
		Message msg = TestUtils.getMessageB();
		forumService.replyTopic(parent.getUid(),sessionA.getUid(),msg);
		
		List list = forumService.getRootTopics(sessionA.getUid());
		
		assertEquals(list.size(),1);
		assertEquals(list.get(0),parent);
		
		//delete parent and its children.
		forumService.deleteTopic(parent.getUid());
		
	}
	public void testUpdateSession() throws DataMissingException, ToolException{
		ForumToolSession sessionA = TestUtils.getSessionA();
		forumService.updateSession(sessionA);
		ForumToolSession session = TestUtils.getSessionA();
		assertEquals(session,sessionA);
		
		//remove test data.
		sessionManager.removeToolSession(sessionA.getSessionId());
	}
	public void testCreateUser(){
		ForumUser userA = TestUtils.getUserA();
		forumService.createUser(userA);
		ForumUser user = TestUtils.getUserA();
		assertEquals(userA,user);
	}
}
