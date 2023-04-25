/*
 * XML Type:  CT_DynamicFilter
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDynamicFilter
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_DynamicFilter(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTDynamicFilter extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDynamicFilter> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctdynamicfilter3bf6type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "type" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STDynamicFilterType.Enum getType();

    /**
     * Gets (as xml) the "type" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STDynamicFilterType xgetType();

    /**
     * Sets the "type" attribute
     */
    void setType(org.openxmlformats.schemas.spreadsheetml.x2006.main.STDynamicFilterType.Enum type);

    /**
     * Sets (as xml) the "type" attribute
     */
    void xsetType(org.openxmlformats.schemas.spreadsheetml.x2006.main.STDynamicFilterType type);

    /**
     * Gets the "val" attribute
     */
    double getVal();

    /**
     * Gets (as xml) the "val" attribute
     */
    org.apache.xmlbeans.XmlDouble xgetVal();

    /**
     * True if has "val" attribute
     */
    boolean isSetVal();

    /**
     * Sets the "val" attribute
     */
    void setVal(double val);

    /**
     * Sets (as xml) the "val" attribute
     */
    void xsetVal(org.apache.xmlbeans.XmlDouble val);

    /**
     * Unsets the "val" attribute
     */
    void unsetVal();

    /**
     * Gets the "valIso" attribute
     */
    java.util.Calendar getValIso();

    /**
     * Gets (as xml) the "valIso" attribute
     */
    org.apache.xmlbeans.XmlDateTime xgetValIso();

    /**
     * True if has "valIso" attribute
     */
    boolean isSetValIso();

    /**
     * Sets the "valIso" attribute
     */
    void setValIso(java.util.Calendar valIso);

    /**
     * Sets (as xml) the "valIso" attribute
     */
    void xsetValIso(org.apache.xmlbeans.XmlDateTime valIso);

    /**
     * Unsets the "valIso" attribute
     */
    void unsetValIso();

    /**
     * Gets the "maxVal" attribute
     */
    double getMaxVal();

    /**
     * Gets (as xml) the "maxVal" attribute
     */
    org.apache.xmlbeans.XmlDouble xgetMaxVal();

    /**
     * True if has "maxVal" attribute
     */
    boolean isSetMaxVal();

    /**
     * Sets the "maxVal" attribute
     */
    void setMaxVal(double maxVal);

    /**
     * Sets (as xml) the "maxVal" attribute
     */
    void xsetMaxVal(org.apache.xmlbeans.XmlDouble maxVal);

    /**
     * Unsets the "maxVal" attribute
     */
    void unsetMaxVal();

    /**
     * Gets the "maxValIso" attribute
     */
    java.util.Calendar getMaxValIso();

    /**
     * Gets (as xml) the "maxValIso" attribute
     */
    org.apache.xmlbeans.XmlDateTime xgetMaxValIso();

    /**
     * True if has "maxValIso" attribute
     */
    boolean isSetMaxValIso();

    /**
     * Sets the "maxValIso" attribute
     */
    void setMaxValIso(java.util.Calendar maxValIso);

    /**
     * Sets (as xml) the "maxValIso" attribute
     */
    void xsetMaxValIso(org.apache.xmlbeans.XmlDateTime maxValIso);

    /**
     * Unsets the "maxValIso" attribute
     */
    void unsetMaxValIso();
}
