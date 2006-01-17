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

import org.lamsfoundation.lams.tool.forum.persistence.ForumUser;
import org.lamsfoundation.lams.tool.forum.test.DAOBaseTest;
import org.lamsfoundation.lams.tool.forum.test.TestUtils;

public class ForumUserDAOTest extends DAOBaseTest{

	public ForumUserDAOTest(String name) {
		super(name);
	}
	public void testSave(){
		ForumUser user = TestUtils.getUserA();
		forumUserDao.save(user);
		ForumUser tUser = forumUserDao.getByUid(user.getUid());
		assertEquals(tUser,user);
		
		//remove test data
		forumUserDao.delete(user);
	}
	public void testGetByUserId(){
		ForumUser user = TestUtils.getUserA();
		forumUserDao.save(user);
		ForumUser tUser = forumUserDao.getByUserId(new Long(1));
		assertEquals(tUser,user);
		
		//remove test data
		forumUserDao.delete(user);
	}

}
