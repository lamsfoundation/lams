/*
 * XML Type:  CT_ExternalDefinedNames
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalDefinedNames
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_ExternalDefinedNames(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTExternalDefinedNamesImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalDefinedNames {
    private static final long serialVersionUID = 1L;

    public CTExternalDefinedNamesImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "definedName"),
    };


    /**
     * Gets a List of "definedName" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalDefinedName> getDefinedNameList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getDefinedNameArray,
                this::setDefinedNameArray,
                this::insertNewDefinedName,
                this::removeDefinedName,
                this::sizeOfDefinedNameArray
            );
        }
    }

    /**
     * Gets array of all "definedName" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalDefinedName[] getDefinedNameArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalDefinedName[0]);
    }

    /**
     * Gets ith "definedName" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalDefinedName getDefinedNameArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalDefinedName target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalDefinedName)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "definedName" element
     */
    @Override
    public int sizeOfDefinedNameArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "definedName" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setDefinedNameArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalDefinedName[] definedNameArray) {
        check_orphaned();
        arraySetterHelper(definedNameArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "definedName" element
     */
    @Override
    public void setDefinedNameArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalDefinedName definedName) {
        generatedSetterHelperImpl(definedName, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "definedName" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalDefinedName insertNewDefinedName(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalDefinedName target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalDefinedName)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "definedName" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalDefinedName addNewDefinedName() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalDefinedName target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalDefinedName)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "definedName" element
     */
    @Override
    public void removeDefinedName(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
