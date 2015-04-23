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

package org.lamsfoundation.lams.tool.forum.persistence.hibernate;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.Type;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.forum.persistence.IMessageSeqDAO;
import org.lamsfoundation.lams.tool.forum.persistence.MessageSeq;
import org.springframework.stereotype.Repository;

@Repository
public class MessageSeqDao extends LAMSBaseDAO implements IMessageSeqDAO {
    private static final String SQL_QUERY_FIND_TOPIC_ID = "from " + MessageSeq.class.getName()
	    + " where message_uid = ?";
    private static final String SQL_QUERY_FIND_NEXT_THREAD_TOP = "from " + MessageSeq.class.getName()
	    + " where root_message_uid = ? and message.uid > ? and message_level = 1";
    private static final String SQL_QUERY_FIND_NEXT_THREAD_MESSAGES = "from " + MessageSeq.class.getName()
	    + " where root_message_uid = ? and thread_message_uid = ? and message_level > 1";
    private static final String SQL_QUERY_GET_COMPLETE_THREAD = "from " + MessageSeq.class.getName()
	    + " where thread_message_uid = ?";
    private static final String SQL_QUERY_GET_SEQ_BY_MESSAGE = "from " + MessageSeq.class.getName()
	    + " where message_uid = ?";

    private static final String SQL_QUERY_NUM_POSTS_BY_TOPIC = "select count(*) from " + MessageSeq.class.getName()
	    + " ms where ms.message.createdBy.userId=? and ms.message.isAuthored = false and ms.rootMessage.uid=?";

    private static final String SQL_QUERY_NUM_POSTS_BY_ROOT_MESSAGE_AND_DATE = "SELECT count(*) FROM "
	    + MessageSeq.class.getName() + " seq WHERE seq.rootMessage.uid = ? AND seq.message.updated > ?";

    private static final Logger log = Logger.getLogger(MessageSeqDao.class);
    
    public MessageSeq getById(Long messageSeqId) {
	return (MessageSeq) find(MessageSeq.class, messageSeqId);
    }

    public MessageSeq getByMessageId(Long messageId) {
	List list = doFind(SQL_QUERY_GET_SEQ_BY_MESSAGE, messageId);	
	if (list != null ) {
	    if ( list.size() > 1) {
		log.warn("Looking up message seq by message id="+messageId+". More than one message seq found!"+list.toString());
	    }
	    return (MessageSeq) list.get(0);
	} else {
	    return null;
	}
    }

    public List getThreadByThreadId(final Long threadMessageId) {
	return doFind(SQL_QUERY_GET_COMPLETE_THREAD, new Object[] { threadMessageId });
    }

    public List getNextThreadByThreadId(final Long rootTopicId, final Long previousThreadMessageId) {
	Query queryObject = getSession().createQuery(SQL_QUERY_FIND_NEXT_THREAD_TOP)
		.setParameter(0, rootTopicId)
		.setParameter(1, previousThreadMessageId)
		.setMaxResults(1);
	List list = queryObject.list();
	if (list != null && list.size() > 0) {
	    MessageSeq threadTop = ((MessageSeq) list.get(0));
	    List all = doFind(SQL_QUERY_FIND_NEXT_THREAD_MESSAGES, new Object[] { rootTopicId, threadTop.getMessage().getUid() });
	    all.add(threadTop);
	    return all;
	}
	return list;
    }

    /* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.forum.persistence.hibernate.IMessageSeqDAO#getTopicThread(java.lang.Long)
	 */
    @Override
    public List getCompleteTopic(Long rootTopicId) {
    	return getSession().createCriteria(MessageSeq.class).add(Restrictions.eq("rootMessage.uid", rootTopicId)).list();
    }

    /* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.forum.persistence.hibernate.IMessageSeqDAO#getByTopicId(java.lang.Long)
	 */
    @Override
	public MessageSeq getByTopicId(Long messageId) {
	List list = doFind(SQL_QUERY_FIND_TOPIC_ID, messageId);
	if (list == null || list.isEmpty())
	    return null;
	return (MessageSeq) list.get(0);
    }

    /* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.forum.persistence.hibernate.IMessageSeqDAO#save(org.lamsfoundation.lams.tool.forum.persistence.MessageSeq)
	 */
    @Override
	public void save(MessageSeq msgSeq) {
	getSession().save(msgSeq);
    }

    /* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.forum.persistence.hibernate.IMessageSeqDAO#deleteByTopicId(java.lang.Long)
	 */
    @Override
	public void deleteByTopicId(Long topicUid) {
	MessageSeq seq = getByTopicId(topicUid);
	if (seq != null)
	    getSession().delete(seq);
    }

    /* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.forum.persistence.hibernate.IMessageSeqDAO#getNumOfPostsByTopic(java.lang.Long, java.lang.Long)
	 */
    @Override
	public int getNumOfPostsByTopic(Long userID, Long topicID) {
	List list = doFind(SQL_QUERY_NUM_POSTS_BY_TOPIC, new Object[] { userID, topicID });
	if (list != null && list.size() > 0)
	    return ((Number) list.get(0)).intValue();
	else
	    return 0;
    }

    /* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.forum.persistence.hibernate.IMessageSeqDAO#getNumOfPostsNewerThan(java.lang.Long, java.util.Date)
	 */
    @Override
	public int getNumOfPostsNewerThan(Long rootMessageId, Date date) {

	// user views forum not the first time
	List messages = doFind(SQL_QUERY_NUM_POSTS_BY_ROOT_MESSAGE_AND_DATE,
		new Object[] { rootMessageId, date });

	if (messages != null && messages.size() > 0) {
	    return ((Number) messages.get(0)).intValue();
	} else {
	    return 0;
	}
    }

}
