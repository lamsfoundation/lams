/*
 * XML Type:  ST_OnOff
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/sharedTypes
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.sharedTypes;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_OnOff(@http://schemas.openxmlformats.org/officeDocument/2006/sharedTypes).
 *
 * This is a union type. Instances are of one of the following types:
 *     org.apache.xmlbeans.XmlBoolean
 *     org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff1
 */
public interface STOnOff extends org.apache.xmlbeans.XmlAnySimpleType {
    java.lang.Object getObjectValue();
    void setObjectValue(java.lang.Object val);
    org.apache.xmlbeans.SchemaType instanceType();
    SimpleTypeFactory<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stonoff9300type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();

}
