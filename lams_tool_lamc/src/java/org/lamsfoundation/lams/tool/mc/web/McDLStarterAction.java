/***************************************************************************
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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ***********************************************************************/

/**
 * @author Ozgur Demirtas
 * 
 * McDLStarterAction activates the Define Later module. 
 * It reuses majority of the functionality from existing authoring module.
 * 
   <!--Define Later Starter Action: initializes the DefineLater module -->
   <action path="/defineLaterStarter" 
   			type="org.lamsfoundation.lams.tool.mc.web.McDLStarterAction" 
   			name="McAuthoringForm" 
   			input=".starter"> 
	
		<exception
	        key="error.exception.McApplication"
	        type="org.lamsfoundation.lams.tool.mc.McApplicationException"
	        handler="org.lamsfoundation.lams.tool.mc.web.CustomStrutsExceptionHandler"
	        path=".mcErrorBox"
	        scope="request"
	      />

		<exception
	        key="error.exception.McApplication"
	        type="java.lang.NullPointerException"
	        handler="org.lamsfoundation.lams.tool.mc.web.CustomStrutsExceptionHandler"
	        path=".mcErrorBox"
	        scope="request"
	      />	         			   			

	  	<forward
		    name="load"
		    path=".questions"
		    redirect="true"
	  	/>
	  	
	  	<forward
	        name="starter"
	        path=".starter"
	        redirect="true"
	     />
	  	
	  	<forward
		    name="errorList"
		    path=".mcErrorBox"
		    redirect="true"
	  	/>
	</action>    
  
*/
/* $$Id$$ */
package org.lamsfoundation.lams.tool.mc.web;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.McApplicationException;
import org.lamsfoundation.lams.tool.mc.McUtils;
import org.lamsfoundation.lams.tool.mc.service.IMcService;
import org.lamsfoundation.lams.tool.mc.service.McServiceProxy;


public class McDLStarterAction extends Action implements McAppConstants {
	static Logger logger = Logger.getLogger(McDLStarterAction.class.getName());

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
  								throws IOException, ServletException, McApplicationException {
		McUtils.cleanUpSessionAbsolute(request);
		logger.debug("init defineLater mode. removed attributes...");
		
		IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
		logger.debug("mcService: " + mcService);
	    request.getSession().setAttribute(TOOL_SERVICE, mcService);
	    
	    McStarterAction mcStarterAction= new McStarterAction();
	    return mcStarterAction.executeDefineLater(mapping, form, request, response, mcService);
	}
}
