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

import org.opensaml.xml.security.SecurityException;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.signature.KeyInfo;

/**
 * Interface for implementations which generate a {@link KeyInfo} based on keying material and other
 * information found within a {@link Credential}.
 */
public interface KeyInfoGenerator {
    
    /**
     * Generate a new KeyInfo object based on keying material and other information within a credential. 
     * 
     * @param credential the credential containing keying material and possibly other information
     * @return a new KeyInfo object
     * @throws SecurityException thrown if there is any error generating the new KeyInfo from the credential
     */
    public KeyInfo generate(Credential credential) throws SecurityException;

}
