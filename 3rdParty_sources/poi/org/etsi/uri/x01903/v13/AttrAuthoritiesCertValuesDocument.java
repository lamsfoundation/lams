/*
 * An XML document type.
 * Localname: AttrAuthoritiesCertValues
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.AttrAuthoritiesCertValuesDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one AttrAuthoritiesCertValues(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public interface AttrAuthoritiesCertValuesDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.AttrAuthoritiesCertValuesDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "attrauthoritiescertvalues4ca8doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "AttrAuthoritiesCertValues" element
     */
    org.etsi.uri.x01903.v13.CertificateValuesType getAttrAuthoritiesCertValues();

    /**
     * Sets the "AttrAuthoritiesCertValues" element
     */
    void setAttrAuthoritiesCertValues(org.etsi.uri.x01903.v13.CertificateValuesType attrAuthoritiesCertValues);

    /**
     * Appends and returns a new empty "AttrAuthoritiesCertValues" element
     */
    org.etsi.uri.x01903.v13.CertificateValuesType addNewAttrAuthoritiesCertValues();
}
