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

package org.opensaml.xml.security.trust;

import org.opensaml.xml.security.credential.CredentialResolver;

/**
 * Evaluates the trustworthiness and validity of a token against 
 * implementation-specific requirements based on trusted credentials
 * obtained via a credential resolver.
 *
 * @param <TokenType> the token type this trust engine evaluates
 */
public interface TrustedCredentialTrustEngine<TokenType> extends TrustEngine<TokenType> {

    /**
     * Gets the credential resolver used to recover trusted credentials that 
     * may be used to validate tokens.
     *
     * @return credential resolver used to recover trusted credentials 
     *         that may be used to validate tokens
     */
    public CredentialResolver getCredentialResolver();
}
