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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.joda.time.DateTime;
import org.opensaml.xml.util.DatatypeHelper;

/**
 * Resource that represents a resource found on the classpath.
 * 
 * Because object on the classpath are not meant to change during runtime the last modification is set to the time the
 * {@link ClasspathResource} is created and is never changed.
 */
public class ClasspathResource extends AbstractFilteredResource {

    /** Classpath location of resource. */
    private URL resource;

    /** Last modification time, set to when resources is created. */
    private DateTime lastModTime;

    /**
     * Constructor.
     * 
     * @param path the path to the file for this resource
     * 
     * @throws ResourceException thrown if the resource path is null or empty or if the resource does not exist
     */
    public ClasspathResource(String path) throws ResourceException {
        super();
        
        if (DatatypeHelper.isEmpty(path)) {
            throw new ResourceException("Resource path may not be null or empty");
        }

        resource = getClass().getResource(path);
        if (resource == null) {
            throw new ResourceException("Classpath resource does not exist: " + path);
        }

        lastModTime = new DateTime();
    }
    
    /**
     * Constructor.
     * 
     * @param path the path to the file for this resource
     * @param resourceFilter filter to apply to this resource
     * 
     * @throws ResourceException thrown if the resource path is null or empty or if the resource does not exist
     * 
     * @deprecated use {@link #setResourceFilter(ResourceFilter)} instead
     */
    public ClasspathResource(String path, ResourceFilter resourceFilter) throws ResourceException {
        super(resourceFilter);
        
        if (DatatypeHelper.isEmpty(path)) {
            throw new ResourceException("Resource path may not be null or empty");
        }

        resource = getClass().getResource(path);
        if (resource == null) {
            throw new ResourceException("Classpath resource does not exist: " + path);
        }

        lastModTime = new DateTime();
    }

    /** {@inheritDoc} */
    public boolean exists() throws ResourceException {
        if (resource != null) {
            return true;
        }

        return false;
    }

    /** {@inheritDoc} */
    public InputStream getInputStream() throws ResourceException {
        try {
            InputStream ins = resource.openStream();
            return applyFilter(ins);
        } catch (IOException e) {
            throw new ResourceException("Unable to open resource: " + resource);
        }
    }

    /** {@inheritDoc} */
    public DateTime getLastModifiedTime() throws ResourceException {
        return lastModTime;
    }

    /** {@inheritDoc} */
    public String getLocation() {
        return resource.toString();
    }

    /** {@inheritDoc} */
    public String toString() {
        return getLocation();
    }

    /** {@inheritDoc} */
    public int hashCode() {
        return getLocation().hashCode();
    }

    /** {@inheritDoc} */
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (o instanceof ClasspathResource) {
            return getLocation().equals(((ClasspathResource) o).getLocation());
        }

        return false;
    }
}