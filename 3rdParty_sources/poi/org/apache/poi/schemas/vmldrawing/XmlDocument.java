/*
 * An XML document type.
 * Localname: xml
 * Namespace: urn:schemas-poi-apache-org:vmldrawing
 * Java type: org.apache.poi.schemas.vmldrawing.XmlDocument
 *
 * Automatically generated - do not modify.
 */
package org.apache.poi.schemas.vmldrawing;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one xml(@urn:schemas-poi-apache-org:vmldrawing) element.
 *
 * This is a complex type.
 */
public interface XmlDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.apache.poi.schemas.vmldrawing.XmlDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "xml2eb5doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "xml" element
     */
    org.apache.poi.schemas.vmldrawing.CTXML getXml();

    /**
     * Sets the "xml" element
     */
    void setXml(org.apache.poi.schemas.vmldrawing.CTXML xml);

    /**
     * Appends and returns a new empty "xml" element
     */
    org.apache.poi.schemas.vmldrawing.CTXML addNewXml();
}
