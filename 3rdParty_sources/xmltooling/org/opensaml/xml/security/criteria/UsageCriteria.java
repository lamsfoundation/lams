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
import org.opensaml.xml.security.credential.UsageType;


/**
 * An implementation of {@link Criteria} which specifies criteria pertaining 
 * usage of the resolved credential. 
 */
public final class UsageCriteria implements Criteria {
   
    /** Key usage type of resolved credentials. */
    private UsageType credUsage;
    
    /**
    * Constructor.
     *
     * @param usage the usage for which a credential is intended
     */
    public UsageCriteria(UsageType usage) {
        setUsage(usage);
    }

    /**
     * Get the key usage criteria.
     * 
     * @return Returns the usage.
     */
    public UsageType getUsage() {
        return credUsage;
    }

    /**
     * Set the key usage criteria.
     * 
     * @param usage The usage to set.
     */
    public void setUsage(UsageType usage) {
        if (usage != null) {
            credUsage = usage;
        } else {
            credUsage = UsageType.UNSPECIFIED;
        }
    }

}
