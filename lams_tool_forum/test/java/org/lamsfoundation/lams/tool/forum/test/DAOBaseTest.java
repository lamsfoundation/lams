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
package org.lamsfoundation.lams.tool.forum.test;

import org.lamsfoundation.lams.tool.forum.persistence.ForumDao;
import org.lamsfoundation.lams.tool.forum.persistence.ForumToolSessionDao;
import org.lamsfoundation.lams.tool.forum.persistence.ForumUserDao;
import org.lamsfoundation.lams.tool.forum.persistence.MessageDao;

public class DAOBaseTest extends BaseTest {
	
	protected ForumDao forumDao;
	protected ForumToolSessionDao forumToolSessionDao;
	protected ForumUserDao forumUserDao;
	protected MessageDao messageDao;
	
	public DAOBaseTest(String name) {
		super(name);
	}
    protected void setUp() throws Exception {
    	super.setUp();
    	forumDao = (ForumDao) context.getBean("forumDao");
    	forumToolSessionDao = (ForumToolSessionDao) context.getBean("forumToolSessionDao");
    	forumUserDao  = (ForumUserDao) context.getBean("forumUserDao");
    	messageDao  = (MessageDao) context.getBean("messageDao");
    }
}
