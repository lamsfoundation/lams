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

import org.joda.time.DateTime;
import org.opensaml.common.SAMLObject;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.xml.AttributeExtensibleXMLObject;
import org.opensaml.xml.ElementExtensibleXMLObject;

/**
 * SAML 2.0 Core SubjectConfirmationData.
 */
public interface SubjectConfirmationData extends SAMLObject, ElementExtensibleXMLObject, AttributeExtensibleXMLObject {

    /** Element local name. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "SubjectConfirmationData";

    /** Default element name. */
    public static final QName DEFAULT_ELEMENT_NAME = new QName(SAMLConstants.SAML20_NS, DEFAULT_ELEMENT_LOCAL_NAME,
            SAMLConstants.SAML20_PREFIX);

    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "SubjectConfirmationDataType";

    /** QName of the XSI type. */
    public static final QName TYPE_NAME = new QName(SAMLConstants.SAML20_NS, TYPE_LOCAL_NAME,
            SAMLConstants.SAML20_PREFIX);

    /** NotBefore attribute name. */
    public static final String NOT_BEFORE_ATTRIB_NAME = "NotBefore";

    /** NotOnOrAfter attribute name. */
    public static final String NOT_ON_OR_AFTER_ATTRIB_NAME = "NotOnOrAfter";

    /** Recipient attribute name. */
    public static final String RECIPIENT_ATTRIB_NAME = "Recipient";

    /** InResponseTo attribute name. */
    public static final String IN_RESPONSE_TO_ATTRIB_NAME = "InResponseTo";

    /** Address attribute name. */
    public static final String ADDRESS_ATTRIB_NAME = "Address";

    /**
     * Gets the time before which this subject is not valid.
     * 
     * @return the time before which this subject is not valid
     */
    public DateTime getNotBefore();

    /**
     * Sets the time before which this subject is not valid.
     * 
     * @param newNotBefore the time before which this subject is not valid
     */
    public void setNotBefore(DateTime newNotBefore);

    /**
     * Gets the time at, or after, which this subject is not valid.
     * 
     * @return the time at, or after, which this subject is not valid
     */
    public DateTime getNotOnOrAfter();

    /**
     * Sets the time at, or after, which this subject is not valid.
     * 
     * @param newNotOnOrAfter the time at, or after, which this subject is not valid
     */
    public void setNotOnOrAfter(DateTime newNotOnOrAfter);

    /**
     * Gets the recipient of this subject.
     * 
     * @return the recipient of this subject
     */
    public String getRecipient();

    /**
     * Sets the recipient of this subject.
     * 
     * @param newRecipient the recipient of this subject
     */
    public void setRecipient(String newRecipient);

    /**
     * Gets the message ID this is in response to.
     * 
     * @return the message ID this is in response to
     */
    public String getInResponseTo();

    /**
     * Sets the message ID this is in response to.
     * 
     * @param newInResponseTo the message ID this is in response to
     */
    public void setInResponseTo(String newInResponseTo);

    /**
     * Gets the IP address to which this information may be pressented.
     * 
     * @return the IP address to which this information may be pressented
     */
    public String getAddress();

    /**
     * Sets the IP address to which this information may be pressented.
     * 
     * @param newAddress the IP address to which this information may be pressented
     */
    public void setAddress(String newAddress);
}