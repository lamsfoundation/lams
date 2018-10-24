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


package org.lamsfoundation.bb.integration.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.lamsfoundation.bb.integration.Constants;
import org.lamsfoundation.bb.integration.util.LamsBuildingBlockException;
import org.lamsfoundation.bb.integration.util.LamsPluginUtil;
import org.lamsfoundation.bb.integration.util.LamsSecurityUtil;
import org.lamsfoundation.bb.integration.util.LineitemUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import blackboard.base.InitializationException;
import blackboard.data.course.CourseMembership;
import blackboard.data.gradebook.Lineitem;
import blackboard.data.gradebook.Score;
import blackboard.persist.Id;
import blackboard.persist.PersistenceException;
import blackboard.persist.course.CourseMembershipDbLoader;
import blackboard.persist.gradebook.ScoreDbLoader;
import blackboard.persist.gradebook.ScoreDbPersister;
import blackboard.platform.BbServiceException;
import blackboard.platform.BbServiceManager;
import blackboard.platform.context.Context;
import blackboard.platform.context.ContextManager;

/**
 * Fixes issues created by GradebookSyncServlet, namely, removes marks of users that had not completed the lesson. 
 */
@SuppressWarnings("serial")
public class GradebookSyncFixServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	ContextManager ctxMgr = null;
	int numberDeletedScores = 0;
	
	try {
	    // get Blackboard context
	    ctxMgr = (ContextManager) BbServiceManager.lookupService(ContextManager.class);
	    Context ctx = ctxMgr.setContext(request);
	    CourseMembershipDbLoader courseMemLoader = CourseMembershipDbLoader.Default.getInstance();
	    ScoreDbLoader scoreLoader = ScoreDbLoader.Default.getInstance();
	    ScoreDbPersister scorePersister = ScoreDbPersister.Default.getInstance();

	    // get Parameter values
	    String lamsLessonIdParam = request.getParameter(Constants.PARAM_LESSON_ID);
	    // validate method parameter
	    if (lamsLessonIdParam == null) {
		throw new RuntimeException("Requred parameters missing. lsid=" + lamsLessonIdParam);
	    }

	    Lineitem lineitem = LineitemUtil.getLineitem(ctx.getUserId(), lamsLessonIdParam, true);
	    if (lineitem == null) {
		throw new ServletException("Lineitem was not found for userId:" + ctx.getUserId() + " and lamsLessonId:" + lamsLessonIdParam);
	    }

	    String username = ctx.getUser().getUserName();
	    String serviceURL = LamsPluginUtil.getServerUrl() + "/services/xml/LessonManager?"
		    + LamsSecurityUtil.generateAuthenticateParameters(username)
		    + "&method=gradebookMarksLesson&lsId=" + lamsLessonIdParam;

	    URL url = new URL(serviceURL);
	    URLConnection conn = url.openConnection();
	    if (!(conn instanceof HttpURLConnection)) {
		throw new RuntimeException("Unable to open connection to: " + serviceURL);
	    }

	    HttpURLConnection httpConn = (HttpURLConnection) conn;

	    if (httpConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
		String errorMsg = "HTTP Response Code: " + httpConn.getResponseCode() + ", HTTP Response Message: "
			+ httpConn.getResponseMessage();
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.getWriter().write(errorMsg);
		return;
	    }

	    InputStream is = conn.getInputStream();

	    // parse xml response
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    DocumentBuilder db = dbf.newDocumentBuilder();
	    Document document = db.parse(is);
	    Node lesson = document.getDocumentElement().getFirstChild();
	    NodeList learnerResults = lesson.getChildNodes();
	    
	    //in order to reduce DB queries we get scores and courseMemberships all at once
	    List<Score> dbScores = scoreLoader.loadByLineitemId(lineitem.getId());
	    List<CourseMembership> courseMemberships = courseMemLoader.loadByCourseId(lineitem.getCourseId(), null, true);
	    
	    //removes marks of users that had not completed the lesson
	    for (CourseMembership courseMembership: courseMemberships) {
		Id courseMembershipId = courseMembership.getId();
		String userName = courseMembership.getUser().getUserName();
		
		// find old score
		Score currentScore = null;
		for (Score dbScore : dbScores) {
		    if (dbScore.getCourseMembershipId().equals(courseMembershipId)) {
			currentScore = dbScore;
			break;
		    }
		}
		
		// determine whether user had finished the lesson
		boolean isUserHadFinishedLesson = false;
		for (int i = 0; i < learnerResults.getLength(); i++) {
		    Node learnerResult = learnerResults.item(i);

		    String extUsername = learnerResult.getAttributes().getNamedItem("extUsername").getNodeValue();

		    if (userName.equals(extUsername)) {
			isUserHadFinishedLesson = true;
			break;
		    }
		}
		
		//remove marks of the user that had not completed the lesson.
		if ((currentScore != null) && !isUserHadFinishedLesson) {
		    scorePersister.deleteById(currentScore.getId());
		    
		    //calculate how many marks are removed
		    numberDeletedScores++;
		}
	    }

	    
	} catch (LamsBuildingBlockException e) {
	    response.setContentType("text/html");
	    PrintWriter out = response.getWriter();
	    out.write("Exception was thrown: " + e.getMessage());
	    return;
	
	} catch (MalformedURLException e) {
	    throw new ServletException("Unable to get LAMS learning designs, bad URL: "
		    + ", please check lams.properties", e);
	} catch (IllegalStateException e) {
	    throw new ServletException(
		    "LAMS Server timeout, did not get a response from the LAMS server. Please contact your systems administrator",
		    e);
	} catch (ConnectException e) {
	    throw new ServletException(
		    "LAMS Server timeout, did not get a response from the LAMS server. Please contact your systems administrator",
		    e);
	} catch (UnsupportedEncodingException e) {
	    throw new ServletException(e);
	} catch (IOException e) {
	    throw new ServletException(e);
	} catch (ParserConfigurationException e) {
	    throw new ServletException(e);
	} catch (SAXException e) {
	    throw new ServletException(e);
	} catch (PersistenceException e) {
	    throw new ServletException(e);
	} catch (InitializationException e) {
	    throw new ServletException(e);
	} catch (BbServiceException e) {
	    throw new ServletException(e);
	} finally {
	    // make sure context is released
	    if (ctxMgr != null) {
		ctxMgr.releaseContext();
	    }
	}
	
	response.setContentType("text/html");
	PrintWriter out = response.getWriter();
        out.write("Complete! " + numberDeletedScores + " marks have been removed from Blackboard Gradecenter.");
	
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	doGet(req, resp);
    }

}
