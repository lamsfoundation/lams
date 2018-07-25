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

package org.opensaml.samlext.saml2cb;

import javax.xml.namespace.QName;

import org.opensaml.common.SAMLObject;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.ws.soap.soap11.ActorBearing;
import org.opensaml.ws.soap.soap11.MustUnderstandBearing;
import org.opensaml.xml.schema.XSBase64Binary;

/**
 * SAML 2.0 Channel Bindings Extensions ChannelBinding element.
 */
public interface ChannelBindings extends XSBase64Binary, MustUnderstandBearing, ActorBearing, SAMLObject {

    /** Element local name. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "ChannelBindings";

    /** Default element name. */
    public static final QName DEFAULT_ELEMENT_NAME = new QName(SAMLConstants.SAML20CB_NS, DEFAULT_ELEMENT_LOCAL_NAME,
            SAMLConstants.SAML20CB_PREFIX);

    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "ChannelBindingsType";

    /** QName of the XSI type. */
    public static final QName TYPE_NAME = new QName(SAMLConstants.SAML20CB_NS, TYPE_LOCAL_NAME,
            SAMLConstants.SAML20CB_PREFIX);

    /** Type attribute name. */
    public static final String TYPE_ATTRIB_NAME = "Type";

    /**
     * Get the Type attribute value.
     * 
     * @return the Type attribute value
     */
    public String getType();
    
    /**
     * Set the Type attribute value.
     * 
     * @param newType the new Type attribute value
     */
    public void setType(String newType);
}