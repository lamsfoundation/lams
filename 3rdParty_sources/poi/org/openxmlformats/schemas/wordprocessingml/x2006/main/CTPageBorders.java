/*
 * XML Type:  CT_PageBorders
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageBorders
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_PageBorders(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTPageBorders extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageBorders> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctpagebordersa4datype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "top" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTopPageBorder getTop();

    /**
     * True if has "top" element
     */
    boolean isSetTop();

    /**
     * Sets the "top" element
     */
    void setTop(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTopPageBorder top);

    /**
     * Appends and returns a new empty "top" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTopPageBorder addNewTop();

    /**
     * Unsets the "top" element
     */
    void unsetTop();

    /**
     * Gets the "left" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageBorder getLeft();

    /**
     * True if has "left" element
     */
    boolean isSetLeft();

    /**
     * Sets the "left" element
     */
    void setLeft(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageBorder left);

    /**
     * Appends and returns a new empty "left" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageBorder addNewLeft();

    /**
     * Unsets the "left" element
     */
    void unsetLeft();

    /**
     * Gets the "bottom" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBottomPageBorder getBottom();

    /**
     * True if has "bottom" element
     */
    boolean isSetBottom();

    /**
     * Sets the "bottom" element
     */
    void setBottom(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBottomPageBorder bottom);

    /**
     * Appends and returns a new empty "bottom" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBottomPageBorder addNewBottom();

    /**
     * Unsets the "bottom" element
     */
    void unsetBottom();

    /**
     * Gets the "right" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageBorder getRight();

    /**
     * True if has "right" element
     */
    boolean isSetRight();

    /**
     * Sets the "right" element
     */
    void setRight(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageBorder right);

    /**
     * Appends and returns a new empty "right" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageBorder addNewRight();

    /**
     * Unsets the "right" element
     */
    void unsetRight();

    /**
     * Gets the "zOrder" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageBorderZOrder.Enum getZOrder();

    /**
     * Gets (as xml) the "zOrder" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageBorderZOrder xgetZOrder();

    /**
     * True if has "zOrder" attribute
     */
    boolean isSetZOrder();

    /**
     * Sets the "zOrder" attribute
     */
    void setZOrder(org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageBorderZOrder.Enum zOrder);

    /**
     * Sets (as xml) the "zOrder" attribute
     */
    void xsetZOrder(org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageBorderZOrder zOrder);

    /**
     * Unsets the "zOrder" attribute
     */
    void unsetZOrder();

    /**
     * Gets the "display" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageBorderDisplay.Enum getDisplay();

    /**
     * Gets (as xml) the "display" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageBorderDisplay xgetDisplay();

    /**
     * True if has "display" attribute
     */
    boolean isSetDisplay();

    /**
     * Sets the "display" attribute
     */
    void setDisplay(org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageBorderDisplay.Enum display);

    /**
     * Sets (as xml) the "display" attribute
     */
    void xsetDisplay(org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageBorderDisplay display);

    /**
     * Unsets the "display" attribute
     */
    void unsetDisplay();

    /**
     * Gets the "offsetFrom" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageBorderOffset.Enum getOffsetFrom();

    /**
     * Gets (as xml) the "offsetFrom" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageBorderOffset xgetOffsetFrom();

    /**
     * True if has "offsetFrom" attribute
     */
    boolean isSetOffsetFrom();

    /**
     * Sets the "offsetFrom" attribute
     */
    void setOffsetFrom(org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageBorderOffset.Enum offsetFrom);

    /**
     * Sets (as xml) the "offsetFrom" attribute
     */
    void xsetOffsetFrom(org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageBorderOffset offsetFrom);

    /**
     * Unsets the "offsetFrom" attribute
     */
    void unsetOffsetFrom();
}
