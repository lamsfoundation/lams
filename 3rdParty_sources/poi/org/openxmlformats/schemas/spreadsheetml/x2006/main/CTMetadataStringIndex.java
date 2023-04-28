/*
 * XML Type:  CT_MetadataStringIndex
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataStringIndex
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_MetadataStringIndex(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTMetadataStringIndex extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataStringIndex> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctmetadatastringindex5bf1type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "x" attribute
     */
    long getX();

    /**
     * Gets (as xml) the "x" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetX();

    /**
     * Sets the "x" attribute
     */
    void setX(long x);

    /**
     * Sets (as xml) the "x" attribute
     */
    void xsetX(org.apache.xmlbeans.XmlUnsignedInt x);

    /**
     * Gets the "s" attribute
     */
    boolean getS();

    /**
     * Gets (as xml) the "s" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetS();

    /**
     * True if has "s" attribute
     */
    boolean isSetS();

    /**
     * Sets the "s" attribute
     */
    void setS(boolean s);

    /**
     * Sets (as xml) the "s" attribute
     */
    void xsetS(org.apache.xmlbeans.XmlBoolean s);

    /**
     * Unsets the "s" attribute
     */
    void unsetS();
}
