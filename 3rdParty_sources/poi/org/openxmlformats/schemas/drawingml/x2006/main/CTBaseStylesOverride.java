/*
 * XML Type:  CT_BaseStylesOverride
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTBaseStylesOverride
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_BaseStylesOverride(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTBaseStylesOverride extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTBaseStylesOverride> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctbasestylesoverride711atype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "clrScheme" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTColorScheme getClrScheme();

    /**
     * True if has "clrScheme" element
     */
    boolean isSetClrScheme();

    /**
     * Sets the "clrScheme" element
     */
    void setClrScheme(org.openxmlformats.schemas.drawingml.x2006.main.CTColorScheme clrScheme);

    /**
     * Appends and returns a new empty "clrScheme" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTColorScheme addNewClrScheme();

    /**
     * Unsets the "clrScheme" element
     */
    void unsetClrScheme();

    /**
     * Gets the "fontScheme" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTFontScheme getFontScheme();

    /**
     * True if has "fontScheme" element
     */
    boolean isSetFontScheme();

    /**
     * Sets the "fontScheme" element
     */
    void setFontScheme(org.openxmlformats.schemas.drawingml.x2006.main.CTFontScheme fontScheme);

    /**
     * Appends and returns a new empty "fontScheme" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTFontScheme addNewFontScheme();

    /**
     * Unsets the "fontScheme" element
     */
    void unsetFontScheme();

    /**
     * Gets the "fmtScheme" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrix getFmtScheme();

    /**
     * True if has "fmtScheme" element
     */
    boolean isSetFmtScheme();

    /**
     * Sets the "fmtScheme" element
     */
    void setFmtScheme(org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrix fmtScheme);

    /**
     * Appends and returns a new empty "fmtScheme" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrix addNewFmtScheme();

    /**
     * Unsets the "fmtScheme" element
     */
    void unsetFmtScheme();
}
