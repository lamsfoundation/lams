/*
 * XML Type:  CT_Protection
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTProtection
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Protection(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public interface CTProtection extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.chart.CTProtection> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctprotectione383type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "chartObject" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean getChartObject();

    /**
     * True if has "chartObject" element
     */
    boolean isSetChartObject();

    /**
     * Sets the "chartObject" element
     */
    void setChartObject(org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean chartObject);

    /**
     * Appends and returns a new empty "chartObject" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean addNewChartObject();

    /**
     * Unsets the "chartObject" element
     */
    void unsetChartObject();

    /**
     * Gets the "data" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean getData();

    /**
     * True if has "data" element
     */
    boolean isSetData();

    /**
     * Sets the "data" element
     */
    void setData(org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean data);

    /**
     * Appends and returns a new empty "data" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean addNewData();

    /**
     * Unsets the "data" element
     */
    void unsetData();

    /**
     * Gets the "formatting" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean getFormatting();

    /**
     * True if has "formatting" element
     */
    boolean isSetFormatting();

    /**
     * Sets the "formatting" element
     */
    void setFormatting(org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean formatting);

    /**
     * Appends and returns a new empty "formatting" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean addNewFormatting();

    /**
     * Unsets the "formatting" element
     */
    void unsetFormatting();

    /**
     * Gets the "selection" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean getSelection();

    /**
     * True if has "selection" element
     */
    boolean isSetSelection();

    /**
     * Sets the "selection" element
     */
    void setSelection(org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean selection);

    /**
     * Appends and returns a new empty "selection" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean addNewSelection();

    /**
     * Unsets the "selection" element
     */
    void unsetSelection();

    /**
     * Gets the "userInterface" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean getUserInterface();

    /**
     * True if has "userInterface" element
     */
    boolean isSetUserInterface();

    /**
     * Sets the "userInterface" element
     */
    void setUserInterface(org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean userInterface);

    /**
     * Appends and returns a new empty "userInterface" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean addNewUserInterface();

    /**
     * Unsets the "userInterface" element
     */
    void unsetUserInterface();
}
