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

import org.joda.time.DateTime;
import org.joda.time.chrono.ISOChronology;
import org.opensaml.common.SAMLVersion;
import org.opensaml.common.impl.AbstractSAMLObjectUnmarshaller;
import org.opensaml.saml2.core.Advice;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Conditions;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.Statement;
import org.opensaml.saml2.core.Subject;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.util.DatatypeHelper;
import org.w3c.dom.Attr;

/**
 * A thread-safe Unmarshaller for {@link org.opensaml.saml2.core.Assertion}.
 */
public class AssertionUnmarshaller extends AbstractSAMLObjectUnmarshaller {

    /** {@inheritDoc} */
    protected void processChildElement(XMLObject parentObject, XMLObject childObject) throws UnmarshallingException {
        Assertion assertion = (Assertion) parentObject;

        if (childObject instanceof Issuer) {
            assertion.setIssuer((Issuer) childObject);
        } else if (childObject instanceof Signature) {
            assertion.setSignature((Signature) childObject);
        } else if (childObject instanceof Subject) {
            assertion.setSubject((Subject) childObject);
        } else if (childObject instanceof Conditions) {
            assertion.setConditions((Conditions) childObject);
        } else if (childObject instanceof Advice) {
            assertion.setAdvice((Advice) childObject);
        } else if (childObject instanceof Statement) {
            assertion.getStatements().add((Statement) childObject);
        } else {
            super.processChildElement(parentObject, childObject);
        }
    }

    /** {@inheritDoc} */
    protected void processAttribute(XMLObject samlObject, Attr attribute) throws UnmarshallingException {
        Assertion assertion = (Assertion) samlObject;

        if (attribute.getLocalName().equals(Assertion.VERSION_ATTRIB_NAME)) {
            assertion.setVersion(SAMLVersion.valueOf(attribute.getValue()));
        } else if (attribute.getLocalName().equals(Assertion.ISSUE_INSTANT_ATTRIB_NAME)
                && !DatatypeHelper.isEmpty(attribute.getValue())) {
            assertion.setIssueInstant(new DateTime(attribute.getValue(), ISOChronology.getInstanceUTC()));
        } else if (attribute.getLocalName().equals(Assertion.ID_ATTRIB_NAME)) {
            assertion.setID(attribute.getValue());
            attribute.getOwnerElement().setIdAttributeNode(attribute, true);
        } else {
            super.processAttribute(samlObject, attribute);
        }
    }
}