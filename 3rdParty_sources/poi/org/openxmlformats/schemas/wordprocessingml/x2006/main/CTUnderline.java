/*
 * XML Type:  CT_Underline
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTUnderline
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Underline(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTUnderline extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTUnderline> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctunderline8406type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "val" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STUnderline.Enum getVal();

    /**
     * Gets (as xml) the "val" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STUnderline xgetVal();

    /**
     * True if has "val" attribute
     */
    boolean isSetVal();

    /**
     * Sets the "val" attribute
     */
    void setVal(org.openxmlformats.schemas.wordprocessingml.x2006.main.STUnderline.Enum val);

    /**
     * Sets (as xml) the "val" attribute
     */
    void xsetVal(org.openxmlformats.schemas.wordprocessingml.x2006.main.STUnderline val);

    /**
     * Unsets the "val" attribute
     */
    void unsetVal();

    /**
     * Gets the "color" attribute
     */
    java.lang.Object getColor();

    /**
     * Gets (as xml) the "color" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STHexColor xgetColor();

    /**
     * True if has "color" attribute
     */
    boolean isSetColor();

    /**
     * Sets the "color" attribute
     */
    void setColor(java.lang.Object color);

    /**
     * Sets (as xml) the "color" attribute
     */
    void xsetColor(org.openxmlformats.schemas.wordprocessingml.x2006.main.STHexColor color);

    /**
     * Unsets the "color" attribute
     */
    void unsetColor();

    /**
     * Gets the "themeColor" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STThemeColor.Enum getThemeColor();

    /**
     * Gets (as xml) the "themeColor" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STThemeColor xgetThemeColor();

    /**
     * True if has "themeColor" attribute
     */
    boolean isSetThemeColor();

    /**
     * Sets the "themeColor" attribute
     */
    void setThemeColor(org.openxmlformats.schemas.wordprocessingml.x2006.main.STThemeColor.Enum themeColor);

    /**
     * Sets (as xml) the "themeColor" attribute
     */
    void xsetThemeColor(org.openxmlformats.schemas.wordprocessingml.x2006.main.STThemeColor themeColor);

    /**
     * Unsets the "themeColor" attribute
     */
    void unsetThemeColor();

    /**
     * Gets the "themeTint" attribute
     */
    byte[] getThemeTint();

    /**
     * Gets (as xml) the "themeTint" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STUcharHexNumber xgetThemeTint();

    /**
     * True if has "themeTint" attribute
     */
    boolean isSetThemeTint();

    /**
     * Sets the "themeTint" attribute
     */
    void setThemeTint(byte[] themeTint);

    /**
     * Sets (as xml) the "themeTint" attribute
     */
    void xsetThemeTint(org.openxmlformats.schemas.wordprocessingml.x2006.main.STUcharHexNumber themeTint);

    /**
     * Unsets the "themeTint" attribute
     */
    void unsetThemeTint();

    /**
     * Gets the "themeShade" attribute
     */
    byte[] getThemeShade();

    /**
     * Gets (as xml) the "themeShade" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STUcharHexNumber xgetThemeShade();

    /**
     * True if has "themeShade" attribute
     */
    boolean isSetThemeShade();

    /**
     * Sets the "themeShade" attribute
     */
    void setThemeShade(byte[] themeShade);

    /**
     * Sets (as xml) the "themeShade" attribute
     */
    void xsetThemeShade(org.openxmlformats.schemas.wordprocessingml.x2006.main.STUcharHexNumber themeShade);

    /**
     * Unsets the "themeShade" attribute
     */
    void unsetThemeShade();
}
