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
package org.lamsfoundation.lams.learningdesign.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.LearningLibrary;
import org.lamsfoundation.lams.learningdesign.LearningLibraryGroup;
import org.lamsfoundation.lams.learningdesign.dao.IActivityDAO;
import org.lamsfoundation.lams.learningdesign.dao.IGroupingDAO;
import org.lamsfoundation.lams.learningdesign.dao.ILearningDesignDAO;
import org.lamsfoundation.lams.learningdesign.dao.ILearningLibraryDAO;
import org.lamsfoundation.lams.learningdesign.dto.LearningDesignDTO;
import org.lamsfoundation.lams.learningdesign.dto.LearningLibraryDTO;
import org.lamsfoundation.lams.learningdesign.dto.LibraryActivityDTO;
import org.lamsfoundation.lams.learningdesign.dto.ValidationErrorDTO;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.dto.ToolDTO;
import org.lamsfoundation.lams.tool.dto.ToolDTONameComparator;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.ILoadedMessageSourceService;
import org.lamsfoundation.lams.util.MessageService;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * The LearningDesignService class contains methods which applies validation rules to determine the validity of a
 * learning design. For the validation rules, please see the AuthoringDesignDoc in lams_documents.
 * 
 * If no errors are found, a learning design is considered valid, it will set the valid_design_flag to true. If
 * validation fails, the validation messages will be returned in the response packet. The validation messages are a list
 * of ValidationErrorDTO objects.
 * 
 * @author mtruong
 * 
 */
public class LearningDesignService implements ILearningDesignService {

    protected Logger log = Logger.getLogger(LearningDesignService.class);
    protected MessageService messageService;

    protected ILearningDesignDAO learningDesignDAO;
    protected IActivityDAO activityDAO;
    protected IGroupingDAO groupingDAO;

    protected ILearningLibraryDAO learningLibraryDAO;
    protected ILoadedMessageSourceService toolActMessageService;

    private static final String LD_SVG_DIR = "lams-www.war\\secure\\learning-design-images";

    /*
     * Default constructor
     * 
     */
    public LearningDesignService() {
    }

    /**********************************************
     * Setter/Getter Methods
     *******************************************/
    /**
     * Set i18n MessageService
     */
    public void setMessageService(MessageService messageService) {
	this.messageService = messageService;
    }

    /**
     * Get i18n MessageService
     */
    public MessageService getMessageService() {
	return this.messageService;
    }

    /**
     * Access a message service related to a programatically loaded message file. Authoring uses this to access the
     * message files for tools and activities.
     */
    public ILoadedMessageSourceService getToolActMessageService() {
	return toolActMessageService;
    }

    public void setToolActMessageService(ILoadedMessageSourceService toolActMessageService) {
	this.toolActMessageService = toolActMessageService;
    }

    public void setLearningLibraryDAO(ILearningLibraryDAO learningLibraryDAO) {
	this.learningLibraryDAO = learningLibraryDAO;
    }

    public void setActivityDAO(IActivityDAO activityDAO) {
	this.activityDAO = activityDAO;
    }

    public void setLearningDesignDAO(ILearningDesignDAO learningDesignDAO) {
	this.learningDesignDAO = learningDesignDAO;
    }

    public void setGroupingDAO(IGroupingDAO groupingDAO) {
	this.groupingDAO = groupingDAO;
    }

    /**********************************************
     * Service Methods
     *******************************************/

    /**
     * Get the learning design DTO
     * 
     * @param learningDesignId
     * @param languageCode
     *            Two letter language code needed to I18N the help url
     * @return LearningDesignDTO
     */
    @Override
    public LearningDesignDTO getLearningDesignDTO(Long learningDesignID, String languageCode) {
	LearningDesign design = learningDesignID != null ? learningDesignDAO.getLearningDesignById(learningDesignID)
		: null;
	return design != null ? new LearningDesignDTO(design, activityDAO, groupingDAO, languageCode) : null;
    }

    /**
     * This method calls other validation methods which apply the validation rules to determine whether or not the
     * learning design is valid.
     * 
     * @param learningDesign
     * @return list of validation errors
     */
    @Override
    public Vector<ValidationErrorDTO> validateLearningDesign(LearningDesign learningDesign) {
	LearningDesignValidator validator = new LearningDesignValidator(learningDesign, messageService);
	return validator.validate();
    }

    @Override
    public void setValid(Long learningLibraryId, boolean valid) {
	LearningLibrary library = learningLibraryDAO.getLearningLibraryById(learningLibraryId);
	library.setValidLibrary(valid);
	learningLibraryDAO.update(library);
    }

    @Override
    public ArrayList<LearningLibraryDTO> getAllLearningLibraryDetails(String languageCode) throws IOException {
	// only return valid learning library
	return getAllLearningLibraryDetails(true, languageCode);
    }

    @Override
    public LearningLibrary getLearningLibrary(Long learningLibraryId) {
	return (LearningLibrary) learningDesignDAO.find(LearningLibrary.class, learningLibraryId);
    }

    @Override
    public List<LearningLibraryGroup> getLearningLibraryGroups() {
	return learningLibraryDAO.getLearningLibraryGroups();
    }

    @Override
    public void saveLearningLibraryGroups(Collection<LearningLibraryGroup> groups) {
	// find out which groups do not exist anymore and get rid of them
	Collection<LearningLibraryGroup> existingGroups = learningLibraryDAO.getLearningLibraryGroups();
	Collection<LearningLibraryGroup> removeGroups = new HashSet<LearningLibraryGroup>(existingGroups);
	removeGroups.removeAll(groups);
	existingGroups.removeAll(removeGroups);
	learningLibraryDAO.deleteAll(removeGroups);

	// find out which groups are new and which ones need to be updated
	for (LearningLibraryGroup group : groups) {
	    boolean found = false;
	    for (LearningLibraryGroup existingGroup : existingGroups) {
		if (existingGroup.equals(group)) {
		    existingGroup.setName(group.getName());
		    existingGroup.setLearningLibraries(group.getLearningLibraries());
		    learningLibraryDAO.update(existingGroup);

		    found = true;
		    break;
		}
	    }

	    if (!found) {
		learningDesignDAO.insert(group);
	    }
	}
    }

    @SuppressWarnings("unchecked")
    @Override
    public ArrayList<LearningLibraryDTO> getAllLearningLibraryDetails(boolean valid, String languageCode)
	    throws IOException {
	Iterator<LearningLibrary> iterator = learningLibraryDAO.getAllLearningLibraries(valid).iterator();
	ArrayList<LearningLibraryDTO> libraries = new ArrayList<LearningLibraryDTO>();
	while (iterator.hasNext()) {
	    LearningLibrary learningLibrary = iterator.next();
	    List<Activity> templateActivities = activityDAO
		    .getActivitiesByLibraryID(learningLibrary.getLearningLibraryId());

	    if ((templateActivities != null) & (templateActivities.size() == 0)) {
		log.error("Learning Library with ID " + learningLibrary.getLearningLibraryId()
			+ " does not have a template activity");
	    }
	    // convert library to DTO format

	    LearningLibraryDTO libraryDTO = learningLibrary.getLearningLibraryDTO(templateActivities, languageCode);
	    internationaliseActivities(libraryDTO.getTemplateActivities());
	    libraries.add(libraryDTO);
	}
	return libraries;
    }

    /**
     * Gets basic information on available tools.
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<ToolDTO> getToolDTOs(boolean includeParallel, String userName) throws IOException {
	User user = (User) learningLibraryDAO.findByProperty(User.class, "login", userName).get(0);
	String languageCode = user.getLocale().getLanguageIsoCode();
	ArrayList<LearningLibraryDTO> learningLibraries = getAllLearningLibraryDetails(languageCode);
	List<ToolDTO> tools = new ArrayList<ToolDTO>();
	for (LearningLibraryDTO learningLibrary : learningLibraries) {
	    // skip invalid tools
	    boolean isParallel = learningLibrary.getTemplateActivities().size() > 1;
	    if (learningLibrary.getValidFlag() && (includeParallel || !isParallel)) {
		List<LibraryActivityDTO> libraryActivityDTOs = learningLibrary.getTemplateActivities();
		LibraryActivityDTO libraryActivityDTO = libraryActivityDTOs.get(0);
		ToolDTO toolDTO = new ToolDTO();
		if (isParallel) {
		    List<Long> childLibraryIDs = new ArrayList<Long>();
		    for (LibraryActivityDTO childActivityDTO : libraryActivityDTOs) {
			Long childToolID = childActivityDTO.getToolID();
			if (childToolID != null) {
			    childLibraryIDs.add(childToolID);
			}
		    }
		    toolDTO.setChildToolIds(childLibraryIDs.toArray(new Long[] {}));
		} else {
		    toolDTO.setToolId(libraryActivityDTO.getToolID());
		}
		toolDTO.setLearningLibraryId(learningLibrary.getLearningLibraryID());
		toolDTO.setLearningLibraryTitle(learningLibrary.getTitle());
		toolDTO.setToolDisplayName(
			isParallel ? learningLibrary.getTitle() : libraryActivityDTO.getActivityTitle());
		toolDTO.setActivityCategoryID(
			isParallel ? Activity.CATEGORY_SPLIT : libraryActivityDTO.getActivityCategoryID());

		if (libraryActivityDTO.getToolID() == null) {
		    toolDTO.setIconPath(libraryActivityDTO.getLibraryActivityUIImage());
		} else {
		    Tool tool = (Tool) learningLibraryDAO.find(Tool.class, libraryActivityDTO.getToolID());
		    String iconPath = "tool/" + tool.getToolSignature() + "/images/icon_" + tool.getToolIdentifier()
			    + ".svg";
		    toolDTO.setIconPath(iconPath);
		    toolDTO.setSupportsOutputs(tool.getSupportsOutputs());
		    toolDTO.setDefaultToolContentId(tool.getDefaultToolContentId());
		}

		tools.add(toolDTO);
	    }
	}

	Collections.sort(tools, new ToolDTONameComparator());
	return tools;
    }

    private void internationaliseActivities(Collection<LibraryActivityDTO> activities) {
	Iterator<LibraryActivityDTO> iter = activities.iterator();
	Locale locale = LocaleContextHolder.getLocale();

	if (log.isDebugEnabled()) {
	    if (locale == null) {
		log.debug("internationaliseActivities: Locale missing.");
	    }
	}

	while (iter.hasNext()) {
	    LibraryActivityDTO activity = iter.next();
	    // update the activity fields
	    String languageFilename = activity.getLanguageFile();
	    if (languageFilename != null) {
		MessageSource toolMessageSource = toolActMessageService.getMessageService(languageFilename);
		if (toolMessageSource != null) {
		    activity.setActivityTitle(toolMessageSource.getMessage(Activity.I18N_TITLE, null,
			    activity.getActivityTitle(), locale));
		    activity.setDescription(toolMessageSource.getMessage(Activity.I18N_DESCRIPTION, null,
			    activity.getDescription(), locale));
		    activity.setHelpText(toolMessageSource.getMessage(Activity.I18N_HELP_TEXT, null,
			    activity.getHelpText(), locale));
		} else {
		    log.warn("Unable to internationalise the library activity " + activity.getActivityID() + " "
			    + activity.getActivityTitle() + " message file " + activity.getLanguageFile()
			    + ". Activity Message source not available");
		}

		// update the tool field - note only tool activities have a tool entry.
		if ((activity.getActivityTypeID() != null)
			&& (Activity.TOOL_ACTIVITY_TYPE == activity.getActivityTypeID().intValue())) {
		    languageFilename = activity.getToolLanguageFile();
		    toolMessageSource = toolActMessageService.getMessageService(languageFilename);
		    if (toolMessageSource != null) {
			activity.setToolDisplayName(toolMessageSource.getMessage(Tool.I18N_DISPLAY_NAME, null,
				activity.getToolDisplayName(), locale));
		    } else {
			log.warn("Unable to internationalise the library activity " + activity.getActivityID() + " "
				+ activity.getActivityTitle() + " message file " + activity.getLanguageFile()
				+ ". Tool Message source not available");
		    }
		}
	    } else {
		log.warn("Unable to internationalise the library activity " + activity.getActivityID() + " "
			+ activity.getActivityTitle() + ". No message file supplied.");
	    }
	}
    }

    /**
     * Gets LD SVG file path.
     */
    public static String getLearningDesignSVGPath(long learningDesignId) {
	String earDirPath = Configuration.get(ConfigurationKeys.LAMS_EAR_DIR);
	String svgDirPath = FileUtil.getFullPath(earDirPath, LearningDesignService.LD_SVG_DIR);
	return FileUtil.getFullPath(svgDirPath, learningDesignId + ".svg");
    }
}