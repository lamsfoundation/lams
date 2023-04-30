/*
 * XML Type:  PGPDataType
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.PGPDataType
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML PGPDataType(@http://www.w3.org/2000/09/xmldsig#).
 *
 * This is a complex type.
 */
public interface PGPDataType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.w3.x2000.x09.xmldsig.PGPDataType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "pgpdatatype3234type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "PGPKeyID" element
     */
    byte[] getPGPKeyID();

    /**
     * Gets (as xml) the "PGPKeyID" element
     */
    org.apache.xmlbeans.XmlBase64Binary xgetPGPKeyID();

    /**
     * True if has "PGPKeyID" element
     */
    boolean isSetPGPKeyID();

    /**
     * Sets the "PGPKeyID" element
     */
    void setPGPKeyID(byte[] pgpKeyID);

    /**
     * Sets (as xml) the "PGPKeyID" element
     */
    void xsetPGPKeyID(org.apache.xmlbeans.XmlBase64Binary pgpKeyID);

    /**
     * Unsets the "PGPKeyID" element
     */
    void unsetPGPKeyID();

    /**
     * Gets the "PGPKeyPacket" element
     */
    byte[] getPGPKeyPacket();

    /**
     * Gets (as xml) the "PGPKeyPacket" element
     */
    org.apache.xmlbeans.XmlBase64Binary xgetPGPKeyPacket();

    /**
     * True if has "PGPKeyPacket" element
     */
    boolean isSetPGPKeyPacket();

    /**
     * Sets the "PGPKeyPacket" element
     */
    void setPGPKeyPacket(byte[] pgpKeyPacket);

    /**
     * Sets (as xml) the "PGPKeyPacket" element
     */
    void xsetPGPKeyPacket(org.apache.xmlbeans.XmlBase64Binary pgpKeyPacket);

    /**
     * Unsets the "PGPKeyPacket" element
     */
    void unsetPGPKeyPacket();
}
