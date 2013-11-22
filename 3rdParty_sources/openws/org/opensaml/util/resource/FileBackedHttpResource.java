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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.apache.commons.httpclient.methods.GetMethod;
import org.joda.time.DateTime;
import org.joda.time.chrono.ISOChronology;
import org.opensaml.xml.util.DatatypeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A resource representing a file read from an HTTP(S) location. Every time the file is successfully read from the URL
 * location it is written to a backing file. If the file can not be read from the URL it is read from this backing file,
 * if available.
 * 
 * Note, large files should not be accessed in this manner as the entire file is read into memory before being written
 * to disk and then returned.
 */
public class FileBackedHttpResource extends HttpResource {
    
    /** Logger. */
    private Logger log = LoggerFactory.getLogger(FileBackedHttpResource.class);

    /** Backing resource file. */
    private File resourceFile;

    /**
     * Constructor.
     * 
     * @param resource HTTP(S) URL of the resource
     * @param backingFile filesystem location to store the resource
     */
    public FileBackedHttpResource(String resource, String backingFile) {
        super(resource);

        if (DatatypeHelper.isEmpty(backingFile)) {
            throw new IllegalArgumentException("Backing file path may not be null or empty");
        }

        resourceFile = new File(backingFile);
    }

    /**
     * Constructor.
     * 
     * @param resource HTTP(S) URL of the resource
     * @param backingFile file: URI location to store the resource
     * 
     * @since 1.2
     */
    public FileBackedHttpResource(String resource, URI backingFile) {
        super(resource);

        if (backingFile == null) {
            throw new IllegalArgumentException("Backing file path may not be null or empty");
        }

        resourceFile = new File(backingFile);
    }

    /**
     * Constructor.
     * 
     * @param resource HTTP(S) URL of the resource
     * @param backingFile filesystem location to store the resource
     * @param resourceFilter filter to apply to this resource
     * 
     * @deprecated use {@link #setResourceFilter(ResourceFilter)} instead
     */
    public FileBackedHttpResource(String resource, String backingFile, ResourceFilter resourceFilter) {
        super(resource, resourceFilter);

        if (DatatypeHelper.isEmpty(backingFile)) {
            throw new IllegalArgumentException("Backing file path may not be null or empty");
        }

        resourceFile = new File(backingFile);
    }

    /**
     * Constructor.
     * 
     * @param resource HTTP(S) URL of the resource
     * @param backingFile filesystem location to store the resource
     * @param resourceFilter filter to apply to this resource
     * 
     * @since 1.2
     * @deprecated use {@link #setResourceFilter(ResourceFilter)} instead
     */
    public FileBackedHttpResource(String resource, URI backingFile, ResourceFilter resourceFilter) {
        super(resource, resourceFilter);

        if (backingFile == null) {
            throw new IllegalArgumentException("Backing file path may not be null or empty");
        }

        resourceFile = new File(backingFile);
    }

    /** {@inheritDoc} */
    public boolean exists() throws ResourceException {
        boolean httpExists;
        try {
            httpExists = super.exists();
        } catch (ResourceException e) {
            log.warn("HTTP resource '{}' was inaccessible for exists(), trying backing file '{}'", 
                    getLocation(), resourceFile.getAbsolutePath());
            return resourceFile.exists();
        }
        
        if (httpExists) {
            return true;
        } else {
            log.warn("HTTP resource '{}' did not exist, trying backing file '{}'",
                    getLocation(), resourceFile.getAbsolutePath());
            return resourceFile.exists();
        }

    }

    /** {@inheritDoc} */
    public InputStream getInputStream() throws ResourceException {
        InputStream ins = null;
        try {
            GetMethod getMethod = super.getResource();
            byte[] response = getMethod.getResponseBody();
            saveToResourceFile(response);
            ins = getMethod.getResponseBodyAsStream();
        } catch (Exception e) {
            try {
                log.warn("HTTP resource '{}' was inaccessible for getInputStream(), trying backing file '{}'", 
                        getLocation(), resourceFile.getAbsolutePath());
                ins = new FileInputStream(resourceFile);
            } catch (IOException ioe) {
                throw new ResourceException("Unable to read resource URL or backing file "
                        + resourceFile.getAbsolutePath(), ioe);
            }
        }

        return applyFilter(ins);
    }

    /** {@inheritDoc} */
    public DateTime getLastModifiedTime() throws ResourceException {
        try {
            return super.getLastModifiedTime();
        } catch (ResourceException e) {
            log.warn("HTTP resource '{}' was inaccessible for getLastModifiedTime(), trying backing file '{}'", 
                    getLocation(), resourceFile.getAbsolutePath());
            long lastModifiedTime = resourceFile.lastModified();
            if (lastModifiedTime == 0) {
                throw new ResourceException("URL resource is not reachable and backing file is not readable");
            }

            return new DateTime(lastModifiedTime, ISOChronology.getInstanceUTC());
        }
    }

    /** {@inheritDoc} */
    public String getLocation() {
        return super.getLocation();
    }

    /**
     * Saves a resource to the backing file.
     * 
     * @param resource the string representation of the resource
     * 
     * @throws ResourceException thrown if the resource backing file can not be written to
     */
    protected void saveToResourceFile(byte[] resource) throws ResourceException {
        try {
            FileOutputStream out = new FileOutputStream(resourceFile);
            out.write(resource);
        } catch (IOException e) {
            throw new ResourceException("Unable to write resource to backing file " + resourceFile.getAbsolutePath(), e);
        }
    }
}