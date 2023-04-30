/*
 * XML Type:  CT_Base64Binary
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBase64Binary
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Base64Binary(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTBase64Binary extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBase64Binary> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctbase64binaryea38type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "val" attribute
     */
    byte[] getVal();

    /**
     * Gets (as xml) the "val" attribute
     */
    org.apache.xmlbeans.XmlBase64Binary xgetVal();

    /**
     * Sets the "val" attribute
     */
    void setVal(byte[] val);

    /**
     * Sets (as xml) the "val" attribute
     */
    void xsetVal(org.apache.xmlbeans.XmlBase64Binary val);
}
