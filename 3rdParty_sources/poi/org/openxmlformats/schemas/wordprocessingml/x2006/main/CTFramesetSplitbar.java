/*
 * XML Type:  CT_FramesetSplitbar
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFramesetSplitbar
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_FramesetSplitbar(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTFramesetSplitbar extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFramesetSplitbar> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctframesetsplitbar11fatype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "w" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTwipsMeasure getW();

    /**
     * True if has "w" element
     */
    boolean isSetW();

    /**
     * Sets the "w" element
     */
    void setW(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTwipsMeasure w);

    /**
     * Appends and returns a new empty "w" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTwipsMeasure addNewW();

    /**
     * Unsets the "w" element
     */
    void unsetW();

    /**
     * Gets the "color" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColor getColor();

    /**
     * True if has "color" element
     */
    boolean isSetColor();

    /**
     * Sets the "color" element
     */
    void setColor(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColor color);

    /**
     * Appends and returns a new empty "color" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColor addNewColor();

    /**
     * Unsets the "color" element
     */
    void unsetColor();

    /**
     * Gets the "noBorder" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getNoBorder();

    /**
     * True if has "noBorder" element
     */
    boolean isSetNoBorder();

    /**
     * Sets the "noBorder" element
     */
    void setNoBorder(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff noBorder);

    /**
     * Appends and returns a new empty "noBorder" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewNoBorder();

    /**
     * Unsets the "noBorder" element
     */
    void unsetNoBorder();

    /**
     * Gets the "flatBorders" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getFlatBorders();

    /**
     * True if has "flatBorders" element
     */
    boolean isSetFlatBorders();

    /**
     * Sets the "flatBorders" element
     */
    void setFlatBorders(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff flatBorders);

    /**
     * Appends and returns a new empty "flatBorders" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewFlatBorders();

    /**
     * Unsets the "flatBorders" element
     */
    void unsetFlatBorders();
}
