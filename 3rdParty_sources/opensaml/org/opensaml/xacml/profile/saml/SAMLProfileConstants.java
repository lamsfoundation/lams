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

package org.opensaml.xacml.profile.saml;

import javax.xml.namespace.QName;

import org.opensaml.xacml.XACMLConstants;

/** Defines the constants for this XACML SAML2.0 profile. */
public class SAMLProfileConstants extends XACMLConstants {

    /** The prefix for the use of saml-xacml assertion. */
    public static final String SAML20XACMLASSERTION_PREFIX = "xacml-saml";

    /** The prefix for the use of saml20-xacml protocol. */
    public static final String SAML20XACMLPROTOCOL_PREFIX = "xacml-samlp";

    /** The namespaces for use of XACML 1.0 SAML 2.0 protocol. */
    public static final String SAML20XACML10P_NS = "urn:oasis:names:tc:xacml:1.0:profile:saml2.0:v2:schema:protocol";

    /** The namespaces for use of XACML 1.0 SAML 2.0 assertion. */
    public static final String SAML20XACML10_NS = "urn:oasis:names:tc:xacml:1.0:profile:saml2.0:v2:schema:assertion";

    /** The namespace for use of XACML 1.1 SAML 2.0 protocol. */
    public static final String SAML20XACML1_1P_NS = "urn:oasis:names:tc:xacml:1.1:profile:saml2.0:v2:schema:protocol";

    /** The namespace for use of XACML 1.1 SAML 2.0 assertion. */
    public static final String SAML20XACML1_1_NS = "urn:oasis:names:tc:xacml:1.1:profile:saml2.0:v2:schema:assertion";

    /** The namespaces for use of XACML 2.0 SAML 2.0 protocol. */
    public static final String SAML20XACML20P_NS = "urn:oasis:names:tc:xacml:2.0:profile:saml2.0:v2:schema:protocol";

    /** The namespaces for use of XACML 2.0 SAML 2.0 assertion. */
    public static final String SAML20XACML20_NS = "urn:oasis:names:tc:xacml:2.0:profile:saml2.0:v2:schema:assertion";

    /** The namespaces for use of XACML 3.0 SAML 2.0 protocol. */
    public static final String SAML20XACML30P_NS = "urn:oasis:names:tc:xacml:3.0:profile:saml2.0:v2:schema:protocol";

    /** The namespaces for use of XACML 3.0 SAML 2.0 assertion. */
    public static final String SAML20XACML30_NS = "urn:oasis:names:tc:xacml:3.0:profile:saml2.0:v2:schema:assertion";

    /** QName of the DataType attribute that must be on SAML attributes that meet the XACML attribute profile spec. */
    public static final QName SAML_DATATYPE_ATTRIB = new QName(
            "urn:oasis:names:tc:SAML:2.0:profiles:attribute:XACML", "DataType", "xacmlprof");

}