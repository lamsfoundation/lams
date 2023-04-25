/*
 * XML Type:  CT_ExternalSheetNames
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalSheetNames
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_ExternalSheetNames(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTExternalSheetNamesImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalSheetNames {
    private static final long serialVersionUID = 1L;

    public CTExternalSheetNamesImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "sheetName"),
    };


    /**
     * Gets a List of "sheetName" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalSheetName> getSheetNameList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getSheetNameArray,
                this::setSheetNameArray,
                this::insertNewSheetName,
                this::removeSheetName,
                this::sizeOfSheetNameArray
            );
        }
    }

    /**
     * Gets array of all "sheetName" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalSheetName[] getSheetNameArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalSheetName[0]);
    }

    /**
     * Gets ith "sheetName" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalSheetName getSheetNameArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalSheetName target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalSheetName)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "sheetName" element
     */
    @Override
    public int sizeOfSheetNameArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "sheetName" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSheetNameArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalSheetName[] sheetNameArray) {
        check_orphaned();
        arraySetterHelper(sheetNameArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "sheetName" element
     */
    @Override
    public void setSheetNameArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalSheetName sheetName) {
        generatedSetterHelperImpl(sheetName, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "sheetName" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalSheetName insertNewSheetName(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalSheetName target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalSheetName)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "sheetName" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalSheetName addNewSheetName() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalSheetName target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalSheetName)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "sheetName" element
     */
    @Override
    public void removeSheetName(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
