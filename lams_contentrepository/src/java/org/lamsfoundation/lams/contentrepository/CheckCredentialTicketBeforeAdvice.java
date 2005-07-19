/* 
  Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation; either version 2 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
  USA

  http://www.gnu.org/licenses/gpl.txt 
*/

package org.lamsfoundation.lams.contentrepository;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.service.IRepositoryAdmin;
import org.springframework.aop.MethodBeforeAdvice;

/** 
 * Ticket checking functionality for implementers of IRepositoryAdmin.
 *
 * All calls to IRepositoryAdmin must have either ICredential or ITicket as
 * their first parameter. In any case, the parameter must not be null.
 * 
 * If the first argument is missing then it throws an AccessDeniedException
 *
 * @author Fiona Malikoff 
 */
public class CheckCredentialTicketBeforeAdvice implements MethodBeforeAdvice {

	protected Logger log = Logger.getLogger(CheckCredentialTicketBeforeAdvice.class);

	/** @throws AccessDeniedException if the first parameter is null or it is an unrecognised ticket.
	 *  @throws RuntimeRepositoryException if the method has as its first parameter an object that is not
	 *    an ICredential or an ITicket
	 */
	public void before(Method m, Object[] args, Object target) 
			throws AccessDeniedException,	RepositoryRuntimeException {
		// assume that the first argument is the ticket or credential

		if ( log.isDebugEnabled() ) {
			log.debug("Method "
					+( m!=null ? m.getName() : "null")
					+" Checking credential/ticket "
					+( args != null && args.length > 0 ? args[0] : "null"));
		}

		if ( m!=null && "toString".equals(m.getName()) ) {
		    // don't check toString - let it through
		    return;
		}
		
		if ( args == null || args[0] == null ) { 

			throw new AccessDeniedException("No ticket/credential supplied. Access to repository denied.");

		} else {

			Object obj = args[0];
			if ( ITicket.class.isInstance(obj) ) {

				IRepositoryAdmin repository = (IRepositoryAdmin) target;
				if ( ! repository.isTicketOkay((ITicket) obj) ) {
					log.error("Supplied ticket not recognised. It may have timed out. Please log in again.");
					throw new AccessDeniedException("Supplied ticket not recognised. It may have timed out. Please log in again.");
				}

			} else if ( ! ICredentials.class.isInstance(obj) ) {
				
				String error = "Method has wrong signature. Method "
					+( m!=null ? m.getName() : "null")
					+" has CheckCredentialTicketBeforeAdvice applied to it, but the first argument is a "
					+obj.getClass().getName()
					+". It must be either a ICredential or a ITicket";
				log.error(error);
				throw new RepositoryRuntimeException(error);
				
			}
		}
	}

}

