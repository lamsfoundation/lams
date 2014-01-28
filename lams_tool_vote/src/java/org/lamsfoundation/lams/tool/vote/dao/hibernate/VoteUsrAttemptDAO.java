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
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueUsr;
import org.lamsfoundation.lams.tool.vote.pojos.VoteUsrAttempt;
import org.lamsfoundation.lams.tool.vote.service.IVoteService;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author Ozgur Demirtas repaired by lfoxton
 *         <p>
 *         Hibernate implementation for database access to VoteUsrAttemptDAO for the voting tool.
 *         </p>
 */
public class VoteUsrAttemptDAO extends HibernateDaoSupport implements IVoteUsrAttemptDAO {

    private static final String LOAD_ATTEMPT_FOR_USER = "from voteUsrAttempt in class VoteUsrAttempt where voteUsrAttempt.queUsrId=:queUsrId";

    private static final String LOAD_ATTEMPT_FOR_QUESTION_CONTENT = "from voteUsrAttempt in class VoteUsrAttempt where voteUsrAttempt.voteQueContentId=:voteQueContentId";

    private static final String LOAD_ATTEMPT_FOR_USER_AND_QUESTION_CONTENT = "from voteUsrAttempt in class VoteUsrAttempt where voteUsrAttempt.queUsrId=:queUsrId and voteUsrAttempt.voteQueContentId=:voteQueContentId";

    private static final String LOAD_ATTEMPT_FOR_USER_AND_QUESTION_CONTENT_AND_SESSION = "from voteUsrAttempt in class VoteUsrAttempt where voteUsrAttempt.queUsrId=:queUsrId and voteUsrAttempt.voteQueContentId=:voteQueContentId";

    private static final String LOAD_ATTEMPT_FOR_USER_AND_SESSION = "from voteUsrAttempt in class VoteUsrAttempt where voteUsrAttempt.queUsrId=:queUsrId";

    private static final String LOAD_USER_ENTRIES = "select distinct voteUsrAttempt.userEntry from VoteUsrAttempt voteUsrAttempt";

    private static final String LOAD_USER_ENTRY_RECORDS = "from voteUsrAttempt in class VoteUsrAttempt where voteUsrAttempt.userEntry=:userEntry and voteUsrAttempt.voteQueContentId=1 ";

    private static final String COUNT_ATTEMPTS_BY_CONTENT_ID = "select count(*) from VoteUsrAttempt att, VoteQueUsr user, VoteSession ses where "
	    + "att.voteQueUsr=user and user.voteSession=ses and " + "ses.voteContentId=:voteContentId";

    private static final String LOAD_ENTRIES_BY_SESSION_ID = "select att from VoteUsrAttempt att, VoteQueUsr user, VoteSession ses where "
	    + "att.voteQueUsr=user and user.voteSession=ses and ses.uid=:voteSessionUid";

    private static final String COUNT_ENTRIES_BY_SESSION_ID = "select count(*) from VoteUsrAttempt att, VoteQueUsr user, VoteSession ses where "
	    + "att.voteQueUsr=user and user.voteSession=ses and ses.uid=:voteSessionUid";

    public VoteUsrAttempt getAttemptByUID(Long uid) {
	String query = "from VoteUsrAttempt attempt where attempt.uid=?";

	List list = getSession().createQuery(query).setLong(0, uid.longValue()).list();

	if (list != null && list.size() > 0) {
	    VoteUsrAttempt attempt = (VoteUsrAttempt) list.get(0);
	    return attempt;
	}
	return null;
    }

    public void saveVoteUsrAttempt(VoteUsrAttempt voteUsrAttempt) {
	this.getHibernateTemplate().save(voteUsrAttempt);
    }

    public List<VoteUsrAttempt> getAttemptsForUser(final Long queUsrId) {
	List list = getSession().createQuery(VoteUsrAttemptDAO.LOAD_ATTEMPT_FOR_USER).setLong("queUsrId",
		queUsrId.longValue()).list();
	return list;
    }

    public int getUserEnteredVotesCountForContent(final Long voteContentUid) {
	List result = getSession().createQuery(VoteUsrAttemptDAO.COUNT_ATTEMPTS_BY_CONTENT_ID).setLong("voteContentId",
		voteContentUid).list();
	Long resultLong = result.get(0) != null ? (Long) result.get(0) : new Long(0);
	return resultLong.intValue();
    }

    public Set getUserEntries() {
	List list = getSession().createQuery(VoteUsrAttemptDAO.LOAD_USER_ENTRIES).list();

	Set set = new HashSet();

	Set userEntries = new HashSet();
	if (list != null && list.size() > 0) {
	    Iterator listIterator = list.iterator();
	    while (listIterator.hasNext()) {
		String entry = (String) listIterator.next();
		if (entry != null && entry.length() > 0) {
		    userEntries.add(entry);
		}
	    }
	}
	return userEntries;
    }

    public List getSessionUserEntries(final Long voteSessionUid) {

	return getSession().createQuery(VoteUsrAttemptDAO.LOAD_ENTRIES_BY_SESSION_ID).setLong("voteSessionUid",
		voteSessionUid).list();
    }

    public Set getSessionUserEntriesSet(final Long voteSessionUid) {
	List<VoteUsrAttempt> list = getSessionUserEntries(voteSessionUid);
	Set<VoteUsrAttempt> sessionUserEntries = new HashSet();
	for (VoteUsrAttempt att : list) {
	    sessionUserEntries.add(att);
	}
	return sessionUserEntries;
    }

    public void removeAttemptsForUserandSession(final Long queUsrId, final Long voteSessionId) {
	String strGetUser = "from voteUsrAttempt in class VoteUsrAttempt where voteUsrAttempt.queUsrId=:queUsrId";
	HibernateTemplate templ = this.getHibernateTemplate();
	List list = getSession().createQuery(strGetUser).setLong("queUsrId", queUsrId.longValue()).list();

	if (list != null && list.size() > 0) {
	    Iterator listIterator = list.iterator();
	    while (listIterator.hasNext()) {
		VoteUsrAttempt attempt = (VoteUsrAttempt) listIterator.next();
		if (attempt.getVoteQueUsr().getVoteSession().getUid().toString().equals(voteSessionId.toString())) {
		    this.getSession().setFlushMode(FlushMode.AUTO);
		    templ.delete(attempt);
		    templ.flush();

		}
	    }
	}
    }

    public int getAttemptsForQuestionContent(final Long voteQueContentId) {
	List list = getSession().createQuery(VoteUsrAttemptDAO.LOAD_ATTEMPT_FOR_QUESTION_CONTENT).setLong(
		"voteQueContentId", voteQueContentId.longValue()).list();

	if (list != null && list.size() > 0) {
	    return list.size();
	}

	return 0;
    }

    public int getStandardAttemptsForQuestionContentAndSessionUid(final Long voteQueContentId, final Long voteSessionUid) {
	List list = getSession().createQuery(VoteUsrAttemptDAO.LOAD_ATTEMPT_FOR_QUESTION_CONTENT).setLong(
		"voteQueContentId", voteQueContentId.longValue()).list();

	List userEntries = new ArrayList();
	if (list != null && list.size() > 0) {
	    Iterator listIterator = list.iterator();
	    while (listIterator.hasNext()) {
		VoteUsrAttempt attempt = (VoteUsrAttempt) listIterator.next();
		if (attempt.getVoteQueUsr().getVoteSession().getUid().toString().equals(voteSessionUid.toString())) {
		    userEntries.add(attempt);
		}
	    }
	}
	return userEntries.size();

    }

    public List getStandardAttemptUsersForQuestionContentAndSessionUid(final Long voteQueContentId,
	    final Long voteSessionUid) {
	List list = getSession().createQuery(VoteUsrAttemptDAO.LOAD_ATTEMPT_FOR_QUESTION_CONTENT).setLong(
		"voteQueContentId", voteQueContentId.longValue()).list();

	List userEntries = new ArrayList();
	if (list != null && list.size() > 0) {
	    Iterator listIterator = list.iterator();
	    while (listIterator.hasNext()) {
		VoteUsrAttempt attempt = (VoteUsrAttempt) listIterator.next();
		if (attempt.getVoteQueUsr().getVoteSession().getUid().toString().equals(voteSessionUid.toString())) {
		    userEntries.add(attempt);
		}
	    }
	}
	return userEntries;

    }

    public List<VoteUsrAttempt> getStandardAttemptsForQuestionContentAndContentUid(final Long voteQueContentId) {
	List list = getSession().createQuery(VoteUsrAttemptDAO.LOAD_ATTEMPT_FOR_QUESTION_CONTENT).setLong(
		"voteQueContentId", voteQueContentId.longValue()).list();
	return list;

    }

    public List getAttemptsForUserAndQuestionContent(final Long queUsrId, final Long voteQueContentId) {
	List list = getSession().createQuery(VoteUsrAttemptDAO.LOAD_ATTEMPT_FOR_USER_AND_QUESTION_CONTENT).setLong(
		"queUsrId", queUsrId.longValue()).setLong("voteQueContentId", voteQueContentId.longValue()).list();

	return list;
    }

    public VoteUsrAttempt getAttemptForUserAndQuestionContentAndSession(final Long queUsrId,
	    final Long voteQueContentId, final Long voteSessionUid) {
	List<VoteUsrAttempt> list = getSession().createQuery(VoteUsrAttemptDAO.LOAD_ATTEMPT_FOR_USER_AND_QUESTION_CONTENT_AND_SESSION)
		.setLong("queUsrId", queUsrId.longValue()).setLong("voteQueContentId", voteQueContentId.longValue())
		.list();

	if (list != null && list.size() > 0) {
	    Iterator<VoteUsrAttempt> listIterator = list.iterator();
	    while (listIterator.hasNext()) {
		VoteUsrAttempt attempt = listIterator.next();
		if (attempt.getVoteQueUsr().getVoteSession().getUid().toString().equals(voteSessionUid.toString())) {
		    return attempt;
		}
	    }
	}
	return null;
    }

    public Set getAttemptsForUserAndSession(final Long queUsrId, final Long voteSessionUid) {

	List list = getSession().createQuery(VoteUsrAttemptDAO.LOAD_ATTEMPT_FOR_USER_AND_SESSION).setLong("queUsrId",
		queUsrId.longValue()).list();

	Set userEntries = new HashSet();
	if (list != null && list.size() > 0) {
	    Iterator listIterator = list.iterator();
	    while (listIterator.hasNext()) {
		VoteUsrAttempt attempt = (VoteUsrAttempt) listIterator.next();

		if (attempt.getVoteQueUsr().getVoteSession().getUid().toString().equals(voteSessionUid.toString())) {
		    Long questionUid = attempt.getVoteQueContent().getUid();
		    if (!questionUid.toString().equals("1")) {
			userEntries.add(attempt.getVoteQueContent().getQuestion());
		    }
		}
	    }
	}
	return userEntries;
    }

    public Set getAttemptsForUserAndSessionUseOpenAnswer(final Long queUsrId, final Long voteSessionId) {

	List list = getSession().createQuery(VoteUsrAttemptDAO.LOAD_ATTEMPT_FOR_USER_AND_SESSION).setLong("queUsrId",
		queUsrId.longValue()).list();

	String openAnswer = "";
	Set userEntries = new HashSet();
	if (list != null && list.size() > 0) {
	    Iterator listIterator = list.iterator();
	    while (listIterator.hasNext()) {
		VoteUsrAttempt attempt = (VoteUsrAttempt) listIterator.next();

		if (attempt.getVoteQueUsr().getVoteSession().getUid().toString().equals(voteSessionId.toString())) {
		    Long questionUid = attempt.getVoteQueContent().getUid();
		    if (!questionUid.toString().equals("1")) {
			userEntries.add(attempt.getVoteQueContent().getQuestion());
		    } else {
			//this is a user entered vote
			if (attempt.getUserEntry().length() > 0) {
			    openAnswer = attempt.getUserEntry();
			    //adding openAnswer to userEntries
			    userEntries.add(openAnswer);
			}

		    }

		}
	    }
	}
	return userEntries;
    }

    public List getUserRecords(final String userEntry) {
	List list = getSession().createQuery(VoteUsrAttemptDAO.LOAD_USER_ENTRY_RECORDS).setString("userEntry",
		userEntry).list();
	return list;
    }

    public List getUserEnteredVotesForSession(final String userEntry, final Long voteSessionUid) {
	List list = getSession().createQuery(VoteUsrAttemptDAO.LOAD_USER_ENTRY_RECORDS).setString("userEntry",
		userEntry).list();

	List sessionUserEntries = new ArrayList();
	if (list != null && list.size() > 0) {
	    Iterator listIterator = list.iterator();
	    while (listIterator.hasNext()) {
		VoteUsrAttempt attempt = (VoteUsrAttempt) listIterator.next();
		if (attempt.getVoteQueUsr().getVoteSession().getUid().toString().equals(voteSessionUid.toString())) {
		    sessionUserEntries.add(attempt.getUserEntry());
		}
	    }
	}
	return sessionUserEntries;
    }

    public int getSessionEntriesCount(final Long voteSessionUid) {
	List result = getSession().createQuery(VoteUsrAttemptDAO.COUNT_ENTRIES_BY_SESSION_ID).setLong("voteSessionUid",
		voteSessionUid).list();
	Long resultLong = result.get(0) != null ? (Long) result.get(0) : new Long(0);
	return resultLong.intValue();
    }

    public void updateVoteUsrAttempt(VoteUsrAttempt voteUsrAttempt) {
	this.getSession().setFlushMode(FlushMode.AUTO);
	this.getHibernateTemplate().update(voteUsrAttempt);
    }

    public void removeVoteUsrAttempt(VoteUsrAttempt voteUsrAttempt) {
	this.getSession().setFlushMode(FlushMode.AUTO);
	this.getHibernateTemplate().delete(voteUsrAttempt);
    }
}