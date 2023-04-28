/*
 * XML Type:  CT_Color
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTColor
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Color(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTColor extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTColor> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctcolorb114type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "scrgbClr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTScRgbColor getScrgbClr();

    /**
     * True if has "scrgbClr" element
     */
    boolean isSetScrgbClr();

    /**
     * Sets the "scrgbClr" element
     */
    void setScrgbClr(org.openxmlformats.schemas.drawingml.x2006.main.CTScRgbColor scrgbClr);

    /**
     * Appends and returns a new empty "scrgbClr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTScRgbColor addNewScrgbClr();

    /**
     * Unsets the "scrgbClr" element
     */
    void unsetScrgbClr();

    /**
     * Gets the "srgbClr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor getSrgbClr();

    /**
     * True if has "srgbClr" element
     */
    boolean isSetSrgbClr();

    /**
     * Sets the "srgbClr" element
     */
    void setSrgbClr(org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor srgbClr);

    /**
     * Appends and returns a new empty "srgbClr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor addNewSrgbClr();

    /**
     * Unsets the "srgbClr" element
     */
    void unsetSrgbClr();

    /**
     * Gets the "hslClr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTHslColor getHslClr();

    /**
     * True if has "hslClr" element
     */
    boolean isSetHslClr();

    /**
     * Sets the "hslClr" element
     */
    void setHslClr(org.openxmlformats.schemas.drawingml.x2006.main.CTHslColor hslClr);

    /**
     * Appends and returns a new empty "hslClr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTHslColor addNewHslClr();

    /**
     * Unsets the "hslClr" element
     */
    void unsetHslClr();

    /**
     * Gets the "sysClr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTSystemColor getSysClr();

    /**
     * True if has "sysClr" element
     */
    boolean isSetSysClr();

    /**
     * Sets the "sysClr" element
     */
    void setSysClr(org.openxmlformats.schemas.drawingml.x2006.main.CTSystemColor sysClr);

    /**
     * Appends and returns a new empty "sysClr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTSystemColor addNewSysClr();

    /**
     * Unsets the "sysClr" element
     */
    void unsetSysClr();

    /**
     * Gets the "schemeClr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTSchemeColor getSchemeClr();

    /**
     * True if has "schemeClr" element
     */
    boolean isSetSchemeClr();

    /**
     * Sets the "schemeClr" element
     */
    void setSchemeClr(org.openxmlformats.schemas.drawingml.x2006.main.CTSchemeColor schemeClr);

    /**
     * Appends and returns a new empty "schemeClr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTSchemeColor addNewSchemeClr();

    /**
     * Unsets the "schemeClr" element
     */
    void unsetSchemeClr();

    /**
     * Gets the "prstClr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTPresetColor getPrstClr();

    /**
     * True if has "prstClr" element
     */
    boolean isSetPrstClr();

    /**
     * Sets the "prstClr" element
     */
    void setPrstClr(org.openxmlformats.schemas.drawingml.x2006.main.CTPresetColor prstClr);

    /**
     * Appends and returns a new empty "prstClr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTPresetColor addNewPrstClr();

    /**
     * Unsets the "prstClr" element
     */
    void unsetPrstClr();
}
