/*
 * An XML document type.
 * Localname: sst
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.SstDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one sst(@http://schemas.openxmlformats.org/spreadsheetml/2006/main) element.
 *
 * This is a complex type.
 */
public interface SstDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.SstDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "sstf81fdoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "sst" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSst getSst();

    /**
     * Sets the "sst" element
     */
    void setSst(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSst sst);

    /**
     * Appends and returns a new empty "sst" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSst addNewSst();
}
