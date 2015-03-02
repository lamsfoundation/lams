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

import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.lamsfoundation.lams.tool.Tool;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class MessageSeqDao extends HibernateDaoSupport {
    private static final String SQL_QUERY_FIND_COMPLETE_TOPIC = "from " + MessageSeq.class.getName()
	    + " where root_message_uid = ?";
    private static final String SQL_QUERY_FIND_TOPIC_ID = "from " + MessageSeq.class.getName()
	    + " where message_uid = ?";
    private static final String SQL_QUERY_FIND_NEXT_THREAD_TOP = "from " + MessageSeq.class.getName()
	    + " where root_message_uid = ? and message.uid > ? and message_level = 1";
    private static final String SQL_QUERY_FIND_NEXT_THREAD_MESSAGES = "from " + MessageSeq.class.getName()
	    + " where root_message_uid = ? and thread_message_uid = ? and message_level > 1";

    private static final String SQL_QUERY_NUM_POSTS_BY_TOPIC = "select count(*) from " + MessageSeq.class.getName()
	    + " ms where ms.message.createdBy.userId=? and ms.message.isAuthored = false and ms.rootMessage.uid=?";

    private static final String SQL_QUERY_NUM_POSTS_BY_ROOT_MESSAGE_AND_DATE = "SELECT count(*) FROM "
	    + MessageSeq.class.getName() + " seq WHERE seq.rootMessage.uid = ? AND seq.message.updated > ?";

    public MessageSeq getById(Long messageSeqId) {
	return (MessageSeq) getHibernateTemplate().get(MessageSeq.class, messageSeqId);
    }

    public List getCompleteTopic(Long rootTopicId) {
	return this.getHibernateTemplate().find(SQL_QUERY_FIND_COMPLETE_TOPIC, rootTopicId);
    }

    public List getThreadByThreadId(final Long rootTopicId, final Long previousThreadMessageId) {
	HibernateTemplate template = this.getHibernateTemplate();
	template.setMaxResults(1);
	List list = template.find(SQL_QUERY_FIND_NEXT_THREAD_TOP, new Object[] { rootTopicId, previousThreadMessageId });	
	template.setMaxResults(0);
	if (list != null && list.size() > 0) {
	    MessageSeq threadTop = ((MessageSeq) list.get(0));
	    List all = template.find(SQL_QUERY_FIND_NEXT_THREAD_MESSAGES, new Object[] { rootTopicId, threadTop.getMessage().getUid() });
	    all.add(threadTop);
	    return all;
	}
	return list;
    }

    // used for the recureive proc call - to be deleted later
    public List getThreadIds(final Long rootTopicId, final Long previousThreadMessageId) {

        return (List) getHibernateTemplate().execute(new HibernateCallback()
        {
            public Object doInHibernate(Session session) throws HibernateException
            {
        	String call = "CALL TL_LAFRUM11_GET_THREAD_MESSAGE_UIDS("+rootTopicId+","+previousThreadMessageId+")";
                return session.createSQLQuery(call).list();
            }
        });
    }
    
//    Query query = session.createSQLQuery(
//		"CALL GetStocks(:stockCode)")
//		.addEntity(Stock.class)
//		.setParameter("stockCode", "7277");
//	 
//	List result = query.list();
//	for(int i=0; i<result.size(); i++){
//		Stock stock = (Stock)result.get(i);
//		System.out.println(stock.getStockCode());
//	}
//	

    public MessageSeq getByTopicId(Long messageId) {
	List list = this.getHibernateTemplate().find(SQL_QUERY_FIND_TOPIC_ID, messageId);
	if (list == null || list.isEmpty())
	    return null;
	return (MessageSeq) list.get(0);
    }

    public void save(MessageSeq msgSeq) {
	this.getHibernateTemplate().save(msgSeq);
    }

    public void deleteByTopicId(Long topicUid) {
	MessageSeq seq = getByTopicId(topicUid);
	if (seq != null)
	    this.getHibernateTemplate().delete(seq);
    }

    public int getNumOfPostsByTopic(Long userID, Long topicID) {
	List list = this.getHibernateTemplate().find(SQL_QUERY_NUM_POSTS_BY_TOPIC, new Object[] { userID, topicID });
	if (list != null && list.size() > 0)
	    return ((Number) list.get(0)).intValue();
	else
	    return 0;
    }

    /**
     * Get number of messages newer than specified date.
     * 
     * @param rootMessageId
     * @param userId
     * @return
     */
    public int getNumOfPostsNewerThan(Long rootMessageId, Date date) {

	// user views forum not the first time
	List messages = this.getHibernateTemplate().find(SQL_QUERY_NUM_POSTS_BY_ROOT_MESSAGE_AND_DATE,
		new Object[] { rootMessageId, date });

	if (messages != null && messages.size() > 0) {
	    return ((Number) messages.get(0)).intValue();
	} else {
	    return 0;
	}
    }

}
