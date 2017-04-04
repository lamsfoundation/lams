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


package org.lamsfoundation.lams.tool.gmap.web.actions;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.authoring.web.AuthoringConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.gmap.model.Gmap;
import org.lamsfoundation.lams.tool.gmap.model.GmapConfigItem;
import org.lamsfoundation.lams.tool.gmap.model.GmapMarker;
import org.lamsfoundation.lams.tool.gmap.model.GmapUser;
import org.lamsfoundation.lams.tool.gmap.service.GmapServiceProxy;
import org.lamsfoundation.lams.tool.gmap.service.IGmapService;
import org.lamsfoundation.lams.tool.gmap.util.GmapConstants;
import org.lamsfoundation.lams.tool.gmap.web.forms.AuthoringForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 * @author
 */
public class AuthoringAction extends LamsDispatchAction {

    private static Logger logger = Logger.getLogger(AuthoringAction.class);

    public IGmapService gmapService;

    // Authoring SessionMap key names
    private static final String KEY_TOOL_CONTENT_ID = "toolContentID";

    private static final String KEY_CONTENT_FOLDER_ID = "contentFolderID";

    private static final String KEY_MODE = "mode";

    /**
     * Default method when no dispatch parameter is specified. It is expected
     * that the parameter <code>toolContentID</code> will be passed in. This
     * will be used to retrieve content for this tool.
     * 
     */
    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// Extract toolContentID from parameters.
	Long toolContentID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);

	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);

	// set up gmapService
	if (gmapService == null) {
	    gmapService = GmapServiceProxy.getGmapService(this.getServlet().getServletContext());
	}

	// retrieving Gmap with given toolContentID
	Gmap gmap = gmapService.getGmapByContentId(toolContentID);
	if (gmap == null) {
	    gmap = gmapService.copyDefaultContent(toolContentID);
	    gmap.setCreateDate(new Date());
	    gmapService.saveOrUpdateGmap(gmap);
	    // TODO NOTE: this causes DB orphans when LD not saved.
	}

	if (mode.isTeacher()) {
	    // Set the defineLater flag so that learners cannot use content
	    // while we
	    // are editing. This flag is released when updateContent is called.
	    gmap.setDefineLater(true);
	    gmapService.saveOrUpdateGmap(gmap);
	    
	    //audit log the teacher has started editing activity in monitor
	    gmapService.auditLogStartEditingActivityInMonitor(toolContentID);
	}

	// Set up the authForm.
	AuthoringForm authForm = (AuthoringForm) form;
	updateAuthForm(authForm, gmap);

	// Set up sessionMap
	SessionMap<String, Object> map = createSessionMap(gmap, mode, contentFolderID, toolContentID);
	authForm.setSessionMapID(map.getSessionID());
	authForm.setGmap(gmap);

	// add the sessionMap to HTTPSession.
	request.getSession().setAttribute(map.getSessionID(), map);
	request.setAttribute(GmapConstants.ATTR_SESSION_MAP, map);

	// get the gmap API key from the config table and add it to the session
	GmapConfigItem gmapKey = gmapService.getConfigItem(GmapConfigItem.KEY_GMAP_KEY);
	if (gmapKey != null && gmapKey.getConfigValue() != null) {
	    request.setAttribute(GmapConstants.ATTR_GMAP_KEY, gmapKey.getConfigValue());
	}

	return mapping.findForward("success");
    }

    public ActionForward updateContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	// TODO need error checking.

	// get authForm and session map.
	AuthoringForm authForm = (AuthoringForm) form;
	SessionMap<String, Object> map = getSessionMap(request, authForm);
	Long toolContentID = (Long) map.get(KEY_TOOL_CONTENT_ID);

	// get gmap content.
	Gmap gmap = gmapService.getGmapByContentId(toolContentID);

	// update gmap content using form inputs.
	ToolAccessMode mode = (ToolAccessMode) map.get(KEY_MODE);

	String contentFolderID = (String) map.get(AttributeNames.PARAM_CONTENT_FOLDER_ID);
	GmapUser gmapUser = null;
	//check whether it is sysadmin:LDEV-906 
	//if(!StringUtils.equals(contentFolderID,"-1" )){
	if (gmap.getCreateBy() == null) {
	    HttpSession ss = SessionManager.getSession();
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    gmapUser = new GmapUser(user, null);
	    gmapService.saveOrUpdateGmapUser(gmapUser);
	} else {
	    gmapUser = gmapService.getUserByUID(gmap.getCreateBy());
	}
	//}

	updateGmap(gmap, authForm, mode, gmapUser);

	// do the same for the gmap markers
	Set<GmapMarker> markers = gmap.getGmapMarkers();
	if (markers == null) {
	    markers = new HashSet<GmapMarker>();
	}

	// set the update date
	gmap.setUpdateDate(new Date());

	// releasing defineLater flag so that learner can start using the tool.
	gmap.setDefineLater(false);

	gmapService.saveOrUpdateGmap(gmap);

	request.setAttribute(AuthoringConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);

	// add the sessionMapID to form
	authForm.setSessionMapID(map.getSessionID());

	request.setAttribute(GmapConstants.ATTR_SESSION_MAP, map);

	// get the gmap API key from the config table and add it to the session
	GmapConfigItem gmapKey = gmapService.getConfigItem(GmapConfigItem.KEY_GMAP_KEY);
	if (gmapKey != null && gmapKey.getConfigValue() != null) {
	    request.setAttribute(GmapConstants.ATTR_GMAP_KEY, gmapKey.getConfigValue());
	}

	return mapping.findForward("success");
    }

    /* ========== Private Methods ********** */

    /**
     * Updates Gmap content using AuthoringForm inputs.
     * 
     * @param authForm
     * @param mode
     * @return
     */
    private void updateGmap(Gmap gmap, AuthoringForm authForm, ToolAccessMode mode, GmapUser guser) {
	gmap.setTitle(authForm.getTitle());
	gmap.setInstructions(authForm.getInstructions());

	//updateMarkerListFromXML(authForm.getMarkersXML(), gmap, guser);
	gmapService.updateMarkerListFromXML(authForm.getMarkersXML(), gmap, guser, true, null);

	if (mode.isAuthor()) { // Teacher cannot modify following
	    gmap.setCreateBy(guser.getUid());
	    gmap.setLockOnFinished(authForm.isLockOnFinished());
	    gmap.setAllowEditMarkers(authForm.isAllowEditMarkers());
	    gmap.setAllowShowAllMarkers(authForm.isAllowShowAllMarkers());
	    gmap.setMaxMarkers(authForm.getMaxMarkers());
	    gmap.setLimitMarkers(authForm.isLimitMarkers());
	    gmap.setAllowZoom(authForm.isAllowZoom());
	    gmap.setAllowTerrain(authForm.isAllowTerrain());
	    gmap.setAllowSatellite(authForm.isAllowSatellite());
	    gmap.setAllowHybrid(authForm.isAllowHybrid());
	    gmap.setMapType(authForm.getMapType());
	    gmap.setMapZoom(authForm.getMapZoom());
	    gmap.setMapCenterLatitude(authForm.getMapCenterLatitude());
	    gmap.setMapCenterLongitude(authForm.getMapCenterLongitude());
	    gmap.setReflectOnActivity(authForm.isReflectOnActivity());
	    gmap.setReflectInstructions(authForm.getReflectInstructions());
	    gmap.setDefaultGeocoderAddress(authForm.getDefaultGeocoderAddress());
	}
    }

    /**
     * Updates AuthoringForm using Gmap content.
     * 
     * @param gmap
     * @param authForm
     * @return
     */
    private void updateAuthForm(AuthoringForm authForm, Gmap gmap) {
	authForm.setTitle(gmap.getTitle());
	authForm.setInstructions(gmap.getInstructions());
	authForm.setLockOnFinished(gmap.isLockOnFinished());
	authForm.setAllowEditMarkers(gmap.isAllowEditMarkers());
	authForm.setAllowShowAllMarkers(gmap.isAllowShowAllMarkers());
	authForm.setMaxMarkers(gmap.getMaxMarkers());
	authForm.setLimitMarkers(gmap.isLimitMarkers());
	authForm.setAllowZoom(gmap.isAllowZoom());
	authForm.setAllowTerrain(gmap.isAllowTerrain());
	authForm.setAllowSatellite(gmap.isAllowSatellite());
	authForm.setAllowHybrid(gmap.isAllowHybrid());
	authForm.setMapType(gmap.getMapType());
	authForm.setMapZoom(gmap.getMapZoom());
	authForm.setMapCenterLatitude(gmap.getMapCenterLatitude());
	authForm.setMapCenterLongitude(gmap.getMapCenterLongitude());
	authForm.setReflectOnActivity(gmap.isReflectOnActivity());
	authForm.setReflectInstructions(gmap.getReflectInstructions());
	authForm.setDefaultGeocoderAddress(gmap.getDefaultGeocoderAddress());
    }

    /**
     * Updates SessionMap using Gmap content.
     * 
     * @param gmap
     * @param mode
     */
    private SessionMap<String, Object> createSessionMap(Gmap gmap, ToolAccessMode mode, String contentFolderID,
	    Long toolContentID) {

	SessionMap<String, Object> map = new SessionMap<String, Object>();

	map.put(KEY_MODE, mode);
	map.put(KEY_CONTENT_FOLDER_ID, contentFolderID);
	map.put(KEY_TOOL_CONTENT_ID, toolContentID);

	return map;
    }

    /**
     * Retrieve the SessionMap from the HttpSession using the author form.
     * 
     * @param request
     * @param authForm
     * @return
     */
    private SessionMap<String, Object> getSessionMap(HttpServletRequest request, AuthoringForm authForm) {
	return (SessionMap<String, Object>) request.getSession().getAttribute(authForm.getSessionMapID());
    }

}
