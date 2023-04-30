/*
 * XML Type:  CT_Cell3D
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTCell3D
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Cell3D(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTCell3D extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTCell3D> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctcell3d10eetype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "bevel" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTBevel getBevel();

    /**
     * Sets the "bevel" element
     */
    void setBevel(org.openxmlformats.schemas.drawingml.x2006.main.CTBevel bevel);

    /**
     * Appends and returns a new empty "bevel" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTBevel addNewBevel();

    /**
     * Gets the "lightRig" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTLightRig getLightRig();

    /**
     * True if has "lightRig" element
     */
    boolean isSetLightRig();

    /**
     * Sets the "lightRig" element
     */
    void setLightRig(org.openxmlformats.schemas.drawingml.x2006.main.CTLightRig lightRig);

    /**
     * Appends and returns a new empty "lightRig" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTLightRig addNewLightRig();

    /**
     * Unsets the "lightRig" element
     */
    void unsetLightRig();

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
