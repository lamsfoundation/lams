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
package org.lams.lams.tool.wiki.persistence;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * WikiDao
 * @author conradb
 *
 *
 */
public class WikiDao extends HibernateDaoSupport {
	private static final String FIND_FORUM_BY_CONTENTID = "from Wiki wiki where wiki.contentId=?";
	
	public void saveOrUpdate(Wiki wiki) {
		wiki.updateModificationData();
		this.getHibernateTemplate().saveOrUpdate(wiki);
	}

	public Wiki getById(Long wikiId) {
		return (Wiki) getHibernateTemplate().get(Wiki.class,wikiId);
	}
	/**
	 * NOTE: before call this method, must be sure delete all messages in this wiki.
	 * Example code like this:
	 * <pre>
	 * <code>wikiMessageDao.deleteWikiMessage(wiki.getUuid());</code>
	 * </pre>
	 * @param wiki
	 */
	public void delete(Wiki wiki) {
		this.getHibernateTemplate().delete(wiki);
	}

	public Wiki getByContentId(Long contentID) {
		List list = getHibernateTemplate().find(FIND_FORUM_BY_CONTENTID,contentID);
		if(list != null && list.size() > 0)
			return (Wiki) list.get(0);
		else
			return null;
	}
}
