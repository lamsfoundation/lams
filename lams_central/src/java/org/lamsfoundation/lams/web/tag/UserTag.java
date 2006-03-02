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
 * ***********************************************************************/

package org.lamsfoundation.lams.web.tag;

import java.io.IOException;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.apache.commons.beanutils.PropertyUtils;

/**
 * Output a property from the userDTO object in the shared session.
 * 
 * Has a single parameter "property", which is the name of the property from the UserDTO to be accessed.
 * May be: 	userID, firstName, lastName, login, email.
 * 
 * Doesn't support theme yet - to be added when we work out what we want from the theme details.
 * 
 * @jsp.tag name="user"
 * 			body-content="empty"
 * 			display-name="user details"
 * 			description="Output details from the shared session UserDTO object"
 * 	
 * @author Fiona Malikoff
 */
public class UserTag extends TagSupport {

	private static final long serialVersionUID = -2801719186682639858L;

	private static final Logger log = Logger.getLogger(UserTag.class);

	/** The property to be output */
	private String property = null;
	/**
	 * 
	 */
	public UserTag() {
		super();
	}
	
	public int doStartTag() throws JspException {
	   	HttpSession ss = SessionManager.getSession();
	   	if ( ss != null ) {
	   		UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	   		if ( user != null ) {
	   			processProperty(user);
	   		} else {
		   		log.warn("UserTag unable to access user details as userDTO is missing. Session is "+ss);
	   		}
	   	} else {
	   		log.warn("UserTag unable to access user details as shared session is missing");
	   	}
	   	
    	return SKIP_BODY;
	}

	public int doEndTag() {
		return EVAL_PAGE;
	}
	
	private void processProperty(UserDTO user) throws JspException {
		try {

			if ( property != null ) {
				Object value = null;
				
		        try {
		            value = PropertyUtils.getProperty(user, property);
		        } catch (Exception e) {
					log.warn("UserTag unable to write out user details due to exception while accessing property value. User id "+user.getUserID(), e);
		        }

		        if ( value != null ) {
		        	JspWriter writer = pageContext.getOut();
		        	writer.print(value);
		        }
				
			}
			
		} catch ( IOException e ) {
				log.error("UserTag unable to write out user details due to IOException. User id "+user.getUserID(), e);
				throw new JspException(e);
		}
	}
	/**
	 * @jsp.attribute required="true" 
	 * 		rtexprvalue="true" 
	 * 		type="String"
	 * 		description="Property of UserDTO to be accessed."
	 * 
	 * @return Returns the property.
	 */
	public String getProperty() {
		return property;
	}
	/**
	 * @param property The property to set.
	 */
	public void setProperty(String property) {
		this.property = property;
	}
}
