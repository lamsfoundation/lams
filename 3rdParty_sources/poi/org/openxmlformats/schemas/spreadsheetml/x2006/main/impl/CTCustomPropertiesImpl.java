/*
 * XML Type:  CT_CustomProperties
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomProperties
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_CustomProperties(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTCustomPropertiesImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomProperties {
    private static final long serialVersionUID = 1L;

    public CTCustomPropertiesImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "customPr"),
    };


    /**
     * Gets a List of "customPr" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomProperty> getCustomPrList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCustomPrArray,
                this::setCustomPrArray,
                this::insertNewCustomPr,
                this::removeCustomPr,
                this::sizeOfCustomPrArray
            );
        }
    }

    /**
     * Gets array of all "customPr" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomProperty[] getCustomPrArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomProperty[0]);
    }

    /**
     * Gets ith "customPr" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomProperty getCustomPrArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomProperty target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomProperty)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "customPr" element
     */
    @Override
    public int sizeOfCustomPrArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "customPr" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCustomPrArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomProperty[] customPrArray) {
        check_orphaned();
        arraySetterHelper(customPrArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "customPr" element
     */
    @Override
    public void setCustomPrArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomProperty customPr) {
        generatedSetterHelperImpl(customPr, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "customPr" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomProperty insertNewCustomPr(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomProperty target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomProperty)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "customPr" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomProperty addNewCustomPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomProperty target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomProperty)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "customPr" element
     */
    @Override
    public void removeCustomPr(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
