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

package org.lams.lams.tool.wiki.test.dao;

import java.util.List;

import org.lams.lams.tool.wiki.persistence.Wiki;
import org.lams.lams.tool.wiki.persistence.WikiToolSession;
import org.lams.lams.tool.wiki.persistence.WikiUser;
import org.lams.lams.tool.wiki.persistence.Message;
import org.lams.lams.tool.wiki.test.DAOBaseTest;
import org.lams.lams.tool.wiki.test.TestUtils;

public class MessageDAOTest extends DAOBaseTest{

	public MessageDAOTest(String name) {
		super(name);
	}
	
	public void testSave(){
		Message msg = TestUtils.getMessageA();
		wikiMessageDao.saveOrUpdate(msg);
		Message tmsg = wikiMessageDao.getById(msg.getUid());
		assertEquals(msg,tmsg);
		
		wikiMessageDao.delete(msg.getUid());
	}
	public void testDelete(){
		Message msg = TestUtils.getMessageA();
		wikiMessageDao.saveOrUpdate(msg);
		wikiMessageDao.delete(msg.getUid());
		
		assertNull(wikiMessageDao.getById(msg.getUid()));
		
	}

	public void testGetBySession(){
		WikiToolSession sessionA = TestUtils.getSessionA();
		wikiToolSessionDao.saveOrUpdate(sessionA);
		
		Message msgA = TestUtils.getMessageA();
		msgA.setToolSession(sessionA);
		wikiMessageDao.saveOrUpdate(msgA);
		Message msgB = TestUtils.getMessageB();
		msgB.setToolSession(sessionA);
		wikiMessageDao.saveOrUpdate(msgB);
		
		List list = wikiMessageDao.getBySession(new Long(1));
		
		assertEquals(2,list.size());
		assertEquals(list.get(0),msgA);
		assertEquals(list.get(1),msgB);
		
		//remove test data
		wikiMessageDao.delete(msgA.getUid());
		wikiMessageDao.delete(msgB.getUid());
		wikiToolSessionDao.delete(sessionA);
	}
	public void testGetBySessionAndUser(){
		WikiToolSession sessionA = TestUtils.getSessionA();
		wikiToolSessionDao.saveOrUpdate(sessionA);
		WikiUser userA = TestUtils.getUserA();
		wikiUserDao.save(userA);
		
		Message msgA = TestUtils.getMessageA();
		msgA.setToolSession(sessionA);
		msgA.setCreatedBy(userA);
		wikiMessageDao.saveOrUpdate(msgA);
		Message msgB = TestUtils.getMessageB();
		msgB.setToolSession(sessionA);
		msgB.setCreatedBy(userA);
		wikiMessageDao.saveOrUpdate(msgB);
		
		List list = wikiMessageDao.getByUserAndSession(userA.getUid(),sessionA.getSessionId());
		
		assertEquals(2,list.size());
		assertEquals(list.get(0),msgA);
		assertEquals(list.get(1),msgB);
		
		//remove test data
		wikiMessageDao.delete(msgA.getUid());
		wikiMessageDao.delete(msgB.getUid());
		wikiToolSessionDao.delete(sessionA);
		wikiUserDao.delete(userA);
	}
	public void testGetFromAuthor(){
		WikiUser userA = TestUtils.getUserA();
		wikiUserDao.save(userA);
		Wiki wikiA = TestUtils.getWikiA();
		wikiDao.saveOrUpdate(wikiA);
		
		Message msgA = TestUtils.getMessageA();
		msgA.setCreatedBy(userA);
		msgA.setWiki(wikiA);
		msgA.setIsAuthored(true);
		wikiMessageDao.saveOrUpdate(msgA);
		Message msgB = TestUtils.getMessageB();
		msgB.setWiki(wikiA);
		msgB.setCreatedBy(userA);
		msgB.setIsAuthored(false);
		wikiMessageDao.saveOrUpdate(msgB);
		
		List list = wikiMessageDao.getTopicsFromAuthor(wikiA.getUid());
		
		assertEquals(1,list.size());
		assertEquals(list.get(0),msgA);
		
		//remove test data
		wikiMessageDao.delete(msgA.getUid());
		wikiMessageDao.delete(msgB.getUid());
		wikiUserDao.delete(userA);
		wikiDao.delete(wikiA);
		
	}
	public void testGetRootTopics(){
		WikiToolSession sessionA = TestUtils.getSessionA();
		wikiToolSessionDao.saveOrUpdate(sessionA);
		
		Message msgA = TestUtils.getMessageA();
		msgA.setToolSession(sessionA);
		wikiMessageDao.saveOrUpdate(msgA);
		
		Message msgB = TestUtils.getMessageB();
		msgB.setParent(msgA);
		msgB.setToolSession(sessionA);
		wikiMessageDao.saveOrUpdate(msgB);
		
		List list = wikiMessageDao.getRootTopics(sessionA.getSessionId());
		
		assertEquals(1,list.size());
		assertEquals(list.get(0),msgA);
		
		//remove test data
		wikiMessageDao.delete(msgB.getUid());
		wikiMessageDao.delete(msgA.getUid());
		wikiToolSessionDao.delete(sessionA);
	}
	public void testGetChildrenTopics(){
		Message msgA = TestUtils.getMessageA();
		wikiMessageDao.saveOrUpdate(msgA);
		
		Message msgB = TestUtils.getMessageB();
		msgB.setParent(msgA);
		wikiMessageDao.saveOrUpdate(msgB);
		
		List list = wikiMessageDao.getChildrenTopics(msgA.getUid());
		
		assertEquals(1,list.size());
		assertEquals(list.get(0),msgB);
		
		//remove test data
		wikiMessageDao.delete(msgB.getUid());
		wikiMessageDao.delete(msgA.getUid());
	}
	

}
