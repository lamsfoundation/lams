/*
 * XML Type:  CT_ChartsheetView
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTChartsheetView
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_ChartsheetView(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTChartsheetView extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTChartsheetView> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctchartsheetview720ftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "extLst" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList getExtLst();

    /**
     * True if has "extLst" element
     */
    boolean isSetExtLst();

    /**
     * Sets the "extLst" element
     */
    void setExtLst(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList extLst);

    /**
     * Appends and returns a new empty "extLst" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList addNewExtLst();

    /**
     * Unsets the "extLst" element
     */
    void unsetExtLst();

    /**
     * Gets the "tabSelected" attribute
     */
    boolean getTabSelected();

    /**
     * Gets (as xml) the "tabSelected" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetTabSelected();

    /**
     * True if has "tabSelected" attribute
     */
    boolean isSetTabSelected();

    /**
     * Sets the "tabSelected" attribute
     */
    void setTabSelected(boolean tabSelected);

    /**
     * Sets (as xml) the "tabSelected" attribute
     */
    void xsetTabSelected(org.apache.xmlbeans.XmlBoolean tabSelected);

    /**
     * Unsets the "tabSelected" attribute
     */
    void unsetTabSelected();

    /**
     * Gets the "zoomScale" attribute
     */
    long getZoomScale();

    /**
     * Gets (as xml) the "zoomScale" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetZoomScale();

    /**
     * True if has "zoomScale" attribute
     */
    boolean isSetZoomScale();

    /**
     * Sets the "zoomScale" attribute
     */
    void setZoomScale(long zoomScale);

    /**
     * Sets (as xml) the "zoomScale" attribute
     */
    void xsetZoomScale(org.apache.xmlbeans.XmlUnsignedInt zoomScale);

    /**
     * Unsets the "zoomScale" attribute
     */
    void unsetZoomScale();

    /**
     * Gets the "workbookViewId" attribute
     */
    long getWorkbookViewId();

    /**
     * Gets (as xml) the "workbookViewId" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetWorkbookViewId();

    /**
     * Sets the "workbookViewId" attribute
     */
    void setWorkbookViewId(long workbookViewId);

    /**
     * Sets (as xml) the "workbookViewId" attribute
     */
    void xsetWorkbookViewId(org.apache.xmlbeans.XmlUnsignedInt workbookViewId);

    /**
     * Gets the "zoomToFit" attribute
     */
    boolean getZoomToFit();

    /**
     * Gets (as xml) the "zoomToFit" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetZoomToFit();

    /**
     * True if has "zoomToFit" attribute
     */
    boolean isSetZoomToFit();

    /**
     * Sets the "zoomToFit" attribute
     */
    void setZoomToFit(boolean zoomToFit);

    /**
     * Sets (as xml) the "zoomToFit" attribute
     */
    void xsetZoomToFit(org.apache.xmlbeans.XmlBoolean zoomToFit);

    /**
     * Unsets the "zoomToFit" attribute
     */
    void unsetZoomToFit();
}
