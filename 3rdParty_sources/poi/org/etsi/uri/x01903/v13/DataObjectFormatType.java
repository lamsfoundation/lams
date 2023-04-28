/*
 * XML Type:  DataObjectFormatType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.DataObjectFormatType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML DataObjectFormatType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public interface DataObjectFormatType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.DataObjectFormatType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "dataobjectformattype44eetype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "Description" element
     */
    java.lang.String getDescription();

    /**
     * Gets (as xml) the "Description" element
     */
    org.apache.xmlbeans.XmlString xgetDescription();

    /**
     * True if has "Description" element
     */
    boolean isSetDescription();

    /**
     * Sets the "Description" element
     */
    void setDescription(java.lang.String description);

    /**
     * Sets (as xml) the "Description" element
     */
    void xsetDescription(org.apache.xmlbeans.XmlString description);

    /**
     * Unsets the "Description" element
     */
    void unsetDescription();

    /**
     * Gets the "ObjectIdentifier" element
     */
    org.etsi.uri.x01903.v13.ObjectIdentifierType getObjectIdentifier();

    /**
     * True if has "ObjectIdentifier" element
     */
    boolean isSetObjectIdentifier();

    /**
     * Sets the "ObjectIdentifier" element
     */
    void setObjectIdentifier(org.etsi.uri.x01903.v13.ObjectIdentifierType objectIdentifier);

    /**
     * Appends and returns a new empty "ObjectIdentifier" element
     */
    org.etsi.uri.x01903.v13.ObjectIdentifierType addNewObjectIdentifier();

    /**
     * Unsets the "ObjectIdentifier" element
     */
    void unsetObjectIdentifier();

    /**
     * Gets the "MimeType" element
     */
    java.lang.String getMimeType();

    /**
     * Gets (as xml) the "MimeType" element
     */
    org.apache.xmlbeans.XmlString xgetMimeType();

    /**
     * True if has "MimeType" element
     */
    boolean isSetMimeType();

    /**
     * Sets the "MimeType" element
     */
    void setMimeType(java.lang.String mimeType);

    /**
     * Sets (as xml) the "MimeType" element
     */
    void xsetMimeType(org.apache.xmlbeans.XmlString mimeType);

    /**
     * Unsets the "MimeType" element
     */
    void unsetMimeType();

    /**
     * Gets the "Encoding" element
     */
    java.lang.String getEncoding();

    /**
     * Gets (as xml) the "Encoding" element
     */
    org.apache.xmlbeans.XmlAnyURI xgetEncoding();

    /**
     * True if has "Encoding" element
     */
    boolean isSetEncoding();

    /**
     * Sets the "Encoding" element
     */
    void setEncoding(java.lang.String encoding);

    /**
     * Sets (as xml) the "Encoding" element
     */
    void xsetEncoding(org.apache.xmlbeans.XmlAnyURI encoding);

    /**
     * Unsets the "Encoding" element
     */
    void unsetEncoding();

    /**
     * Gets the "ObjectReference" attribute
     */
    java.lang.String getObjectReference();

    /**
     * Gets (as xml) the "ObjectReference" attribute
     */
    org.apache.xmlbeans.XmlAnyURI xgetObjectReference();

    /**
     * Sets the "ObjectReference" attribute
     */
    void setObjectReference(java.lang.String objectReference);

    /**
     * Sets (as xml) the "ObjectReference" attribute
     */
    void xsetObjectReference(org.apache.xmlbeans.XmlAnyURI objectReference);
}
