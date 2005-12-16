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

import org.lamsfoundation.lams.tool.forum.persistence.ForumToolSession;
import org.lamsfoundation.lams.tool.forum.persistence.ForumToolSessionDao;
import org.lamsfoundation.lams.tool.forum.test.BaseTest;
import org.lamsfoundation.lams.tool.forum.test.TestUtils;

public class ForumToolSessionDAOTest extends BaseTest{

	public ForumToolSessionDAOTest(String name) {
		super(name);
		
	}
	
	public void testSave(){
		ForumToolSession session = TestUtils.getSessionA();
		ForumToolSessionDao dao = new ForumToolSessionDao();
		dao.saveOrUpdate(session);
		
		ForumToolSession tSession = dao.getBySessionId(session.getSessionId());
		
		assertEquals(session,tSession);
		
		//remove test data
		dao.delete(session);
		
	}
	
	public void testDelete(){
		ForumToolSession session = TestUtils.getSessionA();
		ForumToolSessionDao dao = new ForumToolSessionDao();
		dao.saveOrUpdate(session);
		dao.delete(session);
		
		assertNull(dao.getBySessionId(session.getSessionId()));
	}
	
	public void testGetByContentId(){
		ForumToolSessionDao dao = new ForumToolSessionDao();
		ForumToolSession sessionA = TestUtils.getSessionA();
		dao.saveOrUpdate(sessionA);
		ForumToolSession sessionB = TestUtils.getSessionB();
		dao.saveOrUpdate(sessionB);
		
		List list = dao.getByContentId(new Long(1));
		
		assertEquals(list.size(),2);
		assertEquals(list.get(0),sessionA);
		assertEquals(list.get(1),sessionB);
		//remove test data
		dao.delete(sessionA);
		dao.delete(sessionB);
	}
	
	public void testGetBySessionId(){
		ForumToolSession session = TestUtils.getSessionA();
		ForumToolSessionDao dao = new ForumToolSessionDao();
		dao.saveOrUpdate(session);
		
		ForumToolSession tSession = dao.getBySessionId(session.getSessionId());
		
		assertEquals(session,tSession);
		
		//remove test data
		dao.delete(session);
	}
}
