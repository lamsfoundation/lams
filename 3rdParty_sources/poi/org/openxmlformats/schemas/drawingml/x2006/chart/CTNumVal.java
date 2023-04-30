/*
 * XML Type:  CT_NumVal
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTNumVal
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_NumVal(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public interface CTNumVal extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.chart.CTNumVal> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctnumval2fe1type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "v" element
     */
    java.lang.String getV();

    /**
     * Gets (as xml) the "v" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetV();

    /**
     * Sets the "v" element
     */
    void setV(java.lang.String v);

    /**
     * Sets (as xml) the "v" element
     */
    void xsetV(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring v);

    /**
     * Gets the "idx" attribute
     */
    long getIdx();

    /**
     * Gets (as xml) the "idx" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetIdx();

    /**
     * Sets the "idx" attribute
     */
    void setIdx(long idx);

    /**
     * Sets (as xml) the "idx" attribute
     */
    void xsetIdx(org.apache.xmlbeans.XmlUnsignedInt idx);

    /**
     * Gets the "formatCode" attribute
     */
    java.lang.String getFormatCode();

    /**
     * Gets (as xml) the "formatCode" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetFormatCode();

    /**
     * True if has "formatCode" attribute
     */
    boolean isSetFormatCode();

    /**
     * Sets the "formatCode" attribute
     */
    void setFormatCode(java.lang.String formatCode);

    /**
     * Sets (as xml) the "formatCode" attribute
     */
    void xsetFormatCode(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring formatCode);

    /**
     * Unsets the "formatCode" attribute
     */
    void unsetFormatCode();
}
