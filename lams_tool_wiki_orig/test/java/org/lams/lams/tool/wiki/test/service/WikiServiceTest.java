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

package org.lams.lams.tool.wiki.test.service;

import java.util.List;

import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lams.lams.tool.wiki.dto.MessageDTO;
import org.lams.lams.tool.wiki.persistence.Wiki;
import org.lams.lams.tool.wiki.persistence.WikiToolSession;
import org.lams.lams.tool.wiki.persistence.WikiUser;
import org.lams.lams.tool.wiki.persistence.Message;
import org.lams.lams.tool.wiki.service.IWikiService;
import org.lams.lams.tool.wiki.test.DAOBaseTest;
import org.lams.lams.tool.wiki.test.TestUtils;

public class WikiServiceTest extends DAOBaseTest{
	private IWikiService wikiService;
	private ToolContentManager contentManager;
	private ToolSessionManager sessionManager;
	
	public WikiServiceTest(String name) {
		super(name);
	}
	public void setUp()throws Exception{
		super.setUp();
		wikiService = (IWikiService)context.getBean("wikiService");
		contentManager = (ToolContentManager) wikiService;
		sessionManager = (ToolSessionManager) wikiService;
	}
	public void testUpdateWiki(){
		WikiUser user = TestUtils.getUserA();
		wikiUserDao.save(user);
		
		Wiki wiki = TestUtils.getWikiA();
		wiki.setCreatedBy(user);
		wikiService.updateWiki(wiki);
		
		//get back
		Wiki tWiki = wikiService.getWiki(wiki.getUid());
		assertEquals(tWiki.getContentId(),new Long(1));
		assertEquals(tWiki.getCreatedBy(),user);
		
		//remove test data
		wikiDao.delete(wiki);
		wikiUserDao.delete(user);
	}
	public void testCreateRootTopic(){
		Wiki wiki = TestUtils.getWikiA();
		wikiDao.saveOrUpdate(wiki);
		WikiToolSession session = TestUtils.getSessionA();
		session.setWiki(wiki);
		wikiToolSessionDao.saveOrUpdate(session);
		
		Message msg = TestUtils.getMessageA();
		msg.setWiki(wiki);
		
		wikiService.createRootTopic(wiki.getUid(),session.getSessionId(),msg);
		Message tMsg = wikiService.getMessage(msg.getUid());
		tMsg.setUpdated(msg.getUpdated());
		assertEquals(tMsg,msg);

		//remove test data
		wikiService.deleteTopic(msg.getUid());
		wikiToolSessionDao.delete(session);
		wikiDao.delete(wiki);
	}
	public void testUpdateTopic(){
		Wiki wiki = TestUtils.getWikiA();
		wikiDao.saveOrUpdate(wiki);
		WikiToolSession session = TestUtils.getSessionA();
		session.setWiki(wiki);
		wikiToolSessionDao.saveOrUpdate(session);
		
		Message msg = TestUtils.getMessageA();
		wikiService.createRootTopic(wiki.getUid(),session.getSessionId(),msg);
		
		msg.setBody("update");
		Message tMsg = wikiService.updateTopic(msg);
		//update date will be different
		tMsg.setUpdated(msg.getUpdated());
		tMsg.setBody("update");
		assertEquals(tMsg,msg);
		
		//remove test data
		wikiService.deleteTopic(msg.getUid());
		wikiToolSessionDao.delete(session);
		wikiDao.delete(wiki);
	}
	public void testReplyTopic(){
		Wiki wiki = TestUtils.getWikiA();
		wikiDao.saveOrUpdate(wiki);
		WikiToolSession session = TestUtils.getSessionA();
		session.setWiki(wiki);
		wikiToolSessionDao.saveOrUpdate(session);
		WikiUser user = TestUtils.getUserA();
		wikiUserDao.save(user);
		
		Message parent = TestUtils.getMessageA();
		parent.setCreatedBy(user);
		wikiService.createRootTopic(wiki.getUid(),session.getSessionId(),parent);
		
		Message msg = TestUtils.getMessageB();
		msg.setCreatedBy(user);
		Message tMsg = wikiService.replyTopic(parent.getUid(),session.getSessionId(),msg);
		List list = wikiService.getTopicThread(parent.getUid());
		Message child = ((MessageDTO) list.get(1)).getMessage();
		assertEquals(child,tMsg);
		
//		remove test data
		wikiService.deleteTopic(parent.getUid());
		wikiToolSessionDao.delete(session);
		wikiDao.delete(wiki);
		wikiUserDao.delete(user);

	}
	public void testDeleteTopic(){
		Wiki wiki = TestUtils.getWikiA();
		wikiDao.saveOrUpdate(wiki);
		WikiToolSession session = TestUtils.getSessionA();
		session.setWiki(wiki);
		wikiToolSessionDao.saveOrUpdate(session);
		
		Message parent = TestUtils.getMessageA();
		wikiService.createRootTopic(wiki.getUid(),session.getSessionId(),parent);
		
		Message msg = TestUtils.getMessageB();
		wikiService.replyTopic(parent.getUid(),session.getSessionId(),msg);
		
		//delete parent and its children.
		wikiService.deleteTopic(parent.getUid());
		
		assertNull(wikiService.getMessage(parent.getUid()));
		assertNull(wikiService.getMessage(msg.getUid()));
		
//		remove test data
		wikiToolSessionDao.delete(session);
		wikiDao.delete(wiki);

	}
	public void testGetTopicThread(){
		Wiki wiki = TestUtils.getWikiA();
		wikiDao.saveOrUpdate(wiki);
		WikiToolSession session = TestUtils.getSessionA();
		session.setWiki(wiki);
		wikiToolSessionDao.saveOrUpdate(session);
		WikiUser user = TestUtils.getUserA();
		wikiUserDao.save(user);
		
		Message parent = TestUtils.getMessageA();
		parent.setCreatedBy(user);
		wikiService.createRootTopic(wiki.getUid(),session.getSessionId(),parent);
		
		Message msg = TestUtils.getMessageB();
		msg.setCreatedBy(user);
		wikiService.replyTopic(parent.getUid(),session.getSessionId(),msg);
		
		List list = wikiService.getRootTopics(session.getSessionId());
		
		assertEquals(1,list.size());
		assertEquals(((MessageDTO)list.get(0)).getMessage(),parent);
		
//		remove test data
		//delete parent and its children.
		wikiService.deleteTopic(parent.getUid());
		wikiToolSessionDao.delete(session);
		wikiDao.delete(wiki);
		wikiUserDao.delete(user);
	}

	public void testCreateUser(){
		WikiUser userA = TestUtils.getUserA();
		wikiService.createUser(userA);
		WikiUser user = wikiService.getUser(userA.getUid());
		assertEquals(userA,user);
		
		
//		remove test data
		wikiUserDao.delete(userA);
	}
}
