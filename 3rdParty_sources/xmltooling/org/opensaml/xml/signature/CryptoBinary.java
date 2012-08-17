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

import java.math.BigInteger;

import javax.xml.namespace.QName;

import org.opensaml.xml.schema.XSBase64Binary;
import org.opensaml.xml.util.XMLConstants;

/**
 * XMLObject representing XML Digital Signature, version 20020212, CryptoBinary simple type.
 */
public interface CryptoBinary extends XSBase64Binary {
    
    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "CryptoBinary"; 
        
    /** QName of the XSI type. */
    public static final QName TYPE_NAME = 
        new QName(XMLConstants.XMLSIG_NS, TYPE_LOCAL_NAME, XMLConstants.XMLSIG_PREFIX);
    
    /**
     * Convenience method to get the value of the element as a BigInteger type.
     * 
     * @return the BigInteger representation of the element's content
     */
    public BigInteger getValueBigInt(); 
    
    /**
     * Convenience method to set the value of the element as a BigInteger type.
     * 
     * @param bigInt the new BigInteger representation of the element's content
     */
    public void setValueBigInt(BigInteger bigInt); 
    
}
