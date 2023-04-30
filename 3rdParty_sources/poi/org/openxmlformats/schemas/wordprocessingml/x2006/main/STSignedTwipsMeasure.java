/*
 * XML Type:  ST_SignedTwipsMeasure
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.STSignedTwipsMeasure
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_SignedTwipsMeasure(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a union type. Instances are of one of the following types:
 *     org.apache.xmlbeans.XmlInteger
 *     org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STUniversalMeasure
 */
public interface STSignedTwipsMeasure extends org.apache.xmlbeans.XmlAnySimpleType {
    java.lang.Object getObjectValue();
    void setObjectValue(java.lang.Object val);
    org.apache.xmlbeans.SchemaType instanceType();
    SimpleTypeFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.STSignedTwipsMeasure> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stsignedtwipsmeasureb227type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();

}
