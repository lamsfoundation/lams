/*
 * XML Type:  CT_CommentPr
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCommentPr
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_CommentPr(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTCommentPr extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCommentPr> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctcommentprd920type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "anchor" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTObjectAnchor getAnchor();

    /**
     * Sets the "anchor" element
     */
    void setAnchor(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTObjectAnchor anchor);

    /**
     * Appends and returns a new empty "anchor" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTObjectAnchor addNewAnchor();

    /**
     * Gets the "locked" attribute
     */
    boolean getLocked();

    /**
     * Gets (as xml) the "locked" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetLocked();

    /**
     * True if has "locked" attribute
     */
    boolean isSetLocked();

    /**
     * Sets the "locked" attribute
     */
    void setLocked(boolean locked);

    /**
     * Sets (as xml) the "locked" attribute
     */
    void xsetLocked(org.apache.xmlbeans.XmlBoolean locked);

    /**
     * Unsets the "locked" attribute
     */
    void unsetLocked();

    /**
     * Gets the "defaultSize" attribute
     */
    boolean getDefaultSize();

    /**
     * Gets (as xml) the "defaultSize" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetDefaultSize();

    /**
     * True if has "defaultSize" attribute
     */
    boolean isSetDefaultSize();

    /**
     * Sets the "defaultSize" attribute
     */
    void setDefaultSize(boolean defaultSize);

    /**
     * Sets (as xml) the "defaultSize" attribute
     */
    void xsetDefaultSize(org.apache.xmlbeans.XmlBoolean defaultSize);

    /**
     * Unsets the "defaultSize" attribute
     */
    void unsetDefaultSize();

    /**
     * Gets the "print" attribute
     */
    boolean getPrint();

    /**
     * Gets (as xml) the "print" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetPrint();

    /**
     * True if has "print" attribute
     */
    boolean isSetPrint();

    /**
     * Sets the "print" attribute
     */
    void setPrint(boolean print);

    /**
     * Sets (as xml) the "print" attribute
     */
    void xsetPrint(org.apache.xmlbeans.XmlBoolean print);

    /**
     * Unsets the "print" attribute
     */
    void unsetPrint();

    /**
     * Gets the "disabled" attribute
     */
    boolean getDisabled();

    /**
     * Gets (as xml) the "disabled" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetDisabled();

    /**
     * True if has "disabled" attribute
     */
    boolean isSetDisabled();

    /**
     * Sets the "disabled" attribute
     */
    void setDisabled(boolean disabled);

    /**
     * Sets (as xml) the "disabled" attribute
     */
    void xsetDisabled(org.apache.xmlbeans.XmlBoolean disabled);

    /**
     * Unsets the "disabled" attribute
     */
    void unsetDisabled();

    /**
     * Gets the "autoFill" attribute
     */
    boolean getAutoFill();

    /**
     * Gets (as xml) the "autoFill" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetAutoFill();

    /**
     * True if has "autoFill" attribute
     */
    boolean isSetAutoFill();

    /**
     * Sets the "autoFill" attribute
     */
    void setAutoFill(boolean autoFill);

    /**
     * Sets (as xml) the "autoFill" attribute
     */
    void xsetAutoFill(org.apache.xmlbeans.XmlBoolean autoFill);

    /**
     * Unsets the "autoFill" attribute
     */
    void unsetAutoFill();

    /**
     * Gets the "autoLine" attribute
     */
    boolean getAutoLine();

    /**
     * Gets (as xml) the "autoLine" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetAutoLine();

    /**
     * True if has "autoLine" attribute
     */
    boolean isSetAutoLine();

    /**
     * Sets the "autoLine" attribute
     */
    void setAutoLine(boolean autoLine);

    /**
     * Sets (as xml) the "autoLine" attribute
     */
    void xsetAutoLine(org.apache.xmlbeans.XmlBoolean autoLine);

    /**
     * Unsets the "autoLine" attribute
     */
    void unsetAutoLine();

    /**
     * Gets the "altText" attribute
     */
    java.lang.String getAltText();

    /**
     * Gets (as xml) the "altText" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetAltText();

    /**
     * True if has "altText" attribute
     */
    boolean isSetAltText();

    /**
     * Sets the "altText" attribute
     */
    void setAltText(java.lang.String altText);

    /**
     * Sets (as xml) the "altText" attribute
     */
    void xsetAltText(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring altText);

    /**
     * Unsets the "altText" attribute
     */
    void unsetAltText();

    /**
     * Gets the "textHAlign" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STTextHAlign.Enum getTextHAlign();

    /**
     * Gets (as xml) the "textHAlign" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STTextHAlign xgetTextHAlign();

    /**
     * True if has "textHAlign" attribute
     */
    boolean isSetTextHAlign();

    /**
     * Sets the "textHAlign" attribute
     */
    void setTextHAlign(org.openxmlformats.schemas.spreadsheetml.x2006.main.STTextHAlign.Enum textHAlign);

    /**
     * Sets (as xml) the "textHAlign" attribute
     */
    void xsetTextHAlign(org.openxmlformats.schemas.spreadsheetml.x2006.main.STTextHAlign textHAlign);

    /**
     * Unsets the "textHAlign" attribute
     */
    void unsetTextHAlign();

    /**
     * Gets the "textVAlign" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STTextVAlign.Enum getTextVAlign();

    /**
     * Gets (as xml) the "textVAlign" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STTextVAlign xgetTextVAlign();

    /**
     * True if has "textVAlign" attribute
     */
    boolean isSetTextVAlign();

    /**
     * Sets the "textVAlign" attribute
     */
    void setTextVAlign(org.openxmlformats.schemas.spreadsheetml.x2006.main.STTextVAlign.Enum textVAlign);

    /**
     * Sets (as xml) the "textVAlign" attribute
     */
    void xsetTextVAlign(org.openxmlformats.schemas.spreadsheetml.x2006.main.STTextVAlign textVAlign);

    /**
     * Unsets the "textVAlign" attribute
     */
    void unsetTextVAlign();

    /**
     * Gets the "lockText" attribute
     */
    boolean getLockText();

    /**
     * Gets (as xml) the "lockText" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetLockText();

    /**
     * True if has "lockText" attribute
     */
    boolean isSetLockText();

    /**
     * Sets the "lockText" attribute
     */
    void setLockText(boolean lockText);

    /**
     * Sets (as xml) the "lockText" attribute
     */
    void xsetLockText(org.apache.xmlbeans.XmlBoolean lockText);

    /**
     * Unsets the "lockText" attribute
     */
    void unsetLockText();

    /**
     * Gets the "justLastX" attribute
     */
    boolean getJustLastX();

    /**
     * Gets (as xml) the "justLastX" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetJustLastX();

    /**
     * True if has "justLastX" attribute
     */
    boolean isSetJustLastX();

    /**
     * Sets the "justLastX" attribute
     */
    void setJustLastX(boolean justLastX);

    /**
     * Sets (as xml) the "justLastX" attribute
     */
    void xsetJustLastX(org.apache.xmlbeans.XmlBoolean justLastX);

    /**
     * Unsets the "justLastX" attribute
     */
    void unsetJustLastX();

    /**
     * Gets the "autoScale" attribute
     */
    boolean getAutoScale();

    /**
     * Gets (as xml) the "autoScale" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetAutoScale();

    /**
     * True if has "autoScale" attribute
     */
    boolean isSetAutoScale();

    /**
     * Sets the "autoScale" attribute
     */
    void setAutoScale(boolean autoScale);

    /**
     * Sets (as xml) the "autoScale" attribute
     */
    void xsetAutoScale(org.apache.xmlbeans.XmlBoolean autoScale);

    /**
     * Unsets the "autoScale" attribute
     */
    void unsetAutoScale();
}
