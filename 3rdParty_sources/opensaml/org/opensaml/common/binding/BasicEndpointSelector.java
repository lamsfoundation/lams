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

package org.opensaml.common.binding;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.opensaml.saml2.metadata.Endpoint;
import org.opensaml.saml2.metadata.IndexedEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This endpoint selector retrieves all the endpoints for a given role. A first filter pass removes those endpoints that
 * use bindings which are not supported by the issuer. If the remaining endpoints are not {@link IndexedEndpoint}s the
 * first endpoint in the list is returned. If the remaining endpoints are {@link IndexedEndpoint}s the first endpoint
 * with the isDefault attribute set to true is returned, if no isDefault attribute is set to true the first endpoint to
 * omit this attribute is returned, and if all the endpoints have the isDefault attribute set to false then the first
 * endpoint in the list is returned.
 * 
 * Prior to selecting the endpoint the following fields <strong>must</strong> have had values set: entity role,
 * endpoint type, issuer supported bindings.
 * 
 * While this algorithm with work for selecting the endpoint for responses to AuthnRequests the SAML
 * specification does stipulate additional endpoint selection criteria and as such the use of an endpoint selector
 * specifically meant to handler this situation should be used, for example: AuthnResponseEndpointSelector.
 */
public class BasicEndpointSelector extends AbstractEndpointSelector {
    
    /** Class logger. */
    private Logger log = LoggerFactory.getLogger(BasicEndpointSelector.class);

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public Endpoint selectEndpoint() {
        if(getEntityRoleMetadata() == null){
            return null;
        }
        
        List<? extends Endpoint> endpoints = getEntityRoleMetadata().getEndpoints(getEndpointType());
        if (endpoints == null || endpoints.size() == 0) {
            return null;
        }

        Endpoint selectedEndpoint;
        endpoints = filterEndpointsByProtocolBinding(endpoints);
        if (endpoints == null || endpoints.size() == 0) {
            return null;
        }
        if (endpoints.get(0) instanceof IndexedEndpoint) {
            selectedEndpoint = selectIndexedEndpoint((List<IndexedEndpoint>) endpoints);
        } else {
            selectedEndpoint = selectNonIndexedEndpoint((List<Endpoint>) endpoints);
        }
        
        log.debug("Selected endpoint {} for request", selectedEndpoint.getLocation());
        return selectedEndpoint;
    }
    
    /**
     * Filters the list of possible endpoints by supported outbound bindings.
     * 
     * @param endpoints raw list of endpoints
     * 
     * @return filtered endpoints
     */
    protected List<? extends Endpoint> filterEndpointsByProtocolBinding(List<? extends Endpoint> endpoints) {
        List<Endpoint> filteredEndpoints = new ArrayList<Endpoint>(endpoints);
        Iterator<Endpoint> endpointItr = filteredEndpoints.iterator();
        Endpoint endpoint;
        while (endpointItr.hasNext()) {
            endpoint = endpointItr.next();
            if (!getSupportedIssuerBindings().contains(endpoint.getBinding())) {
                endpointItr.remove();
                continue;
            }
        }

        return filteredEndpoints;
    }

    /**
     * Selects an appropriate endpoint from a list of indexed endpoints.
     * 
     * @param endpoints list of indexed endpoints
     * 
     * @return appropriate endpoint from a list of indexed endpoints or null
     */
    protected Endpoint selectIndexedEndpoint(List<IndexedEndpoint> endpoints) {
        List<IndexedEndpoint> endpointsCopy = new ArrayList<IndexedEndpoint>(endpoints);
        Iterator<IndexedEndpoint> endpointItr = endpointsCopy.iterator();
        IndexedEndpoint firstNoDefaultEndpoint = null;
        IndexedEndpoint currentEndpoint;
        while (endpointItr.hasNext()) {
            currentEndpoint = endpointItr.next();

            // endpoint is the default endpoint
            if (currentEndpoint.isDefault() != null) {
                if (currentEndpoint.isDefault()) {
                    return currentEndpoint;
                }

                if (firstNoDefaultEndpoint == null) {
                    firstNoDefaultEndpoint = currentEndpoint;
                }
            }
        }

        if (firstNoDefaultEndpoint != null) {
            // no endpoint was marked as the default, return first unmarked endpoint
            return firstNoDefaultEndpoint;
        } else {
            if (endpointsCopy.size() > 0) {
                // no endpoint had an index so return the first one
                return endpointsCopy.get(0);
            } else {
                // no endpoints made it through the supported binding filter
                return null;
            }
        }
    }

    /**
     * Selects an appropriate endpoint from a list of non-indexed endpoints.
     * 
     * @param endpoints list of non-indexed endpoints
     * 
     * @return appropriate endpoint from a list of non-indexed endpoints or null
     */
    protected Endpoint selectNonIndexedEndpoint(List<Endpoint> endpoints) {
        Iterator<Endpoint> endpointItr = endpoints.iterator();
        Endpoint endpoint;
        while (endpointItr.hasNext()) {
            endpoint = endpointItr.next();

            // Endpoint is first one of acceptable binding, return it.
            return endpoint;
        }

        // No endpoints had acceptable binding
        return null;
    }
}