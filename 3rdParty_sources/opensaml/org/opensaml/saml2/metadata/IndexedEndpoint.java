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

package org.opensaml.saml2.metadata;

import javax.xml.namespace.QName;

import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.xml.schema.XSBooleanValue;

/**
 * SAML 2.0 Metadata IndexedEndpoint.
 */
public interface IndexedEndpoint extends Endpoint {

    /** Local name, no namespace. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "IndexedEndpoint";

    /** Default element name. */
    public static final QName DEFAULT_ELEMENT_NAME = new QName(SAMLConstants.SAML20MD_NS, DEFAULT_ELEMENT_LOCAL_NAME,
            SAMLConstants.SAML20MD_PREFIX);

    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "IndexedEndpointType";

    /** QName of the XSI type. */
    public static final QName TYPE_NAME = new QName(SAMLConstants.SAML20MD_NS, TYPE_LOCAL_NAME,
            SAMLConstants.SAML20MD_PREFIX);

    /** index attribute name. */
    public static final String INDEX_ATTRIB_NAME = "index";

    /** isDeault attribute name. */
    public static final String IS_DEFAULT_ATTRIB_NAME = "isDefault";

    /**
     * Gets the index of the endpoint.
     * 
     * @return index of the endpoint
     */
    public Integer getIndex();

    /**
     * Sets the index of the endpoint.
     * 
     * @param index index of the endpoint
     */
    public void setIndex(Integer index);

    /**
     * Gets whether this is the default endpoint in a list.
     * 
     * @return whether this is the default endpoint in a list
     */
    public Boolean isDefault();

    /**
     * Gets whether this is the default endpoint in a list.
     * 
     * @return whether this is the default endpoint in a list
     */
    public XSBooleanValue isDefaultXSBoolean();

    /**
     * Sets whether this is the default endpoint in a list. Boolean values will be marshalled to either "true" or
     * "false".
     * 
     * @param newIsDefault whether this is the default endpoint in a list
     */
    public void setIsDefault(Boolean newIsDefault);

    /**
     * Sets whether this is the default endpoint in a list.
     * 
     * @param newIsDefault whether this is the default endpoint in a list
     */
    public void setIsDefault(XSBooleanValue newIsDefault);
}
