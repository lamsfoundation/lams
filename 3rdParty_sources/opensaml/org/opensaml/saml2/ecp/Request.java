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

package org.opensaml.saml2.ecp;

import javax.xml.namespace.QName;

import org.opensaml.common.SAMLObject;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.core.IDPList;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.ws.soap.soap11.ActorBearing;
import org.opensaml.ws.soap.soap11.MustUnderstandBearing;
import org.opensaml.xml.schema.XSBooleanValue;

/**
 * SAML 2.0 ECP Request SOAP header.
 */
public interface Request extends SAMLObject, MustUnderstandBearing, ActorBearing {
    
    /** Element local name. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "Request";

    /** Default element name. */
    public static final QName DEFAULT_ELEMENT_NAME =
        new QName(SAMLConstants.SAML20ECP_NS, DEFAULT_ELEMENT_LOCAL_NAME, SAMLConstants.SAML20ECP_PREFIX);

    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "RequestType";

    /** QName of the XSI type. */
    public static final QName TYPE_NAME =
        new QName(SAMLConstants.SAML20ECP_NS, TYPE_LOCAL_NAME, SAMLConstants.SAML20ECP_PREFIX);

    /** ProviderName attribute name. */
    public static final String PROVIDER_NAME_ATTRIB_NAME = "ProviderName";

    /** IsPassive attribute name. */
    public static final String IS_PASSIVE_NAME_ATTRIB_NAME = "IsPassive";
    
    /**
     * Get the Issuer child elemet.
     * 
     * @return the Issuer child element
     */
    public Issuer getIssuer();
    
    /**
     * Set the Issuer child elemet.
     * 
     * @param newIssuer the new Issuer child element
     */
    public void setIssuer(Issuer newIssuer);
    
    /**
     * Get the IDPList child element.
     * 
     * @return the IDPList child element
     */
    public IDPList getIDPList();
    
    /**
     * Set the IDPList child element.
     * 
     * @param newIDPList the new IDPList child element
     */
    public void setIDPList(IDPList newIDPList);
    
    /**
     * Get the ProviderName attribute value.
     * 
     * @return the ProviderName attribute value
     */
    public String getProviderName();
    
    /**
     * Set the ProviderName attribute value.
     * 
     * @param newProviderName the new ProviderName attribute value
     */
    public void setProviderName(String newProviderName);
    
    /**
     * Get the IsPassive attribute value.
     * 
     * @return the IsPassive attribute value
     */
    public Boolean isPassive();
    
    /**
     * Get the IsPassive attribute value.
     * 
     * @return the IsPassive attribute value
     */
    public XSBooleanValue isPassiveXSBoolean();
    
    /**
     * Set the IsPassive attribute value.
     * 
     * @param newIsPassive the new IsPassive attribute value
     */
    public void setPassive(Boolean newIsPassive);
    
    /**
     * Set the IsPassive attribute value.
     * 
     * @param newIsPassive the new IsPassive attribute value
     */
    public void setPassive(XSBooleanValue newIsPassive);

}
