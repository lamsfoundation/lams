/*
 * An XML document type.
 * Localname: chart
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.ChartDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one chart(@http://schemas.openxmlformats.org/drawingml/2006/chart) element.
 *
 * This is a complex type.
 */
public class ChartDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.chart.ChartDocument {
    private static final long serialVersionUID = 1L;

    public ChartDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "chart"),
    };


    /**
     * Gets the "chart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTRelId getChart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTRelId target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTRelId)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "chart" element
     */
    @Override
    public void setChart(org.openxmlformats.schemas.drawingml.x2006.chart.CTRelId chart) {
        generatedSetterHelperImpl(chart, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "chart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTRelId addNewChart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTRelId target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTRelId)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
