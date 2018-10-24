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

import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.Collection;

/**
 * Basic implementation of {@link PKIXValidationInformation}.
 */
public class BasicPKIXValidationInformation implements PKIXValidationInformation {

    /** Certs used as the trust anchors. */
    private Collection<X509Certificate> trustAnchors;

    /** CRLs used during validation. */
    private Collection<X509CRL> trustedCRLs;

    /** Max verification depth during PKIX validation. */
    private Integer verificationDepth;

    /**
     * Constructor.
     * 
     * @param anchors certs used as trust anchors during validation
     * @param crls CRLs used during validation
     * @param depth max verification path depth
     */
    public BasicPKIXValidationInformation(Collection<X509Certificate> anchors, Collection<X509CRL> crls,
            Integer depth) {
        
        trustAnchors = anchors;
        trustedCRLs = crls;
        verificationDepth = depth;
    }

    /** {@inheritDoc} */
    public Collection<X509CRL> getCRLs() {
        return trustedCRLs;
    }

    /** {@inheritDoc} */
    public Collection<X509Certificate> getCertificates() {
        return trustAnchors;
    }

    /** {@inheritDoc} */
    public Integer getVerificationDepth() {
        return verificationDepth;
    }
}