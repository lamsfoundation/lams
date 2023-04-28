/*
 * XML Type:  CT_Scene3D
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTScene3D
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Scene3D(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTScene3D extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTScene3D> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctscene3d736etype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "camera" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTCamera getCamera();

    /**
     * Sets the "camera" element
     */
    void setCamera(org.openxmlformats.schemas.drawingml.x2006.main.CTCamera camera);

    /**
     * Appends and returns a new empty "camera" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTCamera addNewCamera();

    /**
     * Gets the "lightRig" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTLightRig getLightRig();

    /**
     * Sets the "lightRig" element
     */
    void setLightRig(org.openxmlformats.schemas.drawingml.x2006.main.CTLightRig lightRig);

    /**
     * Appends and returns a new empty "lightRig" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTLightRig addNewLightRig();

    /**
     * Gets the "backdrop" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTBackdrop getBackdrop();

    /**
     * True if has "backdrop" element
     */
    boolean isSetBackdrop();

    /**
     * Sets the "backdrop" element
     */
    void setBackdrop(org.openxmlformats.schemas.drawingml.x2006.main.CTBackdrop backdrop);

    /**
     * Appends and returns a new empty "backdrop" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTBackdrop addNewBackdrop();

    /**
     * Unsets the "backdrop" element
     */
    void unsetBackdrop();

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
