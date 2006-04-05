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
package org.lamsfoundation.lams.learning.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.web.action.LamsAction;

/** 
 * MyEclipse Struts
 * Creation date: 02-03-2005
 * 
 * XDoclet definition:
 * @struts:action path="/test/DblSubmit" name="testDblSubmitForm"
 *                validate="false" scope="request"
 * @struts:action-forward name="display" path="/test/dblSubmitTest.jsp"
 */
public class TestDblSubmitAction extends LamsAction {


    public ActionForward execute(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response) {

        HttpSession session = request.getSession();
        
        boolean valid = this.isTokenValid(request, true);
        request.setAttribute("doubleSubmit", String.valueOf(valid));

        if (valid) {
            // simulate processing time
            try {
                Thread.sleep(1000);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
        }
        
        this.saveToken(request);
        ActionForward forward = mapping.findForward("display");
        return forward;
    }

}