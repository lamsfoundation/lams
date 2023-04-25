/*
 * XML Type:  DigestAlgAndValueType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.DigestAlgAndValueType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML DigestAlgAndValueType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public interface DigestAlgAndValueType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.DigestAlgAndValueType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "digestalgandvaluetype234etype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "DigestMethod" element
     */
    org.w3.x2000.x09.xmldsig.DigestMethodType getDigestMethod();

    /**
     * Sets the "DigestMethod" element
     */
    void setDigestMethod(org.w3.x2000.x09.xmldsig.DigestMethodType digestMethod);

    /**
     * Appends and returns a new empty "DigestMethod" element
     */
    org.w3.x2000.x09.xmldsig.DigestMethodType addNewDigestMethod();

    /**
     * Gets the "DigestValue" element
     */
    byte[] getDigestValue();

    /**
     * Gets (as xml) the "DigestValue" element
     */
    org.w3.x2000.x09.xmldsig.DigestValueType xgetDigestValue();

    /**
     * Sets the "DigestValue" element
     */
    void setDigestValue(byte[] digestValue);

    /**
     * Sets (as xml) the "DigestValue" element
     */
    void xsetDigestValue(org.w3.x2000.x09.xmldsig.DigestValueType digestValue);
}
