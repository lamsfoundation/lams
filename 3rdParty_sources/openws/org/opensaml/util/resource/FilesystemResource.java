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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;

import org.joda.time.DateTime;
import org.joda.time.chrono.ISOChronology;
import org.opensaml.xml.util.DatatypeHelper;

/**
 * A resource representing a file on the local filesystem.
 */
public class FilesystemResource extends AbstractFilteredResource {

    /** The file represented by this resource. */
    private File resource;

    /**
     * Constructor.
     * 
     * @param resourcePath the path to the file for this resource
     * 
     * @throws ResourceException thrown if the resource path is null or empty
     */
    public FilesystemResource(String resourcePath) throws ResourceException {
        super();

        if (DatatypeHelper.isEmpty(resourcePath)) {
            throw new ResourceException("Resource path may not be null or empty");
        }

        resource = new File(resourcePath);
    }

    /**
     * Constructor.
     * 
     * @param resourceURI file: URI to the file
     * 
     * @throws ResourceException thrown if the resource path is null or empty
     * 
     * @since 1.2.0
     */
    public FilesystemResource(URI resourceURI) throws ResourceException {
        super();

        if (resourceURI == null) {
            throw new ResourceException("Resource URL may not be null");
        }

        resource = new File(resourceURI);
    }

    /**
     * Constructor.
     * 
     * @param resourcePath the path to the file for this resource
     * @param resourceFilter filter to apply to this resource
     * 
     * @throws ResourceException thrown if the resource path is null or empty
     * 
     * @deprecated use {@link #setResourceFilter(ResourceFilter)} instead
     */
    public FilesystemResource(String resourcePath, ResourceFilter resourceFilter) throws ResourceException {
        super(resourceFilter);

        if (DatatypeHelper.isEmpty(resourcePath)) {
            throw new ResourceException("Resource path may not be null or empty");
        }

        resource = new File(resourcePath);
    }

    /**
     * Constructor.
     * 
     * @param resourceURI the file: URI to the file for this resource
     * @param resourceFilter filter to apply to this resource
     * 
     * @throws ResourceException thrown if the resource path is null or empty
     * 
     * @since 1.2.0
     * @deprecated use {@link #setResourceFilter(ResourceFilter)} instead
     */
    public FilesystemResource(URI resourceURI, ResourceFilter resourceFilter) throws ResourceException {
        super(resourceFilter);

        if (resourceURI == null) {
            throw new ResourceException("Resource URI may not be null");
        }

        resource = new File(resourceURI);
    }

    /** {@inheritDoc} */
    public boolean exists() throws ResourceException {
        return resource.exists();
    }

    /** {@inheritDoc} */
    public InputStream getInputStream() throws ResourceException {
        try {
            FileInputStream ins = new FileInputStream(resource);
            return applyFilter(ins);
        } catch (FileNotFoundException e) {
            throw new ResourceException("Resource file does not exist: " + resource.getAbsolutePath());
        }
    }

    /** {@inheritDoc} */
    public DateTime getLastModifiedTime() throws ResourceException {
        if (!resource.exists()) {
            throw new ResourceException("Resource file does not exist: " + resource.getAbsolutePath());
        }

        return new DateTime(resource.lastModified(), ISOChronology.getInstanceUTC());
    }

    /** {@inheritDoc} */
    public String getLocation() {
        return resource.getAbsolutePath();
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

        if (o instanceof FilesystemResource) {
            return getLocation().equals(((FilesystemResource) o).getLocation());
        }

        return false;
    }
}