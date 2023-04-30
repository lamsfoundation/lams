/*
 * XML Type:  CT_ConnectorNonVisual
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTConnectorNonVisual
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_ConnectorNonVisual(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTConnectorNonVisual extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTConnectorNonVisual> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctconnectornonvisual0f45type");
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
     * Gets the "cNvCxnSpPr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualConnectorProperties getCNvCxnSpPr();

    /**
     * Sets the "cNvCxnSpPr" element
     */
    void setCNvCxnSpPr(org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualConnectorProperties cNvCxnSpPr);

    /**
     * Appends and returns a new empty "cNvCxnSpPr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualConnectorProperties addNewCNvCxnSpPr();

    /**
     * Gets the "nvPr" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTApplicationNonVisualDrawingProps getNvPr();

    /**
     * Sets the "nvPr" element
     */
    void setNvPr(org.openxmlformats.schemas.presentationml.x2006.main.CTApplicationNonVisualDrawingProps nvPr);

    /**
     * Appends and returns a new empty "nvPr" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTApplicationNonVisualDrawingProps addNewNvPr();
}
