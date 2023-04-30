/*
 * XML Type:  CT_BubbleSer
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTBubbleSer
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_BubbleSer(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public interface CTBubbleSer extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.chart.CTBubbleSer> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctbubblesere172type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "idx" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt getIdx();

    /**
     * Sets the "idx" element
     */
    void setIdx(org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt idx);

    /**
     * Appends and returns a new empty "idx" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt addNewIdx();

    /**
     * Gets the "order" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt getOrder();

    /**
     * Sets the "order" element
     */
    void setOrder(org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt order);

    /**
     * Appends and returns a new empty "order" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt addNewOrder();

    /**
     * Gets the "tx" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTSerTx getTx();

    /**
     * True if has "tx" element
     */
    boolean isSetTx();

    /**
     * Sets the "tx" element
     */
    void setTx(org.openxmlformats.schemas.drawingml.x2006.chart.CTSerTx tx);

    /**
     * Appends and returns a new empty "tx" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTSerTx addNewTx();

    /**
     * Unsets the "tx" element
     */
    void unsetTx();

    /**
     * Gets the "spPr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties getSpPr();

    /**
     * True if has "spPr" element
     */
    boolean isSetSpPr();

    /**
     * Sets the "spPr" element
     */
    void setSpPr(org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties spPr);

    /**
     * Appends and returns a new empty "spPr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties addNewSpPr();

    /**
     * Unsets the "spPr" element
     */
    void unsetSpPr();

    /**
     * Gets the "invertIfNegative" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean getInvertIfNegative();

    /**
     * True if has "invertIfNegative" element
     */
    boolean isSetInvertIfNegative();

    /**
     * Sets the "invertIfNegative" element
     */
    void setInvertIfNegative(org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean invertIfNegative);

    /**
     * Appends and returns a new empty "invertIfNegative" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean addNewInvertIfNegative();

    /**
     * Unsets the "invertIfNegative" element
     */
    void unsetInvertIfNegative();

    /**
     * Gets a List of "dPt" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.chart.CTDPt> getDPtList();

    /**
     * Gets array of all "dPt" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTDPt[] getDPtArray();

    /**
     * Gets ith "dPt" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTDPt getDPtArray(int i);

    /**
     * Returns number of "dPt" element
     */
    int sizeOfDPtArray();

    /**
     * Sets array of all "dPt" element
     */
    void setDPtArray(org.openxmlformats.schemas.drawingml.x2006.chart.CTDPt[] dPtArray);

    /**
     * Sets ith "dPt" element
     */
    void setDPtArray(int i, org.openxmlformats.schemas.drawingml.x2006.chart.CTDPt dPt);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "dPt" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTDPt insertNewDPt(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "dPt" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTDPt addNewDPt();

    /**
     * Removes the ith "dPt" element
     */
    void removeDPt(int i);

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
     * Gets a List of "trendline" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.chart.CTTrendline> getTrendlineList();

    /**
     * Gets array of all "trendline" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTTrendline[] getTrendlineArray();

    /**
     * Gets ith "trendline" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTTrendline getTrendlineArray(int i);

    /**
     * Returns number of "trendline" element
     */
    int sizeOfTrendlineArray();

    /**
     * Sets array of all "trendline" element
     */
    void setTrendlineArray(org.openxmlformats.schemas.drawingml.x2006.chart.CTTrendline[] trendlineArray);

    /**
     * Sets ith "trendline" element
     */
    void setTrendlineArray(int i, org.openxmlformats.schemas.drawingml.x2006.chart.CTTrendline trendline);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "trendline" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTTrendline insertNewTrendline(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "trendline" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTTrendline addNewTrendline();

    /**
     * Removes the ith "trendline" element
     */
    void removeTrendline(int i);

    /**
     * Gets a List of "errBars" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.chart.CTErrBars> getErrBarsList();

    /**
     * Gets array of all "errBars" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTErrBars[] getErrBarsArray();

    /**
     * Gets ith "errBars" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTErrBars getErrBarsArray(int i);

    /**
     * Returns number of "errBars" element
     */
    int sizeOfErrBarsArray();

    /**
     * Sets array of all "errBars" element
     */
    void setErrBarsArray(org.openxmlformats.schemas.drawingml.x2006.chart.CTErrBars[] errBarsArray);

    /**
     * Sets ith "errBars" element
     */
    void setErrBarsArray(int i, org.openxmlformats.schemas.drawingml.x2006.chart.CTErrBars errBars);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "errBars" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTErrBars insertNewErrBars(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "errBars" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTErrBars addNewErrBars();

    /**
     * Removes the ith "errBars" element
     */
    void removeErrBars(int i);

    /**
     * Gets the "xVal" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTAxDataSource getXVal();

    /**
     * True if has "xVal" element
     */
    boolean isSetXVal();

    /**
     * Sets the "xVal" element
     */
    void setXVal(org.openxmlformats.schemas.drawingml.x2006.chart.CTAxDataSource xVal);

    /**
     * Appends and returns a new empty "xVal" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTAxDataSource addNewXVal();

    /**
     * Unsets the "xVal" element
     */
    void unsetXVal();

    /**
     * Gets the "yVal" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource getYVal();

    /**
     * True if has "yVal" element
     */
    boolean isSetYVal();

    /**
     * Sets the "yVal" element
     */
    void setYVal(org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource yVal);

    /**
     * Appends and returns a new empty "yVal" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource addNewYVal();

    /**
     * Unsets the "yVal" element
     */
    void unsetYVal();

    /**
     * Gets the "bubbleSize" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource getBubbleSize();

    /**
     * True if has "bubbleSize" element
     */
    boolean isSetBubbleSize();

    /**
     * Sets the "bubbleSize" element
     */
    void setBubbleSize(org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource bubbleSize);

    /**
     * Appends and returns a new empty "bubbleSize" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource addNewBubbleSize();

    /**
     * Unsets the "bubbleSize" element
     */
    void unsetBubbleSize();

    /**
     * Gets the "bubble3D" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean getBubble3D();

    /**
     * True if has "bubble3D" element
     */
    boolean isSetBubble3D();

    /**
     * Sets the "bubble3D" element
     */
    void setBubble3D(org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean bubble3D);

    /**
     * Appends and returns a new empty "bubble3D" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean addNewBubble3D();

    /**
     * Unsets the "bubble3D" element
     */
    void unsetBubble3D();

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
