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
package org.lamsfoundation.lams.authoring;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.BranchingActivity;
import org.lamsfoundation.lams.learningdesign.ChosenGrouping;
import org.lamsfoundation.lams.learningdesign.ComplexActivity;
import org.lamsfoundation.lams.learningdesign.GateActivity;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.GroupBranchActivityEntry;
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
import org.lamsfoundation.lams.learningdesign.SystemGateActivity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.Transition;
import org.lamsfoundation.lams.learningdesign.dao.IActivityDAO;
import org.lamsfoundation.lams.learningdesign.dao.IGroupDAO;
import org.lamsfoundation.lams.learningdesign.dao.IGroupingDAO;
import org.lamsfoundation.lams.learningdesign.dao.ILearningDesignDAO;
import org.lamsfoundation.lams.learningdesign.dao.ILearningLibraryDAO;
import org.lamsfoundation.lams.learningdesign.dao.ILicenseDAO;
import org.lamsfoundation.lams.learningdesign.dao.ITransitionDAO;
import org.lamsfoundation.lams.lesson.LessonClass;
import org.lamsfoundation.lams.tool.SystemTool;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.dao.ISystemToolDAO;
import org.lamsfoundation.lams.tool.dao.IToolDAO;
import org.lamsfoundation.lams.tool.dao.IToolSessionDAO;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.wddx.WDDXProcessor;
import org.lamsfoundation.lams.util.wddx.WDDXProcessorConversionException;
import org.lamsfoundation.lams.util.wddx.WDDXTAGS;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * @author Manpreet Minhas
 * @author Mailing Truong
 * 
 * This is a utility class for extracting the
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
 * Object extractor has member data, so it should not be used as a singleton.
 * 
 */
public class ObjectExtractor implements IObjectExtractor {
	
	protected IBaseDAO baseDAO = null;
	protected ILearningDesignDAO learningDesignDAO = null;
	protected IActivityDAO activityDAO =null;
	protected ITransitionDAO transitionDAO =null;
	protected ILearningLibraryDAO learningLibraryDAO = null;
	protected ILicenseDAO licenseDAO = null;
	protected IGroupingDAO groupingDAO = null;
	protected IToolDAO toolDAO = null;
	protected ISystemToolDAO systemToolDAO = null;
	protected IGroupDAO groupDAO = null;
	protected IToolSessionDAO toolSessionDAO = null;

	private Integer mode = null;
	
	/* The newActivityMap is a local copy of all the current activities. This will include
	 * the "top level" activities and subactivities. It is used to "crossreference" activities
	 * as we go, without having to repull them from the database. The keys are the UIIDs
	 * of the activities, not the IDs. It is important that the values in this map are the Activity
	 * objects related to the Hibernate session as they are updated by the parseTransitions code.
	 */ 
	protected HashMap<Integer,Activity> newActivityMap = new HashMap<Integer,Activity>();
	/* 
	 * Record the tool sessions and activities as we go for edit on fly. This is needed in case we delete any.
	 * Cannot get them at the end as Hibernate tries to store the activities before getting the 
	 * tool sessions, and this fails due to a foriegn key error. Its the foriegn key error
	 * that we are trying to avoid!
	 */
	protected HashMap<Integer, List<ToolSession>> toolSessionMap = new HashMap<Integer,List<ToolSession>>(); // Activity UIID -> ToolSession
	protected HashMap<Integer, Activity> oldActivityMap = new HashMap<Integer,Activity>(); // Activity UIID -> Activity
	/* cache of groupings - too hard to get them from the db */
	protected HashMap<Integer,Grouping> groupings = new HashMap<Integer,Grouping>();
	protected HashMap<Integer,Group> groups = new HashMap<Integer,Group>();
	protected HashMap<Integer,GroupBranchActivityEntry> branchEntries = new HashMap<Integer,GroupBranchActivityEntry>();
	protected HashMap<Integer,SequenceActivity> firstChildToSequenceMap = new HashMap<Integer,SequenceActivity>();
	/* can't delete as we go as they are linked to other items - keep a list and delete at the end. */
	protected Set<Grouping> groupingsToDelete = new HashSet<Grouping>();
	protected LearningDesign learningDesign = null;
	protected Date modificationDate = null;
	
	protected Logger log = Logger.getLogger(ObjectExtractor.class);	

	/** Constructor to be used if Spring method injection is used */
	public ObjectExtractor() {		
		modificationDate = new Date();
	}

	/** Constructor to be used if Spring method injection is not used */
	public ObjectExtractor(IBaseDAO baseDAO,
			ILearningDesignDAO learningDesignDAO, IActivityDAO activityDAO,
			ILearningLibraryDAO learningLibraryDAO, ILicenseDAO licenseDAO,
			IGroupingDAO groupingDAO, IToolDAO toolDAO, ISystemToolDAO systemToolDAO,
			IGroupDAO groupDAO, ITransitionDAO transitionDAO, IToolSessionDAO toolSessionDAO) {		
		this.baseDAO = baseDAO;
		this.learningDesignDAO = learningDesignDAO;
		this.activityDAO = activityDAO;
		this.learningLibraryDAO = learningLibraryDAO;
		this.licenseDAO = licenseDAO;
		this.groupingDAO = groupingDAO;
		this.toolDAO = toolDAO;
		this.systemToolDAO = systemToolDAO;
		this.groupDAO = groupDAO;
		this.transitionDAO = transitionDAO;
		this.toolSessionDAO = toolSessionDAO;
		modificationDate = new Date();
	}
	
	/** Spring injection methods */
	public IActivityDAO getActivityDAO() {
		return activityDAO;
	}

	public void setActivityDAO(IActivityDAO activityDAO) {
		this.activityDAO = activityDAO;
	}

	public IGroupDAO getGroupDAO() {
		return groupDAO;
	}

	public void setGroupDAO(IGroupDAO groupDAO) {
		this.groupDAO = groupDAO;
	}

	public IGroupingDAO getGroupingDAO() {
		return groupingDAO;
	}

	public void setGroupingDAO(IGroupingDAO groupingDAO) {
		this.groupingDAO = groupingDAO;
	}

	public ILearningDesignDAO getLearningDesignDAO() {
		return learningDesignDAO;
	}

	public void setLearningDesignDAO(ILearningDesignDAO learningDesignDAO) {
		this.learningDesignDAO = learningDesignDAO;
	}

	public ILearningLibraryDAO getLearningLibraryDAO() {
		return learningLibraryDAO;
	}

	public void setLearningLibraryDAO(ILearningLibraryDAO learningLibraryDAO) {
		this.learningLibraryDAO = learningLibraryDAO;
	}

	public ILicenseDAO getLicenseDAO() {
		return licenseDAO;
	}

	public void setLicenseDAO(ILicenseDAO licenseDAO) {
		this.licenseDAO = licenseDAO;
	}

	public IToolSessionDAO getToolSessionDAODAO() {
		return toolSessionDAO;
	}

	public void setToolSessionDAO(IToolSessionDAO toolSessionDAO) {
		this.toolSessionDAO = toolSessionDAO;
	}

	public HashMap<Integer, Activity> getNewActivityMap() {
		return newActivityMap;
	}

	public void setNewActivityMap(HashMap<Integer, Activity> newActivityMap) {
		this.newActivityMap = newActivityMap;
	}

	public IToolDAO getToolDAO() {
		return toolDAO;
	}

	public void setToolDAO(IToolDAO toolDAO) {
		this.toolDAO = toolDAO;
	}

	
	public ISystemToolDAO getSystemToolDAO() {
		return systemToolDAO;
	}

	public void setSystemToolDAO(ISystemToolDAO systemToolDAO) {
		this.systemToolDAO = systemToolDAO;
	}

	public ITransitionDAO getTransitionDAO() {
		return transitionDAO;
	}

	public void setTransitionDAO(ITransitionDAO transitionDAO) {
		this.transitionDAO = transitionDAO;
	}

	public IBaseDAO getBaseDAO() {
		return baseDAO;
	}

	public void setBaseDAO(IBaseDAO baseDAO) {
		this.baseDAO = baseDAO;
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.authoring.IObjectExtractor#extractSaveLearningDesign(java.util.Hashtable)
	 */
	public LearningDesign extractSaveLearningDesign(Hashtable table) throws WDDXProcessorConversionException, ObjectExtractorException {

		learningDesign = null;
	
		Long learningDesignId = WDDXProcessor.convertToLong(table, "learningDesignID");
		//if the learningDesignID is not null, load the existing LearningDesign object from the database, otherwise create a new one.
		learningDesign = learningDesignId!= null ? learningDesignDAO.getLearningDesignById(learningDesignId) : new LearningDesign();
	
		// Check the copy type. Can only update it if it is COPY_TYPE_NONE (ie authoring copy)
		Integer copyTypeID = WDDXProcessor.convertToInteger(table,WDDXTAGS.COPY_TYPE);
		if ( copyTypeID == null ) {
			copyTypeID = LearningDesign.COPY_TYPE_NONE;
		}
		if ( learningDesign != null && learningDesign.getCopyTypeID() != null && 
				! learningDesign.getCopyTypeID().equals(copyTypeID) && !learningDesign.getEditOverrideLock()) {
			throw new ObjectExtractorException("Unable to save learning design.  Cannot change copy type on existing design.");
		}
		if ( ! copyTypeID.equals(LearningDesign.COPY_TYPE_NONE) && !learningDesign.getEditOverrideLock()) {
			throw new ObjectExtractorException("Unable to save learning design.  Learning design is read-only");
		}
		learningDesign.setCopyTypeID(copyTypeID);

		// Pull out all the existing groups. there isn't an easy way to pull them out of the db requires an outer join across
		// three objects (learning design -> grouping activity -> grouping) so put both the existing ones and the new ones
		// here for reference later.
		initialiseGroupings();
		
		// get a mapping of all the existing activities and their tool sessions, in case we need to delete some tool sessions later.
		initialiseToolSessionMap(learningDesign);
		
		//get the core learning design stuff - default to invalid
		learningDesign.setValidDesign(Boolean.FALSE);
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
		if (keyExists(table, WDDXTAGS.EDIT_OVERRIDE_LOCK))
			learningDesign.setEditOverrideLock(WDDXProcessor.convertToBoolean(table, WDDXTAGS.EDIT_OVERRIDE_LOCK));
		if (keyExists(table, WDDXTAGS.DATE_READ_ONLY)) 	
		    learningDesign.setDateReadOnly(WDDXProcessor.convertToDate(table, WDDXTAGS.DATE_READ_ONLY));
		if (keyExists(table, WDDXTAGS.OFFLINE_INSTRUCTIONS))	
		    learningDesign.setOfflineInstructions(WDDXProcessor.convertToString(table,WDDXTAGS.OFFLINE_INSTRUCTIONS));
		if (keyExists(table, WDDXTAGS.ONLINE_INSTRUCTIONS))	
		    learningDesign.setOnlineInstructions(WDDXProcessor.convertToString(table,WDDXTAGS.ONLINE_INSTRUCTIONS));
		if (keyExists(table, WDDXTAGS.HELP_TEXT))	
		    learningDesign.setHelpText(WDDXProcessor.convertToString(table,WDDXTAGS.HELP_TEXT));
//		if (keyExists(table, WDDXTAGS.VERSION))	
//		    learningDesign.setVersion(WDDXProcessor.convertToString(table,WDDXTAGS.VERSION));
		//don't receive version from flash anymore(it was hardcode). Get it from lams configuration database.
		learningDesign.setVersion(Configuration.get(ConfigurationKeys.VERSION));
		
		if (keyExists(table, WDDXTAGS.DURATION))	
		    learningDesign.setDuration(WDDXProcessor.convertToLong(table,WDDXTAGS.DURATION));
		
		if (keyExists(table, WDDXTAGS.DURATION))
		    learningDesign.setDuration(WDDXProcessor.convertToLong(table,WDDXTAGS.DURATION));
		
		if (keyExists(table, WDDXTAGS.CONTENT_FOLDER_ID))
			learningDesign.setContentFolderID(WDDXProcessor.convertToString(table, WDDXTAGS.CONTENT_FOLDER_ID));
		
		if (keyExists(table, WDDXTAGS.SAVE_MODE))
			mode = WDDXProcessor.convertToInteger(table, WDDXTAGS.SAVE_MODE);

		// Set creation date and modification date based on the server timezone, not the client.
		if ( learningDesign.getCreateDateTime() == null )
			learningDesign.setCreateDateTime(modificationDate);
	    learningDesign.setLastModifiedDateTime(modificationDate);

		Integer userId = getUserId();

		if( userId != null ) {
			User user = (User)baseDAO.find(User.class,userId);
			if(user!=null) {
				learningDesign.setUser(user);
			} else {
				throw new ObjectExtractorException("userID missing");
			}
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
				WorkspaceFolder workspaceFolder = (WorkspaceFolder)baseDAO.find(WorkspaceFolder.class,workspaceFolderID);
				learningDesign.setWorkspaceFolder(workspaceFolder);			
			}
			else
			{
			    learningDesign.setWorkspaceFolder(null);
			}
		}

		if (keyExists(table, WDDXTAGS.ORIGINAL_DESIGN_ID))
		{
			Long parentLearningDesignID = WDDXProcessor.convertToLong(table,WDDXTAGS.ORIGINAL_DESIGN_ID);
			if( parentLearningDesignID != null ){
				LearningDesign parent = learningDesignDAO.getLearningDesignById(parentLearningDesignID);
				learningDesign.setOriginalLearningDesign(parent);
			}
			else
			    learningDesign.setOriginalLearningDesign(null);
		}
	
		
		learningDesignDAO.insertOrUpdate(learningDesign);
	
		// now process the "parts" of the learning design
		parseGroupings((Vector)table.get(WDDXTAGS.GROUPINGS));
		parseActivities((Vector)table.get(WDDXTAGS.ACTIVITIES));
		parseActivitiesToMatchUpParentActivityByParentUIID((Vector)table.get(WDDXTAGS.ACTIVITIES));
		parseTransitions((Vector)table.get(WDDXTAGS.TRANSITIONS));
		parseBranchMappings((Vector)table.get(WDDXTAGS.BRANCH_MAPPINGS));
		progressFirstActivityWithinSequence();
		
		learningDesign.setFirstActivity(learningDesign.calculateFirstActivity());
		learningDesignDAO.insertOrUpdate(learningDesign);
		deleteUnwantedGroupings();
		deleteUnwantedToolSessions(learningDesign);
		
		return learningDesign;	
		}

	/** Link SequenceActivities up with their firstActivity entries 
	 * @throws WDDXProcessorConversionException */
	private void progressFirstActivityWithinSequence() throws WDDXProcessorConversionException {
		
		if ( firstChildToSequenceMap.size() > 0 ) {
			for ( Integer firstChildUIID : firstChildToSequenceMap.keySet() ) {
				SequenceActivity sequence = firstChildToSequenceMap.get(firstChildUIID);
				Activity childActivity = newActivityMap.get(firstChildUIID);
				if ( childActivity == null )  {
					String msg = "Unable to find first child activity ("+firstChildUIID
						+") for the sequence activity ("+sequence
						+") referred to in First Child to Sequence map.";
			    	throw new WDDXProcessorConversionException(msg);
				} else {
					sequence.setFirstActivity(childActivity);
				}
			}
			
		}
	}


	/** 
	 * Initialise the map of groupings with those in the db from a previous save.
	 * This must be called as soon as the learning design is read from the db and before it is changed.
	 */
	private void initialiseGroupings() {
		List dbGroupings = groupingDAO.getGroupingsByLearningDesign(learningDesign.getLearningDesignId());
		Iterator iter = dbGroupings.iterator();
		while (iter.hasNext()) {
			Grouping grouping = (Grouping) iter.next();
			groupings.put(grouping.getGroupingUIID(), grouping);
		}
	}

	/** Initialise the map of tool sessions already in the database. Used to work out what will be deleted
	 * by Hibernate later - useful to clean up any unwanted tool sessions for edit on the fly.
	 */
	@SuppressWarnings("unchecked")
	private void initialiseToolSessionMap(LearningDesign learningDesign) {
		if (learningDesign.getEditOverrideLock() && learningDesign.getEditOverrideUser() != null) {
			Iterator iter = learningDesign.getActivities().iterator();
			while ( iter.hasNext() ) {
				Activity activity = (Activity) iter.next();
				oldActivityMap.put(activity.getActivityUIID(), activity);
				List<ToolSession> toolSessions = (List<ToolSession>) toolSessionDAO.getToolSessionByActivity(activity);
				if ( toolSessions != null && toolSessions.size() > 0 )
					toolSessionMap.put(activity.getActivityUIID(),toolSessions);
			}
		}
	}

	/** Delete the old unneeded groupings. Won't be done via a cascade */
	private void deleteUnwantedGroupings() {
		for ( Grouping grouping: groupingsToDelete) {
			groupingDAO.delete(grouping);	
		}
	}

	/** Delete the old tool session. Won't be done via Hibernate cascades as we only want to do it 
	 * for edit on fly. The progress engine pre-generates the tool sessions for class level activities,
	 * so if we edit the design, we need to delete the tool sessions. If we encounter evidence that this
	 * is a grouped activity - either more than one tool session exists or the activity is grouped, then abort. */
	private void deleteUnwantedToolSessions(LearningDesign learningDesign) throws ObjectExtractorException {
		if (learningDesign.getEditOverrideLock() && learningDesign.getEditOverrideUser() != null) {

			for ( Integer uiid : toolSessionMap.keySet() ) {
				if ( ! newActivityMap.containsKey(uiid) ) {
					List toolSessions = toolSessionMap.get(uiid);
					if ( toolSessions != null ) {
						
						Activity activity = oldActivityMap.get(uiid);
						if ( toolSessions.size() > 1 ) {
							throw new ObjectExtractorException("More than one tool session exists for activity "+activity.getTitle()+" ("+uiid+") but this shouldn't be possible. Cannot delete this tool session so editing is not allowed!");
						} else if (toolSessions.size() == 1) { 

							ToolSession toolSession = (ToolSession) toolSessions.get(0);
							if ( activity.isGroupingActivity() ) {
								throw new ObjectExtractorException("Activity "+activity.getTitle()+" ("+activity.getActivityUIID()+") has a tool session but it is grouped. Cannot delete this tool session so editing is not allowed!");
							}
						
							// all okay, do ahead and delete the tool session
							if ( log.isDebugEnabled())
								log.debug("Removing tool session for activity "+activity.getTitle()+" ("+activity.getActivityUIID()+")");
							
							toolSessionDAO.removeToolSession(toolSession);
						}
					}
				}
			}

		}
	}

	/**
	 * Parses the groupings array sent from the WDDX packet. It will create
	 * the groupings object (ChosenGrouping, RandomGrouping) so that when the
	 * GroupingActivity is processed, it can link to the grouping object
	 * that has been created by this method.
	 * @param groupingsList
	 * @throws WDDXProcessorConversionException
	 */
	
	private void parseGroupings(List groupingsList) 
		throws WDDXProcessorConversionException 	
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
	    			groupingDAO.insertOrUpdate(grouping);	
	    			groupings.put(grouping.getGroupingUIID(),grouping);
	            }
	        }
	        
	    }
	}
	

	public Grouping extractGroupingObject(Hashtable groupingDetails) throws WDDXProcessorConversionException{
		
	    Integer groupingUUID = WDDXProcessor.convertToInteger(groupingDetails, WDDXTAGS.GROUPING_UIID);	
	    Integer groupingTypeID=WDDXProcessor.convertToInteger(groupingDetails,WDDXTAGS.GROUPING_TYPE_ID);
	    if (groupingTypeID== null) { 
			throw new WDDXProcessorConversionException("groupingTypeID is missing");
		}

	    Grouping grouping = groupings.get(groupingUUID);
	    // check that the grouping type is still okay - if not get rid of the old hibernate object.
	    if ( grouping != null && ! grouping.getGroupingTypeId().equals(groupingTypeID) ) {
	    	groupings.remove(grouping.getGroupingUIID());
	    	groupingsToDelete.add(grouping);
			grouping = null;
	    }
	
	    if (grouping == null) {
	        Object object = Grouping.getGroupingInstance(groupingTypeID);
			grouping = (Grouping)object;				
			
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

		List groupsList = (Vector)groupingDetails.get(WDDXTAGS.GROUPS);
		if ( groupsList != null && groupsList.size() > 0 ) {
			Iterator iter = groupsList.iterator();
			while ( iter.hasNext() ) {
				Hashtable groupDetails = (Hashtable)iter.next();
				Group group = extractGroupObject(groupDetails, grouping);
				groups.put(group.getGroupUIID(), group);
			}
		}
		return grouping;
	}
	
	@SuppressWarnings("unchecked")
	private Group extractGroupObject(Hashtable groupDetails, Grouping grouping) throws WDDXProcessorConversionException {

		Group group = null;
		Integer groupUIID = WDDXProcessor.convertToInteger(groupDetails,WDDXTAGS.GROUP_UIID);
	    if ( groupUIID == null ) { 
	    	throw new WDDXProcessorConversionException("Group is missing its UUID. Group "+groupDetails+" grouping "+grouping);
	    }
		Long groupID = WDDXProcessor.convertToLong(groupDetails,WDDXTAGS.GROUP_ID);
		
		// Does it exist already? If the group was created at runtime, there will be an ID but no IU ID field.
		// If it was created in authoring, will have a UI ID and may or may not have an ID.
		// So try to match up on UI ID first, failing that match on ID. Then the worst case, which is the group
		// is created at runtime but then modified in authoring (and has has a new UI ID added) is handled.
		if ( grouping.getGroups() != null && grouping.getGroups().size() > 0  ) {
			Group uiid_match = null;
			Group id_match = null;
			Iterator iter = grouping.getGroups().iterator();
			while (uiid_match == null && iter.hasNext()) {
				Group possibleGroup = (Group) iter.next();
				if ( groupUIID.equals(possibleGroup.getGroupUIID()) ) {
					uiid_match = possibleGroup;
				}
				if ( groupID != null && groupID.equals(possibleGroup.getGroupId()) ) {
					id_match = possibleGroup;
				}
			}
			group = uiid_match != null ? uiid_match : id_match;
		}

		if ( group == null ) {
			group = new Group();
			grouping.getGroups().add(group);
		}
		
		group.setGroupName(WDDXProcessor.convertToString(groupDetails,WDDXTAGS.GROUP_NAME));
		group.setGrouping(grouping);
		group.setGroupUIID(groupUIID);
		
		if ( keyExists(groupDetails,WDDXTAGS.ORDER_ID) )
			group.setOrderId(WDDXProcessor.convertToInteger(groupDetails,WDDXTAGS.ORDER_ID));
		else
			group.setOrderId(0);
		
		return group;
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
	
	/**
	 * Parses the list of activities sent from the WDDX packet. The current activities that 
	 * belong to this learning design will be compared with the new list of activities. Any new activities will
	 * be added to the database, existing activities will be updated, and any activities that are not
	 * present in the list of activities from the wddx packet (but appear in the list of current activities) 
	 * are deleted.
	 * 
	 * @param activitiesList The list of activities from the WDDX packet.
	 * @throws WDDXProcessorConversionException
	 * @throws ObjectExtractorException
	 */
	@SuppressWarnings("unchecked")
	private void parseActivities(List activitiesList) 
			throws WDDXProcessorConversionException, ObjectExtractorException {
		
		if(activitiesList!=null){
			Iterator iterator = activitiesList.iterator();
			while(iterator.hasNext()){
				Hashtable activityDetails = (Hashtable)iterator.next();
				Activity activity = extractActivityObject(activityDetails);	
				activityDAO.insertOrUpdate(activity);
				newActivityMap.put(activity.getActivityUIID(), activity); 
			}
		}

		// clear the transitions.
		// clear the old set and reset up the activities set. Done this way to keep the Hibernate cascading happy. 
		// this means we don't have to manually remove the transition object.
	    // Note: This will leave orphan content in the tool tables. It can be removed by the tool content cleaning job, 
        // which may be run from the admin screen or via a cron job. 
		
		learningDesign.getActivities().clear();
		learningDesign.getActivities().addAll(newActivityMap.values());
		
		//TODO: Need to double check that the toolID/toolContentID combinations match entries in lams_tool_content table, or put FK on table.
		learningDesignDAO.insertOrUpdate(learningDesign);
	}
	
	/**
	 * Because the activities list was processed before by the method parseActivities, it is assumed that 
	 * all activities have already been saved into the database. Because the parent activity is already
	 * created and saved, this method will go through the activity list and will match up the parentActivityID 
	 * based on the parentUIID.
	 * 
	 * @param activitiesList
	 * @param learningDesign
	 * @throws WDDXProcessorConversionException
	 * @throws ObjectExtractorException
	 */
	private void parseActivitiesToMatchUpParentActivityByParentUIID(List activitiesList) throws WDDXProcessorConversionException, ObjectExtractorException
	{
		if (activitiesList != null)
		{
			Iterator iterator = activitiesList.iterator();
			while(iterator.hasNext()){
				
				Hashtable activityDetails = (Hashtable)iterator.next();
				
				Integer activityUUID = WDDXProcessor.convertToInteger(activityDetails,WDDXTAGS.ACTIVITY_UIID);
				Activity existingActivity = newActivityMap.get(activityUUID); 
				//match up id to parent based on UIID
				if (keyExists(activityDetails, WDDXTAGS.PARENT_UIID))
			    {
					Integer parentUIID = WDDXProcessor.convertToInteger(activityDetails, WDDXTAGS.PARENT_UIID);
					if( parentUIID!=null ) {
						Activity parentActivity = newActivityMap.get(parentUIID);
						if ( parentActivity == null ) {
							throw new ObjectExtractorException("Parent activity "+parentUIID+" missing for activity "+existingActivity.getTitle()+": "+existingActivity.getActivityUIID());
						}
						existingActivity.setParentActivity(parentActivity);
						existingActivity.setParentUIID(parentUIID);
						if(parentActivity.isComplexActivity()){
							((ComplexActivity) parentActivity).addActivity(existingActivity);
							activityDAO.update(parentActivity);
						}
						
					} else {
						existingActivity.setParentActivity(null);
						existingActivity.setParentUIID(null);
						existingActivity.setOrderId(null); // top level activities don't have order ids.
					}
			    }
				activityDAO.update(existingActivity);
				
			}
		}
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
	@SuppressWarnings("unchecked")
	private void parseTransitions(List transitionsList) throws WDDXProcessorConversionException{
	    
	    HashMap<Integer,Transition> newMap = new HashMap<Integer,Transition>();
		
		if(transitionsList!=null){
			Iterator iterator= transitionsList.iterator();
			while(iterator.hasNext()){
				Hashtable transitionDetails = (Hashtable)iterator.next();
				Transition transition = extractTransitionObject(transitionDetails);
				// Check if transition actually exists. extractTransitionObject returns null
				// if neither the to/from activity exists.
				if ( transition != null ) {
					transitionDAO.insertOrUpdate(transition);
					newMap.put(transition.getTransitionUIID(),transition);
				}
			}
		}
		
		// clean up the links for any old transitions.
		Iterator iter = learningDesign.getTransitions().iterator();
		while (iter.hasNext()) {
			Transition element = (Transition) iter.next();
			Integer uiID = element.getTransitionUIID();
			Transition match = newMap.get(uiID);
			if ( match == null ) {
				// transition is no more, clean up the old activity links
				cleanupTransition(element);
			}
		}
		// clear the old set and reset up the transition set. Done this way to keep the Hibernate cascading happy. 
		// this means we don't have to manually remove the transition object.
	    // Note: This will leave orphan content in the tool tables. It can be removed by the tool content cleaning job, 
        // which may be run from the admin screen or via a cron job. 
		learningDesign.getTransitions().clear();
		learningDesign.getTransitions().addAll(newMap.values());
		
		learningDesignDAO.insertOrUpdate(learningDesign);
	    
	   
	}
	public Activity extractActivityObject(Hashtable activityDetails) throws WDDXProcessorConversionException, ObjectExtractorException {
		
	    //it is assumed that the activityUUID will always be sent by flash.
	    Integer activityUUID = WDDXProcessor.convertToInteger(activityDetails,WDDXTAGS.ACTIVITY_UIID);
	    Activity activity = null;
		Integer activityTypeID = WDDXProcessor.convertToInteger(activityDetails, WDDXTAGS.ACTIVITY_TYPE_ID);
		if ( activityTypeID == null ) {
			throw new ObjectExtractorException("activityTypeID missing");
		}

		//get the activity with the particular activity uuid, if null, then new object needs to be created.
	    Activity existingActivity = activityDAO.getActivityByUIID(activityUUID, learningDesign);
		if (existingActivity != null && ! existingActivity.getActivityTypeId().equals(activityTypeID) ) {
    		existingActivity = null;
		} 
		
		if ( existingActivity != null ) {
			activity = existingActivity;
		} else {
			activity  = Activity.getActivityInstance(activityTypeID.intValue());
	    }
		processActivityType(activity,activityDetails);
		
		
	    if (keyExists(activityDetails, WDDXTAGS.ACTIVITY_TYPE_ID))
	        activity.setActivityTypeId(WDDXProcessor.convertToInteger(activityDetails, WDDXTAGS.ACTIVITY_TYPE_ID));
	    if (keyExists(activityDetails, WDDXTAGS.ACTIVITY_UIID))
	        activity.setActivityUIID(WDDXProcessor.convertToInteger(activityDetails,WDDXTAGS.ACTIVITY_UIID));
	    if (keyExists(activityDetails, WDDXTAGS.DESCRIPTION))
	        activity.setDescription(WDDXProcessor.convertToString(activityDetails,WDDXTAGS.DESCRIPTION));
	    if (keyExists(activityDetails, WDDXTAGS.ACTIVITY_TITLE))
	        activity.setTitle(WDDXProcessor.convertToString(activityDetails,WDDXTAGS.ACTIVITY_TITLE));
	    if (keyExists(activityDetails, WDDXTAGS.HELP_TEXT))
	        activity.setHelpText(WDDXProcessor.convertToString(activityDetails,WDDXTAGS.HELP_TEXT));
	    if (keyExists(activityDetails, WDDXTAGS.XCOORD))
	        activity.setXcoord(WDDXProcessor.convertToInteger(activityDetails, WDDXTAGS.XCOORD));
	    if (keyExists(activityDetails, WDDXTAGS.YCOORD))
	        activity.setYcoord(WDDXProcessor.convertToInteger(activityDetails, WDDXTAGS.YCOORD));

	    if (keyExists(activityDetails, WDDXTAGS.GROUPING_UIID))
	    {
			Integer groupingUIID = WDDXProcessor.convertToInteger(activityDetails,WDDXTAGS.GROUPING_UIID);
			if ( groupingUIID != null ){
				Grouping grouping = groupings.get(groupingUIID);
				if ( grouping != null ) {
					setGrouping(activity, grouping, groupingUIID);
				} else {
					log.warn("Unable to find matching grouping for groupingUIID"+groupingUIID+". Activity UUID"+activityUUID+" will not be grouped.");
					clearGrouping(activity);
				}
			} else {
				clearGrouping(activity);
			}
	    } else {
			clearGrouping(activity);
	    }
		
		if (keyExists(activityDetails, WDDXTAGS.ORDER_ID))
		    activity.setOrderId(WDDXProcessor.convertToInteger(activityDetails,WDDXTAGS.ORDER_ID));
		if (keyExists(activityDetails, WDDXTAGS.DEFINE_LATER))
		    activity.setDefineLater(WDDXProcessor.convertToBoolean(activityDetails,WDDXTAGS.DEFINE_LATER));
		
		activity.setLearningDesign(learningDesign);
		
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
		
		// Set creation date based on the server timezone, not the client.
		if ( activity.getCreateDateTime() == null )
			activity.setCreateDateTime(modificationDate);

		if (keyExists(activityDetails, WDDXTAGS.RUN_OFFLINE))
		    activity.setRunOffline(WDDXProcessor.convertToBoolean(activityDetails,WDDXTAGS.RUN_OFFLINE));
		if (keyExists(activityDetails, WDDXTAGS.ACTIVITY_CATEGORY_ID))
		    activity.setActivityCategoryID(WDDXProcessor.convertToInteger(activityDetails,WDDXTAGS.ACTIVITY_CATEGORY_ID));
		if (keyExists(activityDetails, WDDXTAGS.LIBRARY_IMAGE))	
			activity.setLibraryActivityUiImage(WDDXProcessor.convertToString(activityDetails,WDDXTAGS.LIBRARY_IMAGE));
		
		if (keyExists(activityDetails, WDDXTAGS.GROUPING_SUPPORT_TYPE))	
			activity.setGroupingSupportType(WDDXProcessor.convertToInteger(activityDetails,WDDXTAGS.GROUPING_SUPPORT_TYPE));
		
		if (keyExists(activityDetails, WDDXTAGS.STOP_AFTER_ACTIVITY))	
		    activity.setStopAfterActivity(WDDXProcessor.convertToBoolean(activityDetails,WDDXTAGS.STOP_AFTER_ACTIVITY));

		return activity;
	}

	private void clearGrouping(Activity activity) {
		activity.setGrouping(null);
		activity.setGroupingUIID(null);
		activity.setApplyGrouping(false);
	}
	
	private void setGrouping(Activity activity, Grouping grouping, Integer groupingUIID) {
		activity.setGrouping(grouping);
		activity.setGroupingUIID(groupingUIID);
		activity.setApplyGrouping(true);
	}

	private  void processActivityType(Activity activity, Hashtable activityDetails) 
			throws WDDXProcessorConversionException, ObjectExtractorException {
		if(activity.isGroupingActivity())
			 buildGroupingActivity((GroupingActivity)activity,activityDetails);
		else if(activity.isToolActivity())
			 buildToolActivity((ToolActivity)activity,activityDetails);
		else if(activity.isGateActivity())
			 buildGateActivity(activity,activityDetails);
		else if(activity.isBranchingActivity())
			 buildBranchingActivity((BranchingActivity)activity,activityDetails);
		else 			
			 buildComplexActivity((ComplexActivity)activity,activityDetails);		
	}
	private void buildComplexActivity(ComplexActivity activity,Hashtable activityDetails) throws WDDXProcessorConversionException{
		// clear all the children - will be built up again by parseActivitiesToMatchUpParentActivityByParentUIID
		// we don't use all-delete-orphan on the activities relationship so we can do this clear.
		activity.getActivities().clear();
		if(activity instanceof OptionsActivity)
			buildOptionsActivity((OptionsActivity)activity,activityDetails);
		else if (activity instanceof ParallelActivity)
			buildParallelActivity((ParallelActivity)activity,activityDetails);
		else
			buildSequenceActivity((SequenceActivity)activity,activityDetails);
		
	}
	private void buildBranchingActivity(BranchingActivity branchingActivity,Hashtable activityDetails)
		throws WDDXProcessorConversionException, ObjectExtractorException {
		if ( branchingActivity.isChosenBranchingActivity() ) {
			branchingActivity.setSystemTool(systemToolDAO.getSystemToolByID(SystemTool.TEACHER_CHOSEN_BRANCHING));
		} else if ( branchingActivity.isGroupBranchingActivity() ) {
			branchingActivity.setSystemTool(systemToolDAO.getSystemToolByID(SystemTool.GROUP_BASED_BRANCHING));
		} else if ( branchingActivity.isToolBranchingActivity() ) {
			branchingActivity.setSystemTool(systemToolDAO.getSystemToolByID(SystemTool.TOOL_BASED_BRANCHING));
		}

		branchingActivity.setStartXcoord(WDDXProcessor.convertToInteger(activityDetails, WDDXTAGS.START_XCOORD));
		branchingActivity.setStartYcoord(WDDXProcessor.convertToInteger(activityDetails, WDDXTAGS.START_YCOORD));
		branchingActivity.setEndXcoord(WDDXProcessor.convertToInteger(activityDetails, WDDXTAGS.END_XCOORD));
		branchingActivity.setEndYcoord(WDDXProcessor.convertToInteger(activityDetails, WDDXTAGS.END_YCOORD));
	}	
	
	private void buildGroupingActivity(GroupingActivity groupingActivity,Hashtable activityDetails) 
		throws WDDXProcessorConversionException, ObjectExtractorException {
		/**
		 * read the createGroupingUUID, get the Grouping Object, and set CreateGrouping to that object
		 */
	    Integer createGroupingUIID = WDDXProcessor.convertToInteger(activityDetails,WDDXTAGS.CREATE_GROUPING_UIID);	    
	    Grouping grouping = groupings.get(createGroupingUIID);
	    if (grouping!=null)
	    {
		    groupingActivity.setCreateGrouping(grouping);
		    groupingActivity.setCreateGroupingUIID(createGroupingUIID);
	    }
	    
		SystemTool systemTool = systemToolDAO.getSystemToolByID(SystemTool.GROUPING);
		groupingActivity.setSystemTool(systemTool);
		
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
		Integer firstActivityUIID = WDDXProcessor.convertToInteger(activityDetails, WDDXTAGS.FIRST_ACTIVITY_UIID);
		if ( firstActivityUIID != null ) {
			firstChildToSequenceMap.put(firstActivityUIID, (SequenceActivity)activity);
		}
	}
	
	private void buildToolActivity(ToolActivity toolActivity,Hashtable activityDetails) throws WDDXProcessorConversionException{
		if ( log.isDebugEnabled() ) {
			log.debug("In tool activity UUID"+activityDetails.get(WDDXTAGS.ACTIVITY_UIID)
					+" tool content id=" +activityDetails.get(WDDXTAGS.TOOL_CONTENT_ID));
		}
		
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
		else if (activity instanceof SystemGateActivity)
			buildSystemGateActivity((SystemGateActivity)activity,activityDetails);
		else
			buildScheduleGateActivity((ScheduleGateActivity)activity,activityDetails);
		GateActivity gateActivity = (GateActivity)activity ;
		gateActivity.setGateActivityLevelId(WDDXProcessor.convertToInteger(activityDetails,WDDXTAGS.GATE_ACTIVITY_LEVEL_ID));
		gateActivity.setGateOpen(WDDXProcessor.convertToBoolean(activityDetails,WDDXTAGS.GATE_OPEN));
				
	}
	private void buildSynchGateActivity(SynchGateActivity activity,Hashtable activityDetails) throws WDDXProcessorConversionException{	
		SystemTool systemTool = systemToolDAO.getSystemToolByID(SystemTool.SYNC_GATE);
		activity.setSystemTool(systemTool);
	}
	private void buildPermissionGateActivity(PermissionGateActivity activity,Hashtable activityDetails) throws WDDXProcessorConversionException{		
		SystemTool systemTool = systemToolDAO.getSystemToolByID(SystemTool.PERMISSION_GATE);
		activity.setSystemTool(systemTool);
	}
	private void buildSystemGateActivity(SystemGateActivity activity,Hashtable activityDetails) throws WDDXProcessorConversionException{		
		SystemTool systemTool = systemToolDAO.getSystemToolByID(SystemTool.SYSTEM_GATE);
		activity.setSystemTool(systemTool);
	}
	private void buildScheduleGateActivity(ScheduleGateActivity activity,Hashtable activityDetails) throws WDDXProcessorConversionException{
	    //activity.setGateStartDateTime(WDDXProcessor.convertToDate(activityDetails,WDDXTAGS.GATE_START_DATE));
		//activity.setGateEndDateTime(WDDXProcessor.convertToDate(activityDetails,WDDXTAGS.GATE_END_DATE));
		activity.setGateStartTimeOffset(WDDXProcessor.convertToLong(activityDetails,WDDXTAGS.GATE_START_OFFSET));
		activity.setGateEndTimeOffset(WDDXProcessor.convertToLong(activityDetails,WDDXTAGS.GATE_END_OFFSET));		
		SystemTool systemTool = systemToolDAO.getSystemToolByID(SystemTool.SCHEDULE_GATE);
		activity.setSystemTool(systemTool);
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
	 * <p>
	 * Assumes that all the activities have been read and are in the newActivityMap.
	 * The toActivity and fromActivity are only set if the activity exists in the 
	 * newActivityMap. If this leaves the transition with no to/from activities
	 * then null is returned.
	 * 
	 * @param transitionDetails
	 * @throws WDDXProcessorConversionException
	 */
	private Transition extractTransitionObject(Hashtable transitionDetails) throws WDDXProcessorConversionException{
		
	    Integer transitionUUID = WDDXProcessor.convertToInteger(transitionDetails,WDDXTAGS.TRANSITION_UIID);
	    if ( transitionUUID == null ) { 
	    	throw new WDDXProcessorConversionException("Transition is missing its UUID");
	    }
	    
	    Transition transition = null;	
	    Transition existingTransition = findTransition(transitionUUID);
		
	    if (existingTransition == null) {
	        transition = new Transition();
		    transition.setTransitionUIID(transitionUUID);
	    } else {
	        transition = existingTransition;
	    }

	    if (keyExists(transitionDetails, WDDXTAGS.TO_ACTIVITY_UIID))
	    {
	        Integer toUIID=WDDXProcessor.convertToInteger(transitionDetails,WDDXTAGS.TO_ACTIVITY_UIID); 
			if(toUIID!=null){
				Activity toActivity = newActivityMap.get(toUIID);
				if ( toActivity  != null ) {
					transition.setToActivity(toActivity);
					transition.setToUIID(toUIID);
					//update the transitionTo property for the activity
					toActivity.setTransitionTo(transition);
				} else {
					transition.setToActivity(null);
					transition.setToUIID(null);
				}
			}
	    }
		
	    if (keyExists(transitionDetails, WDDXTAGS.FROM_ACTIVITY_UIID))
	    {
	        Integer fromUIID=WDDXProcessor.convertToInteger(transitionDetails,WDDXTAGS.FROM_ACTIVITY_UIID);
			if(fromUIID!=null){
				Activity fromActivity = newActivityMap.get(fromUIID);
				if ( fromActivity != null ) {
					transition.setFromActivity(fromActivity);
					transition.setFromUIID(fromUIID);
					//update the transitionFrom property for the activity
					fromActivity.setTransitionFrom(transition);
				} else {
					transition.setFromActivity(null);
					transition.setFromUIID(null);
				}
			}	
	    }
	    
	    if(keyExists(transitionDetails, WDDXTAGS.DESCRIPTION))
	        transition.setDescription(WDDXProcessor.convertToString(transitionDetails,WDDXTAGS.DESCRIPTION));
	    if(keyExists(transitionDetails, WDDXTAGS.TITLE))
	        transition.setTitle(WDDXProcessor.convertToString(transitionDetails,WDDXTAGS.TITLE));

	    // Set creation date based on the server timezone, not the client.
		if ( transition.getCreateDateTime() == null )
			transition.setCreateDateTime(modificationDate);

		if ( transition.getToActivity() != null && transition.getFromActivity() != null ) {
			transition.setLearningDesign(learningDesign);		
			return transition; 
		} else {
			// One of the to/from is missing, can't store this transition. Make sure we clean up the related activities 
			cleanupTransition(transition);
			transition.setLearningDesign(null);
			return null;
		}
	}

	/**
	 * Wipe out any links fromany activities that may be linked to it (e.g. the case where a transition has an from activity
	 * but not a too activity. These cases should be picked up by Flash, but just in case.
	 */
	private void cleanupTransition(Transition transition) {
		if(transition.getFromActivity().getTransitionFrom().equals(transition)){
			transition.getFromActivity().setTransitionFrom(null);
		}
		if(transition.getToActivity().getTransitionTo().equals(transition)){
			transition.getToActivity().setTransitionTo(null);
		}
	}

	/** Search in learning design for existing object. Can't go to database as that will trigger 
	* a Flush, and we haven't updated the rest of the design, so this would trigger a 
	* "deleted object would be re-saved by cascade" error.
	*/
	private Transition findTransition(Integer transitionUUID) {
	    Transition existingTransition = null;
		Set transitions = learningDesign.getTransitions();
		Iterator iter = transitions.iterator();
		while (existingTransition==null && iter.hasNext()) {
			Transition element = (Transition) iter.next();
			if ( transitionUUID.equals(element.getTransitionUIID()) ) { 
				existingTransition = element;
			}
		}
		return existingTransition;
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

	public void setMode(Integer mode) {
		this.mode = mode;
	}
	
	public Integer getMode() {
		return mode;
	}
	
	/**
     * Helper method to retrieve the user data. Gets the id from the user details
     * in the shared session
     * @return the user id
     */
    public static Integer getUserId()
    {
        HttpSession ss = SessionManager.getSession();
        UserDTO learner = (UserDTO) ss.getAttribute(AttributeNames.USER);
        return learner != null ? learner.getUserID() : null;
    }
    
	/**
	 * Parses the mappings used for branching. They map groups to the sequence activities
	 * that form a branch within a branching activity.
	 * 
	 * Must be done after all the other parsing as we need to match up activities
	 * and groups.
	 * @param branchMappingsList
	 * @throws WDDXProcessorConversionException
	 */
	
	private void parseBranchMappings(List branchMappingsList) 
		throws WDDXProcessorConversionException 	
	{
	    if (branchMappingsList != null)
	    {
	        Iterator iterator = branchMappingsList.iterator();
	        while(iterator.hasNext())
	        {
            	extractGroupBranchActivityEntry((Hashtable)iterator.next());
	        }
	    }
	}
	
	@SuppressWarnings("unchecked")
	private GroupBranchActivityEntry extractGroupBranchActivityEntry(Hashtable details) throws WDDXProcessorConversionException {

    	Long entryId = WDDXProcessor.convertToLong(details, WDDXTAGS.GROUP_BRANCH_ACTIVITY_ENTRY_ID);	
    	Integer entryUIID = WDDXProcessor.convertToInteger(details, WDDXTAGS.GROUP_BRANCH_ACTIVITY_ENTRY_UIID);	
	    if ( entryUIID == null ) { 
	    	throw new WDDXProcessorConversionException("Group based branch mapping entry is missing its UUID. Entry "+details);
	    }

    	Integer groupUIID = WDDXProcessor.convertToInteger(details, WDDXTAGS.GROUP_UIID);	
    	Integer sequenceActivityUIID=WDDXProcessor.convertToInteger(details,WDDXTAGS.GROUP_BRANCH_SEQUENCE_ACTIVITY_UIID);
    	Integer branchingActivityUIID=WDDXProcessor.convertToInteger(details,WDDXTAGS.GROUP_BRANCH_ACTIVITY_UIID);
    	
    	Group group = groups.get(groupUIID);
   	    if ( group == null ) {
		    	throw new WDDXProcessorConversionException("Group listed in the branch mapping list is missing. Mapping entry UUID "+entryUIID+" groupUIID "+groupUIID);
   	    }

   	    Activity sequenceActivity = newActivityMap.get(sequenceActivityUIID);
   	    if ( sequenceActivity == null ) {
		    	throw new WDDXProcessorConversionException("Sequence Activity listed in the branch mapping list is missing. Mapping entry UUID "+entryUIID+" sequenceActivityUIID "+sequenceActivityUIID);
   	    }
   	    if ( ! sequenceActivity.isSequenceActivity() ) {
		    	throw new WDDXProcessorConversionException("Activity listed in the branch mapping list is not a sequence activity. Mapping entry UUID "+entryUIID+" sequenceActivityUIID "+sequenceActivityUIID);
   	    }
   	    
   	    Activity branchingActivity = newActivityMap.get(branchingActivityUIID);
   	    if ( branchingActivity == null ) {
		    	throw new WDDXProcessorConversionException("Branching Activity listed in the branch mapping list is missing. Mapping entry UUID "+entryUIID+" branchingActivityUIID "+branchingActivityUIID);
   	    }
   	    if ( ! branchingActivity.isBranchingActivity() ) {
		    	throw new WDDXProcessorConversionException("Activity listed in the branch mapping list is not a branching activity. Mapping entry UUID "+entryUIID+" branchingActivityUIID "+branchingActivityUIID);
   	    }

   	    GroupBranchActivityEntry entry = null;

		// Does it exist already? If the mapping was created at runtime, there will be an ID but no IU ID field.
		// If it was created in authoring, will have a UI ID and may or may not have an ID.
		// So try to match up on UI ID first, failing that match on ID. Then the worst case, which is the mapping
		// is created at runtime but then modified in authoring (and has has a new UI ID added) is handled.
		if ( group.getBranchActivities() != null && group.getBranchActivities().size() > 0  ) {
			GroupBranchActivityEntry uiid_match = null;
			GroupBranchActivityEntry id_match = null;
			Iterator iter = group.getBranchActivities().iterator();
			while (uiid_match == null && iter.hasNext()) {
				GroupBranchActivityEntry possibleEntry = (GroupBranchActivityEntry) iter.next();
				if ( entryUIID.equals(possibleEntry.getEntryUIID()) ) {
					uiid_match = possibleEntry;
				}
				if ( entryId != null && entryId.equals(possibleEntry.getEntryId()) ) {
					id_match = possibleEntry;
				}
			}
			entry = uiid_match != null ? uiid_match : id_match;
		}

		if ( entry == null ) {
			group.allocateBranchToGroup(entryId,entryUIID, (SequenceActivity)sequenceActivity, (BranchingActivity)branchingActivity);
   	    } else {
   	    	entry.setBranchSequenceActivity((SequenceActivity)sequenceActivity);
   	    	entry.setBranchingActivity((BranchingActivity)branchingActivity);
   	    	entry.setGroup(group);
   	    }
		
		groupingDAO.update(group);
		return entry;
	}


}
	
