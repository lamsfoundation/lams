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



package org.lamsfoundation.lams.tool.wiki.web.servlets;

import java.io.IOException;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.wiki.dto.WikiDTO;
import org.lamsfoundation.lams.tool.wiki.dto.WikiPageContentDTO;
import org.lamsfoundation.lams.tool.wiki.dto.WikiPageDTO;
import org.lamsfoundation.lams.tool.wiki.dto.WikiSessionDTO;
import org.lamsfoundation.lams.tool.wiki.dto.WikiUserDTO;
import org.lamsfoundation.lams.tool.wiki.model.Wiki;
import org.lamsfoundation.lams.tool.wiki.model.WikiPage;
import org.lamsfoundation.lams.tool.wiki.model.WikiSession;
import org.lamsfoundation.lams.tool.wiki.model.WikiUser;
import org.lamsfoundation.lams.tool.wiki.service.IWikiService;
import org.lamsfoundation.lams.tool.wiki.service.WikiServiceProxy;
import org.lamsfoundation.lams.tool.wiki.util.WikiConstants;
import org.lamsfoundation.lams.tool.wiki.util.WikiException;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.servlet.AbstractExportPortfolioServlet;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * This servlet does export portfolio for teachers and students
 *
 * @author lfoxton
 *
 */
public class ExportServlet extends AbstractExportPortfolioServlet {

    private static final long serialVersionUID = -2829707715037631881L;

    private static Logger logger = Logger.getLogger(ExportServlet.class);

    private final String FILENAME = "wiki_main.html";

    private IWikiService wikiService;

    /**
     * This is the doGet method, it determines whether this is a student or teacher then does the appropriate export
     */
    @Override
    protected String doExport(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies) {

	if (wikiService == null) {
	    wikiService = WikiServiceProxy.getWikiService(getServletContext());
	}

	String basePath = WebUtil.getBaseServerURL() + request.getContextPath();

	try {
	    if (StringUtils.equals(mode, ToolAccessMode.LEARNER.toString())) {
		request.getSession().setAttribute(AttributeNames.ATTR_MODE, ToolAccessMode.LEARNER);
		doLearnerExport(request, response, directoryName, basePath, cookies);
	    } else if (StringUtils.equals(mode, ToolAccessMode.TEACHER.toString())) {
		request.getSession().setAttribute(AttributeNames.ATTR_MODE, ToolAccessMode.TEACHER);
		doTeacherExport(request, response, directoryName, basePath, cookies);
	    }
	} catch (WikiException e) {
	    ExportServlet.logger.error("Cannot perform export for wiki tool.");
	} catch (IOException e) {
	    ExportServlet.logger.error("Cannot perform export for wiki tool.");
	}

	// writeResponseToFile(basePath + "/pages/export/exportPortfolio.jsp",
	// directoryName, FILENAME, cookies);

	return FILENAME;
    }

    /**
     * Do export for learners, outputs the wiki pages into one page which seperates each page into divs which are
     * displayed onclick
     *
     * @param request
     * @param response
     * @param directoryName
     * @param basePath
     * @param cookies
     * @throws WikiException
     * @throws IOException
     */
    private void doLearnerExport(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    String basePath, Cookie[] cookies) throws WikiException, IOException {

	ExportServlet.logger.debug("doExportLearner: toolContentID:" + toolSessionID);

	// check if toolContentID available
	if (toolSessionID == null) {
	    String error = "Tool Session ID is missing. Unable to continue";
	    ExportServlet.logger.error(error);
	    throw new WikiException(error);
	}

	WikiSession wikiSession = wikiService.getSessionBySessionId(toolSessionID);

	Wiki wiki = wikiSession.getWiki();

	// construct wiki dto
	WikiDTO wikiDTO = new WikiDTO(wiki);
	request.getSession().setAttribute(WikiConstants.ATTR_WIKI_DTO, wikiDTO);

	// construct wiki session dto
	WikiSessionDTO sessionDTO = new WikiSessionDTO(wikiSession);
	request.getSession().setAttribute(WikiConstants.ATTR_SESSION_DTO, sessionDTO);

	// construct wiki pages dto
	SortedSet<WikiPageDTO> wikiPageDTOs = new TreeSet<WikiPageDTO>();
	for (WikiPage wikiPage : wikiSession.getWikiPages()) {

	    WikiPageDTO wikiPageDTO = new WikiPageDTO(wikiPage);

	    // Update image links
	    WikiPageContentDTO contentDTO = wikiPageDTO.getCurrentWikiContentDTO();
	    wikiPageDTO.setCurrentWikiContentDTO(contentDTO);

	    wikiPageDTOs.add(wikiPageDTO);
	}
	request.getSession().setAttribute(WikiConstants.ATTR_WIKI_PAGES, wikiPageDTOs);

	// construct current page dto
	request.getSession().setAttribute(WikiConstants.ATTR_CURRENT_WIKI, new WikiPageDTO(wikiSession.getMainPage()));

	// construct main page dto
	request.getSession().setAttribute(WikiConstants.ATTR_MAIN_WIKI_PAGE,
		new WikiPageDTO(wikiSession.getMainPage()));

	// Construct the user dto
	UserDTO lamsUserDTO = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);
	WikiUser wikiUser = wikiService.getUserByUserIdAndSessionId(new Long(lamsUserDTO.getUserID()), toolSessionID);
	WikiUserDTO wikiUserDTO = new WikiUserDTO(wikiUser);
	if (wikiUser.isFinishedActivity()) {
	    // get the notebook entry.
	    NotebookEntry notebookEntry = wikiService.getEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		    WikiConstants.TOOL_SIGNATURE, wikiUser.getUserId().intValue());
	    if (notebookEntry != null) {
		wikiUserDTO.setNotebookEntry(notebookEntry.getEntry());
	    }
	}
	request.getSession().setAttribute(WikiConstants.ATTR_USER_DTO, wikiUserDTO);

	// Set the mode
	request.getSession().setAttribute(WikiConstants.ATTR_MODE, ToolAccessMode.LEARNER);

	writeResponseToFile(basePath + "/pages/export/exportPortfolio.jsp", directoryName, FILENAME, cookies);
    }

    /**
     * Exports for teacher, does a main file and a file for each session so that each session can have its own wiki page
     *
     * @param request
     * @param response
     * @param directoryName
     * @param basePath
     * @param cookies
     * @throws WikiException
     * @throws IOException
     */
    private void doTeacherExport(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    String basePath, Cookie[] cookies) throws WikiException, IOException {

	ExportServlet.logger.debug("doExportTeacher: toolContentID:" + toolContentID);

	// check if toolContentID available
	if (toolContentID == null) {
	    String error = "Tool Content ID is missing. Unable to continue";
	    ExportServlet.logger.error(error);
	    throw new WikiException(error);
	}

	// Set up the main wiki dto
	Wiki wiki = wikiService.getWikiByContentId(toolContentID);
	WikiDTO wikiDTO = new WikiDTO(wiki);
	request.getSession().setAttribute(WikiConstants.ATTR_WIKI_DTO, wikiDTO);

	// Set up the title
	String wikiTitle = wiki.getMainPage().getTitle();
	request.getSession().setAttribute(WikiConstants.ATTR_MAIN_PAGE_TITLE, wikiTitle);

	// Do the main monitoring page
	writeResponseToFile(basePath + "/pages/export/exportPortfolioTeacher.jsp", directoryName, FILENAME, cookies);

	// Do the wiki pages for each session
	for (WikiSession wikiSession : wiki.getWikiSessions()) {
	    // construct wiki session dto
	    WikiSessionDTO sessionDTO = new WikiSessionDTO(wikiSession);

	    for (WikiUserDTO wikiUserDTO : sessionDTO.getUserDTOs()) {
		if (wikiUserDTO.isFinishedActivity()) {
		    // get the notebook entry.
		    NotebookEntry notebookEntry = wikiService.getEntry(sessionDTO.getSessionID(),
			    CoreNotebookConstants.NOTEBOOK_TOOL, WikiConstants.TOOL_SIGNATURE,
			    wikiUserDTO.getUserId().intValue());
		    if (notebookEntry != null) {
			wikiUserDTO.setNotebookEntry(notebookEntry.getEntry());
		    }
		}
		sessionDTO.getUserDTOs().add(wikiUserDTO);
	    }
	    request.getSession().setAttribute(WikiConstants.ATTR_SESSION_DTO, sessionDTO);

	    // construct wiki pages dto
	    SortedSet<WikiPageDTO> wikiPageDTOs = new TreeSet<WikiPageDTO>();
	    for (WikiPage wikiPage : wikiSession.getWikiPages()) {

		WikiPageDTO wikiPageDTO = new WikiPageDTO(wikiPage);

		// Update image links
		WikiPageContentDTO contentDTO = wikiPageDTO.getCurrentWikiContentDTO();
		wikiPageDTO.setCurrentWikiContentDTO(contentDTO);

		wikiPageDTOs.add(wikiPageDTO);
	    }
	    request.getSession().setAttribute(WikiConstants.ATTR_WIKI_PAGES, wikiPageDTOs);

	    // construct current page dto
	    request.getSession().setAttribute(WikiConstants.ATTR_CURRENT_WIKI,
		    new WikiPageDTO(wikiSession.getMainPage()));

	    // construct main page dto
	    request.getSession().setAttribute(WikiConstants.ATTR_MAIN_WIKI_PAGE,
		    new WikiPageDTO(wikiSession.getMainPage()));

	    writeResponseToFile(basePath + "/pages/export/exportPortfolio.jsp", directoryName,
		    wikiSession.getSessionId() + ".html", cookies);

	    // Set the mode
	    request.getSession().setAttribute(WikiConstants.ATTR_MODE, ToolAccessMode.TEACHER);
	}
    }

}
