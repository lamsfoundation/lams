/*
 * XML Type:  CompleteRevocationRefsType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.CompleteRevocationRefsType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CompleteRevocationRefsType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public interface CompleteRevocationRefsType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.CompleteRevocationRefsType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "completerevocationrefstyped8a5type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "CRLRefs" element
     */
    org.etsi.uri.x01903.v13.CRLRefsType getCRLRefs();

    /**
     * True if has "CRLRefs" element
     */
    boolean isSetCRLRefs();

    /**
     * Sets the "CRLRefs" element
     */
    void setCRLRefs(org.etsi.uri.x01903.v13.CRLRefsType crlRefs);

    /**
     * Appends and returns a new empty "CRLRefs" element
     */
    org.etsi.uri.x01903.v13.CRLRefsType addNewCRLRefs();

    /**
     * Unsets the "CRLRefs" element
     */
    void unsetCRLRefs();

    /**
     * Gets the "OCSPRefs" element
     */
    org.etsi.uri.x01903.v13.OCSPRefsType getOCSPRefs();

    /**
     * True if has "OCSPRefs" element
     */
    boolean isSetOCSPRefs();

    /**
     * Sets the "OCSPRefs" element
     */
    void setOCSPRefs(org.etsi.uri.x01903.v13.OCSPRefsType ocspRefs);

    /**
     * Appends and returns a new empty "OCSPRefs" element
     */
    org.etsi.uri.x01903.v13.OCSPRefsType addNewOCSPRefs();

    /**
     * Unsets the "OCSPRefs" element
     */
    void unsetOCSPRefs();

    /**
     * Gets the "OtherRefs" element
     */
    org.etsi.uri.x01903.v13.OtherCertStatusRefsType getOtherRefs();

    /**
     * True if has "OtherRefs" element
     */
    boolean isSetOtherRefs();

    /**
     * Sets the "OtherRefs" element
     */
    void setOtherRefs(org.etsi.uri.x01903.v13.OtherCertStatusRefsType otherRefs);

    /**
     * Appends and returns a new empty "OtherRefs" element
     */
    org.etsi.uri.x01903.v13.OtherCertStatusRefsType addNewOtherRefs();

    /**
     * Unsets the "OtherRefs" element
     */
    void unsetOtherRefs();

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
