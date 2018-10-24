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

package org.opensaml.xml.signature;

import javax.xml.namespace.QName;

import org.opensaml.xml.util.XMLConstants;
import org.opensaml.xml.validation.ValidatingXMLObject;

/** XMLObject representing XML Digital Signature, version 20020212, RSAKeyValue element. */
public interface RSAKeyValue extends ValidatingXMLObject {

    /** Element local name. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "RSAKeyValue";

    /** Default element name. */
    public static final QName DEFAULT_ELEMENT_NAME = new QName(XMLConstants.XMLSIG_NS, DEFAULT_ELEMENT_LOCAL_NAME,
            XMLConstants.XMLSIG_PREFIX);

    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "RSAKeyValueType";

    /** QName of the XSI type. */
    public static final QName TYPE_NAME = new QName(XMLConstants.XMLSIG_NS, TYPE_LOCAL_NAME, XMLConstants.XMLSIG_PREFIX);

    /**
     * Get the Modulus child element.
     * 
     * @return the Modulus child element
     */
    public Modulus getModulus();

    /**
     * Set the Modulus child element.
     * 
     * @param newModulus the new Modulus child element
     */
    public void setModulus(Modulus newModulus);

    /**
     * Get the Exponent child element.
     * 
     * @return the Exponent child element
     */
    public Exponent getExponent();

    /**
     * Set the Exponent child element.
     * 
     * @param newExponent the new Exponent child element
     */
    public void setExponent(Exponent newExponent);

}
