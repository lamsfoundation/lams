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

/**
 * 
 */

package org.opensaml.saml2.core;

import java.util.List;

import javax.xml.namespace.QName;

import org.opensaml.common.xml.SAMLConstants;

/**
 * SAML 2.0 AuthzDecisionQuery.
 */
public interface AuthzDecisionQuery extends SubjectQuery {

    /** Element local name. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "AuthzDecisionQuery";

    /** Default element name. */
    public static final QName DEFAULT_ELEMENT_NAME = new QName(SAMLConstants.SAML20P_NS, DEFAULT_ELEMENT_LOCAL_NAME,
            SAMLConstants.SAML20P_PREFIX);

    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "AuthzDecisionQueryType";

    /** QName of the XSI type. */
    public static final QName TYPE_NAME = new QName(SAMLConstants.SAML20P_NS, TYPE_LOCAL_NAME,
            SAMLConstants.SAML20P_PREFIX);

    /** Resource attribute name. */
    public static final String RESOURCE_ATTRIB_NAME = "Resource";

    /**
     * Gets the Resource attrib value of this query.
     * 
     * @return the Resource attrib value of this query
     */
    public String getResource();

    /**
     * Sets the Resource attrib value of this query.
     * 
     * @param newResource the new Resource attrib value of this query
     */
    public void setResource(String newResource);

    /**
     * Gets the Actions of this query.
     * 
     * @return the Actions of this query
     */
    public List<Action> getActions();

    /**
     * Gets the Evidence of this query.
     * 
     * @return the Evidence of this query
     */
    public Evidence getEvidence();

    /**
     * Sets the Evidence of this query.
     * 
     * @param newEvidence the new Evidence of this query
     */
    public void setEvidence(Evidence newEvidence);

}
