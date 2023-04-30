/*
 * XML Type:  CT_Numbering
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumbering
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Numbering(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTNumberingImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumbering {
    private static final long serialVersionUID = 1L;

    public CTNumberingImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "numPicBullet"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "abstractNum"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "num"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "numIdMacAtCleanup"),
    };


    /**
     * Gets a List of "numPicBullet" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumPicBullet> getNumPicBulletList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getNumPicBulletArray,
                this::setNumPicBulletArray,
                this::insertNewNumPicBullet,
                this::removeNumPicBullet,
                this::sizeOfNumPicBulletArray
            );
        }
    }

    /**
     * Gets array of all "numPicBullet" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumPicBullet[] getNumPicBulletArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumPicBullet[0]);
    }

    /**
     * Gets ith "numPicBullet" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumPicBullet getNumPicBulletArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumPicBullet target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumPicBullet)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "numPicBullet" element
     */
    @Override
    public int sizeOfNumPicBulletArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "numPicBullet" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setNumPicBulletArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumPicBullet[] numPicBulletArray) {
        check_orphaned();
        arraySetterHelper(numPicBulletArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "numPicBullet" element
     */
    @Override
    public void setNumPicBulletArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumPicBullet numPicBullet) {
        generatedSetterHelperImpl(numPicBullet, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "numPicBullet" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumPicBullet insertNewNumPicBullet(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumPicBullet target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumPicBullet)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "numPicBullet" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumPicBullet addNewNumPicBullet() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumPicBullet target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumPicBullet)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "numPicBullet" element
     */
    @Override
    public void removeNumPicBullet(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets a List of "abstractNum" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAbstractNum> getAbstractNumList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getAbstractNumArray,
                this::setAbstractNumArray,
                this::insertNewAbstractNum,
                this::removeAbstractNum,
                this::sizeOfAbstractNumArray
            );
        }
    }

    /**
     * Gets array of all "abstractNum" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAbstractNum[] getAbstractNumArray() {
        return getXmlObjectArray(PROPERTY_QNAME[1], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAbstractNum[0]);
    }

    /**
     * Gets ith "abstractNum" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAbstractNum getAbstractNumArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAbstractNum target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAbstractNum)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "abstractNum" element
     */
    @Override
    public int sizeOfAbstractNumArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets array of all "abstractNum" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setAbstractNumArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAbstractNum[] abstractNumArray) {
        check_orphaned();
        arraySetterHelper(abstractNumArray, PROPERTY_QNAME[1]);
    }

    /**
     * Sets ith "abstractNum" element
     */
    @Override
    public void setAbstractNumArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAbstractNum abstractNum) {
        generatedSetterHelperImpl(abstractNum, PROPERTY_QNAME[1], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "abstractNum" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAbstractNum insertNewAbstractNum(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAbstractNum target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAbstractNum)get_store().insert_element_user(PROPERTY_QNAME[1], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "abstractNum" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAbstractNum addNewAbstractNum() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAbstractNum target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAbstractNum)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Removes the ith "abstractNum" element
     */
    @Override
    public void removeAbstractNum(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], i);
        }
    }

    /**
     * Gets a List of "num" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNum> getNumList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getNumArray,
                this::setNumArray,
                this::insertNewNum,
                this::removeNum,
                this::sizeOfNumArray
            );
        }
    }

    /**
     * Gets array of all "num" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNum[] getNumArray() {
        return getXmlObjectArray(PROPERTY_QNAME[2], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNum[0]);
    }

    /**
     * Gets ith "num" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNum getNumArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNum target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNum)get_store().find_element_user(PROPERTY_QNAME[2], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "num" element
     */
    @Override
    public int sizeOfNumArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Sets array of all "num" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setNumArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNum[] numArray) {
        check_orphaned();
        arraySetterHelper(numArray, PROPERTY_QNAME[2]);
    }

    /**
     * Sets ith "num" element
     */
    @Override
    public void setNumArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNum num) {
        generatedSetterHelperImpl(num, PROPERTY_QNAME[2], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "num" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNum insertNewNum(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNum target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNum)get_store().insert_element_user(PROPERTY_QNAME[2], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "num" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNum addNewNum() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNum target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNum)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Removes the ith "num" element
     */
    @Override
    public void removeNum(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], i);
        }
    }

    /**
     * Gets the "numIdMacAtCleanup" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber getNumIdMacAtCleanup() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "numIdMacAtCleanup" element
     */
    @Override
    public boolean isSetNumIdMacAtCleanup() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "numIdMacAtCleanup" element
     */
    @Override
    public void setNumIdMacAtCleanup(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber numIdMacAtCleanup) {
        generatedSetterHelperImpl(numIdMacAtCleanup, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "numIdMacAtCleanup" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber addNewNumIdMacAtCleanup() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "numIdMacAtCleanup" element
     */
    @Override
    public void unsetNumIdMacAtCleanup() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }
}
