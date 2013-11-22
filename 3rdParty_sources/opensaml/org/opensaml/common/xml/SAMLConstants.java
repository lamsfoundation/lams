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

package org.opensaml.common.xml;

import org.opensaml.xml.util.XMLConstants;

/**
 * XML related constants used in the SAML specifications.
. */
public class SAMLConstants extends XMLConstants{
    //****************************
    // HTTP Constants
    //****************************
    /** HTTP Request Method - POST. */
    public static final String POST_METHOD = "POST";
    
    /** HTTP Method - GET. */
    public static final String GET_METHOD = "GET";
    
    //****************************
    // OpenSAML 2
    //****************************
    /** Directory, on the classpath, schemas are located in. */
    public static final String SCHEMA_DIR = "/schema/";
    
    //****************************
    //    Core XML
    //****************************
    /** XML core schema system Id. */
    public static final String XML_SCHEMA_LOCATION = SCHEMA_DIR + "xml.xsd";
    
    /**  XML Signature schema Id. */
    public static final String XMLSIG_SCHEMA_LOCATION = SCHEMA_DIR + "xmldsig-core-schema.xsd";
    
    /**  XML Signature 1.1 schema Id. */
    public static final String XMLSIG11_SCHEMA_LOCATION = SCHEMA_DIR + "xmldsig11-schema.xsd";
    
    /** XML Encryption schema Id. */
    public static final String XMLENC_SCHEMA_LOCATION = SCHEMA_DIR + "xenc-schema.xsd";
    
    /** XML Encryption 1.1 schema Id. */
    public static final String XMLENC11_SCHEMA_LOCATION = SCHEMA_DIR + "xenc11-schema.xsd";

    //****************************
    //    SOAP
    //****************************
    /**  SOAP 1.1 schema Id. */
    public static final String SOAP11ENV_SCHEMA_LOCATION = SCHEMA_DIR + SCHEMA_DIR + "soap-envelope.xsd";
    
    /**  SOAP 1.1 Envelope XML namespace. */
    public static final String SOAP11ENV_NS = "http://schemas.xmlsoap.org/soap/envelope/";
    
    /**  SOAP 1.1 Envelope QName prefix. */
    public static final String SOAP11ENV_PREFIX = "SOAP-ENV";
    
    /**  Liberty PAOS XML Namespace. */
    public static final String PAOS_NS = "urn:liberty:paos:2003-08";
    
    /**  Liberty PAOS QName prefix. */
    public static final String PAOS_PREFIX = "paos";

    //****************************
    //    SAML 1.X
    //****************************
    /** SAML 1.0 Assertion schema system Id. */
    public static final String SAML10_SCHEMA_LOCATION = SCHEMA_DIR + "cs-sstc-schema-assertion-01.xsd";
    
    /** SAML 1.1 Assertion schema system Id. */
    public static final String SAML11_SCHEMA_LOCATION = SCHEMA_DIR + "cs-sstc-schema-assertion-1.1.xsd";
    
    /** SAML 1.X XML namespace. */
    public static final String SAML1_NS = "urn:oasis:names:tc:SAML:1.0:assertion";
    
    /** SAML 1.0 Protocol schema system Id. */
    public static final String  SAML10P_SCHEMA_LOCATION = SCHEMA_DIR + "cs-sstc-schema-protocol-01.xsd";
    
    /** SAML 1.1 Protocol schema system Id. */
    public static final String SAML11P_SCHEMA_LOCATION = SCHEMA_DIR + "cs-sstc-schema-protocol-1.1.xsd";

    /** SAML 1.X protocol XML namespace. */
    public static final String SAML10P_NS = "urn:oasis:names:tc:SAML:1.0:protocol";
    
    /** SAML 1.1 protocol XML namespace, used only in SAML 2 metadata protocolSupportEnumeration. */
    public static final String SAML11P_NS = "urn:oasis:names:tc:SAML:1.1:protocol";
    
    /** SAML 1.X Protocol QName prefix. */
    public static final String SAML1P_PREFIX = "saml1p";

    /** SAML 1.X Assertion QName prefix. */
    public static final String SAML1_PREFIX = "saml1";
    
    /** SAML 1 Metadata extension XML namespace. */
    public static final String SAML1MD_NS = "urn:oasis:names:tc:SAML:profiles:v1metadata";
    
    /** SAML 1 Metadata extension schema system Id. */
    public static final String SAML1MD_SCHEMA_LOCATION = SCHEMA_DIR + "sstc-saml1x-metadata.xsd";
    
    /** SAML 1 Metadata extension namespace prefix. */
    public static final String SAML1MD_PREFIX = "saml1md";
    
    /** URI for SAML 1 Artifact binding. */
    public static final String SAML1_ARTIFACT_BINDING_URI = "urn:oasis:names:tc:SAML:1.0:profiles:artifact-01";
    
    /** URI for SAML 1 POST binding. */
    public static final String SAML1_POST_BINDING_URI = "urn:oasis:names:tc:SAML:1.0:profiles:browser-post";
    
    /** URI for SAML 1 SOAP 1.1 binding. */
    public static final String SAML1_SOAP11_BINDING_URI = "urn:oasis:names:tc:SAML:1.0:bindings:SOAP-binding";
    
    //****************************
    //    SAML 2.0
    //****************************
    /** SAML 2.0 Assertion schema Id. */
    public static final String SAML20_SCHEMA_LOCATION = SCHEMA_DIR + "saml-schema-assertion-2.0.xsd";
    
    /** SAML 2.0 Assertion XML Namespace. */
    public static final String SAML20_NS = "urn:oasis:names:tc:SAML:2.0:assertion";
    
    /** SAML 2.0 Assertion QName prefix. */
    public static final String SAML20_PREFIX ="saml2";
    
    /** SAML 2.0 Protocol schema Id. */
    public static final String SAML20P_SCHEMA_LOCATION = SCHEMA_DIR + "saml-schema-protocol-2.0.xsd";
    
    /** SAML 2.0 Protocol XML Namespace. */
    public static final String SAML20P_NS = "urn:oasis:names:tc:SAML:2.0:protocol";
    
    /** SAML 2.0 Protocol QName prefix. */
    public static final String SAML20P_PREFIX ="saml2p";
    
    /** SAML 2.0 Protocol Third-party extension schema Id. */
    public static final String SAML20PTHRPTY_SCHEMA_LOCATION = SCHEMA_DIR + "sstc-saml-protocol-ext-thirdparty.xsd";
    
    /** SAML 2.0 Protocol Third-party extension XML Namespace. */
    public static final String SAML20PTHRPTY_NS = "urn:oasis:names:tc:SAML:protocol:ext:third-party";
    
    /** SAML 2.0 Protocol Third-party extension QName prefix. */
    public static final String SAML20PTHRPTY_PREFIX ="thrpty";

    /** SAML 2.0 Protocol Async Logout extension schema Id. */
    public static final String SAML20PASLO_SCHEMA_LOCATION = SCHEMA_DIR + "saml-async-slo-v1.0.xsd";
    
    /** SAML 2.0 Protocol Async Logout extension XML Namespace. */
    public static final String SAML20PASLO_NS = "urn:oasis:names:tc:SAML:2.0:protocol:ext:async-slo";
    
    /** SAML 2.0 Protocol Async Logout extension QName prefix. */
    public static final String SAML20PASLO_PREFIX ="aslo";
    
    /** SAML 2.0 Metadata schema Id. */
    public static final String SAML20MD_SCHEMA_LOCATION = SCHEMA_DIR + "saml-schema-metadata-2.0.xsd";
    
    /** SAML 2.0 Metadata XML Namespace. */
    public static final String SAML20MD_NS ="urn:oasis:names:tc:SAML:2.0:metadata";
    
    /** SAML 2.0 Standalone Query Metadata extension XML namespace. */
    public static final String SAML20MDQUERY_NS = "urn:oasis:names:tc:SAML:metadata:ext:query";
    
    /** SAML 2.0 Standalone Query Metadata extension schema system Id. */
    public static final String SAML20MDQUERY_SCHEMA_LOCATION = SCHEMA_DIR + "sstc-saml-metadata-ext-query.xsd";
    
    /** SAML 2.0 Standalone Query Metadata extension prefix. */
    public static final String SAML20MDQUERY_PREFIX = "query";
    
    /** SAML 2.0 Metadata QName prefix. */
    public static final String SAML20MD_PREFIX = "md";
    
    /** SAML 2.0 Authentication Context schema Id. */
    public static final String SAML20AC_SCHEMA_LOCATION = SCHEMA_DIR + "saml-schema-authn-context-2.0.xsd";
    
    /** SAML 2.0 Authentication Context XML Namespace. */
    public static final String SAML20AC_NS ="urn:oasis:names:tc:SAML:2.0:ac";
    
    /** SAML 2.0 Authentication Context QName prefix. */
    public static final String SAML20AC_PREFIX = "ac";
    
    /** SAML 2.0 Enhanced Client/Proxy SSO Profile schema Id. */
    public static final String SAML20ECP_SCHEMA_LOCATION = SCHEMA_DIR + "saml-schema-ecp-2.0.xsd";
    
    /** SAML 2.0 Enhanced Client/Proxy SSO Profile XML Namespace. */
    public static final String SAML20ECP_NS = "urn:oasis:names:tc:SAML:2.0:profiles:SSO:ecp";
    
    /** SAML 2.0 Enhanced Client/Proxy SSO Profile QName prefix. */
    public static final String SAML20ECP_PREFIX = "ecp";
    
    /** SAML 2.0 Condition for Delegation Restriction schema Id. */
    public static final String SAML20DEL_SCHEMA_LOCATION = SCHEMA_DIR + "sstc-saml-delegation.xsd";
    
    /** SAML 2.0 Condition for Delegation Restriction XML Namespace. */
    public static final String SAML20DEL_NS = "urn:oasis:names:tc:SAML:2.0:conditions:delegation";
    
    /** SAML 2.0 Condition for Delegation Restriction QName prefix. */
    public static final String SAML20DEL_PREFIX = "del";
    
    /** SAML V2.0 Metadata Extension for Entity Attributes schema ID . */
    public static final String SAML20MDATTR_SCHEMA_LOCATION = SCHEMA_DIR + "sstc-metadata-attr.xsd";
    
    /** SAML V2.0 Metadata Extension for Entity Attributes XML Namespace. */
    public static final String SAML20MDATTR_NS = "urn:oasis:names:tc:SAML:metadata:attribute";
    
    /** SAML V2.0 Metadata Extension for Entity Attributes QName prefix. */
    public static final String SAML20MDATTR_PREFIX = "mdattr";

    /** SAML 2.0 Channel Binding Extensions schema Id. */
    public static final String SAML20CB_SCHEMA_LOCATION = SCHEMA_DIR + "sstc-saml-channel-binding-ext-v1.0.xsd";
    
    /** SAML 2.0 Channel Binding Extensions XML Namespace. */
    public static final String SAML20CB_NS = "urn:oasis:names:tc:SAML:protocol:ext:channel-binding";
    
    /** SAML 2.0 Channel Binding Extensions QName prefix. */
    public static final String SAML20CB_PREFIX = "cb";
    
    /** SAML 2.0 DCE PAC Attribute Profile schema Id. */
    public static final String SAML20DCE_SCHEMA_LOCATION = SCHEMA_DIR + "saml-schema-dce-2.0.xsd";
    
    /** SAML 2.0 DCE PAC Attribute Profile XML Namespace. */
    public static final String SAML20DCE_NS = "urn:oasis:names:tc:SAML:2.0:profiles:attribute:DCE";
    
    /** SAML 2.0 DCE PAC Attribute Profile QName prefix. */
    public static final String SAML20DCE_PREFIX = "DCE";
    
    /** SAML 2.0 X.500 Attribute Profile schema Id. */
    public static final String SAML20X500_SCHEMA_LOCATION = SCHEMA_DIR + "saml-schema-x500-2.0.xsd";
    
    /** SAML 2.0 X.500 Attribute Profile XML Namespace. */
    public static final String SAML20X500_NS = "urn:oasis:names:tc:SAML:2.0:profiles:attribute:X500";
    
    /** SAML 2.0 X.500 Attribute Profile QName prefix. */
    public static final String SAML20X500_PREFIX = "x500";
    
    /** SAML 2.0 XACML Attribute Profile schema Id. */
    public static final String SAML20XACML_SCHEMA_LOCATION = SCHEMA_DIR + "saml-schema-xacml-2.0.xsd";
    
    /** SAML 2.0 XACML Attribute Profile XML Namespace. */
    public static final String SAML20XACML_NS = "urn:oasis:names:tc:SAML:2.0:profiles:attribute:XACML";
    
    /** SAML 2.0 XACML Attribute Profile QName prefix. */
    public static final String SAML20XACML_PREFIX = "xacmlprof";

    /** SAML 2.0 Enhanced Client GSS-API schema Id. */
    public static final String SAMLEC_GSS_SCHEMA_LOCATION = SCHEMA_DIR + "ietf-kitten-sasl-saml-ec.xsd";
    
    /** SAML 2.0 Enhanced Client GSS-API XML Namespace. */
    public static final String SAMLEC_GSS_NS = "urn:ietf:params:xml:ns:samlec";
    
    /** SAML 2.0 Enhanced Client GSS-API QName prefix. */
    public static final String SAMLEC_GSS_PREFIX = "samlec";
    
    /** URI for SAML 2 Artifact binding. */
    public static final String SAML2_ARTIFACT_BINDING_URI = "urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Artifact";
    
    /** URI for SAML 2 POST binding. */
    public static final String SAML2_POST_BINDING_URI = "urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST";
    
    /** URI for SAML 2 POST-SimpleSign binding. */
    public static final String SAML2_POST_SIMPLE_SIGN_BINDING_URI = "urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST-SimpleSign";
    
    /** URI for SAML 2 HTTP redirect binding. */
    public static final String SAML2_REDIRECT_BINDING_URI = "urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Redirect";
    
    /** URI for SAML 2 SOAP binding. */
    public static final String SAML2_SOAP11_BINDING_URI = "urn:oasis:names:tc:SAML:2.0:bindings:SOAP";
    
    /** URI for SAML 2 PAOS binding. */
    public static final String SAML2_PAOS_BINDING_URI = "urn:oasis:names:tc:SAML:2.0:bindings:PAOS";
}
