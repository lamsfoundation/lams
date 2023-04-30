/*
 * XML Type:  CT_PictureNonVisual
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing
 * Java type: org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTPictureNonVisual
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_PictureNonVisual(@http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing).
 *
 * This is a complex type.
 */
public interface CTPictureNonVisual extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTPictureNonVisual> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctpicturenonvisual36a3type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "cNvPr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps getCNvPr();

    /**
     * Sets the "cNvPr" element
     */
    void setCNvPr(org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps cNvPr);

    /**
     * Appends and returns a new empty "cNvPr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps addNewCNvPr();

    /**
     * Gets the "cNvPicPr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualPictureProperties getCNvPicPr();

    /**
     * Sets the "cNvPicPr" element
     */
    void setCNvPicPr(org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualPictureProperties cNvPicPr);

    /**
     * Appends and returns a new empty "cNvPicPr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualPictureProperties addNewCNvPicPr();
}
