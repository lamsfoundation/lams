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
import org.opensaml.ws.soap.soap11.ActorBearing;
import org.opensaml.ws.soap.soap11.MustUnderstandBearing;

/**
 * SAML 2.0 ECP Response SOAP header.
 */
public interface Response extends SAMLObject, MustUnderstandBearing, ActorBearing {
    
    /** Element local name. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "Response";

    /** Default element name. */
    public static final QName DEFAULT_ELEMENT_NAME =
        new QName(SAMLConstants.SAML20ECP_NS, DEFAULT_ELEMENT_LOCAL_NAME, SAMLConstants.SAML20ECP_PREFIX);

    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "ResponseType";

    /** QName of the XSI type. */
    public static final QName TYPE_NAME =
        new QName(SAMLConstants.SAML20ECP_NS, TYPE_LOCAL_NAME, SAMLConstants.SAML20ECP_PREFIX);

    /** ProviderName attribute name. */
    public static final String ASSERTION_CONSUMER_SERVICE_URL_ATTRIB_NAME = "AssertionConsumerServiceURL";
    
    /**
     * Get the AssertionConsumerServiceURL attribute value.
     * 
     * @return the AssertionConsumerServiceURL attribute value
     */
    public String getAssertionConsumerServiceURL();

    /**
     * Get the AssertionConsumerServiceURL attribute value.
     * 
     * @param newAssertionConsumerServiceURL the new AssertionConsumerServiceURL attribute value
     */
    public void setAssertionConsumerServiceURL(String newAssertionConsumerServiceURL);

}
