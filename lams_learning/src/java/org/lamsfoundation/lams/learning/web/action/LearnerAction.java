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

package org.lamsfoundation.lams.learning.web.action;

import javax.servlet.http.*;

import org.lamsfoundation.lams.learning.web.bean.SessionBean;
import org.lamsfoundation.lams.web.action.Action;

/** 
 * MyEclipse Struts
 * Creation date: 01-12-2005
 * 
 */
public class LearnerAction extends Action {
	
	/**
	 * Gets the session bean from session.
	 * @return SessionBean for this request, null if no session.
	 */
	protected SessionBean getSessionBean(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return null;
		}
		SessionBean sessionBean = (SessionBean)session.getAttribute(SessionBean.NAME);
		return sessionBean;
	}
	
	/**
	 * Sets the session bean for this session.
	 */
	protected void setSessionBean(SessionBean sessionBean, HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return;
		}
		session.setAttribute(SessionBean.NAME, sessionBean);
	}
	
}