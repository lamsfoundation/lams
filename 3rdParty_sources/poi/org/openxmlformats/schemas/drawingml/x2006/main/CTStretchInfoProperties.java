/*
 * XML Type:  CT_StretchInfoProperties
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTStretchInfoProperties
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_StretchInfoProperties(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTStretchInfoProperties extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTStretchInfoProperties> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctstretchinfopropertiesde57type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "fillRect" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect getFillRect();

    /**
     * True if has "fillRect" element
     */
    boolean isSetFillRect();

    /**
     * Sets the "fillRect" element
     */
    void setFillRect(org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect fillRect);

    /**
     * Appends and returns a new empty "fillRect" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect addNewFillRect();

    /**
     * Unsets the "fillRect" element
     */
    void unsetFillRect();
}
