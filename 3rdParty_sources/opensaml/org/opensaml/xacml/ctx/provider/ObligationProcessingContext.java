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

package org.opensaml.xacml.ctx.provider;

import org.opensaml.xacml.ctx.ResultType;

/**
 * A context for processing obligations.
 */
public class ObligationProcessingContext {

    /** Result of a XACML authorization request. */
    private ResultType result;

    /**
     * Constructor.
     * 
     * @param authzResult result of a XACML authorization request
     */
    public ObligationProcessingContext(ResultType authzResult) {
        if (authzResult == null) {
            throw new IllegalArgumentException("Authorization request result may not be null");
        }
        result = authzResult;
    }

    /**
     * Gets the result of a XACML authorization request.
     * 
     * @return result of a XACML authorization request
     */
    public ResultType getAuthorizationDecisionResult() {
        return result;
    }
}