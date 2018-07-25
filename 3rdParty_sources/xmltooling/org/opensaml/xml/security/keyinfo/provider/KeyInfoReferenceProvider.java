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

package org.opensaml.xml.security.keyinfo.provider;

import java.util.ArrayList;
import java.util.Collection;

import org.opensaml.xml.XMLObject;
import org.opensaml.xml.security.Criteria;
import org.opensaml.xml.security.CriteriaSet;
import org.opensaml.xml.security.SecurityException;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.security.keyinfo.KeyInfoCredentialResolver;
import org.opensaml.xml.security.keyinfo.KeyInfoCriteria;
import org.opensaml.xml.security.keyinfo.KeyInfoProvider;
import org.opensaml.xml.security.keyinfo.KeyInfoResolutionContext;
import org.opensaml.xml.signature.KeyInfo;
import org.opensaml.xml.signature.KeyInfoReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of {@link KeyInfoProvider} which supports {@link KeyInfoReference}.
 * 
 * <p>To prevent cycles, only a single reference step is permitted. Only same-document
 * references are supported.</p>
 */
public class KeyInfoReferenceProvider extends AbstractKeyInfoProvider {

    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(KeyInfoReferenceProvider.class);

    /** {@inheritDoc} */
    public boolean handles(XMLObject keyInfoChild) {
        return getKeyInfoReference(keyInfoChild) != null;
    }

    /** {@inheritDoc} */
    public Collection<Credential> process(KeyInfoCredentialResolver resolver, XMLObject keyInfoChild,
            CriteriaSet criteriaSet, KeyInfoResolutionContext kiContext) throws SecurityException {

        KeyInfoReference ref = getKeyInfoReference(keyInfoChild);
        if (ref == null) {
            return null;
        }

        log.debug("Attempting to follow same-document KeyInfoReference");

        XMLObject target = ref.resolveIDFromRoot(ref.getURI().substring(1));
        if (target == null) {
            log.warn("KeyInfoReference URI could not be dereferenced");
            return null;
        } else if (!(target instanceof KeyInfo)) {
            log.warn("The product of dereferencing the KeyInfoReference was not a KeyInfo");
            return null;
        } else if (!((KeyInfo) target).getXMLObjects(KeyInfoReference.DEFAULT_ELEMENT_NAME).isEmpty()) {
            log.warn("The dereferenced KeyInfo contained a KeyInfoReference, cannot process");
            return null;
        }
        
        log.debug("Recursively processing KeyInfoReference referent");
        
        // Copy the existing CriteriaSet, excluding the KeyInfoCriteria, which is reset to the target.
        CriteriaSet newCriteria = new CriteriaSet();
        newCriteria.add(new KeyInfoCriteria((KeyInfo) target));
        for (Criteria crit : criteriaSet) {
            if (!(crit instanceof KeyInfoCriteria)) {
                newCriteria.add(crit);
            }
        }
        
        // Resolve the new target and copy the results into a collection to return.
        Iterable<Credential> creds = resolver.resolve(newCriteria);
        if (creds != null) {
            Collection<Credential> result = new ArrayList<Credential>();
            for (Credential c : creds) {
                result.add(c);
            }
            return result;
        }
        
        return null;
    }

    /**
     * Get the KeyInfoReference from the passed XML object.
     * 
     * @param xmlObject an XML object, presumably a {@link KeyInfoReference}
     * @return the KeyInfoReference which was found, or null if none or invalid
     */
    protected KeyInfoReference getKeyInfoReference(XMLObject xmlObject) {
        if (xmlObject instanceof KeyInfoReference) {
            KeyInfoReference ref = (KeyInfoReference) xmlObject;
            String uri = ref.getURI();
            if (uri != null && uri.startsWith("#")) {
                return ref;
            } else {
                log.debug("KeyInfoReference did not contain a same-document URI reference, cannot handle");
            }
        }
        return null;
    }
}