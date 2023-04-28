/*
 * An XML document type.
 * Localname: ftr
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.FtrDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one ftr(@http://schemas.openxmlformats.org/wordprocessingml/2006/main) element.
 *
 * This is a complex type.
 */
public interface FtrDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.FtrDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ftre182doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "ftr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHdrFtr getFtr();

    /**
     * Sets the "ftr" element
     */
    void setFtr(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHdrFtr ftr);

    /**
     * Appends and returns a new empty "ftr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHdrFtr addNewFtr();
}
