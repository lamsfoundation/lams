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
 * Options which may be supplied to influence the processing behavior of a {@link PKIXTrustEvaluator}.
 */
public class PKIXValidationOptions {

    /** Flag as to whether empty CRL's will be processed. */
    private boolean processEmptyCRLs;
    
    /** Flag as to whether expired CRL's will be processed. */
    private boolean processExpiredCRLs;
    
    /** Flag as to whether CRL's supplied in the untrusted credential being evaluated will be processed. */
    private boolean processCredentialCRLs;
    
    /** Default verification depth. */
    private Integer defaultVerificationDepth;

    /** Constructor. */
    public PKIXValidationOptions() {
        processEmptyCRLs = true;
        processExpiredCRLs = true;
        processCredentialCRLs = true;
        
        defaultVerificationDepth = new Integer(1);
    }

    /**
     * Whether empty CRL's should be processed.
     * 
     * <p>Default is: <b>true</b></p>
     * 
     * @return Returns the processEmptyCRLs.
     */
    public boolean isProcessEmptyCRLs() {
        return processEmptyCRLs;
    }

    /**
     * Whether empty CRL's should be processed.
     * 
     * <p>Default is: <b>true</b></p>
     * 
     * @param processEmptyCRLs The processEmptyCRLs to set.
     */
    public void setProcessEmptyCRLs(boolean processEmptyCRLs) {
        this.processEmptyCRLs = processEmptyCRLs;
    }

    /**
     * Whether expired CRL's should be processed.
     * 
     * <p>Default is: <b>true</b></p>
     * 
     * @return Returns the processExpiredCRLs.
     */
    public boolean isProcessExpiredCRLs() {
        return processExpiredCRLs;
    }

    /**
     * Whether expired CRL's should be processed.
     * 
     * <p>Default is: <b>true</b></p>
     * 
     * @param processExpiredCRLs The processExpiredCRLs to set.
     */
    public void setProcessExpiredCRLs(boolean processExpiredCRLs) {
        this.processExpiredCRLs = processExpiredCRLs;
    }

    /**
     * Whether CRL's supplied within the untrusted {@link X509Credential} being evaluated should be processed.
     * 
     * <p>Default is: <b>true</b></p>
     * 
     * @return Returns the processCredentialCRLs.
     */
    public boolean isProcessCredentialCRLs() {
        return processCredentialCRLs;
    }

    /**
     * Whether CRL's supplied within the untrusted {@link X509Credential} being evaluated should be processed.
     * 
     * <p>Default is: <b>true</b></p>
     * 
     * @param processCredentialCRLs The processCredentialCRLs to set.
     */
    public void setProcessCredentialCRLs(boolean processCredentialCRLs) {
        this.processCredentialCRLs = processCredentialCRLs;
    }

    /**
     * The default PKIX maximum path verification depth, if not supplied in the 
     * {@link PKIXValidationInformation} being evaluated.
     * 
     * <p>Default is: <b>1</b></p>
     * 
     * @return Returns the defaultVerificationDepth.
     */
    public Integer getDefaultVerificationDepth() {
        return defaultVerificationDepth;
    }

    /**
     * The default PKIX maximum path verification depth, if not supplied in the 
     * {@link PKIXValidationInformation} being evaluated.
     * 
     * <p>Default is: <b>1</b></p>
     * 
     * @param defaultVerificationDepth The defaultVerificationDepth to set.
     */
    public void setDefaultVerificationDepth(Integer defaultVerificationDepth) {
        if (defaultVerificationDepth == null) {
            throw new IllegalArgumentException("Default verification depth may not be null");
        }
        this.defaultVerificationDepth = defaultVerificationDepth;
    }

}
