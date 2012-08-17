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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.io.StringWriter;

import org.joda.time.DateTime;
import org.opensaml.Configuration;
import org.opensaml.common.SAMLObject;
import org.opensaml.common.binding.artifact.SAMLArtifactMap.SAMLArtifactMapEntry;
import org.opensaml.util.storage.AbstractExpiringObject;
import org.opensaml.xml.XMLRuntimeException;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.parse.ParserPool;
import org.opensaml.xml.parse.XMLParserException;
import org.opensaml.xml.util.XMLObjectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Basic implementation of {@link SAMLArtifactMapEntry}. */
public class BasicSAMLArtifactMapEntry extends AbstractExpiringObject implements SAMLArtifactMapEntry {
    
    /** Serial version UID. */
    private static final long serialVersionUID = 1577232740330721369L;

    /** Class Logger. */
    private Logger log = LoggerFactory.getLogger(BasicSAMLArtifactMapEntry.class);

    /** SAML artifact being mapped. */
    private String artifact;

    /** Entity ID of the issuer of the artifact. */
    private String issuer;

    /** Entity ID of the receiver of the artifact. */
    private String relyingParty;

    /** SAML message mapped to the artifact. */
    private transient SAMLObject message;

    /** Serialized SAML object mapped to the artifact. */
    private String serializedMessage;

    /**
     * Constructor.
     * 
     * @deprecated replacement 
     *      {@link BasicSAMLArtifactMapEntry#BasicSAMLArtifactMapEntry(String, String, String, SAMLObject, long)}
     * 
     * @param samlArtifact artifact associated with the message
     * @param issuerId issuer of the artifact
     * @param relyingPartyId receiver of the artifact
     * @param serializedSAML serialized SAML message mapped to the artifact
     * @param lifetime lifetime of the artifact in milliseconds
     */
    public BasicSAMLArtifactMapEntry(String samlArtifact, String issuerId, String relyingPartyId, 
            String serializedSAML, long lifetime) {
        super(new DateTime().plus(lifetime));
        artifact = samlArtifact;
        issuer = issuerId;
        relyingParty = relyingPartyId;
        serializedMessage = serializedSAML;
    }

    /**
     * Constructor.
     * 
     * @param samlArtifact artifact associated with the message
     * @param issuerId issuer of the artifact
     * @param relyingPartyId receiver of the artifact
     * @param samlMessage SAML message mapped to the artifact
     * @param lifetime lifetime of the artifact in milliseconds
     */
    public BasicSAMLArtifactMapEntry(String samlArtifact, String issuerId, String relyingPartyId, 
            SAMLObject samlMessage, long lifetime) {
        super(new DateTime().plus(lifetime));
        artifact = samlArtifact;
        issuer = issuerId;
        relyingParty = relyingPartyId;
        message = samlMessage;
    }

    /** {@inheritDoc} */
    public String getArtifact() {
        return artifact;
    }

    /** {@inheritDoc} */
    public String getIssuerId() {
        return issuer;
    }

    /** {@inheritDoc} */
    public String getRelyingPartyId() {
        return relyingParty;
    }

    /** {@inheritDoc} */
    public SAMLObject getSamlMessage() {
        if (message == null) {
            try {
                deserializeMessage();
            } catch (IOException e) {
                throw new XMLRuntimeException("Error deserializaing SAML message data", e);
            }
        }
        return message;
    }

    /**
     * Sets the SAML message mapped to the artifact.
     * 
     * @param saml SAML message mapped to the artifact
     */
    void setSAMLMessage(SAMLObject saml) {
        if (saml == null) {
            throw new IllegalArgumentException("SAMLObject message may not be null");
        }
        message = saml;
        // Clear the cached serialized version 
        serializedMessage = null;
    }

    /**
     * Gets the serialized form of the SAML message.
     * 
     * @deprecated replacement is: {@link #getSerializedMessage()}
     * 
     * @return serialized form of the SAML message
     * 
     */
    String getSeralizedMessage() {
        return getSerializedMessage();
    }
    
    /**
     * Gets the serialized form of the SAML message.
     * 
     * @return serialized form of the SAML message
     */
    String getSerializedMessage() {
        return serializedMessage;
    }
     
    /**
     *  Serialize the SAMLObject held by the entry and store in the class.
     *  
     *  <p>This option is provided where explicit pre-serialization of the data 
     *  is either necessary or desirable.</p>
     */
    void serializeMessage() {
        if (log == null) {
            log = LoggerFactory.getLogger(BasicSAMLArtifactMapEntry.class);
        }
        
        if (serializedMessage == null) {
            log.debug("Serializing SAMLObject to a string");
            StringWriter writer = new StringWriter();
            try {
                XMLObjectHelper.marshallToWriter(message, writer);
            } catch (MarshallingException e) {
                throw new XMLRuntimeException("Error marshalling the SAMLObject: " + e.getMessage());
            }
            
            serializedMessage = writer.toString();
            
            if (log.isTraceEnabled()) {
                log.trace("Serialized SAMLObject data was:");
                log.trace(serializedMessage);
            }
        } else {
            log.debug("SAMLObject was already serialized, skipping marshall and serialize step");
        }
    }
    
    /**
     *  Deserialize the serialized message data held by the entry so that it is available
     *  as the SAMLObject samleMessage property.
     *  
     *  <p>This option is provided where explicit deserialization of the data 
     *  is either necessary or desirable.</p>
     *  
     *  @throws IOException if there is a problem parsing or unmarshalling the serialized message
     */
    void deserializeMessage() throws IOException {
        if (log == null) {
            log = LoggerFactory.getLogger(BasicSAMLArtifactMapEntry.class);
        }
        
        if (message == null) {
            if (getSerializedMessage() == null) {
                throw new XMLRuntimeException("Serialized SAML message data was not available for deserialization");
            }
            
            ParserPool parserPool = Configuration.getParserPool();
            if (parserPool == null) {
                throw new XMLRuntimeException(
                        "No ParserPool was available for parsing the deserialized artifact map entry");
            }
            log.debug("Deserializing SAMLObject from a string");
            if (log.isTraceEnabled()) {
                log.trace("Serialized SAMLObject data was:");
                log.trace(getSerializedMessage());
            }
            StringReader reader = new StringReader(getSerializedMessage());
            try {
                SAMLObject samlObject = (SAMLObject) XMLObjectHelper.unmarshallFromReader(parserPool, reader);
                message = samlObject;
            } catch (XMLParserException e) {
                throw new IOException("Error parsing XML into DOM: " + e.getMessage());
            } catch (UnmarshallingException e) {
                throw new IOException("Error unmarshalling DOM into SAMLObject: " + e.getMessage());
            }
        }
    }
    
    /**
     * Serialize the entry to the object output stream.
     * 
     * @param out the output stream to which to serialize
     * @throws IOException if there is a problem serializing the entry
     */
    private void writeObject(ObjectOutputStream out) throws IOException {
        if (log == null) {
            log = LoggerFactory.getLogger(BasicSAMLArtifactMapEntry.class);
        }
        log.debug("Serializing object data to ObjectOutputStream");
        
        serializeMessage();
        
        out.defaultWriteObject();
    }


    /**
     * Deserialize the entry from the input stream.
     * 
     * @param in the input stream from which to deserialize
     * @throws IOException if the is a problem deserializing the object
     * @throws ClassNotFoundException if there is a problem loading the class of the object
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        if (log == null) {
            log = LoggerFactory.getLogger(BasicSAMLArtifactMapEntry.class);
        }
        log.debug("Deserializing object data from ObjectInputStream");
        
        in.defaultReadObject();
        
        deserializeMessage();
    }

}