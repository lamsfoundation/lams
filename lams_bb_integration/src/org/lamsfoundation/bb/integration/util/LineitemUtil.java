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

package org.lamsfoundation.bb.integration.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.lamsfoundation.bb.integration.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import blackboard.data.ValidationException;
import blackboard.data.content.Content;
import blackboard.data.content.CourseDocument;
import blackboard.data.course.Course;
import blackboard.data.gradebook.Lineitem;
import blackboard.data.gradebook.Score;
import blackboard.data.gradebook.impl.OutcomeDefinition;
import blackboard.data.gradebook.impl.OutcomeDefinitionScale;
import blackboard.persist.BbPersistenceManager;
import blackboard.persist.Container;
import blackboard.persist.Id;
import blackboard.persist.PersistenceException;
import blackboard.persist.PkId;
import blackboard.persist.content.ContentDbLoader;
import blackboard.persist.course.CourseDbLoader;
import blackboard.persist.gradebook.LineitemDbLoader;
import blackboard.persist.gradebook.LineitemDbPersister;
import blackboard.persist.gradebook.ScoreDbPersister;
import blackboard.persist.gradebook.ext.OutcomeDefinitionScaleDbLoader;
import blackboard.persist.gradebook.ext.OutcomeDefinitionScaleDbPersister;
import blackboard.persist.gradebook.impl.OutcomeDefinitionDbPersister;
import blackboard.platform.persistence.PersistenceServiceFactory;
import blackboard.portal.data.ExtraInfo;
import blackboard.portal.data.PortalExtraInfo;
import blackboard.portal.servlet.PortalUtil;
import blackboard.util.StringUtil;

public class LineitemUtil {

    private final static String LAMS_LINEITEM_STORAGE = "LamsLineitemStorage";

    private static Logger logger = LoggerFactory.getLogger(LineitemUtil.class);

    @SuppressWarnings("deprecation")
    public static void createLineitem(Content bbContent, String userName)
	    throws ValidationException, PersistenceException, IOException, ParserConfigurationException, SAXException {
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

	boolean hasLessonScoreOutputs = LineitemUtil.hasLessonScoreOutputs(bbContent, userName);
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
    private static boolean hasLessonScoreOutputs(Content bbContent, String username) throws IOException, ParserConfigurationException, SAXException {
	
	//at this moment bbContent contains already updated lessonId
	String lessonId = bbContent.getLinkRef();

	String serviceURL = LamsPluginUtil.getServerUrl() + "/services/xml/LessonManager?"
		+ LamsSecurityUtil.generateAuthenticateParameters(username)
		+ "&method=checkLessonForNumericToolOutputs&lsId=" + lessonId;

	URL url = new URL(serviceURL);
	URLConnection conn = url.openConnection();
	if (!(conn instanceof HttpURLConnection)) {
	    throw new RuntimeException("Unable to open connection to: " + serviceURL);
	}

	HttpURLConnection httpConn = (HttpURLConnection) conn;

	if (httpConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
	    throw new RuntimeException("HTTP Response Code: " + httpConn.getResponseCode() + ", HTTP Response Message: "
		    + httpConn.getResponseMessage());
	}

	InputStream is = conn.getInputStream();

	// parse xml response
	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	DocumentBuilder db = dbf.newDocumentBuilder();
	Document document = db.parse(is);
	Element lesson = document.getDocumentElement();
	boolean hasNumericToolOutput = Boolean
		.parseBoolean(lesson.getAttributes().getNamedItem("hasNumericToolOutput").getNodeValue());

	return hasNumericToolOutput;
    }

    /**
     * Removes lineitem. Throws exception if lineitem is not found.
     * 
     * @param _content_id
     * @param _course_id
     * @throws PersistenceException
     * @throws ServletException
     */
    public static void removeLineitem(String _content_id, String _course_id)
	    throws PersistenceException, ServletException {
	BbPersistenceManager bbPm = PersistenceServiceFactory.getInstance().getDbPersistenceManager();
	Container bbContainer = bbPm.getContainer();
	ContentDbLoader courseDocumentLoader = ContentDbLoader.Default.getInstance();
	LineitemDbPersister linePersister = LineitemDbPersister.Default.getInstance();

	Id contentId = new PkId(bbContainer, CourseDocument.DATA_TYPE, _content_id);
	Content bbContent = courseDocumentLoader.loadById(contentId);
	//check isGradecenter option is ON and thus there should exist associated lineitem object
	if (!bbContent.getIsDescribed()) {
	    return;
	}

	Id lineitemId = getLineitem(_content_id, _course_id, true);
	linePersister.deleteById(lineitemId);
	
	//Remove bbContentId -> lineitemid pair from the storage
	PortalExtraInfo pei = PortalUtil.loadPortalExtraInfo(null, null, LAMS_LINEITEM_STORAGE);
	ExtraInfo ei = pei.getExtraInfo();
	ei.clearEntry(_content_id);
	PortalUtil.savePortalExtraInfo(pei);
    }

    /**
     * Changes lineitem's name. Throws exception if lineitem is not found.
     * 
     * @param _content_id
     * @param _course_id
     * @param newLineitemName
     * @throws PersistenceException
     * @throws ServletException
     * @throws ValidationException
     */
    public static void changeLineitemName(String _content_id, String _course_id, String newLineitemName)
	    throws PersistenceException, ServletException, ValidationException {
	LineitemDbLoader lineitemLoader = LineitemDbLoader.Default.getInstance();
	LineitemDbPersister linePersister = LineitemDbPersister.Default.getInstance();

	Id lineitemId = getLineitem(_content_id, _course_id, true);
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
     * @throws SAXException 
     * @throws ParserConfigurationException 
     */
    public static void updateLineitemLessonId(Content bbContent, String courseIdStr, Long newLamsLessonId, String userName)
	    throws PersistenceException, ServletException, ValidationException, IOException, ParserConfigurationException, SAXException {
	LineitemDbLoader lineitemLoader = LineitemDbLoader.Default.getInstance();
	LineitemDbPersister linePersister = LineitemDbPersister.Default.getInstance();
	
	String _content_id = bbContent.getId().toExternalString();
	
	//update only in case grade center is ON
	if (bbContent.getIsDescribed()) {

	    Id lineitemId = getLineitem(_content_id, courseIdStr, false);
	    //in case admin forgot to check "Grade Center Columns and Settings" option on doing course copy/import
	    if (lineitemId == null) {
		createLineitem(bbContent, userName);
		
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
	//get bbContent id
	String _content_id = bbContent.getId().toExternalString();
	//get lineitem id
	String _lineitem_id = lineitem.getId().toExternalString();
	
	//Store lineitemid to the storage bbContentId -> lineitemid (this storage is available since 1.2.3 version)
	PortalExtraInfo pei = PortalUtil.loadPortalExtraInfo(null, null, LAMS_LINEITEM_STORAGE);
	ExtraInfo ei = pei.getExtraInfo();
	ei.setValue(_content_id, _lineitem_id);
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
	String _lineitem_id = ei.getValue(bbContentId);

	// try to get lineitem from any course that user is participating in (for lineitems created in versions after 1.2 and before 1.2.3)
	if (_lineitem_id == null) {
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
	    _lineitem_id = lineitem.getId().toExternalString();
	}

	if (_lineitem_id == null) {
	    throw new LamsBuildingBlockException("Lineitem was not found for contentId:" + bbContentId
		    + ". This is despite the fact that isGradecenter option is ON.");
	}

	Id lineitemId = bbPm.generateId(Lineitem.LINEITEM_DATA_TYPE, _lineitem_id.trim());
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
     * @param isThrowException whether exception should be thrown in case Gradecenter option is OFF or it should return simple null
     * @throws ServletException
     * @throws PersistenceException
     */
    public static Lineitem getLineitem(Id userId, String lamsLessonIdParam, boolean isThrowException)
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
		if (isThrowException) {
		    throw new LamsBuildingBlockException("Operation failed due to lesson (lessonId=" + lamsLessonIdParam
			    + ", bbContentId=" + bbContentId + ") has gradecenter option switched OFF.");
		} else {
		    return null;
		}
	    }
	}

	//get lineitem
	return LineitemUtil.getLineitem(bbContentId == null ? "" : bbContentId, userId, lamsLessonIdParam);
    }
    
    /**
     * Updates and persists currentScore in the DB.
     * 
     * @param lesson
     * @param learnerResult
     * @param currentScore provided score must be initialized (can't be null)
     * @throws PersistenceException 
     * @throws ValidationException 
     */
    public static double updateScoreBasedOnLamsResponse(Node lesson, Node learnerResult, Score currentScore)
	    throws PersistenceException, ValidationException {
	ScoreDbPersister scorePersister = ScoreDbPersister.Default.getInstance();

	Long lessonMaxPossibleMark = new Long(
		lesson.getAttributes().getNamedItem("lessonMaxPossibleMark").getNodeValue());
	String userTotalMarkStr = learnerResult.getAttributes().getNamedItem("userTotalMark").getNodeValue();

	double newScore = StringUtil.isEmpty(userTotalMarkStr) ? 0 : new Double(userTotalMarkStr);

	//set score grade. if Lams supplies one (and lineitem will have score type) we set score; otherwise (and
	// lineitme of type Complete/Incomplete) we set 0
	double gradebookMark = 0;
	if (lessonMaxPossibleMark > 0) {
	    gradebookMark = (newScore / lessonMaxPossibleMark) * Constants.GRADEBOOK_POINTS_POSSIBLE;
	}
	
	currentScore.setGrade(new DecimalFormat("##.##").format(gradebookMark));
	currentScore.validate();
	scorePersister.persist(currentScore);
	
	return gradebookMark;
    }
}
