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
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.gmap.dto.GmapDTO;
import org.lamsfoundation.lams.tool.gmap.dto.GmapUserDTO;
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

		//GmapDTO gmapDTO = new GmapDTO();
		//gmapDTO.title = gmap.getTitle();
		//gmapDTO.instructions = gmap.getInstructions();
		//gmapDTO.allowRichEditor = gmap.isAllowRichEditor();
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
		
		// get any existing Notebook entry
		NotebookEntry nbEntry = null;
		if ( gmapUser != null ) {
			nbEntry = gmapService.getEntry(gmapUser.getEntryUID());
		}
		if (nbEntry != null) {
			learningForm.setEntryText(nbEntry.getEntry());
		}
		
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

			// TODO fix idType to use real value not 999

			if (gmapUser.getEntryUID() == null) {
				gmapUser.setEntryUID(gmapService.createNotebookEntry(
						toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL, GmapConstants.TOOL_SIGNATURE,
						gmapUser.getUserId().intValue(), learningForm
								.getEntryText()));
			} else {
				// update existing entry.
				gmapService.updateEntry(gmapUser.getEntryUID(),
						learningForm.getEntryText());
			}

			gmapUser.setFinishedActivity(true);
			gmapService.saveOrUpdateGmapUser(gmapUser);
			
			
			// Retrieve the session and content.
			GmapSession gmapSession = gmapService.getSessionBySessionId(toolSessionID);
			if (gmapSession == null) {
				throw new GmapException("Cannot retreive session with toolSessionID"+ toolSessionID);
			}
			
			// update the marker list
			Gmap gmap = gmapSession.getGmap();
			updateMarkerListFromXML(learningForm.getMarkersXML(), gmap, gmapUser);

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

	private void updateMarkerListFromXML(String markerXML, Gmap gmap, GmapUser guser)
	{
		//Set<GmapMarker> newMarkers = new HashSet<GmapMarker>();
		Set<GmapMarker> existingMarkers = gmap.getGmapMarkers();
		try 
		{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(new InputSource(new StringReader(markerXML)));
			NodeList list = document.getElementsByTagName("marker");
			
			for (int i =0; i<list.getLength(); i++)
			{
				NamedNodeMap markerNode = ((Node)list.item(i)).getAttributes();
				
				Long uid  = Long.parseLong(markerNode.getNamedItem("markerUID").getNodeValue());
				String markerTitle = markerNode.getNamedItem("title").getNodeValue();
				String infoMessage = markerNode.getNamedItem("infoMessage").getNodeValue();
				Double latitude = Double.parseDouble(markerNode.getNamedItem("latitude").getNodeValue());
				Double longitude = Double.parseDouble(markerNode.getNamedItem("longitude").getNodeValue());

				String markerState = markerNode.getNamedItem("state").getNodeValue();
				
				if (markerState.equals("remove"))
				{
					gmap.removeMarker(uid);
					return;
				}

				GmapMarker marker = null;
				if (markerState.equals("save"))
				{
					marker = new GmapMarker();
					marker.setCreatedBy(guser);
					marker.setCreated(new Date());
				}
				else if (markerState.equals("update"))
				{
					marker = gmap.getMarkerByUid(uid);
				}
				
				marker.setTitle(markerTitle);
				marker.setInfoWindowMessage(infoMessage);
				marker.setLatitude(latitude);
				marker.setLongitude(longitude);
				marker.setGmap(gmap);
				marker.setUpdated(new Date());
				marker.setUpdatedBy(guser);
				marker.setAuthored(false);
				gmapService.saveOrUpdateGmapMarker(marker);
					
				
			}
		}
		catch (Exception e)
		{
			// TODO: improve error handling
			log.error("Could not get marker xml object to update", e);
		}
	}
	
	
	
	
}
