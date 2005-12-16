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

import org.lamsfoundation.lams.tool.forum.persistence.Forum;
import org.lamsfoundation.lams.tool.forum.persistence.ForumToolSession;
import org.lamsfoundation.lams.tool.forum.persistence.ForumUser;
import org.lamsfoundation.lams.tool.forum.persistence.Message;
public class TestUtils {
	
	public static Forum getForumA(){
		Forum forum = new Forum();
		forum.setContentId(new Long(1));
		forum.setCreatedBy(getUserA());
		
		return forum;
		
	}
	public static ForumUser getUserA(){
		ForumUser user = new ForumUser();
		user.setFirstName("UserA");
		user.setUserId(new Long(1));
		
		return user;
	}
	public static ForumToolSession getSessionA(){
		ForumToolSession session = new ForumToolSession();
		session.setSessionId(new Long(1));
		session.setStatus(1);
		Forum forum = new Forum();
		forum.setUid(new Long(1));
		forum.setContentId(new Long(1));
		session.setForum(forum);
		return session;
	}
	public static ForumToolSession getSessionB(){
		ForumToolSession session = new ForumToolSession();
		session.setSessionId(new Long(2));
		session.setStatus(2);
		Forum forum = new Forum();
		forum.setUid(new Long(1));
		forum.setContentId(new Long(1));
		session.setForum(forum);
		return session;
	}
	public static Message getMessageA() {
		Message msg = new Message();
		msg.setBody("bodyA");
		ForumToolSession session = new ForumToolSession();
		session.setUid(new Long(1));
		msg.setToolSession(session);
		msg.setSubject("subjectA");
		Forum forum = new Forum();
		forum.setUid(new Long(1));
		msg.setForum(forum);
		return msg;
	}
	public static Message getMessageB() {
		Message msg = new Message();
		msg.setBody("bodyB");
		msg.setSubject("subjectB");
		ForumToolSession session = new ForumToolSession();
		session.setUid(new Long(1));
		msg.setToolSession(session);
		Forum forum = new Forum();
		forum.setUid(new Long(1));
		msg.setForum(forum);
		return msg;
	}

}
