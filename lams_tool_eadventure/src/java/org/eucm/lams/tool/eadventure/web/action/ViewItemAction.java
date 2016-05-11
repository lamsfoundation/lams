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


package org.eucm.lams.tool.eadventure.web.action;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.eucm.lams.tool.eadventure.EadventureConstants;
import org.eucm.lams.tool.eadventure.dto.InstructionNavDTO;
import org.eucm.lams.tool.eadventure.model.Eadventure;
import org.eucm.lams.tool.eadventure.service.IEadventureService;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class ViewItemAction extends Action {

    private static final Logger log = Logger.getLogger(ViewItemAction.class);

    private static IEadventureService eadventureService;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	String param = mapping.getParameter();
	//-----------------------Display Learning Object function ---------------------------
	if (param.equals("reviewItem")) {
	    return reviewItem(mapping, form, request, response);
	}
	//for preview top frame html page use:
	if (param.equals("nextInstruction")) {
	    return nextInstruction(mapping, form, request, response);
	}
	//for preview top frame html page use:
	if (param.equals("openUrlPopup")) {
	    //return openUrlPopup(mapping, form, request, response);
	}

	return mapping.findForward(EadventureConstants.ERROR);
    }

    /**
     * Open url in popup window page.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    //TODO ver si lo quitamos
    /*
     * private ActionForward openUrlPopup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
     * HttpServletResponse response) {
     * String mode = request.getParameter(AttributeNames.ATTR_MODE);
     * 
     * if (eadventureService == null) {
     * eadventureService = EadventureServiceProxy.getEadventureService(getServlet().getServletContext());
     * }
     * 
     * String sessionMapID = WebUtil.readStrParam(request, EadventureConstants.ATTR_SESSION_MAP_ID);
     * SessionMap sessionMap = (SessionMap)request.getSession().getAttribute(sessionMapID);
     * 
     * EadventureItem item = getEadventureItem(request,sessionMap, mode);
     * 
     * request.setAttribute(EadventureConstants.PARAM_OPEN_URL_POPUP,item.getUrl());
     * request.setAttribute(EadventureConstants.PARAM_TITLE,item.getTitle());
     * return mapping.findForward(EadventureConstants.SUCCESS);
     * }
     */
    /**
     * Return next instrucion to page. It need four input parameters, mode, itemIndex or itemUid, and insIdx.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward nextInstruction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	String mode = request.getParameter(AttributeNames.ATTR_MODE);

	String sessionMapID = WebUtil.readStrParam(request, EadventureConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	Eadventure ead = getEadventure(request, sessionMap, mode);
	if (ead == null) {
	    return mapping.findForward(EadventureConstants.ERROR);
	}

	String instruction = ead.getInstructions();
	InstructionNavDTO navDto = new InstructionNavDTO();
	navDto.setTitle(ead.getTitle());
	navDto.setInstruction(instruction);

	request.setAttribute(EadventureConstants.ATTR_TOOL_CONTENT_ID, ead.getContentId());
	request.setAttribute(EadventureConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	request.setAttribute(EadventureConstants.ATTR_RESOURCE_INSTRUCTION, navDto);

	return mapping.findForward(EadventureConstants.SUCCESS);
    }

    /**
     * Display main frame to display instrcution and item content.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward reviewItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	String mode = request.getParameter(AttributeNames.ATTR_MODE);

	String sessionMapID = WebUtil.readStrParam(request, EadventureConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	Eadventure ead = getEadventure(request, sessionMap, mode);

	String idStr = request.getParameter(EadventureConstants.ATTR_TOOL_SESSION_ID);
	Long sessionId = NumberUtils.createLong(idStr);
	//mark this item access flag if it is learner
	if (ToolAccessMode.LEARNER.toString().equals(mode)) {
	    IEadventureService service = getEadventureService();
	    HttpSession ss = SessionManager.getSession();
	    //get back login user DTO
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    log.error("Vamos a setear el userID!!!!!!!!!!!!!!");
	    log.error("el usuario se llama " + user.getFirstName());
	    log.error("su ID es " + user.getUserID());

	    service.setItemAccess(ead.getUid(), new Long(user.getUserID().intValue()), sessionId);
	}

	if (ead == null) {
	    return mapping.findForward(EadventureConstants.ERROR);
	}
	// TODO revisar
	sessionMap.put(EadventureConstants.ATT_LEARNING_OBJECT, ead);

	//params to eaGame
	/*
	 * sessionMap.put(EadventureConstants.ATT_LEARNING_OBJECT,item);
	 * sessionMap.put(EadventureConstants.ATT_LEARNING_OBJECT,item);
	 * sessionMap.put(EadventureConstants.ATT_LEARNING_OBJECT,item);
	 * sessionMap.put(EadventureConstants.ATT_LEARNING_OBJECT,item);
	 */

	String reviewURL = "/pages/learningobj/mainframe.jsp?sessionMapID=" + sessionMapID;
	int itemIdx = NumberUtils.stringToInt(request.getParameter(EadventureConstants.PARAM_ITEM_INDEX));
	request.setAttribute(EadventureConstants.ATTR_RESOURCE_REVIEW_URL, reviewURL);
	//these attribute will be use to instruction navigator page
	request.setAttribute(AttributeNames.ATTR_MODE, mode);
	request.setAttribute(EadventureConstants.PARAM_ITEM_INDEX, itemIdx);
	Long itemUid = NumberUtils.createLong(request.getParameter(EadventureConstants.PARAM_RESOURCE_ITEM_UID));
	request.setAttribute(EadventureConstants.PARAM_RESOURCE_ITEM_UID, itemUid);
	request.setAttribute(EadventureConstants.ATTR_TOOL_SESSION_ID, sessionId);
	request.setAttribute(AttributeNames.PARAM_TOOL_CONTENT_ID, ead.getContentId());
	request.setAttribute(EadventureConstants.ATTR_SESSION_MAP_ID, sessionMapID);

	return mapping.findForward(EadventureConstants.SUCCESS);

    }

    //*************************************************************************************
    // Private method 
    //*************************************************************************************
    /**
     * Return eadventure according to ToolAccessMode.
     * 
     * @param request
     * @param sessionMap
     * @param mode
     * @return
     */
    private Eadventure getEadventure(HttpServletRequest request, SessionMap sessionMap, String mode) {
	Eadventure ead = null;
	if (EadventureConstants.MODE_AUTHOR_SESSION.equals(mode)) {
	    //int itemIdx = NumberUtils.stringToInt(request.getParameter(EadventureConstants.PARAM_ITEM_INDEX),0);
	    //authoring: does not save item yet, so only has ItemList from session and identity by Index
	    //List<EadventureItem>  eadventureList = new ArrayList<EadventureItem>(getEadventureItemList(sessionMap));
	    ead = (Eadventure) sessionMap.get(EadventureConstants.ATTR_EADVENTURE);
	} else {
	    //Long contentId = NumberUtils.createLong(request.getParameter(EadventureConstants.PARAM_TOOL_CONTENT_ID));
	    Long contentId = ((Long) sessionMap.get(EadventureConstants.PARAM_TOOL_CONTENT_ID));
	    //get back the eadventure and item list and display them on page
	    IEadventureService service = getEadventureService();
	    ead = service.getEadventureByContentId(contentId);

	}
	return ead;
    }

    private IEadventureService getEadventureService() {
	WebApplicationContext wac = WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServlet().getServletContext());
	return (IEadventureService) wac.getBean(EadventureConstants.RESOURCE_SERVICE);
    }

    private static Pattern wikipediaPattern = Pattern.compile("wikipedia",
	    Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    /**
     * List save current eadventure items.
     * 
     * @param request
     * @return
     */
    /*
     * private SortedSet<EadventureItem> getEadventureItemList(SessionMap sessionMap) {
     * SortedSet<EadventureItem> list = (SortedSet) sessionMap.get(EadventureConstants.ATTR_RESOURCE_ITEM_LIST);
     * if(list == null){
     * list = new TreeSet<EadventureItem>(new EadventureItemComparator());
     * sessionMap.put(EadventureConstants.ATTR_RESOURCE_ITEM_LIST,list);
     * }
     * return list;
     * }
     */

}
