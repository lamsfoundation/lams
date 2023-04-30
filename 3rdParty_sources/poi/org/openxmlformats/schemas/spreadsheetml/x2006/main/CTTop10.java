/*
 * XML Type:  CT_Top10
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTop10
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Top10(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTop10 extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTop10> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttop101253type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "top" attribute
     */
    boolean getTop();

    /**
     * Gets (as xml) the "top" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetTop();

    /**
     * True if has "top" attribute
     */
    boolean isSetTop();

    /**
     * Sets the "top" attribute
     */
    void setTop(boolean top);

    /**
     * Sets (as xml) the "top" attribute
     */
    void xsetTop(org.apache.xmlbeans.XmlBoolean top);

    /**
     * Unsets the "top" attribute
     */
    void unsetTop();

    /**
     * Gets the "percent" attribute
     */
    boolean getPercent();

    /**
     * Gets (as xml) the "percent" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetPercent();

    /**
     * True if has "percent" attribute
     */
    boolean isSetPercent();

    /**
     * Sets the "percent" attribute
     */
    void setPercent(boolean percent);

    /**
     * Sets (as xml) the "percent" attribute
     */
    void xsetPercent(org.apache.xmlbeans.XmlBoolean percent);

    /**
     * Unsets the "percent" attribute
     */
    void unsetPercent();

    /**
     * Gets the "val" attribute
     */
    double getVal();

    /**
     * Gets (as xml) the "val" attribute
     */
    org.apache.xmlbeans.XmlDouble xgetVal();

    /**
     * Sets the "val" attribute
     */
    void setVal(double val);

    /**
     * Sets (as xml) the "val" attribute
     */
    void xsetVal(org.apache.xmlbeans.XmlDouble val);

    /**
     * Gets the "filterVal" attribute
     */
    double getFilterVal();

    /**
     * Gets (as xml) the "filterVal" attribute
     */
    org.apache.xmlbeans.XmlDouble xgetFilterVal();

    /**
     * True if has "filterVal" attribute
     */
    boolean isSetFilterVal();

    /**
     * Sets the "filterVal" attribute
     */
    void setFilterVal(double filterVal);

    /**
     * Sets (as xml) the "filterVal" attribute
     */
    void xsetFilterVal(org.apache.xmlbeans.XmlDouble filterVal);

    /**
     * Unsets the "filterVal" attribute
     */
    void unsetFilterVal();
}
