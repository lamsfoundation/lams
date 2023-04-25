/*
 * An XML document type.
 * Localname: handoutMaster
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.HandoutMasterDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one handoutMaster(@http://schemas.openxmlformats.org/presentationml/2006/main) element.
 *
 * This is a complex type.
 */
public interface HandoutMasterDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.HandoutMasterDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "handoutmaster9002doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "handoutMaster" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTHandoutMaster getHandoutMaster();

    /**
     * Sets the "handoutMaster" element
     */
    void setHandoutMaster(org.openxmlformats.schemas.presentationml.x2006.main.CTHandoutMaster handoutMaster);

    /**
     * Appends and returns a new empty "handoutMaster" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTHandoutMaster addNewHandoutMaster();
}
