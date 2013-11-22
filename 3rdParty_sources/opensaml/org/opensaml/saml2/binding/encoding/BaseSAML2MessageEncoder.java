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

package org.opensaml.saml2.binding.encoding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.opensaml.Configuration;
import org.opensaml.common.SAMLObject;
import org.opensaml.common.SignableSAMLObject;
import org.opensaml.common.binding.SAMLMessageContext;
import org.opensaml.common.binding.encoding.SAMLMessageEncoder;
import org.opensaml.saml2.core.StatusResponseType;
import org.opensaml.saml2.metadata.Endpoint;
import org.opensaml.util.URLBuilder;
import org.opensaml.ws.message.encoder.BaseMessageEncoder;
import org.opensaml.ws.message.encoder.MessageEncodingException;
import org.opensaml.xml.XMLObjectBuilder;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.security.SecurityException;
import org.opensaml.xml.security.SecurityHelper;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.SignatureException;
import org.opensaml.xml.signature.Signer;
import org.opensaml.xml.util.DatatypeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for SAML 2 message encoders.
 */
public abstract class BaseSAML2MessageEncoder extends BaseMessageEncoder implements SAMLMessageEncoder {
    
    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(BaseSAML2MessageEncoder.class);

    /** The list of schemes allowed to appear in URLs related to the encoded message. Defaults to 'http' and 'https'. */
    private List<String> allowedURLSchemes;
    
    public BaseSAML2MessageEncoder(){
        super();
        setAllowedURLSchemes(new String[] { "http", "https" });
    }
    
    /**
     * Gets the unmodifiable list of schemes allowed to appear in URLs related to the encoded message.
     * 
     * @return list of URL schemes allowed to appear in a message
     */
    public List<String> getAllowedURLSchemes() {
        return allowedURLSchemes;
    }

    /**
     * Sets the list of list of schemes allowed to appear in URLs related to the encoded message. Note, the appearance
     * of schemes such as 'javascript' may open the system up to attacks (e.g. cross-site scripting attacks).
     * 
     * @param schemes URL schemes allowed to appear in a message
     */
    public void setAllowedURLSchemes(String[] schemes) {
        if (schemes == null || schemes.length == 0) {
            allowedURLSchemes = Collections.emptyList();
        } else {
            List<String> temp = new ArrayList<String>();
            for (String scheme : schemes) {
                temp.add(scheme);
            }
            allowedURLSchemes = Collections.unmodifiableList(temp);
        }
    }
    
    /**
     * Gets the response URL from the relying party endpoint. If the SAML message is a {@link StatusResponseType} and the relying
     * party endpoint contains a response location then that location is returned otherwise the normal endpoint location
     * is returned.
     * 
     * @param messageContext current message context
     * 
     * @return response URL from the relying party endpoint
     * 
     * @throws MessageEncodingException throw if no relying party endpoint is available
     */
    protected URLBuilder getEndpointURL(SAMLMessageContext messageContext) throws MessageEncodingException {
        Endpoint endpoint = messageContext.getPeerEntityEndpoint();
        if (endpoint == null) {
            throw new MessageEncodingException("Endpoint for relying party was null.");
        }
        
        URLBuilder urlBuilder;
        if (messageContext.getOutboundSAMLMessage() instanceof StatusResponseType
                && !DatatypeHelper.isEmpty(endpoint.getResponseLocation())) {
            urlBuilder = new URLBuilder(endpoint.getResponseLocation());
        } else {
            if (DatatypeHelper.isEmpty(endpoint.getLocation())) {
                throw new MessageEncodingException("Relying party endpoint location was null or empty.");
            }
            urlBuilder = new URLBuilder(endpoint.getLocation());
        }
        
        if(!getAllowedURLSchemes().contains(urlBuilder.getScheme())){
           throw new MessageEncodingException("Relying party endpoint used the untrusted URL scheme " + urlBuilder.getScheme()); 
        }
        return urlBuilder;
    }

    /**
     * Checks that the relay state is 80 bytes or less if it is not null.
     * 
     * @param relayState relay state to check
     * 
     * @return true if the relay state is not empty and is less than 80 bytes
     */
    protected boolean checkRelayState(String relayState) {
        if (!DatatypeHelper.isEmpty(relayState)) {
            if (relayState.getBytes().length > 80) {
                log.warn("Relay state exceeds 80 bytes, some application may not support this.");
            }

            return true;
        }

        return false;
    }

    /**
     * Sets the destination attribute on the outbound message if it is a {@link StatusResponseType} message.
     * 
     * @param outboundMessage outbound SAML message
     * @param endpointURL destination endpoint
     */
    protected void setResponseDestination(SAMLObject outboundMessage, String endpointURL) {
        if (outboundMessage instanceof StatusResponseType) {
            ((StatusResponseType) outboundMessage).setDestination(endpointURL);
        }
    }

    /**
     * Signs the given SAML message if it a {@link SignableSAMLObject} and this encoder has signing credentials.
     * 
     * @param messageContext current message context
     * 
     * @throws MessageEncodingException thrown if there is a problem marshalling or signing the outbound message
     */
    @SuppressWarnings("unchecked")
    protected void signMessage(SAMLMessageContext messageContext) throws MessageEncodingException {
        SAMLObject outboundSAML = messageContext.getOutboundSAMLMessage();
        Credential signingCredential = messageContext.getOuboundSAMLMessageSigningCredential();

        if (outboundSAML instanceof SignableSAMLObject && signingCredential != null) {
            SignableSAMLObject signableMessage = (SignableSAMLObject) outboundSAML;

            XMLObjectBuilder<Signature> signatureBuilder = Configuration.getBuilderFactory().getBuilder(
                    Signature.DEFAULT_ELEMENT_NAME);
            Signature signature = signatureBuilder.buildObject(Signature.DEFAULT_ELEMENT_NAME);
            
            signature.setSigningCredential(signingCredential);
            try {
                //TODO pull SecurityConfiguration from SAMLMessageContext?  needs to be added
                //TODO pull binding-specific keyInfoGenName from encoder setting, etc?
                SecurityHelper.prepareSignatureParams(signature, signingCredential, null, null);
            } catch (SecurityException e) {
                throw new MessageEncodingException("Error preparing signature for signing", e);
            }
            
            signableMessage.setSignature(signature);

            try {
                Marshaller marshaller = Configuration.getMarshallerFactory().getMarshaller(signableMessage);
                if (marshaller == null) {
                    throw new MessageEncodingException("No marshaller registered for "
                            + signableMessage.getElementQName() + ", unable to marshall in preperation for signing");
                }
                marshaller.marshall(signableMessage);

                Signer.signObject(signature);
            } catch (MarshallingException e) {
                log.error("Unable to marshall protocol message in preparation for signing", e);
                throw new MessageEncodingException("Unable to marshall protocol message in preparation for signing", e);
            } catch (SignatureException e) {
                log.error("Unable to sign protocol message", e);
                throw new MessageEncodingException("Unable to sign protocol message", e);
            }
        }
    }
}