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
import org.opensaml.common.binding.artifact.SAMLArtifactMap.SAMLArtifactMapEntry;
import org.opensaml.common.binding.artifact.SAMLArtifactMap.SAMLArtifactMapEntryFactory;
import org.opensaml.xml.XMLRuntimeException;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.util.XMLObjectHelper;

/**
 * A basic factory for instances of {@link SAMLArtifactMapEntryFactory}.
 * 
 * <p>
 * If this implementation, if the SAMLObject being stored does not have a parent,
 * then it will be stored as-is.  If it does have a parent, it will first be cloned,
 * with its cloned and cached DOM rooted in a new Document.
 * </p>
 * 
 * <p>
 * If the <code>serializeMessage</code> property is true, then the SAMLObject held by the
 * entry will be internally serialized within the entry before it is returned.
 * This option defaults to false.
 * </p>
 */
public class BasicSAMLArtifactMapEntryFactory implements SAMLArtifactMapEntryFactory {
    
    /** Flag determining whether the SAMLObject message should be explicitly serialized
     * on creation of the new artifact map entry. */
    private boolean serializeMessage;

    /** 
     * Set the flag determining whether the SAMLObject message should be explicitly serialized
     * on creation of the new artifact map entry. Defaults to false.
     * 
     * @param newSerializeMessage the new flag value 
     */
    public void setSerializeMessage(boolean newSerializeMessage) {
        serializeMessage = newSerializeMessage;
    }

    /**
     * Get the flag determining whether the SAMLObject message should be explicitly serialized
     * on creation of the new artifact map entry. Defaults to false.
     * 
     * @return the current flag value
     */
    public boolean isSerializeMessage() {
        return serializeMessage;
    }

    /** {@inheritDoc} */
    public SAMLArtifactMapEntry newEntry(String artifact, String issuerId, String relyingPartyId,
            SAMLObject samlMessage, long lifetime) {
        
        SAMLObject newSAMLMessage = getStorableSAMLMessage(samlMessage);
        
        BasicSAMLArtifactMapEntry  entry = 
            new BasicSAMLArtifactMapEntry(artifact, issuerId, relyingPartyId, newSAMLMessage , lifetime);
        
        if (serializeMessage) {
            entry.serializeMessage();
        }
        return entry;
    }

    /**
     * Get the SAMLObject which will actually be stored in the produced SAMLArtifactMapEntry.
     * 
     * <p>
     * This may or may not be the same SAMLObject that is passed in.  If the SAMLObject does
     * not have a parent, the same object will be returned.  Otherwise, the object will be cloned
     * and the cloned instance returned.
     * </p>
     * 
     * @param samlMessage the SAML message to process
     * @return an equivalent SAML Message
     */
    private SAMLObject getStorableSAMLMessage(SAMLObject samlMessage) {
        if (!samlMessage.hasParent()) {
            return samlMessage;
        } else {
            try {
                return XMLObjectHelper.cloneXMLObject(samlMessage, true);
            } catch (MarshallingException e) {
                throw new XMLRuntimeException("Error during marshalling of SAMLObject", e);
            } catch (UnmarshallingException e) {
                throw new XMLRuntimeException("Error during unmarshalling of SAMLObject", e);
            }
        }
    }

}
