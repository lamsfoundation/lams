/*
 * An XML document type.
 * Localname: hdr
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.HdrDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one hdr(@http://schemas.openxmlformats.org/wordprocessingml/2006/main) element.
 *
 * This is a complex type.
 */
public interface HdrDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.HdrDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "hdra530doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "hdr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHdrFtr getHdr();

    /**
     * Sets the "hdr" element
     */
    void setHdr(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHdrFtr hdr);

    /**
     * Appends and returns a new empty "hdr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHdrFtr addNewHdr();
}
