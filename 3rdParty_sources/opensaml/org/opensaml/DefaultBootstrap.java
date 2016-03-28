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

package org.opensaml;

import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.xml.security.Init;
import org.opensaml.saml1.binding.artifact.SAML1ArtifactBuilderFactory;
import org.opensaml.saml2.binding.artifact.SAML2ArtifactBuilderFactory;
import org.opensaml.ws.soap.client.http.TLSProtocolSocketFactory;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.XMLConfigurator;
import org.opensaml.xml.parse.StaticBasicParserPool;
import org.opensaml.xml.parse.XMLParserException;
import org.opensaml.xml.security.DefaultSecurityConfigurationBootstrap;
import org.opensaml.xml.security.x509.tls.StrictHostnameVerifier;
import org.owasp.esapi.ESAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class can be used to bootstrap the OpenSAML library with the default configurations that ship with the library.
 */
public class DefaultBootstrap {
    
    /** System property used to disable global default HTTPS hostname verification in Apache Commons HttpClient. */
    public static final String SYSPROP_HTTPCLIENT_HTTPS_DISABLE_HOSTNAME_VERIFICATION = 
            "org.opensaml.httpclient.https.disableHostnameVerification";

    /** List of default XMLTooling configuration files. */
    private static String[] xmlToolingConfigs = { 
        "/default-config.xml", 
        "/schema-config.xml", 
        "/signature-config.xml",
        "/signature-validation-config.xml", 
        "/encryption-config.xml", 
        "/encryption-validation-config.xml",
        "/soap11-config.xml", 
        "/wsfed11-protocol-config.xml",
        "/saml1-assertion-config.xml", 
        "/saml1-protocol-config.xml",
        "/saml1-core-validation-config.xml", 
        "/saml2-assertion-config.xml", 
        "/saml2-protocol-config.xml",
        "/saml2-core-validation-config.xml", 
        "/saml1-metadata-config.xml", 
        "/saml2-metadata-config.xml",
        "/saml2-metadata-validation-config.xml", 
        "/saml2-metadata-attr-config.xml",
        "/saml2-metadata-idp-discovery-config.xml",
        "/saml2-metadata-ui-config.xml",
        "/saml2-protocol-aslo-config.xml",
        "/saml2-protocol-thirdparty-config.xml",
        "/saml2-metadata-query-config.xml", 
        "/saml2-assertion-delegation-restriction-config.xml",    
        "/saml2-ecp-config.xml",
        "/saml2-channel-binding-config.xml",
        "/saml-ec-gss-config.xml",
        "/xacml10-saml2-profile-config.xml",
        "/xacml11-saml2-profile-config.xml",
        "/xacml20-context-config.xml",
        "/xacml20-policy-config.xml",
        "/xacml2-saml2-profile-config.xml",
        "/xacml3-saml2-profile-config.xml",    
        "/wsaddressing-config.xml",
        "/wssecurity-config.xml",
        "/wstrust-config.xml",
        "/wspolicy-config.xml",
    };

    /** Constructor. */
    protected DefaultBootstrap() {

    }

    /**
     * Initializes the OpenSAML library, loading default configurations.
     * 
     * @throws ConfigurationException thrown if there is a problem initializing the OpenSAML library
     */
    public static synchronized void bootstrap() throws ConfigurationException {

        initializeXMLSecurity();

        initializeXMLTooling();

        initializeArtifactBuilderFactories();

        initializeGlobalSecurityConfiguration();
        
        initializeParserPool();
        
        initializeESAPI();
        
        initializeHttpClient();
    }

    /**
     *  Initializes the Apache Commons HttpClient library.
     */
    protected static void initializeHttpClient() {
        if (!Boolean.getBoolean(SYSPROP_HTTPCLIENT_HTTPS_DISABLE_HOSTNAME_VERIFICATION)) {
            ProtocolSocketFactory socketFactory = 
                    new TLSProtocolSocketFactory(null, null, new StrictHostnameVerifier());
            Protocol.registerProtocol("https", new Protocol("https", socketFactory, 443));
        }
    }

    /**
     * Initializes the OWASPI ESAPI library.
     */
    protected static void initializeESAPI() {
        Logger log = getLogger();
        String systemPropertyKey = "org.owasp.esapi.SecurityConfiguration";
        String opensamlConfigImpl = ESAPISecurityConfig.class.getName();
        
        String currentValue = System.getProperty(systemPropertyKey);
        if (currentValue == null || currentValue.isEmpty()) {
            log.debug("Setting ESAPI SecurityConfiguration impl to OpenSAML internal class: {}", opensamlConfigImpl);
            System.setProperty(systemPropertyKey, opensamlConfigImpl);
            // We still need to call ESAPI.initialize() despite setting the system property, b/c within the ESAPI class
            // the property is only evaluated once in a static initializer and stored. The initialize method however
            // does overwrite the statically-set value from the system property. But still set the system property for 
            // consistency, so other callers can see what has been set.
            ESAPI.initialize(opensamlConfigImpl);
        } else {
            log.debug("ESAPI SecurityConfiguration impl was already set non-null and non-empty via system property, leaving existing value in place: {}",
                    currentValue);
        }
    }

    /**
     * Initializes the default global parser pool instance.
     * 
     * <p>
     * The ParserPool configured by default here is an instance of
     * {@link StaticBasicParserPool}, with a maxPoolSize property of 50 
     * and all other properties with default values.
     * </p>
     * 
     * <p>
     * If a deployment wishes to use a different parser pool implementation,
     * or one configured with different characteristics, they may either override this method,
     * or simply configure a different ParserPool after bootstrapping via 
     * {@link Configuration#setParserPool(org.opensaml.xml.parse.ParserPool)}.
     * </p>
     * 
     * @throws ConfigurationException thrown if there is a problem initializing the parser pool
     */
    protected static void initializeParserPool() throws ConfigurationException {
        StaticBasicParserPool pp = new StaticBasicParserPool();
        pp.setMaxPoolSize(50);
        try {
            pp.initialize();
        } catch (XMLParserException e) {
            throw new ConfigurationException("Error initializing parser pool", e);
        }
        Configuration.setParserPool(pp);
    }

    /**
     * Initializes the default global security configuration.
     */
    protected static void initializeGlobalSecurityConfiguration() {
        Configuration.setGlobalSecurityConfiguration(DefaultSecurityConfigurationBootstrap.buildDefaultConfig());
    }

    /**
     * Initializes the Apache XMLSecurity libary.
     * 
     * @throws ConfigurationException thrown is there is a problem initializing the library
     */
    protected static void initializeXMLSecurity() throws ConfigurationException {
        Logger log = getLogger();
        String lineBreakPropName = "org.apache.xml.security.ignoreLineBreaks";
        // Don't override if it was set explicitly
        if (System.getProperty(lineBreakPropName) == null) {
            System.setProperty(lineBreakPropName, "true");
        }
        if (!Init.isInitialized()) {
            log.debug("Initializing Apache XMLSecurity library");
            Init.init();
        }
    }
    
    /**
     * Initializes the XMLTooling library with a default set of object providers.
     * 
     * @throws ConfigurationException thrown if there is a problem loading the configuration files
     */
    protected static void initializeXMLTooling() throws ConfigurationException {
        initializeXMLTooling(xmlToolingConfigs);
    }

    /**
     * Initializes the XMLTooling library with an explicitly supplied set of object providers.
     * 
     * @param providerConfigs list of provider configuration files located on the classpath
     * 
     * @throws ConfigurationException thrown if there is a problem loading the configuration files
     */
    protected static void initializeXMLTooling(String[] providerConfigs) throws ConfigurationException {
        Logger log = getLogger();
        Class clazz = Configuration.class;
        XMLConfigurator configurator = new XMLConfigurator();

        for (String config : providerConfigs) {
            log.debug("Loading XMLTooling configuration {}", config);
            configurator.load(clazz.getResourceAsStream(config));
        }
    }

    /**
     * Initializes the artifact factories for SAML 1 and SAML 2 artifacts.
     * 
     * @throws ConfigurationException thrown if there is a problem initializing the artifact factory
     */
    protected static void initializeArtifactBuilderFactories() throws ConfigurationException {
        Logger log = getLogger();
        log.debug("Initializing SAML Artifact builder factories");
        Configuration.setSAML1ArtifactBuilderFactory(new SAML1ArtifactBuilderFactory());
        Configuration.setSAML2ArtifactBuilderFactory(new SAML2ArtifactBuilderFactory());
    }
    
    /**
     * Get an SLF4J Logger.
     * 
     * @return a Logger instance
     */
    protected static Logger getLogger() {
        return LoggerFactory.getLogger(DefaultBootstrap.class);
    }
}