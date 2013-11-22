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

package org.opensaml.samlext.samlec;

import java.util.List;

import javax.xml.namespace.QName;

import org.opensaml.common.SAMLObject;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.ws.soap.soap11.ActorBearing;
import org.opensaml.ws.soap.soap11.MustUnderstandBearing;
import org.opensaml.xml.signature.KeyInfo;

/**
 * SAML-EC GSS-API SessionKey element.
 */
public interface SessionKey extends SAMLObject, MustUnderstandBearing, ActorBearing {

    /** Element local name. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "SessionKey";

    /** Default element name. */
    public static final QName DEFAULT_ELEMENT_NAME = new QName(SAMLConstants.SAMLEC_GSS_NS, DEFAULT_ELEMENT_LOCAL_NAME,
            SAMLConstants.SAMLEC_GSS_PREFIX);

    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "SessionKeyType";

    /** QName of the XSI type. */
    public static final QName TYPE_NAME = new QName(SAMLConstants.SAMLEC_GSS_NS, TYPE_LOCAL_NAME,
            SAMLConstants.SAMLEC_GSS_PREFIX);

    /** Algorithm attribute name. */
    public static final String ALGORITHM_ATTRIB_NAME = "Algorithm";
    
    /**
     * Get the session key derivation algorithm.
     * 
     * @return the algorithm used to derive the session key
     */
    public String getAlgorithm();

    /**
     * Set the session key derivation algorithm.
     * 
     * @param newAlgorithm the algorithm used to derive the session key
     */
    public void setAlgorithm(String newAlgorithm);
    
    /**
     * Get the session key encryption typed.
     * 
     * @return the encryption types of the session key
     */
    public List<EncType> getEncTypes();

    /**
     * Get the KeyInfo object that describes the session key.
     * 
     * @return the KeyInfo object that describes the session key
     */
    public KeyInfo getKeyInfo();

    /**
     * Set the KeyInfo object that describes the session key.
     * 
     * @param newKeyInfo the KeyInfo object that describes the session key
     */
    public void setKeyInfo(KeyInfo newKeyInfo);
}