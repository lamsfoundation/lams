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

package org.opensaml.xml.security.keyinfo;

import org.opensaml.xml.security.credential.Credential;

/**
 * Interface for factories which produce {@link KeyInfoGenerator} instances.
 */
public interface KeyInfoGeneratorFactory {
    
    /**
     * Get a new instance of the generator type produced by the factory.
     * 
     * @return a new KeyInfoGenerator instance
     */
    public KeyInfoGenerator newInstance();
    
    /**
     * Check whether the generators produced by this factory can handle the specified credential.
     * 
     * @param credential the credential to evaluate
     * @return true if the generators produced by this factory can handle the type of the specified credential,
     *          false otherwise
     */
    public boolean handles(Credential credential);
    
    /**
     * Get the type (interface) of the specific type of credential handled by generators produced by
     * this factory.  Primarily used as an index by manager implementions such as {@link KeyInfoGeneratorManager}.
     * 
     * @return the specifc type of credential handled by the generators produced by this factory
     */
    public Class<? extends Credential> getCredentialType();
    
}
