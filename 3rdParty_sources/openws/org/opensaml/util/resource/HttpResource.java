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

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.util.DateParseException;
import org.apache.commons.httpclient.util.DateUtil;
import org.joda.time.DateTime;
import org.joda.time.chrono.ISOChronology;
import org.opensaml.xml.util.DatatypeHelper;

/**
 * A resource representing a file retrieved from a URL using Apache Commons HTTPClient.
 */
public class HttpResource extends AbstractFilteredResource {
    
    /** HttpClient connection timeout in milliseconds. */
    private static final int CONNECTION_TIMEOUT = 90*1000;
    
    /** HttpClient socket timeout in milliseconds. */
    private static final int SOCKET_TIMEOUT = 90*1000;

    /** HTTP URL of the resource. */
    private String resourceUrl;

    /** HTTP client. */
    private HttpClient httpClient;

    /**
     * Constructor.
     * 
     * @param resource HTTP(S) URL of the resource
     */
    public HttpResource(String resource) {
        super();

        resourceUrl = DatatypeHelper.safeTrimOrNullString(resource);
        if (resourceUrl == null) {
            throw new IllegalArgumentException("Resource URL may not be null or empty");
        }

        httpClient = new HttpClient();
        
        HttpConnectionManagerParams connMgrParams = httpClient.getHttpConnectionManager().getParams();
        connMgrParams.setConnectionTimeout(CONNECTION_TIMEOUT);
        connMgrParams.setSoTimeout(SOCKET_TIMEOUT);
    }

    /**
     * Constructor.
     * 
     * @param resource HTTP(S) URL of the resource
     * @param resourceFilter filter to apply to this resource
     * 
     * @deprecated use {@link #setResourceFilter(ResourceFilter)} instead
     */
    public HttpResource(String resource, ResourceFilter resourceFilter) {
        super(resourceFilter);

        resourceUrl = DatatypeHelper.safeTrimOrNullString(resource);
        if (resourceUrl == null) {
            throw new IllegalArgumentException("Resource URL may not be null or empty");
        }

        httpClient = new HttpClient();
        
        HttpConnectionManagerParams connMgrParams = httpClient.getHttpConnectionManager().getParams();
        connMgrParams.setConnectionTimeout(CONNECTION_TIMEOUT);
        connMgrParams.setSoTimeout(SOCKET_TIMEOUT);
    }

    /** {@inheritDoc} */
    public boolean exists() throws ResourceException {
        HeadMethod headMethod = new HeadMethod(resourceUrl);
        headMethod.addRequestHeader("Connection", "close");

        try {
            httpClient.executeMethod(headMethod);
            if (headMethod.getStatusCode() != HttpStatus.SC_OK) {
                return false;
            }

            return true;
        } catch (IOException e) {
            throw new ResourceException("Unable to contact resource URL: " + resourceUrl, e);
        } finally {
            headMethod.releaseConnection();
        }
    }

    /** {@inheritDoc} */
    public InputStream getInputStream() throws ResourceException {
        GetMethod getMethod = getResource();
        try {
            return new ConnectionClosingInputStream(getMethod, applyFilter(getMethod.getResponseBodyAsStream()));
        } catch (IOException e) {
            throw new ResourceException("Unable to read response", e);
        }
    }

    /** {@inheritDoc} */
    public DateTime getLastModifiedTime() throws ResourceException {
        HeadMethod headMethod = new HeadMethod(resourceUrl);
        headMethod.addRequestHeader("Connection", "close");

        try {
            httpClient.executeMethod(headMethod);
            if (headMethod.getStatusCode() != HttpStatus.SC_OK) {
                throw new ResourceException("Unable to retrieve resource URL " + resourceUrl
                        + ", received HTTP status code " + headMethod.getStatusCode());
            }
            Header lastModifiedHeader = headMethod.getResponseHeader("Last-Modified");
            if (lastModifiedHeader != null && !DatatypeHelper.isEmpty(lastModifiedHeader.getValue())) {
                long lastModifiedTime = DateUtil.parseDate(lastModifiedHeader.getValue()).getTime();
                return new DateTime(lastModifiedTime, ISOChronology.getInstanceUTC());
            }

            return new DateTime();
        } catch (IOException e) {
            throw new ResourceException("Unable to contact resource URL: " + resourceUrl, e);
        } catch (DateParseException e) {
            throw new ResourceException("Unable to parse last modified date for resource:" + resourceUrl, e);
        } finally {
            headMethod.releaseConnection();
        }
    }

    /** {@inheritDoc} */
    public String getLocation() {
        return resourceUrl;
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

        if (o instanceof HttpResource) {
            return getLocation().equals(((HttpResource) o).getLocation());
        }

        return false;
    }

    /**
     * Gets remote resource.
     * 
     * @return the remove resource
     * 
     * @throws ResourceException thrown if the resource could not be fetched
     */
    protected GetMethod getResource() throws ResourceException {
        GetMethod getMethod = new GetMethod(resourceUrl);
        getMethod.addRequestHeader("Connection", "close");

        try {
            httpClient.executeMethod(getMethod);
            if (getMethod.getStatusCode() != HttpStatus.SC_OK) {
                throw new ResourceException("Unable to retrieve resource URL " + resourceUrl
                        + ", received HTTP status code " + getMethod.getStatusCode());
            }
            return getMethod;
        } catch (IOException e) {
            throw new ResourceException("Unable to contact resource URL: " + resourceUrl, e);
        }
    }

    /**
     * A wrapper around the {@link InputStream} returned by a {@link HttpMethod} that closes the stream and releases the
     * HTTP connection when {@link #close()} is invoked.
     */
    private static class ConnectionClosingInputStream extends InputStream {

        /** HTTP method that was invoked. */
        private final HttpMethod method;

        /** Stream owned by the given HTTP method. */
        private final InputStream stream;

        /**
         * Constructor.
         *
         * @param httpMethod HTTP method that was invoked
         * @param returnedStream stream owned by the given HTTP method
         */
        public ConnectionClosingInputStream(HttpMethod httpMethod, InputStream returnedStream) {
            method = httpMethod;
            stream = returnedStream;
        }

        /** {@inheritDoc} */
        public int available() throws IOException {
            return stream.available();
        }

        /** {@inheritDoc} */
        public void close() throws IOException {
            stream.close();
            method.releaseConnection();
        }

        /** {@inheritDoc} */
        public void mark(int readLimit) {
            stream.mark(readLimit);
        }

        /** {@inheritDoc} */
        public boolean markSupported() {
            return stream.markSupported();
        }

        /** {@inheritDoc} */
        public int read() throws IOException {
            return stream.read();
        }

        /** {@inheritDoc} */
        public int read(byte[] b) throws IOException {
            return stream.read(b);
        }

        /** {@inheritDoc} */
        public int read(byte[] b, int off, int len) throws IOException {
            return stream.read(b, off, len);
        }

        /** {@inheritDoc} */
        public synchronized void reset() throws IOException {
            stream.reset();
        }

        /** {@inheritDoc} */
        public long skip(long n) throws IOException {
            return stream.skip(n);
        }
    }
}