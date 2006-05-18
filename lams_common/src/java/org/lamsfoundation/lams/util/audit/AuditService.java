/****************************************************************
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
 * ****************************************************************
 */
/* $Id$ */

package org.lamsfoundation.lams.util.audit;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/** 
 * Write out audit entries to a log4j based log file. Gets the user details from the shared session. 
*/
/*
 *  Relies on the followig two entries in the log4j configuration file:
 * 
 *   <category name="org.lamsfoundation.lams.util.audit" additivity="false">
 *    <priority value="INFO"/>
 *    <appender-ref ref="AUDITFILE"/>
 *   </category>
 *
 *
 *  <appender name="AUDITFILE" class="org.jboss.logging.appender.DailyRollingFileAppender">
 *   <errorHandler class="org.jboss.logging.util.OnlyOnceErrorHandler"/>
 *   <param name="File" value="${jboss.server.home.dir}/log/audit.log"/>
 *   <param name="Append" value="true"/>
 *   <param name="Threshold" value="INFO"/>
 *
 *   <!-- Rollover at midnight each day -->
 *   <param name="DatePattern" value="'.'yyyy-MM-dd"/>
 *
 *    <layout class="org.apache.log4j.PatternLayout">
 *     <param name="ConversionPattern" value="%d{ABSOLUTE} [%t:%x] %-5p %c - %m%n"/>
 *   </layout>	    
 *   </appender>
 */
public class AuditService implements IAuditService {

	static Logger logger = Logger.getLogger(AuditService.class.getName());

	private final String AUDIT_CHANGE_I18N_KEY = "audit.change.entry";
	private final String AUDIT_HIDE_I18N_KEY = "audit.hide.entry";
	private final String AUDIT_SHOW_I18N_KEY = "audit.show.entry";
	protected MessageService messageService;

	private String getUserString() {
	   	HttpSession ss = SessionManager.getSession();
	   	if ( ss != null ) {
	   		UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	   		if ( user != null ) {
	   			return user.getLogin()+"("+user.getUserID()+"): ";
	   		}
	   	}
	   	return "User unknown (session does not contain user details): ";
	}

	public void log(String moduleName, String message) {
		logger.info(getUserString()+moduleName+": "+message);
	}

	public void logChange(String moduleName, Long originalUserId, String originalUserLogin,
			String originalText, String newText) {
		String[] args = new String[3];
		args[0] = originalUserLogin+"("+originalUserId+")";
		args[1] = originalText;
		args[2] = newText;
		String message = messageService.getMessage(AUDIT_CHANGE_I18N_KEY, args);
		log(moduleName, message);
	}

	public void logHideEntry(String moduleName, Long originalUserId, String originalUserLogin, String hiddenItem) {
		String[] args = new String[3];
		args[0] = originalUserLogin+"("+originalUserId+")";
		args[1] = hiddenItem;
		String message = messageService.getMessage(AUDIT_HIDE_I18N_KEY, args);
		log(moduleName, message);
	}

	public void logShowEntry(String moduleName, Long originalUserId, String originalUserLogin, String hiddenItem) {
		String[] args = new String[3];
		args[0] = originalUserLogin+"("+originalUserId+")";
		args[1] = hiddenItem;
		String message = messageService.getMessage(AUDIT_SHOW_I18N_KEY, args);
		log(moduleName, message);
	}

	/* ***  Spring Injection Methods *************/
	
	public MessageService getMessageService() {
		return messageService;
	}

	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}

}
