/***************************************************************************
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
 * ************************************************************************
 */
package org.lamsfoundation.lams.util;


import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ChosenGrouping;
import org.lamsfoundation.lams.learningdesign.ComplexActivity;
import org.lamsfoundation.lams.learningdesign.GateActivity;
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
import org.lamsfoundation.lams.learningdesign.dao.hibernate.GroupingDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.LearningDesignDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.LearningLibraryDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.LicenseDAO;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.dao.hibernate.ToolDAO;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;
import org.lamsfoundation.lams.usermanagement.dao.hibernate.UserDAO;
import org.lamsfoundation.lams.usermanagement.dao.hibernate.WorkspaceFolderDAO;
/**
 * @author Manpreet Minhas
 * 
 * This class is used to save the WDDX information into the Database.
 * The WDDX packet sent by FLASH mainly consists of information stored
 * in  key-value pairs in a Hashtable. We check the existence of a 
 * particular key and if present we populate the corresponding object 
 * with its value and then finally persist into the database.
 *   
 */
public class LDWDDXValueObjectStorer {
	
	static LDWDDXValueObjectStorer storer = null;
	protected UserDAO userDAO = null;
	protected LearningDesignDAO learningDesignDAO = null;
	protected ActivityDAO activityDAO =null;
	protected WorkspaceFolderDAO workspaceFolderDAO = null;
	protected LearningLibraryDAO learningLibraryDAO = null;
	protected LicenseDAO licenseDAO;
	protected GroupingDAO groupingDAO;
	protected ToolDAO toolDAO;
	
	public LDWDDXValueObjectStorer(){
		
	}
	
	public LDWDDXValueObjectStorer(LearningDesignDAO learningDesignDAO,LearningLibraryDAO learningLibraryDAO, UserDAO userDAO, ActivityDAO activityDAO,WorkspaceFolderDAO workspaceFolderDAO,LicenseDAO licenseDAO,GroupingDAO groupingDAO){
		this.learningDesignDAO = learningDesignDAO;
		this.learningLibraryDAO = learningLibraryDAO;
		this.userDAO = userDAO;
		this.activityDAO = activityDAO;
		this.workspaceFolderDAO = workspaceFolderDAO;
		this.licenseDAO = licenseDAO;
		this.groupingDAO = groupingDAO;
	}
	/**
	 * @return LDWDDXValueObjectStorer Instance of LDWDDXValueObjectStorer
	 */
	public static LDWDDXValueObjectStorer getInstance()
	{
		return (storer==null? new LDWDDXValueObjectStorer():storer);
	}
	
	/**
	 * This function creates a new LearningDesign object or updates
	 * and existing design depending upon the information provided by FLASH 
	 * and persists it in the database. 
	 * 
	 * @param table Hashtable populated with data from the WDDX packet
	 * @return Long learning_design_id of the design updated/saved 
	 */
	public Long processLearningDesign(Hashtable table)throws Exception{		
		Long learning_design_id = null;
		Long oldDesignID=null;
		LearningDesign learningDesign = null;
		boolean update = false;
		/*
		 * If the hastable already contains the key LEARNING_DESIGN_ID
		 * this means its a request to update an existing design otherwise
		 * it is a request to store a new LearningDesign
		 * */
		if(table.containsKey(WDDXTAGS.LEARNING_DESIGN_ID)){
			update = true;
			learning_design_id  = WDDXProcessor.convertToLong("learning_design_id",table.get(WDDXTAGS.LEARNING_DESIGN_ID));
			learningDesign = learningDesignDAO.getLearningDesignById(learning_design_id);
			if(learningDesign==null)
				throw new Exception("Learning Design with the given learning_design_id " + 
									learning_design_id + " doesn't exists. Update Failed!!");
			
		}else
			learningDesign = new LearningDesign();
		
		boolean readOnly = (WDDXProcessor.convertToBoolean("readOnly",table.get(WDDXTAGS.READ_ONLY))).booleanValue();
		if(readOnly){
			throw new Exception("This design is locked, you cannot save or update it.\n" +
								"Please click on File | Save as... and rename it to save it");
		}
		if(!table.containsKey(WDDXTAGS.FIRST_ACTIVITY_UIID))
			throw new Exception("Mandatory Field first_id is missing");
		
		Integer firstID = WDDXProcessor.convertToInteger("first_activity_ui_id",table.get(WDDXTAGS.FIRST_ACTIVITY_UIID));
		
		updateLearningDesign(table,learningDesign);
		if(update){
			learningDesignDAO.update(learningDesign);			
		}
		else{
			learningDesignDAO.insert(learningDesign);			
			updateDesignActivities(table,learningDesign);		
		}
		calculateFirstActivity(firstID,learningDesign);
		return learningDesign.getLearningDesignId();		
	}	
	/**
	 * This function updates the LearningDesign object with activity information
	 * from the FLASH.
	 * 
	 * @param table The Hashtable containing the information populated by FLASH
	 * @param learningDesign The LearningDesign object to be updated
	 * @throws Exception
	 */
	private void updateDesignActivities(Hashtable table,LearningDesign learningDesign)throws Exception{
		Vector activities =(Vector)table.get(WDDXTAGS.ACTIVITIES);
		Iterator iter = activities.iterator();
		HashSet designActivities = new HashSet();
		while(iter.hasNext()){
			Hashtable activity =(Hashtable)iter.next();
			activity.put(WDDXTAGS.LEARNING_DESIGN_ID,learningDesign.getLearningDesignId());
			designActivities.add(processActivity(activity,learningDesign));
		}
		learningDesign.setActivities(designActivities);
		learningDesignDAO.update(learningDesign);
	}
	/**
	 * This method compares the first_id sent by the Flash to the
	 * one calculated manually by the server. If everything goes well 
	 * both these should be the same but if there is a conflict an 
	 * exception is thrown.
	 * 
	 * @param firstID The activity_ui_id of the first activity sent by flash  
	 * @param design The design for whome the first activity is being calculated
	 * @throws Exception
	 */
	private void calculateFirstActivity(Integer firstID, LearningDesign design)throws Exception{
		Activity flashFirstActivity = activityDAO.getActivityByUIID(firstID,design);
		Activity designFirstActivity = design.calculateFirstActivity();
		if(flashFirstActivity.getActivityId()!=designFirstActivity.getActivityId())
			throw new Exception ("First Activity Conflict. We have two first activities here");
		design.setFirstActivity(flashFirstActivity);
	}
	private void updateDesignTransitions(Hashtable table,LearningDesign learningDesign)throws Exception{
		if(table.containsKey(WDDXTAGS.TRANSITIONS)){
			Vector transitions = (Vector)table.get(WDDXTAGS.TRANSITIONS);
			Iterator iter = transitions.iterator();
			HashSet designTransitions = new HashSet();
			while(iter.hasNext()){
				Hashtable transition = (Hashtable)iter.next();
				transition.put(WDDXTAGS.LEARNING_DESIGN_ID,learningDesign.getLearningDesignId());
				designTransitions.add((transition));
			}
			learningDesign.setTransitions(designTransitions);
			learningDesignDAO.update(learningDesign);
		}
		
	}
	/**
	 * This method uses the Hashtable passed as an argument to build 
	 * and populate a new Transition object. This hashtable is populated
	 * at the Flash side and all we have to do is iterate through it and
	 * set the values for the Transition object. If any of the values being 
	 * passed by the Flash side are inconsistent with the database or 
	 * any of the mandatory fields is missing an Exception is thrown
	 *  
	 * @param table The Hashtable containing the information populated by FLASH 
	 * @return Transition The populated Transition object
	 * @throws Exception
	 */
	private Transition processTransitionObject(Hashtable table)throws Exception{
		Transition transition = new Transition();
		
		if(table.containsKey(WDDXTAGS.TRANSITION_UIID))
			transition.setTransitionUIID(WDDXProcessor.convertToInteger("transition_ui_id",table.get(WDDXTAGS.TRANSITION_UIID)));
		
		if(table.containsKey(WDDXTAGS.DESCRIPTION))
			transition.setDescription((String)table.get(WDDXTAGS.DESCRIPTION));
		
		if(table.containsKey(WDDXTAGS.TITLE))
			transition.setTitle((String)WDDXTAGS.TITLE);
		
		if(table.containsKey(WDDXTAGS.TRANSITION_TO)){
			Long activityID = WDDXProcessor.convertToLong("to_activity_id",table.get(WDDXTAGS.TRANSITION_TO));
			Activity activity = activityDAO.getActivityByActivityId(activityID);
			if(activity!=null)
				transition.setToActivity(activity);
			else
				throw new Exception("Error setting to_activity_id. No activity with an activity_id of " + activityID + " exists");
		}
		if(table.containsKey(WDDXTAGS.TRANSITION_FROM)){
			Long activityID = WDDXProcessor.convertToLong("from_activity_id",table.get(WDDXTAGS.TRANSITION_FROM));
			Activity activity = activityDAO.getActivityByActivityId(activityID);
			if(activity!=null)
				transition.setFromActivity(activity);
			else
				throw new Exception("Error setting from_activity_id. No activity with an activity_id of " + activityID + " exists");
		}
		if(table.containsKey(WDDXTAGS.CREATION_DATE))
			transition.setCreateDateTime((Date)table.get(WDDXTAGS.CREATION_DATE));
		
		if(table.containsKey(WDDXTAGS.TO_ACTIVITY_UIID))
			transition.setToUIID(WDDXProcessor.convertToInteger("to_activity_ui_id",table.get(WDDXTAGS.TO_ACTIVITY_UIID)));
		
		if(table.containsKey(WDDXTAGS.FROM_ACTIVITY_UIID))
			transition.setFromUIID(WDDXProcessor.convertToInteger("from_activity_ui_id",table.get(WDDXTAGS.FROM_ACTIVITY_UIID)));
		
		return transition;
		
	}
	/**
	 * This method uses the Hashtable passed as an argument to update 
	 * the learningDesign object. This hashtable is populated
	 * at the Flash side and all we have to do is iterate through it and
	 * set the values for the LearningDesign object. If any of the values being 
	 * passed by the Flash side are inconsistent with the database or any of the 
	 * mandatory fields is missing an Exception is thrown.
	 * 
	 * @param table The Hashtable parsed from the WDDX string 
	 * @param design The LearningDesign object to be updated/populated
	 * @throws Exception 
	 */
	public void updateLearningDesign(Hashtable table, LearningDesign design) throws Exception{
		try{
			
			if(table.containsKey(WDDXTAGS.LEARNING_DESIGN_UIID))
				design.setLearningDesignUIID(WDDXProcessor.convertToInteger("learning_design_ui_id",table.get(WDDXTAGS.LEARNING_DESIGN_UIID)));
			
			if(table.containsKey(WDDXTAGS.TITLE))
				design.setTitle((String)table.get(WDDXTAGS.TITLE));
			
			if(table.containsKey(WDDXTAGS.DESCRIPTION))			
				design.setDescription((String)table.get(WDDXTAGS.DESCRIPTION));
			
			if(table.containsKey(WDDXTAGS.FIRST_ACTIVITY_ID)){
				Long firstActivityID = WDDXProcessor.convertToLong("first_activity_id",table.get(WDDXTAGS.FIRST_ACTIVITY_ID));
				Activity activity = activityDAO.getActivityByActivityId(firstActivityID);
				if(activity==null)
					throw new Exception("No activity with an activity_id: " + WDDXTAGS.FIRST_ACTIVITY_ID + " exists.");
				design.setFirstActivity(activity);
			}
			
			if(table.containsKey(WDDXTAGS.VERSION))
				design.setVersion((String)table.get(WDDXTAGS.VERSION));
			else
				throw new Exception("Mandatory field version is missing for LearningDesign in the WDDX packet");
			
			if(table.containsKey(WDDXTAGS.MAX_ID))
				design.setMaxId(WDDXProcessor.convertToInteger("max_id",table.get(WDDXTAGS.MAX_ID)));
			
			if(table.containsKey(WDDXTAGS.VALID_DESIGN))
				design.setValidDesign(WDDXProcessor.convertToBoolean("valid_design_flag",table.get(WDDXTAGS.VALID_DESIGN)));
			else
				throw new Exception("Mandatory field valid_design_flag is missing for LearningDesign in the WDDX packet");
				
			if(table.containsKey(WDDXTAGS.READ_ONLY))
				design.setReadOnly(WDDXProcessor.convertToBoolean("read_only_flag",table.get(WDDXTAGS.READ_ONLY)));
			else
				throw new Exception("Mandatory field read_only_flag is missing for LearningDesign in the WDDX packet");
				
			if(table.containsKey(WDDXTAGS.DATE_READ_ONLY))
				design.setDateReadOnly((Date)table.get(WDDXTAGS.DATE_READ_ONLY));
			
			if(table.containsKey(WDDXTAGS.HELP_TEXT))
				design.setHelpText((String)table.get(WDDXTAGS.HELP_TEXT));
			
			if(table.containsKey(WDDXTAGS.COPY_TYPE))
				design.setCopyTypeID(WDDXProcessor.convertToInteger("copy_type",table.get(WDDXTAGS.COPY_TYPE)));
			else
				throw new Exception("Mandatory field copy_type is missing for LearningDesign in the WDDX packet");
			
			if(table.containsKey(WDDXTAGS.CREATION_DATE))
				design.setCreateDateTime((Date)table.get(WDDXTAGS.CREATION_DATE));
			else
				throw new Exception("Mandatory field create_date_time is missing for LearningDesign in the WDDX packet");
		
			if(table.containsKey(WDDXTAGS.USER_ID)){
				Integer userId = WDDXProcessor.convertToInteger("user_id",table.get(WDDXTAGS.USER_ID));
				User user = userDAO.getUserById(userId);
				if(user==null)
					throw new Exception("No user with a user_id: " + WDDXTAGS.USER_ID + " exists.");
				design.setUser(user);
			}else
				throw new Exception("Mandatory Field user_id is missing for LearningDesign in the WDDX packet");
			
			if(table.containsKey(WDDXTAGS.WORKSPACE_FOLDER_ID)){
				Integer workspaceFolderID = WDDXProcessor.convertToInteger("workspace_folder_id",table.get(WDDXTAGS.WORKSPACE_FOLDER_ID));	
				WorkspaceFolder workspaceFolder = workspaceFolderDAO.getWorkspaceFolderByID(workspaceFolderID);				
				if(workspaceFolder==null)
					throw new Exception("No workspaceFolder with a  with workspace_folder_id: " + WDDXTAGS.WORKSPACE_FOLDER_ID + " exists.");
				design.setWorkspaceFolder(workspaceFolder);
				
			}else
				throw new Exception("Mandatory Field workspace_folder_id is missing for LearningDesign in the WDDX packet");
			
			if(table.containsKey(WDDXTAGS.PARENT_DESIGN_ID)){
				Long parentDesignID = WDDXProcessor.convertToLong("parent_learning_design_id",table.get(WDDXTAGS.PARENT_DESIGN_ID));
				LearningDesign parent = learningDesignDAO.getLearningDesignById(parentDesignID);
				if(parent==null)
					throw new Exception("No parent design with with learning_design_id of : " + WDDXTAGS.PARENT_DESIGN_ID + " exists.");
				design.setParentLearningDesign(parent);
			}
			
			if(table.containsKey(WDDXTAGS.DURATION))
					design.setDuration(WDDXProcessor.convertToLong("duration",table.get(WDDXTAGS.DURATION)));
			
			if(table.containsKey(WDDXTAGS.LICENCE_ID)){
				Long licenseID = WDDXProcessor.convertToLong("license_id",table.get(WDDXTAGS.LICENCE_ID));
				License license = licenseDAO.getLicenseByID(licenseID);
				if(license!=null)
					design.setLicense(license);
				else
					throw new Exception("No License with a license_id of : " + licenseID + " exists" );
			}
			
			if(table.containsKey(WDDXTAGS.LICENSE_TEXT))
				design.setLicenseText((String)table.get(WDDXTAGS.LICENSE_TEXT));
			
			
		}catch(WDDXProcessorConversionException wpce){
			String str = wpce.getMessage();
			throw new Exception(wpce.getMessage());
		}
		
	}
	
	/**
	 * This method uses the Hashtable passed as an argument to build and 
	 * populate a new Activity object. This hashtable is populated
	 * at the Flash side and all we have to do is iterate through it and
	 * set the values for the Activity object. If any of the values being 
	 * passed by the Flash side are inconsistent with the database or any of the 
	 * mandatory fields is missing an Exception is thrown.
	 * 
	 * @param table The hashtable passed by Flash 
	 * @return Activity The populated Activity object 
	 * @throws Exception
	 */
	private Activity processActivity(Hashtable table,LearningDesign design) throws Exception {
		Long activityID =null;
		Activity activity = null;
		boolean update = false;
		if(table.containsKey(WDDXTAGS.ACTIVITY_ID)){
			activityID = WDDXProcessor.convertToLong("activity_id",table.get(WDDXTAGS.ACTIVITY_ID));
			activity = activityDAO.getActivityByActivityId(activityID);
			update=true;
		}else{
			 /* If the hastable doesn't contains the key ACTIVITY_ID
			  * this mean its a request to store a new Activity
			  */			
			Integer activityTypeID = WDDXProcessor.convertToInteger("activity_type_id",table.get(WDDXTAGS.ACTIVITY_TYPE_ID));
			activity = createActivityInstance(activityTypeID,table);
		}		
		
		try{
			if(table.containsKey(WDDXTAGS.ACTIVITY_UIID))
				activity.setActivityUIID(WDDXProcessor.convertToInteger("activity_ui_id",table.get(WDDXTAGS.ACTIVITY_UIID)));
			
			if(table.containsKey(WDDXTAGS.DESCRIPTION))
				activity.setDescription((String)table.get(WDDXTAGS.DESCRIPTION));
			
			if(table.containsKey(WDDXTAGS.TITLE))
				activity.setTitle((String)table.get(WDDXTAGS.TITLE));
			
			if(table.containsKey(WDDXTAGS.HELP_TEXT))
				activity.setHelpText((String)table.get(WDDXTAGS.HELP_TEXT));
			
			if(table.containsKey(WDDXTAGS.XCOORD))
				activity.setXcoord(WDDXProcessor.convertToInteger("xcoord",table.get(WDDXTAGS.XCOORD)));
			
			if(table.containsKey(WDDXTAGS.YCOORD))
				activity.setYcoord(WDDXProcessor.convertToInteger("ycoord",table.get(WDDXTAGS.YCOORD)));
			
			if(table.containsKey(WDDXTAGS.PARENT_UIID)){
				Integer parentUIID = WDDXProcessor.convertToInteger("parent_ui_id",table.get(WDDXTAGS.PARENT_UIID));				
				Activity objActivity = activityDAO.getActivityByUIID(parentUIID,design);
				if(objActivity!=null){
					activity.setParentActivity(objActivity);					
					activity.setParentUIID(parentUIID);
				}else
					throw new Exception ("Error setting parent_ui_id. No such activity with activity_ui_id : "+ parentUIID + "exists.");
			}
			
			if(table.containsKey(WDDXTAGS.PARENT_ACTIVITY_ID)){
				Long parentActivityID = WDDXProcessor.convertToLong("parent_activity_id",table.get(WDDXTAGS.PARENT_ACTIVITY_ID)); 
				Activity parent = activityDAO.getActivityByActivityId(parentActivityID);
				if(parent!=null){
					activity.setParentActivity(parent);
					activity.setParentUIID(parent.getActivityUIID());
				}
				else
					throw new Exception("Error setting the parent_activity_id. No such activity with activity_id of: " + parentActivityID + " exists");
					
			}
			
			if(table.containsKey(WDDXTAGS.ACTIVITY_TYPE_ID))
				activity.setActivityTypeId(WDDXProcessor.convertToInteger("learning_activity_type_id",table.get(WDDXTAGS.ACTIVITY_TYPE_ID)));
			
			if(table.containsKey(WDDXTAGS.GROUPING_UIID)){
				Integer groupingUIID = WDDXProcessor.convertToInteger("grouping_ui_id",
																	  table.get(WDDXTAGS.GROUPING_UIID));
				activity.setGroupingUIID(groupingUIID);
				
				Grouping grouping = groupingDAO.getGroupingByUIID(groupingUIID);
				activity.setGrouping(grouping);
			}
			
			if(table.containsKey(WDDXTAGS.DEFINE_LATER))
				activity.setDefineLater((Boolean)table.get(WDDXTAGS.DEFINE_LATER));
			else
				throw new Exception("Mandatory Field define_later is missing for Activity in the WDDX packet");	
			
			if(table.containsKey(WDDXTAGS.RUN_OFFLINE))
				activity.setRunOffline(WDDXProcessor.convertToBoolean("run_offline",table.get(WDDXTAGS.RUN_OFFLINE)));
			else
				throw new Exception("Mandatory Field run_offline is missing for Activity in the WDDX packet");
			
			if(table.containsKey(WDDXTAGS.LEARNING_DESIGN_ID)){
				Long learningDesignID = WDDXProcessor.convertToLong("learning_design_id", table.get(WDDXTAGS.LEARNING_DESIGN_ID));
				LearningDesign learningDesign = learningDesignDAO.getLearningDesignById(learningDesignID);
				if(learningDesign!=null)
					activity.setLearningDesign(learningDesign);
				else
					throw new Exception("Error setting learning_design_Id." + 
										"No such design with learning_design_id : " + learningDesignID + " exists");
			}
			
			if(table.containsKey(WDDXTAGS.LEARNING_LIBRARY_ID)){
				Long learningLibraryID = WDDXProcessor.convertToLong("learning_library_id", table.get(WDDXTAGS.LEARNING_LIBRARY_ID));
				LearningLibrary learningLibrary = learningLibraryDAO.getLearningLibraryById(learningLibraryID);
				if(learningLibrary!=null)
					activity.setLearningLibrary(learningLibrary);
				else
					throw new Exception("Error setting learning_library_Id." + 
							"No such design with learning_library_id : " + learningLibraryID + " exists");
					
			}
			
			if(table.containsKey(WDDXTAGS.CREATION_DATE))
				activity.setCreateDateTime((Date)table.get(WDDXTAGS.CREATION_DATE));
			else
				throw new Exception("Mandatory Field create_date_time is missing for Activity in the WDDX packet");
			
			if(table.containsKey(WDDXTAGS.OFFLINE_INSTRUCTIONS))
				activity.setOfflineInstructions((String)table.get(WDDXTAGS.OFFLINE_INSTRUCTIONS));
			
			if(table.containsKey(WDDXTAGS.LIBRARY_IMAGE))
				activity.setLibraryActivityUiImage((String)table.get(WDDXTAGS.LIBRARY_IMAGE));
			
			if(table.containsKey(WDDXTAGS.LIBRARY_ACTIVITY)){
				Long libraryActivityID = WDDXProcessor.convertToLong("library_activity_id",table.get(WDDXTAGS.LIBRARY_ACTIVITY));
				Activity libActivity = activityDAO.getActivityByActivityId(libraryActivityID);
				if(libActivity!=null)
					activity.setLibraryActivity(libActivity);
				else
					throw new Exception("Error setting library_activity_id" +
										"No Such activity with activity_id of: " + libraryActivityID + " exists");
			}
		}catch(WDDXProcessorConversionException wpce){
			throw new Exception(wpce.getMessage());
		}
		if(update)
			activityDAO.update(activity);
		else
			activityDAO.insert(activity);
		return activity;
	}
	
	/**
	 * Depending upon the activity's type this function instantiates
	 * a new object and calls the corresponding function to populate
	 * its attributes from the hashtable passed on by FLASH
	 * 
	 * @param activityTypeID The type of Activity object to be instantiated
	 * @param table The Hashtable containing the information populated by FLASH
	 * @return Activity The requires populated activity object
	 * @throws Exception
	 */
	private Activity createActivityInstance(Integer activityTypeID,Hashtable table)throws Exception{
		Activity activity = null;
		int type = activityTypeID.intValue();
		switch(type){
			case Activity.TOOL_ACTIVITY_TYPE:
				activity = storeToolActivity(table);
				break;
			case Activity.OPTIONS_ACTIVITY_TYPE:
				activity = storeOptionsActivity(table);
				break;
			case Activity.GROUPING_ACTIVITY_TYPE:
				activity = storeGroupingActivity(table);
				break;
			case Activity.PERMISSION_GATE_ACTIVITY_TYPE:
				activity = storePermissionGateActivity(table);
				break;
			case Activity.SCHEDULE_GATE_ACTIVITY_TYPE:
				activity = storeScheduleGateActivity(table);
				break;
			case Activity.SYNCH_GATE_ACTIVITY_TYPE:
				activity = storeSynchGateActivity(table);
				break;
			case Activity.PARALLEL_ACTIVITY_TYPE:
				activity = storeParallelActivity(table);
				break;
			case Activity.SEQUENCE_ACTIVITY_TYPE:
				activity = storeSequenceActivity(table);
		}		
		
		return activity;
	}	
	
	/** A GroupingActivity creates Grouping. So when we receive a request
	 * to store a GroupingActivity the grouping_id would be missing in 
	 * that case. Instead we will have a unique flash generated value for
	 * grouping_ui_id, based upon which a new record will be generated in
	 * the database for the lams_grouping table.<p> Follwing steps are involved
	 * in saving a GroupingActivity to the database</p>
	 * <ol>
	 * 		<li> Determine the type of Grouping being created by the GroupingActivity </li>
	 * 		<li> Create the Grouping (Chosen/Random)</li>
	 * 		<li> Set the Grouping details for the GroupingActivity</li>
	 * </ol>
	 * 
	 * @param table The Hashtable containing the information populated by FLASH
	 * @return GroupingActivity The populated activity object
	 * @throws Exception
	 * */
	private GroupingActivity storeGroupingActivity(Hashtable table)throws Exception{
		
		GroupingActivity groupingActivity = new GroupingActivity();
		
		Hashtable groupingTable = (Hashtable)table.get(WDDXTAGS.GROUPING);
		
		Integer groupingType =null;
		Grouping grouping = null;
		
		if(groupingTable.containsKey(WDDXTAGS.GROUPING_TYPE_ID))
			groupingType = WDDXProcessor.convertToInteger("grouping_type_id",groupingTable.get(WDDXTAGS.GROUPING_TYPE_ID));
		else
			throw new Exception ("grouping_type_id missing for GroupingActivity.Cannot create Grouping without that");
		
		if(groupingType==Grouping.RANDOM_GROUPING_TYPE)
			grouping = createRandomGrouping(groupingTable);
		else
			grouping = createChosenGrouping(groupingTable);
		
		groupingActivity.setCreateGrouping(grouping);
		groupingActivity.setCreateGroupingUIID(grouping.getGroupingUIID());
		return groupingActivity;
		
	}
	
	/**
	 * This function creates and populates the RandomGrouping object from
	 * the information passed on by FLASH in the Hashtable
	 * 
	 * @param table The Hashtable containing the information populated by FLASH
	 * @return RandomGrouping The populated activity object
	 * @throws Exception
	 */
	private RandomGrouping createRandomGrouping(Hashtable table)throws Exception{
		
		RandomGrouping randomGrouping = new RandomGrouping();		
		
		if(table.containsKey(WDDXTAGS.MAX_NUMBER_OF_GROUPS))
			randomGrouping.setMaxNumberOfGroups(WDDXProcessor.convertToInteger("max_number_of_groups",table.get(WDDXTAGS.MAX_NUMBER_OF_GROUPS)));		
		
		if(table.containsKey(WDDXTAGS.GROUPING_UIID))
			randomGrouping.setGroupingUIID(WDDXProcessor.convertToInteger("grouping_ui_id",table.get(WDDXTAGS.GROUPING_UIID)));
		
		if(table.containsKey(WDDXTAGS.NUMBER_OF_GROUPS))
			randomGrouping.setNumberOfGroups(WDDXProcessor.convertToInteger("number_of_groups",table.get(WDDXTAGS.NUMBER_OF_GROUPS)));
		
		if(table.containsKey(WDDXTAGS.LEARNERS_PER_GROUP))
			randomGrouping.setLearnersPerGroup(WDDXProcessor.convertToInteger("learners_per_group",table.get(WDDXTAGS.LEARNERS_PER_GROUP)));
		
		groupingDAO.insert(randomGrouping);
		return randomGrouping;
		
	}
	/**
	 * This function creates and populates the ChosenGrouping object from
	 * the information passed on by FLASH in the Hashtable
	 * 
	 * @param table The Hashtable containing the information populated by FLASH
	 * @return ChosenGrouping The populated activity object
	 * @throws Exception
	 */
	private ChosenGrouping createChosenGrouping(Hashtable table)throws Exception{
		ChosenGrouping chosenGrouping = new ChosenGrouping();		
		
		if(table.containsKey(WDDXTAGS.MAX_NUMBER_OF_GROUPS))
			chosenGrouping.setMaxNumberOfGroups(WDDXProcessor.convertToInteger("max_number_of_groups",table.get(WDDXTAGS.MAX_NUMBER_OF_GROUPS)));		
		
		if(table.containsKey(WDDXTAGS.GROUPING_UIID))
			chosenGrouping.setGroupingUIID(WDDXProcessor.convertToInteger("grouping_ui_id",table.get(WDDXTAGS.GROUPING_UIID)));
		
		groupingDAO.insert(chosenGrouping);
		return chosenGrouping;
	}
	
	/**
	 * This function creates and populates the OptionsActivity object from
	 * the information passed on by FLASH in the Hashtable
	 * 
	 * @param table The Hashtable containing the information populated by FLASH
	 * @return OptionsActivity The populated activity object
	 * @throws Exception
	 */
	private OptionsActivity storeOptionsActivity(Hashtable table)throws Exception{
		
		OptionsActivity optActivity = new OptionsActivity();
		
		if(table.containsKey(WDDXTAGS.MAX_OPTIONS))
			optActivity.setMaxNumberOfOptions(WDDXProcessor.convertToInteger("max_number_of_options",table.get(WDDXTAGS.MAX_OPTIONS)));
		
		if(table.containsKey(WDDXTAGS.MIN_OPTIONS))
			optActivity.setMinNumberOfOptions(WDDXProcessor.convertToInteger("min_number_of_options",table.get(WDDXTAGS.MIN_OPTIONS)));
		
		if(table.containsKey(WDDXTAGS.OPTIONS_INSTRUCTIONS))
			optActivity.setOptionsInstructions((String)table.get(WDDXTAGS.OPTIONS_INSTRUCTIONS));		
		addChildActivities(optActivity,table);
		return optActivity;
	}	
	
	/**
	 * This function creates and populates the ToolActivity object from
	 * the information passed on by FLASH in the Hashtable
	 * 
	 * @param table The Hashtable containing the information populated by FLASH
	 * @return ToolActivity The populated activity object
	 * @throws Exception
	 */
	private ToolActivity storeToolActivity(Hashtable table)throws Exception{				
		
		ToolActivity toolActivity = new ToolActivity();
		
		if(table.containsKey(WDDXTAGS.TOOL_ID)){
			Long toolID = WDDXProcessor.convertToLong("tool_id",table.get(WDDXTAGS.TOOL_ID));
			Tool tool = toolDAO.getToolByID(toolID);
			if(tool!=null)
				toolActivity.setTool(tool);
			else
				throw new Exception("Error setting tool_id. " +
									"No such tool with tool_id of: " + toolID + "exists");
				
		}
		if(table.containsKey(WDDXTAGS.ORDER_ID))
			toolActivity.setOrderId(WDDXProcessor.convertToInteger("order_id",table.get(WDDXTAGS.ORDER_ID)));
		
		if(table.containsKey(WDDXTAGS.TOOL_CONTENT_ID))
			toolActivity.setToolContentId(WDDXProcessor.convertToLong("tool_content_id",table.get(WDDXTAGS.TOOL_CONTENT_ID)));
		
		return toolActivity;
		
	}
	/**
	 * This functions adds the attributes that are common for all kind of
	 * gate activities (SYNCH/SCHEDULE/PERMISSION). Main purpose of this function
	 * is avoid any unnecessary duplicacy of code.
	 * 
	 * @param gateActivity The GateActivity whose attributes are being set
	 * @param table The Hashtable containing the information populated by FLASH
	 * @throws Exception
	 */
	private void addGateActivityAttributes(GateActivity gateActivity, Hashtable table)throws Exception{
		
		if(table.containsKey(WDDXTAGS.GATE_ACTIVITY_LEVEL_ID))
			gateActivity.setGateActivityLevelId(WDDXProcessor.convertToInteger("gate_activity_level_id",
																						 table.get(WDDXTAGS.GATE_ACTIVITY_LEVEL_ID)));
		if(table.containsKey(WDDXTAGS.GATE_OPEN))
			gateActivity.setGateOpen(WDDXProcessor.convertToBoolean("gate_open",
																			  table.get(WDDXTAGS.GATE_OPEN)));
	}
	/**
	 * This function creates and populates the PermissionGateActivity object from
	 * the information passed on by FLASH in the Hashtable
	 * 
	 * @param table The Hashtable containing the information populated by FLASH
	 * @return PermissionGateActivity The populated activity object
	 * @throws Exception
	 */
	private PermissionGateActivity storePermissionGateActivity(Hashtable table)throws Exception{
		PermissionGateActivity permissionGateActivity = new PermissionGateActivity();		
		addGateActivityAttributes(permissionGateActivity,table);		
		return permissionGateActivity;		
	}
	
	/**
	 * This function creates and populates the ScheduleGateActivity object from
	 * the information passed on by FLASH in the Hashtable
	 * 
	 * @param table The Hashtable containing the information populated by FLASH
	 * @return ScheduleGateActivity The populated activity object
	 * @throws Exception
	 */
	private ScheduleGateActivity storeScheduleGateActivity(Hashtable table)throws Exception{
		ScheduleGateActivity scheduleGateActivity = new ScheduleGateActivity();
		addGateActivityAttributes(scheduleGateActivity,table);
		
		if(table.containsKey(WDDXTAGS.GATE_START_DATE))
			scheduleGateActivity.setGateStartDateTime((Date)table.get(WDDXTAGS.GATE_START_DATE));
		
		if(table.containsKey(WDDXTAGS.GATE_END_DATE))
			scheduleGateActivity.setGateEndDateTime((Date)table.get(WDDXTAGS.GATE_END_DATE));
		
		if(table.containsKey(WDDXTAGS.GATE_START_OFFSET))
			scheduleGateActivity.setGateStartTimeOffset(WDDXProcessor.convertToLong("gate_start_time_offset",
																					table.get(WDDXTAGS.GATE_START_OFFSET)));
		if(table.containsKey(WDDXTAGS.GATE_END_OFFSET))
			scheduleGateActivity.setGateEndTimeOffset(WDDXProcessor.convertToLong("gate_end_time_offset",
																				  table.get(WDDXTAGS.GATE_END_OFFSET)));
		
		return scheduleGateActivity;
	}
	
	/**
	 * This function creates and populates the SynchGateActivity object from
	 * the information passed on by FLASH in the Hashtable
	 * 
	 * @param table The Hashtable containing the information populated by FLASH
	 * @return SynchGateActivity The populated activity object
	 * @throws Exception
	 */
	private SynchGateActivity storeSynchGateActivity(Hashtable table)throws Exception{
		SynchGateActivity synchGateActivity = new SynchGateActivity();
		addGateActivityAttributes(synchGateActivity,table);		
		return synchGateActivity;
	}
	
	/**
	 * This function creates and populates the ParallelActivity object from
	 * the information passed on by FLASH in the Hashtable
	 * 
	 * @param table The Hashtable containing the information populated by FLASH
	 * @return ParallelActivity The populated activity object
	 * @throws Exception
	 */
	private ParallelActivity storeParallelActivity(Hashtable table)throws Exception{
		ParallelActivity parallelActivity = new ParallelActivity();
		addChildActivities(parallelActivity,table);		
		return parallelActivity;
	}
	/**
	 * This function creates and populates the SequenceActivity object from
	 * the information passed on by FLASH in the Hashtable
	 *  
	 * @param table The Hashtable containing the information populated by FLASH
	 * @return SequenceActivity The populated activity object 
	 * @throws Exception
	 */
	private SequenceActivity storeSequenceActivity(Hashtable table) throws Exception{
		SequenceActivity sequenceActivity = new SequenceActivity();
		addChildActivities(sequenceActivity,table);
		return sequenceActivity;
	}	
	/**
	 * This function parses the FLASH populated hashtable for any child activities
	 * for a given ComplexActivity. If found it sets them otherwise it throws an
	 * Exception saying an attempt was made to save a ComplexActivity without 
	 * any child activities coz that is not possible. We cannot have an
	 * OptionsActivity without any optional activities (child activities) inside it.
	 * 
	 * @param complexActivity The ComplexActivity whose children are being set
	 * @param table The Hashtable containing the information populated by FLASH
	 * @throws Exception
	 */
	private void addChildActivities(ComplexActivity complexActivity, Hashtable table)throws Exception{
		
		if(table.containsKey(WDDXTAGS.CHILD_ACTIVITIES)){		
			Vector vector = (Vector)table.get(WDDXTAGS.CHILD_ACTIVITIES);
			HashSet childActivities = new HashSet();
			Iterator iter = vector.iterator();
			while(iter.hasNext()){
				Activity activity = (Activity)iter.next();
				childActivities.add(activity);
			}
			complexActivity.setActivities(childActivities);
		}else
			throw new Exception("An attempt to save a complex activity with no child activities");
	}
}
