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

import org.joda.time.DateTime;
import org.opensaml.common.impl.AbstractSAMLObject;
import org.opensaml.saml2.core.AuthnContext;
import org.opensaml.saml2.core.AuthnStatement;
import org.opensaml.saml2.core.SubjectLocality;
import org.opensaml.xml.XMLObject;

/**
 * A concrete implementation of {@link org.opensaml.saml2.core.AuthnStatement}.
 */
public class AuthnStatementImpl extends AbstractSAMLObject implements AuthnStatement {

    /** Subject Locality of the Authentication Statement. */
    private SubjectLocality subjectLocality;

    /** Authentication Context of the Authentication Statement. */
    private AuthnContext authnContext;

    /** Time of the authentication. */
    private DateTime authnInstant;

    /** Index of the session. */
    private String sessionIndex;

    /** Time at which the session ends. */
    private DateTime sessionNotOnOrAfter;

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected AuthnStatementImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
    }

    /** {@inheritDoc} */
    public SubjectLocality getSubjectLocality() {
        return subjectLocality;
    }

    /** {@inheritDoc} */
    public void setSubjectLocality(SubjectLocality newSubjectLocality) {
        this.subjectLocality = prepareForAssignment(this.subjectLocality, newSubjectLocality);
    }

    /** {@inheritDoc} */
    public AuthnContext getAuthnContext() {
        return authnContext;
    }

    /** {@inheritDoc} */
    public void setAuthnContext(AuthnContext newAuthnContext) {
        this.authnContext = prepareForAssignment(this.authnContext, newAuthnContext);
    }

    /** {@inheritDoc} */
    public DateTime getAuthnInstant() {
        return authnInstant;
    }

    /** {@inheritDoc} */
    public void setAuthnInstant(DateTime newAuthnInstant) {
        this.authnInstant = prepareForAssignment(this.authnInstant, newAuthnInstant);
    }

    /** {@inheritDoc} */
    public String getSessionIndex() {
        return sessionIndex;
    }

    /** {@inheritDoc} */
    public void setSessionIndex(String newSessionIndex) {
        this.sessionIndex = prepareForAssignment(this.sessionIndex, newSessionIndex);
    }

    /** {@inheritDoc} */
    public DateTime getSessionNotOnOrAfter() {
        return sessionNotOnOrAfter;
    }

    /** {@inheritDoc} */
    public void setSessionNotOnOrAfter(DateTime newSessionNotOnOrAfter) {
        this.sessionNotOnOrAfter = prepareForAssignment(this.sessionNotOnOrAfter, newSessionNotOnOrAfter);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();

        children.add(subjectLocality);
        children.add(authnContext);

        return Collections.unmodifiableList(children);
    }
}