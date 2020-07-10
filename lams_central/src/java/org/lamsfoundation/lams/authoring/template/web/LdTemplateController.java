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
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.authoring.service.IAuthoringFullService;
import org.lamsfoundation.lams.authoring.template.AssessMCAnswer;
import org.lamsfoundation.lams.authoring.template.Assessment;
import org.lamsfoundation.lams.authoring.template.Option;
import org.lamsfoundation.lams.authoring.template.TextUtil;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.GateActivity;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.exception.LearningDesignException;
import org.lamsfoundation.lams.qb.model.QbOption;
import org.lamsfoundation.lams.qb.model.QbQuestion;
import org.lamsfoundation.lams.qb.service.IQbService;
import org.lamsfoundation.lams.questions.Answer;
import org.lamsfoundation.lams.questions.Question;
import org.lamsfoundation.lams.questions.QuestionParser;
import org.lamsfoundation.lams.rest.RestTags;
import org.lamsfoundation.lams.rest.ToolRestManager;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.dao.IToolDAO;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.service.ILamsCoreToolService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.AuthoringJsonTags;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.JsonUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Base class for actions processing Learning Design templates.
 *
 * A little history: This code was written when we were still using Struts but we were phasing it out. So it was written
 * with the
 * minimal of Struts code, which was then swapped to being the minimal Spring MVC. Now we are using Spring MVC it would
 * be nice to convert to using more Spring MVC features rather than adding parts piecemeal as the template pages
 * become more complicated. Rather than relying on the web page to keep all the data and doing ajax updates, it would be
 * nice to have a session form or the like backing the page. It would make it easier to have the templates load an
 * existing
 * design from the database for modification and make the processing when the template is saved so it doesn't do one
 * huge
 * parse in one go.
 *
 * Would also be nice to stop hardcoding the icons!
 *
 * @author Marcin Cieslak, Fiona Malikoff
 */
@Controller
public abstract class LdTemplateController {

    @Autowired
    WebApplicationContext applictionContext;

    // Used to append the number to the group label - format as 2 digits so it sorts better.
    NumberFormat groupNumberFormatter = new DecimalFormat("00");

    private static Logger log = Logger.getLogger(LdTemplateController.class);
    public static final int MAX_OPTION_COUNT = 6;
    public static final int MAX_FLOATING_ACTIVITY_OPTIONS = 6; // Hardcoded in the Flash client

    public static final String PARENT_ACTIVITY_TYPE = "parentActivityType"; // used to work out transitions - not used by the authoring module

    @Autowired
    protected ILamsCoreToolService lamsCoreToolService;
    @Autowired
    protected IWorkspaceManagementService workspaceManagementService;
    @Autowired
    protected IAuthoringFullService authoringService;
    @Autowired
    protected IQbService qbService;
    @Autowired
    protected IToolDAO toolDAO;

    protected static final String CONTENT_TYPE_JSON = "application/json;charset=utf-8";

    // icon strings found in the lams_learningdesign_activity table
    protected static final String ASSESSMENT_TOOL_SIGNATURE = "laasse10";
    protected static final String ASSESSMENT_ICON = "tool/laasse10/images/icon_assessment.swf";
    protected static final String ASSESSMENT_TOOL_OUTPUT_DEFINITION = "learner.total.score";
    protected static final String CHAT_TOOL_SIGNATURE = "lachat11";
    protected static final String CHAT_ICON = "tool/lachat11/images/icon_chat.swf";
    protected static final String FORUM_TOOL_SIGNATURE = "lafrum11";
    protected static final String FORUM_ICON = "tool/lafrum11/images/icon_forum.swf";
    protected static final String LEADER_TOOL_SIGNATURE = "lalead11";
    protected static final String LEADER_ICON = "tool/lalead11/images/icon_leaderselection.swf";
    protected static final String MCQ_TOOL_SIGNATURE = "lamc11";
    protected static final String MCQ_ICON = "tool/lamc11/images/icon_mcq.swf";
    protected static final String MCQ_TOOL_OUTPUT_DEFINITION = "learner.mark";
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
    protected static final String SCRATCHIE_TOOL_OUTPUT_DEFINITION = "learner.mark";
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
    protected static final String PEER_REVIEW_TOOL_SIGNATURE = "laprev11";
    protected static final String PEER_REVIEW_ICON = "tool/laprev11/images/icon_peerreview.svg";

    protected static final String CHAT_SCRIBE_DESC = "Combined Chat and Scribe";
    protected static final String FORUM_SCRIBE_DESC = "Combined Forum and Scribe";
    protected static final String RESOURCES_FORUM_DESC = "Combined Share Resources and Forum";

    protected static final String TOOL_CONTENT_SERVLET_URL_SUFFIX = "/lams/rest/ToolContent";
    protected static final String LEARNING_DESIGN_SERVLET_URL_SUFFIX = "/lams/rest/LearningDesign";

    @RequestMapping("")
    @ResponseBody
    public final String unspecified(HttpServletRequest request, HttpServletResponse response, HttpSession httpSession)
	    throws Exception {
	ObjectNode responseJSON = null;
	try {
	    responseJSON = createLearningDesign(request, httpSession);

	    if (!responseJSON.has("learningDesignID") && !responseJSON.has("errors")) {
		log.error(
			"The Learning Design was not created successfully. ResponseJSON missing both learningDesignID and errors"
				+ responseJSON.toString());
		responseJSON = JsonNodeFactory.instance.objectNode();
		responseJSON.put("fatal",
			"The Learning Design was not created successfully. See the server log for more details.");
	    }
	} catch (Exception e) {
	    log.error(e.getMessage(), e);
	    responseJSON = JsonNodeFactory.instance.objectNode();
	    responseJSON.put("fatal",
		    "The Learning Design was not created successfully. See the server log for more details.\n\n"
			    + e.getMessage());
	}

	response.setContentType("application/json;charset=utf-8");
	return responseJSON.toString();
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
    @RequestMapping("/init")
    public String init(HttpServletRequest request) throws Exception {
	String contentFolderID = FileUtil.generateUniqueContentFolderID();
	request.setAttribute(RestTags.CONTENT_FOLDER_ID, contentFolderID);
	return "authoring/template/tbl/tbl";
    }

    public String init(HttpServletRequest request, String forward) throws Exception {
	String contentFolderID = FileUtil.generateUniqueContentFolderID();
	request.setAttribute(RestTags.CONTENT_FOLDER_ID, contentFolderID);
	return forward;
    }

    protected abstract ObjectNode createLearningDesign(HttpServletRequest request, HttpSession httpSession)
	    throws Exception;

    /**
     * Creates transitions between activities in the order they were created.
     * Simple version used if no sequence activities are used.
     */
    protected ArrayNode createTransitions(AtomicInteger maxUIID, ArrayNode activities) {
	ArrayNode transitions = JsonNodeFactory.instance.arrayNode();
	return createTransitions(transitions, maxUIID, activities);
    }

    /**
     * Creates transitions between activities in the order they were created.
     * Complex version used if sequence activities are used - then the top level activities go in one set and each
     * sequence
     * activity is its own set of activities.
     */
    protected ArrayNode createTransitions(AtomicInteger maxUIID, Set<ArrayNode> setsOfActivities) {
	ArrayNode transitions = JsonNodeFactory.instance.arrayNode();
	if (setsOfActivities != null) {
	    for (ArrayNode activities : setsOfActivities) {
		createTransitions(transitions, maxUIID, activities);
	    }
	}
	return transitions;
    }

    /**
     * Processes the transitions between activities within a sequence. This could be the overall
     * sequence or activities within a SequenceActivity. The transitions parameter must not be null.
     */
    private ArrayNode createTransitions(ArrayNode transitions, AtomicInteger maxUIID, ArrayNode activities) {

	for (int activityIndex = 1; activityIndex < activities.size(); activityIndex++) {
	    // If it has parent activity that is a parallel activity or support activity then skip to the next one.
	    // If the parent activity is a sequence activity then we do want transitions
	    ObjectNode fromActivity = (ObjectNode) activities.get(activityIndex - 1);
	    ObjectNode toActivity = (ObjectNode) activities.get(activityIndex);
	    Integer parentType = JsonUtil.optInt(toActivity, PARENT_ACTIVITY_TYPE, (Integer) null);
	    while ((activityIndex < activities.size()) && parentType != null
		    && !parentType.equals(Activity.SEQUENCE_ACTIVITY_TYPE)) {
		activityIndex++;
		toActivity = (ObjectNode) activities.get(activityIndex);
		parentType = JsonUtil.optInt(toActivity, PARENT_ACTIVITY_TYPE, (Integer) null);
	    }
	    int fromUIID = JsonUtil.optInt(fromActivity, AuthoringJsonTags.ACTIVITY_UIID);
	    int toUIID = JsonUtil.optInt(toActivity, AuthoringJsonTags.ACTIVITY_UIID);

	    ObjectNode transitionJSON = JsonNodeFactory.instance.objectNode();
	    transitionJSON.put(AuthoringJsonTags.TRANSITION_UIID, maxUIID.incrementAndGet());
	    transitionJSON.put(AuthoringJsonTags.FROM_ACTIVITY_UIID, fromUIID);
	    transitionJSON.put(AuthoringJsonTags.TO_ACTIVITY_UIID, toUIID);
	    transitionJSON.put(AuthoringJsonTags.TRANSITION_TYPE, 0);

	    transitions.add(transitionJSON);

	    // don't need the parent type any more so remove it so authoring won't get it!
	    fromActivity.remove(PARENT_ACTIVITY_TYPE);
	}

	return transitions;
    }

    protected static final int rowHeightSpace = 100;
    protected static final int activityWidthSpace = 185;
    protected static final int gateHeightOffset = 5;
    protected static final int gateWidthOffset = 50;

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

    /** Work out the offset for a gate icon - different size to an ordinary icons */
    protected Integer[] calcGateOffset(Integer[] currPos) {
	Integer[] newPos = new Integer[2];
	newPos[1] = currPos[1] + gateHeightOffset;
	newPos[0] = currPos[0] + gateWidthOffset;
	return newPos;
    }

    /**
     * Create a title for this learning design, within the right length for the database. The userEnteredString is
     * capitalised and whitespace is removed. The call to saveLearningDesign will make it unique by appending a date
     * if needed.
     *
     * @param sequenceTitle
     * @param workspaceFolderID
     * @return
     */
    private String createTitle(String templateCode, String userEnteredString, Integer workspaceFolderID) {
	String title = WebUtil.removeHTMLtags(userEnteredString);
	title = title.replaceAll("[@%<>/^/*/$]", "");
	if (title.length() > 220) {
	    title.substring(0, 220);
	}
	return title;
    }

    /**
     * Setup Learning Design JSON Data
     *
     * @throws IOException
     * @
     * @throws
     *       HttpException
     */
    protected ObjectNode saveLearningDesign(String templateCode, String userEnteredTitleString,
	    String userEnteredDescription, Integer workspaceFolderID, String contentFolderId, Integer maxUIID,
	    ArrayNode activities, ArrayNode transitions, ArrayNode groupings, ArrayNode branchMappings)
	    throws IOException {

	// fill in required LD data
	ObjectNode ldJSON = JsonNodeFactory.instance.objectNode();
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
	ldJSON.set(AuthoringJsonTags.ACTIVITIES, activities);
	ldJSON.set(AuthoringJsonTags.TRANSITIONS, transitions);
	ldJSON.set(AuthoringJsonTags.GROUPINGS, groupings);
	ldJSON.set(AuthoringJsonTags.BRANCH_MAPPINGS, branchMappings);

	LearningDesign learningDesign = null;
	try {
	    learningDesign = authoringService.saveLearningDesignDetails(ldJSON);
	} catch (Exception e) {
	    log.error("Unable to save learning design with details " + ldJSON, e);
	    throw new LearningDesignException("Unable to save learning design with details " + ldJSON);
	}

	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	responseJSON.put("learningDesignID", learningDesign.getLearningDesignId());
	responseJSON.put("title", learningDesign.getTitle());
	return responseJSON;
    }

    /* ************************************** Non-Tool Activity methods ******************************************** */
    protected ObjectNode createGateActivity(AtomicInteger uiid, int order, Integer[] layoutCoords, String activityTitle,
	    String activityDescription) {

	ObjectNode activityJSON = JsonNodeFactory.instance.objectNode();
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
	activityJSON.put(AuthoringJsonTags.ACTIVITY_TITLE, activityTitle != null ? activityTitle : "Gate");
	if (activityDescription != null) {
	    activityJSON.put(AuthoringJsonTags.DESCRIPTION, activityDescription);
	}
	activityJSON.put(AuthoringJsonTags.ACTIVITY_CATEGORY_ID, Activity.CATEGORY_SYSTEM);
	activityJSON.put(AuthoringJsonTags.ACTIVITY_TYPE_ID, Activity.PERMISSION_GATE_ACTIVITY_TYPE);
	activityJSON.put(AuthoringJsonTags.GATE_ACTIVITY_LEVEL_ID, GateActivity.LEARNER_GATE_LEVEL);

	return activityJSON;
    }

    protected ObjectNode createScheduledGateActivity(AtomicInteger uiid, int order, Integer[] layoutCoords,
	    String activityTitle, String activityDescription, Long startOffset) {

	ObjectNode activityJSON = createGateActivity(uiid, order, layoutCoords, activityTitle, activityDescription);
	activityJSON.put(AuthoringJsonTags.ACTIVITY_TYPE_ID, Activity.SCHEDULE_GATE_ACTIVITY_TYPE);
	activityJSON.put(AuthoringJsonTags.GATE_START_OFFSET, startOffset);

	return activityJSON;
    }

    /** Create a group activity's JSON objects */
    protected ObjectNode[] createGroupingActivity(AtomicInteger uiid, int order, Integer[] layoutCoords,
	    Integer groupingTypeID, Integer numLearners, Integer numGroups, String title, String[] groupNames,
	    ResourceBundle appBundle, MessageFormat formatter) {
	ObjectNode[] responseJSONs = new ObjectNode[2];

	ObjectNode groupingJSON = JsonNodeFactory.instance.objectNode();
	responseJSONs[1] = groupingJSON;
	int groupingUIID = uiid.incrementAndGet();
	groupingJSON.put(AuthoringJsonTags.GROUPING_UIID, groupingUIID);
	groupingJSON.put(AuthoringJsonTags.GROUPING_TYPE_ID, groupingTypeID);
	if (groupingTypeID.equals(Grouping.CHOSEN_GROUPING_TYPE)) {
	    groupingJSON.put(AuthoringJsonTags.MAX_NUMBER_OF_GROUPS, numGroups);
	} else {
	    groupingJSON.put(AuthoringJsonTags.NUMBER_OF_GROUPS, numGroups);
	}
	groupingJSON.put(AuthoringJsonTags.LEARNERS_PER_GROUP, numLearners);
	groupingJSON.put(AuthoringJsonTags.EQUAL_NUMBER_OF_LEARNERS_PER_GROUP, Boolean.FALSE);

	// mimic what Authoring is doing for the group name creations
	ArrayNode groups = JsonNodeFactory.instance.arrayNode();
	if (groupNames != null) {
	    int orderId = 0;
	    for (String groupName : groupNames) {
		ObjectNode group = JsonNodeFactory.instance.objectNode();
		group.put(AuthoringJsonTags.GROUP_NAME, groupName);
		group.put(AuthoringJsonTags.ORDER_ID, orderId++);
		group.put(AuthoringJsonTags.GROUP_UIID, uiid.incrementAndGet());
		groups.add(group);
	    }
	} else {
	    Integer useNumGroups = (numGroups != null && numGroups > 0) ? numGroups : 2;
	    for (int orderId = 0, groupNum = 1; orderId < useNumGroups; orderId++, groupNum++) {
		ObjectNode group = JsonNodeFactory.instance.objectNode();
		String groupName = TextUtil.getText(appBundle, "authoring.fla.default.group.prefix") + " "
			+ groupNumberFormatter.format(groupNum);

		group.put(AuthoringJsonTags.GROUP_NAME, groupName);
		group.put(AuthoringJsonTags.ORDER_ID, orderId);
		group.put(AuthoringJsonTags.GROUP_UIID, uiid.incrementAndGet());
		groups.add(group);
	    }
	}
	groupingJSON.set(AuthoringJsonTags.GROUPS, groups);

	Integer[] pos = layoutCoords != null ? layoutCoords : calcPosition(order);

	ObjectNode activityJSON = JsonNodeFactory.instance.objectNode();
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
     * @
     */
    protected ObjectNode createParallelActivity(AtomicInteger uiid, int order, Integer[] layoutCoords,
	    Integer groupingUIID, String activityTitle, String description) {
	ObjectNode activityJSON = JsonNodeFactory.instance.objectNode();

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
    protected ObjectNode createSupportActivity(AtomicInteger uiid, int order, Integer[] layoutCoords) {
	Integer[] pos = layoutCoords;
	if (pos == null) {
	    pos = calcPosition(order);
	    pos[0] = 20;
	    pos[1] = pos[1] + rowHeightSpace; // put it on its own line down the bottom
	}

	ObjectNode activityJSON = JsonNodeFactory.instance.objectNode();
	activityJSON.put(AuthoringJsonTags.ACTIVITY_UIID, uiid.incrementAndGet());
	activityJSON.put(AuthoringJsonTags.GROUPING_SUPPORT_TYPE, Activity.GROUPING_SUPPORT_NONE);
	activityJSON.put(AuthoringJsonTags.APPLY_GROUPING, false);
	activityJSON.put(AuthoringJsonTags.XCOORD, pos[0]);
	activityJSON.put(AuthoringJsonTags.YCOORD, pos[1]);
	activityJSON.put(AuthoringJsonTags.ACTIVITY_TITLE, "Support Activity");
	activityJSON.put(AuthoringJsonTags.ACTIVITY_CATEGORY_ID, Activity.CATEGORY_SYSTEM);
	activityJSON.put(AuthoringJsonTags.ACTIVITY_TYPE_ID, Activity.FLOATING_ACTIVITY_TYPE);
	activityJSON.put(AuthoringJsonTags.MAX_ACTIVITIES, MAX_FLOATING_ACTIVITY_OPTIONS);
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
    protected ObjectNode createSequenceActivity(Integer reservedUiid, Integer parentUIID, Integer parentActivityType,
	    Integer firstActivityUiid, int orderId, String branchName) {
	ObjectNode activityJSON = JsonNodeFactory.instance.objectNode();
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
     * @
     */
    protected ObjectNode createBranchMapping(AtomicInteger uiid, Integer groupUiid, Integer branchingActivityUiid,
	    Integer sequenceActivityUiid) {
	ObjectNode bmJSON = JsonNodeFactory.instance.objectNode();
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
    protected ObjectNode createGroupBranchingActivity(Integer reservedUiid, Integer defaultBranchUiid,
	    Integer groupingUiid, int order, Integer[] layoutCoords, Integer[] startCoords, Integer[] endCoords,
	    String activityTitle) {
	ObjectNode activityJSON = createBranchingActivity(reservedUiid, defaultBranchUiid, groupingUiid, order,
		layoutCoords, startCoords, endCoords, activityTitle, Activity.GROUP_BRANCHING_ACTIVITY_TYPE);
	return activityJSON;
    }

    /**
     * Create the overall branching activity, which will have branch activities as its children.
     * TODO Branch selected by monitor. Not yet implemented as the branch mappings are yet to be investigated.
     */
//    protected ObjectNode createChosenBranchingActivity(AtomicInteger uiid, int order, Integer[] layoutCoords)  {
//	return createBranchingActivity(uiid, order, layoutCoords, Activity.CHOSEN_BRANCHING_ACTIVITY_TYPE);
//    }

    /**
     * Create the overall branching activity, which will have branch activities as its children.
     * Supports instructor's choice and grouped, but not based on learner output as conditions
     * are not implemented in the REST authoring for the tools
     */
    private ObjectNode createBranchingActivity(Integer reservedUiid, Integer defaultBranchUiid, Integer groupingUiid,
	    int order, Integer[] layoutCoords, Integer[] startCoords, Integer[] endCoords, String activityTitle,
	    int activityType) {

	ObjectNode activityJSON = JsonNodeFactory.instance.objectNode();
	activityJSON.put(AuthoringJsonTags.ACTIVITY_UIID, reservedUiid);
	activityJSON.put(AuthoringJsonTags.GROUPING_SUPPORT_TYPE, Activity.GROUPING_SUPPORT_OPTIONAL);
	activityJSON.put(AuthoringJsonTags.ACTIVITY_TITLE, activityTitle != null ? activityTitle : "Branching");
	activityJSON.put(AuthoringJsonTags.ACTIVITY_CATEGORY_ID, Activity.CATEGORY_SYSTEM);
	activityJSON.put(AuthoringJsonTags.ACTIVITY_TYPE_ID, activityType);
	activityJSON.put(AuthoringJsonTags.MAX_ACTIVITIES, MAX_FLOATING_ACTIVITY_OPTIONS);
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
    protected Long createToolContent(UserDTO user, String toolSignature, ObjectNode toolContentJSON)
	    throws IOException {
	try {
	    Tool tool = getTool(toolSignature);
	    Long toolContentID = authoringService.insertToolContentID(tool.getToolId());

	    // Tools' services implement an interface for processing REST requests
	    ToolRestManager toolRestService = (ToolRestManager) lamsCoreToolService.findToolService(tool);
	    toolRestService.createRestToolContent(user.getUserID(), toolContentID, toolContentJSON);

	    return toolContentID;
	} catch (Exception e) {
	    log.error("Unable to create tool content for " + toolSignature + " with details " + toolContentJSON
		    + ". \nThe tool probably threw an exception - check the server logs for more details.\n"
		    + "If the exception is \"Servlet.service() for servlet ToolContentRestServlet threw exception java.lang.ClassCastException: com.sun.proxy.$ProxyXXX cannot be cast to org.lamsfoundation.lams.rest.ToolRestManager)\""
		    + " then the tool doesn't support the LDTemplate service calls (ie has not implemented the ToolRestManager interface / createRestToolContent() method.");
	    throw new ToolException(
		    "Unable to create tool content for " + toolSignature + " with details " + toolContentJSON);
	}
    }

    /**
     * General method to create tool activity. All calls to create an activity relating to a tool should go through this
     * method
     */
    protected ObjectNode createToolActivity(AtomicInteger uiid, int order, Integer[] layoutCoords, String toolSignature,
	    String toolIcon, Long toolContentID, String contentFolderID, Integer groupingUIID, Integer parentUIID,
	    Integer parentActivityType, String activityTitle, int activityCategory) {
	return createToolActivity(uiid, order, layoutCoords, toolSignature, toolIcon, toolContentID, contentFolderID,
		groupingUIID, parentUIID, parentActivityType, activityTitle, activityCategory, null);
    }

    protected ObjectNode createToolActivity(AtomicInteger uiid, int order, Integer[] layoutCoords, String toolSignature,
	    String toolIcon, Long toolContentID, String contentFolderID, Integer groupingUIID, Integer parentUIID,
	    Integer parentActivityType, String activityTitle, int activityCategory, String toolOutputDefinition) {
	ObjectNode activityJSON = JsonNodeFactory.instance.objectNode();
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
	if (toolOutputDefinition != null) {
	    activityJSON.put(AuthoringJsonTags.TOOL_OUTPUT_DEFINITION, toolOutputDefinition);
	}
	return activityJSON;
    }

    /** Sets up the standard fields that are used by many tools! */
    protected ObjectNode createStandardToolContent(String title, String instructions, String reflectionInstructions,
	    Boolean lockWhenFinished, Boolean allowRichTextEditor, UserDTO user) {
	ObjectNode toolContentJSON = JsonNodeFactory.instance.objectNode();
	toolContentJSON.put(RestTags.TITLE, title != null ? title : "");
	toolContentJSON.put(RestTags.INSTRUCTIONS, instructions != null ? instructions : "");

	if (reflectionInstructions != null) {
	    toolContentJSON.put(RestTags.REFLECT_ON_ACTIVITY, true);
	    toolContentJSON.put(RestTags.REFLECT_INSTRUCTIONS, reflectionInstructions);
	}

	toolContentJSON.put(RestTags.LOCK_WHEN_FINISHED, lockWhenFinished);
	toolContentJSON.put(RestTags.ALLOW_RICH_TEXT_EDITOR, allowRichTextEditor);

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
     * The questions entry should be ArrayNode containing JSON objects, which in turn must contain
     * "questionTitle", "questionText", "displayOrder" (Integer), "type" (Integer). If the type is Multiple Choice,
     * Numerical or Matching Pairs
     * then a ArrayNode "answers" is required.
     *
     * The answers entry should be ArrayNode
     * containing JSON objects, which in turn must contain "answerText" or "answerFloat", "displayOrder" (Integer),
     * "grade" (Integer).
     *
     * For the templates, all the questions that are created will be set up as references, therefore the questions in
     * the assessment == the bank of questions.
     * So references entry will be a ArrayNode containing JSON objects, which in turn must contain "displayOrder"
     * (Integer),
     * "questionDisplayOrder" (Integer - to match to the question). If default grade or random questions are needed then
     * this method needs
     * to be expanded.
     */
    protected Long createAssessmentToolContent(UserDTO user, String title, String instructions,
	    String reflectionInstructions, boolean selectLeaderToolOutput, boolean enableNumbering,
	    boolean enableConfidenceLevels, boolean allowDiscloseAnswers, ArrayNode questions) throws IOException {

	ObjectNode toolContentJSON = createStandardToolContent(title, instructions, reflectionInstructions, null, null,
		user);
	toolContentJSON.put(RestTags.USE_SELECT_LEADER_TOOL_OUTPUT, selectLeaderToolOutput);
	toolContentJSON.put(RestTags.ENABLE_CONFIDENCE_LEVELS, enableConfidenceLevels);
	toolContentJSON.put("numbered", enableNumbering);
	toolContentJSON.put("displaySummary", Boolean.TRUE);
	toolContentJSON.put("allowDiscloseAnswers", allowDiscloseAnswers);
	toolContentJSON.set(RestTags.QUESTIONS, questions);

	ArrayNode references = JsonNodeFactory.instance.arrayNode();
	for (int i = 0; i < questions.size(); i++) {
	    ObjectNode question = (ObjectNode) questions.get(i);
	    Integer questionDisplayOrder = question.get(RestTags.DISPLAY_ORDER).asInt();
	    Integer defaultGrade = JsonUtil.optInt(question, "defaultGrade", 1);
	    references.add(JsonNodeFactory.instance.objectNode().put(RestTags.DISPLAY_ORDER, questionDisplayOrder)
		    .put("questionDisplayOrder", questionDisplayOrder).put("defaultGrade", defaultGrade));
	}
	toolContentJSON.set("references", references);

	return createToolContent(user, LdTemplateController.ASSESSMENT_TOOL_SIGNATURE, toolContentJSON);
    }

    /**
     * Creates a forum activity's JSON details.
     */
    protected ObjectNode createAssessmentActivity(AtomicInteger uiid, int order, Integer[] layoutCoords,
	    Long toolContentID, String contentFolderID, Integer groupingUIID, Integer parentUIID,
	    Integer parentActivityType, String activityTitle) {

	return createToolActivity(uiid, order, layoutCoords, LdTemplateController.ASSESSMENT_TOOL_SIGNATURE,
		LdTemplateController.ASSESSMENT_ICON, toolContentID, contentFolderID, groupingUIID, parentUIID,
		parentActivityType, activityTitle != null ? activityTitle : "Assessment", Activity.CATEGORY_ASSESSMENT,
		LdTemplateController.ASSESSMENT_TOOL_OUTPUT_DEFINITION);
    }

    /**
     * Helper method to create a chat tool content. Only title and instructions are needed but we support reflection,
     * lock on
     * finished and filterKeywords in case it is wanted. The keywords should be a comma deliminated string.
     */
    protected Long createChatToolContent(UserDTO user, String title, String instructions, boolean lockWhenFinished,
	    String filterKeywords, String reflectionInstructions) throws IOException {

	ObjectNode toolContentJSON = createStandardToolContent(title, instructions, reflectionInstructions,
		lockWhenFinished, null, null);
	toolContentJSON.put("filterKeywords", filterKeywords);
	return createToolContent(user, LdTemplateController.CHAT_TOOL_SIGNATURE, toolContentJSON);
    }

    /**
     * Creates a noticeboard activity's JSON details.
     */
    protected ObjectNode createChatActivity(AtomicInteger uiid, int order, Integer[] layoutCoords, Long toolContentID,
	    String contentFolderID, Integer groupingUIID, Integer parentUIID, Integer parentActivityType,
	    String activityTitle) {

	return createToolActivity(uiid, order, layoutCoords, LdTemplateController.CHAT_TOOL_SIGNATURE,
		LdTemplateController.CHAT_ICON, toolContentID, contentFolderID, groupingUIID, parentUIID,
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
    protected Long createForumToolContent(UserDTO user, String title, String instructions, boolean lockWhenFinished,
	    boolean allowRichTextEditor, boolean allowNewTopic, boolean allowRateMessages, boolean allowUpload,
	    boolean limitedMaxCharacters, Integer maxCharacters, ArrayNode topics) throws IOException {

	ObjectNode toolContentJSON = createStandardToolContent(title, instructions, null, lockWhenFinished,
		allowRichTextEditor, user);
	toolContentJSON.set("topics", topics);
	toolContentJSON.put("allowNewTopic", allowNewTopic);
	toolContentJSON.put("allowRateMessages", allowRateMessages);
	toolContentJSON.put("allowUpload", allowUpload);
	toolContentJSON.put("limitedMaxCharacters", limitedMaxCharacters);
	if (limitedMaxCharacters && maxCharacters != null) {
	    toolContentJSON.put("maxCharacters", maxCharacters);
	}
	return createToolContent(user, LdTemplateController.FORUM_TOOL_SIGNATURE, toolContentJSON);
    }

    /**
     * Creates a forum activity's JSON details.
     */
    protected ObjectNode createForumActivity(AtomicInteger uiid, int order, Integer[] layoutCoords, Long toolContentID,
	    String contentFolderID, Integer groupingUIID, Integer parentUIID, Integer parentActivityType,
	    String activityTitle) {

	return createToolActivity(uiid, order, layoutCoords, LdTemplateController.FORUM_TOOL_SIGNATURE,
		LdTemplateController.FORUM_ICON, toolContentID, contentFolderID, groupingUIID, parentUIID,
		parentActivityType, activityTitle != null ? activityTitle : "Forum", Activity.CATEGORY_COLLABORATION);
    }

    /**
     * Helper method to create a leader selection tool content. Only title and instructions are needed. Leader selection
     * does not support lockWhenFinished.
     */
    protected Long createLeaderSelectionToolContent(UserDTO user, String title, String instructions)
	    throws IOException {
	ObjectNode toolContentJSON = createStandardToolContent(title, instructions, null, null, null, null);
	return createToolContent(user, LdTemplateController.LEADER_TOOL_SIGNATURE, toolContentJSON);
    }

    /**
     * Creates a leader selection activity's JSON details.
     */
    protected ObjectNode createLeaderSelectionActivity(AtomicInteger uiid, int order, Integer[] layoutCoords,
	    Long toolContentID, String contentFolderID, Integer groupingUIID, Integer parentUIID,
	    Integer parentActivityType, String activityTitle) {

	return createToolActivity(uiid, order, layoutCoords, LdTemplateController.LEADER_TOOL_SIGNATURE,
		LdTemplateController.LEADER_ICON, toolContentID, contentFolderID, groupingUIID, parentUIID,
		parentActivityType, activityTitle != null ? activityTitle : "Leader Selection",
		Activity.CATEGORY_RESPONSE);
    }

    /**
     * Helper method to create a notebook tool content. Only title and instructions are needed but we support lock on
     * finished and allow rich text editor in case it is wanted.
     */
    protected Long createNotebookToolContent(UserDTO user, String title, String instructions, boolean lockWhenFinished,
	    boolean allowRichTextEditor) throws IOException {

	ObjectNode toolContentJSON = createStandardToolContent(title, instructions, null, lockWhenFinished,
		allowRichTextEditor, null);
	return createToolContent(user, LdTemplateController.NOTEBOOK_TOOL_SIGNATURE, toolContentJSON);
    }

    /**
     * Creates a noticeboard activity's JSON details.
     */
    protected ObjectNode createNotebookActivity(AtomicInteger uiid, int order, Integer[] layoutCoords,
	    Long toolContentID, String contentFolderID, Integer groupingUIID, Integer parentUIID,
	    Integer parentActivityType, String activityTitle) {

	return createToolActivity(uiid, order, layoutCoords, LdTemplateController.NOTEBOOK_TOOL_SIGNATURE,
		LdTemplateController.NOTEBOOK_ICON, toolContentID, contentFolderID, groupingUIID, parentUIID,
		parentActivityType, activityTitle != null ? activityTitle : "Notebook", Activity.CATEGORY_RESPONSE);
    }

    /**
     * Helper method to create a noticeboard tool content. Only title and content are needed for noticeboard! But we
     * support a reflection in case it is wanted.
     */
    protected Long createNoticeboardToolContent(UserDTO user, String title, String content,
	    String reflectionInstructions) throws IOException {

	ObjectNode toolContentJSON = createStandardToolContent(title, null, reflectionInstructions, null, null, null);
	toolContentJSON.put("content", content != null ? content : "");
	return createToolContent(user, LdTemplateController.NOTICEBOARD_TOOL_SIGNATURE, toolContentJSON);
    }

    /**
     * Creates a noticeboard activity's JSON details.
     */
    protected ObjectNode createNoticeboardActivity(AtomicInteger uiid, int order, Integer[] layoutCoords,
	    Long toolContentID, String contentFolderID, Integer groupingUIID, Integer parentUIID,
	    Integer parentActivityType, String activityTitle) {

	return createToolActivity(uiid, order, layoutCoords, LdTemplateController.NOTICEBOARD_TOOL_SIGNATURE,
		LdTemplateController.NOTICEBOARD_ICON, toolContentID, contentFolderID, groupingUIID, parentUIID,
		parentActivityType, activityTitle != null ? activityTitle : "Noticeboard", Activity.CATEGORY_CONTENT);
    }

    /**
     * Helper method to create a Q&A tool content. Only title and instructions are needed but we support other fields in
     * case they are wanted.
     */
    protected Long createQAToolContent(UserDTO user, String title, String instructions, boolean lockWhenFinished,
	    boolean allowRichTextEditor, boolean oneQuestionPerPage, boolean showOtherLearnersAnswers,
	    boolean showOtherLearnersNames, ArrayNode questions) throws IOException {

	ObjectNode toolContentJSON = createStandardToolContent(title, instructions, null, lockWhenFinished,
		allowRichTextEditor, null);
	toolContentJSON.set(RestTags.QUESTIONS, questions);
	toolContentJSON.put("questionsSequenced", oneQuestionPerPage);
	toolContentJSON.put("showOtherAnswers", showOtherLearnersAnswers);
	toolContentJSON.put("usernameVisible", showOtherLearnersNames);
	return createToolContent(user, LdTemplateController.QA_TOOL_SIGNATURE, toolContentJSON);
    }

    /**
     * Creates a Q&A activity's JSON details.
     */
    protected ObjectNode createQAActivity(AtomicInteger uiid, int order, Integer[] layoutCoords, Long toolContentID,
	    String contentFolderID, Integer groupingUIID, Integer parentUIID, Integer parentActivityType,
	    String activityTitle) {

	return createToolActivity(uiid, order, layoutCoords, LdTemplateController.QA_TOOL_SIGNATURE,
		LdTemplateController.QA_ICON, toolContentID, contentFolderID, groupingUIID, parentUIID,
		parentActivityType, activityTitle != null ? activityTitle : "Q&A", Activity.CATEGORY_RESPONSE);
    }

    /**
     * Helper method to create a MCQ tool content. Title, instructions and questions are required (see tool for full
     * details of questions). Other fields are optional.
     */
    protected Long createMCQToolContent(UserDTO user, String title, String instructions,
	    boolean useSelectLeaderToolOuput, boolean enableConfidenceLevel, boolean prefixAnswersWithLetters,
	    ArrayNode questions) throws IOException {

	ObjectNode toolContentJSON = createStandardToolContent(title, instructions, null, null, null, null);
	toolContentJSON.put(RestTags.USE_SELECT_LEADER_TOOL_OUTPUT, useSelectLeaderToolOuput);
	toolContentJSON.set(RestTags.QUESTIONS, questions);
	toolContentJSON.put(RestTags.ENABLE_CONFIDENCE_LEVELS, enableConfidenceLevel);
	toolContentJSON.put("prefixAnswersWithLetters", prefixAnswersWithLetters);
	return createToolContent(user, LdTemplateController.MCQ_TOOL_SIGNATURE, toolContentJSON);
    }

    /**
     * Creates a MCQ activity's JSON details.
     */
    protected ObjectNode createMCQActivity(AtomicInteger uiid, int order, Integer[] layoutCoords, Long toolContentID,
	    String contentFolderID, Integer groupingUIID, Integer parentUIID, Integer parentActivityType,
	    String activityTitle) {

	return createToolActivity(uiid, order, layoutCoords, LdTemplateController.MCQ_TOOL_SIGNATURE,
		LdTemplateController.MCQ_ICON, toolContentID, contentFolderID, groupingUIID, parentUIID,
		parentActivityType, activityTitle != null ? activityTitle : "Multiple Choice",
		Activity.CATEGORY_ASSESSMENT, LdTemplateController.MCQ_TOOL_OUTPUT_DEFINITION);
    }

    /**
     * Helper method to create a mindmap tool content.
     */
    protected Long createMindmapToolContent(UserDTO user, String title, String instructions, boolean lockWhenFinished,
	    boolean multiUserMode, String reflectionInstruction) throws IOException {

	ObjectNode toolContentJSON = createStandardToolContent(title, instructions, null, lockWhenFinished, null, null);
	toolContentJSON.put("multiUserMode", multiUserMode);
	return createToolContent(user, LdTemplateController.MINDMAP_TOOL_SIGNATURE, toolContentJSON);
    }

    /**
     * Creates a multiUserMode activity's JSON details.
     */
    protected ObjectNode createMindmapActivity(AtomicInteger uiid, int order, Integer[] layoutCoords,
	    Long toolContentID, String contentFolderID, Integer groupingUIID, Integer parentUIID,
	    Integer parentActivityType, String activityTitle) {

	return createToolActivity(uiid, order, layoutCoords, LdTemplateController.MINDMAP_TOOL_SIGNATURE,
		LdTemplateController.MINDMAP_ICON, toolContentID, contentFolderID, groupingUIID, parentUIID,
		parentActivityType, activityTitle != null ? activityTitle : "MindMap", Activity.CATEGORY_RESPONSE);
    }

    /**
     * Helper method to create a forum tool content. Forum is an unusual tool in that it caches user's login names and
     * first/last names Mandatory fields in toolContentJSON: title, instructions, resources, user fields firstName,
     * lastName and loginName Topics must contain a ArrayNode of ObjectNode objects, which have the following mandatory
     * fields: title, description, type There should be at least one resource object in the Topics array. See
     * ResourceService for the optional entries.
     */
    protected Long createResourcesToolContent(UserDTO user, String title, String instructions, boolean lockWhenFinished,
	    boolean runContentAutomatically, boolean allowLearnerAddURL, boolean allowLearnerAddFile,
	    boolean notifyInstructors, Integer minResourcesToView, String reflectionInstructions, ArrayNode resources)
	    throws IOException {

	ObjectNode toolContentJSON = createStandardToolContent(title, instructions, reflectionInstructions,
		lockWhenFinished, null, user);
	toolContentJSON.put("allowAddFiles", allowLearnerAddFile);
	toolContentJSON.put("allowAddUrls", allowLearnerAddURL);
	toolContentJSON.put("notifyTeachersOnAssigmentSumbit", notifyInstructors);
	toolContentJSON.put("runAuto", runContentAutomatically);
	if (minResourcesToView != null) {
	    toolContentJSON.put("minViewResourceNumber", minResourcesToView);
	}
	toolContentJSON.set("resources", resources);
	return createToolContent(user, LdTemplateController.SHARE_RESOURCES_TOOL_SIGNATURE, toolContentJSON);
    }

    // resource type - copied from ResourceConstants
    public static final short RESOURCE_TYPE_URL = 1;
    public static final short RESOURCE_TYPE_FILE = 2;
    public static final short RESOURCE_TYPE_WEBSITE = 3;
    public static final short RESOURCE_TYPE_LEARNING_OBJECT = 4;

    private ObjectNode createResourceItem(String title, short type, String[] instructions, File file, int displayOrder)
	    throws IOException {
	ObjectNode item = JsonNodeFactory.instance.objectNode();
	item.put(RestTags.TITLE, title != null ? title : "");
	item.put("type", type);
	item.put(RestTags.DISPLAY_ORDER, displayOrder);

	if (instructions != null) {
	    item.set("instructions", JsonUtil.readArray(instructions));
	} else {
	    item.set("instructions", JsonNodeFactory.instance.arrayNode());
	}

	// TODO files - need to save it somehow, validate the file size, etc
	if (type != LdTemplateController.RESOURCE_TYPE_URL) {
	    LdTemplateController.log
		    .warn("LD Templates not handling files yet - file, website & LO resources won't work. Filename "
			    + file.getAbsoluteFile());
	}
	return item;
    }

    protected ObjectNode createResourceURL(String title, String[] instructions, String URL, boolean openInNewWindow,
	    int displayOrder) throws IOException {
	ObjectNode obj = createResourceItem(title, LdTemplateController.RESOURCE_TYPE_URL, instructions, null,
		displayOrder);
	obj.put("url", URL);
	obj.put("openUrlNewWindow", openInNewWindow);
	return obj;
    }

    protected ObjectNode createResourceFile(String title, String description, String[] instructions, File file,
	    int displayOrder) throws IOException {
	ObjectNode obj = createResourceItem(title, LdTemplateController.RESOURCE_TYPE_FILE, instructions, file,
		displayOrder);
	obj.put("description", description);
	return obj;
    }

    protected ObjectNode createResourceWebsite(String title, String description, String[] instructions, File file,
	    int displayOrder) throws IOException {
	ObjectNode obj = createResourceItem(title, LdTemplateController.RESOURCE_TYPE_WEBSITE, instructions, file,
		displayOrder);
	obj.put("description", description);
	return obj;
    }

    protected ObjectNode createResourceLearningObject(String title, String description, String[] instructions,
	    File file, int displayOrder) throws IOException {
	ObjectNode obj = createResourceItem(title, LdTemplateController.RESOURCE_TYPE_LEARNING_OBJECT, instructions,
		file, displayOrder);
	obj.put("description", description);
	return obj;
    }

    /**
     * Creates a forum activity's JSON details.
     */
    protected ObjectNode createResourcesActivity(AtomicInteger uiid, int order, Integer[] layoutCoords,
	    Long toolContentID, String contentFolderID, Integer groupingUIID, Integer parentUIID,
	    Integer parentActivityType, String activityTitle) {

	return createToolActivity(uiid, order, layoutCoords, LdTemplateController.SHARE_RESOURCES_TOOL_SIGNATURE,
		LdTemplateController.SHARE_RESOURCES_ICON, toolContentID, contentFolderID, groupingUIID, parentUIID,
		parentActivityType, activityTitle != null ? activityTitle : "Share Resources",
		Activity.CATEGORY_CONTENT);
    }

    /**
     * Helper method to create a Scratchie tool content. Title, instructions and questions are required (see tool for
     * full details of questions). Other fields are optional.
     */
    protected Long createScratchieToolContent(UserDTO user, String title, String instructions,
	    boolean useSelectLeaderToolOuput, Integer confidenceLevelsActivityUiid, ArrayNode questions)
	    throws IOException {

	ObjectNode toolContentJSON = createStandardToolContent(title, instructions, null, null, null, null);
	toolContentJSON.set(RestTags.QUESTIONS, questions);
	if (confidenceLevelsActivityUiid != null) {
	    toolContentJSON.put(RestTags.CONFIDENCE_LEVELS_ACTIVITY_UIID, confidenceLevelsActivityUiid);
	}
	return createToolContent(user, LdTemplateController.SCRATCHIE_TOOL_SIGNATURE, toolContentJSON);
    }

    /**
     * Creates a Scratchie activity's JSON details.
     */
    protected ObjectNode createScratchieActivity(AtomicInteger uiid, int order, Integer[] layoutCoords,
	    Long toolContentID, String contentFolderID, Integer groupingUIID, Integer parentUIID,
	    Integer parentActivityType, String activityTitle) {

	return createToolActivity(uiid, order, layoutCoords, LdTemplateController.SCRATCHIE_TOOL_SIGNATURE,
		LdTemplateController.SCRATCHIE_ICON, toolContentID, contentFolderID, groupingUIID, parentUIID,
		parentActivityType, activityTitle != null ? activityTitle : "Scratchie", Activity.CATEGORY_CONTENT,
		LdTemplateController.SCRATCHIE_TOOL_OUTPUT_DEFINITION);
    }

    /**
     * Helper method to create a scribe tool content. Headings which is ArrayNode of strings. There should be at least
     * one heading. The scribe service treats reflectInstructions as optional
     */
    protected Long createScribeToolContent(UserDTO user, String title, String instructions, boolean lockWhenFinished,
	    boolean autoSelectScribe, boolean showAggregatedReports, String reflectionInstructions, ArrayNode questions)
	    throws IOException {

	ObjectNode toolContentJSON = createStandardToolContent(title, instructions, reflectionInstructions,
		lockWhenFinished, null, null);
	toolContentJSON.set(RestTags.QUESTIONS, questions);
	toolContentJSON.put("autoSelectScribe", autoSelectScribe);
	toolContentJSON.put("showAggregatedReports", showAggregatedReports);
	return createToolContent(user, LdTemplateController.SCRIBE_TOOL_SIGNATURE, toolContentJSON);
    }

    /**
     * Creates a scribe activity's JSON details.
     */
    protected ObjectNode createScribeActivity(AtomicInteger uiid, int order, Integer[] layoutCoords, Long toolContentID,
	    String contentFolderID, Integer groupingUIID, Integer parentUIID, Integer parentActivityType,
	    String activityTitle) {

	return createToolActivity(uiid, order, layoutCoords, LdTemplateController.SCRIBE_TOOL_SIGNATURE,
		LdTemplateController.SCRIBE_ICON, toolContentID, contentFolderID, groupingUIID, parentUIID,
		parentActivityType, activityTitle != null ? activityTitle : "Scribe", Activity.CATEGORY_COLLABORATION);
    }

    /**
     * Helper method to create a submit tool content. Another tool that caches user's login names and
     * first/last names Mandatory fields: title & instructions; userDTO which gives user's firstName, lastName &
     * loginName;
     */
    protected Long createSubmitToolContent(UserDTO user, String title, String instructions, boolean lockWhenFinished,
	    Boolean limitUpload, Integer limitUploadNumber, String reflectionInstructions) throws IOException {

	ObjectNode toolContentJSON = createStandardToolContent(title, instructions, reflectionInstructions,
		lockWhenFinished, null, user);
	if (limitUploadNumber != null) {
	    toolContentJSON.put("limitUpload", limitUpload != null ? limitUpload : true);
	    toolContentJSON.put("limitUploadNumber", limitUploadNumber);
	}
	return createToolContent(user, LdTemplateController.SUBMIT_TOOL_SIGNATURE, toolContentJSON);
    }

    /**
     * Creates a forum activity's JSON details.
     */
    protected ObjectNode createSubmitActivity(AtomicInteger uiid, int order, Integer[] layoutCoords, Long toolContentID,
	    String contentFolderID, Integer groupingUIID, Integer parentUIID, Integer parentActivityType,
	    String activityTitle) {

	return createToolActivity(uiid, order, layoutCoords, LdTemplateController.SUBMIT_TOOL_SIGNATURE,
		LdTemplateController.SUBMIT_ICON, toolContentID, contentFolderID, groupingUIID, parentUIID,
		parentActivityType, activityTitle != null ? activityTitle : "Submit File",
		Activity.CATEGORY_ASSESSMENT);
    }

    /**
     * Helper method to create a survey tool content. Another tool that caches user's login names and
     * first/last names! See the survey implementation for the full field list.
     */
    protected Long createSurveyToolContent(UserDTO user, String title, String instructions, Boolean lockWhenFinished,
	    ArrayNode questions) throws IOException {

	ObjectNode toolContentJSON = createStandardToolContent(title, instructions, null, lockWhenFinished, null, user);
	toolContentJSON.set("questions", questions);
	return createToolContent(user, LdTemplateController.SURVEY_TOOL_SIGNATURE, toolContentJSON);
    }

    /**
     * Creates a forum activity's JSON details.
     */
    protected ObjectNode createSurveyActivity(AtomicInteger uiid, int order, Integer[] layoutCoords, Long toolContentID,
	    String contentFolderID, Integer groupingUIID, Integer parentUIID, Integer parentActivityType,
	    String activityTitle) {

	return createToolActivity(uiid, order, layoutCoords, LdTemplateController.SURVEY_TOOL_SIGNATURE,
		LdTemplateController.SURVEY_ICON, toolContentID, contentFolderID, groupingUIID, parentUIID,
		parentActivityType, activityTitle != null ? activityTitle : "Survey", Activity.CATEGORY_RESPONSE);
    }

    /**
     * Helper method to create a Vote tool content. Title, instructions and answers (Array of String) are required.
     * Other fields are optional.
     */
    protected Long createVoteToolContent(UserDTO user, String title, String instructions, ArrayNode answers,
	    Boolean showResults) throws IOException {

	ObjectNode toolContentJSON = createStandardToolContent(title, instructions, null, null, null, null);
	toolContentJSON.set(RestTags.ANSWERS, answers);
	toolContentJSON.put("showResults", showResults);
	return createToolContent(user, LdTemplateController.VOTE_TOOL_SIGNATURE, toolContentJSON);
    }

    /**
     * Creates a MCQ activity's JSON details.
     */
    protected ObjectNode createVoteActivity(AtomicInteger uiid, int order, Integer[] layoutCoords, Long toolContentID,
	    String contentFolderID, Integer groupingUIID, Integer parentUIID, Integer parentActivityType,
	    String activityTitle) {

	return createToolActivity(uiid, order, layoutCoords, LdTemplateController.VOTE_TOOL_SIGNATURE,
		LdTemplateController.VOTE_ICON, toolContentID, contentFolderID, groupingUIID, parentUIID,
		parentActivityType, activityTitle != null ? activityTitle : "Voting", Activity.CATEGORY_RESPONSE);
    }

    /**
     * Helper method to create a wiki tool content.
     */
    protected Long createWikiToolContent(UserDTO user, String title, String instructions, boolean lockWhenFinished,
	    String reflectionInstruction, ArrayNode pages) throws IOException {

	ObjectNode toolContentJSON = createStandardToolContent(title, instructions, null, lockWhenFinished, null, null);
	toolContentJSON.set("pages", pages);
	return createToolContent(user, LdTemplateController.WIKI_TOOL_SIGNATURE, toolContentJSON);
    }

    /**
     * Creates a multiUserMode activity's JSON details.
     */
    protected ObjectNode createWikiActivity(AtomicInteger uiid, int order, Integer[] layoutCoords, Long toolContentID,
	    String contentFolderID, Integer groupingUIID, Integer parentUIID, Integer parentActivityType,
	    String activityTitle) {

	return createToolActivity(uiid, order, layoutCoords, LdTemplateController.WIKI_TOOL_SIGNATURE,
		LdTemplateController.WIKI_ICON, toolContentID, contentFolderID, groupingUIID, parentUIID,
		parentActivityType, activityTitle != null ? activityTitle : "Wiki", Activity.CATEGORY_COLLABORATION);
    }

    /**
     * Helper method to create a Peer Review tool content.
     * Required fields in toolContentJSON: "title", "instructions", "questions", "firstName", "lastName", "lastName",
     * "questions" and "references".
     *
     * The criterias entry should be ArrayNode as defined in PeerReviewCriters object.
     */
    protected Long createPeerReviewToolContent(UserDTO user, String title, String instructions,
	    String reflectionInstructions, ArrayNode criterias) throws IOException {

	ObjectNode toolContentJSON = createStandardToolContent(title, instructions, reflectionInstructions, null, null,
		user);
	toolContentJSON.set("criterias", criterias);
	return createToolContent(user, LdTemplateController.PEER_REVIEW_TOOL_SIGNATURE, toolContentJSON);
    }

    /**
     * Creates a PeerRev activity's JSON details.
     */
    protected ObjectNode createPeerReviewActivity(AtomicInteger uiid, int order, Integer[] layoutCoords,
	    Long toolContentID, String contentFolderID, Integer groupingUIID, Integer parentUIID,
	    Integer parentActivityType, String activityTitle) {

	return createToolActivity(uiid, order, layoutCoords, LdTemplateController.PEER_REVIEW_TOOL_SIGNATURE,
		LdTemplateController.PEER_REVIEW_ICON, toolContentID, contentFolderID, groupingUIID, parentUIID,
		parentActivityType, activityTitle != null ? activityTitle : "Peer Review", Activity.CATEGORY_CONTENT);
    }

    /**
     *
     * /* ************************************** Service related methods **********************************************
     */
    /* ************************************** I18N related methods ************************************************* */

    protected final Tool getTool(String toolSignature) {
	return toolDAO.getToolBySignature(toolSignature);
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
     * which sets up a new CKEditor. Defaults to essay. If questionType = "mcq" then it will do a multiple choice
     */
    @RequestMapping("/createAssessment")
    public String createAssessment(HttpServletRequest request) {
	request.setAttribute("questionNumber", WebUtil.readIntParam(request, "questionNumber"));
	request.setAttribute("containingDivName", WebUtil.readStrParam(request, "containingDivName", true));

	String questionType = WebUtil.readStrParam(request, "questionType");
	if (questionType != null) {
	    if (questionType.equalsIgnoreCase("essay")) {
		return "authoring/template/tool/assessment";
	    }
	    // if it is a import from Question Bank, we need to do further processing
	    if (questionType.equalsIgnoreCase("importQbAe")) {
		return "forward:importQbAe.do";
	    }
	}
	return "/authoring/template/tool/assessmcq";

    }

    @RequestMapping("/importQTI")
    public String importAssessmentQTI(HttpServletRequest request) throws UnsupportedEncodingException {
	String contentFolderID = WebUtil.readStrParam(request, "contentFolderID");
	String templatePage = WebUtil.readStrParam(request, "templatePage");
	List<Assessment> updatedQuestions = preprocessQuestions(QuestionParser.parseQuestionChoiceForm(request),
		contentFolderID);
	request.setAttribute("questions", updatedQuestions);
	request.setAttribute("questionNumber", WebUtil.readIntParam(request, "questionNumber"));
	request.setAttribute("numQuestionsFieldname", WebUtil.readStrParam(request, "numQuestionsFieldname"));
	request.setAttribute("containingDivName", WebUtil.readStrParam(request, "containingDivName", true));
	return "/authoring/template/tool/" + templatePage;
    }

    /**
     * Gets a QB question based on its UID and creates a structure for template wizard JSP.
     */
    @RequestMapping("/importQbAe")
    private String importAeQuestionFromQb(@RequestParam long qbQuestionUid, Model model)
	    throws UnsupportedEncodingException {
	QbQuestion qbQuestion = qbService.getQuestionByUid(qbQuestionUid);

	Assessment question = new Assessment();
	question.setType(qbQuestion.getType().shortValue());
	question.setTitle(qbQuestion.getName());
	question.setText(qbQuestion.getDescription());
	question.setMultipleAnswersAllowed(qbQuestion.isMultipleAnswersAllowed());
	question.setDefaultGrade(qbQuestion.getMaxMark());
	question.setUuid(qbQuestion.getUuid().toString());

	model.addAttribute(AttributeNames.PARAM_CONTENT_FOLDER_ID, qbQuestion.getContentFolderId());
	model.addAttribute("question", question);

	if (question.getType() == Assessment.ASSESSMENT_QUESTION_TYPE_MULTIPLE_CHOICE) {
	    Set<AssessMCAnswer> answers = question.getAnswers();
	    for (QbOption qbOption : qbQuestion.getQbOptions()) {
		AssessMCAnswer answer = new AssessMCAnswer(qbOption.getDisplayOrder(), qbOption.getName(),
			qbOption.getMaxMark());
		answers.add(answer);
	    }
	    return "/authoring/template/tool/assessmcq";
	}
	return "/authoring/template/tool/assessment";
    }

    /**
     * Gets a QB question based on its UID and creates a structure for template wizard JSP.
     */
    @RequestMapping("/importQbIra")
    private String importIraQuestionFromQb(@RequestParam long qbQuestionUid, @RequestParam int questionNumber,
	    Model model) throws UnsupportedEncodingException {
	QbQuestion qbQuestion = qbService.getQuestionByUid(qbQuestionUid);

	Assessment question = new Assessment();
	question.setType(Assessment.ASSESSMENT_QUESTION_TYPE_MULTIPLE_CHOICE);
	question.setTitle(qbQuestion.getName());
	question.setText(qbQuestion.getDescription());
	question.setUuid(qbQuestion.getUuid().toString());

	Set<AssessMCAnswer> answers = question.getAnswers();
	for (QbOption qbOption : qbQuestion.getQbOptions()) {
	    AssessMCAnswer answer = new AssessMCAnswer(qbOption.getDisplayOrder(), qbOption.getName(),
		    qbOption.getMaxMark());
	    answers.add(answer);
	}

	model.addAttribute(AttributeNames.PARAM_CONTENT_FOLDER_ID, qbQuestion.getContentFolderId());
	model.addAttribute("question", question);
	model.addAttribute("questionNumber", questionNumber);
	return "/authoring/template/tool/mcquestion";
    }

    private List<Assessment> preprocessQuestions(Question[] questions, String contentFolderID) {

	List<Assessment> assessments = new ArrayList<>(questions.length);

	// Processing based on QTIUtil from the Assessment tool
	for (Question question : questions) {

	    Assessment assessment = new Assessment();
	    assessments.add(assessment);

	    boolean isMultipleChoice = Question.QUESTION_TYPE_MULTIPLE_CHOICE.equals(question.getType());
	    boolean isMultipleResponse = Question.QUESTION_TYPE_MULTIPLE_RESPONSE.equals(question.getType());
	    int defaultGrade = 1;

	    assessment.setText(QuestionParser.processHTMLField(question.getText(), false, contentFolderID,
		    question.getResourcesFolderPath()));
	    assessment.setTitle(question.getTitle());
	    assessment.setUuid(question.getQbUUID());

	    if (isMultipleChoice) {
		assessment.setType(Assessment.ASSESSMENT_QUESTION_TYPE_MULTIPLE_CHOICE);
		assessment.setMultipleAnswersAllowed(false);
		String correctAnswer = null;

		if (question.getAnswers() != null) {
		    int displayOrder = 1;
		    for (Answer answer : question.getAnswers()) {

			String answerText = QuestionParser.processHTMLField(answer.getText(), false, contentFolderID,
				question.getResourcesFolderPath());
			if ((correctAnswer != null) && correctAnswer.equals(answerText)) {
			    log.warn("Skipping an answer with same text as the correct answer: " + answerText);
			    continue;
			}

			AssessMCAnswer newAnswer = new AssessMCAnswer(displayOrder++, answerText, 0F);

			if ((answer.getScore() != null) && (answer.getScore() > 0)) {
			    if (correctAnswer == null) {
				// whatever the correct answer holds, it becomes the question score
				defaultGrade = Double.valueOf(Math.ceil(answer.getScore())).intValue();
				// 100% goes to the correct answer
				newAnswer.setGrade(1F);
				correctAnswer = answerText;
			    } else {
				log.warn("Choosing only first correct answer, despite another one was found: "
					+ answerText);
			    }
			}

			assessment.getAnswers().add(newAnswer);
		    }
		}

		if (correctAnswer == null) {
		    log.warn("No correct answer found for question: " + question.getText());
		    continue;
		}

	    } else if (isMultipleResponse) {
		assessment.setType(Assessment.ASSESSMENT_QUESTION_TYPE_MULTIPLE_CHOICE);
		assessment.setMultipleAnswersAllowed(true);

		if (question.getAnswers() != null) {
		    float totalScore = 0;
		    for (Answer answer : question.getAnswers()) {
			if ((answer.getScore() != null) && (answer.getScore() > 0)) {
			    // the question score information is stored as sum of answer scores
			    totalScore += answer.getScore();
			}
		    }
		    defaultGrade = Double.valueOf(Math.round(totalScore)).intValue();

		    int displayOrder = 1;
		    for (Answer answer : question.getAnswers()) {
			String answerText = QuestionParser.processHTMLField(answer.getText(), false, contentFolderID,
				question.getResourcesFolderPath());

			AssessMCAnswer newAnswer = new AssessMCAnswer(displayOrder++, answerText, 0F);

			if ((answer.getScore() != null) && (answer.getScore() > 0)) {
			    // set the factor of score for correct answers
			    newAnswer.setGrade(answer.getScore() / totalScore);
			} else {
			    newAnswer.setGrade(0F);
			}

			assessment.getAnswers().add(newAnswer);
		    }
		}
	    } else {
		assessment.setType(Assessment.ASSESSMENT_QUESTION_TYPE_ESSAY);
	    }

	    assessment.setDefaultGrade(defaultGrade);
	}

	return assessments;
    }

    /**
     * Specialised call to create a new question & options for the surveys tab. Returns a fragment of HTML
     * which sets up a new CKEditor. Works with both mcquestion.jsp & surveyquestion.jsp. The template's
     * struts action determines which jsp is used (see TBL and Inquiry uses).
     */
    @RequestMapping("/createQuestion")
    public String createQuestion(HttpServletRequest request) {
	request.setAttribute("questionNumber", WebUtil.readIntParam(request, "questionNumber"));
	String topicNumber = request.getParameter("topicNumber");
	if (topicNumber != null) {
	    request.setAttribute("topicNumber", topicNumber);
	}
	String forward = request.getParameter("forward");
	String path = null;
	if (forward != null) {
	    switch (forward) {
		case ("init"):
		    path = "authoring/template/tbl/tbl";
		    break;
		case ("question"):
		    path = "authoring/template/tool/mcquestion";
		    break;
		case ("questionoption"):
		    path = "authoring/template/tool/mcoption";
		    break;
		case ("redooption"):
		    path = "authoring/template/tool/mcredooption";
		    break;
		case ("assess"):
		    path = "authoring/template/tool/assessment";
		    break;
		case ("assessmcq"):
		    path = "authoring/template/tool/assessmcq";
		    break;
		case ("assessredooption"):
		    path = "authoring/template/tool/assessredooption";
		    break;
		case ("assessoption"):
		    path = "authoring/template/tool/assessoption";
		    break;
		case ("peerreviewstar"):
		    path = "authoring/template/tool/peerreviewstar";
		    break;
		case ("importQbIra"):
		    // further processing in another action method
		    path = "forward:importQbIra.do";
		    break;
		default:
		    path = null;
		    break;
	    }
	}
	return (path != null && path.length() > 0 ? path : "authoring/template/tool/mcquestion");
    }

    /**
     * Specialised call to create a new forum entry. Returns a fragment of HTML
     * which sets up two new fields - subject and body. Works with both forum.jsp.
     */
//    @RequestMapping
//    public String createForum(HttpServletRequest request) {
//	request.setAttribute("topicNumber", request.getParameter("topicNumber"));
//	return mapping.findForward("forum");
//    }

    /**
     * Specialised call to create a new option for a multiple choice question (mcoption.jsp), Survey question
     * (surveyoption.jsp) or assessment multiple choice (assessmcq.jsp).
     * Returns a fragment of HTML which sets up the editing field. The template's
     * struts action determines which jsp is used (see TBL and Inquiry uses).
     */
    @RequestMapping("/createOption")
    public String createOption(HttpServletRequest request) {
	request.setAttribute("questionNumber", WebUtil.readIntParam(request, "questionNumber"));
	request.setAttribute("optionNumber", WebUtil.readIntParam(request, "optionNumber"));
	boolean useAssessmentVersion = WebUtil.readBooleanParam(request, "assess", false);
	request.setAttribute("containingDivName", WebUtil.readStrParam(request, "containingDivName", true));
	return (useAssessmentVersion ? "authoring/template/tool/assessoption" : "authoring/template/tool/mcoption");
    }

    @RequestMapping("/deleteOption")
    public String deleteOption(HttpServletRequest request) {

	Integer questionNumber = WebUtil.readIntParam(request, "questionNumber", true);
	Integer delete = WebUtil.readIntParam(request, "optionNumber");

	boolean useAssessmentVersion = WebUtil.readBooleanParam(request, "assess", false);
	String containingDivName = WebUtil.readStrParam(request, "containingDivName", true);
	String prefixParam = containingDivName != null ? containingDivName + "assmcq" : "question";
	TreeMap<Integer, Option> optionsMap = getOptions(request, questionNumber, prefixParam);
	optionsMap.remove(delete);
	// reorder the displayOrder and setup the return value
	LinkedList<Option> options = new LinkedList<>();
	int displayOrder = 1;
	for (Option option : optionsMap.values()) {
	    option.setDisplayOrder(displayOrder++);
	    options.add(option);
	}
	request.setAttribute("questionNumber", questionNumber);
	request.setAttribute("options", options);
	request.setAttribute("optionCount", options.size());
	request.setAttribute("containingDivName", containingDivName);
	return (useAssessmentVersion ? "authoring/template/tool/assessredooption"
		: "authoring/template/tool/mcredooption");
    }

    @RequestMapping("/swapOption")
    public String swapOption(HttpServletRequest request) {

	Integer questionNumber = WebUtil.readIntParam(request, "questionNumber", true);

	// Work out which two to swap and make swap1 the smaller of the two.
	// They should always be consecutive, so we can (the new) swap1 with whichever is the next item.
	int swap1 = WebUtil.readIntParam(request, "optionNumber1");
	int swap2 = WebUtil.readIntParam(request, "optionNumber2");
	if (swap2 < swap1) {
	    swap1 = swap2;
	}

	boolean useAssessmentVersion = WebUtil.readBooleanParam(request, "assess", false);
	String containingDivName = WebUtil.readStrParam(request, "containingDivName", true);
	String prefixParam = containingDivName != null ? containingDivName + "assmcq" : "question";
	TreeMap<Integer, Option> optionsMap = getOptions(request, questionNumber, prefixParam);
	// reorder the options and setup the return value
	LinkedList<Option> options = new LinkedList<>();

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
	request.setAttribute("containingDivName", WebUtil.readStrParam(request, "containingDivName", true));
	return (useAssessmentVersion ? "authoring/template/tool/assessredooption"
		: "authoring/template/tool/mcredooption");
    }

    // if mcq paramPrefix = "question". if assessment multiple choice paramPrefix = assmcq
    private TreeMap<Integer, Option> getOptions(HttpServletRequest request, Integer questionNumber,
	    String prefixParam) {

	// correctDisplayIdInteger is used for MCQ but not Survey - the value will be ignored by the
	// survey jsp page.
	Integer correctDisplayIdInteger = WebUtil.readIntParam(request, prefixParam + questionNumber + "correct", true);
	int correctDisplayId = correctDisplayIdInteger != null ? correctDisplayIdInteger.intValue() : 0;

	TreeMap<Integer, Option> optionDtos = new TreeMap<>();

	for (int i = 1; i <= MAX_OPTION_COUNT; i++) {
	    String optionText = request.getParameter(prefixParam + questionNumber + "option" + i);
	    if (optionText != null) {
		// Grade is used for assessment
		String grade = request.getParameter(prefixParam + questionNumber + "option" + i + "grade");
		Option option = new Option(i, i == correctDisplayId, optionText, grade);
		optionDtos.put(new Integer(i), option);
	    }
	}

	return optionDtos;
    }
//
//    /**
//     * Specialised call to create a new URL field & title.
//     */
//    @RequestMapping("/createResource")
//    public ActionForward createResource(HttpServletRequest request) {
//	request.setAttribute("urlNumber", request.getParameter("urlNumber"));
//	if (request.getParameter("branchNumberUnderscore") != null) {
//	    request.setAttribute("branchNumberUnderscore", request.getParameter("branchNumberUnderscore"));
//	}
//	return mapping.findForward("resource");
//    }
//
//    /**
//     * Specialised call to create a new set of fields for a branch.
//     */
//    @RequestMapping("/createBranch")
//    public String createBranch(HttpServletRequest request) {
//	request.setAttribute("branchNumber", request.getParameter("branchNumber"));
//	return mapping.findForward("branch");
//    }

    /**
     * Specialised call to create a new rating criteria for the Peer Review fields. Returns a fragment of HTML
     * which sets up the new fields.
     */
    @RequestMapping("/createRatingCriteria")
    public String createRatingCriteria(HttpServletRequest request) {
	request.setAttribute("criteriaNumber", WebUtil.readIntParam(request, "criteriaNumber"));
	return "authoring/template/tool/peerreviewstar";
    }

}