/*
 * An XML document type.
 * Localname: volTypes
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.VolTypesDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one volTypes(@http://schemas.openxmlformats.org/spreadsheetml/2006/main) element.
 *
 * This is a complex type.
 */
public interface VolTypesDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.VolTypesDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "voltypes8703doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "volTypes" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolTypes getVolTypes();

    /**
     * Sets the "volTypes" element
     */
    void setVolTypes(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolTypes volTypes);

    /**
     * Appends and returns a new empty "volTypes" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolTypes addNewVolTypes();
}
