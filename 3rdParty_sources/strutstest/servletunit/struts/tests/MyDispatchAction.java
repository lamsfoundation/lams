/*
 * User: dxseale
 * Date: Nov 8, 2002
 */
package servletunit.struts.tests;

import org.apache.struts.actions.DispatchAction;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;

public class MyDispatchAction extends DispatchAction {

    public ActionForward actionOne(ActionMapping mapping,
                                   ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response)
            throws IOException, ServletException
    {
        return mapping.findForward("action1");
    }

    public ActionForward actionTwo(ActionMapping mapping,
                                   ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response)
            throws IOException, ServletException
    {
        return mapping.findForward("action2");
    }

    public ActionForward actionThree(ActionMapping mapping,
                                   ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response)
            throws IOException, ServletException
    {
        return new ActionForward(mapping.getInput());
    }

}
