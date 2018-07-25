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

package org.opensaml.xml.security.credential.criteria;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

import org.bouncycastle.util.Arrays;
import org.opensaml.xml.security.Criteria;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.security.x509.X509Credential;
import org.opensaml.xml.security.x509.X509DigestCriteria;
import org.opensaml.xml.util.DatatypeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * An implementation of {@link Criteria} which specifies criteria based on
 * the digest of an X.509 certificate.
 */
public final class EvaluableX509DigestCredentialCriteria implements EvaluableCredentialCriteria {

    /** Logger. */
    private final Logger log = LoggerFactory.getLogger(EvaluableX509DigestCredentialCriteria.class);
    
    /** Digest algorithm. */
    private final String algorithm;
    
    /** X.509 certificate digest. */
    private final byte[] x509digest;

    /**
     * Constructor.
     * 
     * @param criteria the criteria which is the basis for evaluation
     */
    public EvaluableX509DigestCredentialCriteria(X509DigestCriteria criteria) {
        if (criteria == null) {
            throw new NullPointerException("Criteria instance may not be null");
        }
        algorithm = criteria.getAlgorithm();
        x509digest = criteria.getDigest();
    }
    
    /**
     * Constructor.
     *
     * @param alg algorithm of digest computation
     * @param digest certificate digest
     */
    public EvaluableX509DigestCredentialCriteria(String alg, byte[] digest) {
        if (digest == null || digest.length == 0) {
            throw new IllegalArgumentException("X.509 digest cannot be null or empty");
        } else if (DatatypeHelper.isEmpty(alg)) {
            throw new IllegalArgumentException("Digest algorithm criteria value cannot be null or empty");
        }
        x509digest = digest;
        algorithm = DatatypeHelper.safeTrimOrNullString(alg);
    }

    /** {@inheritDoc} */
    public Boolean evaluate(Credential target) {
        if (target == null) {
            log.error("Credential target was null");
            return null;
        } else if (!(target instanceof X509Credential)) {
            log.info("Credential is not an X509Credential, does not satisfy X.509 digest criteria");
            return Boolean.FALSE;
        }

        X509Certificate entityCert = ((X509Credential) target).getEntityCertificate();
        if (entityCert == null) {
            log.info("X509Credential did not contain an entity certificate, does not satisfy criteria");
            return Boolean.FALSE;
        }
        
        try {
            MessageDigest hasher = MessageDigest.getInstance(algorithm);
            byte[] hashed = hasher.digest(entityCert.getEncoded());
            return Arrays.areEqual(hashed, x509digest);
        } catch (CertificateEncodingException e) {
            log.error("Unable to encode certificate for digest operation", e);
        } catch (NoSuchAlgorithmException e) {
            log.error("Unable to obtain a digest implementation for algorithm {" + algorithm + "}" , e);
        }
        
        return null;
    }
    
}