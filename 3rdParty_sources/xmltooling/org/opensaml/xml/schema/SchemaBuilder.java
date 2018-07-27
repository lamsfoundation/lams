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

package org.opensaml.xml.schema;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.opensaml.xml.parse.LoggingErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/** A helper class for building {@link Schema} from a set of input. */
public final class SchemaBuilder {

    /** Language of the schema files. */
    public static enum SchemaLanguage {

        /** W3 XML Schema. */
        XML("xsd"),

        /** OASIS RELAX NG Schema. */
        RELAX("rng");

        /** File extension used for the schema files. */
        private String schemaFileExtension;

        /**
         * Constructor.
         * 
         * @param extension file extension used for the schema files
         */
        private SchemaLanguage(String extension) {
            schemaFileExtension = extension;
        }

        /**
         * Gets the file extension used for the schema files.
         * 
         * @return file extension used for the schema files
         */
        public String getSchemaFileExtension() {
            return schemaFileExtension;
        }
    };
    
    /** Constructor. */
    private SchemaBuilder() {}

    /**
     * Builds a schema from the given schema source.
     * 
     * @param lang schema language, must not be null
     * @param schemaFileOrDirectory file or directory which contains schema sources
     * 
     * @return the constructed schema
     * 
     * @throws SAXException thrown if there is a problem converting the schema sources in to a schema
     */
    public static Schema buildSchema(SchemaLanguage lang, String schemaFileOrDirectory) throws SAXException {
        if(schemaFileOrDirectory == null){
            return null;
        }
        
        return buildSchema(lang, new File(schemaFileOrDirectory));
    }

    /**
     * Builds a schema from the given schema sources.
     * 
     * @param lang schema language, must not be null
     * @param schemaFilesOrDirectories files or directories which contains schema sources
     * 
     * @return the constructed schema
     * 
     * @throws SAXException thrown if there is a problem converting the schema sources in to a schema
     */
    public static Schema buildSchema(SchemaLanguage lang, String[] schemaFilesOrDirectories) throws SAXException {
        if(schemaFilesOrDirectories == null || schemaFilesOrDirectories.length == 0){
            return null;
        }
        
        return buildSchema(lang, schemaFilesOrDirectories);
    }

    /**
     * Builds a schema from the given schema source.
     * 
     * @param lang schema language, must not be null
     * @param schemaFileOrDirectory file or directory which contains schema sources
     * 
     * @return the constructed schema
     * 
     * @throws SAXException thrown if there is a problem converting the schema sources in to a schema
     */
    public static Schema buildSchema(SchemaLanguage lang, File schemaFileOrDirectory) throws SAXException {
        if(schemaFileOrDirectory == null){
            return null;
        }
        
        return buildSchema(lang, new File[]{schemaFileOrDirectory});
    }

    /**
     * Builds a schema from the given schema sources.
     * 
     * @param lang schema language, must not be null
     * @param schemaFilesOrDirectories files or directories which contains schema sources
     * 
     * @return the constructed schema
     * 
     * @throws SAXException thrown if there is a problem converting the schema sources in to a schema
     */
    public static Schema buildSchema(SchemaLanguage lang, File[] schemaFilesOrDirectories) throws SAXException {
        if(schemaFilesOrDirectories == null || schemaFilesOrDirectories.length == 0){
            return null;
        }
        
        ArrayList<File> schemaFiles = new ArrayList<File>();
        getSchemaFiles(lang, schemaFilesOrDirectories, schemaFiles);
        
        if(schemaFiles.isEmpty()){
            return null;
        }
        
        ArrayList<Source> schemaSources = new ArrayList<Source>();
        for(File schemaFile : schemaFiles){
            schemaSources.add(new StreamSource(schemaFile));
        }
        return buildSchema(lang, schemaSources.toArray(new Source[]{}));
    }

    /**
     * Builds a schema from the given schema source.
     * 
     * @param lang schema language, must not be null
     * @param schemaSource schema source
     * 
     * @return the constructed schema
     * 
     * @throws SAXException thrown if there is a problem converting the schema sources in to a schema
     */
    public static Schema buildSchema(SchemaLanguage lang, InputStream schemaSource) throws SAXException {
        if(schemaSource == null){
            return null;
        }
        
        return buildSchema(lang, new StreamSource[] { new StreamSource(schemaSource) });
    }

    /**
     * Builds a schema from the given schema sources.
     * 
     * @param lang schema language, must not be null
     * @param schemaSources schema sources
     * 
     * @return the constructed schema
     * 
     * @throws SAXException thrown if there is a problem converting the schema sources in to a schema
     */
    public static Schema buildSchema(SchemaLanguage lang, InputStream[] schemaSources) throws SAXException {
        if(schemaSources == null || schemaSources.length == 0){
            return null;
        }
        
        ArrayList<StreamSource> sources = new ArrayList<StreamSource>();
        for (InputStream schemaSource : schemaSources) {
            if (schemaSource == null) {
                continue;
            }
            sources.add(new StreamSource(schemaSource));
        }

        if (sources.isEmpty()) {
            return null;
        }

        return buildSchema(lang, sources.toArray(new Source[] {}));
    }

    /**
     * Gets all of the schema files in the given set of readable files, directories or subdirectories.
     * 
     * @param lang schema language, must not be null
     * @param schemaFilesOrDirectories files and directories which may contain schema files
     * @param accumulatedSchemaFiles list that accumulates the schema files
     */
    protected static void getSchemaFiles(SchemaLanguage lang, File[] schemaFilesOrDirectories,
            List<File> accumulatedSchemaFiles) {
        Logger log = getLogger();
        
        if(lang == null){
            throw new IllegalArgumentException("Schema language may not be null");
        }
        
        if (schemaFilesOrDirectories == null || schemaFilesOrDirectories.length == 0) {
            return;
        }

        for (File handle : schemaFilesOrDirectories) {
            if (handle == null) {
                continue;
            }

            if (!handle.canRead()) {
                log.debug("Ignoring '{}', no read permission", handle.getAbsolutePath());
            }

            if (handle.isFile() && handle.getName().endsWith(lang.getSchemaFileExtension())) {
                log.debug("Added schema source '{}'", handle.getAbsolutePath());
                accumulatedSchemaFiles.add(handle);
            }

            if (handle.isDirectory()) {
                getSchemaFiles(lang, handle.listFiles(), accumulatedSchemaFiles);
            }
        }
    }

    /**
     * Builds a schema from the given schema sources.
     * 
     * @param lang schema language, must not be null
     * @param schemaSources schema sources, must not be null
     * 
     * @return the constructed schema
     * 
     * @throws SAXException thrown if there is a problem converting the schema sources in to a schema
     */
    protected static Schema buildSchema(SchemaLanguage lang, Source[] schemaSources) throws SAXException {
        if(lang == null){
            throw new IllegalArgumentException("Schema language may not be null");
        }
        
        if(schemaSources == null){
            throw new IllegalArgumentException("Schema sources may not be null");
        }
        
        SchemaFactory schemaFactory;

        if (lang == SchemaLanguage.XML) {
            schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        } else {
            schemaFactory = SchemaFactory.newInstance(XMLConstants.RELAXNG_NS_URI);
        }

        schemaFactory.setErrorHandler(new LoggingErrorHandler(LoggerFactory.getLogger(SchemaBuilder.class)));
        return schemaFactory.newSchema(schemaSources);
    }
    
    /**
     * Get an SLF4J Logger.
     * 
     * @return a Logger instance
     */
    private static Logger getLogger() {
        return LoggerFactory.getLogger(SchemaBuilder.class);
    }
}