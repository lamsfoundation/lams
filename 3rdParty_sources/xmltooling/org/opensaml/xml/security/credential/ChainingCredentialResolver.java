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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.opensaml.xml.security.CriteriaSet;
import org.opensaml.xml.security.SecurityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An implementation of {@link CredentialResolver} which chains together one or more underlying credential resolver
 * implementations. Resolved credentials are returned from all underlying resolvers in the chain, in the order implied
 * by the order of the resolvers in the chain.
 */
public class ChainingCredentialResolver extends AbstractCredentialResolver {

    /** Logger. */
    private final Logger log = LoggerFactory.getLogger(ChainingCredentialResolver.class);

    /** List of credential resolvers in the chain. */
    private List<CredentialResolver> resolvers;

    /**
     * Constructor.
     */
    public ChainingCredentialResolver() {
        resolvers = new ArrayList<CredentialResolver>();
    }

    /**
     * Get the (modifiable) list of credential resolvers which comprise the resolver chain.
     * 
     * @return the list of credential resolvers in the chain
     */
    public List<CredentialResolver> getResolverChain() {
        return resolvers;
    }

    /** {@inheritDoc} */
    public Iterable<Credential> resolve(CriteriaSet criteriaSet) throws SecurityException {
        if (resolvers.isEmpty()) {
            log.warn("Chaining credential resolver resolution was attempted with an empty resolver chain");
            throw new IllegalStateException("The resolver chain is empty");
        }
        return new CredentialIterable(this, criteriaSet);
    }

    /**
     * Implementation of {@link Iterable} to be returned by {@link ChainingCredentialResolver}.
     */
    public class CredentialIterable implements Iterable<Credential> {

        /** The chaining credential resolver which owns this instance. */
        private ChainingCredentialResolver parent;

        /** The criteria set on which to base resolution. */
        private CriteriaSet critSet;

        /**
         * Constructor.
         * 
         * @param resolver the chaining parent of this iterable
         * @param criteriaSet the set of criteria which is input to the underyling resolvers
         */
        public CredentialIterable(ChainingCredentialResolver resolver, CriteriaSet criteriaSet) {
            parent = resolver;
            critSet = criteriaSet;
        }

        /** {@inheritDoc} */
        public Iterator<Credential> iterator() {
            return new CredentialIterator(parent, critSet);
        }

    }

    /**
     * Implementation of {@link Iterator} to be returned (indirectly) by {@link ChainingCredentialResolver}.
     */
    public class CredentialIterator implements Iterator<Credential> {

        /** Logger. */
        private final Logger log = LoggerFactory.getLogger(CredentialIterator.class);

        /** The chaining credential resolver which owns this instance. */
        private ChainingCredentialResolver parent;

        /** The criteria set on which to base resolution. */
        private CriteriaSet critSet;

        /** The iterator over resolvers in the chain. */
        private Iterator<CredentialResolver> resolverIterator;

        /** The iterator over Credential instances from the current resolver. */
        private Iterator<Credential> credentialIterator;

        /** The current resolver which is returning credentials. */
        private CredentialResolver currentResolver;

        /** The next credential that is safe to return. */
        private Credential nextCredential;

        /**
         * Constructor.
         * 
         * @param resolver the chaining parent of this iterable
         * @param criteriaSet the set of criteria which is input to the underyling resolvers
         */
        public CredentialIterator(ChainingCredentialResolver resolver, CriteriaSet criteriaSet) {
            parent = resolver;
            critSet = criteriaSet;
            resolverIterator = parent.getResolverChain().iterator();
            credentialIterator = getNextCredentialIterator();
            nextCredential = null;
        }

        /** {@inheritDoc} */
        public boolean hasNext() {
            if (nextCredential != null) {
                return true;
            }
            nextCredential = getNextCredential();
            if (nextCredential != null) {
                return true;
            }
            return false;
        }

        /** {@inheritDoc} */
        public Credential next() {
            Credential tempCred;
            if (nextCredential != null) {
                tempCred = nextCredential;
                nextCredential = null;
                return tempCred;
            }
            tempCred = getNextCredential();
            if (tempCred != null) {
                return tempCred;
            } else {
                throw new NoSuchElementException("No more Credential elements are available");
            }
        }

        /** {@inheritDoc} */
        public void remove() {
            throw new UnsupportedOperationException("Remove operation is not supported by this iterator");
        }

        /**
         * Get the iterator from the next resolver in the chain.
         * 
         * @return an iterator of credentials
         */
        private Iterator<Credential> getNextCredentialIterator() {
            while (resolverIterator.hasNext()) {
                currentResolver = resolverIterator.next();
                    log.debug("Getting credential iterator from next resolver in chain: {}", currentResolver.getClass().toString());
                try {
                    return currentResolver.resolve(critSet).iterator();
                } catch (SecurityException e) {
                    log.error(String.format("Error resolving credentials from chaining resolver member '%s'",
                            currentResolver.getClass().getName()), e);
                    if (resolverIterator.hasNext()) {
                        log.error("Will attempt to resolve credentials from next member of resolver chain");
                    }
                }
            }

            log.debug("No more credential resolvers available in the resolver chain");
            currentResolver = null;
            return null;
        }

        /**
         * Get the next credential that will be returned by this iterator.
         * 
         * @return the next credential to return
         */
        private Credential getNextCredential() {
            if (credentialIterator != null) {
                if (credentialIterator.hasNext()) {
                    return credentialIterator.next();
                }
            }

            credentialIterator = getNextCredentialIterator();
            while (credentialIterator != null) {
                if (credentialIterator.hasNext()) {
                    return credentialIterator.next();
                }
                credentialIterator = getNextCredentialIterator();
            }

            return null;
        }

    }

}
