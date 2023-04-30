/*
 * XML Type:  CT_SchemaLibrary
 * Namespace: http://schemas.openxmlformats.org/schemaLibrary/2006/main
 * Java type: org.openxmlformats.schemas.schemaLibrary.x2006.main.CTSchemaLibrary
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.schemaLibrary.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_SchemaLibrary(@http://schemas.openxmlformats.org/schemaLibrary/2006/main).
 *
 * This is a complex type.
 */
public interface CTSchemaLibrary extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.schemaLibrary.x2006.main.CTSchemaLibrary> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctschemalibrary4028type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "schema" elements
     */
    java.util.List<org.openxmlformats.schemas.schemaLibrary.x2006.main.CTSchema> getSchemaList();

    /**
     * Gets array of all "schema" elements
     */
    org.openxmlformats.schemas.schemaLibrary.x2006.main.CTSchema[] getSchemaArray();

    /**
     * Gets ith "schema" element
     */
    org.openxmlformats.schemas.schemaLibrary.x2006.main.CTSchema getSchemaArray(int i);

    /**
     * Returns number of "schema" element
     */
    int sizeOfSchemaArray();

    /**
     * Sets array of all "schema" element
     */
    void setSchemaArray(org.openxmlformats.schemas.schemaLibrary.x2006.main.CTSchema[] schemaArray);

    /**
     * Sets ith "schema" element
     */
    void setSchemaArray(int i, org.openxmlformats.schemas.schemaLibrary.x2006.main.CTSchema schema);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "schema" element
     */
    org.openxmlformats.schemas.schemaLibrary.x2006.main.CTSchema insertNewSchema(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "schema" element
     */
    org.openxmlformats.schemas.schemaLibrary.x2006.main.CTSchema addNewSchema();

    /**
     * Removes the ith "schema" element
     */
    void removeSchema(int i);
}
