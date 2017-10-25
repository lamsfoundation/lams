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



package org.lamsfoundation.lams.confidencelevel.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.confidencelevel.ConfidenceLevel;
import org.lamsfoundation.lams.confidencelevel.dao.IConfidenceLevelDAO;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;

public class ConfidenceLevelService implements IConfidenceLevelService {

    private static Logger log = Logger.getLogger(ConfidenceLevelService.class);

    private IConfidenceLevelDAO confidenceLevelDAO;

    protected IUserManagementService userManagementService;

    protected MessageService messageService;
    
    @Override
    public void removeUserCommitsByContent(Integer userId, Long toolSessionId) {
	List<ConfidenceLevel> confidenceLevels = confidenceLevelDAO.getConfidenceLevelsByUser(userId, toolSessionId);
	for (ConfidenceLevel confidenceLevel : confidenceLevels) {
	    confidenceLevelDAO.delete(confidenceLevel);
	}
    }

    @Override
    public void saveOrUpdateConfidenceLevel(ConfidenceLevel confidenceLevel) {
	confidenceLevelDAO.saveOrUpdate(confidenceLevel);
    }

    @Override
    public void saveConfidenceLevel(Long toolSessionId, Integer userId, Long questionUid, int confidenceLevelInt) {

	ConfidenceLevel confidenceLevel = confidenceLevelDAO.getConfidenceLevel(userId, questionUid);

	// persist MessageConfidenceLevel changes in DB
	if (confidenceLevel == null) { // add
	    confidenceLevel = new ConfidenceLevel();
	    confidenceLevel.setQuestionUid(questionUid);

	    User learner = (User) userManagementService.findById(User.class, userId);
	    confidenceLevel.setLearner(learner);

	    confidenceLevel.setToolSessionId(toolSessionId);
	}

	confidenceLevel.setConfidenceLevel(confidenceLevelInt);
	confidenceLevelDAO.saveOrUpdate(confidenceLevel);
    }

    @Override
    public int saveConfidenceLevels(Long toolSessionId, Integer userId, Map<Long, Integer> questionUidToConfidenceLevelMap) {

	User learner = (User) userManagementService.findById(User.class, userId);
	int numConfidenceLevels = 0;
	
	List<ConfidenceLevel> dbConfidenceLevels = confidenceLevelDAO.getConfidenceLevelsByUser(userId, toolSessionId);
	for ( ConfidenceLevel dbConfidenceLevel: dbConfidenceLevels ) {
	    Long questionUid = dbConfidenceLevel.getQuestionUid();
	    Integer newConfidenceLevel = questionUidToConfidenceLevelMap.get(questionUid);
	    if ( newConfidenceLevel != null ) {
		dbConfidenceLevel.setConfidenceLevel(newConfidenceLevel);
		questionUidToConfidenceLevelMap.remove(questionUid);
		numConfidenceLevels++;
		confidenceLevelDAO.saveOrUpdate(dbConfidenceLevel);
	    } else {
		dbConfidenceLevel.setConfidenceLevel(0);
	    }
	}
	for ( Map.Entry<Long, Integer> entry : questionUidToConfidenceLevelMap.entrySet() ) {
	    ConfidenceLevel confidenceLevel = new ConfidenceLevel();
	    confidenceLevel.setQuestionUid(entry.getKey());
	    confidenceLevel.setLearner(learner);
	    confidenceLevel.setConfidenceLevel(entry.getValue());
	    confidenceLevel.setToolSessionId(toolSessionId);
	    confidenceLevelDAO.saveOrUpdate(confidenceLevel);
	    numConfidenceLevels++;
	}
	    
	return numConfidenceLevels;
    }

    @Override
    public List<ConfidenceLevel> getConfidenceLevelsByUser(Integer userId, Long toolSessionId) {
	return confidenceLevelDAO.getConfidenceLevelsByUser(userId.intValue(), toolSessionId);
    }
    
    @Override
    public List<ConfidenceLevel> getConfidenceLevelsByQuestionAndSession(Long questionUid, Long toolSessionId) {
	return confidenceLevelDAO.getConfidenceLevelsByQuestionAndSession(questionUid, toolSessionId);
    }
    
    /* ********** Used by Spring to "inject" the linked objects ************* */

    public void setConfidenceLevelDAO(IConfidenceLevelDAO confidenceLevelDAO) {
	this.confidenceLevelDAO = confidenceLevelDAO;
    }

    /**
     *
     * @param IUserManagementService
     *            The userManagementService to set.
     */
    public void setUserManagementService(IUserManagementService userManagementService) {
	this.userManagementService = userManagementService;
    }

    /**
     * Set i18n MessageService
     */
    public void setMessageService(MessageService messageService) {
	this.messageService = messageService;
    }
}
