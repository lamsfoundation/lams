//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.4/xslt/JavaClass.xsl

package org.lamsfoundation.lams.learning.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.lamsfoundation.lams.web.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/** 
 * MyEclipse Struts
 * Creation date: 02-03-2005
 * 
 * XDoclet definition:
 * @struts:action path="/test/DblSubmit" name="testDblSubmitForm"
 *                validate="false" scope="request"
 * @struts:action-forward name="display" path="/test/dblSubmitTest.jsp"
 */
public class TestDblSubmitAction extends Action {


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