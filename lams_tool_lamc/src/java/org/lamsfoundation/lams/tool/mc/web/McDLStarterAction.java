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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
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
   <action 
   	path="/defineLaterStarter" 
   	type="org.lamsfoundation.lams.tool.mc.web.McDLStarterAction" 
   	name="McAuthoringForm" 
	scope="request"
   	input="/index.jsp"> 

	  	<forward
		    name="load"
          	path="/AuthoringMaincontent.jsp"
		    redirect="false"
	  	/>
	  	
	  	<forward
	        name="starter"
	        path="/index.jsp"
	        redirect="false"
	     />
	  
        <forward
          name="loadViewOnly"
          path="/authoring/AuthoringTabsHolder.jsp"
          redirect="false"
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
import org.lamsfoundation.lams.tool.mc.service.IMcService;
import org.lamsfoundation.lams.tool.mc.service.McServiceProxy;


public class McDLStarterAction extends Action implements McAppConstants {
	static Logger logger = Logger.getLogger(McDLStarterAction.class.getName());

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
  								throws IOException, ServletException, McApplicationException {
		McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;
		
		IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
		mcAuthoringForm.setMcService(mcService);
		
	    McStarterAction mcStarterAction= new McStarterAction();
	    return mcStarterAction.executeDefineLater(mapping, mcAuthoringForm, request, response, mcService);
	}
}
