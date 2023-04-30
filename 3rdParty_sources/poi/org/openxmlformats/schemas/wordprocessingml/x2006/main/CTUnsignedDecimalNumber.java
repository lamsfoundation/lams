/*
 * XML Type:  CT_UnsignedDecimalNumber
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTUnsignedDecimalNumber
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_UnsignedDecimalNumber(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTUnsignedDecimalNumber extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTUnsignedDecimalNumber> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctunsigneddecimalnumber9cedtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "val" attribute
     */
    java.math.BigInteger getVal();

    /**
     * Gets (as xml) the "val" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STUnsignedDecimalNumber xgetVal();

    /**
     * Sets the "val" attribute
     */
    void setVal(java.math.BigInteger val);

    /**
     * Sets (as xml) the "val" attribute
     */
    void xsetVal(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STUnsignedDecimalNumber val);
}
