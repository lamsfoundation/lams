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
import org.opensaml.xml.XMLObject;

/**
 * SAML 2.0 Core Advice.
 */
public interface Advice extends SAMLObject {

    /** Element local name. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "Advice";

    /** Default element name. */
    public static final QName DEFAULT_ELEMENT_NAME = new QName(SAMLConstants.SAML20_NS, DEFAULT_ELEMENT_LOCAL_NAME,
            SAMLConstants.SAML20_PREFIX);

    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "AdviceType";

    /** QName of the XSI type. */
    public static final QName TYPE_NAME = new QName(SAMLConstants.SAML20_NS, TYPE_LOCAL_NAME,
            SAMLConstants.SAML20_PREFIX);

    /**
     * Gets the list of all child elements attached to this advice.
     * 
     * @return the list of all child elements attached to this advice
     */
    public List<XMLObject> getChildren();

    /**
     * Gets the list of child elements attached to this advice that match a particular QName.
     * 
     * @param typeOrName the QName of the child elements to return
     * @return the list of matching child elements attached to this advice
     */
    public List<XMLObject> getChildren(QName typeOrName);

    /**
     * Gets the list of AssertionID references used as advice.
     * 
     * @return the list of AssertionID references used as advice
     */
    public List<AssertionIDRef> getAssertionIDReferences();

    /**
     * Gets the list of AssertionURI references used as advice.
     * 
     * @return the list of AssertionURI references used as advice
     */
    public List<AssertionURIRef> getAssertionURIReferences();

    /**
     * Gets the list of Assertions used as advice.
     * 
     * @return the list of Assertions used as advice
     */
    public List<Assertion> getAssertions();

    /**
     * Gets the list of EncryptedAssertions used as advice.
     * 
     * @return the list of EncryptedAssertions used as advice
     */
    public List<EncryptedAssertion> getEncryptedAssertions();
}