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

package org.lamsfoundation.lams.tool.forum.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.forum.dao.IForumDAO;
import org.lamsfoundation.lams.tool.forum.model.Forum;
import org.lamsfoundation.lams.tool.forum.model.ForumCondition;
import org.springframework.stereotype.Repository;

/**
 * ForumDao
 *
 * @author conradb
 *
 *
 */
@Repository
public class ForumDao extends LAMSBaseDAO implements IForumDAO {
    private static final String FIND_FORUM_BY_CONTENTID = "from Forum forum where forum.contentId=?";

    @Override
    public void saveOrUpdate(Forum forum) {
	forum.updateModificationData();
	this.getSession().saveOrUpdate(forum);
    }

    @Override
    public Forum getById(Long forumId) {
	return getSession().get(Forum.class, forumId);
    }

    @Override
    public void delete(Forum forum) {
	this.getSession().delete(forum);
    }

    @Override
    public Forum getByContentId(Long contentID) {
	List list = doFindCacheable(ForumDao.FIND_FORUM_BY_CONTENTID, contentID);
	if (list != null && list.size() > 0) {
	    return (Forum) list.get(0);
	} else {
	    return null;
	}
    }

    @Override
    public void deleteCondition(ForumCondition condition) {
	if (condition != null && condition.getConditionId() != null) {
	    this.getSession().delete(condition);
	}
    }
}
