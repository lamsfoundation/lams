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

import org.lamsfoundation.lams.tool.wiki.persistence.Wiki;
import org.lamsfoundation.lams.tool.wiki.test.DAOBaseTest;
import org.lamsfoundation.lams.tool.wiki.test.TestUtils;

public class WikiDAOTest extends DAOBaseTest{

	public WikiDAOTest(String name) {
		super(name);
	}
	
	public void testSave(){
		Wiki wiki = TestUtils.getWikiA();
		wikiDao.saveOrUpdate(wiki);
		
		Wiki tWiki = wikiDao.getById(wiki.getUid());
		assertEquals(tWiki.getContentId(),new Long(1));
		
		//remove test data
		wikiDao.delete(wiki);
	}
	
	public void testDelete(){
		Wiki wiki = TestUtils.getWikiA();
		wikiDao.saveOrUpdate(wiki);
			
		wikiDao.delete(wiki);
		
		assertNull(wikiDao.getById(wiki.getUid()));
	}
	public void testGetByContentId(){
		
		Wiki wiki = TestUtils.getWikiA();
		
		wikiDao.saveOrUpdate(wiki);
		Wiki twiki = wikiDao.getByContentId(wiki.getContentId());
		assertEquals(twiki, wiki);
		
		//remove test data
		wikiDao.delete(wiki);
		
	}

}
