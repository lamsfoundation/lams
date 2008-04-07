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

package org.lamsfoundation.lams.tool.wiki.test;

import org.lamsfoundation.lams.tool.wiki.persistence.Wiki;
import org.lamsfoundation.lams.tool.wiki.persistence.WikiToolSession;
import org.lamsfoundation.lams.tool.wiki.persistence.WikiUser;
import org.lamsfoundation.lams.tool.wiki.persistence.Message;
public class TestUtils {
	
	public static Wiki getWikiA(){
		Wiki wiki = new Wiki();
		wiki.setContentId(new Long(1));
		
		return wiki;
		
	}
	public static WikiUser getUserA(){
		WikiUser user = new WikiUser();
		user.setFirstName("UserA");
		user.setUserId(new Long(1));
		
		return user;
	}
	public static WikiToolSession getSessionA(){
		WikiToolSession session = new WikiToolSession();
		session.setSessionId(new Long(1));
		session.setStatus(1);
		return session;
	}
	public static WikiToolSession getSessionB(){
		WikiToolSession session = new WikiToolSession();
		session.setSessionId(new Long(2));
		session.setStatus(2);
		return session;
	}
	public static Message getMessageA() {
		Message msg = new Message();
		msg.setBody("bodyA");
		msg.setSubject("subjectA");
		return msg;
	}
	public static Message getMessageB() {
		Message msg = new Message();
		msg.setBody("bodyB");
		msg.setSubject("subjectB");
		return msg;
	}

}
