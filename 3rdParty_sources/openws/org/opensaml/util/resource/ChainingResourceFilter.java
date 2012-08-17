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

package org.opensaml.util.resource;

import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Resource filter that executes a list of resource filters in order. */
public class ChainingResourceFilter implements ResourceFilter {

    /** Class logger. */
    private Logger log = LoggerFactory.getLogger(ChainingResourceFilter.class);
    
    /** Registered resource filters. */
    private List<ResourceFilter> resourceFilters;

    /**
     * Constructor.
     * 
     * @param filters resource filters to execute in order
     */
    public ChainingResourceFilter(List<ResourceFilter> filters) {
        resourceFilters = filters;
    }

    /** {@inheritDoc} */
    public InputStream applyFilter(InputStream resource) throws ResourceException {
        if (resourceFilters == null || resourceFilters.isEmpty()) {
            log.debug("No resource filters configured, nothing to do");
            return resource;
        }

        for (ResourceFilter filter : resourceFilters) {
            log.debug("Applying filter '{}'", filter.getClass().getName());
            resource = filter.applyFilter(resource);
        }

        return resource;
    }
}