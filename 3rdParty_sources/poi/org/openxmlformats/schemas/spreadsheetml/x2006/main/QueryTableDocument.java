/*
 * An XML document type.
 * Localname: queryTable
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.QueryTableDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one queryTable(@http://schemas.openxmlformats.org/spreadsheetml/2006/main) element.
 *
 * This is a complex type.
 */
public interface QueryTableDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.QueryTableDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "querytable8143doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "queryTable" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTQueryTable getQueryTable();

    /**
     * Sets the "queryTable" element
     */
    void setQueryTable(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTQueryTable queryTable);

    /**
     * Appends and returns a new empty "queryTable" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTQueryTable addNewQueryTable();
}
