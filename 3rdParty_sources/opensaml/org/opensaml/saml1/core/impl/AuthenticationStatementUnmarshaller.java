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

import org.joda.time.DateTime;
import org.joda.time.chrono.ISOChronology;
import org.opensaml.saml1.core.AuthenticationStatement;
import org.opensaml.saml1.core.AuthorityBinding;
import org.opensaml.saml1.core.SubjectLocality;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.util.DatatypeHelper;
import org.w3c.dom.Attr;

/**
 * A thread-safe Unmarshaller for {@link org.opensaml.saml1.core.AuthenticationStatement} objects.
 */
public class AuthenticationStatementUnmarshaller extends SubjectStatementUnmarshaller {

    /** {@inheritDoc} */
    protected void processChildElement(XMLObject parentSAMLObject, XMLObject childSAMLObject)
            throws UnmarshallingException {

        AuthenticationStatement authenticationStatement = (AuthenticationStatement) parentSAMLObject;

        if (childSAMLObject instanceof SubjectLocality) {
            authenticationStatement.setSubjectLocality((SubjectLocality) childSAMLObject);
        } else if (childSAMLObject instanceof AuthorityBinding) {
            authenticationStatement.getAuthorityBindings().add((AuthorityBinding) childSAMLObject);
        } else {
            super.processChildElement(parentSAMLObject, childSAMLObject);
        }
    }

    /** {@inheritDoc} */
    protected void processAttribute(XMLObject samlObject, Attr attribute) throws UnmarshallingException {
        AuthenticationStatement authenticationStatement = (AuthenticationStatement) samlObject;

        if (AuthenticationStatement.AUTHENTICATIONINSTANT_ATTRIB_NAME.equals(attribute.getLocalName())
                && !DatatypeHelper.isEmpty(attribute.getValue())) {
            DateTime value = new DateTime(attribute.getValue(), ISOChronology.getInstanceUTC());
            authenticationStatement.setAuthenticationInstant(value);
        } else if (AuthenticationStatement.AUTHENTICATIONMETHOD_ATTRIB_NAME.equals(attribute.getLocalName())) {
            authenticationStatement.setAuthenticationMethod(attribute.getValue());
        } else {
            super.processAttribute(samlObject, attribute);
        }
    }
}