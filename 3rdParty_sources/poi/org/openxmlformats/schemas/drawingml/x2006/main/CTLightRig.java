/*
 * XML Type:  CT_LightRig
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTLightRig
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_LightRig(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTLightRig extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTLightRig> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctlightrigad35type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "rot" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTSphereCoords getRot();

    /**
     * True if has "rot" element
     */
    boolean isSetRot();

    /**
     * Sets the "rot" element
     */
    void setRot(org.openxmlformats.schemas.drawingml.x2006.main.CTSphereCoords rot);

    /**
     * Appends and returns a new empty "rot" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTSphereCoords addNewRot();

    /**
     * Unsets the "rot" element
     */
    void unsetRot();

    /**
     * Gets the "rig" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STLightRigType.Enum getRig();

    /**
     * Gets (as xml) the "rig" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STLightRigType xgetRig();

    /**
     * Sets the "rig" attribute
     */
    void setRig(org.openxmlformats.schemas.drawingml.x2006.main.STLightRigType.Enum rig);

    /**
     * Sets (as xml) the "rig" attribute
     */
    void xsetRig(org.openxmlformats.schemas.drawingml.x2006.main.STLightRigType rig);

    /**
     * Gets the "dir" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STLightRigDirection.Enum getDir();

    /**
     * Gets (as xml) the "dir" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STLightRigDirection xgetDir();

    /**
     * Sets the "dir" attribute
     */
    void setDir(org.openxmlformats.schemas.drawingml.x2006.main.STLightRigDirection.Enum dir);

    /**
     * Sets (as xml) the "dir" attribute
     */
    void xsetDir(org.openxmlformats.schemas.drawingml.x2006.main.STLightRigDirection dir);
}
