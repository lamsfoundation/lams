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
package org.lamsfoundation.lams.tool.forum.persistence;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * TimestampDao
 * 
 * @author ruslan
 */
public class TimestampDao extends HibernateDaoSupport {

    private static final String GET_TIMESTAMP_BY_MESSAGE_AND_USER = " FROM " + Timestamp.class.getName()
	    + " ts WHERE ts.message.uid = ? AND ts.forumUser.uid = ? ";

    public void delete(Timestamp timestamp) {
	this.getHibernateTemplate().delete(timestamp);
    }

    /**
     * Save timestamp.
     *
     * @param timestamp
     * @return
     */
    public void saveOrUpdate(Timestamp timestamp) {
	this.getHibernateTemplate().saveOrUpdate(timestamp);
    }

    /**
     * Get timestamp.
     *
     * @param messageId
     * @param forumUserId
     * @return
     */
    public Timestamp getTimestamp(Long messageId, Long forumUserId) {
	List timestampList = this.getHibernateTemplate().find(GET_TIMESTAMP_BY_MESSAGE_AND_USER,
		new Object[] { messageId, forumUserId });
	if (timestampList != null && timestampList.size() > 0) {
	    return (Timestamp) (timestampList.get(0));
	} else {
	    return null;
	}
    }

}
