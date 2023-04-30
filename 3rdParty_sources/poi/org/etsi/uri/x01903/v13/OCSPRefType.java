/*
 * XML Type:  OCSPRefType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.OCSPRefType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML OCSPRefType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public interface OCSPRefType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.OCSPRefType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ocspreftype089etype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "OCSPIdentifier" element
     */
    org.etsi.uri.x01903.v13.OCSPIdentifierType getOCSPIdentifier();

    /**
     * Sets the "OCSPIdentifier" element
     */
    void setOCSPIdentifier(org.etsi.uri.x01903.v13.OCSPIdentifierType ocspIdentifier);

    /**
     * Appends and returns a new empty "OCSPIdentifier" element
     */
    org.etsi.uri.x01903.v13.OCSPIdentifierType addNewOCSPIdentifier();

    /**
     * Gets the "DigestAlgAndValue" element
     */
    org.etsi.uri.x01903.v13.DigestAlgAndValueType getDigestAlgAndValue();

    /**
     * True if has "DigestAlgAndValue" element
     */
    boolean isSetDigestAlgAndValue();

    /**
     * Sets the "DigestAlgAndValue" element
     */
    void setDigestAlgAndValue(org.etsi.uri.x01903.v13.DigestAlgAndValueType digestAlgAndValue);

    /**
     * Appends and returns a new empty "DigestAlgAndValue" element
     */
    org.etsi.uri.x01903.v13.DigestAlgAndValueType addNewDigestAlgAndValue();

    /**
     * Unsets the "DigestAlgAndValue" element
     */
    void unsetDigestAlgAndValue();
}
