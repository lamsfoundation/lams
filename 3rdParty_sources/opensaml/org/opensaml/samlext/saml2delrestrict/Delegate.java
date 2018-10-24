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

package org.opensaml.samlext.saml2delrestrict;

import javax.xml.namespace.QName;

import org.joda.time.DateTime;
import org.opensaml.common.SAMLObject;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.core.BaseID;
import org.opensaml.saml2.core.EncryptedID;
import org.opensaml.saml2.core.NameID;

/**
 * SAML 2.0 Condition for Delegation Restriction - Delegate element.
 */
public interface Delegate extends SAMLObject {
    
    /** Element local name. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "Delegate";

    /** Default element name. */
    public static final QName DEFAULT_ELEMENT_NAME =
        new QName(SAMLConstants.SAML20DEL_NS, DEFAULT_ELEMENT_LOCAL_NAME, SAMLConstants.SAML20DEL_PREFIX);
    
    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "DelegateType";

    /** QName of the XSI type. */
    public static final QName TYPE_NAME =
        new QName(SAMLConstants.SAML20DEL_NS, TYPE_LOCAL_NAME, SAMLConstants.SAML20DEL_PREFIX);
    
    /** DelegationInstant attribute name. */
    public static final String DELEGATION_INSTANT_ATTRIB_NAME = "DelegationInstant";
    
    /** ConfirmationMethod attribute name. */
    public static final String CONFIRMATION_METHOD_ATTRIB_NAME = "ConfirmationMethod";
    

    /**
     * Gets the BaseID child element of the delegate.
     * 
     * @return the base identifier of the delegate
     */
    public BaseID getBaseID();

    /**
     * Sets the BaseID child element of the delegate.
     * 
     * @param newBaseID the base identifier of the delegate
     */
    public void setBaseID(BaseID newBaseID);

    /**
     * Gets the NameID child element of the delegate.
     * 
     * @return the name identifier of the principal for this request
     */
    public NameID getNameID();

    /**
     * Sets the NameID child element of the delegate.
     * 
     * @param newNameID the name identifier of the delegate
     */
    public void setNameID(NameID newNameID);

    /**
     * Gets the EncryptedID child element of the delegate.
     * 
     * @return the encrypted name identifier of the delegate
     */
    public EncryptedID getEncryptedID();

    /**
     * Sets the EncryptedID child element of the delegate.
     * 
     * @param newEncryptedID the new encrypted name identifier of the delegate
     */
    public void setEncryptedID(EncryptedID newEncryptedID);
    
    /**
     * Get the delegation instant attribute value.
     * 
     * @return the delegation instant
     */
    public DateTime getDelegationInstant();
    
    /**
     * Set the delegation instant attribute value.
     * 
     * @param newInstant the new delegation instant
     */
    public void setDelegationInstant(DateTime newInstant);
    
    /**
     * Get the confirmation method attribute value.
     * 
     * @return the confirmation method
     */
    public String getConfirmationMethod();
    
    /**
     * Set the confirmation method attribute value.
     * 
     * @param newMethod the new confirmation method
     */
    public void setConfirmationMethod(String newMethod);

}
