/*
 * XML Type:  EncapsulatedPKIDataType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.EncapsulatedPKIDataType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML EncapsulatedPKIDataType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is an atomic type that is a restriction of org.etsi.uri.x01903.v13.EncapsulatedPKIDataType.
 */
public interface EncapsulatedPKIDataType extends org.apache.xmlbeans.XmlBase64Binary {
    DocumentFactory<org.etsi.uri.x01903.v13.EncapsulatedPKIDataType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "encapsulatedpkidatatype4081type");
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
