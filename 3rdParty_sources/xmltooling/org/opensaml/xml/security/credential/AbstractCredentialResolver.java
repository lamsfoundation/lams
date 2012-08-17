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

package org.opensaml.xml.security.credential;

import org.opensaml.xml.security.CriteriaSet;
import org.opensaml.xml.security.SecurityException;

/**
 * Abstract base class for {@link CredentialResolver} implementations.
 */
public abstract class AbstractCredentialResolver implements CredentialResolver {

    /** {@inheritDoc} */
    public Credential resolveSingle(CriteriaSet criteriaSet) throws SecurityException {
        Iterable<Credential> creds = resolve(criteriaSet);
        if (creds.iterator().hasNext()) {
            return creds.iterator().next();
        } else {
            return null;
        }
    }

    /** {@inheritDoc} */
    public abstract Iterable<Credential> resolve(CriteriaSet criteriaSet) throws SecurityException;

}
