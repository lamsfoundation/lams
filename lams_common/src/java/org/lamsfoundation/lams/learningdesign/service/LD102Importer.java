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

import java.security.GeneralSecurityException;
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
import org.lamsfoundation.lams.learningdesign.ComplexActivity;
import org.lamsfoundation.lams.learningdesign.GateActivity;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.LearningLibrary;
import org.lamsfoundation.lams.learningdesign.OptionsActivity;
import org.lamsfoundation.lams.learningdesign.ParallelActivity;
import org.lamsfoundation.lams.learningdesign.RandomGrouping;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.Transition;
import org.lamsfoundation.lams.learningdesign.dao.IActivityDAO;
import org.lamsfoundation.lams.learningdesign.dao.ILearningDesignDAO;
import org.lamsfoundation.lams.learningdesign.dao.ILearningLibraryDAO;
import org.lamsfoundation.lams.learningdesign.dto.ValidationErrorDTO;
import org.lamsfoundation.lams.tool.SystemTool;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.ToolContent;
import org.lamsfoundation.lams.tool.ToolContentImport102Manager;
import org.lamsfoundation.lams.tool.ToolImportSupport;
import org.lamsfoundation.lams.tool.dao.ISystemToolDAO;
import org.lamsfoundation.lams.tool.dao.IToolContentDAO;
import org.lamsfoundation.lams.tool.dao.IToolDAO;
import org.lamsfoundation.lams.tool.dao.IToolImportSupportDAO;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.wddx.WDDXProcessor;
import org.lamsfoundation.lams.util.wddx.WDDXProcessorConversionException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/** 
 * Create a learning design from the LAMS 1.0.x WDDX packet. Used by the Import action.
 * 
 * To be created using Spring, but it must not be a singleton as it has instance data.
 * Requires a Spring context so that it can communicate with the tools to create the
 * tool data.
 */
public class LD102Importer implements ApplicationContextAware{

	private static final String MSG_KEY_PERM_GATE = "imported.permission.gate.title";
	private static final String MSG_KEY_SYNC_GATE = "imported.synchronise.gate.title";
			
	private Logger log = Logger.getLogger(LD102Importer.class);
	private ApplicationContext context;
	private ILearningDesignService learningDesignService = null;
	private MessageService messageService = null;
	private IBaseDAO baseDAO = null;
	private ILearningLibraryDAO learningLibraryDAO = null;
	private ILearningDesignDAO learningDesignDAO = null;
	private IActivityDAO activityDAO = null;
	private IToolDAO toolDAO = null;
	private IToolImportSupportDAO toolImportSupportDAO = null;
	private IToolContentDAO toolContentDAO = null;
	private ISystemToolDAO systemToolDAO = null;
	private LearningDesign ldInProgress = null;
	private Integer maxId = null; 
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
	
	/** put aside all the optional activities as we find them and process after the first pass through the activities. */
	private Set<Hashtable> optionalActivitiesToDo = new HashSet<Hashtable>();
	/** put aside all the transitions as we find them and process them after the activities (including the optional activities). */
	private Set<Hashtable> transitionsToDo = new HashSet<Hashtable>();
	/** as we create an activity, stick it in this map so we can find it quickly later (ie while processing transitions). Key is the UI ID */
	private Map<Integer, Activity> newActivityMap = new HashMap<Integer, Activity>();
	/** we are "losing" a level of activity when we combined activity and task levels, so need to keep the relationships for getting the 
	 * right activity for a transition. Key is parent ui id, value is the child ui id
	 */ 
	private Map<Integer, Integer> flattenedActivityMap = new HashMap<Integer, Integer>();
	/** store the content by content id field, ready for use when the matching activity is processed */
	private Map<Integer, Hashtable> contentMap = new HashMap<Integer, Hashtable>();

	/** images to use for each tool, based on the library activities in the database. Key is tool id, value is url. */
	private Map<Long, String> libraryActivityUiImages;
	/** Map of the 1.0.2 tool signature (key) to the tool which now supports it (value) */
	private Map<String, Tool> toolImportSupport;

	/** The value to be past back and forth with the 1.0.x authoring tool to indicate a null value for a numeric id */
	private static final Integer NUMERIC_NULL_VALUE_INTEGER = new Integer(-1);
	
	private static final String LD_OBJECT_TYPE = "LearningDesign";
	private static final String ABSTRACT_TASK_OBJECT_TYPE = "task";
	private static final String MULTI_TASK_OBJECT_TYPE = "Multitask";
	private static final String TRANSITION_OBJECT_TYPE = "transition";
	private static final String ACTIVITY_OBJECT_TYPE = "Activity";
	private static final String OPTIONAL_OBJECT_TYPE = "optionalactivity";
	private static final String CONTENT_OBJECT_TYPE = "content";

   /**
     * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
     */
    public void setApplicationContext(ApplicationContext context) throws BeansException
    {
        this.context = context;
    }

	public void setLearningDesignService(ILearningDesignService learningDesignService) {
		this.learningDesignService = learningDesignService;
	}
	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}

	public void setBaseDAO (IBaseDAO baseDAO) {
		this.baseDAO = baseDAO;
	}
	public void setToolDAO (IToolDAO toolDAO) {
		this.toolDAO = toolDAO;
	}
	
	public void setSystemToolDAO(ISystemToolDAO systemToolDAO) {
		this.systemToolDAO = systemToolDAO;
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
				&& ! toolType.equalsIgnoreCase(ToolContentImport102Manager.TAGS_GROUPING) )
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
				&& toolType.equalsIgnoreCase(ToolContentImport102Manager.TAGS_GROUPING) )
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

	
	public Long storeLDDataWDDX(String ldWddxPacket, User importer, WorkspaceFolder folder, List<String> toolsErrorMsgs) throws ImportToolContentException
	{
		this.libraryActivityUiImages = getLibraryActivityUiImages();
		this.toolImportSupport = getToolImportSupport();
		this.toolsErrorMsgs = toolsErrorMsgs;
		if ( toolsErrorMsgs == null ) {
			log.warn("The list toolsErrorMsgs supplied is null so any warnings will be logged but won't appear in the user's screen.");
			toolsErrorMsgs = new ArrayList<String>();
		}
		
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

		if ( ! ldWddxPacket.startsWith("<wddxPacket") ) {
			// assume it is encrypted
			log.debug("Packet is probably encrypting. Attempting to decrypt.");
			try {
				ldWddxPacket = ImportExportUtil.decryptImport(ldWddxPacket);
			} catch (GeneralSecurityException e) {
				log.error("Unable to decrypt packet",e);
				throw new ImportToolContentException("Invalid packet format - failed decryption. See log for details.", e);
			}
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
		if ( newActivitiesTransitions == null ) {
			// log warning - no activities found in learning design.
			log.warn("No activities or transitions were found for  #"+ldInProgress.getLearningDesignId());
		} else {
			processActivitiesTransitions(newActivitiesTransitions);
		}
		
		for ( Hashtable optionTable : optionalActivitiesToDo) {
			setupOptionalActivity(optionTable);
		}
		ldInProgress.setFirstActivity(ldInProgress.calculateFirstActivity());
		ldInProgress.setMaxID(maxId);
		
		// all activities are set up so now we can do the transitions
		for ( Hashtable transTable: transitionsToDo ) {
			setupTransition(transTable);
		}
		
		// now set up the activity -> grouping defn links. Can't be done earlier as there
		// are the groupings are set up as the activities are processed.
		assignGroupings();

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
		
		// set up maxId locally and then save at the end of the process - the max id may need to be changed
		// when setting up the transitions to accomodate the addition of permission and sync gates.
		try {
			maxId = WDDXProcessor.convertToInt("Max ID", newLdHashTable.get(WDDXTAGS102.LD_MAXID));
			ld.setMaxID(maxId );
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

		ldInProgress = ld;
	}

	/** Process the activities and transitions. Returns a list of optional Activities yet to
	 * be processed.
	 */
	protected void processActivitiesTransitions(List newActivitiesTransitions)
	{	
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
				if ( NUMERIC_NULL_VALUE_INTEGER.equals(objId) ) {
					String message = "Id value for activity/transition is internal null value. The activity/transition may not appear in the correct place in the sequence. Activity/transition is "+clientObj;
					log.warn(message);
					toolsErrorMsgs.add(message);
				}
	
				if ( isActivity(objectType) ) {
					processActivity(clientObj, objId);
				}
				else if ( isOptionalActivity(objectType) ) {
					// put aside for processing later.
					optionalActivitiesToDo.add(clientObj);				
				}	
				else				{
					// put aside for processing later.
					transitionsToDo.add(clientObj);
				}
				
			} catch (WDDXProcessorConversionException e) {
				handleWDDXProcessorConversionException(e);
			}

		}
		
	}

	private void processActivity(Hashtable activityDetails, Integer activityUIID) throws WDDXProcessorConversionException {

		// Long systemID =  WDDXProcessor.convertToLong(activityDetails,WDDXTAGS102.SID); not needed
		Long nextTransition = WDDXProcessor.convertToLong(activityDetails,WDDXTAGS102.FOLLOWING_TRANSITION);
		Integer xCoOrd = WDDXProcessor.convertToInteger(activityDetails,WDDXTAGS102.ACT_X);
		Integer yCoOrd = WDDXProcessor.convertToInteger(activityDetails,WDDXTAGS102.ACT_Y);
		// String libraryID = WDDXProcessor.convertToString(activityDetails,WDDXTAGS102.ACT_LIBRARY_ID); not needed
		String title = WDDXProcessor.convertToString(activityDetails,WDDXTAGS102.TITLE);
		String description = WDDXProcessor.convertToString(activityDetails,WDDXTAGS102.DESCRIPTION);
		List taskTransitions = 	(List)activityDetails.get(WDDXTAGS102.ACT_TASKTRAN);
		
		if ( taskTransitions.size()==1 ) {
			// standard case - a single tool, grouping or parallel activity
			Hashtable task = (Hashtable) taskTransitions.get(0);
			createActivityFromTask(task, nextTransition, xCoOrd, yCoOrd, activityUIID, title, description);
			
		} else if ( taskTransitions.size()==3 ) {
			createReflectiveActivity(activityDetails, activityUIID, nextTransition, xCoOrd, yCoOrd, title, description, taskTransitions);
			
		} else {
			String message = "Unexpected activity format for activity "+ title +" expect either 1 or 3 tasks within an activity. This activity will be missing from the sequence. Activity "+activityDetails;
			log.warn(message);
			toolsErrorMsgs.add(message);
		}
		
	}

	/** Assume this is a "do something on one task/multi-task, reflect on it another multitask" 
	 * e.g. Chat & Scribe + Notebook Scribe + Notebook
	 * In all cases in 1.0.2 the second part should only be for the purposes of reflection
	 * so we just need to convert the first part and set the "journal" flag on the task that
	 * had the content output (as that is the one displayed on the following notebook).
	 */ 
	private Activity createReflectiveActivity(Hashtable activityDetails, Integer activityUIID, Long nextTransition, Integer xCoOrd, Integer yCoOrd, String title, String description, List taskTransitions) throws WDDXProcessorConversionException {
		Activity realTask = null;
		Activity reflectiveTask = null;
		String reflectiveTitle = null;
		String reflectiveDescription = null;

		Integer firstTaskId = WDDXProcessor.convertToInteger(activityDetails,WDDXTAGS102.ACT_FIRSTTASK);
		if ( firstTaskId == null ) {
			// hmmm, firstTaskId missing - try to derive it from the transition.
			Iterator iter = taskTransitions.iterator();
			while ( iter.hasNext() ) {
				Hashtable task = (Hashtable) iter.next();
				if ( isTransition((String) task.get(WDDXTAGS102.OBJECT_TYPE)) ) {
					firstTaskId =  WDDXProcessor.convertToInteger(task, WDDXTAGS102.TRANSITION_FROM_TASKS);
				}
			}
			
			if ( firstTaskId == null || NUMERIC_NULL_VALUE_INTEGER.equals(firstTaskId) ) {
				firstTaskId = null;
				String message = "Unable to determine the first task definition in a combined activity "+ title +". This activity will be missing from the sequence. Activity "+activityDetails;
				log.warn(message);
				toolsErrorMsgs.add(message);
				return null;
			} else {
				String message = "First task definition missing from activity "+ title +". Guessed it from the rest of the packet. This activity may be incorrect. Activity "+activityDetails;
				log.warn(message);
				toolsErrorMsgs.add(message);
			}
		}
		
		Iterator iter = taskTransitions.iterator();
		while ( iter.hasNext() ) {
			Hashtable task = (Hashtable) iter.next();
			String objectType = (String) task.get(WDDXTAGS102.OBJECT_TYPE);
			String toolType = (String) task.get(WDDXTAGS102.TASK_TOOLTYPE);
			
			if ( ! isTransition(objectType) ) {
				// Throw away the transition - we only keep the first activity so don't need the transition
				// So only process what we assume is a simple task or a multitask.
				Integer taskId = WDDXProcessor.convertToInteger(task,WDDXTAGS102.LD_ITEM_ID);
				if ( firstTaskId.equals(taskId) ) {
					
					// create the activity that we keep
					realTask = createActivityFromTask(task, nextTransition, xCoOrd, yCoOrd, activityUIID, title, description);

					Integer outputContentTaskId  = null;
					if ( isMultiTask(objectType, toolType) ) {
						outputContentTaskId = WDDXProcessor.convertToInteger(task, WDDXTAGS102.MTASK_OUTPUT_CONTENT_TASK);
						ComplexActivity cActivity = (ComplexActivity) realTask;
						if ( outputContentTaskId != null && ! NUMERIC_NULL_VALUE_INTEGER.equals(outputContentTaskId) ) {
							Iterator actIter = cActivity.getActivities().iterator();
							while (reflectiveTask==null && actIter.hasNext()) {
								Activity subActivity = (Activity) actIter.next();
								if ( outputContentTaskId.equals(subActivity.getActivityUIID()) ) {
									reflectiveTask = subActivity;
								}
							}
						}
					} else {
						reflectiveTask = realTask;
					}
					
					if ( reflectiveTask==null ) {
						String message = "Can't find the task in activity "+title+" that should be reflective. The activity will be created but the reflective part will be missing. Task is "+activityDetails;
						log.warn(message);
						toolsErrorMsgs.add(message);
					} 
					
				} else {
					
					// reflective part - just need the title and description - don't create matching activities
					List subTasks = (List) task.get(WDDXTAGS102.MTASK_SUBTASKS);
					Integer journalTaskInputContentId = null;
					if ( subTasks != null ) {
						Iterator subTaskIterator = subTasks.iterator();
						while (subTaskIterator.hasNext()) {
							Hashtable subTask = (Hashtable) subTaskIterator.next();
							if ( ToolContentImport102Manager.TAGS_JOURNAL.equals(subTask.get(WDDXTAGS102.TASK_TOOLTYPE)) ) {
								journalTaskInputContentId = WDDXProcessor.convertToInteger(subTask, WDDXTAGS102.TASK_INPUT_CONTENT);
							}
						}
					}
					
					Hashtable journalContent = null;
					if ( journalTaskInputContentId != null && ! NUMERIC_NULL_VALUE_INTEGER.equals(journalTaskInputContentId) ) {
						journalContent = contentMap.get(journalTaskInputContentId);
					} 
					
					if ( journalContent != null ) {
						reflectiveTitle = (String) journalContent.get(WDDXTAGS102.TITLE);
						reflectiveDescription = (String) journalContent.get(WDDXTAGS102.CONTENT_BODY);
					} else {
						String message = "Can't find the journal details in the reflective part of "+title+". The reflective settings in this activity will be missing. Task is "+task;
						log.warn(message);
						toolsErrorMsgs.add(message);
					}

				}
			}
		}
		
		if ( realTask == null ) {
			String message = "Couldn't find the \"real\" activity inside "+title+". This activity will be missing from the sequence. Activity "+activityDetails;
			log.warn(message);
			toolsErrorMsgs.add(message);
		} else if ( reflectiveTask == null ) {
			String message = "Couldn't find \"reflective\" activity inside "+title+". The reflective settings in this activity will be missing. ";
			log.warn(message);
			toolsErrorMsgs.add(message);
		} else {
			// TODO make the appropriate activity reflective. Don't have support for this in the tools yet!
			String message = "Activity "+reflectiveTask.getTitle()+" should be reflective ("+reflectiveTitle+":"+reflectiveDescription+") but this isn't supported in the tools yet. To be added when the tools are ready.";
			log.warn(message);
			toolsErrorMsgs.add(message);
		}
		
		return realTask;
	}

	/** Handles simple (including grouping), parallel and optional activities. Can cope with the sub activities but not
	 * transitions between activities. 
	 */ 
	private Activity createActivityFromTask(Hashtable taskDetails, Long nextTransition, Integer xCoOrd, Integer yCoOrd, 
			Integer parentActivityId, String parentTitle, String parentDescription) throws WDDXProcessorConversionException {
		
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

		if ( isMultiTask(objectType, toolType) ) {
			activity = setupParallelActivity(taskDetails, contentId, taskUIID, parentTitle, parentDescription);
		}
		else if ( isGroupingToolTask(objectType, toolType) ) {
			activity = setupGroupingActivity(taskDetails, contentId, taskUIID, parentActivityId);
		}
		else {
			activity = setupToolActivity(taskDetails, contentId, taskUIID);
		}
		
		if ( activity != null ) {
			processCommonActivityFields(taskDetails, xCoOrd, yCoOrd, activity);
			newActivityMap.put(activity.getActivityUIID(), activity);
			flattenedActivityMap.put(parentActivityId, activity.getActivityUIID());
			baseDAO.insert(activity);
		}
		
		return activity;
	}

	private void processCommonActivityFields(Hashtable taskDetails, Integer xCoOrd, Integer yCoOrd, Activity activity) throws WDDXProcessorConversionException {
		// Don't overwrite title and description if they have already be set up from the content. 
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
		
		activity.setOrderId(null); // if needed, will be set when the parent activity is created.
		
		activity.setLearningDesign(ldInProgress);
		activity.setCreateDateTime(createDate);
		activity.setRunOffline(Boolean.FALSE);
		
	}

	private Activity setupParallelActivity(Hashtable taskDetails, Integer contentId, Integer taskUIID, String parentTitle, String parentDescription) throws WDDXProcessorConversionException {
		ParallelActivity activity = (ParallelActivity) Activity.getActivityInstance(Activity.PARALLEL_ACTIVITY_TYPE);
		activity.setActivityTypeId(Activity.PARALLEL_ACTIVITY_TYPE);
		activity.setActivityCategoryID(Activity.CATEGORY_SYSTEM);
		activity.setActivityUIID(taskUIID);
		activity.setDescription(parentDescription);
		activity.setTitle(parentTitle);
		
		// create the child activities and set their order id
		List subTasks = (List) taskDetails.get(WDDXTAGS102.MTASK_SUBTASKS);
		String subTaskOrderString = (String ) taskDetails.get(WDDXTAGS102.MTASK_SUBTASK_ORDER); 
		if ( subTasks == null || subTasks.size() != 2) {
			String message = "Can't find two subtasks for the parallel activity "+parentTitle+". The parallel activity will not be in the sequence. Activity is "+taskDetails;
			log.warn(message);
			toolsErrorMsgs.add(message);
			return null;
		}
		String[] subTaskOrder = subTaskOrderString.split(",");
		int backupUnusedOrderId = subTaskOrder.length;
		
		Iterator iter = subTasks.iterator();
		while ( iter.hasNext() )
		{
			Hashtable subTask = (Hashtable) iter.next();
			Activity subActivity = createActivityFromTask(subTask, null, null, null, null, parentTitle, parentDescription);
			if ( subActivity != null ) {
				// won't be created if it can't find a matching tool.
				subActivity.setParentActivity(activity);
				subActivity.setParentUIID(activity.getActivityUIID());
				activity.addActivity(subActivity);
				
				Integer orderId = null;
				String subTaskUIID = subActivity.getActivityUIID()!=null ? subActivity.getActivityUIID().toString() : "";
				for ( int i=0; orderId==null && i<subTaskOrder.length; i++ ) {
					if ( subTaskOrder[i].equals(subTaskUIID) ) {
						orderId = new Integer(i);
					}
				}
				if ( orderId == null ) {
					// can't find its order in the task list so just assign it the next clear id. 
					orderId = backupUnusedOrderId++;
					String message = "Order of activities in parallel activity "+parentTitle+" can't be determined properly. The order on the screen may be wrong. Activity is "+taskDetails;
					log.warn(message);
					toolsErrorMsgs.add(message);
				}
				
				subActivity.setOrderId(orderId);
			}
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

	private ToolActivity setupToolActivity(Hashtable taskDetails, Integer wddxContentId, Integer taskUIID) {
		ToolActivity activity = new ToolActivity();
		
	    activity.setActivityUIID(taskUIID);
		activity.setActivityTypeId(Activity.TOOL_ACTIVITY_TYPE);
	    activity.setActivityCategoryID(Activity.CATEGORY_CONTENT);
		
	    // first, find the matching new tool and set up the tool, tool content details.  
	    String toolType = (String) taskDetails.get(WDDXTAGS102.TASK_TOOLTYPE);
	    String title = (String) taskDetails.get(WDDXTAGS102.TITLE);
	    Tool tool = toolType != null ? toolImportSupport.get(toolType) : null;
	    if ( tool == null ) {
			String message = "Unable to find a tool that supports the activity "+title+". This activity will be skipped. Activity is "+taskDetails;
			log.warn(message);
			toolsErrorMsgs.add(message);
	    	return null;
	    }

	    ToolContent newContent = new ToolContent(tool);
	    toolContentDAO.saveToolContent(newContent);
	    Long toolContentId = newContent.getToolContentId();
	    
	    activity.setTool(tool);	
	    activity.setToolContentId(toolContentId);
	    
		// Get the tool to create the "real" content */
	    Hashtable content = contentMap.get(wddxContentId);
	    if ( content != null ) {
	    	try {
	    		Boolean defineLater = WDDXProcessor.convertToBoolean(content, ToolContentImport102Manager.CONTENT_DEFINE_LATER);
	    		defineLater = defineLater != null ? defineLater : Boolean.FALSE;
	    		activity.setDefineLater(defineLater);
	    		
			    ToolContentImport102Manager toolService = (ToolContentImport102Manager) context.getBean(tool.getServiceName());
		    	toolService.import102ToolContent(toolContentId, ldInProgress.getUser().getUserId(), content);
	    	} catch ( Exception e ) {
		    	String message = "Tool content for activity "+title+" cannot be set up due to an error. Activity will be use the default content. Activity is "+taskDetails; 
				log.warn(message,e);
				toolsErrorMsgs.add(message);
	    	}
	    } else {
	    	String message = "Tool content for activity "+title+" is missing. Activity will be set up with default content. Activity is "+taskDetails; 
			log.warn(message);
			toolsErrorMsgs.add(message);
	    }

	    // Now find an icon for the activity. The icon is in the activity tables so look for a library activity that matches this tool.
	    // It may not always find the right icon if there is more than one possible match but its a start!
	    activity.setLibraryActivityUiImage(libraryActivityUiImages.get(tool.getToolId()));
		return activity;
	}


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



	/** Process the content from the WDDX packet. In 1.0.x, the content was managed by the 
	 * content manager. In 2.x it is managed by the tools. So we go through the content vector
	 * and build up a list of all the tool content. Then as we go through the activities 
	 * we will call the tool to set up its content.
	 * @throws WDDXProcessorConversionException 
	 */
	protected void processContent(List newContent) {	

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
				
			} catch ( WDDXProcessorConversionException e) {
				handleWDDXProcessorConversionException(e);
			}
		}
	}
	

	/** 
	 * Create an optional activity. Must process all the other activities first as the optional definition entry 
	 * only refers to the activities, it doesn't include them.
	 */ 
	private void setupOptionalActivity(Hashtable optionObj) {
		
		try {
			OptionsActivity optionsActivity = (OptionsActivity) Activity.getActivityInstance(Activity.OPTIONS_ACTIVITY_TYPE);
			optionsActivity.setActivityCategoryID(Activity.CATEGORY_SYSTEM);
			optionsActivity.setActivityUIID(WDDXProcessor.convertToInteger(optionObj, WDDXTAGS102.LD_ITEM_ID));
			if (keyExists(optionObj, WDDXTAGS102.OPTACT_MIN_NUMBER_COMPLETE))
				optionsActivity.setMinNumberOfOptions(WDDXProcessor.convertToInteger(optionObj,WDDXTAGS102.OPTACT_MIN_NUMBER_COMPLETE));
			optionsActivity.setMaxNumberOfOptions(null); // not supported in 1.0.2.
			optionsActivity.setOptionsInstructions(null); // not supported in 1.0.2. 
		
			Integer xCoOrd = WDDXProcessor.convertToInteger(optionObj,WDDXTAGS102.ACT_X);
			Integer yCoOrd = WDDXProcessor.convertToInteger(optionObj,WDDXTAGS102.ACT_Y);
			processCommonActivityFields(optionObj, xCoOrd, yCoOrd, optionsActivity);
		
			// work out the child activities and move them from the main activity set to the optional activity.
			Vector subActivityUIIDs = (Vector) optionObj.get(WDDXTAGS102.OPTACT_ACTIVITIES);
			int orderId = 1;
			if ( subActivityUIIDs != null ) {
				Iterator subActIter =  subActivityUIIDs.iterator();
				while ( subActIter.hasNext() ) {
					Integer id = WDDXProcessor.convertToInteger("Activity ID in Optional Activity", subActIter.next());
					Activity childActivity = getMatchingActivity(id);
					if ( childActivity == null ) {
						String message = "Activity inside optional activity "+optionsActivity.getTitle()+" cannot be matched to a known activity. The child activity will be missing from the optional activity but it may appear in the design elsewhere. Child activity UI ID "+id;
						log.warn(message);
						toolsErrorMsgs.add(message);
					} else { 
						optionsActivity.getActivities().add(childActivity);
						childActivity.setParentActivity(optionsActivity);
						childActivity.setParentUIID(optionsActivity.getActivityUIID());
						childActivity.setOrderId(new Integer(orderId++));
					}
				}
			}
			optionsActivity.setLearningDesign(ldInProgress);
			baseDAO.insert(optionsActivity);
			
			// don't need to put it in the flatten map as the optional activity is a single entity in the wddx packet, 
			// not a two parter like the other activities
			newActivityMap.put(optionsActivity.getActivityUIID(), optionsActivity);
			ldInProgress.getActivities().add(optionsActivity);
			
		} catch (WDDXProcessorConversionException e) {
			handleWDDXProcessorConversionException(e);
		}
		
	}

	
	/** 
	 * @param clientObj - hashtable from WDDX
	 * @param ui_id - Flash assigned id.
	 * @param transition - transition to update
	 */
	protected void setupTransition( Hashtable clientObj) {
	
		try {
		    Transition transition = new Transition();
		    GateActivity gate = null; // only needed for synchronise
		    Transition gateTransition = null; 
		    transition.setTransitionUIID(WDDXProcessor.convertToInteger("Transition ID", clientObj.get(WDDXTAGS102.LD_ITEM_ID)));
		    transition.setCreateDateTime(createDate);			

		    // if there is a completion type then we need to set up a gate and add an extra transition.
		    String completion = (String)clientObj.get(WDDXTAGS102.TRANSITION_COMPLETIONTYPE);
		    if ( completion != null && completion.length() > 0 ) {
			    gate = setupGateActivity(completion);
			    
			    gateTransition = new Transition();
		    	gateTransition.setTransitionUIID(maxId++);
		    	gate.setTransitionTo(gateTransition);
		    	gateTransition.setFromActivity(gate);
		    	gateTransition.setFromUIID(gate.getActivityUIID());
		    }
	
			Integer toActivity = WDDXProcessor.convertToInteger("ToTaskActivities",clientObj.get(WDDXTAGS102.TRANSITION_TO_TASKS));
			if ( ! NUMERIC_NULL_VALUE_INTEGER.equals(toActivity) ) {
				Activity activity = getMatchingActivity(toActivity);
				if ( activity != null ) {
					if ( gate != null ) {
						setToActivity(transition, gate);
						setToActivity(gateTransition, activity);
						// don't have x,y co-ords for the made up gate so make some that would overlap with the destination activity.
						int x = activity.getXcoord() != null ? activity.getXcoord() : 0;
						gate.setXcoord(x>=20 ? x-20 : 5);
						gate.setYcoord(activity.getYcoord());
						
					} else {
						setToActivity(transition, activity);
					}
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

			if ( gateTransition != null ) {
				gateTransition.setLearningDesign(ldInProgress);		
				ldInProgress.getTransitions().add(gateTransition);
				baseDAO.insert(gateTransition);
			}

		} catch ( WDDXProcessorConversionException e ) {
			handleWDDXProcessorConversionException(e);
		}
	}

	private GateActivity setupGateActivity(String completion) {
		
		GateActivity gate = null;
		Integer syncType = null; 
		SystemTool systemTool = null;
		Integer activityType = null;
		String title = null;
		
		if ( WDDXTAGS102.TRANSITION_COMPLETION_SYNCRONIZE.equals(completion)) {
			syncType = new Integer(Activity.SYNCH_GATE_ACTIVITY_TYPE);
			systemTool = systemToolDAO.getSystemToolByID(SystemTool.SYNC_GATE);
			activityType = new Integer(Activity.SYNCH_GATE_ACTIVITY_TYPE);
			title = messageService.getMessage(MSG_KEY_SYNC_GATE);
		} else {
			syncType = new Integer(Activity.PERMISSION_GATE_ACTIVITY_TYPE);
			systemTool = systemToolDAO.getSystemToolByID(SystemTool.PERMISSION_GATE);
			activityType = new Integer(Activity.PERMISSION_GATE_ACTIVITY_TYPE);
			title = messageService.getMessage(MSG_KEY_PERM_GATE);
		}
		
		if ( syncType != null ) {
			gate = (GateActivity) Activity.getActivityInstance(syncType.intValue());
			gate.setActivityTypeId(activityType.intValue());
			gate.setActivityCategoryID(Activity.CATEGORY_SYSTEM);
			gate.setSystemTool(systemTool);
			gate.setActivityUIID(maxId++);
			gate.setTitle(title!=null?title:"Gate");
			gate.setGateOpen(false);
			gate.setWaitingLearners(null);
			gate.setGateActivityLevelId(GateActivity.LEARNER_GATE_LEVEL);
			gate.setApplyGrouping(false); // not nullable so default to false
			gate.setGroupingSupportType(Activity.GROUPING_SUPPORT_OPTIONAL);
			gate.setOrderId(null); 
			gate.setDefineLater(Boolean.FALSE);
			gate.setCreateDateTime(createDate);
			gate.setRunOffline(Boolean.FALSE);
			gate.setLearningDesign(ldInProgress);
			
		    ldInProgress.getActivities().add(gate);
		    baseDAO.insert(gate);
		}
		return gate;
	}

	private void setToActivity(Transition transition, Activity activity) {
		transition.setToUIID(activity.getActivityUIID());
		transition.setToActivity(activity);
		activity.setTransitionTo(transition);
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
	
	/** Get all Map of the current tools that support the 1.0.2 signatures. */
	private Map<String, Tool> getToolImportSupport() {
		
		HashMap<String, Tool> supportedTools = new HashMap<String, Tool>();
	    Iterator iter = toolImportSupportDAO.getAllToolImportSupport().iterator();
	    while ( iter.hasNext() ) {
	    	ToolImportSupport support = (ToolImportSupport) iter.next();
	    	Tool tool = toolDAO.getToolBySignature(support.getInstalledToolSignature());
	    	if ( tool != null && tool.isValid() ) {
	    		supportedTools.put(support.getSupportsToolSignature(), tool);
	    	}
	    }
		return supportedTools;
	}

}
