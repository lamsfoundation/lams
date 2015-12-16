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

/* $Id$ */

package org.lamsfoundation.lams.comments.dao.hibernate;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.lamsfoundation.lams.comments.Comment;
import org.lamsfoundation.lams.comments.dao.ICommentDAO;
import org.lamsfoundation.lams.comments.util.TopicComparator;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author Fiona Malikoff
 */
public class CommentDAO extends HibernateDaoSupport implements ICommentDAO {

    /* Standard DAO call */

    @Override
    public void saveOrUpdate(Comment comment) {
	this.getHibernateTemplate().saveOrUpdate(comment);
    }

    @Override
    public void update(Comment comment) {
	this.getHibernateTemplate().saveOrUpdate(comment);
    }

    @Override
    public Comment getById(Long commentId) {
	return (Comment) getHibernateTemplate().get(Comment.class, commentId);
    }

    /* Session Level Lookups */
    private static final String SQL_QUERY_FIND_ROOT_TOPICS = "from " + Comment.class.getName() + " cs "
	    + " where cs.parent is null and cs.session.externalId=:externalId and "
	    + " cs.session.externalIdType=:externalIdType and cs.session.externalSignature=:externalSignature";

    @Override
    public Comment getRootTopic(Long externalId, Integer externalIdType, String externalSignature) {
	@SuppressWarnings("rawtypes")
	List list = getSession().createQuery(SQL_QUERY_FIND_ROOT_TOPICS).setLong("externalId", externalId)
		.setInteger("externalIdType", externalIdType).setString("externalSignature", externalSignature).list();
	if (list != null && list.size() > 0)
	    return (Comment) list.get(0);
	return null;
    }

    /* Thread based lookups - Returns a complex structure so that the likes information can be passed 
     * back with it. */
    private static final String SQL_QUERY_FIND_FIRST_THREAD_TOP = "select uid from lams_comment"
	    + " where root_comment_uid = :rootUid and comment_level = 1 order by uid DESC";

    private static final String SQL_QUERY_FIND_NEXT_THREAD_TOP = "select uid from lams_comment"
	    + " where root_comment_uid = :rootUid and uid < :lastUid and comment_level = 1 order by uid DESC";

    private static final String SQL_QUERY_FIND_NEXT_THREAD_MESSAGES = 
	    "SELECT c.*, SUM(l.vote) likes_total, l2.vote user_vote FROM lams_comment c "
	    + " LEFT JOIN lams_comment_likes l ON c.uid = l.comment_uid "
	    + " LEFT JOIN lams_comment_likes l2 ON c.uid = l2.comment_uid AND l2.user_id=:userId "
	    + " WHERE c.thread_comment_uid IN (:threadIds) "
	    + " GROUP BY c.uid";
    
    private static final String SQL_QUERY_GET_COMPLETE_THREAD = 
	    "SELECT c.*, SUM(l.vote) likes_total, l2.vote user_vote FROM lams_comment c "
	    + " LEFT JOIN lams_comment_likes l ON c.uid = l.comment_uid "
	    + " LEFT JOIN lams_comment_likes l2 ON c.uid = l2.comment_uid AND l2.user_id=:userId "
	    + " WHERE c.thread_comment_uid = :threadId "
	    + " GROUP BY c.uid";


    @Override
    @SuppressWarnings("unchecked")
    public SortedSet<Comment> getThreadByThreadId(Long threadCommentId, Integer userId) {
	SQLQuery query = getSession().createSQLQuery(SQL_QUERY_GET_COMPLETE_THREAD);
	query.addEntity("comment", Comment.class)
		.addScalar("likes_total", Hibernate.INTEGER)
		.addScalar("user_vote", Hibernate.INTEGER)
		.setLong("userId", userId != null ? userId : 0)
		.setLong("threadId", threadCommentId);
	List<Object[]> results = query.list();
	return upgradeComments(results);
    }

    private SortedSet<Comment> upgradeComments(List<Object[]> rawObjects) {
	SortedSet<Comment> results = new TreeSet<Comment>(new TopicComparator());
	for ( Object[] rawObject : rawObjects ) {
	    Comment comment = (Comment) rawObject[0];
	    Integer likeCount = (Integer) rawObject[1];
	    comment.setLikeCount(likeCount != null ? likeCount : 0);
	    Integer userVote = (Integer) rawObject[2];
	    comment.setVote(userVote);
	    results.add(comment);
	}
	return results;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public SortedSet<Comment> getNextThreadByThreadId(final Long rootTopicId, final Long previousThreadMessageId, Integer numberOfThreads, Integer userId) {

	List<Number> threadUidList = null;
	if (previousThreadMessageId == null || previousThreadMessageId == 0L) {
	    threadUidList = (List<Number>) getSession().createSQLQuery(SQL_QUERY_FIND_FIRST_THREAD_TOP)
		    .setLong("rootUid", rootTopicId)
		    .setMaxResults(numberOfThreads)
		    .list();
	} else {
	    threadUidList = (List<Number>) getSession().createSQLQuery(SQL_QUERY_FIND_NEXT_THREAD_TOP)
		    .setLong("rootUid", rootTopicId)
		    .setLong("lastUid", previousThreadMessageId)
		    .setMaxResults(numberOfThreads)
		    .list();
	}

	if (threadUidList != null && threadUidList.size() > 0) {
	    SQLQuery query =  getSession().createSQLQuery(SQL_QUERY_FIND_NEXT_THREAD_MESSAGES);
	    query.addEntity("comment", Comment.class)
	    	.addScalar("likes_total", Hibernate.INTEGER)
	    	.addScalar("user_vote", Hibernate.INTEGER)
	    	.setLong("userId", userId != null ? userId : 0)
	    	.setParameterList("threadIds", threadUidList);
	    List<Object[]> results = query.list();
	    return upgradeComments(results);
	}
	return new TreeSet<Comment>();
    }

}
