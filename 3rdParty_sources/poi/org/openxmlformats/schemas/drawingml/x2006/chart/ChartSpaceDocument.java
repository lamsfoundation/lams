/*
 * An XML document type.
 * Localname: chartSpace
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.ChartSpaceDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one chartSpace(@http://schemas.openxmlformats.org/drawingml/2006/chart) element.
 *
 * This is a complex type.
 */
public interface ChartSpaceDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.chart.ChartSpaceDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "chartspace36e0doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "chartSpace" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace getChartSpace();

    /**
     * Sets the "chartSpace" element
     */
    void setChartSpace(org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace chartSpace);

    /**
     * Appends and returns a new empty "chartSpace" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace addNewChartSpace();
}
