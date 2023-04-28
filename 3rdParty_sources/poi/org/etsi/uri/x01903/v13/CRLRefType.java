/*
 * XML Type:  CRLRefType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.CRLRefType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CRLRefType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public interface CRLRefType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.CRLRefType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "crlreftype4444type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "DigestAlgAndValue" element
     */
    org.etsi.uri.x01903.v13.DigestAlgAndValueType getDigestAlgAndValue();

    /**
     * Sets the "DigestAlgAndValue" element
     */
    void setDigestAlgAndValue(org.etsi.uri.x01903.v13.DigestAlgAndValueType digestAlgAndValue);

    /**
     * Appends and returns a new empty "DigestAlgAndValue" element
     */
    org.etsi.uri.x01903.v13.DigestAlgAndValueType addNewDigestAlgAndValue();

    /**
     * Gets the "CRLIdentifier" element
     */
    org.etsi.uri.x01903.v13.CRLIdentifierType getCRLIdentifier();

    /**
     * True if has "CRLIdentifier" element
     */
    boolean isSetCRLIdentifier();

    /**
     * Sets the "CRLIdentifier" element
     */
    void setCRLIdentifier(org.etsi.uri.x01903.v13.CRLIdentifierType crlIdentifier);

    /**
     * Appends and returns a new empty "CRLIdentifier" element
     */
    org.etsi.uri.x01903.v13.CRLIdentifierType addNewCRLIdentifier();

    /**
     * Unsets the "CRLIdentifier" element
     */
    void unsetCRLIdentifier();
}
