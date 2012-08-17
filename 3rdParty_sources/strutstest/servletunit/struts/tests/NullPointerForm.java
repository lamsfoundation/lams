package servletunit.struts.tests;

import org.apache.struts.action.*;
import javax.servlet.http.*;

/**
 * An Action Form that always throws a NullPointerAction during validation.
 * Used in unit testing
 * @author Sean Pritchard
 * @version 1.0
 */
public class NullPointerForm extends ActionForm {

    public NullPointerForm() {
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request){
        throw new NullPointerException("NullPointer during validation");
    }
}