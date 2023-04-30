/*
 * An XML document type.
 * Localname: singleXmlCells
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.SingleXmlCellsDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one singleXmlCells(@http://schemas.openxmlformats.org/spreadsheetml/2006/main) element.
 *
 * This is a complex type.
 */
public interface SingleXmlCellsDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.SingleXmlCellsDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "singlexmlcells33bfdoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "singleXmlCells" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSingleXmlCells getSingleXmlCells();

    /**
     * Sets the "singleXmlCells" element
     */
    void setSingleXmlCells(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSingleXmlCells singleXmlCells);

    /**
     * Appends and returns a new empty "singleXmlCells" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSingleXmlCells addNewSingleXmlCells();
}
