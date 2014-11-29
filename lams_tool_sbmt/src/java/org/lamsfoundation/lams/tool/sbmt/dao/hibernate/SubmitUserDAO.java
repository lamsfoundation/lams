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

package org.lamsfoundation.lams.tool.sbmt.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.sbmt.SubmitUser;
import org.lamsfoundation.lams.tool.sbmt.dao.ISubmitUserDAO;
import org.springframework.stereotype.Repository;

@Repository
public class SubmitUserDAO extends LAMSBaseDAO implements ISubmitUserDAO {
	private static final String FIND_BY_USER_ID_SESSION_ID = "from " + SubmitUser.class.getName() +
	 		" where user_id=? and session_id=?";
	private static final String FIND_BY_USER_ID_CONTENT_ID = "from " + SubmitUser.class.getName() +
	" where user_id=? and content_id=?";

	private static final String FIND_BY_SESSION_ID = "from " + SubmitUser.class.getName() + " where session_id=?";
	
	
	public SubmitUser getLearner(Long sessionID, Integer userID) {
		List list = doFind(FIND_BY_USER_ID_SESSION_ID,new Object[]{userID,sessionID});
		if(list.size() > 0)
			return (SubmitUser) list.get(0);
		else
			return null;
	}

	public SubmitUser getContentUser(Long contentId, Integer userID) {
		List list = doFind(FIND_BY_USER_ID_CONTENT_ID,new Object[]{userID,contentId});
		if(list.size() > 0)
			return (SubmitUser) list.get(0);
		else
			return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<SubmitUser> getUsersBySession(Long sessionID){
		return (List<SubmitUser>) doFind(FIND_BY_SESSION_ID,sessionID);
	}
	
	
	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.sbmt.dao.ILearnerDAO#updateLearer(org.lamsfoundation.lams.tool.sbmt.Learner)
	 */
	public void saveOrUpdateUser(SubmitUser learner) {
		this.insertOrUpdate(learner);
	}


}
