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

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

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
import org.lamsfoundation.lams.lesson.LessonClass;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.dao.hibernate.ToolDAO;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;
import org.lamsfoundation.lams.usermanagement.dao.hibernate.UserDAO;
import org.lamsfoundation.lams.usermanagement.dao.hibernate.WorkspaceFolderDAO;
import org.lamsfoundation.lams.util.wddx.WDDXProcessor;
import org.lamsfoundation.lams.util.wddx.WDDXProcessorConversionException;

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
	public LearningDesign extractLearningDesign(Hashtable table) throws WDDXProcessorConversionException, ObjectExtractorException {

		LearningDesign learningDesign = null;
	
		Long learningDesignId = WDDXProcessor.convertToLong(table, "learningDesignID");
		//if the learningDesignID is not null, load the existing LearningDesign object from the database, otherwise create a new one.
		learningDesign = learningDesignId!= null ? learningDesignDAO.getLearningDesignById(learningDesignId) : new LearningDesign();
		
		//get the core learning design stuff
		//LearningDesign learningDesign = new LearningDesign();
		
		learningDesign.setLearningDesignUIID(WDDXProcessor.convertToInteger(table,"learningDesignUIID"));
		learningDesign.setDescription(WDDXProcessor.convertToString(table,"description"));
		learningDesign.setTitle(WDDXProcessor.convertToString(table,"title"));
		learningDesign.setMaxID(WDDXProcessor.convertToInteger(table,"maxID"));
		learningDesign.setValidDesign(WDDXProcessor.convertToBoolean(table,"validDesign"));
		learningDesign.setReadOnly(WDDXProcessor.convertToBoolean(table,"readOnly"));
		learningDesign.setDateReadOnly(WDDXProcessor.convertToDate(table,"dateReadOnly"));
		learningDesign.setOfflineInstructions(WDDXProcessor.convertToString(table,"offlineInstructions"));
		learningDesign.setOnlineInstructions(WDDXProcessor.convertToString(table,"onlineInstructions"));
		learningDesign.setHelpText(WDDXProcessor.convertToString(table,"helpText"));
		learningDesign.setCopyTypeID(WDDXProcessor.convertToInteger(table,"copyTypeID"));
		learningDesign.setCreateDateTime(WDDXProcessor.convertToDate(table,"createDateTime"));
		learningDesign.setVersion(WDDXProcessor.convertToString(table,"version"));
		learningDesign.setDuration(WDDXProcessor.convertToLong(table,"duration"));
		learningDesign.setLastModifiedDateTime(WDDXProcessor.convertToDate(table,"lastModifiedDateTime"));
		
		// not required at authoring time.
		learningDesign.setLessonOrgName(WDDXProcessor.convertToString(table,"lessonOrgName"));
		learningDesign.setLessonName(WDDXProcessor.convertToString(table,"lessonName"));					
		learningDesign.setLessonStartDateTime(WDDXProcessor.convertToDate(table,"lessonStartDateTime"));
		learningDesign.setLessonID(WDDXProcessor.convertToLong(table,"lessionID"));
		learningDesign.setLessonOrgID(WDDXProcessor.convertToLong(table,"lessionOrgID"));
		learningDesign.setDuration(WDDXProcessor.convertToLong(table,"duration"));
		
		Integer userId = WDDXProcessor.convertToInteger(table,"userID");
		if( userId != null ) {
			User user = userDAO.getUserById(userId);
			if(user!=null) {
				learningDesign.setUser(user);
			} else {
				throw new ObjectExtractorException("userID missing");
			}
		}
		
		Long licenseID = WDDXProcessor.convertToLong(table,"licenseID");
		if( licenseID!=null ){
			License license = licenseDAO.getLicenseByID(licenseID);
			learningDesign.setLicense(license);			
		} else {
			learningDesign.setLicense(null);
		}
		learningDesign.setLicenseText(WDDXProcessor.convertToString(table,"licenseText"));				

		Integer workspaceFolderID = WDDXProcessor.convertToInteger(table,"workspaceFolderID");
		if( workspaceFolderID!=null ){
			WorkspaceFolder workspaceFolder = workspaceFolderDAO.getWorkspaceFolderByID(workspaceFolderID);
			learningDesign.setWorkspaceFolder(workspaceFolder);			
		}

		Long parentLearningDesignID = WDDXProcessor.convertToLong(table,"parentLearningDesignID");
		if( parentLearningDesignID != null ){
			LearningDesign parent = learningDesignDAO.getLearningDesignById(parentLearningDesignID);
			learningDesign.setParentLearningDesign(parent);
		}
		
		learningDesignDAO.insert(learningDesign);

		// now process the "parts" of the learning design
		parseActivities((Vector)table.get("activities"),learningDesign);
		parseTransitions((Vector)table.get("transitions"),learningDesign);
		calculateFirstActivity(WDDXProcessor.convertToInteger(table,"firstActivityUIID"),learningDesign);				

		return learningDesign;	
		}
	
	private void parseActivities(List activitiesList, LearningDesign learningDesign) 
			throws WDDXProcessorConversionException, ObjectExtractorException {
		HashSet set = new HashSet();
		if(activitiesList!=null){
			Iterator iterator = activitiesList.iterator();
			while(iterator.hasNext()){
				Hashtable activityDetails = (Hashtable)iterator.next();
				Activity activity = extractActivityObject(activityDetails,learningDesign);	
				activityDAO.insert(activity);
				set.add(activity);
			}
		}
		// TODO: Need to double check that the toolID/toolContentID combinations match entries in lams_tool_content table, or put FK on table.
		learningDesign.setActivities(set);
		learningDesignDAO.update(learningDesign);
	}
	private void parseTransitions(List transitionsList, LearningDesign learningDesign) throws WDDXProcessorConversionException{
		HashSet set = new HashSet();
		if(transitionsList!=null){
			Iterator iterator= transitionsList.iterator();
			while(iterator.hasNext()){
				Hashtable transitionDetails = (Hashtable)iterator.next();
				Transition transition = extractTransitionObject(transitionDetails,learningDesign);
				transitionDAO.insert(transition);
				set.add(transition);
			}
		}
		learningDesign.setTransitions(set);
		learningDesignDAO.update(learningDesign);
	}
	/**TODO This function has to be tested with real data*/
	public void calculateFirstActivity(Integer firstID, LearningDesign design){		
		Activity flashFirstActivity = activityDAO.getActivityByUIID(firstID,design);
		design.setFirstActivity(flashFirstActivity);
		learningDesignDAO.update(design);
	}	
	public Activity extractActivityObject(Hashtable activityDetails, LearningDesign design) throws WDDXProcessorConversionException, ObjectExtractorException {
		
		Integer activityTypeID = WDDXProcessor.convertToInteger(activityDetails, "activityTypeID");
		if ( activityTypeID == null ) {
			throw new ObjectExtractorException("activityTypeID missing");
		}
		
		Object activityObject = Activity.getActivityInstance(activityTypeID.intValue());
		processActivityType(activityObject,activityDetails);
		Activity activity =(Activity)activityObject;
		activity.setActivityTypeId(activityTypeID);
		activity.setActivityUIID(WDDXProcessor.convertToInteger(activityDetails,"activityUIID"));
		activity.setDescription(WDDXProcessor.convertToString(activityDetails,"description"));
		activity.setTitle(WDDXProcessor.convertToString(activityDetails,"title"));
		activity.setHelpText(WDDXProcessor.convertToString(activityDetails,"helpText"));
		activity.setXcoord(WDDXProcessor.convertToInteger(activityDetails, "xcoord"));
		activity.setYcoord(WDDXProcessor.convertToInteger(activityDetails, "ycoord"));

		Integer parentUIID = WDDXProcessor.convertToInteger(activityDetails, "parentUIID");
		if( parentUIID!=null ) {
			Activity parentActivity = activityDAO.getActivityByUIID(parentUIID,design);
			activity.setParentActivity(parentActivity);
			activity.setParentUIID(parentUIID);
		} else {
			activity.setParentActivity(null);
			activity.setParentUIID(null);
		}
		
		Long groupingID = WDDXProcessor.convertToLong(activityDetails,"groupingID");
		Integer groupingUIID = WDDXProcessor.convertToInteger(activityDetails,"groupingUIID");
		if( groupingID!=null ){
			Grouping grouping = groupingDAO.getGroupingById(groupingID);
			activity.setGrouping(grouping);
			activity.setGroupingUIID(groupingUIID);
		} else {
			activity.setGrouping(null);
			activity.setGroupingUIID(null);
		}
		
		activity.setOrderId(WDDXProcessor.convertToInteger(activityDetails,"orderID"));
		activity.setDefineLater(WDDXProcessor.convertToBoolean(activityDetails,"defineLater"));
		activity.setLearningDesign(design);
		
		Long learningLibraryID = WDDXProcessor.convertToLong(activityDetails,"learningLibraryID");
		if( learningLibraryID!=null ){
			LearningLibrary library = learningLibraryDAO.getLearningLibraryById(learningLibraryID);
			activity.setLearningLibrary(library);
		} else {
			activity.setLearningLibrary(null);
		}
		
		activity.setCreateDateTime(WDDXProcessor.convertToDate(activityDetails,"createDateTime"));
		activity.setRunOffline(WDDXProcessor.convertToBoolean(activityDetails,"runOffline"));
		activity.setActivityCategoryID(WDDXProcessor.convertToInteger(activityDetails,"activityCategoryID"));
		activity.setLibraryActivityUiImage(WDDXProcessor.convertToString(activityDetails,"libraryActivityUiImage"));
		
		Long libraryActivityID = WDDXProcessor.convertToLong(activityDetails,"libraryActivityID");
		if( libraryActivityID != null ){
			Activity libraryActivity = activityDAO.getActivityByActivityId(libraryActivityID);
			activity.setLibraryActivity(libraryActivity);
		} else {
			activity.setLibraryActivity(null);
		}
		
		activity.setApplyGrouping(WDDXProcessor.convertToBoolean(activityDetails,"applyGrouping"));
		activity.setGroupingSupportType(WDDXProcessor.convertToInteger(activityDetails,"groupingSupportType"));
		return activity;
	}	 
	private  void processActivityType(Object activity, Hashtable activityDetails) 
			throws WDDXProcessorConversionException, ObjectExtractorException {
		if(activity instanceof GroupingActivity)
			 buildGroupingActivity((GroupingActivity)activity,activityDetails);
		else if(activity instanceof ToolActivity)
			 buildToolActivity((ToolActivity)activity,activityDetails);
		else if(activity instanceof GateActivity)
			 buildGateActivity(activity,activityDetails);
		else 			
			 buildComplexActivity(activity,activityDetails);		
	}
	private void buildComplexActivity(Object activity,Hashtable activityDetails) throws WDDXProcessorConversionException{		
		if(activity instanceof OptionsActivity)
			buildOptionsActivity((OptionsActivity)activity,activityDetails);
		else if (activity instanceof ParallelActivity)
			buildParallelActivity((ParallelActivity)activity,activityDetails);
		else
			buildSequenceActivity((SequenceActivity)activity,activityDetails);
		
	}
	private void buildGroupingActivity(GroupingActivity groupingActivity,Hashtable activityDetails) 
		throws WDDXProcessorConversionException, ObjectExtractorException {
		Hashtable groupingDetails = (Hashtable) activityDetails.get("groupingDTO"); 
		if( groupingDetails != null ){
			Grouping grouping = extractGroupingObject(groupingDetails);		
			groupingActivity.setCreateGrouping(grouping);
			groupingActivity.setCreateGroupingUIID(grouping.getGroupingUIID());
		} else {
			groupingActivity.setCreateGrouping(null);
			groupingActivity.setCreateGroupingUIID(null);
		}
	}	

	private void buildOptionsActivity(OptionsActivity optionsActivity,Hashtable activityDetails) throws WDDXProcessorConversionException{
		optionsActivity.setMaxNumberOfOptions(WDDXProcessor.convertToInteger(activityDetails,"maxOptions"));
		optionsActivity.setMinNumberOfOptions(WDDXProcessor.convertToInteger(activityDetails,"minOptions"));
		optionsActivity.setOptionsInstructions(WDDXProcessor.convertToString(activityDetails,"optionsInstructions"));		
	}
	private void buildParallelActivity(ParallelActivity activity,Hashtable activityDetails) throws WDDXProcessorConversionException{		
	}
	private void buildSequenceActivity(SequenceActivity activity,Hashtable activityDetails) throws WDDXProcessorConversionException{
		
	}
	private void buildToolActivity(ToolActivity toolActivity,Hashtable activityDetails) throws WDDXProcessorConversionException{
		toolActivity.setToolContentId(WDDXProcessor.convertToLong(activityDetails,"toolContentID"));
		Tool tool =toolDAO.getToolByID(WDDXProcessor.convertToLong(activityDetails,"toolID"));
		toolActivity.setTool(tool);											 
	}
	private void buildGateActivity(Object activity,Hashtable activityDetails) throws WDDXProcessorConversionException{
		if(activity instanceof SynchGateActivity)
			buildSynchGateActivity((SynchGateActivity)activity,activityDetails);
		else if (activity instanceof PermissionGateActivity)
			buildPermissionGateActivity((PermissionGateActivity)activity,activityDetails);
		else
			buildScheduleGateActivity((ScheduleGateActivity)activity,activityDetails);
		GateActivity gateActivity = (GateActivity)activity ;
		gateActivity.setGateActivityLevelId(WDDXProcessor.convertToInteger(activityDetails,"gateActivityLevelID"));
		gateActivity.setGateOpen(WDDXProcessor.convertToBoolean(activityDetails,"gateOpen"));
				
	}
	private void buildSynchGateActivity(SynchGateActivity activity,Hashtable activityDetails) throws WDDXProcessorConversionException{	
	}
	private void buildPermissionGateActivity(PermissionGateActivity activity,Hashtable activityDetails) throws WDDXProcessorConversionException{		
	}
	private static void buildScheduleGateActivity(ScheduleGateActivity activity,Hashtable activityDetails) throws WDDXProcessorConversionException{
		activity.setGateStartDateTime(WDDXProcessor.convertToDate(activityDetails,"gateStartDateTime"));
		activity.setGateEndDateTime(WDDXProcessor.convertToDate(activityDetails,"gateEndDateTime"));
		activity.setGateStartTimeOffset(WDDXProcessor.convertToLong(activityDetails,"gateStartTimeOffset"));
		activity.setGateEndTimeOffset(WDDXProcessor.convertToLong(activityDetails,"gateEndTimeOffset"));		
	}
	

	public Grouping extractGroupingObject(Hashtable groupingDetails) throws WDDXProcessorConversionException, ObjectExtractorException{
		Integer groupingType=WDDXProcessor.convertToInteger(groupingDetails,"groupingType");
		if ( groupingType == null ) { 
			throw new ObjectExtractorException("groupingType missing");
		}
		
		Object object = Grouping.getGroupingInstance(groupingType);
		
		if(object instanceof RandomGrouping)
			createRandomGrouping((RandomGrouping)object,groupingDetails);
		else if(object instanceof ChosenGrouping)
			createChosenGrouping((ChosenGrouping)object,groupingDetails);
		else
			createLessonClass((LessonClass)object, groupingDetails);
		
		Grouping grouping = (Grouping)object;
		grouping.setGroupingId(WDDXProcessor.convertToLong(groupingDetails,"groupingID"));
		grouping.setGroupingUIID(WDDXProcessor.convertToInteger(groupingDetails,"groupingUIID"));
		grouping.setMaxNumberOfGroups(WDDXProcessor.convertToInteger(groupingDetails,"maxNumberOfGroups"));
		groupingDAO.insert(grouping);
		return grouping;
	}
	private void createRandomGrouping(RandomGrouping randomGrouping,Hashtable groupingDetails) throws WDDXProcessorConversionException{
		randomGrouping.setLearnersPerGroup(WDDXProcessor.convertToInteger(groupingDetails,"learnersPerGroup"));
		randomGrouping.setNumberOfGroups(WDDXProcessor.convertToInteger(groupingDetails,"numberOfGroups"));
	}
	private void createChosenGrouping(ChosenGrouping chosenGrouping,Hashtable groupingDetails) throws WDDXProcessorConversionException{
		
	}
	private void createLessonClass(LessonClass lessonClass, Hashtable groupingDetails) throws WDDXProcessorConversionException{
		Group group = groupDAO.getGroupById(WDDXProcessor.convertToLong(groupingDetails,"staffGroupID"));
		if(group!=null)
			lessonClass.setStaffGroup(group);
	}

	/** Create the transition from a WDDX based hashtable. It is easier to go 
	 * straight to the data object rather than going via the DTO, as the DTO
	 * returns the special null values from the getter methods. This makes it
	 * hard to set up the transaction object from the transitionDTO.
	 * 
	 * @param transitionDetails
	 * @throws WDDXProcessorConversionException
	 */
	private Transition extractTransitionObject(Hashtable transitionDetails, LearningDesign learningDesign) throws WDDXProcessorConversionException{
		
		Transition transition = new Transition();
		
		Long transitionID= WDDXProcessor.convertToLong(transitionDetails,"transitionID");
		if ( transitionID != null )
			transition.setTransitionId(transitionID);
			
		transition.setTransitionUIID(WDDXProcessor.convertToInteger(transitionDetails,"transitionUIID"));
		
		Integer toUIID=WDDXProcessor.convertToInteger(transitionDetails,"toUIID"); 
		if(toUIID!=null){
			Activity toActivity = activityDAO.getActivityByUIID(toUIID, learningDesign);
			transition.setToActivity(toActivity);
			transition.setToUIID(toUIID);
		}

		Integer fromUIID=WDDXProcessor.convertToInteger(transitionDetails,"fromUIID");
		if(fromUIID!=null){
			Activity fromActivity = activityDAO.getActivityByUIID(fromUIID, learningDesign);
			transition.setFromActivity(fromActivity);
			transition.setFromUIID(fromUIID);
		}		
		transition.setDescription(WDDXProcessor.convertToString(transitionDetails,"description"));
		transition.setTitle(WDDXProcessor.convertToString(transitionDetails,"title"));
		transition.setCreateDateTime(WDDXProcessor.convertToDate(transitionDetails,"createDateTime"));			
		transition.setLearningDesign(learningDesign);		
		return transition;
	}		
	

}
	

