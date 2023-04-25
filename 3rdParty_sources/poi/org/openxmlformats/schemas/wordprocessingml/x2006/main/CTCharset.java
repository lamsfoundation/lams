/*
 * XML Type:  CT_Charset
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCharset
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Charset(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTCharset extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCharset> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctcharset8ec6type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "val" attribute
     */
    byte[] getVal();

    /**
     * Gets (as xml) the "val" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STUcharHexNumber xgetVal();

    /**
     * True if has "val" attribute
     */
    boolean isSetVal();

    /**
     * Sets the "val" attribute
     */
    void setVal(byte[] val);

    /**
     * Sets (as xml) the "val" attribute
     */
    void xsetVal(org.openxmlformats.schemas.wordprocessingml.x2006.main.STUcharHexNumber val);

    /**
     * Unsets the "val" attribute
     */
    void unsetVal();

    /**
     * Gets the "characterSet" attribute
     */
    java.lang.String getCharacterSet();

    /**
     * Gets (as xml) the "characterSet" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetCharacterSet();

    /**
     * True if has "characterSet" attribute
     */
    boolean isSetCharacterSet();

    /**
     * Sets the "characterSet" attribute
     */
    void setCharacterSet(java.lang.String characterSet);

    /**
     * Sets (as xml) the "characterSet" attribute
     */
    void xsetCharacterSet(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString characterSet);

    /**
     * Unsets the "characterSet" attribute
     */
    void unsetCharacterSet();
}
