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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;

import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.io.Unmarshaller;
import org.opensaml.xml.parse.BasicParserPool;
import org.opensaml.xml.parse.XMLParserException;
import org.opensaml.xml.util.DatatypeHelper;
import org.opensaml.xml.util.XMLConstants;
import org.opensaml.xml.util.XMLHelper;
import org.opensaml.xml.validation.Validator;
import org.opensaml.xml.validation.ValidatorSuite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Reads in an XML configuration and configures the XMLTooling library accordingly.
 */
public class XMLConfigurator {

    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(XMLConfigurator.class);
    
    /** Whether the XML configuration elements that configured object providers should be retained. */
    private boolean retainXMLConfiguration;

    /** Pool of parsers used to read and validate configurations. */
    private BasicParserPool parserPool;

    /** Schema used to validate configruation files. */
    private Schema configurationSchema;

    /**
     * Constructor.
     * 
     * @throws ConfigurationException thrown if the validation schema for configuration files can not be created
     */
    public XMLConfigurator() throws ConfigurationException {
        this(false);
    }
    
    /**
     * Constructor.
     * 
     * @param retainXML whether to retain the XML configuration elements within the {@link Configuration}.
     * 
     * @throws ConfigurationException thrown if the validation schema for configuration files can not be created
     * 
     * @deprecated this method will be removed once {@link Configuration} no longer has the option to store the XML configuration fragements
     */
    public XMLConfigurator(boolean retainXML) throws ConfigurationException {
        retainXMLConfiguration = retainXML;
        parserPool = new BasicParserPool();
        SchemaFactory factory = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Source schemaSource = new StreamSource(XMLConfigurator.class
                .getResourceAsStream(XMLConstants.XMLTOOLING_SCHEMA_LOCATION));
        try {
            configurationSchema = factory.newSchema(schemaSource);

            parserPool.setIgnoreComments(true);
            parserPool.setIgnoreElementContentWhitespace(true);
            parserPool.setSchema(configurationSchema);
        } catch (SAXException e) {
            throw new ConfigurationException("Unable to read XMLTooling configuration schema", e);
        }
    }

    /**
     * Loads the configuration file(s) from the given file. If the file is a directory each file within the directory is
     * loaded.
     * 
     * @param configurationFile the configuration file(s) to be loaded
     * 
     * @throws ConfigurationException thrown if the configuration file(s) can not be be read or invalid
     */
    public void load(File configurationFile) throws ConfigurationException {
        if (configurationFile == null || !configurationFile.canRead()) {
            log.error("Unable to read configuration file {}", configurationFile);
        }

        try {
            if (configurationFile.isDirectory()) {
                File[] configurations = configurationFile.listFiles();
                for (int i = 0; i < configurations.length; i++) {
                    log.debug("Parsing configuration file {}", configurations[i].getAbsolutePath());
                    load(new FileInputStream(configurations[i]));
                }
            } else {
                // Given file is not a directory so try to load it directly
                log.debug("Parsing configuration file {}", configurationFile.getAbsolutePath());
                load(new FileInputStream(configurationFile));
            }
        } catch (FileNotFoundException e) {
            // ignore, we already have the files
        }
    }

    /**
     * Loads a configuration file from an input stream.
     * 
     * @param configurationStream configuration stream
     * 
     * @throws ConfigurationException thrown if the given configuration is invalid or can not be read
     */
    public void load(InputStream configurationStream) throws ConfigurationException {
        try {
            Document configuration = parserPool.parse(configurationStream);
            load(configuration);
        } catch (XMLParserException e) {
            log.error("Invalid configuration file", e);
            throw new ConfigurationException("Unable to create DocumentBuilder", e);
        }

    }

    /**
     * Loads the configuration docuement.
     * 
     * @param configuration the configurationd document
     * @throws ConfigurationException thrown if the configuration file(s) can not be be read or invalid
     */
    public void load(Document configuration) throws ConfigurationException {
        log.debug("Loading configuration from XML Document");
        log.trace("{}", XMLHelper.nodeToString(configuration.getDocumentElement()));

        // Schema validation
        log.debug("Schema validating configuration Document");
        validateConfiguration(configuration);
        log.debug("Configuration document validated");

        load(configuration.getDocumentElement());
    }

    /**
     * Loads a configuration after it's been schema validated.
     * 
     * @param configurationRoot root of the configuration
     * 
     * @throws ConfigurationException thrown if there is a problem processing the configuration
     */
    protected void load(Element configurationRoot) throws ConfigurationException {
        // Initialize object providers
        NodeList objectProviders = configurationRoot.getElementsByTagNameNS(XMLConstants.XMLTOOLING_CONFIG_NS,
                "ObjectProviders");
        if (objectProviders.getLength() > 0) {
            log.debug("Preparing to load ObjectProviders");
            initializeObjectProviders((Element) objectProviders.item(0));
            log.debug("ObjectProviders load complete");
        }

        // Initialize validator suites
        NodeList validatorSuitesNodes = configurationRoot.getElementsByTagNameNS(XMLConstants.XMLTOOLING_CONFIG_NS,
                "ValidatorSuites");
        if (validatorSuitesNodes.getLength() > 0) {
            log.debug("Preparing to load ValidatorSuites");
            initializeValidatorSuites((Element) validatorSuitesNodes.item(0));
            log.debug("ValidatorSuites load complete");
        }

        // Initialize ID attributes
        NodeList idAttributesNodes = configurationRoot.getElementsByTagNameNS(XMLConstants.XMLTOOLING_CONFIG_NS,
                "IDAttributes");
        if (idAttributesNodes.getLength() > 0) {
            log.debug("Preparing to load IDAttributes");
            initializeIDAttributes((Element) idAttributesNodes.item(0));
            log.debug("IDAttributes load complete");
        }
    }

    /**
     * Intializes the object providers defined in the configuration file.
     * 
     * @param objectProviders the configuration for the various object providers
     * 
     * @throws ConfigurationException thrown if the configuration elements are invalid
     */
    protected void initializeObjectProviders(Element objectProviders) throws ConfigurationException {
        // Process ObjectProvider child elements
        Element objectProvider;
        Attr qNameAttrib;
        QName objectProviderName;
        Element configuration;
        XMLObjectBuilder builder;
        Marshaller marshaller;
        Unmarshaller unmarshaller;

        NodeList providerList = objectProviders.getElementsByTagNameNS(XMLConstants.XMLTOOLING_CONFIG_NS,
                "ObjectProvider");
        for (int i = 0; i < providerList.getLength(); i++) {
            objectProvider = (Element) providerList.item(i);

            // Get the element name of type this object provider is for
            qNameAttrib = objectProvider.getAttributeNodeNS(null, "qualifiedName");
            objectProviderName = XMLHelper.getAttributeValueAsQName(qNameAttrib);

            log.debug("Initializing object provider {}", objectProviderName);

            try {
                configuration = (Element) objectProvider.getElementsByTagNameNS(XMLConstants.XMLTOOLING_CONFIG_NS,
                        "BuilderClass").item(0);
                builder = (XMLObjectBuilder) createClassInstance(configuration);

                configuration = (Element) objectProvider.getElementsByTagNameNS(XMLConstants.XMLTOOLING_CONFIG_NS,
                        "MarshallingClass").item(0);
                marshaller = (Marshaller) createClassInstance(configuration);

                configuration = (Element) objectProvider.getElementsByTagNameNS(XMLConstants.XMLTOOLING_CONFIG_NS,
                        "UnmarshallingClass").item(0);
                unmarshaller = (Unmarshaller) createClassInstance(configuration);

                if(retainXMLConfiguration){
                Configuration.registerObjectProvider(objectProviderName, builder, marshaller, unmarshaller,
                        objectProvider);
                }else{
                    Configuration.registerObjectProvider(objectProviderName, builder, marshaller, unmarshaller);
                }

                log.debug("{} intialized and configuration cached", objectProviderName);
            } catch (ConfigurationException e) {
                log.error("Error initializing object provier " + objectProvider, e);
                // clean up any parts of the object provider that might have been registered before the failure
                Configuration.deregisterObjectProvider(objectProviderName);
                throw e;
            }
        }
    }

    /**
     * Initializes the validator suites specified in the configuration file.
     * 
     * @param validatorSuitesElement the ValidatorSuites element from the configuration file
     * 
     * @throws ConfigurationException thrown if there is a problem initializing the validator suites, usually because of
     *             malformed elements
     */
    protected void initializeValidatorSuites(Element validatorSuitesElement) throws ConfigurationException {
        ValidatorSuite validatorSuite;
        Validator validator;
        Element validatorSuiteElement;
        String validatorSuiteId;
        Element validatorElement;
        QName validatorQName;

        NodeList validatorSuiteList = validatorSuitesElement.getElementsByTagNameNS(XMLConstants.XMLTOOLING_CONFIG_NS,
                "ValidatorSuite");
        for (int i = 0; i < validatorSuiteList.getLength(); i++) {
            validatorSuiteElement = (Element) validatorSuiteList.item(i);
            validatorSuiteId = validatorSuiteElement.getAttributeNS(null, "id");
            validatorSuite = new ValidatorSuite(validatorSuiteId);

            log.debug("Initializing ValidatorSuite {}", validatorSuiteId);
            log.trace(XMLHelper.nodeToString(validatorSuiteElement));

            NodeList validatorList = validatorSuiteElement.getElementsByTagNameNS(XMLConstants.XMLTOOLING_CONFIG_NS,
                    "Validator");
            for (int j = 0; j < validatorList.getLength(); j++) {
                validatorElement = (Element) validatorList.item(j);
                validatorQName = XMLHelper.getAttributeValueAsQName(validatorElement.getAttributeNodeNS(null,
                        "qualifiedName"));

                validator = (Validator) createClassInstance(validatorElement);
                validatorSuite.registerValidator(validatorQName, validator);
            }

            log.debug("ValidtorSuite {} has been initialized", validatorSuiteId);
            if(retainXMLConfiguration){
                Configuration.registerValidatorSuite(validatorSuiteId, validatorSuite, validatorSuiteElement);
            }else{
                Configuration.registerValidatorSuite(validatorSuiteId, validatorSuite);
            }
        }
    }

    /**
     * Registers the global ID attributes specified in the configuration file.
     * 
     * @param idAttributesElement the IDAttributes element from the configuration file
     * 
     * @throws ConfigurationException thrown if there is a problem with a parsing or registering the the ID attribute
     */
    protected void initializeIDAttributes(Element idAttributesElement) throws ConfigurationException {
        Element idAttributeElement;
        QName attributeQName;

        NodeList idAttributeList = idAttributesElement.getElementsByTagNameNS(XMLConstants.XMLTOOLING_CONFIG_NS,
                "IDAttribute");

        for (int i = 0; i < idAttributeList.getLength(); i++) {
            idAttributeElement = (Element) idAttributeList.item(i);
            attributeQName = XMLHelper.getElementContentAsQName(idAttributeElement);
            if (attributeQName == null) {
                log.debug("IDAttribute element was empty, no registration performed");
            } else {
                Configuration.registerIDAttribute(attributeQName);
                log.debug("IDAttribute {} has been registered", attributeQName);
            }
        }
    }

    /**
     * Constructs an instance of the given class.
     * 
     * @param configuration the current configuration element
     * 
     * @return an instance of the given class
     * 
     * @throws ConfigurationException thrown if the class can not be instaniated
     */
    protected Object createClassInstance(Element configuration) throws ConfigurationException {
        String className = configuration.getAttributeNS(null, "className");
        className = DatatypeHelper.safeTrimOrNullString(className);

        if (className == null) {
            return null;
        }

        try {
            log.trace("Creating instance of {}", className);
            ClassLoader classLoader = this.getClass().getClassLoader();
            if (classLoader == null) {
                classLoader = ClassLoader.getSystemClassLoader();
            }
            Class clazz = classLoader.loadClass(className);
            Constructor constructor = clazz.getConstructor();
            return constructor.newInstance();
        } catch (Exception e) {
            log.error("Can not create instance of " + className, e);
            throw new ConfigurationException("Can not create instance of " + className, e);
        }
    }

    /**
     * Schema validates the given configuration.
     * 
     * @param configuration the configuration to validate
     * 
     * @throws ConfigurationException thrown if the configuration is not schema-valid
     */
    protected void validateConfiguration(Document configuration) throws ConfigurationException {
        try {
            javax.xml.validation.Validator schemaValidator = configurationSchema.newValidator();
            schemaValidator.validate(new DOMSource(configuration));
        } catch (IOException e) {
            // Should never get here as the DOM is already in memory
            String errorMsg = "Unable to read configuration file DOM";
            log.error(errorMsg, e);
            throw new ConfigurationException(errorMsg, e);
        } catch (SAXException e) {
            String errorMsg = "Configuration file does not validate against schema";
            log.error(errorMsg, e);
            throw new ConfigurationException(errorMsg, e);
        }
    }
}