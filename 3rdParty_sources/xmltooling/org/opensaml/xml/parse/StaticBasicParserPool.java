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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.Map;
import java.util.Stack;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;

import org.opensaml.xml.Configuration;
import org.opensaml.xml.util.LazyMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * A pool of JAXP 1.3 {@link DocumentBuilder}s.
 * 
 * <p>This implementation of {@link ParserPool} does not allow its properties to be modified once
 * it has been initialized.  For an implementation that does support versioned properties over
 * time, see {@link BasicParserPool}.</p>
 * 
 * <p>This is a pool implementation of the caching factory variety, and as such imposes no upper bound 
 * on the number of DocumentBuilders allowed to be concurrently checked out and in use. It does however
 * impose a limit on the size of the internal cache of idle builder instances via the value configured 
 * via {@link #setMaxPoolSize(int)}.</p>
 * 
 * <p>Builders retrieved from this pool may (but are not required to) be returned to the pool with the method
 * {@link #returnBuilder(DocumentBuilder)}.</p>
 * 
 * <p>References to builders are kept by way of {@link SoftReference} so that the garbage collector 
 * may reap the builders if the system is running out of memory.</p>
 */
public class StaticBasicParserPool implements ParserPool {

    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(StaticBasicParserPool.class);
    
    /** Flag to track whether pool is in the initialized state. */
    private boolean initialized;

    /** Factory used to create new builders. */
    private DocumentBuilderFactory builderFactory;

    /** Cache of document builders. */
    private Stack<SoftReference<DocumentBuilder>> builderPool;

    /** Max number of builders allowed in the pool. Default value: 5 */
    private int maxPoolSize;

    /** Builder attributes. */
    private Map<String, Object> builderAttributes;

    /** Whether the builders are coalescing. Default value: true */
    private boolean coalescing;

    /** Whether the builders expand entity references. Default value: true */
    private boolean expandEntityReferences;

    /** Builder features. */
    private Map<String, Boolean> builderFeatures;

    /** Whether the builders ignore comments. Default value: true */
    private boolean ignoreComments;

    /** Whether the builders ignore element content whitespace. Default value: true */
    private boolean ignoreElementContentWhitespace;

    /** Whether the builders are namespace aware. Default value: true */
    private boolean namespaceAware;

    /** Schema used to validate parsed content. */
    private Schema schema;

    /** Whether the builder should validate. Default value: false */
    private boolean dtdValidating;

    /** Whether the builders are XInclude aware. Default value: false */
    private boolean xincludeAware;

    /** Entity resolver used by builders. */
    private EntityResolver entityResolver;

    /** Error handler used by builders. */
    private ErrorHandler errorHandler;

    /** Constructor. */
    public StaticBasicParserPool() {
        initialized = false;
        maxPoolSize = 5;
        builderPool = new Stack<SoftReference<DocumentBuilder>>();
        builderAttributes = new LazyMap<String, Object>();
        coalescing = true;
        expandEntityReferences = true;
        builderFeatures = new LazyMap<String, Boolean>();
        ignoreComments = true;
        ignoreElementContentWhitespace = true;
        namespaceAware = true;
        schema = null;
        dtdValidating = false;
        xincludeAware = false;
        errorHandler = new LoggingErrorHandler(log);

    }
    
    /** 
     * Initialize the pool. 
     * 
     * @throws XMLParserException thrown if pool can not be initialized,
     *        or if it is already initialized
     * 
     **/
    public synchronized void initialize() throws XMLParserException {
        if (initialized) {
            throw new XMLParserException("Parser pool was already initialized");
        }
        initializeFactory();
        initialized = true;
    }
    
    /**
     * Return initialized state. 
     * 
     * @return true if pool is initialized, false otherwise
     */
    public synchronized boolean isInitialized() {
        return initialized;
    }

    /** {@inheritDoc} */
    public DocumentBuilder getBuilder() throws XMLParserException {
        DocumentBuilder builder = null;
        
        if (!initialized) {
            throw new XMLParserException("Parser pool has not been initialized");
        }
        
        synchronized(builderPool) {
            if (!builderPool.isEmpty()) {
                builder = builderPool.pop().get();
            }
        }
        
        // Will be null if either the stack was empty, or the SoftReference
        // has been garbage-collected
        if (builder == null) {
            builder = createBuilder();
        }

        if (builder != null) {
            return new DocumentBuilderProxy(builder, this);
        }

        return null;
    }

    /** {@inheritDoc} */
    public void returnBuilder(DocumentBuilder builder) {
        if (!(builder instanceof DocumentBuilderProxy)) {
            return;
        }
        
        DocumentBuilderProxy proxiedBuilder = (DocumentBuilderProxy) builder;
        if (proxiedBuilder.getOwningPool() != this) {
            return;
        }
        
        synchronized (proxiedBuilder) {
            if (proxiedBuilder.isReturned()) {
                return;
            }
            // Not strictly true in that it may not actually be pushed back
            // into the cache, depending on builderPool.size() below.  But 
            // that's ok.  returnBuilder() shouldn't normally be called twice
            // on the same builder instance anyway, and it also doesn't matter
            // whether a builder is ever logically returned to the pool.
            proxiedBuilder.setReturned(true);
        }
        
        DocumentBuilder unwrappedBuilder = proxiedBuilder.getProxiedBuilder();
        unwrappedBuilder.reset();
        SoftReference<DocumentBuilder> builderReference = new SoftReference<DocumentBuilder>(unwrappedBuilder);
        
        synchronized(builderPool) {
            if (builderPool.size() < maxPoolSize) {
                builderPool.push(builderReference);
            }
        }
    }

    /** {@inheritDoc} */
    public Document newDocument() throws XMLParserException {
        DocumentBuilder builder = getBuilder();
        Document document = builder.newDocument();
        returnBuilder(builder);
        return document;
    }

    /** {@inheritDoc} */
    public Document parse(InputStream input) throws XMLParserException {
        DocumentBuilder builder = getBuilder();
        try {
            Document document = builder.parse(input);
            return document;
        } catch (SAXException e) {
            throw new XMLParserException("Invalid XML", e);
        } catch (IOException e) {
            throw new XMLParserException("Unable to read XML from input stream", e);
        } finally {
            returnBuilder(builder);
        }
    }

    /** {@inheritDoc} */
    public Document parse(Reader input) throws XMLParserException {
        DocumentBuilder builder = getBuilder();
        try {
            Document document = builder.parse(new InputSource(input));
            return document;
        } catch (SAXException e) {
            throw new XMLParserException("Invalid XML", e);
        } catch (IOException e) {
            throw new XMLParserException("Unable to read XML from input stream", e);
        } finally {
            returnBuilder(builder);
        }
    }

    /**
     * Gets the max number of builders the pool will hold.
     * 
     * @return max number of builders the pool will hold
     */
    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    /**
     * Sets the max number of builders the pool will hold.
     * 
     * @param newSize max number of builders the pool will hold
     */
    public void setMaxPoolSize(int newSize) {
        checkValidModifyState();
        maxPoolSize = newSize;
    }

    /**
     * Gets the builder attributes used when creating builders. This collection is unmodifiable.
     * 
     * @return builder attributes used when creating builders
     */
    public Map<String, Object> getBuilderAttributes() {
        return Collections.unmodifiableMap(builderAttributes);
    }

    /**
     * Sets the builder attributes used when creating builders.
     * 
     * @param newAttributes builder attributes used when creating builders
     */
    public void setBuilderAttributes(Map<String, Object> newAttributes) {
        checkValidModifyState();
        builderAttributes = newAttributes;
    }

    /**
     * Gets whether the builders are coalescing.
     * 
     * @return whether the builders are coalescing
     */
    public boolean isCoalescing() {
        return coalescing;
    }

    /**
     * Sets whether the builders are coalescing.
     * 
     * @param isCoalescing whether the builders are coalescing
     */
    public void setCoalescing(boolean isCoalescing) {
        checkValidModifyState();
        coalescing = isCoalescing;
    }

    /**
     * Gets whether builders expand entity references.
     * 
     * @return whether builders expand entity references
     */
    public boolean isExpandEntityReferences() {
        return expandEntityReferences;
    }

    /**
     * Sets whether builders expand entity references.
     * 
     * @param expand whether builders expand entity references
     */
    public void setExpandEntityReferences(boolean expand) {
        checkValidModifyState();
        expandEntityReferences = expand;
    }

    /**
     * Gets the builders' features. This collection is unmodifiable.
     * 
     * @return the builders' features
     */
    public Map<String, Boolean> getBuilderFeatures() {
        return Collections.unmodifiableMap(builderFeatures);
    }

    /**
     * Sets the the builders' features.
     * 
     * @param newFeatures the builders' features
     */
    public void setBuilderFeatures(Map<String, Boolean> newFeatures) {
        checkValidModifyState();
        builderFeatures = newFeatures;
    }

    /**
     * Gets whether the builders ignore comments.
     * 
     * @return whether the builders ignore comments
     */
    public boolean getIgnoreComments() {
        return ignoreComments;
    }

    /**
     * Sets whether the builders ignore comments.
     * 
     * @param ignore The ignoreComments to set.
     */
    public void setIgnoreComments(boolean ignore) {
        checkValidModifyState();
        ignoreComments = ignore;
    }

    /**
     * Get whether the builders ignore element content whitespace.
     * 
     * @return whether the builders ignore element content whitespace
     */
    public boolean isIgnoreElementContentWhitespace() {
        return ignoreElementContentWhitespace;
    }

    /**
     * Sets whether the builders ignore element content whitespace.
     * 
     * @param ignore whether the builders ignore element content whitespace
     */
    public void setIgnoreElementContentWhitespace(boolean ignore) {
        checkValidModifyState();
        ignoreElementContentWhitespace = ignore;
    }

    /**
     * Gets whether the builders are namespace aware.
     * 
     * @return whether the builders are namespace aware
     */
    public boolean isNamespaceAware() {
        return namespaceAware;
    }

    /**
     * Sets whether the builders are namespace aware.
     * 
     * @param isNamespaceAware whether the builders are namespace aware
     */
    public void setNamespaceAware(boolean isNamespaceAware) {
        checkValidModifyState();
        namespaceAware = isNamespaceAware;
    }

    /** {@inheritDoc} */
    public Schema getSchema() {
        return schema;
    }

    /** {@inheritDoc} */
    public synchronized void setSchema(Schema newSchema) {
        // Note this method is synchronized because it is more than just an atomic assignment.
        // Don't want inconsistent data in the factory via initializeFactory(), also synchronized.
        checkValidModifyState();
        schema = newSchema;
        if (schema != null) {
            setNamespaceAware(true);
            builderAttributes.remove("http://java.sun.com/xml/jaxp/properties/schemaSource");
            builderAttributes.remove("http://java.sun.com/xml/jaxp/properties/schemaLanguage");
        }
    }

    /**
     * Gets whether the builders are validating.
     * 
     * @return whether the builders are validating
     */
    public boolean isDTDValidating() {
        return dtdValidating;
    }

    /**
     * Sets whether the builders are validating.
     * 
     * @param isValidating whether the builders are validating
     */
    public void setDTDValidating(boolean isValidating) {
        checkValidModifyState();
        dtdValidating = isValidating;
    }

    /**
     * Gets whether the builders are XInclude aware.
     * 
     * @return whether the builders are XInclude aware
     */
    public boolean isXincludeAware() {
        return xincludeAware;
    }

    /**
     * Sets whether the builders are XInclude aware.
     * 
     * @param isXIncludeAware whether the builders are XInclude aware
     */
    public void setXincludeAware(boolean isXIncludeAware) {
        checkValidModifyState();
        xincludeAware = isXIncludeAware;
    }

    /**
     * Gets the size of the current pool storage.
     * 
     * @return current pool storage size
     */
    protected int getPoolSize() {
        return builderPool.size();
    }
    
    /**
     * Check that pool is in a valid state to be modified.
     */
    protected void checkValidModifyState() {
        if (initialized) {
            throw new IllegalStateException("Pool is already initialized, property changes not allowed");
        }
    }

    /**
     * Initializes the pool with a new set of configuration options.
     * 
     * @throws XMLParserException thrown if there is a problem initialzing the pool
     */
    protected synchronized void initializeFactory() throws XMLParserException {
        DocumentBuilderFactory newFactory = DocumentBuilderFactory.newInstance();
        setAttributes(newFactory, builderAttributes);
        setFeatures(newFactory, builderFeatures);
        newFactory.setCoalescing(coalescing);
        newFactory.setExpandEntityReferences(expandEntityReferences);
        newFactory.setIgnoringComments(ignoreComments);
        newFactory.setIgnoringElementContentWhitespace(ignoreElementContentWhitespace);
        newFactory.setNamespaceAware(namespaceAware);
        newFactory.setSchema(schema);
        newFactory.setValidating(dtdValidating);
        newFactory.setXIncludeAware(xincludeAware);
        builderFactory = newFactory;
    }
    
    /**
     * Sets document builder attributes. If an attribute is not supported it is ignored.
     * 
     * @param factory document builder factory upon which the attribute will be set
     * @param attributes the set of attributes to be set
     */
    protected void setAttributes(DocumentBuilderFactory factory, Map<String, Object> attributes) {
        if (attributes == null || attributes.isEmpty()) {
            return;
        }

        for (Map.Entry<String, Object> attribute : attributes.entrySet()) {
            try {
                log.debug("Setting DocumentBuilderFactory attribute '{}'", attribute.getKey());
                factory.setAttribute(attribute.getKey(), attribute.getValue());
            } catch (IllegalArgumentException e) {
                log.warn("DocumentBuilderFactory attribute '{}' is not supported", attribute.getKey());
            }
        }
    }

    /**
     * Sets document builder features. If an features is not supported it is ignored.
     * 
     * @param factory document builder factory upon which the attribute will be set
     * @param features the set of features to be set
     */
    protected void setFeatures(DocumentBuilderFactory factory, Map<String, Boolean> features) {
        if (features == null || features.isEmpty()) {
            return;
        }

        for (Map.Entry<String, Boolean> feature : features.entrySet()) {
            try {
                log.debug("Setting DocumentBuilderFactory attribute '{}'", feature.getKey());
                factory.setFeature(feature.getKey(), feature.getValue());
            } catch (ParserConfigurationException e) {
                log.warn("DocumentBuilderFactory feature '{}' is not supported", feature.getKey());
            }
        }
    }

    /**
     * Creates a new document builder.
     * 
     * @return newly created document builder
     * 
     * @throws XMLParserException thrown if their is a configuration error with the builder factory
     */
    protected DocumentBuilder createBuilder() throws XMLParserException {
        try {
            DocumentBuilder builder = builderFactory.newDocumentBuilder();

            if (entityResolver != null) {
                builder.setEntityResolver(entityResolver);
            }

            if (errorHandler != null) {
                builder.setErrorHandler(errorHandler);
            }

            return builder;
        } catch (ParserConfigurationException e) {
            log.error("Unable to create new document builder", e);
            throw new XMLParserException("Unable to create new document builder", e);
        }
    }

    /**
     * A proxy that prevents the manages document builders retrieved from the parser pool.
     */
    protected class DocumentBuilderProxy extends DocumentBuilder {

        /** Builder being proxied. */
        private DocumentBuilder builder;

        /** Pool that owns this parser. */
        private ParserPool owningPool;

        /** Track accounting state of whether this builder has been returned to the owning pool. */
        private boolean returned;

        /**
         * Constructor.
         * 
         * @param target document builder to proxy
         * @param owner the owning pool
         */
        public DocumentBuilderProxy(DocumentBuilder target, StaticBasicParserPool owner) {
            owningPool = owner;
            builder = target;
            returned = false;
        }

        /** {@inheritDoc} */
        public DOMImplementation getDOMImplementation() {
            checkValidState();
            return builder.getDOMImplementation();
        }

        /** {@inheritDoc} */
        public Schema getSchema() {
            checkValidState();
            return builder.getSchema();
        }

        /** {@inheritDoc} */
        public boolean isNamespaceAware() {
            checkValidState();
            return builder.isNamespaceAware();
        }

        /** {@inheritDoc} */
        public boolean isValidating() {
            checkValidState();
            return builder.isValidating();
        }

        /** {@inheritDoc} */
        public boolean isXIncludeAware() {
            checkValidState();
            return builder.isXIncludeAware();
        }

        /** {@inheritDoc} */
        public Document newDocument() {
            checkValidState();
            return builder.newDocument();
        }

        /** {@inheritDoc} */
        public Document parse(File f) throws SAXException, IOException {
            checkValidState();
            return builder.parse(f);
        }

        /** {@inheritDoc} */
        public Document parse(InputSource is) throws SAXException, IOException {
            checkValidState();
            return builder.parse(is);
        }

        /** {@inheritDoc} */
        public Document parse(InputStream is) throws SAXException, IOException {
            checkValidState();
            return builder.parse(is);
        }

        /** {@inheritDoc} */
        public Document parse(InputStream is, String systemId) throws SAXException, IOException {
            checkValidState();
            return builder.parse(is, systemId);
        }

        /** {@inheritDoc} */
        public Document parse(String uri) throws SAXException, IOException {
            checkValidState();
            return builder.parse(uri);
        }

        /** {@inheritDoc} */
        public void reset() {
            // ignore, entity resolver and error handler can't be changed
        }

        /** {@inheritDoc} */
        public void setEntityResolver(EntityResolver er) {
            checkValidState();
            return;
        }

        /** {@inheritDoc} */
        public void setErrorHandler(ErrorHandler eh) {
            checkValidState();
            return;
        }

        /**
         * Gets the pool that owns this parser.
         * 
         * @return pool that owns this parser
         */
        protected ParserPool getOwningPool() {
            return owningPool;
        }
        
        /**
         * Gets the proxied document builder.
         * 
         * @return proxied document builder
         */
        protected DocumentBuilder getProxiedBuilder() {
            return builder;
        }
        
        /**
         * Check accounting state as to whether this parser has been returned to the
         * owning pool.
         * 
         * @return true if parser has been returned to the owning pool, otherwise false
         */
        protected boolean isReturned() {
            return returned;
        }
        
        /**
         * Set accounting state as to whether this parser has been returned to the
         * owning pool.
         * 
         * @param isReturned set true to indicate that parser has been returned to the owning pool
         */
        protected void setReturned(boolean isReturned) {
           this.returned = isReturned; 
        }
        
        /**
         * Check whether the parser is in a valid and usable state, and if not, throw a runtime exception.
         * 
         * @throws IllegalStateException thrown if the parser is in a state such that it can not be used
         */
        protected void checkValidState() throws IllegalStateException {
            if (isReturned()) {
                throw new IllegalStateException("DocumentBuilderProxy has already been returned to its owning pool");
            }
        }

        /** {@inheritDoc} */
        protected void finalize() throws Throwable {
            super.finalize();
            owningPool.returnBuilder(this);
        }
    }
}