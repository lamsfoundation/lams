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
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.LearningLibrary;
import org.lamsfoundation.lams.learningdesign.OptionsActivity;
import org.lamsfoundation.lams.learningdesign.RandomGrouping;
import org.lamsfoundation.lams.learningdesign.Transition;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.ActivityDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.GroupingDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.LearningDesignDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.LearningLibraryDAO;
import org.lamsfoundation.lams.learningdesign.dto.LearningDesignDTO;
import org.lamsfoundation.lams.learningdesign.dto.LearningLibraryDTO;
import org.lamsfoundation.lams.learningdesign.dto.LibraryActivityDTO;
import org.lamsfoundation.lams.learningdesign.dto.ValidationErrorDTO;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.util.ILoadedMessageSourceService;
import org.lamsfoundation.lams.util.MessageService;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * The LearningDesignService class contains methods which applies validation rules
 * to determine the validity of a learning design. For the validation rules, please
 * see the AuthoringDesignDoc in lams_documents.
 * 
 * If no errors are found, a learning design is considered valid, it will set the valid_design_flag to true.
 * If validation fails, the validation messages will be returned in the response packet. The validation
 * messages are a list of ValidationErrorDTO objects.
 * 
 * @author mtruong
 *
 */
public class LearningDesignService implements ILearningDesignService{
	
	protected Logger log = Logger.getLogger(LearningDesignService.class);
	protected MessageService messageService;
	
	protected LearningDesignDAO learningDesignDAO;
	protected ActivityDAO activityDAO;
	protected GroupingDAO groupingDAO;
	
	protected LearningLibraryDAO learningLibraryDAO;
	protected ILoadedMessageSourceService toolActMessageService;
	
	/*
	 * Default constructor
	 * 
	 */
	public LearningDesignService()
	{
	}

	/**********************************************
	 * Setter/Getter Methods
	 * *******************************************/
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
	
	/** Access a message service related to a programatically loaded message file.
	 * Authoring uses this to access the message files for tools and activities.
	 */
	public ILoadedMessageSourceService getToolActMessageService() {
		return toolActMessageService;
	}

	public void setToolActMessageService(ILoadedMessageSourceService toolActMessageService) {
		this.toolActMessageService = toolActMessageService;
	}
	
	public void setLearningLibraryDAO(LearningLibraryDAO learningLibraryDAO) {
		this.learningLibraryDAO = learningLibraryDAO;
	}
	
	public void setActivityDAO(ActivityDAO activityDAO) {
		this.activityDAO = activityDAO;
	}

	public void setLearningDesignDAO(LearningDesignDAO learningDesignDAO) {
		this.learningDesignDAO = learningDesignDAO;
	}
	
	public void setGroupingDAO(GroupingDAO groupingDAO) {
		this.groupingDAO = groupingDAO;
	}

	
	/**********************************************
	 * Service Methods
	 * *******************************************/

	/**
	 * Get the learning design DTO, suitable to send to Flash via WDDX 
	 * @param learningDesignId
	 * @return LearningDesignDTO
	 */
	public LearningDesignDTO getLearningDesignDTO(Long learningDesignID) {
		LearningDesign design = learningDesignID!=null ? learningDesignDAO.getLearningDesignById(learningDesignID) : null;
		return design != null ? new LearningDesignDTO(design,activityDAO,groupingDAO) : null;
	}
	
	/**
	 * This method calls other validation methods which apply the validation 
	 * rules to determine whether or not the learning design is valid.
	 *
	 * @param learningDesign
	 * @return list of validation errors
	 */
	public Vector<ValidationErrorDTO> validateLearningDesign(LearningDesign learningDesign)
	{
		LearningDesignValidator validator = new LearningDesignValidator(learningDesign, messageService);
		return validator.validate();
	}
	
	public void setValid(Long learningLibraryId, boolean valid) {
		LearningLibrary library = learningLibraryDAO.getLearningLibraryById(learningLibraryId);
		library.setValidLibrary(valid);
		learningLibraryDAO.update(library);
	}

	public ArrayList<LearningLibraryDTO> getAllLearningLibraryDetails()throws IOException{
		//only return valid learning library
		return getAllLearningLibraryDetails(true);
	}
	public ArrayList<LearningLibraryDTO> getAllLearningLibraryDetails(boolean valid)throws IOException{
		Iterator iterator= learningLibraryDAO.getAllLearningLibraries(valid).iterator();
		ArrayList<LearningLibraryDTO> libraries = new ArrayList<LearningLibraryDTO>();
		while(iterator.hasNext()){
			LearningLibrary learningLibrary = (LearningLibrary)iterator.next();		
			List templateActivities = activityDAO.getActivitiesByLibraryID(learningLibrary.getLearningLibraryId());
			
			if (templateActivities!=null & templateActivities.size()==0)
			{
				log.error("Learning Library with ID " + learningLibrary.getLearningLibraryId() + " does not have a template activity");
			}
			// convert library to DTO format
			LearningLibraryDTO libraryDTO = learningLibrary.getLearningLibraryDTO(templateActivities);
			internationaliseActivities(libraryDTO.getTemplateActivities());
			libraries.add(libraryDTO);
		}
		return libraries;
	}

	private void internationaliseActivities(Collection activities) {		
		Iterator iter = activities.iterator();
		Locale locale = LocaleContextHolder.getLocale();
		
		if ( log.isDebugEnabled()) {
			if ( locale != null )
				log.debug("internationaliseActivities: Locale has lang/country "+locale.getLanguage()+","+locale.getCountry());
			else 
				log.debug("internationaliseActivities: Locale missing.");
		}
		
		while (iter.hasNext()) {
			LibraryActivityDTO activity = (LibraryActivityDTO) iter.next();
			// update the activity fields
			String languageFilename = activity.getLanguageFile();
			if ( languageFilename  != null ) {
				MessageSource toolMessageSource = toolActMessageService.getMessageService(languageFilename);
				if ( toolMessageSource != null ) {
					activity.setActivityTitle(toolMessageSource.getMessage(Activity.I18N_TITLE,null,activity.getActivityTitle(),locale));
					activity.setDescription(toolMessageSource.getMessage(Activity.I18N_DESCRIPTION,null,activity.getDescription(),locale));
					activity.setHelpText(toolMessageSource.getMessage(Activity.I18N_HELP_TEXT,null,activity.getHelpText(),locale));
				} else {
					log.warn("Unable to internationalise the library activity "+activity.getActivityID()+" "+activity.getActivityTitle()
							+" message file "+activity.getLanguageFile()+". Activity Message source not available");
				}

				// update the tool field - note only tool activities have a tool entry.
				if ( activity.getActivityTypeID()!=null && Activity.TOOL_ACTIVITY_TYPE == activity.getActivityTypeID().intValue() ) {
					languageFilename = activity.getToolLanguageFile();
					toolMessageSource = toolActMessageService.getMessageService(languageFilename);
					if ( toolMessageSource != null ) {
						activity.setToolDisplayName(toolMessageSource.getMessage(Tool.I18N_DISPLAY_NAME,null,activity.getToolDisplayName(),locale));
					} else {
						log.warn("Unable to internationalise the library activity "+activity.getActivityID()+" "+activity.getActivityTitle()
							+" message file "+activity.getLanguageFile()+". Tool Message source not available");
					}
				}
			} else {
				log.warn("Unable to internationalise the library activity "+activity.getActivityID()+" "+activity.getActivityTitle()
						+". No message file supplied.");
			}
		}
	}

	
}