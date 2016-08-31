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



package org.lamsfoundation.lams.tool.sbmt.dao.hibernate;

import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
import org.lamsfoundation.lams.tool.sbmt.SubmitUser;
import org.lamsfoundation.lams.tool.sbmt.dao.ISubmitUserDAO;
import org.lamsfoundation.lams.tool.sbmt.dto.StatisticDTO;
import org.lamsfoundation.lams.tool.sbmt.util.SbmtConstants;
import org.springframework.stereotype.Repository;

@Repository
public class SubmitUserDAO extends LAMSBaseDAO implements ISubmitUserDAO {
    private static final String FIND_BY_USER_ID_SESSION_ID = "from " + SubmitUser.class.getName()
	    + " where user_id=? and session_id=?";
    private static final String FIND_BY_USER_ID_CONTENT_ID = "from " + SubmitUser.class.getName()
	    + " where user_id=? and content_id=?";

    private static final String FIND_BY_SESSION_ID = "from " + SubmitUser.class.getName() + " where session_id=?";

    @Override
    public SubmitUser getLearner(Long sessionID, Integer userID) {
	List list = doFind(FIND_BY_USER_ID_SESSION_ID, new Object[] { userID, sessionID });
	if (list.size() > 0) {
	    return (SubmitUser) list.get(0);
	} else {
	    return null;
	}
    }

    @Override
    public SubmitUser getContentUser(Long contentId, Integer userID) {
	List list = doFind(FIND_BY_USER_ID_CONTENT_ID, new Object[] { userID, contentId });
	if (list.size() > 0) {
	    return (SubmitUser) list.get(0);
	} else {
	    return null;
	}
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<SubmitUser> getUsersBySession(Long sessionID) {
	return (List<SubmitUser>) doFind(FIND_BY_SESSION_ID, sessionID);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.sbmt.dao.ILearnerDAO#updateLearer(org.lamsfoundation.lams.tool.sbmt.Learner)
     */
    @Override
    public void saveOrUpdateUser(SubmitUser learner) {
	this.insertOrUpdate(learner);
    }

    @Override
    @SuppressWarnings("unchecked")
    /**
     * Will return List<[SubmitUser, Integer1, Integer2, String], [SubmitUser, Integer1, Integer2, String], ... ,
     * [SubmitUser, Integer1, Integer2, String]>
     * where Integer1 is the number of files uploaded, Integer2 is the number of files marked
     * and String is the notebook entry. No notebook entries needed? Will return null in their place.
     */
    public List<Object[]> getUsersForTablesorter(final Long sessionId, int page, int size, int sorting,
	    String searchString, boolean getNotebookEntries, ICoreNotebookService coreNotebookService) {
	String sortingOrder;
	switch (sorting) {
	    case SbmtConstants.SORT_BY_USERNAME_ASC:
		sortingOrder = "user.last_name ASC, user.first_name ASC";
		break;
	    case SbmtConstants.SORT_BY_USERNAME_DESC:
		sortingOrder = "user.last_name DESC, user.first_name DESC";
		break;
	    case SbmtConstants.SORT_BY_NUM_FILES_ASC:
		sortingOrder = " numFiles ASC";
		break;
	    case SbmtConstants.SORT_BY_NUM_FILES_DESC:
		sortingOrder = " numFiles DESC";
		break;
	    // when sorting by number marked, keep the "can be marked" ones together, to make it easier for marking time
	    case SbmtConstants.SORT_BY_MARKED_ASC:
		sortingOrder = " numFilesMarked ASC, numFiles ASC";
		break;
	    case SbmtConstants.SORT_BY_MARKED_DESC:
		sortingOrder = " numFilesMarked DESC, numFiles DESC";
		break;
	    default:
		sortingOrder = "user.last_name, user.first_name";
	}

	// If the session uses notebook, then get the sql to join across to get the entries
	String[] notebookEntryStrings = null;
	if (getNotebookEntries) {
	    notebookEntryStrings = coreNotebookService.getNotebookEntrySQLStrings(sessionId.toString(),
		    SbmtConstants.TOOL_SIGNATURE, "user.user_id");
	}

	// Basic select for the user records
	StringBuilder queryText = new StringBuilder();
	queryText.append("SELECT user.*, COUNT(details.submission_id) numFiles, COALESCE(SUM(details.removed),0) numFilesRemoved, count(report.marks) numFilesMarked ");
	queryText.append(notebookEntryStrings != null ? notebookEntryStrings[0] : ", NULL notebookEntry");
	queryText.append(" FROM tl_lasbmt11_user user ");
	queryText.append(" LEFT JOIN tl_lasbmt11_submission_details details ON user.uid = details.learner_id ");
	queryText.append(" LEFT JOIN tl_lasbmt11_report report ON details.submission_id = report.report_id ");

	// If using notebook, add the notebook join
	if (notebookEntryStrings != null) {
	    queryText.append(notebookEntryStrings[1]);
	}

	queryText.append(" WHERE user.session_id = :sessionId");

	// If filtering by name add a name based where clause
	buildNameSearch(searchString, queryText);

	// finally group by user for the file counts
	queryText.append(" GROUP BY user.uid");

	// Now specify the sort based on the switch statement above.
	queryText.append(" ORDER BY " + sortingOrder);

	SQLQuery query = getSession().createSQLQuery(queryText.toString());
	query.addEntity("user", SubmitUser.class)
		.addScalar("numFiles", IntegerType.INSTANCE).addScalar("numFilesRemoved", IntegerType.INSTANCE)
		.addScalar("numFilesMarked", IntegerType.INSTANCE).addScalar("notebookEntry", StringType.INSTANCE)
		.setLong("sessionId", sessionId.longValue()).setFirstResult(page * size).setMaxResults(size);
	return query.list(); 
    }

    private void buildNameSearch(String searchString, StringBuilder sqlBuilder) {
	if (!StringUtils.isBlank(searchString)) {
	    String[] tokens = searchString.trim().split("\\s+");
	    for (String token : tokens) {
		String escToken = StringEscapeUtils.escapeSql(token);
		sqlBuilder.append(" AND (user.first_name LIKE '%").append(escToken)
			.append("%' OR user.last_name LIKE '%").append(escToken).append("%' OR user.login_name LIKE '%")
			.append(escToken).append("%') ");
	    }
	}
    }

    @Override
    @SuppressWarnings("rawtypes")
    public int getCountUsersBySession(final Long sessionId, String searchString) {

	StringBuilder queryText = new StringBuilder(
		"SELECT count(*) FROM tl_lasbmt11_user user WHERE user.session_id = :sessionId ");
	buildNameSearch(searchString, queryText);

	List list = getSession().createSQLQuery(queryText.toString()).setLong("sessionId", sessionId).list();
	if (list == null || list.size() == 0) {
	    return 0;
	}
	return ((Number) list.get(0)).intValue();
    }

    private static final String GET_STATISTICS = "SELECT session.session_id sessionId, session.session_name sessionName, "
	    + " COUNT(detail.submission_id) totalUploadedFiles, COUNT(report.marks) markedCount"
	    + " FROM tl_lasbmt11_session session, tl_lasbmt11_submission_details detail, tl_lasbmt11_report report "
	    + " WHERE session.content_id = :contentId and detail.session_id = session.session_id "
	    + " AND detail.submission_id = report.report_id " + " GROUP BY session.session_id";

    @Override
    @SuppressWarnings("unchecked")
    public List<StatisticDTO> getStatisticsBySession(final Long contentId) {

	SQLQuery query = getSession().createSQLQuery(GET_STATISTICS);
	query.addScalar("sessionId", LongType.INSTANCE).addScalar("sessionName", StringType.INSTANCE)
		.addScalar("totalUploadedFiles", IntegerType.INSTANCE).addScalar("markedCount", IntegerType.INSTANCE)
		.setLong("contentId", contentId).setResultTransformer(Transformers.aliasToBean(StatisticDTO.class));

	List<StatisticDTO> list = query.list();
	for (StatisticDTO dto : list) {
	    dto.setNotMarkedCount(dto.getTotalUploadedFiles() - dto.getMarkedCount());
	}
	return list;
    }

}
