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

import java.util.List;

import javax.xml.namespace.QName;

import org.opensaml.xml.XMLObject;
import org.opensaml.xml.encryption.AgreementMethod;
import org.opensaml.xml.encryption.EncryptedKey;
import org.opensaml.xml.util.XMLConstants;
import org.opensaml.xml.validation.ValidatingXMLObject;

/** XMLObject representing XML Digital Signature, version 20020212, KeyInfoType complex type. */
public interface KeyInfoType extends ValidatingXMLObject {

    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "KeyInfoType";

    /** QName of the XSI type. */
    public static final QName TYPE_NAME = new QName(XMLConstants.XMLSIG_NS, TYPE_LOCAL_NAME, XMLConstants.XMLSIG_PREFIX);

    /** Id attribute name. */
    public static final String ID_ATTRIB_NAME = "Id";

    /**
     * Get the Id attribute value.
     * 
     * @return the Id attribute value
     */
    public String getID();

    /**
     * Set the Id attribute value.
     * 
     * @param newID the new Id attribute value
     */
    public void setID(String newID);

    /**
     * Get the list of all XMLObject children.
     * 
     * @return the list of XMLObject children
     */
    public List<XMLObject> getXMLObjects();

    /**
     * Get the list of XMLObject children whose type or element QName matches the specified QName.
     * 
     * @param typeOrName the QName of the desired elements
     * 
     * @return the matching list of XMLObject children
     */
    public List<XMLObject> getXMLObjects(QName typeOrName);

    /**
     * Get the list of KeyName child elements.
     * 
     * @return the list of KeyName child elements
     */
    public List<KeyName> getKeyNames();

    /**
     * Get the list of KeyValue child elements.
     * 
     * @return the list of KeyValue child elements
     */
    public List<KeyValue> getKeyValues();

    /**
     * Get the list of RetrievalMethod child elements.
     * 
     * @return the list of RetrievalMethod child elements
     */
    public List<RetrievalMethod> getRetrievalMethods();

    /**
     * Get the list of X509Data child elements.
     * 
     * @return the list of X509Data child elements
     */
    public List<X509Data> getX509Datas();

    /**
     * Get the list of PGPData child elements.
     * 
     * @return the list of PGPData child elements
     */
    public List<PGPData> getPGPDatas();

    /**
     * Get the list of SPKIData child elements.
     * 
     * @return the list of SPKIData child elements
     */
    public List<SPKIData> getSPKIDatas();

    /**
     * Get the list of MgmtData child elements.
     * 
     * @return the list of MgmtData child elements
     */
    public List<MgmtData> getMgmtDatas();

    /**
     * Get the list of AgreementMethod child elements.
     * 
     * Note: AgreementMethod is actually defined in the XML Encryption schema, and is not explicitly defined in the
     * KeyInfoType content model, but for convenience this named getter method is exposed.
     * 
     * @return the list of AgreementMethod child elements
     */
    public List<AgreementMethod> getAgreementMethods();

    /**
     * Get the list of EncryptedKey child elements
     * 
     * Note: EncryptedKey is actually defined in the XML Encryption schema, and is not explicitly defined in the
     * KeyInfoType content model, but for convenience this named getter method is exposed.
     * 
     * @return the list of EncryptedKey child elements
     */
    public List<EncryptedKey> getEncryptedKeys();

}
