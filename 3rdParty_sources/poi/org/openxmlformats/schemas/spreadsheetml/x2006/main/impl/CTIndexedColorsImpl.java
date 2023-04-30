/*
 * XML Type:  CT_IndexedColors
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIndexedColors
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_IndexedColors(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTIndexedColorsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIndexedColors {
    private static final long serialVersionUID = 1L;

    public CTIndexedColorsImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "rgbColor"),
    };


    /**
     * Gets a List of "rgbColor" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRgbColor> getRgbColorList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getRgbColorArray,
                this::setRgbColorArray,
                this::insertNewRgbColor,
                this::removeRgbColor,
                this::sizeOfRgbColorArray
            );
        }
    }

    /**
     * Gets array of all "rgbColor" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRgbColor[] getRgbColorArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRgbColor[0]);
    }

    /**
     * Gets ith "rgbColor" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRgbColor getRgbColorArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRgbColor target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRgbColor)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "rgbColor" element
     */
    @Override
    public int sizeOfRgbColorArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "rgbColor" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setRgbColorArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRgbColor[] rgbColorArray) {
        check_orphaned();
        arraySetterHelper(rgbColorArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "rgbColor" element
     */
    @Override
    public void setRgbColorArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRgbColor rgbColor) {
        generatedSetterHelperImpl(rgbColor, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "rgbColor" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRgbColor insertNewRgbColor(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRgbColor target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRgbColor)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "rgbColor" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRgbColor addNewRgbColor() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRgbColor target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRgbColor)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "rgbColor" element
     */
    @Override
    public void removeRgbColor(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
