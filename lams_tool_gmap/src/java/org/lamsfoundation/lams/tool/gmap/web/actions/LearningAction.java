/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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

package org.lamsfoundation.lams.tool.gmap.web.actions;

import java.io.IOException;
import java.io.StringReader;
import java.util.Date;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.gmap.dto.GmapDTO;
import org.lamsfoundation.lams.tool.gmap.dto.GmapUserDTO;
import org.lamsfoundation.lams.tool.gmap.dto.GmapSessionDTO;
import org.lamsfoundation.lams.tool.gmap.model.Gmap;
import org.lamsfoundation.lams.tool.gmap.model.GmapMarker;
import org.lamsfoundation.lams.tool.gmap.model.GmapSession;
import org.lamsfoundation.lams.tool.gmap.model.GmapUser;
import org.lamsfoundation.lams.tool.gmap.service.IGmapService;
import org.lamsfoundation.lams.tool.gmap.service.GmapServiceProxy;
import org.lamsfoundation.lams.tool.gmap.util.GmapConstants;
import org.lamsfoundation.lams.tool.gmap.util.GmapException;
import org.lamsfoundation.lams.tool.gmap.web.forms.LearningForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * @author
 * @version
 * 
 * @struts.action path="/learning" parameter="dispatch" scope="request"
 *                name="learningForm"
 * @struts.action-forward name="gmap" path="tiles:/learning/main"
 * @struts.action-forward name="runOffline" path="tiles:/learning/runOffline"
 * @struts.action-forward name="defineLater" path="tiles:/learning/defineLater"
 */
public class LearningAction extends LamsDispatchAction {

	private static Logger log = Logger.getLogger(LearningAction.class);

	private static final boolean MODE_OPTIONAL = false;

	private IGmapService gmapService;

	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		LearningForm learningForm = (LearningForm) form;

		// 'toolSessionID' and 'mode' paramters are expected to be present.
		// TODO need to catch exceptions and handle errors.
		ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE, MODE_OPTIONAL);

		Long toolSessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

		// set up gmapService
		if (gmapService == null) {
			gmapService = GmapServiceProxy.getGmapService(this.getServlet().getServletContext());
		}

		// Retrieve the session and content.
		GmapSession gmapSession = gmapService.getSessionBySessionId(toolSessionID);
		if (gmapSession == null) {
			throw new GmapException("Cannot retreive session with toolSessionID"+ toolSessionID);
		}

		Gmap gmap = gmapSession.getGmap();
		if (gmap.getTitle() == null) {
			throw new GmapException("Cannot retreive gmap with toolSessionID"+ toolSessionID);
		}
		
		
		// check defineLater
		if (gmap.isDefineLater()) {
			return mapping.findForward("defineLater");
		}

		// set mode, toolSessionID and GmapDTO
		request.setAttribute("mode", mode.toString());
		learningForm.setToolSessionID(toolSessionID);

		// Putting in the GmapDTO for the request
		GmapDTO gmapDTO = new GmapDTO(gmap);
		request.setAttribute("gmapDTO", gmapDTO);

		// Set the content in use flag.
		if (!gmap.isContentInUse()) {
			gmap.setContentInUse(new Boolean(true));
			gmapService.saveOrUpdateGmap(gmap);
		}

		// check runOffline
		if (gmap.isRunOffline()) {
			return mapping.findForward("runOffline");
		}
		
		GmapUser gmapUser;
		if (mode.equals(ToolAccessMode.TEACHER)) {
			Long userID = WebUtil.readLongParam(request, AttributeNames.PARAM_USER_ID, false);
			gmapUser = gmapService.getUserByUserIdAndSessionId(userID, toolSessionID);
		} else {
			gmapUser = getCurrentUser(toolSessionID);
		}
		GmapUserDTO gmapUserDTO = new GmapUserDTO(gmapUser);
		request.setAttribute(GmapConstants.ATTR_USER_DTO, gmapUserDTO);
		
		
		// Putting in a gmap session attribute, along with markers for this session
		GmapSessionDTO gmapSessionDTO= new GmapSessionDTO(gmapSession);
		gmapSessionDTO.setMarkerDTOs(gmapService.getGmapMarkersBySessionId(toolSessionID));
		if (!gmapSessionDTO.getUserDTOs().contains(gmapUserDTO))
		{
			gmapSessionDTO.getUserDTOs().add(gmapUserDTO);
		}
		request.setAttribute("gmapSessionDTO", gmapSessionDTO);
		
		
		// set readOnly flag.
		if (mode.equals(ToolAccessMode.TEACHER) || (gmap.isLockOnFinished() && gmapUser.isFinishedActivity())) {
			request.setAttribute("contentEditable", false);
		} else {
			request.setAttribute("contentEditable", true);
		}

		return mapping.findForward("gmap");
	}

	private GmapUser getCurrentUser(Long toolSessionId) {
		UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(
				AttributeNames.USER);

		// attempt to retrieve user using userId and toolSessionId
		GmapUser gmapUser = gmapService
				.getUserByUserIdAndSessionId(new Long(user.getUserID()
						.intValue()), toolSessionId);

		if (gmapUser == null) {
			GmapSession gmapSession = gmapService
					.getSessionBySessionId(toolSessionId);
			gmapUser = gmapService.createGmapUser(user,
					gmapSession);
		}

		return gmapUser;
	}

	public ActionForward finishActivity(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		Long toolSessionID = WebUtil.readLongParam(request, "toolSessionID");

		GmapUser gmapUser = getCurrentUser(toolSessionID);

		if (gmapUser != null) {

			LearningForm learningForm = (LearningForm) form;	
			
			// Retrieve the session and content.
			GmapSession gmapSession = gmapService.getSessionBySessionId(toolSessionID);
			if (gmapSession == null) {
				throw new GmapException("Cannot retreive session with toolSessionID"+ toolSessionID);
			}
			
			// update the marker list
			Gmap gmap = gmapSession.getGmap();
			gmapService.updateMarkerListFromXML(learningForm.getMarkersXML(), gmap, gmapUser, true, gmapSession);
			
			// Set the user finished flag
			gmapUser.setFinishedActivity(true);
			gmapService.saveOrUpdateGmapUser(gmapUser);
			

		} else {
			log.error("finishActivity(): couldn't find GmapUser with id: "
					+ gmapUser.getUserId() + "and toolSessionID: "
					+ toolSessionID);
		}

		ToolSessionManager sessionMgrService = GmapServiceProxy.getGmapSessionManager(getServlet().getServletContext());
		String nextActivityUrl;
		try {
			nextActivityUrl = sessionMgrService.leaveToolSession(toolSessionID,gmapUser.getUserId());
			response.sendRedirect(nextActivityUrl);
		} catch (DataMissingException e) {
			throw new GmapException(e);
		} catch (ToolException e) {
			throw new GmapException(e);
		} catch (IOException e) {
			throw new GmapException(e);
		}

		return null; // TODO need to return proper page.
	}
	
	
	public ActionForward saveMarkers(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{

		Long toolSessionID = WebUtil.readLongParam(request, "toolSessionID");

		GmapUser gmapUser = getCurrentUser(toolSessionID);

		if (gmapUser != null) {

			LearningForm learningForm = (LearningForm) form;	
			
			// Retrieve the session and content.
			GmapSession gmapSession = gmapService.getSessionBySessionId(toolSessionID);
			if (gmapSession == null) {
				throw new GmapException("Cannot retreive session with toolSessionID"+ toolSessionID);
			}
			
			// update the marker list
			Gmap gmap = gmapSession.getGmap();
			gmapService.updateMarkerListFromXML(learningForm.getMarkersXML(), gmap, gmapUser, false, gmapSession);
			//updateMarkerListFromXML(learningForm.getMarkersXML(), gmap, gmapUser, gmapSession);

		} else {
			log.error("saveMarkers(): couldn't find GmapUser with id: "
					+ gmapUser.getUserId() + "and toolSessionID: "
					+ toolSessionID);
		}

		return unspecified(mapping, form, request, response);
	}
}
