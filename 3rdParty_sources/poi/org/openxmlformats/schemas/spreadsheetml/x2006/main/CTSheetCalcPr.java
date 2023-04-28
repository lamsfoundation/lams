/*
 * XML Type:  CT_SheetCalcPr
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetCalcPr
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_SheetCalcPr(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTSheetCalcPr extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetCalcPr> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctsheetcalcprc6d5type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "fullCalcOnLoad" attribute
     */
    boolean getFullCalcOnLoad();

    /**
     * Gets (as xml) the "fullCalcOnLoad" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetFullCalcOnLoad();

    /**
     * True if has "fullCalcOnLoad" attribute
     */
    boolean isSetFullCalcOnLoad();

    /**
     * Sets the "fullCalcOnLoad" attribute
     */
    void setFullCalcOnLoad(boolean fullCalcOnLoad);

    /**
     * Sets (as xml) the "fullCalcOnLoad" attribute
     */
    void xsetFullCalcOnLoad(org.apache.xmlbeans.XmlBoolean fullCalcOnLoad);

    /**
     * Unsets the "fullCalcOnLoad" attribute
     */
    void unsetFullCalcOnLoad();
}
