/***************************************************************************
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
 * ************************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.authoring.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.authoring.service.IAuthoringService;
import org.lamsfoundation.lams.util.FileUtilException;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.audit.IAuditService;
import org.lamsfoundation.lams.util.wddx.FlashMessage;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author Manpreet Minhas
 * 
 * @struts.action name = "AuthoringAction"
 * 				  path = "/authoring/author"
 * 				  parameter = "method"
 * 				  validate = "false"
 * @struts.action-forward name = "success" path = "/index.jsp"
 *    
 */
public class AuthoringAction extends LamsDispatchAction{
	
	private static Logger log = Logger.getLogger(AuthoringAction.class);

	/** If you want the output given as a jsp, set the request parameter "jspoutput" to 
     * some value other than an empty string (e.g. 1, true, 0, false, blah). 
     * If you want it returned as a stream (ie for Flash), do not define this parameter
     */  
	public static String USE_JSP_OUTPUT = "jspoutput";
	
	private static IAuditService auditService;
	
	public IAuthoringService getAuthoringService(){
		WebApplicationContext webContext = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServlet().getServletContext());
		return (IAuthoringService) webContext.getBean(AuthoringConstants.AUTHORING_SERVICE_BEAN_NAME);		
	}
	
	/** Output the supplied WDDX packet. If the request parameter USE_JSP_OUTPUT
	 * is set, then it sets the session attribute "parameterName" to the wddx packet string.
	 * If  USE_JSP_OUTPUT is not set, then the packet is written out to the 
	 * request's PrintWriter.
	 *   
	 * @param mapping action mapping (for the forward to the success jsp)
	 * @param request needed to check the USE_JSP_OUTPUT parameter
	 * @param response to write out the wddx packet if not using the jsp
	 * @param wddxPacket wddxPacket or message to be sent/displayed
	 * @param parameterName session attribute to set if USE_JSP_OUTPUT is set
	 * @throws IOException
	 */
	private ActionForward outputPacket(ActionMapping mapping, HttpServletRequest request, HttpServletResponse response,
	        		String wddxPacket, String parameterName) throws IOException {
	    String useJSP = WebUtil.readStrParam(request, USE_JSP_OUTPUT, true);
	    if ( useJSP != null && useJSP.length() >= 0 ) {
		    request.getSession().setAttribute(parameterName,wddxPacket);
		    return mapping.findForward("success");
	    } else {
	        PrintWriter writer = response.getWriter();
	        writer.println(wddxPacket);
	        return null;
	    }
	}
	
	public ActionForward getLearningDesignDetails(ActionMapping mapping,
								   ActionForm form,
								   HttpServletRequest request,
								   HttpServletResponse response)throws ServletException, IOException{
		String wddxPacket;
		IAuthoringService authoringService = getAuthoringService();
		try {
			Long learningDesignID = new Long(WebUtil.readLongParam(request,"learningDesignID"));
			wddxPacket = authoringService.getLearningDesignDetails(learningDesignID);
		} catch (Exception e) {
			wddxPacket = handleException(e, "getLearningDesignDetails", authoringService).serializeMessage();
		}
		return outputPacket(mapping, request, response, wddxPacket, "details");
	}
	
	public ActionForward getLearningDesignsForUser(ActionMapping mapping,
											ActionForm form,
											HttpServletRequest request,
											HttpServletResponse response)throws ServletException, IOException{
		String wddxPacket;
		IAuthoringService authoringService = getAuthoringService();
		try {
			Long userID = new Long(WebUtil.readLongParam(request,"userID"));
			wddxPacket = authoringService.getLearningDesignsForUser(userID);
		} catch (Exception e) {
			wddxPacket = handleException(e, "getLearningDesignsForUser", authoringService).serializeMessage();
		}
		return outputPacket(mapping, request, response, wddxPacket, "details");
	}
	public ActionForward getAllLearningDesignDetails(ActionMapping mapping,
											ActionForm form,
											HttpServletRequest request,
											HttpServletResponse response)throws ServletException, IOException{
		String wddxPacket;
		IAuthoringService authoringService = getAuthoringService();
		try {		
			wddxPacket = authoringService.getAllLearningDesignDetails();
		} catch (Exception e) {
			wddxPacket = handleException(e, "getAllLearningDesignDetails", authoringService).serializeMessage();
		}
		log.debug("getAllLearningDesignDetails: returning "+wddxPacket);
		return outputPacket(mapping, request, response, wddxPacket, "details");
	}

	public ActionForward getAllLearningLibraryDetails(ActionMapping mapping,
													  ActionForm form,
													  HttpServletRequest request,
													  HttpServletResponse response)throws ServletException, IOException{
		String wddxPacket;
		IAuthoringService authoringService = getAuthoringService();
		try {
			wddxPacket = authoringService.getAllLearningLibraryDetails();
		} catch (Exception e) {
			wddxPacket = handleException(e, "getAllLearningLibraryDetails", authoringService).serializeMessage();
		}
		log.debug("getAllLearningLibraryDetails: returning "+wddxPacket);
		return outputPacket(mapping, request, response, wddxPacket, "details");
	}
	
	public ActionForward getToolContentID(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
	 
		String wddxPacket;
		IAuthoringService authoringService = getAuthoringService();
		try {
		    Long toolID = new Long(WebUtil.readLongParam(request,"toolID"));
		    wddxPacket = authoringService.getToolContentID(toolID);
		} catch (Exception e) {
			wddxPacket = handleException(e, "getAllLearningLibraryDetails", authoringService).serializeMessage();
		}
	    return outputPacket(mapping, request, response, wddxPacket, "details");   
	    
	}
	
	/** Copy some existing content. Used when the user copies an activity in authoring.
	 * Expects one parameters - toolContentId (the content to be copied) */
	public ActionForward copyToolContent(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
	 
		String wddxPacket;
		IAuthoringService authoringService = getAuthoringService();
		try {
		    long toolContentID = WebUtil.readLongParam(request,AttributeNames.PARAM_TOOL_CONTENT_ID);
		    wddxPacket = authoringService.copyToolContent(toolContentID);
		} catch (Exception e) {
			wddxPacket = handleException(e, "copyToolContent", authoringService).serializeMessage();
		}
	    return outputPacket(mapping, request, response, wddxPacket, "details");   
	    
	}
	/**
	 * This method returns a list of all available license in
	 * WDDX format. 
	 * 
	 * This will include our supported Creative Common
	 * licenses and an "OTHER" license which may be used for user entered license details.
	 * The picture url supplied should be a full URL i.e. if it was a relative URL in the 
	 * database, it should have been converted to a complete server URL (starting http://)
	 * before sending to the client.
	 * 
	 * @return String The required information in WDDX format
	 * @throws IOException
	 */
	public ActionForward getAvailableLicenses(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)throws ServletException, Exception{

		FlashMessage flashMessage = null;
    	try {
		    IAuthoringService authoringService = getAuthoringService();
		    Vector licenses = authoringService.getAvailableLicenses();
    		flashMessage = new FlashMessage("getAvailableLicenses",licenses);
		} catch (Exception e) {
			log.error("getAvailableLicenses: License details unavailable due to system error.",e);
			flashMessage = new FlashMessage("getAvailableLicenses",
					"License details unavailable due to system error :" + e.getMessage(),
					FlashMessage.ERROR);
			
			getAuditService().log(AuthoringAction.class.getName(), e.toString());
		}
		
        PrintWriter writer = response.getWriter();
        writer.println(flashMessage.serializeMessage());
        return null;
	}	
	
	public ActionForward createUniqueContentFolder(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)throws ServletException, Exception{

		String wddxPacket;
		IAuthoringService authoringService = getAuthoringService();
    	
		try {
			wddxPacket = authoringService.generateUniqueContentFolder();
		} catch (FileUtilException fue) {
			wddxPacket = handleException(fue, "createUniqueContentFolder", authoringService).serializeMessage();
		} catch (Exception e) {
			wddxPacket = handleException(e, "createUniqueContentFolder", authoringService).serializeMessage();
		} 
    	 
    	PrintWriter writer = response.getWriter();
        writer.println(wddxPacket);
        return null;
 	}	
	
	public ActionForward getHelpURL(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)throws ServletException, Exception{

		String wddxPacket;
		IAuthoringService authoringService = getAuthoringService();
		try {
			wddxPacket = authoringService.getHelpURL();
		} catch (Exception e) {
			wddxPacket = handleException(e, "getHelpURL", authoringService).serializeMessage();
		} 
		
		PrintWriter writer = response.getWriter();
        writer.println(wddxPacket);
        return null;
	}
	/**
	 * Handle flash error.
	 * @param e
	 * @param methodKey
	 * @param monitoringService
	 * @return
	 */
	  private FlashMessage handleException(Exception e, String methodKey, IAuthoringService authoringService) {
		log.error("Exception thrown "+methodKey,e);
		getAuditService().log(AuthoringAction.class.getName()+":"+methodKey, e.toString());

		String[] msg = new String[1];
		msg[0] = e.getMessage();
		return new FlashMessage(methodKey,
			authoringService.getMessageService().getMessage("error.system.error", msg),
			FlashMessage.CRITICAL_ERROR);
    }
	  
	/**
	 * Get AuditService bean.
	 * @return
	 */
	private IAuditService getAuditService(){
		if(auditService==null){
			WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
			auditService = (IAuditService) ctx.getBean("auditService");
		}
		return auditService;
	}

}
