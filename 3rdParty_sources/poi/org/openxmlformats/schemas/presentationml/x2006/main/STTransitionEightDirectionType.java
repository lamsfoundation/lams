/*
 * XML Type:  ST_TransitionEightDirectionType
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.STTransitionEightDirectionType
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_TransitionEightDirectionType(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a union type. Instances are of one of the following types:
 *     org.openxmlformats.schemas.presentationml.x2006.main.STTransitionSideDirectionType
 *     org.openxmlformats.schemas.presentationml.x2006.main.STTransitionCornerDirectionType
 */
public interface STTransitionEightDirectionType extends org.apache.xmlbeans.XmlAnySimpleType {
    java.lang.Object getObjectValue();
    void setObjectValue(java.lang.Object val);
    org.apache.xmlbeans.SchemaType instanceType();
    SimpleTypeFactory<org.openxmlformats.schemas.presentationml.x2006.main.STTransitionEightDirectionType> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "sttransitioneightdirectiontype8576type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();

}
