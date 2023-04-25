/*
 * XML Type:  CT_TrendlineLbl
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTTrendlineLbl
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TrendlineLbl(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public interface CTTrendlineLbl extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.chart.CTTrendlineLbl> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttrendlinelbl4537type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "layout" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTLayout getLayout();

    /**
     * True if has "layout" element
     */
    boolean isSetLayout();

    /**
     * Sets the "layout" element
     */
    void setLayout(org.openxmlformats.schemas.drawingml.x2006.chart.CTLayout layout);

    /**
     * Appends and returns a new empty "layout" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTLayout addNewLayout();

    /**
     * Unsets the "layout" element
     */
    void unsetLayout();

    /**
     * Gets the "tx" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTTx getTx();

    /**
     * True if has "tx" element
     */
    boolean isSetTx();

    /**
     * Sets the "tx" element
     */
    void setTx(org.openxmlformats.schemas.drawingml.x2006.chart.CTTx tx);

    /**
     * Appends and returns a new empty "tx" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTTx addNewTx();

    /**
     * Unsets the "tx" element
     */
    void unsetTx();

    /**
     * Gets the "numFmt" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTNumFmt getNumFmt();

    /**
     * True if has "numFmt" element
     */
    boolean isSetNumFmt();

    /**
     * Sets the "numFmt" element
     */
    void setNumFmt(org.openxmlformats.schemas.drawingml.x2006.chart.CTNumFmt numFmt);

    /**
     * Appends and returns a new empty "numFmt" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTNumFmt addNewNumFmt();

    /**
     * Unsets the "numFmt" element
     */
    void unsetNumFmt();

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
     * Gets the "txPr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody getTxPr();

    /**
     * True if has "txPr" element
     */
    boolean isSetTxPr();

    /**
     * Sets the "txPr" element
     */
    void setTxPr(org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody txPr);

    /**
     * Appends and returns a new empty "txPr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody addNewTxPr();

    /**
     * Unsets the "txPr" element
     */
    void unsetTxPr();

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
