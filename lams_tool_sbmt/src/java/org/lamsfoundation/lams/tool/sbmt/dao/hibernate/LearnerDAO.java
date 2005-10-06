/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */
package org.lamsfoundation.lams.tool.sbmt.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.FlushMode;
import org.hibernate.Hibernate;
import org.hibernate.type.Type;

import org.lamsfoundation.lams.learningdesign.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.tool.sbmt.Learner;
import org.lamsfoundation.lams.tool.sbmt.dao.ILearnerDAO;

public class LearnerDAO extends BaseDAO implements ILearnerDAO {
	private static final String TABLENAME = "tl_lasbmt11_session_learners";
	private static final String FIND_BY_USER_ID_SESSION_ID = "from " + TABLENAME +
	 " in class " + Learner.class.getName() +
	 " where user_id=? and session_id=?";
	private static final String FIND_FOR_USER_BY_SESSION = "from " + TABLENAME +
								" in class " + Learner.class.getName() +
								" where user_id=? AND session_id=?";

	public Learner getLearner(Long sessionID, Long userID) {
		if ( sessionID != null && userID != null ) {
			return (Learner) this.getSession().createQuery(FIND_BY_USER_ID_SESSION_ID)
				.setLong(0, userID.longValue())
				.setLong(1, sessionID.longValue())
				.uniqueResult();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.sbmt.dao.ILearnerDAO#saveLearner(org.lamsfoundation.lams.tool.sbmt.Learner)
	 */
	public void saveLearner(Learner learner) {
		this.getSession().setFlushMode(FlushMode.AUTO);
		this.insert(learner);
		this.getHibernateTemplate().flush();
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.sbmt.dao.ILearnerDAO#updateLearer(org.lamsfoundation.lams.tool.sbmt.Learner)
	 */
	public void updateLearer(Learner learner) {
		this.getSession().setFlushMode(FlushMode.AUTO);
		this.update(learner);
		this.getHibernateTemplate().flush();
	}

	public List getSubmissionDetailsForUserBySession(Long userID, Long sessionID) {
		if ( userID != null && sessionID != null ) {
			Learner learner = (Learner) this.getSession().createQuery(FIND_FOR_USER_BY_SESSION)
										.setLong(0, userID.longValue())
										.setLong(1, sessionID.longValue())
										.uniqueResult();
			if(learner != null && learner.getSubmissionDetails() != null) {
				return new ArrayList(learner.getSubmissionDetails());
			}
		}
		return null;
	}

}
