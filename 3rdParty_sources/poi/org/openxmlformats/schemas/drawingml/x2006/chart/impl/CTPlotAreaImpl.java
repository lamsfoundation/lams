/*
 * XML Type:  CT_PlotArea
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTPlotArea
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_PlotArea(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public class CTPlotAreaImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.chart.CTPlotArea {
    private static final long serialVersionUID = 1L;

    public CTPlotAreaImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "layout"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "areaChart"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "area3DChart"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "lineChart"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "line3DChart"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "stockChart"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "radarChart"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "scatterChart"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "pieChart"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "pie3DChart"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "doughnutChart"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "barChart"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "bar3DChart"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "ofPieChart"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "surfaceChart"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "surface3DChart"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "bubbleChart"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "valAx"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "catAx"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "dateAx"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "serAx"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "dTable"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "spPr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "extLst"),
    };


    /**
     * Gets the "layout" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTLayout getLayout() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTLayout target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTLayout)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "layout" element
     */
    @Override
    public boolean isSetLayout() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "layout" element
     */
    @Override
    public void setLayout(org.openxmlformats.schemas.drawingml.x2006.chart.CTLayout layout) {
        generatedSetterHelperImpl(layout, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "layout" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTLayout addNewLayout() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTLayout target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTLayout)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "layout" element
     */
    @Override
    public void unsetLayout() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets a List of "areaChart" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.chart.CTAreaChart> getAreaChartList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getAreaChartArray,
                this::setAreaChartArray,
                this::insertNewAreaChart,
                this::removeAreaChart,
                this::sizeOfAreaChartArray
            );
        }
    }

    /**
     * Gets array of all "areaChart" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTAreaChart[] getAreaChartArray() {
        return getXmlObjectArray(PROPERTY_QNAME[1], new org.openxmlformats.schemas.drawingml.x2006.chart.CTAreaChart[0]);
    }

    /**
     * Gets ith "areaChart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTAreaChart getAreaChartArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTAreaChart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTAreaChart)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "areaChart" element
     */
    @Override
    public int sizeOfAreaChartArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets array of all "areaChart" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setAreaChartArray(org.openxmlformats.schemas.drawingml.x2006.chart.CTAreaChart[] areaChartArray) {
        check_orphaned();
        arraySetterHelper(areaChartArray, PROPERTY_QNAME[1]);
    }

    /**
     * Sets ith "areaChart" element
     */
    @Override
    public void setAreaChartArray(int i, org.openxmlformats.schemas.drawingml.x2006.chart.CTAreaChart areaChart) {
        generatedSetterHelperImpl(areaChart, PROPERTY_QNAME[1], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "areaChart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTAreaChart insertNewAreaChart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTAreaChart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTAreaChart)get_store().insert_element_user(PROPERTY_QNAME[1], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "areaChart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTAreaChart addNewAreaChart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTAreaChart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTAreaChart)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Removes the ith "areaChart" element
     */
    @Override
    public void removeAreaChart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], i);
        }
    }

    /**
     * Gets a List of "area3DChart" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.chart.CTArea3DChart> getArea3DChartList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getArea3DChartArray,
                this::setArea3DChartArray,
                this::insertNewArea3DChart,
                this::removeArea3DChart,
                this::sizeOfArea3DChartArray
            );
        }
    }

    /**
     * Gets array of all "area3DChart" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTArea3DChart[] getArea3DChartArray() {
        return getXmlObjectArray(PROPERTY_QNAME[2], new org.openxmlformats.schemas.drawingml.x2006.chart.CTArea3DChart[0]);
    }

    /**
     * Gets ith "area3DChart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTArea3DChart getArea3DChartArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTArea3DChart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTArea3DChart)get_store().find_element_user(PROPERTY_QNAME[2], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "area3DChart" element
     */
    @Override
    public int sizeOfArea3DChartArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Sets array of all "area3DChart" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setArea3DChartArray(org.openxmlformats.schemas.drawingml.x2006.chart.CTArea3DChart[] area3DChartArray) {
        check_orphaned();
        arraySetterHelper(area3DChartArray, PROPERTY_QNAME[2]);
    }

    /**
     * Sets ith "area3DChart" element
     */
    @Override
    public void setArea3DChartArray(int i, org.openxmlformats.schemas.drawingml.x2006.chart.CTArea3DChart area3DChart) {
        generatedSetterHelperImpl(area3DChart, PROPERTY_QNAME[2], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "area3DChart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTArea3DChart insertNewArea3DChart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTArea3DChart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTArea3DChart)get_store().insert_element_user(PROPERTY_QNAME[2], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "area3DChart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTArea3DChart addNewArea3DChart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTArea3DChart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTArea3DChart)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Removes the ith "area3DChart" element
     */
    @Override
    public void removeArea3DChart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], i);
        }
    }

    /**
     * Gets a List of "lineChart" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.chart.CTLineChart> getLineChartList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getLineChartArray,
                this::setLineChartArray,
                this::insertNewLineChart,
                this::removeLineChart,
                this::sizeOfLineChartArray
            );
        }
    }

    /**
     * Gets array of all "lineChart" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTLineChart[] getLineChartArray() {
        return getXmlObjectArray(PROPERTY_QNAME[3], new org.openxmlformats.schemas.drawingml.x2006.chart.CTLineChart[0]);
    }

    /**
     * Gets ith "lineChart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTLineChart getLineChartArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTLineChart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTLineChart)get_store().find_element_user(PROPERTY_QNAME[3], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "lineChart" element
     */
    @Override
    public int sizeOfLineChartArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Sets array of all "lineChart" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setLineChartArray(org.openxmlformats.schemas.drawingml.x2006.chart.CTLineChart[] lineChartArray) {
        check_orphaned();
        arraySetterHelper(lineChartArray, PROPERTY_QNAME[3]);
    }

    /**
     * Sets ith "lineChart" element
     */
    @Override
    public void setLineChartArray(int i, org.openxmlformats.schemas.drawingml.x2006.chart.CTLineChart lineChart) {
        generatedSetterHelperImpl(lineChart, PROPERTY_QNAME[3], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "lineChart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTLineChart insertNewLineChart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTLineChart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTLineChart)get_store().insert_element_user(PROPERTY_QNAME[3], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "lineChart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTLineChart addNewLineChart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTLineChart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTLineChart)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Removes the ith "lineChart" element
     */
    @Override
    public void removeLineChart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], i);
        }
    }

    /**
     * Gets a List of "line3DChart" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.chart.CTLine3DChart> getLine3DChartList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getLine3DChartArray,
                this::setLine3DChartArray,
                this::insertNewLine3DChart,
                this::removeLine3DChart,
                this::sizeOfLine3DChartArray
            );
        }
    }

    /**
     * Gets array of all "line3DChart" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTLine3DChart[] getLine3DChartArray() {
        return getXmlObjectArray(PROPERTY_QNAME[4], new org.openxmlformats.schemas.drawingml.x2006.chart.CTLine3DChart[0]);
    }

    /**
     * Gets ith "line3DChart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTLine3DChart getLine3DChartArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTLine3DChart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTLine3DChart)get_store().find_element_user(PROPERTY_QNAME[4], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "line3DChart" element
     */
    @Override
    public int sizeOfLine3DChartArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Sets array of all "line3DChart" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setLine3DChartArray(org.openxmlformats.schemas.drawingml.x2006.chart.CTLine3DChart[] line3DChartArray) {
        check_orphaned();
        arraySetterHelper(line3DChartArray, PROPERTY_QNAME[4]);
    }

    /**
     * Sets ith "line3DChart" element
     */
    @Override
    public void setLine3DChartArray(int i, org.openxmlformats.schemas.drawingml.x2006.chart.CTLine3DChart line3DChart) {
        generatedSetterHelperImpl(line3DChart, PROPERTY_QNAME[4], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "line3DChart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTLine3DChart insertNewLine3DChart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTLine3DChart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTLine3DChart)get_store().insert_element_user(PROPERTY_QNAME[4], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "line3DChart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTLine3DChart addNewLine3DChart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTLine3DChart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTLine3DChart)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Removes the ith "line3DChart" element
     */
    @Override
    public void removeLine3DChart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], i);
        }
    }

    /**
     * Gets a List of "stockChart" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.chart.CTStockChart> getStockChartList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getStockChartArray,
                this::setStockChartArray,
                this::insertNewStockChart,
                this::removeStockChart,
                this::sizeOfStockChartArray
            );
        }
    }

    /**
     * Gets array of all "stockChart" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTStockChart[] getStockChartArray() {
        return getXmlObjectArray(PROPERTY_QNAME[5], new org.openxmlformats.schemas.drawingml.x2006.chart.CTStockChart[0]);
    }

    /**
     * Gets ith "stockChart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTStockChart getStockChartArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTStockChart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTStockChart)get_store().find_element_user(PROPERTY_QNAME[5], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "stockChart" element
     */
    @Override
    public int sizeOfStockChartArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Sets array of all "stockChart" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setStockChartArray(org.openxmlformats.schemas.drawingml.x2006.chart.CTStockChart[] stockChartArray) {
        check_orphaned();
        arraySetterHelper(stockChartArray, PROPERTY_QNAME[5]);
    }

    /**
     * Sets ith "stockChart" element
     */
    @Override
    public void setStockChartArray(int i, org.openxmlformats.schemas.drawingml.x2006.chart.CTStockChart stockChart) {
        generatedSetterHelperImpl(stockChart, PROPERTY_QNAME[5], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "stockChart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTStockChart insertNewStockChart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTStockChart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTStockChart)get_store().insert_element_user(PROPERTY_QNAME[5], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "stockChart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTStockChart addNewStockChart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTStockChart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTStockChart)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Removes the ith "stockChart" element
     */
    @Override
    public void removeStockChart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], i);
        }
    }

    /**
     * Gets a List of "radarChart" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.chart.CTRadarChart> getRadarChartList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getRadarChartArray,
                this::setRadarChartArray,
                this::insertNewRadarChart,
                this::removeRadarChart,
                this::sizeOfRadarChartArray
            );
        }
    }

    /**
     * Gets array of all "radarChart" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTRadarChart[] getRadarChartArray() {
        return getXmlObjectArray(PROPERTY_QNAME[6], new org.openxmlformats.schemas.drawingml.x2006.chart.CTRadarChart[0]);
    }

    /**
     * Gets ith "radarChart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTRadarChart getRadarChartArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTRadarChart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTRadarChart)get_store().find_element_user(PROPERTY_QNAME[6], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "radarChart" element
     */
    @Override
    public int sizeOfRadarChartArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]);
        }
    }

    /**
     * Sets array of all "radarChart" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setRadarChartArray(org.openxmlformats.schemas.drawingml.x2006.chart.CTRadarChart[] radarChartArray) {
        check_orphaned();
        arraySetterHelper(radarChartArray, PROPERTY_QNAME[6]);
    }

    /**
     * Sets ith "radarChart" element
     */
    @Override
    public void setRadarChartArray(int i, org.openxmlformats.schemas.drawingml.x2006.chart.CTRadarChart radarChart) {
        generatedSetterHelperImpl(radarChart, PROPERTY_QNAME[6], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "radarChart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTRadarChart insertNewRadarChart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTRadarChart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTRadarChart)get_store().insert_element_user(PROPERTY_QNAME[6], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "radarChart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTRadarChart addNewRadarChart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTRadarChart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTRadarChart)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Removes the ith "radarChart" element
     */
    @Override
    public void removeRadarChart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], i);
        }
    }

    /**
     * Gets a List of "scatterChart" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.chart.CTScatterChart> getScatterChartList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getScatterChartArray,
                this::setScatterChartArray,
                this::insertNewScatterChart,
                this::removeScatterChart,
                this::sizeOfScatterChartArray
            );
        }
    }

    /**
     * Gets array of all "scatterChart" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTScatterChart[] getScatterChartArray() {
        return getXmlObjectArray(PROPERTY_QNAME[7], new org.openxmlformats.schemas.drawingml.x2006.chart.CTScatterChart[0]);
    }

    /**
     * Gets ith "scatterChart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTScatterChart getScatterChartArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTScatterChart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTScatterChart)get_store().find_element_user(PROPERTY_QNAME[7], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "scatterChart" element
     */
    @Override
    public int sizeOfScatterChartArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]);
        }
    }

    /**
     * Sets array of all "scatterChart" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setScatterChartArray(org.openxmlformats.schemas.drawingml.x2006.chart.CTScatterChart[] scatterChartArray) {
        check_orphaned();
        arraySetterHelper(scatterChartArray, PROPERTY_QNAME[7]);
    }

    /**
     * Sets ith "scatterChart" element
     */
    @Override
    public void setScatterChartArray(int i, org.openxmlformats.schemas.drawingml.x2006.chart.CTScatterChart scatterChart) {
        generatedSetterHelperImpl(scatterChart, PROPERTY_QNAME[7], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "scatterChart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTScatterChart insertNewScatterChart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTScatterChart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTScatterChart)get_store().insert_element_user(PROPERTY_QNAME[7], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "scatterChart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTScatterChart addNewScatterChart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTScatterChart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTScatterChart)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Removes the ith "scatterChart" element
     */
    @Override
    public void removeScatterChart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[7], i);
        }
    }

    /**
     * Gets a List of "pieChart" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.chart.CTPieChart> getPieChartList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getPieChartArray,
                this::setPieChartArray,
                this::insertNewPieChart,
                this::removePieChart,
                this::sizeOfPieChartArray
            );
        }
    }

    /**
     * Gets array of all "pieChart" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTPieChart[] getPieChartArray() {
        return getXmlObjectArray(PROPERTY_QNAME[8], new org.openxmlformats.schemas.drawingml.x2006.chart.CTPieChart[0]);
    }

    /**
     * Gets ith "pieChart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTPieChart getPieChartArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTPieChart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTPieChart)get_store().find_element_user(PROPERTY_QNAME[8], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "pieChart" element
     */
    @Override
    public int sizeOfPieChartArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[8]);
        }
    }

    /**
     * Sets array of all "pieChart" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setPieChartArray(org.openxmlformats.schemas.drawingml.x2006.chart.CTPieChart[] pieChartArray) {
        check_orphaned();
        arraySetterHelper(pieChartArray, PROPERTY_QNAME[8]);
    }

    /**
     * Sets ith "pieChart" element
     */
    @Override
    public void setPieChartArray(int i, org.openxmlformats.schemas.drawingml.x2006.chart.CTPieChart pieChart) {
        generatedSetterHelperImpl(pieChart, PROPERTY_QNAME[8], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "pieChart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTPieChart insertNewPieChart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTPieChart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTPieChart)get_store().insert_element_user(PROPERTY_QNAME[8], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "pieChart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTPieChart addNewPieChart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTPieChart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTPieChart)get_store().add_element_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * Removes the ith "pieChart" element
     */
    @Override
    public void removePieChart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[8], i);
        }
    }

    /**
     * Gets a List of "pie3DChart" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.chart.CTPie3DChart> getPie3DChartList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getPie3DChartArray,
                this::setPie3DChartArray,
                this::insertNewPie3DChart,
                this::removePie3DChart,
                this::sizeOfPie3DChartArray
            );
        }
    }

    /**
     * Gets array of all "pie3DChart" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTPie3DChart[] getPie3DChartArray() {
        return getXmlObjectArray(PROPERTY_QNAME[9], new org.openxmlformats.schemas.drawingml.x2006.chart.CTPie3DChart[0]);
    }

    /**
     * Gets ith "pie3DChart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTPie3DChart getPie3DChartArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTPie3DChart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTPie3DChart)get_store().find_element_user(PROPERTY_QNAME[9], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "pie3DChart" element
     */
    @Override
    public int sizeOfPie3DChartArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[9]);
        }
    }

    /**
     * Sets array of all "pie3DChart" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setPie3DChartArray(org.openxmlformats.schemas.drawingml.x2006.chart.CTPie3DChart[] pie3DChartArray) {
        check_orphaned();
        arraySetterHelper(pie3DChartArray, PROPERTY_QNAME[9]);
    }

    /**
     * Sets ith "pie3DChart" element
     */
    @Override
    public void setPie3DChartArray(int i, org.openxmlformats.schemas.drawingml.x2006.chart.CTPie3DChart pie3DChart) {
        generatedSetterHelperImpl(pie3DChart, PROPERTY_QNAME[9], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "pie3DChart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTPie3DChart insertNewPie3DChart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTPie3DChart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTPie3DChart)get_store().insert_element_user(PROPERTY_QNAME[9], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "pie3DChart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTPie3DChart addNewPie3DChart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTPie3DChart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTPie3DChart)get_store().add_element_user(PROPERTY_QNAME[9]);
            return target;
        }
    }

    /**
     * Removes the ith "pie3DChart" element
     */
    @Override
    public void removePie3DChart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[9], i);
        }
    }

    /**
     * Gets a List of "doughnutChart" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.chart.CTDoughnutChart> getDoughnutChartList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getDoughnutChartArray,
                this::setDoughnutChartArray,
                this::insertNewDoughnutChart,
                this::removeDoughnutChart,
                this::sizeOfDoughnutChartArray
            );
        }
    }

    /**
     * Gets array of all "doughnutChart" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTDoughnutChart[] getDoughnutChartArray() {
        return getXmlObjectArray(PROPERTY_QNAME[10], new org.openxmlformats.schemas.drawingml.x2006.chart.CTDoughnutChart[0]);
    }

    /**
     * Gets ith "doughnutChart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTDoughnutChart getDoughnutChartArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTDoughnutChart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTDoughnutChart)get_store().find_element_user(PROPERTY_QNAME[10], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "doughnutChart" element
     */
    @Override
    public int sizeOfDoughnutChartArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[10]);
        }
    }

    /**
     * Sets array of all "doughnutChart" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setDoughnutChartArray(org.openxmlformats.schemas.drawingml.x2006.chart.CTDoughnutChart[] doughnutChartArray) {
        check_orphaned();
        arraySetterHelper(doughnutChartArray, PROPERTY_QNAME[10]);
    }

    /**
     * Sets ith "doughnutChart" element
     */
    @Override
    public void setDoughnutChartArray(int i, org.openxmlformats.schemas.drawingml.x2006.chart.CTDoughnutChart doughnutChart) {
        generatedSetterHelperImpl(doughnutChart, PROPERTY_QNAME[10], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "doughnutChart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTDoughnutChart insertNewDoughnutChart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTDoughnutChart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTDoughnutChart)get_store().insert_element_user(PROPERTY_QNAME[10], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "doughnutChart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTDoughnutChart addNewDoughnutChart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTDoughnutChart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTDoughnutChart)get_store().add_element_user(PROPERTY_QNAME[10]);
            return target;
        }
    }

    /**
     * Removes the ith "doughnutChart" element
     */
    @Override
    public void removeDoughnutChart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[10], i);
        }
    }

    /**
     * Gets a List of "barChart" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.chart.CTBarChart> getBarChartList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getBarChartArray,
                this::setBarChartArray,
                this::insertNewBarChart,
                this::removeBarChart,
                this::sizeOfBarChartArray
            );
        }
    }

    /**
     * Gets array of all "barChart" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBarChart[] getBarChartArray() {
        return getXmlObjectArray(PROPERTY_QNAME[11], new org.openxmlformats.schemas.drawingml.x2006.chart.CTBarChart[0]);
    }

    /**
     * Gets ith "barChart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBarChart getBarChartArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBarChart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBarChart)get_store().find_element_user(PROPERTY_QNAME[11], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "barChart" element
     */
    @Override
    public int sizeOfBarChartArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[11]);
        }
    }

    /**
     * Sets array of all "barChart" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setBarChartArray(org.openxmlformats.schemas.drawingml.x2006.chart.CTBarChart[] barChartArray) {
        check_orphaned();
        arraySetterHelper(barChartArray, PROPERTY_QNAME[11]);
    }

    /**
     * Sets ith "barChart" element
     */
    @Override
    public void setBarChartArray(int i, org.openxmlformats.schemas.drawingml.x2006.chart.CTBarChart barChart) {
        generatedSetterHelperImpl(barChart, PROPERTY_QNAME[11], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "barChart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBarChart insertNewBarChart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBarChart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBarChart)get_store().insert_element_user(PROPERTY_QNAME[11], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "barChart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBarChart addNewBarChart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBarChart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBarChart)get_store().add_element_user(PROPERTY_QNAME[11]);
            return target;
        }
    }

    /**
     * Removes the ith "barChart" element
     */
    @Override
    public void removeBarChart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[11], i);
        }
    }

    /**
     * Gets a List of "bar3DChart" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.chart.CTBar3DChart> getBar3DChartList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getBar3DChartArray,
                this::setBar3DChartArray,
                this::insertNewBar3DChart,
                this::removeBar3DChart,
                this::sizeOfBar3DChartArray
            );
        }
    }

    /**
     * Gets array of all "bar3DChart" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBar3DChart[] getBar3DChartArray() {
        return getXmlObjectArray(PROPERTY_QNAME[12], new org.openxmlformats.schemas.drawingml.x2006.chart.CTBar3DChart[0]);
    }

    /**
     * Gets ith "bar3DChart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBar3DChart getBar3DChartArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBar3DChart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBar3DChart)get_store().find_element_user(PROPERTY_QNAME[12], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "bar3DChart" element
     */
    @Override
    public int sizeOfBar3DChartArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[12]);
        }
    }

    /**
     * Sets array of all "bar3DChart" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setBar3DChartArray(org.openxmlformats.schemas.drawingml.x2006.chart.CTBar3DChart[] bar3DChartArray) {
        check_orphaned();
        arraySetterHelper(bar3DChartArray, PROPERTY_QNAME[12]);
    }

    /**
     * Sets ith "bar3DChart" element
     */
    @Override
    public void setBar3DChartArray(int i, org.openxmlformats.schemas.drawingml.x2006.chart.CTBar3DChart bar3DChart) {
        generatedSetterHelperImpl(bar3DChart, PROPERTY_QNAME[12], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "bar3DChart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBar3DChart insertNewBar3DChart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBar3DChart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBar3DChart)get_store().insert_element_user(PROPERTY_QNAME[12], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "bar3DChart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBar3DChart addNewBar3DChart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBar3DChart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBar3DChart)get_store().add_element_user(PROPERTY_QNAME[12]);
            return target;
        }
    }

    /**
     * Removes the ith "bar3DChart" element
     */
    @Override
    public void removeBar3DChart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[12], i);
        }
    }

    /**
     * Gets a List of "ofPieChart" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.chart.CTOfPieChart> getOfPieChartList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getOfPieChartArray,
                this::setOfPieChartArray,
                this::insertNewOfPieChart,
                this::removeOfPieChart,
                this::sizeOfOfPieChartArray
            );
        }
    }

    /**
     * Gets array of all "ofPieChart" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTOfPieChart[] getOfPieChartArray() {
        return getXmlObjectArray(PROPERTY_QNAME[13], new org.openxmlformats.schemas.drawingml.x2006.chart.CTOfPieChart[0]);
    }

    /**
     * Gets ith "ofPieChart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTOfPieChart getOfPieChartArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTOfPieChart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTOfPieChart)get_store().find_element_user(PROPERTY_QNAME[13], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "ofPieChart" element
     */
    @Override
    public int sizeOfOfPieChartArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[13]);
        }
    }

    /**
     * Sets array of all "ofPieChart" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setOfPieChartArray(org.openxmlformats.schemas.drawingml.x2006.chart.CTOfPieChart[] ofPieChartArray) {
        check_orphaned();
        arraySetterHelper(ofPieChartArray, PROPERTY_QNAME[13]);
    }

    /**
     * Sets ith "ofPieChart" element
     */
    @Override
    public void setOfPieChartArray(int i, org.openxmlformats.schemas.drawingml.x2006.chart.CTOfPieChart ofPieChart) {
        generatedSetterHelperImpl(ofPieChart, PROPERTY_QNAME[13], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "ofPieChart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTOfPieChart insertNewOfPieChart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTOfPieChart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTOfPieChart)get_store().insert_element_user(PROPERTY_QNAME[13], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "ofPieChart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTOfPieChart addNewOfPieChart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTOfPieChart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTOfPieChart)get_store().add_element_user(PROPERTY_QNAME[13]);
            return target;
        }
    }

    /**
     * Removes the ith "ofPieChart" element
     */
    @Override
    public void removeOfPieChart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[13], i);
        }
    }

    /**
     * Gets a List of "surfaceChart" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.chart.CTSurfaceChart> getSurfaceChartList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getSurfaceChartArray,
                this::setSurfaceChartArray,
                this::insertNewSurfaceChart,
                this::removeSurfaceChart,
                this::sizeOfSurfaceChartArray
            );
        }
    }

    /**
     * Gets array of all "surfaceChart" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTSurfaceChart[] getSurfaceChartArray() {
        return getXmlObjectArray(PROPERTY_QNAME[14], new org.openxmlformats.schemas.drawingml.x2006.chart.CTSurfaceChart[0]);
    }

    /**
     * Gets ith "surfaceChart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTSurfaceChart getSurfaceChartArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTSurfaceChart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTSurfaceChart)get_store().find_element_user(PROPERTY_QNAME[14], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "surfaceChart" element
     */
    @Override
    public int sizeOfSurfaceChartArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[14]);
        }
    }

    /**
     * Sets array of all "surfaceChart" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSurfaceChartArray(org.openxmlformats.schemas.drawingml.x2006.chart.CTSurfaceChart[] surfaceChartArray) {
        check_orphaned();
        arraySetterHelper(surfaceChartArray, PROPERTY_QNAME[14]);
    }

    /**
     * Sets ith "surfaceChart" element
     */
    @Override
    public void setSurfaceChartArray(int i, org.openxmlformats.schemas.drawingml.x2006.chart.CTSurfaceChart surfaceChart) {
        generatedSetterHelperImpl(surfaceChart, PROPERTY_QNAME[14], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "surfaceChart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTSurfaceChart insertNewSurfaceChart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTSurfaceChart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTSurfaceChart)get_store().insert_element_user(PROPERTY_QNAME[14], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "surfaceChart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTSurfaceChart addNewSurfaceChart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTSurfaceChart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTSurfaceChart)get_store().add_element_user(PROPERTY_QNAME[14]);
            return target;
        }
    }

    /**
     * Removes the ith "surfaceChart" element
     */
    @Override
    public void removeSurfaceChart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[14], i);
        }
    }

    /**
     * Gets a List of "surface3DChart" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.chart.CTSurface3DChart> getSurface3DChartList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getSurface3DChartArray,
                this::setSurface3DChartArray,
                this::insertNewSurface3DChart,
                this::removeSurface3DChart,
                this::sizeOfSurface3DChartArray
            );
        }
    }

    /**
     * Gets array of all "surface3DChart" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTSurface3DChart[] getSurface3DChartArray() {
        return getXmlObjectArray(PROPERTY_QNAME[15], new org.openxmlformats.schemas.drawingml.x2006.chart.CTSurface3DChart[0]);
    }

    /**
     * Gets ith "surface3DChart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTSurface3DChart getSurface3DChartArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTSurface3DChart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTSurface3DChart)get_store().find_element_user(PROPERTY_QNAME[15], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "surface3DChart" element
     */
    @Override
    public int sizeOfSurface3DChartArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[15]);
        }
    }

    /**
     * Sets array of all "surface3DChart" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSurface3DChartArray(org.openxmlformats.schemas.drawingml.x2006.chart.CTSurface3DChart[] surface3DChartArray) {
        check_orphaned();
        arraySetterHelper(surface3DChartArray, PROPERTY_QNAME[15]);
    }

    /**
     * Sets ith "surface3DChart" element
     */
    @Override
    public void setSurface3DChartArray(int i, org.openxmlformats.schemas.drawingml.x2006.chart.CTSurface3DChart surface3DChart) {
        generatedSetterHelperImpl(surface3DChart, PROPERTY_QNAME[15], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "surface3DChart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTSurface3DChart insertNewSurface3DChart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTSurface3DChart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTSurface3DChart)get_store().insert_element_user(PROPERTY_QNAME[15], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "surface3DChart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTSurface3DChart addNewSurface3DChart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTSurface3DChart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTSurface3DChart)get_store().add_element_user(PROPERTY_QNAME[15]);
            return target;
        }
    }

    /**
     * Removes the ith "surface3DChart" element
     */
    @Override
    public void removeSurface3DChart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[15], i);
        }
    }

    /**
     * Gets a List of "bubbleChart" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.chart.CTBubbleChart> getBubbleChartList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getBubbleChartArray,
                this::setBubbleChartArray,
                this::insertNewBubbleChart,
                this::removeBubbleChart,
                this::sizeOfBubbleChartArray
            );
        }
    }

    /**
     * Gets array of all "bubbleChart" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBubbleChart[] getBubbleChartArray() {
        return getXmlObjectArray(PROPERTY_QNAME[16], new org.openxmlformats.schemas.drawingml.x2006.chart.CTBubbleChart[0]);
    }

    /**
     * Gets ith "bubbleChart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBubbleChart getBubbleChartArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBubbleChart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBubbleChart)get_store().find_element_user(PROPERTY_QNAME[16], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "bubbleChart" element
     */
    @Override
    public int sizeOfBubbleChartArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[16]);
        }
    }

    /**
     * Sets array of all "bubbleChart" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setBubbleChartArray(org.openxmlformats.schemas.drawingml.x2006.chart.CTBubbleChart[] bubbleChartArray) {
        check_orphaned();
        arraySetterHelper(bubbleChartArray, PROPERTY_QNAME[16]);
    }

    /**
     * Sets ith "bubbleChart" element
     */
    @Override
    public void setBubbleChartArray(int i, org.openxmlformats.schemas.drawingml.x2006.chart.CTBubbleChart bubbleChart) {
        generatedSetterHelperImpl(bubbleChart, PROPERTY_QNAME[16], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "bubbleChart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBubbleChart insertNewBubbleChart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBubbleChart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBubbleChart)get_store().insert_element_user(PROPERTY_QNAME[16], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "bubbleChart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBubbleChart addNewBubbleChart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBubbleChart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBubbleChart)get_store().add_element_user(PROPERTY_QNAME[16]);
            return target;
        }
    }

    /**
     * Removes the ith "bubbleChart" element
     */
    @Override
    public void removeBubbleChart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[16], i);
        }
    }

    /**
     * Gets a List of "valAx" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.chart.CTValAx> getValAxList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getValAxArray,
                this::setValAxArray,
                this::insertNewValAx,
                this::removeValAx,
                this::sizeOfValAxArray
            );
        }
    }

    /**
     * Gets array of all "valAx" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTValAx[] getValAxArray() {
        return getXmlObjectArray(PROPERTY_QNAME[17], new org.openxmlformats.schemas.drawingml.x2006.chart.CTValAx[0]);
    }

    /**
     * Gets ith "valAx" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTValAx getValAxArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTValAx target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTValAx)get_store().find_element_user(PROPERTY_QNAME[17], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "valAx" element
     */
    @Override
    public int sizeOfValAxArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[17]);
        }
    }

    /**
     * Sets array of all "valAx" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setValAxArray(org.openxmlformats.schemas.drawingml.x2006.chart.CTValAx[] valAxArray) {
        check_orphaned();
        arraySetterHelper(valAxArray, PROPERTY_QNAME[17]);
    }

    /**
     * Sets ith "valAx" element
     */
    @Override
    public void setValAxArray(int i, org.openxmlformats.schemas.drawingml.x2006.chart.CTValAx valAx) {
        generatedSetterHelperImpl(valAx, PROPERTY_QNAME[17], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "valAx" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTValAx insertNewValAx(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTValAx target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTValAx)get_store().insert_element_user(PROPERTY_QNAME[17], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "valAx" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTValAx addNewValAx() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTValAx target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTValAx)get_store().add_element_user(PROPERTY_QNAME[17]);
            return target;
        }
    }

    /**
     * Removes the ith "valAx" element
     */
    @Override
    public void removeValAx(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[17], i);
        }
    }

    /**
     * Gets a List of "catAx" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.chart.CTCatAx> getCatAxList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCatAxArray,
                this::setCatAxArray,
                this::insertNewCatAx,
                this::removeCatAx,
                this::sizeOfCatAxArray
            );
        }
    }

    /**
     * Gets array of all "catAx" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTCatAx[] getCatAxArray() {
        return getXmlObjectArray(PROPERTY_QNAME[18], new org.openxmlformats.schemas.drawingml.x2006.chart.CTCatAx[0]);
    }

    /**
     * Gets ith "catAx" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTCatAx getCatAxArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTCatAx target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTCatAx)get_store().find_element_user(PROPERTY_QNAME[18], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "catAx" element
     */
    @Override
    public int sizeOfCatAxArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[18]);
        }
    }

    /**
     * Sets array of all "catAx" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCatAxArray(org.openxmlformats.schemas.drawingml.x2006.chart.CTCatAx[] catAxArray) {
        check_orphaned();
        arraySetterHelper(catAxArray, PROPERTY_QNAME[18]);
    }

    /**
     * Sets ith "catAx" element
     */
    @Override
    public void setCatAxArray(int i, org.openxmlformats.schemas.drawingml.x2006.chart.CTCatAx catAx) {
        generatedSetterHelperImpl(catAx, PROPERTY_QNAME[18], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "catAx" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTCatAx insertNewCatAx(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTCatAx target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTCatAx)get_store().insert_element_user(PROPERTY_QNAME[18], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "catAx" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTCatAx addNewCatAx() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTCatAx target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTCatAx)get_store().add_element_user(PROPERTY_QNAME[18]);
            return target;
        }
    }

    /**
     * Removes the ith "catAx" element
     */
    @Override
    public void removeCatAx(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[18], i);
        }
    }

    /**
     * Gets a List of "dateAx" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.chart.CTDateAx> getDateAxList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getDateAxArray,
                this::setDateAxArray,
                this::insertNewDateAx,
                this::removeDateAx,
                this::sizeOfDateAxArray
            );
        }
    }

    /**
     * Gets array of all "dateAx" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTDateAx[] getDateAxArray() {
        return getXmlObjectArray(PROPERTY_QNAME[19], new org.openxmlformats.schemas.drawingml.x2006.chart.CTDateAx[0]);
    }

    /**
     * Gets ith "dateAx" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTDateAx getDateAxArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTDateAx target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTDateAx)get_store().find_element_user(PROPERTY_QNAME[19], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "dateAx" element
     */
    @Override
    public int sizeOfDateAxArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[19]);
        }
    }

    /**
     * Sets array of all "dateAx" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setDateAxArray(org.openxmlformats.schemas.drawingml.x2006.chart.CTDateAx[] dateAxArray) {
        check_orphaned();
        arraySetterHelper(dateAxArray, PROPERTY_QNAME[19]);
    }

    /**
     * Sets ith "dateAx" element
     */
    @Override
    public void setDateAxArray(int i, org.openxmlformats.schemas.drawingml.x2006.chart.CTDateAx dateAx) {
        generatedSetterHelperImpl(dateAx, PROPERTY_QNAME[19], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "dateAx" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTDateAx insertNewDateAx(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTDateAx target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTDateAx)get_store().insert_element_user(PROPERTY_QNAME[19], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "dateAx" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTDateAx addNewDateAx() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTDateAx target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTDateAx)get_store().add_element_user(PROPERTY_QNAME[19]);
            return target;
        }
    }

    /**
     * Removes the ith "dateAx" element
     */
    @Override
    public void removeDateAx(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[19], i);
        }
    }

    /**
     * Gets a List of "serAx" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.chart.CTSerAx> getSerAxList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getSerAxArray,
                this::setSerAxArray,
                this::insertNewSerAx,
                this::removeSerAx,
                this::sizeOfSerAxArray
            );
        }
    }

    /**
     * Gets array of all "serAx" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTSerAx[] getSerAxArray() {
        return getXmlObjectArray(PROPERTY_QNAME[20], new org.openxmlformats.schemas.drawingml.x2006.chart.CTSerAx[0]);
    }

    /**
     * Gets ith "serAx" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTSerAx getSerAxArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTSerAx target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTSerAx)get_store().find_element_user(PROPERTY_QNAME[20], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "serAx" element
     */
    @Override
    public int sizeOfSerAxArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[20]);
        }
    }

    /**
     * Sets array of all "serAx" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSerAxArray(org.openxmlformats.schemas.drawingml.x2006.chart.CTSerAx[] serAxArray) {
        check_orphaned();
        arraySetterHelper(serAxArray, PROPERTY_QNAME[20]);
    }

    /**
     * Sets ith "serAx" element
     */
    @Override
    public void setSerAxArray(int i, org.openxmlformats.schemas.drawingml.x2006.chart.CTSerAx serAx) {
        generatedSetterHelperImpl(serAx, PROPERTY_QNAME[20], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "serAx" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTSerAx insertNewSerAx(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTSerAx target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTSerAx)get_store().insert_element_user(PROPERTY_QNAME[20], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "serAx" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTSerAx addNewSerAx() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTSerAx target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTSerAx)get_store().add_element_user(PROPERTY_QNAME[20]);
            return target;
        }
    }

    /**
     * Removes the ith "serAx" element
     */
    @Override
    public void removeSerAx(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[20], i);
        }
    }

    /**
     * Gets the "dTable" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTDTable getDTable() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTDTable target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTDTable)get_store().find_element_user(PROPERTY_QNAME[21], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "dTable" element
     */
    @Override
    public boolean isSetDTable() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[21]) != 0;
        }
    }

    /**
     * Sets the "dTable" element
     */
    @Override
    public void setDTable(org.openxmlformats.schemas.drawingml.x2006.chart.CTDTable dTable) {
        generatedSetterHelperImpl(dTable, PROPERTY_QNAME[21], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "dTable" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTDTable addNewDTable() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTDTable target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTDTable)get_store().add_element_user(PROPERTY_QNAME[21]);
            return target;
        }
    }

    /**
     * Unsets the "dTable" element
     */
    @Override
    public void unsetDTable() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[21], 0);
        }
    }

    /**
     * Gets the "spPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties getSpPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties)get_store().find_element_user(PROPERTY_QNAME[22], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "spPr" element
     */
    @Override
    public boolean isSetSpPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[22]) != 0;
        }
    }

    /**
     * Sets the "spPr" element
     */
    @Override
    public void setSpPr(org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties spPr) {
        generatedSetterHelperImpl(spPr, PROPERTY_QNAME[22], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "spPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties addNewSpPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties)get_store().add_element_user(PROPERTY_QNAME[22]);
            return target;
        }
    }

    /**
     * Unsets the "spPr" element
     */
    @Override
    public void unsetSpPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[22], 0);
        }
    }

    /**
     * Gets the "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList)get_store().find_element_user(PROPERTY_QNAME[23], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "extLst" element
     */
    @Override
    public boolean isSetExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[23]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[23], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList)get_store().add_element_user(PROPERTY_QNAME[23]);
            return target;
        }
    }

    /**
     * Unsets the "extLst" element
     */
    @Override
    public void unsetExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[23], 0);
        }
    }
}
