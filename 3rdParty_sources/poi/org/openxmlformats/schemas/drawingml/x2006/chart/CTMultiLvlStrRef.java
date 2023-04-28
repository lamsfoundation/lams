/*
 * XML Type:  CT_MultiLvlStrRef
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTMultiLvlStrRef
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_MultiLvlStrRef(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public interface CTMultiLvlStrRef extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.chart.CTMultiLvlStrRef> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctmultilvlstrrefe271type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "f" element
     */
    java.lang.String getF();

    /**
     * Gets (as xml) the "f" element
     */
    org.apache.xmlbeans.XmlString xgetF();

    /**
     * Sets the "f" element
     */
    void setF(java.lang.String f);

    /**
     * Sets (as xml) the "f" element
     */
    void xsetF(org.apache.xmlbeans.XmlString f);

    /**
     * Gets the "multiLvlStrCache" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTMultiLvlStrData getMultiLvlStrCache();

    /**
     * True if has "multiLvlStrCache" element
     */
    boolean isSetMultiLvlStrCache();

    /**
     * Sets the "multiLvlStrCache" element
     */
    void setMultiLvlStrCache(org.openxmlformats.schemas.drawingml.x2006.chart.CTMultiLvlStrData multiLvlStrCache);

    /**
     * Appends and returns a new empty "multiLvlStrCache" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTMultiLvlStrData addNewMultiLvlStrCache();

    /**
     * Unsets the "multiLvlStrCache" element
     */
    void unsetMultiLvlStrCache();

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
