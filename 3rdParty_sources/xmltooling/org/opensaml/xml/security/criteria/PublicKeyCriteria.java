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

package org.opensaml.xml.security.criteria;

import java.security.PublicKey;

import org.opensaml.xml.security.Criteria;

/**
 * An implementation of {@link Criteria} which specifies public key criteria.
 */
public final class PublicKeyCriteria implements Criteria {

    /** Specifier of public key associated with resolved credentials. */
    private PublicKey publicKey;
    
    /**
     * Constructor.
     *
     * @param pubKey public key
     */
    public PublicKeyCriteria(PublicKey pubKey) {
        setPublicKey(pubKey);
    }
    
    /**
     * Get the public key criteria.
     * 
     * @return Returns the publicKey.
     */
    public PublicKey getPublicKey() {
        return publicKey;
    }

    /**
     * Set the public key criteria. 
     * 
     * @param key The publicKey to set.
     */
    public void setPublicKey(PublicKey key) {
        if (key == null) {
            throw new IllegalArgumentException("Public key criteria value must be supplied");
        }
        publicKey = key;
    }

}
