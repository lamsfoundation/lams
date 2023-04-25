/*
 * XML Type:  CT_DatastoreSchemaRef
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/customXml
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.customXml.CTDatastoreSchemaRef
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.customXml;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_DatastoreSchemaRef(@http://schemas.openxmlformats.org/officeDocument/2006/customXml).
 *
 * This is a complex type.
 */
public interface CTDatastoreSchemaRef extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.customXml.CTDatastoreSchemaRef> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctdatastoreschemaref6e37type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "uri" attribute
     */
    java.lang.String getUri();

    /**
     * Gets (as xml) the "uri" attribute
     */
    org.apache.xmlbeans.XmlString xgetUri();

    /**
     * Sets the "uri" attribute
     */
    void setUri(java.lang.String uri);

    /**
     * Sets (as xml) the "uri" attribute
     */
    void xsetUri(org.apache.xmlbeans.XmlString uri);
}
