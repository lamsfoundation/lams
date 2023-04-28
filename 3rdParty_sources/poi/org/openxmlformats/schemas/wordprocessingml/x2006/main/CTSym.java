/*
 * XML Type:  CT_Sym
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSym
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Sym(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTSym extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSym> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctsym0dabtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "font" attribute
     */
    java.lang.String getFont();

    /**
     * Gets (as xml) the "font" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetFont();

    /**
     * True if has "font" attribute
     */
    boolean isSetFont();

    /**
     * Sets the "font" attribute
     */
    void setFont(java.lang.String font);

    /**
     * Sets (as xml) the "font" attribute
     */
    void xsetFont(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString font);

    /**
     * Unsets the "font" attribute
     */
    void unsetFont();

    /**
     * Gets the "char" attribute
     */
    byte[] getChar();

    /**
     * Gets (as xml) the "char" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STShortHexNumber xgetChar();

    /**
     * True if has "char" attribute
     */
    boolean isSetChar();

    /**
     * Sets the "char" attribute
     */
    void setChar(byte[] xchar);

    /**
     * Sets (as xml) the "char" attribute
     */
    void xsetChar(org.openxmlformats.schemas.wordprocessingml.x2006.main.STShortHexNumber xchar);

    /**
     * Unsets the "char" attribute
     */
    void unsetChar();
}
