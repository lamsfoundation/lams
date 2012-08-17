/*
 * Licensed to the University Corporation for Advanced Internet Development, 
 * Inc. (UCAID) under one or more contributor license agreements.  See the 
 * NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The UCAID licenses this file to You under the Apache 
 * License, Version 2.0 (the "License"); you may not use this file except in 
 * compliance with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensaml.xacml.ctx;

import javax.xml.namespace.QName;

import org.opensaml.xacml.XACMLConstants;
import org.opensaml.xacml.XACMLObject;

/** XACML context StatusCode schema type. */
public interface StatusCodeType extends XACMLObject {

    /** Local name of the StatusCode element. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "StatusCode";

    /** Default element name. */
    public static final QName DEFAULT_ELEMENT_NAME = new QName(XACMLConstants.XACML20CTX_NS,
            DEFAULT_ELEMENT_LOCAL_NAME, XACMLConstants.XACMLCONTEXT_PREFIX);

    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "StatusCodeType";

    /** QName of the XSI type XACML20. */
    public static final QName TYPE_NAME = new QName(XACMLConstants.XACML20CTX_NS, TYPE_LOCAL_NAME,
            XACMLConstants.XACMLCONTEXT_PREFIX);

    /** Name of the Value attribute. */
    public static final String VALUE_ATTTRIB_NAME = "Value";

    /** Missing attribute status code. */
    public static final String SC_MISSING_ATTRIBUTE = "urn:oasis:names:tc:xacml:1.0:status:missing-attribute";

    /** Ok status code. */
    public static final String SC_OK = "urn:oasis:names:tc:xacml:1.0:status:ok";

    /** Processing error status code. */
    public static final String SC_PROCESSING_ERROR = "urn:oasis:names:tc:xacml:1.0:status:processing-error";

    /** Syntax error status code. */
    public static final String SC_SYNTAX_ERROR = "urn:oasis:names:tc:xacml:1.0:status:syntax-error";

    /**
     * Gets the status code.
     * 
     * @return status code
     */
    public StatusCodeType getStatusCode();

    /**
     * Sets the status code.
     * 
     * @param code status code
     */
    public void setStatusCode(StatusCodeType code);

    /**
     * Gets the value of the attribute named value of the status element.
     * 
     * @return The value of the attribute named value for the status element
     */
    public String getValue();

    /**
     * Sets the attribute named value of the status elements.
     * 
     * @param value The wanted value for the attribute value
     */
    public void setValue(String value);
}
