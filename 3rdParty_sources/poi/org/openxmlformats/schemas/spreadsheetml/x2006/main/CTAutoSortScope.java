/*
 * XML Type:  CT_AutoSortScope
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTAutoSortScope
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_AutoSortScope(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTAutoSortScope extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTAutoSortScope> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctautosortscope0dc6type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "pivotArea" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotArea getPivotArea();

    /**
     * Sets the "pivotArea" element
     */
    void setPivotArea(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotArea pivotArea);

    /**
     * Appends and returns a new empty "pivotArea" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotArea addNewPivotArea();
}
