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

import org.joda.time.DateTime;
import org.opensaml.common.SAMLVersion;
import org.opensaml.common.SignableSAMLObject;
import org.opensaml.common.xml.SAMLConstants;

/**
 * SAML 2.0 Core Assertion.
 */
public interface Assertion extends SignableSAMLObject, Evidentiary {

    /** Element local name. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "Assertion";

    /** Default element name. */
    public static final QName DEFAULT_ELEMENT_NAME = new QName(SAMLConstants.SAML20_NS, DEFAULT_ELEMENT_LOCAL_NAME,
            SAMLConstants.SAML20_PREFIX);

    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "AssertionType";

    /** QName of the XSI type. */
    public static final QName TYPE_NAME = new QName(SAMLConstants.SAML20_NS, TYPE_LOCAL_NAME,
            SAMLConstants.SAML20_PREFIX);

    /** Version attribute name. */
    public static final String VERSION_ATTRIB_NAME = "Version";

    /** IssueInstant attribute name. */
    public static final String ISSUE_INSTANT_ATTRIB_NAME = "IssueInstant";

    /** ID attribute name. */
    public static final String ID_ATTRIB_NAME = "ID";

    /**
     * Gets the SAML Version of this assertion.
     * 
     * @return the SAML Version of this assertion.
     */
    public SAMLVersion getVersion();

    /**
     * Sets the SAML Version of this assertion.
     * 
     * @param newVersion the SAML Version of this assertion
     */
    public void setVersion(SAMLVersion newVersion);

    /**
     * Gets the issue instance of this assertion.
     * 
     * @return the issue instance of this assertion
     */
    public DateTime getIssueInstant();

    /**
     * Sets the issue instance of this assertion.
     * 
     * @param newIssueInstance the issue instance of this assertion
     */
    public void setIssueInstant(DateTime newIssueInstance);

    /**
     * Sets the ID of this assertion.
     * 
     * @return the ID of this assertion
     */
    public String getID();

    /**
     * Sets the ID of this assertion.
     * 
     * @param newID the ID of this assertion
     */
    public void setID(String newID);

    /**
     * Gets the Issuer of this assertion.
     * 
     * @return the Issuer of this assertion
     */
    public Issuer getIssuer();

    /**
     * Sets the Issuer of this assertion.
     * 
     * @param newIssuer the Issuer of this assertion
     */
    public void setIssuer(Issuer newIssuer);

    /**
     * Gets the Subject of this assertion.
     * 
     * @return the Subject of this assertion
     */
    public Subject getSubject();

    /**
     * Sets the Subject of this assertion.
     * 
     * @param newSubject the Subject of this assertion
     */
    public void setSubject(Subject newSubject);

    /**
     * Gets the Conditions placed on this assertion.
     * 
     * @return the Conditions placed on this assertion
     */
    public Conditions getConditions();

    /**
     * Sets the Conditions placed on this assertion.
     * 
     * @param newConditions the Conditions placed on this assertion
     */
    public void setConditions(Conditions newConditions);

    /**
     * Gets the Advice for this assertion.
     * 
     * @return the Advice for this assertion
     */
    public Advice getAdvice();

    /**
     * Sets the Advice for this assertion.
     * 
     * @param newAdvice the Advice for this assertion
     */
    public void setAdvice(Advice newAdvice);

    /**
     * Gets the list of statements attached to this assertion.
     * 
     * @return the list of statements attached to this assertion
     */
    public List<Statement> getStatements();

    /**
     * Gets the list of statements attached to this assertion that match a particular QName.
     * 
     * @param typeOrName the QName of the statements to return
     * @return the list of statements attached to this assertion
     */
    public List<Statement> getStatements(QName typeOrName);

    /**
     * Gets the list of AuthnStatements attached to this assertion.
     * 
     * @return the list of AuthnStatements attached to this assertion
     */
    public List<AuthnStatement> getAuthnStatements();

    /**
     * Gets the list of AuthzDecisionStatements attached to this assertion.
     * 
     * @return the list of AuthzDecisionStatements attached to this assertion
     */
    public List<AuthzDecisionStatement> getAuthzDecisionStatements();

    /**
     * Gets the list of AttributeStatement attached to this assertion.
     * 
     * @return the list of AttributeStatement attached to this assertion
     */
    public List<AttributeStatement> getAttributeStatements();
}