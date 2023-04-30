/*
 * XML Type:  CT_MultiLvlStrData
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTMultiLvlStrData
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_MultiLvlStrData(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public interface CTMultiLvlStrData extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.chart.CTMultiLvlStrData> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctmultilvlstrdatafb14type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "ptCount" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt getPtCount();

    /**
     * True if has "ptCount" element
     */
    boolean isSetPtCount();

    /**
     * Sets the "ptCount" element
     */
    void setPtCount(org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt ptCount);

    /**
     * Appends and returns a new empty "ptCount" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt addNewPtCount();

    /**
     * Unsets the "ptCount" element
     */
    void unsetPtCount();

    /**
     * Gets a List of "lvl" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.chart.CTLvl> getLvlList();

    /**
     * Gets array of all "lvl" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTLvl[] getLvlArray();

    /**
     * Gets ith "lvl" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTLvl getLvlArray(int i);

    /**
     * Returns number of "lvl" element
     */
    int sizeOfLvlArray();

    /**
     * Sets array of all "lvl" element
     */
    void setLvlArray(org.openxmlformats.schemas.drawingml.x2006.chart.CTLvl[] lvlArray);

    /**
     * Sets ith "lvl" element
     */
    void setLvlArray(int i, org.openxmlformats.schemas.drawingml.x2006.chart.CTLvl lvl);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "lvl" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTLvl insertNewLvl(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "lvl" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTLvl addNewLvl();

    /**
     * Removes the ith "lvl" element
     */
    void removeLvl(int i);

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
