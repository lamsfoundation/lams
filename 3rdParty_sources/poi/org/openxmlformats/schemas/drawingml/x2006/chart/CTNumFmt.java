/*
 * XML Type:  CT_NumFmt
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTNumFmt
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_NumFmt(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public interface CTNumFmt extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.chart.CTNumFmt> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctnumfmtc0f5type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "formatCode" attribute
     */
    java.lang.String getFormatCode();

    /**
     * Gets (as xml) the "formatCode" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetFormatCode();

    /**
     * Sets the "formatCode" attribute
     */
    void setFormatCode(java.lang.String formatCode);

    /**
     * Sets (as xml) the "formatCode" attribute
     */
    void xsetFormatCode(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring formatCode);

    /**
     * Gets the "sourceLinked" attribute
     */
    boolean getSourceLinked();

    /**
     * Gets (as xml) the "sourceLinked" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetSourceLinked();

    /**
     * True if has "sourceLinked" attribute
     */
    boolean isSetSourceLinked();

    /**
     * Sets the "sourceLinked" attribute
     */
    void setSourceLinked(boolean sourceLinked);

    /**
     * Sets (as xml) the "sourceLinked" attribute
     */
    void xsetSourceLinked(org.apache.xmlbeans.XmlBoolean sourceLinked);

    /**
     * Unsets the "sourceLinked" attribute
     */
    void unsetSourceLinked();
}
