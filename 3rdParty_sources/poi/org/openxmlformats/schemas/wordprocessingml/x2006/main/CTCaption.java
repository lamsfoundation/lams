/*
 * XML Type:  CT_Caption
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCaption
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Caption(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTCaption extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCaption> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctcaption7c6ctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "name" attribute
     */
    java.lang.String getName();

    /**
     * Gets (as xml) the "name" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetName();

    /**
     * Sets the "name" attribute
     */
    void setName(java.lang.String name);

    /**
     * Sets (as xml) the "name" attribute
     */
    void xsetName(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString name);

    /**
     * Gets the "pos" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STCaptionPos.Enum getPos();

    /**
     * Gets (as xml) the "pos" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STCaptionPos xgetPos();

    /**
     * True if has "pos" attribute
     */
    boolean isSetPos();

    /**
     * Sets the "pos" attribute
     */
    void setPos(org.openxmlformats.schemas.wordprocessingml.x2006.main.STCaptionPos.Enum pos);

    /**
     * Sets (as xml) the "pos" attribute
     */
    void xsetPos(org.openxmlformats.schemas.wordprocessingml.x2006.main.STCaptionPos pos);

    /**
     * Unsets the "pos" attribute
     */
    void unsetPos();

    /**
     * Gets the "chapNum" attribute
     */
    java.lang.Object getChapNum();

    /**
     * Gets (as xml) the "chapNum" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff xgetChapNum();

    /**
     * True if has "chapNum" attribute
     */
    boolean isSetChapNum();

    /**
     * Sets the "chapNum" attribute
     */
    void setChapNum(java.lang.Object chapNum);

    /**
     * Sets (as xml) the "chapNum" attribute
     */
    void xsetChapNum(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff chapNum);

    /**
     * Unsets the "chapNum" attribute
     */
    void unsetChapNum();

    /**
     * Gets the "heading" attribute
     */
    java.math.BigInteger getHeading();

    /**
     * Gets (as xml) the "heading" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber xgetHeading();

    /**
     * True if has "heading" attribute
     */
    boolean isSetHeading();

    /**
     * Sets the "heading" attribute
     */
    void setHeading(java.math.BigInteger heading);

    /**
     * Sets (as xml) the "heading" attribute
     */
    void xsetHeading(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber heading);

    /**
     * Unsets the "heading" attribute
     */
    void unsetHeading();

    /**
     * Gets the "noLabel" attribute
     */
    java.lang.Object getNoLabel();

    /**
     * Gets (as xml) the "noLabel" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff xgetNoLabel();

    /**
     * True if has "noLabel" attribute
     */
    boolean isSetNoLabel();

    /**
     * Sets the "noLabel" attribute
     */
    void setNoLabel(java.lang.Object noLabel);

    /**
     * Sets (as xml) the "noLabel" attribute
     */
    void xsetNoLabel(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff noLabel);

    /**
     * Unsets the "noLabel" attribute
     */
    void unsetNoLabel();

    /**
     * Gets the "numFmt" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STNumberFormat.Enum getNumFmt();

    /**
     * Gets (as xml) the "numFmt" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STNumberFormat xgetNumFmt();

    /**
     * True if has "numFmt" attribute
     */
    boolean isSetNumFmt();

    /**
     * Sets the "numFmt" attribute
     */
    void setNumFmt(org.openxmlformats.schemas.wordprocessingml.x2006.main.STNumberFormat.Enum numFmt);

    /**
     * Sets (as xml) the "numFmt" attribute
     */
    void xsetNumFmt(org.openxmlformats.schemas.wordprocessingml.x2006.main.STNumberFormat numFmt);

    /**
     * Unsets the "numFmt" attribute
     */
    void unsetNumFmt();

    /**
     * Gets the "sep" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STChapterSep.Enum getSep();

    /**
     * Gets (as xml) the "sep" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STChapterSep xgetSep();

    /**
     * True if has "sep" attribute
     */
    boolean isSetSep();

    /**
     * Sets the "sep" attribute
     */
    void setSep(org.openxmlformats.schemas.wordprocessingml.x2006.main.STChapterSep.Enum sep);

    /**
     * Sets (as xml) the "sep" attribute
     */
    void xsetSep(org.openxmlformats.schemas.wordprocessingml.x2006.main.STChapterSep sep);

    /**
     * Unsets the "sep" attribute
     */
    void unsetSep();
}
