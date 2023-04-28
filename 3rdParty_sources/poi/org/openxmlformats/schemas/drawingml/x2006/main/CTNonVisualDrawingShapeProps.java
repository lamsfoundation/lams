/*
 * XML Type:  CT_NonVisualDrawingShapeProps
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingShapeProps
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_NonVisualDrawingShapeProps(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTNonVisualDrawingShapeProps extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingShapeProps> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctnonvisualdrawingshapepropsf17btype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "spLocks" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTShapeLocking getSpLocks();

    /**
     * True if has "spLocks" element
     */
    boolean isSetSpLocks();

    /**
     * Sets the "spLocks" element
     */
    void setSpLocks(org.openxmlformats.schemas.drawingml.x2006.main.CTShapeLocking spLocks);

    /**
     * Appends and returns a new empty "spLocks" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTShapeLocking addNewSpLocks();

    /**
     * Unsets the "spLocks" element
     */
    void unsetSpLocks();

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
     * Gets the "txBox" attribute
     */
    boolean getTxBox();

    /**
     * Gets (as xml) the "txBox" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetTxBox();

    /**
     * True if has "txBox" attribute
     */
    boolean isSetTxBox();

    /**
     * Sets the "txBox" attribute
     */
    void setTxBox(boolean txBox);

    /**
     * Sets (as xml) the "txBox" attribute
     */
    void xsetTxBox(org.apache.xmlbeans.XmlBoolean txBox);

    /**
     * Unsets the "txBox" attribute
     */
    void unsetTxBox();
}
