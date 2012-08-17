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

package org.opensaml.saml2.core;

import javax.xml.namespace.QName;

import org.opensaml.common.SAMLObject;
import org.opensaml.common.xml.SAMLConstants;

/**
 * SAML 2.0 Core Action.
 */
public interface Action extends SAMLObject {

    /** Element local name. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "Action";

    /** Default element name. */
    public static final QName DEFAULT_ELEMENT_NAME = new QName(SAMLConstants.SAML20_NS, DEFAULT_ELEMENT_LOCAL_NAME,
            SAMLConstants.SAML20_PREFIX);

    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "ActionType";

    /** QName of the XSI type. */
    public static final QName TYPE_NAME = new QName(SAMLConstants.SAML20_NS, TYPE_LOCAL_NAME,
            SAMLConstants.SAML20_PREFIX);

    /** Name of the Namespace attribute. */
    public static final String NAMEPSACE_ATTRIB_NAME = "Namespace";

    /** Read/Write/Execute/Delete/Control action namespace. */
    public static final String RWEDC_NS_URI = "urn:oasis:names:tc:SAML:1.0:action:rwedc";

    /** Read/Write/Execute/Delete/Control negation action namespace. */
    public static final String RWEDC_NEGATION_NS_URI = "urn:oasis:names:tc:SAML:1.0:action:rwedc-negation";

    /** Get/Head/Put/Post action namespace. */
    public static final String GHPP_NS_URI = "urn:oasis:names:tc:SAML:1.0:action:ghpp";

    /** UNIX file permission action namespace. */
    public static final String UNIX_NS_URI = "urn:oasis:names:tc:SAML:1.0:action:unix";

    /** Read action. */
    public static final String READ_ACTION = "Read";

    /** Write action. */
    public static final String WRITE_ACTION = "Write";

    /** Execute action. */
    public static final String EXECUTE_ACTION = "Execute";

    /** Delete action. */
    public static final String DELETE_ACTION = "Delete";

    /** Control action. */
    public static final String CONTROL_ACTION = "Control";

    /** Negated Read action. */
    public static final String NEG_READ_ACTION = "~Read";

    /** Negated Write action. */
    public static final String NEG_WRITE_ACTION = "~Write";

    /** Negated Execute action. */
    public static final String NEG_EXECUTE_ACTION = "~Execute";

    /** Negated Delete action. */
    public static final String NEG_DELETE_ACTION = "~Delete";

    /** Negated Control action. */
    public static final String NEG_CONTROL_ACTION = "~Control";

    /** HTTP GET action. */
    public static final String HTTP_GET_ACTION = "GET";

    /** HTTP HEAD action. */
    public static final String HTTP_HEAD_ACTION = "HEAD";

    /** HTTP PUT action. */
    public static final String HTTP_PUT_ACTION = "PUT";

    /** HTTP POST action. */
    public static final String HTTP_POST_ACTION = "POST";

    /**
     * Gets the namespace scope of the specified action.
     * 
     * @return the namespace scope of the specified action
     */
    public String getNamespace();

    /**
     * Sets the namespace scope of the specified action.
     * 
     * @param newNamespace the namespace scope of the specified action
     */
    public void setNamespace(String newNamespace);

    /**
     * Gets the URI of the action to be performed.
     * 
     * @return the URI of the action to be performed
     */
    public String getAction();

    /**
     * Sets the URI of the action to be performed.
     * 
     * @param newAction the URI of the action to be performed
     */
    public void setAction(String newAction);
}