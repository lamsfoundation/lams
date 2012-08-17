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

import org.opensaml.saml1.core.Action;
import org.opensaml.saml1.core.AuthorizationDecisionStatement;
import org.opensaml.saml1.core.DecisionTypeEnumeration;
import org.opensaml.saml1.core.Evidence;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.UnmarshallingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Attr;

/**
 * A thread-safe Unmarshaller for {@link org.opensaml.saml1.core.impl.AuthorizationDecisionStatementImpl} objects.
 */
public class AuthorizationDecisionStatementUnmarshaller extends SubjectStatementUnmarshaller {

    /** Logger. */
    private final Logger log = LoggerFactory.getLogger(AuthorizationDecisionStatementUnmarshaller.class);

    /** {@inheritDoc} */
    protected void processChildElement(XMLObject parentSAMLObject, XMLObject childSAMLObject)
            throws UnmarshallingException {

        AuthorizationDecisionStatement authorizationDecisionStatement;
        authorizationDecisionStatement = (AuthorizationDecisionStatement) parentSAMLObject;

        if (childSAMLObject instanceof Action) {
            authorizationDecisionStatement.getActions().add((Action) childSAMLObject);
        } else if (childSAMLObject instanceof Evidence) {
            authorizationDecisionStatement.setEvidence((Evidence) childSAMLObject);
        } else {
            super.processChildElement(parentSAMLObject, childSAMLObject);
        }
    }

    /** {@inheritDoc} */
    protected void processAttribute(XMLObject samlObject, Attr attribute) throws UnmarshallingException {

        AuthorizationDecisionStatement authorizationDecisionStatement;
        authorizationDecisionStatement = (AuthorizationDecisionStatement) samlObject;

        if (AuthorizationDecisionStatement.DECISION_ATTRIB_NAME.equals(attribute.getLocalName())) {
            String value = attribute.getValue();
            if (value.equals(DecisionTypeEnumeration.PERMIT.toString())) {
                authorizationDecisionStatement.setDecision(DecisionTypeEnumeration.PERMIT);
            } else if (value.equals(DecisionTypeEnumeration.DENY.toString())) {
                authorizationDecisionStatement.setDecision(DecisionTypeEnumeration.DENY);
            } else if (value.equals(DecisionTypeEnumeration.INDETERMINATE.toString())) {
                authorizationDecisionStatement.setDecision(DecisionTypeEnumeration.INDETERMINATE);
            } else {
                log.error("Unknown value for DecisionType '" + value + "'");
                throw new UnmarshallingException("Unknown value for DecisionType '" + value + "'");
            }
        } else if (AuthorizationDecisionStatement.RESOURCE_ATTRIB_NAME.equals(attribute.getLocalName())) {
            authorizationDecisionStatement.setResource(attribute.getValue());
        } else {
            super.processAttribute(samlObject, attribute);
        }
    }
}