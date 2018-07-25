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
import org.opensaml.xml.util.DatatypeHelper;

/**
 * An implementation of {@link Criteria} which specifies key name criteria.
 */
public final class KeyNameCriteria implements Criteria {

    /** Key name of resolved credentials.  */
    private String keyName;
    
    /**
     * Constructor.
     *
     * @param name key name
     */
    public KeyNameCriteria(String name) {
        setKeyName(name);
    }

    /**
     * Get the key name criteria.
     * 
     * @return Returns the keyName.
     */
    public String getKeyName() {
        return keyName;
    }

    /**
     * Set the key name criteria.
     * 
     * @param name The keyName to set.
     */
    public void setKeyName(String name) {
        if (DatatypeHelper.isEmpty(name)) {
            throw new IllegalArgumentException("Key name criteria value must be supplied");
        }
        keyName = DatatypeHelper.safeTrimOrNullString(name);
    }

}
