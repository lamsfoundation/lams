/*
 * XML Type:  CT_RgbColor
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRgbColor
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_RgbColor(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTRgbColor extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRgbColor> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctrgbcolor95dftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "rgb" attribute
     */
    byte[] getRgb();

    /**
     * Gets (as xml) the "rgb" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STUnsignedIntHex xgetRgb();

    /**
     * True if has "rgb" attribute
     */
    boolean isSetRgb();

    /**
     * Sets the "rgb" attribute
     */
    void setRgb(byte[] rgb);

    /**
     * Sets (as xml) the "rgb" attribute
     */
    void xsetRgb(org.openxmlformats.schemas.spreadsheetml.x2006.main.STUnsignedIntHex rgb);

    /**
     * Unsets the "rgb" attribute
     */
    void unsetRgb();
}
