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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ***********************************************************************/

/**
 * @author Ozgur Demirtas
 * 
 * QaDLStarterAction activates the Define Later module. 
 * It reuses majority of the functionality from existing authoring module.
 * 
 * 	   <!--Define Later Starter Action: initializes the DefineLater module -->
   <action 
   	path="/defineLaterStarter" 
   	type="org.lamsfoundation.lams.tool.qa.web.QaDLStarterAction" 
   	name="QaAuthoringForm" 
   	input="/index.jsp"> 

		<exception
			key="error.exception.QaApplication"
			type="org.lamsfoundation.lams.tool.qa.QaApplicationException"
			handler="org.lamsfoundation.lams.tool.qa.web.CustomStrutsExceptionHandler"
			path="/SystemErrorContent.jsp"
			scope="request"
		/>
		    
		<exception
		    key="error.exception.QaApplication"
		    type="java.lang.NullPointerException"
		    handler="org.lamsfoundation.lams.tool.qa.web.CustomStrutsExceptionHandler"
		    path="/SystemErrorContent.jsp"
		    scope="request"
		/>	         			

	  	<forward
		    name="load"
          	path="/AuthoringMaincontent.jsp"
		    redirect="true"
	  	/>
	  	
	  	<forward
	        name="starter"
	        path="/index.jsp"
	        redirect="true"
	     />
	  
        <forward
          name="loadViewOnly"
          path="/authoring/AuthoringTabsHolder.jsp"
          redirect="false"
        />
	  	
	  	<forward
		    name="errorList"
		    path="/QaErrorBox.jsp"
		    redirect="true"
	  	/>
	</action>  

 * 
  
*/
/* $$Id$$ */
package org.lamsfoundation.lams.tool.qa.web;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.QaApplicationException;
import org.lamsfoundation.lams.tool.qa.QaUtils;
import org.lamsfoundation.lams.tool.qa.service.IQaService;
import org.lamsfoundation.lams.tool.qa.service.QaServiceProxy;


public class QaDLStarterAction extends Action implements QaAppConstants {
	static Logger logger = Logger.getLogger(QaDLStarterAction.class.getName());

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
  								throws IOException, ServletException, QaApplicationException {
		QaUtils.cleanUpSessionAbsolute(request);
		logger.debug("init defineLater mode. removed attributes...");
		
		IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
		request.getSession().setAttribute(TOOL_SERVICE, qaService);
		
	    QaStarterAction qaStarterAction= new QaStarterAction();
	    return qaStarterAction.executeDefineLater(mapping, form, request, response, qaService);
	}
}
