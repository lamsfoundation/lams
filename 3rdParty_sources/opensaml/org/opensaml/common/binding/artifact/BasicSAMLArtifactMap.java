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

package org.opensaml.common.binding.artifact;

import org.opensaml.common.SAMLObject;
import org.opensaml.util.storage.StorageService;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.parse.ParserPool;
import org.opensaml.xml.util.DatatypeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Basic artifact map implementation that uses a {@link StorageService} to store and retrieve artifacts.
 */
public class BasicSAMLArtifactMap implements SAMLArtifactMap {
    
    /** The default StorageService partition name to use. */
    public static final String DEFAULT_STORAGE_PARTITION = "artifact";

    /** Class Logger. */
    private final Logger log = LoggerFactory.getLogger(BasicSAMLArtifactMap.class);

    /** Artifact mapping storage. */
    private StorageService<String, SAMLArtifactMapEntry> artifactStore;

    /** Storage service partition used by this cache. default: artifact */
    private String partition;

    /** Lifetime of an artifact in milliseconds. */
    private long artifactLifetime;
    
    /** Factory for SAMLArtifactMapEntry instances. */
    private SAMLArtifactMapEntryFactory entryFactory;

    /**
     * Constructor.
     * 
     * @deprecated replacement {@link BasicSAMLArtifactMap#BasicSAMLArtifactMap(StorageService, long)}
     * 
     * @param parser parser pool used to parse serialized messages.
     *         <strong>(Note: ParserPool arg is deprecated and no longer used).</strong>
     * @param storage artifact mapping storage
     * @param lifetime lifetime of an artifact in milliseconds
     */
    public BasicSAMLArtifactMap(ParserPool parser, StorageService<String, SAMLArtifactMapEntry> storage, 
            long lifetime) {
        this(storage, DEFAULT_STORAGE_PARTITION, lifetime);
    }
    
    /**
     * Constructor.
     * 
     * @param storage artifact mapping storage
     * @param lifetime lifetime of an artifact in milliseconds
     */
    public BasicSAMLArtifactMap(StorageService<String, SAMLArtifactMapEntry> storage, long lifetime) {
        this(storage, DEFAULT_STORAGE_PARTITION, lifetime);
    }

    /**
     * Constructor.
     * 
     * @param storage artifact mapping storage
     * @param storageParition name of storage service partition to use
     * @param lifetime lifetime of an artifact in milliseconds
     */
    public BasicSAMLArtifactMap(StorageService<String, SAMLArtifactMapEntry> storage, String storageParition,
            long lifetime) {
        this(new BasicSAMLArtifactMapEntryFactory(), storage, storageParition, lifetime);
    }
    
    /**
     * Constructor.
     * 
     * @param factory the SAML artifact map entry factory to use
     * @param storage artifact mapping storage
     * @param lifetime lifetime of an artifact in milliseconds
     */
    public BasicSAMLArtifactMap(SAMLArtifactMapEntryFactory factory, 
            StorageService<String, SAMLArtifactMapEntry> storage,  long lifetime) {
        this(factory, storage, DEFAULT_STORAGE_PARTITION, lifetime);
    }
    
    /**
     * Constructor.
     * 
     * @param factory the SAML artifact map entry factory to use
     * @param storage artifact mapping storage
     * @param storageParition name of storage service partition to use
     * @param lifetime lifetime of an artifact in milliseconds
     */
    public BasicSAMLArtifactMap(SAMLArtifactMapEntryFactory factory,
            StorageService<String, SAMLArtifactMapEntry> storage, 
            String storageParition, long lifetime) {
        entryFactory = factory;
        artifactStore = storage;
        if (!DatatypeHelper.isEmpty(storageParition)) {
            partition = DatatypeHelper.safeTrim(storageParition);
        } else {
            partition = DEFAULT_STORAGE_PARTITION;
        }
        artifactLifetime = lifetime;
    }

    /** {@inheritDoc} */
    public boolean contains(String artifact) {
        return artifactStore.contains(partition, artifact);
    }

    /** {@inheritDoc} */
    public SAMLArtifactMapEntry get(String artifact) {
        log.debug("Attempting to retrieve entry for artifact: {}", artifact);
        SAMLArtifactMapEntry entry = artifactStore.get(partition, artifact);

        if(entry == null){
            log.debug("No entry found for artifact: {}", artifact);
            return null;
        }
        
        if (entry.isExpired()) {
            log.debug("Entry for artifact was expired: {}", artifact);
            remove(artifact);
            return null;
        }
        
        log.debug("Found valid entry for artifact: {}", artifact);
        return entry;
    }

    /** {@inheritDoc} */
    public void put(String artifact, String relyingPartyId, String issuerId, SAMLObject samlMessage)
            throws MarshallingException {

        SAMLArtifactMapEntry artifactEntry = entryFactory.newEntry(artifact, issuerId, relyingPartyId, 
                samlMessage, artifactLifetime);
        
        if (log.isDebugEnabled()) {
            log.debug("Storing new artifact entry '{}' for relying party '{}', expiring at '{}'", 
                    new Object[] {artifact, relyingPartyId, artifactEntry.getExpirationTime()});
        }
        
        artifactStore.put(partition, artifact, artifactEntry);
    }

    /** {@inheritDoc} */
    public void remove(String artifact) {
        log.debug("Removing artifact entry: {}", artifact);
        artifactStore.remove(partition, artifact);
    }
    
}