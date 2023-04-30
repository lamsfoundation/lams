/*
 * An XML document type.
 * Localname: theme
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.ThemeDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one theme(@http://schemas.openxmlformats.org/drawingml/2006/main) element.
 *
 * This is a complex type.
 */
public interface ThemeDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.ThemeDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "themefd26doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "theme" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeStyleSheet getTheme();

    /**
     * Sets the "theme" element
     */
    void setTheme(org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeStyleSheet theme);

    /**
     * Appends and returns a new empty "theme" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeStyleSheet addNewTheme();
}
