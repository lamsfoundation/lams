/*
 * XML Type:  CT_PrintSettings
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTPrintSettings
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_PrintSettings(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public interface CTPrintSettings extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.chart.CTPrintSettings> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctprintsettings61b6type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "headerFooter" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTHeaderFooter getHeaderFooter();

    /**
     * True if has "headerFooter" element
     */
    boolean isSetHeaderFooter();

    /**
     * Sets the "headerFooter" element
     */
    void setHeaderFooter(org.openxmlformats.schemas.drawingml.x2006.chart.CTHeaderFooter headerFooter);

    /**
     * Appends and returns a new empty "headerFooter" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTHeaderFooter addNewHeaderFooter();

    /**
     * Unsets the "headerFooter" element
     */
    void unsetHeaderFooter();

    /**
     * Gets the "pageMargins" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTPageMargins getPageMargins();

    /**
     * True if has "pageMargins" element
     */
    boolean isSetPageMargins();

    /**
     * Sets the "pageMargins" element
     */
    void setPageMargins(org.openxmlformats.schemas.drawingml.x2006.chart.CTPageMargins pageMargins);

    /**
     * Appends and returns a new empty "pageMargins" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTPageMargins addNewPageMargins();

    /**
     * Unsets the "pageMargins" element
     */
    void unsetPageMargins();

    /**
     * Gets the "pageSetup" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup getPageSetup();

    /**
     * True if has "pageSetup" element
     */
    boolean isSetPageSetup();

    /**
     * Sets the "pageSetup" element
     */
    void setPageSetup(org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup pageSetup);

    /**
     * Appends and returns a new empty "pageSetup" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup addNewPageSetup();

    /**
     * Unsets the "pageSetup" element
     */
    void unsetPageSetup();

    /**
     * Gets the "legacyDrawingHF" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTRelId getLegacyDrawingHF();

    /**
     * True if has "legacyDrawingHF" element
     */
    boolean isSetLegacyDrawingHF();

    /**
     * Sets the "legacyDrawingHF" element
     */
    void setLegacyDrawingHF(org.openxmlformats.schemas.drawingml.x2006.chart.CTRelId legacyDrawingHF);

    /**
     * Appends and returns a new empty "legacyDrawingHF" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTRelId addNewLegacyDrawingHF();

    /**
     * Unsets the "legacyDrawingHF" element
     */
    void unsetLegacyDrawingHF();
}
