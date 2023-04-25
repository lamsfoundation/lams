/*
 * An XML document type.
 * Localname: tblStyleLst
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.TblStyleLstDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one tblStyleLst(@http://schemas.openxmlformats.org/drawingml/2006/main) element.
 *
 * This is a complex type.
 */
public interface TblStyleLstDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.TblStyleLstDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "tblstylelst4997doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "tblStyleLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleList getTblStyleLst();

    /**
     * Sets the "tblStyleLst" element
     */
    void setTblStyleLst(org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleList tblStyleLst);

    /**
     * Appends and returns a new empty "tblStyleLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleList addNewTblStyleLst();
}
