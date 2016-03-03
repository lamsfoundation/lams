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
/* $$Id$$ */
package org.lamsfoundation.lams.tool.service;

import java.io.IOException;

import org.lamsfoundation.lams.tool.IToolVO;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.util.FileUtilException;

/**
 * This interface defines all the service available for self contained tool module from lams. Any service that would be
 * used by other lams module such as, lams_learning etc, should not appear in this interface.
 * 
 * @author chris
 * @author Jacky Fang
 * @author Ozgur Demirtas 24/06/2005
 */
public interface ILamsToolService {
    IToolVO getToolByID(Long toolId);

    IToolVO getToolBySignature(final String toolSignature);

    long getToolDefaultContentIdBySignature(final String toolSignature);

    String generateUniqueContentFolder() throws FileUtilException, IOException;

    /**
     * Return content folder (unique to each learner and lesson) which is used for storing user generated content. It's
     * been used by CKEditor.
     * 
     * @param toolSessionId
     * @param userId
     * @return
     */
    String getLearnerContentFolder(Long toolSessionId, Long userId);

    void saveOrUpdateTool(Tool tool);

    Tool getPersistToolBySignature(final String toolSignature);

    ToolSession getToolSession(Long toolSessionId);

    /**
     * Allows the tool to ask whether or not the activity is grouped and therefore it should expect more than one tool
     * session.
     * 
     * @param toolContentID
     * @return
     */
    Boolean isGroupedActivity(long toolContentID);

    /**
     * Returns leader's UserId from the nearest Leader Selection Tool (the nearest to the specified activity) , and null
     * if no Leader Selection Tools available.
     * 
     * @param toolSessionId
     *            sessionId of the specified activity
     * @param learnerId
     *            userId (used to get appropriate Leader Selection Tool's session)
     * @return
     */
    Long getLeaderUserId(Long toolSessionId, Integer learnerId);

    /**
     * Get a count of all the users that would be returned by getUsersForActivity(Long toolSessionId);
     */
    Integer getCountUsersForActivity(Long toolSessionId);
}