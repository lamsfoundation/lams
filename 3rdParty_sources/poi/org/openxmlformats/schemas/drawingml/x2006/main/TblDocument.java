/*
 * An XML document type.
 * Localname: tbl
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.TblDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one tbl(@http://schemas.openxmlformats.org/drawingml/2006/main) element.
 *
 * This is a complex type.
 */
public interface TblDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.TblDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "tbleb1bdoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "tbl" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTable getTbl();

    /**
     * Sets the "tbl" element
     */
    void setTbl(org.openxmlformats.schemas.drawingml.x2006.main.CTTable tbl);

    /**
     * Appends and returns a new empty "tbl" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTable addNewTbl();
}
