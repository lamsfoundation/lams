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

package org.opensaml.saml2.metadata.impl;

import java.util.StringTokenizer;

import javax.xml.namespace.QName;

import org.joda.time.DateTime;
import org.joda.time.chrono.ISOChronology;
import org.opensaml.common.impl.AbstractSAMLObjectUnmarshaller;
import org.opensaml.saml2.common.CacheableSAMLObject;
import org.opensaml.saml2.common.Extensions;
import org.opensaml.saml2.common.TimeBoundSAMLObject;
import org.opensaml.saml2.metadata.ContactPerson;
import org.opensaml.saml2.metadata.KeyDescriptor;
import org.opensaml.saml2.metadata.Organization;
import org.opensaml.saml2.metadata.RoleDescriptor;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.util.DatatypeHelper;
import org.opensaml.xml.util.XMLHelper;
import org.w3c.dom.Attr;

/**
 * A thread safe Unmarshaller for {@link org.opensaml.saml2.metadata.RoleDescriptor} objects.
 */
public abstract class RoleDescriptorUnmarshaller extends AbstractSAMLObjectUnmarshaller {

    /** {@inheritDoc} */
    protected void processChildElement(XMLObject parentSAMLObject, XMLObject childSAMLObject)
            throws UnmarshallingException {
        RoleDescriptor roleDescriptor = (RoleDescriptor) parentSAMLObject;

        if (childSAMLObject instanceof Extensions) {
            roleDescriptor.setExtensions((Extensions) childSAMLObject);
        } else if (childSAMLObject instanceof Signature) {
            roleDescriptor.setSignature((Signature) childSAMLObject);
        } else if (childSAMLObject instanceof KeyDescriptor) {
            roleDescriptor.getKeyDescriptors().add((KeyDescriptor) childSAMLObject);
        } else if (childSAMLObject instanceof Organization) {
            roleDescriptor.setOrganization((Organization) childSAMLObject);
        } else if (childSAMLObject instanceof ContactPerson) {
            roleDescriptor.getContactPersons().add((ContactPerson) childSAMLObject);
        } else {
            super.processChildElement(parentSAMLObject, childSAMLObject);
        }
    }

    /** {@inheritDoc} */
    protected void processAttribute(XMLObject samlObject, Attr attribute) throws UnmarshallingException {
        RoleDescriptor roleDescriptor = (RoleDescriptor) samlObject;

        if (attribute.getLocalName().equals(RoleDescriptor.ID_ATTRIB_NAME)) {
            roleDescriptor.setID(attribute.getValue());
            attribute.getOwnerElement().setIdAttributeNode(attribute, true);
        } else if (attribute.getLocalName().equals(TimeBoundSAMLObject.VALID_UNTIL_ATTRIB_NAME)
                && !DatatypeHelper.isEmpty(attribute.getValue())) {
            roleDescriptor.setValidUntil(new DateTime(attribute.getValue(), ISOChronology.getInstanceUTC()));
        } else if (attribute.getLocalName().equals(CacheableSAMLObject.CACHE_DURATION_ATTRIB_NAME)) {
            roleDescriptor.setCacheDuration(XMLHelper.durationToLong(attribute.getValue()));
        } else if (attribute.getLocalName().equals(RoleDescriptor.PROTOCOL_ENUMERATION_ATTRIB_NAME)) {
            StringTokenizer protocolTokenizer = new StringTokenizer(attribute.getValue(), " ");
            while (protocolTokenizer.hasMoreTokens()) {
                roleDescriptor.addSupportedProtocol(protocolTokenizer.nextToken());
            }
        } else if (attribute.getLocalName().equals(RoleDescriptor.ERROR_URL_ATTRIB_NAME)) {
            roleDescriptor.setErrorURL(attribute.getValue());
        } else {
            QName attribQName = XMLHelper.getNodeQName(attribute);
            if (attribute.isId()) {
                roleDescriptor.getUnknownAttributes().registerID(attribQName);
            }
            roleDescriptor.getUnknownAttributes().put(attribQName, attribute.getValue());
        }
    }
}