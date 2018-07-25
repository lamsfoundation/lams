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

package org.opensaml.saml1.core;

import javax.xml.namespace.QName;

import org.opensaml.common.SAMLObject;
import org.opensaml.common.xml.SAMLConstants;

/**
 * Interface to define how a SubjectLocality element behaves.
 */
public interface SubjectLocality extends SAMLObject {

    /** Element name, no namespace. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "SubjectLocality";

    /** Default element name. */
    public static final QName DEFAULT_ELEMENT_NAME = new QName(SAMLConstants.SAML1_NS, DEFAULT_ELEMENT_LOCAL_NAME,
            SAMLConstants.SAML1_PREFIX);

    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "SubjectLocalityType";

    /** QName of the XSI type. */
    public static final QName TYPE_NAME = new QName(SAMLConstants.SAML1_NS, TYPE_LOCAL_NAME, SAMLConstants.SAML1_PREFIX);

    /** Name for the IPAddress attribute. */
    public static final String IPADDRESS_ATTRIB_NAME = "IPAddress";

    /** Name for the DNSAddress attribute. */
    public static final String DNSADDRESS_ATTRIB_NAME = "DNSAddress";

    /**
     * Gets the IP address of the locality.
     * 
     * @return IP address of the locality
     */
    public String getIPAddress();

    /**
     * Sets the IP address of the locality.
     * 
     * @param address IP address of the locality
     */
    public void setIPAddress(String address);

    /**
     * Gets the DNS name of the locality.
     * 
     * @return DNS name of the locality
     */
    public String getDNSAddress();

    /**
     * Sets the DNS name of the locality.
     * 
     * @param address DNS name of the locality
     */
    public void setDNSAddress(String address);
}
