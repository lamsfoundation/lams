/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ************************************************************************
 */
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
import org.apache.struts.actions.DispatchAction;
import org.lamsfoundation.lams.authoring.service.IAuthoringService;
import org.lamsfoundation.lams.learningdesign.exception.LearningDesignException;
import org.lamsfoundation.lams.usermanagement.exception.UserException;
import org.lamsfoundation.lams.usermanagement.exception.WorkspaceFolderException;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.wddx.FlashMessage;
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
public class AuthoringAction extends DispatchAction{
	
	private static Logger log = Logger.getLogger(AuthoringAction.class);

	/** If you want the output given as a jsp, set the request parameter "jspoutput" to 
     * some value other than an empty string (e.g. 1, true, 0, false, blah). 
     * If you want it returned as a stream (ie for Flash), do not define this parameter
     */  
	public static String USE_JSP_OUTPUT = "jspoutput";
	
	/** Id of theme to be retrieved from the db */
	public static final String THEME_ID_PARAMETER = "themeID";
		
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
		Long learningDesignID = new Long(WebUtil.readLongParam(request,"learningDesignID"));
		IAuthoringService authoringService = getAuthoringService();
		String wddxPacket = authoringService.getLearningDesignDetails(learningDesignID);
		return outputPacket(mapping, request, response, wddxPacket, "details");
	}
	
	public ActionForward getLearningDesignsForUser(ActionMapping mapping,
											ActionForm form,
											HttpServletRequest request,
											HttpServletResponse response)throws ServletException, IOException{
		Long userID = new Long(WebUtil.readLongParam(request,"userID"));
		IAuthoringService authoringService = getAuthoringService();
		String wddxPacket = authoringService.getLearningDesignsForUser(userID);
		return outputPacket(mapping, request, response, wddxPacket, "details");
	}
	public ActionForward getAllLearningDesignDetails(ActionMapping mapping,
											ActionForm form,
											HttpServletRequest request,
											HttpServletResponse response)throws ServletException, IOException{
		IAuthoringService authoringService = getAuthoringService();
		String wddxPacket = authoringService.getAllLearningDesignDetails();
		return outputPacket(mapping, request, response, wddxPacket, "details");
	}

	public ActionForward getAllLearningLibraryDetails(ActionMapping mapping,
													  ActionForm form,
													  HttpServletRequest request,
													  HttpServletResponse response)throws ServletException, IOException{
		IAuthoringService authoringService = getAuthoringService();
		String wddxPacket = authoringService.getAllLearningLibraryDetails();
		return outputPacket(mapping, request, response, wddxPacket, "details");
	}
	

	
	/**
	 * Returns a string representing the requested theme in WDDX format
	 * 
	 * @param learningDesignID The learning_design_id of the design whose WDDX packet is requested 
	 * @return String The requested LearningDesign in WDDX format
	 * @throws Exception
	 */
	public ActionForward getTheme(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)throws ServletException, Exception{

		Long themeId = new Long(WebUtil.readLongParam(request,THEME_ID_PARAMETER));
		IAuthoringService authoringService = getAuthoringService();
		String message = authoringService.getTheme(themeId);
		request.getSession().setAttribute("message",message);
		return outputPacket(mapping, request, response, message, "message");
	}


	/**
	 * This method returns a list of all available themes in
	 * WDDX format. We need to work out if this should be restricted
	 * by user.
	 * 
	 * @return String The required information in WDDX format
	 * @throws IOException
	 */
	public ActionForward getThemes(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)throws ServletException, Exception{
	    
	    IAuthoringService authoringService = getAuthoringService();
	    String message = authoringService.getThemes();
	    request.getSession().setAttribute("message",message);
	    return outputPacket(mapping, request, response, message, "message");
	}
	
	public ActionForward getToolContentID(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
	 
	    Long toolID = new Long(WebUtil.readLongParam(request,"toolID"));
	    IAuthoringService authoringService = getAuthoringService();
	    String message = authoringService.getToolContentID(toolID);
	    return outputPacket(mapping, request, response, message, "details");   
	    
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
			flashMessage = new FlashMessage("getAvailableLicenses",
					"License details unavailable due to system error :" + e.getMessage(),
					FlashMessage.ERROR);
		}
		
        PrintWriter writer = response.getWriter();
        writer.println(flashMessage.serializeMessage());
        return null;
	}	
	

}
