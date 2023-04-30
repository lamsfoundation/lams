/*
 * An XML document type.
 * Localname: dialogsheet
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.DialogsheetDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one dialogsheet(@http://schemas.openxmlformats.org/spreadsheetml/2006/main) element.
 *
 * This is a complex type.
 */
public interface DialogsheetDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.DialogsheetDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "dialogsheet3662doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "dialogsheet" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDialogsheet getDialogsheet();

    /**
     * Sets the "dialogsheet" element
     */
    void setDialogsheet(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDialogsheet dialogsheet);

    /**
     * Appends and returns a new empty "dialogsheet" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDialogsheet addNewDialogsheet();
}
