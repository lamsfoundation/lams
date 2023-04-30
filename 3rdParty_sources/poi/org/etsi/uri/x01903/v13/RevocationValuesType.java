/*
 * XML Type:  RevocationValuesType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.RevocationValuesType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML RevocationValuesType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public interface RevocationValuesType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.RevocationValuesType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "revocationvaluestype9a6etype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "CRLValues" element
     */
    org.etsi.uri.x01903.v13.CRLValuesType getCRLValues();

    /**
     * True if has "CRLValues" element
     */
    boolean isSetCRLValues();

    /**
     * Sets the "CRLValues" element
     */
    void setCRLValues(org.etsi.uri.x01903.v13.CRLValuesType crlValues);

    /**
     * Appends and returns a new empty "CRLValues" element
     */
    org.etsi.uri.x01903.v13.CRLValuesType addNewCRLValues();

    /**
     * Unsets the "CRLValues" element
     */
    void unsetCRLValues();

    /**
     * Gets the "OCSPValues" element
     */
    org.etsi.uri.x01903.v13.OCSPValuesType getOCSPValues();

    /**
     * True if has "OCSPValues" element
     */
    boolean isSetOCSPValues();

    /**
     * Sets the "OCSPValues" element
     */
    void setOCSPValues(org.etsi.uri.x01903.v13.OCSPValuesType ocspValues);

    /**
     * Appends and returns a new empty "OCSPValues" element
     */
    org.etsi.uri.x01903.v13.OCSPValuesType addNewOCSPValues();

    /**
     * Unsets the "OCSPValues" element
     */
    void unsetOCSPValues();

    /**
     * Gets the "OtherValues" element
     */
    org.etsi.uri.x01903.v13.OtherCertStatusValuesType getOtherValues();

    /**
     * True if has "OtherValues" element
     */
    boolean isSetOtherValues();

    /**
     * Sets the "OtherValues" element
     */
    void setOtherValues(org.etsi.uri.x01903.v13.OtherCertStatusValuesType otherValues);

    /**
     * Appends and returns a new empty "OtherValues" element
     */
    org.etsi.uri.x01903.v13.OtherCertStatusValuesType addNewOtherValues();

    /**
     * Unsets the "OtherValues" element
     */
    void unsetOtherValues();

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
