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

import java.util.List;

import javax.xml.namespace.QName;

import org.opensaml.common.xml.SAMLConstants;

/**
 * SAML 2.0 Core ProxyRestriction.
 */
public interface ProxyRestriction extends Condition {

    /** Element local name. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "ProxyRestriction";

    /** Default element name. */
    public static final QName DEFAULT_ELEMENT_NAME = new QName(SAMLConstants.SAML20_NS, DEFAULT_ELEMENT_LOCAL_NAME,
            SAMLConstants.SAML20_PREFIX);

    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "ProxyRestrictionType";

    /** QName of the XSI type. */
    public static final QName TYPE_NAME = new QName(SAMLConstants.SAML20_NS, TYPE_LOCAL_NAME,
            SAMLConstants.SAML20_PREFIX);

    /** Count attribute name. */
    public static final String COUNT_ATTRIB_NAME = "Count";

    /**
     * Gets the number of times the assertion may be proxied.
     * 
     * @return the number of times the assertion may be proxied
     */
    public Integer getProxyCount();

    /**
     * Sets the number of times the assertion may be proxied.
     * 
     * @param newProxyCount the number of times the assertion may be proxied
     */
    public void setProxyCount(Integer newProxyCount);

    /**
     * Gets the list of audiences to whom the assertion may be proxied.
     * 
     * @return the list of audiences to whom the assertion may be proxied
     */
    public List<Audience> getAudiences();
}