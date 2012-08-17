/*
 * User: dxseale
 * Date: Nov 7, 2002
 */
package servletunit.struts.tests;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

public class ComplexForm extends ActionForm {

    private Object complexObject;
    private String username;
    private String password;

    public String getUsername() {
        if (username != null)
            return username;
        else
            return "";
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        if (password != null)
            return password;
        else
            return "";
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Object getComplexObject() {
        return complexObject;
    }

    public void setComplexObject(Object complexObject) {
        this.complexObject = complexObject;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        String testFlag = request.getParameter("test.reset");
        if ((testFlag != null) && (testFlag.equals("true"))) {
            this.setComplexObject(null);
            this.setPassword(null);
            this.setUsername(null);
        }
    }


}
