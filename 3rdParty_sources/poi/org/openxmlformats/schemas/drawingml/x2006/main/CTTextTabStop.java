/*
 * XML Type:  CT_TextTabStop
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTTextTabStop
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TextTabStop(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTextTabStop extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTTextTabStop> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttexttabstopb57btype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "pos" attribute
     */
    java.lang.Object getPos();

    /**
     * Gets (as xml) the "pos" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32 xgetPos();

    /**
     * True if has "pos" attribute
     */
    boolean isSetPos();

    /**
     * Sets the "pos" attribute
     */
    void setPos(java.lang.Object pos);

    /**
     * Sets (as xml) the "pos" attribute
     */
    void xsetPos(org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32 pos);

    /**
     * Unsets the "pos" attribute
     */
    void unsetPos();

    /**
     * Gets the "algn" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STTextTabAlignType.Enum getAlgn();

    /**
     * Gets (as xml) the "algn" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STTextTabAlignType xgetAlgn();

    /**
     * True if has "algn" attribute
     */
    boolean isSetAlgn();

    /**
     * Sets the "algn" attribute
     */
    void setAlgn(org.openxmlformats.schemas.drawingml.x2006.main.STTextTabAlignType.Enum algn);

    /**
     * Sets (as xml) the "algn" attribute
     */
    void xsetAlgn(org.openxmlformats.schemas.drawingml.x2006.main.STTextTabAlignType algn);

    /**
     * Unsets the "algn" attribute
     */
    void unsetAlgn();
}
