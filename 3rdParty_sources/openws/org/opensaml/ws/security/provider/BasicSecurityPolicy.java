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

import java.util.ArrayList;
import java.util.List;

import org.opensaml.ws.message.MessageContext;
import org.opensaml.ws.security.SecurityPolicy;
import org.opensaml.ws.security.SecurityPolicyException;
import org.opensaml.ws.security.SecurityPolicyRule;

/**
 * Basic security policy implementation which evaluates a given set of {@link SecurityPolicyRule} in an ordered manner.
 * 
 * A policy evaluates successfully if, and only if, all policy rules evaluate successfully.
 */
public class BasicSecurityPolicy implements SecurityPolicy {
    
    /** Registered security rules. */
    private ArrayList<SecurityPolicyRule> rules;
    
    /** Constructor. */
    public BasicSecurityPolicy(){
        rules = new ArrayList<SecurityPolicyRule>(5);
    }
    
    /** {@inheritDoc} */
    public List<SecurityPolicyRule> getPolicyRules() {
        return rules;
    }

    /** {@inheritDoc} */
    public void evaluate(MessageContext messageContext) throws SecurityPolicyException {
        for(SecurityPolicyRule rule : getPolicyRules()){
            rule.evaluate(messageContext);
        }
    }
}