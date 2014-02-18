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

/* $Id$ */
package org.lamsfoundation.ld.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import javax.servlet.ServletException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.ld.integration.Constants;
import org.lamsfoundation.ld.integration.blackboard.LamsSecurityUtil;

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
import blackboard.persist.PersistenceException;
import blackboard.persist.PkId;
import blackboard.persist.content.ContentDbLoader;
import blackboard.persist.gradebook.LineitemDbLoader;
import blackboard.persist.gradebook.LineitemDbPersister;
import blackboard.persist.gradebook.ext.OutcomeDefinitionScaleDbLoader;
import blackboard.persist.gradebook.ext.OutcomeDefinitionScaleDbPersister;
import blackboard.persist.gradebook.impl.OutcomeDefinitionDbPersister;
import blackboard.platform.BbServiceManager;
import blackboard.platform.context.Context;
import blackboard.portal.data.ExtraInfo;
import blackboard.portal.data.PortalExtraInfo;
import blackboard.portal.servlet.PortalUtil;

public class LineitemUtil {
    
    private static Logger logger = Logger.getLogger(LineitemUtil.class);

    public static void createLineitem(Context ctx, Content bbContent) throws ValidationException, PersistenceException, IOException {
	
	BbPersistenceManager bbPm = BbServiceManager.getPersistenceService().getDbPersistenceManager();

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
	LineitemDbPersister linePersister = (LineitemDbPersister) bbPm.getPersister(LineitemDbPersister.TYPE);
	linePersister.persist(lineitem);

	OutcomeDefinitionDbPersister ocdPersister = (OutcomeDefinitionDbPersister) bbPm
		.getPersister(OutcomeDefinitionDbPersister.TYPE);
	OutcomeDefinition ocd = lineitem.getOutcomeDefinition();
	ocd.setCourseId(courseId);
	ocd.setPosition(1);

	OutcomeDefinitionScaleDbLoader ods2Loader = (OutcomeDefinitionScaleDbLoader) bbPm
		.getLoader(OutcomeDefinitionScaleDbLoader.TYPE);
	OutcomeDefinitionScaleDbPersister uutcomeDefinitionScaleDbPersister = (OutcomeDefinitionScaleDbPersister) bbPm
		.getPersister(OutcomeDefinitionScaleDbPersister.TYPE);

	boolean hasLessonScoreOutputs = LineitemUtil.hasLessonScoreOutputs(ctx, bbContent);
	OutcomeDefinitionScale ods;
	if (hasLessonScoreOutputs) {
	    ods = ods2Loader.loadByCourseIdAndTitle(courseId, OutcomeDefinitionScale.SCORE);
	    ods.setNumericScale(true);
	    ods.setPercentageScale(true);
	    ocd.setScorable(true);
	} else {
	    ods = ods2Loader.loadByCourseIdAndTitle(courseId, OutcomeDefinitionScale.COMPLETE_INCOMPLETE);
	    ods.setNumericScale(false);
	    ocd.setScorable(false);
	}
	uutcomeDefinitionScaleDbPersister.persist(ods);
	ocd.setScale(ods);
	ocdPersister.persist(ocd);
	
	//Construct bbContent id
	PkId bbContentPkId = (PkId) bbContent.getId();
	String bbContentId = "_" + bbContentPkId.getPk1() + "_" + bbContentPkId.getPk2();
	//Construct lineitem id
	PkId lineitemPkId = (PkId) lineitem.getId();
	String lineitemId = "_" + lineitemPkId.getPk1() + "_" + lineitemPkId.getPk2();
	
	//Store lineitemid to the storage bbContentId -> lineitemid)
	PortalExtraInfo pei = PortalUtil.loadPortalExtraInfo(null, null, "LamsLineitemStorage");
	ExtraInfo ei = pei.getExtraInfo();
	ei.setValue(bbContentId, lineitemId);
	PortalUtil.savePortalExtraInfo(pei);
    }
    
    /**
     * Checks whether lesson has scorable outputs (i.e. MCQ or Assessment activity).
     * 
     * @param ctx
     *            the blackboard contect, contains session data
     * @return a url pointing to the LAMS lesson, monitor, author session
     * @throws IOException 
     */
    private static boolean hasLessonScoreOutputs(Context ctx, Content bbContent) throws IOException {
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
	
	String learningDesignSvgUrl = LamsSecurityUtil.generateRequestLearningDesignImage(ctx, true) + "&ldId=" + ldId.trim();

	URL url = new URL(learningDesignSvgUrl);
	URLConnection conn = url.openConnection();
	if (!(conn instanceof HttpURLConnection)) {
	    throw new RuntimeException("Unable to open connection to: " + learningDesignSvgUrl);
	}

	HttpURLConnection httpConn = (HttpURLConnection) conn;

	if (httpConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
	    throw new RuntimeException("HTTP Response Code: " + httpConn.getResponseCode()
		    + ", HTTP Response Message: " + httpConn.getResponseMessage());
	}

	// InputStream is = url.openConnection().getInputStream();
	InputStream is = conn.getInputStream();

	// parse xml response
	String learningDesignSvg = IOUtils.toString(is, "UTF-8");
	boolean hasLessonScoreOutputs = (learningDesignSvg.indexOf("icon_mcq.png") != -1) || (learningDesignSvg.indexOf("icon_assessment.png") != -1);

	return hasLessonScoreOutputs;
    }
    
    public static void removeLineitem(String bbContentId, String courseIdStr) throws PersistenceException, ServletException {
	BbPersistenceManager bbPm = BbServiceManager.getPersistenceService().getDbPersistenceManager();
	
        Container bbContainer = bbPm.getContainer();
        Id contentId = new PkId( bbContainer, CourseDocument.DATA_TYPE, bbContentId );
        ContentDbLoader courseDocumentLoader = (ContentDbLoader) bbPm.getLoader( ContentDbLoader.TYPE );
        Content bbContent = (Content)courseDocumentLoader.loadById( contentId );
        //check isGradecenter option is ON 
        if (!bbContent.getIsDescribed()) {//(isDescribed field is used for storing isGradecenter parameter)
            return;
        }
        
        Id lineitemId = getLineitem(bbContentId, courseIdStr);
	LineitemDbPersister linePersister = (LineitemDbPersister) bbPm.getPersister(LineitemDbPersister.TYPE);
	linePersister.deleteById(lineitemId);

    }
    
    public static void changeLineitemName(String bbContentId, String courseIdStr, String newLineitemName)
	    throws PersistenceException, ServletException, ValidationException {
	BbPersistenceManager bbPm = BbServiceManager.getPersistenceService().getDbPersistenceManager();

	Id lineitemId = getLineitem(bbContentId, courseIdStr);

	LineitemDbLoader lineitemLoader = (LineitemDbLoader) bbPm.getLoader(LineitemDbLoader.TYPE);
	Lineitem lineitem = (Lineitem) lineitemLoader.loadById(lineitemId);
	lineitem.setName(newLineitemName);

	LineitemDbPersister linePersister = (LineitemDbPersister) bbPm.getPersister(LineitemDbPersister.TYPE);
	linePersister.persist(lineitem);
    }
    
    /**
     * Gets existing lineitem object.
     * @throws ServletException 
     * @throws PersistenceException 
     */
    private static Id getLineitem(String bbContentId, String courseIdStr) throws ServletException, PersistenceException {
	BbPersistenceManager bbPm = BbServiceManager.getPersistenceService().getDbPersistenceManager();
	
	//get lineitemid from the storage (bbContentId -> lineitemid)
	PortalExtraInfo pei = PortalUtil.loadPortalExtraInfo(null, null, "LamsLineitemStorage");
	ExtraInfo ei = pei.getExtraInfo();
	String lineitemIdStr = ei.getValue(bbContentId);
	
	//TODO remove the following paragraph after a while. (It removes lineitems created in versions after 1.2 and before 1.2.3)
	if (lineitemIdStr == null) {
	    // get stored bbContentId -> lamsLessonId.
	    PortalExtraInfo portalExtraInfo = PortalUtil.loadPortalExtraInfo(null, null, "LamsStorage");
	    ExtraInfo extraInfo = portalExtraInfo.getExtraInfo();
	    String lamsLessonId = extraInfo.getValue(bbContentId);

	    if (lamsLessonId == null || "".equals(lamsLessonId)) {
		throw new ServletException(
			"LamsLessonId not found in PortalExtraInfo LamsStorage which corresponds to bbContentId:"
				+ bbContentId);
	    }

	    // search for appropriate lineitem
	    Id courseId = bbPm.generateId(Course.DATA_TYPE, courseIdStr);
	    LineitemDbLoader lineitemLoader = LineitemDbLoader.Default.getInstance();
	    List<Lineitem> lineitems = lineitemLoader.loadByCourseId(courseId);

	    Lineitem lineitem = null;
	    for (Lineitem lineitemIter : lineitems) {
		if (lineitemIter.getAssessmentId() != null && lineitemIter.getAssessmentId().equals(lamsLessonId)) {
		    lineitem = lineitemIter;
		    break;
		}
	    }

	    if (lineitem == null) {
		throw new ServletException("Lineitem that corresponds to bbContentId: " + bbContentId + " was not found");
	    }

	    // delete lineitem (can't delete it simply doing linePersister.deleteById(lineitem.getId()) due to BB9 bug)
	    PkId lineitemPkId = (PkId) lineitem.getId();
	    lineitemIdStr = "_" + lineitemPkId.getPk1() + "_" + lineitemPkId.getPk2();
	}
	
	if (lineitemIdStr == null) {
	    throw new ServletException("Lineitem was not found for contentId:" + bbContentId
		    + ". This is despite the fact that isGradecenter option is ON.");
	}
	
	Id lineitemId = bbPm.generateId(Lineitem.LINEITEM_DATA_TYPE, lineitemIdStr.trim());
	return lineitemId;
    }
}
