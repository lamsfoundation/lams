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

package org.opensaml.ws.wspolicy.impl;

import javax.xml.namespace.QName;

import org.opensaml.ws.wspolicy.PolicyReference;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.util.XMLHelper;
import org.w3c.dom.Attr;

/**
 * Unmarshaller for the wsp:PolicyReference element.
 * 
 */
public class PolicyReferenceUnmarshaller extends AbstractWSPolicyObjectUnmarshaller {

    /** {@inheritDoc} */
    protected void processAttribute(XMLObject xmlObject, Attr attribute) throws UnmarshallingException {
        PolicyReference pr = (PolicyReference) xmlObject;

        QName uriName = new QName(PolicyReference.URI_ATTRIB_NAME);
        QName digestName = new QName(PolicyReference.DIGEST_ATTRIB_NAME);
        QName digestAlgorithmName = new QName(PolicyReference.DIGEST_ALGORITHM_ATTRIB_NAME);

        QName attribQName = 
            XMLHelper.constructQName(attribute.getNamespaceURI(), attribute.getLocalName(), attribute .getPrefix());

        if (uriName.equals(attribQName)) {
            pr.setURI(attribute.getValue());
        } else if (digestName.equals(attribQName)) {
            pr.setDigest(attribute.getValue());
        } else if (digestAlgorithmName.equals(attribQName)) {
            pr.setDigestAlgorithm(attribute.getValue());
        } else {
            XMLHelper.unmarshallToAttributeMap(pr.getUnknownAttributes(), attribute);
        }
    }

}
