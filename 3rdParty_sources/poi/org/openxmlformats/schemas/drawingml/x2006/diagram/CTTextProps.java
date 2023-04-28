/*
 * XML Type:  CT_TextProps
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.CTTextProps
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TextProps(@http://schemas.openxmlformats.org/drawingml/2006/diagram).
 *
 * This is a complex type.
 */
public interface CTTextProps extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.diagram.CTTextProps> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttextprops2c38type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "sp3d" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTShape3D getSp3D();

    /**
     * True if has "sp3d" element
     */
    boolean isSetSp3D();

    /**
     * Sets the "sp3d" element
     */
    void setSp3D(org.openxmlformats.schemas.drawingml.x2006.main.CTShape3D sp3D);

    /**
     * Appends and returns a new empty "sp3d" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTShape3D addNewSp3D();

    /**
     * Unsets the "sp3d" element
     */
    void unsetSp3D();

    /**
     * Gets the "flatTx" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTFlatText getFlatTx();

    /**
     * True if has "flatTx" element
     */
    boolean isSetFlatTx();

    /**
     * Sets the "flatTx" element
     */
    void setFlatTx(org.openxmlformats.schemas.drawingml.x2006.main.CTFlatText flatTx);

    /**
     * Appends and returns a new empty "flatTx" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTFlatText addNewFlatTx();

    /**
     * Unsets the "flatTx" element
     */
    void unsetFlatTx();
}
