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

package org.opensaml.xml.util;

import javax.xml.namespace.QName;

/**
 * XML related constants.
 */
public class XMLConstants {

    // ****************************
    // XML Tooling
    // ****************************
    /** Configuration namespace. */
    public static final String XMLTOOLING_CONFIG_NS = "http://www.opensaml.org/xmltooling-config";

    /** Configuration namespace prefix. */
    public static final String XMLTOOLING_CONFIG_PREFIX = "xt";

    /** Name of the object provider used for objects that don't have a registered object provider. */
    public static final String XMLTOOLING_DEFAULT_OBJECT_PROVIDER = "DEFAULT";

    /** Location, on the classpath, of the XMLTooling configuration schema. */
    public static final String XMLTOOLING_SCHEMA_LOCATION = "/schema/xmltooling-config.xsd";

    // ****************************
    // Core XML
    // ****************************
    /** XML core namespace. */
    public static final String XML_NS = "http://www.w3.org/XML/1998/namespace";

    /** XML core prefix for xml attributes. */
    public static final String XML_PREFIX = "xml";

    /** XML namespace for xmlns attributes. */
    public static final String XMLNS_NS = "http://www.w3.org/2000/xmlns/";

    /** XML namespace prefix for xmlns attributes. */
    public static final String XMLNS_PREFIX = "xmlns";

    /** XML Schema namespace. */
    public static final String XSD_NS = "http://www.w3.org/2001/XMLSchema";

    /** XML Schema QName prefix. */
    public static final String XSD_PREFIX = "xs";

    /** XML Schema Instance namespace. */
    public static final String XSI_NS = "http://www.w3.org/2001/XMLSchema-instance";

    /** XML Schema Instance QName prefix. */
    public static final String XSI_PREFIX = "xsi";

    /** XML Signature 1.0 namespace. */
    public static final String XMLSIG_NS = "http://www.w3.org/2000/09/xmldsig#";

    /** XML Signature 1.0 QName prefix. */
    public static final String XMLSIG_PREFIX = "ds";

    /** XML Signature 1.1 namespace. */
    public static final String XMLSIG11_NS = "http://www.w3.org/2009/xmldsig11#";

    /** XML Signature 1.1 QName prefix. */
    public static final String XMLSIG11_PREFIX = "ds11";
    
    /** XML Encryption 1.0 namespace. */
    public static final String XMLENC_NS = "http://www.w3.org/2001/04/xmlenc#";

    /** XML Encryption 1.0 QName prefix. */
    public static final String XMLENC_PREFIX = "xenc";
    
    /** XML Encryption 1.1 namespace. */
    public static final String XMLENC11_NS = "http://www.w3.org/2009/xmlenc11#";

    /** XML Encryption 1.1 QName prefix. */
    public static final String XMLENC11_PREFIX = "xenc11";
    
    /** XML Schema instance <code>xsi:type</code> attribute QName. */
    public static final QName XSI_TYPE_ATTRIB_NAME = 
        new QName(XSI_NS, "type", XSI_PREFIX);

    /** XML Schema instance <code>xsi:type</code> attribute QName. */
    public static final QName XSI_SCHEMA_LOCATION_ATTRIB_NAME = 
        new QName(XSI_NS, "schemaLocation", XSI_PREFIX);

    /** XML Schema instance <code>xsi:type</code> attribute QName. */
    public static final QName XSI_NO_NAMESPACE_SCHEMA_LOCATION_ATTRIB_NAME = 
        new QName(XSI_NS, "noNamespaceSchemaLocation", XSI_PREFIX);
    
    /** XML Schema instance <code>xsi:type</code> attribute QName. */
    public static final QName XSI_NIL_ATTRIB_NAME = 
        new QName(XSI_NS, "nil", XSI_PREFIX);

    /** Constructor. */
    protected XMLConstants() {
        
    }
}