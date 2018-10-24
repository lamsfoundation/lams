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

import org.opensaml.xacml.policy.ObligationType;
import org.opensaml.xml.util.DatatypeHelper;

/**
 * Base class for all obligation handlers.
 * 
 * Handlers are executed in order of precedence. Handlers with a higher precedence are executed before those with a
 * lower precedence. Handlers with the same precedence are executed in random order.
 * 
 * Obligation handlers <strong>must</strong> be stateless.
 */
public abstract class BaseObligationHandler {

    /** ID of the handled obligation. */
    private String id;

    /** Precedence of this handler. */
    private int precedence;

    /**
     * Constructor. Obligation has the lowest precedence
     * 
     * @param obligationId ID of the handled obligation
     */
    protected BaseObligationHandler(String obligationId) {
        this(obligationId, Integer.MIN_VALUE);
    }

    /**
     * Constructor.
     * 
     * @param obligationId ID of the handled obligation
     * @param handlerPrecedence precedence of this handler
     */
    protected BaseObligationHandler(String obligationId, int handlerPrecedence) {
        id = DatatypeHelper.safeTrimOrNullString(obligationId);
        if (id == null) {
            throw new IllegalArgumentException("Provided obligation ID may not be null or empty");
        }

        precedence = handlerPrecedence;
    }

    /**
     * Gets the ID of the handled obligation.
     * 
     * @return ID of the handled obligation
     */
    public String getObligationId() {
        return id;
    }

    /**
     * Gets the precedence of the handler.
     * 
     * @return precedence of the handler
     */
    public int getHandlerPrecedence() {
        return precedence;
    }

    /**
     * Evaluates the obligation represented by this handler.
     * 
     * @param context current processing context
     * @param obligation the obligation as returned by the PDP
     * 
     * @throws ObligationProcessingException thrown if there is a problem evaluating this handler
     */
    public abstract void evaluateObligation(ObligationProcessingContext context, ObligationType obligation)
            throws ObligationProcessingException;

    /** {@inheritDoc} */
    public int hashCode() {
        return getObligationId().hashCode();
    }

    /** {@inheritDoc} */
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof BaseObligationHandler) {
            return DatatypeHelper.safeEquals(getObligationId(), ((BaseObligationHandler) obj).getObligationId());
        }

        return false;
    }
}