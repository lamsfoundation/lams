/*
 * XML Type:  SignerRoleType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.SignerRoleType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML SignerRoleType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public interface SignerRoleType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.SignerRoleType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "signerroletypef58etype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "ClaimedRoles" element
     */
    org.etsi.uri.x01903.v13.ClaimedRolesListType getClaimedRoles();

    /**
     * True if has "ClaimedRoles" element
     */
    boolean isSetClaimedRoles();

    /**
     * Sets the "ClaimedRoles" element
     */
    void setClaimedRoles(org.etsi.uri.x01903.v13.ClaimedRolesListType claimedRoles);

    /**
     * Appends and returns a new empty "ClaimedRoles" element
     */
    org.etsi.uri.x01903.v13.ClaimedRolesListType addNewClaimedRoles();

    /**
     * Unsets the "ClaimedRoles" element
     */
    void unsetClaimedRoles();

    /**
     * Gets the "CertifiedRoles" element
     */
    org.etsi.uri.x01903.v13.CertifiedRolesListType getCertifiedRoles();

    /**
     * True if has "CertifiedRoles" element
     */
    boolean isSetCertifiedRoles();

    /**
     * Sets the "CertifiedRoles" element
     */
    void setCertifiedRoles(org.etsi.uri.x01903.v13.CertifiedRolesListType certifiedRoles);

    /**
     * Appends and returns a new empty "CertifiedRoles" element
     */
    org.etsi.uri.x01903.v13.CertifiedRolesListType addNewCertifiedRoles();

    /**
     * Unsets the "CertifiedRoles" element
     */
    void unsetCertifiedRoles();
}
