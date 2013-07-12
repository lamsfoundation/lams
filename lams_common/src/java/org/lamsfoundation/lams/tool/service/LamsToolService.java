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
package org.lamsfoundation.lams.tool.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.tool.IToolVO;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.dao.IToolContentDAO;
import org.lamsfoundation.lams.tool.dao.IToolDAO;
import org.lamsfoundation.lams.tool.dao.IToolSessionDAO;
import org.lamsfoundation.lams.tool.exception.LamsToolServiceException;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.FileUtilException;

/**
 * 
 * @author Jacky Fang
 * @since 2005-3-17
 * 
 * @author Ozgur Demirtas 24/06/2005
 * 
 */
public class LamsToolService implements ILamsToolService {
    private static Logger log = Logger.getLogger(LamsToolService.class);

    public IToolDAO toolDAO;
    public IToolSessionDAO toolSessionDAO;
    public IToolContentDAO toolContentDAO;

    @Override
    public Set<User> getAllPotentialLearners(long toolSessionId) throws LamsToolServiceException {

	ToolSession session = toolSessionDAO.getToolSession(toolSessionId);
	if (session != null) {
	    return session.getLearners();
	} else {
	    log.error("No tool session found for " + toolSessionId + ". No potential learners being returned.");
	    return new HashSet<User>();
	}
    }

    @Override
    public IToolVO getToolBySignature(final String toolSignature) {
	Tool tool = toolDAO.getToolBySignature(toolSignature);
	return tool.createBasicToolVO();
    }

    @Override
    public Tool getPersistToolBySignature(final String toolSignature) {
	return toolDAO.getToolBySignature(toolSignature);
    }

    @Override
    public long getToolDefaultContentIdBySignature(final String toolSignature) {
	return toolDAO.getToolDefaultContentIdBySignature(toolSignature);
    }

    @Override
    public String generateUniqueContentFolder() throws FileUtilException, IOException {

	return FileUtil.generateUniqueContentFolderID();
    }

    @Override
    public String getLearnerContentFolder(Long toolSessionId, Long userId) {

	ToolSession toolSession = this.getToolSession(toolSessionId);
	Long lessonId = toolSession.getLesson().getLessonId();
	String learnerContentFolder = FileUtil.getLearnerContentFolder(lessonId, userId);

	return learnerContentFolder;
    }

    @Override
    public void saveOrUpdateTool(Tool tool) {
	toolDAO.saveOrUpdateTool(tool);
    }

    /**
     * Get the tool session object using the toolSessionId
     * 
     * @param toolSessionId
     * @return
     */
    @Override
    public ToolSession getToolSession(Long toolSessionId) {
	return toolSessionDAO.getToolSession(toolSessionId);
    }

    @Override
    public Boolean isGroupedActivity(long toolContentID) {
	List<Activity> activities = toolContentDAO.findByProperty(Activity.class, "toolContentId", toolContentID);
	if (activities.size() == 1) {
	    Activity activity = activities.get(0);
	    return activity.getApplyGrouping();
	} else {
	    log.debug("ToolContent contains multiple activities, can't test whether grouping applies.");
	    return null;
	}
    }

    /**
     * @return Returns the toolDAO.
     */
    public IToolDAO getToolDAO() {
	return toolDAO;
    }

    /**
     * @param toolDAO
     *            The toolDAO to set.
     */
    public void setToolDAO(IToolDAO toolDAO) {
	this.toolDAO = toolDAO;
    }

    public IToolSessionDAO getToolSessionDAO() {
	return toolSessionDAO;
    }

    public void setToolSessionDAO(IToolSessionDAO toolSessionDAO) {
	this.toolSessionDAO = toolSessionDAO;
    }

    /**
     * 
     * @return
     */
    public IToolContentDAO getToolContentDAO() {
	return toolContentDAO;
    }

    /**
     * 
     * @param toolContentDAO
     */
    public void setToolContentDAO(IToolContentDAO toolContentDAO) {
	this.toolContentDAO = toolContentDAO;
    }
}
