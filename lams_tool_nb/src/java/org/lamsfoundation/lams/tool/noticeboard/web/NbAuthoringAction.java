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


package org.lamsfoundation.lams.tool.noticeboard.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.lamsfoundation.lams.authoring.web.AuthoringConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.noticeboard.NbApplicationException;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardConstants;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;
import org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService;
import org.lamsfoundation.lams.tool.noticeboard.service.NoticeboardServiceProxy;
import org.lamsfoundation.lams.tool.noticeboard.util.NbWebUtil;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * <p>
 * This class is a simple combination of NbAuthoringStarterAction and NbAuthoringAction. It has been created for the
 * purpose of supporting the new authoring page which is done using DHTML.
 * </p>
 *
 * <p>
 * The unspecified method, is the same as the execute method for NbAuthoringStarterAction. It will get called when the
 * method parameter is not specified (that is on first entry into the authoring environment).
 * </p>
 *
 * <p>
 * The save, upload and delete method is the same as that of NbAuthoringAction, to see its explanation, please see
 * org.lamsfoundation.lams.tool.noticeboard.web.NbAuthoringAction
 * </p>
 * 
 * @author mtruong
 */
public class NbAuthoringAction extends LamsDispatchAction {
    private static Logger logger = Logger.getLogger(NbAuthoringAction.class.getName());
    public final static String FORM = "NbAuthoringForm";

    /** Get the user from the shared session */
    public UserDTO getUser(HttpServletRequest request) {
	// set up the user details
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	if (user == null) {
	    MessageResources resources = getResources(request);
	    String error = resources.getMessage(NoticeboardConstants.ERR_MISSING_PARAM, "User");
	    logger.error(error);
	    throw new NbApplicationException(error);
	}
	return user;
    }

    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NbApplicationException {
	/*
	 * Retrieve the Service
	 */
	INoticeboardService nbService = NoticeboardServiceProxy.getNbService(getServlet().getServletContext());

	//to ensure that we are working with a new form, not one from previous session
	NbAuthoringForm nbForm = new NbAuthoringForm();

	Long contentId = WebUtil.readLongParam(request, NoticeboardConstants.TOOL_CONTENT_ID);
	String contentFolderId = WebUtil.readStrParam(request, NoticeboardConstants.CONTENT_FOLDER_ID);

	nbForm.setToolContentID(contentId.toString());

	/*
	 * DefineLater is used in the basic screen. If defineLater is set, then in the authoring page,
	 * the two tabs {Advanced, Instructions} are not visible.
	 */
	nbForm.setDefineLater(request.getParameter(NoticeboardConstants.DEFINE_LATER));

	if (!contentExists(nbService, contentId)) {
	    //	Pre-fill the form with the default content
	    //NoticeboardContent nb =	nbService.retrieveNoticeboard(NoticeboardConstants.DEFAULT_CONTENT_ID);
	    Long defaultToolContentId = nbService
		    .getToolDefaultContentIdBySignature(NoticeboardConstants.TOOL_SIGNATURE);
	    //  logger.debug("Default tool content id is " + defaultToolContentId);
	    NoticeboardContent nb = nbService.retrieveNoticeboard(defaultToolContentId);

	    if (nb == null) {
		String error = "There is data missing in the database";
		logger.error(error);
		throw new NbApplicationException(error);
	    }

	    //initialise the values in the form, so the values will be shown in the jsp
	    nbForm.setToolContentID(contentId.toString());
	    nbForm.setContentFolderID(contentFolderId);
	    nbForm.setTitle(nb.getTitle());
	    nbForm.setBasicContent(nb.getContent());

	//content already exists on the database
	} else	{
	    //get the values from the database
	    NoticeboardContent nb = nbService.retrieveNoticeboard(contentId);

	    /*
	     * If retrieving existing content, check whether the contentInUse flag is set, if set, the
	     * author is not allowed to edit content
	     */

	    /*
	     * Define later set to true when the edit activity tab is brought up
	     * So that users cannot start using the content while the staff member is editing the content
	     */
	    nbForm.populateFormWithNbContentValues(nb);
	    nbForm.setContentFolderID(contentFolderId);
	    boolean isDefineLater = Boolean.parseBoolean(nbForm.getDefineLater());
	    nb.setDefineLater(isDefineLater);
	    nbService.saveNoticeboard(nb);

	    if (isDefineLater) {
		request.setAttribute(AttributeNames.ATTR_MODE, ToolAccessMode.TEACHER.toString());
	    }
	}

	request.setAttribute(FORM, nbForm);
	return mapping.findForward(NoticeboardConstants.AUTHOR_PAGE);
    }

    /**
     * Checks the session to see if the title and content session variables exist or not.
     * 
     * @param session
     *            The HttpSession to check.
     * @return true if the parameters title and content exists in the session, false otherwise
     */
    private boolean contentExists(INoticeboardService service, Long id) {
	NoticeboardContent nb = service.retrieveNoticeboard(id);
	if (nb == null) {
	    return false;
	} else {
	    return true;
	}

    }

    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NbApplicationException {

	NbAuthoringForm nbForm = (NbAuthoringForm) form;
	//copyAuthoringFormValuesIntoFormBean(request, nbForm);

	INoticeboardService nbService = NoticeboardServiceProxy.getNbService(getServlet().getServletContext());
	String idAsString = nbForm.getToolContentID();
	if (idAsString == null) {
	    MessageResources resources = getResources(request);
	    String error = resources.getMessage(NoticeboardConstants.ERR_MISSING_PARAM, "Tool Content Id");
	    logger.error(error);
	    throw new NbApplicationException(error);
	}
	Long content_id = NbWebUtil.convertToLong(nbForm.getToolContentID());

	//throws exception if the content id does not exist
	checkContentId(content_id);

	NoticeboardContent nbContent = nbService.retrieveNoticeboard(content_id);
	if (nbContent == null) {
	    //create a new noticeboard object 
	    nbContent = new NoticeboardContent();
	    nbContent.setNbContentId(content_id);
	}

	nbForm.copyValuesIntoNbContent(nbContent);
	if (nbContent.getDateCreated() == null) {
	    nbContent.setDateCreated(nbContent.getDateUpdated());
	}

	UserDTO user = getUser(request);
	nbContent.setCreatorUserId(new Long(user.getUserID().longValue()));

	// Author has finished editing the content and mark the defineLater flag to false
	nbContent.setDefineLater(false);
	nbService.saveNoticeboard(nbContent);

	request.setAttribute(AuthoringConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);
	return mapping.findForward(NoticeboardConstants.AUTHOR_PAGE);

    }

    /**
     * It is assumed that the contentId is passed as a http parameter
     * if the contentId is null, an exception is thrown, otherwise proceed as normal
     * 
     * @param contentId
     *            the <code>toolContentId</code> to check
     */
    private void checkContentId(Long contentId) {
	if (contentId == null) {
	    String error = "Unable to continue. Tool content id missing.";

	    throw new NbApplicationException(error);
	}
    }

}
