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

package org.opensaml.saml1.core.impl;

import org.joda.time.DateTime;
import org.joda.time.chrono.ISOChronology;
import org.opensaml.common.SAMLVersion;
import org.opensaml.common.impl.AbstractSAMLObjectUnmarshaller;
import org.opensaml.saml1.core.RequestAbstractType;
import org.opensaml.saml1.core.RespondWith;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.util.DatatypeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

/**
 * A thread safe Unmarshaller for {@link org.opensaml.saml1.core.RequestAbstractType} objects.
 */
public abstract class RequestAbstractTypeUnmarshaller extends AbstractSAMLObjectUnmarshaller {

    /** Logger. */
    private final Logger log = LoggerFactory.getLogger(RequestAbstractType.class);

    /** {@inheritDoc} */
    public XMLObject unmarshall(Element domElement) throws UnmarshallingException {
        // After regular unmarshalling, check the minor version and set ID-ness if not SAML 1.0
        RequestAbstractType request = (RequestAbstractType) super.unmarshall(domElement);
        if (request.getMinorVersion() != 0 && !DatatypeHelper.isEmpty(request.getID())) {
            domElement.setIdAttributeNS(null, RequestAbstractType.ID_ATTRIB_NAME, true);
        }
        return request;
    }

    /** {@inheritDoc} */
    protected void processChildElement(XMLObject parentSAMLObject, XMLObject childSAMLObject)
            throws UnmarshallingException {
        RequestAbstractType request = (RequestAbstractType) parentSAMLObject;

        if (childSAMLObject instanceof Signature) {
            request.setSignature((Signature) childSAMLObject);
        } else if (childSAMLObject instanceof RespondWith) {
            request.getRespondWiths().add((RespondWith) childSAMLObject);
        } else {
            super.processChildElement(parentSAMLObject, childSAMLObject);
        }
    }

    /** {@inheritDoc} */
    protected void processAttribute(XMLObject samlElement, Attr attribute) throws UnmarshallingException {
        RequestAbstractType request = (RequestAbstractType) samlElement;

        if (RequestAbstractType.ID_ATTRIB_NAME.equals(attribute.getLocalName())) {
            request.setID(attribute.getValue());
        } else if (RequestAbstractType.ISSUEINSTANT_ATTRIB_NAME.equals(attribute.getLocalName())
                && !DatatypeHelper.isEmpty(attribute.getValue())) {
            DateTime cal = new DateTime(attribute.getValue(), ISOChronology.getInstanceUTC());
            request.setIssueInstant(cal);
        } else if (RequestAbstractType.MINORVERSION_ATTRIB_NAME.equals(attribute.getLocalName())) {
            int minor;
            try {
                minor = Integer.parseInt(attribute.getValue());
            } catch (NumberFormatException n) {
                log.error("Unable to parse minor version string", n);
                throw new UnmarshallingException(n);
            }
            if (minor == 0) {
                request.setVersion(SAMLVersion.VERSION_10);
            } else if (minor == 1) {
                request.setVersion(SAMLVersion.VERSION_11);
            }
        } else {
            super.processAttribute(samlElement, attribute);
        }
    }

}