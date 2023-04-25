/*
 * XML Type:  CT_AxDataSource
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTAxDataSource
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_AxDataSource(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public interface CTAxDataSource extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.chart.CTAxDataSource> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctaxdatasource1440type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "multiLvlStrRef" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTMultiLvlStrRef getMultiLvlStrRef();

    /**
     * True if has "multiLvlStrRef" element
     */
    boolean isSetMultiLvlStrRef();

    /**
     * Sets the "multiLvlStrRef" element
     */
    void setMultiLvlStrRef(org.openxmlformats.schemas.drawingml.x2006.chart.CTMultiLvlStrRef multiLvlStrRef);

    /**
     * Appends and returns a new empty "multiLvlStrRef" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTMultiLvlStrRef addNewMultiLvlStrRef();

    /**
     * Unsets the "multiLvlStrRef" element
     */
    void unsetMultiLvlStrRef();

    /**
     * Gets the "numRef" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTNumRef getNumRef();

    /**
     * True if has "numRef" element
     */
    boolean isSetNumRef();

    /**
     * Sets the "numRef" element
     */
    void setNumRef(org.openxmlformats.schemas.drawingml.x2006.chart.CTNumRef numRef);

    /**
     * Appends and returns a new empty "numRef" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTNumRef addNewNumRef();

    /**
     * Unsets the "numRef" element
     */
    void unsetNumRef();

    /**
     * Gets the "numLit" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTNumData getNumLit();

    /**
     * True if has "numLit" element
     */
    boolean isSetNumLit();

    /**
     * Sets the "numLit" element
     */
    void setNumLit(org.openxmlformats.schemas.drawingml.x2006.chart.CTNumData numLit);

    /**
     * Appends and returns a new empty "numLit" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTNumData addNewNumLit();

    /**
     * Unsets the "numLit" element
     */
    void unsetNumLit();

    /**
     * Gets the "strRef" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTStrRef getStrRef();

    /**
     * True if has "strRef" element
     */
    boolean isSetStrRef();

    /**
     * Sets the "strRef" element
     */
    void setStrRef(org.openxmlformats.schemas.drawingml.x2006.chart.CTStrRef strRef);

    /**
     * Appends and returns a new empty "strRef" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTStrRef addNewStrRef();

    /**
     * Unsets the "strRef" element
     */
    void unsetStrRef();

    /**
     * Gets the "strLit" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTStrData getStrLit();

    /**
     * True if has "strLit" element
     */
    boolean isSetStrLit();

    /**
     * Sets the "strLit" element
     */
    void setStrLit(org.openxmlformats.schemas.drawingml.x2006.chart.CTStrData strLit);

    /**
     * Appends and returns a new empty "strLit" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTStrData addNewStrLit();

    /**
     * Unsets the "strLit" element
     */
    void unsetStrLit();
}
