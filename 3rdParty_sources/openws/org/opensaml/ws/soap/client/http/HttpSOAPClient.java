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

package org.opensaml.ws.soap.client.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.List;

import net.jcip.annotations.ThreadSafe;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.opensaml.ws.security.SecurityPolicy;
import org.opensaml.ws.security.SecurityPolicyResolver;
import org.opensaml.ws.soap.client.SOAPClient;
import org.opensaml.ws.soap.client.SOAPClientException;
import org.opensaml.ws.soap.client.SOAPFaultException;
import org.opensaml.ws.soap.client.SOAPMessageContext;
import org.opensaml.ws.soap.common.SOAPException;
import org.opensaml.ws.soap.soap11.Envelope;
import org.opensaml.ws.soap.soap11.Fault;
import org.opensaml.xml.Configuration;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.io.Unmarshaller;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.parse.ParserPool;
import org.opensaml.xml.parse.XMLParserException;
import org.opensaml.xml.security.SecurityException;
import org.opensaml.xml.util.XMLHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

/**
 * SOAP client that uses HTTP as the underlying transport and POST as the binding.
 * 
 * <strong>NOTE</strong> this client does not provide access to a {@link org.opensaml.ws.transport.InTransport} or
 * {@link org.opensaml.ws.transport.OutTransport}. Therefore any {@link SecurityPolicy} which operates on these object
 * can not be used with this client.
 */
@ThreadSafe
public class HttpSOAPClient implements SOAPClient {

    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(HttpSOAPClient.class);

    /** HTTP client used to send requests and receive responses. */
    private HttpClient httpClient;

    /** Pool of XML parsers used to parser incoming responses. */
    private ParserPool parserPool;

    /**
     * Constructor.
     * 
     * @param client Client used to make outbound HTTP requests. This client SHOULD employ a
     *            {@link org.apache.commons.httpclient.MultiThreadedHttpConnectionManager} and may be shared with other
     *            objects.
     * @param parser pool of XML parsers used to parse incoming responses
     */
    public HttpSOAPClient(HttpClient client, ParserPool parser) {
        if (client == null) {
            throw new IllegalArgumentException("HtppClient may not be null");
        }
        httpClient = client;

        if (parser == null) {
            throw new IllegalArgumentException("ParserPool may not be null");
        }
        parserPool = parser;
    }

    /** {@inheritDoc} */
    public void send(String endpoint, SOAPMessageContext messageContext) throws SOAPException, SecurityException {
        PostMethod post = null;
        try {
            post = createPostMethod(endpoint, (HttpSOAPRequestParameters) messageContext.getSOAPRequestParameters(),
                    (Envelope) messageContext.getOutboundMessage());

            int result = httpClient.executeMethod(post);
            log.debug("Received HTTP status code of {} when POSTing SOAP message to {}", result, endpoint);

            if (result == HttpStatus.SC_OK) {
                processSuccessfulResponse(post, messageContext);
            } else if (result == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
                processFaultResponse(post, messageContext);
            } else {
                throw new SOAPClientException("Received " + result + " HTTP response status code from HTTP request to "
                        + endpoint);
            }
        } catch (IOException e) {
            throw new SOAPClientException("Unable to send request to " + endpoint, e);
        } finally {
            if (post != null) {
                post.releaseConnection();
            }
        }
    }

    /**
     * Creates the post method used to send the SOAP request.
     * 
     * @param endpoint endpoint to which the message is sent
     * @param requestParams HTTP request parameters
     * @param message message to be sent
     * 
     * @return the post method to be used to send this message
     * 
     * @throws SOAPClientException thrown if the message could not be marshalled
     */
    protected PostMethod createPostMethod(String endpoint, HttpSOAPRequestParameters requestParams, Envelope message)
            throws SOAPClientException {
        log.debug("POSTing SOAP message to {}", endpoint);

        PostMethod post = new PostMethod(endpoint);
        post.setRequestEntity(createRequestEntity(message, Charset.forName("UTF-8")));
        if (requestParams != null && requestParams.getSoapAction() != null) {
            post.setRequestHeader(HttpSOAPRequestParameters.SOAP_ACTION_HEADER, requestParams.getSoapAction());
        }

        return post;
    }

    /**
     * Creates the request entity that makes up the POST message body.
     * 
     * @param message message to be sent
     * @param charset character set used for the message
     * 
     * @return request entity that makes up the POST message body
     * 
     * @throws SOAPClientException thrown if the message could not be marshalled
     */
    protected RequestEntity createRequestEntity(Envelope message, Charset charset) throws SOAPClientException {
        try {
            Marshaller marshaller = Configuration.getMarshallerFactory().getMarshaller(message);
            ByteArrayOutputStream arrayOut = new ByteArrayOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(arrayOut, charset);

            if (log.isDebugEnabled()) {
                log.debug("Outbound SOAP message is:\n" + XMLHelper.prettyPrintXML(marshaller.marshall(message)));
            }
            XMLHelper.writeNode(marshaller.marshall(message), writer);
            return new ByteArrayRequestEntity(arrayOut.toByteArray(), "text/xml");
        } catch (MarshallingException e) {
            throw new SOAPClientException("Unable to marshall SOAP envelope", e);
        }
    }

    /**
     * Processes a successful, as determined by an HTTP 200 status code, response.
     * 
     * @param httpMethod the HTTP method used to send the request and receive the response
     * @param messageContext current messages context
     * 
     * @throws SOAPClientException thrown if there is a problem reading the response from the {@link PostMethod}
     */
    protected void processSuccessfulResponse(PostMethod httpMethod, SOAPMessageContext messageContext)
            throws SOAPClientException {
        try {
            Envelope response = unmarshallResponse(httpMethod.getResponseBodyAsStream());
            messageContext.setInboundMessage(response);
            evaluateSecurityPolicy(messageContext);
        } catch (IOException e) {
            throw new SOAPClientException("Unable to read response", e);
        }
    }

    /**
     * Processes a SOAP fault, as determined by an HTTP 500 status code, response.
     * 
     * @param httpMethod the HTTP method used to send the request and receive the response
     * @param messageContext current messages context
     * 
     * @throws SOAPClientException thrown if the response can not be read from the {@link PostMethod}
     * @throws SOAPFaultException an exception containing the SOAP fault
     */
    protected void processFaultResponse(PostMethod httpMethod, SOAPMessageContext messageContext)
            throws SOAPClientException, SOAPFaultException {
        try {
            Envelope response = unmarshallResponse(httpMethod.getResponseBodyAsStream());
            messageContext.setInboundMessage(response);

            List<XMLObject> faults = response.getBody().getUnknownXMLObjects(Fault.DEFAULT_ELEMENT_NAME);
            if (faults.size() < 1) {
                throw new SOAPClientException("HTTP status code was 500 but SOAP response did not contain a Fault");
            }
            Fault fault = (Fault) faults.get(0);

            log.debug("SOAP fault code {} with message {}", fault.getCode().getValue(), fault.getMessage().getValue());
            SOAPFaultException faultException = new SOAPFaultException("SOAP Fault: " + fault.getCode().getValue()
                    + " Fault Message: " + fault.getMessage().getValue());
            faultException.setFault(fault);
            throw faultException;
        } catch (IOException e) {
            throw new SOAPClientException("Unable to read response", e);
        }
    }

    /**
     * Unmarshalls the incoming response from a POST request.
     * 
     * @param responseStream input stream bearing the response
     * 
     * @return the response
     * 
     * @throws SOAPClientException thrown if the incoming response can not be unmarshalled into an {@link Envelope}
     */
    protected Envelope unmarshallResponse(InputStream responseStream) throws SOAPClientException {
        try {
            Element responseElem = parserPool.parse(responseStream).getDocumentElement();
            if (log.isDebugEnabled()) {
                log.debug("Inbound SOAP message was:\n" + XMLHelper.prettyPrintXML(responseElem));
            }
            Unmarshaller unmarshaller = Configuration.getUnmarshallerFactory().getUnmarshaller(responseElem);
            return (Envelope) unmarshaller.unmarshall(responseElem);
        } catch (XMLParserException e) {
            throw new SOAPClientException("Unable to parse the XML within the response", e);
        } catch (UnmarshallingException e) {
            throw new SOAPClientException("unable to unmarshall the response DOM", e);
        }
    }

    /**
     * Evaluates the security policy associated with the given message context. If no policy resolver is registered or
     * no policy is located during the resolution process then no policy is evaluated. Note that neither the inbound or
     * outbound message transport is available.
     * 
     * @param messageContext current message context
     * 
     * @throws SOAPClientException thrown if there is a problem resolving or evaluating a security policy
     */
    protected void evaluateSecurityPolicy(SOAPMessageContext messageContext) throws SOAPClientException {
        SecurityPolicyResolver policyResolver = messageContext.getSecurityPolicyResolver();
        if (policyResolver == null) {
            return;
        }

        SecurityPolicy policy = null;
        try {
            policy = policyResolver.resolveSingle(messageContext);
            if (policy == null) {
                return;
            }
        } catch (SecurityException e) {
            throw new SOAPClientException("Unable to resolve security policy for inbound SOAP response", e);
        }

        try {
            log.debug("Evaluating security policy for inbound SOAP response");
            policy.evaluate(messageContext);
        } catch (SecurityException e) {
            throw new SOAPClientException("Inbound SOAP response does not meet security policy", e);
        }
    }
}