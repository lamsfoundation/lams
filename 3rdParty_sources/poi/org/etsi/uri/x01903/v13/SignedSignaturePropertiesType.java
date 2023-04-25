/*
 * XML Type:  SignedSignaturePropertiesType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.SignedSignaturePropertiesType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML SignedSignaturePropertiesType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public interface SignedSignaturePropertiesType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.SignedSignaturePropertiesType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "signedsignaturepropertiestype06abtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "SigningTime" element
     */
    java.util.Calendar getSigningTime();

    /**
     * Gets (as xml) the "SigningTime" element
     */
    org.apache.xmlbeans.XmlDateTime xgetSigningTime();

    /**
     * True if has "SigningTime" element
     */
    boolean isSetSigningTime();

    /**
     * Sets the "SigningTime" element
     */
    void setSigningTime(java.util.Calendar signingTime);

    /**
     * Sets (as xml) the "SigningTime" element
     */
    void xsetSigningTime(org.apache.xmlbeans.XmlDateTime signingTime);

    /**
     * Unsets the "SigningTime" element
     */
    void unsetSigningTime();

    /**
     * Gets the "SigningCertificate" element
     */
    org.etsi.uri.x01903.v13.CertIDListType getSigningCertificate();

    /**
     * True if has "SigningCertificate" element
     */
    boolean isSetSigningCertificate();

    /**
     * Sets the "SigningCertificate" element
     */
    void setSigningCertificate(org.etsi.uri.x01903.v13.CertIDListType signingCertificate);

    /**
     * Appends and returns a new empty "SigningCertificate" element
     */
    org.etsi.uri.x01903.v13.CertIDListType addNewSigningCertificate();

    /**
     * Unsets the "SigningCertificate" element
     */
    void unsetSigningCertificate();

    /**
     * Gets the "SignaturePolicyIdentifier" element
     */
    org.etsi.uri.x01903.v13.SignaturePolicyIdentifierType getSignaturePolicyIdentifier();

    /**
     * True if has "SignaturePolicyIdentifier" element
     */
    boolean isSetSignaturePolicyIdentifier();

    /**
     * Sets the "SignaturePolicyIdentifier" element
     */
    void setSignaturePolicyIdentifier(org.etsi.uri.x01903.v13.SignaturePolicyIdentifierType signaturePolicyIdentifier);

    /**
     * Appends and returns a new empty "SignaturePolicyIdentifier" element
     */
    org.etsi.uri.x01903.v13.SignaturePolicyIdentifierType addNewSignaturePolicyIdentifier();

    /**
     * Unsets the "SignaturePolicyIdentifier" element
     */
    void unsetSignaturePolicyIdentifier();

    /**
     * Gets the "SignatureProductionPlace" element
     */
    org.etsi.uri.x01903.v13.SignatureProductionPlaceType getSignatureProductionPlace();

    /**
     * True if has "SignatureProductionPlace" element
     */
    boolean isSetSignatureProductionPlace();

    /**
     * Sets the "SignatureProductionPlace" element
     */
    void setSignatureProductionPlace(org.etsi.uri.x01903.v13.SignatureProductionPlaceType signatureProductionPlace);

    /**
     * Appends and returns a new empty "SignatureProductionPlace" element
     */
    org.etsi.uri.x01903.v13.SignatureProductionPlaceType addNewSignatureProductionPlace();

    /**
     * Unsets the "SignatureProductionPlace" element
     */
    void unsetSignatureProductionPlace();

    /**
     * Gets the "SignerRole" element
     */
    org.etsi.uri.x01903.v13.SignerRoleType getSignerRole();

    /**
     * True if has "SignerRole" element
     */
    boolean isSetSignerRole();

    /**
     * Sets the "SignerRole" element
     */
    void setSignerRole(org.etsi.uri.x01903.v13.SignerRoleType signerRole);

    /**
     * Appends and returns a new empty "SignerRole" element
     */
    org.etsi.uri.x01903.v13.SignerRoleType addNewSignerRole();

    /**
     * Unsets the "SignerRole" element
     */
    void unsetSignerRole();

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
