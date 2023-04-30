/*
 * XML Type:  CT_PatternFillProperties
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTPatternFillProperties
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_PatternFillProperties(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTPatternFillProperties extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTPatternFillProperties> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctpatternfillproperties3637type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "fgClr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTColor getFgClr();

    /**
     * True if has "fgClr" element
     */
    boolean isSetFgClr();

    /**
     * Sets the "fgClr" element
     */
    void setFgClr(org.openxmlformats.schemas.drawingml.x2006.main.CTColor fgClr);

    /**
     * Appends and returns a new empty "fgClr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTColor addNewFgClr();

    /**
     * Unsets the "fgClr" element
     */
    void unsetFgClr();

    /**
     * Gets the "bgClr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTColor getBgClr();

    /**
     * True if has "bgClr" element
     */
    boolean isSetBgClr();

    /**
     * Sets the "bgClr" element
     */
    void setBgClr(org.openxmlformats.schemas.drawingml.x2006.main.CTColor bgClr);

    /**
     * Appends and returns a new empty "bgClr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTColor addNewBgClr();

    /**
     * Unsets the "bgClr" element
     */
    void unsetBgClr();

    /**
     * Gets the "prst" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STPresetPatternVal.Enum getPrst();

    /**
     * Gets (as xml) the "prst" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STPresetPatternVal xgetPrst();

    /**
     * True if has "prst" attribute
     */
    boolean isSetPrst();

    /**
     * Sets the "prst" attribute
     */
    void setPrst(org.openxmlformats.schemas.drawingml.x2006.main.STPresetPatternVal.Enum prst);

    /**
     * Sets (as xml) the "prst" attribute
     */
    void xsetPrst(org.openxmlformats.schemas.drawingml.x2006.main.STPresetPatternVal prst);

    /**
     * Unsets the "prst" attribute
     */
    void unsetPrst();
}
