/*
 * XML Type:  CT_DigSigBlob
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/extended-properties
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.CTDigSigBlob
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.extendedProperties;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_DigSigBlob(@http://schemas.openxmlformats.org/officeDocument/2006/extended-properties).
 *
 * This is a complex type.
 */
public interface CTDigSigBlob extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.CTDigSigBlob> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctdigsigblob73c9type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "blob" element
     */
    byte[] getBlob();

    /**
     * Gets (as xml) the "blob" element
     */
    org.apache.xmlbeans.XmlBase64Binary xgetBlob();

    /**
     * Sets the "blob" element
     */
    void setBlob(byte[] blob);

    /**
     * Sets (as xml) the "blob" element
     */
    void xsetBlob(org.apache.xmlbeans.XmlBase64Binary blob);
}
