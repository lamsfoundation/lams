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
package org.lamsfoundation.lams.tool;

import java.util.List;
import java.util.SortedMap;

import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.usermanagement.User;



/**
 * The interface that defines the tool's contract regarding session. It must 
 * be implemented by the tool to establish the communication channel between
 * tool and lams core service.
 * 
 * @author Jacky Fang 
 * @since 2004-12-6
 * @version 1.1
 */
public interface ToolSessionManager {
    
    /**
     * Create a tool session for a piece of tool content using the tool 
     * session id generated by LAMS. If no content exists with the given 
     * tool content id, then use the default content id.
     * 
     * @param toolSessionId the generated tool session id.
     * @param toolSessionName the tool session name.
     * @param toolContentId the tool content id specified.
     * @throws ToolException if an error occurs e.g. defaultContent is missing.
     */
    void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException;
    
    /**
     * Call the controller service to complete and leave the tool session.
     * @param toolSessionId the runtime tool session id.
     * @return the url for next activity.
     * @throws DataMissingException if no tool session matches the toolSessionId 
     * @throws ToolException if any other error occurs
     */
    String leaveToolSession(Long toolSessionId, Long learnerId) 
    	throws DataMissingException, ToolException;

    /**
     * Export the XML fragment for the session export. Not used yet - will be used 
     * when we do exports of designs and lesson data.
     * @throws DataMissingException if no tool session matches the toolSessionId 
     * @throws ToolException if any other error occurs
     */
    ToolSessionExportOutputData exportToolSession(Long toolSessionId) 
    	throws DataMissingException, ToolException;

    /**
     * Export the XML fragment for the session export. Not used yet - will be used 
     * when we do exports of designs and lesson data.
     * @throws DataMissingException if no tool session matches the toolSessionId 
     * @throws ToolException if any other error occurs
     */
    ToolSessionExportOutputData exportToolSession(List toolSessionIds) 
    	throws DataMissingException, ToolException;
    
    /**
     * Remove sesson data according specified the tool session id. 
     * @param toolSessionId the generated tool session id.
     * @throws DataMissingException if no tool session matches the toolSessionId 
     * @throws ToolException if any other error occurs
     */
    void removeToolSession(Long toolSessionId)
    	throws DataMissingException, ToolException;
    
    /** Get all the outputs that match the list of names. 
     * 
     * If names is null, then all possible output will be returned. If names is an array of length 0, then no output will be returned. 
     * 
     * The toolSessionId and learnerId will be used to determine the data on which the outputs are to be based. If the output is for 
     * the entire group/class (e.g. average mark) then the tool can ignore the learnerId. 
     * 
     * If the learnerId is null, then return the outputs based on all learners in that toolSession. If the 
     * output is nonsense for all learners, then return an "empty" but valid answer. For example, for a mark
     * you might return 0.
     *
     * Note: the learnerId may not be the userId of the current user as the current user may be a staff member.
     *
     * The tool's output will be returned in a map, where the key is the "name" for each output and the ToolOutput is the output for 
     * that "name". At present, if there are multiple attempts at an activity for one learner, we assume each attempt would have a 
     * different toolSessionId, and hence getToolOutput[] would be called multiple times. This may not be a valid assumption.
 	*/
    SortedMap<String, ToolOutput> getToolOutput(List<String> names, Long toolSessionId, Long learnerId);
    
    /** 
     * Get the outputs for a particular tool output name. 
     * 
     * The toolSessionId and learnerId will be used to determine the data on which the outputs are to be based. If the output is for 
     * the entire group/class (e.g. average mark) then the tool can ignore the learnerId. 
     * 
     * If the learnerId is null, then return the outputs based on all learners in that toolSession. If the 
     * output is nonsense for all learners, then return an "empty" but valid answer. For example, for a mark
     * you might return 0.
     *
     * Note: the learnerId may not be the userId of the current user as the current user may be a staff member.
 	*/
    ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId);

    /**
     * Notifies tool that the user is force completed. Currently it's been utilized only by leader aware tools, which
     * copy results from leader to non-leader. All other tools leave this method blank.
     * 
     * @param toolSessionId
     * @param learner
     *            user to be force completed
     * @throws ToolException
     */
    void forceCompleteUser(Long toolSessionId, User user);
    
}
