/*
 * An XML document type.
 * Localname: SignatureTime
 * Namespace: http://schemas.openxmlformats.org/package/2006/digital-signature
 * Java type: org.openxmlformats.schemas.xpackage.x2006.digitalSignature.SignatureTimeDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.xpackage.x2006.digitalSignature;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one SignatureTime(@http://schemas.openxmlformats.org/package/2006/digital-signature) element.
 *
 * This is a complex type.
 */
public interface SignatureTimeDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.xpackage.x2006.digitalSignature.SignatureTimeDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "signaturetime9c91doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "SignatureTime" element
     */
    org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTSignatureTime getSignatureTime();

    /**
     * Sets the "SignatureTime" element
     */
    void setSignatureTime(org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTSignatureTime signatureTime);

    /**
     * Appends and returns a new empty "SignatureTime" element
     */
    org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTSignatureTime addNewSignatureTime();
}
