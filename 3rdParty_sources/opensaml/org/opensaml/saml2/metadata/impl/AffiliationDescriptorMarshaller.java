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

package org.opensaml.saml2.metadata.impl;

import java.util.Map.Entry;

import javax.xml.namespace.QName;

import org.opensaml.Configuration;
import org.opensaml.common.impl.AbstractSAMLObjectMarshaller;
import org.opensaml.saml2.common.CacheableSAMLObject;
import org.opensaml.saml2.common.TimeBoundSAMLObject;
import org.opensaml.saml2.metadata.AffiliationDescriptor;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.util.XMLHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

/**
 * A thread safe Marshaller for {@link org.opensaml.saml2.metadata.AffiliationDescriptor} objects.
 */
public class AffiliationDescriptorMarshaller extends AbstractSAMLObjectMarshaller {

    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(AffiliationDescriptorMarshaller.class);

    /** {@inheritDoc} */
    protected void marshallAttributes(XMLObject samlElement, Element domElement) throws MarshallingException {
        AffiliationDescriptor descriptor = (AffiliationDescriptor) samlElement;

        // Set affiliationOwnerID
        if (descriptor.getOwnerID() != null) {
            domElement.setAttributeNS(null, AffiliationDescriptor.OWNER_ID_ATTRIB_NAME, descriptor.getOwnerID());
        }

        // Set ID
        if (descriptor.getID() != null) {
            domElement.setAttributeNS(null, AffiliationDescriptor.ID_ATTRIB_NAME, descriptor.getID());
            domElement.setIdAttributeNS(null, AffiliationDescriptor.ID_ATTRIB_NAME, true);
        }

        // Set the validUntil attribute
        if (descriptor.getValidUntil() != null) {
            log.debug("Writting validUntil attribute to AffiliationDescriptor DOM element");
            String validUntilStr = Configuration.getSAMLDateFormatter().print(descriptor.getValidUntil());
            domElement.setAttributeNS(null, TimeBoundSAMLObject.VALID_UNTIL_ATTRIB_NAME, validUntilStr);
        }

        // Set the cacheDuration attribute
        if (descriptor.getCacheDuration() != null) {
            log.debug("Writting cacheDuration attribute to AffiliationDescriptor DOM element");
            String cacheDuration = XMLHelper.longToDuration(descriptor.getCacheDuration());
            domElement.setAttributeNS(null, CacheableSAMLObject.CACHE_DURATION_ATTRIB_NAME, cacheDuration);
        }

        Attr attribute;
        for (Entry<QName, String> entry : descriptor.getUnknownAttributes().entrySet()) {
            attribute = XMLHelper.constructAttribute(domElement.getOwnerDocument(), entry.getKey());
            attribute.setValue(entry.getValue());
            domElement.setAttributeNodeNS(attribute);
            if (Configuration.isIDAttribute(entry.getKey())
                    || descriptor.getUnknownAttributes().isIDAttribute(entry.getKey())) {
                attribute.getOwnerElement().setIdAttributeNode(attribute, true);
            }
        }
    }
}