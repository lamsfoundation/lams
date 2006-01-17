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
import org.lamsfoundation.lams.tool.forum.dto.MessageDTO;
import org.lamsfoundation.lams.tool.forum.persistence.Forum;
import org.lamsfoundation.lams.tool.forum.persistence.ForumToolSession;
import org.lamsfoundation.lams.tool.forum.persistence.ForumUser;
import org.lamsfoundation.lams.tool.forum.persistence.Message;
import org.lamsfoundation.lams.tool.forum.service.IForumService;
import org.lamsfoundation.lams.tool.forum.test.DAOBaseTest;
import org.lamsfoundation.lams.tool.forum.test.TestUtils;

public class ForumServiceTest extends DAOBaseTest{
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
		ForumUser user = TestUtils.getUserA();
		forumService.createUser(user);
		
		Forum forum = TestUtils.getForumA();
		forum.setCreatedBy(user);
		forumService.updateForum(forum);
		
		//get back
		Forum tForum = forumService.getForum(forum.getUid());
		assertEquals(tForum.getContentId(),new Long(1));
		assertEquals(tForum.getCreatedBy(),user);
		
		//remove test data
		forumDao.delete(forum);
		forumUserDao.delete(user);
	}
	public void testCreateRootTopic(){
		Forum forum = TestUtils.getForumA();
		forumDao.saveOrUpdate(forum);
		ForumToolSession session = TestUtils.getSessionA();
		session.setForum(forum);
		forumToolSessionDao.saveOrUpdate(session);
		
		Message msg = TestUtils.getMessageA();
		msg.setForum(forum);
		
		forumService.createRootTopic(forum.getUid(),session.getSessionId(),msg);
		Message tMsg = forumService.getMessage(msg.getUid());
		tMsg.setUpdated(msg.getUpdated());
		assertEquals(tMsg,msg);

		//remove test data
		forumService.deleteTopic(msg.getUid());
		forumToolSessionDao.delete(session);
		forumDao.delete(forum);
	}
	public void testUpdateTopic(){
		Forum forum = TestUtils.getForumA();
		forumDao.saveOrUpdate(forum);
		ForumToolSession session = TestUtils.getSessionA();
		session.setForum(forum);
		forumToolSessionDao.saveOrUpdate(session);
		
		Message msg = TestUtils.getMessageA();
		forumService.createRootTopic(forum.getUid(),session.getSessionId(),msg);
		
		msg.setBody("update");
		Message tMsg = forumService.updateTopic(msg);
		//update date will be different
		tMsg.setUpdated(msg.getUpdated());
		tMsg.setBody("update");
		assertEquals(tMsg,msg);
		
		//remove test data
		forumService.deleteTopic(msg.getUid());
		forumToolSessionDao.delete(session);
		forumDao.delete(forum);
	}
	public void testReplyTopic(){
		Forum forum = TestUtils.getForumA();
		forumDao.saveOrUpdate(forum);
		ForumToolSession session = TestUtils.getSessionA();
		session.setForum(forum);
		forumToolSessionDao.saveOrUpdate(session);
		ForumUser user = TestUtils.getUserA();
		forumUserDao.save(user);
		
		Message parent = TestUtils.getMessageA();
		parent.setCreatedBy(user);
		forumService.createRootTopic(forum.getUid(),session.getSessionId(),parent);
		
		Message msg = TestUtils.getMessageB();
		msg.setCreatedBy(user);
		Message tMsg = forumService.replyTopic(parent.getUid(),session.getSessionId(),msg);
		List list = forumService.getTopicThread(parent.getUid());
		Message child = ((MessageDTO) list.get(1)).getMessage();
		assertEquals(child,tMsg);
		
//		remove test data
		forumService.deleteTopic(parent.getUid());
		forumToolSessionDao.delete(session);
		forumDao.delete(forum);
		forumUserDao.delete(user);

	}
	public void testDeleteTopic(){
		Forum forum = TestUtils.getForumA();
		forumDao.saveOrUpdate(forum);
		ForumToolSession session = TestUtils.getSessionA();
		session.setForum(forum);
		forumToolSessionDao.saveOrUpdate(session);
		
		Message parent = TestUtils.getMessageA();
		forumService.createRootTopic(forum.getUid(),session.getSessionId(),parent);
		
		Message msg = TestUtils.getMessageB();
		forumService.replyTopic(parent.getUid(),session.getSessionId(),msg);
		
		//delete parent and its children.
		forumService.deleteTopic(parent.getUid());
		
		assertNull(forumService.getMessage(parent.getUid()));
		assertNull(forumService.getMessage(msg.getUid()));
		
//		remove test data
		forumToolSessionDao.delete(session);
		forumDao.delete(forum);

	}
	public void testGetTopicThread(){
		Forum forum = TestUtils.getForumA();
		forumDao.saveOrUpdate(forum);
		ForumToolSession session = TestUtils.getSessionA();
		session.setForum(forum);
		forumToolSessionDao.saveOrUpdate(session);
		ForumUser user = TestUtils.getUserA();
		forumUserDao.save(user);
		
		Message parent = TestUtils.getMessageA();
		parent.setCreatedBy(user);
		forumService.createRootTopic(forum.getUid(),session.getSessionId(),parent);
		
		Message msg = TestUtils.getMessageB();
		msg.setCreatedBy(user);
		forumService.replyTopic(parent.getUid(),session.getSessionId(),msg);
		
		List list = forumService.getRootTopics(session.getSessionId());
		
		assertEquals(1,list.size());
		assertEquals(((MessageDTO)list.get(0)).getMessage(),parent);
		
//		remove test data
		//delete parent and its children.
		forumService.deleteTopic(parent.getUid());
		forumToolSessionDao.delete(session);
		forumDao.delete(forum);
		forumUserDao.delete(user);
	}
	public void testUpdateSession() throws DataMissingException, ToolException{
		ForumToolSession sessionA = TestUtils.getSessionA();
		forumService.updateSession(sessionA);
		ForumToolSession session = forumService.getSessionBySessionId(sessionA.getSessionId());
		assertEquals(session,sessionA);
		
		//remove test data.
		sessionManager.removeToolSession(sessionA.getSessionId());
	}
	public void testCreateUser(){
		ForumUser userA = TestUtils.getUserA();
		forumService.createUser(userA);
		ForumUser user = forumService.getUser(userA.getUid());
		assertEquals(userA,user);
	}
}
