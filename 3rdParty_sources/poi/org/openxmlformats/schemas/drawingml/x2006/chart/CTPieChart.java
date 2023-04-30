/*
 * XML Type:  CT_PieChart
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTPieChart
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_PieChart(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public interface CTPieChart extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.chart.CTPieChart> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctpiechartd34atype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


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
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.chart.CTPieSer> getSerList();

    /**
     * Gets array of all "ser" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTPieSer[] getSerArray();

    /**
     * Gets ith "ser" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTPieSer getSerArray(int i);

    /**
     * Returns number of "ser" element
     */
    int sizeOfSerArray();

    /**
     * Sets array of all "ser" element
     */
    void setSerArray(org.openxmlformats.schemas.drawingml.x2006.chart.CTPieSer[] serArray);

    /**
     * Sets ith "ser" element
     */
    void setSerArray(int i, org.openxmlformats.schemas.drawingml.x2006.chart.CTPieSer ser);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "ser" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTPieSer insertNewSer(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "ser" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTPieSer addNewSer();

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
     * Gets the "firstSliceAng" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTFirstSliceAng getFirstSliceAng();

    /**
     * True if has "firstSliceAng" element
     */
    boolean isSetFirstSliceAng();

    /**
     * Sets the "firstSliceAng" element
     */
    void setFirstSliceAng(org.openxmlformats.schemas.drawingml.x2006.chart.CTFirstSliceAng firstSliceAng);

    /**
     * Appends and returns a new empty "firstSliceAng" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTFirstSliceAng addNewFirstSliceAng();

    /**
     * Unsets the "firstSliceAng" element
     */
    void unsetFirstSliceAng();

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
