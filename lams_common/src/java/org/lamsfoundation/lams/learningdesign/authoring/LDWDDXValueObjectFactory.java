/*
 * Created on Jan 12, 2005
 */
package org.lamsfoundation.lams.learningdesign.authoring;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.LearningLibrary;
import org.lamsfoundation.lams.learningdesign.OptionsActivity;
import org.lamsfoundation.lams.learningdesign.ScheduleGateActivity;
import org.lamsfoundation.lams.learningdesign.Transition;


/**
 * @author Minhas
 *  
 * TODO When sending a number as a value within a hashtable, we have to either put a proper 
 * value in the hashtable (ie not null) or leave out that tag all together. On the 
 * flash end, if we leave out the tag, it will return nothing when that tag is checked, 
 * but it won't cause an error. Therefore when sending ids, if the id doesn't exist 
 * (value is null), don't add that tag to the hashtable, and that tag will be left out
 * of the packet.
 */
public class LDWDDXValueObjectFactory {
	
	static LDWDDXValueObjectFactory factory = null;
	
	public static LDWDDXValueObjectFactory getInstance()
	{
		return (factory==null? new LDWDDXValueObjectFactory():factory);
	}
	
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
	
	private Hashtable buildLearningLibraryObject(LearningLibrary library){
		Hashtable libraries = new Hashtable();
		
		libraries.put(WDDXTAGS.LEARNING_LIBRARY_ID,HashtableUtils.getIdLong(library.getLearningLibraryId()));
		libraries.put(WDDXTAGS.DESCRIPTION,HashtableUtils.getValue(library.getDescription()));
		libraries.put(WDDXTAGS.TITLE,HashtableUtils.getValue(library.getTitle()));
		libraries.put(WDDXTAGS.CREATION_DATE,HashtableUtils.getIdDate(library.getCreateDateTime()));
		
		Collection coll = library.getActivities();
		Vector activities = new Vector();
		if(coll!=null){
			Iterator iter = coll.iterator();
			while (iter.hasNext()){	
				Object object = iter.next();
				Hashtable output = buildActivityObject(object);
				activities.add(output);
			}			
		}
		libraries.put(WDDXTAGS.LIB_ACTIVITIES,activities);
		return libraries;
	}
	private void processActivityType(Hashtable activities, Object activity){
		String activityType = null;
		String className = activity.getClass().getName();
		
		if (className.equals("org.lamsfoundation.lams.learningdesign.GroupingActivity"))
			activityType="GroupingActivity";
		else if (className.equals("org.lamsfoundation.lams.learningdesign.ToolActivity"))
			activityType="ToolActivity";
		else if (className.equals("org.lamsfoundation.lams.learningdesign.SynchGateActivity"))
			activityType="SynchGateActivity";
		else if (className.equals("org.lamsfoundation.lams.learningdesign.ScheduleGateActivity")){
			activityType="ScheduleGateActivity";
			buildScheduleGateActivityObject(activities,activity);
		}
		else if (className.equals("org.lamsfoundation.lams.learningdesign.PermissionGateActivity"))
			activityType="PermissionGateActivity";
		else if (className.equals("org.lamsfoundation.lams.learningdesign.ParallelActivity"))
			activityType="ParallelActivity";
		else if (className.equals("org.lamsfoundation.lams.learningdesign.OptionsActivity")){
			activityType="OptionsActivity";
			buildOptionsActivityObject(activities,activity);
		}
		else
			activityType="SequenceActivity";
			
		activities.put(WDDXTAGS.ACTIVITY_TYPE,activityType);		
	}
	
	private Hashtable buildActivityObject(Object objActivity){
		Hashtable activities = new Hashtable();	
		
		activities.put(WDDXTAGS.OBJECT_TYPE,"Activity");
		
		processActivityType(activities,objActivity);
		
		Activity activity =(Activity)objActivity;		
		activities.put(WDDXTAGS.ACTIVITY_ID,HashtableUtils.getIdLong(activity.getActivityId()));
		activities.put(WDDXTAGS.ID,HashtableUtils.getIdInteger(activity.getId()));
		activities.put(WDDXTAGS.DESCRIPTION, HashtableUtils.getValue(activity.getDescription()));
		activities.put(WDDXTAGS.TITLE, HashtableUtils.getValue(activity.getTitle()));
		activities.put(WDDXTAGS.XCOORD,HashtableUtils.getIdInteger(activity.getXcoord()));
		activities.put(WDDXTAGS.YCOORD,HashtableUtils.getIdInteger(activity.getYcoord()));
		activities.put(WDDXTAGS.CREATION_DATE,HashtableUtils.getIdDate(activity.getCreateDateTime()));
		activities.put(WDDXTAGS.OFFLINE_INSTRUCTIONS, HashtableUtils.getValue(activity.getOfflineInstructions()));
		activities.put(WDDXTAGS.LIBRARY_IMAGE,HashtableUtils.getValue(activity.getLibraryActivityUiImage()));
		
		if(activity.getParentActivity()!=null)
			activities.put(WDDXTAGS.PARENT_ACTIVITY_ID,activity.getParentActivity().getActivityId());
		
		activities.put(WDDXTAGS.ACTIVITY_TYPE_ID,HashtableUtils.getIdInteger(activity.getActivityTypeId()));
		
		if(activity.getGrouping()!=null)
			activities.put(WDDXTAGS.GROUPING_ID,activity.getGrouping().getGroupingId());
		
		activities.put(WDDXTAGS.ORDER_ID,HashtableUtils.getIdInteger(activity.getOrderId()));
		activities.put(WDDXTAGS.DEFINE_LATER,HashtableUtils.getBoolean(activity.getDefineLater()));
		
		if(activity.getLearningDesign()!=null)
			activities.put(WDDXTAGS.LEARNING_DESIGN_ID,activity.getLearningDesign().getLearningDesignId());
		
		if(activity.getLearningLibrary()!=null)
			activities.put(WDDXTAGS.LEARNING_LIBRARY_ID,activity.getLearningLibrary().getLearningLibraryId());
		
				
		Hashtable transitions = new Hashtable();
		
		Collection coll = activity.getTransitionsByFromActivityId();
		Vector progress = new Vector();
		if(coll!=null){
			Iterator iter = coll.iterator();
			while (iter.hasNext()){
				Transition transition =(Transition) iter.next();
				Hashtable output = buildTransitionObject(transition);
				progress.add(output);
			}
		}
		transitions.put(WDDXTAGS.TRANSITION_FROM,progress);		
		
		coll = activity.getTransitionsByToActivityId();
		progress = new Vector();
		if(coll!=null){
			Iterator iter = coll.iterator();
			while (iter.hasNext()){
				Transition transition =(Transition) iter.next();
				Hashtable output = buildTransitionObject(transition);
				progress.add(output);
			}
		}
		transitions.put(WDDXTAGS.TRANSITION_TO,progress);
		
		activities.put(WDDXTAGS.ACTIVITY_TRANSITIONS, transitions);		
		return activities;
	}
	private void buildOptionsActivityObject(Hashtable activities, Object activity){		
		OptionsActivity optActivity =(OptionsActivity)activity;
		activities.put(WDDXTAGS.MAX_OPTIONS,HashtableUtils.getIdInteger(optActivity.getMaxNumberOfOptions()));
		activities.put(WDDXTAGS.MIN_OPTIONS,HashtableUtils.getIdInteger(optActivity.getMinNumberOfOptions()));
	}
	private void buildScheduleGateActivityObject(Hashtable activities, Object activity){
		ScheduleGateActivity schActivity =(ScheduleGateActivity)activity;
		activities.put(WDDXTAGS.GATE_END_DATE,HashtableUtils.getIdDate(schActivity.getGateEndDateTime()));
		activities.put(WDDXTAGS.GATE_START_DATE,HashtableUtils.getIdDate(schActivity.getGateStartDateTime()));		
	}
	private Hashtable buildTransitionObject(Transition trans){
		Hashtable transitions = new Hashtable();
		
		transitions.put(WDDXTAGS.TRANSITION_ID,HashtableUtils.getIdLong(trans.getTransitionId()));
		transitions.put(WDDXTAGS.ID,HashtableUtils.getIdInteger(trans.getId()));
		transitions.put(WDDXTAGS.DESCRIPTION,HashtableUtils.getValue(trans.getDescription()));
		transitions.put(WDDXTAGS.TITLE,HashtableUtils.getValue(trans.getTitle()));
		
		Long toActivityID = (trans.getActivityByToActivityId()==null? new Long(-1) : trans.getActivityByToActivityId().getActivityId());
		transitions.put(WDDXTAGS.TRANSITION_TO, toActivityID);
		
		Long fromActivityID = (trans.getActivityByFromActivityId()==null? new Long(-1): trans.getActivityByFromActivityId().getActivityId());
		transitions.put(WDDXTAGS.TRANSITION_FROM, fromActivityID);
		
		Long learningDesignID = (trans.getLearningDesign()==null? new Long(-1): trans.getLearningDesign().getLearningDesignId());
		transitions.put(WDDXTAGS.LEARNING_DESIGN_ID, learningDesignID);
		
		transitions.put(WDDXTAGS.CREATION_DATE,HashtableUtils.getIdDate(trans.getCreateDateTime()));
		
		return transitions;
	}
	
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
	public Hashtable buildLearningDesignObject(LearningDesign design){
		Hashtable designs = new Hashtable();
		
		designs.put(WDDXTAGS.OBJECT_TYPE,LearningDesign.DESIGN_OBJECT);
		designs.put(WDDXTAGS.LEARNING_DESIGN_ID,HashtableUtils.getIdLong(design.getLearningDesignId()));
		designs.put(WDDXTAGS.ID, HashtableUtils.getIdInteger(design.getId()));
		designs.put(WDDXTAGS.DESCRIPTION,HashtableUtils.getValue(design.getDescription()));
		designs.put(WDDXTAGS.TITLE,HashtableUtils.getValue(design.getTitle()));
		designs.put(WDDXTAGS.VALID_DESIGN,HashtableUtils.getBoolean(design.getValidDesign()));
		designs.put(WDDXTAGS.READ_ONLY,HashtableUtils.getBoolean(design.getReadOnly()));
		designs.put(WDDXTAGS.MAX_ID,HashtableUtils.getIdInteger(design.getMaxId()));
		designs.put(WDDXTAGS.DATE_READ_ONLY,HashtableUtils.getIdDate(design.getDateReadOnly()));
		designs.put(WDDXTAGS.READ_ACCESS,HashtableUtils.getIdLong(design.getReadAccess()));
		designs.put(WDDXTAGS.WRITE_ACCESS,HashtableUtils.getIdLong(design.getWriteAccess()));
		designs.put(WDDXTAGS.OPEN_DATE,HashtableUtils.getIdDate(design.getOpenDateTime()));		
		designs.put(WDDXTAGS.CLOSE_DATE,HashtableUtils.getIdDate(design.getCloseDateTime()));
		designs.put(WDDXTAGS.HELP_TEXT,HashtableUtils.getValue(design.getHelpText()));
		designs.put(WDDXTAGS.LESSON_COPY,HashtableUtils.getBoolean(design.getLessonCopy()));
		designs.put(WDDXTAGS.CREATION_DATE,HashtableUtils.getIdDate(design.getCreateDateTime()));
		designs.put(WDDXTAGS.VERSION,HashtableUtils.getValue(design.getVersion()));
		
		designs.put(WDDXTAGS.FIRST_ACTIVITY_ID,HashtableUtils.getIdLong(design.getFirstActivity().getActivityId()));
		
		if(design.getUser()!=null)
			designs.put(WDDXTAGS.USER_ID,design.getUser().getUserId());
				
		if(design.getParentLearningDesign()!=null)
			designs.put(WDDXTAGS.PARENT_DESIGN_ID,design.getParentLearningDesign().getLearningDesignId());
		
		Collection coll = design.getActivities();
		Vector activities = new Vector();
		if(coll!=null){
			Iterator iter = coll.iterator();
			while (iter.hasNext()){	
				Object object = iter.next();
				Hashtable output = buildActivityObject(object);
				activities.add(output);
			}			
		}
		designs.put(WDDXTAGS.LIB_ACTIVITIES,activities);
		return designs;
	}
		

}
