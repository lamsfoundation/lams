/*
 * XML Type:  CT_X
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTX
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_X(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTX extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTX> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctx8517type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "v" attribute
     */
    int getV();

    /**
     * Gets (as xml) the "v" attribute
     */
    org.apache.xmlbeans.XmlInt xgetV();

    /**
     * True if has "v" attribute
     */
    boolean isSetV();

    /**
     * Sets the "v" attribute
     */
    void setV(int v);

    /**
     * Sets (as xml) the "v" attribute
     */
    void xsetV(org.apache.xmlbeans.XmlInt v);

    /**
     * Unsets the "v" attribute
     */
    void unsetV();
}
