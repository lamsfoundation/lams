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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.opensaml.common.impl.AbstractSAMLObject;
import org.opensaml.samlext.samlec.EncType;
import org.opensaml.samlext.samlec.SessionKey;
import org.opensaml.ws.soap.soap11.ActorBearing;
import org.opensaml.ws.soap.soap11.MustUnderstandBearing;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.schema.XSBooleanValue;
import org.opensaml.xml.signature.KeyInfo;
import org.opensaml.xml.util.XMLObjectChildrenList;

/**
 * Concrete implementation of {@link SessionKey}.
 */
public class SessionKeyImpl extends AbstractSAMLObject implements SessionKey {

    /** soap11:actor attribute. */
    private String soap11Actor;
    
    /** soap11:mustUnderstand. */
    private XSBooleanValue soap11MustUnderstand;

    /** Algorithm attribute. */
    private String algorithm;
    
    /** EncType children. */
    private XMLObjectChildrenList<EncType> encTypes;

    /** KeyInfo child. */
    private KeyInfo keyInfo;
    
    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected SessionKeyImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        
        encTypes = new XMLObjectChildrenList<EncType>(this);
    }

    /** {@inheritDoc} */
    public Boolean isSOAP11MustUnderstand() {
        if (soap11MustUnderstand != null) {
            return soap11MustUnderstand.getValue();
        }
        return Boolean.FALSE;
    }

    /** {@inheritDoc} */
    public XSBooleanValue isSOAP11MustUnderstandXSBoolean() {
        return soap11MustUnderstand;
    }

    /** {@inheritDoc} */
    public void setSOAP11MustUnderstand(Boolean newMustUnderstand) {
        if (newMustUnderstand != null) {
            soap11MustUnderstand = prepareForAssignment(soap11MustUnderstand, 
                    new XSBooleanValue(newMustUnderstand, true));
        } else {
            soap11MustUnderstand = prepareForAssignment(soap11MustUnderstand, null);
        }
        manageQualifiedAttributeNamespace(MustUnderstandBearing.SOAP11_MUST_UNDERSTAND_ATTR_NAME, 
                soap11MustUnderstand != null);
    }

    /** {@inheritDoc} */
    public void setSOAP11MustUnderstand(XSBooleanValue newMustUnderstand) {
            soap11MustUnderstand = prepareForAssignment(soap11MustUnderstand, newMustUnderstand);
            manageQualifiedAttributeNamespace(MustUnderstandBearing.SOAP11_MUST_UNDERSTAND_ATTR_NAME, 
                    soap11MustUnderstand != null);
    }

    /** {@inheritDoc} */
    public String getSOAP11Actor() {
        return soap11Actor;
    }

    /** {@inheritDoc} */
    public void setSOAP11Actor(String newActor) {
        soap11Actor = prepareForAssignment(soap11Actor, newActor);
        manageQualifiedAttributeNamespace(ActorBearing.SOAP11_ACTOR_ATTR_NAME, soap11Actor != null);
    }
    
    /** {@inheritDoc} */
    public String getAlgorithm() {
        return algorithm;
    }

    /** {@inheritDoc} */
    public void setAlgorithm(String newAlgorithm) {
        algorithm = prepareForAssignment(algorithm, newAlgorithm);
    }

    /** {@inheritDoc} */
    public List<EncType> getEncTypes() {
        return encTypes;
    }
    
    /** {@inheritDoc} */
    public KeyInfo getKeyInfo() {
        return keyInfo;
    }

    /** {@inheritDoc} */
    public void setKeyInfo(KeyInfo newKeyInfo) {
        keyInfo = prepareForAssignment(keyInfo, newKeyInfo);
    }
    
    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();

        children.addAll(encTypes);
        
        if (keyInfo != null) {
            children.add(keyInfo);
        }

        return Collections.unmodifiableList(children);
    }
}