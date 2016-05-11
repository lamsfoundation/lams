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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */


package org.lamsfoundation.lams.tool.videoRecorder.service;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.lamsfoundation.lams.tool.OutputFactory;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.videoRecorder.util.VideoRecorderConstants;

/**
 * Output factory for VideoRecorder tool. Currently it provides only one type of output - the entry that user provided.
 *
 * @author Paul Georges
 */
public class VideoRecorderOutputFactory extends OutputFactory {

    /**
     * {@inheritDoc}
     */
    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Object toolContentObject,
	    int definitionType) throws ToolException {
	SortedMap<String, ToolOutputDefinition> definitionMap = new TreeMap<String, ToolOutputDefinition>();

	ToolOutputDefinition numberOfRecordingsDefinition = buildRangeDefinition(
		VideoRecorderConstants.NB_RECORDINGS_DEFINITION_NAME, new Long(0), null);
	definitionMap.put(VideoRecorderConstants.NB_RECORDINGS_DEFINITION_NAME, numberOfRecordingsDefinition);

	ToolOutputDefinition numberOfCommentsDefinition = buildRangeDefinition(
		VideoRecorderConstants.NB_COMMENTS_DEFINITION_NAME, new Long(0), null);
	definitionMap.put(VideoRecorderConstants.NB_COMMENTS_DEFINITION_NAME, numberOfCommentsDefinition);

	ToolOutputDefinition numberOfRatingsDefinition = buildRangeDefinition(
		VideoRecorderConstants.NB_RATINGS_DEFINITION_NAME, new Long(0), null);
	definitionMap.put(VideoRecorderConstants.NB_RATINGS_DEFINITION_NAME, numberOfRatingsDefinition);

	return definitionMap;
    }

    /**
     * Follows {@link VideoRecorderService#getToolOutput(List, Long, Long)}.
     *
     */
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, IVideoRecorderService videoRecorderService,
	    Long toolSessionId, Long learnerId) {

	TreeMap<String, ToolOutput> outputs = new TreeMap<String, ToolOutput>();

	if (names == null || names.contains(VideoRecorderConstants.NB_RECORDINGS_DEFINITION_NAME)) {
	    outputs.put(VideoRecorderConstants.NB_RECORDINGS_DEFINITION_NAME,
		    getNbRecordings(videoRecorderService, learnerId, toolSessionId));
	}

	if (names == null || names.contains(VideoRecorderConstants.NB_COMMENTS_DEFINITION_NAME)) {
	    outputs.put(VideoRecorderConstants.NB_COMMENTS_DEFINITION_NAME,
		    getNbComments(videoRecorderService, learnerId, toolSessionId));
	}

	if (names == null || names.contains(VideoRecorderConstants.NB_RATINGS_DEFINITION_NAME)) {
	    outputs.put(VideoRecorderConstants.NB_RATINGS_DEFINITION_NAME,
		    getNbRatings(videoRecorderService, learnerId, toolSessionId));
	}

	return outputs;

    }

    public ToolOutput getToolOutput(String name, IVideoRecorderService videoRecorderService, Long toolSessionId,
	    Long learnerId) {
	ToolOutput toolOutput = null;
	if (name != null && name.equals(VideoRecorderConstants.NB_RECORDINGS_DEFINITION_NAME)) {
	    toolOutput = getNbRecordings(videoRecorderService, learnerId, toolSessionId);
	} else if (name != null && name.equals(VideoRecorderConstants.NB_COMMENTS_DEFINITION_NAME)) {
	    toolOutput = getNbComments(videoRecorderService, learnerId, toolSessionId);
	} else if (name != null && name.equals(VideoRecorderConstants.NB_RATINGS_DEFINITION_NAME)) {
	    toolOutput = getNbRatings(videoRecorderService, learnerId, toolSessionId);
	}

	return toolOutput;
    }

    private ToolOutput getNbRecordings(IVideoRecorderService videoRecorderService, Long learnerId, Long toolSessionId) {
	Long nb = videoRecorderService.getNbRecordings(learnerId, toolSessionId);
	return new ToolOutput(VideoRecorderConstants.NB_RECORDINGS_DEFINITION_NAME,
		getI18NText(VideoRecorderConstants.NB_RECORDINGS_DEFINITION_NAME, true), new Long(nb));
    }

    private ToolOutput getNbComments(IVideoRecorderService videoRecorderService, Long learnerId, Long toolSessionId) {
	Long nb = videoRecorderService.getNbComments(learnerId, toolSessionId);
	return new ToolOutput(VideoRecorderConstants.NB_COMMENTS_DEFINITION_NAME,
		getI18NText(VideoRecorderConstants.NB_COMMENTS_DEFINITION_NAME, true), new Long(nb));
    }

    private ToolOutput getNbRatings(IVideoRecorderService videoRecorderService, Long learnerId, Long toolSessionId) {
	Long nb = videoRecorderService.getNbRatings(learnerId, toolSessionId);
	return new ToolOutput(VideoRecorderConstants.NB_RATINGS_DEFINITION_NAME,
		getI18NText(VideoRecorderConstants.NB_RATINGS_DEFINITION_NAME, true), new Long(nb));
    }
}
