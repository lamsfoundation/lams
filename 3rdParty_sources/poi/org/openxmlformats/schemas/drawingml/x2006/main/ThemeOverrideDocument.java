/*
 * An XML document type.
 * Localname: themeOverride
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.ThemeOverrideDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one themeOverride(@http://schemas.openxmlformats.org/drawingml/2006/main) element.
 *
 * This is a complex type.
 */
public interface ThemeOverrideDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.ThemeOverrideDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "themeoverridee412doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "themeOverride" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTBaseStylesOverride getThemeOverride();

    /**
     * Sets the "themeOverride" element
     */
    void setThemeOverride(org.openxmlformats.schemas.drawingml.x2006.main.CTBaseStylesOverride themeOverride);

    /**
     * Appends and returns a new empty "themeOverride" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTBaseStylesOverride addNewThemeOverride();
}
