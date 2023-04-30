/*
 * XML Type:  CT_LineEndProperties
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTLineEndProperties
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_LineEndProperties(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTLineEndProperties extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTLineEndProperties> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctlineendproperties8acbtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "type" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STLineEndType.Enum getType();

    /**
     * Gets (as xml) the "type" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STLineEndType xgetType();

    /**
     * True if has "type" attribute
     */
    boolean isSetType();

    /**
     * Sets the "type" attribute
     */
    void setType(org.openxmlformats.schemas.drawingml.x2006.main.STLineEndType.Enum type);

    /**
     * Sets (as xml) the "type" attribute
     */
    void xsetType(org.openxmlformats.schemas.drawingml.x2006.main.STLineEndType type);

    /**
     * Unsets the "type" attribute
     */
    void unsetType();

    /**
     * Gets the "w" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STLineEndWidth.Enum getW();

    /**
     * Gets (as xml) the "w" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STLineEndWidth xgetW();

    /**
     * True if has "w" attribute
     */
    boolean isSetW();

    /**
     * Sets the "w" attribute
     */
    void setW(org.openxmlformats.schemas.drawingml.x2006.main.STLineEndWidth.Enum w);

    /**
     * Sets (as xml) the "w" attribute
     */
    void xsetW(org.openxmlformats.schemas.drawingml.x2006.main.STLineEndWidth w);

    /**
     * Unsets the "w" attribute
     */
    void unsetW();

    /**
     * Gets the "len" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STLineEndLength.Enum getLen();

    /**
     * Gets (as xml) the "len" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STLineEndLength xgetLen();

    /**
     * True if has "len" attribute
     */
    boolean isSetLen();

    /**
     * Sets the "len" attribute
     */
    void setLen(org.openxmlformats.schemas.drawingml.x2006.main.STLineEndLength.Enum len);

    /**
     * Sets (as xml) the "len" attribute
     */
    void xsetLen(org.openxmlformats.schemas.drawingml.x2006.main.STLineEndLength len);

    /**
     * Unsets the "len" attribute
     */
    void unsetLen();
}
