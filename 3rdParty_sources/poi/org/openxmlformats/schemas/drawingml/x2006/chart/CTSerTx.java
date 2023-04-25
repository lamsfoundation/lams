/*
 * XML Type:  CT_SerTx
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTSerTx
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_SerTx(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public interface CTSerTx extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.chart.CTSerTx> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctsertxd722type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "strRef" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTStrRef getStrRef();

    /**
     * True if has "strRef" element
     */
    boolean isSetStrRef();

    /**
     * Sets the "strRef" element
     */
    void setStrRef(org.openxmlformats.schemas.drawingml.x2006.chart.CTStrRef strRef);

    /**
     * Appends and returns a new empty "strRef" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTStrRef addNewStrRef();

    /**
     * Unsets the "strRef" element
     */
    void unsetStrRef();

    /**
     * Gets the "v" element
     */
    java.lang.String getV();

    /**
     * Gets (as xml) the "v" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetV();

    /**
     * True if has "v" element
     */
    boolean isSetV();

    /**
     * Sets the "v" element
     */
    void setV(java.lang.String v);

    /**
     * Sets (as xml) the "v" element
     */
    void xsetV(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring v);

    /**
     * Unsets the "v" element
     */
    void unsetV();
}
