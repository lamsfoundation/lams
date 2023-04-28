/*
 * XML Type:  ST_ElementTypes
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.STElementTypes
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_ElementTypes(@http://schemas.openxmlformats.org/drawingml/2006/diagram).
 *
 * This is a list type whose items are org.openxmlformats.schemas.drawingml.x2006.diagram.STElementType.
 */
public interface STElementTypes extends org.apache.xmlbeans.XmlAnySimpleType {
    java.util.List getListValue();
    java.util.List xgetListValue();
    void setListValue(java.util.List<?> list);
    SimpleTypeFactory<org.openxmlformats.schemas.drawingml.x2006.diagram.STElementTypes> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stelementtypes62e4type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();

}
