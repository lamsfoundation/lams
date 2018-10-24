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

import java.util.Set;

/**
 * Specialization of {@link PKIXValidationOptions} which specifies options specific to a {@link PKIXTrustEvaluator}
 * based on the Java CertPath API.
 */
public class CertPathPKIXValidationOptions extends PKIXValidationOptions {
    
    /** Force RevocationEnabled flag. */
    private boolean forceRevocationEnabled;
   
    /** Value for RevocationEnabled when forced. */
    private boolean revocationEnabled;

    /** Disable policy mapping flag. */
    private boolean policyMappingInhibit;

    /** Flag for disallowing the "any" policy OID. */
    private boolean anyPolicyInhibit;

    /** Acceptable policy OIDs. */
    private Set<String> initialPolicies;

    /** Constructor. */
    public CertPathPKIXValidationOptions() {
        super();
        forceRevocationEnabled = false;
        revocationEnabled = true;
        policyMappingInhibit = false;
        anyPolicyInhibit = false;
        initialPolicies = null;
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
     * @param flag The forceRevocationEnabled to set.
     */
    public void setForceRevocationEnabled(boolean flag) {
        forceRevocationEnabled = flag;
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
     * @param flag The revocationEnabled to set.
     */
    public void setRevocationEnabled(boolean flag) {
        revocationEnabled = flag;
    }

    /**
     * Returns the value of the policy mapping inhibited flag of the underlying CertPath Provider.
     * 
     * @return Returns the policyMappingInhibit boolean.
     */
    public boolean isPolicyMappingInhibited() {
        return policyMappingInhibit;
    }

    /**
     * Sets the policy mapping inhibited flag for the underlying CertPath Provider.
     * See also RFC 5280, section 6.1.1 (e).
     * 
     * <p>Default is: <b>false</b></p>
     * 
     * @param flag the policyMappingInhibit boolean to set.
     */
    public void setPolicyMappingInhibit(boolean flag) {
        policyMappingInhibit = flag;
    }

    /**
     * Returns the value of the any policy inhibited flag of the underlying CertPath Provider.
     * 
     * @return Returns the anyPolicyInhibit boolean.
     */
    public boolean isAnyPolicyInhibited() {
        return anyPolicyInhibit;
    }

    /**
     * Sets the any policy inhibited flag for the underlying CertPath Provider.
     * See also RFC 5280, section 6.1.1 (g).
     * 
     * <p>Default is: <b>false</b></p>
     * 
     * @param flag the anyPolicyInhibit boolean to set.
     */
    public void setAnyPolicyInhibit(boolean flag) {
        anyPolicyInhibit = flag;
    }

    /**
     * Returns the set of initial policies (OID strings) of the underlying CertPath Provider.
     * See also RFC 5280, section 6.1.1 (c).
     * 
     * @return Returns the initialPolicies set.
     */
    public Set<String> getInitialPolicies() {
        return initialPolicies;
    }

    /**
     * Sets the initial policy identifiers (OID strings) for the underlying CertPath Provider,
     * i.e. those policies that are acceptable to the certificate user.
     * See also RFC 5280, section 6.1.1 (c).
     * 
     * @param newPolicies the initial set of policy identifiers (OID strings)
     */
    public void setInitialPolicies(Set<String> newPolicies) {
        initialPolicies = newPolicies;
    }
}
