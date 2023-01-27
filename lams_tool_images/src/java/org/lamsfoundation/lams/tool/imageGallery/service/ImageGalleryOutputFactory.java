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

package org.lamsfoundation.lams.tool.imageGallery.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.lamsfoundation.lams.rating.dto.ItemRatingDTO;
import org.lamsfoundation.lams.tool.OutputFactory;
import org.lamsfoundation.lams.tool.SimpleURL;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.imageGallery.ImageGalleryConstants;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGallery;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGalleryItem;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGallerySession;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGalleryUser;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.web.util.AttributeNames;

public class ImageGalleryOutputFactory extends OutputFactory {

    /** The number of posts the learner has made in one lesson. */
    protected final static String OUTPUT_NAME_LEARNER_NUM_IMAGES_UPLOADED = "learner.number.of.images.uploaded";
    protected final static String OUTPUT_NAME_LEARNER_NUM_COMMENTS = "learner.number.of.comments";
    protected final static String OUTPUT_NAME_LEARNER_NUM_VOTES = "learner.number.of.votes";

    protected final static String OUTPUT_NAME_UPLOADED_IMAGES_URLS = "uploaded.images.urls";

    /**
     * @see org.lamsfoundation.lams.tool.OutputDefinitionFactory#getToolOutputDefinitions(java.lang.Object)
     */
    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Object toolContentObject,
	    int definitionType) {

	TreeMap<String, ToolOutputDefinition> definitionMap = new TreeMap<>();

	Class simpleUrlArrayClass = SimpleURL[].class;
	switch (definitionType) {
	    case ToolOutputDefinition.DATA_OUTPUT_DEFINITION_TYPE_CONDITION:
		ToolOutputDefinition definition = buildRangeDefinition(
			ImageGalleryOutputFactory.OUTPUT_NAME_LEARNER_NUM_IMAGES_UPLOADED, new Long(0), null);
		definitionMap.put(ImageGalleryOutputFactory.OUTPUT_NAME_LEARNER_NUM_IMAGES_UPLOADED, definition);

		definition = buildRangeDefinition(ImageGalleryOutputFactory.OUTPUT_NAME_LEARNER_NUM_COMMENTS,
			new Long(0), null);
		definitionMap.put(ImageGalleryOutputFactory.OUTPUT_NAME_LEARNER_NUM_COMMENTS, definition);

		definition = buildRangeDefinition(ImageGalleryOutputFactory.OUTPUT_NAME_LEARNER_NUM_VOTES, new Long(0),
			null);
		definitionMap.put(ImageGalleryOutputFactory.OUTPUT_NAME_LEARNER_NUM_VOTES, definition);
		break;
	    case ToolOutputDefinition.DATA_OUTPUT_DEFINITION_TYPE_DATA_FLOW:
		ToolOutputDefinition allUsersUploadedImagesDefinition = buildComplexOutputDefinition(
			ImageGalleryOutputFactory.OUTPUT_NAME_UPLOADED_IMAGES_URLS, simpleUrlArrayClass);
		definitionMap.put(ImageGalleryOutputFactory.OUTPUT_NAME_UPLOADED_IMAGES_URLS,
			allUsersUploadedImagesDefinition);
		break;
	}

	return definitionMap;
    }

    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, IImageGalleryService imageGalleryService,
	    Long toolSessionId, Long learnerId) {

	TreeMap<String, ToolOutput> outputs = new TreeMap<>();
	// tool output cache
	TreeMap<String, ToolOutput> baseOutputs = new TreeMap<>();
	if (names == null) {
	    outputs.put(ImageGalleryOutputFactory.OUTPUT_NAME_LEARNER_NUM_IMAGES_UPLOADED,
		    getToolOutput(ImageGalleryOutputFactory.OUTPUT_NAME_LEARNER_NUM_IMAGES_UPLOADED,
			    imageGalleryService, toolSessionId, learnerId));
	    outputs.put(ImageGalleryOutputFactory.OUTPUT_NAME_LEARNER_NUM_COMMENTS,
		    getToolOutput(ImageGalleryOutputFactory.OUTPUT_NAME_LEARNER_NUM_COMMENTS, imageGalleryService,
			    toolSessionId, learnerId));
	    outputs.put(ImageGalleryOutputFactory.OUTPUT_NAME_LEARNER_NUM_VOTES,
		    getToolOutput(ImageGalleryOutputFactory.OUTPUT_NAME_LEARNER_NUM_VOTES, imageGalleryService,
			    toolSessionId, learnerId));
	    outputs.put(ImageGalleryOutputFactory.OUTPUT_NAME_UPLOADED_IMAGES_URLS,
		    getToolOutput(ImageGalleryOutputFactory.OUTPUT_NAME_UPLOADED_IMAGES_URLS, imageGalleryService,
			    toolSessionId, learnerId));
	} else {
	    for (String name : names) {
		String[] nameParts = splitConditionName(name);
		if (baseOutputs.get(nameParts[0]) != null) {
		    outputs.put(name, baseOutputs.get(nameParts[0]));
		} else {
		    ToolOutput output = getToolOutput(name, imageGalleryService, toolSessionId, learnerId);
		    if (output != null) {
			outputs.put(name, output);
			baseOutputs.put(nameParts[0], output);
		    }
		}
	    }
	}

	return outputs;
    }

    public ToolOutput getToolOutput(String name, IImageGalleryService imageGalleryService, Long toolSessionId,
	    Long learnerId) {
	if (name != null) {
	    String[] nameParts = splitConditionName(name);
	    ImageGallerySession session = imageGalleryService.getImageGallerySessionBySessionId(toolSessionId);
	    if (session != null) {

		ImageGalleryUser user = imageGalleryService.getUserByIDAndSession(learnerId, toolSessionId);

		if (nameParts[0].equals(ImageGalleryOutputFactory.OUTPUT_NAME_LEARNER_NUM_IMAGES_UPLOADED)) {
		    return getNumUploadedImages(user, session);
		} else if (nameParts[0].equals(ImageGalleryOutputFactory.OUTPUT_NAME_LEARNER_NUM_COMMENTS)) {
		    return getNumComments(imageGalleryService, user, session);
		} else if (nameParts[0].equals(ImageGalleryOutputFactory.OUTPUT_NAME_LEARNER_NUM_VOTES)) {
		    return getNumVotes(user, session, imageGalleryService);
		} else if (nameParts[0].equals(ImageGalleryOutputFactory.OUTPUT_NAME_UPLOADED_IMAGES_URLS)) {
		    List<SimpleURL> uploadedImagesUrls = new ArrayList<>();
		    Set<ImageGalleryItem> sessionImages = imageGalleryService
			    .getImagesForGroup(session.getImageGallery(), toolSessionId);
		    for (ImageGalleryItem image : sessionImages) {
			if (!image.isCreateByAuthor()) {
			    String serverUrl = Configuration.get(ConfigurationKeys.SERVER_URL);
			    String innerUrl = serverUrl + "download/?uuid=" + image.getOriginalFileDisplayUuid()
				    + "&preferDownload=false&" + AttributeNames.PARAM_TOOL_CONTENT_HANDLER_NAME + "="
				    + ImageGalleryConstants.TOOL_CONTENT_HANDLER_NAME;
			    String fullUrl = "javascript:var dummy = window.open('" + innerUrl + "','"
				    + image.getTitle() + "','resizable,width=" + image.getOriginalImageWidth()
				    + ",height=" + image.getOriginalImageHeight() + ",scrollbars')";
			    SimpleURL simpleUrl = new SimpleURL(image.getTitle(), fullUrl);
			    uploadedImagesUrls.add(simpleUrl);
			}
		    }
		    SimpleURL[] uploadedImagesUrlsArray = uploadedImagesUrls.toArray(new SimpleURL[] {});
		    return new ToolOutput(ImageGalleryOutputFactory.OUTPUT_NAME_UPLOADED_IMAGES_URLS,
			    getI18NText(ImageGalleryOutputFactory.OUTPUT_NAME_UPLOADED_IMAGES_URLS, true),
			    uploadedImagesUrlsArray, false);
		}
	    }
	}
	return null;
    }

    /**
     * Get the number of images for a specific user. Will always return a ToolOutput object.
     */
    private ToolOutput getNumUploadedImages(ImageGalleryUser user, ImageGallerySession session) {
	ImageGallery imageGallery = session.getImageGallery();

	int countImages = 0;
	if (user != null) {
	    Set<ImageGalleryItem> allImages = imageGallery.getImageGalleryItems();

	    for (ImageGalleryItem image : allImages) {
		if (user.getUserId().equals(image.getCreateBy().getUserId())) {
		    countImages++;
		}
	    }
	} else {
	    countImages = imageGallery.getImageGalleryItems().size();
	}

	return new ToolOutput(ImageGalleryOutputFactory.OUTPUT_NAME_LEARNER_NUM_IMAGES_UPLOADED,
		getI18NText(ImageGalleryOutputFactory.OUTPUT_NAME_LEARNER_NUM_IMAGES_UPLOADED, true), countImages);
    }

    /**
     * Get the number of images for a specific user. Will always return a ToolOutput object.
     */
    private ToolOutput getNumComments(IImageGalleryService imageGalleryService, ImageGalleryUser user,
	    ImageGallerySession session) {
	ImageGallery imageGallery = session.getImageGallery();
	Long contentId = imageGallery.getContentId();

	Set<ImageGalleryItem> allImages = imageGallery.getImageGalleryItems();
	List<Long> itemIds = new LinkedList<>();
	for (ImageGalleryItem image : allImages) {
	    itemIds.add(image.getUid());
	}

	boolean isCommentsEnabled = imageGalleryService.isCommentsEnabled(contentId);

	int countComments = 0;
	if (isCommentsEnabled) {

	    boolean isCommentsByOtherUsersRequired = user == null;
	    Long userId = user == null ? -1L : user.getUserId();
	    List<ItemRatingDTO> ratingCriteriaDtos = imageGalleryService.getRatingCriteriaDtos(contentId,
		    session.getSessionId(), itemIds, isCommentsByOtherUsersRequired, userId);

	    if (user != null) {
		for (ItemRatingDTO ratingCriteriaDto : ratingCriteriaDtos) {
		    boolean isUserCommentedOnThisImage = ratingCriteriaDto.getCommentPostedByUser() != null;
		    if (isUserCommentedOnThisImage) {
			countComments++;
		    }
		}

	    } else {
		for (ItemRatingDTO ratingCriteriaDto : ratingCriteriaDtos) {
		    countComments += ratingCriteriaDto.getCommentDtos().size();
		}
	    }
	}

	return new ToolOutput(ImageGalleryOutputFactory.OUTPUT_NAME_LEARNER_NUM_IMAGES_UPLOADED,
		getI18NText(ImageGalleryOutputFactory.OUTPUT_NAME_LEARNER_NUM_IMAGES_UPLOADED, true), countComments);
    }

    /**
     * Get the number of imageVotes for a specific user. Will always return a ToolOutput object.
     */
    private ToolOutput getNumVotes(ImageGalleryUser user, ImageGallerySession session,
	    IImageGalleryService imageGalleryService) {

	int countVotes = 0;
	if (user != null) {
	    countVotes = imageGalleryService.getNumberVotesByUserId(user.getUserId());
	} else {
	    List<ImageGalleryUser> users = imageGalleryService.getUserListBySessionId(session.getSessionId());
	    for (ImageGalleryUser dbUser : users) {
		int userCountVotes = imageGalleryService.getNumberVotesByUserId(user.getUserId());
		countVotes += userCountVotes;
	    }
	}

	return new ToolOutput(ImageGalleryOutputFactory.OUTPUT_NAME_LEARNER_NUM_IMAGES_UPLOADED,
		getI18NText(ImageGalleryOutputFactory.OUTPUT_NAME_LEARNER_NUM_IMAGES_UPLOADED, true), countVotes);
    }

}
