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


package org.lamsfoundation.lams.learning.export.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.learning.export.ActivityPortfolio;
import org.lamsfoundation.lams.learning.export.ExportPortfolioConstants;
import org.lamsfoundation.lams.learning.export.ExportPortfolioException;
import org.lamsfoundation.lams.learning.export.NotebookPortfolio;
import org.lamsfoundation.lams.learning.export.Portfolio;
import org.lamsfoundation.lams.learning.service.ICoreLearnerService;
import org.lamsfoundation.lams.learningdesign.Competence;
import org.lamsfoundation.lams.learningdesign.dao.IActivityDAO;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.dao.ILessonDAO;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
import org.lamsfoundation.lams.themes.Theme;
import org.lamsfoundation.lams.themes.service.IThemeService;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.exception.LamsToolServiceException;
import org.lamsfoundation.lams.tool.service.ILamsCoreToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.util.CSSThemeUtil;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.FileUtilException;
import org.lamsfoundation.lams.util.HttpUrlConnectionUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.zipfile.ZipFileUtil;
import org.lamsfoundation.lams.util.zipfile.ZipFileUtilException;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * Generates the learner and teacher portfolios. These are designed to be offline versions of a sequence, so that
 * learners and teachers can refer to them after the lesson has finished.
 *
 * In general, anything that the learner sees should be in their portfolio. The teacher version should have the details
 * for all students in the lesson.
 *
 * @author mtruong,fmalikoff
 *
 */
public class ExportPortfolioService implements IExportPortfolioService {

    private static Logger log = Logger.getLogger(ExportPortfolioService.class);

    private ILamsCoreToolService lamsCoreToolService;
    private ICoreNotebookService coreNotebookService;
    private IActivityDAO activityDAO;
    private ICoreLearnerService learnerService;
    private IBaseDAO baseDAO;
    private ILessonDAO lessonDAO;
    protected MessageService messageService;
    private IThemeService themeService;

    /**
     * @param learnerService
     *            The learnerService to set.
     */
    public void setLearnerService(ICoreLearnerService learnerService) {
	this.learnerService = learnerService;
    }

    /**
     * @param learnerService
     *            The learnerService to set.
     */
    public void setCoreNotebookService(ICoreNotebookService coreNotebookService) {
	this.coreNotebookService = coreNotebookService;
    }

    /**
     * @param activityDAO
     *            The activityDAO to set.
     */
    public void setActivityDAO(IActivityDAO activityDAO) {
	this.activityDAO = activityDAO;
    }

    /**
     * @param lessonDAO
     *            The lessonDAO to set.
     */
    public void setLessonDAO(ILessonDAO lessonDAO) {
	this.lessonDAO = lessonDAO;
    }

    public void setBaseDAO(IBaseDAO baseDAO) {
	this.baseDAO = baseDAO;
    }

    /**
     * @param lamsCoreToolService
     *            The lamsCoreToolService to set.
     */
    public void setLamsCoreToolService(ILamsCoreToolService lamsCoreToolService) {
	this.lamsCoreToolService = lamsCoreToolService;
    }

    public void setMessageService(MessageService messageService) {
	this.messageService = messageService;
    }

    public void setThemeService(IThemeService themeService) {
	this.themeService = themeService;
    }

    /** @see org.lamsfoundation.lams.learning.export.service.IExportPortfolioService#exportPortfolioForTeacher(org.lamsfoundation.lams.lesson.Lesson) */
    @Override
    public Portfolio exportPortfolioForTeacher(Long lessonId, Cookie[] cookies) {
	Lesson lesson = lessonDAO.getLesson(lessonId);

	ArrayList<ActivityPortfolio> portfolios = null; // each portfolio contains information about its activity,
	// ordered in the same sequence as the ordered activity list.
	Portfolio exports = null;

	if (lesson != null) {
	    try {
		PortfolioBuilder builder = new PortfolioBuilder(lesson.getLearningDesign(), activityDAO,
			lamsCoreToolService, coreNotebookService, ToolAccessMode.TEACHER, lesson, null, null);
		builder.parseLearningDesign();
		portfolios = builder.getPortfolioList();

		exports = doExport(portfolios, null, cookies, lesson);

	    } catch (LamsToolServiceException e) {
		/** TODO avoid throwing exceptions if possible */
		throw new ExportPortfolioException(
			"An exception has occurred while generating portfolios. The error is: " + e);
	    }
	} else {
	    String error = "The Lesson with lessonID " + lessonId + "is null.";
	    ExportPortfolioService.log.error(error);
	    /*
	     * Instead of throwing an exception, create the Portfolio object with the ToolPortfolio[] set to null. Set
	     * the value of exportTmpDir using createDirectory() method, so that the main file can be generated in that
	     * directory.
	     */
	    exports = createPortfolioIndicatingErrorHasOccurred(lesson);
	}

	return exports;

    }

    /**
     * @see org.lamsfoundation.lams.learning.export.service.IExportPortfolioService#exportPortfolioForStudent(java.lang.Long,
     *      org.lamsfoundation.lams.usermanagement.User,boolean)
     */
    @Override
    public Portfolio exportPortfolioForStudent(Integer userId, Long lessonID, boolean anonymity,
	    ToolAccessMode accessMode, Cookie[] cookies) {
	ArrayList<ActivityPortfolio> portfolios = null;
	ArrayList<NotebookPortfolio> notes = null;
	Portfolio exports = null;

	User learner = (User) baseDAO.find(User.class, userId);
	Lesson lesson = lessonDAO.getLesson(lessonID);

	if (learner != null) {
	    LearnerProgress learnerProgress = learnerService.getProgress(learner.getUserId(), lessonID);

	    if (learnerProgress != null) {

		try {

		    PortfolioBuilder builder = new PortfolioBuilder(lesson.getLearningDesign(), activityDAO,
			    lamsCoreToolService, coreNotebookService, ToolAccessMode.LEARNER, lesson, learnerProgress,
			    learner);

		    builder.parseLearningDesign();
		    builder.processNotebook(accessMode);

		    portfolios = builder.getPortfolioList();
		    notes = builder.getNotebookList();

		    if (portfolios.size() >= 0) {
			exports = doExport(portfolios, notes, cookies, lesson);
			exports.setLearnerName(
				learner.getFirstName() + " " + learner.getLastName() + " (" + learner.getLogin() + ")");
		    } else {
			ExportPortfolioService.log.error("The learner has not completed or attempted any activities");
		    }

		} catch (LamsToolServiceException e) {
		    ExportPortfolioService.log.error("An exception has occurred while generating portfolios.", e);
		}

	    } else {
		ExportPortfolioService.log.error("The LearnerProgress cannot be found for userId " + userId
			+ " participating in lessonId " + lessonID);
	    }
	} else {
	    ExportPortfolioService.log.error("The User object with userId" + userId + "or Lesson object with lessonId"
		    + lessonID + " is null. Cannot Continue");
	}

	if (exports == null) {
	    exports = createPortfolioIndicatingErrorHasOccurred(lesson);
	}

	return exports;

    }

    private Portfolio createPortfolioIndicatingErrorHasOccurred(Lesson lesson) {
	String tempDir = createDirectory(ExportPortfolioConstants.DIR_SUFFIX_EXPORT);
	File dir = new File(tempDir);
	String exportID = dir.getName();

	Portfolio dummyPortfolio = new Portfolio(exportID);
	dummyPortfolio.setExportTmpDir(tempDir);
	dummyPortfolio.setLessonDescription(lesson.getLessonDescription());
	dummyPortfolio.setLessonName(lesson.getLessonName());
	return dummyPortfolio;
    }

    /** @see org.lamsfoundation.lams.learning.export.service.IExportPortfolioService#zipPortfolio(String, String) */
    @Override
    public String zipPortfolio(String filename, String directoryToZip) {
	String zipfileName, dirToPutZip;
	// create tmp dir to put zip file

	try {
	    dirToPutZip = FileUtil.createTempDirectory(ExportPortfolioConstants.DIR_SUFFIX_ZIP);
	    zipfileName = ZipFileUtil.createZipFile(filename, directoryToZip, dirToPutZip);
	} catch (FileUtilException e) {
	    throw new ExportPortfolioException("Cannot create the temporary directory for this export", e);
	} catch (ZipFileUtilException e) {
	    throw new ExportPortfolioException("An error has occurred while zipping up the directory ", e);
	}
	int index = dirToPutZip.lastIndexOf(File.separator);
	return index > -1 ? dirToPutZip.substring(index + 1) : dirToPutZip;
    }

    /** @see org.lamsfoundation.lams.learning.export.service.IExportPortfolioService#doExport(Vector, Cookie[]) */
    public Portfolio doExport(ArrayList<ActivityPortfolio> portfolios, ArrayList<NotebookPortfolio> notes,
	    Cookie[] cookies, Lesson lesson) {
	String tempDirectoryName;

	// create the root directory for the export
	tempDirectoryName = createDirectory(ExportPortfolioConstants.DIR_SUFFIX_EXPORT);
	File dir = new File(tempDirectoryName);
	String exportID = dir.getName();

	Portfolio portfolio = new Portfolio(exportID);
	portfolio.setExportTmpDir(tempDirectoryName);
	portfolio.setContentFolderID(lesson.getLearningDesign().getContentFolderID());
	portfolio.setLessonName(lesson.getLessonName());
	portfolio.setLessonDescription(lesson.getLessonDescription());
	portfolio.setLessonStartDate(lesson.getStartDateTime());

	if (portfolios != null) {
	    processPortfolios(portfolios, cookies, tempDirectoryName);
	    portfolio.setActivityPortfolios(portfolios.toArray(new ActivityPortfolio[portfolios.size()]));
	}

	if (notes != null) {
	    processNotes(notes, tempDirectoryName, portfolio);
	    portfolio.setNotebookPortfolios(notes.toArray(new NotebookPortfolio[notes.size()]));
	}

	Set<Competence> competences = lesson.getLearningDesign().getCompetences();
	if (competences != null) {
	    portfolio.getCompetencesDefined().addAll(competences);
	}

	return portfolio;

    }

    private void processPortfolios(List<ActivityPortfolio> portfolios, Cookie[] cookies, String tempDirectoryName) {

	Iterator<ActivityPortfolio> i = portfolios.iterator();
	// iterate through the list of portfolios, create subdirectory,
	while (i.hasNext()) {
	    ActivityPortfolio activityPortfolio = i.next();

	    // create a subdirectory with the name ActivityXX where XX is the activityId
	    String subDirectoryName = ExportPortfolioConstants.SUBDIRECTORY_BASENAME
		    + activityPortfolio.getActivityId().toString();
	    if (!createSubDirectory(tempDirectoryName, subDirectoryName)) {
		throw new ExportPortfolioException("The subdirectory " + subDirectoryName + " could not be created.");
	    }

	    // The directory in which the activity must place its files
	    String activitySubDirectory = tempDirectoryName + File.separator + subDirectoryName;

	    // for security reasons, append the relative directory name to the end of the export url instead of the
	    // whole path
	    String relativePath = activitySubDirectory.substring(FileUtil.getTempDir().length() + 1,
		    activitySubDirectory.length());

	    // Some activities (parallel, optional, sequence) don't have export urls.
	    if (!activityPortfolio.isHeadingNoPage()) {
		activityPortfolio
			.setExportUrl(HttpUrlConnectionUtil.getLamsLocalAddress() + activityPortfolio.getExportUrl());
		activityPortfolio.setExportUrl(WebUtil.appendParameterToURL(activityPortfolio.getExportUrl(),
			AttributeNames.PARAM_DIRECTORY_NAME, relativePath));

		// get tool to export its files, mainFileName is the name of the main HTML page that the tool exported.
		String mainFileName = connectToToolViaExportURL(activityPortfolio.getActivityName(),
			activityPortfolio.getExportUrl(), cookies, activitySubDirectory);

		// toolLink is used in main page, so that it can link with the tools export pages.
		String toolLink = subDirectoryName + "/" + mainFileName;
		activityPortfolio.setToolLink(toolLink);
	    }

	    if ((activityPortfolio.getChildPortfolios() != null)
		    && (activityPortfolio.getChildPortfolios().size() > 0)) {
		processPortfolios(activityPortfolio.getChildPortfolios(), cookies, tempDirectoryName);
	    }
	}

    }

    private void processNotes(List<NotebookPortfolio> notes, String tempDirectoryName, Portfolio portfolio) {

	if (notes.size() > 0) {

	    // create a subdirectory with the name Notebook
	    String subDirectoryName = ExportPortfolioConstants.SUBDIRECTORY_NOTEBOOK_BASENAME;

	    if (!createSubDirectory(tempDirectoryName, subDirectoryName)) {
		throw new ExportPortfolioException("The subdirectory " + subDirectoryName + " could not be created.");
	    } else {
		File dir = new File(tempDirectoryName, subDirectoryName);
		portfolio.setNotebookDir(dir.getAbsolutePath());
	    }

	    String mainFileName = ExportPortfolioConstants.MAIN_NOTEBOOK_FILENAME;

	    // notebookLink is used in main page, so that it can link with the tools export pages.
	    String notebookLink = subDirectoryName + "/" + mainFileName;
	    portfolio.setNotebookLink(notebookLink);

	}
    }

    /**
     * Helper method which calls the FileUtil to create a subdirectory. This method might not be needed, can be used why
     * calling the FileUtil directly.
     *
     * @param parentDir
     *            The name of the parent directory
     * @param subDir
     *            The name of the child directory to create.
     * @return true is the subdirectory was created, false otherwise
     */
    private boolean createSubDirectory(String parentDir, String subDir) {
	boolean created;
	try {
	    created = FileUtil.createDirectory(parentDir, subDir);

	} catch (FileUtilException e) {
	    /** TODO avoid throwing exceptions if possible */
	    throw new ExportPortfolioException("An error has occurred while creating the sub directory " + subDir);
	}

	return created;
    }

    /**
     * Helper method which calls the FileUtil to create directory. It will check whether the directory exists, if so,
     * then it will overwrite the existing directory. (It is assumed that the cron jobs should delete the directory +
     * files anyway, after the export is done)
     *
     * @param parentDir
     *            The name of the parent directory
     * @param subDir
     *            The name of the child directory to create.
     * @return true is the subdirectory was created, false otherwise
     */
    private String createDirectory(String name) {
	String tmpDir = null;
	try {
	    tmpDir = FileUtil.createTempDirectory(name);
	} catch (FileUtilException e) {
	    throw new ExportPortfolioException("Unable to create temporary directory " + name + " for export.", e);
	}
	return tmpDir;
    }

    /** @see org.lamsfoundation.lams.learning.export.service.IExportPortfolioService#exportToolPortfolio(org.lamsfoundation.lams.learning.export.Portfolio) */
    public String connectToToolViaExportURL(String activityName, String exportURL, Cookie[] cookies,
	    String directoryToStoreErrorFile) {
	String filename = null;
	try {
	    if (exportURL != null) {
		filename = HttpUrlConnectionUtil.connectToToolExportURL(exportURL, cookies);
	    }
	    if (filename == null) {
		filename = ExportPortfolioConstants.EXPORT_ERROR_FILENAME;
		ExportPortfolioService.log.error(
			"A problem has occurred while connecting to the tool's export url. The export url may be invalid or may not exist");
		writeErrorMessageToFile(directoryToStoreErrorFile, activityName);
	    }

	} catch (MalformedURLException e) {
	    ExportPortfolioService.log.error("The URL " + exportURL + " given is invalid.", e);
	    writeErrorMessageToFile(directoryToStoreErrorFile, activityName);
	} catch (FileNotFoundException e) {
	    ExportPortfolioService.log.error("The directory " + directoryToStoreErrorFile + " may not exist.", e);
	    writeErrorMessageToFile(directoryToStoreErrorFile, activityName);
	} catch (IOException e) {
	    ExportPortfolioService.log
		    .error("A problem has occurred while writing the contents of " + exportURL + " to file.", e);
	    writeErrorMessageToFile(directoryToStoreErrorFile, activityName);

	}

	return filename;
    }

    /** Generate the main page, given this portfolio */
    @Override
    public void generateMainPage(HttpServletRequest request, Portfolio portfolio, Cookie[] cookies) {
	String url = HttpUrlConnectionUtil.getLamsLocalAddress() + "learning/exportPortfolio/main.jsp";

	String filename = ExportPortfolioConstants.MAIN_EXPORT_FILENAME;
	try {
	    request.getSession().setAttribute("portfolio", portfolio);
	    HttpUrlConnectionUtil.writeResponseToFile(url, portfolio.getExportTmpDir(), filename, cookies);
	    request.getSession().removeAttribute("portfolio");
	} catch (MalformedURLException e) {
	    ExportPortfolioService.log.error("The URL given is invalid. Exception Message was: " + e);
	} catch (FileNotFoundException e) {
	    ExportPortfolioService.log.error("The directory or file may not exist. Exception Message was: " + e);
	} catch (IOException e) {
	    ExportPortfolioService.log.error("A problem has occurred while writing the contents of " + url
		    + " to file. Exception Message was: " + e);
	}
    }

    /** Generate the main page, given this portfolio */
    @Override
    public void generateNotebookPage(HttpServletRequest request, Portfolio portfolio, Cookie[] cookies) {
	String url = HttpUrlConnectionUtil.getLamsLocalAddress() + "learning/exportPortfolio/notebook.jsp";

	String filename = ExportPortfolioConstants.MAIN_NOTEBOOK_FILENAME;
	try {
	    request.getSession().setAttribute("portfolio", portfolio);
	    HttpUrlConnectionUtil.writeResponseToFile(url, portfolio.getNotebookDir(), filename, cookies);
	    request.getSession().removeAttribute("portfolio");
	} catch (MalformedURLException e) {
	    ExportPortfolioService.log.error("The URL given is invalid. Exception Message was: " + e);
	} catch (FileNotFoundException e) {
	    ExportPortfolioService.log.error("The directory or file may not exist. Exception Message was: " + e);
	} catch (IOException e) {
	    ExportPortfolioService.log.error("A problem has occurred while writing the contents of " + url
		    + " to file. Exception Message was: " + e);
	}
    }

    private void writeErrorMessageToFile(String directoryToStoreFile, String activityTitle) {
	try {
	    String errorMessage = messageService.getMessage(ExportPortfolioConstants.EXPORT_ACTIVITY_ERROR_KEY,
		    new Object[] { activityTitle });
	    String filepath = directoryToStoreFile + File.separator + ExportPortfolioConstants.EXPORT_ERROR_FILENAME;
	    BufferedWriter fileout = new BufferedWriter(new FileWriter(filepath));
	    fileout.write(errorMessage);
	    fileout.close();
	} catch (IOException e) {
	    ExportPortfolioService.log.error("Exception occured trying to write out error message to file.", e);
	}
    }

    /**
     * Gets the themes for the current user. This is used to determine the stylesheets included in the export file. We
     * need the full theme, not just the name, so we can get the directory names for the images.
     */
    @Override
    public Collection<Theme> getUserThemes() {
	List<String> themeNames = CSSThemeUtil.getAllUserThemes();
	Set<Theme> userThemes = new HashSet<Theme>();
	for (String themeName : themeNames) {
	    Theme theme = themeService.getTheme(themeName);
	    if (theme != null) {
		userThemes.add(theme);
	    }
	}
	return userThemes;
    }

}
