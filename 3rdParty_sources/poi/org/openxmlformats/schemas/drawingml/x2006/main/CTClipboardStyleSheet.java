/*
 * XML Type:  CT_ClipboardStyleSheet
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTClipboardStyleSheet
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_ClipboardStyleSheet(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTClipboardStyleSheet extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTClipboardStyleSheet> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctclipboardstylesheet6015type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "themeElements" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTBaseStyles getThemeElements();

    /**
     * Sets the "themeElements" element
     */
    void setThemeElements(org.openxmlformats.schemas.drawingml.x2006.main.CTBaseStyles themeElements);

    /**
     * Appends and returns a new empty "themeElements" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTBaseStyles addNewThemeElements();

    /**
     * Gets the "clrMap" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping getClrMap();

    /**
     * Sets the "clrMap" element
     */
    void setClrMap(org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping clrMap);

    /**
     * Appends and returns a new empty "clrMap" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping addNewClrMap();
}
