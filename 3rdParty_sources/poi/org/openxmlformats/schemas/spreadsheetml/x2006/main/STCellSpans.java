/*
 * XML Type:  ST_CellSpans
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellSpans
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_CellSpans(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a list type whose items are org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellSpan.
 */
public interface STCellSpans extends org.apache.xmlbeans.XmlAnySimpleType {
    java.util.List getListValue();
    java.util.List xgetListValue();
    void setListValue(java.util.List<?> list);
    SimpleTypeFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellSpans> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stcellspans60f6type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();

}
