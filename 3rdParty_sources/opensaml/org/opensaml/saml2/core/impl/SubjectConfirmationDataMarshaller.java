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

import java.util.Map.Entry;

import javax.xml.namespace.QName;

import org.opensaml.Configuration;
import org.opensaml.common.impl.AbstractSAMLObjectMarshaller;
import org.opensaml.saml2.core.SubjectConfirmationData;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.util.XMLHelper;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

/**
 * A thread-safe Marshaller for {@link org.opensaml.saml2.core.SubjectConfirmationData} objects.
 */
public class SubjectConfirmationDataMarshaller extends AbstractSAMLObjectMarshaller {

    /** {@inheritDoc} */
    protected void marshallAttributes(XMLObject samlObject, Element domElement) throws MarshallingException {
        SubjectConfirmationData subjectCD = (SubjectConfirmationData) samlObject;

        if (subjectCD.getNotBefore() != null) {
            String notBeforeStr = Configuration.getSAMLDateFormatter().print(subjectCD.getNotBefore());
            domElement.setAttributeNS(null, SubjectConfirmationData.NOT_BEFORE_ATTRIB_NAME, notBeforeStr);
        }

        if (subjectCD.getNotOnOrAfter() != null) {
            String notOnOrAfterStr = Configuration.getSAMLDateFormatter().print(subjectCD.getNotOnOrAfter());
            domElement.setAttributeNS(null, SubjectConfirmationData.NOT_ON_OR_AFTER_ATTRIB_NAME, notOnOrAfterStr);
        }

        if (subjectCD.getRecipient() != null) {
            domElement.setAttributeNS(null, SubjectConfirmationData.RECIPIENT_ATTRIB_NAME, subjectCD.getRecipient());
        }

        if (subjectCD.getInResponseTo() != null) {
            domElement.setAttributeNS(null, SubjectConfirmationData.IN_RESPONSE_TO_ATTRIB_NAME, subjectCD
                    .getInResponseTo());
        }

        if (subjectCD.getAddress() != null) {
            domElement.setAttributeNS(null, SubjectConfirmationData.ADDRESS_ATTRIB_NAME, subjectCD.getAddress());
        }

        Attr attribute;
        for (Entry<QName, String> entry : subjectCD.getUnknownAttributes().entrySet()) {
            attribute = XMLHelper.constructAttribute(domElement.getOwnerDocument(), entry.getKey());
            attribute.setValue(entry.getValue());
            domElement.setAttributeNodeNS(attribute);
            if (Configuration.isIDAttribute(entry.getKey())
                    || subjectCD.getUnknownAttributes().isIDAttribute(entry.getKey())) {
                attribute.getOwnerElement().setIdAttributeNode(attribute, true);
            }
        }
    }
}