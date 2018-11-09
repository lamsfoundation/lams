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



package org.lamsfoundation.lams.comments.dao.hibernate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.hibernate.query.NativeQuery;
import org.hibernate.type.IntegerType;
import org.lamsfoundation.lams.comments.Comment;
import org.lamsfoundation.lams.comments.dao.ICommentDAO;
import org.lamsfoundation.lams.comments.service.ICommentService;
import org.lamsfoundation.lams.comments.util.TopicComparator;
import org.lamsfoundation.lams.comments.util.TopicComparatorLike;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;

/**
 * @author Fiona Malikoff
 */
public class CommentDAO extends LAMSBaseDAO implements ICommentDAO {

    /* Standard DAO call */

    @Override
    public void saveOrUpdate(Comment comment) {
	saveObject(comment);
    }

    @Override
    public Comment getById(Long commentId) {
	return (Comment) getSession().get(Comment.class, commentId);
    }

    /* Session Level Lookups */
    private static final String SQL_QUERY_FIND_ROOT_TOPICS = "from " + Comment.class.getName() + " cs "
	    + " where cs.parent is null and cs.session.externalId=:externalId and "
	    + " cs.session.externalIdType=:externalIdType and cs.session.externalSignature=:externalSignature";

    private static final String SQL_QUERY_FIND_ROOT_TOPICS_EXTRA_ID = "from " + Comment.class.getName() + " cs "
	    + " where cs.parent is null and cs.session.externalId=:externalId and "
	    + " cs.session.externalSecondaryId=:externalSecondaryId and "
	    + " cs.session.externalIdType=:externalIdType and cs.session.externalSignature=:externalSignature";

    @Override
    public Comment getRootTopic(Long externalId, Long externalSecondaryId, Integer externalIdType,
	    String externalSignature) {
	@SuppressWarnings("rawtypes")
	List list = null;
	if (externalSecondaryId == null)
	    list = getSession().createQuery(SQL_QUERY_FIND_ROOT_TOPICS).setParameter("externalId", externalId)
		    .setParameter("externalIdType", externalIdType).setParameter("externalSignature", externalSignature)
		    .list();
	else
	    list = getSession().createQuery(SQL_QUERY_FIND_ROOT_TOPICS_EXTRA_ID).setParameter("externalId", externalId)
		    .setParameter("externalSecondaryId", externalSecondaryId).setParameter("externalIdType", externalIdType)
		    .setParameter("externalSignature", externalSignature).list();

	if (list != null && list.size() > 0) {
	    return (Comment) list.get(0);
	}
	return null;
    }

    /*
     * Thread based lookups - Returns a complex structure so that the likes information can be passed
     * back with it.
     */
    private static final String SQL_QUERY_GET_COMPLETE_THREAD = "SELECT c.*, SUM(l.vote) likes_total, l2.vote user_vote FROM lams_comment c "
	    + " LEFT JOIN lams_comment_likes l ON c.uid = l.comment_uid "
	    + " LEFT JOIN lams_comment_likes l2 ON c.uid = l2.comment_uid AND l2.user_id=:userId "
	    + " WHERE c.thread_comment_uid = :threadId " + " GROUP BY c.uid";

    @Override
    @SuppressWarnings("unchecked")
    public SortedSet<Comment> getThreadByThreadId(Long threadCommentId, Integer sortBy, Integer userId) {
	NativeQuery<Object[]> query = getSession().createNativeQuery(SQL_QUERY_GET_COMPLETE_THREAD);
	query.addEntity("comment", Comment.class).addScalar("likes_total", IntegerType.INSTANCE)
		.addScalar("user_vote", IntegerType.INSTANCE).setParameter("userId", userId != null ? userId : 0)
		.setParameter("threadId", threadCommentId);
	List<Object[]> results = query.list();
	return upgradeComments(results, sortBy);
    }

    private SortedSet<Comment> upgradeComments(List<Object[]> rawObjects, Integer sortBy) {
	Comparator<Comment> comparator = ICommentService.SORT_BY_LIKE.equals(sortBy) ? new TopicComparatorLike()
		: new TopicComparator();
	SortedSet<Comment> results = new TreeSet<Comment>(comparator);
	for (Object[] rawObject : rawObjects) {
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
    public SortedSet<Comment> getNextThreadByThreadId(final Long rootTopicId, final Long previousThreadMessageId,
	    Integer numberOfThreads, Integer sortBy, String extraSortParam, Integer userId) {

	if (ICommentService.SORT_BY_LIKE.equals(sortBy)) {
	    return getNextThreadByThreadIdLikes(rootTopicId, previousThreadMessageId, numberOfThreads, sortBy,
		    extraSortParam, userId);
	} else {
	    return getNextThreadByThreadIdNewestFirst(rootTopicId, previousThreadMessageId, numberOfThreads, sortBy,
		    userId);
	}

    }

    private static final String SQL_QUERY_FIND_FIRST_THREAD_TOP_BY_UID = "SELECT uid FROM lams_comment"
	    + " WHERE root_comment_uid = :rootUid AND comment_level = 1 AND sticky = 0 ORDER BY uid DESC";
    private static final String SQL_QUERY_FIND_NEXT_THREAD_TOP = "SELECT uid FROM lams_comment"
	    + " WHERE root_comment_uid = :rootUid AND uid < :lastUid AND comment_level = 1 AND sticky = 0 ORDER BY uid DESC";

    private static final String SQL_QUERY_FIND_NEXT_THREAD_MESSAGES = "SELECT c.*, SUM(l.vote) likes_total, l2.vote user_vote FROM lams_comment c "
	    + " LEFT JOIN lams_comment_likes l ON c.uid = l.comment_uid "
	    + " LEFT JOIN lams_comment_likes l2 ON c.uid = l2.comment_uid AND l2.user_id=:userId "
	    + " WHERE c.thread_comment_uid IN (:threadIds) " + " GROUP BY c.uid";

    @SuppressWarnings({ "unchecked" })
    private SortedSet<Comment> getNextThreadByThreadIdNewestFirst(final Long rootTopicId,
	    final Long previousThreadMessageId, Integer numberOfThreads, Integer sortBy, Integer userId) {

	// the search to get to the top level is quite light, so get just the uids
	// then build a complete set.
	List<Number> threadUidList = null;
	if (previousThreadMessageId == null || previousThreadMessageId == 0L) {
	    threadUidList = getSession().createSQLQuery(SQL_QUERY_FIND_FIRST_THREAD_TOP_BY_UID)
		    .setParameter("rootUid", rootTopicId).setMaxResults(numberOfThreads).list();
	} else {
	    threadUidList = getSession().createSQLQuery(SQL_QUERY_FIND_NEXT_THREAD_TOP).setParameter("rootUid", rootTopicId)
		    .setParameter("lastUid", previousThreadMessageId).setMaxResults(numberOfThreads).list();
	}

	if (threadUidList != null && threadUidList.size() > 0) {
	    NativeQuery<Object[]> query = getSession().createNativeQuery(SQL_QUERY_FIND_NEXT_THREAD_MESSAGES);
	    query.addEntity("comment", Comment.class).addScalar("likes_total", IntegerType.INSTANCE)
		    .addScalar("user_vote", IntegerType.INSTANCE).setParameter("userId", userId != null ? userId : 0)
		    .setParameterList("threadIds", threadUidList);
	    List<Object[]> results = query.list();
	    return upgradeComments(results, sortBy);
	}
	return new TreeSet<Comment>();
    }

    private static final String SQL_QUERY_FIND_FIRST_THREAD_TOP_BY_LIKES = "SELECT c.*, COALESCE(SUM(l.vote),0) likes_total, l2.vote user_vote"
	    + " FROM lams_comment c " + " LEFT JOIN lams_comment_likes l ON c.uid = l.comment_uid "
	    + " LEFT JOIN lams_comment_likes l2 ON c.uid = l2.comment_uid AND l2.user_id=:userId "
	    + " WHERE root_comment_uid = :rootUid AND comment_level = 1 AND sticky = 0 " + " GROUP BY c.uid "
	    + " ORDER BY likes_total DESC, c.uid DESC";

    private static final String SQL_QUERY_FIND_NEXT_THREAD_TOP_BY_LIKE = "SELECT * FROM ( "
	    + " SELECT c.*, COALESCE(SUM(l.vote),0) likes_total, l2.vote user_vote " + " FROM lams_comment c "
	    + " LEFT JOIN lams_comment_likes l ON c.uid = l.comment_uid "
	    + " LEFT JOIN lams_comment_likes l2 ON c.uid = l2.comment_uid AND l2.user_id=:userId "
	    + " WHERE root_comment_uid = :rootUid AND comment_level = 1 AND sticky = 0 " + " GROUP BY c.uid "
	    + " ORDER BY likes_total DESC, c.uid DESC) cl " + " WHERE (cl.likes_total = :like AND cl.uid < :lastUid ) "
	    + " OR cl.likes_total < :like";

    private static final String SQL_QUERY_FIND_NEXT_THREAD_MESSAGES_REPLIES_ONLY = "SELECT c.*, COALESCE(SUM(l.vote),0) likes_total, l2.vote user_vote FROM lams_comment c "
	    + " LEFT JOIN lams_comment_likes l ON c.uid = l.comment_uid "
	    + " LEFT JOIN lams_comment_likes l2 ON c.uid = l2.comment_uid AND l2.user_id=:userId "
	    + " WHERE c.thread_comment_uid IN (:threadIds) and comment_level > 1 " + " GROUP BY c.uid";

    @SuppressWarnings({ "unchecked" })
    private SortedSet<Comment> getNextThreadByThreadIdLikes(final Long rootTopicId, final Long previousThreadMessageId,
	    Integer numberOfThreads, Integer sortBy, String extraSortParam, Integer userId) {

	// the search to get to the top level is quite heavy and involves grouping the likes, so get all the data
	// for the top level then get the child replies.
	List<Object[]> topThreadObjects = null;
	if (previousThreadMessageId == null || previousThreadMessageId == 0L) {
	    topThreadObjects = getSession().createSQLQuery(SQL_QUERY_FIND_FIRST_THREAD_TOP_BY_LIKES)
		    .addEntity("comment", Comment.class).addScalar("likes_total", IntegerType.INSTANCE)
		    .addScalar("user_vote", IntegerType.INSTANCE).setParameter("rootUid", rootTopicId)
		    .setParameter("userId", userId != null ? userId : 0).setMaxResults(numberOfThreads).list();
	} else {
	    // get more entries with the same number of likes or less likes
	    topThreadObjects = getSession().createSQLQuery(SQL_QUERY_FIND_NEXT_THREAD_TOP_BY_LIKE)
		    .addEntity("comment", Comment.class).addScalar("likes_total", IntegerType.INSTANCE)
		    .addScalar("user_vote", IntegerType.INSTANCE).setParameter("rootUid", rootTopicId)
		    .setParameter("lastUid", previousThreadMessageId).setParameter("like", extraSortParam)
		    .setParameter("userId", userId != null ? userId : 0).setMaxResults(numberOfThreads).list();
	}
	if (topThreadObjects != null && topThreadObjects.size() > 0) {
	    // build the list of uids
	    List<Number> threadUidList = new ArrayList<Number>();
	    for (Object[] rawObject : topThreadObjects) {
		Comment comment = (Comment) rawObject[0];
		threadUidList.add(comment.getUid());
	    }
	   NativeQuery<Object[]> query = getSession().createNativeQuery(SQL_QUERY_FIND_NEXT_THREAD_MESSAGES_REPLIES_ONLY);
	    query.addEntity("comment", Comment.class).addScalar("likes_total", IntegerType.INSTANCE)
		    .addScalar("user_vote", IntegerType.INSTANCE).setParameter("userId", userId != null ? userId : 0)
		    .setParameterList("threadIds", threadUidList);
	    List<Object[]> results = query.list();
	    topThreadObjects.addAll(results);
	    return upgradeComments(topThreadObjects, sortBy);
	}
	return new TreeSet<Comment>();
    }

    @Override
    public SortedSet<Comment> getStickyThreads(final Long rootTopicId, Integer sortBy, String extraSortParam,
	    Integer userId) {

	if (ICommentService.SORT_BY_LIKE.equals(sortBy)) {
	    return getStickyByThreadIdLikes(rootTopicId, sortBy, extraSortParam, userId);
	} else {
	    return getStickyByThreadIdNewestFirst(rootTopicId, sortBy, userId);
	}

    }

    private static final String SQL_QUERY_FIND_STICKY_BY_UID = "SELECT uid FROM lams_comment"
	    + " WHERE root_comment_uid = :rootUid AND comment_level = 1 AND sticky = 1 ORDER BY uid DESC";

    @SuppressWarnings({ "unchecked" })
    private SortedSet<Comment> getStickyByThreadIdNewestFirst(final Long rootTopicId, Integer sortBy, Integer userId) {

	// the search to get to the top level is quite light, so get just the uids
	// then build a complete set.
	List<Number> threadUidList = getSession().createNativeQuery(SQL_QUERY_FIND_STICKY_BY_UID)
		.setParameter("rootUid", rootTopicId).list();

	if (threadUidList != null && threadUidList.size() > 0) {
	    NativeQuery<Object[]> query = getSession().createSQLQuery(SQL_QUERY_FIND_NEXT_THREAD_MESSAGES);
	    query.addEntity("comment", Comment.class).addScalar("likes_total", IntegerType.INSTANCE)
		    .addScalar("user_vote", IntegerType.INSTANCE).setParameter("userId", userId != null ? userId : 0)
		    .setParameterList("threadIds", threadUidList);
	    List<Object[]> results = query.list();
	    return upgradeComments(results, sortBy);
	}
	return new TreeSet<Comment>();
    }

    private static final String SQL_QUERY_FIND_STICKY_BY_LIKES = "SELECT c.*, COALESCE(SUM(l.vote),0) likes_total, l2.vote user_vote"
	    + " FROM lams_comment c " + " LEFT JOIN lams_comment_likes l ON c.uid = l.comment_uid "
	    + " LEFT JOIN lams_comment_likes l2 ON c.uid = l2.comment_uid AND l2.user_id=:userId "
	    + " WHERE root_comment_uid = :rootUid AND comment_level = 1  AND c.sticky = 1 " + " GROUP BY c.uid "
	    + " ORDER BY likes_total DESC, c.uid DESC";

    @SuppressWarnings({ "unchecked" })
    private SortedSet<Comment> getStickyByThreadIdLikes(final Long rootTopicId, Integer sortBy, String extraSortParam,
	    Integer userId) {

	List<Object[]> topThreadObjects = getSession().createNativeQuery(SQL_QUERY_FIND_STICKY_BY_LIKES)
		.addEntity("comment", Comment.class).addScalar("likes_total", IntegerType.INSTANCE)
		.addScalar("user_vote", IntegerType.INSTANCE).setParameter("rootUid", rootTopicId)
		.setParameter("userId", userId != null ? userId : 0).list();

	if (topThreadObjects != null && topThreadObjects.size() > 0) {
	    // build the list of uids
	    List<Number> threadUidList = new ArrayList<Number>();
	    for (Object[] rawObject : topThreadObjects) {
		Comment comment = (Comment) rawObject[0];
		threadUidList.add(comment.getUid());
	    }
	    NativeQuery<Object[]> query = getSession().createNativeQuery(SQL_QUERY_FIND_NEXT_THREAD_MESSAGES_REPLIES_ONLY);
	    query.addEntity("comment", Comment.class).addScalar("likes_total", IntegerType.INSTANCE)
		    .addScalar("user_vote", IntegerType.INSTANCE).setParameter("userId", userId != null ? userId : 0)
		    .setParameterList("threadIds", threadUidList);
	    List<Object[]> results = query.list();
	    topThreadObjects.addAll(results);
	    return upgradeComments(topThreadObjects, sortBy);
	}
	return new TreeSet<Comment>();
    }

}
