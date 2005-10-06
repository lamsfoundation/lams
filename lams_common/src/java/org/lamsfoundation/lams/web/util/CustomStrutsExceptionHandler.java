
package org.lamsfoundation.lams.web.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;


/**
 * @author Jacky
 * 
 */
public class CustomStrutsExceptionHandler extends ExceptionHandler
{

    //---------------------------------------------------------------------
    // Instance Data
    //---------------------------------------------------------------------
    private static Logger logger = Logger.getLogger(CustomStrutsExceptionHandler.class);
    private static final String UNKNOWN_EXCEPTION = "Unknown runtime exception!";

    // commons logging reference
    /**
     * Handle the exception. Standard execute method with addition of logging
     * the stacktrace.
     */
    public ActionForward execute(Exception ex,
                                 ExceptionConfig ae,
                                 ActionMapping mapping,
                                 ActionForm formInstance,
                                 HttpServletRequest request,
                                 HttpServletResponse response) 
    {
        // write the exception information to the log file
        logger.fatal("fatal System exception: [" + ex.getMessage() + "] ", ex);
        ActionForward forward = null;

        String property = null;

        /*
         * Get the path for the forward either from the exception element or
         * from the input attribute.
         */

        String path = null;
        if (ae.getPath() != null)
        {
            path = ae.getPath();
        }
        else
        {
            path = mapping.getInput();
        }
        // Construct the forward object
        forward = new ActionForward(path);

        String errorMessage = ex.getMessage().equals("null")?UNKNOWN_EXCEPTION:ex.getMessage();
        ActionError error = new ActionError( "error.system.survey", errorMessage );
        
        storeException(request, property, error, forward, ae.getScope( ));
        // process the exception as normal
        return forward;
    }

}