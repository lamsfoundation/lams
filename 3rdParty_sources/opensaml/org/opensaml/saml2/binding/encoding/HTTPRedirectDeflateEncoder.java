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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

import org.opensaml.common.SAMLObject;
import org.opensaml.common.SignableSAMLObject;
import org.opensaml.common.binding.SAMLMessageContext;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.core.RequestAbstractType;
import org.opensaml.saml2.core.StatusResponseType;
import org.opensaml.util.URLBuilder;
import org.opensaml.ws.message.MessageContext;
import org.opensaml.ws.message.encoder.MessageEncodingException;
import org.opensaml.ws.transport.http.HTTPOutTransport;
import org.opensaml.ws.transport.http.HTTPTransportUtils;
import org.opensaml.xml.Configuration;
import org.opensaml.xml.security.SecurityConfiguration;
import org.opensaml.xml.security.SecurityException;
import org.opensaml.xml.security.SecurityHelper;
import org.opensaml.xml.security.SigningUtil;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.util.Base64;
import org.opensaml.xml.util.Pair;
import org.opensaml.xml.util.XMLHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SAML 2.0 HTTP Redirect encoder using the DEFLATE encoding method.
 * 
 * This encoder only supports DEFLATE compression and DSA-SHA1 and RSA-SHA1 signatures.
 */
public class HTTPRedirectDeflateEncoder extends BaseSAML2MessageEncoder {

    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(HTTPRedirectDeflateEncoder.class);

    /** Constructor. */
    public HTTPRedirectDeflateEncoder() {
        super();
    }

    /** {@inheritDoc} */
    public String getBindingURI() {
        return SAMLConstants.SAML2_REDIRECT_BINDING_URI;
    }
    
    /** {@inheritDoc} */
    public boolean providesMessageConfidentiality(MessageContext messageContext) throws MessageEncodingException {
        return false;
    }

    /** {@inheritDoc} */
    public boolean providesMessageIntegrity(MessageContext messageContext) throws MessageEncodingException {
        return false;
    }

    /** {@inheritDoc} */
    protected void doEncode(MessageContext messageContext) throws MessageEncodingException {
        if (!(messageContext instanceof SAMLMessageContext)) {
            log.error("Invalid message context type, this encoder only support SAMLMessageContext");
            throw new MessageEncodingException(
                    "Invalid message context type, this encoder only support SAMLMessageContext");
        }

        if (!(messageContext.getOutboundMessageTransport() instanceof HTTPOutTransport)) {
            log.error("Invalid outbound message transport type, this encoder only support HTTPOutTransport");
            throw new MessageEncodingException(
                    "Invalid outbound message transport type, this encoder only support HTTPOutTransport");
        }

        SAMLMessageContext samlMsgCtx = (SAMLMessageContext) messageContext;

        String endpointURL = getEndpointURL(samlMsgCtx).buildURL();

        setResponseDestination(samlMsgCtx.getOutboundSAMLMessage(), endpointURL);

        removeSignature(samlMsgCtx);

        String encodedMessage = deflateAndBase64Encode(samlMsgCtx.getOutboundSAMLMessage());

        String redirectURL = buildRedirectURL(samlMsgCtx, endpointURL, encodedMessage);

        HTTPOutTransport out = (HTTPOutTransport) messageContext.getOutboundMessageTransport();
        HTTPTransportUtils.addNoCacheHeaders(out);
        HTTPTransportUtils.setUTF8Encoding(out);

        out.sendRedirect(redirectURL);
    }

    /**
     * Removes the signature from the protocol message.
     * 
     * @param messageContext current message context
     */
    protected void removeSignature(SAMLMessageContext messageContext) {
        SignableSAMLObject message = (SignableSAMLObject) messageContext.getOutboundSAMLMessage();
        if (message.isSigned()) {
            log.debug("Removing SAML protocol message signature");
            message.setSignature(null);
        }
    }

    /**
     * DEFLATE (RFC1951) compresses the given SAML message.
     * 
     * @param message SAML message
     * 
     * @return DEFLATE compressed message
     * 
     * @throws MessageEncodingException thrown if there is a problem compressing the message
     */
    protected String deflateAndBase64Encode(SAMLObject message) throws MessageEncodingException {
        log.debug("Deflating and Base64 encoding SAML message");
        try {
            String messageStr = XMLHelper.nodeToString(marshallMessage(message));

            ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
            Deflater deflater = new Deflater(Deflater.DEFLATED, true);
            DeflaterOutputStream deflaterStream = new DeflaterOutputStream(bytesOut, deflater);
            deflaterStream.write(messageStr.getBytes("UTF-8"));
            deflaterStream.finish();

            return Base64.encodeBytes(bytesOut.toByteArray(), Base64.DONT_BREAK_LINES);
        } catch (IOException e) {
            throw new MessageEncodingException("Unable to DEFLATE and Base64 encode SAML message", e);
        }
    }

    /**
     * Builds the URL to redirect the client to.
     * 
     * @param messagesContext current message context
     * @param endpointURL endpoint URL to send encoded message to
     * @param message Deflated and Base64 encoded message
     * 
     * @return URL to redirect client to
     * 
     * @throws MessageEncodingException thrown if the SAML message is neither a RequestAbstractType or Response
     */
    protected String buildRedirectURL(SAMLMessageContext messagesContext, String endpointURL, String message)
            throws MessageEncodingException {
        log.debug("Building URL to redirect client to");
        URLBuilder urlBuilder = new URLBuilder(endpointURL);

        List<Pair<String, String>> queryParams = urlBuilder.getQueryParams();
        queryParams.clear();

        if (messagesContext.getOutboundSAMLMessage() instanceof RequestAbstractType) {
            queryParams.add(new Pair<String, String>("SAMLRequest", message));
        } else if (messagesContext.getOutboundSAMLMessage() instanceof StatusResponseType) {
            queryParams.add(new Pair<String, String>("SAMLResponse", message));
        } else {
            throw new MessageEncodingException(
                    "SAML message is neither a SAML RequestAbstractType or StatusResponseType");
        }

        String relayState = messagesContext.getRelayState();
        if (checkRelayState(relayState)) {
            queryParams.add(new Pair<String, String>("RelayState", relayState));
        }

        Credential signingCredential = messagesContext.getOuboundSAMLMessageSigningCredential();
        if (signingCredential != null) {
            // TODO pull SecurityConfiguration from SAMLMessageContext? needs to be added
            String sigAlgURI = getSignatureAlgorithmURI(signingCredential, null);
            Pair<String, String> sigAlg = new Pair<String, String>("SigAlg", sigAlgURI);
            queryParams.add(sigAlg);
            String sigMaterial = urlBuilder.buildQueryString();

            queryParams.add(new Pair<String, String>("Signature", generateSignature(signingCredential, sigAlgURI,
                    sigMaterial)));
        }

        return urlBuilder.buildURL();
    }

    /**
     * Gets the signature algorithm URI to use with the given signing credential.
     * 
     * @param credential the credential that will be used to sign the message
     * @param config the SecurityConfiguration to use (may be null)
     * 
     * @return signature algorithm to use with the given signing credential
     * 
     * @throws MessageEncodingException thrown if the algorithm URI could not be derived from the supplied credential
     */
    protected String getSignatureAlgorithmURI(Credential credential, SecurityConfiguration config)
            throws MessageEncodingException {

        SecurityConfiguration secConfig;
        if (config != null) {
            secConfig = config;
        } else {
            secConfig = Configuration.getGlobalSecurityConfiguration();
        }

        String signAlgo = secConfig.getSignatureAlgorithmURI(credential);

        if (signAlgo == null) {
            throw new MessageEncodingException("The signing credential's algorithm URI could not be derived");
        }

        return signAlgo;
    }

    /**
     * Generates the signature over the query string.
     * 
     * @param signingCredential credential that will be used to sign query string
     * @param algorithmURI algorithm URI of the signing credential
     * @param queryString query string to be signed
     * 
     * @return base64 encoded signature of query string
     * 
     * @throws MessageEncodingException there is an error computing the signature
     */
    protected String generateSignature(Credential signingCredential, String algorithmURI, String queryString)
            throws MessageEncodingException {

        log.debug(String.format("Generating signature with key type '%s', algorithm URI '%s' over query string '%s'",
                SecurityHelper.extractSigningKey(signingCredential).getAlgorithm(), algorithmURI, queryString));

        String b64Signature = null;
        try {
            byte[] rawSignature = SigningUtil.signWithURI(signingCredential, algorithmURI, queryString
                    .getBytes("UTF-8"));
            b64Signature = Base64.encodeBytes(rawSignature, Base64.DONT_BREAK_LINES);
            log.debug("Generated digital signature value (base64-encoded) {}", b64Signature);
        } catch (SecurityException e) {
            log.error("Error during URL signing process", e);
            throw new MessageEncodingException("Unable to sign URL query string", e);
        } catch (UnsupportedEncodingException e) {
            // UTF-8 encoding is required to be supported by all JVMs
        }

        return b64Signature;
    }
}