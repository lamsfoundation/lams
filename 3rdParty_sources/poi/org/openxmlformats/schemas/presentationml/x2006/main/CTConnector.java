/*
 * XML Type:  CT_Connector
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTConnector
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Connector(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTConnector extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTConnector> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctconnector3522type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "nvCxnSpPr" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTConnectorNonVisual getNvCxnSpPr();

    /**
     * Sets the "nvCxnSpPr" element
     */
    void setNvCxnSpPr(org.openxmlformats.schemas.presentationml.x2006.main.CTConnectorNonVisual nvCxnSpPr);

    /**
     * Appends and returns a new empty "nvCxnSpPr" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTConnectorNonVisual addNewNvCxnSpPr();

    /**
     * Gets the "spPr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties getSpPr();

    /**
     * Sets the "spPr" element
     */
    void setSpPr(org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties spPr);

    /**
     * Appends and returns a new empty "spPr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties addNewSpPr();

    /**
     * Gets the "style" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTShapeStyle getStyle();

    /**
     * True if has "style" element
     */
    boolean isSetStyle();

    /**
     * Sets the "style" element
     */
    void setStyle(org.openxmlformats.schemas.drawingml.x2006.main.CTShapeStyle style);

    /**
     * Appends and returns a new empty "style" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTShapeStyle addNewStyle();

    /**
     * Unsets the "style" element
     */
    void unsetStyle();

    /**
     * Gets the "extLst" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify getExtLst();

    /**
     * True if has "extLst" element
     */
    boolean isSetExtLst();

    /**
     * Sets the "extLst" element
     */
    void setExtLst(org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify extLst);

    /**
     * Appends and returns a new empty "extLst" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify addNewExtLst();

    /**
     * Unsets the "extLst" element
     */
    void unsetExtLst();
}
