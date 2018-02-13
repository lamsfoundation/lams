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
package org.lamsfoundation.lams.authoring.template.web;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpException;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.authoring.service.IAuthoringService;
import org.lamsfoundation.lams.authoring.template.AssessMCAnswer;
import org.lamsfoundation.lams.authoring.template.Option;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.GateActivity;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.rest.RestTags;
import org.lamsfoundation.lams.rest.ToolRestManager;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.dao.IToolDAO;
import org.lamsfoundation.lams.tool.service.ILamsCoreToolService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.AuthoringJsonTags;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.JsonUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Base class for actions processing Learning Design templates.
 *
 * TODO stop hardcoding the icons!
 *
 * @author Marcin Cieslak, Fiona Malikoff
 */
public abstract class LdTemplateAction extends DispatchAction {

    private static Logger log = Logger.getLogger(LdTemplateAction.class);
    public static final int MAX_OPTION_COUNT = 5;
    public static final int MAX_FLOATING_ACTIVITY_OPTIONS = 6; // Hardcoded in the Flash client

    public static final String PARENT_ACTIVITY_TYPE = "parentActivityType"; // used to work out transitions - not used by the authoring module

    private final HttpClient httpClient = new DefaultHttpClient();

    private static ILamsCoreToolService lamsCoreToolService;
    private static IWorkspaceManagementService workspaceManagementService;
    private static IAuthoringService authoringService;
    private static IToolDAO toolDAO;

    protected static final String CONTENT_TYPE_JSON = "application/json;charset=utf-8";

    // icon strings found in the lams_learningdesign_activity table
    protected static final String ASSESSMENT_TOOL_SIGNATURE = "laasse10";
    protected static final String ASSESSMENT_ICON = "tool/laasse10/images/icon_assessment.swf";
    protected static final String CHAT_TOOL_SIGNATURE = "lachat11";
    protected static final String CHAT_ICON = "tool/lachat11/images/icon_chat.swf";
    protected static final String FORUM_TOOL_SIGNATURE = "lafrum11";
    protected static final String FORUM_ICON = "tool/lafrum11/images/icon_forum.swf";
    protected static final String LEADER_TOOL_SIGNATURE = "lalead11";
    protected static final String LEADER_ICON = "tool/lalead11/images/icon_leaderselection.swf";
    protected static final String MCQ_TOOL_SIGNATURE = "lamc11";
    protected static final String MCQ_ICON = "tool/lamc11/images/icon_mcq.swf";
    protected static final String NOTEBOOK_TOOL_SIGNATURE = "lantbk11";
    protected static final String NOTEBOOK_ICON = "tool/lantbk11/images/icon_notebook.swf";
    protected static final String NOTICEBOARD_TOOL_SIGNATURE = "lanb11";
    protected static final String NOTICEBOARD_ICON = "tool/lanb11/images/icon_htmlnb.swf";
    protected static final String QA_TOOL_SIGNATURE = "laqa11";
    protected static final String QA_ICON = "tool/laqa11/images/icon_questionanswer.swf";
    protected static final String SHARE_RESOURCES_TOOL_SIGNATURE = "larsrc11";
    protected static final String SHARE_RESOURCES_ICON = "tool/larsrc11/images/icon_rsrc.swf";
    protected static final String SCRATCHIE_TOOL_SIGNATURE = "lascrt11";
    protected static final String SCRATCHIE_ICON = "tool/lascrt11/images/icon_scratchie.swf";
    protected static final String SCRIBE_TOOL_SIGNATURE = "lascrb11";
    protected static final String SCRIBE_ICON = "tool/lascrb11/images/icon_scribe.swf";
    protected static final String SUBMIT_TOOL_SIGNATURE = "lasbmt11";
    protected static final String SUBMIT_ICON = "tool/lasbmt11/images/icon_reportsubmission.swf";
    protected static final String SURVEY_TOOL_SIGNATURE = "lasurv11";
    protected static final String SURVEY_ICON = "tool/lasurv11/images/icon_survey.swf";
    protected static final String WIKI_TOOL_SIGNATURE = "lawiki10";
    protected static final String WIKI_ICON = "tool/lawiki10/images/icon_wiki.swf";
    protected static final String MINDMAP_TOOL_SIGNATURE = "lamind10";
    protected static final String MINDMAP_ICON = "tool/lamind10/images/icon_mindmap.swf";
    protected static final String VOTE_TOOL_SIGNATURE = "lavote11";
    protected static final String VOTE_ICON = "tool/lavote11/images/icon_ranking.swf";

    protected static final String CHAT_SCRIBE_DESC = "Combined Chat and Scribe";
    protected static final String FORUM_SCRIBE_DESC = "Combined Forum and Scribe";
    protected static final String RESOURCES_FORUM_DESC = "Combined Share Resources and Forum";

    protected static final String TOOL_CONTENT_SERVLET_URL_SUFFIX = "/lams/rest/ToolContent";
    protected static final String LEARNING_DESIGN_SERVLET_URL_SUFFIX = "/lams/rest/LearningDesign";

    @Override
    public final ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	JSONObject responseJSON = null;
	try {
	    responseJSON = createLearningDesign(request);

	    if (!responseJSON.has("learningDesignID") && !responseJSON.has("errors")) {
		log.error(
			"The Learning Design was not created successfully. ResponseJSON missing both learningDesignID and errors"
				+ responseJSON.toString());
		responseJSON = new JSONObject().put("fatal",
			"The Learning Design was not created successfully. See the server log for more details.");
	    }
	} catch (Exception e) {
	    log.error(e.getMessage(), e);
	    responseJSON = new JSONObject().put("fatal",
		    "The Learning Design was not created successfully. See the server log for more details.\n\n"
			    + e.getMessage());
	}

	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(responseJSON);
	return null;
    }

    /**
     * If you have a CKEditor in your template interface, you should set up an "init" forward for your servlet in
     * response to this call (e.g. pbl.do?method=init) and it should forward to your JSP form. Calling this method sets
     * up the contentFolderID needed by the CKEditor to support image upload. The fields that match these values
     * are in init.jsp and should be included in the JSP form. The JSP should then call the unspecified
     * method (e.g. pbl.do) to process the form.
     *
     * If you wish to set up other values in the initialisation that are form specific, override this method
     * in your servlet, remembering to call this method.
     */
    public ActionForward init(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	String contentFolderID = FileUtil.generateUniqueContentFolderID();
	request.setAttribute(RestTags.CONTENT_FOLDER_ID, contentFolderID);
	return mapping.findForward("init");
    }

//	
//	String responseBody = null;
//	try {
//	    HttpResponse httpResponse = httpClient.execute(httpPost);
//	    int statusCode = httpResponse.getStatusLine().getStatusCode();
//	    if (statusCode != HttpStatus.SC_OK) {
//		LdTemplateAction.log.error("Error while calling REST servlet: " + url);
//		return null;
//	    }
//	    responseBody = EntityUtils.toString(httpResponse.getEntity());
//	} finally {
//	    httpPost.releaseConnection();
//	}
//
//	return new JSONObject(responseBody);
//    }

    protected abstract JSONObject createLearningDesign(HttpServletRequest request)
	    throws Exception;

    /**
     * Creates transitions between activities in the order they were created.
     * Simple version used if no sequence activities are used.
     */
    protected JSONArray createTransitions(AtomicInteger maxUIID, JSONArray activities) throws JSONException {
	JSONArray transitions = new JSONArray();
	return createTransitions(transitions, maxUIID, activities);
    }

    /**
     * Creates transitions between activities in the order they were created.
     * Complex version used if sequence activities are used - then the top level activities go in one set and each
     * sequence
     * activity is its own set of activities.
     */
    protected JSONArray createTransitions(AtomicInteger maxUIID, Set<JSONArray> setsOfActivities) throws JSONException {
	JSONArray transitions = new JSONArray();
	if (setsOfActivities != null) {
	    for (JSONArray activities : setsOfActivities) {
		createTransitions(transitions, maxUIID, activities);
	    }
	}
	return transitions;
    }

    /**
     * Processes the transitions between activities within a sequence. This could be the overall
     * sequence or activities within a SequenceActivity. The transitions parameter must not be null.
     */
    private JSONArray createTransitions(JSONArray transitions, AtomicInteger maxUIID, JSONArray activities)
	    throws JSONException {

	for (int activityIndex = 1; activityIndex < activities.length(); activityIndex++) {
	    // If it has parent activity that is a parallel activity or support activity then skip to the next one.
	    // If the parent activity is a sequence activity then we do want transitions
	    JSONObject fromActivity = activities.getJSONObject(activityIndex - 1);
	    JSONObject toActivity = activities.getJSONObject(activityIndex);
	    Integer parentType = JsonUtil.opt(toActivity, PARENT_ACTIVITY_TYPE, (Integer) null);
	    while ((activityIndex < activities.length()) && parentType != null
		    && !parentType.equals(Activity.SEQUENCE_ACTIVITY_TYPE)) {
		activityIndex++;
		toActivity = activities.getJSONObject(activityIndex);
		parentType = JsonUtil.opt(toActivity, PARENT_ACTIVITY_TYPE, (Integer) null);
	    }
	    int fromUIID = fromActivity.getInt(AuthoringJsonTags.ACTIVITY_UIID);
	    int toUIID = toActivity.getInt(AuthoringJsonTags.ACTIVITY_UIID);

	    JSONObject transitionJSON = new JSONObject();
	    transitionJSON.put(AuthoringJsonTags.TRANSITION_UIID, maxUIID.incrementAndGet());
	    transitionJSON.put(AuthoringJsonTags.FROM_ACTIVITY_UIID, fromUIID);
	    transitionJSON.put(AuthoringJsonTags.TO_ACTIVITY_UIID, toUIID);
	    transitionJSON.put(AuthoringJsonTags.TRANSITION_TYPE, 0);

	    transitions.put(transitionJSON);

	    // don't need the parent type any more so remove it so authoring won't get it!
	    fromActivity.remove(PARENT_ACTIVITY_TYPE);
	}

	return transitions;
    }

    protected static final int rowHeightSpace = 100;
    protected static final int activityWidthSpace = 150;

    /**
     * Calculate where to draw an activity. Aim for 4 activities per line. Returns Integer[x,y]
     * Used when there is no hardcoding of location of activities.
     * If some activities use hardcoding of layout, then see calcPositionNextRight(), calcPositionBelow()
     * to calculate relative positions.
     * 
     * @return
     */
    protected Integer[] calcPosition(int order) {
	Integer[] pos = new Integer[2];
	int activity4space = activityWidthSpace * 4;
	int rawPos = order * activityWidthSpace;
	int rowNum = rawPos / (activity4space);
	pos[0] = (20 + rawPos) - (rowNum * activity4space); // x
	pos[1] = rowHeightSpace + (rowHeightSpace * rowNum); // y

	return pos;
    }

    /** Work out the next location for an activity to the right of the current activity (given by currPos) */
    protected Integer[] calcPositionNextRight(Integer[] currPos) {
	Integer[] newPos = new Integer[2];
	newPos[1] = currPos[1]; // same row so y co-ord stays the same
	newPos[0] = currPos[0] + activityWidthSpace; // move x co-ord along to the right.
	return newPos;
    }

    /** Work out the next location for an activity directly below the current activity (given by currPos) */
    protected Integer[] calcPositionBelow(Integer[] currPos) {
	Integer[] newPos = new Integer[2];
	newPos[0] = currPos[0]; // same column so x co-ord stays the same
	newPos[1] = currPos[1] + rowHeightSpace; // move y co-ord down a row.
	return newPos;
    }

    /**
     * Create a unique title for this learning design, within the right length for the database. The title will
     * be <ConvertedUserEnteredString>_<TEMPLATE_CODE>_UniqueDate, where the userEnteredString is capitalised and
     * whitespace is removed.
     * 
     * @param sequenceTitle
     * @param workspaceFolderID
     * @return
     */
    private String createTitle(String templateCode, String userEnteredString, Integer workspaceFolderID) {
	String title = WebUtil.removeHTMLtags(userEnteredString);
	title = title.replaceAll("[@%<>/^/*/$]", "");
	title = getAuthoringService().getUniqueNameForLearningDesign(title, workspaceFolderID);
	if (title.length() > 220) {
	    title.substring(0, 220);
	}
	return title;
    }

    /**
     * Setup Learning Design JSON Data
     *
     * @throws IOException
     * @throws JSONException
     * @throws HttpException
     */
    protected JSONObject saveLearningDesign(String templateCode, String userEnteredTitleString,
	    String userEnteredDescription, Integer workspaceFolderID, String contentFolderId, Integer maxUIID,
	    JSONArray activities, JSONArray transitions, JSONArray groupings, JSONArray branchMappings)
	    throws HttpException, JSONException, IOException {

	// fill in required LD data
	JSONObject ldJSON = new JSONObject();
	ldJSON.put(AuthoringJsonTags.WORKSPACE_FOLDER_ID, workspaceFolderID);
	ldJSON.put(AuthoringJsonTags.COPY_TYPE, 1);
	ldJSON.put(AuthoringJsonTags.TITLE, createTitle(templateCode, userEnteredTitleString, workspaceFolderID));
	ldJSON.put(AuthoringJsonTags.DESCRIPTION, WebUtil.removeHTMLtags(userEnteredDescription));
	ldJSON.put(AuthoringJsonTags.DESIGN_TYPE, templateCode.toLowerCase());
	ldJSON.put(AuthoringJsonTags.MAX_ID, maxUIID);
	ldJSON.put(AuthoringJsonTags.READ_ONLY, false);
	ldJSON.put(AuthoringJsonTags.EDIT_OVERRIDE_LOCK, false);
	ldJSON.put(AuthoringJsonTags.CONTENT_FOLDER_ID, contentFolderId);
	ldJSON.put(AuthoringJsonTags.SAVE_MODE, 0);
	ldJSON.put(AuthoringJsonTags.VALID_DESIGN, true);
	ldJSON.put(AuthoringJsonTags.ACTIVITIES, activities);
	ldJSON.put(AuthoringJsonTags.TRANSITIONS, transitions);
	ldJSON.putOpt(AuthoringJsonTags.GROUPINGS, groupings);
	ldJSON.putOpt(AuthoringJsonTags.BRANCH_MAPPINGS, branchMappings);

	LearningDesign learningDesign = null;
	try {
	    learningDesign = getAuthoringService().saveLearningDesignDetails(ldJSON);
	} catch ( Exception e )  {
	    LdTemplateAction.log.error("Unable to learning design with details " + ldJSON, e);
	    throw new HttpException("Unable to learning design with details " + ldJSON);

	}

	JSONObject responseJSON = new JSONObject();
	responseJSON.put("learningDesignID", learningDesign.getLearningDesignId());
	responseJSON.put("title", learningDesign.getTitle());
	return responseJSON;
    }

    /* ************************************** Non-Tool Activity methods ******************************************** */
    protected JSONObject createGateActivity(AtomicInteger uiid, int order, Integer[] layoutCoords)
	    throws JSONException {

	JSONObject activityJSON = new JSONObject();
	Integer[] pos = layoutCoords;
	if (pos == null) {
	    pos = calcPosition(order);
	    pos[0] = pos[0] + 40; // gate is only a small icon so it needs to be offset to look right
	    pos[1] = pos[1] + 15;
	}
	;

	activityJSON.put(AuthoringJsonTags.ACTIVITY_UIID, uiid.incrementAndGet());
	activityJSON.put(AuthoringJsonTags.GROUPING_SUPPORT_TYPE, 2);
	activityJSON.put(AuthoringJsonTags.APPLY_GROUPING, false);
	activityJSON.put(AuthoringJsonTags.XCOORD, pos[0]);
	activityJSON.put(AuthoringJsonTags.YCOORD, pos[1]);
	activityJSON.put(AuthoringJsonTags.ACTIVITY_TITLE, "Gate"); // I18N
	activityJSON.put(AuthoringJsonTags.ACTIVITY_CATEGORY_ID, Activity.CATEGORY_SYSTEM);
	activityJSON.put(AuthoringJsonTags.ACTIVITY_TYPE_ID, Activity.PERMISSION_GATE_ACTIVITY_TYPE);
	activityJSON.put(AuthoringJsonTags.GATE_ACTIVITY_LEVEL_ID, GateActivity.LEARNER_GATE_LEVEL);

	return activityJSON;
    }

    /** Create a group activity's JSON objects */
    protected JSONObject[] createGroupingActivity(AtomicInteger uiid, int order, Integer[] layoutCoords,
	    Integer groupingTypeID, Integer numLearners, Integer numGroups, String title, String[] groupNames)
	    throws JSONException {
	JSONObject[] responseJSONs = new JSONObject[2];

	JSONObject groupingJSON = new JSONObject();
	responseJSONs[1] = groupingJSON;
	int groupingUIID = uiid.incrementAndGet();
	groupingJSON.put(AuthoringJsonTags.GROUPING_UIID, groupingUIID);
	groupingJSON.put(AuthoringJsonTags.GROUPING_TYPE_ID, groupingTypeID);
	groupingJSON.put(AuthoringJsonTags.NUMBER_OF_GROUPS, numGroups);
	groupingJSON.put(AuthoringJsonTags.LEARNERS_PER_GROUP, numLearners);
	groupingJSON.put(AuthoringJsonTags.EQUAL_NUMBER_OF_LEARNERS_PER_GROUP, Boolean.FALSE);

	int orderId = 0;
	if (groupNames != null) {
	    JSONArray groups = new JSONArray();
	    for (String groupName : groupNames) {
		JSONObject group = new JSONObject();
		group.put(AuthoringJsonTags.GROUP_NAME, groupName);
		group.put(AuthoringJsonTags.ORDER_ID, orderId++);
		group.put(AuthoringJsonTags.GROUP_UIID, uiid.incrementAndGet());
		groups.put(group);
	    }
	    groupingJSON.put(AuthoringJsonTags.GROUPS, groups);
	}

	Integer[] pos = layoutCoords != null ? layoutCoords : calcPosition(order);

	JSONObject activityJSON = new JSONObject();
	responseJSONs[0] = activityJSON;
	activityJSON.put(AuthoringJsonTags.ACTIVITY_UIID, uiid.incrementAndGet());
	activityJSON.put(AuthoringJsonTags.GROUPING_SUPPORT_TYPE, 2);
	activityJSON.put(AuthoringJsonTags.XCOORD, pos[0]);
	activityJSON.put(AuthoringJsonTags.YCOORD, pos[1]);
	activityJSON.put(AuthoringJsonTags.ACTIVITY_TITLE, title != null ? title : "Grouping");
	activityJSON.put(AuthoringJsonTags.ACTIVITY_CATEGORY_ID, Activity.CATEGORY_SYSTEM);
	activityJSON.put(AuthoringJsonTags.ACTIVITY_TYPE_ID, Activity.GROUPING_ACTIVITY_TYPE);
	activityJSON.put(AuthoringJsonTags.CREATE_GROUPING_UIID, groupingUIID);

	return responseJSONs;
    }

    /**
     * Create a parallel activity from two already created activities. Use one of the descriptions CHAT_SCRIBE_DESC,
     * FORUM_SCRIBE_DESC or RESOURCES_FORUM_DESC for the description field otherwise an exported learning design cannot
     * be imported!
     *
     * @param uiid
     * @param order
     * @return
     * @throws JSONException
     */
    protected JSONObject createParallelActivity(AtomicInteger uiid, int order, Integer[] layoutCoords,
	    Integer groupingUIID, String activityTitle, String description) throws JSONException {
	JSONObject activityJSON = new JSONObject();

	Integer[] pos = layoutCoords;
	if (pos == null) {
	    pos = calcPosition(order);
	    pos[1] = pos[1] - 50; // Extra tall!
	}
	;

	activityJSON.put(AuthoringJsonTags.ACTIVITY_UIID, uiid.incrementAndGet());
	activityJSON.put(AuthoringJsonTags.GROUPING_SUPPORT_TYPE, 2);
	activityJSON.put(AuthoringJsonTags.APPLY_GROUPING, false);
	activityJSON.put(AuthoringJsonTags.XCOORD, pos[0]);
	activityJSON.put(AuthoringJsonTags.YCOORD, pos[1]);
	activityJSON.put(AuthoringJsonTags.ACTIVITY_TITLE, activityTitle != null ? activityTitle : "Parallel Activity");
	activityJSON.put(AuthoringJsonTags.ACTIVITY_CATEGORY_ID, Activity.CATEGORY_SPLIT);
	activityJSON.put(AuthoringJsonTags.ACTIVITY_TYPE_ID, Activity.PARALLEL_ACTIVITY_TYPE);
	activityJSON.put(AuthoringJsonTags.DESCRIPTION, description);
	if (groupingUIID != null) {
	    activityJSON.put(AuthoringJsonTags.GROUPING_UIID, groupingUIID);
	    activityJSON.put(AuthoringJsonTags.APPLY_GROUPING, true);
	} else {
	    activityJSON.put(AuthoringJsonTags.APPLY_GROUPING, false);
	}
	return activityJSON;
    }

    /**
     * Create a support activity group from already created activities. The support activities run outside the
     * sequenced activities
     */
    protected JSONObject createSupportActivity(AtomicInteger uiid, int order, Integer[] layoutCoords)
	    throws JSONException {
	Integer[] pos = layoutCoords;
	if (pos == null) {
	    pos = calcPosition(order);
	    pos[0] = 20;
	    pos[1] = pos[1] + rowHeightSpace; // put it on its own line down the bottom
	}

	JSONObject activityJSON = new JSONObject();
	activityJSON.put(AuthoringJsonTags.ACTIVITY_UIID, uiid.incrementAndGet());
	activityJSON.put(AuthoringJsonTags.GROUPING_SUPPORT_TYPE, Activity.GROUPING_SUPPORT_NONE);
	activityJSON.put(AuthoringJsonTags.APPLY_GROUPING, false);
	activityJSON.put(AuthoringJsonTags.XCOORD, pos[0]);
	activityJSON.put(AuthoringJsonTags.YCOORD, pos[1]);
	activityJSON.put(AuthoringJsonTags.ACTIVITY_TITLE, "Support Activity");
	activityJSON.put(AuthoringJsonTags.ACTIVITY_CATEGORY_ID, Activity.CATEGORY_SYSTEM);
	activityJSON.put(AuthoringJsonTags.ACTIVITY_TYPE_ID, Activity.FLOATING_ACTIVITY_TYPE);
	activityJSON.putOpt(AuthoringJsonTags.MAX_ACTIVITIES, MAX_FLOATING_ACTIVITY_OPTIONS);
	return activityJSON;
    }

    /**
     * Create a sequence (or branch) activity, which is a single path that a user follows. This activity will go
     * into a branching activity. If branchName is null then the branch will be "Branch <no>".
     * The sequence activity has a circular dependence - to define the SequenceActivity you need the UIID of the first
     * activity in the sequence. But the first activity in the sequence needs the SequenceActivity's UIID as its parent
     * activity UIID.
     * So "reserve" a uuid (using incrementAndGet()) in the template for the SequenceActivity, create the child activity
     * (setting
     * the parent activity as the reserved uiid) and then create the SequenceActivity using the reserved uiid.
     */
    protected JSONObject createSequenceActivity(Integer reservedUiid, Integer parentUIID, Integer parentActivityType,
	    Integer firstActivityUiid, int orderId, String branchName) throws JSONException {
	JSONObject activityJSON = new JSONObject();
	activityJSON.put(AuthoringJsonTags.ACTIVITY_UIID, reservedUiid);
	activityJSON.put(AuthoringJsonTags.GROUPING_SUPPORT_TYPE, Activity.GROUPING_SUPPORT_OPTIONAL);
	activityJSON.put(AuthoringJsonTags.APPLY_GROUPING, false);
	activityJSON.put(AuthoringJsonTags.ACTIVITY_TITLE, branchName != null ? branchName : "Branch " + orderId);
	activityJSON.put(AuthoringJsonTags.ACTIVITY_CATEGORY_ID, Activity.CATEGORY_SYSTEM);
	activityJSON.put(AuthoringJsonTags.ACTIVITY_TYPE_ID, Activity.SEQUENCE_ACTIVITY_TYPE);
	activityJSON.put(AuthoringJsonTags.ORDER_ID, orderId);
	activityJSON.put(AuthoringJsonTags.PARENT_UIID, parentUIID);
	activityJSON.put(PARENT_ACTIVITY_TYPE, parentActivityType);
	activityJSON.put(AuthoringJsonTags.DEFAULT_ACTIVITY_UIID, firstActivityUiid);
	activityJSON.put(AuthoringJsonTags.STOP_AFTER_ACTIVITY, false);
	return activityJSON;
    }

    /**
     * Map a branch to a group. Creates JSON similar to:
     * {"entryUIID":16,"groupUIID":11,"branchingActivityUIID":1,"sequenceActivityUIID":3}
     * Note: it needs the UIID of the matching group, not of the grouping activity
     * 
     * @throws JSONException
     */
    protected JSONObject createBranchMapping(AtomicInteger uiid, Integer groupUiid, Integer branchingActivityUiid,
	    Integer sequenceActivityUiid) throws JSONException {
	JSONObject bmJSON = new JSONObject();
	bmJSON.put(AuthoringJsonTags.BRANCH_ACTIVITY_ENTRY_UIID, uiid.incrementAndGet());
	bmJSON.put(AuthoringJsonTags.GROUP_UIID, groupUiid);
	bmJSON.put(AuthoringJsonTags.BRANCH_ACTIVITY_UIID, branchingActivityUiid);
	bmJSON.put(AuthoringJsonTags.BRANCH_SEQUENCE_ACTIVITY_UIID, sequenceActivityUiid);
	return bmJSON;

    }

    /**
     * Create the overall branching activity, which will have branch activities as its children.
     * Branch selected based on a group.
     *
     * The branching activity has a circular dependence - to define the BranchingActivity you need the UIID of the
     * default branch (sequence).
     * But the default sequence needs the BranchingActivity's UIID as its parent activity UIID.
     * So "reserve" a uuid (using incrementAndGet()) in the template for the BranchingActivity, create the branches
     * (setting
     * the parent activity as the reserved uiid) and then create the BranchingActivity using the reserved uiid.
     */
    protected JSONObject createGroupBranchingActivity(Integer reservedUiid, Integer defaultBranchUiid,
	    Integer groupingUiid, int order, Integer[] layoutCoords, Integer[] startCoords, Integer[] endCoords,
	    String activityTitle) throws JSONException {
	JSONObject activityJSON = createBranchingActivity(reservedUiid, defaultBranchUiid, groupingUiid, order,
		layoutCoords, startCoords, endCoords, activityTitle, Activity.GROUP_BRANCHING_ACTIVITY_TYPE);
	return activityJSON;
    }

    /**
     * Create the overall branching activity, which will have branch activities as its children.
     * TODO Branch selected by monitor. Not yet implemented as the branch mappings are yet to be investigated.
     */
//    protected JSONObject createChosenBranchingActivity(AtomicInteger uiid, int order, Integer[] layoutCoords) throws JSONException {
//	return createBranchingActivity(uiid, order, layoutCoords, Activity.CHOSEN_BRANCHING_ACTIVITY_TYPE);
//    }

    /**
     * Create the overall branching activity, which will have branch activities as its children.
     * Supports instructor's choice and grouped, but not based on learner output as conditions
     * are not implemented in the REST authoring for the tools
     */
    private JSONObject createBranchingActivity(Integer reservedUiid, Integer defaultBranchUiid, Integer groupingUiid,
	    int order, Integer[] layoutCoords, Integer[] startCoords, Integer[] endCoords, String activityTitle,
	    int activityType) throws JSONException {

	JSONObject activityJSON = new JSONObject();
	activityJSON.put(AuthoringJsonTags.ACTIVITY_UIID, reservedUiid);
	activityJSON.put(AuthoringJsonTags.GROUPING_SUPPORT_TYPE, Activity.GROUPING_SUPPORT_OPTIONAL);
	activityJSON.put(AuthoringJsonTags.ACTIVITY_TITLE, activityTitle != null ? activityTitle : "Branching");
	activityJSON.put(AuthoringJsonTags.ACTIVITY_CATEGORY_ID, Activity.CATEGORY_SYSTEM);
	activityJSON.put(AuthoringJsonTags.ACTIVITY_TYPE_ID, activityType);
	activityJSON.putOpt(AuthoringJsonTags.MAX_ACTIVITIES, MAX_FLOATING_ACTIVITY_OPTIONS);
	activityJSON.put(AuthoringJsonTags.XCOORD, layoutCoords[0]);
	activityJSON.put(AuthoringJsonTags.YCOORD, layoutCoords[1]);
	activityJSON.put(AuthoringJsonTags.START_XCOORD, startCoords[0]);
	activityJSON.put(AuthoringJsonTags.START_YCOORD, startCoords[1]);
	activityJSON.put(AuthoringJsonTags.END_XCOORD, endCoords[0]);
	activityJSON.put(AuthoringJsonTags.END_YCOORD, endCoords[1]);
	activityJSON.put(AuthoringJsonTags.STOP_AFTER_ACTIVITY, false);
	activityJSON.put(AuthoringJsonTags.DEFAULT_ACTIVITY_UIID, defaultBranchUiid);

	if (groupingUiid != null) {
	    activityJSON.put(AuthoringJsonTags.GROUPING_UIID, groupingUiid);
	    activityJSON.put(AuthoringJsonTags.APPLY_GROUPING, true);
	} else {
	    activityJSON.put(AuthoringJsonTags.APPLY_GROUPING, false);
	}

	return activityJSON;
    }

    /* ************************************** Tool related methods ********************************************** */
    /** General method to create a tool content. All calls to create tool content should go through this method */
    protected Long createToolContent(UserDTO user, String toolSignature, JSONObject toolContentJSON)
	    throws JSONException, HttpException, IOException {

	try {
	    Tool tool = getTool(toolSignature);
	    Long toolContentID = getAuthoringService().insertToolContentID(tool.getToolId());

	    // Tools' services implement an interface for processing REST requests
	    ToolRestManager toolRestService = (ToolRestManager) getLamsCoreToolService().findToolService(tool);
	    toolRestService.createRestToolContent(user.getUserID(), toolContentID, toolContentJSON);

	    return toolContentID;
	} catch (Exception e) {
	    LdTemplateAction.log.error("Unable to create tool content for " + toolSignature + " with details "
		    + toolContentJSON
		    + ". \nThe tool probably threw an exception - check the server logs for more details.\n"
		    + "If the exception is \"Servlet.service() for servlet ToolContentRestServlet threw exception java.lang.ClassCastException: com.sun.proxy.$ProxyXXX cannot be cast to org.lamsfoundation.lams.rest.ToolRestManager)\""
		    + " then the tool doesn't support the LDTemplate service calls (ie has not implemented the ToolRestManager interface / createRestToolContent() method.");
	    throw new HttpException(
		    "Unable to create tool content for " + toolSignature + " with details " + toolContentJSON);
	}

    }

    /**
     * General method to create tool activity. All calls to create an activity relating to a tool should go through this
     * method
     */
    protected JSONObject createToolActivity(AtomicInteger uiid, int order, Integer[] layoutCoords, String toolSignature,
	    String toolIcon, Long toolContentID, String contentFolderID, Integer groupingUIID, Integer parentUIID,
	    Integer parentActivityType, String activityTitle, int activityCategory) throws JSONException {
	JSONObject activityJSON = new JSONObject();
	Tool tool = getTool(toolSignature);

	Integer[] pos = layoutCoords != null ? layoutCoords : calcPosition(order);

	activityJSON.put(AuthoringJsonTags.ACTIVITY_UIID, uiid.incrementAndGet());
	activityJSON.put(RestTags.CONTENT_FOLDER_ID, contentFolderID);
	activityJSON.put(AuthoringJsonTags.TOOL_ID, tool.getToolId());
	activityJSON.put(AuthoringJsonTags.LEARNING_LIBRARY_ID, tool.getLearningLibraryId());
	activityJSON.put(AuthoringJsonTags.TOOL_CONTENT_ID, toolContentID);
	activityJSON.put(AuthoringJsonTags.GROUPING_SUPPORT_TYPE, 2); // based on values in lams_learning_activity field - this seem to be
	// 2 for all tools
	activityJSON.put(AuthoringJsonTags.LIBRARY_IMAGE, toolIcon);
	activityJSON.put(AuthoringJsonTags.XCOORD, pos[0]);
	activityJSON.put(AuthoringJsonTags.YCOORD, pos[1]);
	activityJSON.put(AuthoringJsonTags.ACTIVITY_TITLE, activityTitle != null ? activityTitle : "Activity");
	activityJSON.put(AuthoringJsonTags.ACTIVITY_CATEGORY_ID, activityCategory);
	activityJSON.put(AuthoringJsonTags.ACTIVITY_TYPE_ID, Activity.TOOL_ACTIVITY_TYPE);
	if (parentUIID != null) {
	    activityJSON.put(AuthoringJsonTags.PARENT_UIID, parentUIID);
	    activityJSON.put(PARENT_ACTIVITY_TYPE, parentActivityType);
	}
	if (groupingUIID != null) {
	    activityJSON.put(AuthoringJsonTags.GROUPING_UIID, groupingUIID);
	    activityJSON.put(AuthoringJsonTags.APPLY_GROUPING, true);
	} else {
	    activityJSON.put(AuthoringJsonTags.APPLY_GROUPING, false);
	}
	return activityJSON;
    }

    /** Sets up the standard fields that are used by many tools! */
    protected JSONObject createStandardToolContent(String title, String instructions, String reflectionInstructions,
	    Boolean lockWhenFinished, Boolean allowRichTextEditor, UserDTO user) throws JSONException {
	JSONObject toolContentJSON = new JSONObject();
	toolContentJSON.put(RestTags.TITLE, title != null ? title : "");
	toolContentJSON.put(RestTags.INSTRUCTIONS, instructions != null ? instructions : "");

	if (reflectionInstructions != null) {
	    toolContentJSON.put(RestTags.REFLECT_ON_ACTIVITY, true);
	    toolContentJSON.put(RestTags.REFLECT_INSTRUCTIONS, reflectionInstructions);
	}

	toolContentJSON.putOpt(RestTags.LOCK_WHEN_FINISHED, lockWhenFinished);
	toolContentJSON.putOpt(RestTags.ALLOW_RICH_TEXT_EDITOR, allowRichTextEditor);

	if (user != null) {
	    toolContentJSON.put("firstName", user.getFirstName());
	    toolContentJSON.put("lastName", user.getLastName());
	    toolContentJSON.put("loginName", user.getLogin());
	}
	return toolContentJSON;
    }

    /**
     * Helper method to create a Assessment tool content. Assessment is one of the unusuals tool in that it caches
     * user's login names and
     * first/last names Mandatory fields in toolContentJSON: title, instructions, resources, user fields firstName,
     * lastName and loginName.
     *
     * Required fields in toolContentJSON: "title", "instructions", "questions", "firstName", "lastName", "lastName",
     * "questions" and "references".
     *
     * The questions entry should be JSONArray containing JSON objects, which in turn must contain
     * "questionTitle", "questionText", "displayOrder" (Integer), "type" (Integer). If the type is Multiple Choice,
     * Numerical or Matching Pairs
     * then a JSONArray "answers" is required.
     *
     * The answers entry should be JSONArray
     * containing JSON objects, which in turn must contain "answerText" or "answerFloat", "displayOrder" (Integer),
     * "grade" (Integer).
     *
     * For the templates, all the questions that are created will be set up as references, therefore the questions in
     * the assessment == the bank of questions.
     * So references entry will be a JSONArray containing JSON objects, which in turn must contain "displayOrder"
     * (Integer),
     * "questionDisplayOrder" (Integer - to match to the question). If default grade or random questions are needed then
     * this method needs
     * to be expanded.
     */
    protected Long createAssessmentToolContent(UserDTO user, String title, String instructions,
	    String reflectionInstructions, JSONArray questions)
	    throws JSONException, HttpException, IOException {

	JSONObject toolContentJSON = createStandardToolContent(title, instructions, reflectionInstructions, null, null,
		user);
	toolContentJSON.put(RestTags.QUESTIONS, questions);

	JSONArray references = new JSONArray();
	for (int i = 0; i < questions.length(); i++) {
	    Integer questionDisplayOrder = ((JSONObject) questions.get(i)).getInt(RestTags.DISPLAY_ORDER);
	    references.put(new JSONObject().put(RestTags.DISPLAY_ORDER, questionDisplayOrder)
		    .put("questionDisplayOrder", questionDisplayOrder));
	}
	toolContentJSON.put("references", references);

	return createToolContent(user, LdTemplateAction.ASSESSMENT_TOOL_SIGNATURE, toolContentJSON);
    }

    // assessment type - copied from ResourceConstants
    // question type;
    public static final short ASSESSMENT_QUESTION_TYPE_MULTIPLE_CHOICE = 1;
    public static final short ASSESSMENT_QUESTION_TYPE_MATCHING_PAIRS = 2;
    public static final short ASSESSMENT_QUESTION_TYPE_SHORT_ANSWER = 3;
    public static final short ASSESSMENT_QUESTION_TYPE_NUMERICAL = 4;
    public static final short ASSESSMENT_QUESTION_TYPE_TRUE_FALSE = 5;
    public static final short ASSESSMENT_QUESTION_TYPE_ESSAY = 6;
    public static final short ASSESSMENT_QUESTION_TYPE_ORDERING = 7;

    protected JSONObject createAssessmentQuestionEssayType(String questionTitle, String questionText, int displayOrder,
	    boolean required) throws JSONException {
	JSONObject essayJSON = new JSONObject();
	essayJSON.put(RestTags.QUESTION_TITLE, questionTitle != null ? questionTitle : "");
	essayJSON.put(RestTags.QUESTION_TEXT, questionText != null ? questionText : "");
	essayJSON.put("type", ASSESSMENT_QUESTION_TYPE_ESSAY);
	essayJSON.put(RestTags.DISPLAY_ORDER, displayOrder);
	essayJSON.put("answerRequired", required);
	return essayJSON;
    }

    protected JSONObject createAssessmentQuestionMultipleChoiceType(String questionTitle, String questionText,
	    int displayOrder, boolean required, List<AssessMCAnswer> answers) throws JSONException {
	JSONObject mcJSON = createAssessmentQuestionEssayType(questionTitle, questionText, displayOrder, required);
	JSONArray answersJSON = new JSONArray();
	for (AssessMCAnswer answer : answers) {
	    answersJSON.put(answer.getAsJSONObject());
	}
	mcJSON.put(RestTags.ANSWERS, answersJSON);
	mcJSON.put("type", ASSESSMENT_QUESTION_TYPE_MULTIPLE_CHOICE);
	log.debug("assessment: " + mcJSON);
	return mcJSON;
    }

    /**
     * Creates a forum activity's JSON details.
     */
    protected JSONObject createAssessmentActivity(AtomicInteger uiid, int order, Integer[] layoutCoords,
	    Long toolContentID, String contentFolderID, Integer groupingUIID, Integer parentUIID,
	    Integer parentActivityType, String activityTitle) throws JSONException {

	return createToolActivity(uiid, order, layoutCoords, LdTemplateAction.ASSESSMENT_TOOL_SIGNATURE,
		LdTemplateAction.ASSESSMENT_ICON, toolContentID, contentFolderID, groupingUIID, parentUIID,
		parentActivityType, activityTitle != null ? activityTitle : "Assessment", Activity.CATEGORY_ASSESSMENT);
    }

    /**
     * Helper method to create a chat tool content. Only title and instructions are needed but we support reflection,
     * lock on
     * finished and filterKeywords in case it is wanted. The keywords should be a comma deliminated string.
     */
    protected Long createChatToolContent(UserDTO user, String title, String instructions,
	    boolean lockWhenFinished, String filterKeywords, String reflectionInstructions)
	    throws JSONException, HttpException, IOException {

	JSONObject toolContentJSON = createStandardToolContent(title, instructions, reflectionInstructions,
		lockWhenFinished, null, null);
	toolContentJSON.put("filterKeywords", filterKeywords);
	return createToolContent(user, LdTemplateAction.CHAT_TOOL_SIGNATURE, toolContentJSON);
    }

    /**
     * Creates a noticeboard activity's JSON details.
     */
    protected JSONObject createChatActivity(AtomicInteger uiid, int order, Integer[] layoutCoords, Long toolContentID,
	    String contentFolderID, Integer groupingUIID, Integer parentUIID, Integer parentActivityType,
	    String activityTitle) throws JSONException {

	return createToolActivity(uiid, order, layoutCoords, LdTemplateAction.CHAT_TOOL_SIGNATURE,
		LdTemplateAction.CHAT_ICON, toolContentID, contentFolderID, groupingUIID, parentUIID,
		parentActivityType, activityTitle != null ? activityTitle : "Chat", Activity.CATEGORY_COLLABORATION);
    }

    /**
     * Helper method to create a forum tool content. Forum is an unusual tool in that it caches user's login names and
     * first/last names Mandatory fields: title & instructions; userDTO which gives user's firstName, lastName &
     * loginName; topics which is Set<Map> where the <Map> contains the keys subject & body There should be at least one
     * topic.
     *
     * Optional fields: allowAnonym, allowEdit, allowNewTopic, allowRateMessages, allowRichTextEditor, allowUpload,
     * limitedMaxCharacters, limitedMinCharacters, lockWhenFinished, maxCharacters, maximumRate, maximumReply,
     * minCharacters minimumRate, minimumReply, notifyLearnersOnForumPosting, notifyLearnersOnMarkRelease,
     * notifyTeachersOnForumPosting reflectInstructions, reflectOnActivity, submissionDeadline
     * 
     * @param limitedMaxCharacters
     *            Should the maximum number of characters in a posting be limited
     * @param maxCharacters
     *            if limitedMaxCharacters == true then if maxCharacters == null let forum use default otherwise use the
     *            value supplied.
     */
    protected Long createForumToolContent(UserDTO user, String title, String instructions,
	    boolean lockWhenFinished, boolean allowRichTextEditor, boolean allowNewTopic, boolean allowRateMessages,
	    boolean allowUpload, boolean limitedMaxCharacters, Integer maxCharacters, JSONArray topics)
	    throws JSONException, HttpException, IOException {

	JSONObject toolContentJSON = createStandardToolContent(title, instructions, null, lockWhenFinished,
		allowRichTextEditor, user);
	toolContentJSON.put("topics", topics);
	toolContentJSON.put("allowNewTopic", allowNewTopic);
	toolContentJSON.put("allowRateMessages", allowRateMessages);
	toolContentJSON.put("allowUpload", allowUpload);
	toolContentJSON.put("limitedMaxCharacters", limitedMaxCharacters);
	if (limitedMaxCharacters && maxCharacters != null) {
	    toolContentJSON.put("maxCharacters", maxCharacters);
	}
	return createToolContent(user, LdTemplateAction.FORUM_TOOL_SIGNATURE, toolContentJSON);
    }

    /**
     * Creates a forum activity's JSON details.
     */
    protected JSONObject createForumActivity(AtomicInteger uiid, int order, Integer[] layoutCoords, Long toolContentID,
	    String contentFolderID, Integer groupingUIID, Integer parentUIID, Integer parentActivityType,
	    String activityTitle) throws JSONException {

	return createToolActivity(uiid, order, layoutCoords, LdTemplateAction.FORUM_TOOL_SIGNATURE,
		LdTemplateAction.FORUM_ICON, toolContentID, contentFolderID, groupingUIID, parentUIID,
		parentActivityType, activityTitle != null ? activityTitle : "Forum", Activity.CATEGORY_COLLABORATION);
    }

    /**
     * Helper method to create a leader selection tool content. Only title and instructions are needed. Leader selection
     * does not support lockWhenFinished.
     */
    protected Long createLeaderSelectionToolContent(UserDTO user, String title, String instructions)
	    throws JSONException, HttpException, IOException {

	JSONObject toolContentJSON = createStandardToolContent(title, instructions, null, null, null, null);
	return createToolContent(user, LdTemplateAction.LEADER_TOOL_SIGNATURE, toolContentJSON);
    }

    /**
     * Creates a leader selection activity's JSON details.
     */
    protected JSONObject createLeaderSelectionActivity(AtomicInteger uiid, int order, Integer[] layoutCoords,
	    Long toolContentID, String contentFolderID, Integer groupingUIID, Integer parentUIID,
	    Integer parentActivityType, String activityTitle) throws JSONException {

	return createToolActivity(uiid, order, layoutCoords, LdTemplateAction.LEADER_TOOL_SIGNATURE,
		LdTemplateAction.LEADER_ICON, toolContentID, contentFolderID, groupingUIID, parentUIID,
		parentActivityType, activityTitle != null ? activityTitle : "Leader Selection",
		Activity.CATEGORY_RESPONSE);
    }

    /**
     * Helper method to create a notebook tool content. Only title and instructions are needed but we support lock on
     * finished and allow rich text editor in case it is wanted.
     */
    protected Long createNotebookToolContent(UserDTO user, String title, String instructions,
	    boolean lockWhenFinished, boolean allowRichTextEditor) throws JSONException, HttpException, IOException {

	JSONObject toolContentJSON = createStandardToolContent(title, instructions, null, lockWhenFinished,
		allowRichTextEditor, null);
	return createToolContent(user, LdTemplateAction.NOTEBOOK_TOOL_SIGNATURE, toolContentJSON);
    }

    /**
     * Creates a noticeboard activity's JSON details.
     */
    protected JSONObject createNotebookActivity(AtomicInteger uiid, int order, Integer[] layoutCoords,
	    Long toolContentID, String contentFolderID, Integer groupingUIID, Integer parentUIID,
	    Integer parentActivityType, String activityTitle) throws JSONException {

	return createToolActivity(uiid, order, layoutCoords, LdTemplateAction.NOTEBOOK_TOOL_SIGNATURE,
		LdTemplateAction.NOTEBOOK_ICON, toolContentID, contentFolderID, groupingUIID, parentUIID,
		parentActivityType, activityTitle != null ? activityTitle : "Notebook", Activity.CATEGORY_RESPONSE);
    }

    /**
     * Helper method to create a noticeboard tool content. Only title and content are needed for noticeboard! But we
     * support a reflection in case it is wanted.
     */
    protected Long createNoticeboardToolContent(UserDTO user, String title, String content,
	    String reflectionInstructions) throws JSONException, HttpException, IOException {

	JSONObject toolContentJSON = createStandardToolContent(title, null, reflectionInstructions, null, null, null);
	toolContentJSON.put("content", content != null ? content : "");
	return createToolContent(user, LdTemplateAction.NOTICEBOARD_TOOL_SIGNATURE, toolContentJSON);
    }

    /**
     * Creates a noticeboard activity's JSON details.
     */
    protected JSONObject createNoticeboardActivity(AtomicInteger uiid, int order, Integer[] layoutCoords,
	    Long toolContentID, String contentFolderID, Integer groupingUIID, Integer parentUIID,
	    Integer parentActivityType, String activityTitle) throws JSONException {

	return createToolActivity(uiid, order, layoutCoords, LdTemplateAction.NOTICEBOARD_TOOL_SIGNATURE,
		LdTemplateAction.NOTICEBOARD_ICON, toolContentID, contentFolderID, groupingUIID, parentUIID,
		parentActivityType, activityTitle != null ? activityTitle : "Noticeboard", Activity.CATEGORY_CONTENT);
    }

    /**
     * Helper method to create a Q&A tool content. Only title and instructions are needed but we support other fields in
     * case they are wanted.
     */
    protected Long createQAToolContent(UserDTO user, String title, String instructions,
	    boolean lockWhenFinished, boolean allowRichTextEditor, boolean oneQuestionPerPage,
	    boolean showOtherLearnersAnswers, boolean showOtherLearnersNames, JSONArray questions)
	    throws JSONException, HttpException, IOException {

	JSONObject toolContentJSON = createStandardToolContent(title, instructions, null, lockWhenFinished,
		allowRichTextEditor, null);
	toolContentJSON.put(RestTags.QUESTIONS, questions);
	toolContentJSON.put("questionsSequenced", oneQuestionPerPage);
	toolContentJSON.put("showOtherAnswers", showOtherLearnersAnswers);
	toolContentJSON.put("usernameVisible", showOtherLearnersNames);
	return createToolContent(user, LdTemplateAction.QA_TOOL_SIGNATURE, toolContentJSON);
    }

    /**
     * Creates a Q&A activity's JSON details.
     */
    protected JSONObject createQAActivity(AtomicInteger uiid, int order, Integer[] layoutCoords, Long toolContentID,
	    String contentFolderID, Integer groupingUIID, Integer parentUIID, Integer parentActivityType,
	    String activityTitle) throws JSONException {

	return createToolActivity(uiid, order, layoutCoords, LdTemplateAction.QA_TOOL_SIGNATURE,
		LdTemplateAction.QA_ICON, toolContentID, contentFolderID, groupingUIID, parentUIID, parentActivityType,
		activityTitle != null ? activityTitle : "Q&A", Activity.CATEGORY_RESPONSE);
    }

    /**
     * Helper method to create a MCQ tool content. Title, instructions and questions are required (see tool for full
     * details of questions). Other fields are optional.
     */
    protected Long createMCQToolContent(UserDTO user, String title, String instructions,
	    boolean useSelectLeaderToolOuput, JSONArray questions) throws JSONException, HttpException, IOException {

	JSONObject toolContentJSON = createStandardToolContent(title, instructions, null, null, null, null);
	toolContentJSON.put(RestTags.USE_SELECT_LEADER_TOOL_OUTPUT, useSelectLeaderToolOuput);
	toolContentJSON.put(RestTags.QUESTIONS, questions);
	return createToolContent(user, LdTemplateAction.MCQ_TOOL_SIGNATURE, toolContentJSON);
    }

    /**
     * Creates a MCQ activity's JSON details.
     */
    protected JSONObject createMCQActivity(AtomicInteger uiid, int order, Integer[] layoutCoords, Long toolContentID,
	    String contentFolderID, Integer groupingUIID, Integer parentUIID, Integer parentActivityType,
	    String activityTitle) throws JSONException {

	return createToolActivity(uiid, order, layoutCoords, LdTemplateAction.MCQ_TOOL_SIGNATURE,
		LdTemplateAction.MCQ_ICON, toolContentID, contentFolderID, groupingUIID, parentUIID, parentActivityType,
		activityTitle != null ? activityTitle : "Multiple Choice", Activity.CATEGORY_ASSESSMENT);
    }

    /**
     * Helper method to create a mindmap tool content.
     */
    protected Long createMindmapToolContent(UserDTO user, String title, String instructions,
	    boolean lockWhenFinished, boolean multiUserMode, String reflectionInstruction)
	    throws JSONException, HttpException, IOException {

	JSONObject toolContentJSON = createStandardToolContent(title, instructions, null, lockWhenFinished, null, null);
	toolContentJSON.put("multiUserMode", multiUserMode);
	return createToolContent(user, LdTemplateAction.MINDMAP_TOOL_SIGNATURE, toolContentJSON);
    }

    /**
     * Creates a multiUserMode activity's JSON details.
     */
    protected JSONObject createMindmapActivity(AtomicInteger uiid, int order, Integer[] layoutCoords,
	    Long toolContentID, String contentFolderID, Integer groupingUIID, Integer parentUIID,
	    Integer parentActivityType, String activityTitle) throws JSONException {

	return createToolActivity(uiid, order, layoutCoords, LdTemplateAction.MINDMAP_TOOL_SIGNATURE,
		LdTemplateAction.MINDMAP_ICON, toolContentID, contentFolderID, groupingUIID, parentUIID,
		parentActivityType, activityTitle != null ? activityTitle : "MindMap", Activity.CATEGORY_RESPONSE);
    }

    /**
     * Helper method to create a forum tool content. Forum is an unusual tool in that it caches user's login names and
     * first/last names Mandatory fields in toolContentJSON: title, instructions, resources, user fields firstName,
     * lastName and loginName Topics must contain a JSONArray of JSONObject objects, which have the following mandatory
     * fields: title, description, type There should be at least one resource object in the Topics array. See
     * ResourceService for the optional entries.
     */
    protected Long createResourcesToolContent(UserDTO user, String title, String instructions,
	    boolean lockWhenFinished, boolean runContentAutomatically, boolean allowLearnerAddURL,
	    boolean allowLearnerAddFile, boolean notifyInstructors, Integer minResourcesToView,
	    String reflectionInstructions, JSONArray resources)
	    throws JSONException, HttpException, IOException {

	JSONObject toolContentJSON = createStandardToolContent(title, instructions, reflectionInstructions,
		lockWhenFinished, null, user);
	toolContentJSON.put("allowAddFiles", allowLearnerAddFile);
	toolContentJSON.put("allowAddUrls", allowLearnerAddURL);
	toolContentJSON.put("notifyTeachersOnAssigmentSumbit", notifyInstructors);
	toolContentJSON.put("runAuto", runContentAutomatically);
	if (minResourcesToView != null) {
	    toolContentJSON.put("minViewResourceNumber", minResourcesToView);
	}
	toolContentJSON.put("resources", resources);
	return createToolContent(user, LdTemplateAction.SHARE_RESOURCES_TOOL_SIGNATURE, toolContentJSON);
    }

    // resource type - copied from ResourceConstants
    public static final short RESOURCE_TYPE_URL = 1;
    public static final short RESOURCE_TYPE_FILE = 2;
    public static final short RESOURCE_TYPE_WEBSITE = 3;
    public static final short RESOURCE_TYPE_LEARNING_OBJECT = 4;

    private JSONObject createResourceItem(String title, short type, String[] instructions, File file, int displayOrder)
	    throws JSONException {
	JSONObject item = new JSONObject();
	item.put(RestTags.TITLE, title != null ? title : "");
	item.put("type", type);
	item.put(RestTags.DISPLAY_ORDER, displayOrder);

	if (instructions != null) {
	    item.put("instructions", instructions);
	} else {
	    item.put("instructions", new JSONArray());
	}

	// TODO files - need to save it somehow, validate the file size, etc
	if (type != LdTemplateAction.RESOURCE_TYPE_URL) {
	    LdTemplateAction.log
		    .warn("LD Templates not handling files yet - file, website & LO resources won't work. Filename "
			    + file.getAbsoluteFile());
	}
	return item;
    }

    protected JSONObject createResourceURL(String title, String[] instructions, String URL, boolean openInNewWindow,
	    int displayOrder) throws JSONException {
	JSONObject obj = createResourceItem(title, LdTemplateAction.RESOURCE_TYPE_URL, instructions, null,
		displayOrder);
	obj.put("url", URL);
	obj.put("openUrlNewWindow", openInNewWindow);
	return obj;
    }

    protected JSONObject createResourceFile(String title, String description, String[] instructions, File file,
	    int displayOrder) throws JSONException {
	JSONObject obj = createResourceItem(title, LdTemplateAction.RESOURCE_TYPE_FILE, instructions, file,
		displayOrder);
	obj.put("description", description);
	return obj;
    }

    protected JSONObject createResourceWebsite(String title, String description, String[] instructions, File file,
	    int displayOrder) throws JSONException {
	JSONObject obj = createResourceItem(title, LdTemplateAction.RESOURCE_TYPE_WEBSITE, instructions, file,
		displayOrder);
	obj.put("description", description);
	return obj;
    }

    protected JSONObject createResourceLearningObject(String title, String description, String[] instructions,
	    File file, int displayOrder) throws JSONException {
	JSONObject obj = createResourceItem(title, LdTemplateAction.RESOURCE_TYPE_LEARNING_OBJECT, instructions, file,
		displayOrder);
	obj.put("description", description);
	return obj;
    }

    /**
     * Creates a forum activity's JSON details.
     */
    protected JSONObject createResourcesActivity(AtomicInteger uiid, int order, Integer[] layoutCoords,
	    Long toolContentID, String contentFolderID, Integer groupingUIID, Integer parentUIID,
	    Integer parentActivityType, String activityTitle) throws JSONException {

	return createToolActivity(uiid, order, layoutCoords, LdTemplateAction.SHARE_RESOURCES_TOOL_SIGNATURE,
		LdTemplateAction.SHARE_RESOURCES_ICON, toolContentID, contentFolderID, groupingUIID, parentUIID,
		parentActivityType, activityTitle != null ? activityTitle : "Share Resources",
		Activity.CATEGORY_CONTENT);
    }

    /**
     * Helper method to create a Scratchie tool content. Title, instructions and questions are required (see tool for
     * full details of questions). Other fields are optional.
     */
    protected Long createScratchieToolContent(UserDTO user, String title, String instructions,
	    boolean useSelectLeaderToolOuput, JSONArray questions) throws JSONException, HttpException, IOException {

	JSONObject toolContentJSON = createStandardToolContent(title, instructions, null, null, null, null);
	toolContentJSON.put(RestTags.QUESTIONS, questions);
	toolContentJSON.put(RestTags.USE_SELECT_LEADER_TOOL_OUTPUT, useSelectLeaderToolOuput);
	return createToolContent(user, LdTemplateAction.SCRATCHIE_TOOL_SIGNATURE, toolContentJSON);
    }

    /**
     * Creates a Scratchie activity's JSON details.
     */
    protected JSONObject createScratchieActivity(AtomicInteger uiid, int order, Integer[] layoutCoords,
	    Long toolContentID, String contentFolderID, Integer groupingUIID, Integer parentUIID,
	    Integer parentActivityType, String activityTitle) throws JSONException {

	return createToolActivity(uiid, order, layoutCoords, LdTemplateAction.SCRATCHIE_TOOL_SIGNATURE,
		LdTemplateAction.SCRATCHIE_ICON, toolContentID, contentFolderID, groupingUIID, parentUIID,
		parentActivityType, activityTitle != null ? activityTitle : "Scratchie", Activity.CATEGORY_CONTENT);
    }

    /**
     * Helper method to create a scribe tool content. Headings which is JSONArray of strings. There should be at least
     * one heading. The scribe service treats reflectInstructions as optional
     */
    protected Long createScribeToolContent(UserDTO user, String title, String instructions,
	    boolean lockWhenFinished, boolean autoSelectScribe, boolean showAggregatedReports,
	    String reflectionInstructions, JSONArray questions) throws JSONException, HttpException, IOException {

	JSONObject toolContentJSON = createStandardToolContent(title, instructions, reflectionInstructions,
		lockWhenFinished, null, null);
	toolContentJSON.put(RestTags.QUESTIONS, questions);
	toolContentJSON.put("autoSelectScribe", autoSelectScribe);
	toolContentJSON.put("showAggregatedReports", showAggregatedReports);
	return createToolContent(user, LdTemplateAction.SCRIBE_TOOL_SIGNATURE, toolContentJSON);
    }

    /**
     * Creates a scribe activity's JSON details.
     */
    protected JSONObject createScribeActivity(AtomicInteger uiid, int order, Integer[] layoutCoords, Long toolContentID,
	    String contentFolderID, Integer groupingUIID, Integer parentUIID, Integer parentActivityType,
	    String activityTitle) throws JSONException {

	return createToolActivity(uiid, order, layoutCoords, LdTemplateAction.SCRIBE_TOOL_SIGNATURE,
		LdTemplateAction.SCRIBE_ICON, toolContentID, contentFolderID, groupingUIID, parentUIID,
		parentActivityType, activityTitle != null ? activityTitle : "Scribe", Activity.CATEGORY_COLLABORATION);
    }

    /**
     * Helper method to create a submit tool content. Another tool that caches user's login names and
     * first/last names Mandatory fields: title & instructions; userDTO which gives user's firstName, lastName &
     * loginName;
     */
    protected Long createSubmitToolContent(UserDTO user, String title, String instructions,
	    boolean lockWhenFinished, Boolean limitUpload, Integer limitUploadNumber, String reflectionInstructions) 
		    throws JSONException, HttpException, IOException {

	JSONObject toolContentJSON = createStandardToolContent(title, instructions, reflectionInstructions,
		lockWhenFinished, null, user);
	if (limitUploadNumber != null) {
	    toolContentJSON.put("limitUpload", limitUpload != null ? limitUpload : true);
	    toolContentJSON.put("limitUploadNumber", limitUploadNumber);
	}
	return createToolContent(user, LdTemplateAction.SUBMIT_TOOL_SIGNATURE, toolContentJSON);
    }

    /**
     * Creates a forum activity's JSON details.
     */
    protected JSONObject createSubmitActivity(AtomicInteger uiid, int order, Integer[] layoutCoords, Long toolContentID,
	    String contentFolderID, Integer groupingUIID, Integer parentUIID, Integer parentActivityType,
	    String activityTitle) throws JSONException {

	return createToolActivity(uiid, order, layoutCoords, LdTemplateAction.SUBMIT_TOOL_SIGNATURE,
		LdTemplateAction.SUBMIT_ICON, toolContentID, contentFolderID, groupingUIID, parentUIID,
		parentActivityType, activityTitle != null ? activityTitle : "Submit File",
		Activity.CATEGORY_ASSESSMENT);
    }

    /**
     * Helper method to create a survey tool content. Another tool that caches user's login names and
     * first/last names! See the survey implementation for the full field list.
     */
    protected Long createSurveyToolContent(UserDTO user, String title, String instructions,
	    Boolean lockWhenFinished, JSONArray questions)
	    throws JSONException, HttpException, IOException {

	JSONObject toolContentJSON = createStandardToolContent(title, instructions, null, lockWhenFinished, null, user);
	toolContentJSON.put("questions", questions);
	return createToolContent(user, LdTemplateAction.SURVEY_TOOL_SIGNATURE, toolContentJSON);
    }

    /**
     * Creates a forum activity's JSON details.
     */
    protected JSONObject createSurveyActivity(AtomicInteger uiid, int order, Integer[] layoutCoords, Long toolContentID,
	    String contentFolderID, Integer groupingUIID, Integer parentUIID, Integer parentActivityType,
	    String activityTitle) throws JSONException {

	return createToolActivity(uiid, order, layoutCoords, LdTemplateAction.SURVEY_TOOL_SIGNATURE,
		LdTemplateAction.SURVEY_ICON, toolContentID, contentFolderID, groupingUIID, parentUIID,
		parentActivityType, activityTitle != null ? activityTitle : "Survey", Activity.CATEGORY_RESPONSE);
    }

    /**
     * Helper method to create a Vote tool content. Title, instructions and answers (Array of String) are required.
     * Other fields are optional.
     */
    protected Long createVoteToolContent(UserDTO user, String title, String instructions,
	    JSONArray answers, Boolean showResults) throws JSONException, HttpException, IOException {

	JSONObject toolContentJSON = createStandardToolContent(title, instructions, null, null, null, null);
	toolContentJSON.put(RestTags.ANSWERS, answers);
	toolContentJSON.putOpt("showResults", showResults);
	return createToolContent(user, LdTemplateAction.VOTE_TOOL_SIGNATURE, toolContentJSON);
    }

    /**
     * Creates a MCQ activity's JSON details.
     */
    protected JSONObject createVoteActivity(AtomicInteger uiid, int order, Integer[] layoutCoords, Long toolContentID,
	    String contentFolderID, Integer groupingUIID, Integer parentUIID, Integer parentActivityType,
	    String activityTitle) throws JSONException {

	return createToolActivity(uiid, order, layoutCoords, LdTemplateAction.VOTE_TOOL_SIGNATURE,
		LdTemplateAction.VOTE_ICON, toolContentID, contentFolderID, groupingUIID, parentUIID,
		parentActivityType, activityTitle != null ? activityTitle : "Voting", Activity.CATEGORY_RESPONSE);
    }

    /**
     * Helper method to create a wiki tool content.
     */
    protected Long createWikiToolContent(UserDTO user, String title, String instructions,
	    boolean lockWhenFinished, String reflectionInstruction, JSONArray pages)
	    throws JSONException, HttpException, IOException {

	JSONObject toolContentJSON = createStandardToolContent(title, instructions, null, lockWhenFinished, null, null);
	toolContentJSON.put("pages", pages);
	return createToolContent(user, LdTemplateAction.WIKI_TOOL_SIGNATURE, toolContentJSON);
    }

    /**
     * Creates a multiUserMode activity's JSON details.
     */
    protected JSONObject createWikiActivity(AtomicInteger uiid, int order, Integer[] layoutCoords, Long toolContentID,
	    String contentFolderID, Integer groupingUIID, Integer parentUIID, Integer parentActivityType,
	    String activityTitle) throws JSONException {

	return createToolActivity(uiid, order, layoutCoords, LdTemplateAction.WIKI_TOOL_SIGNATURE,
		LdTemplateAction.WIKI_ICON, toolContentID, contentFolderID, groupingUIID, parentUIID,
		parentActivityType, activityTitle != null ? activityTitle : "Wiki", Activity.CATEGORY_COLLABORATION);
    }
    /* ************************************** Service related methods ********************************************** */
    /* ************************************** I18N related methods ************************************************* */

    protected final IAuthoringService getAuthoringService() {
	if (LdTemplateAction.authoringService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    LdTemplateAction.authoringService = (IAuthoringService) ctx.getBean("authoringService");
	}
	return LdTemplateAction.authoringService;
    }

    protected final Tool getTool(String toolSignature) {
	if (LdTemplateAction.toolDAO == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    LdTemplateAction.toolDAO = (IToolDAO) ctx.getBean("toolDAO");
	}
	return LdTemplateAction.toolDAO.getToolBySignature(toolSignature);
    }

    protected final IWorkspaceManagementService getWorkspaceManagementService() {
	if (LdTemplateAction.workspaceManagementService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    LdTemplateAction.workspaceManagementService = (IWorkspaceManagementService) ctx
		    .getBean("workspaceManagementService");
	}
	return LdTemplateAction.workspaceManagementService;
    }

    private ILamsCoreToolService getLamsCoreToolService() {
	if (LdTemplateAction.lamsCoreToolService == null) {
	    LdTemplateAction.lamsCoreToolService = (ILamsCoreToolService) WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext()).getBean("lamsCoreToolService");
	}
	return LdTemplateAction.lamsCoreToolService;
    }

    class ToolDetails {
	String signature;
	String icon;
	int activityType;

	ToolDetails(String signature, String icon, int activityType) {
	    this.signature = signature;
	    this.icon = icon;
	    this.activityType = activityType;
	}

    }

    /********************************* Page Interaction Support *************************************/

    /**
     * Specialised call to create a new question for the Assessment fields. Returns a fragment of HTML
     * which sets up a new CKEditor.
     */
    public ActionForward createAssessment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("questionNumber", WebUtil.readIntParam(request, "questionNumber"));
	return mapping.findForward("assess");
    }

    /**
     * Specialised call to create a new question & options for the surveys tab. Returns a fragment of HTML
     * which sets up a new CKEditor. Works with both mcquestion.jsp & surveyquestion.jsp. The template's
     * struts action determines which jsp is used (see TBL and Inquiry uses).
     */
    public ActionForward createQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("questionNumber", WebUtil.readIntParam(request, "questionNumber"));
	String topicNumber = request.getParameter("topicNumber");
	if (topicNumber != null) {
	    request.setAttribute("topicNumber", topicNumber);
	}
	String forward = request.getParameter("forward");
	return mapping.findForward(forward != null && forward.length() > 0 ? forward : "question");
    }

    /**
     * Specialised call to create a new forum entry. Returns a fragment of HTML
     * which sets up two new fields - subject and body. Works with both forum.jsp.
     */
    public ActionForward createForum(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("topicNumber", request.getParameter("topicNumber"));
	return mapping.findForward("forum");
    }

    /**
     * Specialised call to create a new option for a multiple choice question (mcoption.jsp) or Survey question
     * (surveyoption.jsp) Returns a fragment of HTML which sets up the editing field. The template's
     * struts action determines which jsp is used (see TBL and Inquiry uses).
     */
    public ActionForward createOption(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("questionNumber", WebUtil.readIntParam(request, "questionNumber"));
	request.setAttribute("optionNumber", WebUtil.readIntParam(request, "optionNumber"));
	return mapping.findForward("questionoption");
    }

    public ActionForward deleteOption(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Integer questionNumber = WebUtil.readIntParam(request, "questionNumber", true);
	Integer delete = WebUtil.readIntParam(request, "optionNumber");
	TreeMap<Integer, Option> optionsMap = getOptions(request, questionNumber);
	optionsMap.remove(delete);
	// reorder the displayOrder and setup the return value
	LinkedList<Option> options = new LinkedList<Option>();
	int displayOrder = 1;
	for (Option option : optionsMap.values()) {
	    option.setDisplayOrder(displayOrder++);
	    options.add(option);
	}
	request.setAttribute("questionNumber", questionNumber);
	request.setAttribute("options", options);
	request.setAttribute("optionCount", options.size());
	return mapping.findForward("redooption");
    }

    public ActionForward swapOption(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Integer questionNumber = WebUtil.readIntParam(request, "questionNumber", true);

	// Work out which two to swap and make swap1 the smaller of the two.
	// They should always be consecutive, so we can (the new) swap1 with whichever is the next item.
	int swap1 = WebUtil.readIntParam(request, "optionNumber1");
	int swap2 = WebUtil.readIntParam(request, "optionNumber2");
	if (swap2 < swap1) {
	    swap1 = swap2;
	}

	TreeMap<Integer, Option> optionsMap = getOptions(request, questionNumber);
	// reorder the options and setup the return value
	LinkedList<Option> options = new LinkedList<Option>();
	int displayOrder = 1;

	Option swap = null;
	for (Option option : optionsMap.values()) {
	    if (swap != null) {
		swap.setDisplayOrder(option.getDisplayOrder());
		option.setDisplayOrder(swap1);
		options.add(option);
		options.add(swap);
		swap = null;
	    } else if (option.getDisplayOrder() == swap1) {
		swap = option;
	    } else {
		options.add(option);
	    }
	}
	if (swap != null) {
	    // something wrong - ended up with swap object last!
	    options.add(swap);
	}
	request.setAttribute("questionNumber", questionNumber);
	request.setAttribute("options", options);
	request.setAttribute("optionCount", options.size());
	return mapping.findForward("redooption");
    }

    private TreeMap<Integer, Option> getOptions(HttpServletRequest request, Integer questionNumber) {

	// correctDisplayIdInteger is used for MCQ but not Survey - the value will be ignored by the
	// survey jsp page.
	Integer correctDisplayIdInteger = WebUtil.readIntParam(request, "question" + questionNumber + "correct", true);
	int correctDisplayId = correctDisplayIdInteger != null ? correctDisplayIdInteger.intValue() : 0;

	TreeMap<Integer, Option> optionDtos = new TreeMap<Integer, Option>();

	for (int i = 1; i <= MAX_OPTION_COUNT; i++) {
	    String optionText = request.getParameter("question" + questionNumber + "option" + i);
	    if (optionText != null) {
		// Grade is used for assessment
		String grade = request.getParameter("question" + questionNumber + "option" + i + "grade");
		Option option = new Option(i, i == correctDisplayId, optionText, grade);
		optionDtos.put(new Integer(i), option);
	    }
	}

	return optionDtos;
    }

    /**
     * Specialised call to create a new URL field & title.
     */
    public ActionForward createResource(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("urlNumber", request.getParameter("urlNumber"));
	if (request.getParameter("branchNumberUnderscore") != null) {
	    request.setAttribute("branchNumberUnderscore", request.getParameter("branchNumberUnderscore"));
	}
	return mapping.findForward("resource");
    }

    /**
     * Specialised call to create a new set of fields for a branch.
     */
    public ActionForward createBranch(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("branchNumber", request.getParameter("branchNumber"));
	return mapping.findForward("branch");
    }

}