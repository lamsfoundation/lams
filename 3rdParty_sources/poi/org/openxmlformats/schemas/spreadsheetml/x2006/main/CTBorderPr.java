/*
 * XML Type:  CT_BorderPr
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_BorderPr(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTBorderPr extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctborderpre497type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "color" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor getColor();

    /**
     * True if has "color" element
     */
    boolean isSetColor();

    /**
     * Sets the "color" element
     */
    void setColor(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor color);

    /**
     * Appends and returns a new empty "color" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor addNewColor();

    /**
     * Unsets the "color" element
     */
    void unsetColor();

    /**
     * Gets the "style" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STBorderStyle.Enum getStyle();

    /**
     * Gets (as xml) the "style" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STBorderStyle xgetStyle();

    /**
     * True if has "style" attribute
     */
    boolean isSetStyle();

    /**
     * Sets the "style" attribute
     */
    void setStyle(org.openxmlformats.schemas.spreadsheetml.x2006.main.STBorderStyle.Enum style);

    /**
     * Sets (as xml) the "style" attribute
     */
    void xsetStyle(org.openxmlformats.schemas.spreadsheetml.x2006.main.STBorderStyle style);

    /**
     * Unsets the "style" attribute
     */
    void unsetStyle();
}
