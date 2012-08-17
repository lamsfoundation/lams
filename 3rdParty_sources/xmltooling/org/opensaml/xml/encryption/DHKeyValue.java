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

package org.opensaml.xml.encryption;

import javax.xml.namespace.QName;

import org.opensaml.xml.util.XMLConstants;
import org.opensaml.xml.validation.ValidatingXMLObject;

/**
 * XMLObject representing XML Encryption, version 20021210, DHKeyValue element.
 */
public interface DHKeyValue extends ValidatingXMLObject {

    /** Element local name. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "DHKeyValue";

    /** Default element name. */
    public final QName DEFAULT_ELEMENT_NAME = new QName(XMLConstants.XMLENC_NS, DEFAULT_ELEMENT_LOCAL_NAME,
            XMLConstants.XMLENC_PREFIX);

    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "DHKeyValueType";

    /** QName of the XSI type. */
    public static final QName TYPE_NAME = new QName(XMLConstants.XMLENC_NS, TYPE_LOCAL_NAME, 
            XMLConstants.XMLENC_PREFIX);

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
     * Get the Generator child element.
     * 
     * @return the Generator child element
     */
    public Generator getGenerator();

    /**
     * Set the G child element.
     * 
     * @param newGenerator the new G child element
     */
    public void setGenerator(Generator newGenerator);

    /**
     * Get the Public element.
     * 
     * @return the Public element
     */
    public Public getPublic();

    /**
     * Set the Public element.
     * 
     * @param newPublic the new Public child element
     */
    public void setPublic(Public newPublic);

    /**
     * Get the seed element.
     * 
     * @return the seed element
     */
    public Seed getSeed();

    /**
     * Set the seed element.
     * 
     * @param newSeed new seed element
     */
    public void setSeed(Seed newSeed);

    /**
     * Get the pgenCounter element.
     * 
     * @return the pgenCounter element
     */
    public PgenCounter getPgenCounter();

    /**
     * Set the pgenCounter element.
     * 
     * @param newPgenCounter new pgenCounter element
     */
    public void setPgenCounter(PgenCounter newPgenCounter);

}
