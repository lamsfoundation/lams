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
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;
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
import org.lamsfoundation.lams.util.wddx.WDDXTAGS;

/**
 * @author Manpreet Minhas
 * modified by Mailing Truong
 * 
 * This is a utitlity class for extracting the
 * information from the WDDX packet sent by the FLASH.
 * 
 * The following rules are applied:
 * The client sends a subset of all possible data. 
 * If a field is included, then the value associated with this field should
 * be persisted (the value maybe a new value or an unchanged value)
 * If a field is not included then the server should assume that the value
 * is unchanged.
 * If the value of a field is one of the special null values, then null
 * should be persisted.
 * 
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
	
	protected Logger log = Logger.getLogger(ObjectExtractor.class);	

	
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
		
		if (keyExists(table, WDDXTAGS.LEARNING_DESIGN_UIID)) 
		    learningDesign.setLearningDesignUIID(WDDXProcessor.convertToInteger(table,WDDXTAGS.LEARNING_DESIGN_UIID)); 
		if (keyExists(table, WDDXTAGS.DESCRIPTION)) 
		    learningDesign.setDescription(WDDXProcessor.convertToString(table,WDDXTAGS.DESCRIPTION));
		if (keyExists(table, WDDXTAGS.TITLE))	
		    learningDesign.setTitle(WDDXProcessor.convertToString(table, WDDXTAGS.TITLE));
		if (keyExists(table, WDDXTAGS.MAX_ID))	
		    learningDesign.setMaxID(WDDXProcessor.convertToInteger(table,WDDXTAGS.MAX_ID));
		if (keyExists(table, WDDXTAGS.VALID_DESIGN))  
		    learningDesign.setValidDesign(WDDXProcessor.convertToBoolean(table,WDDXTAGS.VALID_DESIGN));
		if (keyExists(table, WDDXTAGS.READ_ONLY))	
		    learningDesign.setReadOnly(WDDXProcessor.convertToBoolean(table,WDDXTAGS.READ_ONLY));
		if (keyExists(table, WDDXTAGS.DATE_READ_ONLY)) 	
		    learningDesign.setDateReadOnly(WDDXProcessor.convertToDate(table, WDDXTAGS.DATE_READ_ONLY));
		if (keyExists(table, WDDXTAGS.OFFLINE_INSTRUCTIONS))	
		    learningDesign.setOfflineInstructions(WDDXProcessor.convertToString(table,WDDXTAGS.OFFLINE_INSTRUCTIONS));
		if (keyExists(table, WDDXTAGS.ONLINE_INSTRUCTIONS))	
		    learningDesign.setOnlineInstructions(WDDXProcessor.convertToString(table,WDDXTAGS.ONLINE_INSTRUCTIONS));
		if (keyExists(table, WDDXTAGS.HELP_TEXT))	
		    learningDesign.setHelpText(WDDXProcessor.convertToString(table,WDDXTAGS.HELP_TEXT));
		if (keyExists(table, WDDXTAGS.COPY_TYPE))	
		    learningDesign.setCopyTypeID(WDDXProcessor.convertToInteger(table,WDDXTAGS.COPY_TYPE));
		if (keyExists(table, WDDXTAGS.CREATION_DATE))	
		    learningDesign.setCreateDateTime(WDDXProcessor.convertToDate(table,WDDXTAGS.CREATION_DATE));
		if (keyExists(table, WDDXTAGS.VERSION))	
		    learningDesign.setVersion(WDDXProcessor.convertToString(table,WDDXTAGS.VERSION));
		if (keyExists(table, WDDXTAGS.DURATION))	
		    learningDesign.setDuration(WDDXProcessor.convertToLong(table,WDDXTAGS.DURATION));
		if (keyExists(table, WDDXTAGS.LAST_MODIFIED_DATE))	
		    learningDesign.setLastModifiedDateTime(WDDXProcessor.convertToDate(table,WDDXTAGS.LAST_MODIFIED_DATE));
		
		// not required at authoring time.
		if (keyExists(table, WDDXTAGS.LESSON_ORG_NAME))
		    learningDesign.setLessonOrgName(WDDXProcessor.convertToString(table,WDDXTAGS.LESSON_ORG_NAME));
		if (keyExists(table, WDDXTAGS.LESSON_NAME))
		    learningDesign.setLessonName(WDDXProcessor.convertToString(table,WDDXTAGS.LESSON_NAME));					
		if (keyExists(table, WDDXTAGS.LESSON_START_DATETIME))
		    learningDesign.setLessonStartDateTime(WDDXProcessor.convertToDate(table,WDDXTAGS.LESSON_START_DATETIME));
		if (keyExists(table, WDDXTAGS.LESSON_ID))
		    learningDesign.setLessonID(WDDXProcessor.convertToLong(table,WDDXTAGS.LESSON_ID));
		if (keyExists(table, WDDXTAGS.LESSON_ORG_ID))
		    learningDesign.setLessonOrgID(WDDXProcessor.convertToLong(table,WDDXTAGS.LESSON_ORG_ID));
		if (keyExists(table, WDDXTAGS.DURATION))
		    learningDesign.setDuration(WDDXProcessor.convertToLong(table,WDDXTAGS.DURATION));
		
		if (keyExists(table, WDDXTAGS.USER_ID))
		{
		    Integer userId = WDDXProcessor.convertToInteger(table,WDDXTAGS.USER_ID);
		    if( userId != null ) {
				User user = userDAO.getUserById(userId);
				if(user!=null) {
					learningDesign.setUser(user);
				} else {
					throw new ObjectExtractorException("userID missing");
				}
			}
		    //else dont do anything.
		}
	
		if (keyExists(table, WDDXTAGS.LICENCE_ID))
		{		
			Long licenseID = WDDXProcessor.convertToLong(table,WDDXTAGS.LICENCE_ID);
			if( licenseID!=null ){
				License license = licenseDAO.getLicenseByID(licenseID);
				learningDesign.setLicense(license);			
			} else {
				learningDesign.setLicense(null); //special null value set
			}
		}	
		if (keyExists(table, WDDXTAGS.LICENSE_TEXT))
		    learningDesign.setLicenseText(WDDXProcessor.convertToString(table,WDDXTAGS.LICENSE_TEXT));				

		if (keyExists(table, WDDXTAGS.WORKSPACE_FOLDER_ID))
		{
			Integer workspaceFolderID = WDDXProcessor.convertToInteger(table, WDDXTAGS.WORKSPACE_FOLDER_ID);
			if( workspaceFolderID!=null ){
				WorkspaceFolder workspaceFolder = workspaceFolderDAO.getWorkspaceFolderByID(workspaceFolderID);
				learningDesign.setWorkspaceFolder(workspaceFolder);			
			}
			else
			{
			    learningDesign.setWorkspaceFolder(null);
			}
		}

		if (keyExists(table, WDDXTAGS.PARENT_DESIGN_ID))
		{
			Long parentLearningDesignID = WDDXProcessor.convertToLong(table,WDDXTAGS.PARENT_DESIGN_ID);
			if( parentLearningDesignID != null ){
				LearningDesign parent = learningDesignDAO.getLearningDesignById(parentLearningDesignID);
				learningDesign.setParentLearningDesign(parent);
			}
			else
			    learningDesign.setParentLearningDesign(null);
		}
	
		
		learningDesignDAO.insert(learningDesign);
	
		// now process the "parts" of the learning design
		//Vector v = (Vector)table.get(WDDXTAGS.GROUPINGS);
		parseGroupings((Vector)table.get(WDDXTAGS.GROUPINGS), learningDesign);
		parseActivities((Vector)table.get(WDDXTAGS.ACTIVITIES),learningDesign);
		parseTransitions((Vector)table.get(WDDXTAGS.TRANSITIONS),learningDesign);

		learningDesign.setFirstActivity(learningDesign.calculateFirstActivity());
		learningDesignDAO.update(learningDesign);
		
		return learningDesign;	
		}
	/**
	 * Parses the groupings array sent from the WDDX packet. It will create
	 * the groupings object (ChosenGrouping, RandomGrouping) so that when the
	 * GroupingActivity is processed, it can link to the grouping object
	 * that has been created by this method.
	 * @param groupingsList
	 * @param learningDesign
	 * @throws WDDXProcessorConversionException
	 * @throws ObjectExtractorException
	 */
	
	private void parseGroupings(List groupingsList, LearningDesign learningDesign) 
		throws WDDXProcessorConversionException, ObjectExtractorException 	
	{
	   //iterate through the list of groupings objects
	    //each object should contain information groupingUUID, groupingID, groupingTypeID
	    if (groupingsList != null)
	    {
	        Iterator iterator = groupingsList.iterator();
	        while(iterator.hasNext())
	        {
	            Hashtable groupingDetails = (Hashtable)iterator.next();
	            if( groupingDetails != null )
	            {
	    			Grouping grouping = extractGroupingObject(groupingDetails);	
	    			groupingDAO.insert(grouping);	    			
	            }
	        }
	        
	    }
	    
	    
	
	}
	
	
	
	/**
	 * Parses the list of activities sent from the WDDX packet. The current activities that 
	 * belong to this learning design will be compared with the new list of activities. Any new activities will
	 * be added to the database, existing activities will be updated, and any activities that are not
	 * present in the list of activities from the wddx packet (but appear in the list of current activities) 
	 * are deleted.
	 * 
	 * @param activitiesList The list of activities from the WDDX packet.
	 * @param learningDesign
	 * @throws WDDXProcessorConversionException
	 * @throws ObjectExtractorException
	 */
	private void parseActivities(List activitiesList, LearningDesign learningDesign) 
			throws WDDXProcessorConversionException, ObjectExtractorException {
		HashSet updatedSet = new HashSet();
		Set currentActivities = learningDesign.getActivities();
		if(activitiesList!=null){
			Iterator iterator = activitiesList.iterator();
			while(iterator.hasNext()){
				Hashtable activityDetails = (Hashtable)iterator.next();
				Activity activity = extractActivityObject(activityDetails,learningDesign);	
				activityDAO.insert(activity);
				currentActivities.add(activity);
				updatedSet.add(activity); //used to keep track of which ids that are still in use
			}
		}
		
		
		/* Iterate through currentActivities, any activities that arent present in the updatedSet will be deleted */ 
		Iterator itr = currentActivities.iterator();
		
		while (itr.hasNext())
		{
		    Activity activity = (Activity)itr.next();
		    if (!updatedSet.contains(activity))
		    { 
			      itr.remove(); //removes object from collection;
			      deleteActivity(activity, learningDesign);
		    }
		}
		//TODO: Need to double check that the toolID/toolContentID combinations match entries in lams_tool_content table, or put FK on table.
		learningDesign.setActivities(currentActivities);
		learningDesignDAO.update(learningDesign);
	}
	
	/**
	 * Like parseActivities, parseTransitions parses the list of transitions from the wddx packet. 
	 * New transitions will be added, existing transitions updated and any transitions that are no
	 * longer needed are deleted.
	 * 
	 * @param transitionsList The list of transitions from the wddx packet
	 * @param learningDesign
	 * @throws WDDXProcessorConversionException
	 */
	private void parseTransitions(List transitionsList, LearningDesign learningDesign) throws WDDXProcessorConversionException{
	    
	    HashSet updatedSet = new HashSet();
		Set currentTransitions = learningDesign.getTransitions();
		if (currentTransitions == null)
		    currentTransitions = new HashSet();
		
		if(transitionsList!=null){
			Iterator iterator= transitionsList.iterator();
			while(iterator.hasNext()){
				Hashtable transitionDetails = (Hashtable)iterator.next();
				Transition transition = extractTransitionObject(transitionDetails,learningDesign);
				transitionDAO.insert(transition);
				currentTransitions.add(transition);
				updatedSet.add(transition);
			}
		}
		
		Iterator itr = currentTransitions.iterator();
		
		while (itr.hasNext())
		{
		    Transition transition = (Transition)itr.next();
		    if (!updatedSet.contains(transition))
		    {
		       /* This will leave orphan content in the tool tables. It can be removed by the tool content cleaning job, 
		        which may be run from the admin screen or via a cron job. However, the transitionDAO.delete() method is 
		        called to actually delete the object */
		        
		        itr.remove();        
		        transitionDAO.delete(transition);
		     
		    }
		}
		
		learningDesign.setTransitions(currentTransitions);
		learningDesignDAO.update(learningDesign);
	    
	   
	}
	public Activity extractActivityObject(Hashtable activityDetails, LearningDesign design) throws WDDXProcessorConversionException, ObjectExtractorException {
		
	    //it is assumed that the activityUUID will always be sent by flash.
	    Integer activityUUID = WDDXProcessor.convertToInteger(activityDetails,WDDXTAGS.ACTIVITY_UIID);
	    Activity activity = null;
	    //get the activity with the particular activity uuid, if null, then new object needs to be created.
	    Activity existingActivity = activityDAO.getActivityByUIID(activityUUID, design);
	    if (existingActivity == null)
	    {
			Integer activityTypeID = WDDXProcessor.convertToInteger(activityDetails, WDDXTAGS.ACTIVITY_TYPE_ID);
			if ( activityTypeID == null ) {
				throw new ObjectExtractorException("activityTypeID missing");
			}
			
			Activity activityObject = Activity.getActivityInstance(activityTypeID.intValue());
			activity =(Activity)activityObject;
	    }
	    else
	    {
	        activity = existingActivity; //otherwise load existing activity
	    }
		processActivityType(activity,activityDetails);
		
		
	    if (keyExists(activityDetails, WDDXTAGS.ACTIVITY_TYPE_ID))
	        activity.setActivityTypeId(WDDXProcessor.convertToInteger(activityDetails, WDDXTAGS.ACTIVITY_TYPE_ID));
	    if (keyExists(activityDetails, WDDXTAGS.ACTIVITY_UIID))
	        activity.setActivityUIID(WDDXProcessor.convertToInteger(activityDetails,WDDXTAGS.ACTIVITY_UIID));
	    if (keyExists(activityDetails, WDDXTAGS.DESCRIPTION))
	        activity.setDescription(WDDXProcessor.convertToString(activityDetails,WDDXTAGS.DESCRIPTION));
	    if (keyExists(activityDetails, WDDXTAGS.TITLE))
	        activity.setTitle(WDDXProcessor.convertToString(activityDetails,WDDXTAGS.TITLE));
	    if (keyExists(activityDetails, WDDXTAGS.HELP_TEXT))
	        activity.setHelpText(WDDXProcessor.convertToString(activityDetails,WDDXTAGS.HELP_TEXT));
	    if (keyExists(activityDetails, WDDXTAGS.XCOORD))
	        activity.setXcoord(WDDXProcessor.convertToInteger(activityDetails, WDDXTAGS.XCOORD));
	    if (keyExists(activityDetails, WDDXTAGS.YCOORD))
	        activity.setYcoord(WDDXProcessor.convertToInteger(activityDetails, WDDXTAGS.YCOORD));

	    if (keyExists(activityDetails, WDDXTAGS.PARENT_UIID))
	    {
			Integer parentUIID = WDDXProcessor.convertToInteger(activityDetails, WDDXTAGS.PARENT_UIID);
			if( parentUIID!=null ) {
				Activity parentActivity = activityDAO.getActivityByUIID(parentUIID,design);
				activity.setParentActivity(parentActivity);
				activity.setParentUIID(parentUIID);
			} else {
				activity.setParentActivity(null);
				activity.setParentUIID(null);
			}
	    }
		
	    if (keyExists(activityDetails, WDDXTAGS.GROUPING_ID))
	    {
			Long groupingID = WDDXProcessor.convertToLong(activityDetails,WDDXTAGS.GROUPING_ID);
			Integer groupingUIID = WDDXProcessor.convertToInteger(activityDetails,WDDXTAGS.GROUPING_UIID);
			if( groupingID!=null ){
				Grouping grouping = groupingDAO.getGroupingById(groupingID);
				activity.setGrouping(grouping);
				activity.setGroupingUIID(groupingUIID);
			} else {
				activity.setGrouping(null);
				activity.setGroupingUIID(null);
			}
	    }
		
		if (keyExists(activityDetails, WDDXTAGS.ORDER_ID))
		    activity.setOrderId(WDDXProcessor.convertToInteger(activityDetails,WDDXTAGS.ORDER_ID));
		if (keyExists(activityDetails, WDDXTAGS.DEFINE_LATER))
		    activity.setDefineLater(WDDXProcessor.convertToBoolean(activityDetails,WDDXTAGS.DEFINE_LATER));
		
		activity.setLearningDesign(design);
		
		if (keyExists(activityDetails, WDDXTAGS.LEARNING_LIBRARY_ID))
		{
			Long learningLibraryID = WDDXProcessor.convertToLong(activityDetails,WDDXTAGS.LEARNING_LIBRARY_ID);
			if( learningLibraryID!=null ){
				LearningLibrary library = learningLibraryDAO.getLearningLibraryById(learningLibraryID);
				activity.setLearningLibrary(library);
			} else {
				activity.setLearningLibrary(null);
			}
		}
		
		if (keyExists(activityDetails, WDDXTAGS.CREATION_DATE))
		    activity.setCreateDateTime(WDDXProcessor.convertToDate(activityDetails,WDDXTAGS.CREATION_DATE));
		if (keyExists(activityDetails, WDDXTAGS.RUN_OFFLINE))
		    activity.setRunOffline(WDDXProcessor.convertToBoolean(activityDetails,WDDXTAGS.RUN_OFFLINE));
		if (keyExists(activityDetails, WDDXTAGS.ACTIVITY_CATEGORY_ID))
		    activity.setActivityCategoryID(WDDXProcessor.convertToInteger(activityDetails,WDDXTAGS.ACTIVITY_CATEGORY_ID));
		if (keyExists(activityDetails, WDDXTAGS.LIBRARY_IMAGE))	
			activity.setLibraryActivityUiImage(WDDXProcessor.convertToString(activityDetails,WDDXTAGS.LIBRARY_IMAGE));
		
		if (keyExists(activityDetails, WDDXTAGS.LIBRARY_ACTIVITY))
		{
			Long libraryActivityID = WDDXProcessor.convertToLong(activityDetails,WDDXTAGS.LIBRARY_ACTIVITY);
			if( libraryActivityID != null ){
				Activity libraryActivity = activityDAO.getActivityByActivityId(libraryActivityID);
				activity.setLibraryActivity(libraryActivity);
			} else {
				activity.setLibraryActivity(null);
			}
		}
		
		if (keyExists(activityDetails, WDDXTAGS.APPLY_GROUPING))	
		    activity.setApplyGrouping(WDDXProcessor.convertToBoolean(activityDetails,WDDXTAGS.APPLY_GROUPING));
		if (keyExists(activityDetails, WDDXTAGS.GROUPING_SUPPORT_TYPE))	
			activity.setGroupingSupportType(WDDXProcessor.convertToInteger(activityDetails,WDDXTAGS.GROUPING_SUPPORT_TYPE));
		
		return activity;
	}	 
	private  void processActivityType(Activity activity, Hashtable activityDetails) 
			throws WDDXProcessorConversionException, ObjectExtractorException {
		if(activity.isGroupingActivity())
			 buildGroupingActivity((GroupingActivity)activity,activityDetails);
		else if(activity.isToolActivity())
			 buildToolActivity((ToolActivity)activity,activityDetails);
		else if(activity.isGateActivity())
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
		/**
		 * read the createGroupingUUID, get the Grouping Object, and set CreateGrouping to that object
		 */
	    Integer createGroupingUIID = WDDXProcessor.convertToInteger(activityDetails,WDDXTAGS.CREATE_GROUPING_UIID);	    
	    Grouping grouping = groupingDAO.getGroupingByUIID(createGroupingUIID);
	    if (grouping!=null)
	    {
		    groupingActivity.setCreateGrouping(grouping);
		    groupingActivity.setCreateGroupingUIID(createGroupingUIID);
	    }
	    /*Hashtable groupingDetails = (Hashtable) activityDetails.get(WDDXTAGS.GROUPING_DTO); 
		if( groupingDetails != null ){
			Grouping grouping = extractGroupingObject(groupingDetails);		
			groupingActivity.setCreateGrouping(grouping);
			groupingActivity.setCreateGroupingUIID(grouping.getGroupingUIID());
		} else {
			groupingActivity.setCreateGrouping(null);
			groupingActivity.setCreateGroupingUIID(null);
		} */
	}	

	private void buildOptionsActivity(OptionsActivity optionsActivity,Hashtable activityDetails) throws WDDXProcessorConversionException{
		if (keyExists(activityDetails, WDDXTAGS.MAX_OPTIONS))
		    optionsActivity.setMaxNumberOfOptions(WDDXProcessor.convertToInteger(activityDetails,WDDXTAGS.MAX_OPTIONS));
		if (keyExists(activityDetails, WDDXTAGS.MIN_OPTIONS))
		    optionsActivity.setMinNumberOfOptions(WDDXProcessor.convertToInteger(activityDetails,WDDXTAGS.MIN_OPTIONS));
		if (keyExists(activityDetails, WDDXTAGS.OPTIONS_INSTRUCTIONS))
		    optionsActivity.setOptionsInstructions(WDDXProcessor.convertToString(activityDetails,WDDXTAGS.OPTIONS_INSTRUCTIONS));		
	}
	private void buildParallelActivity(ParallelActivity activity,Hashtable activityDetails) throws WDDXProcessorConversionException{		
	}
	private void buildSequenceActivity(SequenceActivity activity,Hashtable activityDetails) throws WDDXProcessorConversionException{
		
	}
	private void buildToolActivity(ToolActivity toolActivity,Hashtable activityDetails) throws WDDXProcessorConversionException{
	    if (keyExists(activityDetails, WDDXTAGS.TOOL_CONTENT_ID))
	        toolActivity.setToolContentId(WDDXProcessor.convertToLong(activityDetails,WDDXTAGS.TOOL_CONTENT_ID));
	    if (keyExists(activityDetails, WDDXTAGS.TOOL_ID))
	    {
			Tool tool =toolDAO.getToolByID(WDDXProcessor.convertToLong(activityDetails,WDDXTAGS.TOOL_ID));
			toolActivity.setTool(tool);	
		}
	}
	private void buildGateActivity(Object activity,Hashtable activityDetails) throws WDDXProcessorConversionException{
		if(activity instanceof SynchGateActivity)
			buildSynchGateActivity((SynchGateActivity)activity,activityDetails);
		else if (activity instanceof PermissionGateActivity)
			buildPermissionGateActivity((PermissionGateActivity)activity,activityDetails);
		else
			buildScheduleGateActivity((ScheduleGateActivity)activity,activityDetails);
		GateActivity gateActivity = (GateActivity)activity ;
		gateActivity.setGateActivityLevelId(WDDXProcessor.convertToInteger(activityDetails,WDDXTAGS.GATE_ACTIVITY_LEVEL_ID));
		gateActivity.setGateOpen(WDDXProcessor.convertToBoolean(activityDetails,WDDXTAGS.GATE_OPEN));
				
	}
	private void buildSynchGateActivity(SynchGateActivity activity,Hashtable activityDetails) throws WDDXProcessorConversionException{	
	}
	private void buildPermissionGateActivity(PermissionGateActivity activity,Hashtable activityDetails) throws WDDXProcessorConversionException{		
	}
	private static void buildScheduleGateActivity(ScheduleGateActivity activity,Hashtable activityDetails) throws WDDXProcessorConversionException{
	    activity.setGateStartDateTime(WDDXProcessor.convertToDate(activityDetails,WDDXTAGS.GATE_START_DATE));
		activity.setGateEndDateTime(WDDXProcessor.convertToDate(activityDetails,WDDXTAGS.GATE_END_DATE));
		activity.setGateStartTimeOffset(WDDXProcessor.convertToLong(activityDetails,WDDXTAGS.GATE_START_OFFSET));
		activity.setGateEndTimeOffset(WDDXProcessor.convertToLong(activityDetails,WDDXTAGS.GATE_END_OFFSET));		
	}
	

	public Grouping extractGroupingObject(Hashtable groupingDetails) throws WDDXProcessorConversionException, ObjectExtractorException{
		
	    //get grouping by uuid
	    Grouping grouping;
	    Integer groupingUUID = WDDXProcessor.convertToInteger(groupingDetails, WDDXTAGS.GROUPING_UIID);	
	    Integer groupingTypeID=WDDXProcessor.convertToInteger(groupingDetails,WDDXTAGS.GROUPING_TYPE_ID);
	    if (groupingTypeID== null) { 
			throw new ObjectExtractorException("groupingTypeID is missing");
		}
	    Grouping existingGrouping = groupingDAO.getGroupingByUIID(groupingUUID);
	    if (existingGrouping != null)
	        grouping = existingGrouping;	   
	    else
	    {
	        Object object = Grouping.getGroupingInstance(groupingTypeID);
			grouping = (Grouping)object;				
			
			if(keyExists(groupingDetails, WDDXTAGS.GROUPING_ID))
				    grouping.setGroupingId(WDDXProcessor.convertToLong(groupingDetails,WDDXTAGS.GROUPING_ID));
			if (keyExists(groupingDetails, WDDXTAGS.GROUPING_UIID))
				    grouping.setGroupingUIID(WDDXProcessor.convertToInteger(groupingDetails,WDDXTAGS.GROUPING_UIID));
		}	    
	   	    
	 
	    if(grouping.isRandomGrouping())
			createRandomGrouping((RandomGrouping)grouping,groupingDetails);
		else if(grouping.isChosenGrouping())
			createChosenGrouping((ChosenGrouping)grouping,groupingDetails);
		else
			createLessonClass((LessonClass)grouping, groupingDetails);  
		
		if (keyExists(groupingDetails,WDDXTAGS.MAX_NUMBER_OF_GROUPS))
		    grouping.setMaxNumberOfGroups(WDDXProcessor.convertToInteger(groupingDetails,WDDXTAGS.MAX_NUMBER_OF_GROUPS));
		
		return grouping;
	}
	private void createRandomGrouping(RandomGrouping randomGrouping,Hashtable groupingDetails) throws WDDXProcessorConversionException{
	    if (keyExists(groupingDetails, WDDXTAGS.LEARNERS_PER_GROUP))
	        randomGrouping.setLearnersPerGroup(WDDXProcessor.convertToInteger(groupingDetails,WDDXTAGS.LEARNERS_PER_GROUP));
		if (keyExists(groupingDetails, WDDXTAGS.NUMBER_OF_GROUPS))
		    randomGrouping.setNumberOfGroups(WDDXProcessor.convertToInteger(groupingDetails,WDDXTAGS.NUMBER_OF_GROUPS));
	}
	private void createChosenGrouping(ChosenGrouping chosenGrouping,Hashtable groupingDetails) throws WDDXProcessorConversionException{
		//no extra properties as yet
	}
	private void createLessonClass(LessonClass lessonClass, Hashtable groupingDetails) throws WDDXProcessorConversionException{
	    if (keyExists(groupingDetails, WDDXTAGS.STAFF_GROUP_ID))
	    {
			Group group = groupDAO.getGroupById(WDDXProcessor.convertToLong(groupingDetails,WDDXTAGS.STAFF_GROUP_ID));
			if(group!=null)
				lessonClass.setStaffGroup(group);
	    }
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
		
	    Integer transitionUUID = WDDXProcessor.convertToInteger(transitionDetails,WDDXTAGS.TRANSITION_UIID);
	    Transition transition = null;	
	    /* It is assumed that the transitionUUID will always be sent */
	    Transition existingTransition = transitionDAO.getTransitionByUUID(transitionUUID, learningDesign);
	 
	    if (existingTransition == null)
	        transition = new Transition();	   
	    else
	        transition = existingTransition;
	    
	    transition.setTransitionUIID(transitionUUID);
	    
	    if (keyExists(transitionDetails, WDDXTAGS.TRANSITION_ID))
	    {
			Long transitionID= WDDXProcessor.convertToLong(transitionDetails,WDDXTAGS.TRANSITION_ID);
			if ( transitionID != null )
				transition.setTransitionId(transitionID);
	    }		
		
	    if (keyExists(transitionDetails, WDDXTAGS.TO_ACTIVITY_UIID))
	    {
	        Integer toUIID=WDDXProcessor.convertToInteger(transitionDetails,WDDXTAGS.TO_ACTIVITY_UIID); 
			if(toUIID!=null){
				Activity toActivity = activityDAO.getActivityByUIID(toUIID, learningDesign);
				transition.setToActivity(toActivity);
				transition.setToUIID(toUIID);
				//update the transitionTo property for the activity
				toActivity.setTransitionTo(transition);
				activityDAO.update(toActivity);
				
			}
	    }
		
	    if (keyExists(transitionDetails, WDDXTAGS.FROM_ACTIVITY_UIID))
	    {
	        Integer fromUIID=WDDXProcessor.convertToInteger(transitionDetails,WDDXTAGS.FROM_ACTIVITY_UIID);
			if(fromUIID!=null){
				Activity fromActivity = activityDAO.getActivityByUIID(fromUIID, learningDesign);
				transition.setFromActivity(fromActivity);
				transition.setFromUIID(fromUIID);
				//update the transitionFrom property for the activity
				fromActivity.setTransitionFrom(transition);
				activityDAO.update(fromActivity);
				
			}	
	    }
	    
	    if(keyExists(transitionDetails, WDDXTAGS.DESCRIPTION))
	        transition.setDescription(WDDXProcessor.convertToString(transitionDetails,WDDXTAGS.DESCRIPTION));
	    if(keyExists(transitionDetails, WDDXTAGS.TITLE))
	        transition.setTitle(WDDXProcessor.convertToString(transitionDetails,WDDXTAGS.TITLE));
		if(keyExists(transitionDetails, WDDXTAGS.CREATION_DATE))
		    transition.setCreateDateTime(WDDXProcessor.convertToDate(transitionDetails,WDDXTAGS.CREATION_DATE));			
		
		transition.setLearningDesign(learningDesign);		
		return transition;
	}		
	
	/**
	 * Checks whether the hashtable contains the key specified by <code>key</code>
	 * If the key exists, returns true, otherwise return false.
	 * @param table The hashtable to check
	 * @param key The key to find
	 * @return
	 */
	private boolean keyExists(Hashtable table, String key) 
	{
	    if (table.containsKey(key))
	        return true;
	    else
	        return false;
	}

	/**
	 * Helper method to delete an activity from a learning design. Before the activity is deleted,
	 * any associations with a transition is removed: any transitions to or from the activity
	 * is deleted.
	 * @param activityToDelete
	 * @param design
	 */
	private void deleteActivity(Activity activityToDelete, LearningDesign design)
	{
	   
	   Transition transitionFrom = activityToDelete.getTransitionFrom();	 	   
	   if (transitionFrom != null)
	       deleteTransition(transitionFrom, design);
	     
	   Transition transitionTo = activityToDelete.getTransitionTo();
	   if (transitionTo != null)
	       deleteTransition(transitionTo, design);
	
	   activityDAO.delete(activityToDelete);
	
	}
	
	/**
	 * Helper method which deletes a Transition object. Before the transition is deleted,
	 * any relationship that this transition has with an activity, will be removed.
	 * @param transition
	 * @param design
	 */
	private void deleteTransition(Transition transition, LearningDesign design)
	{
	    Activity fromActivity = transition.getFromActivity();
	    fromActivity.setTransitionFrom(null);
	    activityDAO.update(fromActivity);
	    
		Activity toActivity = transition.getToActivity();
		toActivity.setTransitionTo(null);
		activityDAO.update(toActivity); 
		
		//This will leave orphan content in the tool tables. It will be removed by the tool content cleaning job, 
		//which may be run from the admin screen or via a cron job
		design.getTransitions().remove(transition);
		
		//actually delete the object from the table.
		transitionDAO.delete(transition);
	}
}
	

