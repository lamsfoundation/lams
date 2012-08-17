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

package org.opensaml.ws.security.provider;

import java.util.Collections;
import java.util.List;

import org.opensaml.ws.message.MessageContext;
import org.opensaml.ws.security.SecurityPolicy;
import org.opensaml.ws.security.SecurityPolicyResolver;
import org.opensaml.xml.security.SecurityException;
import org.opensaml.xml.util.LazyList;

/** A simple security policy resolver implementation that returns a static list of policies. */
public class StaticSecurityPolicyResolver implements SecurityPolicyResolver {

    /** Registered security policies. */
    private List<SecurityPolicy> securityPolicies;

    /**
     * Constructor.
     * 
     * @param policy the static policy returned by this resolver
     */
    public StaticSecurityPolicyResolver(SecurityPolicy policy) {
        securityPolicies = new LazyList<SecurityPolicy>();
        if(policy != null){
            securityPolicies.add(policy);
        }
    }
    
    /**
     * Constructor.
     * 
     * @param policies the static list of policies returned by this resolver
     */
    public StaticSecurityPolicyResolver(List<SecurityPolicy> policies) {
        securityPolicies = new LazyList<SecurityPolicy>();
        if(policies != null){
            securityPolicies.addAll(policies);
        }
    }

    /** {@inheritDoc} */
    public Iterable<SecurityPolicy> resolve(MessageContext criteria) throws SecurityException {
        return Collections.unmodifiableList(securityPolicies);
    }

    /**
     * {@inheritDoc}
     * 
     * If more than one policy is registered with this resolver this method returns the first policy in the list.
     */
    public SecurityPolicy resolveSingle(MessageContext criteria) throws SecurityException {
        if (!securityPolicies.isEmpty()) {
            return securityPolicies.get(0);
        } else {
            return null;
        }
    }
}