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

/** XMLObject representing XML Digital Signature, version 20020212, DSAKeyValue element. */
public interface DSAKeyValue extends ValidatingXMLObject {

    /** Element local name. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "DSAKeyValue";

    /** Default element name. */
    public static final QName DEFAULT_ELEMENT_NAME = new QName(XMLConstants.XMLSIG_NS, DEFAULT_ELEMENT_LOCAL_NAME,
            XMLConstants.XMLSIG_PREFIX);

    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "DSAKeyValueType";

    /** QName of the XSI type. */
    public static final QName TYPE_NAME = new QName(XMLConstants.XMLSIG_NS, TYPE_LOCAL_NAME, XMLConstants.XMLSIG_PREFIX);

    /**
     * Get the P child element.
     * 
     * @return the P child element
     */
    public P getP();

    /**
     * Set the P child element.
     * 
     * @param newP the new P child element
     */
    public void setP(P newP);

    /**
     * Get the Q child element.
     * 
     * @return the Q child element
     */
    public Q getQ();

    /**
     * Set the Q child element.
     * 
     * @param newQ the new Q child element
     */
    public void setQ(Q newQ);

    /**
     * Get the G child element.
     * 
     * @return the G child element
     */
    public G getG();

    /**
     * Set the G child element.
     * 
     * @param newG the new G child element
     */
    public void setG(G newG);

    /**
     * Get the Y child element.
     * 
     * @return the Y child element
     */
    public Y getY();

    /**
     * Set the Y child element.
     * 
     * @param newY the new Y child element
     */
    public void setY(Y newY);

    /**
     * Get the J child element.
     * 
     * @return the J child element
     */
    public J getJ();

    /**
     * Set the J child element.
     * 
     * @param newJ the new J child element
     */
    public void setJ(J newJ);

    /**
     * Get the Seed element.
     * 
     * @return the Seed element
     */
    public Seed getSeed();

    /**
     * Set the Seed element.
     * 
     * @param newSeed new Seed element
     */
    public void setSeed(Seed newSeed);

    /**
     * Get the PgenCounter element.
     * 
     * @return the PgenCounter element
     */
    public PgenCounter getPgenCounter();

    /**
     * Set the PgenCounter element.
     * 
     * @param newPgenCounter new PgenCounter element
     */
    public void setPgenCounter(PgenCounter newPgenCounter);

}