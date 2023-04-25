/*
 * XML Type:  ST_Ints
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.STInts
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_Ints(@http://schemas.openxmlformats.org/drawingml/2006/diagram).
 *
 * This is a list type whose items are org.apache.xmlbeans.XmlInt.
 */
public interface STInts extends org.apache.xmlbeans.XmlAnySimpleType {
    java.util.List getListValue();
    java.util.List xgetListValue();
    void setListValue(java.util.List<?> list);
    SimpleTypeFactory<org.openxmlformats.schemas.drawingml.x2006.diagram.STInts> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stints703dtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();

}
