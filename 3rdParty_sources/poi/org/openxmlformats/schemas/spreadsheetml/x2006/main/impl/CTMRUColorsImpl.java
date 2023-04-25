/*
 * XML Type:  CT_MRUColors
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMRUColors
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_MRUColors(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTMRUColorsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMRUColors {
    private static final long serialVersionUID = 1L;

    public CTMRUColorsImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "color"),
    };


    /**
     * Gets a List of "color" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor> getColorList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getColorArray,
                this::setColorArray,
                this::insertNewColor,
                this::removeColor,
                this::sizeOfColorArray
            );
        }
    }

    /**
     * Gets array of all "color" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor[] getColorArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor[0]);
    }

    /**
     * Gets ith "color" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor getColorArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "color" element
     */
    @Override
    public int sizeOfColorArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "color" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setColorArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor[] colorArray) {
        check_orphaned();
        arraySetterHelper(colorArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "color" element
     */
    @Override
    public void setColorArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor color) {
        generatedSetterHelperImpl(color, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "color" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor insertNewColor(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "color" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor addNewColor() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "color" element
     */
    @Override
    public void removeColor(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
