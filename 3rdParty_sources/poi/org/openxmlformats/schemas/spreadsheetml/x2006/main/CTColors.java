/*
 * XML Type:  CT_Colors
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColors
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Colors(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTColors extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColors> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctcolors6579type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "indexedColors" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIndexedColors getIndexedColors();

    /**
     * True if has "indexedColors" element
     */
    boolean isSetIndexedColors();

    /**
     * Sets the "indexedColors" element
     */
    void setIndexedColors(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIndexedColors indexedColors);

    /**
     * Appends and returns a new empty "indexedColors" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIndexedColors addNewIndexedColors();

    /**
     * Unsets the "indexedColors" element
     */
    void unsetIndexedColors();

    /**
     * Gets the "mruColors" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMRUColors getMruColors();

    /**
     * True if has "mruColors" element
     */
    boolean isSetMruColors();

    /**
     * Sets the "mruColors" element
     */
    void setMruColors(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMRUColors mruColors);

    /**
     * Appends and returns a new empty "mruColors" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMRUColors addNewMruColors();

    /**
     * Unsets the "mruColors" element
     */
    void unsetMruColors();
}
