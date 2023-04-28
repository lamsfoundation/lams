/*
 * XML Type:  ST_ParameterVal
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.STParameterVal
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_ParameterVal(@http://schemas.openxmlformats.org/drawingml/2006/diagram).
 *
 * This is a union type. Instances are of one of the following types:
 *     org.openxmlformats.schemas.drawingml.x2006.diagram.STDiagramHorizontalAlignment
 *     org.openxmlformats.schemas.drawingml.x2006.diagram.STVerticalAlignment
 *     org.openxmlformats.schemas.drawingml.x2006.diagram.STChildDirection
 *     org.openxmlformats.schemas.drawingml.x2006.diagram.STChildAlignment
 *     org.openxmlformats.schemas.drawingml.x2006.diagram.STSecondaryChildAlignment
 *     org.openxmlformats.schemas.drawingml.x2006.diagram.STLinearDirection
 *     org.openxmlformats.schemas.drawingml.x2006.diagram.STSecondaryLinearDirection
 *     org.openxmlformats.schemas.drawingml.x2006.diagram.STStartingElement
 *     org.openxmlformats.schemas.drawingml.x2006.diagram.STBendPoint
 *     org.openxmlformats.schemas.drawingml.x2006.diagram.STConnectorRouting
 *     org.openxmlformats.schemas.drawingml.x2006.diagram.STArrowheadStyle
 *     org.openxmlformats.schemas.drawingml.x2006.diagram.STConnectorDimension
 *     org.openxmlformats.schemas.drawingml.x2006.diagram.STRotationPath
 *     org.openxmlformats.schemas.drawingml.x2006.diagram.STCenterShapeMapping
 *     org.openxmlformats.schemas.drawingml.x2006.diagram.STNodeHorizontalAlignment
 *     org.openxmlformats.schemas.drawingml.x2006.diagram.STNodeVerticalAlignment
 *     org.openxmlformats.schemas.drawingml.x2006.diagram.STFallbackDimension
 *     org.openxmlformats.schemas.drawingml.x2006.diagram.STTextDirection
 *     org.openxmlformats.schemas.drawingml.x2006.diagram.STPyramidAccentPosition
 *     org.openxmlformats.schemas.drawingml.x2006.diagram.STPyramidAccentTextMargin
 *     org.openxmlformats.schemas.drawingml.x2006.diagram.STTextBlockDirection
 *     org.openxmlformats.schemas.drawingml.x2006.diagram.STTextAnchorHorizontal
 *     org.openxmlformats.schemas.drawingml.x2006.diagram.STTextAnchorVertical
 *     org.openxmlformats.schemas.drawingml.x2006.diagram.STDiagramTextAlignment
 *     org.openxmlformats.schemas.drawingml.x2006.diagram.STAutoTextRotation
 *     org.openxmlformats.schemas.drawingml.x2006.diagram.STGrowDirection
 *     org.openxmlformats.schemas.drawingml.x2006.diagram.STFlowDirection
 *     org.openxmlformats.schemas.drawingml.x2006.diagram.STContinueDirection
 *     org.openxmlformats.schemas.drawingml.x2006.diagram.STBreakpoint
 *     org.openxmlformats.schemas.drawingml.x2006.diagram.STOffset
 *     org.openxmlformats.schemas.drawingml.x2006.diagram.STHierarchyAlignment
 *     org.apache.xmlbeans.XmlInt
 *     org.apache.xmlbeans.XmlDouble
 *     org.apache.xmlbeans.XmlBoolean
 *     org.apache.xmlbeans.XmlString
 *     org.openxmlformats.schemas.drawingml.x2006.diagram.STConnectorPoint
 */
public interface STParameterVal extends org.apache.xmlbeans.XmlAnySimpleType {
    java.lang.Object getObjectValue();
    void setObjectValue(java.lang.Object val);
    org.apache.xmlbeans.SchemaType instanceType();
    SimpleTypeFactory<org.openxmlformats.schemas.drawingml.x2006.diagram.STParameterVal> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stparametervalc369type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();

}
