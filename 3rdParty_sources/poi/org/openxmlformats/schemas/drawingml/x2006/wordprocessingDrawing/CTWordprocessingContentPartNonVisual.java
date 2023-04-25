/*
 * XML Type:  CT_WordprocessingContentPartNonVisual
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing
 * Java type: org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingContentPartNonVisual
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_WordprocessingContentPartNonVisual(@http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing).
 *
 * This is a complex type.
 */
public interface CTWordprocessingContentPartNonVisual extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingContentPartNonVisual> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctwordprocessingcontentpartnonvisual94a1type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "cNvPr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps getCNvPr();

    /**
     * True if has "cNvPr" element
     */
    boolean isSetCNvPr();

    /**
     * Sets the "cNvPr" element
     */
    void setCNvPr(org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps cNvPr);

    /**
     * Appends and returns a new empty "cNvPr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps addNewCNvPr();

    /**
     * Unsets the "cNvPr" element
     */
    void unsetCNvPr();

    /**
     * Gets the "cNvContentPartPr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualContentPartProperties getCNvContentPartPr();

    /**
     * True if has "cNvContentPartPr" element
     */
    boolean isSetCNvContentPartPr();

    /**
     * Sets the "cNvContentPartPr" element
     */
    void setCNvContentPartPr(org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualContentPartProperties cNvContentPartPr);

    /**
     * Appends and returns a new empty "cNvContentPartPr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualContentPartProperties addNewCNvContentPartPr();

    /**
     * Unsets the "cNvContentPartPr" element
     */
    void unsetCNvContentPartPr();
}
