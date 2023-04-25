/*
 * XML Type:  SignatureProductionPlaceType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.SignatureProductionPlaceType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML SignatureProductionPlaceType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public interface SignatureProductionPlaceType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.SignatureProductionPlaceType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "signatureproductionplacetype9684type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "City" element
     */
    java.lang.String getCity();

    /**
     * Gets (as xml) the "City" element
     */
    org.apache.xmlbeans.XmlString xgetCity();

    /**
     * True if has "City" element
     */
    boolean isSetCity();

    /**
     * Sets the "City" element
     */
    void setCity(java.lang.String city);

    /**
     * Sets (as xml) the "City" element
     */
    void xsetCity(org.apache.xmlbeans.XmlString city);

    /**
     * Unsets the "City" element
     */
    void unsetCity();

    /**
     * Gets the "StateOrProvince" element
     */
    java.lang.String getStateOrProvince();

    /**
     * Gets (as xml) the "StateOrProvince" element
     */
    org.apache.xmlbeans.XmlString xgetStateOrProvince();

    /**
     * True if has "StateOrProvince" element
     */
    boolean isSetStateOrProvince();

    /**
     * Sets the "StateOrProvince" element
     */
    void setStateOrProvince(java.lang.String stateOrProvince);

    /**
     * Sets (as xml) the "StateOrProvince" element
     */
    void xsetStateOrProvince(org.apache.xmlbeans.XmlString stateOrProvince);

    /**
     * Unsets the "StateOrProvince" element
     */
    void unsetStateOrProvince();

    /**
     * Gets the "PostalCode" element
     */
    java.lang.String getPostalCode();

    /**
     * Gets (as xml) the "PostalCode" element
     */
    org.apache.xmlbeans.XmlString xgetPostalCode();

    /**
     * True if has "PostalCode" element
     */
    boolean isSetPostalCode();

    /**
     * Sets the "PostalCode" element
     */
    void setPostalCode(java.lang.String postalCode);

    /**
     * Sets (as xml) the "PostalCode" element
     */
    void xsetPostalCode(org.apache.xmlbeans.XmlString postalCode);

    /**
     * Unsets the "PostalCode" element
     */
    void unsetPostalCode();

    /**
     * Gets the "CountryName" element
     */
    java.lang.String getCountryName();

    /**
     * Gets (as xml) the "CountryName" element
     */
    org.apache.xmlbeans.XmlString xgetCountryName();

    /**
     * True if has "CountryName" element
     */
    boolean isSetCountryName();

    /**
     * Sets the "CountryName" element
     */
    void setCountryName(java.lang.String countryName);

    /**
     * Sets (as xml) the "CountryName" element
     */
    void xsetCountryName(org.apache.xmlbeans.XmlString countryName);

    /**
     * Unsets the "CountryName" element
     */
    void unsetCountryName();
}
