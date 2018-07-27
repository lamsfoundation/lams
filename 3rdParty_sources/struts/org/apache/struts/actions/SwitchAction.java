/*
 *
 *
 * Copyright 2002-2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.struts.actions;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.ModuleUtils;

/**
 * <p>A standard <strong>Action</strong> that switches to a new module
 * and then forwards control to a URI (specified in a number of possible ways)
 * within the new module.</p>
 *
 * <p>Valid request parameters for this Action are:</p>
 * <ul>
 * <li><strong>page</strong> - Module-relative URI (beginning with "/")
 *     to which control should be forwarded after switching.</li>
 * <li><strong>prefix</strong> - The module prefix (beginning with "/")
 *     of the module to which control should be switched.  Use a
 *     zero-length string for the default module.  The
 *     appropriate <code>ModuleConfig</code> object will be stored as a
 *     request attribute, so any subsequent logic will assume the new
 *     module.</li>
 * </ul>
 *
 * @version $Rev: 54929 $ $Date$
 * @since Struts 1.1
 */
public class SwitchAction extends Action {


    // ----------------------------------------------------- Instance Variables


    /**
     * Commons Logging instance.
     */
    protected static Log log = LogFactory.getLog(SwitchAction.class);


    /**
     * The message resources for this package.
     */
    protected static MessageResources messages =
        MessageResources.getMessageResources
        ("org.apache.struts.actions.LocalStrings");


    // --------------------------------------------------------- Public Methods


    // See superclass for JavaDoc
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
        throws Exception {

        // Identify the request parameters controlling our actions
        String page = request.getParameter("page");
        String prefix = request.getParameter("prefix");
        if ((page == null) || (prefix == null)) {
            String message = messages.getMessage("switch.required");
            log.error(message);
            throw new ServletException(message);
        }

        // Switch to the requested module
        ModuleUtils.getInstance().selectModule(prefix, request, getServlet().getServletContext());
        
        if (request.getAttribute(Globals.MODULE_KEY) == null) {
            String message = messages.getMessage("switch.prefix", prefix);
            log.error(message);
            throw new ServletException(message);
        }

        // Forward control to the specified module-relative URI
        return (new ActionForward(page));

    }


}
