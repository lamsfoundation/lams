/*
 * XML Type:  CT_CustomSheetViews
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomSheetViews
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_CustomSheetViews(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTCustomSheetViewsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomSheetViews {
    private static final long serialVersionUID = 1L;

    public CTCustomSheetViewsImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "customSheetView"),
    };


    /**
     * Gets a List of "customSheetView" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomSheetView> getCustomSheetViewList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCustomSheetViewArray,
                this::setCustomSheetViewArray,
                this::insertNewCustomSheetView,
                this::removeCustomSheetView,
                this::sizeOfCustomSheetViewArray
            );
        }
    }

    /**
     * Gets array of all "customSheetView" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomSheetView[] getCustomSheetViewArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomSheetView[0]);
    }

    /**
     * Gets ith "customSheetView" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomSheetView getCustomSheetViewArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomSheetView target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomSheetView)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "customSheetView" element
     */
    @Override
    public int sizeOfCustomSheetViewArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "customSheetView" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCustomSheetViewArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomSheetView[] customSheetViewArray) {
        check_orphaned();
        arraySetterHelper(customSheetViewArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "customSheetView" element
     */
    @Override
    public void setCustomSheetViewArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomSheetView customSheetView) {
        generatedSetterHelperImpl(customSheetView, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "customSheetView" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomSheetView insertNewCustomSheetView(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomSheetView target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomSheetView)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "customSheetView" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomSheetView addNewCustomSheetView() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomSheetView target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomSheetView)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "customSheetView" element
     */
    @Override
    public void removeCustomSheetView(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
