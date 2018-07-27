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
import org.opensaml.common.impl.AbstractSAMLObjectUnmarshaller;
import org.opensaml.saml2.core.AuthnContext;
import org.opensaml.saml2.core.AuthnStatement;
import org.opensaml.saml2.core.SubjectLocality;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.util.DatatypeHelper;
import org.w3c.dom.Attr;

/**
 * A thread-safe Unmarshaller for {@link org.opensaml.saml2.core.AuthnStatement}.
 */
public class AuthnStatementUnmarshaller extends AbstractSAMLObjectUnmarshaller {

    /** {@inheritDoc} */
    protected void processChildElement(XMLObject parentObject, XMLObject childObject) throws UnmarshallingException {
        AuthnStatement authnStatement = (AuthnStatement) parentObject;
        if (childObject instanceof SubjectLocality) {
            authnStatement.setSubjectLocality((SubjectLocality) childObject);
        } else if (childObject instanceof AuthnContext) {
            authnStatement.setAuthnContext((AuthnContext) childObject);
        } else {
            super.processChildElement(parentObject, childObject);
        }
    }

    /** {@inheritDoc} */
    protected void processAttribute(XMLObject samlObject, Attr attribute) throws UnmarshallingException {
        AuthnStatement authnStatement = (AuthnStatement) samlObject;
        if (attribute.getLocalName().equals(AuthnStatement.AUTHN_INSTANT_ATTRIB_NAME)
                && !DatatypeHelper.isEmpty(attribute.getValue())) {
            authnStatement.setAuthnInstant(new DateTime(attribute.getValue(), ISOChronology.getInstanceUTC()));
        } else if (attribute.getLocalName().equals(AuthnStatement.SESSION_INDEX_ATTRIB_NAME)) {
            authnStatement.setSessionIndex(attribute.getValue());
        } else if (attribute.getLocalName().equals(AuthnStatement.SESSION_NOT_ON_OR_AFTER_ATTRIB_NAME)
                && !DatatypeHelper.isEmpty(attribute.getValue())) {
            authnStatement.setSessionNotOnOrAfter(new DateTime(attribute.getValue(), ISOChronology.getInstanceUTC()));
        } else {
            super.processAttribute(samlObject, attribute);
        }
    }
}