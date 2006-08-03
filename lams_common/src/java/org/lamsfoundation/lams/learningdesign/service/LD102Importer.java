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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */
package org.lamsfoundation.lams.learningdesign.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.LearningLibrary;
import org.lamsfoundation.lams.learningdesign.RandomGrouping;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.Transition;
import org.lamsfoundation.lams.learningdesign.dao.IActivityDAO;
import org.lamsfoundation.lams.learningdesign.dao.ILearningDesignDAO;
import org.lamsfoundation.lams.learningdesign.dao.ILearningLibraryDAO;
import org.lamsfoundation.lams.learningdesign.dto.ValidationErrorDTO;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.ToolContent;
import org.lamsfoundation.lams.tool.ToolImportSupport;
import org.lamsfoundation.lams.tool.dao.IToolContentDAO;
import org.lamsfoundation.lams.tool.dao.IToolDAO;
import org.lamsfoundation.lams.tool.dao.IToolImportSupportDAO;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;
import org.lamsfoundation.lams.util.wddx.WDDXProcessor;
import org.lamsfoundation.lams.util.wddx.WDDXProcessorConversionException;

/** Create a learning design from the LAMS 1.0.x WDDX packet. Used by the Import action. */
public class LD102Importer {

	private Logger log = Logger.getLogger(LD102Importer.class);
	private ILearningDesignService learningDesignService = null;
	private IBaseDAO baseDAO = null;
	private ILearningLibraryDAO learningLibraryDAO = null;
	private ILearningDesignDAO learningDesignDAO = null;
	private IActivityDAO activityDAO = null;
	private IToolDAO toolDAO = null;
	private IToolImportSupportDAO toolImportSupportDAO = null;
	private IToolContentDAO toolContentDAO = null;

	private LearningDesign ldInProgress = null;
	private String originalPacket = null;
	private List<String> toolsErrorMsgs;
	private Date createDate = new Date();
	
	/** 
	* Store relationship between an activity and its grouping for processing after all the activities have been process.
	* can't do it as we go as the groupings may not be set up yet! 
	* should contain the activity ui id (Integer) as the key and the grouping ui id (Integer) as the value
	*/
	private Map<Integer, Integer> groupingsToAssign = new HashMap<Integer, Integer>();
	/** As we create a grouping, store it in newGroupings so it can be assigned to its activities later
	 * key is grouping ui id, value is the grouping object
	 */
	private Map<Integer, Grouping> newGroupings= new HashMap<Integer, Grouping>();
	
	/** put aside all the transitions as we find them and process them after the activities. */
	private List<Hashtable> transitionsToDo = new ArrayList<Hashtable>();
	/** as we create an activity, stick it in this map so we can find it quickly later (ie while processing transitions). Key is the UI ID */
	private Map<Integer, Activity> newActivityMap = new HashMap<Integer, Activity>();
	/** we are "losing" a level of activity when we combined activity and task levels, so need to keep the relationships for getting the 
	 * right activity for a transition. Key is parent ui id, value is the child ui id
	 */ 
	private Map<Integer, Integer> flattenedActivityMap = new HashMap<Integer, Integer>();
	/** images to use for each tool, based on the library activities in the database. Key is tool id, value is url. */
	private Map<Long, String> libraryActivityUiImages;
	/** store the content by content id field, ready for use when the matching activity is processed */
	private Map<Integer, Hashtable> contentMap = new HashMap<Integer, Hashtable>();

	/** The value to be past back and forth with the 1.0.x authoring tool to indicate a null value for a numeric id */
	private static final Long NUMERIC_NULL_VALUE_LONG = new Long(-1);
	/** The value to be past back and forth with the 1.0.x authoring tool to indicate a null value for a numeric id */
	private static final Integer NUMERIC_NULL_VALUE_INTEGER = new Integer(-1);
	
	private static final String LD_OBJECT_TYPE = "LearningDesign";
	private static final String ABSTRACT_TASK_OBJECT_TYPE = "task";
	private static final String MULTI_TASK_OBJECT_TYPE = "Multitask";
	private static final String TRANSITION_OBJECT_TYPE = "transition";
	private static final String ACTIVITY_OBJECT_TYPE = "Activity";
	private static final String OPTIONAL_OBJECT_TYPE = "optionalactivity";
	private static final String CONTENT_OBJECT_TYPE = "content";

	private static final String TOOLDATA_TAGS_TYPE_AUTHORING = "authoring";
	private static final String TOOLDATA_TAGS_JOURNAL = "journal";
	private static final String TOOLDATA_TAGS_NOTICEBOARD = "noticeboard";
	private static final String TOOLDATA_TAGS_MESSAGEBOARD = "messageboard";
	private static final String TOOLDATA_TAGS_LOMS = "loms";
	private static final String TOOLDATA_TAGS_CHAT = "chat";
	private static final String TOOLDATA_TAGS_RPT_SUBMIT= "reportsubmission";
	private static final String TOOLDATA_TAGS_RPT_MARK= "reportmarking";
	private static final String TOOLDATA_TAGS_GROUPING = "group";
	private static final String TOOLDATA_TAGS_GROUPREPORTING = "groupreporting";
	private static final String TOOLDATA_TAGS_RANKING = "ranking";
	private static final String TOOLDATA_TAGS_QUESTIONANSWER = "qa";
	private static final String TOOLDATA_TAGS_SIMPLE_ASSESSMENT = "simpleassessment";
	private static final String TOOLDATA_TAGS_URLCONTENT = "urlcontent";
	private static final String TOOLDATA_TAGS_FILECONTENT = "filecontent"; 
	private static final String TOOLDATA_TAGS_HTMLNOTICBOARD = "htmlnb";
	private static final String TOOLDATA_TAGS_SINGLE_RESOURCE = "singleresource";	
	private static final String TOOLDATA_TAGS_IMAGEGALLERY = "imagegallery"; 
	private static final String TOOLDATA_TAGS_IMAGERANKING = "imageranking";
	
	public LD102Importer(ILearningDesignService learningDesignService, IBaseDAO baseDAO, 
			ILearningDesignDAO learningDesignDAO, ILearningLibraryDAO learningLibraryDAO, IActivityDAO activityDAO, IToolDAO toolDAO, 
			IToolImportSupportDAO toolImportSupportDAO, IToolContentDAO toolContentDAO, List<String> toolsErrorMsgs) {
		this.learningDesignService = learningDesignService;
		this.baseDAO = baseDAO;
		this.learningLibraryDAO = learningLibraryDAO;
		this.learningDesignDAO = learningDesignDAO;
		this.activityDAO = activityDAO;
		this.toolDAO = toolDAO;
		this.toolImportSupportDAO = toolImportSupportDAO;
		this.toolContentDAO = toolContentDAO;
		this.toolsErrorMsgs = toolsErrorMsgs;
		if ( toolsErrorMsgs == null ) {
			log.warn("The list toolsErrorMsgs supplied is null so any warnings will be logged but won't appear in the user's screen.");
			toolsErrorMsgs = new ArrayList<String>();
		}
		
		this.libraryActivityUiImages = getLibraryActivityUiImages();  
	}
	
	public void setLearningDesignService(ILearningDesignService learningDesignService) {
		this.learningDesignService = learningDesignService;
	}
	public void setBaseDAO (IBaseDAO baseDAO) {
		this.baseDAO = baseDAO;
	}
	public void setToolDAO (IToolDAO toolDAO) {
		this.toolDAO = toolDAO;
	}
	public void setToolImportSupportDAO (
			IToolImportSupportDAO toolImportSupportDAO) {
		this.toolImportSupportDAO = toolImportSupportDAO;
	}
	public void setToolContentDAO(IToolContentDAO toolContentDAO) {
		this.toolContentDAO = toolContentDAO;
	}

	public void setActivityDAO(IActivityDAO activityDAO) {
		this.activityDAO = activityDAO;
	}
	public void setLearningLibraryDAO(ILearningLibraryDAO learningLibraryDAO) {
		this.learningLibraryDAO = learningLibraryDAO;
	}
	public void setLearningDesignDAO(ILearningDesignDAO learningDesignDAO) {
		this.learningDesignDAO = learningDesignDAO;
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

	protected static boolean isTask(String objectType) {
		if ( objectType!=null && 
			( objectType.equalsIgnoreCase(ABSTRACT_TASK_OBJECT_TYPE)
				||  objectType.equalsIgnoreCase(MULTI_TASK_OBJECT_TYPE) ) )
			return true;
		else
			return false;
	}
	
	protected static boolean isSimpleTask(String objectType, String toolType) {
		if ( objectType!=null && toolType!=null 
				&& objectType.equalsIgnoreCase(ABSTRACT_TASK_OBJECT_TYPE)
				&& ! toolType.equalsIgnoreCase(TOOLDATA_TAGS_GROUPING) )
			return true;
		else
			return false;
	}

	protected static boolean isMultiTask(String objectType, String toolType) {
		return ( objectType==null ? false : objectType.equalsIgnoreCase(MULTI_TASK_OBJECT_TYPE));
	}

	protected static boolean isGroupingToolTask(String objectType, String toolType) {
		if ( objectType!=null && toolType!=null 
				&& objectType.equalsIgnoreCase(ABSTRACT_TASK_OBJECT_TYPE)
				&& toolType.equalsIgnoreCase(TOOLDATA_TAGS_GROUPING) )
			return true;
		else
			return false;
	}

	protected static boolean isTransition(String objectType) {
		return ( objectType==null ? false : objectType.equalsIgnoreCase(TRANSITION_OBJECT_TYPE));
	}

	protected static boolean isActivity(String objectType) {
		return ( objectType==null ? false : objectType.equalsIgnoreCase(ACTIVITY_OBJECT_TYPE));
	}
	
	protected static boolean isOptionalActivity(String objectType) {
		return ( objectType==null ? false : objectType.equalsIgnoreCase(OPTIONAL_OBJECT_TYPE));
	}
	
	protected static boolean isContent(String objectType) {
		return ( objectType==null ? false : objectType.equalsIgnoreCase(CONTENT_OBJECT_TYPE));
	}
	
	protected static boolean isLearningDesign(String objectType) {
		return ( objectType==null ? false : objectType.trim().equalsIgnoreCase(LD_OBJECT_TYPE));
	}
	
	protected static String getExpectedActivityTransition  ()
	{
		return("Wrong format for data - object type expected is"
		+ ACTIVITY_OBJECT_TYPE
		+ " or "
		+ OPTIONAL_OBJECT_TYPE
		+ " or "
		+ TRANSITION_OBJECT_TYPE);
	}
	
	protected static String getExpectedActivity  ()
	{
		return("Wrong format for data - object type expected is" + ACTIVITY_OBJECT_TYPE);
	}
	
	protected static String getExpectedOptionalActivity  ()
	{
		return("Wrong format for data - object type expected is" + OPTIONAL_OBJECT_TYPE);
	}

	protected static String getExpectedContent ()
	{
		return("Wrong format for data - object type expected is" + CONTENT_OBJECT_TYPE);
	}
	
	protected static String getExpectedTaskTransition  () 
	{
		return("Wrong format for data - object type expected is"
		+ ABSTRACT_TASK_OBJECT_TYPE
		+ " or "
		+ MULTI_TASK_OBJECT_TYPE
		+ " or "
		+ TRANSITION_OBJECT_TYPE);
	}
	
	protected static String getExpectedTask  ()
	{
		return("Wrong format for data - object type expected is"
		+ ABSTRACT_TASK_OBJECT_TYPE
		+ " or "
		+ MULTI_TASK_OBJECT_TYPE);
	}

	/** Guaranteed not to return null - use when putting values into hash table
	 */
	private String getValue( String possValue )
	{
		return ( possValue==null ? "" : possValue );	
	}

	/* Check that the WDDX packet doesn't contain any invalid "<null />" strings 
	 * returns true if they do exist */
	private boolean containsNulls(String packet)
	{
		if (packet.indexOf("<null />") != -1)
			return true;
		else
			return false;
	}

	
	public Long storeLDDataWDDX(String ldWddxPacket, User importer, WorkspaceFolder folder) throws ImportToolContentException
	{
		originalPacket = ldWddxPacket;
		
		if (importer == null)
		{
			throw new ImportToolContentException("Importer is missing");
		}

		if (containsNulls(ldWddxPacket))
		{
			log.error("Unable to process WDDX packet from client due to containing '<null/>' - this would cause WDDX to crash.");
			log.error("Packet was " + ldWddxPacket);
			throw new ImportToolContentException("Invalid packet format - contains nulls. See log for details.");
		}

		Hashtable ldHashTable = null;

		if (log.isDebugEnabled())
			log.debug("Request received to import 1.0.x learning design data" + ldWddxPacket);

		try {
			ldHashTable = (Hashtable) WDDXProcessor.deserialize(ldWddxPacket);
		} catch (Exception e){
			log.error("Unable to process the imported packet: ", e);
			log.error("Packet was " + ldWddxPacket);
			throw new ImportToolContentException("Invalid import data format - unable to deserialise packet. See log for details.");
		}

		if (ldHashTable == null) {
			log.error("WDDX packet is empty. Packet was " + ldWddxPacket);
			throw new ImportToolContentException("Invalid packet format - no details in packet. See log for details.");
		}
		
		else if (isLearningDesign((String) ldHashTable.get(WDDXTAGS102.OBJECT_TYPE))){
			
			processLearningDesign(ldHashTable, importer, folder);
			
			Vector<ValidationErrorDTO> listOfValidationErrorDTOs = learningDesignService.validateLearningDesign(ldInProgress);
			if (listOfValidationErrorDTOs.size() > 0) {
				ldInProgress.setValidDesign(Boolean.FALSE);
				log.info("Imported learning design is invalid. New learning design id "+ldInProgress.getLearningDesignId()+" title ");
				for ( ValidationErrorDTO dto : listOfValidationErrorDTOs ) {
					log.info("Learning design id "+ldInProgress.getLearningDesignId()+" validation error "+dto);
				}
			}
			else {
				ldInProgress.setValidDesign(Boolean.TRUE);
			}
			baseDAO.update(ldInProgress);
		}
		
		else {
			log.error("Packet is not a learning design packet. Packet was " + ldWddxPacket);
			throw new ImportToolContentException("Invalid packet format - it is not a learning design packet. See log for details.");
		}
		
		return(ldInProgress != null ? ldInProgress.getLearningDesignId() : null);

	}

	/** 
	 * Add a message to the error message list about this but keep going - want to make this importer import as much as it possibly can rather
	 * than giving up.
	 * @param e
	 */
	private void handleWDDXProcessorConversionException(WDDXProcessorConversionException e) {
		log.error("Unable to process the imported packet properly due to a WDDXProcessorConversionException - will continue with the design in case future parts okay.", e);
		log.error("Packet was " + originalPacket);
		toolsErrorMsgs.add("Invalid import data format - WDDX caused an exception. Some data from the design will have been lost. See log for more details.");
	}

	private void processLearningDesign(Hashtable newLdHashTable, User importer, WorkspaceFolder folder)
	{
		
		// put the learning design in a member variable - the updating of the tasks and
		// activities needs to get to the content, but its a pain to keep passing everything around!
		createLearningDesign(newLdHashTable, importer, folder);
		baseDAO.insert(ldInProgress);

		// put all the content in the contentMap ready for use by activities.
		Vector newContent = (Vector) newLdHashTable.get(WDDXTAGS102.LD_CONTENT);
		if ( newContent != null )
		{
			processContent(newContent);
		}

		// activities
		Vector newActivitiesTransitions = (Vector) newLdHashTable.get(WDDXTAGS102.LD_ACTTRAN);
		Set optionalActivitiesToProcess = null;
		if ( newActivitiesTransitions == null )
		{
			// log warning - no activities found in learning design.
			log.warn("No activities or transitions were found for  #"+ldInProgress.getLearningDesignId());
		}
		else 
		{
			optionalActivitiesToProcess = processActivitiesTransitions(newActivitiesTransitions);
		}
		
	//	processOptionalActivities(optionalActivitiesToProcess);
		ldInProgress.setFirstActivity(ldInProgress.calculateFirstActivity());
		
		// all activities are set up so now we can do the transitions
		for ( Hashtable transTable: transitionsToDo ) {
			processTransition(transTable);
		}
		
		// now set up the activity -> grouping defn links. Can't be done earlier as there
		// are the groupings are set up as the activities are processed.
		assignGroupings();
		
		// finally set all the dummy output tasks to be replaced with dynamic content
//		setOutputTasksAsReplaceWithDynamic();
		baseDAO.update(ldInProgress);

	}

	/**
	 * Create the main learning design object from this Learning design DTO object. It also following our
	 * import rules:
	 * <li>lams_license - all details null as 1.0.x doesn't support licenses</li>
	 * <li>lams_copy_type - Set to 1.This indicates it is "normal" design.</li>
	 * <li>lams_workspace_folder - An input parameters to let user choose import workspace</li>
	 * <li>User - The person who execute import action</li>
	 * <li>OriginalLearningDesign - set to null</li>
	 * 
	 * Sets ldInProgress to the new learning design.
	 */
	private void createLearningDesign(Hashtable newLdHashTable, User importer, WorkspaceFolder folder) {
		
		LearningDesign ld = new LearningDesign();
	
		ld.setLearningDesignId(null);
		ld.setLearningDesignUIID(null);
		ld.setDescription((String) newLdHashTable.get(WDDXTAGS102.DESCRIPTION));
		
		String title = (String) newLdHashTable.get(WDDXTAGS102.TITLE);
		ld.setTitle(ImportExportUtil.generateUniqueLDTitle(folder, title, learningDesignDAO));
		ld.setHelpText((String) newLdHashTable.get(WDDXTAGS102.LD_HELPTEXT));
		
		ld.setValidDesign(Boolean.FALSE);
		ld.setReadOnly(Boolean.FALSE);
		ld.setDateReadOnly(null);
		ld.setOfflineInstructions(null);	
		ld.setOnlineInstructions(null);
		
		try {
			ld.setMaxID(WDDXProcessor.convertToInt("Max ID", newLdHashTable.get(WDDXTAGS102.LD_MAXID)) );
		} catch ( WDDXProcessorConversionException e) {
			handleWDDXProcessorConversionException(e);
		}
	
		ld.setUser(importer);
		if(folder != null)
			ld.setWorkspaceFolder(folder);

		ld.setCopyTypeID(LearningDesign.COPY_TYPE_NONE);
		ld.setCreateDateTime(createDate);
		ld.setLastModifiedDateTime(createDate);
		ld.setVersion((String) newLdHashTable.get(WDDXTAGS102.LD_VERSION));
		ld.setLastModifiedDateTime(createDate);
		
		ld.setDuration(null);
		ld.setLicenseText(null);
		ld.setLicense(null);
		ld.setLessonOrgID(null);
		ld.setLessonOrgName(null);
		ld.setLessonID(null);
		ld.setLessonName(null);
		ld.setLessonStartDateTime(null);

		ldInProgress = ld;
	}
	/* Go through all the simple tasks and set any output contents as "to be replaced
	 * with dynamic content at runtime".
	 */
	protected void setOutputTasksAsReplaceWithDynamic()
		throws Exception
	{	
		// get a map of all the simple tasks by id.
		// use the map to iterate over all the output tasks
/*		Hashtable tasksMap = ldInProgress.allTasksWithContent(true);
		Collection simpleTasks = tasksMap.values();
		if ( simpleTasks != null )
		{
			Iterator iter = simpleTasks.iterator();
			Map contentMap = m_contentUtil.getCurrentContentsAsMapBySid();
			while ( iter.hasNext() )
			{
				SimpleTaskVO task = (SimpleTaskVO) iter.next();
				Long outputContentSid = task.getOutputContentSid();
				if ( outputContentSid != null )
				{
					Content content = (Content) contentMap.get(outputContentSid);
					content.setReplaceWithDynamic(true);
					m_contentUtil.updateContent(content);
				}
			}
		} */
	}

	/** Process the activities and transitions. Returns a list of optional Activities yet to
	 * be processed.
	 */
	protected Set processActivitiesTransitions(List newActivitiesTransitions)
	{	
		Set newOptionalActivities = new HashSet();
										
		Iterator tempIterator = newActivitiesTransitions.iterator();
		while ( tempIterator.hasNext() )
		{
			Hashtable clientObj = (Hashtable) tempIterator.next();
			
			// check that it is the object we expect
			String objectType = (String) clientObj.get(WDDXTAGS102.OBJECT_TYPE);
			if ( ! ( isActivity(objectType) || isTransition(objectType) || isOptionalActivity(objectType) ) )
			{
				String message = getExpectedActivityTransition()+ " received "+ objectType + ". The matching activity/transition will be skipped. Object is "+clientObj;
				log.warn(message);
				toolsErrorMsgs.add(message);
			}

			try {
				Integer objId = WDDXProcessor.convertToInteger("Activity/Transition ID", clientObj.get(WDDXTAGS102.LD_ITEM_ID) );
				if ( NUMERIC_NULL_VALUE_INTEGER.equals(objId) )
				{
					String message = "Id value for activity/transition is internal null value. The activity/transition may not appear in the correct place in the sequence. Activity/transition is "+clientObj;
					log.warn(message);
					toolsErrorMsgs.add(message);
				}
	
				if ( isActivity(objectType) )
				{
					processActivity(clientObj, objId);
				}
				else if ( isOptionalActivity(objectType) )
				{
					// put aside for processing later.
					newOptionalActivities.add(clientObj);				
				}	
				else
				{
					transitionsToDo.add(clientObj);
				}
				
			} catch (WDDXProcessorConversionException e) {
				handleWDDXProcessorConversionException(e);
			}

		}
		
		return newOptionalActivities;
		
	}

	private void processActivity(Hashtable activityDetails, Integer activityUIID) throws WDDXProcessorConversionException {

//		Long activityUIID = ui_id;
//		Long systemID =  WDDXProcessor.convertToLong(activityDetails,WDDXTAGS102.SID);
		Long nextTransition;
		nextTransition = WDDXProcessor.convertToLong(activityDetails,WDDXTAGS102.FOLLOWING_TRANSITION);
		Integer xCoOrd = WDDXProcessor.convertToInteger(activityDetails,WDDXTAGS102.ACT_X);
		Integer yCoOrd = WDDXProcessor.convertToInteger(activityDetails,WDDXTAGS102.ACT_Y);
//		Long firstTask = WDDXProcessor.convertToLong(activityDetails,WDDXTAGS102.ACT_FIRSTTASK);
//		String libraryID = WDDXProcessor.convertToString(activityDetails,WDDXTAGS102.ACT_LIBRARY_ID);
//		String title = WDDXProcessor.convertToString(activityDetails,WDDXTAGS102.TITLE);
//		String description = WDDXProcessor.convertToString(activityDetails,WDDXTAGS102.DESCRIPTION);
		List taskTransitions = 	(List)activityDetails.get(WDDXTAGS102.ACT_TASKTRAN);
		
		Activity activity = null;
		if ( taskTransitions.size()==1 ) {
			// easiest case - this is really a single activity 
			Hashtable task = (Hashtable) taskTransitions.get(0);
			activity = createActivityFromTask(task, nextTransition, xCoOrd, yCoOrd, activityUIID);
		} else {
			// TODO parallel and optional
		}
		
		if ( activity != null ) {
			newActivityMap.put(activity.getActivityUIID(), activity);
			flattenedActivityMap.put(activityUIID, activity.getActivityUIID());
		}
		
	}

	private Activity createActivityFromTask(Hashtable taskDetails, Long nextTransition, Integer xCoOrd, Integer yCoOrd, Integer parentActivityId) throws WDDXProcessorConversionException {
		
		String objectType = (String) taskDetails.get(WDDXTAGS102.OBJECT_TYPE);
		String toolType = (String) taskDetails.get(WDDXTAGS102.TASK_TOOLTYPE);
		Integer contentId = WDDXProcessor.convertToInteger(taskDetails,WDDXTAGS102.TASK_INPUT_CONTENT);
		Activity activity = null;
		Integer taskUIID = null;
		
	    if (keyExists(taskDetails, WDDXTAGS102.LD_ITEM_ID)) {
	    	taskUIID = WDDXProcessor.convertToInteger(taskDetails,WDDXTAGS102.LD_ITEM_ID);
	    } else {
			String message = "Id value for task is missing. The activity may not appear in the correct place in the sequence. Activity is "+taskDetails;
			log.warn(message);
			toolsErrorMsgs.add(message);
		}

		if ( isMultiTask(objectType, toolType) )
		{
			toolsErrorMsgs.add("Hit a multitask - not yet implemented.");
			activity.setActivityTypeId(Activity.PARALLEL_ACTIVITY_TYPE);
		    activity.setActivityCategoryID(Activity.CATEGORY_SYSTEM);
		    activity.setActivityUIID(taskUIID);
//			TODO activity = new OptionsActivity();
//			TODO activity = new ParallelActivity();
		}
		else if ( isGroupingToolTask(objectType, toolType) )
		{
			activity = setupGroupingActivity(taskDetails, contentId, taskUIID, parentActivityId);
		}
		else {
			activity = setupToolActivity(taskDetails, contentId, taskUIID);
		}
		
		if ( activity != null ) {
			
			// title and description may already be set up.
			if ( activity.getDescription() == null && keyExists(taskDetails, WDDXTAGS102.DESCRIPTION))
		        activity.setDescription(WDDXProcessor.convertToString(taskDetails,WDDXTAGS102.DESCRIPTION));
		    if ( activity.getTitle() == null && keyExists(taskDetails, WDDXTAGS102.TITLE))
		        activity.setTitle(WDDXProcessor.convertToString(taskDetails,WDDXTAGS102.TITLE));
	
		    activity.setXcoord(xCoOrd);
	        activity.setYcoord(yCoOrd);
	        activity.setHelpText(null);
	
			activity.setApplyGrouping(false); // not nullable so default to false
	        parseGroupingValue(taskDetails.get(WDDXTAGS102.TASK_GROUPING), activity);
	        activity.setGroupingSupportType(Activity.GROUPING_SUPPORT_OPTIONAL);
	        
	//		TODO if (keyExists(activityDetails, WDDXTAGS.ORDER_ID))
	//		    activity.setOrderId(WDDXProcessor.convertToInteger(activityDetails,WDDXTAGS.ORDER_ID));
		    
		    activity.setDefineLater(Boolean.FALSE);
			activity.setLearningDesign(ldInProgress);
		    activity.setCreateDateTime(createDate);
		    activity.setRunOffline(Boolean.FALSE);
			
		    baseDAO.insert(activity);
		}
		
		return activity;
	}

	private GroupingActivity setupGroupingActivity(Hashtable taskDetails, Integer contentId, Integer taskUIID, Integer parentActivityId) throws WDDXProcessorConversionException {
		
		RandomGrouping grouping = (RandomGrouping) Grouping.getGroupingInstance(Grouping.RANDOM_GROUPING_TYPE);
		grouping.setGroupingUIID(taskUIID);
		baseDAO.insert(grouping);
		
		GroupingActivity groupingActivity = (GroupingActivity) Activity.getActivityInstance(Activity.GROUPING_ACTIVITY_TYPE);
		groupingActivity.setActivityCategoryID(Activity.CATEGORY_SYSTEM);
		groupingActivity.setActivityUIID(parentActivityId);
		groupingActivity.setCreateGrouping(grouping);
		groupingActivity.setCreateGroupingUIID(grouping.getGroupingUIID());

		Hashtable groupingContent = contentMap.get(contentId);
		if ( groupingContent == null ) {
			String message = "Unable to find a content for grouping. Default values will apply. Grouping was "+taskDetails;
			log.warn(message);
			toolsErrorMsgs.add(message);
		} else {
			Integer numGroups = WDDXProcessor.convertToInteger(groupingContent,WDDXTAGS102.CONTENT_NUMGROUPS);
			if ( numGroups != null && ! NUMERIC_NULL_VALUE_INTEGER.equals(numGroups) ) {
		    	grouping.setNumberOfGroups(numGroups);
			} 

			Integer maxNumLearner = WDDXProcessor.convertToInteger(groupingContent,WDDXTAGS102.CONTENT_MAXNUM_GROUP);
			if ( maxNumLearner != null && ! NUMERIC_NULL_VALUE_INTEGER.equals(maxNumLearner) ) {
				grouping.setLearnersPerGroup(maxNumLearner);
			}
			
			groupingActivity.setTitle((String)groupingContent.get(WDDXTAGS102.TITLE));
			groupingActivity.setDescription((String)groupingContent.get(WDDXTAGS102.DESCRIPTION));
		}
		
		newGroupings.put(grouping.getGroupingUIID(), grouping);
		return groupingActivity;
	}

	private ToolActivity setupToolActivity(Hashtable taskDetails, Integer contentId, Integer taskUIID) {
		ToolActivity activity = new ToolActivity();
		
	    activity.setActivityUIID(taskUIID);
		activity.setActivityTypeId(Activity.TOOL_ACTIVITY_TYPE);
	    activity.setActivityCategoryID(Activity.CATEGORY_CONTENT);
		
	    // first, find the matching new tool and set up the tool, tool content details.  

	    String toolType = (String) taskDetails.get(WDDXTAGS102.TASK_TOOLTYPE);
	    List toolsSupportingSignature = toolImportSupportDAO.getToolSignatureWhichSupports(toolType);
	    Tool tool = null;
	    Iterator iter = toolsSupportingSignature.iterator();
	    while ( iter.hasNext() && tool == null ) {
	    	ToolImportSupport support = (ToolImportSupport) iter.next();
	    	tool = toolDAO.getToolBySignature(support.getInstalledToolSignature());
	    	if ( tool != null && ! tool.isValid() ) {
	    		// if not valid then not installed properly so don't use it!
	    		tool = null;
	    	}
	    }
	    
	    if ( tool == null ) {
			String message = "Unable to find a tool that supports the activity "+taskDetails.get(WDDXTAGS102.TITLE)+". This activity will be skipped. Activity is "+taskDetails;
			log.warn(message);
			toolsErrorMsgs.add(message);
	    	return null;
	    }

	    ToolContent newContent = new ToolContent(tool);
	    toolContentDAO.saveToolContent(newContent);

	    activity.setTool(tool);	
	    activity.setToolContentId(newContent.getToolContentId());

	    // Now find an icon for the activity. The icon is in the activity tables so look for a library activity that matches this tool.
	    // It may not always find the right icon if there is more than one possible match but its a start!
	    activity.setLibraryActivityUiImage(libraryActivityUiImages.get(tool.getToolId()));
		return activity;
	}
	
/*		private void buildOptionsActivity(OptionsActivity optionsActivity,Hashtable activityDetails) throws WDDXProcessorConversionException{
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
		SystemTool systemTool = systemToolDAO.getSystemToolByID(SystemTool.SYNC_GATE);
		activity.setSystemTool(systemTool);
	}
*/	

	/** 
	 * Get the grouping value for a particular task. Could be "c" for class or a number 
	 * which is the ui id of the related grouping task. Signify class in database as not grouped.
	 * Can't actually assign the groupings yet as they may not have been created - just
	 * note it for later in groupingsToDo.
	 */
	private void parseGroupingValue(Object groupingIdRaw, Activity activity)
	{
		if ( log.isDebugEnabled())
			log.debug("updateGroupingValue: groupingId"+(groupingIdRaw!=null?groupingIdRaw.toString():"null")
				+ " task "+ activity.toString());
		
		Integer groupingId = null;		
		if ( groupingIdRaw != null )
		{
			try {
				groupingId = WDDXProcessor.convertToInteger("Grouping ID", groupingIdRaw);
			} catch ( Exception e)  {}
		}

		if ( groupingId != null )		
		{
			groupingsToAssign.put(activity.getActivityUIID(), groupingId);
			log.debug("Is grouped activity, id="+groupingId);
		}
	}
	
	/** 
	 * Assign the real groupings to the grouped activities
	 */
	private void assignGroupings() {
		
		 for ( Map.Entry<Integer, Integer> entry : groupingsToAssign.entrySet()) {
			 
			 Activity activity = newActivityMap.get(entry.getKey());
			 Grouping grouping = newGroupings.get(entry.getValue());
			 if ( activity != null && grouping != null ) {
				 activity.setGrouping(grouping);
				 activity.setGroupingUIID(grouping.getGroupingUIID());
 				 activity.setApplyGrouping(true);
			 } else {
				String message = "Unable to find a pair (activity/grouping) for a grouped activity. The activity will not be grouped. Activity UIID"+entry.getKey()+" grouping UIID "+entry.getValue();
				log.warn(message);
				toolsErrorMsgs.add(message);
			 }
		 }
 	 }


	/*
	 * get the content's sid based on the internal learning design id - used by 
	 * updateSimpleTask. Match on the creationToolKey field in the content object
	 * Throws an exception if there was a "valid" creationToolKey value but the content
	 * could not be found.
	 
	private Long getContentSid( Long creationToolKey )
		throws Exception
	{
		if ( creationToolKey == null || creationToolKey.equals(LDWDDXValueObjectFactory.NUMERIC_NULL_VALUE_LONG) )
			return null;
			
		Map currentContents = m_contentUtil.getCurrentContentsAsMapByCreationToolKey();
		Content content = (Content) currentContents.get(creationToolKey.toString());
		if ( content == null ){
			if ( log.isDebugEnabled())
				log.debug("Content missing - looking for creationToolKey "+creationToolKey);

			throw new Exception("There is something wrong in the learning design you are trying to save. " +
				"If you are importing it, it may contain Share Resources that has a  reference to an uploaded file. "+
				"Currently LAMS does not support export/import designs with a file resource. "+
				"(Missing: creationToolKey="+creationToolKey+")");
		}

		return ( content.getSid() ); 
	}

	*//** Update the grouping task and the related  group definition. This is the task that 
	 * triggers the creation of a group. *//*
	protected void updateGroupingTask(PersistenceSession session, Hashtable clientObj, GroupingTaskVO groupTask, Integer objId)
		throws Exception 
	{
		// update the values shared with simple task
		updateSimpleTask(clientObj, groupTask);
		
		// set up matching GroupDefnVO if needed
		GroupDefnVO groupDefn = groupTask.getGroupDefn();
		if ( groupDefn == null )
		{
			groupDefn = new GroupDefnVO();
			storeInSession(session, groupDefn);
			groupTask.setGroupDefn(groupDefn);
		}
		groupTask.getGroupDefn().setId(objId.intValue());
		
		// found the matching content object and store link to that!
		Integer contentId = WDDXProcessor.convertToInteger("Group Defn Content Id", clientObj.get(WDDXTAGS102.TASK_INPUT_CONTENT) );
		if ( contentId != null )
		{
			Map contentMap = m_contentUtil.getCurrentContentsAsMapByCreationToolKey();
			Content content = (Content) contentMap.get(contentId.toString());
			groupDefn.setDefnContentSid(content.getSid());
		}
	}

	*/
	
	/** Process the content from the WDDX packet. In 1.0.x, the content was managed by the 
	 * content manager. In 2.x it is managed by the tools. So we go through the content vector
	 * and build up a list of all the tool content. Then as we go through the activities 
	 * we will call the tool to set up its content.
	 * @throws WDDXProcessorConversionException 
	 */
	protected void processContent(List newContent) {	

		// cycle through content from the packet. Update existing ones, add new ones
		// don't have delete support at the moment.
		Iterator iterator = newContent.iterator();
		while ( iterator.hasNext() )
		{
			Hashtable clientObj = (Hashtable) iterator.next();

			if ( clientObj == null )
			{
				log.warn("Packet contains invalid content - one of the content objects is null!");	
				log.warn("Content list is"+newContent.toString());
				toolsErrorMsgs.add("Packet contained an empty content object. See log for more details.");
			}
						
			// check that it is the object we expect
			String objectType = (String) clientObj.get(WDDXTAGS102.OBJECT_TYPE);
			if ( ! isContent(objectType)  ) {
				String message = getExpectedContent()+ " received "+ objectType + ". The matching activity will be missing its content. Object is "+clientObj;
				log.warn(message);
				toolsErrorMsgs.add(message);
			}
			
			try {
				Integer objId = WDDXProcessor.convertToInteger("Content ID", clientObj.get(WDDXTAGS102.LD_ITEM_ID) );
				if ( NUMERIC_NULL_VALUE_INTEGER.equals(objId) ) {
					String message = "Id value for content is internal null value. The matching activity will be missing its content. Content is "+clientObj;
					log.warn(message);
					toolsErrorMsgs.add(message);
				}
		
				contentMap.put(objId, clientObj);
				// TODO convert the content object to something we can use.
	/*			ContentConverter converter = new ContentConverter();
				aContent = converter.createNewContentObject((String) clientObj.get(WDDXTAGS102.CONTENT_TYPE));
				// create the content object in the database, get updated version
				aContent = m_contentUtil.insertContent(aContent);
				converter.convertToDBData(clientObj, aContent, objId);
				m_contentUtil.updateContent(aContent);
	*/
			} catch ( WDDXProcessorConversionException e) {
				handleWDDXProcessorConversionException(e);
			}
		}
	}
	

	/** Process the optional activities
	 *//*
	protected void processOptionalActivities(PersistenceSession session, Set newOptionalActivities )
		throws Exception
	{	
		
		if ( ldInProgress.getOptionalActivities() == null )
		{
			ldInProgress.setOptionalActivities(new HashSet());
		}

		if ( newOptionalActivities == null )
			return;
			
		// after processing the optional activities, this set will contain all the ids
		// of the "desired" ones. Any other optional activities whose
		// ids don't appear in this set are then due to be deleted.
		Set idsUpdated = new HashSet(); 	
			
		Iterator tempIterator = newOptionalActivities.iterator();
		while ( tempIterator.hasNext() )
		{
			Hashtable clientObj = (Hashtable) tempIterator.next();
			
			// check that it is the object we expect
			String objectType = (String) clientObj.get(WDDXTAGS102.OBJECT_TYPE);
			if ( ! isOptionalActivity(objectType)  )
			{
				throw new Exception(getExpectedOptionalActivity()
					+ " received "+ objectType + ". Unable to store .");
			}
			
			Integer objId = WDDXProcessor.convertToInteger("Optional Activity ID", clientObj.get(WDDXTAGS102.LD_ITEM_ID) );
			if ( LDWDDXValueObjectFactory.NUMERIC_NULL_VALUE_INTEGER.equals(objId) )
			{
				throw new Exception("Id for optional activity is the internal null value. Optional activity is"+clientObj.toString());
			}

			OptionalActivityDefnVO optAct = ldInProgress.selectOptionalActivity(objId.intValue());
			if ( optAct == null ) {
					optAct = new OptionalActivityDefnVO();
					storeInSession(session, optAct);
					ldInProgress.getOptionalActivities().add(optAct);					
			} 
			updateOptionalActivity(session, clientObj, objId, optAct);
			m_allItems.put(objId, optAct);
			idsUpdated.add(objId);
			
		}
		
		// now delete all the old, untouched optional activities
		tempIterator = ldInProgress.getOptionalActivities().iterator();
		while ( tempIterator.hasNext() )
		{
			OptionalActivityDefnVO activity = (OptionalActivityDefnVO) tempIterator.next();
			Integer activityId = new Integer(activity.getId());
			if ( ! idsUpdated.contains(activityId) )
			{
				log.debug("Removing optional activity as no longer needed in design #"
					+ldInProgress.getSid()+" activity: "+activity.toString());
				tempIterator.remove();
				deleteOptionalActivity(session, activity);
			}
		}

		
	}



		
	*//** Update a multitask - this will contain inner tasks
	 *//*	
	protected void updateMultiTask(PersistenceSession session, Hashtable clientObj, MultiTaskVO multiTask, Integer objId) 
		throws Exception
	{
		Integer taskId = WDDXProcessor.convertToInteger("InputContentTask",
									clientObj.get(WDDXTAGS102.MTASK_INPUT_CONTENT_TASK));
		if ( ! LDWDDXValueObjectFactory.NUMERIC_NULL_VALUE_INTEGER.equals(objId) )
			multiTask.setInputContentTask(taskId);
		else
			multiTask.setInputContentTask(null);

		taskId = WDDXProcessor.convertToInteger("OutputContentTask",
									clientObj.get(WDDXTAGS102.MTASK_OUTPUT_CONTENT_TASK));
		if ( ! LDWDDXValueObjectFactory.NUMERIC_NULL_VALUE_INTEGER.equals(objId) )
			multiTask.setOutputContentTask(taskId);
		else
			multiTask.setOutputContentTask(null);
		
		multiTask.setTaskOrderOnScreen((String)clientObj.get(WDDXTAGS102.MTASK_SUBTASK_ORDER));
		
		Set subTasks = multiTask.getSubTasks();
		if ( subTasks == null ) {
			subTasks = new HashSet();
			multiTask.setSubTasks(subTasks);
			subTasks = multiTask.getSubTasks();
		}
		
		List subTasksWDDX = (List)clientObj.get(WDDXTAGS102.MTASK_SUBTASKS);
		if ( subTasksWDDX != null )
		{
			Iterator iter = subTasksWDDX.iterator();
			while ( iter.hasNext() )
			{
				
				Hashtable subTaskWDDX = (Hashtable) iter.next();

				String objectType = (String) subTaskWDDX.get(WDDXTAGS102.OBJECT_TYPE);
				if ( ! isTask(objectType)	)
				{
					throw new Exception(getExpectedTask() + "received "  +  objectType );
				}
				taskId = WDDXProcessor.convertToInteger("Task ID", subTaskWDDX.get(WDDXTAGS102.LD_ITEM_ID) );
				AbstractTaskVO subTask = multiTask.selectTaskById(taskId.intValue());
				if ( subTask == null ) {
					subTask = createNewTask( subTaskWDDX );
					storeInSession(session,subTask);
					subTasks.add(subTask);	
				}
				
				updateTask(session, subTaskWDDX, subTask, taskId );

			}
		}
	
	}


	*/
	
	/** Links the tasks that are done as a group to the group definition. 
	 * <p>
	 * throws exception with any errors
	 *//*
	protected void setGroupToTaskLinks() throws Exception
	{
		if ( log.isDebugEnabled() )
			log.debug("setGroupToTaskLinks: m_groupingsToDo="+(m_groupingsToDo!=null?m_groupingsToDo.toString():"null"));
			
		if ( m_groupingsToDo == null || m_groupingsToDo.isEmpty())
			return;

		Map allTasksById = ldInProgress.allTasks(false);
		Set keys = m_groupingsToDo.keySet();
		Iterator linksToProcess = keys.iterator();
		while ( linksToProcess.hasNext() )
		{
			Integer taskId = (Integer) linksToProcess.next();
			AbstractTaskVO task = (AbstractTaskVO) allTasksById.get(taskId);	
			if ( task == null )
			{
				String message = "Assigning grouping definition to a task. Internal error task not found. Task sid = "+taskId;
				log.error(message);
				throw new Exception (message);
			}

			Integer groupingTaskId = (Integer) m_groupingsToDo.get(taskId);
			
			log.debug("Processing group link, task id="+taskId+" groupingTaskId "+groupingTaskId);

			Object obj  = null;
			GroupingTaskVO groupingtask  = null;
			try {
				obj = allTasksById.get(groupingTaskId);
				groupingtask  = (GroupingTaskVO) obj;
			} catch ( ClassCastException cce )
			{
				String message = "Assigning grouping definition to a task. Grouping task not correct type. Task id = "+groupingTaskId
					+" expected to be grouping task, got "+obj.toString() 
					+" Was trying to find grouping task for task "+task.toString();
				log.error(message);
				throw new Exception (message);
			}			
			
			if ( groupingtask == null )
			{
				String message = "Assigning grouping definition to a task. Matching grouping task not found. "
					+ "Looking for grouping task "
					+ groupingTaskId
					+ " for task "
					+ task.toString();
				log.error(message);
				throw new Exception (message);
			}
			
			GroupDefnVO groupDefn = groupingtask.getGroupDefn();
			if ( groupDefn == null )
			{
				String message = "Assigning grouping definition to a task. Matching grouping task missing grouping defn. "
					+ "Grouping task "
					+ groupingtask.toString()
					+ " for task "
					+ task.toString();
				log.error(message);
				throw new Exception (message);
			}
	
			if ( log.isDebugEnabled() )
				log.debug("Adding group definition to groupsForTask. GroupDefn="+groupDefn);
				

			// finally, all is well so add definition link to task. Really should be
			// validating that the the grouping task is done first in the sequence but
			// haven't implemented that yet!		
			if ( task.getGroupsForTask() == null )
			{
				task.setGroupsForTask(new HashSet());
			}				
			
			task.getGroupsForTask().add(groupDefn);

			if ( log.isDebugEnabled() )
				log.debug("GroupsForTask is now ="+task.getGroupsForTask());
						
		}
	}

	*/
	
	/** 
	 * @param clientObj - hashtable from WDDX
	 * @param ui_id - Flash assigned id.
	 * @param transition - transition to update
	 */
	protected void processTransition( Hashtable clientObj) {
	
		try {
		    Transition transition = new Transition();
		    transition.setTransitionUIID(WDDXProcessor.convertToInteger("Transition ID", clientObj.get(WDDXTAGS102.LD_ITEM_ID)));
		    transition.setCreateDateTime(createDate);			
	
		    String completion = (String)clientObj.get(WDDXTAGS102.TRANSITION_COMPLETIONTYPE);
		    if ( WDDXTAGS102.TRANSITION_COMPLETION_SYNCRONIZE.equals(completion)) {
		    	// TODO create synchronise gate
		    } else if ( completion != null && completion.length() > 0 ) {
		    	// TODO create permission gate
		    }
	
			Integer toActivity = WDDXProcessor.convertToInteger("ToTaskActivities",clientObj.get(WDDXTAGS102.TRANSITION_TO_TASKS));
			if ( ! NUMERIC_NULL_VALUE_INTEGER.equals(toActivity) ) {
				Activity activity = getMatchingActivity(toActivity);
				if ( activity != null ) {
					transition.setToUIID(activity.getActivityUIID());
					transition.setToActivity(activity);
					activity.setTransitionTo(transition);
				}
			} 
			if ( transition.getToActivity() == null ) {
				String msg = "Can't find matching activity "+toActivity+" for transition. Transition will be missing in the design. Transition "+clientObj;
				log.error(msg);
				toolsErrorMsgs.add(msg);
				return;
			}
				
			Integer fromActivity = WDDXProcessor.convertToInteger("FromTaskActivities",clientObj.get(WDDXTAGS102.TRANSITION_FROM_TASKS));
			if ( ! NUMERIC_NULL_VALUE_INTEGER.equals(fromActivity) ) {
				Activity activity = getMatchingActivity(fromActivity);
				if ( activity != null ) {
					transition.setFromUIID(activity.getActivityUIID());
					transition.setFromActivity(activity);
					activity.setTransitionFrom(transition);
				}
			} 
			if ( transition.getFromActivity() == null ) {
				String msg = "Can't find matching activity "+fromActivity+" for transition. Transition will be missing in the design. Transition "+clientObj;
				log.error(msg);
				toolsErrorMsgs.add(msg);
				return;
			}
			
			transition.setLearningDesign(ldInProgress);		
			ldInProgress.getTransitions().add(transition);
		    baseDAO.insert(transition);

		} catch ( WDDXProcessorConversionException e ) {
			handleWDDXProcessorConversionException(e);
		}
	}

	/** 1.0.x has tasks within activities, so the UIID on the 2.0 activity is the UIID of the task but the transition may be looking for
	 * the UIID of the outer activity. So when we go looking for an activity we look for either the activity based on the activity or the 
	 * "child" task.
	 * @param uiID
	 * @return
	 */
	private Activity getMatchingActivity(Integer uiID) {
		
		Activity activity = newActivityMap.get(uiID);
		if ( activity == null ) {
			Integer childActivityId = flattenedActivityMap.get(uiID);
			if ( childActivityId!=null ) {
				activity = newActivityMap.get(childActivityId);
			}
		}
		return activity;
	}
/*
	protected void processTaskTransitions(PersistenceSession session, List newTT, ActivityDefnVO activity ) 
		throws Exception
	{	
		Set currentTasks = activity.getTasks();
		Set currentTransitions = activity.getTransitions();
		
		// much the same again - cycle through tasks and transitions. Update existing ones, add new ones
		
		Iterator iterator = newTT.iterator();
		while ( iterator.hasNext() )
		{
			Hashtable clientObj = (Hashtable) iterator.next();
			
			// check that it is the object we expect
			String objectType = (String) clientObj.get(WDDXTAGS102.OBJECT_TYPE);
			if ( ! ( isTask(objectType) || isTransition(objectType))	)
			{
				throw new Exception(getExpectedTaskTransition()+ " received "  +  objectType );
			}

			Integer objId = WDDXProcessor.convertToInteger("Task/Transition ID", clientObj.get(WDDXTAGS102.LD_ITEM_ID) );
			if ( LDWDDXValueObjectFactory.NUMERIC_NULL_VALUE_INTEGER.equals(objId) )
				throw new Exception("Id value for task/transition is internal null value. Task/transition is "+clientObj.toString());

			if ( isTask(objectType) ) {
				AbstractTaskVO task = activity.selectTaskById(objId.intValue());
				if ( task == null ) {
					task = createNewTask( clientObj );
					storeInSession(session,task);
					currentTasks.add(task);	
				} 
				updateTask(session, clientObj, task, objId );
				m_allItems.put(objId, task);
			} else {
				TransitionDefnVO transition = activity.selectTransitionbyId(objId.intValue());
				if ( transition == null ) {
					transition = new TransitionDefnVO();
					storeInSession(session,transition);
					currentTransitions.add(transition);					
				}
				updateTransition(clientObj, objId, transition);
				m_allItems.put(objId, transition);
			}
		}
		
	}

	*//** 
	 * update Optional Activity (OptionalActivityDefnVO)
	 * @param clientObj - hashtable from WDDX
	 * @param objId - internal id within learning design. will be 0 or 1 for library activities
	 * @param activity - optional activity to update
	 *//*
	protected void updateOptionalActivity(PersistenceSession session, Hashtable clientObj,Integer objId,
												OptionalActivityDefnVO optAct)
		throws Exception 
	{
		optAct.setId(objId.intValue());
		optAct.setDescription((String)clientObj.get(WDDXTAGS102.DESCRIPTION));
		optAct.setTitle((String)clientObj.get(WDDXTAGS102.TITLE));

		Integer toTask = WDDXProcessor.convertToInteger("Minimum number activities to complete",
							clientObj.get(WDDXTAGS102.OPTACT_MIN_NUMBER_COMPLETE));
		optAct.setMinNumberComplete(toTask);
		
		optAct.setXcoordStart(WDDXProcessor.convertToInteger("X co-ord start",
												clientObj.get(WDDXTAGS102.OPTACT_X_START)));
		optAct.setYcoordStart(WDDXProcessor.convertToInteger("Y co-ord start",
												clientObj.get(WDDXTAGS102.OPTACT_Y_START)));
		optAct.setXcoordEnd(WDDXProcessor.convertToInteger("X co-ord start",
												clientObj.get(WDDXTAGS102.OPTACT_X_END)));
		optAct.setYcoordEnd(WDDXProcessor.convertToInteger("Y co-ord start",
												clientObj.get(WDDXTAGS102.OPTACT_Y_END)));

		
		// activites, min # to compelte
		Set activities = optAct.getActivities();
		if (  activities == null )
		{
			optAct.setActivities(new HashSet());
		}					
		
		Vector activityIds = (Vector) clientObj.get(WDDXTAGS102.OPTACT_ACTIVITIES);
		if ( activityIds != null )
		{
			Iterator iter = activityIds.iterator();
			while ( iter.hasNext()) 
			{
				Integer id = WDDXProcessor.convertToInteger("Activity ID in Optional Activity", iter.next());
				ActivityDefnVO act = ldInProgress.selectActivity(id.intValue());
				if ( act == null )
				{
					log.error("Unable to find activity object to match optional activity definition. Optional activity is "
						+optAct.toString()	+" looking for activity id "	+id);
					throw new Exception("Unable to find activity object to match optional activity definition. "
						+" Optional activity id is "+ 
						+optAct.getId()	+" looking for activity id "	+id);
				}
				
				optAct.getActivities().add(act);
			}
		}
		
		// now create/update the dummy task used at runtime (never passed to authoring client)
		updateDummyTask(session, optAct);

	}

*/
	/** Create/Update a dummy task (stored using setDummyTask)
	 * @param optAct - Optional Activity that this dummy task belongs.
	 *//*
	public void updateDummyTask(PersistenceSession session, OptionalActivityDefnVO optAct) throws Exception
	{
		OptionalActivityDummyTaskVO result = optAct.getDummyTask();

		if ( result == null ) {
			result =  new OptionalActivityDummyTaskVO();
		}			
		result.setCompletion("");
		result.setDescription(optAct.getDescription());
		if ( result.getGroupsForTask() == null )
		{
			result.setGroupsForTask(new HashSet());
		}
		result.setId(optAct.getId()); 
		result.setTitle(optAct.getTitle());
		
		if ( result.getSubFirstTasks() == null )
		{
			result.setSubFirstTasks(new HashSet());
		}
		result.getSubFirstTasks().clear();
		
		if ( optAct.getActivities() != null )
		{
			Iterator iter = optAct.getActivities().iterator();
			while ( iter.hasNext())
			{
				ActivityDefnVO activity = (ActivityDefnVO) iter.next();	
				AbstractTaskVO task = activity.getFirstTask();
				if ( task != null )
				{
					result.getSubFirstTasks().add(task);
				}
			}
		}
		result.setOptionalActivity(optAct);
		
		storeInSession(session,result);
		//session.flush();
		
		optAct.setDummyTask(result);
	}
*/

	/** Get all the tool icons based on the library activities
	 * @return Map of tool icons - key is tool id, value is url. */
	private Map<Long, String> getLibraryActivityUiImages() {
		
		HashMap<Long, String> imageURLS = new HashMap<Long, String>();
		Iterator iterator = learningLibraryDAO.getAllLearningLibraries().iterator();
		while(iterator.hasNext()){
			LearningLibrary learningLibrary = (LearningLibrary)iterator.next();		
			List templateActivities = activityDAO.getActivitiesByLibraryID(learningLibrary.getLearningLibraryId());
			Iterator actIterator = templateActivities.iterator();
			while ( actIterator.hasNext() ) {
				Activity activity = (Activity) actIterator.next();
				if ( activity.isToolActivity() ) {
					ToolActivity toolActivity = (ToolActivity) activityDAO.getActivityByActivityId(activity.getActivityId(), ToolActivity.class);
					Tool tool = toolActivity.getTool();
					if ( ! imageURLS.containsKey(tool.getToolId()) ) {
						imageURLS.put(tool.getToolId(), activity.getLibraryActivityUiImage());
					}
				}
			}
		}
		return imageURLS;
	}
}
