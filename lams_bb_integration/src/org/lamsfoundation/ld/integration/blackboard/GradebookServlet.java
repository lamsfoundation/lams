/**
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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
 */
package org.lamsfoundation.ld.integration.blackboard;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.lamsfoundation.ld.integration.Constants;
import org.lamsfoundation.ld.integration.util.LamsPluginUtil;
import org.lamsfoundation.ld.integration.util.LamsSecurityUtil;
import org.lamsfoundation.ld.integration.util.LineitemUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import blackboard.base.InitializationException;
import blackboard.data.ValidationException;
import blackboard.data.course.CourseMembership;
import blackboard.data.gradebook.Lineitem;
import blackboard.data.gradebook.Score;
import blackboard.data.user.User;
import blackboard.persist.Id;
import blackboard.persist.KeyNotFoundException;
import blackboard.persist.PersistenceException;
import blackboard.persist.course.CourseMembershipDbLoader;
import blackboard.persist.gradebook.ScoreDbLoader;
import blackboard.persist.user.UserDbLoader;
import blackboard.platform.BbServiceException;
import blackboard.platform.BbServiceManager;
import blackboard.platform.context.ContextManager;

/**
 * Deals with Blackboard Grade Center.
 */
public class GradebookServlet extends HttpServlet {

    private static final long serialVersionUID = -3587062723412672084L;
    private static Logger logger = Logger.getLogger(GradebookServlet.class);

    /**
     * Receives call from Lams ab lesson completion. After that get the latest marks for this user in this lesson and stores it in DB. 
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	ContextManager ctxMgr = null;
	
	try {
	    // get Blackboard context
	    ctxMgr = (ContextManager) BbServiceManager.lookupService(ContextManager.class);
	    UserDbLoader userLoader = UserDbLoader.Default.getInstance();
	    CourseMembershipDbLoader courseMembershipLoader = CourseMembershipDbLoader.Default.getInstance();
	    ScoreDbLoader scoreLoader = ScoreDbLoader.Default.getInstance();

	    // get Parameter values
	    String userName = request.getParameter(Constants.PARAM_USER_ID);
	    String timeStamp = request.getParameter(Constants.PARAM_TIMESTAMP);
	    String hash = request.getParameter(Constants.PARAM_HASH);
	    String lamsLessonIdParam = request.getParameter(Constants.PARAM_LESSON_ID);

	    // check parameters
	    if (userName == null || timeStamp == null || hash == null || lamsLessonIdParam == null) {
		response.sendError(HttpServletResponse.SC_BAD_REQUEST, "missing expected parameters");
		return;
	    }

	    //check user rights
	    String secretKey = LamsPluginUtil.getSecretKey();
	    String serverId = LamsPluginUtil.getServerId();
	    if (!LamsSecurityUtil.sha1(
		    timeStamp.toLowerCase() + userName.toLowerCase() + serverId.toLowerCase()
			    + secretKey.toLowerCase()).equals(hash)) {
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "authentication failed");
		return;
	    }

	    // get user list, but no role info since there are no course info
	    User user = userLoader.loadByUserName(userName);
	    if (user == null) {
		throw new ServletException("User not found with userName:" + userName);
	    }
	    Id userId = user.getId();

	    String serviceURL = LamsSecurityUtil.getServerAddress() + "/services/xml/LessonManager?"
		    + LamsSecurityUtil.generateAuthenticateParameters(userName)
		    + "&method=gradebookMarksUser"
		    + "&lsId=" + lamsLessonIdParam 
		    + "&outputsUser=" + URLEncoder.encode(userName, "UTF8");
	    
	    URL url = new URL(serviceURL);
	    URLConnection conn = url.openConnection();
	    if (!(conn instanceof HttpURLConnection)) {
		throw new RuntimeException("Unable to open connection to: " + serviceURL);
	    }

	    HttpURLConnection httpConn = (HttpURLConnection) conn;

	    if (httpConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
		String errorMsg = "HTTP Response Code: " + httpConn.getResponseCode() + ", HTTP Response Message: "
			+ httpConn.getResponseMessage();
		logger.error(errorMsg);
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, errorMsg);
		return;
	    }

	    // InputStream is = url.openConnection().getInputStream();
	    InputStream is = conn.getInputStream();

	    // parse xml response
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    DocumentBuilder db = dbf.newDocumentBuilder();
	    Document document = db.parse(is);
	    
	    Node lesson = document.getDocumentElement().getFirstChild();
	    Node learnerResult = lesson.getFirstChild();

	    Lineitem lineitem = LineitemUtil.getLineitem(userId, lamsLessonIdParam);
	    if (lineitem == null) {
		throw new ServletException("Lineitem was not found for userId:" + userId + " and lamsLessonId:" + lamsLessonIdParam);
	    }
	    // do not remove the following line: it's required to instantiate the object
	    logger.info("Record score for " + lineitem.getName() + " lesson. It now has " + lineitem.getScores().size()
		    + " scores.");

	    // store new score
	    CourseMembership courseMembership = courseMembershipLoader.loadByCourseAndUserId(lineitem.getCourseId(),
		    userId);
	    Score currentScore = null;
	    try {
		currentScore = scoreLoader.loadByCourseMembershipIdAndLineitemId(courseMembership.getId(),
			lineitem.getId());
	    } catch (KeyNotFoundException c) {
		currentScore = new Score();
		currentScore.setLineitemId(lineitem.getId());
		currentScore.setCourseMembershipId(courseMembership.getId());
	    }
	    
	    //updates and persists currentScore in the DB
	    LineitemUtil.updateScoreBasedOnLamsResponse(lesson, learnerResult, currentScore);
	    
	    // get LAMS server address
	    String serverUrlWithLamsWord = LamsSecurityUtil.getServerAddress();
	    URI uri = new URI(serverUrlWithLamsWord);
	    //strip out '/lams/' from the end of the URL
	    String serverUrl = serverUrlWithLamsWord.lastIndexOf(uri.getPath()) == -1 ? serverUrlWithLamsWord
		    : serverUrlWithLamsWord.substring(0, serverUrlWithLamsWord.lastIndexOf(uri.getPath()));
	    
	    //allow lessonComplete.jsp on LAMS side to make an Ajax call to this servlet
	    response.addHeader("Access-Control-Allow-Origin", serverUrl);
	    
	    //notifying LAMS that score has been stored successfully
	    response.setContentType("text/html");
	    PrintWriter out = response.getWriter();
	    out.write("OK");
	    
	    //the following paragraph is kept due to the Ernie's request to keep it for potential usage in the future
//	    NodeList activities = document.getDocumentElement().getFirstChild().getChildNodes();
//
//	    float maxResult = 0;
//	    float userResult = 0;
//	    for (int i = 0; i < activities.getLength(); i++) {
//		Node activity = activities.item(i);
//		
//		for (int j = 0; j < activity.getChildNodes().getLength(); j++) {
//		    
//		    Node toolOutput = activity.getChildNodes().item(j);
//		    String toolOutputName = toolOutput.getAttributes().getNamedItem("name").getNodeValue();
//		    // The only numeric outputs we get from LAMS are for the MCQ and Assessment activities
//		    // learner.total.score = Assessment 
//		    // learner.mark = MCQ
//		    if ("learner.mark".equals(toolOutputName) || "learner.total.score".equals(toolOutputName)) {
//			String userResultStr = toolOutput.getAttributes().getNamedItem("output").getNodeValue();
//			String maxResultStr = toolOutput.getAttributes().getNamedItem("marksPossible").getNodeValue();
//			
//			userResult += Float.parseFloat(userResultStr);
//			maxResult += Float.parseFloat(maxResultStr);
//		    }
//		}
//		
//	    }
	    
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
	} catch (URISyntaxException e) {
	    throw new ServletException(e);
	} catch (PersistenceException e) {
	    throw new ServletException(e);
	} catch (ValidationException e) {
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

    }

}
