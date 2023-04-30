/*
 * XML Type:  CT_FieldUsage
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFieldUsage
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_FieldUsage(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTFieldUsage extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFieldUsage> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctfieldusagedb90type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "x" attribute
     */
    int getX();

    /**
     * Gets (as xml) the "x" attribute
     */
    org.apache.xmlbeans.XmlInt xgetX();

    /**
     * Sets the "x" attribute
     */
    void setX(int x);

    /**
     * Sets (as xml) the "x" attribute
     */
    void xsetX(org.apache.xmlbeans.XmlInt x);
}
