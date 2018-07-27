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

import org.opensaml.xml.security.CriteriaSet;
import org.opensaml.xml.security.SecurityException;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.security.keyinfo.KeyInfoCredentialResolver;
import org.opensaml.xml.security.trust.TrustEngine;

/**
 * Evaluates the trustworthiness and validity of XML or raw Signatures against implementation-specific requirements.
 */
public interface SignatureTrustEngine extends TrustEngine<Signature> {
    
    /**
     * Get the KeyInfoCredentialResolver instance used to resolve (advisory) signing credential information
     * from KeyInfo elements contained within a Signature element.
     * 
     * Note that credential(s) obtained via this resolver are not themselves trusted.  They must be evaluated
     * against the trusted credential information obtained from the trusted credential resolver.
     * 
     * @return a KeyInfoCredentialResolver instance
     */
    public KeyInfoCredentialResolver getKeyInfoResolver();

    /**
     * Determines whether a raw signature over specified content is valid and signed by a trusted credential.
     * 
     * <p>A candidate verification credential may optionally be supplied.  If one is supplied and is
     * determined to successfully verify the signature, an attempt will be made to establish
     * trust on this basis.</p>
     * 
     * <p>If a candidate credential is not supplied, or it does not successfully verify the signature,
     * some implementations may be able to resolve candidate verification credential(s) in an
     * implementation-specific manner based on the trusted criteria supplied, and then attempt 
     * to verify the signature and establish trust on this basis.</p>
     * 
     * @param signature the signature value
     * @param content the content that was signed
     * @param algorithmURI the signature algorithm URI which was used to sign the content
     * @param trustBasisCriteria criteria used to describe and/or resolve the information
     *          which serves as the basis for trust evaluation
     * @param candidateCredential the untrusted candidate credential containing the validation key
     *          for the signature (optional)
     * 
     * @return true if the signature was valid for the provided content and was signed by a key
     *          contained within a credential established as trusted based on the supplied criteria,
     *          otherwise false
     * 
     * @throws SecurityException thrown if there is a problem attempting to verify the signature such as the signature
     *             algorithim not being supported
     */
    public boolean validate(byte[] signature, byte[] content, String algorithmURI, CriteriaSet trustBasisCriteria,
            Credential candidateCredential) throws SecurityException;
}