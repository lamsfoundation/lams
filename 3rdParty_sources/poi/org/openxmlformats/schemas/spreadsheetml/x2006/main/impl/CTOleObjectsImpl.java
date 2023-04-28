/*
 * XML Type:  CT_OleObjects
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObjects
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_OleObjects(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTOleObjectsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObjects {
    private static final long serialVersionUID = 1L;

    public CTOleObjectsImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "oleObject"),
    };


    /**
     * Gets a List of "oleObject" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject> getOleObjectList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getOleObjectArray,
                this::setOleObjectArray,
                this::insertNewOleObject,
                this::removeOleObject,
                this::sizeOfOleObjectArray
            );
        }
    }

    /**
     * Gets array of all "oleObject" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject[] getOleObjectArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject[0]);
    }

    /**
     * Gets ith "oleObject" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject getOleObjectArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "oleObject" element
     */
    @Override
    public int sizeOfOleObjectArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "oleObject" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setOleObjectArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject[] oleObjectArray) {
        check_orphaned();
        arraySetterHelper(oleObjectArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "oleObject" element
     */
    @Override
    public void setOleObjectArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject oleObject) {
        generatedSetterHelperImpl(oleObject, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "oleObject" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject insertNewOleObject(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "oleObject" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject addNewOleObject() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "oleObject" element
     */
    @Override
    public void removeOleObject(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
