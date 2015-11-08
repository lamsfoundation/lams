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

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.lamsfoundation.lams.tool.forum.util.ForumConstants;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class ForumUserDao extends HibernateDaoSupport {

    private static final String SQL_QUERY_FIND_BY_USER_ID_SESSION_ID = "from " + ForumUser.class.getName() + " as f"
	    + " where user_id=? and f.session.sessionId=?";

    private static final String SQL_QUERY_FIND_BY_USER_ID = "from " + ForumUser.class.getName() + " as f"
	    + " where user_id=? and session_id is null";

    private static final String SQL_QUERY_FIND_BY_SESSION_ID = "from " + ForumUser.class.getName() + " as f "
	    + " where f.session.sessionId=?";


    public List<ForumUser> getBySessionId(Long sessionID) {
	return this.getHibernateTemplate().find(SQL_QUERY_FIND_BY_SESSION_ID, sessionID);
    }

    public void save(ForumUser forumUser) {
	this.getHibernateTemplate().save(forumUser);
    }

    public ForumUser getByUserIdAndSessionId(Long userId, Long sessionId) {
	List list = this.getHibernateTemplate().find(SQL_QUERY_FIND_BY_USER_ID_SESSION_ID,
		new Object[] { userId, sessionId });

	if (list == null || list.isEmpty())
	    return null;

	return (ForumUser) list.get(0);
    }

    public ForumUser getByUserId(Long userId) {
	List list = this.getHibernateTemplate().find(SQL_QUERY_FIND_BY_USER_ID, userId);

	if (list == null || list.isEmpty())
	    return null;

	return (ForumUser) list.get(0);
    }

    public ForumUser getByUid(Long userUid) {
	return (ForumUser) this.getHibernateTemplate().get(ForumUser.class, userUid);
    }

    @SuppressWarnings("unchecked")
    public List<ForumUser> getUsersForTablesorter(final Long sessionId, int page, int size, int sorting, String searchString) {
	String sortingOrder = "";
	switch (sorting) {
	case ForumConstants.SORT_BY_NO:
	    sortingOrder = "user.lastName";
	    break;
	case ForumConstants.SORT_BY_USER_NAME_ASC:
	    sortingOrder = "user.lastName ASC, user.firstName ASC";
	    break;
	case ForumConstants.SORT_BY_USER_NAME_DESC:
	    sortingOrder = "user.lastName DESC, user.firstName DESC";
	    break;
	case ForumConstants.SORT_BY_LAST_POSTING_ASC:
	    sortingOrder = " MAX(message.updated) ASC";
	    break;
	case ForumConstants.SORT_BY_LAST_POSTING_DESC:
	    sortingOrder = " MAX(message.updated) DESC";
	    break;
	case ForumConstants.SORT_BY_NUMBER_OF_POSTS_ASC:
	    sortingOrder = " COUNT(message) ASC";
	    break;
	case ForumConstants.SORT_BY_NUMBER_OF_POSTS_DESC:
	    sortingOrder = " COUNT(message) DESC";
	    break;
	}

	String filteredSearchString = buildNameSearch(searchString);

	String queryText = null;
	if (sorting == ForumConstants.SORT_BY_NUMBER_OF_POSTS_ASC
		|| sorting == ForumConstants.SORT_BY_NUMBER_OF_POSTS_DESC
		|| sorting == ForumConstants.SORT_BY_LAST_POSTING_ASC
		|| sorting == ForumConstants.SORT_BY_LAST_POSTING_DESC) {

	    queryText = "SELECT user FROM "
		    + Message.class.getName() + " as message " + " RIGHT JOIN message.createdBy as user "
		    + " WHERE user.session.sessionId=:sessionId "
		    + ( filteredSearchString != null ? filteredSearchString : "" )
		    + " GROUP BY user.userId ORDER BY " + sortingOrder;

	} else {

	    queryText = "from user in class ForumUser "
		    + "where user.session.sessionId=:sessionId " 
		    + ( filteredSearchString != null ? filteredSearchString : "" )
		    + " ORDER BY " + sortingOrder;
	}
	
	Query query = getSession().createQuery(queryText).setLong("sessionId", sessionId.longValue()).setFirstResult(page * size).setMaxResults(size);
	return query.list();

    }

    private String buildNameSearch(String searchString) {
	String filteredSearchString = null;
	if (!StringUtils.isBlank(searchString)) {
	    StringBuilder searchStringBuilder = new StringBuilder("");
	    String[] tokens = searchString.trim().split("\\s+");
	    for (String token : tokens) {
		String escToken = StringEscapeUtils.escapeSql(token);
		searchStringBuilder.append(" AND (user.firstName LIKE '%").append(escToken)
			.append("%' OR user.lastName LIKE '%").append(escToken)
			.append("%' OR user.loginName LIKE '%").append(escToken).append("%')");
	    }
	    filteredSearchString = searchStringBuilder.toString();
	}
	return filteredSearchString;
    } 

    @SuppressWarnings("rawtypes")
    public int getCountUsersBySession(final Long sessionId, String searchString) {

	String filteredSearchString = buildNameSearch(searchString);
	String queryText = "SELECT COUNT(*) from " 
		+ ForumUser.class.getName() 
		+ " as user where user.session.sessionId=:sessionId ";
	if ( filteredSearchString != null ) 
	    queryText += filteredSearchString;

	List list = getSession().createQuery(queryText).setLong("sessionId", sessionId.longValue()).list();

	if (list == null || list.size() == 0) {
	    return 0;
	}
	return ((Number) list.get(0)).intValue();
    }

    public void delete(ForumUser user) {
	this.getHibernateTemplate().delete(user);
    }

}
