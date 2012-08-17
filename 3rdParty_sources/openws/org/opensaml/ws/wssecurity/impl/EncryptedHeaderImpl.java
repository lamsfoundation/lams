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

package org.opensaml.ws.wssecurity.impl;

import java.util.Collections;
import java.util.List;

import org.opensaml.ws.soap.soap11.ActorBearing;
import org.opensaml.ws.soap.soap11.MustUnderstandBearing;
import org.opensaml.ws.soap.soap12.RelayBearing;
import org.opensaml.ws.soap.soap12.RoleBearing;
import org.opensaml.ws.wssecurity.EncryptedHeader;
import org.opensaml.ws.wssecurity.IdBearing;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.encryption.EncryptedData;
import org.opensaml.xml.schema.XSBooleanValue;
import org.opensaml.xml.util.LazyList;

/**
 * Implementation of {@link EncryptedHeader}.
 */
public class EncryptedHeaderImpl extends AbstractWSSecurityObject implements EncryptedHeader {
    
    /** EncryptedData child element. */
    private EncryptedData encryptedData;
    
    /** The <code>@wsu:Id</code> atribute. */
    private String wsuId;
    
    /** The <code>@soap11:mustUnderstand</code> atribute. */
    private XSBooleanValue soap11MustUnderstand;
    
    /** The <code>@soap11:actor</code> atribute. */
    private String soap11Actor;
    
    /** The <code>@soap12:mustUnderstand</code> atribute. */
    private XSBooleanValue soap12MustUnderstand;
    
    /** The <code>@soap12:role</code> atribute. */
    private String soap12Role;
    
    /** The <code>@soap12:relay</code> atribute. */
    private XSBooleanValue soap12Relay;
    
    /**
     * Constructor.
     * 
     * @param namespaceURI namespace of the element
     * @param elementLocalName name of the element
     * @param namespacePrefix namespace prefix of the element
     */
    public EncryptedHeaderImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
    }

    /** {@inheritDoc} */
    public EncryptedData getEncryptedData() {
        return encryptedData;
    }

    /** {@inheritDoc} */
    public void setEncryptedData(EncryptedData newEncryptedData) {
        encryptedData = prepareForAssignment(encryptedData, newEncryptedData);
    }

    /** {@inheritDoc} */
    public String getWSUId() {
        return wsuId;
    }

    /** {@inheritDoc} */
    public void setWSUId(String newId) {
        String oldId = wsuId;
        wsuId = prepareForAssignment(wsuId, newId);
        registerOwnID(oldId, wsuId);
        manageQualifiedAttributeNamespace(IdBearing.WSU_ID_ATTR_NAME, wsuId != null);
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
    public Boolean isSOAP12MustUnderstand() {
        if (soap12MustUnderstand != null) {
            return soap12MustUnderstand.getValue();
        }
        return Boolean.FALSE;
    }

    /** {@inheritDoc} */
    public XSBooleanValue isSOAP12MustUnderstandXSBoolean() {
        return soap12MustUnderstand;
    }

    /** {@inheritDoc} */
    public void setSOAP12MustUnderstand(Boolean newMustUnderstand) {
        if (newMustUnderstand != null) {
            soap12MustUnderstand = prepareForAssignment(soap12MustUnderstand, 
                    new XSBooleanValue(newMustUnderstand, false));
        } else {
            soap12MustUnderstand = prepareForAssignment(soap12MustUnderstand, null);
        }
        manageQualifiedAttributeNamespace(org.opensaml.ws.soap.soap12.MustUnderstandBearing.SOAP12_MUST_UNDERSTAND_ATTR_NAME, 
                soap12MustUnderstand != null);
    }

    /** {@inheritDoc} */
    public void setSOAP12MustUnderstand(XSBooleanValue newMustUnderstand) {
            soap12MustUnderstand = prepareForAssignment(soap12MustUnderstand, newMustUnderstand);
            manageQualifiedAttributeNamespace(org.opensaml.ws.soap.soap12.MustUnderstandBearing.SOAP12_MUST_UNDERSTAND_ATTR_NAME, 
                    soap12MustUnderstand != null);
    }

    /** {@inheritDoc} */
    public String getSOAP12Role() {
        return soap12Role;
    }

    /** {@inheritDoc} */
    public void setSOAP12Role(String newRole) {
        soap12Role = prepareForAssignment(soap12Role, newRole);
        manageQualifiedAttributeNamespace(RoleBearing.SOAP12_ROLE_ATTR_NAME, soap12Role != null);
    }

    /** {@inheritDoc} */
    public Boolean isSOAP12Relay() {
        if (soap12Relay != null) {
            return soap12Relay.getValue();
        }
        return Boolean.FALSE;
    }

    /** {@inheritDoc} */
    public XSBooleanValue isSOAP12RelayXSBoolean() {
        return soap12Relay;
    }

    /** {@inheritDoc} */
    public void setSOAP12Relay(Boolean newRelay) {
        if (newRelay != null) {
            soap12Relay = prepareForAssignment(soap12Relay, 
                    new XSBooleanValue(newRelay, false));
        } else {
            soap12Relay = prepareForAssignment(soap12Relay, null);
        }
        manageQualifiedAttributeNamespace(RelayBearing.SOAP12_RELAY_ATTR_NAME, soap12Relay != null);
    }

    /** {@inheritDoc} */
    public void setSOAP12Relay(XSBooleanValue newRelay) {
            soap12Relay = prepareForAssignment(soap12Relay, newRelay);
            manageQualifiedAttributeNamespace(RelayBearing.SOAP12_RELAY_ATTR_NAME, soap12Relay != null);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        LazyList<XMLObject> children = new LazyList<XMLObject>();
        if (encryptedData != null) {
            children.add(encryptedData);
        }
        return Collections.unmodifiableList(children);
    }

}
