/*
 * XML Type:  CT_Tx
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTTx
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Tx(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public interface CTTx extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.chart.CTTx> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttx9678type");
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
     * Gets the "rich" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody getRich();

    /**
     * True if has "rich" element
     */
    boolean isSetRich();

    /**
     * Sets the "rich" element
     */
    void setRich(org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody rich);

    /**
     * Appends and returns a new empty "rich" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody addNewRich();

    /**
     * Unsets the "rich" element
     */
    void unsetRich();
}
