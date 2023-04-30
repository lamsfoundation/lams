/*
 * XML Type:  CT_GvmlGraphicFrameNonVisual
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGraphicFrameNonVisual
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_GvmlGraphicFrameNonVisual(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTGvmlGraphicFrameNonVisual extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGraphicFrameNonVisual> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctgvmlgraphicframenonvisuald34btype");
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
     * Gets the "cNvGraphicFramePr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualGraphicFrameProperties getCNvGraphicFramePr();

    /**
     * Sets the "cNvGraphicFramePr" element
     */
    void setCNvGraphicFramePr(org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualGraphicFrameProperties cNvGraphicFramePr);

    /**
     * Appends and returns a new empty "cNvGraphicFramePr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualGraphicFrameProperties addNewCNvGraphicFramePr();
}
