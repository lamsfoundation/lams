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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** A {@link Resource} whose contents may be run through a filter as it is being read. */
public abstract class AbstractFilteredResource implements Resource {
    
    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(AbstractFilteredResource.class);

    /** Associated resource filter. */
    private ResourceFilter resourceFilter;

    /** Constructor. */
    protected AbstractFilteredResource() {

    }

    /**
     * Constructor.
     * 
     * @param filter the filter used on the resource
     * 
     * @deprecated use {@link #setResourceFilter(ResourceFilter)} instead
     */
    protected AbstractFilteredResource(ResourceFilter filter) {
        resourceFilter = filter;
    }

    /**
     * Gets the resource filter associated with this resource.
     * 
     * @return resource filter associated with this resource
     */
    public ResourceFilter getResourceFilter() {
        return resourceFilter;
    }
    
    /**
     * Sets the resource filter associated with this resource.
     * 
     * @param filter filter associated with this resource
     */
    public void setResourceFilter(ResourceFilter filter){
        resourceFilter = filter;
    }

    /**
     * Applies the filter to the given stream resulting in the returned stream. If no filter is set than the given
     * stream is the returned stream.
     * 
     * @param stream the stream to filter
     * 
     * @return the filtered stream
     * 
     * @throws ResourceException thrown if the filter can not be applied to the stream
     */
    protected InputStream applyFilter(InputStream stream) throws ResourceException {
        ResourceFilter filter = getResourceFilter();
        if (filter != null) {
            log.debug("Apply filter '{}' to resource '{}'", filter.getClass(), this.getLocation());
            return getResourceFilter().applyFilter(stream);
        } else {
            return stream;
        }
    }
}