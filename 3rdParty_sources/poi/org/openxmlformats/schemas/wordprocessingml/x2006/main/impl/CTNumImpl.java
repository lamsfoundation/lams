/*
 * XML Type:  CT_Num
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNum
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Num(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTNumImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNum {
    private static final long serialVersionUID = 1L;

    public CTNumImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "abstractNumId"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "lvlOverride"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "numId"),
    };


    /**
     * Gets the "abstractNumId" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber getAbstractNumId() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "abstractNumId" element
     */
    @Override
    public void setAbstractNumId(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber abstractNumId) {
        generatedSetterHelperImpl(abstractNumId, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "abstractNumId" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber addNewAbstractNumId() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Gets a List of "lvlOverride" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumLvl> getLvlOverrideList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getLvlOverrideArray,
                this::setLvlOverrideArray,
                this::insertNewLvlOverride,
                this::removeLvlOverride,
                this::sizeOfLvlOverrideArray
            );
        }
    }

    /**
     * Gets array of all "lvlOverride" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumLvl[] getLvlOverrideArray() {
        return getXmlObjectArray(PROPERTY_QNAME[1], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumLvl[0]);
    }

    /**
     * Gets ith "lvlOverride" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumLvl getLvlOverrideArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumLvl target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumLvl)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "lvlOverride" element
     */
    @Override
    public int sizeOfLvlOverrideArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets array of all "lvlOverride" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setLvlOverrideArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumLvl[] lvlOverrideArray) {
        check_orphaned();
        arraySetterHelper(lvlOverrideArray, PROPERTY_QNAME[1]);
    }

    /**
     * Sets ith "lvlOverride" element
     */
    @Override
    public void setLvlOverrideArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumLvl lvlOverride) {
        generatedSetterHelperImpl(lvlOverride, PROPERTY_QNAME[1], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "lvlOverride" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumLvl insertNewLvlOverride(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumLvl target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumLvl)get_store().insert_element_user(PROPERTY_QNAME[1], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "lvlOverride" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumLvl addNewLvlOverride() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumLvl target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumLvl)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Removes the ith "lvlOverride" element
     */
    @Override
    public void removeLvlOverride(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], i);
        }
    }

    /**
     * Gets the "numId" attribute
     */
    @Override
    public java.math.BigInteger getNumId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return (target == null) ? null : target.getBigIntegerValue();
        }
    }

    /**
     * Gets (as xml) the "numId" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber xgetNumId() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Sets the "numId" attribute
     */
    @Override
    public void setNumId(java.math.BigInteger numId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setBigIntegerValue(numId);
        }
    }

    /**
     * Sets (as xml) the "numId" attribute
     */
    @Override
    public void xsetNumId(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber numId) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(numId);
        }
    }
}
