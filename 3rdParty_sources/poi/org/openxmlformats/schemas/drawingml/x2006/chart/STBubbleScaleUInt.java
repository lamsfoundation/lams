/*
 * XML Type:  ST_BubbleScaleUInt
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.STBubbleScaleUInt
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_BubbleScaleUInt(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.drawingml.x2006.chart.STBubbleScaleUInt.
 */
public interface STBubbleScaleUInt extends org.apache.xmlbeans.XmlUnsignedInt {
    int getIntValue();
    void setIntValue(int i);
    SimpleTypeFactory<org.openxmlformats.schemas.drawingml.x2006.chart.STBubbleScaleUInt> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stbubblescaleuintf8fetype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();

}
