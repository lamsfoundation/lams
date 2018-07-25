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

import javax.xml.namespace.QName;

import org.opensaml.common.xml.SAMLConstants;


/**
 * SAML 2.0 Core ManageNameIDRequest.
 */
public interface ManageNameIDRequest extends RequestAbstractType {
    
    /** Element local name. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "ManageNameIDRequest";
    
    /** Default element name. */
    public static final QName DEFAULT_ELEMENT_NAME = 
        new QName(SAMLConstants.SAML20P_NS, DEFAULT_ELEMENT_LOCAL_NAME, SAMLConstants.SAML20P_PREFIX);
    
    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "ManageNameIDRequestType"; 
        
    /** QName of the XSI type. */
    public static final QName TYPE_NAME = 
        new QName(SAMLConstants.SAML20P_NS, TYPE_LOCAL_NAME, SAMLConstants.SAML20P_PREFIX);
    
    /**
     * Get the NameID of the request.
     * 
     * @return the NameID of the request
     */
    public NameID getNameID();

    /**
     * Set the NameID of the request.
     * 
     * @param newNameID the new NameID of the request
     */
    public void setNameID(NameID newNameID);

    /**
     * Get the EncryptedID of the request.
     * 
     * @return the EncryptedID of the request
     */
    public EncryptedID getEncryptedID();

    /**
     * Set the EncryptedID of the request.
     * 
     * @param newEncryptedID the new EncryptedID of the request
     */
    public void setEncryptedID(EncryptedID newEncryptedID);

    /**
     * Get the NewID of the request.
     * 
     * @return the NewID of the request
     */
    public NewID getNewID();

    /**
     * Set the NewID of the request.
     * 
     * @param newNewID the new NewID of the request
     */
    public void setNewID(NewID newNewID);

    /**
     * Get the NewEncryptedID of the request.
     * 
     * @return the NewEncryptedID of the request
     */
    public NewEncryptedID getNewEncryptedID();

    /**
     * Set the NewEncryptedID of the request.
     * 
     * @param newNewEncryptedID the new NewEncryptedID of the request
     */
    public void setNewEncryptedID(NewEncryptedID newNewEncryptedID);

    /**
     * Get the Terminate of the request.
     * 
     * @return the Terminate of the request
     */
    public Terminate getTerminate();

    /**
     * Set the Terminate of the request.
     * 
     * @param newTerminate the new NewID Terminate of the request
     */
    public void setTerminate(Terminate newTerminate);

}
