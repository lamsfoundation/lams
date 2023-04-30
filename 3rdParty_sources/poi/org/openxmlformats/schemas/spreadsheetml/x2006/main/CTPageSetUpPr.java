/*
 * XML Type:  CT_PageSetUpPr
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetUpPr
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_PageSetUpPr(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTPageSetUpPr extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetUpPr> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctpagesetuppr24cftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "autoPageBreaks" attribute
     */
    boolean getAutoPageBreaks();

    /**
     * Gets (as xml) the "autoPageBreaks" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetAutoPageBreaks();

    /**
     * True if has "autoPageBreaks" attribute
     */
    boolean isSetAutoPageBreaks();

    /**
     * Sets the "autoPageBreaks" attribute
     */
    void setAutoPageBreaks(boolean autoPageBreaks);

    /**
     * Sets (as xml) the "autoPageBreaks" attribute
     */
    void xsetAutoPageBreaks(org.apache.xmlbeans.XmlBoolean autoPageBreaks);

    /**
     * Unsets the "autoPageBreaks" attribute
     */
    void unsetAutoPageBreaks();

    /**
     * Gets the "fitToPage" attribute
     */
    boolean getFitToPage();

    /**
     * Gets (as xml) the "fitToPage" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetFitToPage();

    /**
     * True if has "fitToPage" attribute
     */
    boolean isSetFitToPage();

    /**
     * Sets the "fitToPage" attribute
     */
    void setFitToPage(boolean fitToPage);

    /**
     * Sets (as xml) the "fitToPage" attribute
     */
    void xsetFitToPage(org.apache.xmlbeans.XmlBoolean fitToPage);

    /**
     * Unsets the "fitToPage" attribute
     */
    void unsetFitToPage();
}
