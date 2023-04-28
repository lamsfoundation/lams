/*
 * XML Type:  CT_PathShadeProperties
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTPathShadeProperties
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_PathShadeProperties(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTPathShadeProperties extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTPathShadeProperties> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctpathshadeproperties7ccctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "fillToRect" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect getFillToRect();

    /**
     * True if has "fillToRect" element
     */
    boolean isSetFillToRect();

    /**
     * Sets the "fillToRect" element
     */
    void setFillToRect(org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect fillToRect);

    /**
     * Appends and returns a new empty "fillToRect" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect addNewFillToRect();

    /**
     * Unsets the "fillToRect" element
     */
    void unsetFillToRect();

    /**
     * Gets the "path" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STPathShadeType.Enum getPath();

    /**
     * Gets (as xml) the "path" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STPathShadeType xgetPath();

    /**
     * True if has "path" attribute
     */
    boolean isSetPath();

    /**
     * Sets the "path" attribute
     */
    void setPath(org.openxmlformats.schemas.drawingml.x2006.main.STPathShadeType.Enum path);

    /**
     * Sets (as xml) the "path" attribute
     */
    void xsetPath(org.openxmlformats.schemas.drawingml.x2006.main.STPathShadeType path);

    /**
     * Unsets the "path" attribute
     */
    void unsetPath();
}
