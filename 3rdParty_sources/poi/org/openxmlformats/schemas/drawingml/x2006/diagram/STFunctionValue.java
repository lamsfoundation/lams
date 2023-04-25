/*
 * XML Type:  ST_FunctionValue
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.STFunctionValue
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_FunctionValue(@http://schemas.openxmlformats.org/drawingml/2006/diagram).
 *
 * This is a union type. Instances are of one of the following types:
 *     org.apache.xmlbeans.XmlInt
 *     org.apache.xmlbeans.XmlBoolean
 *     org.openxmlformats.schemas.drawingml.x2006.diagram.STDirection
 *     org.openxmlformats.schemas.drawingml.x2006.diagram.STHierBranchStyle
 *     org.openxmlformats.schemas.drawingml.x2006.diagram.STAnimOneStr
 *     org.openxmlformats.schemas.drawingml.x2006.diagram.STAnimLvlStr
 *     org.openxmlformats.schemas.drawingml.x2006.diagram.STResizeHandlesStr
 */
public interface STFunctionValue extends org.apache.xmlbeans.XmlAnySimpleType {
    java.lang.Object getObjectValue();
    void setObjectValue(java.lang.Object val);
    org.apache.xmlbeans.SchemaType instanceType();
    SimpleTypeFactory<org.openxmlformats.schemas.drawingml.x2006.diagram.STFunctionValue> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stfunctionvalue7652type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();

}
