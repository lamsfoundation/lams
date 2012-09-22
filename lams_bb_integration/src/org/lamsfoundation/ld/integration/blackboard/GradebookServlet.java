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
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;
import org.lamsfoundation.ld.integration.Constants;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import blackboard.data.course.Course;
import blackboard.data.course.CourseMembership;
import blackboard.data.gradebook.Lineitem;
import blackboard.data.gradebook.Score;
import blackboard.data.gradebook.impl.Outcome;
import blackboard.data.gradebook.impl.OutcomeDefinition.CalculationType;
import blackboard.data.gradebook.impl.OutcomeDefinitionCategory;
import blackboard.data.gradebook.impl.OutcomeDefinitionScale;
import blackboard.data.user.User;
import blackboard.persist.BbPersistenceManager;
import blackboard.persist.KeyNotFoundException;
import blackboard.persist.PersistenceException;
import blackboard.persist.PkId;
import blackboard.persist.course.CourseDbLoader;
import blackboard.persist.course.CourseMembershipDbLoader;
import blackboard.persist.gradebook.LineitemDbLoader;
import blackboard.persist.gradebook.LineitemDbPersister;
import blackboard.persist.gradebook.ScoreDbLoader;
import blackboard.persist.gradebook.ScoreDbPersister;
import blackboard.persist.user.UserDbLoader;
import blackboard.platform.BbServiceManager;
import blackboard.platform.context.Context;
import blackboard.platform.context.ContextManager;
import blackboard.portal.data.ExtraInfo;
import blackboard.portal.data.PortalExtraInfo;
import blackboard.portal.servlet.PortalUtil;

/**
 * Deals with Blackboard Grade Center.
 */
public class GradebookServlet extends HttpServlet {

    private static final long serialVersionUID = -3587062723412672084L;
    static Logger logger = Logger.getLogger(GradebookServlet.class);

    /**
     * Receives call from Lams ab lesson completion. After that get the latest marks for this user in this lesson and stores it in DB. <br>
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	ContextManager ctxMgr = null;
	 String errorMsg = "error";
	
	try {
	    // get Blackboard context
	    ctxMgr = (ContextManager) BbServiceManager.lookupService(ContextManager.class);
	    Context ctx = ctxMgr.setContext(request);

	    // get Parameter values
	    String userName = request.getParameter(Constants.PARAM_USER_ID);
	    String timeStamp = request.getParameter(Constants.PARAM_TIMESTAMP);
	    String hash = request.getParameter(Constants.PARAM_HASH);
	    String lessonIdStr = request.getParameter(Constants.PARAM_LESSON_ID);

	    // check paramaeters
	    if (userName == null || timeStamp == null || hash == null) {
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "missing expected parameters");
		return;
	    }

	    String secretKey = LamsPluginUtil.getSecretKey();
	    String serverId = LamsPluginUtil.getServerId();

	    if (!sha1(
		    timeStamp.toLowerCase() + userName.toLowerCase() + serverId.toLowerCase()
			    + secretKey.toLowerCase()).equals(hash)) {
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "authentication failed");
	    }

	    // get the persistence manager
	    BbPersistenceManager bbPm = BbServiceManager.getPersistenceService().getDbPersistenceManager();

	    // get user list, but no role info since there are no course info
	    UserDbLoader userLoader = (UserDbLoader) bbPm.getLoader(UserDbLoader.TYPE);
	    User user = userLoader.loadByUserName(userName);

	    if (user == null) {
		throw new ServletException("user not found");
	    }

	    String serverAddr = LamsSecurityUtil.getServerAddress();

	    Document document = null;
	    String serviceURL = serverAddr + "/services/xml/LessonManager?"
		    + LamsSecurityUtil.generateAuthenticateParameters(ctx) + "&courseId="
		    + "&method=toolOutputsUser&lsId=" + lessonIdStr + "&outputsUser="
		    + URLEncoder.encode(userName, "UTF8");
	    
	    URL url = new URL(serviceURL);
	    URLConnection conn = url.openConnection();
	    if (!(conn instanceof HttpURLConnection)) {
		logger.error("Unable to open connection to: " + serviceURL);
	    }

	    HttpURLConnection httpConn = (HttpURLConnection) conn;

	    if (httpConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
		errorMsg = "HTTP Response Code: " + httpConn.getResponseCode() + ", HTTP Response Message: "
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
	    document = db.parse(is);
	    
	    
	    NodeList activities = document.getDocumentElement().getFirstChild().getChildNodes();

	    float maxResult = 0;
	    float userResult = 0;
	    for (int i = 0; i < activities.getLength(); i++) {
		Node activity = activities.item(i);
		
		for (int j = 0; j < activity.getChildNodes().getLength(); j++) {
		    
		    Node toolOutput = activity.getChildNodes().item(j);
		    String toolOutputName = toolOutput.getAttributes().getNamedItem("name").getNodeValue();
		    // The only numeric outputs we get from LAMS are for the MCQ and Assessment activities
		    // learner.total.score = Assessment 
		    // learner.mark = MCQ
		    if ("learner.mark".equals(toolOutputName) || "learner.total.score".equals(toolOutputName)) {
			String userResultStr = toolOutput.getAttributes().getNamedItem("output").getNodeValue();
			String maxResultStr = toolOutput.getAttributes().getNamedItem("marksPossible").getNodeValue();
			
			userResult += Float.parseFloat(userResultStr);
			maxResult += Float.parseFloat(maxResultStr);
		    }
		}
		
	    }
                
		
	    CourseDbLoader cLoader = CourseDbLoader.Default.getInstance();
	    LineitemDbLoader lineitemLoader = LineitemDbLoader.Default.getInstance();

	    List<Course> userCourses = cLoader.loadByUserId(ctx.getUserId());

	    // search for appropriate lineitem
	    Lineitem lineitem = null;
	    for (Course userCourse : userCourses) {
		List<Lineitem> lineitems = lineitemLoader.loadByCourseId(userCourse.getId());

		for (Lineitem lineitemIter : lineitems) {
		    if (lineitemIter.getAssessmentId() != null && lineitemIter.getAssessmentId().equals(lessonIdStr)) {
			lineitem = lineitemIter;
			break;
		    }
		}

	    }

	    if (lineitem == null) {
		throw new ServletException("lineitem not found");
	    }

	    // store new score
	    CourseMembershipDbLoader memLoader = (CourseMembershipDbLoader) bbPm
		    .getLoader(CourseMembershipDbLoader.TYPE);
	    ScoreDbLoader scoreLoader = (ScoreDbLoader) bbPm.getLoader(ScoreDbLoader.TYPE);
	    ScoreDbPersister scorePersister = (ScoreDbPersister) bbPm.getPersister(ScoreDbPersister.TYPE);
	    CourseMembership cms = memLoader.loadByCourseAndUserId(lineitem.getCourseId(), ctx.getUserId());
	    Score current_score = null;
	    try {
		current_score = scoreLoader.loadByCourseMembershipIdAndLineitemId(cms.getId(), lineitem.getId());
	    } catch (KeyNotFoundException c) {
		current_score = new Score();
		current_score.setLineitemId(lineitem.getId());
		current_score.setCourseMembershipId(cms.getId());
	    }

	    //set score grade. if Lams supplies one (and lineitem will have score type) we set score; otherwise (and lineitme of type Complete/Incomplete) we set 0
	    float gradebookMark = 0;
	    if (maxResult > 0) {
		gradebookMark = (userResult / maxResult) * Constants.GRADEBOOK_POINTS_POSSIBLE;
	    }
	    current_score.setGrade(new DecimalFormat("##.##").format(gradebookMark));
	    current_score.validate();
	    scorePersister.persist(current_score);

	    
	} catch (MalformedURLException e) {
	    errorMsg = "Unable to get LAMS learning designs, bad URL: " + ", please check lams.properties";
	    logger.error(errorMsg, e);
	    e.printStackTrace();
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST, errorMsg);
	} catch (IllegalStateException e) {
	    errorMsg = "LAMS Server timeout, did not get a response from the LAMS server. Please contact your systems administrator";
	    logger.error(errorMsg, e);
	    e.printStackTrace();
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST, errorMsg);
	} catch (ConnectException e) {
	    errorMsg = "LAMS Server timeout, did not get a response from the LAMS server. Please contact your systems administrator";
	    logger.error(errorMsg, e);
	    e.printStackTrace();
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST, errorMsg);
	} catch (UnsupportedEncodingException e) {
	    logger.error(e);
	    e.printStackTrace();
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST, errorMsg);
	} catch (IOException e) {
	    logger.error(e);
	    e.printStackTrace();
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST, errorMsg);
	} catch (ParserConfigurationException e) {
	    logger.error(e);
	    e.printStackTrace();
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST, errorMsg);
	} catch (SAXException e) {
	    logger.error(e);
	    e.printStackTrace();
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST, errorMsg);
	} catch (Exception e) {
	    e.printStackTrace();
	    logger.error("Problem with gradebook servlet: " + e.getMessage(), e);
	    logger.error(e.getStackTrace().toString());
	    throw new ServletException(e);
	} finally {
	    // make sure context is released
	    if (ctxMgr != null)
		ctxMgr.releaseContext();
	}
	
	response.setContentType("text/html");
	PrintWriter out = response.getWriter();
        out.write("OK");
    }

    private String sha1(String str) {
	try {
	    MessageDigest md = MessageDigest.getInstance("SHA1");
	    return new String(Hex.encodeHex(md.digest(str.getBytes())));
	} catch (NoSuchAlgorithmException e) {
	    throw new RuntimeException(e);
	}
    }

}
