/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
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

package org.lamsfoundation.lams.tool.dimdim.web.actions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.tool.dimdim.dto.ContentDTO;
import org.lamsfoundation.lams.tool.dimdim.dto.UserDTO;
import org.lamsfoundation.lams.tool.dimdim.model.Dimdim;
import org.lamsfoundation.lams.tool.dimdim.model.DimdimSession;
import org.lamsfoundation.lams.tool.dimdim.model.DimdimUser;
import org.lamsfoundation.lams.tool.dimdim.service.DimdimServiceProxy;
import org.lamsfoundation.lams.tool.dimdim.service.IDimdimService;
import org.lamsfoundation.lams.tool.dimdim.util.Constants;
import org.lamsfoundation.lams.tool.dimdim.web.forms.MonitoringForm;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * @author
 * @version
 * 
 * @struts.action path="/monitoring" parameter="dispatch" scope="request"
 *                name="monitoringForm" validate="false"
 * 
 * @struts.action-forward name="success" path="tiles:/monitoring/main"
 * @struts.action-forward name="dimdim_display"
 *                        path="tiles:/monitoring/dimdim_display"
 * 
 */
public class MonitoringAction extends DispatchAction {

	// private static final Logger logger =
	// Logger.getLogger(MonitoringAction.class);

	private IDimdimService dimdimService;

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		// set up dimdimService
		dimdimService = DimdimServiceProxy.getDimdimService(this.getServlet()
				.getServletContext());

		return super.execute(mapping, form, request, response);
	}

	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		Long toolContentID = new Long(WebUtil.readLongParam(request,
				AttributeNames.PARAM_TOOL_CONTENT_ID));

		String contentFolderID = WebUtil.readStrParam(request,
				AttributeNames.PARAM_CONTENT_FOLDER_ID);

		Dimdim dimdim = dimdimService.getDimdimByContentId(toolContentID);

		if (dimdim == null) {
			// TODO error page.
		}

		ContentDTO contentDT0 = new ContentDTO(dimdim);

		MonitoringForm monitoringForm = (MonitoringForm) form;
		// populate using authoring values
		monitoringForm.setTopic(contentDT0.getTopic());
		monitoringForm.setMeetingKey((contentDT0.getMeetingKey()));
		monitoringForm.setMaxAttendeeMikes((contentDT0.getMaxAttendeeMikes()));

		// TODO may need to populate using session values if they already exist

		Long currentTab = WebUtil.readLongParam(request,
				AttributeNames.PARAM_CURRENT_TAB, true);
		contentDT0.setCurrentTab(currentTab);

		request.setAttribute(Constants.ATTR_CONTENT_DTO, contentDT0);
		request.setAttribute(Constants.ATTR_CONTENT_FOLDER_ID, contentFolderID);
		return mapping.findForward("success");
	}

	public ActionForward showDimdim(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long uid = new Long(WebUtil.readLongParam(request,
				Constants.PARAM_USER_UID));

		DimdimUser user = dimdimService.getUserByUID(uid);
		NotebookEntry entry = dimdimService.getEntry(user.getEntryUID());

		UserDTO userDTO = new UserDTO(user, entry);

		request.setAttribute(Constants.ATTR_USER_DTO, userDTO);

		return mapping.findForward("dimdim_display");
	}

	public ActionForward startDimdim(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		MonitoringForm monitoringForm = new MonitoringForm();

		// get dimdim session
		DimdimSession session = dimdimService
				.getSessionBySessionId(monitoringForm.getToolSessionID());

		// update dimdim meeting settings
		session.setTopic(monitoringForm.getTopic());
		session.setMeetingKey(monitoringForm.getMeetingKey());
		session.setMaxAttendeeMikes(monitoringForm.getMaxAttendeeMikes());

		// Get LAMS userDTO
		org.lamsfoundation.lams.usermanagement.dto.UserDTO lamsUserDTO = (org.lamsfoundation.lams.usermanagement.dto.UserDTO) SessionManager
				.getSession().getAttribute(AttributeNames.USER);

		// get dimdim confkey
		try {
			URL url = new URL(
					"http://172.20.100.217:8080/dimdim/StartNewConferenceCheck.action?"
							+ "email="
							+ URLEncoder.encode(lamsUserDTO.getEmail(), "UTF8")
							+ "&displayName="
							+ URLEncoder.encode(lamsUserDTO.getFirstName()
									+ lamsUserDTO.getLastName(), "UTF8")
							+ "&confName="
							+ URLEncoder.encode(session.getTopic(), "UTF8")
							+ "&confKey="
							+ URLEncoder
									.encode(session.getMeetingKey(), "UTF8")
							+ "&lobby=false" + "&networkProfile=3"
							+ "&meetingHours=99" + "&maxAttendeeMikes=0"
							+ "&returnUrl=asdf" + "&presenterAV=av"
							+ "&privateChatEnabled=true"
							+ "&publicChatEnabled=true"
							+ "&screenShareEnabled=true"
							+ "&whiteboardEnabled=true");

			URLConnection connection = url.openConnection();

			BufferedReader in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null)
				System.out.println(inputLine);
			in.close();

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mapping.findForward("dimdim_conference");
	}
}
