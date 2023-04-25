/*
 * An XML document type.
 * Localname: sldSyncPr
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.SldSyncPrDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one sldSyncPr(@http://schemas.openxmlformats.org/presentationml/2006/main) element.
 *
 * This is a complex type.
 */
public interface SldSyncPrDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.SldSyncPrDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "sldsyncpr54dbdoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "sldSyncPr" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTSlideSyncProperties getSldSyncPr();

    /**
     * Sets the "sldSyncPr" element
     */
    void setSldSyncPr(org.openxmlformats.schemas.presentationml.x2006.main.CTSlideSyncProperties sldSyncPr);

    /**
     * Appends and returns a new empty "sldSyncPr" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTSlideSyncProperties addNewSldSyncPr();
}
