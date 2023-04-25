/*
 * XML Type:  ST_PositiveFixedPercentage
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedPercentage
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_PositiveFixedPercentage(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a union type. Instances are of one of the following types:
 *     org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedPercentageDecimal
 *     org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STPositiveFixedPercentage
 */
public interface STPositiveFixedPercentage extends org.apache.xmlbeans.XmlAnySimpleType {
    java.lang.Object getObjectValue();
    void setObjectValue(java.lang.Object val);
    org.apache.xmlbeans.SchemaType instanceType();
    SimpleTypeFactory<org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedPercentage> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stpositivefixedpercentagee756type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();

}
