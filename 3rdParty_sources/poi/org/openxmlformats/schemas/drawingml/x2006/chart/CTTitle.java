/*
 * XML Type:  CT_Title
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTTitle
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Title(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public interface CTTitle extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.chart.CTTitle> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttitleb54etype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


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
     * Gets the "overlay" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean getOverlay();

    /**
     * True if has "overlay" element
     */
    boolean isSetOverlay();

    /**
     * Sets the "overlay" element
     */
    void setOverlay(org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean overlay);

    /**
     * Appends and returns a new empty "overlay" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean addNewOverlay();

    /**
     * Unsets the "overlay" element
     */
    void unsetOverlay();

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
