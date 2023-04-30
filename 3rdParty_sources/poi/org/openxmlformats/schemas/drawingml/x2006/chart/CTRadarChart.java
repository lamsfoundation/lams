/*
 * XML Type:  CT_RadarChart
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTRadarChart
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_RadarChart(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public interface CTRadarChart extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.chart.CTRadarChart> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctradarchart0f04type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "radarStyle" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTRadarStyle getRadarStyle();

    /**
     * Sets the "radarStyle" element
     */
    void setRadarStyle(org.openxmlformats.schemas.drawingml.x2006.chart.CTRadarStyle radarStyle);

    /**
     * Appends and returns a new empty "radarStyle" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTRadarStyle addNewRadarStyle();

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
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.chart.CTRadarSer> getSerList();

    /**
     * Gets array of all "ser" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTRadarSer[] getSerArray();

    /**
     * Gets ith "ser" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTRadarSer getSerArray(int i);

    /**
     * Returns number of "ser" element
     */
    int sizeOfSerArray();

    /**
     * Sets array of all "ser" element
     */
    void setSerArray(org.openxmlformats.schemas.drawingml.x2006.chart.CTRadarSer[] serArray);

    /**
     * Sets ith "ser" element
     */
    void setSerArray(int i, org.openxmlformats.schemas.drawingml.x2006.chart.CTRadarSer ser);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "ser" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTRadarSer insertNewSer(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "ser" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTRadarSer addNewSer();

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
