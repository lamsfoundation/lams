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
 * SAML 2.0 Core IDPEntry.
 */
public interface IDPEntry extends SAMLObject {

    /** Element local name. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "IDPEntry";

    /** Default element name. */
    public static final QName DEFAULT_ELEMENT_NAME = new QName(SAMLConstants.SAML20P_NS, DEFAULT_ELEMENT_LOCAL_NAME,
            SAMLConstants.SAML20P_PREFIX);

    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "IDPEntryType";

    /** QName of the XSI type. */
    public static final QName TYPE_NAME = new QName(SAMLConstants.SAML20P_NS, TYPE_LOCAL_NAME,
            SAMLConstants.SAML20P_PREFIX);

    /** ProviderID attribute name. */
    public static final String PROVIDER_ID_ATTRIB_NAME = "ProviderID";

    /** Name attribute name. */
    public static final String NAME_ATTRIB_NAME = "Name";

    /** Loc attribute name. */
    public static final String LOC_ATTRIB_NAME = "Loc";

    /**
     * Gets ProviderID URI.
     * 
     * @return the ProviderID URI
     */
    public String getProviderID();

    /**
     * Sets the ProviderID URI.
     * 
     * @param newProviderID the new ProviderID URI
     */
    public void setProviderID(String newProviderID);

    /**
     * Gets the Name value.
     * 
     * @return the Name value
     */
    public String getName();

    /**
     * Sets the Name value.
     * 
     * @param newName the Name value
     */
    public void setName(String newName);

    /**
     * Gets the Loc value.
     * 
     * @return the Loc value
     */
    public String getLoc();

    /**
     * Sets the Loc value.
     * 
     * @param newLoc the new Loc value
     */
    public void setLoc(String newLoc);

}