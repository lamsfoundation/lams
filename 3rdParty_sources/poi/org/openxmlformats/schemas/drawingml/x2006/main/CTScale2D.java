/*
 * XML Type:  CT_Scale2D
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTScale2D
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Scale2D(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTScale2D extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTScale2D> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctscale2dbf4dtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "sx" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTRatio getSx();

    /**
     * Sets the "sx" element
     */
    void setSx(org.openxmlformats.schemas.drawingml.x2006.main.CTRatio sx);

    /**
     * Appends and returns a new empty "sx" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTRatio addNewSx();

    /**
     * Gets the "sy" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTRatio getSy();

    /**
     * Sets the "sy" element
     */
    void setSy(org.openxmlformats.schemas.drawingml.x2006.main.CTRatio sy);

    /**
     * Appends and returns a new empty "sy" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTRatio addNewSy();
}
