/*
 * XML Type:  ReferenceInfoType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.ReferenceInfoType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ReferenceInfoType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public interface ReferenceInfoType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.ReferenceInfoType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "referenceinfotype5cf5type");
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

    /**
     * Gets the "URI" attribute
     */
    java.lang.String getURI();

    /**
     * Gets (as xml) the "URI" attribute
     */
    org.apache.xmlbeans.XmlAnyURI xgetURI();

    /**
     * True if has "URI" attribute
     */
    boolean isSetURI();

    /**
     * Sets the "URI" attribute
     */
    void setURI(java.lang.String uri);

    /**
     * Sets (as xml) the "URI" attribute
     */
    void xsetURI(org.apache.xmlbeans.XmlAnyURI uri);

    /**
     * Unsets the "URI" attribute
     */
    void unsetURI();
}
