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

package org.opensaml.xml.parse;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * A entity resolver that resolves an entity's location within the classpath.
 * 
 * Entity URIs <strong>must</strong> begin with the prefix <code>classpath:</code> and be followed by either an
 * absolute or relative classpath. Relative classpaths are relative to <strong>this</strong> class.
 * 
 * This resolver will <strong>not</strong> attempt to resolve any other URIs.
 */
public class ClasspathResolver implements EntityResolver, LSResourceResolver {

    /** UR scheme for classpath locations. */
    public static final String CLASSPATH_URI_SCHEME = "classpath:";

    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(ClasspathResolver.class);

    /** {@inheritDoc} */
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        InputStream resourceStream = resolver(publicId, systemId);
        if (resourceStream != null) {
            return new InputSource(resourceStream);
        }

        return null;
    }

    /** {@inheritDoc} */
    public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
        return new LSInputImpl(publicId, systemId, resolver(publicId, systemId));
    }

    /**
     * Resolves an id against the classpath. System ID is tried first, then public ID.
     * 
     * @param publicId resources public ID
     * @param systemId resources system ID
     * 
     * @return resolved resource or null
     */
    protected InputStream resolver(String publicId, String systemId) {
        String resource = null;
        InputStream resourceIns = null;

        if (systemId.startsWith(CLASSPATH_URI_SCHEME)) {
            log.trace("Attempting to resolve, within the classpath, the entity with the following system id: {}",
                    systemId);
            resource = systemId.replaceFirst("classpath:", "");
            resourceIns = getClass().getResourceAsStream(resource);
        }

        if (resourceIns == null && publicId != null && publicId.startsWith(CLASSPATH_URI_SCHEME)) {
            log.trace("Attempting to resolve, within the classpath, the entity with the following public id: {}",
                    resource);
            resource = publicId.replaceFirst("classpath:", "");
            resourceIns = getClass().getResourceAsStream(resource);
        }

        if (resourceIns == null) {
            log.trace("Entity was not resolved from classpath");
            return null;
        } else {
            log.trace("Entity resolved from classpath");
            return resourceIns;
        }
    }

    /**
     * Implementation of DOM 3 {@link LSInput}.
     */
    protected class LSInputImpl implements LSInput {

        /** Public ID of the resolved resource. */
        private String publicId;

        /** System ID of the resolved recource. */
        private String systemId;

        /** Resolved resource. */
        private BufferedInputStream buffInput;

        /**
         * Constructor.
         * 
         * @param pubId public id of the resolved resource
         * @param sysId system id of the resolved resource
         * @param input resolved resource
         */
        public LSInputImpl(String pubId, String sysId, InputStream input) {
            publicId = pubId;
            systemId = sysId;
            buffInput = new BufferedInputStream(input);
        }

        /** {@inheritDoc} */
        public String getBaseURI() {
            return null;
        }

        /** {@inheritDoc} */
        public InputStream getByteStream() {
            return buffInput;
        }

        /** {@inheritDoc} */
        public boolean getCertifiedText() {
            return false;
        }

        /** {@inheritDoc} */
        public Reader getCharacterStream() {
            return new InputStreamReader(buffInput);
        }

        /** {@inheritDoc} */
        public String getEncoding() {
            return null;
        }

        /** {@inheritDoc} */
        public String getPublicId() {
            return publicId;
        }

        /** {@inheritDoc} */
        public String getStringData() {
            synchronized (buffInput) {
                try {
                    buffInput.reset();
                    byte[] input = new byte[buffInput.available()];
                    buffInput.read(input);
                    return new String(input);
                } catch (IOException e) {
                    return null;
                }
            }
        }

        /** {@inheritDoc} */
        public String getSystemId() {
            return systemId;
        }

        /** {@inheritDoc} */
        public void setBaseURI(String uri) {
        }

        /** {@inheritDoc} */
        public void setByteStream(InputStream byteStream) {
        }

        /** {@inheritDoc} */
        public void setCertifiedText(boolean isCertifiedText) {
        }

        /** {@inheritDoc} */
        public void setCharacterStream(Reader characterStream) {
        }

        /** {@inheritDoc} */
        public void setEncoding(String encoding) {
        }

        /** {@inheritDoc} */
        public void setPublicId(String id) {
        }

        /** {@inheritDoc} */
        public void setStringData(String stringData) {
        }

        /** {@inheritDoc} */
        public void setSystemId(String id) {
        }
    }
}