/*
 * XML Type:  CT_PCDKPIs
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPCDKPIs
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_PCDKPIs(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTPCDKPIs extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPCDKPIs> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctpcdkpis8c1ftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "kpi" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPCDKPI> getKpiList();

    /**
     * Gets array of all "kpi" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPCDKPI[] getKpiArray();

    /**
     * Gets ith "kpi" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPCDKPI getKpiArray(int i);

    /**
     * Returns number of "kpi" element
     */
    int sizeOfKpiArray();

    /**
     * Sets array of all "kpi" element
     */
    void setKpiArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPCDKPI[] kpiArray);

    /**
     * Sets ith "kpi" element
     */
    void setKpiArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPCDKPI kpi);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "kpi" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPCDKPI insertNewKpi(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "kpi" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPCDKPI addNewKpi();

    /**
     * Removes the ith "kpi" element
     */
    void removeKpi(int i);

    /**
     * Gets the "count" attribute
     */
    long getCount();

    /**
     * Gets (as xml) the "count" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetCount();

    /**
     * True if has "count" attribute
     */
    boolean isSetCount();

    /**
     * Sets the "count" attribute
     */
    void setCount(long count);

    /**
     * Sets (as xml) the "count" attribute
     */
    void xsetCount(org.apache.xmlbeans.XmlUnsignedInt count);

    /**
     * Unsets the "count" attribute
     */
    void unsetCount();
}
