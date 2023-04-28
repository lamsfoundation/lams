/*
 * XML Type:  CT_Rst
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRst
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Rst(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTRstImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRst {
    private static final long serialVersionUID = 1L;

    public CTRstImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "t"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "r"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "rPh"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "phoneticPr"),
    };


    /**
     * Gets the "t" element
     */
    @Override
    public java.lang.String getT() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "t" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetT() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return target;
        }
    }

    /**
     * True if has "t" element
     */
    @Override
    public boolean isSetT() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "t" element
     */
    @Override
    public void setT(java.lang.String t) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[0]);
            }
            target.setStringValue(t);
        }
    }

    /**
     * Sets (as xml) the "t" element
     */
    @Override
    public void xsetT(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring t) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().add_element_user(PROPERTY_QNAME[0]);
            }
            target.set(t);
        }
    }

    /**
     * Unsets the "t" element
     */
    @Override
    public void unsetT() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets a List of "r" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRElt> getRList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getRArray,
                this::setRArray,
                this::insertNewR,
                this::removeR,
                this::sizeOfRArray
            );
        }
    }

    /**
     * Gets array of all "r" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRElt[] getRArray() {
        return getXmlObjectArray(PROPERTY_QNAME[1], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRElt[0]);
    }

    /**
     * Gets ith "r" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRElt getRArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRElt target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRElt)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "r" element
     */
    @Override
    public int sizeOfRArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets array of all "r" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setRArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRElt[] rArray) {
        check_orphaned();
        arraySetterHelper(rArray, PROPERTY_QNAME[1]);
    }

    /**
     * Sets ith "r" element
     */
    @Override
    public void setRArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRElt r) {
        generatedSetterHelperImpl(r, PROPERTY_QNAME[1], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "r" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRElt insertNewR(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRElt target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRElt)get_store().insert_element_user(PROPERTY_QNAME[1], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "r" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRElt addNewR() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRElt target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRElt)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Removes the ith "r" element
     */
    @Override
    public void removeR(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], i);
        }
    }

    /**
     * Gets a List of "rPh" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPhoneticRun> getRPhList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getRPhArray,
                this::setRPhArray,
                this::insertNewRPh,
                this::removeRPh,
                this::sizeOfRPhArray
            );
        }
    }

    /**
     * Gets array of all "rPh" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPhoneticRun[] getRPhArray() {
        return getXmlObjectArray(PROPERTY_QNAME[2], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPhoneticRun[0]);
    }

    /**
     * Gets ith "rPh" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPhoneticRun getRPhArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPhoneticRun target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPhoneticRun)get_store().find_element_user(PROPERTY_QNAME[2], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "rPh" element
     */
    @Override
    public int sizeOfRPhArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Sets array of all "rPh" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setRPhArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPhoneticRun[] rPhArray) {
        check_orphaned();
        arraySetterHelper(rPhArray, PROPERTY_QNAME[2]);
    }

    /**
     * Sets ith "rPh" element
     */
    @Override
    public void setRPhArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPhoneticRun rPh) {
        generatedSetterHelperImpl(rPh, PROPERTY_QNAME[2], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "rPh" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPhoneticRun insertNewRPh(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPhoneticRun target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPhoneticRun)get_store().insert_element_user(PROPERTY_QNAME[2], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "rPh" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPhoneticRun addNewRPh() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPhoneticRun target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPhoneticRun)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Removes the ith "rPh" element
     */
    @Override
    public void removeRPh(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], i);
        }
    }

    /**
     * Gets the "phoneticPr" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPhoneticPr getPhoneticPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPhoneticPr target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPhoneticPr)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "phoneticPr" element
     */
    @Override
    public boolean isSetPhoneticPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "phoneticPr" element
     */
    @Override
    public void setPhoneticPr(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPhoneticPr phoneticPr) {
        generatedSetterHelperImpl(phoneticPr, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "phoneticPr" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPhoneticPr addNewPhoneticPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPhoneticPr target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPhoneticPr)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "phoneticPr" element
     */
    @Override
    public void unsetPhoneticPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }
}
