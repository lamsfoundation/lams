/*
 * XML Type:  CT_PageSetup
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_PageSetup(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public interface CTPageSetup extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctpagesetupdb38type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "paperSize" attribute
     */
    long getPaperSize();

    /**
     * Gets (as xml) the "paperSize" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetPaperSize();

    /**
     * True if has "paperSize" attribute
     */
    boolean isSetPaperSize();

    /**
     * Sets the "paperSize" attribute
     */
    void setPaperSize(long paperSize);

    /**
     * Sets (as xml) the "paperSize" attribute
     */
    void xsetPaperSize(org.apache.xmlbeans.XmlUnsignedInt paperSize);

    /**
     * Unsets the "paperSize" attribute
     */
    void unsetPaperSize();

    /**
     * Gets the "paperHeight" attribute
     */
    java.lang.String getPaperHeight();

    /**
     * Gets (as xml) the "paperHeight" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STPositiveUniversalMeasure xgetPaperHeight();

    /**
     * True if has "paperHeight" attribute
     */
    boolean isSetPaperHeight();

    /**
     * Sets the "paperHeight" attribute
     */
    void setPaperHeight(java.lang.String paperHeight);

    /**
     * Sets (as xml) the "paperHeight" attribute
     */
    void xsetPaperHeight(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STPositiveUniversalMeasure paperHeight);

    /**
     * Unsets the "paperHeight" attribute
     */
    void unsetPaperHeight();

    /**
     * Gets the "paperWidth" attribute
     */
    java.lang.String getPaperWidth();

    /**
     * Gets (as xml) the "paperWidth" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STPositiveUniversalMeasure xgetPaperWidth();

    /**
     * True if has "paperWidth" attribute
     */
    boolean isSetPaperWidth();

    /**
     * Sets the "paperWidth" attribute
     */
    void setPaperWidth(java.lang.String paperWidth);

    /**
     * Sets (as xml) the "paperWidth" attribute
     */
    void xsetPaperWidth(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STPositiveUniversalMeasure paperWidth);

    /**
     * Unsets the "paperWidth" attribute
     */
    void unsetPaperWidth();

    /**
     * Gets the "firstPageNumber" attribute
     */
    long getFirstPageNumber();

    /**
     * Gets (as xml) the "firstPageNumber" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetFirstPageNumber();

    /**
     * True if has "firstPageNumber" attribute
     */
    boolean isSetFirstPageNumber();

    /**
     * Sets the "firstPageNumber" attribute
     */
    void setFirstPageNumber(long firstPageNumber);

    /**
     * Sets (as xml) the "firstPageNumber" attribute
     */
    void xsetFirstPageNumber(org.apache.xmlbeans.XmlUnsignedInt firstPageNumber);

    /**
     * Unsets the "firstPageNumber" attribute
     */
    void unsetFirstPageNumber();

    /**
     * Gets the "orientation" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.STPageSetupOrientation.Enum getOrientation();

    /**
     * Gets (as xml) the "orientation" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.STPageSetupOrientation xgetOrientation();

    /**
     * True if has "orientation" attribute
     */
    boolean isSetOrientation();

    /**
     * Sets the "orientation" attribute
     */
    void setOrientation(org.openxmlformats.schemas.drawingml.x2006.chart.STPageSetupOrientation.Enum orientation);

    /**
     * Sets (as xml) the "orientation" attribute
     */
    void xsetOrientation(org.openxmlformats.schemas.drawingml.x2006.chart.STPageSetupOrientation orientation);

    /**
     * Unsets the "orientation" attribute
     */
    void unsetOrientation();

    /**
     * Gets the "blackAndWhite" attribute
     */
    boolean getBlackAndWhite();

    /**
     * Gets (as xml) the "blackAndWhite" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetBlackAndWhite();

    /**
     * True if has "blackAndWhite" attribute
     */
    boolean isSetBlackAndWhite();

    /**
     * Sets the "blackAndWhite" attribute
     */
    void setBlackAndWhite(boolean blackAndWhite);

    /**
     * Sets (as xml) the "blackAndWhite" attribute
     */
    void xsetBlackAndWhite(org.apache.xmlbeans.XmlBoolean blackAndWhite);

    /**
     * Unsets the "blackAndWhite" attribute
     */
    void unsetBlackAndWhite();

    /**
     * Gets the "draft" attribute
     */
    boolean getDraft();

    /**
     * Gets (as xml) the "draft" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetDraft();

    /**
     * True if has "draft" attribute
     */
    boolean isSetDraft();

    /**
     * Sets the "draft" attribute
     */
    void setDraft(boolean draft);

    /**
     * Sets (as xml) the "draft" attribute
     */
    void xsetDraft(org.apache.xmlbeans.XmlBoolean draft);

    /**
     * Unsets the "draft" attribute
     */
    void unsetDraft();

    /**
     * Gets the "useFirstPageNumber" attribute
     */
    boolean getUseFirstPageNumber();

    /**
     * Gets (as xml) the "useFirstPageNumber" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetUseFirstPageNumber();

    /**
     * True if has "useFirstPageNumber" attribute
     */
    boolean isSetUseFirstPageNumber();

    /**
     * Sets the "useFirstPageNumber" attribute
     */
    void setUseFirstPageNumber(boolean useFirstPageNumber);

    /**
     * Sets (as xml) the "useFirstPageNumber" attribute
     */
    void xsetUseFirstPageNumber(org.apache.xmlbeans.XmlBoolean useFirstPageNumber);

    /**
     * Unsets the "useFirstPageNumber" attribute
     */
    void unsetUseFirstPageNumber();

    /**
     * Gets the "horizontalDpi" attribute
     */
    int getHorizontalDpi();

    /**
     * Gets (as xml) the "horizontalDpi" attribute
     */
    org.apache.xmlbeans.XmlInt xgetHorizontalDpi();

    /**
     * True if has "horizontalDpi" attribute
     */
    boolean isSetHorizontalDpi();

    /**
     * Sets the "horizontalDpi" attribute
     */
    void setHorizontalDpi(int horizontalDpi);

    /**
     * Sets (as xml) the "horizontalDpi" attribute
     */
    void xsetHorizontalDpi(org.apache.xmlbeans.XmlInt horizontalDpi);

    /**
     * Unsets the "horizontalDpi" attribute
     */
    void unsetHorizontalDpi();

    /**
     * Gets the "verticalDpi" attribute
     */
    int getVerticalDpi();

    /**
     * Gets (as xml) the "verticalDpi" attribute
     */
    org.apache.xmlbeans.XmlInt xgetVerticalDpi();

    /**
     * True if has "verticalDpi" attribute
     */
    boolean isSetVerticalDpi();

    /**
     * Sets the "verticalDpi" attribute
     */
    void setVerticalDpi(int verticalDpi);

    /**
     * Sets (as xml) the "verticalDpi" attribute
     */
    void xsetVerticalDpi(org.apache.xmlbeans.XmlInt verticalDpi);

    /**
     * Unsets the "verticalDpi" attribute
     */
    void unsetVerticalDpi();

    /**
     * Gets the "copies" attribute
     */
    long getCopies();

    /**
     * Gets (as xml) the "copies" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetCopies();

    /**
     * True if has "copies" attribute
     */
    boolean isSetCopies();

    /**
     * Sets the "copies" attribute
     */
    void setCopies(long copies);

    /**
     * Sets (as xml) the "copies" attribute
     */
    void xsetCopies(org.apache.xmlbeans.XmlUnsignedInt copies);

    /**
     * Unsets the "copies" attribute
     */
    void unsetCopies();
}
