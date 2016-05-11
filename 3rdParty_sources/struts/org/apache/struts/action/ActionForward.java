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


import org.apache.struts.config.ForwardConfig;


/**
 * <p>An <strong>ActionForward</strong> represents a destination to which the
 * controller, <code>RequestProcessor</code>, might be directed to
 * perform a <code>RequestDispatcher.forward</code> or
 * <code>HttpServletResponse.sendRedirect</code> to, as a result of
 * processing activities of an <code>Action</code> class. Instances of this
 * class may be created dynamically as necessary, or configured in association
 * with an <code>ActionMapping</code> instance for named lookup of potentially
 * multiple destinations for a particular mapping instance.</p>
 *
 * <p>An <code>ActionForward</code> has the following minimal set of properties.
 * Additional properties can be provided as needed by subclassses.</p>
 * <ul>
 * <li><strong>contextRelative</strong> - Should the <code>path</code>
 *     value be interpreted as context-relative (instead of
 *     module-relative, if it starts with a '/' character? [false]</li>
 * <li><strong>name</strong> - Logical name by which this instance may be
 *     looked up in relationship to a particular <code>ActionMapping</code>.
 *     </li>
 * <li><strong>path</strong> - Module-relative or context-relative URI to
 *     which control should be forwarded, or an absolute or relative URI to
 *     which control should be redirected.</li>
 * <li><strong>redirect</strong> - Set to <code>true</code> if the controller
 *     servlet should call <code>HttpServletResponse.sendRedirect()</code>
 *     on the associated path; otherwise <code>false</code>.  [false]</li>
 * </ul>
 *
 * <p>Since Struts 1.1 this class extends <code>ForwardConfig</code>
 * and inherits the <code>contextRelative</code> property.
 *
 * <p><strong>NOTE</strong> - This class would have been deprecated and
 * replaced by <code>org.apache.struts.config.ForwardConfig</code> except
 * for the fact that it is part of the public API that existing applications
 * are using.</p>
 *
 * @version $Rev: 54929 $ $Date$
 */

public class ActionForward extends ForwardConfig {


    /**
     * <p>Construct a new instance with default values.</p>
     */
    public ActionForward() {

        this(null, false);

    }


    /**
     * <p>Construct a new instance with the specified path.</p>
     *
     * @param path Path for this instance
     */
    public ActionForward(String path) {

        this(path, false);

    }


    /**
     * <p>Construct a new instance with the specified
     * <code>path</code> and <code>redirect</code> flag.</p>
     *
     * @param path Path for this instance
     * @param redirect Redirect flag for this instance
     */
    public ActionForward(String path, boolean redirect) {

        super();
        setName(null);
        setPath(path);
        setRedirect(redirect);

    }


    /**
     * <p>Construct a new instance with the specified <code>name</code>,
     * <code>path</code> and <code>redirect</code> flag.</p>
     *
     * @param name Name of this instance
     * @param path Path for this instance
     * @param redirect Redirect flag for this instance
     */
    public ActionForward(String name, String path, boolean redirect) {

        super();
        setName(name);
        setPath(path);
        setRedirect(redirect);

    }


    /**
     * <p>Construct a new instance with the specified values.</p>
     *
     * @param name Name of this instance
     * @param path Path for this instance
     * @param redirect Redirect flag for this instance
     * @param contextRelative Context relative flag for this instance
     * @deprecated Use module rather than contextRelative
     */
    public ActionForward(String name, String path, boolean redirect,
                         boolean contextRelative) {

        super();
        setName(name);
        setPath(path);
        setRedirect(redirect);
        setContextRelative(contextRelative);

    }


    /**
     * Construct a new instance with the specified values.
     *
     * @param name Name of this forward
     * @param path Path to which control should be forwarded or redirected
     * @param redirect Should we do a redirect?
     * @param module Module prefix, if any
     */
    public ActionForward(String name, String path, boolean redirect,
                         String module) {

        super();
        setName(name);
        setPath(path);
        setRedirect(redirect);
        setModule(module);

    }


    /**
     * <p>Construct a new instance based on the values of another ActionForward.</p>
     *
     * @param copyMe An ActionForward instance to copy
     * @since Struts 1.2.1
     */
    public ActionForward(ActionForward copyMe) {
        this(copyMe.getName(),copyMe.getPath(),copyMe.getRedirect(),copyMe.getModule());
        setContextRelative(copyMe.getContextRelative());
    }

}
