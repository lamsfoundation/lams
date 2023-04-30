/*
 * XML Type:  CT_FlatText
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTFlatText
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_FlatText(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTFlatText extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTFlatText> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctflattextbc61type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


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
}
