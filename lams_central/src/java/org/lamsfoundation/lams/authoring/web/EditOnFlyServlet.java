package org.lamsfoundation.lams.authoring.web;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.log4j.Logger;
import org.lamsfoundation.lams.authoring.service.IAuthoringService;
import org.lamsfoundation.lams.util.audit.IAuditService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.exception.UserException;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.util.wddx.FlashMessage;
import org.lamsfoundation.lams.web.servlet.AbstractStoreWDDXPacketServlet;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Edit a learning design (Edit-On-The-Fly)
 * 
 * @author Mitch Seaton
 *
 * @web:servlet name="editLD"
 * @web:servlet-mapping url-pattern="/eof/authoring/editLearningDesign"
 */

public class EditOnFlyServlet extends HttpServlet {

	private static Logger log = Logger.getLogger(EditOnFlyServlet.class);
	private static IAuditService auditService;
	
	private Integer userID = null;
	private Long learningDesignID = null;
	
	public IAuthoringService getAuthoringService(){
		WebApplicationContext webContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
		return (IAuthoringService) webContext.getBean(AuthoringConstants.AUTHORING_SERVICE_BEAN_NAME);		
	}
	
	/**
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest, HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		String returnPacket = null;
		PrintWriter writer = null;
		
		auditService = getAuditService();
		
		 // Set to expire far in the past.
		response.setHeader("Expires", "Sat, 6 May 1995 12:00:00 GMT");

		// Set standard HTTP/1.1 no-cache headers.
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");

		// Set IE extended HTTP/1.1 no-cache headers (use addHeader).
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");

		// Set standard HTTP/1.0 no-cache header.
		response.setHeader("Pragma", "no-cache");
		
		// TODO: Check roles/permissions on the User to see if they have access to edit this design
		userID = getUserID(request);
		learningDesignID =  new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_LEARNINGDESIGN_ID));
		
		try {
			writer = response.getWriter();
			
			returnPacket = doSetup(request, response);
			
		} catch ( Exception e ) {
			// Don't want exceptions flowing back to Flash if we can help it.
			
			FlashMessage flashMessage = FlashMessage.getExceptionOccured(getMessageKey(),
					e.getMessage()!=null?e.getMessage():e.getClass().getName());
			
			if(writer == null)
				writer = response.getWriter();
			
			writer.write(flashMessage.serializeMessage());
			
			auditService.log(EditOnFlyServlet.class.getName(),"Exception triggered: "+e.toString());
			
			return;
		}
		
		//	can't inform Flash if an exception occurs now...
		if ( writer != null && returnPacket != null)
			writer.println(returnPacket);
	}

	/**
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		doGet(request, response);
		
	}
	
	/**
	 * 
	 */
	private String doSetup(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, UserException, IOException {
		String packet = null;
		
		IAuthoringService service = getAuthoringService();
		
		try {
			if(service.setupEditOnFlyLock(learningDesignID, userID))
					packet = service.setupEditOnFlyGate(learningDesignID, userID);
			else
				packet = new FlashMessage("setupEditOnFlyLock", false, FlashMessage.OBJECT_MESSAGE).serializeMessage();
		} catch(Exception e) {
			packet = FlashMessage.getExceptionOccured("doSetup", e.getMessage()).serializeMessage();
		}
		
		return packet;
	}
	
	/**
	 * 
	 */
	private Integer getUserID(HttpServletRequest request) {
		Integer userId = WebUtil.readIntParam(request, AttributeNames.PARAM_USER_ID, true);
		if ( userId == null ) {
			HttpSession session = SessionManager.getSession();
			UserDTO userDto = (UserDTO)session.getAttribute(AttributeNames.USER);
			userId = userDto.getUserID();
		}
		return userId;
	}

	/**
	 * 
	 * @return
	 */
	protected String getMessageKey() {
		return IAuthoringService.START_EDIT_ON_FLY_MESSAGE_KEY;
	}
	
	/**
	 * Get AuditService bean.
	 * @return
	 */
	private IAuditService getAuditService(){
		if(auditService==null){
			WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext());
			auditService = (IAuditService) ctx.getBean("auditService");
		}
		return auditService;
	}
}
