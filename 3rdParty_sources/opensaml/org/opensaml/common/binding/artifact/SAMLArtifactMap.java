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
import org.opensaml.util.storage.ExpiringObject;
import org.opensaml.xml.io.MarshallingException;

/**
 * Maps an artifact to a SAML message and back again.
 * 
 * <p>Artifacts must be thread safe.</p>
 * 
 * <p>
 * An implementation of this interface MUST ensure that the persisted SAML message is no longer tied to any 
 * parent {@link org.opensaml.xml.XMLObject} that may have contained it.  This ensures that it can be safely added 
 * to another object once retrieved from the map.  This might for example be achieved by: 
 * 1) cloning the SAMLObject prior to storage, or 2) by serializing it to a string and re-parsing 
 * and unmarhsalling it once retrieved from the underlying data store.
 * This requirement may be handled by the SAMLArtifactMap directly, or by the use of of a specific 
 * implementation of {@link SAMLArtifactMapEntryFactory}.
 * </p>
 */
public interface SAMLArtifactMap {

    /**
     * Checks if a given artifact has a map entry.
     * 
     * @param artifact the artifact to check
     * 
     * @return true of this map has an entry for the given artifact, false it not
     */
    public boolean contains(String artifact);

    /**
     * Creates a mapping between a given artifact and the SAML message to which it maps.
     * 
     * @param artifact the artifact
     * @param relyingPartyId ID of the party the artifact was sent to
     * @param issuerId ID of the issuer of the artifact
     * @param samlMessage the SAML message
     * 
     * @throws MarshallingException thrown if the given SAML message can not be marshalled
     */
    public void put(String artifact, String relyingPartyId, String issuerId, SAMLObject samlMessage)
            throws MarshallingException;

    /**
     * Gets the artifact entry for the given artifact.
     * 
     * @param artifact the artifact to retrieve the entry for
     * 
     * @return the entry or null if the artifact has already expired or did not exist
     */
    public SAMLArtifactMapEntry get(String artifact);

    /**
     * Removes the artifact from this map.
     * 
     * @param artifact artifact to be removed
     */
    public void remove(String artifact);
    
    /**
     * Represents a mapping between an artifact a SAML message with some associated metadata. 
     */
    public interface SAMLArtifactMapEntry extends ExpiringObject {

        /**
         * Gets the artifact that maps to the SAML message.
         * 
         * @return artifact that maps to the SAML message
         */
        public String getArtifact();

        /**
         * Gets the ID of the issuer of the artifact.
         * 
         * @return ID of the issuer of the artifact
         */
        public String getIssuerId();

        /**
         * Gets the ID of the relying party the artifact was sent to.
         * 
         * @return ID of the relying party the artifact was sent to
         */
        public String getRelyingPartyId();

        /**
         * Gets SAML message the artifact maps to.
         * 
         * @return SAML message the artifact maps to
         */
        public SAMLObject getSamlMessage();
    }
    
    /**
     * A factory for producing SAMLArtifactMapEntry instances based on standard inputs.
     */
    public interface SAMLArtifactMapEntryFactory {
        
        /**
         * Factory method which produces a {@link SAMLArtifactMapEntry}.
         * 
         * @param artifact the artifact
         * @param issuerId ID of the issuer of the artifact
         * @param relyingPartyId ID of the party the artifact was sent to
         * @param samlMessage the SAML message
         * @param lifetime the lifetime of the artifact entry, in milliseconds
         * 
         * @return the new map entry instance
         */
        public SAMLArtifactMapEntry newEntry(String artifact, String issuerId,  String relyingPartyId, 
                SAMLObject samlMessage, long lifetime);
        
    }
}