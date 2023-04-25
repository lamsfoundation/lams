/*
 * XML Type:  ST_UnsignedInts
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.STUnsignedInts
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_UnsignedInts(@http://schemas.openxmlformats.org/drawingml/2006/diagram).
 *
 * This is a list type whose items are org.apache.xmlbeans.XmlUnsignedInt.
 */
public interface STUnsignedInts extends org.apache.xmlbeans.XmlAnySimpleType {
    java.util.List getListValue();
    java.util.List xgetListValue();
    void setListValue(java.util.List<?> list);
    SimpleTypeFactory<org.openxmlformats.schemas.drawingml.x2006.diagram.STUnsignedInts> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stunsignedints7248type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();

}
