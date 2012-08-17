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

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.opensaml.xacml.ctx.DecisionType.DECISION;
import org.opensaml.xacml.policy.EffectType;
import org.opensaml.xacml.policy.ObligationType;
import org.opensaml.xacml.policy.ObligationsType;

/** A service for evaluating the obligations within a context. */
public class ObligationService {

    /** Read/write lock around the registered obligation handlers. */
    private ReentrantReadWriteLock rwLock;

    /** Registered obligation handlers. */
    private Set<BaseObligationHandler> obligationHandlers;

    /** Constructor. */
    public ObligationService() {
        rwLock = new ReentrantReadWriteLock(true);
        obligationHandlers = new TreeSet<BaseObligationHandler>(new ObligationHandlerComparator());
    }

    /**
     * Gets the registered obligation handlers.
     * 
     * @return registered obligation handlers
     */
    public Set<BaseObligationHandler> getObligationHandlers() {
        return Collections.unmodifiableSet(obligationHandlers);
    }

    /**
     * Adds an obligation handler to the list of registered handlers
     * 
     * This method waits until a write lock is obtained for the set of registered obligation handlers.
     * 
     * @param handler the handler to add to the list of registered handlers.
     */
    public void addObligationhandler(BaseObligationHandler handler) {
        if (handler == null) {
            return;
        }

        Lock writeLock = rwLock.writeLock();
        writeLock.lock();
        try {
            obligationHandlers.add(handler);
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Adds a collection of obligation handler to the list of registered handlers
     * 
     * This method waits until a write lock is obtained for the set of registered obligation handlers.
     * 
     * @param handlers the collection of handlers to add to the list of registered handlers.
     */
    public void addObligationhandler(Collection<BaseObligationHandler> handlers) {
        if (handlers == null || handlers.isEmpty()) {
            return;
        }

        Lock writeLock = rwLock.writeLock();
        writeLock.lock();
        try {
            obligationHandlers.addAll(handlers);
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Removes an obligation handler from the list of registered handlers
     * 
     * This method waits until a write lock is obtained for the set of registered obligation handlers.
     * 
     * @param handler the handler to remove from the list of registered handlers.
     */
    public void removeObligationHandler(BaseObligationHandler handler) {
        if (handler == null) {
            return;
        }

        Lock writeLock = rwLock.writeLock();
        writeLock.lock();
        try {
            obligationHandlers.remove(handler);
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Processes the obligations within the effective XACML policy.
     * 
     * This method waits until a read lock is obtained for the set of registered obligation handlers.
     * 
     * @param context current processing context
     * 
     * @throws ObligationProcessingException thrown if there is a problem evaluating an obligation
     */
    public void processObligations(ObligationProcessingContext context) throws ObligationProcessingException {
        Lock readLock = rwLock.readLock();
        readLock.lock();
        try {
            Iterator<BaseObligationHandler> handlerItr = obligationHandlers.iterator();
            Map<String, ObligationType> effectiveObligations = preprocessObligations(context);

            BaseObligationHandler handler;
            while (handlerItr.hasNext()) {
                handler = handlerItr.next();
                if (effectiveObligations.containsKey(handler.getObligationId())) {
                    handler.evaluateObligation(context, effectiveObligations.get(handler.getObligationId()));
                }
            }
        } finally {
            readLock.unlock();
        }
    }

    /**
     * Preprocesses the obligations returned within the result. This preprocessing determines the active effect, based
     * on {@link org.opensaml.xacml.ctx.ResultType#getDecision()}, and creates an index that maps obligation IDs to the
     * {@link ObligationType} returned by the PDP.
     * 
     * @param context current processing context
     * 
     * @return preprocessed obligations
     */
    protected Map<String, ObligationType> preprocessObligations(ObligationProcessingContext context) {
        HashMap<String, ObligationType> effectiveObligations = new HashMap<String, ObligationType>();

        ObligationsType obligations = context.getAuthorizationDecisionResult().getObligations();
        if (obligations == null || obligations.getObligations() == null) {
            return effectiveObligations;
        }

        EffectType activeEffect;
        if (context.getAuthorizationDecisionResult().getDecision().getDecision() == DECISION.Permit) {
            activeEffect = EffectType.Permit;
        } else {
            activeEffect = EffectType.Deny;
        }

        for (ObligationType obligation : obligations.getObligations()) {
            if (obligation != null && obligation.getFulfillOn() == activeEffect) {
                effectiveObligations.put(obligation.getObligationId(), obligation);
            }
        }

        return effectiveObligations;
    }

    /** Comparator used to order obligation handlers by precedence. */
    private class ObligationHandlerComparator implements Comparator<BaseObligationHandler> {

        /** {@inheritDoc} */
        public int compare(BaseObligationHandler o1, BaseObligationHandler o2) {
            if (o1.getHandlerPrecedence() == o2.getHandlerPrecedence()) {
                // If they have the same precedence sort lexigraphically
                return o1.getObligationId().compareTo(o2.getObligationId());
            }

            if (o1.getHandlerPrecedence() < o2.getHandlerPrecedence()) {
                return -1;
            }

            return 1;
        }
    }
}