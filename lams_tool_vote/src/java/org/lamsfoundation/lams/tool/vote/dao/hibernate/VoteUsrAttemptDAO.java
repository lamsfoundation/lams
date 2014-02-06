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

import org.hibernate.FlushMode;
import org.lamsfoundation.lams.tool.vote.dao.IVoteUsrAttemptDAO;
import org.lamsfoundation.lams.tool.vote.pojos.VoteUsrAttempt;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Hibernate implementation for database access to VoteUsrAttemptDAO for the voting tool.
 * 
 * @author Ozgur Demirtas repaired by lfoxton
 */
public class VoteUsrAttemptDAO extends HibernateDaoSupport implements IVoteUsrAttemptDAO {

    private static final String LOAD_ATTEMPT_FOR_USER = "from voteUsrAttempt in class VoteUsrAttempt where voteUsrAttempt.queUsrId=:queUsrId";

    private static final String LOAD_ATTEMPT_FOR_QUESTION_CONTENT = "from voteUsrAttempt in class VoteUsrAttempt where voteUsrAttempt.voteQueContentId=:voteQueContentId";

    private static final String LOAD_ATTEMPT_FOR_QUESTION_CONTENT_AND_SESSION = "from voteUsrAttempt in class VoteUsrAttempt where voteUsrAttempt.voteQueContentId=:voteQueContentId and voteUsrAttempt.voteQueUsr.voteSession.uid=:sessionUid";

    private static final String LOAD_ATTEMPT_FOR_USER_AND_QUESTION_CONTENT = "from voteUsrAttempt in class VoteUsrAttempt where voteUsrAttempt.queUsrId=:queUsrId and voteUsrAttempt.voteQueContentId=:voteQueContentId";

    private static final String LOAD_ATTEMPT_FOR_USER_AND_QUESTION_CONTENT_AND_SESSION = "from voteUsrAttempt in class VoteUsrAttempt where voteUsrAttempt.queUsrId=:queUsrId and voteUsrAttempt.voteQueContentId=:voteQueContentId and voteUsrAttempt.voteQueUsr.voteSession.uid=:sessionUid";

    private static final String LOAD_ATTEMPT_FOR_USER_AND_SESSION = "from voteUsrAttempt in class VoteUsrAttempt where voteUsrAttempt.queUsrId=:queUsrId and voteUsrAttempt.voteQueUsr.voteSession.uid=:sessionUid";

    private static final String LOAD_USER_ENTRIES = "select distinct voteUsrAttempt.userEntry from VoteUsrAttempt voteUsrAttempt where voteUsrAttempt.voteQueUsr.voteSession.voteContent.uid=:voteContentUid";

    private static final String LOAD_USER_ENTRY_RECORDS = "from voteUsrAttempt in class VoteUsrAttempt where voteUsrAttempt.userEntry=:userEntry and voteUsrAttempt.voteQueContentId=1 and voteUsrAttempt.voteQueUsr.voteSession.voteContent.uid=:voteContentUid";

    private static final String COUNT_ATTEMPTS_BY_CONTENT_ID = "select count(*) from VoteUsrAttempt att, VoteQueUsr user, VoteSession ses where "
	    + "att.voteQueUsr=user and user.voteSession=ses and " + "ses.voteContentId=:voteContentId";

    private static final String LOAD_ENTRIES_BY_SESSION_UID = "select att from VoteUsrAttempt att, VoteQueUsr user, VoteSession ses where "
	    + "att.voteQueUsr=user and user.voteSession=ses and ses.uid=:voteSessionUid";

    private static final String COUNT_ENTRIES_BY_SESSION_ID = "select count(*) from VoteUsrAttempt att, VoteQueUsr user, VoteSession ses where "
	    + "att.voteQueUsr=user and user.voteSession=ses and ses.uid=:voteSessionUid";

    @Override
    public VoteUsrAttempt getAttemptByUID(Long uid) {
	String query = "from VoteUsrAttempt attempt where attempt.uid=?";

	List<VoteUsrAttempt> list = getSession().createQuery(query).setLong(0, uid.longValue()).list();

	if ((list != null) && (list.size() > 0)) {
	    VoteUsrAttempt attempt = list.get(0);
	    return attempt;
	}
	return null;
    }

    @Override
    public void saveVoteUsrAttempt(VoteUsrAttempt voteUsrAttempt) {
	this.getHibernateTemplate().save(voteUsrAttempt);
    }

    @Override
    public List<VoteUsrAttempt> getAttemptsForUser(final Long queUsrId) {
	List list = getSession().createQuery(VoteUsrAttemptDAO.LOAD_ATTEMPT_FOR_USER)
		.setLong("queUsrId", queUsrId.longValue()).list();
	return list;
    }

    @Override
    public int getUserEnteredVotesCountForContent(final Long voteContentUid) {
	List result = getSession().createQuery(VoteUsrAttemptDAO.COUNT_ATTEMPTS_BY_CONTENT_ID)
		.setLong("voteContentId", voteContentUid).list();
	Long resultLong = result.get(0) != null ? (Long) result.get(0) : new Long(0);
	return resultLong.intValue();
    }

    @Override
    public Set<String> getUserEntries(final Long voteContentUid) {
	List<String> list = getSession().createQuery(VoteUsrAttemptDAO.LOAD_USER_ENTRIES)
		.setLong("voteContentUid", voteContentUid).list();

	Set<String> userEntries = new HashSet<String>();
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

    @Override
    public List<VoteUsrAttempt> getUserRecords(final Long voteContentUid, final String userEntry) {
	List<VoteUsrAttempt> list = getSession().createQuery(VoteUsrAttemptDAO.LOAD_USER_ENTRY_RECORDS)
		.setLong("voteContentUid", voteContentUid).setString("userEntry", userEntry).list();
	return list;
    }

    @Override
    public Set<VoteUsrAttempt> getSessionUserEntriesSet(final Long voteSessionUid) {
	List<VoteUsrAttempt> list = getSession().createQuery(VoteUsrAttemptDAO.LOAD_ENTRIES_BY_SESSION_UID)
		.setLong("voteSessionUid", voteSessionUid).list();

	Set<VoteUsrAttempt> sessionUserEntries = new HashSet();
	sessionUserEntries.addAll(list);
	return sessionUserEntries;
    }

    @Override
    public void removeAttemptsForUserandSession(final Long queUsrId, final Long sessionUid) {
	String strGetUser = "from voteUsrAttempt in class VoteUsrAttempt where voteUsrAttempt.queUsrId=:queUsrId and voteUsrAttempt.voteQueUsr.voteSession.uid=:sessionUid";
	HibernateTemplate templ = this.getHibernateTemplate();
	List<VoteUsrAttempt> list = getSession().createQuery(strGetUser).setLong("queUsrId", queUsrId.longValue())
		.setLong("sessionUid", sessionUid).list();

	if ((list != null) && (list.size() > 0)) {
	    Iterator<VoteUsrAttempt> listIterator = list.iterator();
	    while (listIterator.hasNext()) {
		VoteUsrAttempt attempt = listIterator.next();
		this.getSession().setFlushMode(FlushMode.AUTO);
		templ.delete(attempt);
		templ.flush();
	    }
	}
    }

    @Override
    public int getAttemptsForQuestionContent(final Long voteQueContentId) {
	List list = getSession().createQuery(VoteUsrAttemptDAO.LOAD_ATTEMPT_FOR_QUESTION_CONTENT)
		.setLong("voteQueContentId", voteQueContentId.longValue()).list();

	if ((list != null) && (list.size() > 0)) {
	    return list.size();
	}

	return 0;
    }

    @Override
    public int getStandardAttemptsForQuestionContentAndSessionUid(final Long voteQueContentId, final Long sessionUid) {
	List list = getStandardAttemptUsersForQuestionContentAndSessionUid(voteQueContentId, sessionUid);
	return list.size();
    }

    @Override
    public List<VoteUsrAttempt> getStandardAttemptUsersForQuestionContentAndSessionUid(final Long voteQueContentId,
	    final Long sessionUid) {
	List<VoteUsrAttempt> list = getSession()
		.createQuery(VoteUsrAttemptDAO.LOAD_ATTEMPT_FOR_QUESTION_CONTENT_AND_SESSION)
		.setLong("voteQueContentId", voteQueContentId.longValue())
		.setLong("sessionUid", sessionUid.longValue()).list();

	List<VoteUsrAttempt> userEntries = new ArrayList();
	if ((list != null) && (list.size() > 0)) {
	    userEntries.addAll(list);
	}
	return userEntries;

    }

    @Override
    public List<VoteUsrAttempt> getStandardAttemptsForQuestionContentAndContentUid(final Long voteQueContentId) {
	List<VoteUsrAttempt> list = getSession().createQuery(VoteUsrAttemptDAO.LOAD_ATTEMPT_FOR_QUESTION_CONTENT)
		.setLong("voteQueContentId", voteQueContentId.longValue()).list();
	return list;

    }

    @Override
    public List getAttemptsForUserAndQuestionContent(final Long queUsrId, final Long voteQueContentId) {
	List list = getSession().createQuery(VoteUsrAttemptDAO.LOAD_ATTEMPT_FOR_USER_AND_QUESTION_CONTENT)
		.setLong("queUsrId", queUsrId.longValue()).setLong("voteQueContentId", voteQueContentId.longValue())
		.list();

	return list;
    }

    @Override
    public VoteUsrAttempt getAttemptForUserAndQuestionContentAndSession(final Long queUsrId,
	    final Long voteQueContentId, final Long sessionUid) {
	List<VoteUsrAttempt> list = getSession()
		.createQuery(VoteUsrAttemptDAO.LOAD_ATTEMPT_FOR_USER_AND_QUESTION_CONTENT_AND_SESSION)
		.setLong("queUsrId", queUsrId.longValue()).setLong("voteQueContentId", voteQueContentId.longValue())
		.setLong("sessionUid", sessionUid.longValue()).list();

	if ((list == null) || (list.size() == 0)) {
	    return null;
	}
	return list.get(0);
    }

    @Override
    public Set<String> getAttemptsForUserAndSession(final Long queUsrId, final Long sessionUid) {

	List<VoteUsrAttempt> list = getSession().createQuery(VoteUsrAttemptDAO.LOAD_ATTEMPT_FOR_USER_AND_SESSION)
		.setLong("queUsrId", queUsrId.longValue()).setLong("sessionUid", sessionUid.longValue()).list();

	Set<String> userEntries = new HashSet<String>();
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

    @Override
    public Set<String> getAttemptsForUserAndSessionUseOpenAnswer(final Long queUsrId, final Long sessionUid) {

	List<VoteUsrAttempt> list = getSession().createQuery(VoteUsrAttemptDAO.LOAD_ATTEMPT_FOR_USER_AND_SESSION)
		.setLong("queUsrId", queUsrId.longValue()).setLong("sessionUid", sessionUid.longValue()).list();

	String openAnswer = "";
	Set<String> userEntries = new HashSet<String>();
	if ((list != null) && (list.size() > 0)) {
	    Iterator<VoteUsrAttempt> listIterator = list.iterator();
	    while (listIterator.hasNext()) {
		VoteUsrAttempt attempt = listIterator.next();

		Long questionUid = attempt.getVoteQueContent().getUid();
		if (!questionUid.toString().equals("1")) {
		    userEntries.add(attempt.getVoteQueContent().getQuestion());
		} else {
		    // this is a user entered vote
		    if (attempt.getUserEntry().length() > 0) {
			openAnswer = attempt.getUserEntry();
			// adding openAnswer to userEntries
			userEntries.add(openAnswer);
		    }

		}
	    }
	}
	return userEntries;
    }

    @Override
    public int getSessionEntriesCount(final Long voteSessionUid) {
	List result = getSession().createQuery(VoteUsrAttemptDAO.COUNT_ENTRIES_BY_SESSION_ID)
		.setLong("voteSessionUid", voteSessionUid).list();
	Long resultLong = result.get(0) != null ? (Long) result.get(0) : new Long(0);
	return resultLong.intValue();
    }

    @Override
    public void updateVoteUsrAttempt(VoteUsrAttempt voteUsrAttempt) {
	this.getSession().setFlushMode(FlushMode.AUTO);
	this.getHibernateTemplate().update(voteUsrAttempt);
    }

    @Override
    public void removeVoteUsrAttempt(VoteUsrAttempt voteUsrAttempt) {
	this.getSession().setFlushMode(FlushMode.AUTO);
	this.getHibernateTemplate().delete(voteUsrAttempt);
    }
}