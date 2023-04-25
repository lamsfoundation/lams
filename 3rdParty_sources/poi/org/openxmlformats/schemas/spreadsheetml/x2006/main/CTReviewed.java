/*
 * XML Type:  CT_Reviewed
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTReviewed
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Reviewed(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTReviewed extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTReviewed> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctreviewedfa00type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "rId" attribute
     */
    long getRId();

    /**
     * Gets (as xml) the "rId" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetRId();

    /**
     * Sets the "rId" attribute
     */
    void setRId(long rId);

    /**
     * Sets (as xml) the "rId" attribute
     */
    void xsetRId(org.apache.xmlbeans.XmlUnsignedInt rId);
}
