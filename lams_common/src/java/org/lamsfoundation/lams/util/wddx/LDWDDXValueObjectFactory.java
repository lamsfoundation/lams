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
package org.lamsfoundation.lams.util.wddx;

import java.util.Collection;
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
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.dao.hibernate.ToolDAO;




/**
 * @author Manpreet Minhas
 *  
 * When sending a number as a value within a hashtable, we have to either put a proper 
 * value in the hashtable (ie not null) or leave out that tag all together. On the 
 * flash end, if we leave out the tag, it will return nothing when that tag is checked, 
 * but it won't cause an error. Therefore when sending ids, if the id doesn't exist 
 * (value is null), don't add that tag to the hashtable, and that tag will be left out
 * of the packet.
 */
public class LDWDDXValueObjectFactory {
	
	static LDWDDXValueObjectFactory factory = null;
	
	protected ActivityDAO activityDAO;
	protected ToolDAO toolDAO;
	
	public LDWDDXValueObjectFactory(){
		
	}
	/**
	 * Constructor
	 * 
	 * @param activityDAO The activityDAO to set
	 * @param toolDAO The toolDAO to set 
	 */
	public LDWDDXValueObjectFactory(ActivityDAO activityDAO,ToolDAO toolDAO){
		this.activityDAO = activityDAO;
		this.toolDAO = toolDAO;
	}
	/**
	 * @return LDWDDXValueObjectFactory Returns an instance of LDWDDXValueObjectFactory
	 */
	public static LDWDDXValueObjectFactory getInstance()
	{
		return (factory==null? new LDWDDXValueObjectFactory():factory);
	}
	
	/**
	 * Returns the list of all Learning Libraries
	 * @param learningLibraries List of Learning Libraries
	 * @return Hashtable
	 */
	public Hashtable requestLearningLibraryList(Collection learningLibraries){
		Hashtable libraryList = new Hashtable();
		
		libraryList.put(WDDXTAGS.OBJECT_TYPE,LearningLibrary.LIBRARY_LIST_OBJECT);
		libraryList.put(WDDXTAGS.TITLE,"Learning Libraries");
		libraryList.put(WDDXTAGS.DESCRIPTION,"All available system libraries");
		
		Vector libraries = new Vector();
		if(learningLibraries!=null){
			Iterator iter = learningLibraries.iterator();
			while(iter.hasNext()){
				LearningLibrary library = (LearningLibrary)iter.next();				
				Hashtable output = buildLearningLibraryObject(library);
				libraries.add(output);
			}
		}
		libraryList.put(WDDXTAGS.LIB_PACKAGE,libraries);		
		return libraryList;		
	}
	
	/**
	 * Converts the LearningLibrary object into Hashtable 
	 * with Key as the attribute_name and value as value as 
	 * the attribute_value from the underlying database
	 * 
	 * @param library The LearningLibrary to be converted
	 * @return Hashtable
	 */
	private Hashtable buildLearningLibraryObject(LearningLibrary library){
		Hashtable libraries = new Hashtable();
		
		Long libraryID = library.getLearningLibraryId();
		
		libraries.put(WDDXTAGS.LEARNING_LIBRARY_ID,libraryID);
		
		libraries.put(WDDXTAGS.DESCRIPTION,
					  (library.getDescription()!=null?library.getDescription():WDDXTAGS.STRING_NULL_VALUE));
		
		libraries.put(WDDXTAGS.TITLE,
					  (library.getTitle()!=null?library.getTitle():WDDXTAGS.STRING_NULL_VALUE));
		
		libraries.put(WDDXTAGS.CREATION_DATE,
					  (library.getCreateDateTime()!=null?library.getCreateDateTime():WDDXTAGS.DATE_NULL_VALUE));
		
		Collection coll = activityDAO.getActivitiesByLibraryID(libraryID);
		Vector templateActivities = new Vector();		
		if(coll!=null){
			Iterator iter = coll.iterator();
			while (iter.hasNext()){	
				Object object = iter.next();
				Hashtable output = buildActivityObject(object);	
				addAuthoringURLS(object,output);
				templateActivities.add(output);
			}			
		}
		libraries.put(WDDXTAGS.LIB_ACTIVITIES,templateActivities);
		return libraries;
	}	
	
	/**
	 * In case the activity object is of type ToolActivity, 
	 * this function adds tool specific information required by the 
	 * authoring enviornment such as Tool's Authoring Url's.
	 * 
	 * @param activity The activity object to be parsed
	 * @param output The hashtable to be updated
	 */
	private void addAuthoringURLS(Object activity,Hashtable output){		
		if (activity.getClass().getName().equals("org.lamsfoundation.lams.learningdesign.ToolActivity")){
			ToolActivity toolActivity = (ToolActivity)activity;
			Tool tool =toolActivity.getTool();
			Hashtable toolOutput = buildToolObject(tool);
			output.put(WDDXTAGS.AUTH_URL,toolOutput);
		}		
		
	}	
	/**
	 * Converts the Tool object into Hashtable 
	 * with Key as the attribute_name and value as value as 
	 * the attribute_value from the underlying database
	 * 
	 * @param tool The Tool to be converted
	 * @return Hashtable
	 */
	private Hashtable buildToolObject(Tool tool){
		Hashtable table = new Hashtable();
		table.put(WDDXTAGS.OBJECT_TYPE,"Tool");
		table.put(WDDXTAGS.TOOL_ID,tool.getToolId());
		table.put(WDDXTAGS.TOOL_DISPLAY_NAME,tool.getToolDisplayName());
		table.put(WDDXTAGS.TOOl_AUTH_URL,tool.getAuthorUrl());
		return table;
	}
	/**
	 * Determines the type of activity and adds
	 * activity specific attributes  
	 * 
	 * @param activities
	 * @param activity
	 */
	private  void processActivityType(Hashtable activities, Object activity){		
		String className = activity.getClass().getName();
		if(activity instanceof GroupingActivity)
			buildGroupingActivityObject(activities,activity);
		else if(activity instanceof ToolActivity)
			buildToolActivity(activities,(ToolActivity)activity);
		else if(activity instanceof GateActivity)
			buildGateActivityObject(activities,activity);
		else 			
			buildComplexActivityObject(activities,activity);		
	}
	
	/**
	 * Converts the Activity object into Hashtable 
	 * with Key as the attribute_name and value as value as 
	 * the attribute_value from the underlying database
	 * 
	 * @param objActivity The object to be converted
	 * @return Hashtable
	 */
	public  Hashtable buildActivityObject(Object objActivity){
		
		Hashtable activities = getCutDownActivityDetails(objActivity);		
		processActivityType(activities,objActivity);
		
		Activity activity =(Activity)objActivity;
		
		activities.put(WDDXTAGS.HELP_TEXT,
					  (activity.getHelpText()!=null?activity.getHelpText():WDDXTAGS.STRING_NULL_VALUE));		
		
		activities.put(WDDXTAGS.PARENT_ACTIVITY_ID,
					   (activity.getParentActivity()!=null?activity.getParentActivity().getActivityId():WDDXTAGS.NUMERIC_NULL_VALUE_LONG));
		
		activities.put(WDDXTAGS.PARENT_UIID,
					  (activity.getParentActivity()!=null?activity.getParentActivity().getActivityUIID():WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER));		
		
		activities.put(WDDXTAGS.ACTIVITY_TYPE_ID,
					  (activity.getActivityTypeId()!=null?activity.getActivityTypeId():WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER));
		
		activities.put(WDDXTAGS.GROUPING_ID,
					  (activity.getGrouping()!=null?activity.getGrouping().getGroupingId():WDDXTAGS.NUMERIC_NULL_VALUE_LONG));
		
		activities.put(WDDXTAGS.GROUPING_UIID,
					  (activity.getGrouping()!=null?activity.getGrouping().getGroupingUIID():WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER));
		
		activities.put(WDDXTAGS.DEFINE_LATER,activity.getDefineLater());
		
		activities.put(WDDXTAGS.RUN_OFFLINE,activity.getRunOffline());
		
		activities.put(WDDXTAGS.LEARNING_DESIGN_ID,
					  (activity.getLearningDesign()!=null?activity.getLearningDesign().getLearningDesignId():WDDXTAGS.NUMERIC_NULL_VALUE_LONG));
		
		activities.put(WDDXTAGS.LEARNING_LIBRARY_ID,
					  (activity.getLearningLibrary()!=null?activity.getLearningLibrary().getLearningLibraryId():WDDXTAGS.NUMERIC_NULL_VALUE_LONG));
		
		if(activity.getCreateDateTime()!=null)
			activities.put(WDDXTAGS.CREATION_DATE,activity.getCreateDateTime());
		
		activities.put(WDDXTAGS.OFFLINE_INSTRUCTIONS,
					  (activity.getOfflineInstructions()!=null?activity.getOfflineInstructions():WDDXTAGS.STRING_NULL_VALUE));
		
		if(activity.getLibraryActivityUiImage()!=null)
			activities.put(WDDXTAGS.LIBRARY_IMAGE,activity.getLibraryActivityUiImage());		
		
		activities.put(WDDXTAGS.LIBRARY_ACTIVITY,
					  (activity.getLibraryActivity()!=null?activity.getLibraryActivity().getActivityId():WDDXTAGS.NUMERIC_NULL_VALUE_LONG));
		
		return activities;
	}
	/**
	 * This function populates the hashtable to be sent to FLASH
	 * with all the information realted to the GroupingActivity. 
	 * A GroupingActivity is always associated with Grouping 
	 * (Chosen or Random)so it includes that information as well.
	 *  
	 * @param activities
	 * @param activity
	 */
	private  void buildGroupingActivityObject(Hashtable activities, Object activity){
		
		GroupingActivity groupingActivity = (GroupingActivity)activity;
		Grouping grouping = groupingActivity.getCreateGrouping();
		Integer groupingType = grouping.getGroupingTypeId();
		
		Hashtable groupingTable = null;
		if(groupingType==Grouping.CHOSEN_GROUPING_TYPE)
			groupingTable = addChosenGroupingAttributes((ChosenGrouping)grouping);
		else
			groupingTable = addRandomGroupingAttributes((RandomGrouping)grouping);
		
		groupingTable.put(WDDXTAGS.MAX_NUMBER_OF_GROUPS,grouping.getMaxNumberOfGroups());
		groupingTable.put(WDDXTAGS.GROUPING_UIID,grouping.getGroupingUIID());
		
		activities.put(WDDXTAGS.CREATE_GROUPING_ID,grouping.getGroupingId());
		activities.put(WDDXTAGS.CREATE_GROUPING_UIID,grouping.getGroupingUIID());
		activities.put(WDDXTAGS.GROUPING,groupingTable);
		
		
	}	
	/**
	 * As of now there are no additional attributes defined for
	 * ChosenGrouping, so leaving this function blank. More attributes
	 * would be added later on
	 * 
	 * @param chosenGrouping The Grouping object whose values have to be 
	 * 						 populated into the hashtable
	 * @return Hashtable The populated hashtable to be passed on to FLASH 
	 */
	private  Hashtable addChosenGroupingAttributes(ChosenGrouping chosenGrouping){
		return new Hashtable();
	}
	/**
	 * This function adds RandomGrouping specific attributes to the
	 * hashtable to be passed on to FLASH
	 * 
	 * @param randomGrouping The Grouping object whose values have to be 
	 * 						 populated into the hashtable
	 * @return Hashtable The populated hashtable to be passed on to FLASH
	 */
	private  Hashtable addRandomGroupingAttributes(RandomGrouping randomGrouping){
		Hashtable groupingTable = new Hashtable();		
		groupingTable.put(WDDXTAGS.NUMBER_OF_GROUPS,randomGrouping.getNumberOfGroups());
		groupingTable.put(WDDXTAGS.LEARNERS_PER_GROUP,randomGrouping.getLearnersPerGroup());
		return groupingTable;
	}
	
	
	/**
	 * Adds ToolActivity specific attributes
	 * @param activities
	 * @param toolActivity
	 */
	private  void buildToolActivity(Hashtable activities, ToolActivity toolActivity){		
		
		activities.put(WDDXTAGS.ORDER_ID,
					  (toolActivity.getOrderId()!=null?toolActivity.getOrderId():WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER));
		if(toolActivity.getTool()!=null)
			activities.put(WDDXTAGS.TOOL_ID,toolActivity.getTool().getToolId());			
		if(toolActivity.getToolContentId()!=null)
			activities.put(WDDXTAGS.TOOL_CONTENT_ID,toolActivity.getToolContentId());		
	}
	/**
	 * Adds OptionsActivity specific attributes
	 * @param activities
	 * @param optActivity
	 */
	private  void buildOptionsActivityObject(Hashtable activities, OptionsActivity optActivity){
		
		if(optActivity.getMaxNumberOfOptions()!=null)
			activities.put(WDDXTAGS.MAX_OPTIONS,optActivity.getMaxNumberOfOptions());
		
		if(optActivity.getMinNumberOfOptions()!=null)
			activities.put(WDDXTAGS.MIN_OPTIONS,optActivity.getMinNumberOfOptions());
		
		activities.put(WDDXTAGS.OPTIONS_INSTRUCTIONS,
					   (optActivity.getOptionsInstructions()!=null?optActivity.getOptionsInstructions():WDDXTAGS.STRING_NULL_VALUE));
		
		Iterator iter = optActivity.getActivities().iterator();		
		addChildActivities(activities,iter);		
	}	
	/**
	 * TODO As of now SequenceActivity has no additional attributes of its own.
	 * So leaving this function empty for now. In case it is decided that
	 * this activity will have no additional attributes, we have to get rid 
	 * of this function 
	 * 
	 * @param activities
	 * @param sequenceActivity
	 */
	private  void buildSequenceActivityObject(Hashtable activities,SequenceActivity sequenceActivity){
		
	}
	
	/**
	 * TODO As of now ParallelActivity has no additional attributes of its own.
	 * So leaving this function empty for now. In case it is decided that
	 * this activity will have no additional attributes, we have to get rid 
	 * of this function
	 * 
	 * @param activities
	 * @param parallelActivity
	 */
	private  void buildParallelActivityObject(Hashtable activities,ParallelActivity parallelActivity){
		
	}
	/**
	 * Adds ScheduleGateActivity specific attributes
	 * @param activities
	 * @param schActivity
	 */
	private  void buildScheduleGateActivityObject(Hashtable activities, ScheduleGateActivity schActivity){

		if(schActivity.getGateEndTimeOffset()!=null)
			activities.put(WDDXTAGS.GATE_END_OFFSET,schActivity.getGateEndTimeOffset());
		
		if(schActivity.getGateStartTimeOffset()!=null)
			activities.put(WDDXTAGS.GATE_START_OFFSET,schActivity.getGateStartTimeOffset());

		activities.put(WDDXTAGS.GATE_END_DATE,
					   (schActivity.getGateEndDateTime()!=null?schActivity.getGateEndDateTime():WDDXTAGS.DATE_NULL_VALUE));
		
		activities.put(WDDXTAGS.GATE_START_DATE,
					  (schActivity.getGateStartDateTime()!=null?schActivity.getGateStartDateTime():WDDXTAGS.DATE_NULL_VALUE));
	}
	/**
	 * Adds GateActivity specific attributes  
	 * @param activities
	 * @param activity
	 */
	private  void buildGateActivityObject(Hashtable activities, Object activity){
		
		if(activity instanceof PermissionGateActivity)
			buildPermissionGateActivityObject(activities,(PermissionGateActivity)activity);
		else if(activity instanceof SynchGateActivity)
			buildSynchGateActivityObject(activities,(SynchGateActivity)activity);
		else
			buildScheduleGateActivityObject(activities,(ScheduleGateActivity)activity);
			
		GateActivity gateActivity = (GateActivity)activity;
		
		activities.put(WDDXTAGS.GATE_ACTIVITY_LEVEL_ID,
				   (gateActivity.getGateActivityLevelId()!=null?gateActivity.getGateActivityLevelId():WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER));
	
		activities.put(WDDXTAGS.GATE_OPEN,
				   (gateActivity.getGateOpen()!=null?gateActivity.getGateOpen():WDDXTAGS.BOOLEAN_NULL_VALUE));			
	}
	private  void buildPermissionGateActivityObject(Hashtable activities,PermissionGateActivity permissionGateActivity){
		
	}
	private  void buildSynchGateActivityObject(Hashtable activities,SynchGateActivity synchGateActivity){
		
	}
	
	/**
	 * Adds ComplexActivity specific attributes
	 * @param activities
	 * @param activity
	 */
	private  void buildComplexActivityObject(Hashtable activities, Object activity){
		
		if(activity instanceof OptionsActivity)
			buildOptionsActivityObject(activities,(OptionsActivity)activity);
		else if(activity instanceof SequenceActivity)
			buildSequenceActivityObject(activities,(SequenceActivity)activity);
		else
			buildParallelActivityObject(activities,(ParallelActivity)activity);
		
		ComplexActivity complexActivity = (ComplexActivity)activity;
		Iterator iter = complexActivity.getActivities().iterator();
		addChildActivities(activities,iter);
	}	
	/**
	 * @param activities
	 * @param iter
	 */
	private  void addChildActivities(Hashtable activities,Iterator iter){
		Vector childActivities = new Vector();
		while(iter.hasNext()){
			Object object = iter.next();
			childActivities.add(buildActivityObject(object));
		}
		activities.put(WDDXTAGS.CHILD_ACTIVITIES,childActivities);
	}
	/**
	 * Converts the Transition object into Hashtable 
	 * with Key as the attribute_name and value as value as 
	 * the attribute_value from the underlying database
	 * 
	 * @param trans
	 * @return Hashtable
	 */
	public static Hashtable buildTransitionObject(Transition trans){
		Hashtable transitions = new Hashtable();
		
		transitions.put(WDDXTAGS.TRANSITION_ID,trans.getTransitionId());
		
		transitions.put(WDDXTAGS.TRANSITION_UIID,
					   (trans.getTransitionUIID()!=null?trans.getTransitionUIID():WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER));
		
		if(trans.getDescription()!=null)
			transitions.put(WDDXTAGS.DESCRIPTION,trans.getDescription());
		
		if(trans.getTitle()!=null)
			transitions.put(WDDXTAGS.TITLE,trans.getTitle());
		
		transitions.put(WDDXTAGS.TRANSITION_TO,
					   (trans.getToActivity()!=null?trans.getToActivity().getActivityId():WDDXTAGS.NUMERIC_NULL_VALUE_LONG));
		
		transitions.put(WDDXTAGS.TRANSITION_FROM,
					   (trans.getFromActivity()!=null?trans.getFromActivity().getActivityId():WDDXTAGS.NUMERIC_NULL_VALUE_LONG));
		
		transitions.put(WDDXTAGS.LEARNING_DESIGN_ID,
					   (trans.getLearningDesign()!=null?trans.getLearningDesign().getLearningDesignId():WDDXTAGS.NUMERIC_NULL_VALUE_LONG));
		transitions.put(WDDXTAGS.TO_ACTIVITY_UIID,
					   (trans.getToUIID()!=null?trans.getToUIID():WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER));
		
		transitions.put(WDDXTAGS.FROM_ACTIVITY_UIID,
					   (trans.getFromUIID()!=null?trans.getFromUIID():WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER));
		
		return transitions;
	}
	
	/**
	 * Returns the list of all availabe Learning Designs
	 * @param learningDesigns
	 * @return Hashtable
	 */
	public Hashtable requestLearningDesignList(Collection learningDesigns){
		Hashtable designList = new Hashtable();
		
		designList.put(WDDXTAGS.OBJECT_TYPE,"LearningDesignList");
		designList.put(WDDXTAGS.TITLE,"Learning Designs");
		designList.put(WDDXTAGS.DESCRIPTION,"All available Learning Designs");
		
		Vector designs = new Vector();
		if(learningDesigns!=null){
			Iterator iter = learningDesigns.iterator();
			while(iter.hasNext()){
				LearningDesign design = (LearningDesign)iter.next();				
				Hashtable output = buildLearningDesignObject(design);
				designs.add(output);
			}
		}
		designList.put(WDDXTAGS.DESIGN_PACKAGE,designs);
		return designList;		
	}
	/**
	 * Converts the LearningDesign object into Hashtable 
	 * with Key as the attribute_name and value as value as 
	 * the attribute_value from the underlying database
	 * 
	 * @param design The LearningDesign to be converted
	 * @return Hashtable
	 */
	public Hashtable buildLearningDesignObject(LearningDesign design){
		Hashtable designs = getCutDownLearningDesignDetails(design);
		
		
		designs.put(WDDXTAGS.LEARNING_DESIGN_UIID,
					(design.getLearningDesignUIID()!=null?design.getLearningDesignUIID():WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER));
		
		designs.put(WDDXTAGS.VALID_DESIGN,design.getValidDesign());
		
		designs.put(WDDXTAGS.READ_ONLY,design.getReadOnly());
		
		if(design.getDateReadOnly()!=null)
			designs.put(WDDXTAGS.DATE_READ_ONLY,design.getDateReadOnly());
		
		designs.put(WDDXTAGS.USER_ID,design.getUser().getUserId());
		
		designs.put(WDDXTAGS.VERSION,
					(design.getVersion()!=null?design.getVersion():WDDXTAGS.STRING_NULL_VALUE));
		
		designs.put(WDDXTAGS.PARENT_DESIGN_ID,
				   (design.getParentLearningDesign()!=null?design.getParentLearningDesign().getLearningDesignId():WDDXTAGS.NUMERIC_NULL_VALUE_LONG));		
		
		designs.put(WDDXTAGS.WORKSPACE_FOLDER_ID,design.getWorkspaceFolder().getWorkspaceFolderId());
		
		designs.put(WDDXTAGS.DURATION,
				   (design.getDuration()!=null?design.getDuration():WDDXTAGS.NUMERIC_NULL_VALUE_LONG));
		
		Collection coll = design.getActivities();
		HashSet parentActivities = new HashSet();
		Iterator iter =null;		
		if(coll!=null){
			iter = coll.iterator();
			while (iter.hasNext()){
				Activity activity = (Activity)iter.next();
				if(activity.getParentActivity()==null){
					parentActivities.add(activity);
				}
			}
		}
		iter = parentActivities.iterator();
		Vector activities = new Vector();
		while(iter.hasNext()){
			Object object = iter.next();
			Hashtable output = buildActivityObject(object);
			activities.add(output);
		}
		designs.put(WDDXTAGS.ACTIVITIES,activities);
		
		coll = design.getTransitions();
		Vector transitions = new Vector();
		if(coll!=null){
			iter = coll.iterator();
			while (iter.hasNext()){	
				Transition trans  =(Transition) iter.next();
				Hashtable output = buildTransitionObject(trans);
				transitions.add(output);
			}
		}
		designs.put(WDDXTAGS.TRANSITIONS,transitions);
		
		return designs;
	}
	
	public static Hashtable getCutDownActivityDetails(Object objActivity){
		Hashtable activities = new Hashtable();	
		
		activities.put(WDDXTAGS.OBJECT_TYPE,"Activity");		
		Activity activity =(Activity)objActivity;		
		activities.put(WDDXTAGS.ACTIVITY_ID,activity.getActivityId());
		
		activities.put(WDDXTAGS.ACTIVITY_TYPE_ID,
				      (activity.getActivityCategoryID()!=null?activity.getActivityCategoryID():WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER));
		
		activities.put(WDDXTAGS.ACTIVITY_UIID,
					   (activity.getActivityUIID()!=null?activity.getActivityUIID():WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER));
		
		activities.put(WDDXTAGS.DESCRIPTION,
					  (activity.getDescription()!=null?activity.getDescription():WDDXTAGS.STRING_NULL_VALUE));
		
		activities.put(WDDXTAGS.TITLE,
					  (activity.getTitle()!=null?activity.getTitle():WDDXTAGS.STRING_NULL_VALUE));
		
		activities.put(WDDXTAGS.XCOORD,
		  			  (activity.getXcoord()!=null?activity.getXcoord():WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER));
		
		activities.put(WDDXTAGS.YCOORD,
					   (activity.getYcoord()!=null?activity.getYcoord():WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER));		
			
		return activities;
	}
	
	public static Hashtable getCutDownLearningDesignDetails(LearningDesign design){
		Hashtable designs = new Hashtable();
		
		designs.put(WDDXTAGS.OBJECT_TYPE,LearningDesign.DESIGN_OBJECT);
		
		designs.put(WDDXTAGS.LEARNING_DESIGN_ID,
				   (design.getLearningDesignId()!=null?design.getLearningDesignId():WDDXTAGS.NUMERIC_NULL_VALUE_LONG));
		
		designs.put(WDDXTAGS.DESCRIPTION,
				   (design.getDescription()!=null?design.getDescription():WDDXTAGS.STRING_NULL_VALUE));
		
		designs.put(WDDXTAGS.TITLE,
				   (design.getTitle()!=null?design.getTitle():WDDXTAGS.STRING_NULL_VALUE));
		
		designs.put(WDDXTAGS.FIRST_ACTIVITY_ID,
				   (design.getFirstActivity()!=null?design.getFirstActivity().getActivityId():WDDXTAGS.NUMERIC_NULL_VALUE_LONG));
		
		designs.put(WDDXTAGS.FIRST_ACTIVITY_UIID,
				   (design.getFirstActivity()!=null?design.getFirstActivity().getActivityUIID():WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER));
		
		designs.put(WDDXTAGS.MAX_ID,
				    (design.getMaxId()!=null?design.getMaxId():WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER));
		
		designs.put(WDDXTAGS.HELP_TEXT,
					(design.getHelpText()!=null?design.getHelpText():WDDXTAGS.STRING_NULL_VALUE));
		
		designs.put(WDDXTAGS.VERSION,
					(design.getVersion()!=null?design.getVersion():WDDXTAGS.STRING_NULL_VALUE));
		
		designs.put(WDDXTAGS.LICENCE_ID,
					(design.getLicense()!=null?design.getLicense().getLicenseID():WDDXTAGS.NUMERIC_NULL_VALUE_LONG));
		
		designs.put(WDDXTAGS.LICENSE_TEXT,
				   (design.getLicenseText()!=null?design.getLicenseText():WDDXTAGS.STRING_NULL_VALUE));
		return designs;
	}	
}
