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

import java.io.UnsupportedEncodingException;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.opensaml.common.binding.SAMLMessageContext;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.ws.message.encoder.MessageEncodingException;
import org.opensaml.xml.Configuration;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.security.SecurityConfiguration;
import org.opensaml.xml.security.SecurityException;
import org.opensaml.xml.security.SecurityHelper;
import org.opensaml.xml.security.SigningUtil;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.security.keyinfo.KeyInfoGenerator;
import org.opensaml.xml.signature.KeyInfo;
import org.opensaml.xml.util.Base64;
import org.opensaml.xml.util.DatatypeHelper;
import org.opensaml.xml.util.XMLHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SAML 2.0 HTTP-POST-SimpleSign binding message encoder.
 * 
 * <p>
 * The spec does not preclude the SAML 2 protocol message from being signed using the XML Signature method, in addition
 * to the SimpleSign method specified by this binding. Signing via XML Signature over the SAML request and response
 * payload may be toggled by the <code>signXMLProtocolMessage</code> parameter to the constructor
 * {@link HTTPPostSimpleSignEncoder#HTTPPostSimpleSignEncoder(VelocityEngine, String, boolean)}. If this constructor
 * variant is not used, the flag defaults to <code>false</code>.
 * </p>
 */
public class HTTPPostSimpleSignEncoder extends HTTPPostEncoder {

    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(HTTPPostSimpleSignEncoder.class);

    /**
     * Flag to indicate whether the SAML 2 protocol message should additionally be signed using the XML Signature, in
     * addition to SimpleSign.
     */
    private boolean signProtocolMessageWithXMLDSIG;

    /**
     * Constructor.
     * 
     * @param engine Velocity engine instance used to create POST body
     * @param templateId ID of the template used to create POST body
     */
    public HTTPPostSimpleSignEncoder(VelocityEngine engine, String templateId) {
        super(engine, templateId);
        signProtocolMessageWithXMLDSIG = false;
    }

    /**
     * Constructor.
     * 
     * @param engine Velocity engine instance used to create POST body
     * @param templateId ID of the template used to create POST body
     * @param signXMLProtocolMessage if true, the protocol message will be signed according to the XML Signature
     *            specification, in addition to the HTTP-POST-SimpleSign binding specification
     */
    public HTTPPostSimpleSignEncoder(VelocityEngine engine, String templateId, boolean signXMLProtocolMessage) {
        super(engine, templateId);
        signProtocolMessageWithXMLDSIG = signXMLProtocolMessage;
    }

    /** {@inheritDoc} */
    public String getBindingURI() {
        return SAMLConstants.SAML2_POST_SIMPLE_SIGN_BINDING_URI;
    }

    /** {@inheritDoc} */
    protected void signMessage(SAMLMessageContext messageContext) throws MessageEncodingException {
        if (signProtocolMessageWithXMLDSIG) {
            super.signMessage(messageContext);
        }
    }

    /** {@inheritDoc} */
    protected void populateVelocityContext(VelocityContext velocityContext, SAMLMessageContext messageContext,
            String endpointURL) throws MessageEncodingException {

        super.populateVelocityContext(velocityContext, messageContext, endpointURL);

        Credential signingCredential = messageContext.getOuboundSAMLMessageSigningCredential();
        if (signingCredential == null) {
            log.debug("No signing credential was supplied, skipping HTTP-Post simple signing");
            return;
        }

        // TODO pull SecurityConfiguration from SAMLMessageContext? needs to be added
        // TODO pull binding-specific keyInfoGenName from encoder setting, etc?
        String sigAlgURI = getSignatureAlgorithmURI(signingCredential, null);
        velocityContext.put("SigAlg", sigAlgURI);

        String formControlData = buildFormDataToSign(velocityContext, messageContext, sigAlgURI);
        velocityContext.put("Signature", generateSignature(signingCredential, sigAlgURI, formControlData));

        KeyInfoGenerator kiGenerator = SecurityHelper.getKeyInfoGenerator(signingCredential, null, null);
        if (kiGenerator != null) {
            String kiBase64 = buildKeyInfo(signingCredential, kiGenerator);
            if (!DatatypeHelper.isEmpty(kiBase64)) {
                velocityContext.put("KeyInfo", kiBase64);
            }
        }
    }

    /**
     * Build the {@link KeyInfo} from the signing credential.
     * 
     * @param signingCredential the credential used for signing
     * @param kiGenerator the generator for the KeyInfo
     * @throws MessageEncodingException thrown if there is an error generating or marshalling the KeyInfo
     * @return the marshalled, serialized and base64-encoded KeyInfo, or null if none was generated
     */
    protected String buildKeyInfo(Credential signingCredential, KeyInfoGenerator kiGenerator)
            throws MessageEncodingException {

        try {
            KeyInfo keyInfo = kiGenerator.generate(signingCredential);
            if (keyInfo != null) {
                Marshaller marshaller = Configuration.getMarshallerFactory().getMarshaller(keyInfo);
                if (marshaller == null) {
                    log.error("No KeyInfo marshaller available from configuration");
                    throw new MessageEncodingException("No KeyInfo marshaller was configured");
                }
                String kiXML = XMLHelper.nodeToString(marshaller.marshall(keyInfo));
                String kiBase64 = Base64.encodeBytes(kiXML.getBytes(), Base64.DONT_BREAK_LINES);
                return kiBase64;
            } else {
                return null;
            }
        } catch (SecurityException e) {
            log.error("Error generating KeyInfo from signing credential", e);
            throw new MessageEncodingException("Error generating KeyInfo from signing credential", e);
        } catch (MarshallingException e) {
            log.error("Error marshalling KeyInfo based on signing credential", e);
            throw new MessageEncodingException("Error marshalling KeyInfo based on signing credential", e);
        }
    }

    /**
     * Build the form control data string over which the signature is computed.
     * 
     * @param velocityContext the Velocity context which is already populated with the values for SAML message and relay
     *            state
     * @param messageContext  the SAML message context being processed
     * @param sigAlgURI the signature algorithm URI
     * 
     * @return the form control data string for signature computation
     */
    protected String buildFormDataToSign(VelocityContext velocityContext, SAMLMessageContext messageContext, String sigAlgURI) {
        StringBuilder builder = new StringBuilder();

        boolean isRequest = false;
        if (velocityContext.get("SAMLRequest") != null) {
            isRequest = true;
        }

        String msgB64;
        if (isRequest) {
            msgB64 = (String) velocityContext.get("SAMLRequest");
        } else {
            msgB64 = (String) velocityContext.get("SAMLResponse");
        }

        String msg = null;
        try {
            msg = new String(Base64.decode(msgB64), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // All JVM's required to support UTF-8
        }

        if (isRequest) {
            builder.append("SAMLRequest=" + msg);
        } else {
            builder.append("SAMLResponse=" + msg);
        }

        if (messageContext.getRelayState() != null) {
            builder.append("&RelayState=" + messageContext.getRelayState());
        }

        builder.append("&SigAlg=" + sigAlgURI);

        return builder.toString();
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
     * Generates the signature over the string of concatenated form control data as indicated by the SimpleSign spec.
     * 
     * @param signingCredential credential that will be used to sign
     * @param algorithmURI algorithm URI of the signing credential
     * @param formData form control data to be signed
     * 
     * @return base64 encoded signature of form control data
     * 
     * @throws MessageEncodingException there is an error computing the signature
     */
    protected String generateSignature(Credential signingCredential, String algorithmURI, String formData)
            throws MessageEncodingException {

        log.debug(String.format(
                "Generating signature with key type '%s', algorithm URI '%s' over form control string '%s'",
                SecurityHelper.extractSigningKey(signingCredential).getAlgorithm(), algorithmURI, formData));

        String b64Signature = null;
        try {
            byte[] rawSignature = SigningUtil.signWithURI(signingCredential, algorithmURI, formData.getBytes("UTF-8"));
            b64Signature = Base64.encodeBytes(rawSignature, Base64.DONT_BREAK_LINES);
            log.debug("Generated digital signature value (base64-encoded) {}", b64Signature);
        } catch (SecurityException e) {
            log.error("Error during URL signing process", e);
            throw new MessageEncodingException("Unable to sign form control string", e);
        } catch (UnsupportedEncodingException e) {
            // UTF-8 encoding is required to be supported by all JVMs
        }

        return b64Signature;
    }

}
