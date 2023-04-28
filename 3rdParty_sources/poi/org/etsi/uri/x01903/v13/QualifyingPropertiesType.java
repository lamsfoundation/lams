/*
 * XML Type:  QualifyingPropertiesType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.QualifyingPropertiesType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML QualifyingPropertiesType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public interface QualifyingPropertiesType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.QualifyingPropertiesType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "qualifyingpropertiestype9e16type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "SignedProperties" element
     */
    org.etsi.uri.x01903.v13.SignedPropertiesType getSignedProperties();

    /**
     * True if has "SignedProperties" element
     */
    boolean isSetSignedProperties();

    /**
     * Sets the "SignedProperties" element
     */
    void setSignedProperties(org.etsi.uri.x01903.v13.SignedPropertiesType signedProperties);

    /**
     * Appends and returns a new empty "SignedProperties" element
     */
    org.etsi.uri.x01903.v13.SignedPropertiesType addNewSignedProperties();

    /**
     * Unsets the "SignedProperties" element
     */
    void unsetSignedProperties();

    /**
     * Gets the "UnsignedProperties" element
     */
    org.etsi.uri.x01903.v13.UnsignedPropertiesType getUnsignedProperties();

    /**
     * True if has "UnsignedProperties" element
     */
    boolean isSetUnsignedProperties();

    /**
     * Sets the "UnsignedProperties" element
     */
    void setUnsignedProperties(org.etsi.uri.x01903.v13.UnsignedPropertiesType unsignedProperties);

    /**
     * Appends and returns a new empty "UnsignedProperties" element
     */
    org.etsi.uri.x01903.v13.UnsignedPropertiesType addNewUnsignedProperties();

    /**
     * Unsets the "UnsignedProperties" element
     */
    void unsetUnsignedProperties();

    /**
     * Gets the "Target" attribute
     */
    java.lang.String getTarget();

    /**
     * Gets (as xml) the "Target" attribute
     */
    org.apache.xmlbeans.XmlAnyURI xgetTarget();

    /**
     * Sets the "Target" attribute
     */
    void setTarget(java.lang.String target);

    /**
     * Sets (as xml) the "Target" attribute
     */
    void xsetTarget(org.apache.xmlbeans.XmlAnyURI target);

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
