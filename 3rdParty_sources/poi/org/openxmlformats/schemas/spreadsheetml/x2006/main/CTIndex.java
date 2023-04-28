/*
 * XML Type:  CT_Index
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIndex
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Index(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTIndex extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIndex> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctindex5371type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "v" attribute
     */
    long getV();

    /**
     * Gets (as xml) the "v" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetV();

    /**
     * Sets the "v" attribute
     */
    void setV(long v);

    /**
     * Sets (as xml) the "v" attribute
     */
    void xsetV(org.apache.xmlbeans.XmlUnsignedInt v);
}
