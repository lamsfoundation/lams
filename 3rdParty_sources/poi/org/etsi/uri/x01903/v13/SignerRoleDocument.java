/*
 * An XML document type.
 * Localname: SignerRole
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.SignerRoleDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one SignerRole(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public interface SignerRoleDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.SignerRoleDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "signerrole7b44doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "SignerRole" element
     */
    org.etsi.uri.x01903.v13.SignerRoleType getSignerRole();

    /**
     * Sets the "SignerRole" element
     */
    void setSignerRole(org.etsi.uri.x01903.v13.SignerRoleType signerRole);

    /**
     * Appends and returns a new empty "SignerRole" element
     */
    org.etsi.uri.x01903.v13.SignerRoleType addNewSignerRole();
}
