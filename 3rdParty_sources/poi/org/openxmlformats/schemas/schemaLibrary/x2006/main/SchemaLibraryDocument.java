/*
 * An XML document type.
 * Localname: schemaLibrary
 * Namespace: http://schemas.openxmlformats.org/schemaLibrary/2006/main
 * Java type: org.openxmlformats.schemas.schemaLibrary.x2006.main.SchemaLibraryDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.schemaLibrary.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one schemaLibrary(@http://schemas.openxmlformats.org/schemaLibrary/2006/main) element.
 *
 * This is a complex type.
 */
public interface SchemaLibraryDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.schemaLibrary.x2006.main.SchemaLibraryDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "schemalibraryec54doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "schemaLibrary" element
     */
    org.openxmlformats.schemas.schemaLibrary.x2006.main.CTSchemaLibrary getSchemaLibrary();

    /**
     * Sets the "schemaLibrary" element
     */
    void setSchemaLibrary(org.openxmlformats.schemas.schemaLibrary.x2006.main.CTSchemaLibrary schemaLibrary);

    /**
     * Appends and returns a new empty "schemaLibrary" element
     */
    org.openxmlformats.schemas.schemaLibrary.x2006.main.CTSchemaLibrary addNewSchemaLibrary();
}
