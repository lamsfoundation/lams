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

package org.opensaml.common.xml;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.opensaml.xml.parse.ClasspathResolver;
import org.opensaml.xml.parse.LoggingErrorHandler;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 * A convenience builder for creating {@link Schema}s for validating SAML 1_0, 1_1, and 2_0.
 * 
 * Additional schema may be registered by {@link #addExtensionSchema(String)} with the given argument a relative or
 * absolute path that will be resolved against the classpath. Note that relative paths are relative to <strong>this</strong>
 * class. Also, schema files must be provided in the order they are referenced, that is if schema B depends on schema A
 * then schema A must appear first in the list of registered extension schemas.
 * 
 * Schemas may use a schema location attribute. These schema locations will be resolved by the {@link ClasspathResolver}.
 * If schema locations are used they will be resolved and will meet the aformentioned schema ordering requirement.
 * 
 * The schema objects produced here are thread safe and should be re-used, to that end the schema builder will cache
 * created schema using {@link SoftReference}s, allowing the VM to reclaim the memory used by schemas if necessary.
 */
public final class SAMLSchemaBuilder {

    /** SAML 1_0 Schema with SAML 2_0 schemas and extensions. */
    private static SoftReference<Schema> saml10Schema;

    /** SAML 1_0 Schema with SAML 2_0 schemas and extensions. */
    private static SoftReference<Schema> saml11Schema;

    /** Classpath relative location of basic XML schemas. */
    private static String[] baseXMLSchemas = {
        "/schema/xml.xsd",
        "/schema/XMLSchema.xsd",
        "/schema/xmldsig-core-schema.xsd",
        "/schema/xenc-schema.xsd",
        "/schema/xmldsig11-schema.xsd",
        "/schema/xenc11-schema.xsd",
    };

    /** Classpath relative location of SOAP 1_1 schemas. */
    private static String[] soapSchemas = { "/schema/soap-envelope.xsd", };

    /** Classpath relative location of SAML 1_0 schemas. */
    private static String[] saml10Schemas = { "/schema/cs-sstc-schema-assertion-01.xsd",
            "/schema/cs-sstc-schema-protocol-01.xsd", };

    /** Classpath relative location of SAML 1_1 schemas. */
    private static String[] saml11Schemas = { "/schema/cs-sstc-schema-assertion-1.1.xsd",
            "/schema/cs-sstc-schema-protocol-1.1.xsd", };

    /** Classpath relative location of SAML 2_0 schemas. */
    private static String[] saml20Schemas = { 
        "/schema/saml-schema-assertion-2.0.xsd",
        "/schema/saml-schema-authn-context-2.0.xsd",
        "/schema/saml-schema-authn-context-auth-telephony-2.0.xsd",
        "/schema/saml-schema-authn-context-ip-2.0.xsd",
        "/schema/saml-schema-authn-context-ippword-2.0.xsd",
        "/schema/saml-schema-authn-context-kerberos-2.0.xsd",
        "/schema/saml-schema-authn-context-mobileonefactor-reg-2.0.xsd",
        "/schema/saml-schema-authn-context-mobileonefactor-unreg-2.0.xsd",
        "/schema/saml-schema-authn-context-mobiletwofactor-reg-2.0.xsd",
        "/schema/saml-schema-authn-context-mobiletwofactor-unreg-2.0.xsd",
        "/schema/saml-schema-authn-context-nomad-telephony-2.0.xsd",
        "/schema/saml-schema-authn-context-personal-telephony-2.0.xsd",
        "/schema/saml-schema-authn-context-pgp-2.0.xsd",
        "/schema/saml-schema-authn-context-ppt-2.0.xsd",
        "/schema/saml-schema-authn-context-pword-2.0.xsd",
        "/schema/saml-schema-authn-context-session-2.0.xsd",
        "/schema/saml-schema-authn-context-smartcard-2.0.xsd",
        "/schema/saml-schema-authn-context-smartcardpki-2.0.xsd",
        "/schema/saml-schema-authn-context-softwarepki-2.0.xsd",
        "/schema/saml-schema-authn-context-spki-2.0.xsd",
        "/schema/saml-schema-authn-context-srp-2.0.xsd",
        "/schema/saml-schema-authn-context-sslcert-2.0.xsd",
        "/schema/saml-schema-authn-context-telephony-2.0.xsd",
        "/schema/saml-schema-authn-context-timesync-2.0.xsd",
        "/schema/saml-schema-authn-context-types-2.0.xsd",
        "/schema/saml-schema-authn-context-x509-2.0.xsd",
        "/schema/saml-schema-authn-context-xmldsig-2.0.xsd",
        "/schema/saml-schema-dce-2.0.xsd",
        "/schema/saml-schema-ecp-2.0.xsd",
        "/schema/saml-schema-metadata-2.0.xsd",
        "/schema/saml-schema-protocol-2.0.xsd",
        "/schema/saml-schema-x500-2.0.xsd",
        "/schema/saml-schema-xacml-2.0.xsd",
    };

    /** Classpath relative location of SAML extension schemas. */
    private static String[] baseExtSchemas = {
        "/schema/sstc-saml1x-metadata.xsd",
        "/schema/sstc-saml-idp-discovery.xsd",
        "/schema/sstc-saml-protocol-ext-thirdparty.xsd",
        "/schema/sstc-saml-metadata-ext-query.xsd",
        "/schema/sstc-saml-metadata-ui-v1.0.xsd",
        "/schema/sstc-metadata-attr.xsd",
        "/schema/sstc-saml-delegation.xsd",
        "/schema/saml-metadata-rpi-v1.0.xsd",
        "/schema/sstc-saml-delegation.xsd",
        "/schema/sstc-saml-channel-binding-ext-v1.0.xsd",
        "/schema/saml-async-slo-v1.0.xsd",
        "/schema/ietf-kitten-sasl-saml-ec.xsd",
    };

    /** Additional schema locations relative to classpath. */
    private static List<String> extensionSchema = new ArrayList<String>();

    /** Constructor. */
    private SAMLSchemaBuilder() {

    }

    /**
     * Gets a schema that can validate SAML 1.0, 2.0, and all registered extensions.
     * 
     * @return schema that can validate SAML 1.0, 2.0, and all registered extensions
     * 
     * @throws SAXException thrown if a schema object can not be created
     */
    public static synchronized Schema getSAML10Schema() throws SAXException {
        if (saml10Schema == null || saml10Schema.get() == null) {
            saml10Schema = new SoftReference<Schema>(buildSchema(saml10Schemas));
        }

        return saml10Schema.get();
    }

    /**
     * Gets a schema that can validate SAML 1.1, 2.0, and all registered extensions.
     * 
     * @return schema that can validate SAML 1.1, 2.0, and all registered extensions
     * 
     * @throws SAXException thrown if a schema object can not be created
     */
    public static synchronized Schema getSAML11Schema() throws SAXException {
        if (saml11Schema == null || saml11Schema.get() == null) {
            saml11Schema = new SoftReference<Schema>(buildSchema(saml11Schemas));
        }

        return saml11Schema.get();
    }

    /**
     * Gets an unmodifiable list of currently registered schema extensions.
     * 
     * @return unmodifiable list of currently registered schema extensions
     */
    public static List<String> getExtensionSchema() {
        return Collections.unmodifiableList(extensionSchema);
    }

    /**
     * Registers a new schema extension. The schema location will be searched for on the classpath.
     * 
     * @param schema new schema extension
     */
    public static void addExtensionSchema(String schema) {
        extensionSchema.add(schema);

        saml10Schema = null;

        saml11Schema = null;
    }

    /**
     * Removes a currently registered schema.
     * 
     * @param schema currently registered schema
     */
    public static void removeSchema(String schema) {
        extensionSchema.remove(schema);

        synchronized (saml10Schema) {
            saml10Schema = null;
        }

        synchronized (saml11Schema) {
            saml11Schema = null;
        }
    }

    /**
     * Builds a schema object based on the given SAML 1_X schema set.
     * 
     * @param saml1Schema SAML 1_X schema set
     * 
     * @return constructed schema
     * 
     * @throws SAXException thrown if a schema object can not be created
     */
    private static Schema buildSchema(String[] saml1Schema) throws SAXException {
        Class<SAMLSchemaBuilder> clazz = SAMLSchemaBuilder.class;
        List<Source> schemaSources = new ArrayList<Source>();

        for (String source : baseXMLSchemas) {
            schemaSources.add(new StreamSource(clazz.getResourceAsStream(source)));
        }

        for (String source : soapSchemas) {
            schemaSources.add(new StreamSource(clazz.getResourceAsStream(source)));
        }

        for (String source : saml1Schema) {
            schemaSources.add(new StreamSource(clazz.getResourceAsStream(source)));
        }

        for (String source : saml20Schemas) {
            schemaSources.add(new StreamSource(clazz.getResourceAsStream(source)));
        }

        for (String source : baseExtSchemas) {
            schemaSources.add(new StreamSource(clazz.getResourceAsStream(source)));
        }

        for (String source : extensionSchema) {
            schemaSources.add(new StreamSource(clazz.getResourceAsStream(source)));
        }

        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        schemaFactory.setResourceResolver(new ClasspathResolver());
        schemaFactory.setErrorHandler(new LoggingErrorHandler(LoggerFactory.getLogger(clazz)));
        return schemaFactory.newSchema(schemaSources.toArray(new StreamSource[0]));
    }
}
