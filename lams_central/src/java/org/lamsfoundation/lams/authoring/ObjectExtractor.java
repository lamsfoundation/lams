/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
package org.lamsfoundation.lams.authoring;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ChosenGrouping;
import org.lamsfoundation.lams.learningdesign.GateActivity;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.LearningLibrary;
import org.lamsfoundation.lams.learningdesign.License;
import org.lamsfoundation.lams.learningdesign.OptionsActivity;
import org.lamsfoundation.lams.learningdesign.ParallelActivity;
import org.lamsfoundation.lams.learningdesign.PermissionGateActivity;
import org.lamsfoundation.lams.learningdesign.RandomGrouping;
import org.lamsfoundation.lams.learningdesign.ScheduleGateActivity;
import org.lamsfoundation.lams.learningdesign.SequenceActivity;
import org.lamsfoundation.lams.learningdesign.SynchGateActivity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.Transition;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.ActivityDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.GroupDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.GroupingDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.LearningDesignDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.LearningLibraryDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.LicenseDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.TransitionDAO;
import org.lamsfoundation.lams.learningdesign.dto.AuthoringActivityDTO;
import org.lamsfoundation.lams.learningdesign.dto.GroupingDTO;
import org.lamsfoundation.lams.learningdesign.dto.LearningDesignDTO;
import org.lamsfoundation.lams.learningdesign.dto.TransitionDTO;
import org.lamsfoundation.lams.lesson.LessonClass;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.dao.hibernate.ToolDAO;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;
import org.lamsfoundation.lams.usermanagement.dao.hibernate.UserDAO;
import org.lamsfoundation.lams.usermanagement.dao.hibernate.WorkspaceFolderDAO;
import org.lamsfoundation.lams.util.wddx.WDDXProcessorConversionException;
import org.lamsfoundation.lams.util.wddx.WDDXTAGS;

/**
 * @author Manpreet Minhas
 * 
 * This is a utitlity class for extracting the
 * information from the WDDX packet sent by the FLASH.
 */
public class ObjectExtractor {
	
	protected UserDAO userDAO = null;
	protected LearningDesignDAO learningDesignDAO = null;
	protected ActivityDAO activityDAO =null;
	protected TransitionDAO transitionDAO =null;
	protected WorkspaceFolderDAO workspaceFolderDAO = null;
	protected LearningLibraryDAO learningLibraryDAO = null;
	protected LicenseDAO licenseDAO = null;
	protected GroupingDAO groupingDAO = null;
	protected ToolDAO toolDAO = null;
	protected GroupDAO groupDAO = null;
	
	public ObjectExtractor(UserDAO userDAO,
			LearningDesignDAO learningDesignDAO, ActivityDAO activityDAO,
			WorkspaceFolderDAO workspaceFolderDAO,
			LearningLibraryDAO learningLibraryDAO, LicenseDAO licenseDAO,
			GroupingDAO groupingDAO, ToolDAO toolDAO,
			GroupDAO groupDAO,TransitionDAO transitionDAO) {		
		this.userDAO = userDAO;
		this.learningDesignDAO = learningDesignDAO;
		this.activityDAO = activityDAO;
		this.workspaceFolderDAO = workspaceFolderDAO;
		this.learningLibraryDAO = learningLibraryDAO;
		this.licenseDAO = licenseDAO;
		this.groupingDAO = groupingDAO;
		this.toolDAO = toolDAO;
		this.groupDAO = groupDAO;
		this.transitionDAO = transitionDAO;
	}
	public LearningDesign extractLearningDesignObject(LearningDesignDTO learningDesignDTO)throws Exception{
		LearningDesign learningDesign = LearningDesignDTO.extractLearningDesign(learningDesignDTO);
		
		if(learningDesignDTO.getUserID()!=null &&
				!learningDesignDTO.getUserID().equals(WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER)){
			User user = userDAO.getUserById(learningDesignDTO.getUserID());
			if(user!=null)
				learningDesign.setUser(user);
		}
		if(!learningDesignDTO.getLicenseID().equals(WDDXTAGS.NUMERIC_NULL_VALUE_LONG)){
			License license = licenseDAO.getLicenseByID(learningDesignDTO.getLicenseID());
			if(license!=null)
				learningDesign.setLicense(license);			
		}
		if(!learningDesignDTO.getWorkspaceFolderID().equals(WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER)){
			WorkspaceFolder workspaceFolder = workspaceFolderDAO.getWorkspaceFolderByID(learningDesignDTO.getWorkspaceFolderID());
			if(workspaceFolder!=null)
				learningDesign.setWorkspaceFolder(workspaceFolder);			
		}
		if(!learningDesignDTO.getParentLearningDesignID().equals(WDDXTAGS.NUMERIC_NULL_VALUE_LONG)){
			LearningDesign parent = learningDesignDAO.getLearningDesignById(learningDesignDTO.getParentLearningDesignID());
			if(parent!=null)
				learningDesign.setParentLearningDesign(parent);
		}
		learningDesignDAO.insert(learningDesign);
		parseActivities(learningDesignDTO,learningDesign);
		parseTransitions(learningDesignDTO,learningDesign);
		calculateFirstActivity(learningDesignDTO.getFirstActivityUIID(),learningDesign);				
		return learningDesign;	
	}
	private void parseActivities(LearningDesignDTO learningDesignDTO, LearningDesign learningDesign) throws WDDXProcessorConversionException{
		HashSet set = new HashSet();
		if(learningDesignDTO.getActivities()!=null){
			ArrayList table = learningDesignDTO.getActivities();
			Iterator iterator = table.iterator();
			while(iterator.hasNext()){
				Hashtable activityDetails = (Hashtable)iterator.next();
				AuthoringActivityDTO authoringActivityDTO = new AuthoringActivityDTO(activityDetails);
				Activity activity = extractActivityObject(authoringActivityDTO,learningDesign);	
				activityDAO.insert(activity);
				set.add(activity);
			}
		}
		learningDesign.setActivities(set);
		learningDesignDAO.update(learningDesign);
	}
	private void parseTransitions(LearningDesignDTO learningDesignDTO, LearningDesign learningDesign) throws WDDXProcessorConversionException{
		HashSet set = new HashSet();
		if(learningDesignDTO.getTransitions()!=null){
			ArrayList table = learningDesignDTO.getTransitions();
			Iterator iterator= table.iterator();
			while(iterator.hasNext()){
				Hashtable transitionDetails = (Hashtable)iterator.next();
				TransitionDTO transitionDTO = new TransitionDTO(transitionDetails);				
				Transition transition = extractTransitionObject(transitionDTO,learningDesign);
				transitionDAO.insert(transition);
				set.add(transition);
			}
		}
		learningDesign.setTransitions(set);
		learningDesignDAO.update(learningDesign);
	}
	/**TODO This function has to be tested with real data*/
	public void calculateFirstActivity(Integer firstID, LearningDesign design)throws Exception{		
		Activity flashFirstActivity = activityDAO.getActivityByUIID(firstID,design);
		design.setFirstActivity(flashFirstActivity);
		learningDesignDAO.update(design);
	}	
	public Activity extractActivityObject(AuthoringActivityDTO authoringActivityDTO, LearningDesign design){		
		Object activityObject = Activity.getActivityInstance(authoringActivityDTO.getActivityTypeID().intValue());
		processActivityType(activityObject,authoringActivityDTO);
		Activity activity =(Activity)activityObject;
		activity.setActivityUIID(authoringActivityDTO.getActivityUIID());
		activity.setDescription(authoringActivityDTO.getDescription());
		activity.setTitle(authoringActivityDTO.getTitle());
		activity.setHelpText(authoringActivityDTO.getHelpText());
		activity.setXcoord(authoringActivityDTO.getXcoord());
		activity.setYcoord(authoringActivityDTO.getYcoord());
		
		if(authoringActivityDTO.getParentUIID()!=null &&
				!authoringActivityDTO.getParentUIID().equals(WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER)){
			Activity parentActivity = activityDAO.getActivityByUIID(authoringActivityDTO.getParentUIID(),design);
			if(parentActivity!=null)
				activity.setParentActivity(parentActivity);
			activity.setParentUIID(authoringActivityDTO.getParentUIID());
		}
		
		activity.setActivityTypeId(authoringActivityDTO.getActivityTypeID());
		
		if(authoringActivityDTO.getGroupingID()!=null && 
				!authoringActivityDTO.getGroupingID().equals(WDDXTAGS.NUMERIC_NULL_VALUE_LONG)){
			Grouping grouping = groupingDAO.getGroupingById(authoringActivityDTO.getGroupingID());
			if(grouping!=null)
				activity.setGrouping(grouping);
			activity.setGroupingUIID(authoringActivityDTO.getGroupingUIID());
		}
		
		activity.setOrderId(authoringActivityDTO.getOrderID());
		activity.setDefineLater(authoringActivityDTO.getDefineLater());
		activity.setLearningDesign(design);
		
		if(authoringActivityDTO.getLearningLibraryID()!=null &&
				!authoringActivityDTO.getLearningLibraryID().equals(WDDXTAGS.NUMERIC_NULL_VALUE_LONG)){
			LearningLibrary library = learningLibraryDAO.getLearningLibraryById(authoringActivityDTO.getLearningLibraryID());
			if(library!=null)
				activity.setLearningLibrary(library);
		}
		
		activity.setCreateDateTime(authoringActivityDTO.getCreateDateTime());
		activity.setRunOffline(authoringActivityDTO.getRunOffline());
		activity.setActivityCategoryID(authoringActivityDTO.getActivityCategoryID());
		activity.setLibraryActivityUiImage(authoringActivityDTO.getLibraryActivityUiImage());
		
		if(authoringActivityDTO.getLibraryActivityID()!=null &&
				!authoringActivityDTO.getLibraryActivityID().equals(WDDXTAGS.NUMERIC_NULL_VALUE_LONG)){
			Activity libraryActivity = activityDAO.getActivityByActivityId(authoringActivityDTO.getLibraryActivityID());
			if(libraryActivity!=null)
				activity.setLibraryActivity(libraryActivity);
		}
		
		activity.setApplyGrouping(authoringActivityDTO.getApplyGrouping());
		activity.setGroupingSupportType(authoringActivityDTO.getGroupingSupportType());
		return activity;
	}	 
	private  void processActivityType(Object activity, AuthoringActivityDTO authoringActivityDTO){
		if(activity instanceof GroupingActivity)
			 buildGroupingActivity((GroupingActivity)activity,authoringActivityDTO);
		else if(activity instanceof ToolActivity)
			 buildToolActivity((ToolActivity)activity,authoringActivityDTO);
		else if(activity instanceof GateActivity)
			 buildGateActivity(activity,authoringActivityDTO);
		else 			
			 buildComplexActivity(activity,authoringActivityDTO);		
	}
	private void buildComplexActivity(Object activity,AuthoringActivityDTO authoringActivityDTO){		
		if(activity instanceof OptionsActivity)
			buildOptionsActivity((OptionsActivity)activity,authoringActivityDTO);
		else if (activity instanceof ParallelActivity)
			buildParallelActivity((ParallelActivity)activity,authoringActivityDTO);
		else
			buildSequenceActivity((SequenceActivity)activity,authoringActivityDTO);
		
	}
	private void buildGroupingActivity(GroupingActivity groupingActivity,AuthoringActivityDTO authoringActivityDTO){
		Integer groupingType = authoringActivityDTO.getGroupingType();
		Grouping grouping = extractGroupingObject(authoringActivityDTO.getGroupingDTO());		
		groupingActivity.setCreateGrouping(grouping);
		groupingActivity.setCreateGroupingUIID(grouping.getGroupingUIID());		
	}	
	private void buildOptionsActivity(OptionsActivity optionsActivity,AuthoringActivityDTO authoringActivityDTO){
		optionsActivity.setMaxNumberOfOptions(authoringActivityDTO.getMaxOptions());
		optionsActivity.setMinNumberOfOptions(authoringActivityDTO.getMinOptions());
		optionsActivity.setOptionsInstructions(authoringActivityDTO.getOptionsInstructions());		
	}
	private void buildParallelActivity(ParallelActivity activity,AuthoringActivityDTO authoringActivityDTO){		
	}
	private void buildSequenceActivity(SequenceActivity activity,AuthoringActivityDTO authoringActivityDTO){
		
	}
	private void buildToolActivity(ToolActivity toolActivity,AuthoringActivityDTO authoringActivityDTO){
		toolActivity.setToolContentId(authoringActivityDTO.getToolContentID());
		Tool tool =toolDAO.getToolByID(authoringActivityDTO.getToolID());
		toolActivity.setTool(tool);											 
	}
	private void buildGateActivity(Object activity,AuthoringActivityDTO authoringActivityDTO){
		if(activity instanceof SynchGateActivity)
			buildSynchGateActivity((SynchGateActivity)activity,authoringActivityDTO);
		else if (activity instanceof PermissionGateActivity)
			buildPermissionGateActivity((PermissionGateActivity)activity,authoringActivityDTO);
		else
			buildScheduleGateActivity((ScheduleGateActivity)activity,authoringActivityDTO);
		GateActivity gateActivity = (GateActivity)activity ;
		gateActivity.setGateActivityLevelId(authoringActivityDTO.getGateActivityLevelID());
		gateActivity.setGateOpen(authoringActivityDTO.getGateOpen());
				
	}
	private void buildSynchGateActivity(SynchGateActivity activity,AuthoringActivityDTO authoringActivityDTO){	
	}
	private void buildPermissionGateActivity(PermissionGateActivity activity,AuthoringActivityDTO authoringActivityDTO){		
	}
	private static void buildScheduleGateActivity(ScheduleGateActivity activity,AuthoringActivityDTO authoringActivityDTO){
		activity.setGateStartDateTime(authoringActivityDTO.getGateStartDateTime());
		activity.setGateEndDateTime(authoringActivityDTO.getGateEndDateTime());
		activity.setGateStartTimeOffset(authoringActivityDTO.getGateStartTimeOffset());
		activity.setGateEndTimeOffset(authoringActivityDTO.getGateEndTimeOffset());		
	}
	public Grouping extractGroupingObject(GroupingDTO groupingDTO){		
		Object object = Grouping.getGroupingInstance(groupingDTO.getGroupingType());
		
		if(object instanceof RandomGrouping)
			createRandomGrouping((RandomGrouping)object,groupingDTO);
		else if(object instanceof ChosenGrouping)
			createChosenGrouping((ChosenGrouping)object,groupingDTO);
		else
			createLessonClass((LessonClass)object, groupingDTO);
		
		Grouping grouping = (Grouping)object;
		grouping.setGroupingUIID(groupingDTO.getGroupingUIID());
		grouping.setMaxNumberOfGroups(groupingDTO.getMaxNumberOfGroups());
		groupingDAO.insert(grouping);
		return grouping;
	}
	private void createRandomGrouping(RandomGrouping randomGrouping,GroupingDTO groupingDTO){
		randomGrouping.setLearnersPerGroup(groupingDTO.getLearnersPerGroup());
		randomGrouping.setNumberOfGroups(groupingDTO.getNumberOfGroups());
	}
	private void createChosenGrouping(ChosenGrouping chosenGrouping,GroupingDTO groupingDTO){
		
	}
	private void createLessonClass(LessonClass lessonClass, GroupingDTO groupingDTO){
		Group group = groupDAO.getGroupById(groupingDTO.getStaffGroupID());
		if(group!=null)
			lessonClass.setStaffGroup(group);
	}
	private Transition extractTransitionObject(TransitionDTO transitionDTO,LearningDesign learningDesign){
		Transition transition = new Transition();
		transition.setTransitionUIID(transitionDTO.getTransitionUIID());
		if(transitionDTO.getToUIID()!=null){
			Activity toActivity = activityDAO.getActivityByUIID(transitionDTO.getToUIID(), learningDesign);
			transition.setToActivity(toActivity);
			transition.setToUIID(transitionDTO.getToUIID());
		}
		if(transitionDTO.getFromUIID()!=null){
			Activity fromActivity = activityDAO.getActivityByUIID(transitionDTO.getFromUIID(), learningDesign);
			transition.setFromActivity(fromActivity);
			transition.setFromUIID(transitionDTO.getFromUIID());
		}		
		transition.setDescription(transitionDTO.getDescription());
		transition.setTitle(transitionDTO.getTitle());
		transition.setCreateDateTime(transitionDTO.getCreateDateTime());			
		transition.setLearningDesign(learningDesign);		
		return transition;
	}		
}
	

