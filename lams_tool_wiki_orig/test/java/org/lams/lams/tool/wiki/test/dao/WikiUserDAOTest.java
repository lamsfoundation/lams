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

import org.lams.lams.tool.wiki.persistence.WikiUser;
import org.lams.lams.tool.wiki.test.DAOBaseTest;
import org.lams.lams.tool.wiki.test.TestUtils;

public class WikiUserDAOTest extends DAOBaseTest{

	public WikiUserDAOTest(String name) {
		super(name);
	}
	public void testSave(){
		WikiUser user = TestUtils.getUserA();
		wikiUserDao.save(user);
		WikiUser tUser = wikiUserDao.getByUid(user.getUid());
		assertEquals(tUser,user);
		
		//remove test data
		wikiUserDao.delete(user);
	}
	public void testGetByUserId(){
		WikiUser user = TestUtils.getUserA();
		wikiUserDao.save(user);
		WikiUser tUser = wikiUserDao.getByUserId(new Long(1));
		assertEquals(tUser,user);
		
		//remove test data
		wikiUserDao.delete(user);
	}

}
