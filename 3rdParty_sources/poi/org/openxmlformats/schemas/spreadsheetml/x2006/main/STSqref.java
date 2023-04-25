/*
 * XML Type:  ST_Sqref
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.STSqref
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_Sqref(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a list type whose items are org.openxmlformats.schemas.spreadsheetml.x2006.main.STRef.
 */
public interface STSqref extends org.apache.xmlbeans.XmlAnySimpleType {
    java.util.List getListValue();
    java.util.List xgetListValue();
    void setListValue(java.util.List<?> list);
    SimpleTypeFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.STSqref> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stsqrefb044type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();

}
