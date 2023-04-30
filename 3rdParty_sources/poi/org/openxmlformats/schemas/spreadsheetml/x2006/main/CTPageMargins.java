/*
 * XML Type:  CT_PageMargins
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageMargins
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_PageMargins(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTPageMargins extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageMargins> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctpagemargins5455type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "left" attribute
     */
    double getLeft();

    /**
     * Gets (as xml) the "left" attribute
     */
    org.apache.xmlbeans.XmlDouble xgetLeft();

    /**
     * Sets the "left" attribute
     */
    void setLeft(double left);

    /**
     * Sets (as xml) the "left" attribute
     */
    void xsetLeft(org.apache.xmlbeans.XmlDouble left);

    /**
     * Gets the "right" attribute
     */
    double getRight();

    /**
     * Gets (as xml) the "right" attribute
     */
    org.apache.xmlbeans.XmlDouble xgetRight();

    /**
     * Sets the "right" attribute
     */
    void setRight(double right);

    /**
     * Sets (as xml) the "right" attribute
     */
    void xsetRight(org.apache.xmlbeans.XmlDouble right);

    /**
     * Gets the "top" attribute
     */
    double getTop();

    /**
     * Gets (as xml) the "top" attribute
     */
    org.apache.xmlbeans.XmlDouble xgetTop();

    /**
     * Sets the "top" attribute
     */
    void setTop(double top);

    /**
     * Sets (as xml) the "top" attribute
     */
    void xsetTop(org.apache.xmlbeans.XmlDouble top);

    /**
     * Gets the "bottom" attribute
     */
    double getBottom();

    /**
     * Gets (as xml) the "bottom" attribute
     */
    org.apache.xmlbeans.XmlDouble xgetBottom();

    /**
     * Sets the "bottom" attribute
     */
    void setBottom(double bottom);

    /**
     * Sets (as xml) the "bottom" attribute
     */
    void xsetBottom(org.apache.xmlbeans.XmlDouble bottom);

    /**
     * Gets the "header" attribute
     */
    double getHeader();

    /**
     * Gets (as xml) the "header" attribute
     */
    org.apache.xmlbeans.XmlDouble xgetHeader();

    /**
     * Sets the "header" attribute
     */
    void setHeader(double header);

    /**
     * Sets (as xml) the "header" attribute
     */
    void xsetHeader(org.apache.xmlbeans.XmlDouble header);

    /**
     * Gets the "footer" attribute
     */
    double getFooter();

    /**
     * Gets (as xml) the "footer" attribute
     */
    org.apache.xmlbeans.XmlDouble xgetFooter();

    /**
     * Sets the "footer" attribute
     */
    void setFooter(double footer);

    /**
     * Sets (as xml) the "footer" attribute
     */
    void xsetFooter(org.apache.xmlbeans.XmlDouble footer);
}
