/*
 * An XML document type.
 * Localname: settings
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.SettingsDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one settings(@http://schemas.openxmlformats.org/wordprocessingml/2006/main) element.
 *
 * This is a complex type.
 */
public interface SettingsDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.SettingsDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "settings9dd1doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "settings" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSettings getSettings();

    /**
     * Sets the "settings" element
     */
    void setSettings(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSettings settings);

    /**
     * Appends and returns a new empty "settings" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSettings addNewSettings();
}
