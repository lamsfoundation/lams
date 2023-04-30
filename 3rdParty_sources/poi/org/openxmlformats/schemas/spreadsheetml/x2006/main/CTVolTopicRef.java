/*
 * XML Type:  CT_VolTopicRef
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolTopicRef
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_VolTopicRef(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTVolTopicRef extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolTopicRef> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctvoltopicrefacf6type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "r" attribute
     */
    java.lang.String getR();

    /**
     * Gets (as xml) the "r" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellRef xgetR();

    /**
     * Sets the "r" attribute
     */
    void setR(java.lang.String r);

    /**
     * Sets (as xml) the "r" attribute
     */
    void xsetR(org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellRef r);

    /**
     * Gets the "s" attribute
     */
    long getS();

    /**
     * Gets (as xml) the "s" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetS();

    /**
     * Sets the "s" attribute
     */
    void setS(long s);

    /**
     * Sets (as xml) the "s" attribute
     */
    void xsetS(org.apache.xmlbeans.XmlUnsignedInt s);
}
