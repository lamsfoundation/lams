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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $$Id$$ */	
package org.lamsfoundation.lams.tool.forum.persistence;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * ForumDao
 * @author conradb
 *
 *
 */
public class ForumDao extends HibernateDaoSupport {
	private static final String FIND_FORUM_BY_CONTENTID = "from Forum forum where forum.contentId=?";
	
	public void saveOrUpdate(Forum forum) {
		forum.updateModificationData();
		this.getHibernateTemplate().saveOrUpdate(forum);
		this.getHibernateTemplate().flush();
	}

	public Forum getById(Long forumId) {
		return (Forum) getHibernateTemplate().get(Forum.class,forumId);
	}
	/**
	 * NOTE: before call this method, must be sure delete all messages in this forum.
	 * Example code like this:
	 * <pre>
	 * <code>messageDao.deleteForumMessage(forum.getUuid());</code>
	 * </pre>
	 * @param forum
	 */
	public void delete(Forum forum) {
		this.getHibernateTemplate().delete(forum);
		this.getHibernateTemplate().flush();
	}

	public Forum getByContentId(Long contentID) {
		List list = getHibernateTemplate().find(FIND_FORUM_BY_CONTENTID,contentID);
		if(list != null && list.size() > 0)
			return (Forum) list.get(0);
		else
			return null;
	}
	public void flush() {
		this.getHibernateTemplate().flush();
	}

}
