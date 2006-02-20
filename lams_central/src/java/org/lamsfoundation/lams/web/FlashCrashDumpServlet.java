package org.lamsfoundation.lams.web;

import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.wddx.FlashMessage;
import org.lamsfoundation.lams.util.wddx.WDDXProcessor;
import org.lamsfoundation.lams.util.wddx.WDDXTAGS;
import org.lamsfoundation.lams.web.servlet.AbstractStoreWDDXPacketServlet;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;


/**
 * Write a WDDX packet out to a log file for bug reporting / troubleshooting.
 * 
 * If the Flash packet contains a variable "crashDataBatch" at the top level of the packet,
 * then the value of this field will be included in the name of the file. This is handy
 * for grouping packets. 
 * 
 * @author Fiona Malikoff
 * 
 * @web:servlet name="flashCrashDump"
 * @web:servlet-mapping url-pattern="/flashCrashDump"

 */
public class FlashCrashDumpServlet extends AbstractStoreWDDXPacketServlet {
	
	private static Logger log = Logger.getLogger(HomeAction.class);

	private static final String MESSAGE_KEY = "flashCrashDump";
	private static final String PREFIX = "Flash_";

	protected String process(String wddxPacket, HttpServletRequest request) 
		throws Exception
		{
			HttpSession ss = SessionManager.getSession();
			UserDTO user = ss !=null ? (UserDTO) ss.getAttribute(AttributeNames.USER) : null;
			if ( user == null ) {
				log.warn("FlashCrashDumpServlet: Attempt to dump file by someone not logged in.");
				return new FlashMessage(MESSAGE_KEY,"User not logged in - unable to dump file.", FlashMessage.ERROR).serializeMessage();
			}
			
			String id = PREFIX + user.getLogin(); 
			try {
				// attempt to get the special Flash flag which groups packets together - not
				// all the crash dump data can come in one packet as Flash can't cope with large
				// packets.
				Hashtable table = (Hashtable)WDDXProcessor.deserialize(wddxPacket);
				String batch = (String) table.get(WDDXTAGS.CRASH_DUMP_BATCH);
				log.debug("FlashCrashDumpServlet: batch value is "+batch);
				if ( batch != null ) {
					batch = StringUtils.deleteWhitespace(batch);
					id = id + "_" + batch;
				}
			} catch ( Exception e ) {
				log.warn("FlashCrashDumpServlet: Unable to deserialize packet - invalid data. Just writing out packet "+id,e);
			}
			
			String filename = FileUtil.createDumpFile(wddxPacket.getBytes(), id, "xml");
			FlashMessage flashMessage = new FlashMessage(MESSAGE_KEY,filename);
			return flashMessage.serializeMessage(); 		
		}
	
	protected String getMessageKey(String designDetails, HttpServletRequest request) {
		return MESSAGE_KEY;
	}

}