package org.lamsfoundation.lams.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.wddx.FlashMessage;
import org.lamsfoundation.lams.web.servlet.AbstractStoreWDDXPacketServlet;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;


/**
 * Write a WDDX packet out to a log file for bug reporting / troubleshooting.
 * 
 * @author Fiona Malikoff
 * 
 * @web:servlet name="flashCrashDump"
 * @web:servlet-mapping url-pattern="/flashCrashDump"

 */
public class FlashCrashDumpServlet extends AbstractStoreWDDXPacketServlet {
	
	private static final String MESSAGE_KEY = "flashCrashDump";
	private static final String PREFIX = "Flash_";

	protected String process(String wddxPacket, HttpServletRequest request) 
		throws Exception
		{
			HttpSession ss = SessionManager.getSession();
			UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
			String id = PREFIX + ( user != null ? user.getLastName() : "unknown") + "_"; 
			
			String filename = FileUtil.createDumpFile(wddxPacket.getBytes(), id);
			FlashMessage flashMessage = new FlashMessage(MESSAGE_KEY,filename);
			return flashMessage.serializeMessage(); 		
		}
	
	protected String getMessageKey(String designDetails, HttpServletRequest request) {
		return MESSAGE_KEY;
	}

}