/*
 * XML Type:  CT_IndexRange
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTIndexRange
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_IndexRange(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTIndexRange extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTIndexRange> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctindexrangeda7atype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "st" attribute
     */
    long getSt();

    /**
     * Gets (as xml) the "st" attribute
     */
    org.openxmlformats.schemas.presentationml.x2006.main.STIndex xgetSt();

    /**
     * Sets the "st" attribute
     */
    void setSt(long st);

    /**
     * Sets (as xml) the "st" attribute
     */
    void xsetSt(org.openxmlformats.schemas.presentationml.x2006.main.STIndex st);

    /**
     * Gets the "end" attribute
     */
    long getEnd();

    /**
     * Gets (as xml) the "end" attribute
     */
    org.openxmlformats.schemas.presentationml.x2006.main.STIndex xgetEnd();

    /**
     * Sets the "end" attribute
     */
    void setEnd(long end);

    /**
     * Sets (as xml) the "end" attribute
     */
    void xsetEnd(org.openxmlformats.schemas.presentationml.x2006.main.STIndex end);
}
