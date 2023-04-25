/*
 * XML Type:  CT_PresetGeometry2D
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTPresetGeometry2D
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_PresetGeometry2D(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTPresetGeometry2D extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTPresetGeometry2D> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctpresetgeometry2db1detype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "avLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuideList getAvLst();

    /**
     * True if has "avLst" element
     */
    boolean isSetAvLst();

    /**
     * Sets the "avLst" element
     */
    void setAvLst(org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuideList avLst);

    /**
     * Appends and returns a new empty "avLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuideList addNewAvLst();

    /**
     * Unsets the "avLst" element
     */
    void unsetAvLst();

    /**
     * Gets the "prst" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STShapeType.Enum getPrst();

    /**
     * Gets (as xml) the "prst" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STShapeType xgetPrst();

    /**
     * Sets the "prst" attribute
     */
    void setPrst(org.openxmlformats.schemas.drawingml.x2006.main.STShapeType.Enum prst);

    /**
     * Sets (as xml) the "prst" attribute
     */
    void xsetPrst(org.openxmlformats.schemas.drawingml.x2006.main.STShapeType prst);
}
