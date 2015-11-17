/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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
package org.lamsfoundation.lams.tool.spreadsheet.dao.hibernate;

import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.spreadsheet.SpreadsheetConstants;
import org.lamsfoundation.lams.tool.spreadsheet.dao.SpreadsheetUserDAO;
import org.lamsfoundation.lams.tool.spreadsheet.model.SpreadsheetUser;


public class SpreadsheetUserDAOHibernate extends BaseDAOHibernate implements SpreadsheetUserDAO{
	
	private static final String FIND_BY_USER_ID_CONTENT_ID = "from " + SpreadsheetUser.class.getName() + " as u where u.userId =? and u.spreadsheet.contentId=?";
	private static final String FIND_BY_USER_ID_SESSION_ID = "from " + SpreadsheetUser.class.getName() + " as u where u.userId =? and u.session.sessionId=?";
	private static final String FIND_BY_SESSION_ID = "from " + SpreadsheetUser.class.getName() + " as u where u.session.sessionId=?";

	public SpreadsheetUser getUserByUserIDAndSessionID(Long userID, Long sessionId) {
		List list = this.getHibernateTemplate().find(FIND_BY_USER_ID_SESSION_ID,new Object[]{userID,sessionId});
		if(list == null || list.size() == 0)
			return null;
		return (SpreadsheetUser) list.get(0);
	}

	public SpreadsheetUser getUserByUserIDAndContentID(Long userId, Long contentId) {
		List list = this.getHibernateTemplate().find(FIND_BY_USER_ID_CONTENT_ID,new Object[]{userId,contentId});
		if(list == null || list.size() == 0)
			return null;
		return (SpreadsheetUser) list.get(0);
	}

	public List<SpreadsheetUser> getBySessionID(Long sessionId) {
		return this.getHibernateTemplate().find(FIND_BY_SESSION_ID,sessionId);
	}

	@SuppressWarnings("unchecked")
	public List<SpreadsheetUser> getUsersForTablesorter(final Long sessionId, int page, int size, int sorting, String searchString) {
	    String sortingOrder;
	    switch (sorting) {
	    case SpreadsheetConstants.SORT_BY_USERNAME_ASC:
		sortingOrder = "user.lastName ASC, user.firstName ASC";
		break;
	    case SpreadsheetConstants.SORT_BY_USERNAME_DESC:
		sortingOrder = "user.lastName DESC, user.firstName DESC";
		break;
	    case SpreadsheetConstants.SORT_BY_MARKED_ASC:
		sortingOrder = " mark.marks ASC";
		break;
	    case SpreadsheetConstants.SORT_BY_MARKED_DESC:
		sortingOrder = " mark.marks DESC";
		break;
	    default:
		sortingOrder = "user.lastName, user.firstName";
	    }

	    String filteredSearchString = buildNameSearch(searchString);
	    String queryText = "SELECT user FROM " + SpreadsheetUser.class.getName() + " as user ";
	    if (sorting == SpreadsheetConstants.SORT_BY_MARKED_ASC
		    || sorting == SpreadsheetConstants.SORT_BY_MARKED_DESC) {
		queryText += " LEFT JOIN user.userModifiedSpreadsheet as ums "
			+ " LEFT JOIN ums.mark as mark ";
	    }

	    queryText+=  " WHERE user.session.sessionId=:sessionId "
		    + ( filteredSearchString != null ? filteredSearchString : "" )
		    + " ORDER BY " + sortingOrder;

	    return getSession().createQuery(queryText).setLong("sessionId", sessionId.longValue()).setFirstResult(page * size).setMaxResults(size).list();
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
	    String queryText = "SELECT count(*) FROM " + SpreadsheetUser.class.getName() + " user WHERE user.session.sessionId=:sessionId ";
	    if ( filteredSearchString != null ) 
		queryText += filteredSearchString;
	    
	    List list = getSession().createQuery(queryText).setLong("sessionId", sessionId.longValue()).list();

	    if (list == null || list.size() == 0) {
		return 0;
	    }
	    return ((Number) list.get(0)).intValue();
	}


}
