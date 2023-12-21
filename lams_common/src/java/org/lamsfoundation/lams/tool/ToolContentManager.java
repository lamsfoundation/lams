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

package org.lamsfoundation.lams.tool;

import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;

import java.util.SortedMap;

/**
 * Tool interface that defines the contract regarding tool content manipulation.
 *
 * @author Jacky Fang 2004-12-7
 * @author Fiona Malikoff May 2005
 */
public interface ToolContentManager {

    /**
     * Make a copy of requested tool content. It will be needed by LAMS to create a copy of learning design and start a
     * new tool session. If no content exists with the given fromToolContentId or if fromToolContent is null, then use
     * the default content id.
     *
     * @param fromContentId
     * 	the original tool content id.
     * @param toContentId
     * 	the destination tool content id.
     * @throws ToolException
     * 	if an error occurs e.g. defaultContent is missing
     */
    public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException;

    /**
     * Reset this tool content's define later flag to false. I.e., allowing learners to access it. The toolContentId
     * should already exist in the tool. This method will normally be called after teacher cancels editing content in
     * Monitor.
     *
     * @param toolContentId
     * 	the tool content id of the tool content to be changed.
     * @throws DataMissingException
     * 	if no tool content matches the toolContentId
     * @throws ToolException
     * 	if any other error occurs
     */
    public void resetDefineLater(Long toolContentId) throws DataMissingException, ToolException;

    /**
     * Remove tool's content according specified the content id.
     *
     * If the tool content includes files in the content repository then the files should be removed from the
     * repository. If no matching data exists, the tool should return without throwing an exception.
     *
     * @param toolContentId
     * 	the requested tool content id.
     * @throws ToolException
     * 	if any other error occurs
     */
    public void removeToolContent(Long toolContentId) throws ToolException;

    /**
     * Removes content previously added by the given user.
     */
    public void removeLearnerContent(Long toolContentId, Integer userId, boolean resetActivityCompletionOnly)
	    throws ToolException;

    /**
     * Export the XML fragment for the tool's content, along with any files needed for the content.
     *
     * @throws DataMissingException
     * 	if no tool content matches the toolSessionId
     * @throws ToolException
     * 	if any other error occurs
     */
    public void exportToolContent(Long toolContentId, String toPath) throws DataMissingException, ToolException;

    /**
     * Import the XML fragment for the tool's content, along with any files needed for the content.
     *
     * @throws ToolException
     * 	if any other error occurs
     */
    public void importToolContent(Long toolContentId, Integer newUserUid, String toolContentPath, String fromVersion,
	    String toVersion) throws ToolException;

    /**
     * Get the definitions for possible output for an activity, based on the toolContentId. These may be definitions
     * that are always available for the tool (e.g. number of marks for Multiple Choice) or a custom definition created
     * for a particular activity such as the answer to the third question contains the word Koala and hence the need for
     * the toolContentId
     *
     * @return SortedMap of ToolOutputDefinitions with the key being the name of each definition.
     *
     * 	Added in LAMS 2.1
     */
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId, int definitionType)
	    throws ToolException;

    public Class<?>[] getSupportedToolOutputDefinitionClasses(int definitionType);

    /**
     * Finds title entered in the tool content.
     */
    public String getToolContentTitle(Long toolContentId);

    /**
     * Is an activity being edited by Monitor?
     */
    public boolean isContentEdited(Long toolContentId);

    /**
     * Can the activity be modified?
     */
    public boolean isReadOnly(Long toolContentId);

    /**
     * Get contribution URL to show in monitoring
     */
    public default String getContributionURL(Long toolContentId) {
	return null;
    }
}