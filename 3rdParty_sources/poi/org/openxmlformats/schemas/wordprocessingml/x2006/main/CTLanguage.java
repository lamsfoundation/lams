/*
 * XML Type:  CT_Language
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLanguage
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Language(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTLanguage extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLanguage> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctlanguage7b90type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "val" attribute
     */
    java.lang.String getVal();

    /**
     * Gets (as xml) the "val" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STLang xgetVal();

    /**
     * True if has "val" attribute
     */
    boolean isSetVal();

    /**
     * Sets the "val" attribute
     */
    void setVal(java.lang.String val);

    /**
     * Sets (as xml) the "val" attribute
     */
    void xsetVal(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STLang val);

    /**
     * Unsets the "val" attribute
     */
    void unsetVal();

    /**
     * Gets the "eastAsia" attribute
     */
    java.lang.String getEastAsia();

    /**
     * Gets (as xml) the "eastAsia" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STLang xgetEastAsia();

    /**
     * True if has "eastAsia" attribute
     */
    boolean isSetEastAsia();

    /**
     * Sets the "eastAsia" attribute
     */
    void setEastAsia(java.lang.String eastAsia);

    /**
     * Sets (as xml) the "eastAsia" attribute
     */
    void xsetEastAsia(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STLang eastAsia);

    /**
     * Unsets the "eastAsia" attribute
     */
    void unsetEastAsia();

    /**
     * Gets the "bidi" attribute
     */
    java.lang.String getBidi();

    /**
     * Gets (as xml) the "bidi" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STLang xgetBidi();

    /**
     * True if has "bidi" attribute
     */
    boolean isSetBidi();

    /**
     * Sets the "bidi" attribute
     */
    void setBidi(java.lang.String bidi);

    /**
     * Sets (as xml) the "bidi" attribute
     */
    void xsetBidi(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STLang bidi);

    /**
     * Unsets the "bidi" attribute
     */
    void unsetBidi();
}
