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

package org.opensaml.saml2.ecp.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.opensaml.common.impl.AbstractSAMLObject;
import org.opensaml.saml2.core.IDPList;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.ecp.Request;
import org.opensaml.ws.soap.soap11.ActorBearing;
import org.opensaml.ws.soap.soap11.MustUnderstandBearing;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.schema.XSBooleanValue;

/**
 *  A concrete implementation of {@link Request}.
 */
public class RequestImpl extends AbstractSAMLObject implements Request {
    
    /** IDPList child element. */
    private IDPList idpList;
    
    /** Issuer child element. */
    private Issuer issuer;
    
    /** ProviderName attribute. */
    private String providerName;
    
    /** IsPassive attribute value. */
    private XSBooleanValue isPassive;
    
    /** soap11:actor attribute. */
    private String soap11Actor;
    
    /** soap11:mustUnderstand. */
    private XSBooleanValue soap11MustUnderstand;
    
    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected RequestImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
    }

    /** {@inheritDoc} */
    public IDPList getIDPList() {
        return idpList;
    }
    
    /** {@inheritDoc} */
    public void setIDPList(IDPList newIDPList) {
        idpList = prepareForAssignment(idpList, newIDPList);
    }

    /** {@inheritDoc} */
    public Issuer getIssuer() {
        return issuer;
    }
    
    /** {@inheritDoc} */
    public void setIssuer(Issuer newIssuer) {
        issuer = prepareForAssignment(issuer, newIssuer);
    }

    /** {@inheritDoc} */
    public String getProviderName() {
        return providerName;
    }

    /** {@inheritDoc} */
    public void setProviderName(String newProviderName) {
        providerName = prepareForAssignment(providerName, newProviderName);
    }

    /** {@inheritDoc} */
    public Boolean isPassive() {
        if (isPassive != null) {
            return isPassive.getValue();
        }

        return Boolean.FALSE;
    }

    /** {@inheritDoc} */
    public XSBooleanValue isPassiveXSBoolean() {
        return isPassive;
    }

    /** {@inheritDoc} */
    public void setPassive(Boolean newIsPassive) {
        if (newIsPassive != null) {
            isPassive = prepareForAssignment(isPassive, new XSBooleanValue(newIsPassive, false));
        } else {
            isPassive = prepareForAssignment(isPassive, null);
        }
    }

    /** {@inheritDoc} */
    public void setPassive(XSBooleanValue newIsPassive) {
        this.isPassive = prepareForAssignment(this.isPassive, newIsPassive);
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
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();
        if (issuer!=null) {
            children.add(issuer);
        }
        if (idpList!=null) {
            children.add(idpList);
        }
        return Collections.unmodifiableList(children);
    }

}
