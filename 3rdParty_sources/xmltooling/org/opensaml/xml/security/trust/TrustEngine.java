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

import org.opensaml.xml.security.CriteriaSet;
import org.opensaml.xml.security.SecurityException;

/**
 * Evaluates the trustworthiness and validity of a token against 
 * implementation-specific requirements.
 *
 * @param <TokenType> the token type this trust engine evaluates
 */
public interface TrustEngine<TokenType> {
    
    /**
     * Validates the token against trusted information obtained in an
     * implementation-specific manner.
     *
     * @param token security token to validate
     * @param trustBasisCriteria criteria used to describe and/or resolve the information
     *          which serves as the basis for trust evaluation
     *
     * @return true if the token is trusted and valid, false if not
     *
     * @throws SecurityException thrown if there is a problem validating the security token
     */
    public boolean validate(TokenType token, CriteriaSet trustBasisCriteria) throws SecurityException;
}
