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

package org.opensaml.xml.security.x509;

import org.opensaml.xml.security.Criteria;
import org.opensaml.xml.util.DatatypeHelper;

/**
 * An implementation of {@link Criteria} which specifies criteria based on
 * the digest of an X.509 certificate.
 */
public final class X509DigestCriteria implements Criteria {
    
    /** Digest algorithm. */
    private String algorithm;
    
    /** X.509 certificate digest. */
    private byte[] x509digest;
    
    /**
     * Constructor.
     *
     * @param alg algorithm of digest computation
     * @param digest certificate digest
     */
    public X509DigestCriteria(String alg, byte[] digest) {
        setAlgorithm(alg);
        setDigest(digest);
    }

    /**
     * Get the digest algorithm.
     * 
     * @return the digest algorithm
     */
    public String getAlgorithm() {
        return algorithm;
    }

    /**
     * Set the digest algorithm.
     * 
     * @param alg the digest algorithm to set
     */
    public void setAlgorithm(String alg) {
        if (DatatypeHelper.isEmpty(alg)) {
            throw new IllegalArgumentException("Digest algorithm criteria value cannot be null or empty");
        }
        algorithm = DatatypeHelper.safeTrimOrNullString(alg);
    }
    
    /**
     * Get the certificate digest.
     * 
     * @return the digest
     */
    public byte[] getDigest() {
        return x509digest;
    }

    /**
     * Set the certificate digest.
     * 
     * @param digest the certificate digest to set
     */
    public void setDigest(byte[] digest) {
        if (digest == null || digest.length == 0) {
            throw new IllegalArgumentException("Certificate digest criteria value cannot be null or empty");
        }
        x509digest = digest;
    }

}