/*******************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * ============================================================= License
 * Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2.0 as published by the
 * Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.authoring;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ActivityEvaluation;
import org.lamsfoundation.lams.learningdesign.ActivityOrderComparator;
import org.lamsfoundation.lams.learningdesign.BranchActivityEntry;
import org.lamsfoundation.lams.learningdesign.BranchCondition;
import org.lamsfoundation.lams.learningdesign.BranchingActivity;
import org.lamsfoundation.lams.learningdesign.ChosenGrouping;
import org.lamsfoundation.lams.learningdesign.Competence;
import org.lamsfoundation.lams.learningdesign.ComplexActivity;
import org.lamsfoundation.lams.learningdesign.ConditionGateActivity;
import org.lamsfoundation.lams.learningdesign.DataTransition;
import org.lamsfoundation.lams.learningdesign.FloatingActivity;
import org.lamsfoundation.lams.learningdesign.GateActivity;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.learningdesign.LearnerChoiceGrouping;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.LearningDesignAnnotation;
import org.lamsfoundation.lams.learningdesign.LearningLibrary;
import org.lamsfoundation.lams.learningdesign.License;
import org.lamsfoundation.lams.learningdesign.OptionsActivity;
import org.lamsfoundation.lams.learningdesign.OptionsWithSequencesActivity;
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
import org.lamsfoundation.lams.learningdesign.dao.IBranchActivityEntryDAO;
import org.lamsfoundation.lams.learningdesign.dao.ICompetenceDAO;
import org.lamsfoundation.lams.learningdesign.dao.ICompetenceMappingDAO;
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
import org.lamsfoundation.lams.tool.service.ILamsCoreToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;
import org.lamsfoundation.lams.util.AuthoringJsonTags;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.JsonUtil;

/**
 * @author Manpreet Minhas
 * @author Mailing Truong
 *
 *         This is a utility class for extracting the information from the JSON packet sent by Authoring.
 *
 *         The following rules are applied: The client sends a subset of all possible data. If a field is included, then
 *         the value associated with this field should be persisted (the value maybe a new value or an unchanged value)
 *         If a field is not included then the server should assume that the value is unchanged. If the value of a field
 *         is one of the special null values, then null should be persisted.
 *
 *         Object extractor has member data, so it should not be used as a singleton.
 *
 */
public class ObjectExtractor implements IObjectExtractor {

    public static final Integer DEFAULT_COORD = new Integer(10); // default coordinate used if the entry came from Flash
    // is 0 or less.

    protected IBaseDAO baseDAO = null;
    protected ILearningDesignDAO learningDesignDAO = null;
    protected IActivityDAO activityDAO = null;
    protected ICompetenceDAO competenceDAO = null;
    protected ICompetenceMappingDAO competenceMappingDAO = null;
    protected ITransitionDAO transitionDAO = null;
    protected ILearningLibraryDAO learningLibraryDAO = null;
    protected ILicenseDAO licenseDAO = null;
    protected IGroupingDAO groupingDAO = null;
    protected IToolDAO toolDAO = null;
    protected ISystemToolDAO systemToolDAO = null;
    protected IGroupDAO groupDAO = null;
    protected IToolSessionDAO toolSessionDAO = null;
    protected IBranchActivityEntryDAO branchActivityEntryDAO = null;
    protected ILamsCoreToolService lamsCoreToolService = null;

    private Integer mode = null;

    /*
     * The newActivityMap is a local copy of all the current activities. This will include the "top level" activities
     * and subactivities. It is used to "crossreference" activities as we go, without having to repull them from the
     * database. The keys are the UIIDs of the activities, not the IDs. It is important that the values in this map are
     * the Activity objects related to the Hibernate session as they are updated by the parseTransitions code.
     */
    protected HashMap<Integer, Activity> newActivityMap = new HashMap<Integer, Activity>();
    /*
     * Record the tool sessions and activities as we go for edit on fly. This is needed in case we delete any. Cannot
     * get them at the end as Hibernate tries to store the activities before getting the tool sessions, and this fails
     * due to a foriegn key error. Its the foriegn key error that we are trying to avoid!
     */
    protected HashMap<Integer, List<ToolSession>> toolSessionMap = new HashMap<Integer, List<ToolSession>>(); // Activity
    // UIID
    // ->
    // ToolSession
    /*
     * Used for easy access (rather than going back to the db) and makes it easier to clean them up at the end - they
     * can't be deleted easily via the cascades as we don't get a list of deleted entries sent back from the client! We
     * would end up with mappings still attached to a sequence activity that shouldn't be there! Not done as a map as we
     * sometimes will check via UIID, sometimes by ID
     */
    protected List<BranchActivityEntry> oldbranchActivityEntryList = new ArrayList<BranchActivityEntry>();

    protected HashMap<Integer, Activity> oldActivityMap = new HashMap<Integer, Activity>(); // Activity
    // UIID
    // ->
    // Activity
    /* cache of groupings - too hard to get them from the db */
    protected HashMap<Integer, Grouping> groupings = new HashMap<Integer, Grouping>();
    protected HashMap<Integer, Group> groups = new HashMap<Integer, Group>();
    protected HashMap<Integer, BranchActivityEntry> branchEntries = new HashMap<Integer, BranchActivityEntry>();
    protected HashMap<Integer, ComplexActivity> defaultActivityMap = new HashMap<Integer, ComplexActivity>();
    /*
     * can't delete as we go as they are linked to other items and have no way of knowing from the packet which ones
     * will need to be deleted, so start off assuming all need to be deleted and remove the ones we want to keep.
     */
    protected HashMap<Integer, Grouping> groupingsToDelete = new HashMap<Integer, Grouping>();
    protected LearningDesign learningDesign = null;
    protected Date modificationDate = null;
    /* cache of system tools so we aren't going back to the db all the time */
    protected HashMap<Long, SystemTool> systemTools = new HashMap<Long, SystemTool>();

    protected Logger log = Logger.getLogger(ObjectExtractor.class);

    /** Constructor to be used if Spring method injection is used */
    public ObjectExtractor() {
	modificationDate = new Date();
    }

    /** Spring injection methods */
    public IActivityDAO getActivityDAO() {
	return activityDAO;
    }

    public void setActivityDAO(IActivityDAO activityDAO) {
	this.activityDAO = activityDAO;
    }

    public ICompetenceDAO getCompetenceDAO() {
	return competenceDAO;
    }

    public void setCompetenceDAO(ICompetenceDAO competenceDAO) {
	this.competenceDAO = competenceDAO;
    }

    public ICompetenceMappingDAO getCompetenceMappingDAO() {
	return competenceMappingDAO;
    }

    public void setCompetenceMappingDAO(ICompetenceMappingDAO competenceMappingDAO) {
	this.competenceMappingDAO = competenceMappingDAO;
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

    public IBranchActivityEntryDAO getBranchActivityEntryDAO() {
	return branchActivityEntryDAO;
    }

    public void setBranchActivityEntryDAO(IBranchActivityEntryDAO branchActivityEntryDAO) {
	this.branchActivityEntryDAO = branchActivityEntryDAO;
    }

    public ILamsCoreToolService getLamsCoreToolService() {
	return lamsCoreToolService;
    }

    public void setLamsCoreToolService(ILamsCoreToolService lamsCoreToolService) {
	this.lamsCoreToolService = lamsCoreToolService;
    }

    @Override
    public LearningDesign extractSaveLearningDesign(JSONObject ldJSON, LearningDesign existingLearningDesign,
	    WorkspaceFolder workspaceFolder, User user) throws ObjectExtractorException, ParseException, JSONException {

	// if the learningDesign already exists, update it, otherwise create a
	// new one.
	learningDesign = existingLearningDesign != null ? existingLearningDesign : new LearningDesign();

	// Check the copy type. Can only update it if it is COPY_TYPE_NONE (ie
	// authoring copy)
	Integer copyTypeID = (Integer) JsonUtil.opt(ldJSON, AuthoringJsonTags.COPY_TYPE);
	if (copyTypeID == null) {
	    copyTypeID = LearningDesign.COPY_TYPE_NONE;
	}
	if ((learningDesign != null) && (learningDesign.getCopyTypeID() != null)
		&& !learningDesign.getCopyTypeID().equals(copyTypeID) && !learningDesign.getEditOverrideLock()) {
	    throw new ObjectExtractorException(
		    "Unable to save learning design.  Cannot change copy type on existing design.");
	}
	if (!copyTypeID.equals(LearningDesign.COPY_TYPE_NONE) && !learningDesign.getEditOverrideLock()) {
	    throw new ObjectExtractorException("Unable to save learning design.  Learning design is read-only");
	}
	learningDesign.setCopyTypeID(copyTypeID);

	learningDesign.setWorkspaceFolder(workspaceFolder);
	learningDesign.setUser(user);
	String contentFolderID = (String) JsonUtil.opt(ldJSON, AuthoringJsonTags.CONTENT_FOLDER_ID);
	if (contentFolderID == null) {
	    contentFolderID = FileUtil.generateUniqueContentFolderID();
	    ldJSON.put(AuthoringJsonTags.CONTENT_FOLDER_ID, contentFolderID);
	}
	learningDesign.setContentFolderID(contentFolderID);

	if (existingLearningDesign == null) {
	    Integer originalUserID = (Integer) JsonUtil.opt(ldJSON, AuthoringJsonTags.ORIGINAL_USER_ID);
	    if (originalUserID == null) {
		learningDesign.setOriginalUser(user);
	    } else {
		User originalUser = (User) getBaseDAO().find(User.class, originalUserID);
		learningDesign.setOriginalUser(originalUser);
	    }
	} else {
	    learningDesign.setOriginalUser(existingLearningDesign.getOriginalUser());
	}

	// Pull out all the existing groups. there isn't an easy way to pull
	// them out of the db requires an outer join across
	// three objects (learning design -> grouping activity -> grouping) so
	// put both the existing ones and the new ones
	// here for reference later.
	initialiseGroupings();

	// Branch activity mappings are indirectly accessible via sequence
	// activities or groupings. Have a separate list
	// to make them easier to delete unwanted ones later.
	intialiseBranchActivityMappings();

	// get a mapping of all the existing activities and their tool sessions,
	// in case we need to delete some tool sessions later.
	initialiseToolSessionMap(learningDesign);

	// get the core learning design stuff - default to invalid
	learningDesign.setValidDesign(JsonUtil.opt(ldJSON, AuthoringJsonTags.VALID_DESIGN, false));
	learningDesign.setLearningDesignUIID((Integer) JsonUtil.opt(ldJSON, AuthoringJsonTags.LEARNING_DESIGN_UIID));
	learningDesign.setDescription((String) JsonUtil.opt(ldJSON, AuthoringJsonTags.DESCRIPTION));
	learningDesign.setTitle((String) JsonUtil.opt(ldJSON, AuthoringJsonTags.TITLE));
	learningDesign.setMaxID((Integer) JsonUtil.opt(ldJSON, AuthoringJsonTags.MAX_ID));
	learningDesign.setReadOnly((Boolean) JsonUtil.opt(ldJSON, AuthoringJsonTags.READ_ONLY));
	learningDesign.setEditOverrideLock((Boolean) JsonUtil.opt(ldJSON, AuthoringJsonTags.EDIT_OVERRIDE_LOCK));
	learningDesign.setDateReadOnly(
		DateUtil.convertFromString((String) JsonUtil.opt(ldJSON, AuthoringJsonTags.DATE_READ_ONLY)));
	learningDesign.setHelpText((String) JsonUtil.opt(ldJSON, AuthoringJsonTags.HELP_TEXT));
	String version = (String) JsonUtil.opt(ldJSON, AuthoringJsonTags.VERSION);
	if (version == null) {
	    version = Configuration.get(ConfigurationKeys.SERVER_VERSION_NUMBER);
	}
	learningDesign.setDesignType((String) JsonUtil.opt(ldJSON, AuthoringJsonTags.DESIGN_TYPE));
	learningDesign.setVersion(version);
	learningDesign.setDuration(JsonUtil.optLong(ldJSON, AuthoringJsonTags.DURATION));

	mode = (Integer) JsonUtil.opt(ldJSON, AuthoringJsonTags.SAVE_MODE);

	// Set creation date and modification date based on the server timezone,
	// not the client.
	if (learningDesign.getCreateDateTime() == null) {
	    learningDesign.setCreateDateTime(modificationDate);
	}
	learningDesign.setLastModifiedDateTime(modificationDate);

	Long licenseID = JsonUtil.optLong(ldJSON, AuthoringJsonTags.LICENCE_ID);
	if (licenseID != null) {
	    License license = licenseDAO.getLicenseByID(licenseID);
	    learningDesign.setLicense(license);
	}
	learningDesign.setLicenseText((String) JsonUtil.opt(ldJSON, AuthoringJsonTags.LICENSE_TEXT));

	Long originalLearningDesignID = JsonUtil.optLong(ldJSON, AuthoringJsonTags.ORIGINAL_DESIGN_ID);
	if (originalLearningDesignID != null) {
	    LearningDesign originalLearningDesign = learningDesignDAO.getLearningDesignById(originalLearningDesignID);
	    learningDesign.setOriginalLearningDesign(originalLearningDesign);
	}

	learningDesignDAO.insertOrUpdate(learningDesign);

	// now process the "parts" of the learning design
	parseGroupings((JSONArray) JsonUtil.opt(ldJSON, AuthoringJsonTags.GROUPINGS));
	parseActivities((JSONArray) JsonUtil.opt(ldJSON, AuthoringJsonTags.ACTIVITIES));
	parseActivitiesToMatchUpParentandInputActivities(
		(JSONArray) JsonUtil.opt(ldJSON, AuthoringJsonTags.ACTIVITIES));
	parseTransitions((JSONArray) JsonUtil.opt(ldJSON, AuthoringJsonTags.TRANSITIONS));
	parseBranchMappings((JSONArray) JsonUtil.opt(ldJSON, AuthoringJsonTags.BRANCH_MAPPINGS));
	parseAnnotations((JSONArray) JsonUtil.opt(ldJSON, AuthoringJsonTags.ANNOTATIONS));

	progressDefaultChildActivities();

	learningDesign.setFirstActivity(learningDesign.calculateFirstActivity());
	learningDesign.setFloatingActivity(learningDesign.calculateFloatingActivity());
	learningDesignDAO.insertOrUpdate(learningDesign);

	deleteUnwantedGroupings();
	deleteUnwantedToolSessions(learningDesign);
	learningDesignDAO.insertOrUpdate(learningDesign);
	return learningDesign;
    }

    /**
     * Link SequenceActivities up with their firstActivity entries and BranchingActivities with their default branch.
     * Also tidy up the order ids for sequence activities so that they are in the same order as the transitions - needed
     * for the IMSLD export conversion to work. Not all the transitions may be drawn yet, so number off the others best
     * we can.
     *
     * @throws ObjectExtractorException
     */
    private void progressDefaultChildActivities() throws ObjectExtractorException {

	if (defaultActivityMap.size() > 0) {
	    for (Integer defaultChildUIID : defaultActivityMap.keySet()) {
		ComplexActivity complex = defaultActivityMap.get(defaultChildUIID);
		Activity defaultActivity = newActivityMap.get(defaultChildUIID);
		if (defaultActivity == null) {
		    String msg = "Unable to find the default child activity (" + defaultChildUIID
			    + ") for the activity (" + complex + ") referred to in First Child to Sequence map.";
		    throw new ObjectExtractorException(msg);
		} else {
		    complex.setDefaultActivity(defaultActivity);

		    // fix up the order ids for SequenceActivities
		    if (complex.isSequenceActivity()) {
			Set unprocessedChildren = new TreeSet(new ActivityOrderComparator());
			unprocessedChildren.addAll(complex.getActivities());

			unprocessedChildren.remove(defaultActivity);
			defaultActivity.setOrderId(1);

			int nextOrderId = 2;
			Activity nextActivity = defaultActivity.getTransitionFrom() != null
				? defaultActivity.getTransitionFrom().getToActivity() : null;
			while (nextActivity != null) {
			    boolean removed = unprocessedChildren.remove(nextActivity);
			    if (!removed) {
				log.error(
					"Next activity should be a child of the current sequence, but it isn't. Could we have a loop in the transitions? Aborting the ordering of ids based on transitions. Sequence activity "
						+ complex + " next activity " + nextActivity);
				break;
			    }
			    nextActivity.setOrderId(nextOrderId++);
			    nextActivity = nextActivity.getTransitionFrom() != null
				    ? nextActivity.getTransitionFrom().getToActivity() : null;
			}

			if (unprocessedChildren.size() > 0) {
			    Iterator iter = unprocessedChildren.iterator();
			    while (iter.hasNext()) {
				nextActivity = (Activity) iter.next();
				nextActivity.setOrderId(nextOrderId++);
			    }
			}
		    }
		}
	    }

	}
    }

    /**
     * Initialise the map of groupings with those in the db from a previous save. This must be called as soon as the
     * learning design is read from the db and before it is changed.
     */
    private void initialiseGroupings() {
	List dbGroupings = groupingDAO.getGroupingsByLearningDesign(learningDesign.getLearningDesignId());
	Iterator iter = dbGroupings.iterator();
	while (iter.hasNext()) {
	    Grouping grouping = (Grouping) iter.next();
	    groupings.put(grouping.getGroupingUIID(), grouping);
	    groupingsToDelete.put(grouping.getGroupingUIID(), grouping);
	}
    }

    private void intialiseBranchActivityMappings() {
	oldbranchActivityEntryList = branchActivityEntryDAO
		.getEntriesByLearningDesign(learningDesign.getLearningDesignId());
    }

    /**
     * Initialise the map of tool sessions already in the database. Used to work out what will be deleted by Hibernate
     * later - useful to clean up any unwanted tool sessions for edit on the fly.
     */
    @SuppressWarnings("unchecked")
    private void initialiseToolSessionMap(LearningDesign learningDesign) {
	if (learningDesign.getEditOverrideLock() && (learningDesign.getEditOverrideUser() != null)) {
	    Iterator iter = learningDesign.getActivities().iterator();
	    while (iter.hasNext()) {
		Activity activity = (Activity) iter.next();
		oldActivityMap.put(activity.getActivityUIID(), activity);
		List<ToolSession> toolSessions = toolSessionDAO.getToolSessionByActivity(activity);
		if ((toolSessions != null) && (toolSessions.size() > 0)) {
		    toolSessionMap.put(activity.getActivityUIID(), toolSessions);
		}
	    }
	}
    }

    /** Delete the old unneeded groupings. Won't be done via a cascade */
    private void deleteUnwantedGroupings() {
	for (Grouping grouping : groupingsToDelete.values()) {
	    groupingDAO.delete(grouping);
	}
    }

    /**
     * Delete the old tool session. Won't be done via Hibernate cascades as we only want to do it for edit on fly. The
     * progress engine pre-generates the tool sessions for class level activities, so if we edit the design, we need to
     * delete the tool sessions. If we encounter evidence that this is a grouped activity - either more than one tool
     * session exists or the activity is grouped, then abort.
     */
    private void deleteUnwantedToolSessions(LearningDesign learningDesign) throws ObjectExtractorException {
	if (learningDesign.getEditOverrideLock() && (learningDesign.getEditOverrideUser() != null)) {

	    for (Integer uiid : toolSessionMap.keySet()) {
		if (!newActivityMap.containsKey(uiid)) {
		    List toolSessions = toolSessionMap.get(uiid);
		    if (toolSessions != null) {

			Activity activity = oldActivityMap.get(uiid);
			if (toolSessions.size() > 1) {
			    throw new ObjectExtractorException("More than one tool session exists for activity "
				    + activity.getTitle() + " (" + uiid
				    + ") but this shouldn't be possible. Cannot delete this tool session so editing is not allowed!");
			} else if (toolSessions.size() == 1) {

			    ToolSession toolSession = (ToolSession) toolSessions.get(0);
			    if (activity.isGroupingActivity()) {
				throw new ObjectExtractorException("Activity " + activity.getTitle() + " ("
					+ activity.getActivityUIID()
					+ ") has a tool session but it is grouped. Cannot delete this tool session so editing is not allowed!");
			    }

			    // all okay, do ahead and delete the tool session
			    if (log.isDebugEnabled()) {
				log.debug("Removing tool session for activity " + activity.getTitle() + " ("
					+ activity.getActivityUIID() + ")");
			    }

			    toolSessionDAO.removeToolSession(toolSession);
			}
		    }
		}
	    }

	}
    }

    private void parseGroupings(JSONArray groupingsList) throws JSONException {
	// iterate through the list of groupings objects
	// each object should contain information groupingUUID, groupingID,
	// groupingTypeID
	if (groupingsList != null) {
	    for (int groupingIndex = 0; groupingIndex < groupingsList.length(); groupingIndex++) {
		JSONObject groupingDetails = groupingsList.getJSONObject(groupingIndex);
		if (groupingDetails != null) {
		    Grouping grouping = extractGroupingObject(groupingDetails);
		    groupingDAO.insertOrUpdate(grouping);
		    groupings.put(grouping.getGroupingUIID(), grouping);
		}
	    }
	}
    }

    public Grouping extractGroupingObject(JSONObject groupingDetails) throws JSONException {
	Integer groupingUIID = (Integer) JsonUtil.opt(groupingDetails, "groupingUIID");
	Integer groupingTypeID = (Integer) JsonUtil.opt(groupingDetails, "groupingTypeID");

	Grouping grouping = groupings.get(groupingUIID);
	// check that the grouping type is still okay - if it is we keep the
	// grouping otherwise
	// we get rid of the old hibernate object.
	if (grouping != null) {
	    if (grouping.getGroupingTypeId().equals(groupingTypeID)) {
		groupingsToDelete.remove(groupingUIID);
	    } else {
		groupings.remove(grouping.getGroupingUIID());
		grouping = null;
	    }
	}

	if (grouping == null) {
	    Object object = Grouping.getGroupingInstance(groupingTypeID);
	    grouping = (Grouping) object;
	    grouping.setGroupingUIID(groupingUIID);
	}

	if (grouping.isRandomGrouping()) {
	    createRandomGrouping((RandomGrouping) grouping, groupingDetails);
	} else if (grouping.isChosenGrouping()) {
	    createChosenGrouping((ChosenGrouping) grouping, groupingDetails);
	}

	else if (grouping.isLearnerChoiceGrouping()) {
	    createLearnerChoiceGrouping((LearnerChoiceGrouping) grouping, groupingDetails);
	} else {
	    createLessonClass((LessonClass) grouping, groupingDetails);
	}

	grouping.setMaxNumberOfGroups((Integer) JsonUtil.opt(groupingDetails, "maxNumberOfGroups"));

	Set<Group> groupsToDelete = new HashSet<Group>(grouping.getGroups());

	JSONArray groupsList = (JSONArray) JsonUtil.opt(groupingDetails, "groups");
	if (groupsList != null) {
	    for (int groupIndex = 0; groupIndex < groupsList.length(); groupIndex++) {
		JSONObject groupDetails = groupsList.getJSONObject(groupIndex);
		Group group = extractGroupObject(groupDetails, grouping);
		groups.put(group.getGroupUIID(), group);
		groupsToDelete.remove(group);
	    }
	}

	if (groupsToDelete.size() > 0) {
	    Iterator iter = groupsToDelete.iterator();
	    while (iter.hasNext()) {
		Group group = (Group) iter.next();
		if (group.getBranchActivities() != null) {
		    Iterator branchIter = group.getBranchActivities().iterator();
		    while (branchIter.hasNext()) {
			BranchActivityEntry entry = (BranchActivityEntry) branchIter.next();
			entry.setGroup(null);
		    }
		    group.getBranchActivities().clear();
		}
		grouping.getGroups().remove(group);
	    }
	}
	return grouping;
    }

    private Group extractGroupObject(JSONObject groupDetails, Grouping grouping) throws JSONException {

	Group group = null;
	Integer groupUIID = groupDetails.getInt(AuthoringJsonTags.GROUP_UIID);
	Long groupID = JsonUtil.optLong(groupDetails, AuthoringJsonTags.GROUP_ID);

	// Does it exist already? If the group was created at runtime, there
	// will be an ID but no IU ID field.
	// If it was created in authoring, will have a UI ID and may or may not
	// have an ID.
	// So try to match up on UI ID first, failing that match on ID. Then the
	// worst case, which is the group
	// is created at runtime but then modified in authoring (and has has a
	// new UI ID added) is handled.
	if ((grouping.getGroups() != null) && (grouping.getGroups().size() > 0)) {
	    Group uiid_match = null;
	    Group id_match = null;
	    Iterator iter = grouping.getGroups().iterator();
	    while ((uiid_match == null) && iter.hasNext()) {
		Group possibleGroup = (Group) iter.next();
		if (groupUIID.equals(possibleGroup.getGroupUIID())) {
		    uiid_match = possibleGroup;
		}
		if ((groupID != null) && groupID.equals(possibleGroup.getGroupId())) {
		    id_match = possibleGroup;
		}
	    }
	    group = uiid_match != null ? uiid_match : id_match;
	}

	if (group == null) {
	    group = new Group();
	    grouping.getGroups().add(group);
	}

	group.setGroupName((String) JsonUtil.opt(groupDetails, AuthoringJsonTags.GROUP_NAME));
	group.setGrouping(grouping);
	group.setGroupUIID(groupUIID);
	group.setOrderId(JsonUtil.opt(groupDetails, AuthoringJsonTags.ORDER_ID, 0));

	return group;
    }

    private void createRandomGrouping(RandomGrouping randomGrouping, JSONObject groupingDetails) throws JSONException {
	// the two settings are mutually exclusive. Authoring takes care of this,
	// but we'll code it here too just to make sure.
	Integer numLearnersPerGroup = (Integer) JsonUtil.opt(groupingDetails, AuthoringJsonTags.LEARNERS_PER_GROUP);
	if ((numLearnersPerGroup != null) && (numLearnersPerGroup.intValue() > 0)) {
	    randomGrouping.setLearnersPerGroup(numLearnersPerGroup);
	    randomGrouping.setNumberOfGroups(null);
	} else {
	    Integer numGroups = (Integer) JsonUtil.opt(groupingDetails, AuthoringJsonTags.NUMBER_OF_GROUPS);
	    if ((numGroups != null) && (numGroups.intValue() > 0)) {
		randomGrouping.setNumberOfGroups(numGroups);
	    } else {
		randomGrouping.setNumberOfGroups(null);
	    }
	    randomGrouping.setLearnersPerGroup(null);
	}
    }

    private void createChosenGrouping(ChosenGrouping chosenGrouping, JSONObject groupingDetails) {
	// no extra properties as yet
    }

    private void parseActivities(JSONArray activitiesList) throws ObjectExtractorException, JSONException {

	if (activitiesList != null) {
	    for (int activityIndex = 0; activityIndex < activitiesList.length(); activityIndex++) {
		JSONObject activityDetails = activitiesList.getJSONObject(activityIndex);
		Activity activity = extractActivityObject(activityDetails);
		activityDAO.insertOrUpdate(activity);

		// if its a tool activity, extract evaluation details
		if (activity.isToolActivity()) {
		    extractEvaluationObject(activityDetails, (ToolActivity) activity);
		}

		newActivityMap.put(activity.getActivityUIID(), activity);
	    }
	}

	// clear the transitions.
	// clear the old set and reset up the activities set. Done this way to
	// keep the Hibernate cascading happy.
	// this means we don't have to manually remove the transition object.
	// Note: This will leave orphan content in the tool tables. It can be
	// removed by the tool content cleaning job,
	// which may be run from the admin screen or via a cron job.

	learningDesign.getActivities().clear();
	learningDesign.getActivities().addAll(newActivityMap.values());

	// TODO: Need to double check that the toolID/toolContentID combinations
	// match entries in lams_tool_content table, or put FK on table.
	learningDesignDAO.insertOrUpdate(learningDesign);
    }

    private void extractEvaluationObject(JSONObject activityDetails, ToolActivity toolActivity)
	    throws ObjectExtractorException, JSONException {

	Set<ActivityEvaluation> activityEvaluations = toolActivity.getActivityEvaluations();
	ActivityEvaluation activityEvaluation;

	// Get the first (only) ActivityEvaluation if it exists
	if ((activityEvaluations != null) && (activityEvaluations.size() >= 1)) {
	    activityEvaluation = activityEvaluations.iterator().next();
	} else {
	    activityEvaluation = new ActivityEvaluation();
	}

	String toolOutputDefinition = (String) JsonUtil.opt(activityDetails, AuthoringJsonTags.TOOL_OUTPUT_DEFINITION);
	if (!StringUtils.isBlank(toolOutputDefinition)) {
	    activityEvaluations = new HashSet<ActivityEvaluation>();
	    activityEvaluation.setActivity(toolActivity);
	    activityEvaluation.setToolOutputDefinition(toolOutputDefinition);
	    activityEvaluations.add(activityEvaluation);
	    toolActivity.setActivityEvaluations(activityEvaluations);
	    baseDAO.insertOrUpdate(activityEvaluation);

	    // update the parent toolActivity
	    toolActivity.setActivityEvaluations(activityEvaluations);
	    activityDAO.insertOrUpdate(toolActivity);

	} else if (activityEvaluation.getUid() != null) {
	    // update the parent toolActivity
	    toolActivity.setActivityEvaluations(new HashSet<ActivityEvaluation>());
	    activityDAO.insertOrUpdate(toolActivity);
	    baseDAO.delete(activityEvaluation);
	}
    }

    private void parseCompetences(JSONArray competenceList) throws ObjectExtractorException, JSONException {
	Set<Competence> existingCompetences = learningDesign.getCompetences();
	if (competenceList != null) {
	    for (int competenceIndex = 0; competenceIndex < competenceList.length(); competenceIndex++) {
		JSONObject competeneJSON = competenceList.getJSONObject(competenceIndex);
		String title = competeneJSON.getString(AuthoringJsonTags.TITLE);
		String description = competeneJSON.getString(AuthoringJsonTags.DESCRIPTION);

		if (getComptenceFromSet(existingCompetences, title) != null) {
		    Competence updateCompetence = getComptenceFromSet(existingCompetences, title);
		    updateCompetence.setDescription(description);
		    competenceDAO.saveOrUpdate(updateCompetence);
		} else {
		    Competence newCompetence = new Competence();
		    newCompetence.setTitle(title);
		    newCompetence.setDescription(description);
		    newCompetence.setLearningDesign(learningDesign);
		    competenceDAO.saveOrUpdate(newCompetence);
		}
	    }

	    // now go through and delete any competences from the old list,
	    // that dont exist in the new list.
	    Set<Competence> removeCompetences = new HashSet<Competence>();
	    if (existingCompetences != null) {
		if ((competenceList != null) && (competenceList.length() > 0)) {
		    for (Competence existingCompetence : existingCompetences) {
			boolean remove = true;
			for (int competenceIndex = 0; competenceIndex < competenceList.length(); competenceIndex++) {
			    if (existingCompetence.getTitle().equals(
				    competenceList.getJSONObject(competenceIndex).getString(AuthoringJsonTags.TITLE))) {
				remove = false;
				break;
			    }
			}

			if (remove) {
			    removeCompetences.add(existingCompetence);
			}
		    }
		} else {
		    removeCompetences.addAll(existingCompetences);
		}
		// competenceDAO.deleteAll(removeCompetences);
		learningDesign.getCompetences().removeAll(removeCompetences);
	    }
	}
    }

    private void parseAnnotations(JSONArray annotationList) throws ObjectExtractorException, JSONException {
	if (annotationList == null) {
	    return;
	}

	Set<LearningDesignAnnotation> existingAnnotations = learningDesign.getAnnotations();
	if (existingAnnotations == null) {
	    existingAnnotations = new HashSet<LearningDesignAnnotation>();
	    learningDesign.setAnnotations(existingAnnotations);
	}
	Set<LearningDesignAnnotation> updatedAnnotations = new HashSet<LearningDesignAnnotation>();

	for (int annotationIndex = 0; annotationIndex < annotationList.length(); annotationIndex++) {
	    JSONObject annotationJSON = annotationList.getJSONObject(annotationIndex);
	    boolean found = false;
	    LearningDesignAnnotation annotation = null;

	    for (LearningDesignAnnotation existingAnnotation : existingAnnotations) {
		if (existingAnnotation.getAnnotationUIID()
			.equals(annotationJSON.getInt(AuthoringJsonTags.ANNOTATION_UIID))) {
		    annotation = existingAnnotation;
		    found = true;
		    break;
		}
	    }

	    if (annotation == null) {
		annotation = new LearningDesignAnnotation();
	    }
	    annotation.setLearningDesignId(learningDesign.getLearningDesignId());
	    annotation.setAnnotationUIID(annotationJSON.getInt(AuthoringJsonTags.ANNOTATION_UIID));
	    annotation.setTitle((String) JsonUtil.opt(annotationJSON, AuthoringJsonTags.TITLE));
	    annotation.setXcoord((Integer) JsonUtil.opt(annotationJSON, AuthoringJsonTags.XCOORD));
	    annotation.setYcoord((Integer) JsonUtil.opt(annotationJSON, AuthoringJsonTags.YCOORD));
	    annotation.setEndXcoord((Integer) JsonUtil.opt(annotationJSON, AuthoringJsonTags.END_XCOORD));
	    annotation.setEndYcoord((Integer) JsonUtil.opt(annotationJSON, AuthoringJsonTags.END_YCOORD));
	    annotation.setColor((String) JsonUtil.opt(annotationJSON, AuthoringJsonTags.COLOR));

	    if (found) {
		baseDAO.update(annotation);
	    } else {
		baseDAO.insert(annotation);
	    }

	    updatedAnnotations.add(annotation);
	}

	learningDesign.getAnnotations().clear();
	learningDesign.getAnnotations().addAll(updatedAnnotations);
    }

    private Competence getComptenceFromSet(Set<Competence> competences, String title) {
	if (competences != null) {
	    for (Competence competence : competences) {
		if (competence.getTitle().equals(title)) {
		    return competence;
		}
	    }

	}
	return null;
    }

    private void parseActivitiesToMatchUpParentandInputActivities(JSONArray activitiesList)
	    throws ObjectExtractorException, JSONException {
	if (activitiesList != null) {
	    for (int activityIndex = 0; activityIndex < activitiesList.length(); activityIndex++) {
		JSONObject activityDetails = activitiesList.getJSONObject(activityIndex);

		Integer activityUUID = activityDetails.getInt(AuthoringJsonTags.ACTIVITY_UIID);
		Activity existingActivity = newActivityMap.get(activityUUID);

		// match up activity to parent based on UIID
		Integer parentUIID = (Integer) JsonUtil.opt(activityDetails, AuthoringJsonTags.PARENT_UIID);
		if (parentUIID != null) {

		    Activity parentActivity = newActivityMap.get(parentUIID);
		    if (parentActivity == null) {
			throw new ObjectExtractorException("Parent activity " + parentUIID + " missing for activity "
				+ existingActivity.getTitle() + ": " + existingActivity.getActivityUIID());
		    }
		    existingActivity.setParentActivity(parentActivity);
		    existingActivity.setParentUIID(parentUIID);
		    if (parentActivity.isComplexActivity()) {
			((ComplexActivity) parentActivity).addActivity(existingActivity);
			activityDAO.update(parentActivity);
		    }

		} else {
		    existingActivity.setParentActivity(null);
		    existingActivity.setParentUIID(null);
		    existingActivity.setOrderId(null); // top level
		    // activities don't
		    // have order ids.
		}

		// match up activity to input activities based on UIID. At
		// present there will be only one input activity
		existingActivity.getInputActivities().clear();
		Integer inputActivityUIID = (Integer) JsonUtil.opt(activityDetails,
			AuthoringJsonTags.INPUT_TOOL_ACTIVITY_UIID);
		if (inputActivityUIID != null) {
		    Activity inputActivity = newActivityMap.get(inputActivityUIID);
		    if (inputActivity == null) {
			throw new ObjectExtractorException(
				"Input activity " + inputActivityUIID + " missing for activity "
					+ existingActivity.getTitle() + ": " + existingActivity.getActivityUIID());
		    }
		    existingActivity.getInputActivities().add(inputActivity);
		}

		activityDAO.update(existingActivity);
	    }
	}
    }

    private void parseTransitions(JSONArray transitionsList) throws JSONException {

	HashMap<Integer, Transition> newMap = new HashMap<Integer, Transition>();

	if (transitionsList != null) {
	    for (int transitionIndex = 0; transitionIndex < transitionsList.length(); transitionIndex++) {
		JSONObject transitionDetails = transitionsList.getJSONObject(transitionIndex);
		Transition transition = extractTransitionObject(transitionDetails);
		// Check if transition actually exists. extractTransitionObject
		// returns null
		// if neither the to/from activity exists.
		if (transition != null) {
		    transitionDAO.insertOrUpdate(transition);
		    newMap.put(transition.getTransitionUIID(), transition);
		}
	    }
	}

	// clean up the links for any old transitions.
	Iterator iter = learningDesign.getTransitions().iterator();
	while (iter.hasNext()) {
	    Transition element = (Transition) iter.next();
	    Integer uiID = element.getTransitionUIID();
	    Transition match = newMap.get(uiID);
	    if (match == null) {
		// transition is no more, clean up the old activity links
		cleanupTransition(element);
	    }
	}
	// clear the old set and reset up the transition set. Done this way to
	// keep the Hibernate cascading happy.
	// this means we don't have to manually remove the transition object.
	// Note: This will leave orphan content in the tool tables. It can be
	// removed by the tool content cleaning job,
	// which may be run from the admin screen or via a cron job.
	learningDesign.getTransitions().clear();
	learningDesign.getTransitions().addAll(newMap.values());

	learningDesignDAO.insertOrUpdate(learningDesign);

    }

    public Activity extractActivityObject(JSONObject activityDetails) throws ObjectExtractorException, JSONException {

	// it is assumed that the activityUUID will always be sent by Authoring
	Integer activityUIID = (Integer) JsonUtil.opt(activityDetails, AuthoringJsonTags.ACTIVITY_UIID);
	Integer activityTypeID = (Integer) JsonUtil.opt(activityDetails, AuthoringJsonTags.ACTIVITY_TYPE_ID);
	Activity activity = null;

	// get the activity with the particular activity uuid, if null, then new
	// object needs to be created.
	Activity existingActivity = activityDAO.getActivityByUIID(activityUIID, learningDesign);
	if ((existingActivity != null) && !existingActivity.getActivityTypeId().equals(activityTypeID)) {
	    existingActivity = null;
	}

	if (existingActivity != null) {
	    activity = existingActivity;
	} else {
	    activity = Activity.getActivityInstance(activityTypeID.intValue());
	}
	processActivityType(activity, activityDetails);

	activity.setActivityTypeId(activityTypeID);
	activity.setActivityUIID(activityUIID);
	activity.setDescription((String) JsonUtil.opt(activityDetails, AuthoringJsonTags.DESCRIPTION));
	activity.setTitle((String) JsonUtil.opt(activityDetails, AuthoringJsonTags.ACTIVITY_TITLE));
	activity.setHelpText((String) JsonUtil.opt(activityDetails, AuthoringJsonTags.HELP_TEXT));

	activity.setXcoord(getCoord(activityDetails, AuthoringJsonTags.XCOORD));
	activity.setYcoord(getCoord(activityDetails, AuthoringJsonTags.YCOORD));

	Integer groupingUIID = (Integer) JsonUtil.opt(activityDetails, AuthoringJsonTags.GROUPING_UIID);

	if (groupingUIID == null) {
	    clearGrouping(activity);
	} else {
	    Grouping grouping = groupings.get(groupingUIID);
	    if (grouping != null) {
		setGrouping(activity, grouping, groupingUIID);
	    } else {
		log.warn("Unable to find matching grouping for groupingUIID " + groupingUIID + ". Activity UUID "
			+ activityUIID + " will not be grouped.");
		clearGrouping(activity);
	    }

	}

	activity.setOrderId((Integer) JsonUtil.opt(activityDetails, AuthoringJsonTags.ORDER_ID));

	activity.setLearningDesign(learningDesign);

	Long learningLibraryID = JsonUtil.optLong(activityDetails, AuthoringJsonTags.LEARNING_LIBRARY_ID);

	if (learningLibraryID != null) {
	    LearningLibrary library = learningLibraryDAO.getLearningLibraryById(learningLibraryID);
	    activity.setLearningLibrary(library);
	}

	// Set creation date based on the server timezone, not the client.
	if (activity.getCreateDateTime() == null) {
	    activity.setCreateDateTime(modificationDate);
	}

	activity.setActivityCategoryID((Integer) JsonUtil.opt(activityDetails, AuthoringJsonTags.ACTIVITY_CATEGORY_ID));
	activity.setLibraryActivityUiImage((String) JsonUtil.opt(activityDetails, AuthoringJsonTags.LIBRARY_IMAGE));
	activity.setGroupingSupportType(
		(Integer) JsonUtil.opt(activityDetails, AuthoringJsonTags.GROUPING_SUPPORT_TYPE));
	activity.setStopAfterActivity(JsonUtil.opt(activityDetails, AuthoringJsonTags.STOP_AFTER_ACTIVITY, false));

	return activity;
    }

    private Integer getCoord(JSONObject details, String tag) throws JSONException {
	// the coordinate can be Integer or Double in JSON, need to be ready for any
	Number number = (Number) JsonUtil.opt(details, tag);
	Integer coord = number == null ? null : number.intValue();
	return (coord == null) || (coord >= 0) ? coord : ObjectExtractor.DEFAULT_COORD;
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

    private void processActivityType(Activity activity, JSONObject activityDetails)
	    throws ObjectExtractorException, JSONException {
	if (activity.isGroupingActivity()) {
	    buildGroupingActivity((GroupingActivity) activity, activityDetails);
	} else if (activity.isToolActivity()) {
	    buildToolActivity((ToolActivity) activity, activityDetails);
	} else if (activity.isGateActivity()) {
	    buildGateActivity(activity, activityDetails);
	} else {
	    buildComplexActivity((ComplexActivity) activity, activityDetails);
	}
    }

    private void buildComplexActivity(ComplexActivity activity, JSONObject activityDetails)
	    throws ObjectExtractorException, JSONException {
	// clear all the children - will be built up again by
	// parseActivitiesToMatchUpParentActivityByParentUIID
	// we don't use all-delete-orphan on the activities relationship so we
	// can do this clear.
	activity.getActivities().clear();

	activity.setDefaultActivity(null);
	Integer defaultActivityMapUIID = (Integer) JsonUtil.opt(activityDetails,
		AuthoringJsonTags.DEFAULT_ACTIVITY_UIID);
	if (defaultActivityMapUIID != null) {
	    defaultActivityMap.put(defaultActivityMapUIID, activity);
	}
	if (activity instanceof OptionsWithSequencesActivity) {
	    buildOptionsWithSequencesActivity((OptionsWithSequencesActivity) activity, activityDetails);
	} else if (activity instanceof OptionsActivity) {
	    buildOptionsActivity((OptionsActivity) activity, activityDetails);
	} else if (activity instanceof ParallelActivity) {
	    buildParallelActivity((ParallelActivity) activity);
	} else if (activity instanceof BranchingActivity) {
	    buildBranchingActivity((BranchingActivity) activity, activityDetails);
	} else if (activity instanceof FloatingActivity) {
	    buildFloatingActivity((FloatingActivity) activity, activityDetails);
	} else {
	    buildSequenceActivity((SequenceActivity) activity);
	}
    }

    private void buildFloatingActivity(FloatingActivity floatingActivity, JSONObject activityDetails)
	    throws ObjectExtractorException, JSONException {
	floatingActivity
		.setMaxNumberOfActivities((Integer) JsonUtil.opt(activityDetails, AuthoringJsonTags.MAX_ACTIVITIES));

	SystemTool systemTool = getSystemTool(SystemTool.FLOATING_ACTIVITIES);
	floatingActivity.setSystemTool(systemTool);
    }

    private void buildBranchingActivity(BranchingActivity branchingActivity, JSONObject activityDetails)
	    throws ObjectExtractorException, JSONException {
	if (branchingActivity.isChosenBranchingActivity()) {
	    branchingActivity.setSystemTool(getSystemTool(SystemTool.TEACHER_CHOSEN_BRANCHING));
	} else if (branchingActivity.isGroupBranchingActivity()) {
	    branchingActivity.setSystemTool(getSystemTool(SystemTool.GROUP_BASED_BRANCHING));
	} else if (branchingActivity.isToolBranchingActivity()) {
	    branchingActivity.setSystemTool(getSystemTool(SystemTool.TOOL_BASED_BRANCHING));
	}

	branchingActivity.setStartXcoord(getCoord(activityDetails, AuthoringJsonTags.START_XCOORD));
	branchingActivity.setStartYcoord(getCoord(activityDetails, AuthoringJsonTags.START_YCOORD));
	branchingActivity.setEndXcoord(getCoord(activityDetails, AuthoringJsonTags.END_XCOORD));
	branchingActivity.setEndYcoord(getCoord(activityDetails, AuthoringJsonTags.END_YCOORD));
    }

    private void buildGroupingActivity(GroupingActivity groupingActivity, JSONObject activityDetails)
	    throws ObjectExtractorException, JSONException {
	/**
	 * read the createGroupingUUID, get the Grouping Object, and set CreateGrouping to that object
	 */
	Integer createGroupingUIID = (Integer) JsonUtil.opt(activityDetails, AuthoringJsonTags.CREATE_GROUPING_UIID);
	Grouping grouping = groupings.get(createGroupingUIID);
	if (grouping != null) {
	    groupingActivity.setCreateGrouping(grouping);
	    groupingActivity.setCreateGroupingUIID(createGroupingUIID);
	}

	SystemTool systemTool = getSystemTool(SystemTool.GROUPING);
	groupingActivity.setSystemTool(systemTool);
    }

    private void buildOptionsActivity(OptionsActivity optionsActivity, JSONObject activityDetails)
	    throws JSONException {
	optionsActivity.setMaxNumberOfOptions((Integer) JsonUtil.opt(activityDetails, AuthoringJsonTags.MAX_OPTIONS));
	optionsActivity.setMinNumberOfOptions((Integer) JsonUtil.opt(activityDetails, AuthoringJsonTags.MIN_OPTIONS));
	optionsActivity
		.setOptionsInstructions((String) JsonUtil.opt(activityDetails, AuthoringJsonTags.OPTIONS_INSTRUCTIONS));
    }

    private void buildOptionsWithSequencesActivity(OptionsWithSequencesActivity optionsActivity,
	    JSONObject activityDetails) throws JSONException {
	buildOptionsActivity(optionsActivity, activityDetails);

	optionsActivity.setStartXcoord(getCoord(activityDetails, AuthoringJsonTags.START_XCOORD));
	optionsActivity.setStartYcoord(getCoord(activityDetails, AuthoringJsonTags.START_YCOORD));
	optionsActivity.setEndXcoord(getCoord(activityDetails, AuthoringJsonTags.END_XCOORD));
	optionsActivity.setEndYcoord(getCoord(activityDetails, AuthoringJsonTags.END_YCOORD));
    }

    private void buildParallelActivity(ParallelActivity activity) {
    }

    private void buildSequenceActivity(SequenceActivity activity) {
	activity.setSystemTool(getSystemTool(SystemTool.SEQUENCE));
    }

    private void buildToolActivity(ToolActivity toolActivity, JSONObject activityDetails) throws JSONException {
	Tool tool = toolDAO.getToolByID(JsonUtil.optLong(activityDetails, AuthoringJsonTags.TOOL_ID));
	toolActivity.setTool(tool);

	// copy content if its the default one
	Long toolContentId = JsonUtil.optLong(activityDetails, AuthoringJsonTags.TOOL_CONTENT_ID);
	if ((toolContentId == null) || toolContentId.equals(tool.getDefaultToolContentId())) {
	    if ((toolActivity.getToolContentId() == null)
		    || toolActivity.getToolContentId().equals(tool.getDefaultToolContentId())) {
		toolContentId = getLamsCoreToolService().notifyToolToCopyContent(toolActivity, null);
	    } else {
		toolContentId = toolActivity.getToolContentId();
	    }
	}
	toolActivity.setToolContentId(toolContentId);
    }

    private void buildGateActivity(Object activity, JSONObject activityDetails) throws JSONException {
	if (activity instanceof SynchGateActivity) {
	    buildSynchGateActivity((SynchGateActivity) activity);
	} else if (activity instanceof PermissionGateActivity) {
	    buildPermissionGateActivity((PermissionGateActivity) activity);
	} else if (activity instanceof SystemGateActivity) {
	    buildSystemGateActivity((SystemGateActivity) activity);
	} else if (activity instanceof ConditionGateActivity) {
	    buildConditionGateActivity((ConditionGateActivity) activity);
	} else {
	    buildScheduleGateActivity((ScheduleGateActivity) activity, activityDetails);
	}
	GateActivity gateActivity = (GateActivity) activity;
	gateActivity.setGateActivityLevelId(
		(Integer) JsonUtil.opt(activityDetails, AuthoringJsonTags.GATE_ACTIVITY_LEVEL_ID));
	gateActivity.setGateOpen((Boolean) JsonUtil.opt(activityDetails, AuthoringJsonTags.GATE_OPEN));

    }

    private void buildSynchGateActivity(SynchGateActivity activity) {
	activity.setSystemTool(getSystemTool(SystemTool.SYNC_GATE));
    }

    private void buildPermissionGateActivity(PermissionGateActivity activity) {
	activity.setSystemTool(getSystemTool(SystemTool.PERMISSION_GATE));
    }

    private void buildSystemGateActivity(SystemGateActivity activity) {
	activity.setSystemTool(getSystemTool(SystemTool.SYSTEM_GATE));
    }

    private void buildScheduleGateActivity(ScheduleGateActivity activity, JSONObject activityDetails)
	    throws JSONException {
	activity.setGateStartTimeOffset(JsonUtil.optLong(activityDetails, AuthoringJsonTags.GATE_START_OFFSET));
	activity.setGateEndTimeOffset(JsonUtil.optLong(activityDetails, AuthoringJsonTags.GATE_END_OFFSET));
	activity.setGateActivityCompletionBased(
		(Boolean) JsonUtil.opt(activityDetails, AuthoringJsonTags.GATE_ACTIVITY_COMPLETION_BASED));
	SystemTool systemTool = getSystemTool(SystemTool.SCHEDULE_GATE);
	activity.setSystemTool(systemTool);
    }

    private void createLessonClass(LessonClass lessonClass, JSONObject groupingDetails) throws JSONException {
	Long groupId = JsonUtil.optLong(groupingDetails, AuthoringJsonTags.STAFF_GROUP_ID);
	if (groupId != null) {
	    Group group = groupDAO.getGroupById(groupId);
	    if (group != null) {
		lessonClass.setStaffGroup(group);
	    }
	}
    }

    private Transition extractTransitionObject(JSONObject transitionDetails) throws JSONException {

	Integer transitionUUID = transitionDetails.getInt(AuthoringJsonTags.TRANSITION_UIID);
	Integer toUIID = transitionDetails.getInt(AuthoringJsonTags.TO_ACTIVITY_UIID);
	Integer fromUIID = transitionDetails.getInt(AuthoringJsonTags.FROM_ACTIVITY_UIID);
	Integer transitionType = transitionDetails.getInt(AuthoringJsonTags.TRANSITION_TYPE);

	Transition transition = null;
	Transition existingTransition = findTransition(transitionUUID, toUIID, fromUIID, transitionType);

	if (existingTransition == null) {
	    if (false/*
		      * It will eventually be implemented in Authoring. Now we need to check what kind of transition are
		      * we
		      * dealing with transitionType.equals(Transition.DATA_TRANSITION_TYPE)
		      */) {
		transition = new DataTransition();
	    } else {
		transition = new Transition();
	    }
	} else {
	    transition = existingTransition;
	}

	transition.setTransitionUIID(transitionUUID);

	Activity toActivity = newActivityMap.get(toUIID);
	if (toActivity != null) {
	    transition.setToActivity(toActivity);
	    transition.setToUIID(toUIID);
	    // update the transitionTo property for the activity
	    if (transition.isProgressTransition()) {
		toActivity.setTransitionTo(transition);
	    }
	} else {
	    transition.setToActivity(null);
	    transition.setToUIID(null);
	}

	Activity fromActivity = newActivityMap.get(fromUIID);
	if (fromActivity != null) {
	    transition.setFromActivity(fromActivity);
	    transition.setFromUIID(fromUIID);
	    // update the transitionFrom property for the activity
	    if (transition.isProgressTransition()) {
		fromActivity.setTransitionFrom(transition);
	    }
	} else {
	    transition.setFromActivity(null);
	    transition.setFromUIID(null);
	}

	transition.setDescription((String) JsonUtil.opt(transitionDetails, AuthoringJsonTags.DESCRIPTION));
	transition.setTitle((String) JsonUtil.opt(transitionDetails, AuthoringJsonTags.TITLE));

	// Set creation date based on the server timezone, not the client.
	if (transition.getCreateDateTime() == null) {
	    transition.setCreateDateTime(modificationDate);
	}

	if ((transition.getToActivity() != null) && (transition.getFromActivity() != null)) {
	    transition.setLearningDesign(learningDesign);
	    return transition;
	} else {
	    // One of the to/from is missing, can't store this transition. Make
	    // sure we clean up the related activities
	    cleanupTransition(transition);
	    transition.setLearningDesign(null);
	    return null;
	}
    }

    /**
     * Wipe out any links fromany activities that may be linked to it (e.g. the case where a transition has an from
     * activity but not a too activity. These cases should be picked up by Authoring, but just in case.
     */
    private void cleanupTransition(Transition transition) {
	if ((transition.getFromActivity() != null)
		&& transition.equals(transition.getFromActivity().getTransitionFrom())) {
	    transition.getFromActivity().setTransitionFrom(null);
	}
	if ((transition.getToActivity() != null) && transition.equals(transition.getToActivity().getTransitionTo())) {
	    transition.getToActivity().setTransitionTo(null);
	}
    }

    /**
     * Search in learning design for existing object. Can't go to database as that will trigger a Flush, and we haven't
     * updated the rest of the design, so this would trigger a "deleted object would be re-saved by cascade" error.
     *
     * Check both the UUID for a match, and the to and from for a match. If the user deletes a transition then redraws
     * it between the same activities, then inserting a new one in the db will trigger a duplicate key exception. So we
     * need to reuse any that have the same to/from.
     */
    private Transition findTransition(Integer transitionUUID, Integer toUIID, Integer fromUIID,
	    Integer transitionType) {
	Transition existingTransition = null;
	Set transitions = learningDesign.getTransitions();
	Iterator iter = transitions.iterator();
	while ((existingTransition == null) && iter.hasNext()) {
	    Transition element = (Transition) iter.next();
	    if (element.getTransitionType().equals(transitionType)) {
		if ((transitionUUID != null) && transitionUUID.equals(element.getTransitionUIID())) {
		    existingTransition = element;
		} else if ((toUIID != null) && toUIID.equals(element.getToUIID()) && (fromUIID != null)
			&& fromUIID.equals(element.getFromUIID())) {
		    existingTransition = element;
		}
	    }
	}
	return existingTransition;
    }

    @Override
    public void setMode(Integer mode) {
	this.mode = mode;
    }

    @Override
    public Integer getMode() {
	return mode;
    }

    private void parseBranchMappings(JSONArray branchMappingsList) throws JSONException, ObjectExtractorException {
	if (branchMappingsList != null) {
	    for (int branchMappingIndex = 0; branchMappingIndex < branchMappingsList.length(); branchMappingIndex++) {
		extractBranchActivityEntry(branchMappingsList.getJSONObject(branchMappingIndex));
	    }
	}

	// now go through and delete any old branch mappings that are no longer
	// used.
	// need to remove them from their collections to make sure it isn't
	// accidently re-added.
	Iterator iter = oldbranchActivityEntryList.iterator();
	while (iter.hasNext()) {
	    BranchActivityEntry oldEntry = (BranchActivityEntry) iter.next();

	    SequenceActivity sequenceActivity = oldEntry.getBranchSequenceActivity();
	    if (sequenceActivity == null) {
		oldEntry.getBranchingActivity().getBranchActivityEntries().remove(oldEntry);

	    } else {
		sequenceActivity.getBranchEntries().remove(oldEntry);
	    }

	    Group group = oldEntry.getGroup();
	    if (group != null) {
		group.getBranchActivities().remove(oldEntry);
	    }
	    activityDAO.delete(oldEntry);
	}
    }

    private BranchActivityEntry extractBranchActivityEntry(JSONObject details)
	    throws JSONException, ObjectExtractorException {

	Long entryId = JsonUtil.optLong(details, AuthoringJsonTags.BRANCH_ACTIVITY_ENTRY_ID);
	Integer entryUIID = details.getInt(AuthoringJsonTags.BRANCH_ACTIVITY_ENTRY_UIID);

	Integer sequenceActivityUIID = (Integer) JsonUtil.opt(details, AuthoringJsonTags.BRANCH_SEQUENCE_ACTIVITY_UIID);
	Boolean gateOpenWhenConditionMet = (Boolean) JsonUtil.opt(details,
		AuthoringJsonTags.BRANCH_GATE_OPENS_WHEN_CONDITION_MET);
	Integer branchingActivityUIID = null;
	if (gateOpenWhenConditionMet != null) {
	    branchingActivityUIID = details.getInt(AuthoringJsonTags.BRANCH_GATE_ACTIVITY_UIID);
	} else {
	    branchingActivityUIID = details.getInt(AuthoringJsonTags.BRANCH_ACTIVITY_UIID);
	}

	Activity branchingActivity = newActivityMap.get(branchingActivityUIID);
	if (branchingActivity == null) {
	    throw new ObjectExtractorException(
		    "Branching Activity listed in the branch mapping list is missing. Mapping entry UUID " + entryUIID
			    + " branchingActivityUIID " + branchingActivityUIID);
	}
	if (!branchingActivity.isBranchingActivity() && !branchingActivity.isConditionGate()) {
	    throw new ObjectExtractorException(
		    "Activity listed in the branch mapping list is not a branching activity nor a condition gate. Mapping entry UUID "
			    + entryUIID + " branchingActivityUIID " + branchingActivityUIID);
	}
	// sequence activity is null for a condition gate
	SequenceActivity sequenceActivity = null;
	if (!branchingActivity.isConditionGate()) {
	    Activity activity = newActivityMap.get(sequenceActivityUIID);
	    if (activity == null) {
		throw new ObjectExtractorException(
			"Sequence Activity listed in the branch mapping list is missing. Mapping entry UUID "
				+ entryUIID + " sequenceActivityUIID " + sequenceActivityUIID);
	    } else if (!activity.isSequenceActivity()) {
		throw new ObjectExtractorException(
			"Activity listed in the branch mapping list is not a sequence activity. Mapping entry UUID "
				+ entryUIID + " sequenceActivityUIID " + sequenceActivityUIID);
	    } else {
		sequenceActivity = (SequenceActivity) activity;
	    }
	}

	// If the mapping was created at runtime, there will be an ID but no IU
	// ID field.
	// If it was created in authoring, will have a UI ID and may or may not
	// have an ID.
	// So try to match up on UI ID first, failing that match on ID. Then the
	// worst case, which is the mapping
	// is created at runtime but then modified in authoring (and has has a
	// new UI ID added) is handled.
	BranchActivityEntry uiid_match = null;
	BranchActivityEntry id_match = null;
	Iterator iter = null;
	if (sequenceActivity == null) {
	    ConditionGateActivity conditionGateActitivity = (ConditionGateActivity) branchingActivity;
	    if (conditionGateActitivity.getBranchActivityEntries() != null) {
		iter = conditionGateActitivity.getBranchActivityEntries().iterator();
	    }
	} else {
	    if (sequenceActivity.getBranchEntries() != null) {
		iter = sequenceActivity.getBranchEntries().iterator();
	    }
	}

	if (iter != null) {
	    while ((uiid_match == null) && iter.hasNext()) {
		BranchActivityEntry possibleEntry = (BranchActivityEntry) iter.next();
		if (entryUIID.equals(possibleEntry.getEntryUIID())) {
		    uiid_match = possibleEntry;
		}
		if ((entryId != null) && entryId.equals(possibleEntry.getEntryId())) {
		    id_match = possibleEntry;
		}
	    }
	}

	BranchActivityEntry entry = uiid_match != null ? uiid_match : id_match;

	// Does it exist already? If it does, remove it from the "old" set
	// (which is used for doing deletions later).
	oldbranchActivityEntryList.remove(entry);

	BranchCondition condition = extractCondition(details.optJSONObject(AuthoringJsonTags.BRANCH_CONDITION), entry);

	Integer groupUIID = (Integer) JsonUtil.opt(details, AuthoringJsonTags.GROUP_UIID);
	Group group = null;
	if (groupUIID != null) {
	    group = groups.get(groupUIID);
	    if (group == null) {
		throw new ObjectExtractorException(
			"Group listed in the branch mapping list is missing. Mapping entry UUID " + entryUIID
				+ " groupUIID " + groupUIID);
	    }
	}

	if ((condition == null) && (group == null)) {
	    throw new ObjectExtractorException(
		    "Branch mapping has neither a group or a condition. Not a valid mapping. " + details);
	}

	if (entry == null) {
	    if (condition != null) {
		entry = condition.allocateBranchToCondition(entryUIID, sequenceActivity, branchingActivity,
			gateOpenWhenConditionMet);
	    } else {
		entry = group.allocateBranchToGroup(entryUIID, sequenceActivity, (BranchingActivity) branchingActivity);
	    }
	} else {
	    entry.setEntryUIID(entryUIID);
	    entry.setBranchSequenceActivity(sequenceActivity);
	    entry.setBranchingActivity(branchingActivity);
	    entry.setGateOpenWhenConditionMet(gateOpenWhenConditionMet);
	}

	Group existingGroup = entry.getGroup();
	if ((existingGroup != null) && !existingGroup.getGroupId().equals(group.getGroupId())) {
	    existingGroup.getBranchActivities().remove(entry);
	}
	entry.setGroup(group);
	if (group != null) {
	    if (group.getBranchActivities() == null) {
		group.setBranchActivities(new HashSet<BranchActivityEntry>());
	    }
	    group.getBranchActivities().add(entry);
	}

	entry.setCondition(condition);

	if (branchingActivity.isConditionGate()) {
	    ConditionGateActivity conditionGateActitivity = (ConditionGateActivity) branchingActivity;
	    if (conditionGateActitivity.getBranchActivityEntries() == null) {
		conditionGateActitivity.setBranchActivityEntries(new HashSet());
	    }
	    conditionGateActitivity.getBranchActivityEntries().add(entry);
	} else {
	    if (sequenceActivity.getBranchEntries() == null) {
		sequenceActivity.setBranchEntries(new HashSet());
	    }
	    sequenceActivity.getBranchEntries().add(entry);
	    activityDAO.update(sequenceActivity);
	}

	if (group != null) {
	    groupingDAO.update(group);
	}

	return entry;
    }

    private BranchCondition extractCondition(JSONObject conditionDetails, BranchActivityEntry entry)
	    throws JSONException {

	BranchCondition condition = null;

	if (conditionDetails != null) {

	    Long conditionID = JsonUtil.optLong(conditionDetails, AuthoringJsonTags.CONDITION_ID);
	    if (entry != null) {
		condition = entry.getCondition();
	    }
	    if ((condition != null) && (conditionID != null) && !condition.getConditionId().equals(conditionID)) {
		log.warn(
			"Unexpected behaviour: condition supplied in JSON packet has a different ID to matching branch activity entry in the database. Dropping old database condition."
				+ " Old db condition " + condition + " new entry in JSON " + conditionDetails);
		condition = null; // Hibernate should dispose of it
		// automatically via the cascade
	    }

	    Integer conditionUIID = conditionDetails.getInt(AuthoringJsonTags.CONDITION_UIID);
	    String conditionType = conditionDetails.getString(AuthoringJsonTags.CONDITION_TYPE);

	    if (BranchCondition.OUTPUT_TYPE_COMPLEX.equals(conditionType)
		    || BranchCondition.OUTPUT_TYPE_STRING.equals(conditionType)) {
		// This is different than "conditionID" !!!
		Long newConditionID = condition == null ? conditionDetails.getLong(AuthoringJsonTags.CONDITION_ID)
			: condition.getConditionId();
		// we need to get the proper subclass, since the one provided by branch entry is not valid
		BranchCondition originalCondition = branchActivityEntryDAO.getConditionByID(newConditionID);
		if (originalCondition == null) {
		    log.error("Could not find condition with given ID: " + conditionID);
		} else {
		    if (condition == null) {
			condition = (BranchCondition) originalCondition.clone();
		    } else {
			condition = originalCondition;
		    }
		    condition.setConditionUIID(conditionUIID);
		}
	    } else if (condition == null) {
		condition = new BranchCondition(null, conditionUIID,
			conditionDetails.getInt(AuthoringJsonTags.ORDER_ID),
			conditionDetails.getString(AuthoringJsonTags.CONDITION_NAME),
			conditionDetails.getString(AuthoringJsonTags.CONDITION_DISPLAY_NAME),
			conditionDetails.getString(AuthoringJsonTags.CONDITION_TYPE),
			(String) JsonUtil.opt(conditionDetails, AuthoringJsonTags.CONDITION_START_VALUE),
			(String) JsonUtil.opt(conditionDetails, AuthoringJsonTags.CONDITION_END_VALUE),
			(String) JsonUtil.opt(conditionDetails, AuthoringJsonTags.CONDITION_EXACT_MATCH_VALUE));
	    } else {
		condition.setConditionUIID(conditionUIID);
		condition.setDisplayName(conditionDetails.getString(AuthoringJsonTags.CONDITION_DISPLAY_NAME));
		condition.setEndValue((String) JsonUtil.opt(conditionDetails, AuthoringJsonTags.CONDITION_END_VALUE));
		condition.setExactMatchValue(
			(String) JsonUtil.opt(conditionDetails, AuthoringJsonTags.CONDITION_EXACT_MATCH_VALUE));
		condition.setName(conditionDetails.getString(AuthoringJsonTags.CONDITION_NAME));
		condition.setOrderId(conditionDetails.getInt(AuthoringJsonTags.ORDER_ID));
		condition.setStartValue(
			(String) JsonUtil.opt(conditionDetails, AuthoringJsonTags.CONDITION_START_VALUE));
		condition.setType(conditionDetails.getString(AuthoringJsonTags.CONDITION_TYPE));
	    }
	}
	return condition;
    }

    private SystemTool getSystemTool(Long systemToolId) {
	SystemTool tool = systemTools.get(systemToolId);
	if (tool == null) {
	    tool = systemToolDAO.getSystemToolByID(systemToolId);
	    if (tool != null) {
		systemTools.put(systemToolId, tool);
	    } else {
		log.error("ObjectExtractor: Unable to find matching system tool for id " + systemToolId);
	    }
	}
	return tool;
    }

    private void createLearnerChoiceGrouping(LearnerChoiceGrouping learnerChoiceGrouping, JSONObject groupingDetails)
	    throws JSONException {
	Integer numLearnersPerGroup = (Integer) JsonUtil.opt(groupingDetails, AuthoringJsonTags.LEARNERS_PER_GROUP);

	if ((numLearnersPerGroup != null) && (numLearnersPerGroup.intValue() > 0)) {
	    learnerChoiceGrouping.setLearnersPerGroup(numLearnersPerGroup);
	    learnerChoiceGrouping.setNumberOfGroups(null);
	    learnerChoiceGrouping.setEqualNumberOfLearnersPerGroup(null);
	} else {
	    Integer numGroups = (Integer) JsonUtil.opt(groupingDetails, AuthoringJsonTags.NUMBER_OF_GROUPS);

	    if ((numGroups != null) && (numGroups.intValue() > 0)) {
		learnerChoiceGrouping.setNumberOfGroups(numGroups);
	    } else {
		learnerChoiceGrouping.setNumberOfGroups(null);
	    }
	    learnerChoiceGrouping.setLearnersPerGroup(null);

	    Boolean equalNumberOfLearnersPerGroup = (Boolean) JsonUtil.opt(groupingDetails,
		    AuthoringJsonTags.EQUAL_NUMBER_OF_LEARNERS_PER_GROUP);
	    if (equalNumberOfLearnersPerGroup != null) {
		learnerChoiceGrouping.setEqualNumberOfLearnersPerGroup(equalNumberOfLearnersPerGroup);
	    }
	}
	learnerChoiceGrouping.setViewStudentsBeforeSelection(
		(Boolean) JsonUtil.opt(groupingDetails, AuthoringJsonTags.VIEW_STUDENTS_BEFORE_SELECTION));
    }

    private void buildConditionGateActivity(ConditionGateActivity activity) {
	activity.setSystemTool(getSystemTool(SystemTool.CONDITION_GATE));
    }
}