/*
 * XML Type:  SignedPropertiesType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.SignedPropertiesType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML SignedPropertiesType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public interface SignedPropertiesType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.SignedPropertiesType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "signedpropertiestype163dtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "SignedSignatureProperties" element
     */
    org.etsi.uri.x01903.v13.SignedSignaturePropertiesType getSignedSignatureProperties();

    /**
     * True if has "SignedSignatureProperties" element
     */
    boolean isSetSignedSignatureProperties();

    /**
     * Sets the "SignedSignatureProperties" element
     */
    void setSignedSignatureProperties(org.etsi.uri.x01903.v13.SignedSignaturePropertiesType signedSignatureProperties);

    /**
     * Appends and returns a new empty "SignedSignatureProperties" element
     */
    org.etsi.uri.x01903.v13.SignedSignaturePropertiesType addNewSignedSignatureProperties();

    /**
     * Unsets the "SignedSignatureProperties" element
     */
    void unsetSignedSignatureProperties();

    /**
     * Gets the "SignedDataObjectProperties" element
     */
    org.etsi.uri.x01903.v13.SignedDataObjectPropertiesType getSignedDataObjectProperties();

    /**
     * True if has "SignedDataObjectProperties" element
     */
    boolean isSetSignedDataObjectProperties();

    /**
     * Sets the "SignedDataObjectProperties" element
     */
    void setSignedDataObjectProperties(org.etsi.uri.x01903.v13.SignedDataObjectPropertiesType signedDataObjectProperties);

    /**
     * Appends and returns a new empty "SignedDataObjectProperties" element
     */
    org.etsi.uri.x01903.v13.SignedDataObjectPropertiesType addNewSignedDataObjectProperties();

    /**
     * Unsets the "SignedDataObjectProperties" element
     */
    void unsetSignedDataObjectProperties();

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
