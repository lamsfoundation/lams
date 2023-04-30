/*
 * XML Type:  SignaturePolicyIdentifierType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.SignaturePolicyIdentifierType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML SignaturePolicyIdentifierType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public interface SignaturePolicyIdentifierType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.SignaturePolicyIdentifierType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "signaturepolicyidentifiertype80aftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "SignaturePolicyId" element
     */
    org.etsi.uri.x01903.v13.SignaturePolicyIdType getSignaturePolicyId();

    /**
     * True if has "SignaturePolicyId" element
     */
    boolean isSetSignaturePolicyId();

    /**
     * Sets the "SignaturePolicyId" element
     */
    void setSignaturePolicyId(org.etsi.uri.x01903.v13.SignaturePolicyIdType signaturePolicyId);

    /**
     * Appends and returns a new empty "SignaturePolicyId" element
     */
    org.etsi.uri.x01903.v13.SignaturePolicyIdType addNewSignaturePolicyId();

    /**
     * Unsets the "SignaturePolicyId" element
     */
    void unsetSignaturePolicyId();

    /**
     * Gets the "SignaturePolicyImplied" element
     */
    org.apache.xmlbeans.XmlObject getSignaturePolicyImplied();

    /**
     * True if has "SignaturePolicyImplied" element
     */
    boolean isSetSignaturePolicyImplied();

    /**
     * Sets the "SignaturePolicyImplied" element
     */
    void setSignaturePolicyImplied(org.apache.xmlbeans.XmlObject signaturePolicyImplied);

    /**
     * Appends and returns a new empty "SignaturePolicyImplied" element
     */
    org.apache.xmlbeans.XmlObject addNewSignaturePolicyImplied();

    /**
     * Unsets the "SignaturePolicyImplied" element
     */
    void unsetSignaturePolicyImplied();
}
