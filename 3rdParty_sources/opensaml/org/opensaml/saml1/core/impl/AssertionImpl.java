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

package org.opensaml.saml1.core.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

import org.joda.time.DateTime;
import org.opensaml.common.SAMLVersion;
import org.opensaml.common.impl.AbstractSignableSAMLObject;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml1.core.Advice;
import org.opensaml.saml1.core.Assertion;
import org.opensaml.saml1.core.AttributeStatement;
import org.opensaml.saml1.core.AuthenticationStatement;
import org.opensaml.saml1.core.AuthorizationDecisionStatement;
import org.opensaml.saml1.core.Conditions;
import org.opensaml.saml1.core.Statement;
import org.opensaml.saml1.core.SubjectStatement;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.IndexedXMLObjectChildrenList;

/**
 * This class implements the SAML 1 <code> Assertion </code> statement.
 */
public class AssertionImpl extends AbstractSignableSAMLObject implements Assertion {

    /** The <code> AssertionID </code> attrribute */
    private String id;
    
    /** SAML version of this assertion */
    private SAMLVersion version;
    
    /** Object version of the <code> Issuer </code> attribute. */
    private String issuer;

    /** Object version of the <code> IssueInstant </code> attribute. */
    private DateTime issueInstant;

    /** (Possibly null) Singleton object version of the <code> Conditions </code> element. */
    private Conditions conditions;

    /** (Possibly null) Singleton object version of the <code> Advice </code> element. */
    private Advice advice;

    /** Object representnation of all the <code> Statement <\code> elements. */
    private final IndexedXMLObjectChildrenList<Statement> statements;
    
    /**
     * Constructor
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected AssertionImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        statements = new IndexedXMLObjectChildrenList<Statement>(this);
        version = SAMLVersion.VERSION_11;
    }
    
    /** {@inheritDoc} */
    public int getMajorVersion(){
        return version.getMajorVersion();
    }
    
    /** {@inheritDoc} */
    public int getMinorVersion() {
        return version.getMinorVersion();
    }
    
    /** {@inheritDoc} */
    public void setVersion(SAMLVersion newVersion){
        version = prepareForAssignment(version, newVersion);
    }

    /** {@inheritDoc} */
    public String getID() {
        return id;
    }

    /** {@inheritDoc} */
    public void setID(String id) {
        String oldID = this.id;
        this.id = prepareForAssignment(this.id, id);   
        registerOwnID(oldID, this.id);
    }

    /** {@inheritDoc} */
    public String getIssuer() {
        return this.issuer;
    }

    /** {@inheritDoc} */
    public void setIssuer(String issuer) {
        this.issuer = prepareForAssignment(this.issuer, issuer);
    }

    /** {@inheritDoc} */
    public DateTime getIssueInstant() {
        return this.issueInstant;
    }

    /** {@inheritDoc} */
    public void setIssueInstant(DateTime issueInstant) {
        this.issueInstant = prepareForAssignment(this.issueInstant, issueInstant);
    }

    /** {@inheritDoc} */
    public Conditions getConditions() {
        return conditions;
    }

    /** {@inheritDoc} */
    public void setConditions(Conditions conditions) throws IllegalArgumentException {
        this.conditions = prepareForAssignment(this.conditions, conditions);
    }

    /** {@inheritDoc} */
    public Advice getAdvice() {
        return advice;
    }

    /** {@inheritDoc} */
    public void setAdvice(Advice advice) throws IllegalArgumentException {
        this.advice = prepareForAssignment(this.advice, advice);
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
    public List<SubjectStatement> getSubjectStatements() {
        QName statementQName = new QName(SAMLConstants.SAML1_NS, SubjectStatement.DEFAULT_ELEMENT_LOCAL_NAME);
        return (List<SubjectStatement>) statements.subList(statementQName);
    }

    /** {@inheritDoc} */
    public List<AuthenticationStatement> getAuthenticationStatements() {
        QName statementQName = new QName(SAMLConstants.SAML1_NS, AuthenticationStatement.DEFAULT_ELEMENT_LOCAL_NAME);
        return (List<AuthenticationStatement>) statements.subList(statementQName);
    }

    /** {@inheritDoc} */
    public List<AuthorizationDecisionStatement> getAuthorizationDecisionStatements() {
        QName statementQName = new QName(SAMLConstants.SAML1_NS, AuthorizationDecisionStatement.DEFAULT_ELEMENT_LOCAL_NAME);
        return (List<AuthorizationDecisionStatement>) statements.subList(statementQName);
    }

    /** {@inheritDoc} */
    public List<AttributeStatement> getAttributeStatements() {
        QName statementQName = new QName(SAMLConstants.SAML1_NS, AttributeStatement.DEFAULT_ELEMENT_LOCAL_NAME);
        return (List<AttributeStatement>) statements.subList(statementQName);
    }
    
    /** {@inheritDoc} */
    public String getSignatureReferenceID(){
        return id;
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {

        ArrayList<XMLObject> children = new ArrayList<XMLObject>();

        if (conditions != null) {
            children.add(conditions);
        }

        if (advice != null) {
            children.add(advice);
        }

        children.addAll(statements);
        
        if(getSignature() != null){
            children.add(getSignature());
        }

        if (children.size() == 0) {
            return null;
        }
        return Collections.unmodifiableList(children);
    }
}