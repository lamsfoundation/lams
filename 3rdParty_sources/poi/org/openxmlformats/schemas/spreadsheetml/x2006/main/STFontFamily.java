/*
 * XML Type:  ST_FontFamily
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.STFontFamily
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_FontFamily(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.spreadsheetml.x2006.main.STFontFamily.
 */
public interface STFontFamily extends org.apache.xmlbeans.XmlInteger {
    int getIntValue();
    void setIntValue(int i);
    SimpleTypeFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.STFontFamily> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stfontfamily9c6ctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();

}
