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

/* $$Id$$ */
package org.lamsfoundation.lams.learning.export.web.action;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learning.export.ExportPortfolioConstants;
import org.lamsfoundation.lams.learning.export.ExportPortfolioException;
import org.lamsfoundation.lams.learning.export.Portfolio;
import org.lamsfoundation.lams.learning.export.service.ExportPortfolioServiceProxy;
import org.lamsfoundation.lams.learning.export.service.IExportPortfolioService;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.security.ISecurityService;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * @author mtruong
 * 
 *         MainExportServlet is responsible for calling the export service, which is responsible for calling all tools
 *         to do its export. The main page will be generated after all tools have completed its export. At the time of
 *         writing, the html for the main page is done manually. All of the outputs of the export will be zipped up and
 *         placed in the temporary export directory. The relative path of the file location of this zip file is
 *         returned.
 * 
 */
public class MainExportServlet extends HttpServlet {

    private static Logger log = Logger.getLogger(MainExportServlet.class);

    private static final long serialVersionUID = 7788509831929373666L;
    private String exportTmpDir;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
	    IOException {
	doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
	    IOException, ExportPortfolioException {
	Portfolio portfolios = null;
	String exportFilename = "";
	String role = WebUtil.readStrParam(request, AttributeNames.PARAM_ROLE, true);
	String mode = WebUtil.readStrParam(request, AttributeNames.PARAM_MODE);
	Long lessonID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID));
	String learnerContentFolder = FileUtil.getLearnerContentFolder(lessonID);
	Integer userIdParam = WebUtil.readIntParam(request, AttributeNames.PARAM_USER_ID, true);
	HttpSession session = SessionManager.getSession();
	UserDTO userDto = (UserDTO) session.getAttribute(AttributeNames.USER);
	Integer currentUserId = userDto.getUserID();

	Cookie[] cookies = request.getCookies();
	if (MainExportServlet.log.isDebugEnabled()) {
	    int numCookies = cookies != null ? cookies.length : 0;
	    MainExportServlet.log.debug("Export portfolio: mode " + mode + " # cookies "
		    + new Integer(numCookies).toString());
	}

	IExportPortfolioService exportService = ExportPortfolioServiceProxy.getExportPortfolioService(this
		.getServletContext());
	ILessonService lessonService = ExportPortfolioServiceProxy.getLessonService(this.getServletContext());
	ISecurityService securityService = ExportPortfolioServiceProxy.getSecurityService(this.getServletContext());
	Lesson lesson = lessonService.getLesson(lessonID);
	ToolAccessMode accessMode = ToolAccessMode.TEACHER.toString().equals(role) ? ToolAccessMode.TEACHER : null;

	if (mode.equals(ToolAccessMode.LEARNER.toString())) {
	    if (accessMode == null) {
		if (!lesson.getLearnerExportAvailable()) {
		    throw new ExportPortfolioException("Lesson with ID: " + lesson.getLessonId()
			    + " does not allow export portfolio for learners");
		}

		if (!securityService.isLessonLearner(lesson.getLessonId(), currentUserId, "export portfolio", false)) {
		    response.sendError(HttpServletResponse.SC_FORBIDDEN, "The user is not a learner in the lesson");
		    return;
		}
		LearnerProgress learnerProgress = lessonService.getUserProgressForLesson(currentUserId,
			lesson.getLessonId());
		if ((learnerProgress == null) || !learnerProgress.isComplete()) {
		    MainExportServlet.log.error("Learner with ID: " + currentUserId
			    + " has not finished lesson with ID: " + lesson.getLessonId());
		    response.sendError(HttpServletResponse.SC_FORBIDDEN, "The learner has not finished the lesson");
		    return;
		}
	    } else if (!securityService.isLessonMonitor(lesson.getLessonId(), currentUserId, "export portfolio", false)) {
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "The user is not a learner in the lesson");
		return;
	    }

	    portfolios = exportService.exportPortfolioForStudent(userIdParam == null ? currentUserId : userIdParam,
		    lessonID, true, accessMode, cookies);
	    String learnerName = portfolios.getLearnerName();
	    String learnerLogin = learnerName.substring(learnerName.indexOf('(') + 1, learnerName.lastIndexOf(')'));
	    exportFilename = ExportPortfolioConstants.EXPORT_LEARNER_PREFIX + " " + portfolios.getLessonName() + " "
		    + learnerLogin + ".zip";
	} else if (mode.equals(ToolAccessMode.TEACHER.toString())) {
	    if (!securityService.isLessonMonitor(lesson.getLessonId(), currentUserId, "export portfolio", false)) {
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "The user is not a monitor in the lesson");
		return;
	    }

	    // done in the monitoring environment
	    portfolios = exportService.exportPortfolioForTeacher(lessonID, cookies);
	    exportFilename = ExportPortfolioConstants.EXPORT_TEACHER_PREFIX + " " + portfolios.getLessonName() + ".zip";
	}

	if (portfolios != null) {
	    exportFilename = FileUtil.stripInvalidChars(exportFilename);
	    exportTmpDir = portfolios.getExportTmpDir();

	    exportService.generateMainPage(request, portfolios, cookies);

	    if (portfolios.getNotebookPortfolios() != null) {
		if (portfolios.getNotebookPortfolios().length > 0) {
		    exportService.generateNotebookPage(request, portfolios, cookies);
		}
	    }

	    // correct all image links in created htmls.
	    replaceImageFolderLinks(portfolios.getContentFolderID(), learnerContentFolder);

	    // bundle the stylesheet with the package
	    CSSBundler bundler = new CSSBundler(request, cookies, exportTmpDir, exportService.getUserThemes());
	    bundler.bundleStylesheet();

	    // bundle all user uploaded content and CKEditor smileys with the package
	    ImageBundler imageBundler = new ImageBundler(exportTmpDir, portfolios.getContentFolderID(),
		    learnerContentFolder);
	    imageBundler.bundleImages();

	    // zip up the contents of the temp export folder using a constant name
	    String exportZipDir = exportService.zipPortfolio(ExportPortfolioConstants.EXPORT_TEMP_FILENAME,
		    exportTmpDir);
	    /*
	     * -- Used for testing timeout change the export url in exportWaitingPage to -- String exportUrl =
	     * learning_root + "portfolioExport?" + request.getQueryString()+"&sleep=1800000"; -- to pause for 30 mins.
	     * Integer sleeptime = WebUtil.checkInteger("sleep", request.getParameter("sleep"), true); if ( sleeptime !=
	     * null ) { log.debug("Testing timeouts. Sleeping for "+sleeptime/1000+" seconds"); try {
	     * Thread.sleep(sleeptime); } catch (InterruptedException e) { log.error("Sleep interrupted",e); } }
	     */
	    // return the relative (to server's temp dir) filelocation of the
	    // zip file so that it can be picked up in exportWaitingPage.jsp
	    PrintWriter out = response.getWriter();
	    out.print(URLEncoder.encode(exportZipDir + File.separator + exportFilename, "UTF-8"));
	} else {
	    // redirect the request to another page.
	}

    }

    /**
     * Corrects links in all html files in temporary directory and its subdirectories.
     * 
     * @param contentFolderI
     *            32-character content folder name
     * @param learnerContentFolder
     *            runtime content folder
     */
    private void replaceImageFolderLinks(String contentFolderID, String learnerContentFolder) {
	File tempDir = new File(exportTmpDir);

	// finds all the html extension files
	Collection<File> jspFiles = FileUtils.listFiles(tempDir, new String[] { "html" }, true);

	// iterates thru the collection and sends this
	for (Iterator<File> it = jspFiles.iterator(); it.hasNext();) {
	    Object element = it.next();
	    MainExportServlet.log.debug("Correcting links in file " + element.toString());
	    replaceImageFolderLinks(element.toString(), contentFolderID, learnerContentFolder);
	}
    }

    /**
     * Corrects links in current particular html file.
     * 
     * @param filename
     *            filename
     * @param contentFolderID
     *            32-character content folder name
     * @param learnerContentFolder
     *            runtime content folder
     */
    private void replaceImageFolderLinks(String filename, String contentFolderID, String learnerContentFolder) {
	try {
	    // ((\\\\)?/)* stands for any number of slashes(/) or escaped slashes(\/, which was produced by running
	    // StringEscapeUtils.escapeJavaScript() beforehand)
	    String ckeditorpath = "((\\\\)?/)*lams((\\\\)?/)+www((\\\\)?/)+secure((\\\\)?/)+" + contentFolderID;
	    String ckeditorrecpath = "../" + contentFolderID + "/Recordings";
	    String ckeditorsmiley = "/lams/ckeditor/images/smiley";
	    String ckeditorvr = "/lams/ckeditor/plugins/videorecorder";
	    String learnerContentPath = "((\\\\)?/)+lams((\\\\)?/)+www((\\\\)?/)+secure((\\\\)?/)+"
		    + learnerContentFolder.replaceAll("/", "((\\\\\\\\)?/)+");

	    // Replacing string
	    String newckeditorpath = "../" + contentFolderID;
	    String newckeditorrecpath = "../../../../" + contentFolderID + "/Recordings";
	    String newckeditorsmiley = "../ckeditor/images/smiley";
	    String newckeditorvr = "../ckeditor/plugins/videorecorder";
	    String newLearnerContentPath = "../" + learnerContentFolder;
	    ;

	    File fin = new File(filename);
	    // Open and input stream
	    FileInputStream fis = new FileInputStream(fin);

	    BufferedReader in = new BufferedReader(new InputStreamReader(fis, "UTF-8"));

	    // The pattern matches control characters
	    Pattern p = Pattern.compile(ckeditorpath);
	    Matcher m = p.matcher("");

	    Pattern p2 = Pattern.compile(ckeditorrecpath);
	    Matcher m2 = p2.matcher("");

	    Pattern p3 = Pattern.compile(ckeditorsmiley);
	    Matcher m3 = p3.matcher("");

	    Pattern p4 = Pattern.compile(ckeditorvr);
	    Matcher m4 = p4.matcher("");

	    Pattern p5 = Pattern.compile(learnerContentPath);
	    Matcher m5 = p5.matcher("");

	    String aLine = null;
	    String output = "";

	    while ((aLine = in.readLine()) != null) {
		m.reset(aLine);

		// Replace the p matching pattern with the newckeditorpath
		String firstpass = m.replaceAll(newckeditorpath);

		// Replace the p2 matching patterns with the newckeditorrecpath
		m2.reset(firstpass);
		String secondpass = m2.replaceAll(newckeditorrecpath);

		// Replace the p2 matching patterns with the newckeditorsmiley
		m3.reset(secondpass);
		String thirdpass = m3.replaceAll(newckeditorsmiley);

		// Replace the p3 matching patterns with the newckeditorvr
		m4.reset(thirdpass);
		String forthpass = m4.replaceAll(newckeditorvr);

		// Replace the p4 matching patterns with the newLearnerContentPath
		m5.reset(forthpass);
		String result = m5.replaceAll(newLearnerContentPath);

		output = output + result + "\n";
	    }
	    in.close();

	    // open output file

	    File fout = new File(filename);
	    FileOutputStream fos = new FileOutputStream(fout);
	    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));

	    out.write(output);
	    out.newLine();
	    out.close();
	} catch (IOException e) {
	    MainExportServlet.log.error("Unable to correct imagefolder links in file " + filename, e);
	}
    }
}