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
package org.lamsfoundation.lams.tool.forum.test.dao;

import java.util.List;

import org.lamsfoundation.lams.tool.forum.persistence.Message;
import org.lamsfoundation.lams.tool.forum.persistence.MessageDao;
import org.lamsfoundation.lams.tool.forum.test.BaseTest;
import org.lamsfoundation.lams.tool.forum.test.TestUtils;

public class MessageDAOTest extends BaseTest{

	public MessageDAOTest(String name) {
		super(name);
	}
	
	public void testSave(){
		Message msg = TestUtils.getMessageA();
		MessageDao dao = new MessageDao();
		dao.saveOrUpdate(msg);
		Message tmsg = dao.getById(msg.getUid());
		assertEquals(msg,tmsg);
		
		dao.delete(msg.getUid());
	}
	public void testDelete(){
		Message msg = TestUtils.getMessageA();
		MessageDao dao = new MessageDao();
		dao.saveOrUpdate(msg);
		dao.delete(msg.getUid());
		
		assertNull(dao.getById(msg.getUid()));
		
	}

	public void testGetBySession(){
		MessageDao dao = new MessageDao();
		Message msgA = TestUtils.getMessageA();
		dao.saveOrUpdate(msgA);
		Message msgB = TestUtils.getMessageB();
		dao.saveOrUpdate(msgB);
		
		List list = dao.getBySession(new Long(1));
		
		assertEquals(list.size(),2);
		assertEquals(list.get(0),msgA);
		assertEquals(list.get(1),msgB);
		
		//remove test data
		dao.delete(msgA.getUid());
		dao.delete(msgB.getUid());
		
	}
	public void testGetBySessionAndUser(){
		MessageDao dao = new MessageDao();
		Message msgA = TestUtils.getMessageA();
		dao.saveOrUpdate(msgA);
		Message msgB = TestUtils.getMessageB();
		dao.saveOrUpdate(msgB);
		
		List list = dao.getByUserAndSession(new Long(1),new Long(1));
		
		assertEquals(list.size(),1);
		assertEquals(list.get(0),msgA);
		
		//remove test data
		dao.delete(msgA.getUid());
		dao.delete(msgB.getUid());
	}
	public void testGetFromAuthor(){
		MessageDao dao = new MessageDao();
		Message msgA = TestUtils.getMessageA();
		msgA.setIsAuthored(true);
		dao.saveOrUpdate(msgA);
		Message msgB = TestUtils.getMessageB();
		msgB.setIsAuthored(false);
		dao.saveOrUpdate(msgB);
		
		List list = dao.getTopicsFromAuthor(new Long(1));
		
		assertEquals(list.size(),1);
		assertEquals(list.get(0),msgA);
		
		//remove test data
		dao.delete(msgA.getUid());
		dao.delete(msgB.getUid());
		
	}
	public void testGetRootTopics(){
		MessageDao dao = new MessageDao();
		Message msgA = TestUtils.getMessageA();
		msgA.setIsAuthored(true);
		dao.saveOrUpdate(msgA);
		Message msgB = TestUtils.getMessageB();
		msgB.setIsAuthored(false);
		msgB.setParent(msgA);
		dao.saveOrUpdate(msgB);
		
		List list = dao.getRootTopics(new Long(1));
		
		assertEquals(list.size(),1);
		assertEquals(list.get(0),msgA);
		
		//remove test data
		dao.delete(msgA.getUid());
		dao.delete(msgB.getUid());
	}
	public void testGetChildrenTopics(){
		MessageDao dao = new MessageDao();
		Message msgA = TestUtils.getMessageA();
		msgA.setIsAuthored(true);
		dao.saveOrUpdate(msgA);
		Message msgB = TestUtils.getMessageB();
		msgB.setIsAuthored(false);
		msgB.setParent(msgA);
		dao.saveOrUpdate(msgB);
		
		List list = dao.getChildrenTopics(new Long(1));
		
		assertEquals(list.size(),1);
		assertEquals(list.get(0),msgB);
		
		//remove test data
		dao.delete(msgA.getUid());
		dao.delete(msgB.getUid());
	}
	

}
