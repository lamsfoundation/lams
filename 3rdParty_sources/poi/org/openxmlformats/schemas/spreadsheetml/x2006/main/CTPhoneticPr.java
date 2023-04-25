/*
 * XML Type:  CT_PhoneticPr
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPhoneticPr
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_PhoneticPr(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTPhoneticPr extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPhoneticPr> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctphoneticpr898btype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "fontId" attribute
     */
    long getFontId();

    /**
     * Gets (as xml) the "fontId" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STFontId xgetFontId();

    /**
     * Sets the "fontId" attribute
     */
    void setFontId(long fontId);

    /**
     * Sets (as xml) the "fontId" attribute
     */
    void xsetFontId(org.openxmlformats.schemas.spreadsheetml.x2006.main.STFontId fontId);

    /**
     * Gets the "type" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STPhoneticType.Enum getType();

    /**
     * Gets (as xml) the "type" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STPhoneticType xgetType();

    /**
     * True if has "type" attribute
     */
    boolean isSetType();

    /**
     * Sets the "type" attribute
     */
    void setType(org.openxmlformats.schemas.spreadsheetml.x2006.main.STPhoneticType.Enum type);

    /**
     * Sets (as xml) the "type" attribute
     */
    void xsetType(org.openxmlformats.schemas.spreadsheetml.x2006.main.STPhoneticType type);

    /**
     * Unsets the "type" attribute
     */
    void unsetType();

    /**
     * Gets the "alignment" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STPhoneticAlignment.Enum getAlignment();

    /**
     * Gets (as xml) the "alignment" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STPhoneticAlignment xgetAlignment();

    /**
     * True if has "alignment" attribute
     */
    boolean isSetAlignment();

    /**
     * Sets the "alignment" attribute
     */
    void setAlignment(org.openxmlformats.schemas.spreadsheetml.x2006.main.STPhoneticAlignment.Enum alignment);

    /**
     * Sets (as xml) the "alignment" attribute
     */
    void xsetAlignment(org.openxmlformats.schemas.spreadsheetml.x2006.main.STPhoneticAlignment alignment);

    /**
     * Unsets the "alignment" attribute
     */
    void unsetAlignment();
}
