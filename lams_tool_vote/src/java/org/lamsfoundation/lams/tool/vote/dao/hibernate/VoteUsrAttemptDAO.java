/***************************************************************************
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
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.vote.dao.hibernate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BooleanType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
import org.lamsfoundation.lams.tool.vote.VoteAppConstants;
import org.lamsfoundation.lams.tool.vote.dao.IVoteUsrAttemptDAO;
import org.lamsfoundation.lams.tool.vote.dto.OpenTextAnswerDTO;
import org.lamsfoundation.lams.tool.vote.dto.VoteStatsDTO;
import org.lamsfoundation.lams.tool.vote.model.VoteUsrAttempt;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.springframework.stereotype.Repository;

/**
 * Hibernate implementation for database access to VoteUsrAttemptDAO for the voting tool.
 *
 * @author Ozgur Demirtas repaired by lfoxton
 */
@Repository
public class VoteUsrAttemptDAO extends LAMSBaseDAO implements IVoteUsrAttemptDAO {

    private static final String LOAD_ATTEMPT_FOR_USER = "from voteUsrAttempt in class VoteUsrAttempt where voteUsrAttempt.voteQueUsr.uid=:queUsrId";

    // The following two queries are the same except one loads the attempts, the other counts them
    private static final String LOAD_ATTEMPT_FOR_QUESTION_CONTENT_AND_SESSION = "from voteUsrAttempt in class VoteUsrAttempt where voteUsrAttempt.voteQueContent.uid=:voteQueContentId and voteUsrAttempt.voteQueUsr.voteSession.uid=:sessionUid";
    private static final String COUNT_ATTEMPT_FOR_QUESTION_CONTENT_AND_SESSION = "select count(*) from voteUsrAttempt in class VoteUsrAttempt where voteUsrAttempt.voteQueContent.uid=:voteQueContentId and voteUsrAttempt.voteQueUsr.voteSession.uid=:sessionUid";

    private static final String LOAD_ATTEMPT_FOR_USER_AND_QUESTION_CONTENT = "from voteUsrAttempt in class VoteUsrAttempt where voteUsrAttempt.voteQueUsr.uid=:queUsrId and voteUsrAttempt.voteQueContent.uid=:voteQueContentId";

    private static final String LOAD_ATTEMPT_FOR_USER_AND_QUESTION_CONTENT_AND_SESSION = "from voteUsrAttempt in class VoteUsrAttempt where voteUsrAttempt.voteQueUsr.uid=:queUsrId and voteUsrAttempt.voteQueContent.uid=:voteQueContentId and voteUsrAttempt.voteQueUsr.voteSession.uid=:sessionUid";

    private static final String LOAD_ATTEMPT_FOR_USER_AND_SESSION = "from voteUsrAttempt in class VoteUsrAttempt where voteUsrAttempt.voteQueUsr.uid=:queUsrId and voteUsrAttempt.voteQueUsr.voteSession.uid=:sessionUid";

    private static final String LOAD_USER_ENTRIES = "select distinct voteUsrAttempt.userEntry from VoteUsrAttempt voteUsrAttempt where voteUsrAttempt.voteQueUsr.voteSession.voteContent.uid=:voteContentUid";

    private static final String LOAD_USER_ENTRY_RECORDS = "from voteUsrAttempt in class VoteUsrAttempt where voteUsrAttempt.userEntry=:userEntry and voteUsrAttempt.voteQueContent.uid=1 and voteUsrAttempt.voteQueUsr.voteSession.voteContent.uid=:voteContentUid";

    private static final String LOAD_OPEN_TEXT_ENTRIES_BY_SESSION_UID = "select att from VoteUsrAttempt att, VoteQueUsr user, VoteSession ses where "
	    + "att.voteQueUsr=user and user.voteSession=ses and ses.uid=:voteSessionUid and att.userEntry is not null and att.userEntry <> \'\'";

    private static final String COUNT_ENTRIES_BY_SESSION_ID = "select count(*) from VoteUsrAttempt att, VoteQueUsr user, VoteSession ses where "
	    + "att.voteQueUsr=user and user.voteSession=ses and ses.uid=:voteSessionUid";

    @SuppressWarnings("unchecked")
    @Override
    public VoteUsrAttempt getAttemptByUID(Long uid) {
	String query = "from VoteUsrAttempt attempt where attempt.uid=:attemptUid";

	List<VoteUsrAttempt> list = getSessionFactory().getCurrentSession().createQuery(query)
		.setParameter("attemptUid", uid).list();

	if ((list != null) && (list.size() > 0)) {
	    VoteUsrAttempt attempt = list.get(0);
	    return attempt;
	}
	return null;
    }

    @Override
    public void saveVoteUsrAttempt(VoteUsrAttempt voteUsrAttempt) {
	this.getSession().save(voteUsrAttempt);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<VoteUsrAttempt> getAttemptsForUser(final Long queUsrId) {
	List<VoteUsrAttempt> list = getSessionFactory().getCurrentSession()
		.createQuery(VoteUsrAttemptDAO.LOAD_ATTEMPT_FOR_USER).setParameter("queUsrId", queUsrId).list();
	return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<String> getUserEntries(final Long voteContentUid) {
	List<String> list = getSessionFactory().getCurrentSession().createQuery(VoteUsrAttemptDAO.LOAD_USER_ENTRIES)
		.setParameter("voteContentUid", voteContentUid).list();

	Set<String> userEntries = new HashSet<>();
	if ((list != null) && (list.size() > 0)) {
	    Iterator<String> listIterator = list.iterator();
	    while (listIterator.hasNext()) {
		String entry = listIterator.next();
		if (entry != null && entry.length() > 0) {
		    userEntries.add(entry);
		}
	    }
	}
	return userEntries;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<VoteUsrAttempt> getUserAttempts(final Long voteContentUid, final String userEntry) {
	List<VoteUsrAttempt> list = getSessionFactory().getCurrentSession()
		.createQuery(VoteUsrAttemptDAO.LOAD_USER_ENTRY_RECORDS).setParameter("voteContentUid", voteContentUid)
		.setParameter("userEntry", userEntry).list();
	return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<VoteUsrAttempt> getSessionOpenTextUserEntries(final Long voteSessionUid) {
	return getSession().createQuery(VoteUsrAttemptDAO.LOAD_OPEN_TEXT_ENTRIES_BY_SESSION_UID)
		.setParameter("voteSessionUid", voteSessionUid).list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void removeAttemptsForUserandSession(final Long queUsrId, final Long sessionUid) {
	String strGetUser = "from voteUsrAttempt in class VoteUsrAttempt where voteUsrAttempt.voteQueUsr.uid=:queUsrId and voteUsrAttempt.voteQueUsr.voteSession.uid=:sessionUid";
	List<VoteUsrAttempt> list = getSessionFactory().getCurrentSession().createQuery(strGetUser)
		.setParameter("queUsrId", queUsrId.longValue()).setParameter("sessionUid", sessionUid).list();

	if ((list != null) && (list.size() > 0)) {
	    Iterator<VoteUsrAttempt> listIterator = list.iterator();
	    while (listIterator.hasNext()) {
		VoteUsrAttempt attempt = listIterator.next();
		getSession().delete(attempt);
		getSession().flush();
	    }
	}
    }

    @SuppressWarnings("unchecked")
    @Override
    public int getStandardAttemptsForQuestionContentAndSessionUid(final Long questionUid, final Long sessionUid) {
	List<Number> list = getSession().createQuery(VoteUsrAttemptDAO.COUNT_ATTEMPT_FOR_QUESTION_CONTENT_AND_SESSION)
		.setParameter("voteQueContentId", questionUid).setParameter("sessionUid", sessionUid).list();

	if (list == null || list.size() == 0) {
	    return 0;
	}
	return list.get(0).intValue();

    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public List<VoteUsrAttempt> getAttemptsForQuestionContentAndSessionUid(final Long questionUid,
	    final Long sessionUid) {
	List<VoteUsrAttempt> list = getSessionFactory().getCurrentSession()
		.createQuery(VoteUsrAttemptDAO.LOAD_ATTEMPT_FOR_QUESTION_CONTENT_AND_SESSION)
		.setParameter("voteQueContentId", questionUid).setParameter("sessionUid", sessionUid).list();

	List<VoteUsrAttempt> userEntries = new ArrayList();
	if ((list != null) && (list.size() > 0)) {
	    userEntries.addAll(list);
	}
	return userEntries;

    }

    @SuppressWarnings("unchecked")
    @Override
    public List<VoteUsrAttempt> getAttemptsForUserAndQuestionContent(final Long queUsrId, final Long questionUid) {
	List<VoteUsrAttempt> list = getSessionFactory().getCurrentSession()
		.createQuery(VoteUsrAttemptDAO.LOAD_ATTEMPT_FOR_USER_AND_QUESTION_CONTENT)
		.setParameter("queUsrId", queUsrId).setParameter("voteQueContentId", questionUid).list();

	return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public VoteUsrAttempt getAttemptForUserAndQuestionContentAndSession(final Long queUsrId, final Long questionUid,
	    final Long sessionUid) {
	List<VoteUsrAttempt> list = getSessionFactory().getCurrentSession()
		.createQuery(VoteUsrAttemptDAO.LOAD_ATTEMPT_FOR_USER_AND_QUESTION_CONTENT_AND_SESSION)
		.setParameter("queUsrId", queUsrId).setParameter("voteQueContentId", questionUid)
		.setParameter("sessionUid", sessionUid).list();

	if ((list == null) || (list.size() == 0)) {
	    return null;
	}
	return list.get(0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<String> getAttemptsForUserAndSession(final Long queUsrId, final Long sessionUid) {

	List<VoteUsrAttempt> list = getSessionFactory().getCurrentSession()
		.createQuery(VoteUsrAttemptDAO.LOAD_ATTEMPT_FOR_USER_AND_SESSION).setParameter("queUsrId", queUsrId)
		.setParameter("sessionUid", sessionUid).list();

	Set<String> userEntries = new HashSet<>();
	if ((list != null) && (list.size() > 0)) {
	    Iterator<VoteUsrAttempt> listIterator = list.iterator();
	    while (listIterator.hasNext()) {
		VoteUsrAttempt attempt = listIterator.next();

		Long questionUid = attempt.getVoteQueContent().getUid();
		if (!questionUid.toString().equals("1")) {
		    userEntries.add(attempt.getVoteQueContent().getQuestion());
		}
	    }
	}
	return userEntries;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<VoteUsrAttempt> getAttemptsForUserAndSessionUseOpenAnswer(final Long queUsrId, final Long sessionUid) {

	return getSession().createQuery(VoteUsrAttemptDAO.LOAD_ATTEMPT_FOR_USER_AND_SESSION)
		.setParameter("queUsrId", queUsrId).setParameter("sessionUid", sessionUid).list();

    }

    @SuppressWarnings("unchecked")
    @Override
    public int getSessionEntriesCount(final Long voteSessionUid) {
	List<Long> result = getSessionFactory().getCurrentSession()
		.createQuery(VoteUsrAttemptDAO.COUNT_ENTRIES_BY_SESSION_ID)
		.setParameter("voteSessionUid", voteSessionUid).list();
	Long resultLong = result.get(0) != null ? (Long) result.get(0) : 0L;
	return resultLong.intValue();
    }

    @Override
    public void updateVoteUsrAttempt(VoteUsrAttempt voteUsrAttempt) {
	this.getSession().update(voteUsrAttempt);
    }

    @Override
    public void removeVoteUsrAttempt(VoteUsrAttempt voteUsrAttempt) {
	this.getSession().delete(voteUsrAttempt);
    }

    // Used by Monitoring

    private static final String FIND_USER_ANSWERS_BY_QUESTION_UID_SELECT = "SELECT user.user_id, user.username username, user.fullname fullname, attempt.attempt_time attemptTime ";
    private static final String FIND_USER_ANSWERS_BY_QUESTION_UID_FROM = " FROM tl_lavote11_usr user "
	    + " JOIN tl_lavote11_usr_attempt attempt on user.uid = attempt.que_usr_id AND attempt.vote_nomination_content_id = :questionUid ";
    private static final String FIND_USER_ANSWERS_BY_QUESTION_UID_SESSION_ADDITION = " AND user.vote_session_id = :sessionUid ";

    @Override
    @SuppressWarnings("unchecked")
    /**
     * Gets the basic details about an attempt for a nomination. questionUid must not be null, sessionUid may be NULL.
     * This is
     * unusual for these methods - usually sessionId may not be null. In this case if sessionUid is null then you get
     * the values for the whole class, not just the group.
     *
     * Will return List<[login (String), fullname(String), attemptTime(Timestamp]>
     */
    public List<Object[]> getUserAttemptsForTablesorter(Long sessionUid, Long questionUid, int page, int size,
	    int sorting, String searchString, IUserManagementService userManagementService) {
	String sortingOrder;
	switch (sorting) {
	    case VoteAppConstants.SORT_BY_NAME_ASC:
		sortingOrder = "user.fullname ASC";
		break;
	    case VoteAppConstants.SORT_BY_NAME_DESC:
		sortingOrder = "user.fullname DESC";
		break;
	    case VoteAppConstants.SORT_BY_DATE_ASC:
		sortingOrder = "attempt.attempt_time ASC";
		break;
	    case VoteAppConstants.SORT_BY_DATE_DESC:
		sortingOrder = "attempt.attempt_time DESC";
		break;
	    default:
		sortingOrder = "user.uid";
	}

	String[] portraitStrings = userManagementService.getPortraitSQL("user.user_id");

	// Basic select for the user records
	StringBuilder queryText = new StringBuilder(FIND_USER_ANSWERS_BY_QUESTION_UID_SELECT).append(portraitStrings[0])
		.append(FIND_USER_ANSWERS_BY_QUESTION_UID_FROM).append(portraitStrings[1]);

	if (sessionUid != null) {
	    queryText.append(FIND_USER_ANSWERS_BY_QUESTION_UID_SESSION_ADDITION);
	}

	// If filtering by name add a name based where clause
	buildNameSearch(searchString, queryText, true);

	// Now specify the sort based on the switch statement above.
	queryText.append(" ORDER BY " + sortingOrder);

	NativeQuery<Object[]> query = getSession().createSQLQuery(queryText.toString());
	query.addScalar("user_id", IntegerType.INSTANCE).addScalar("username", StringType.INSTANCE)
		.addScalar("fullname", StringType.INSTANCE).addScalar("attemptTime", TimestampType.INSTANCE)
		.addScalar("portraitId", StringType.INSTANCE).setParameter("questionUid", questionUid)
		.setFirstResult(page * size).setMaxResults(size);
	if (sessionUid != null) {
	    query.setParameter("sessionUid", sessionUid);
	}

	return query.list();
    }

    private void buildNameSearch(String searchString, StringBuilder sqlBuilder, boolean useWhere) {
	if (!StringUtils.isBlank(searchString)) {
	    String[] tokens = searchString.trim().split("\\s+");
	    for (String token : tokens) {
		String escToken = StringEscapeUtils.escapeSql(token);
		sqlBuilder.append(useWhere ? " WHERE " : " AND ").append("(user.fullname LIKE '%").append(escToken)
			.append("%' OR user.username LIKE '%").append(escToken).append("%') ");
	    }
	}
    }

    private static final String COUNT_USERS_BY_QUESTION_UID = "SELECT count(*) " + " FROM tl_lavote11_usr user "
	    + " JOIN tl_lavote11_usr_attempt attempt ON user.uid = attempt.que_usr_id AND attempt.vote_nomination_content_id = :questionUid ";
    private static final String COUNT_USERS_BY_QUESTION_UID_SESSION_ADDITION = " AND user.vote_session_id = :sessionUid ";

    private static final String COUNT_USERS_BY_SESSION_UID = "SELECT count(*) "
	    + " FROM tl_lavote11_usr user WHERE user.vote_session_id = :sessionUid ";

    @Override
    @SuppressWarnings("rawtypes")
    public int getCountUsersBySession(Long sessionUid, Long questionUid, String searchString) {

	NativeQuery query;

	if (questionUid == null) {

	    // get all the users in this session - used for reflections
	    StringBuilder queryText = new StringBuilder(COUNT_USERS_BY_SESSION_UID);
	    buildNameSearch(searchString, queryText, false); // all ready have a WHERE so need an AND
	    query = getSession().createSQLQuery(queryText.toString());
	    query.setParameter("sessionUid", sessionUid);

	} else {

	    // get all the users by a question, possibly restricting by session
	    StringBuilder queryText = new StringBuilder(COUNT_USERS_BY_QUESTION_UID);
	    if (sessionUid != null) {
		queryText.append(COUNT_USERS_BY_QUESTION_UID_SESSION_ADDITION);
	    }
	    buildNameSearch(searchString, queryText, true);

	    query = getSession().createSQLQuery(queryText.toString());
	    query.setParameter("questionUid", questionUid);
	    if (sessionUid != null) {
		query.setParameter("sessionUid", sessionUid);
	    }
	}

	List list = query.list();
	if (list == null || list.size() == 0) {
	    return 0;
	}
	return ((Number) list.get(0)).intValue();
    }

    @Override
    @SuppressWarnings("unchecked")
    /**
     * Will return List<[login (String), fullname(String), String (notebook entry)]>
     */
    public List<Object[]> getUserReflectionsForTablesorter(final Long sessionUid, int page, int size, int sorting,
	    String searchString, ICoreNotebookService coreNotebookService,
	    IUserManagementService userManagementService) {
	String sortingOrder;
	switch (sorting) {
	    case VoteAppConstants.SORT_BY_NAME_ASC:
		sortingOrder = "user.fullname ASC";
		break;
	    case VoteAppConstants.SORT_BY_NAME_DESC:
		sortingOrder = "user.fullname DESC";
		break;
	    default:
		sortingOrder = "user.uid";
	}

	// If the session uses notebook, then get the sql to join across to get the entries
	String[] notebookEntryStrings = coreNotebookService.getNotebookEntrySQLStrings("session.vote_session_id",
		VoteAppConstants.MY_SIGNATURE, "user.user_id");

	String[] portraitStrings = userManagementService.getPortraitSQL("user.user_id");

	// Basic select for the user records
	StringBuilder queryText = new StringBuilder();
	queryText.append("SELECT user.user_id user_id, user.username username, user.fullname fullname ");
	queryText.append(notebookEntryStrings[0]);
	queryText.append(portraitStrings[0]);
	queryText.append(" FROM tl_lavote11_usr user ");
	queryText.append(
		" JOIN tl_lavote11_session session ON user.vote_session_id = :sessionUid AND user.vote_session_id = session.uid ");

	// Add the notebook join
	queryText.append(notebookEntryStrings[1]);
	queryText.append(portraitStrings[1]);

	// If filtering by name add a name based where clause
	buildNameSearch(searchString, queryText, true);

	// Now specify the sort based on the switch statement above.
	queryText.append(" ORDER BY " + sortingOrder);

	NativeQuery<Object[]> query = getSession().createSQLQuery(queryText.toString());
	query.addScalar("user_id", IntegerType.INSTANCE).addScalar("username", StringType.INSTANCE)
		.addScalar("fullname", StringType.INSTANCE).addScalar("notebookEntry", StringType.INSTANCE)
		.addScalar("portraitId", StringType.INSTANCE).setParameter("sessionUid", sessionUid)
		.setFirstResult(page * size).setMaxResults(size);

	return query.list();
    }

    private static final String FIND_USER_OPEN_TEXT_SELECT = "SELECT user.uid userUid, user.username login, user.fullname fullName, "
	    + " attempt.uid userEntryUid, attempt.userEntry userEntry, attempt.attempt_time attemptTime, attempt.visible visible ";

    private static final String FIND_USER_OPEN_TEXT_FROM = " FROM tl_lavote11_usr user "
	    + " JOIN tl_lavote11_usr_attempt attempt ON user.uid = attempt.que_usr_id AND attempt.vote_nomination_content_id = 1 ";

    private static final String FIND_USER_OPEN_TEXT_SESSION_UID_ADD = "AND user.vote_session_id=:sessionUid";
    private static final String FIND_USER_OPEN_TEXT_CONTENT_UID_ADD = "JOIN tl_lavote11_session session ON user.vote_session_id = session.uid "
	    + " JOIN tl_lavote11_content content ON session.vote_content_id = content.uid and content.content_id = :toolContentId";

    @Override
    @SuppressWarnings("unchecked")
    /**
     * Gets the details about an open text entry. Either sessionUid or toolContentId must be supplied - if sessionUid is
     * supplied
     * then it will be restricted to that session. Due to the large number of fields needed, a DTO will be returned.
     *
     * Will return List<OpenTextAnswerDTO>
     */
    public List<OpenTextAnswerDTO> getUserOpenTextAttemptsForTablesorter(Long sessionUid, Long toolContentId, int page,
	    int size, int sorting, String searchStringVote, String searchStringUsername,
	    IUserManagementService userManagementService) {
	String sortingOrder;
	switch (sorting) {
	    case VoteAppConstants.SORT_BY_NAME_ASC:
		sortingOrder = "user.fullname ASC";
		break;
	    case VoteAppConstants.SORT_BY_NAME_DESC:
		sortingOrder = "user.fullname DESC";
		break;
	    case VoteAppConstants.SORT_BY_DATE_ASC:
		sortingOrder = "attempt.attempt_time ASC";
		break;
	    case VoteAppConstants.SORT_BY_DATE_DESC:
		sortingOrder = "attempt.attempt_time DESC";
		break;
	    case VoteAppConstants.SORT_BY_ENTRY_ASC:
		sortingOrder = "attempt.userEntry ASC";
		break;
	    case VoteAppConstants.SORT_BY_ENTRY_DESC:
		sortingOrder = "attempt.userEntry DESC";
		break;
	    case VoteAppConstants.SORT_BY_VISIBLE_ASC:
		sortingOrder = "attempt.visible ASC";
		break;
	    case VoteAppConstants.SORT_BY_VISIBLE_DESC:
		sortingOrder = "attempt.visible DESC";
		break;
	    default:
		sortingOrder = "user.uid";
	}

	String[] portraitStrings = userManagementService.getPortraitSQL("user.user_id");

	// Basic select for the user records
	StringBuilder queryText = new StringBuilder(FIND_USER_OPEN_TEXT_SELECT).append(portraitStrings[0])
		.append(FIND_USER_OPEN_TEXT_FROM);

	if (sessionUid != null) {
	    queryText.append(FIND_USER_OPEN_TEXT_SESSION_UID_ADD);
	} else {
	    queryText.append(FIND_USER_OPEN_TEXT_CONTENT_UID_ADD);
	}
	queryText.append(portraitStrings[1]);

	// If filtering by name/entry add a where clause
	buildCombinedSearch(searchStringVote, searchStringUsername, queryText);

	// Now specify the sort based on the switch statement above.
	queryText.append(" ORDER BY " + sortingOrder);

	NativeQuery<OpenTextAnswerDTO> query = getSession().createSQLQuery(queryText.toString());
	query.addScalar("userUid", LongType.INSTANCE).addScalar("login", StringType.INSTANCE)
		.addScalar("fullName", StringType.INSTANCE).addScalar("userEntryUid", LongType.INSTANCE)
		.addScalar("userEntry", StringType.INSTANCE).addScalar("attemptTime", TimestampType.INSTANCE)
		.addScalar("visible", BooleanType.INSTANCE).addScalar("portraitId", StringType.INSTANCE)
		.setFirstResult(page * size).setMaxResults(size)
		.setResultTransformer(Transformers.aliasToBean(OpenTextAnswerDTO.class));

	if (sessionUid != null) {
	    query.setParameter("sessionUid", sessionUid);
	} else {
	    query.setParameter("toolContentId", toolContentId);
	}

	return query.list();
    }

    private void buildCombinedSearch(String searchStringVote, String searchStringUsername, StringBuilder sqlBuilder) {

	if (!StringUtils.isBlank(searchStringVote)) {
	    String[] tokens = searchStringVote.trim().split("\\s+");
	    for (String token : tokens) {
		String escToken = StringEscapeUtils.escapeSql(token);
		sqlBuilder.append(" WHERE (userEntry LIKE '%").append(escToken).append("%') ");
	    }
	} else {
	    buildNameSearch(searchStringUsername, sqlBuilder, true);
	}
    }

    private static final String COUNT_USERS_OPEN_TEXT_BY_SESSION_UID = "SELECT count(*) "
	    + " FROM tl_lavote11_usr user "
	    + " JOIN tl_lavote11_usr_attempt attempt ON user.uid = attempt.que_usr_id AND attempt.vote_nomination_content_id = 1 ";

    @Override
    @SuppressWarnings("rawtypes")
    public int getCountUsersForOpenTextEntries(Long sessionUid, Long toolContentId, String searchStringVote,
	    String searchStringUsername) {

	NativeQuery query;
	StringBuilder queryText = new StringBuilder(COUNT_USERS_OPEN_TEXT_BY_SESSION_UID);

	if (sessionUid != null) {
	    // get all the users who did an open text reply, restricting by session
	    queryText.append(FIND_USER_OPEN_TEXT_SESSION_UID_ADD);
	    buildCombinedSearch(searchStringVote, searchStringUsername, queryText);
	    query = getSession().createSQLQuery(queryText.toString());
	    query.setParameter("sessionUid", sessionUid);

	} else {

	    // get all the users for this content (more than one session potentially)
	    queryText.append(FIND_USER_OPEN_TEXT_CONTENT_UID_ADD);
	    buildCombinedSearch(searchStringVote, searchStringUsername, queryText);
	    query = getSession().createSQLQuery(queryText.toString());
	    query.setParameter("toolContentId", toolContentId);

	}

	List list = query.list();
	if (list == null || list.size() == 0) {
	    return 0;
	}
	return ((Number) list.get(0)).intValue();
    }

    private static final String GET_STATISTICS = "SELECT session.session_name sessionName, session.uid sessionUid, SUM(user.responseFinalised) countUsersComplete "
	    + " FROM tl_lavote11_usr user " + " JOIN tl_lavote11_session session ON user.vote_session_id = session.uid "
	    + " JOIN tl_lavote11_content content ON session.vote_content_id = content.uid and content.content_id = :contentId "
	    + " GROUP BY sessionUid " + " ORDER BY sessionUid";

    @Override
    @SuppressWarnings("unchecked")
    public List<VoteStatsDTO> getStatisticsBySession(Long toolContentId) {

	NativeQuery<VoteStatsDTO> query = getSession().createSQLQuery(GET_STATISTICS);
	query.addScalar("sessionUid", LongType.INSTANCE).addScalar("sessionName", StringType.INSTANCE)
		.addScalar("countUsersComplete", IntegerType.INSTANCE).setParameter("contentId", toolContentId)
		.setResultTransformer(Transformers.aliasToBean(VoteStatsDTO.class));

	return query.list();
    }

}