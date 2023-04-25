/*
 * XML Type:  CT_VolMain
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolMain
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_VolMain(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTVolMainImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolMain {
    private static final long serialVersionUID = 1L;

    public CTVolMainImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "tp"),
        new QName("", "first"),
    };


    /**
     * Gets a List of "tp" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolTopic> getTpList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getTpArray,
                this::setTpArray,
                this::insertNewTp,
                this::removeTp,
                this::sizeOfTpArray
            );
        }
    }

    /**
     * Gets array of all "tp" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolTopic[] getTpArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolTopic[0]);
    }

    /**
     * Gets ith "tp" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolTopic getTpArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolTopic target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolTopic)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "tp" element
     */
    @Override
    public int sizeOfTpArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "tp" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setTpArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolTopic[] tpArray) {
        check_orphaned();
        arraySetterHelper(tpArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "tp" element
     */
    @Override
    public void setTpArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolTopic tp) {
        generatedSetterHelperImpl(tp, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "tp" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolTopic insertNewTp(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolTopic target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolTopic)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "tp" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolTopic addNewTp() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolTopic target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolTopic)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "tp" element
     */
    @Override
    public void removeTp(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets the "first" attribute
     */
    @Override
    public java.lang.String getFirst() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "first" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetFirst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Sets the "first" attribute
     */
    @Override
    public void setFirst(java.lang.String first) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setStringValue(first);
        }
    }

    /**
     * Sets (as xml) the "first" attribute
     */
    @Override
    public void xsetFirst(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring first) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(first);
        }
    }
}
