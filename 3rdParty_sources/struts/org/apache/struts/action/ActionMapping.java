/*
 *
 *
 * Copyright 2000-2004 The Apache Software Foundation.
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


package org.apache.struts.action;


import java.util.ArrayList;

import org.apache.struts.config.ActionConfig;
import org.apache.struts.config.ForwardConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * <p>An <strong>ActionMapping</strong> represents the information that the
 * controller, <code>RequestProcessor</code>, knows about the mapping
 * of a particular request to an instance of a particular <code>Action</code> class.
 * The <code>ActionMapping</code> instance used to select a particular
 * <code>Action</code> is passed on to that <code>Action</code>, thereby providing
 * access to any custom configuration information included with the
 * <code>ActionMapping</code> object.</p>
 *
 * <p>Since Struts 1.1 this class extends <code>ActionConfig</code>.
 *
 * <p><strong>NOTE</strong> - This class would have been deprecated and
 * replaced by <code>org.apache.struts.config.ActionConfig</code> except
 * for the fact that it is part of the public API that existing applications
 * are using.</p>
 *
 * @version $Rev: 180001 $ $Date$
 */

public class ActionMapping extends ActionConfig {


    /**
     * <p>Commons Logging instance.</p>
     *
     * @since Struts 1.2.8
     */
    private static Log log = LogFactory.getLog(ActionMapping.class);


    /**
     * <p>Find and return the <code>ForwardConfig</code> instance defining
     * how forwarding to the specified logical name should be handled. This is
     * performed by checking local and then global configurations for the
     * specified forwarding configuration. If no forwarding configuration
     * can be found, return <code>null</code>.</p>
     *
     * @param name Logical name of the forwarding instance to be returned
     */
    public ActionForward findForward(String name) {

        ForwardConfig config = findForwardConfig(name);
        if (config == null) {
            config = getModuleConfig().findForwardConfig(name);
        }
        if (config == null) {
            if (log.isWarnEnabled()) {
                log.warn("Unable to find '" + name + "' forward.");
            }
        }
        return ((ActionForward) config);

    }


    /**
     * <p>Return the logical names of all locally defined forwards for this
     * mapping. If there are no such forwards, a zero-length array
     * is returned.</p>
     */
    public String[] findForwards() {

        ArrayList results = new ArrayList();
        ForwardConfig fcs[] = findForwardConfigs();
        for (int i = 0; i < fcs.length; i++) {
            results.add(fcs[i].getName());
        }
        return ((String[]) results.toArray(new String[results.size()]));

    }


    /**
     * <p>Create (if necessary) and return an {@link ActionForward} that
     * corresponds to the <code>input</code> property of this Action.</p>
     *
     * @since Struts 1.1
     */
    public ActionForward getInputForward() {

        if (getModuleConfig().getControllerConfig().getInputForward()) {
            return (findForward(getInput()));
        } else {
            return (new ActionForward(getInput()));
        }

    }


}
