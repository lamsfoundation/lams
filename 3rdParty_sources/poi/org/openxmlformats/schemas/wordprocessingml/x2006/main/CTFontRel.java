/*
 * XML Type:  CT_FontRel
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontRel
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_FontRel(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTFontRel extends org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontRel> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctfontrelc308type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "fontKey" attribute
     */
    java.lang.String getFontKey();

    /**
     * Gets (as xml) the "fontKey" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid xgetFontKey();

    /**
     * True if has "fontKey" attribute
     */
    boolean isSetFontKey();

    /**
     * Sets the "fontKey" attribute
     */
    void setFontKey(java.lang.String fontKey);

    /**
     * Sets (as xml) the "fontKey" attribute
     */
    void xsetFontKey(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid fontKey);

    /**
     * Unsets the "fontKey" attribute
     */
    void unsetFontKey();

    /**
     * Gets the "subsetted" attribute
     */
    java.lang.Object getSubsetted();

    /**
     * Gets (as xml) the "subsetted" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff xgetSubsetted();

    /**
     * True if has "subsetted" attribute
     */
    boolean isSetSubsetted();

    /**
     * Sets the "subsetted" attribute
     */
    void setSubsetted(java.lang.Object subsetted);

    /**
     * Sets (as xml) the "subsetted" attribute
     */
    void xsetSubsetted(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff subsetted);

    /**
     * Unsets the "subsetted" attribute
     */
    void unsetSubsetted();
}
