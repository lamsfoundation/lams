/*
 * XML Type:  CT_NonVisualGroupDrawingShapeProps
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualGroupDrawingShapeProps
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_NonVisualGroupDrawingShapeProps(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTNonVisualGroupDrawingShapeProps extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualGroupDrawingShapeProps> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctnonvisualgroupdrawingshapeprops610ctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "grpSpLocks" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGroupLocking getGrpSpLocks();

    /**
     * True if has "grpSpLocks" element
     */
    boolean isSetGrpSpLocks();

    /**
     * Sets the "grpSpLocks" element
     */
    void setGrpSpLocks(org.openxmlformats.schemas.drawingml.x2006.main.CTGroupLocking grpSpLocks);

    /**
     * Appends and returns a new empty "grpSpLocks" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGroupLocking addNewGrpSpLocks();

    /**
     * Unsets the "grpSpLocks" element
     */
    void unsetGrpSpLocks();

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
}
