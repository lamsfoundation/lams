/*
 * XML Type:  CT_NumDataSource
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_NumDataSource(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public interface CTNumDataSource extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctnumdatasourcef0bbtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "numRef" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTNumRef getNumRef();

    /**
     * True if has "numRef" element
     */
    boolean isSetNumRef();

    /**
     * Sets the "numRef" element
     */
    void setNumRef(org.openxmlformats.schemas.drawingml.x2006.chart.CTNumRef numRef);

    /**
     * Appends and returns a new empty "numRef" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTNumRef addNewNumRef();

    /**
     * Unsets the "numRef" element
     */
    void unsetNumRef();

    /**
     * Gets the "numLit" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTNumData getNumLit();

    /**
     * True if has "numLit" element
     */
    boolean isSetNumLit();

    /**
     * Sets the "numLit" element
     */
    void setNumLit(org.openxmlformats.schemas.drawingml.x2006.chart.CTNumData numLit);

    /**
     * Appends and returns a new empty "numLit" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTNumData addNewNumLit();

    /**
     * Unsets the "numLit" element
     */
    void unsetNumLit();
}
