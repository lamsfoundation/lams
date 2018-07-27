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

package org.opensaml.xml;

import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;

import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.io.MarshallerFactory;
import org.opensaml.xml.io.Unmarshaller;
import org.opensaml.xml.io.UnmarshallerFactory;
import org.opensaml.xml.parse.ParserPool;
import org.opensaml.xml.security.SecurityConfiguration;
import org.opensaml.xml.util.XMLConstants;
import org.opensaml.xml.validation.ValidatorSuite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

/** Class for loading library configuration files and retrieving the configured components. */
public class Configuration {

    /** Default object provider. */
    private static QName defaultProvider = new QName(XMLConstants.XMLTOOLING_CONFIG_NS,
            XMLConstants.XMLTOOLING_DEFAULT_OBJECT_PROVIDER);

    /** Object provider configuration elements indexed by QName. */
    private static Map<QName, Element> configuredObjectProviders = new ConcurrentHashMap<QName, Element>(0);

    /** Validator suite configuration elements indexed by suite IDs. */
    private static Map<String, Element> validatorSuiteConfigurations = new ConcurrentHashMap<String, Element>(0);

    /** Configured XMLObject builder factory. */
    private static XMLObjectBuilderFactory builderFactory = new XMLObjectBuilderFactory();

    /** Configured XMLObject marshaller factory. */
    private static MarshallerFactory marshallerFactory = new MarshallerFactory();

    /** Configured XMLObject unmarshaller factory. */
    private static UnmarshallerFactory unmarshallerFactory = new UnmarshallerFactory();

    /** Configured ValidatorSuites. */
    private static Map<String, ValidatorSuite> validatorSuites = new ConcurrentHashMap<String, ValidatorSuite>(5);

    /** Configured set of attribute QNames which have been globally registered as having an ID type. */
    private static Set<QName> idAttributeNames = new CopyOnWriteArraySet<QName>();

    /** Configured global security configuration information. */
    private static SecurityConfiguration globalSecurityConfig;

    /** Configured parser pool. */
    private static ParserPool parserPool;

    /** Constructor. */
    protected Configuration() {

    }
    
    /**
     * Get the currently configured ParserPool instance.
     * 
     * @return the currently ParserPool
     */
    public static ParserPool getParserPool() {
        return parserPool;
    }

    /**
     * Set the currently configured ParserPool instance.
     * 
     * @param newParserPool the new ParserPool instance to configure
     */
    public static void setParserPool(ParserPool newParserPool) {
        parserPool = newParserPool;
    }
    
    /**
     * Gets the QName for the object provider that will be used for XMLObjects that do not have a registered object
     * provider.
     * 
     * @return the QName for the default object provider
     */
    public static QName getDefaultProviderQName() {
        return defaultProvider;
    }

    /**
     * Adds an object provider to this configuration.
     * 
     * @param providerName the name of the object provider, corresponding to the element name or type name that the
     *            builder, marshaller, and unmarshaller operate on
     * @param builder the builder for that given provider
     * @param marshaller the marshaller for the provider
     * @param unmarshaller the unmarshaller for the provider
     */
    public static void registerObjectProvider(QName providerName, XMLObjectBuilder builder, Marshaller marshaller,
            Unmarshaller unmarshaller) {
        Logger log = getLogger();
        log.debug("Registering new builder, marshaller, and unmarshaller for {}", providerName);
        builderFactory.registerBuilder(providerName, builder);
        marshallerFactory.registerMarshaller(providerName, marshaller);
        unmarshallerFactory.registerUnmarshaller(providerName, unmarshaller);
    }

    /**
     * Removes the builder, marshaller, and unmarshaller registered to the given key.
     * 
     * @param key the key of the builder, marshaller, and unmarshaller to be removed
     */
    public static void deregisterObjectProvider(QName key) {
        Logger log = getLogger();
        log.debug("Unregistering builder, marshaller, and unmarshaller for {}", key);
        configuredObjectProviders.remove(key);
        builderFactory.deregisterBuilder(key);
        marshallerFactory.deregisterMarshaller(key);
        unmarshallerFactory.deregisterUnmarshaller(key);
    }

    /**
     * Gets the XMLObject builder factory that has been configured with information from loaded configuration files.
     * 
     * @return the XMLObject builder factory
     */
    public static XMLObjectBuilderFactory getBuilderFactory() {
        return builderFactory;
    }

    /**
     * Gets the XMLObject marshaller factory that has been configured with information from loaded configuration files.
     * 
     * @return the XMLObject marshaller factory
     */
    public static MarshallerFactory getMarshallerFactory() {
        return marshallerFactory;
    }

    /**
     * Gets the XMLObject unmarshaller factory that has been configured with information from loaded configuration
     * files.
     * 
     * @return the XMLObject unmarshaller factory
     */
    public static UnmarshallerFactory getUnmarshallerFactory() {
        return unmarshallerFactory;
    }

    /**
     * Registers a configured validator suite.
     * 
     * @param suiteId the ID of the suite
     * @param suite the configured suite
     */
    public static void registerValidatorSuite(String suiteId, ValidatorSuite suite) {
        validatorSuites.put(suiteId, suite);
    }

    /**
     * Removes a registered validator suite.
     * 
     * @param suiteId the ID of the suite
     */
    public static void deregisterValidatorSuite(String suiteId) {
        validatorSuiteConfigurations.remove(suiteId);
        validatorSuites.remove(suiteId);
    }

    /**
     * Gets a configured ValidatorSuite by its ID.
     * 
     * @param suiteId the suite's ID
     * 
     * @return the ValidatorSuite or null if no suite was registered under that ID
     */
    public static ValidatorSuite getValidatorSuite(String suiteId) {
        return validatorSuites.get(suiteId);
    }

    /**
     * Register an attribute as having a type of ID.
     * 
     * @param attributeName the QName of the ID attribute to be registered
     */
    public static void registerIDAttribute(QName attributeName) {
        if (!idAttributeNames.contains(attributeName)) {
            idAttributeNames.add(attributeName);
        }
    }

    /**
     * Deregister an attribute as having a type of ID.
     * 
     * @param attributeName the QName of the ID attribute to be de-registered
     */
    public static void deregisterIDAttribute(QName attributeName) {
        if (idAttributeNames.contains(attributeName)) {
            idAttributeNames.remove(attributeName);
        }
    }

    /**
     * Determine whether a given attribute is registered as having an ID type.
     * 
     * @param attributeName the QName of the attribute to be checked for ID type.
     * @return true if attribute is registered as having an ID type.
     */
    public static boolean isIDAttribute(QName attributeName) {
        return idAttributeNames.contains(attributeName);
    }

    /**
     * Get the global security configuration.
     * 
     * @return the global security configuration instance
     */
    public static SecurityConfiguration getGlobalSecurityConfiguration() {
        return globalSecurityConfig;
    }

    /**
     * Set the global security configuration.
     * 
     * @param config the new global security configuration instance
     */
    public static void setGlobalSecurityConfiguration(SecurityConfiguration config) {
        globalSecurityConfig = config;
    }

    /**
     * Validates that the system is not using the horribly buggy Sun JAXP implementation.
     */
    public static void validateNonSunJAXP() {
        Logger log = getLogger();
        String builderFactoryClass = DocumentBuilderFactory.newInstance().getClass().getName();
        log.debug("VM using JAXP parser {}", builderFactoryClass);

        if (builderFactoryClass.startsWith("com.sun")) {
            String errorMsg = "\n\n\nOpenSAML requires an xml parser that supports JAXP 1.3 and DOM3.\n"
                    + "The JVM is currently configured to use the Sun XML parser, which is known\n"
                    + "to be buggy and can not be used with OpenSAML.  Please endorse a functional\n"
                    + "JAXP library(ies) such as Xerces and Xalan.  For instructions on how to endorse\n"
                    + "a new parser see http://java.sun.com/j2se/1.5.0/docs/guide/standards/index.html\n\n\n";

            log.error(errorMsg);
            throw new Error(errorMsg);
        }
    }

    /**
     * Validates that the set of security providers configured in the JVM supports required cryptographic capabilities,
     * for example for the XML Encryption and XML Signature specifications.
     * 
     * Depending on the requirements of the calling code, failure to fully support encryption and signature requirements
     * may or may not be significant, so return a status flag to let the caller make that determination.
     * 
     * @return false if one or more capablities are not present, otherwise true
     */
    public static boolean validateJCEProviders() {
        Logger log = getLogger();
        boolean ret = true;

        // XML Encryption spec requires AES support (128 and 256).
        // Some JRE's are known to ship with no JCE's that support
        // the ISO10126Padding padding scheme.

        String errorMsgAESPadding = "The JCE providers currently configured in the JVM do not support\n"
                + "required capabilities for XML Encryption, either the 'AES' cipher algorithm\n"
                + "or the 'ISO10126Padding' padding scheme\n";

        try {
            Cipher.getInstance("AES/CBC/ISO10126Padding");
        } catch (NoSuchAlgorithmException e) {
            // IBM JCE returns this as the top-level exception even for the unsupported padding case. :-(
            // Otherwise would be nice to make the error msg more specific.
            log.warn(errorMsgAESPadding);
            ret = false;
        } catch (NoSuchPaddingException e) {
            log.warn(errorMsgAESPadding);
            ret = false;
        }

        // Could do more tests here as needed.

        return ret;
    }

    /**
     * Adds an object provider to this configuration.
     * 
     * @param providerName the name of the object provider, corresponding to the element name or type name that the
     *            builder, marshaller, and unmarshaller operate on
     * @param builder the builder for that given provider
     * @param marshaller the marshaller for the provider
     * @param unmarshaller the unmarshaller for the provider
     * @param configuration optional XML configuration snippet
     * 
     * @deprecated this method is deprecated with no replacement
     */
    public static void registerObjectProvider(QName providerName, XMLObjectBuilder builder, Marshaller marshaller,
            Unmarshaller unmarshaller, Element configuration) {
        Logger log = getLogger();
        log.debug("Registering new builder, marshaller, and unmarshaller for {}", providerName);
        if (configuration != null) {
            configuredObjectProviders.put(providerName, configuration);
        }
        builderFactory.registerBuilder(providerName, builder);
        marshallerFactory.registerMarshaller(providerName, marshaller);
        unmarshallerFactory.registerUnmarshaller(providerName, unmarshaller);
    }

    /**
     * Gets a clone of the configuration element for a qualified element. Note that this configuration reflects the
     * state of things as they were when the configuration was loaded, applications may have programmatically removed
     * builder, marshallers, and unmarshallers during runtime.
     * 
     * @param qualifedName the namespace qualifed element name of the schema type of the object provider
     * 
     * @return the object provider configuration element or null if no object provider is configured with that name
     * 
     * @deprecated this method is deprecated with no replacement
     */
    public static Element getObjectProviderConfiguration(QName qualifedName) {
        Element configElement = configuredObjectProviders.get(qualifedName);
        if (configElement != null) {
            return (Element) configElement.cloneNode(true);
        }
        return null;
    }

    /**
     * Registers a configured validator suite.
     * 
     * @param suiteId the ID of the suite
     * @param suite the configured suite
     * @param configuration optional XML configuration information
     * 
     * @deprecated this method is deprecated with no replacement
     */
    public static void registerValidatorSuite(String suiteId, ValidatorSuite suite, Element configuration) {
        if (configuration != null) {
            validatorSuiteConfigurations.put(suiteId, configuration);
        }
        validatorSuites.put(suiteId, suite);
    }

    /**
     * Gets a clone of the ValidatorSuite configuration element for the ID. Note that this configuration reflects the
     * state of things as they were when the configuration was loaded, applications may have programmatically removed
     * altered the suite during runtime.
     * 
     * @param suiteId the ID of the ValidatorSuite whose configuration is to be retrieved
     * 
     * @return the validator suite configuration element or null if no suite is configured with that ID
     * 
     * @deprecated this method is deprecated with no replacement
     */
    public static Element getValidatorSuiteConfiguration(String suiteId) {
        Element configElement = validatorSuiteConfigurations.get(suiteId);
        if (configElement != null) {
            return (Element) configElement.cloneNode(true);
        }

        return null;
    }
    
    /**
     * Get an SLF4J Logger.
     * 
     * @return a Logger instance
     */
    private static Logger getLogger() {
        return LoggerFactory.getLogger(Configuration.class);
    }

    static {
        validateJCEProviders();

        // Default to registering the xml:id attribute as an ID type for all configurations
        registerIDAttribute(new QName(javax.xml.XMLConstants.XML_NS_URI, "id"));
    }
}