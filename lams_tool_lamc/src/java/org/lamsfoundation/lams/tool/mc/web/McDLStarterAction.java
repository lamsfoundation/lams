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
		    name="errorList"
		    path=".mcErrorBox"
		    redirect="true"
	  	/>
	</action>  
  
*/
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
		McStarterAction mcStarterAction= new McStarterAction();

		IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	    request.getSession().setAttribute(TOOL_SERVICE, mcService);
		
	    return mcStarterAction.executeDefineLater(mapping, form, request, response, mcService);
	}
}
