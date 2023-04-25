/*
 * XML Type:  CT_Shape3D
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTShape3D
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Shape3D(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTShape3D extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTShape3D> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctshape3d6783type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "bevelT" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTBevel getBevelT();

    /**
     * True if has "bevelT" element
     */
    boolean isSetBevelT();

    /**
     * Sets the "bevelT" element
     */
    void setBevelT(org.openxmlformats.schemas.drawingml.x2006.main.CTBevel bevelT);

    /**
     * Appends and returns a new empty "bevelT" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTBevel addNewBevelT();

    /**
     * Unsets the "bevelT" element
     */
    void unsetBevelT();

    /**
     * Gets the "bevelB" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTBevel getBevelB();

    /**
     * True if has "bevelB" element
     */
    boolean isSetBevelB();

    /**
     * Sets the "bevelB" element
     */
    void setBevelB(org.openxmlformats.schemas.drawingml.x2006.main.CTBevel bevelB);

    /**
     * Appends and returns a new empty "bevelB" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTBevel addNewBevelB();

    /**
     * Unsets the "bevelB" element
     */
    void unsetBevelB();

    /**
     * Gets the "extrusionClr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTColor getExtrusionClr();

    /**
     * True if has "extrusionClr" element
     */
    boolean isSetExtrusionClr();

    /**
     * Sets the "extrusionClr" element
     */
    void setExtrusionClr(org.openxmlformats.schemas.drawingml.x2006.main.CTColor extrusionClr);

    /**
     * Appends and returns a new empty "extrusionClr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTColor addNewExtrusionClr();

    /**
     * Unsets the "extrusionClr" element
     */
    void unsetExtrusionClr();

    /**
     * Gets the "contourClr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTColor getContourClr();

    /**
     * True if has "contourClr" element
     */
    boolean isSetContourClr();

    /**
     * Sets the "contourClr" element
     */
    void setContourClr(org.openxmlformats.schemas.drawingml.x2006.main.CTColor contourClr);

    /**
     * Appends and returns a new empty "contourClr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTColor addNewContourClr();

    /**
     * Unsets the "contourClr" element
     */
    void unsetContourClr();

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
     * Gets the "z" attribute
     */
    java.lang.Object getZ();

    /**
     * Gets (as xml) the "z" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate xgetZ();

    /**
     * True if has "z" attribute
     */
    boolean isSetZ();

    /**
     * Sets the "z" attribute
     */
    void setZ(java.lang.Object z);

    /**
     * Sets (as xml) the "z" attribute
     */
    void xsetZ(org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate z);

    /**
     * Unsets the "z" attribute
     */
    void unsetZ();

    /**
     * Gets the "extrusionH" attribute
     */
    long getExtrusionH();

    /**
     * Gets (as xml) the "extrusionH" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate xgetExtrusionH();

    /**
     * True if has "extrusionH" attribute
     */
    boolean isSetExtrusionH();

    /**
     * Sets the "extrusionH" attribute
     */
    void setExtrusionH(long extrusionH);

    /**
     * Sets (as xml) the "extrusionH" attribute
     */
    void xsetExtrusionH(org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate extrusionH);

    /**
     * Unsets the "extrusionH" attribute
     */
    void unsetExtrusionH();

    /**
     * Gets the "contourW" attribute
     */
    long getContourW();

    /**
     * Gets (as xml) the "contourW" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate xgetContourW();

    /**
     * True if has "contourW" attribute
     */
    boolean isSetContourW();

    /**
     * Sets the "contourW" attribute
     */
    void setContourW(long contourW);

    /**
     * Sets (as xml) the "contourW" attribute
     */
    void xsetContourW(org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate contourW);

    /**
     * Unsets the "contourW" attribute
     */
    void unsetContourW();

    /**
     * Gets the "prstMaterial" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STPresetMaterialType.Enum getPrstMaterial();

    /**
     * Gets (as xml) the "prstMaterial" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STPresetMaterialType xgetPrstMaterial();

    /**
     * True if has "prstMaterial" attribute
     */
    boolean isSetPrstMaterial();

    /**
     * Sets the "prstMaterial" attribute
     */
    void setPrstMaterial(org.openxmlformats.schemas.drawingml.x2006.main.STPresetMaterialType.Enum prstMaterial);

    /**
     * Sets (as xml) the "prstMaterial" attribute
     */
    void xsetPrstMaterial(org.openxmlformats.schemas.drawingml.x2006.main.STPresetMaterialType prstMaterial);

    /**
     * Unsets the "prstMaterial" attribute
     */
    void unsetPrstMaterial();
}
