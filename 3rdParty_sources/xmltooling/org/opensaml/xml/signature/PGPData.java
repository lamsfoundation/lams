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

import org.opensaml.xml.ElementExtensibleXMLObject;
import org.opensaml.xml.util.XMLConstants;
import org.opensaml.xml.validation.ValidatingXMLObject;

/**
 * XMLObject representing XML Digital Signature, version 20020212, PGPData element.
 */
public interface PGPData extends ValidatingXMLObject, ElementExtensibleXMLObject {
    
    /** Element local name */
    public final static String DEFAULT_ELEMENT_LOCAL_NAME = "PGPData";
    
    /** Default element name */
    public final static QName DEFAULT_ELEMENT_NAME = new QName(XMLConstants.XMLSIG_NS, DEFAULT_ELEMENT_LOCAL_NAME, XMLConstants.XMLSIG_PREFIX);
    
    /** Local name of the XSI type */
    public final static String TYPE_LOCAL_NAME = "PGPDataType"; 
        
    /** QName of the XSI type */
    public final static QName TYPE_NAME = new QName(XMLConstants.XMLSIG_NS, TYPE_LOCAL_NAME, XMLConstants.XMLSIG_PREFIX);
    
    /**
     * Get PGPKeyID child element
     * 
     * @return the PGPKeyID child element
     */
    public PGPKeyID getPGPKeyID();
    
    /**
     * Set PGPKeyID child element
     * 
     * @param newPGPKeyID the new PGPKeyID
     */
    public void setPGPKeyID(PGPKeyID newPGPKeyID);
    
    /**
     * Get PGPKeyPacket child element
     * 
     * @return the PGPKeyPacket child element
     */
    public PGPKeyPacket getPGPKeyPacket();
    
    /**
     * Set PGPKeyPacket child element
     * 
     * @param newPGPKeyPacket the new PGPKeyPacket
     */
    public void setPGPKeyPacket(PGPKeyPacket newPGPKeyPacket);
    
}
