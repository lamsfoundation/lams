/*
 * XML Type:  CT_Shape
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTShape
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Shape(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTShape extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTShape> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctshapecfcetype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "nvSpPr" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTShapeNonVisual getNvSpPr();

    /**
     * Sets the "nvSpPr" element
     */
    void setNvSpPr(org.openxmlformats.schemas.presentationml.x2006.main.CTShapeNonVisual nvSpPr);

    /**
     * Appends and returns a new empty "nvSpPr" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTShapeNonVisual addNewNvSpPr();

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
     * Gets the "txBody" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody getTxBody();

    /**
     * True if has "txBody" element
     */
    boolean isSetTxBody();

    /**
     * Sets the "txBody" element
     */
    void setTxBody(org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody txBody);

    /**
     * Appends and returns a new empty "txBody" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody addNewTxBody();

    /**
     * Unsets the "txBody" element
     */
    void unsetTxBody();

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

    /**
     * Gets the "useBgFill" attribute
     */
    boolean getUseBgFill();

    /**
     * Gets (as xml) the "useBgFill" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetUseBgFill();

    /**
     * True if has "useBgFill" attribute
     */
    boolean isSetUseBgFill();

    /**
     * Sets the "useBgFill" attribute
     */
    void setUseBgFill(boolean useBgFill);

    /**
     * Sets (as xml) the "useBgFill" attribute
     */
    void xsetUseBgFill(org.apache.xmlbeans.XmlBoolean useBgFill);

    /**
     * Unsets the "useBgFill" attribute
     */
    void unsetUseBgFill();
}
