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

package org.opensaml.xacml;

/** Defines the constants for the XACML providers. */
public class XACMLConstants {

    /** The prefix for the use of xacml policy. */
    public static final String XACML_PREFIX = "xacml";

    /** The prefix for the use of xacml context. */
    public static final String XACMLCONTEXT_PREFIX = "xacml-context";

    /** The namespaces for use of XACML 1.0 context. */
    public static final String XACML10CTX_NS = "urn:oasis:names:tc:xacml:1.0:context";

    /** The namespaces for use of XACML 1.0 policy. */
    public static final String XACML10_NS = "urn:oasis:names:tc:xacml:1.0:policy";

    /** The namespaces for use of XACML 2.0 context. */
    public static final String XACML20CTX_NS = "urn:oasis:names:tc:xacml:2.0:context:schema:os";

    /** The namespaces for use of XACML 2.0 policy. */
    public static final String XACML20_NS = "urn:oasis:names:tc:xacml:2.0:policy:schema:os";

    /** The namespaces for use of XACML 3.0. */
    public static final String XACML30_NS = "urn:oasis:names:tc:xacml:3.0:schema:os";

    /** X.500 Name datatype URI. */
    public static final String X500_NAME_DATATYPE_URI = "urn:oasis:names:tc:xacml:1.0:data-type:x500Name";

    /** RFC822 Name datatype URI. */
    public static final String RFC822_NAME_DATATYPE_URI = "urn:oasis:names:tc:xacml:1.0:data-type:rfc822Name";

    /** IP address datatype URI. */
    public static final String IP_ADDRESS_DATATYPE_URI = "urn:oasis:names:tc:xacml:1.0:data-type:ipAddress";

    /** DNS Name datatype URI. */
    public static final String DNS_NAME_DATATYPE_URI = "urn:oasis:names:tc:xacml:1.0:data-type:dnsName";
}