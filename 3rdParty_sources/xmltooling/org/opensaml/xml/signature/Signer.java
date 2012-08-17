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

import org.apache.xml.security.Init;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.signature.XMLSignature;
import org.opensaml.xml.security.SecurityHelper;
import org.opensaml.xml.signature.impl.SignatureImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for creating the digital signatures for the given signable XMLObjects.
 * 
 * This must be done as a seperate step because in order to support the following cases:
 * <ul>
 * <li>Multiple signable objects appear in the DOM tree, in which case the order that the objects should be signed in
 * is not known (e.g. object 1 could appear first in the tree, but contain a reference to signable object 2)</li>
 * <li>The DOM tree resulting from marshalling of the XMLObject tree is grafted onto another DOM tree which may cause
 * element ID conflicts that would invalidate the signature</li>
 * </ul>
 */
public class Signer {

    /** Constructor. */
    protected Signer() {

    }

    /**
     * Signs the given XMLObject in the order provided.
     * 
     * @param xmlObjects an orderded list of XMLObject to be signed
     * @throws SignatureException  thrown if there is an error computing the signature
     */
    public static void signObjects(List<Signature> xmlObjects) throws SignatureException {
        for (Signature xmlObject : xmlObjects) {
            signObject(xmlObject);
        }
    }

    /**
     * Signs a single XMLObject.
     * 
     * @param signature the signature to computer the signature on
     * @throws SignatureException thrown if there is an error computing the signature
     */
    public static void signObject(Signature signature) throws SignatureException {
        Logger log = getLogger();
        try {
            XMLSignature xmlSignature = ((SignatureImpl) signature).getXMLSignature();

            if (xmlSignature == null) {
                log.error("Unable to compute signature, Signature XMLObject does not have the XMLSignature "
                        + "created during marshalling.");
                throw new SignatureException("XMLObject does not have an XMLSignature instance, unable to compute signature");
            }
            log.debug("Computing signature over XMLSignature object");
            xmlSignature.sign(SecurityHelper.extractSigningKey(signature.getSigningCredential()));
        } catch (XMLSecurityException e) {
            log.error("An error occured computing the digital signature", e);
            throw new SignatureException("Signature computation error", e);
        }
    }
    
    /**
     * Get an SLF4J Logger.
     * 
     * @return a Logger instance
     */
    private static Logger getLogger() {
        return LoggerFactory.getLogger(Signer.class);
    }

    /*
     * Initialize the Apache XML security library if it hasn't been already
     */
    static {
        Logger log = getLogger();
        if (!Init.isInitialized()) {
            log.debug("Initializing XML security library");
            Init.init();
        }
    }
}