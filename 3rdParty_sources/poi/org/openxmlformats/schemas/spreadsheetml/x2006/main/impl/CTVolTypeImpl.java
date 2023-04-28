/*
 * XML Type:  CT_VolType
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolType
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_VolType(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTVolTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolType {
    private static final long serialVersionUID = 1L;

    public CTVolTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "main"),
        new QName("", "type"),
    };


    /**
     * Gets a List of "main" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolMain> getMainList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getMainArray,
                this::setMainArray,
                this::insertNewMain,
                this::removeMain,
                this::sizeOfMainArray
            );
        }
    }

    /**
     * Gets array of all "main" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolMain[] getMainArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolMain[0]);
    }

    /**
     * Gets ith "main" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolMain getMainArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolMain target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolMain)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "main" element
     */
    @Override
    public int sizeOfMainArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "main" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setMainArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolMain[] mainArray) {
        check_orphaned();
        arraySetterHelper(mainArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "main" element
     */
    @Override
    public void setMainArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolMain main) {
        generatedSetterHelperImpl(main, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "main" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolMain insertNewMain(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolMain target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolMain)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "main" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolMain addNewMain() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolMain target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolMain)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "main" element
     */
    @Override
    public void removeMain(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets the "type" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STVolDepType.Enum getType() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? null : (org.openxmlformats.schemas.spreadsheetml.x2006.main.STVolDepType.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "type" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STVolDepType xgetType() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STVolDepType target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STVolDepType)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Sets the "type" attribute
     */
    @Override
    public void setType(org.openxmlformats.schemas.spreadsheetml.x2006.main.STVolDepType.Enum type) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setEnumValue(type);
        }
    }

    /**
     * Sets (as xml) the "type" attribute
     */
    @Override
    public void xsetType(org.openxmlformats.schemas.spreadsheetml.x2006.main.STVolDepType type) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STVolDepType target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STVolDepType)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STVolDepType)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(type);
        }
    }
}
