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

/* $Id$ */
package org.lamsfoundation.lams.tool.imageGallery.service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.lamsfoundation.lams.tool.OutputFactory;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageComment;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGallery;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGalleryItem;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGallerySession;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGalleryUser;

public class ImageGalleryOutputFactory extends OutputFactory {

    /** The number of posts the learner has made in one lesson. */
    protected final static String OUTPUT_NAME_LEARNER_NUM_IMAGES_UPLOADED = "learner.number.of.images.uploaded";
    protected final static String OUTPUT_NAME_LEARNER_NUM_COMMENTS = "learner.number.of.comments";
    protected final static String OUTPUT_NAME_LEARNER_NUM_VOTES = "learner.number.of.votes";

    /**
     * @see org.lamsfoundation.lams.tool.OutputDefinitionFactory#getToolOutputDefinitions(java.lang.Object)
     */
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Object toolContentObject) {

	TreeMap<String, ToolOutputDefinition> definitionMap = new TreeMap<String, ToolOutputDefinition>();
	
	ToolOutputDefinition definition = buildRangeDefinition(OUTPUT_NAME_LEARNER_NUM_IMAGES_UPLOADED, new Long(0), null);
	definitionMap.put(OUTPUT_NAME_LEARNER_NUM_IMAGES_UPLOADED, definition);	

	definition = buildRangeDefinition(OUTPUT_NAME_LEARNER_NUM_COMMENTS, new Long(0), null);
	definitionMap.put(OUTPUT_NAME_LEARNER_NUM_COMMENTS, definition);
	
	definition = buildRangeDefinition(OUTPUT_NAME_LEARNER_NUM_VOTES, new Long(0), null);
	definitionMap.put(OUTPUT_NAME_LEARNER_NUM_VOTES, definition);

	return definitionMap;
    }

    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, IImageGalleryService imageGalleryService, Long toolSessionId,
	    Long learnerId) {

	TreeMap<String, ToolOutput> output = new TreeMap<String, ToolOutput>();

	ImageGallerySession session = imageGalleryService.getImageGallerySessionBySessionId(toolSessionId);
	if (session != null) {

	    ImageGalleryUser user = imageGalleryService.getUserByIDAndSession(learnerId, toolSessionId);

	    if (names == null || names.contains(OUTPUT_NAME_LEARNER_NUM_IMAGES_UPLOADED)) {
		output.put(OUTPUT_NAME_LEARNER_NUM_IMAGES_UPLOADED, getNumUploadedImages(user, session));
	    }
	    if (names == null || names.contains(OUTPUT_NAME_LEARNER_NUM_COMMENTS)) {
		output.put(OUTPUT_NAME_LEARNER_NUM_COMMENTS, getNumComments(user, session));
	    }
	    if (names == null || names.contains(OUTPUT_NAME_LEARNER_NUM_VOTES)) {
		output.put(OUTPUT_NAME_LEARNER_NUM_VOTES, getNumVotes(user, session, imageGalleryService));
	    }	    
	}

	return output;
    }

    public ToolOutput getToolOutput(String name, IImageGalleryService imageGalleryService, Long toolSessionId, Long learnerId) {
	if (name != null) {
	    ImageGallerySession session = imageGalleryService.getImageGallerySessionBySessionId(toolSessionId);
	    if (session != null) {

		ImageGalleryUser user = imageGalleryService.getUserByIDAndSession(learnerId, toolSessionId);

		if (name.equals(OUTPUT_NAME_LEARNER_NUM_IMAGES_UPLOADED)) {
		    return getNumUploadedImages(user, session);
		} else if (name.equals(OUTPUT_NAME_LEARNER_NUM_COMMENTS)) {
		    return getNumComments(user, session);
		} else if (name.equals(OUTPUT_NAME_LEARNER_NUM_VOTES)) {
		    return getNumVotes(user, session, imageGalleryService);
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
	
	return new ToolOutput(ImageGalleryOutputFactory.OUTPUT_NAME_LEARNER_NUM_IMAGES_UPLOADED, getI18NText(
		ImageGalleryOutputFactory.OUTPUT_NAME_LEARNER_NUM_IMAGES_UPLOADED, true), countImages);
    }

    /**
     * Get the number of images for a specific user. Will always return a ToolOutput object.
     */
    private ToolOutput getNumComments(ImageGalleryUser user, ImageGallerySession session) {
	ImageGallery imageGallery = session.getImageGallery();
	
	int countComments = 0;
	if (user != null) {
	    Set<ImageGalleryItem> allImages = imageGallery.getImageGalleryItems();
	    Iterator<ImageGalleryItem> it = allImages.iterator();
	    while(it.hasNext()) {
		ImageGalleryItem image = it.next();
		Set<ImageComment> imageComments = image.getComments();
		for (ImageComment comment : imageComments) {
		    if (user.getUserId().equals(comment.getCreateBy().getUserId())) {
			countComments++;   
		    }
		}
	    }
	} else {
	    Set<ImageGalleryItem> allImages = imageGallery.getImageGalleryItems();	    
	    for (ImageGalleryItem image : allImages) {
		Set<ImageComment> imageComments = image.getComments();
		for (ImageComment comment : imageComments) {
		    countComments++;   
		}
	    }
	}
	
	return new ToolOutput(ImageGalleryOutputFactory.OUTPUT_NAME_LEARNER_NUM_IMAGES_UPLOADED, getI18NText(
		ImageGalleryOutputFactory.OUTPUT_NAME_LEARNER_NUM_IMAGES_UPLOADED, true), countComments);
    }
    
    /**
     * Get the number of images for a specific user. Will always return a ToolOutput object.
     */
    private ToolOutput getNumVotes(ImageGalleryUser user, ImageGallerySession session, IImageGalleryService imageGalleryService) {
	ImageGallery imageGallery = session.getImageGallery();
	
	int countVotes = 0;
	if (user != null) {
	    if ((user.getVotedImageUid() != null) && !user.getVotedImageUid().equals(new Long(0))){
		countVotes = 1;
	    }
	} else {
	    List<ImageGalleryUser> users = imageGalleryService.getUserListBySessionId(session.getSessionId());
	    for (ImageGalleryUser dbUser : users) {
		if ((dbUser.getVotedImageUid() != null) && !dbUser.getVotedImageUid().equals(new Long(0))){
		    countVotes++;
		}
	    }
	}
	
	return new ToolOutput(ImageGalleryOutputFactory.OUTPUT_NAME_LEARNER_NUM_IMAGES_UPLOADED, getI18NText(
		ImageGalleryOutputFactory.OUTPUT_NAME_LEARNER_NUM_IMAGES_UPLOADED, true), countVotes);
    }

}
