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

import java.util.Iterator;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * TimestampDao
 * @author ruslan
 */
public class TimestampDao extends HibernateDaoSupport {
    
    /* 0 if user opens forum first time; otherwise >0 */
    private static final String SQL_QUERY_FIRST_TIME_BY_MESSAGE_USER = 
	" SELECT count(*) FROM " + Timestamp.class.getName() + " ts WHERE ts.message.uid = ? AND ts.forumUser.uid = ? ";
    
    private static final String SQL_QUERY_TIMESTAMP_BY_MESSAGE_USER = 
	" FROM " + Timestamp.class.getName() + " ts WHERE ts.message.uid = ? AND ts.forumUser.uid = ? ";
    
    private static final String SQL_QUERY_MESSAGE_NUMBER_BY_MESSAGE_USER = 
	"SELECT count(*) FROM " + Message.class.getName() + " mes WHERE mes.uid IN (SELECT seq.message.uid FROM " + 
	MessageSeq.class.getName() + " seq WHERE seq.rootMessage.uid = ?) " + " AND mes.updated > (SELECT ts.timestamp FROM " + 
	Timestamp.class.getName() + " ts WHERE ts.message.uid = ? AND ts.forumUser.uid = ?)";
    
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
	List timestampList = this.getHibernateTemplate().find(SQL_QUERY_TIMESTAMP_BY_MESSAGE_USER, new Object[]{messageId, forumUserId});
	if (timestampList != null && timestampList.size() > 0)
	    return (Timestamp) (timestampList.get(0));
	else
	    return null;
    }
    
    /**
     * Get number of new postings.
     * 
     * @param messageId
     * @param userId
     * @return 
     */
    public int getNewMessagesNum(Long messageId, Long userId) {
	List firstTimeList = this.getHibernateTemplate().find(SQL_QUERY_FIRST_TIME_BY_MESSAGE_USER, new Object[]{messageId, userId});
	if (firstTimeList != null && firstTimeList.size() > 0)
	{
		if (((Number)firstTimeList.get(0)).intValue() > 0) // if not first time
		{
		    List postingsList = this.getHibernateTemplate().find(SQL_QUERY_MESSAGE_NUMBER_BY_MESSAGE_USER, 
			    new Object[]{messageId, messageId, userId});
				
		    if(postingsList != null && postingsList.size() > 0)
			return ((Number)postingsList.get(0)).intValue();
		    else
			    return 0;
		}
		else
		    return -1; // user views forum for the first time
	}
	else
	    	return 0;
    }
    
}
