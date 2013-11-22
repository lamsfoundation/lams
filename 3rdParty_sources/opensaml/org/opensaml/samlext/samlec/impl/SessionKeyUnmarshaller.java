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

package org.opensaml.samlext.samlec.impl;

import javax.xml.namespace.QName;

import org.opensaml.common.impl.AbstractSAMLObjectUnmarshaller;
import org.opensaml.samlext.samlec.EncType;
import org.opensaml.samlext.samlec.SessionKey;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.schema.XSBooleanValue;
import org.opensaml.xml.signature.KeyInfo;
import org.opensaml.xml.util.XMLHelper;
import org.w3c.dom.Attr;

/**
 * A thread-safe Unmarshaller for {@link SessionKey} objects.
 */
public class SessionKeyUnmarshaller extends AbstractSAMLObjectUnmarshaller {

    /** {@inheritDoc} */
    protected void processChildElement(XMLObject parentObject, XMLObject childObject) throws UnmarshallingException {
        SessionKey key = (SessionKey) parentObject;

        if (childObject instanceof EncType) {
            key.getEncTypes().add((EncType) childObject);
        } else if (childObject instanceof KeyInfo) {
            key.setKeyInfo((KeyInfo) childObject);
        } else {
            super.processChildElement(parentObject, childObject);
        }
    }

    /** {@inheritDoc} */
    protected void processAttribute(XMLObject samlObject, Attr attribute) throws UnmarshallingException {
        SessionKey key = (SessionKey) samlObject;

        QName attrName = XMLHelper.getNodeQName(attribute);
        if (SessionKey.SOAP11_MUST_UNDERSTAND_ATTR_NAME.equals(attrName)) {
            key.setSOAP11MustUnderstand(XSBooleanValue.valueOf(attribute.getValue()));
        } else if (SessionKey.SOAP11_ACTOR_ATTR_NAME.equals(attrName)) {
            key.setSOAP11Actor(attribute.getValue()); 
        } else if (attribute.getLocalName().equals(SessionKey.ALGORITHM_ATTRIB_NAME)) {
            key.setAlgorithm(attribute.getValue());
        } else {
            super.processAttribute(samlObject, attribute);
        }
    }
    
}