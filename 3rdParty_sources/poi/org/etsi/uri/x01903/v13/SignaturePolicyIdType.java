/*
 * XML Type:  SignaturePolicyIdType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.SignaturePolicyIdType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML SignaturePolicyIdType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public interface SignaturePolicyIdType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.SignaturePolicyIdType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "signaturepolicyidtype0ca1type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "SigPolicyId" element
     */
    org.etsi.uri.x01903.v13.ObjectIdentifierType getSigPolicyId();

    /**
     * Sets the "SigPolicyId" element
     */
    void setSigPolicyId(org.etsi.uri.x01903.v13.ObjectIdentifierType sigPolicyId);

    /**
     * Appends and returns a new empty "SigPolicyId" element
     */
    org.etsi.uri.x01903.v13.ObjectIdentifierType addNewSigPolicyId();

    /**
     * Gets the "Transforms" element
     */
    org.w3.x2000.x09.xmldsig.TransformsType getTransforms();

    /**
     * True if has "Transforms" element
     */
    boolean isSetTransforms();

    /**
     * Sets the "Transforms" element
     */
    void setTransforms(org.w3.x2000.x09.xmldsig.TransformsType transforms);

    /**
     * Appends and returns a new empty "Transforms" element
     */
    org.w3.x2000.x09.xmldsig.TransformsType addNewTransforms();

    /**
     * Unsets the "Transforms" element
     */
    void unsetTransforms();

    /**
     * Gets the "SigPolicyHash" element
     */
    org.etsi.uri.x01903.v13.DigestAlgAndValueType getSigPolicyHash();

    /**
     * Sets the "SigPolicyHash" element
     */
    void setSigPolicyHash(org.etsi.uri.x01903.v13.DigestAlgAndValueType sigPolicyHash);

    /**
     * Appends and returns a new empty "SigPolicyHash" element
     */
    org.etsi.uri.x01903.v13.DigestAlgAndValueType addNewSigPolicyHash();

    /**
     * Gets the "SigPolicyQualifiers" element
     */
    org.etsi.uri.x01903.v13.SigPolicyQualifiersListType getSigPolicyQualifiers();

    /**
     * True if has "SigPolicyQualifiers" element
     */
    boolean isSetSigPolicyQualifiers();

    /**
     * Sets the "SigPolicyQualifiers" element
     */
    void setSigPolicyQualifiers(org.etsi.uri.x01903.v13.SigPolicyQualifiersListType sigPolicyQualifiers);

    /**
     * Appends and returns a new empty "SigPolicyQualifiers" element
     */
    org.etsi.uri.x01903.v13.SigPolicyQualifiersListType addNewSigPolicyQualifiers();

    /**
     * Unsets the "SigPolicyQualifiers" element
     */
    void unsetSigPolicyQualifiers();
}
