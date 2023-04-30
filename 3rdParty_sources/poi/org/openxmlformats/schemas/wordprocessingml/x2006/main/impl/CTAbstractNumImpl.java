/*
 * XML Type:  CT_AbstractNum
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAbstractNum
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_AbstractNum(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTAbstractNumImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAbstractNum {
    private static final long serialVersionUID = 1L;

    public CTAbstractNumImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "nsid"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "multiLevelType"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "tmpl"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "name"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "styleLink"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "numStyleLink"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "lvl"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "abstractNumId"),
    };


    /**
     * Gets the "nsid" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber getNsid() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "nsid" element
     */
    @Override
    public boolean isSetNsid() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "nsid" element
     */
    @Override
    public void setNsid(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber nsid) {
        generatedSetterHelperImpl(nsid, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "nsid" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber addNewNsid() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "nsid" element
     */
    @Override
    public void unsetNsid() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "multiLevelType" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMultiLevelType getMultiLevelType() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMultiLevelType target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMultiLevelType)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "multiLevelType" element
     */
    @Override
    public boolean isSetMultiLevelType() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "multiLevelType" element
     */
    @Override
    public void setMultiLevelType(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMultiLevelType multiLevelType) {
        generatedSetterHelperImpl(multiLevelType, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "multiLevelType" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMultiLevelType addNewMultiLevelType() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMultiLevelType target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMultiLevelType)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "multiLevelType" element
     */
    @Override
    public void unsetMultiLevelType() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "tmpl" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber getTmpl() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "tmpl" element
     */
    @Override
    public boolean isSetTmpl() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "tmpl" element
     */
    @Override
    public void setTmpl(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber tmpl) {
        generatedSetterHelperImpl(tmpl, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "tmpl" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber addNewTmpl() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "tmpl" element
     */
    @Override
    public void unsetTmpl() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "name" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString getName() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "name" element
     */
    @Override
    public boolean isSetName() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "name" element
     */
    @Override
    public void setName(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString name) {
        generatedSetterHelperImpl(name, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "name" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString addNewName() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "name" element
     */
    @Override
    public void unsetName() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "styleLink" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString getStyleLink() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "styleLink" element
     */
    @Override
    public boolean isSetStyleLink() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "styleLink" element
     */
    @Override
    public void setStyleLink(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString styleLink) {
        generatedSetterHelperImpl(styleLink, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "styleLink" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString addNewStyleLink() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "styleLink" element
     */
    @Override
    public void unsetStyleLink() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "numStyleLink" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString getNumStyleLink() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "numStyleLink" element
     */
    @Override
    public boolean isSetNumStyleLink() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "numStyleLink" element
     */
    @Override
    public void setNumStyleLink(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString numStyleLink) {
        generatedSetterHelperImpl(numStyleLink, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "numStyleLink" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString addNewNumStyleLink() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Unsets the "numStyleLink" element
     */
    @Override
    public void unsetNumStyleLink() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], 0);
        }
    }

    /**
     * Gets a List of "lvl" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLvl> getLvlList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getLvlArray,
                this::setLvlArray,
                this::insertNewLvl,
                this::removeLvl,
                this::sizeOfLvlArray
            );
        }
    }

    /**
     * Gets array of all "lvl" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLvl[] getLvlArray() {
        return getXmlObjectArray(PROPERTY_QNAME[6], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLvl[0]);
    }

    /**
     * Gets ith "lvl" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLvl getLvlArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLvl target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLvl)get_store().find_element_user(PROPERTY_QNAME[6], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "lvl" element
     */
    @Override
    public int sizeOfLvlArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]);
        }
    }

    /**
     * Sets array of all "lvl" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setLvlArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLvl[] lvlArray) {
        check_orphaned();
        arraySetterHelper(lvlArray, PROPERTY_QNAME[6]);
    }

    /**
     * Sets ith "lvl" element
     */
    @Override
    public void setLvlArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLvl lvl) {
        generatedSetterHelperImpl(lvl, PROPERTY_QNAME[6], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "lvl" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLvl insertNewLvl(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLvl target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLvl)get_store().insert_element_user(PROPERTY_QNAME[6], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "lvl" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLvl addNewLvl() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLvl target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLvl)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Removes the ith "lvl" element
     */
    @Override
    public void removeLvl(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], i);
        }
    }

    /**
     * Gets the "abstractNumId" attribute
     */
    @Override
    public java.math.BigInteger getAbstractNumId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            return (target == null) ? null : target.getBigIntegerValue();
        }
    }

    /**
     * Gets (as xml) the "abstractNumId" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber xgetAbstractNumId() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Sets the "abstractNumId" attribute
     */
    @Override
    public void setAbstractNumId(java.math.BigInteger abstractNumId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.setBigIntegerValue(abstractNumId);
        }
    }

    /**
     * Sets (as xml) the "abstractNumId" attribute
     */
    @Override
    public void xsetAbstractNumId(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber abstractNumId) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.set(abstractNumId);
        }
    }
}
