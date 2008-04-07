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

package org.lamsfoundation.lams.tool.wiki.test.dao;

import java.util.List;

import org.lamsfoundation.lams.tool.wiki.persistence.Wiki;
import org.lamsfoundation.lams.tool.wiki.persistence.WikiToolSession;
import org.lamsfoundation.lams.tool.wiki.test.DAOBaseTest;
import org.lamsfoundation.lams.tool.wiki.test.TestUtils;

public class WikiToolSessionDAOTest extends DAOBaseTest{

	public WikiToolSessionDAOTest(String name) {
		super(name);
		
	}
	
	public void testSave(){
		WikiToolSession session = TestUtils.getSessionA();
		wikiToolSessionDao.saveOrUpdate(session);
		
		WikiToolSession tSession = wikiToolSessionDao.getBySessionId(session.getSessionId());
		
		assertEquals(session,tSession);
		
		//remove test data
		wikiToolSessionDao.delete(session);
		
	}
	
	public void testDelete(){
		WikiToolSession session = TestUtils.getSessionA();
		wikiToolSessionDao.saveOrUpdate(session);
		wikiToolSessionDao.delete(session);
		
		assertNull(wikiToolSessionDao.getBySessionId(session.getSessionId()));
	}
	
	public void testGetByContentId(){
		Wiki wikiA = TestUtils.getWikiA();
		wikiDao.saveOrUpdate(wikiA);
		
		WikiToolSession sessionA = TestUtils.getSessionA();
		sessionA.setWiki(wikiA);
		wikiToolSessionDao.saveOrUpdate(sessionA);
		WikiToolSession sessionB = TestUtils.getSessionB();
		sessionB.setWiki(wikiA);
		wikiToolSessionDao.saveOrUpdate(sessionB);
		
		List list = wikiToolSessionDao.getByContentId(new Long(1));
		
		assertEquals(2,list.size());
		assertEquals(list.get(0),sessionA);
		assertEquals(list.get(1),sessionB);
		//remove test data
		wikiToolSessionDao.delete(sessionA);
		wikiToolSessionDao.delete(sessionB);
		wikiDao.delete(wikiA);
	}
	
	public void testGetBySessionId(){
		WikiToolSession session = TestUtils.getSessionA();
		wikiToolSessionDao.saveOrUpdate(session);
		
		WikiToolSession tSession = wikiToolSessionDao.getBySessionId(session.getSessionId());
		
		assertEquals(session,tSession);
		
		//remove test data
		wikiToolSessionDao.delete(session);
	}
}
