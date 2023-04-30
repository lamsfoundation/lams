/*
 * An XML document type.
 * Localname: chart
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.ChartDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one chart(@http://schemas.openxmlformats.org/drawingml/2006/chart) element.
 *
 * This is a complex type.
 */
public interface ChartDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.chart.ChartDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "chartf85cdoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "chart" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTRelId getChart();

    /**
     * Sets the "chart" element
     */
    void setChart(org.openxmlformats.schemas.drawingml.x2006.chart.CTRelId chart);

    /**
     * Appends and returns a new empty "chart" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTRelId addNewChart();
}
