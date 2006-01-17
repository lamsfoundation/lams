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

import org.lamsfoundation.lams.tool.forum.persistence.Forum;
import org.lamsfoundation.lams.tool.forum.persistence.ForumToolSession;
import org.lamsfoundation.lams.tool.forum.test.DAOBaseTest;
import org.lamsfoundation.lams.tool.forum.test.TestUtils;

public class ForumToolSessionDAOTest extends DAOBaseTest{

	public ForumToolSessionDAOTest(String name) {
		super(name);
		
	}
	
	public void testSave(){
		ForumToolSession session = TestUtils.getSessionA();
		forumToolSessionDao.saveOrUpdate(session);
		
		ForumToolSession tSession = forumToolSessionDao.getBySessionId(session.getSessionId());
		
		assertEquals(session,tSession);
		
		//remove test data
		forumToolSessionDao.delete(session);
		
	}
	
	public void testDelete(){
		ForumToolSession session = TestUtils.getSessionA();
		forumToolSessionDao.saveOrUpdate(session);
		forumToolSessionDao.delete(session);
		
		assertNull(forumToolSessionDao.getBySessionId(session.getSessionId()));
	}
	
	public void testGetByContentId(){
		Forum forumA = TestUtils.getForumA();
		forumDao.saveOrUpdate(forumA);
		
		ForumToolSession sessionA = TestUtils.getSessionA();
		sessionA.setForum(forumA);
		forumToolSessionDao.saveOrUpdate(sessionA);
		ForumToolSession sessionB = TestUtils.getSessionB();
		sessionB.setForum(forumA);
		forumToolSessionDao.saveOrUpdate(sessionB);
		
		List list = forumToolSessionDao.getByContentId(new Long(1));
		
		assertEquals(2,list.size());
		assertEquals(list.get(0),sessionA);
		assertEquals(list.get(1),sessionB);
		//remove test data
		forumToolSessionDao.delete(sessionA);
		forumToolSessionDao.delete(sessionB);
		forumDao.delete(forumA);
	}
	
	public void testGetBySessionId(){
		ForumToolSession session = TestUtils.getSessionA();
		forumToolSessionDao.saveOrUpdate(session);
		
		ForumToolSession tSession = forumToolSessionDao.getBySessionId(session.getSessionId());
		
		assertEquals(session,tSession);
		
		//remove test data
		forumToolSessionDao.delete(session);
	}
}
