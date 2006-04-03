/****************************************************************
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
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.web.session;

import java.io.IOException;

import org.apache.catalina.authenticator.FormAuthenticator;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.deploy.LoginConfig;
/**
 * This class is special for JBOSS/Tomcat.
 * 
 * The resson is JBOSS ignores any Filter if j_security_checks submit. It becomes impossilble to preset the current
 * session ID to UniversialLoginModule. This class will replace default <code>SystemSessionFilter</code> setting.
 * 
 * @author Steve.Ni
 * 
 * @version $Revision$
 */
public class LoginFormAuthenticator extends FormAuthenticator{

	public boolean authenticate(Request request, Response response, LoginConfig config) throws IOException {
		String uri = request.getRequestURI();
		boolean result;
		//only when URI is j_security_check, execute the shared session initialize. 
		//Otherwise, the shared session initializtion will run in Filter.
		if(uri.endsWith("j_security_check")){
			SessionManager.startSession(request,response);
			result = super.authenticate(request, response, config);
			SessionManager.endSession();
		}else
			result = super.authenticate(request, response, config);
			
		return result;
	}

	
}
