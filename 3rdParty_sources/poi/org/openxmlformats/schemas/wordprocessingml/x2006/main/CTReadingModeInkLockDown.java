/*
 * XML Type:  CT_ReadingModeInkLockDown
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTReadingModeInkLockDown
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_ReadingModeInkLockDown(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTReadingModeInkLockDown extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTReadingModeInkLockDown> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctreadingmodeinklockdown1e84type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "actualPg" attribute
     */
    java.lang.Object getActualPg();

    /**
     * Gets (as xml) the "actualPg" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff xgetActualPg();

    /**
     * Sets the "actualPg" attribute
     */
    void setActualPg(java.lang.Object actualPg);

    /**
     * Sets (as xml) the "actualPg" attribute
     */
    void xsetActualPg(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff actualPg);

    /**
     * Gets the "w" attribute
     */
    java.math.BigInteger getW();

    /**
     * Gets (as xml) the "w" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STPixelsMeasure xgetW();

    /**
     * Sets the "w" attribute
     */
    void setW(java.math.BigInteger w);

    /**
     * Sets (as xml) the "w" attribute
     */
    void xsetW(org.openxmlformats.schemas.wordprocessingml.x2006.main.STPixelsMeasure w);

    /**
     * Gets the "h" attribute
     */
    java.math.BigInteger getH();

    /**
     * Gets (as xml) the "h" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STPixelsMeasure xgetH();

    /**
     * Sets the "h" attribute
     */
    void setH(java.math.BigInteger h);

    /**
     * Sets (as xml) the "h" attribute
     */
    void xsetH(org.openxmlformats.schemas.wordprocessingml.x2006.main.STPixelsMeasure h);

    /**
     * Gets the "fontSz" attribute
     */
    java.lang.Object getFontSz();

    /**
     * Gets (as xml) the "fontSz" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumberOrPercent xgetFontSz();

    /**
     * Sets the "fontSz" attribute
     */
    void setFontSz(java.lang.Object fontSz);

    /**
     * Sets (as xml) the "fontSz" attribute
     */
    void xsetFontSz(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumberOrPercent fontSz);
}
