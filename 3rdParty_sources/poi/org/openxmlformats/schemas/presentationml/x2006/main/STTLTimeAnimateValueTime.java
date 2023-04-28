/*
 * XML Type:  ST_TLTimeAnimateValueTime
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeAnimateValueTime
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_TLTimeAnimateValueTime(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a union type. Instances are of one of the following types:
 *     org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedPercentageDecimal
 *     org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STPositiveFixedPercentage
 *     org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeIndefinite
 */
public interface STTLTimeAnimateValueTime extends org.apache.xmlbeans.XmlAnySimpleType {
    java.lang.Object getObjectValue();
    void setObjectValue(java.lang.Object val);
    org.apache.xmlbeans.SchemaType instanceType();
    SimpleTypeFactory<org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeAnimateValueTime> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "sttltimeanimatevaluetimecbb3type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();

}
