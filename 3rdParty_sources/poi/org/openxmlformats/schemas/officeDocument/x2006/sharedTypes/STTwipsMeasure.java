/*
 * XML Type:  ST_TwipsMeasure
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/sharedTypes
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.sharedTypes;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_TwipsMeasure(@http://schemas.openxmlformats.org/officeDocument/2006/sharedTypes).
 *
 * This is a union type. Instances are of one of the following types:
 *     org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STUnsignedDecimalNumber
 *     org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STPositiveUniversalMeasure
 */
public interface STTwipsMeasure extends org.apache.xmlbeans.XmlAnySimpleType {
    java.lang.Object getObjectValue();
    void setObjectValue(java.lang.Object val);
    org.apache.xmlbeans.SchemaType instanceType();
    SimpleTypeFactory<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "sttwipsmeasure9c4ftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();

}
