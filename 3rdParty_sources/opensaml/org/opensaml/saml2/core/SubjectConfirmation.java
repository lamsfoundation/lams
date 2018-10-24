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
 * SAML 2.0 Core SubjectConfirmation.
 */
public interface SubjectConfirmation extends SAMLObject {

    /** Element local name. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "SubjectConfirmation";

    /** Default element name. */
    public static final QName DEFAULT_ELEMENT_NAME = new QName(SAMLConstants.SAML20_NS, DEFAULT_ELEMENT_LOCAL_NAME,
            SAMLConstants.SAML20_PREFIX);

    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "SubjectConfirmationType";

    /** QName of the XSI type. */
    public static final QName TYPE_NAME = new QName(SAMLConstants.SAML20_NS, TYPE_LOCAL_NAME,
            SAMLConstants.SAML20_PREFIX);

    /** Method attribute name. */
    public static final String METHOD_ATTRIB_NAME = "Method";
    
    /** URI for the Holder of Key subject confirmation method, {@value}. */
    public static final String METHOD_HOLDER_OF_KEY = "urn:oasis:names:tc:SAML:2.0:cm:holder-of-key";
    
    /** URI for the Sender Vouches subject confirmation method, {@value}. */
    public static final String METHOD_SENDER_VOUCHES = "urn:oasis:names:tc:SAML:2.0:cm:sender-vouches";
    
    /** URI for the Bearer subject confirmation method, {@value}. */
    public static final String METHOD_BEARER = "urn:oasis:names:tc:SAML:2.0:cm:bearer";

    /**
     * Get the method used to confirm this subject.
     * 
     * @return the method used to confirm this subject
     */
    public String getMethod();

    /**
     * Sets the method used to confirm this subject.
     * 
     * @param newMethod the method used to confirm this subject
     */
    public void setMethod(String newMethod);

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
     * Gets the encrypted name identifier of the principal for this request.
     * 
     * @return the encrypted name identifier of the principal for this request
     */
    public EncryptedID getEncryptedID();

    /**
     * Sets the encrypted name identifier of the principal for this request.
     * 
     * @param newEncryptedID the new encrypted name identifier of the principal for this request
     */
    public void setEncryptedID(EncryptedID newEncryptedID);

    /**
     * Gets the data about how this subject was confirmed or constraints on the confirmation.
     * 
     * @return the data about how this subject was confirmed or constraints on the confirmation
     */
    public SubjectConfirmationData getSubjectConfirmationData();

    /**
     * Sets the data about how this subject was confirmed or constraints on the confirmation.
     * 
     * @param newSubjectConfirmationData the data about how this subject was confirmed or constraints on the
     *            confirmation
     */
    public void setSubjectConfirmationData(SubjectConfirmationData newSubjectConfirmationData);
}