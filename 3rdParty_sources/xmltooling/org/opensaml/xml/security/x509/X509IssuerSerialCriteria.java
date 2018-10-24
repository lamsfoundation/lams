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

import java.math.BigInteger;

import javax.security.auth.x500.X500Principal;

import org.opensaml.xml.security.Criteria;

/**
 * An implementation of {@link Criteria} which specifies criteria based on
 * X.509 certificate issuer name and serial number.
 */
public final class X509IssuerSerialCriteria implements Criteria {
    
    /** X.509 certificate issuer name. */
    private X500Principal issuerName;
    
    /** X.509 certificate serial number. */
    private BigInteger serialNumber;
    
    /**
     * Constructor.
     *
     * @param issuer certificate issuer name
     * @param serial certificate serial number
     */
    public X509IssuerSerialCriteria(X500Principal issuer, BigInteger serial) {
        setIssuerName(issuer);
        setSerialNumber(serial);
    }
    
    /** Get the issuer name.
     * 
     * @return Returns the issuer name.
     */
    public X500Principal getIssuerName() {
        return issuerName;
    }

    /**
     * Set the issuer name.
     * 
     * @param issuer The issuer name to set.
     */
    public void setIssuerName(X500Principal issuer) {
        if (issuer == null) {
            throw new IllegalArgumentException("Issuer principal criteria value may not be null");
        }
        this.issuerName = issuer;
    }

    /**
     * Get the serial number.
     * 
     * @return Returns the serial number.
     */
    public BigInteger getSerialNumber() {
        return serialNumber;
    }

    /**
     * Set the serial number.
     * 
     * @param serial The serial number to set.
     */
    public void setSerialNumber(BigInteger serial) {
        if (serial == null) {
            throw new IllegalArgumentException("Serial number criteria value may not be null");
        }
        this.serialNumber = serial;
    }

}
