/*
 * XML Type:  CT_LineChart
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTLineChart
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_LineChart(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public interface CTLineChart extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.chart.CTLineChart> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctlinechart249ctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "grouping" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTGrouping getGrouping();

    /**
     * Sets the "grouping" element
     */
    void setGrouping(org.openxmlformats.schemas.drawingml.x2006.chart.CTGrouping grouping);

    /**
     * Appends and returns a new empty "grouping" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTGrouping addNewGrouping();

    /**
     * Gets the "varyColors" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean getVaryColors();

    /**
     * True if has "varyColors" element
     */
    boolean isSetVaryColors();

    /**
     * Sets the "varyColors" element
     */
    void setVaryColors(org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean varyColors);

    /**
     * Appends and returns a new empty "varyColors" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean addNewVaryColors();

    /**
     * Unsets the "varyColors" element
     */
    void unsetVaryColors();

    /**
     * Gets a List of "ser" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.chart.CTLineSer> getSerList();

    /**
     * Gets array of all "ser" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTLineSer[] getSerArray();

    /**
     * Gets ith "ser" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTLineSer getSerArray(int i);

    /**
     * Returns number of "ser" element
     */
    int sizeOfSerArray();

    /**
     * Sets array of all "ser" element
     */
    void setSerArray(org.openxmlformats.schemas.drawingml.x2006.chart.CTLineSer[] serArray);

    /**
     * Sets ith "ser" element
     */
    void setSerArray(int i, org.openxmlformats.schemas.drawingml.x2006.chart.CTLineSer ser);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "ser" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTLineSer insertNewSer(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "ser" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTLineSer addNewSer();

    /**
     * Removes the ith "ser" element
     */
    void removeSer(int i);

    /**
     * Gets the "dLbls" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTDLbls getDLbls();

    /**
     * True if has "dLbls" element
     */
    boolean isSetDLbls();

    /**
     * Sets the "dLbls" element
     */
    void setDLbls(org.openxmlformats.schemas.drawingml.x2006.chart.CTDLbls dLbls);

    /**
     * Appends and returns a new empty "dLbls" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTDLbls addNewDLbls();

    /**
     * Unsets the "dLbls" element
     */
    void unsetDLbls();

    /**
     * Gets the "dropLines" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTChartLines getDropLines();

    /**
     * True if has "dropLines" element
     */
    boolean isSetDropLines();

    /**
     * Sets the "dropLines" element
     */
    void setDropLines(org.openxmlformats.schemas.drawingml.x2006.chart.CTChartLines dropLines);

    /**
     * Appends and returns a new empty "dropLines" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTChartLines addNewDropLines();

    /**
     * Unsets the "dropLines" element
     */
    void unsetDropLines();

    /**
     * Gets the "hiLowLines" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTChartLines getHiLowLines();

    /**
     * True if has "hiLowLines" element
     */
    boolean isSetHiLowLines();

    /**
     * Sets the "hiLowLines" element
     */
    void setHiLowLines(org.openxmlformats.schemas.drawingml.x2006.chart.CTChartLines hiLowLines);

    /**
     * Appends and returns a new empty "hiLowLines" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTChartLines addNewHiLowLines();

    /**
     * Unsets the "hiLowLines" element
     */
    void unsetHiLowLines();

    /**
     * Gets the "upDownBars" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTUpDownBars getUpDownBars();

    /**
     * True if has "upDownBars" element
     */
    boolean isSetUpDownBars();

    /**
     * Sets the "upDownBars" element
     */
    void setUpDownBars(org.openxmlformats.schemas.drawingml.x2006.chart.CTUpDownBars upDownBars);

    /**
     * Appends and returns a new empty "upDownBars" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTUpDownBars addNewUpDownBars();

    /**
     * Unsets the "upDownBars" element
     */
    void unsetUpDownBars();

    /**
     * Gets the "marker" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean getMarker();

    /**
     * True if has "marker" element
     */
    boolean isSetMarker();

    /**
     * Sets the "marker" element
     */
    void setMarker(org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean marker);

    /**
     * Appends and returns a new empty "marker" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean addNewMarker();

    /**
     * Unsets the "marker" element
     */
    void unsetMarker();

    /**
     * Gets the "smooth" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean getSmooth();

    /**
     * True if has "smooth" element
     */
    boolean isSetSmooth();

    /**
     * Sets the "smooth" element
     */
    void setSmooth(org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean smooth);

    /**
     * Appends and returns a new empty "smooth" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean addNewSmooth();

    /**
     * Unsets the "smooth" element
     */
    void unsetSmooth();

    /**
     * Gets a List of "axId" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt> getAxIdList();

    /**
     * Gets array of all "axId" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt[] getAxIdArray();

    /**
     * Gets ith "axId" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt getAxIdArray(int i);

    /**
     * Returns number of "axId" element
     */
    int sizeOfAxIdArray();

    /**
     * Sets array of all "axId" element
     */
    void setAxIdArray(org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt[] axIdArray);

    /**
     * Sets ith "axId" element
     */
    void setAxIdArray(int i, org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt axId);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "axId" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt insertNewAxId(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "axId" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt addNewAxId();

    /**
     * Removes the ith "axId" element
     */
    void removeAxId(int i);

    /**
     * Gets the "extLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList getExtLst();

    /**
     * True if has "extLst" element
     */
    boolean isSetExtLst();

    /**
     * Sets the "extLst" element
     */
    void setExtLst(org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList extLst);

    /**
     * Appends and returns a new empty "extLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList addNewExtLst();

    /**
     * Unsets the "extLst" element
     */
    void unsetExtLst();
}
