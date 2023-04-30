/*
 * XML Type:  CT_Border
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Border(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTBorder extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctbordercdfctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "val" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder.Enum getVal();

    /**
     * Gets (as xml) the "val" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder xgetVal();

    /**
     * Sets the "val" attribute
     */
    void setVal(org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder.Enum val);

    /**
     * Sets (as xml) the "val" attribute
     */
    void xsetVal(org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder val);

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

    /**
     * Gets the "sz" attribute
     */
    java.math.BigInteger getSz();

    /**
     * Gets (as xml) the "sz" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STEighthPointMeasure xgetSz();

    /**
     * True if has "sz" attribute
     */
    boolean isSetSz();

    /**
     * Sets the "sz" attribute
     */
    void setSz(java.math.BigInteger sz);

    /**
     * Sets (as xml) the "sz" attribute
     */
    void xsetSz(org.openxmlformats.schemas.wordprocessingml.x2006.main.STEighthPointMeasure sz);

    /**
     * Unsets the "sz" attribute
     */
    void unsetSz();

    /**
     * Gets the "space" attribute
     */
    java.math.BigInteger getSpace();

    /**
     * Gets (as xml) the "space" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STPointMeasure xgetSpace();

    /**
     * True if has "space" attribute
     */
    boolean isSetSpace();

    /**
     * Sets the "space" attribute
     */
    void setSpace(java.math.BigInteger space);

    /**
     * Sets (as xml) the "space" attribute
     */
    void xsetSpace(org.openxmlformats.schemas.wordprocessingml.x2006.main.STPointMeasure space);

    /**
     * Unsets the "space" attribute
     */
    void unsetSpace();

    /**
     * Gets the "shadow" attribute
     */
    java.lang.Object getShadow();

    /**
     * Gets (as xml) the "shadow" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff xgetShadow();

    /**
     * True if has "shadow" attribute
     */
    boolean isSetShadow();

    /**
     * Sets the "shadow" attribute
     */
    void setShadow(java.lang.Object shadow);

    /**
     * Sets (as xml) the "shadow" attribute
     */
    void xsetShadow(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff shadow);

    /**
     * Unsets the "shadow" attribute
     */
    void unsetShadow();

    /**
     * Gets the "frame" attribute
     */
    java.lang.Object getFrame();

    /**
     * Gets (as xml) the "frame" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff xgetFrame();

    /**
     * True if has "frame" attribute
     */
    boolean isSetFrame();

    /**
     * Sets the "frame" attribute
     */
    void setFrame(java.lang.Object frame);

    /**
     * Sets (as xml) the "frame" attribute
     */
    void xsetFrame(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff frame);

    /**
     * Unsets the "frame" attribute
     */
    void unsetFrame();
}
