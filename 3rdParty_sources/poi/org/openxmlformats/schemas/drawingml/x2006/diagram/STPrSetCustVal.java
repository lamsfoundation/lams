/*
 * XML Type:  ST_PrSetCustVal
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_PrSetCustVal(@http://schemas.openxmlformats.org/drawingml/2006/diagram).
 *
 * This is a union type. Instances are of one of the following types:
 *     org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STPercentage
 *     org.apache.xmlbeans.XmlInt
 */
public interface STPrSetCustVal extends org.apache.xmlbeans.XmlAnySimpleType {
    java.lang.Object getObjectValue();
    void setObjectValue(java.lang.Object val);
    org.apache.xmlbeans.SchemaType instanceType();
    SimpleTypeFactory<org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stprsetcustval4e33type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();

}
