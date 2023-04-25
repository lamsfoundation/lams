/*
 * An XML document type.
 * Localname: workbook
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.WorkbookDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one workbook(@http://schemas.openxmlformats.org/spreadsheetml/2006/main) element.
 *
 * This is a complex type.
 */
public interface WorkbookDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.WorkbookDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "workbookec17doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "workbook" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbook getWorkbook();

    /**
     * Sets the "workbook" element
     */
    void setWorkbook(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbook workbook);

    /**
     * Appends and returns a new empty "workbook" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbook addNewWorkbook();
}
