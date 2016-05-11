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

import java.io.Serializable;


/**
 * <p>An encapsulation of an individual error message returned by the
 * <code>validate</code> method of an <code>ActionForm</code>, consisting
 * of a message key (to be used to look up message text in an appropriate
 * message resources database) plus up to four placeholder objects that can
 * be used for parametric replacement in the message text.</p>
 *
 * <p>The placeholder objects are referenced in the message text using the same
 * syntax used by the JDK <code>MessageFormat</code> class. Thus, the first
 * placeholder is '{0}', the second is '{1}', etc.</p>
 *
 * <p>Since Struts 1.1 <code>ActionError</code> extends <code>ActionMessage</code>.
 *
 * @version $Rev: 54929 $ $Date$
 * @deprecated Please use <code>ActionMessage</code> instead, deprecated since 1.2.0.
 */
public class ActionError extends ActionMessage implements Serializable {


    // ----------------------------------------------------------- Constructors


    /**
     * <p>Construct an action error with no replacement values.</p>
     *
     * @param key Message key for this error message
     */
    public ActionError(String key) {

        super(key);

    }


    /**
     * <p>Construct an action error with the specified replacement values.</p>
     *
     * @param key Message key for this error message
     * @param value0 First replacement value
     */
    public ActionError(String key, Object value0) {

        super(key, value0);

    }


    /**
     * <p>Construct an action error with the specified replacement values.</p>
     *
     * @param key Message key for this error message
     * @param value0 First replacement value
     * @param value1 Second replacement value
     */
    public ActionError(String key, Object value0, Object value1) {

        super(key, value0, value1);

    }


    /**
     * <p>Construct an action error with the specified replacement values.</p>
     *
     * @param key Message key for this error message
     * @param value0 First replacement value
     * @param value1 Second replacement value
     * @param value2 Third replacement value
     */
    public ActionError(String key, Object value0, Object value1,
                       Object value2) {

        super(key, value0, value1, value2);

    }


    /**
     * <p>Construct an action error with the specified replacement values.</p>
     *
     * @param key Message key for this error message
     * @param value0 First replacement value
     * @param value1 Second replacement value
     * @param value2 Third replacement value
     * @param value3 Fourth replacement value
     */
    public ActionError(String key, Object value0, Object value1,
                       Object value2, Object value3) {

        super(key, value0, value1, value2, value3);

    }


    /**
     * <p>Construct an action error with the specified replacement values.</p>
     *
     * @param key Message key for this message
     * @param values Array of replacement values
     */
    public ActionError(String key, Object[] values) {

        super(key, values);

    }

}
