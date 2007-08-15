/***************************************************************************
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
 * ***********************************************************************/
/* $$Id$$ */
package org.lamsfoundation.lams.tool.mc.dao;

import java.util.List;

import org.lamsfoundation.lams.tool.mc.pojos.McContent;
import org.lamsfoundation.lams.tool.mc.pojos.McSession;
import org.lamsfoundation.lams.tool.mc.pojos.McUsrAttempt;


/**
 * 
 * @author Ozgur Demirtas
 * * <p>Interface for the McUsrAttempt DAO, defines methods needed to access/modify user attempt data</p>
 *
 */
public interface IMcUsrAttemptDAO
{
    
    /**
	 *  * <p>Return the persistent instance of a McUsrAttempt  
	 * with the given identifier <code>uid</code>, returns null if not found. </p>
	 * 
	 * @param uid
	 * @return McQueContent
	 */
	public McUsrAttempt getMcUserAttemptByUID(Long uid);

	/**
	 *  * <p>saves McUsrAttempt  
	 * with the given identifier <code>mcUsrAttempt</code> </p>
	 * 
	 * @param uid
	 * @return 
	 */
	public void saveMcUsrAttempt(McUsrAttempt mcUsrAttempt);
    
	/**
	 *  * <p>updates McUsrAttempt  
	 * with the given identifier <code>mcUsrAttempt</code> </p>
	 * 
	 * @param mcUsrAttempt
	 * @return 
	 */	
	public void updateMcUsrAttempt(McUsrAttempt mcUsrAttempt);
	
	/**
	 *  * <p>removes McUsrAttempt  
	 * with the given identifier <code>uid</code> </p>
	 * 
	 * @param uid
	 * @return 
	 */	
	public void removeMcUsrAttemptByUID(Long uid);

	/**
	 *  * <p>removes McUsrAttempt  
	 * with the given identifier <code>mcUsrAttempt</code> </p>
	 * 
	 * @param mcUsrAttempt
	 * @return 
	 */
	public void removeMcUsrAttempt(McUsrAttempt mcUsrAttempt);
	
	/**
	 * <p>gets all the attempts (for all questions) for one user in one tool session </p>
	 * 
	 * @param queUsrId
	 * @return 
	 */
	public List getUserAttemptsForSession(Long queUsrId);

	/**
	 * Get the most recent attempts (for all questions) for one user in one tool session
	 * @param queUserUid
	 * @return
	 */
	public List getLatestAttemptsForAUser(Long queUserUid);
	
	/**
	 * <p>gets all the attempts for one questions for one user in one tool session <code>queUsrId</code>,
	 * ordered by the attempt id. If there is more than one option selected for a question, the attempts 
	 * are "batched". </p>
	 * 
	 * @param queUsrId
	 * @return 
	 */
	public List<McUsrAttempt> getAllAttemptsForAUserForOneQuestionContentOrderByAttempt(Long queUsrUid,  Long mcQueContentId);
	
	/** 
	 * Get the highest attempt order for a user for a particular question 
	 */
	public List<McUsrAttempt> getLatestAttemptsForAUserForOneQuestionContent(Long queUsrUid, Long mcQueContentId);

	/**
	 *  * <p>returns a list of attempts  
	 * with the given identifiers <code>queUsrId</code> and <code>mcQueContentId</code> and <code>attemptOrder</code> </p>
	 * 
	 * @param queUsrId
	 * @param mcQueContentId
	 * @param attemptOrder
	 * @return 
	 */
	public List getAttemptByAttemptOrder(final Long queUsrUid, final Long mcQueContentId, final Integer attemptOrder);
		
}


