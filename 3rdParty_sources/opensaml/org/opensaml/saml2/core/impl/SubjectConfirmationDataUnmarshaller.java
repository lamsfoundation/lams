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

import javax.xml.namespace.QName;

import org.joda.time.DateTime;
import org.joda.time.chrono.ISOChronology;
import org.opensaml.common.impl.AbstractSAMLObjectUnmarshaller;
import org.opensaml.saml2.core.SubjectConfirmationData;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.util.DatatypeHelper;
import org.opensaml.xml.util.XMLHelper;
import org.w3c.dom.Attr;

/**
 * A thread-safe Unmarshaller for {@link org.opensaml.saml2.core.SubjectConfirmationData} objects.
 */
public class SubjectConfirmationDataUnmarshaller extends AbstractSAMLObjectUnmarshaller {

    /**
     * {@inheritDoc}
     */
    protected void processChildElement(XMLObject parentSAMLObject, XMLObject childSAMLObject)
            throws UnmarshallingException {
        SubjectConfirmationData subjectCD = (SubjectConfirmationData) parentSAMLObject;

        subjectCD.getUnknownXMLObjects().add(childSAMLObject);
    }

    /** {@inheritDoc} */
    protected void processAttribute(XMLObject samlObject, Attr attribute) throws UnmarshallingException {
        SubjectConfirmationData subjectCD = (SubjectConfirmationData) samlObject;

        if (attribute.getLocalName().equals(SubjectConfirmationData.NOT_BEFORE_ATTRIB_NAME)
                && !DatatypeHelper.isEmpty(attribute.getValue())) {
            subjectCD.setNotBefore(new DateTime(attribute.getValue(), ISOChronology.getInstanceUTC()));
        } else if (attribute.getLocalName().equals(SubjectConfirmationData.NOT_ON_OR_AFTER_ATTRIB_NAME)
                && !DatatypeHelper.isEmpty(attribute.getValue())) {
            subjectCD.setNotOnOrAfter(new DateTime(attribute.getValue(), ISOChronology.getInstanceUTC()));
        } else if (attribute.getLocalName().equals(SubjectConfirmationData.RECIPIENT_ATTRIB_NAME)) {
            subjectCD.setRecipient(attribute.getValue());
        } else if (attribute.getLocalName().equals(SubjectConfirmationData.IN_RESPONSE_TO_ATTRIB_NAME)) {
            subjectCD.setInResponseTo(attribute.getValue());
        } else if (attribute.getLocalName().equals(SubjectConfirmationData.ADDRESS_ATTRIB_NAME)) {
            subjectCD.setAddress(attribute.getValue());
        } else {
            QName attribQName = XMLHelper.getNodeQName(attribute);
            if (attribute.isId()) {
                subjectCD.getUnknownAttributes().registerID(attribQName);
            }
            subjectCD.getUnknownAttributes().put(attribQName, attribute.getValue());
        }
    }
}