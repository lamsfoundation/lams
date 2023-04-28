/*
 * XML Type:  CT_Background
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTBackground
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Background(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTBackground extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTBackground> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctbackground36f7type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "bgPr" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTBackgroundProperties getBgPr();

    /**
     * True if has "bgPr" element
     */
    boolean isSetBgPr();

    /**
     * Sets the "bgPr" element
     */
    void setBgPr(org.openxmlformats.schemas.presentationml.x2006.main.CTBackgroundProperties bgPr);

    /**
     * Appends and returns a new empty "bgPr" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTBackgroundProperties addNewBgPr();

    /**
     * Unsets the "bgPr" element
     */
    void unsetBgPr();

    /**
     * Gets the "bgRef" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference getBgRef();

    /**
     * True if has "bgRef" element
     */
    boolean isSetBgRef();

    /**
     * Sets the "bgRef" element
     */
    void setBgRef(org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference bgRef);

    /**
     * Appends and returns a new empty "bgRef" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference addNewBgRef();

    /**
     * Unsets the "bgRef" element
     */
    void unsetBgRef();

    /**
     * Gets the "bwMode" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode.Enum getBwMode();

    /**
     * Gets (as xml) the "bwMode" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode xgetBwMode();

    /**
     * True if has "bwMode" attribute
     */
    boolean isSetBwMode();

    /**
     * Sets the "bwMode" attribute
     */
    void setBwMode(org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode.Enum bwMode);

    /**
     * Sets (as xml) the "bwMode" attribute
     */
    void xsetBwMode(org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode bwMode);

    /**
     * Unsets the "bwMode" attribute
     */
    void unsetBwMode();
}
