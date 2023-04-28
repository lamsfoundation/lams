/*
 * XML Type:  ObjectType
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.ObjectType
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ObjectType(@http://www.w3.org/2000/09/xmldsig#).
 *
 * This is a complex type.
 */
public interface ObjectType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.w3.x2000.x09.xmldsig.ObjectType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "objecttypec966type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "Id" attribute
     */
    java.lang.String getId();

    /**
     * Gets (as xml) the "Id" attribute
     */
    org.apache.xmlbeans.XmlID xgetId();

    /**
     * True if has "Id" attribute
     */
    boolean isSetId();

    /**
     * Sets the "Id" attribute
     */
    void setId(java.lang.String id);

    /**
     * Sets (as xml) the "Id" attribute
     */
    void xsetId(org.apache.xmlbeans.XmlID id);

    /**
     * Unsets the "Id" attribute
     */
    void unsetId();

    /**
     * Gets the "MimeType" attribute
     */
    java.lang.String getMimeType();

    /**
     * Gets (as xml) the "MimeType" attribute
     */
    org.apache.xmlbeans.XmlString xgetMimeType();

    /**
     * True if has "MimeType" attribute
     */
    boolean isSetMimeType();

    /**
     * Sets the "MimeType" attribute
     */
    void setMimeType(java.lang.String mimeType);

    /**
     * Sets (as xml) the "MimeType" attribute
     */
    void xsetMimeType(org.apache.xmlbeans.XmlString mimeType);

    /**
     * Unsets the "MimeType" attribute
     */
    void unsetMimeType();

    /**
     * Gets the "Encoding" attribute
     */
    java.lang.String getEncoding();

    /**
     * Gets (as xml) the "Encoding" attribute
     */
    org.apache.xmlbeans.XmlAnyURI xgetEncoding();

    /**
     * True if has "Encoding" attribute
     */
    boolean isSetEncoding();

    /**
     * Sets the "Encoding" attribute
     */
    void setEncoding(java.lang.String encoding);

    /**
     * Sets (as xml) the "Encoding" attribute
     */
    void xsetEncoding(org.apache.xmlbeans.XmlAnyURI encoding);

    /**
     * Unsets the "Encoding" attribute
     */
    void unsetEncoding();
}
