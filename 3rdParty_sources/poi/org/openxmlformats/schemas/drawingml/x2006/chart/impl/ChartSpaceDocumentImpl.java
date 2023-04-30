/*
 * An XML document type.
 * Localname: chartSpace
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.ChartSpaceDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one chartSpace(@http://schemas.openxmlformats.org/drawingml/2006/chart) element.
 *
 * This is a complex type.
 */
public class ChartSpaceDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.chart.ChartSpaceDocument {
    private static final long serialVersionUID = 1L;

    public ChartSpaceDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "chartSpace"),
    };


    /**
     * Gets the "chartSpace" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace getChartSpace() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "chartSpace" element
     */
    @Override
    public void setChartSpace(org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace chartSpace) {
        generatedSetterHelperImpl(chartSpace, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "chartSpace" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace addNewChartSpace() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
