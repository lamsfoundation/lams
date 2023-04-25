/*
 * An XML document type.
 * Localname: themeManager
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.ThemeManagerDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one themeManager(@http://schemas.openxmlformats.org/drawingml/2006/main) element.
 *
 * This is a complex type.
 */
public interface ThemeManagerDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.ThemeManagerDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "thememanager7113doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "themeManager" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTEmptyElement getThemeManager();

    /**
     * Sets the "themeManager" element
     */
    void setThemeManager(org.openxmlformats.schemas.drawingml.x2006.main.CTEmptyElement themeManager);

    /**
     * Appends and returns a new empty "themeManager" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTEmptyElement addNewThemeManager();
}
