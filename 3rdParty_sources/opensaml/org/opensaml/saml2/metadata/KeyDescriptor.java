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

import java.util.List;

import javax.xml.namespace.QName;

import org.opensaml.common.SAMLObject;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.xml.security.credential.UsageType;
import org.opensaml.xml.signature.KeyInfo;

public interface KeyDescriptor extends SAMLObject {

    /** Element name, no namespace */
    public final static String DEFAULT_ELEMENT_LOCAL_NAME = "KeyDescriptor";
    
    /** Default element name */
    public final static QName DEFAULT_ELEMENT_NAME = new QName(SAMLConstants.SAML20MD_NS, DEFAULT_ELEMENT_LOCAL_NAME, SAMLConstants.SAML20MD_PREFIX);
    
    /** Local name of the XSI type */
    public final static String TYPE_LOCAL_NAME = "KeyDescriptorType"; 
        
    /** QName of the XSI type */
    public final static QName TYPE_NAME = new QName(SAMLConstants.SAML20MD_NS, TYPE_LOCAL_NAME, SAMLConstants.SAML20MD_PREFIX);

    /** "use" attribute's local name */
    public final static String USE_ATTRIB_NAME = "use";

    /**
     * Gets the use of this key.
     * 
     * @return the use of this key
     */
    public UsageType getUse();

    /**
     * Sets the use of this key.
     * 
     * @param newType the use of this key
     */
    public void setUse(UsageType newType);

    /**
     * Gets information about the key, including the key itself.
     * 
     * @return information about the key, including the key itself
     */
    public KeyInfo getKeyInfo();

    /**
     * Sets information about the key, including the key itself.
     * 
     * @param newKeyInfo information about the key, including the key itself
     */
    public void setKeyInfo(KeyInfo newKeyInfo);

    /**
     * Gets the encryption methods that are supported by the entity.
     * 
     * @return the encryption methods that are supported by the entity
     */
    public List<EncryptionMethod> getEncryptionMethods();
}