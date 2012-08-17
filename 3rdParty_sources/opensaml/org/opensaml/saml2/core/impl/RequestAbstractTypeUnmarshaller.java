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
import org.opensaml.saml2.common.Extensions;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.RequestAbstractType;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.util.DatatypeHelper;
import org.w3c.dom.Attr;

/**
 * A thread-safe Unmarshaller for {@link org.opensaml.saml2.core.RequestAbstractType} objects.
 */
public abstract class RequestAbstractTypeUnmarshaller extends AbstractSAMLObjectUnmarshaller {

    /** {@inheritDoc} */
    protected void processAttribute(XMLObject samlObject, Attr attribute) throws UnmarshallingException {
        RequestAbstractType req = (RequestAbstractType) samlObject;

        if (attribute.getLocalName().equals(RequestAbstractType.VERSION_ATTRIB_NAME)) {
            req.setVersion(SAMLVersion.valueOf(attribute.getValue()));
        } else if (attribute.getLocalName().equals(RequestAbstractType.ID_ATTRIB_NAME)) {
            req.setID(attribute.getValue());
            attribute.getOwnerElement().setIdAttributeNode(attribute, true);
        } else if (attribute.getLocalName().equals(RequestAbstractType.ISSUE_INSTANT_ATTRIB_NAME)
                && !DatatypeHelper.isEmpty(attribute.getValue())) {
            req.setIssueInstant(new DateTime(attribute.getValue(), ISOChronology.getInstanceUTC()));
        } else if (attribute.getLocalName().equals(RequestAbstractType.DESTINATION_ATTRIB_NAME)) {
            req.setDestination(attribute.getValue());
        } else if (attribute.getLocalName().equals(RequestAbstractType.CONSENT_ATTRIB_NAME)) {
            req.setConsent(attribute.getValue());
        } else {
            super.processAttribute(samlObject, attribute);
        }
    }

    /** {@inheritDoc} */
    protected void processChildElement(XMLObject parentSAMLObject, XMLObject childSAMLObject)
            throws UnmarshallingException {
        RequestAbstractType req = (RequestAbstractType) parentSAMLObject;

        if (childSAMLObject instanceof Issuer) {
            req.setIssuer((Issuer) childSAMLObject);
        } else if (childSAMLObject instanceof Signature) {
            req.setSignature((Signature) childSAMLObject);
        } else if (childSAMLObject instanceof Extensions) {
            req.setExtensions((Extensions) childSAMLObject);
        } else {
            super.processChildElement(parentSAMLObject, childSAMLObject);
        }
    }
}