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

import org.opensaml.common.SAMLObject;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.xml.encryption.EncryptedData;
import org.opensaml.xml.encryption.EncryptedKey;

/**
 * SAML 2.0 Core EncryptedElementType.
 */
public interface EncryptedElementType extends SAMLObject {
    
    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "EncryptedElementType"; 
        
    /** QName of the XSI type. */
    public static final QName TYPE_NAME = 
        new QName(SAMLConstants.SAML20_NS, TYPE_LOCAL_NAME, SAMLConstants.SAML20_PREFIX);
    
    /**
     * Get the EncryptedData child element.
     * 
     * @return the EncryptedData child element
     */
    public EncryptedData getEncryptedData();
    
    /**
     * Set the EncryptedData child element.
     * 
     * @param newEncryptedData the new EncryptedData child element
     */
    public void setEncryptedData(EncryptedData newEncryptedData);
    
    /**
     * A list of EncryptedKey child elements.
     * 
     * @return a list of EncryptedKey child elements
     */
    public List<EncryptedKey> getEncryptedKeys();

}