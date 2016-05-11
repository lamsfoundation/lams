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


package org.lamsfoundation.lams.tool.kaltura.service;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.lamsfoundation.lams.tool.OutputFactory;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.kaltura.util.KalturaConstants;

/**
 * Output factory for Kaltura tool. Currently it provides two types of output - Number of videos viewed and Number of
 * videos uploaded.
 *
 * @author Andrey Balan
 */
public class KalturaOutputFactory extends OutputFactory {

    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Object toolContentObject,
	    int definitionType) throws ToolException {

	SortedMap<String, ToolOutputDefinition> definitionMap = new TreeMap<String, ToolOutputDefinition>();

	ToolOutputDefinition viewedDefinition = buildRangeDefinition(KalturaConstants.LEARNER_NUMBER_VIEWED_VIDEOS,
		new Long(0), null);
	definitionMap.put(KalturaConstants.LEARNER_NUMBER_VIEWED_VIDEOS, viewedDefinition);

	ToolOutputDefinition uploadedDefinition = buildRangeDefinition(KalturaConstants.LEARNER_NUMBER_UPLOADED_VIDEOS,
		new Long(0), null);
	definitionMap.put(KalturaConstants.LEARNER_NUMBER_UPLOADED_VIDEOS, uploadedDefinition);

	return definitionMap;
    }

    /**
     * Follows {@link PixlrService#getToolOutput(List, Long, Long)}.
     */
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, IKalturaService kalturaService,
	    Long toolSessionId, Long learnerId) {

	TreeMap<String, ToolOutput> outputs = new TreeMap<String, ToolOutput>();
	// tool output cache
	if (names == null || names.contains(KalturaConstants.LEARNER_NUMBER_VIEWED_VIDEOS)) {
	    outputs.put(KalturaConstants.LEARNER_NUMBER_VIEWED_VIDEOS, getToolOutput(
		    KalturaConstants.LEARNER_NUMBER_VIEWED_VIDEOS, kalturaService, toolSessionId, learnerId));
	}
	if (names == null || names.contains(KalturaConstants.LEARNER_NUMBER_UPLOADED_VIDEOS)) {
	    outputs.put(KalturaConstants.LEARNER_NUMBER_UPLOADED_VIDEOS, getToolOutput(
		    KalturaConstants.LEARNER_NUMBER_UPLOADED_VIDEOS, kalturaService, toolSessionId, learnerId));
	}

	return outputs;

    }

    public ToolOutput getToolOutput(String name, IKalturaService kalturaService, Long toolSessionId, Long learnerId) {
	if (name.equals(KalturaConstants.LEARNER_NUMBER_VIEWED_VIDEOS)) {
	    return getNumberViewedVideos(kalturaService, toolSessionId, learnerId);
	} else if (name.equals(KalturaConstants.LEARNER_NUMBER_UPLOADED_VIDEOS)) {
	    return getNumberUploadedVideos(kalturaService, toolSessionId, learnerId);
	}

	return null;
    }

    /**
     * Get the number of videos viewed.
     */
    private ToolOutput getNumberViewedVideos(IKalturaService kalturaService, Long toolSessionId, Long learnerId) {

	int numberViewedVideos = kalturaService.getNumberViewedVideos(toolSessionId, learnerId);

	return new ToolOutput(KalturaConstants.LEARNER_NUMBER_VIEWED_VIDEOS,
		getI18NText(KalturaConstants.LEARNER_NUMBER_VIEWED_VIDEOS, true), numberViewedVideos);

    }

    /**
     * Get the number of videos uploaded.
     */
    private ToolOutput getNumberUploadedVideos(IKalturaService kalturaService, Long toolSessionId, Long learnerId) {

	int numberUploadedVideos = kalturaService.getNumberUploadedVideos(toolSessionId, learnerId);

	return new ToolOutput(KalturaConstants.LEARNER_NUMBER_UPLOADED_VIDEOS,
		getI18NText(KalturaConstants.LEARNER_NUMBER_UPLOADED_VIDEOS, true), numberUploadedVideos);

    }
}
