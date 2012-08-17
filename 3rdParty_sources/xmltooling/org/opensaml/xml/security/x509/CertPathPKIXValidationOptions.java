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

/**
 * Specialization of {@link PKIXValidationOptions} which specifies options specific to a {@link PKIXTrustEvaluator}
 * based on the Java CertPath API.
 */
public class CertPathPKIXValidationOptions extends PKIXValidationOptions {
    
    /** Force RevocationEnabled flag. */
    private boolean forceRevocationEnabled;
   
    /** Value for RevocationEnabled when forced. */
    private boolean revocationEnabled;

    /** Constructor. */
    public CertPathPKIXValidationOptions() {
        super();
        forceRevocationEnabled = false;
        revocationEnabled = true;
    }
    
    /**
     * If true, the revocation behavior of the underlying CertPath provider will be forced to the
     * value supplied by {@link #isRevocationEnabled()}. If false, the revocation behavior
     * of the underlying provider will be determined by the PKIXTrustEvaluator implementation.
     * 
     * <p>Default is: <b>false</b></p>
     * 
     * @return Returns the forceRevocationEnabled.
     */
    public boolean isForceRevocationEnabled() {
        return forceRevocationEnabled;
    }

    /**
     * If true, the revocation behavior of the underlying CertPath provider will be forced to the
     * value supplied by {@link #isRevocationEnabled()}. If false, the revocation behavior
     * of the underlying provider will be determined by the PKIXTrustEvaluator implementation.
     * 
     * <p>Default is: <b>false</b></p>
     * 
     * @param forceRevocationEnabled The forceRevocationEnabled to set.
     */
    public void setForceRevocationEnabled(boolean forceRevocationEnabled) {
        this.forceRevocationEnabled = forceRevocationEnabled;
    }

    /**
     * If {@link #isForceRevocationEnabled()} is true, the revocation behavior of the underlying CertPath Provider
     * will be forced to this value. If the former is false, the revocation behavior
     * of the underlying provider will be determined by the PKIXTrustEvaluator implementation.
     * 
     * <p>Default is: <b>true</b></p>
     * 
     * @return Returns the revocationEnabled.
     */
    public boolean isRevocationEnabled() {
        return revocationEnabled;
    }

    /**
     * If {@link #isForceRevocationEnabled()} is true, the revocation behavior of the underlying CertPath Provider
     * will be forced to this value. If the former is false, the revocation behavior
     * of the underlying provider will be determined by the PKIXTrustEvaluator implementation.
     * 
     * <p>Default is: <b>true</b></p>
     * 
     * @param revocationEnabled The revocationEnabled to set.
     */
    public void setRevocationEnabled(boolean revocationEnabled) {
        this.revocationEnabled = revocationEnabled;
    }
    

}
