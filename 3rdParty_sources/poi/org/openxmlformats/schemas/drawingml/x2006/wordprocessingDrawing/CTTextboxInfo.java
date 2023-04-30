/*
 * XML Type:  CT_TextboxInfo
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing
 * Java type: org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTTextboxInfo
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TextboxInfo(@http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing).
 *
 * This is a complex type.
 */
public interface CTTextboxInfo extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTTextboxInfo> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttextboxinfo7dfdtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "txbxContent" element
     */
    org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTTxbxContent getTxbxContent();

    /**
     * Sets the "txbxContent" element
     */
    void setTxbxContent(org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTTxbxContent txbxContent);

    /**
     * Appends and returns a new empty "txbxContent" element
     */
    org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTTxbxContent addNewTxbxContent();

    /**
     * Gets the "extLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList getExtLst();

    /**
     * True if has "extLst" element
     */
    boolean isSetExtLst();

    /**
     * Sets the "extLst" element
     */
    void setExtLst(org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList extLst);

    /**
     * Appends and returns a new empty "extLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList addNewExtLst();

    /**
     * Unsets the "extLst" element
     */
    void unsetExtLst();

    /**
     * Gets the "id" attribute
     */
    int getId();

    /**
     * Gets (as xml) the "id" attribute
     */
    org.apache.xmlbeans.XmlUnsignedShort xgetId();

    /**
     * True if has "id" attribute
     */
    boolean isSetId();

    /**
     * Sets the "id" attribute
     */
    void setId(int id);

    /**
     * Sets (as xml) the "id" attribute
     */
    void xsetId(org.apache.xmlbeans.XmlUnsignedShort id);

    /**
     * Unsets the "id" attribute
     */
    void unsetId();
}
