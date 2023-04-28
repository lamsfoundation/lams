/*
 * XML Type:  CT_TextFont
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TextFont(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTextFont extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttextfont92b7type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "typeface" attribute
     */
    java.lang.String getTypeface();

    /**
     * Gets (as xml) the "typeface" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STTextTypeface xgetTypeface();

    /**
     * Sets the "typeface" attribute
     */
    void setTypeface(java.lang.String typeface);

    /**
     * Sets (as xml) the "typeface" attribute
     */
    void xsetTypeface(org.openxmlformats.schemas.drawingml.x2006.main.STTextTypeface typeface);

    /**
     * Gets the "panose" attribute
     */
    byte[] getPanose();

    /**
     * Gets (as xml) the "panose" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STPanose xgetPanose();

    /**
     * True if has "panose" attribute
     */
    boolean isSetPanose();

    /**
     * Sets the "panose" attribute
     */
    void setPanose(byte[] panose);

    /**
     * Sets (as xml) the "panose" attribute
     */
    void xsetPanose(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STPanose panose);

    /**
     * Unsets the "panose" attribute
     */
    void unsetPanose();

    /**
     * Gets the "pitchFamily" attribute
     */
    byte getPitchFamily();

    /**
     * Gets (as xml) the "pitchFamily" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STPitchFamily xgetPitchFamily();

    /**
     * True if has "pitchFamily" attribute
     */
    boolean isSetPitchFamily();

    /**
     * Sets the "pitchFamily" attribute
     */
    void setPitchFamily(byte pitchFamily);

    /**
     * Sets (as xml) the "pitchFamily" attribute
     */
    void xsetPitchFamily(org.openxmlformats.schemas.drawingml.x2006.main.STPitchFamily pitchFamily);

    /**
     * Unsets the "pitchFamily" attribute
     */
    void unsetPitchFamily();

    /**
     * Gets the "charset" attribute
     */
    byte getCharset();

    /**
     * Gets (as xml) the "charset" attribute
     */
    org.apache.xmlbeans.XmlByte xgetCharset();

    /**
     * True if has "charset" attribute
     */
    boolean isSetCharset();

    /**
     * Sets the "charset" attribute
     */
    void setCharset(byte charset);

    /**
     * Sets (as xml) the "charset" attribute
     */
    void xsetCharset(org.apache.xmlbeans.XmlByte charset);

    /**
     * Unsets the "charset" attribute
     */
    void unsetCharset();
}
