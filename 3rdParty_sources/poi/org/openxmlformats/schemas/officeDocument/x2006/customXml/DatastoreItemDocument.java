/*
 * An XML document type.
 * Localname: datastoreItem
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/customXml
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.customXml.DatastoreItemDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.customXml;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one datastoreItem(@http://schemas.openxmlformats.org/officeDocument/2006/customXml) element.
 *
 * This is a complex type.
 */
public interface DatastoreItemDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.customXml.DatastoreItemDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "datastoreitem0028doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "datastoreItem" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.customXml.CTDatastoreItem getDatastoreItem();

    /**
     * Sets the "datastoreItem" element
     */
    void setDatastoreItem(org.openxmlformats.schemas.officeDocument.x2006.customXml.CTDatastoreItem datastoreItem);

    /**
     * Appends and returns a new empty "datastoreItem" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.customXml.CTDatastoreItem addNewDatastoreItem();
}
