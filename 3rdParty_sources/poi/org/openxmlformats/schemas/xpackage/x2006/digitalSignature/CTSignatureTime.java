/*
 * XML Type:  CT_SignatureTime
 * Namespace: http://schemas.openxmlformats.org/package/2006/digital-signature
 * Java type: org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTSignatureTime
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.xpackage.x2006.digitalSignature;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_SignatureTime(@http://schemas.openxmlformats.org/package/2006/digital-signature).
 *
 * This is a complex type.
 */
public interface CTSignatureTime extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTSignatureTime> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctsignaturetime461dtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "Format" element
     */
    java.lang.String getFormat();

    /**
     * Gets (as xml) the "Format" element
     */
    org.openxmlformats.schemas.xpackage.x2006.digitalSignature.STFormat xgetFormat();

    /**
     * Sets the "Format" element
     */
    void setFormat(java.lang.String format);

    /**
     * Sets (as xml) the "Format" element
     */
    void xsetFormat(org.openxmlformats.schemas.xpackage.x2006.digitalSignature.STFormat format);

    /**
     * Gets the "Value" element
     */
    java.lang.String getValue();

    /**
     * Gets (as xml) the "Value" element
     */
    org.openxmlformats.schemas.xpackage.x2006.digitalSignature.STValue xgetValue();

    /**
     * Sets the "Value" element
     */
    void setValue(java.lang.String value);

    /**
     * Sets (as xml) the "Value" element
     */
    void xsetValue(org.openxmlformats.schemas.xpackage.x2006.digitalSignature.STValue value);
}
