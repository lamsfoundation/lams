/*
 * Created on Jan 14, 2005
 *
 *  
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.learningdesign.authoring;


import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.OptionsActivity;
import org.lamsfoundation.lams.learningdesign.ParallelActivity;
import org.lamsfoundation.lams.learningdesign.PermissionGateActivity;
import org.lamsfoundation.lams.learningdesign.ScheduleGateActivity;
import org.lamsfoundation.lams.learningdesign.SequenceActivity;
import org.lamsfoundation.lams.learningdesign.SynchGateActivity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.LearningDesignDAO;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dao.hibernate.UserDAO;

/**
 * @author Minhas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LDWDDXValueObjectStorer {
	
	static LDWDDXValueObjectStorer storer = null;
	
	/**
	 * @return LDWDDXValueObjectStorer Instance of LDWDDXValueObjectStorer
	 */
	public static LDWDDXValueObjectStorer getInstance()
	{
		return (storer==null? new LDWDDXValueObjectStorer():storer);
	}
	
	/**
	 * @param table Hashtable populated with data from the WDDX packet
	 * @return Long learning_design_id of the design updated/saved 
	 */
	public Long processLearningDesign(Hashtable table)throws Exception{
		Long learning_design_id = null;
		Long oldDesignID=null;
		LearningDesign learningDesign = null;
		learning_design_id = (Long) table.get(WDDXTAGS.LEARNING_DESIGN_ID);
		learningDesign = findLearningDesign(learning_design_id,false);
		
		boolean readOnly = ((Boolean)table.get(WDDXTAGS.READ_ONLY)).booleanValue();
		if(readOnly){
			throw new Exception("This design is locked, you cannot update it.\n" +
								"Please click on File | Save as... and rename it to save it");
		}			
		updateLearningDesign(table,learningDesign);		
		Long parentDesignID = (Long)table.get(WDDXTAGS.PARENT_DESIGN_ID);
		LearningDesign parent = findLearningDesign(parentDesignID,true);		
		learningDesign.setParentLearningDesign(parent);		
		return learningDesign.getLearningDesignId();
		
	}	
	
	/**
	 * @param designID The learning_design_id of the design to be fetched 
	 * @param parent True if you are searching for a design with parent_design_id = designID
	 * 				 False if you are just searching for design with the given designID (default)
	 * @return LearningDesign The LearningDesign corresponding to the given designID 
	 */
	public LearningDesign findLearningDesign(Long designID,boolean parent){		
		LearningDesignDAO ldDAO = new LearningDesignDAO();		
		LearningDesign learningDesign = ldDAO.getLearningDesignById(designID);
		
		/* If learningDesign is null, this means either its a request to save a new design
		* or the design with the given designID doesn't exist
		*/
		if(learningDesign==null && parent!=true){
			learningDesign = new LearningDesign();			
		}		
		return learningDesign;
	}
	
	/**
	 * @param table The Hashtable parsed from the WDDX string 
	 * @param design The LearningDesign object to be updated/populated
	 */
	public void updateLearningDesign(Hashtable table, LearningDesign design){
		design.setId((Integer)table.get(WDDXTAGS.ID));
		design.setTitle((String)table.get(WDDXTAGS.TITLE));
		design.setDescription((String)table.get(WDDXTAGS.DESCRIPTION));		
		design.setMaxId((Integer)table.get(WDDXTAGS.MAX_ID));
		design.setValidDesign((Boolean)table.get(WDDXTAGS.VALID_DESIGN));
		design.setReadOnly((Boolean)table.get(WDDXTAGS.READ_ONLY));
		design.setDateReadOnly((Date)table.get(WDDXTAGS.DATE_READ_ONLY));
		design.setHelpText((String)table.get(WDDXTAGS.HELP_TEXT));
		design.setLessonCopy((Boolean)table.get(WDDXTAGS.LESSON_COPY));
		design.setCreateDateTime((Date)table.get(WDDXTAGS.CREATION_DATE));
		design.setVersion((String)table.get(WDDXTAGS.VERSION));
		design.setOpenDateTime((Date)table.get(WDDXTAGS.OPEN_DATE));
		design.setCloseDateTime((Date)table.get(WDDXTAGS.CLOSE_DATE));
		
		int userId =((Long) table.get(WDDXTAGS.USER_ID)).intValue();
		UserDAO userDAO = new UserDAO();		
		User user = userDAO.getUserById(new Integer(userId));		
		design.setUser(user);		
		
		Vector activities =(Vector)table.get(WDDXTAGS.LIB_ACTIVITIES);
		Iterator iter = activities.iterator();
		Hashtable designActivities = new Hashtable();
		while(iter.hasNext()){
			Hashtable activity =(Hashtable)iter.next();
			
			//processActivity(activity);
		}	
		design.setActivities((Set)table.get(WDDXTAGS.LIB_ACTIVITIES));
		//design.setF
		//design.setFirstActivityId(design.calculateFirstActivityID());
		design.setTransitions((Set)table.get(WDDXTAGS.ACTIVITY_TRANSITIONS));	
	}
	
	private Activity processActivity(Hashtable table){
		String activityType = (String) table.get(WDDXTAGS.ACTIVITY_TYPE);
		
		Activity activity = null;
		
		if (activityType.equals("GroupingActivity"))
			activity = new GroupingActivity();
		else if (activityType.equals("ToolActivity"))
			activity = new ToolActivity();
		else if (activityType.equals("SynchGateActivity"))
			activity = new SynchGateActivity();
		else if (activityType.equals("ScheduleGateActivity"))
			activity = new ScheduleGateActivity();
		else if (activityType.equals("PermissionGateActivity"))
			activity = new PermissionGateActivity();
		else if (activityType.equals("ParallelActivity"))
			activity = new ParallelActivity();
		else if (activityType.equals("OptionsActivity"))
			activity = new OptionsActivity();
		else
			activity = new SequenceActivity();
		
		activity.setId((Integer)table.get(WDDXTAGS.ID));
		activity.setDescription((String)table.get(WDDXTAGS.DESCRIPTION));
		activity.setTitle((String)table.get(WDDXTAGS.TITLE));
		activity.setXcoord((Integer)table.get(WDDXTAGS.XCOORD));
		activity.setYcoord((Integer)table.get(WDDXTAGS.YCOORD));
		activity.setCreateDateTime((Date)table.get(WDDXTAGS.CREATION_DATE));
		activity.setOfflineInstructions((String)table.get(WDDXTAGS.OFFLINE_INSTRUCTIONS));
		activity.setLibraryActivityUiImage((String)table.get(WDDXTAGS.LIBRARY_IMAGE));
		
		return activity;
	}
	
	/**
	 * @return String Standarad ClientStatus message in case of an error in WDDX format
	 */
	public static String getStandardErrorStatusMessages(){
		StringBuffer message = new StringBuffer();
		message.append("<wddxPacket version='1.0'><header/><data>");
		message.append("<struct type='Lorg.lamsfoundation.lams.util.ClientStatusMessage;'>");
		message.append("<var name='message'><string>An Exception has occured while serializing the status message</string></var>");
		message.append("<var name='objectType'><string>StatusMessage</string></var>");
		message.append("<var name='responseData'><string></string></var>");
		message.append("<var name='statusType'><string>Error</string></var>");
		message.append("</struct></data></wddxPacket>");
		return message.toString();
	}
}
