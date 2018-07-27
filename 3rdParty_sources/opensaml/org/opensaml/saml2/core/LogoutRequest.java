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

import org.joda.time.DateTime;
import org.opensaml.common.xml.SAMLConstants;

/**
 * SAML 2.0 Core LogoutRequest.
 */
public interface LogoutRequest extends RequestAbstractType {
    
    /** Element local name. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "LogoutRequest";
    
    /** Default element name. */
    public static final QName DEFAULT_ELEMENT_NAME = 
        new QName(SAMLConstants.SAML20P_NS, DEFAULT_ELEMENT_LOCAL_NAME, SAMLConstants.SAML20P_PREFIX);
    
    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "LogoutRequestType"; 
        
    /** QName of the XSI type. */
    public static final QName TYPE_NAME = 
        new QName(SAMLConstants.SAML20P_NS, TYPE_LOCAL_NAME, SAMLConstants.SAML20P_PREFIX);
    
    /** Reason attribute name. */
    public static final String REASON_ATTRIB_NAME = "Reason";
    
    /** NotOnOrAfter attribute name. */
    public static final String NOT_ON_OR_AFTER_ATTRIB_NAME = "NotOnOrAfter";
    
    /** User-initiated logout reason. */
    public static final String USER_REASON = "urn:oasis:names:tc:SAML:2.0:logout:user";

    /** Admin-initiated logout reason. */
    public static final String ADMIN_REASON = "urn:oasis:names:tc:SAML:2.0:logout:admin";
    
    /** Global timeout logout reason. */
    public static final String GLOBAL_TIMEOUT_REASON = "urn:oasis:names:tc:SAML:2.0:logout:global-timeout";
    
    /** SP timeout logout reason. */
    public static final String SP_TIMEOUT_REASON = "urn:oasis:names:tc:SAML:2.0:logout:sp-timeout";
    
    /**
     * Get the Reason attrib value of the request.
     * 
     * @return the Reason value of the request
     */
    public String getReason();

    /**
     * Set the Reason attrib value of the request.
     * 
     * @param newReason the new Reason value of the request
     */
    public void setReason(String newReason);
    
    /**
     * Get the NotOnOrAfter attrib value of the request.
     * 
     * @return the NotOnOrAfter value of the request
     */
    public DateTime getNotOnOrAfter();

    /**
     * Set the NotOnOrAfter attrib value of the request.
     * 
     * @param newNotOnOrAfter the new NotOnOrAfter value of the request
     */
    public void setNotOnOrAfter(DateTime newNotOnOrAfter);
    
    /**
     * Gets the base identifier of the principal for this request.
     * 
     * @return the base identifier of the principal for this request
     */
    public BaseID getBaseID();
    
    /**
     * Sets the base identifier of the principal for this request.
     * 
     * @param newBaseID the base identifier of the principal for this request
     */
    public void setBaseID(BaseID newBaseID);
    
    /**
     * Gets the name identifier of the principal for this request.
     * 
     * @return the name identifier of the principal for this request
     */
    public NameID getNameID();
    
    /**
     * Sets the name identifier of the principal for this request.
     * 
     * @param newNameID the name identifier of the principal for this request
     */
    public void setNameID(NameID newNameID);
    
    /**
     * Gets the encrytped name identifier of the principal for this request.
     * 
     * @return the encrytped name identifier of the principal for this request
     */
    public EncryptedID getEncryptedID();
    
    /**
     * Sets the encrypted name identifier of the principal for this request.
     * 
     * @param newEncryptedID the new encrypted name identifier of the principal for this request
     */
    public void setEncryptedID(EncryptedID newEncryptedID);
       
    /**
     *  Get the list of SessionIndexes for the request.
     * 
     * 
     * @return the list of SessionIndexes
     */
    public List<SessionIndex> getSessionIndexes();


}
