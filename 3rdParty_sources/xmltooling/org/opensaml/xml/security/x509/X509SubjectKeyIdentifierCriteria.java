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

/**
 * An implementation of {@link Criteria} which specifies criteria based on
 * X.509 certificate subject key identifier.
 */
public final class X509SubjectKeyIdentifierCriteria implements Criteria {
    
    /** X.509 certificate subject key identifier. */
    private byte[] subjectKeyIdentifier;
    
    /**
     * Constructor.
     *
     * @param ski certificate subject key identifier
     */
    public X509SubjectKeyIdentifierCriteria(byte[] ski) {
        setSubjectKeyIdentifier(ski);
    }
    
    /**
     * Get the subject key identifier.
     * 
     * @return Returns the subject key identifier
     */
    public byte[] getSubjectKeyIdentifier() {
        return subjectKeyIdentifier;
    }

    /**
     * Set the subject key identifier.
     * 
     * @param ski The subject key identifier to set.
     */
    public void setSubjectKeyIdentifier(byte[] ski) {
        if (ski == null || ski.length == 0) {
            throw new IllegalArgumentException("Subject key identifier criteria value must be non-null and non-empty");
        }
        subjectKeyIdentifier = ski;
    }
    
    
    

}
