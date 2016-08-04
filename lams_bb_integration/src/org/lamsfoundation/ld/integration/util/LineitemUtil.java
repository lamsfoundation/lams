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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA 
 * 
 * http://www.gnu.org/licenses/gpl.txt 
 * **************************************************************** 
 */

package org.lamsfoundation.ld.integration.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.ld.integration.Constants;

import blackboard.data.ValidationException;
import blackboard.data.content.Content;
import blackboard.data.content.CourseDocument;
import blackboard.data.course.Course;
import blackboard.data.gradebook.Lineitem;
import blackboard.data.gradebook.impl.OutcomeDefinition;
import blackboard.data.gradebook.impl.OutcomeDefinitionScale;
import blackboard.persist.BbPersistenceManager;
import blackboard.persist.Container;
import blackboard.persist.Id;
import blackboard.persist.KeyNotFoundException;
import blackboard.persist.PersistenceException;
import blackboard.persist.PkId;
import blackboard.persist.content.ContentDbLoader;
import blackboard.persist.course.CourseDbLoader;
import blackboard.persist.gradebook.LineitemDbLoader;
import blackboard.persist.gradebook.LineitemDbPersister;
import blackboard.persist.gradebook.ext.OutcomeDefinitionScaleDbLoader;
import blackboard.persist.gradebook.ext.OutcomeDefinitionScaleDbPersister;
import blackboard.persist.gradebook.impl.OutcomeDefinitionDbPersister;
import blackboard.platform.BbServiceManager;
import blackboard.platform.context.Context;
import blackboard.platform.persistence.PersistenceServiceFactory;
import blackboard.portal.data.ExtraInfo;
import blackboard.portal.data.PortalExtraInfo;
import blackboard.portal.servlet.PortalUtil;

public class LineitemUtil {

    private final static String LAMS_LINEITEM_STORAGE = "LamsLineitemStorage";

    private static Logger logger = Logger.getLogger(LineitemUtil.class);

    public static void createLineitem(Content bbContent, Context ctx, String userName)
	    throws ValidationException, PersistenceException, IOException {
	LineitemDbPersister linePersister = LineitemDbPersister.Default.getInstance();
	OutcomeDefinitionDbPersister outcomeDefinitionPersister = OutcomeDefinitionDbPersister.Default.getInstance();
	OutcomeDefinitionScaleDbLoader outcomeDefinitionScaleLoader = OutcomeDefinitionScaleDbLoader.Default
		.getInstance();
	OutcomeDefinitionScaleDbPersister uutcomeDefinitionScaleDbPersister = OutcomeDefinitionScaleDbPersister.Default
		.getInstance();

	String title = bbContent.getTitle();
	Id courseId = bbContent.getCourseId();
	String lamsLessonId = bbContent.getLinkRef();

	// Create new Gradebook column for current bbContent
	Lineitem lineitem = new Lineitem();
	lineitem.setCourseId(courseId);
	lineitem.setName(title);
	lineitem.setPointsPossible(Constants.GRADEBOOK_POINTS_POSSIBLE);
	lineitem.setType(Constants.GRADEBOOK_LINEITEM_TYPE);
	lineitem.setIsAvailable(true);
	lineitem.setDateAdded();
	lineitem.setAssessmentLocation(Lineitem.AssessmentLocation.EXTERNAL);
	lineitem.setAssessmentId(lamsLessonId, Lineitem.AssessmentLocation.EXTERNAL);
	lineitem.validate();
	linePersister.persist(lineitem);

	OutcomeDefinition outcomeDefinition = lineitem.getOutcomeDefinition();
	outcomeDefinition.setCourseId(courseId);
	outcomeDefinition.setPosition(1);

	boolean hasLessonScoreOutputs = LineitemUtil.hasLessonScoreOutputs(bbContent, ctx, userName);
	OutcomeDefinitionScale outcomeDefinitionScale;
	if (hasLessonScoreOutputs) {
	    outcomeDefinitionScale = outcomeDefinitionScaleLoader.loadByCourseIdAndTitle(courseId,
		    OutcomeDefinitionScale.SCORE);
	    outcomeDefinitionScale.setNumericScale(true);
	    outcomeDefinitionScale.setPercentageScale(true);
	    outcomeDefinition.setScorable(true);
	} else {
	    outcomeDefinitionScale = outcomeDefinitionScaleLoader.loadByCourseIdAndTitle(courseId,
		    OutcomeDefinitionScale.COMPLETE_INCOMPLETE);
	    outcomeDefinitionScale.setNumericScale(false);
	    outcomeDefinition.setScorable(false);
	}
	uutcomeDefinitionScaleDbPersister.persist(outcomeDefinitionScale);
	outcomeDefinition.setScale(outcomeDefinitionScale);
	outcomeDefinitionPersister.persist(outcomeDefinition);

	updateLamsLineitemStorage(bbContent, lineitem);
    }

    /*
     * Checks whether lesson has scorable outputs (i.e. MCQ or Assessment activity).
     * 
     * @param ctx
     * the blackboard context, contains session data
     * 
     * @return an url pointing to the LAMS lesson, monitor, author session
     * 
     * @throws IOException
     */
    private static boolean hasLessonScoreOutputs(Content bbContent, Context ctx, String username) throws IOException {
	String ldId = ctx.getRequestParameter("sequence_id");
	
	//sequence_id parameter is null in case we come from modify_proc
	if (ldId == null) {
	    //get sequence_id from bbcontent URL set in start_lesson_proc
	    String bbContentUrl = bbContent.getUrl();
	    String[] params = bbContentUrl.split("&");
	    for (String param : params) {
		String paramName = param.split("=")[0];
		String paramValue = param.split("=")[1];

		if ("ldid".equals(paramName)) {
		    ldId = paramValue;
		    break;
		}
	    }
	}

	String learningDesignSvgUrl = LamsSecurityUtil.generateRequestLearningDesignImage(username, true) + "&ldId="
		+ ldId.trim();

	URL url = new URL(learningDesignSvgUrl);
	URLConnection conn = url.openConnection();
	if (!(conn instanceof HttpURLConnection)) {
	    throw new RuntimeException("Unable to open connection to: " + learningDesignSvgUrl);
	}

	HttpURLConnection httpConn = (HttpURLConnection) conn;

	if (httpConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
	    throw new RuntimeException("HTTP Response Code: " + httpConn.getResponseCode() + ", HTTP Response Message: "
		    + httpConn.getResponseMessage());
	}

	// InputStream is = url.openConnection().getInputStream();
	InputStream is = conn.getInputStream();

	// parse xml response
	String learningDesignSvg = IOUtils.toString(is, "UTF-8");
	boolean hasLessonScoreOutputs = (learningDesignSvg.indexOf("icon_mcq.png") != -1)
		|| (learningDesignSvg.indexOf("icon_assessment.png") != -1);

	return hasLessonScoreOutputs;
    }

    /**
     * Removes lineitem. Throws exception if lineitem is not found.
     * 
     * @param bbContentId
     * @param courseIdStr
     * @throws PersistenceException
     * @throws ServletException
     */
    public static void removeLineitem(String bbContentId, String courseIdStr)
	    throws PersistenceException, ServletException {
	BbPersistenceManager bbPm = PersistenceServiceFactory.getInstance().getDbPersistenceManager();
	Container bbContainer = bbPm.getContainer();
	ContentDbLoader courseDocumentLoader = ContentDbLoader.Default.getInstance();
	LineitemDbPersister linePersister = LineitemDbPersister.Default.getInstance();

	Id contentId = new PkId(bbContainer, CourseDocument.DATA_TYPE, bbContentId);
	Content bbContent = courseDocumentLoader.loadById(contentId);
	//check isGradecenter option is ON (isDescribed field is used for storing isGradecenter parameter)
	if (!bbContent.getIsDescribed()) {
	    return;
	}

	Id lineitemId = getLineitem(bbContentId, courseIdStr, true);
	linePersister.deleteById(lineitemId);
    }

    /**
     * Changes lineitem's name. Throws exception if lineitem is not found.
     * 
     * @param bbContentId
     * @param courseIdStr
     * @param newLineitemName
     * @throws PersistenceException
     * @throws ServletException
     * @throws ValidationException
     */
    public static void changeLineitemName(String bbContentId, String courseIdStr, String newLineitemName)
	    throws PersistenceException, ServletException, ValidationException {
	LineitemDbLoader lineitemLoader = LineitemDbLoader.Default.getInstance();
	LineitemDbPersister linePersister = LineitemDbPersister.Default.getInstance();

	Id lineitemId = getLineitem(bbContentId, courseIdStr, true);
	Lineitem lineitem = lineitemLoader.loadById(lineitemId);
	lineitem.setName(newLineitemName);
	linePersister.persist(lineitem);
    }
    
    /**
     * Changes lineitem's lamsLessonId. Throws exception if lineitem is not found.
     * 
     * @param bbContentId
     * @param courseIdStr
     * @param newLineitemName
     * @throws PersistenceException
     * @throws ServletException
     * @throws ValidationException
     * @throws IOException 
     */
    public static void updateLineitemLessonId(Content bbContent, String courseIdStr, Long newLamsLessonId, Context ctx, String userName)
	    throws PersistenceException, ServletException, ValidationException, IOException {
	LineitemDbLoader lineitemLoader = LineitemDbLoader.Default.getInstance();
	LineitemDbPersister linePersister = LineitemDbPersister.Default.getInstance();
	
	PkId contentId = (PkId) bbContent.getId();
	String _content_id = "_" + contentId.getPk1() + "_" + contentId.getPk2();
	
	//update only in case grade center is ON
	if (bbContent.getIsDescribed()) {

	    Id lineitemId = getLineitem(_content_id, courseIdStr, false);
	    //in case admin forgot to check "Grade Center Columns and Settings" option on doing course copy/import
	    if (lineitemId == null) {
		createLineitem(bbContent, ctx, userName);
		
	    //in case he checked it and BB created Lineitem object, then just need to update it
	    } else {
		Lineitem lineitem = lineitemLoader.loadById(lineitemId);
		lineitem.setAssessmentId(Long.toString(newLamsLessonId), Lineitem.AssessmentLocation.EXTERNAL);
		linePersister.persist(lineitem);

		updateLamsLineitemStorage(bbContent, lineitem);
	    }

	}

	// store internalContentId -> externalContentId. It's used for GradebookServlet. Store it just in case
	PortalExtraInfo pei = PortalUtil.loadPortalExtraInfo(null, null, "LamsStorage");
	ExtraInfo ei = pei.getExtraInfo();
	ei.setValue(_content_id, Long.toString(newLamsLessonId));
	PortalUtil.savePortalExtraInfo(pei);
    }
    
    /* Updates  "LamsLineitemStorage" storage used for storing bbContentId->lineitem
     * @param bbContent
     * @param lineitem
     * @throws PersistenceException
     */
    private static void updateLamsLineitemStorage(Content bbContent, Lineitem lineitem) throws PersistenceException {
	//Construct bbContent id
	PkId bbContentPkId = (PkId) bbContent.getId();
	String bbContentId = "_" + bbContentPkId.getPk1() + "_" + bbContentPkId.getPk2();
	//Construct lineitem id
	PkId lineitemPkId = (PkId) lineitem.getId();
	String lineitemId = "_" + lineitemPkId.getPk1() + "_" + lineitemPkId.getPk2();
	
	//Store lineitemid to the storage bbContentId -> lineitemid (this storage is available since 1.2.3 version)
	PortalExtraInfo pei = PortalUtil.loadPortalExtraInfo(null, null, LAMS_LINEITEM_STORAGE);
	ExtraInfo ei = pei.getExtraInfo();
	ei.setValue(bbContentId, lineitemId);
	PortalUtil.savePortalExtraInfo(pei);
    }   

    /*
     * Finds lineitem by bbContentId and courseId.
     * 
     * @throws ServletException
     * 
     * @throws PersistenceException
     */
    private static Id getLineitem(String bbContentId, String courseIdStr, boolean isThrowException)
	    throws ServletException, PersistenceException, LamsBuildingBlockException {
	BbPersistenceManager bbPm = PersistenceServiceFactory.getInstance().getDbPersistenceManager();
	LineitemDbLoader lineitemLoader = LineitemDbLoader.Default.getInstance();

	//get lineitemId from the storage (bbContentId -> lineitemId)
	PortalExtraInfo pei = PortalUtil.loadPortalExtraInfo(null, null, LAMS_LINEITEM_STORAGE);
	ExtraInfo ei = pei.getExtraInfo();
	String lineitemIdStr = ei.getValue(bbContentId);

	// try to get lineitem from any course that user is participating in (for lineitems created in versions after 1.2 and before 1.2.3)
	if (lineitemIdStr == null) {
	    // get stored bbContentId -> lamsLessonId
	    PortalExtraInfo portalExtraInfo = PortalUtil.loadPortalExtraInfo(null, null, "LamsStorage");
	    ExtraInfo extraInfo = portalExtraInfo.getExtraInfo();
	    String lamsLessonId = extraInfo.getValue(bbContentId);

	    if (lamsLessonId == null || "".equals(lamsLessonId)) {
		if (isThrowException) {
		    throw new LamsBuildingBlockException("lamsLessonId corresponding to bbContentId= " + bbContentId
			    + " was not found in LamsStorage");
		} else {
		    return null;
		}
	    }

	    // search for appropriate lineitem
	    Id courseId = bbPm.generateId(Course.DATA_TYPE, courseIdStr);
	    List<Lineitem> lineitems = lineitemLoader.loadByCourseId(courseId);

	    Lineitem lineitem = null;
	    for (Lineitem lineitemIter : lineitems) {
		if (lineitemIter.getAssessmentId() != null && lineitemIter.getAssessmentId().equals(lamsLessonId)) {
		    lineitem = lineitemIter;
		    break;
		}
	    }

	    if (lineitem == null) {
		if (isThrowException) {
		    throw new LamsBuildingBlockException(
				"Lineitem that corresponds to bbContentId: " + bbContentId + " was not found");
		} else {
		    return null;
		}
	    }

	    // delete lineitem (can't delete it simply doing linePersister.deleteById(lineitem.getId()) due to BB9 bug)
	    PkId lineitemPkId = (PkId) lineitem.getId();
	    lineitemIdStr = "_" + lineitemPkId.getPk1() + "_" + lineitemPkId.getPk2();
	}

	if (lineitemIdStr == null) {
	    throw new LamsBuildingBlockException("Lineitem was not found for contentId:" + bbContentId
		    + ". This is despite the fact that isGradecenter option is ON.");
	}

	Id lineitemId = bbPm.generateId(Lineitem.LINEITEM_DATA_TYPE, lineitemIdStr.trim());
	return lineitemId;
    }

    /**
     * Finds lineitem by bbContentId, userId and lamsLessonId.
     * 
     * @throws ServletException
     * @throws PersistenceException
     */
    public static Lineitem getLineitem(String bbContentId, Id userId, String lamsLessonIdParam)
	    throws ServletException, PersistenceException {
	BbPersistenceManager bbPm = PersistenceServiceFactory.getInstance().getDbPersistenceManager();
	CourseDbLoader courseLoader = CourseDbLoader.Default.getInstance();
	LineitemDbLoader lineitemLoader = LineitemDbLoader.Default.getInstance();

	// get lineitemId from the storage (bbContentId -> lineitemid)
	PortalExtraInfo pei = PortalUtil.loadPortalExtraInfo(null, null, LAMS_LINEITEM_STORAGE);
	ExtraInfo ei = pei.getExtraInfo();
	String lineitemIdStr = ei.getValue(bbContentId);

	if (lineitemIdStr != null) {
	    Id lineitemId = bbPm.generateId(Lineitem.LINEITEM_DATA_TYPE, lineitemIdStr.trim());
	    return lineitemLoader.loadById(lineitemId);

	    // try to get lineitem from any course that user is participating in (for lineitems created in versions after 1.2 and before 1.2.3)   
	} else {

	    // search for appropriate lineitem
	    List<Course> userCourses = courseLoader.loadByUserId(userId);
	    for (Course userCourse : userCourses) {
		List<Lineitem> lineitems = lineitemLoader.loadByCourseId(userCourse.getId());

		for (Lineitem lineitem : lineitems) {
		    if (lineitem.getAssessmentId() != null && lineitem.getAssessmentId().equals(lamsLessonIdParam)) {
			return lineitem;
		    }
		}
	    }
	}

	return null;
    }

    /**
     * Finds lineitem by userId and lamsLessonId. The same as above method but first finds bbContentId and also checks
     * Grade center option is ON.
     * 
     * @throws ServletException
     * @throws PersistenceException
     */
    public static Lineitem getLineitem(Id userId, String lamsLessonIdParam)
	    throws ServletException, PersistenceException {
	BbPersistenceManager bbPm = PersistenceServiceFactory.getInstance().getDbPersistenceManager();
	Container bbContainer = bbPm.getContainer();
	ContentDbLoader contentDbLoader = ContentDbLoader.Default.getInstance();

	//Finds bbContentId from "LamsStorage" corresponding to specified lamsLessonId.
	//Note: bbContentId can be null in case it's Chen Rui's BB, which doesn't have "LamsStorage".
	PortalExtraInfo portalExtraInfo = PortalUtil.loadPortalExtraInfo(null, null, "LamsStorage");
	ExtraInfo extraInfo = portalExtraInfo.getExtraInfo();
	Set<String> bbContentIds = extraInfo.getKeys();
	String bbContentId = null;
	for (String bbContentIdIter : bbContentIds) {
	    String lamsLessonId = extraInfo.getValue(bbContentIdIter);
	    if (lamsLessonIdParam.equals(lamsLessonId)) {
		bbContentId = bbContentIdIter;
		break;
	    }
	}

	if (bbContentId != null) {
	    Id contentId = new PkId(bbContainer, CourseDocument.DATA_TYPE, bbContentId);
	    Content bbContent = contentDbLoader.loadById(contentId);
	    // check isGradecenter option is ON (bbContent.isDescribed field is used for storing isGradecenter parameter)
	    if (!bbContent.getIsDescribed()) {
		throw new LamsBuildingBlockException("Operation failed due to lesson (lessonId=" + lamsLessonIdParam
			+ ") has gradecenter option switched OFF.");
	    }
	}

	//get lineitem
	return LineitemUtil.getLineitem(bbContentId == null ? "" : bbContentId, userId, lamsLessonIdParam);
    }
}
