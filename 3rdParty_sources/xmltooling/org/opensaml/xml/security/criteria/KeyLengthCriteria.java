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

import org.opensaml.xml.security.Criteria;

/**
 * An implementation of {@link Criteria} which specifies key length criteria.
 */
public final class KeyLengthCriteria implements Criteria {
    
    /** Key length of resolved credentials. */
    private Integer keyLength;
    
    /**
     * Constructor.
     *
     * @param length key length 
     */
    public KeyLengthCriteria(Integer length) {
        setKeyLength(length);
    }

    /**
     * Get the key length.
     * 
     * @return Returns the keyLength.
     */
    public Integer getKeyLength() {
        return keyLength;
    }

    /**
     * Set the key length.
     * 
     * @param length The keyLength to set.
     */
    public void setKeyLength(Integer length) {
        if (length == null) {
            throw new IllegalArgumentException("Key length criteria value must be supplied");
        }
        keyLength = length;
    }

}
