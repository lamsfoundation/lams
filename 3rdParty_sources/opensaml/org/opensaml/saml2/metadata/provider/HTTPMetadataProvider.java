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

package org.opensaml.saml2.metadata.provider;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Timer;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A metadata provider that pulls metadata using an HTTP GET. Metadata is cached until one of these criteria is met:
 * <ul>
 * <li>The smallest cacheDuration within the metadata is exceeded</li>
 * <li>The earliest validUntil time within the metadata is exceeded</li>
 * <li>The maximum cache duration is exceeded</li>
 * </ul>
 * 
 * Metadata is filtered prior to determining the cache expiration data. This allows a filter to remove XMLObjects that
 * may effect the cache duration but for which the user of this provider does not care about.
 * 
 * It is the responsibility of the caller to re-initialize, via {@link #initialize()}, if any properties of this
 * provider are changed.
 */
public class HTTPMetadataProvider extends AbstractReloadingMetadataProvider {

    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(HTTPMetadataProvider.class);

    /** HTTP Client used to pull the metadata. */
    private HttpClient httpClient;

    /** URL to the Metadata. */
    private URI metadataURI;

    /** The ETag provided when the currently cached metadata was fetched. */
    private String cachedMetadataETag;

    /** The Last-Modified information provided when the currently cached metadata was fetched. */
    private String cachedMetadataLastModified;

    /** URL scope that requires authentication. */
    private AuthScope authScope;

    /**
     * Constructor.
     * 
     * @param metadataURL the URL to fetch the metadata
     * @param requestTimeout the time, in milliseconds, to wait for the metadata server to respond
     * 
     * @throws MetadataProviderException thrown if the URL is not a valid URL or the metadata can not be retrieved from
     *             the URL
     */
    @Deprecated
    public HTTPMetadataProvider(String metadataURL, int requestTimeout) throws MetadataProviderException {
        super();
        try {
            metadataURI = new URI(metadataURL);
        } catch (URISyntaxException e) {
            throw new MetadataProviderException("Illegal URL syntax", e);
        }

        HttpClientParams clientParams = new HttpClientParams();
        clientParams.setSoTimeout(requestTimeout);
        httpClient = new HttpClient(clientParams);
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(requestTimeout);
        authScope = new AuthScope(metadataURI.getHost(), metadataURI.getPort());

    }

    /**
     * Constructor.
     * 
     * @param client HTTP client used to pull in remote metadata
     * @param backgroundTaskTimer timer used to schedule background metadata refresh tasks
     * @param metadataURL URL to the remove remote metadata
     * 
     * @throws MetadataProviderException thrown if the HTTP client is null or the metadata URL provided is invalid
     */
    public HTTPMetadataProvider(Timer backgroundTaskTimer, HttpClient client, String metadataURL)
            throws MetadataProviderException {
        super(backgroundTaskTimer);

        if (client == null) {
            throw new MetadataProviderException("HTTP client may not be null");
        }
        httpClient = client;

        try {
            metadataURI = new URI(metadataURL);
        } catch (URISyntaxException e) {
            throw new MetadataProviderException("Illegal URL syntax", e);
        }

        authScope = new AuthScope(metadataURI.getHost(), metadataURI.getPort());
    }

    /**
     * Gets the URL to fetch the metadata.
     * 
     * @return the URL to fetch the metadata
     */
    public String getMetadataURI() {
        return metadataURI.toASCIIString();
    }

    /**
     * Sets the username and password used to access the metadata URL. To disable BASIC authentication set the username
     * and password to null;
     * 
     * @param username the username
     * @param password the password
     */
    public void setBasicCredentials(String username, String password) {
        if (username == null && password == null) {
            httpClient.getState().setCredentials(null, null);
        } else {
            UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
            httpClient.getState().setCredentials(authScope, credentials);
        }
    }

    /**
     * Gets the length of time in milliseconds to wait for the server to respond.
     * 
     * @return length of time in milliseconds to wait for the server to respond
     */
    public int getRequestTimeout() {
        return httpClient.getParams().getSoTimeout();
    }

    /**
     * Sets the socket factory used to create sockets to the HTTP server.
     * 
     * @see <a href="http://jakarta.apache.org/commons/httpclient/sslguide.html">HTTPClient SSL guide</a>
     * 
     * @param newSocketFactory the socket factory used to produce sockets used to connect to the server
     * 
     * @deprecated set this information on HTTP client used by provider
     */
    public void setSocketFactory(ProtocolSocketFactory newSocketFactory) {
        log.debug("Using the custom socket factory {} to connect to the HTTP server", newSocketFactory.getClass()
                .getName());
        Protocol protocol = new Protocol(metadataURI.getScheme(), newSocketFactory, metadataURI.getPort());
        httpClient.getHostConfiguration().setHost(metadataURI.getHost(), metadataURI.getPort(), protocol);
    }

    /**
     * Gets the maximum amount of time, in seconds, metadata will be cached for.
     * 
     * @return maximum amount of time, in seconds, metadata will be cached for
     * 
     * @deprecated use {@link #getMaxRefreshDelay()} instead
     */
    public int getMaxCacheDuration() {
        return (int) getMaxRefreshDelay();
    }

    /**
     * Sets the maximum amount of time, in seconds, metadata will be cached for.
     * 
     * @param newDuration maximum amount of time, in seconds, metadata will be cached for
     * 
     * @deprecated use {@link #setMaxRefreshDelay(long)} instead
     */
    public void setMaxCacheDuration(int newDuration) {
        setMaxRefreshDelay(newDuration * 1000);
    }

    /**
     * Gets whether cached metadata should be discarded if it expires and can not be refreshed.
     * 
     * @return whether cached metadata should be discarded if it expires and can not be refreshed.
     * 
     * @deprecated use {@link #requireValidMetadata()} instead
     */
    public boolean maintainExpiredMetadata() {
        return !requireValidMetadata();
    }

    /**
     * Sets whether cached metadata should be discarded if it expires and can not be refreshed.
     * 
     * @param maintain whether cached metadata should be discarded if it expires and can not be refreshed.
     * 
     * @deprecated use {@link #setRequireValidMetadata(boolean)} instead
     */
    public void setMaintainExpiredMetadata(boolean maintain) {
        setRequireValidMetadata(!maintain);
    }

    /** {@inheritDoc} */
    public synchronized void destroy() {
        httpClient = null;
        metadataURI = null;
        cachedMetadataETag = null;
        cachedMetadataLastModified = null;
        authScope = null;
        
        super.destroy();
    }
    
    /** {@inheritDoc} */
    protected String getMetadataIdentifier() {
        return metadataURI.toString();
    }

    /**
     * Gets the metadata document from the remote server.
     * 
     * @return the metadata from remote server, or null if the metadata document has not changed since the last
     *         retrieval
     * 
     * @throws MetadataProviderException thrown if there is a problem retrieving the metadata from the remote server
     */
    protected byte[] fetchMetadata() throws MetadataProviderException {
        GetMethod getMethod = buildGetMethod();

        try {
            log.debug("Attempting to fetch metadata document from '{}'", metadataURI);
            httpClient.executeMethod(getMethod);
            int httpStatus = getMethod.getStatusCode();

            if (httpStatus == HttpStatus.SC_NOT_MODIFIED) {
                log.debug("Metadata document from '{}' has not changed since last retrieval", getMetadataURI());
                return null;
            }

            if (getMethod.getStatusCode() != HttpStatus.SC_OK) {
                String errMsg = "Non-ok status code " + getMethod.getStatusCode()
                        + " returned from remote metadata source " + metadataURI;
                log.error(errMsg);
                throw new MetadataProviderException(errMsg);
            }

            processConditionalRetrievalHeaders(getMethod);

            byte[] rawMetadata = getMetadataBytesFromResponse(getMethod);
            log.debug("Successfully fetched {}bytes of metadata from {}", rawMetadata.length, getMetadataURI());

            return rawMetadata;
        } catch (IOException e) {
            String errMsg = "Error retrieving metadata from " + metadataURI;
            log.error(errMsg, e);
            throw new MetadataProviderException(errMsg, e);
        }finally{
            getMethod.releaseConnection();
        }
    }

    /**
     * Builds the HTTP GET method used to fetch the metadata. The returned method advertises support for GZIP and
     * deflate compression, enables conditional GETs if the cached metadata came with either an ETag or Last-Modified
     * information, and sets up basic authentication if such is configured.
     * 
     * @return the constructed GET method
     */
    protected GetMethod buildGetMethod() {
        GetMethod getMethod = new GetMethod(getMetadataURI());
        getMethod.addRequestHeader("Connection", "close");

        getMethod.setRequestHeader("Accept-Encoding", "gzip,deflate");
        if (cachedMetadataETag != null) {
            getMethod.setRequestHeader("If-None-Match", cachedMetadataETag);
        }
        if (cachedMetadataLastModified != null) {
            getMethod.setRequestHeader("If-Modified-Since", cachedMetadataLastModified);
        }

        if (httpClient.getState().getCredentials(authScope) != null) {
            log.debug("Using BASIC authentication when retrieving metadata from '{}", metadataURI);
            getMethod.setDoAuthentication(true);
        }

        return getMethod;
    }

    /**
     * Records the ETag and Last-Modified headers, from the response, if they are present.
     * 
     * @param getMethod GetMethod containing a valid HTTP response
     */
    protected void processConditionalRetrievalHeaders(GetMethod getMethod) {
        Header httpHeader = getMethod.getResponseHeader("ETag");
        if (httpHeader != null) {
            cachedMetadataETag = httpHeader.getValue();
        }

        httpHeader = getMethod.getResponseHeader("Last-Modified");
        if (httpHeader != null) {
            cachedMetadataLastModified = httpHeader.getValue();
        }
    }

    /**
     * Extracts the raw metadata bytes from the response taking in to account possible deflate and GZip compression.
     * 
     * @param getMethod GetMethod containing a valid HTTP response
     * 
     * @return the raw metadata bytes
     * 
     * @throws MetadataProviderException thrown if there is a problem getting the raw metadata bytes from the response
     */
    protected byte[] getMetadataBytesFromResponse(GetMethod getMethod) throws MetadataProviderException {
        log.debug("Attempting to extract metadata from response to request for metadata from '{}'", getMetadataURI());
        try {
            InputStream ins = getMethod.getResponseBodyAsStream();

            Header httpHeader = getMethod.getResponseHeader("Content-Encoding");
            if (httpHeader != null) {
                String contentEncoding = httpHeader.getValue();
                if ("deflate".equalsIgnoreCase(contentEncoding)) {
                    log.debug("Metadata document from '{}' was deflate compressed, decompressing it", metadataURI);
                    ins = new InflaterInputStream(ins);
                }

                if ("gzip".equalsIgnoreCase(contentEncoding)) {
                    log.debug("Metadata document from '{}' was GZip compressed, decompressing it", metadataURI);
                    ins = new GZIPInputStream(ins);
                }
            }

            return inputstreamToByteArray(ins);
        } catch (IOException e) {
            log.error("Unable to read response", e);
            throw new MetadataProviderException("Unable to read response", e);
        }
    }
}