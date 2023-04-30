/*
 * XML Type:  UnsignedPropertiesType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.UnsignedPropertiesType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML UnsignedPropertiesType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public interface UnsignedPropertiesType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.UnsignedPropertiesType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "unsignedpropertiestype49d6type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "UnsignedSignatureProperties" element
     */
    org.etsi.uri.x01903.v13.UnsignedSignaturePropertiesType getUnsignedSignatureProperties();

    /**
     * True if has "UnsignedSignatureProperties" element
     */
    boolean isSetUnsignedSignatureProperties();

    /**
     * Sets the "UnsignedSignatureProperties" element
     */
    void setUnsignedSignatureProperties(org.etsi.uri.x01903.v13.UnsignedSignaturePropertiesType unsignedSignatureProperties);

    /**
     * Appends and returns a new empty "UnsignedSignatureProperties" element
     */
    org.etsi.uri.x01903.v13.UnsignedSignaturePropertiesType addNewUnsignedSignatureProperties();

    /**
     * Unsets the "UnsignedSignatureProperties" element
     */
    void unsetUnsignedSignatureProperties();

    /**
     * Gets the "UnsignedDataObjectProperties" element
     */
    org.etsi.uri.x01903.v13.UnsignedDataObjectPropertiesType getUnsignedDataObjectProperties();

    /**
     * True if has "UnsignedDataObjectProperties" element
     */
    boolean isSetUnsignedDataObjectProperties();

    /**
     * Sets the "UnsignedDataObjectProperties" element
     */
    void setUnsignedDataObjectProperties(org.etsi.uri.x01903.v13.UnsignedDataObjectPropertiesType unsignedDataObjectProperties);

    /**
     * Appends and returns a new empty "UnsignedDataObjectProperties" element
     */
    org.etsi.uri.x01903.v13.UnsignedDataObjectPropertiesType addNewUnsignedDataObjectProperties();

    /**
     * Unsets the "UnsignedDataObjectProperties" element
     */
    void unsetUnsignedDataObjectProperties();

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
}
