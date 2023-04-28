/*
 * XML Type:  CT_TextNormalAutofit
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTTextNormalAutofit
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TextNormalAutofit(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTextNormalAutofit extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTTextNormalAutofit> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttextnormalautofitbbdftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "fontScale" attribute
     */
    java.lang.Object getFontScale();

    /**
     * Gets (as xml) the "fontScale" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STTextFontScalePercentOrPercentString xgetFontScale();

    /**
     * True if has "fontScale" attribute
     */
    boolean isSetFontScale();

    /**
     * Sets the "fontScale" attribute
     */
    void setFontScale(java.lang.Object fontScale);

    /**
     * Sets (as xml) the "fontScale" attribute
     */
    void xsetFontScale(org.openxmlformats.schemas.drawingml.x2006.main.STTextFontScalePercentOrPercentString fontScale);

    /**
     * Unsets the "fontScale" attribute
     */
    void unsetFontScale();

    /**
     * Gets the "lnSpcReduction" attribute
     */
    java.lang.Object getLnSpcReduction();

    /**
     * Gets (as xml) the "lnSpcReduction" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STTextSpacingPercentOrPercentString xgetLnSpcReduction();

    /**
     * True if has "lnSpcReduction" attribute
     */
    boolean isSetLnSpcReduction();

    /**
     * Sets the "lnSpcReduction" attribute
     */
    void setLnSpcReduction(java.lang.Object lnSpcReduction);

    /**
     * Sets (as xml) the "lnSpcReduction" attribute
     */
    void xsetLnSpcReduction(org.openxmlformats.schemas.drawingml.x2006.main.STTextSpacingPercentOrPercentString lnSpcReduction);

    /**
     * Unsets the "lnSpcReduction" attribute
     */
    void unsetLnSpcReduction();
}
