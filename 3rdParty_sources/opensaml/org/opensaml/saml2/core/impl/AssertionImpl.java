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

package org.opensaml.saml2.core.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

import org.joda.time.DateTime;
import org.opensaml.common.SAMLVersion;
import org.opensaml.common.impl.AbstractSignableSAMLObject;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.core.Advice;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.AuthnStatement;
import org.opensaml.saml2.core.AuthzDecisionStatement;
import org.opensaml.saml2.core.Conditions;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.Statement;
import org.opensaml.saml2.core.Subject;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.IndexedXMLObjectChildrenList;

/**
 * A concrete implementation of {@link org.opensaml.saml2.core.Assertion}.
 */
public class AssertionImpl extends AbstractSignableSAMLObject implements Assertion {

    /** SAML Version of the assertion. */
    private SAMLVersion version;

    /** Issue Instant of the assertion. */
    private DateTime issueInstant;

    /** ID of the assertion. */
    private String id;

    /** Issuer of the assertion. */
    private Issuer issuer;

    /** Subject of the assertion. */
    private Subject subject;

    /** Conditions of the assertion. */
    private Conditions conditions;

    /** Advice of the assertion. */
    private Advice advice;

    /** Statements of the assertion. */
    private final IndexedXMLObjectChildrenList<Statement> statements;

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected AssertionImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        version = SAMLVersion.VERSION_20;
        statements = new IndexedXMLObjectChildrenList<Statement>(this);
    }

    /** {@inheritDoc} */
    public SAMLVersion getVersion() {
        return version;
    }

    /** {@inheritDoc} */
    public void setVersion(SAMLVersion newVersion) {
        this.version = prepareForAssignment(this.version, newVersion);
    }

    /** {@inheritDoc} */
    public DateTime getIssueInstant() {
        return issueInstant;
    }

    /** {@inheritDoc} */
    public void setIssueInstant(DateTime newIssueInstance) {
        this.issueInstant = prepareForAssignment(this.issueInstant, newIssueInstance);
    }

    /** {@inheritDoc} */
    public String getID() {
        return id;
    }

    /** {@inheritDoc} */
    public void setID(String newID) {
        String oldID = this.id;
        this.id = prepareForAssignment(this.id, newID);
        registerOwnID(oldID, this.id);
    }

    /** {@inheritDoc} */
    public Issuer getIssuer() {
        return issuer;
    }

    /** {@inheritDoc} */
    public void setIssuer(Issuer newIssuer) {
        this.issuer = prepareForAssignment(this.issuer, newIssuer);
    }

    /** {@inheritDoc} */
    public Subject getSubject() {
        return subject;
    }

    /** {@inheritDoc} */
    public void setSubject(Subject newSubject) {
        this.subject = prepareForAssignment(this.subject, newSubject);
    }

    /** {@inheritDoc} */
    public Conditions getConditions() {
        return conditions;
    }

    /** {@inheritDoc} */
    public void setConditions(Conditions newConditions) {
        this.conditions = prepareForAssignment(this.conditions, newConditions);
    }

    /** {@inheritDoc} */
    public Advice getAdvice() {
        return advice;
    }

    /** {@inheritDoc} */
    public void setAdvice(Advice newAdvice) {
        this.advice = prepareForAssignment(this.advice, newAdvice);
    }

    /** {@inheritDoc} */
    public List<Statement> getStatements() {
        return statements;
    }

    /** {@inheritDoc} */
    public List<Statement> getStatements(QName typeOrName) {
        return (List<Statement>) statements.subList(typeOrName);
    }

    /** {@inheritDoc} */
    public List<AuthnStatement> getAuthnStatements() {
        QName statementQName = new QName(SAMLConstants.SAML20_NS, AuthnStatement.DEFAULT_ELEMENT_LOCAL_NAME,
                SAMLConstants.SAML20_PREFIX);
        return (List<AuthnStatement>) statements.subList(statementQName);
    }

    /** {@inheritDoc} */
    public List<AuthzDecisionStatement> getAuthzDecisionStatements() {
        QName statementQName = new QName(SAMLConstants.SAML20_NS, AuthzDecisionStatement.DEFAULT_ELEMENT_LOCAL_NAME,
                SAMLConstants.SAML20_PREFIX);
        return (List<AuthzDecisionStatement>) statements.subList(statementQName);
    }

    /** {@inheritDoc} */
    public List<AttributeStatement> getAttributeStatements() {
        QName statementQName = new QName(SAMLConstants.SAML20_NS, AttributeStatement.DEFAULT_ELEMENT_LOCAL_NAME,
                SAMLConstants.SAML20_PREFIX);
        return (List<AttributeStatement>) statements.subList(statementQName);
    }
    
    /** {@inheritDoc} */
    public String getSignatureReferenceID(){
        return id;
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();

        children.add(issuer);
        
        if(getSignature() != null){
            children.add(getSignature());
        }
        
        children.add(subject);
        children.add(conditions);
        children.add(advice);
        children.addAll(statements);

        return Collections.unmodifiableList(children);
    }
}