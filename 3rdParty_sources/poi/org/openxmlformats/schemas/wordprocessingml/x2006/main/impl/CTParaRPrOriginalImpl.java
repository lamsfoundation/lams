/*
 * XML Type:  CT_ParaRPrOriginal
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTParaRPrOriginal
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_ParaRPrOriginal(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTParaRPrOriginalImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTParaRPrOriginal {
    private static final long serialVersionUID = 1L;

    public CTParaRPrOriginalImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "ins"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "del"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "moveFrom"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "moveTo"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "rStyle"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "rFonts"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "b"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "bCs"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "i"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "iCs"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "caps"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "smallCaps"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "strike"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "dstrike"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "outline"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "shadow"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "emboss"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "imprint"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "noProof"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "snapToGrid"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "vanish"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "webHidden"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "color"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "spacing"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "w"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "kern"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "position"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "sz"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "szCs"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "highlight"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "u"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "effect"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "bdr"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "shd"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "fitText"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "vertAlign"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "rtl"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "cs"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "em"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "lang"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "eastAsianLayout"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "specVanish"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "oMath"),
    };


    /**
     * Gets the "ins" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange getIns() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "ins" element
     */
    @Override
    public boolean isSetIns() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "ins" element
     */
    @Override
    public void setIns(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange ins) {
        generatedSetterHelperImpl(ins, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "ins" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange addNewIns() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "ins" element
     */
    @Override
    public void unsetIns() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "del" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange getDel() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "del" element
     */
    @Override
    public boolean isSetDel() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "del" element
     */
    @Override
    public void setDel(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange del) {
        generatedSetterHelperImpl(del, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "del" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange addNewDel() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "del" element
     */
    @Override
    public void unsetDel() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "moveFrom" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange getMoveFrom() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "moveFrom" element
     */
    @Override
    public boolean isSetMoveFrom() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "moveFrom" element
     */
    @Override
    public void setMoveFrom(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange moveFrom) {
        generatedSetterHelperImpl(moveFrom, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "moveFrom" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange addNewMoveFrom() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "moveFrom" element
     */
    @Override
    public void unsetMoveFrom() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "moveTo" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange getMoveTo() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "moveTo" element
     */
    @Override
    public boolean isSetMoveTo() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "moveTo" element
     */
    @Override
    public void setMoveTo(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange moveTo) {
        generatedSetterHelperImpl(moveTo, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "moveTo" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange addNewMoveTo() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "moveTo" element
     */
    @Override
    public void unsetMoveTo() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets a List of "rStyle" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString> getRStyleList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getRStyleArray,
                this::setRStyleArray,
                this::insertNewRStyle,
                this::removeRStyle,
                this::sizeOfRStyleArray
            );
        }
    }

    /**
     * Gets array of all "rStyle" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString[] getRStyleArray() {
        return getXmlObjectArray(PROPERTY_QNAME[4], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString[0]);
    }

    /**
     * Gets ith "rStyle" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString getRStyleArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().find_element_user(PROPERTY_QNAME[4], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "rStyle" element
     */
    @Override
    public int sizeOfRStyleArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Sets array of all "rStyle" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setRStyleArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString[] rStyleArray) {
        check_orphaned();
        arraySetterHelper(rStyleArray, PROPERTY_QNAME[4]);
    }

    /**
     * Sets ith "rStyle" element
     */
    @Override
    public void setRStyleArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString rStyle) {
        generatedSetterHelperImpl(rStyle, PROPERTY_QNAME[4], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "rStyle" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString insertNewRStyle(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().insert_element_user(PROPERTY_QNAME[4], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "rStyle" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString addNewRStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Removes the ith "rStyle" element
     */
    @Override
    public void removeRStyle(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], i);
        }
    }

    /**
     * Gets a List of "rFonts" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts> getRFontsList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getRFontsArray,
                this::setRFontsArray,
                this::insertNewRFonts,
                this::removeRFonts,
                this::sizeOfRFontsArray
            );
        }
    }

    /**
     * Gets array of all "rFonts" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts[] getRFontsArray() {
        return getXmlObjectArray(PROPERTY_QNAME[5], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts[0]);
    }

    /**
     * Gets ith "rFonts" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts getRFontsArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts)get_store().find_element_user(PROPERTY_QNAME[5], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "rFonts" element
     */
    @Override
    public int sizeOfRFontsArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Sets array of all "rFonts" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setRFontsArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts[] rFontsArray) {
        check_orphaned();
        arraySetterHelper(rFontsArray, PROPERTY_QNAME[5]);
    }

    /**
     * Sets ith "rFonts" element
     */
    @Override
    public void setRFontsArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts rFonts) {
        generatedSetterHelperImpl(rFonts, PROPERTY_QNAME[5], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "rFonts" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts insertNewRFonts(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts)get_store().insert_element_user(PROPERTY_QNAME[5], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "rFonts" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts addNewRFonts() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Removes the ith "rFonts" element
     */
    @Override
    public void removeRFonts(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], i);
        }
    }

    /**
     * Gets a List of "b" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff> getBList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getBArray,
                this::setBArray,
                this::insertNewB,
                this::removeB,
                this::sizeOfBArray
            );
        }
    }

    /**
     * Gets array of all "b" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] getBArray() {
        return getXmlObjectArray(PROPERTY_QNAME[6], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[0]);
    }

    /**
     * Gets ith "b" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getBArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[6], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "b" element
     */
    @Override
    public int sizeOfBArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]);
        }
    }

    /**
     * Sets array of all "b" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setBArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] bArray) {
        check_orphaned();
        arraySetterHelper(bArray, PROPERTY_QNAME[6]);
    }

    /**
     * Sets ith "b" element
     */
    @Override
    public void setBArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff b) {
        generatedSetterHelperImpl(b, PROPERTY_QNAME[6], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "b" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff insertNewB(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().insert_element_user(PROPERTY_QNAME[6], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "b" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewB() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Removes the ith "b" element
     */
    @Override
    public void removeB(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], i);
        }
    }

    /**
     * Gets a List of "bCs" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff> getBCsList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getBCsArray,
                this::setBCsArray,
                this::insertNewBCs,
                this::removeBCs,
                this::sizeOfBCsArray
            );
        }
    }

    /**
     * Gets array of all "bCs" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] getBCsArray() {
        return getXmlObjectArray(PROPERTY_QNAME[7], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[0]);
    }

    /**
     * Gets ith "bCs" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getBCsArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[7], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "bCs" element
     */
    @Override
    public int sizeOfBCsArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]);
        }
    }

    /**
     * Sets array of all "bCs" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setBCsArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] bCsArray) {
        check_orphaned();
        arraySetterHelper(bCsArray, PROPERTY_QNAME[7]);
    }

    /**
     * Sets ith "bCs" element
     */
    @Override
    public void setBCsArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff bCs) {
        generatedSetterHelperImpl(bCs, PROPERTY_QNAME[7], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "bCs" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff insertNewBCs(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().insert_element_user(PROPERTY_QNAME[7], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "bCs" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewBCs() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Removes the ith "bCs" element
     */
    @Override
    public void removeBCs(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[7], i);
        }
    }

    /**
     * Gets a List of "i" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff> getIList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getIArray,
                this::setIArray,
                this::insertNewI,
                this::removeI,
                this::sizeOfIArray
            );
        }
    }

    /**
     * Gets array of all "i" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] getIArray() {
        return getXmlObjectArray(PROPERTY_QNAME[8], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[0]);
    }

    /**
     * Gets ith "i" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getIArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[8], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "i" element
     */
    @Override
    public int sizeOfIArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[8]);
        }
    }

    /**
     * Sets array of all "i" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setIArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] iValueArray) {
        check_orphaned();
        arraySetterHelper(iValueArray, PROPERTY_QNAME[8]);
    }

    /**
     * Sets ith "i" element
     */
    @Override
    public void setIArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff iValue) {
        generatedSetterHelperImpl(iValue, PROPERTY_QNAME[8], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "i" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff insertNewI(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().insert_element_user(PROPERTY_QNAME[8], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "i" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewI() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * Removes the ith "i" element
     */
    @Override
    public void removeI(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[8], i);
        }
    }

    /**
     * Gets a List of "iCs" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff> getICsList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getICsArray,
                this::setICsArray,
                this::insertNewICs,
                this::removeICs,
                this::sizeOfICsArray
            );
        }
    }

    /**
     * Gets array of all "iCs" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] getICsArray() {
        return getXmlObjectArray(PROPERTY_QNAME[9], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[0]);
    }

    /**
     * Gets ith "iCs" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getICsArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[9], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "iCs" element
     */
    @Override
    public int sizeOfICsArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[9]);
        }
    }

    /**
     * Sets array of all "iCs" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setICsArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] iCsArray) {
        check_orphaned();
        arraySetterHelper(iCsArray, PROPERTY_QNAME[9]);
    }

    /**
     * Sets ith "iCs" element
     */
    @Override
    public void setICsArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff iCs) {
        generatedSetterHelperImpl(iCs, PROPERTY_QNAME[9], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "iCs" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff insertNewICs(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().insert_element_user(PROPERTY_QNAME[9], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "iCs" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewICs() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[9]);
            return target;
        }
    }

    /**
     * Removes the ith "iCs" element
     */
    @Override
    public void removeICs(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[9], i);
        }
    }

    /**
     * Gets a List of "caps" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff> getCapsList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCapsArray,
                this::setCapsArray,
                this::insertNewCaps,
                this::removeCaps,
                this::sizeOfCapsArray
            );
        }
    }

    /**
     * Gets array of all "caps" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] getCapsArray() {
        return getXmlObjectArray(PROPERTY_QNAME[10], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[0]);
    }

    /**
     * Gets ith "caps" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getCapsArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[10], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "caps" element
     */
    @Override
    public int sizeOfCapsArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[10]);
        }
    }

    /**
     * Sets array of all "caps" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCapsArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] capsArray) {
        check_orphaned();
        arraySetterHelper(capsArray, PROPERTY_QNAME[10]);
    }

    /**
     * Sets ith "caps" element
     */
    @Override
    public void setCapsArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff caps) {
        generatedSetterHelperImpl(caps, PROPERTY_QNAME[10], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "caps" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff insertNewCaps(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().insert_element_user(PROPERTY_QNAME[10], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "caps" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewCaps() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[10]);
            return target;
        }
    }

    /**
     * Removes the ith "caps" element
     */
    @Override
    public void removeCaps(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[10], i);
        }
    }

    /**
     * Gets a List of "smallCaps" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff> getSmallCapsList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getSmallCapsArray,
                this::setSmallCapsArray,
                this::insertNewSmallCaps,
                this::removeSmallCaps,
                this::sizeOfSmallCapsArray
            );
        }
    }

    /**
     * Gets array of all "smallCaps" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] getSmallCapsArray() {
        return getXmlObjectArray(PROPERTY_QNAME[11], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[0]);
    }

    /**
     * Gets ith "smallCaps" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getSmallCapsArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[11], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "smallCaps" element
     */
    @Override
    public int sizeOfSmallCapsArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[11]);
        }
    }

    /**
     * Sets array of all "smallCaps" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSmallCapsArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] smallCapsArray) {
        check_orphaned();
        arraySetterHelper(smallCapsArray, PROPERTY_QNAME[11]);
    }

    /**
     * Sets ith "smallCaps" element
     */
    @Override
    public void setSmallCapsArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff smallCaps) {
        generatedSetterHelperImpl(smallCaps, PROPERTY_QNAME[11], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "smallCaps" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff insertNewSmallCaps(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().insert_element_user(PROPERTY_QNAME[11], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "smallCaps" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewSmallCaps() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[11]);
            return target;
        }
    }

    /**
     * Removes the ith "smallCaps" element
     */
    @Override
    public void removeSmallCaps(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[11], i);
        }
    }

    /**
     * Gets a List of "strike" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff> getStrikeList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getStrikeArray,
                this::setStrikeArray,
                this::insertNewStrike,
                this::removeStrike,
                this::sizeOfStrikeArray
            );
        }
    }

    /**
     * Gets array of all "strike" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] getStrikeArray() {
        return getXmlObjectArray(PROPERTY_QNAME[12], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[0]);
    }

    /**
     * Gets ith "strike" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getStrikeArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[12], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "strike" element
     */
    @Override
    public int sizeOfStrikeArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[12]);
        }
    }

    /**
     * Sets array of all "strike" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setStrikeArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] strikeArray) {
        check_orphaned();
        arraySetterHelper(strikeArray, PROPERTY_QNAME[12]);
    }

    /**
     * Sets ith "strike" element
     */
    @Override
    public void setStrikeArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff strike) {
        generatedSetterHelperImpl(strike, PROPERTY_QNAME[12], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "strike" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff insertNewStrike(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().insert_element_user(PROPERTY_QNAME[12], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "strike" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewStrike() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[12]);
            return target;
        }
    }

    /**
     * Removes the ith "strike" element
     */
    @Override
    public void removeStrike(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[12], i);
        }
    }

    /**
     * Gets a List of "dstrike" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff> getDstrikeList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getDstrikeArray,
                this::setDstrikeArray,
                this::insertNewDstrike,
                this::removeDstrike,
                this::sizeOfDstrikeArray
            );
        }
    }

    /**
     * Gets array of all "dstrike" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] getDstrikeArray() {
        return getXmlObjectArray(PROPERTY_QNAME[13], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[0]);
    }

    /**
     * Gets ith "dstrike" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getDstrikeArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[13], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "dstrike" element
     */
    @Override
    public int sizeOfDstrikeArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[13]);
        }
    }

    /**
     * Sets array of all "dstrike" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setDstrikeArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] dstrikeArray) {
        check_orphaned();
        arraySetterHelper(dstrikeArray, PROPERTY_QNAME[13]);
    }

    /**
     * Sets ith "dstrike" element
     */
    @Override
    public void setDstrikeArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff dstrike) {
        generatedSetterHelperImpl(dstrike, PROPERTY_QNAME[13], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "dstrike" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff insertNewDstrike(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().insert_element_user(PROPERTY_QNAME[13], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "dstrike" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewDstrike() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[13]);
            return target;
        }
    }

    /**
     * Removes the ith "dstrike" element
     */
    @Override
    public void removeDstrike(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[13], i);
        }
    }

    /**
     * Gets a List of "outline" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff> getOutlineList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getOutlineArray,
                this::setOutlineArray,
                this::insertNewOutline,
                this::removeOutline,
                this::sizeOfOutlineArray
            );
        }
    }

    /**
     * Gets array of all "outline" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] getOutlineArray() {
        return getXmlObjectArray(PROPERTY_QNAME[14], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[0]);
    }

    /**
     * Gets ith "outline" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getOutlineArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[14], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "outline" element
     */
    @Override
    public int sizeOfOutlineArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[14]);
        }
    }

    /**
     * Sets array of all "outline" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setOutlineArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] outlineArray) {
        check_orphaned();
        arraySetterHelper(outlineArray, PROPERTY_QNAME[14]);
    }

    /**
     * Sets ith "outline" element
     */
    @Override
    public void setOutlineArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff outline) {
        generatedSetterHelperImpl(outline, PROPERTY_QNAME[14], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "outline" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff insertNewOutline(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().insert_element_user(PROPERTY_QNAME[14], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "outline" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewOutline() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[14]);
            return target;
        }
    }

    /**
     * Removes the ith "outline" element
     */
    @Override
    public void removeOutline(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[14], i);
        }
    }

    /**
     * Gets a List of "shadow" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff> getShadowList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getShadowArray,
                this::setShadowArray,
                this::insertNewShadow,
                this::removeShadow,
                this::sizeOfShadowArray
            );
        }
    }

    /**
     * Gets array of all "shadow" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] getShadowArray() {
        return getXmlObjectArray(PROPERTY_QNAME[15], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[0]);
    }

    /**
     * Gets ith "shadow" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getShadowArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[15], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "shadow" element
     */
    @Override
    public int sizeOfShadowArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[15]);
        }
    }

    /**
     * Sets array of all "shadow" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setShadowArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] shadowArray) {
        check_orphaned();
        arraySetterHelper(shadowArray, PROPERTY_QNAME[15]);
    }

    /**
     * Sets ith "shadow" element
     */
    @Override
    public void setShadowArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff shadow) {
        generatedSetterHelperImpl(shadow, PROPERTY_QNAME[15], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "shadow" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff insertNewShadow(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().insert_element_user(PROPERTY_QNAME[15], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "shadow" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewShadow() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[15]);
            return target;
        }
    }

    /**
     * Removes the ith "shadow" element
     */
    @Override
    public void removeShadow(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[15], i);
        }
    }

    /**
     * Gets a List of "emboss" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff> getEmbossList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getEmbossArray,
                this::setEmbossArray,
                this::insertNewEmboss,
                this::removeEmboss,
                this::sizeOfEmbossArray
            );
        }
    }

    /**
     * Gets array of all "emboss" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] getEmbossArray() {
        return getXmlObjectArray(PROPERTY_QNAME[16], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[0]);
    }

    /**
     * Gets ith "emboss" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getEmbossArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[16], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "emboss" element
     */
    @Override
    public int sizeOfEmbossArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[16]);
        }
    }

    /**
     * Sets array of all "emboss" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setEmbossArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] embossArray) {
        check_orphaned();
        arraySetterHelper(embossArray, PROPERTY_QNAME[16]);
    }

    /**
     * Sets ith "emboss" element
     */
    @Override
    public void setEmbossArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff emboss) {
        generatedSetterHelperImpl(emboss, PROPERTY_QNAME[16], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "emboss" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff insertNewEmboss(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().insert_element_user(PROPERTY_QNAME[16], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "emboss" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewEmboss() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[16]);
            return target;
        }
    }

    /**
     * Removes the ith "emboss" element
     */
    @Override
    public void removeEmboss(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[16], i);
        }
    }

    /**
     * Gets a List of "imprint" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff> getImprintList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getImprintArray,
                this::setImprintArray,
                this::insertNewImprint,
                this::removeImprint,
                this::sizeOfImprintArray
            );
        }
    }

    /**
     * Gets array of all "imprint" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] getImprintArray() {
        return getXmlObjectArray(PROPERTY_QNAME[17], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[0]);
    }

    /**
     * Gets ith "imprint" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getImprintArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[17], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "imprint" element
     */
    @Override
    public int sizeOfImprintArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[17]);
        }
    }

    /**
     * Sets array of all "imprint" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setImprintArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] imprintArray) {
        check_orphaned();
        arraySetterHelper(imprintArray, PROPERTY_QNAME[17]);
    }

    /**
     * Sets ith "imprint" element
     */
    @Override
    public void setImprintArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff imprint) {
        generatedSetterHelperImpl(imprint, PROPERTY_QNAME[17], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "imprint" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff insertNewImprint(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().insert_element_user(PROPERTY_QNAME[17], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "imprint" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewImprint() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[17]);
            return target;
        }
    }

    /**
     * Removes the ith "imprint" element
     */
    @Override
    public void removeImprint(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[17], i);
        }
    }

    /**
     * Gets a List of "noProof" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff> getNoProofList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getNoProofArray,
                this::setNoProofArray,
                this::insertNewNoProof,
                this::removeNoProof,
                this::sizeOfNoProofArray
            );
        }
    }

    /**
     * Gets array of all "noProof" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] getNoProofArray() {
        return getXmlObjectArray(PROPERTY_QNAME[18], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[0]);
    }

    /**
     * Gets ith "noProof" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getNoProofArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[18], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "noProof" element
     */
    @Override
    public int sizeOfNoProofArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[18]);
        }
    }

    /**
     * Sets array of all "noProof" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setNoProofArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] noProofArray) {
        check_orphaned();
        arraySetterHelper(noProofArray, PROPERTY_QNAME[18]);
    }

    /**
     * Sets ith "noProof" element
     */
    @Override
    public void setNoProofArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff noProof) {
        generatedSetterHelperImpl(noProof, PROPERTY_QNAME[18], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "noProof" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff insertNewNoProof(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().insert_element_user(PROPERTY_QNAME[18], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "noProof" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewNoProof() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[18]);
            return target;
        }
    }

    /**
     * Removes the ith "noProof" element
     */
    @Override
    public void removeNoProof(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[18], i);
        }
    }

    /**
     * Gets a List of "snapToGrid" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff> getSnapToGridList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getSnapToGridArray,
                this::setSnapToGridArray,
                this::insertNewSnapToGrid,
                this::removeSnapToGrid,
                this::sizeOfSnapToGridArray
            );
        }
    }

    /**
     * Gets array of all "snapToGrid" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] getSnapToGridArray() {
        return getXmlObjectArray(PROPERTY_QNAME[19], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[0]);
    }

    /**
     * Gets ith "snapToGrid" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getSnapToGridArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[19], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "snapToGrid" element
     */
    @Override
    public int sizeOfSnapToGridArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[19]);
        }
    }

    /**
     * Sets array of all "snapToGrid" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSnapToGridArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] snapToGridArray) {
        check_orphaned();
        arraySetterHelper(snapToGridArray, PROPERTY_QNAME[19]);
    }

    /**
     * Sets ith "snapToGrid" element
     */
    @Override
    public void setSnapToGridArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff snapToGrid) {
        generatedSetterHelperImpl(snapToGrid, PROPERTY_QNAME[19], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "snapToGrid" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff insertNewSnapToGrid(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().insert_element_user(PROPERTY_QNAME[19], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "snapToGrid" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewSnapToGrid() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[19]);
            return target;
        }
    }

    /**
     * Removes the ith "snapToGrid" element
     */
    @Override
    public void removeSnapToGrid(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[19], i);
        }
    }

    /**
     * Gets a List of "vanish" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff> getVanishList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getVanishArray,
                this::setVanishArray,
                this::insertNewVanish,
                this::removeVanish,
                this::sizeOfVanishArray
            );
        }
    }

    /**
     * Gets array of all "vanish" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] getVanishArray() {
        return getXmlObjectArray(PROPERTY_QNAME[20], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[0]);
    }

    /**
     * Gets ith "vanish" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getVanishArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[20], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "vanish" element
     */
    @Override
    public int sizeOfVanishArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[20]);
        }
    }

    /**
     * Sets array of all "vanish" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setVanishArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] vanishArray) {
        check_orphaned();
        arraySetterHelper(vanishArray, PROPERTY_QNAME[20]);
    }

    /**
     * Sets ith "vanish" element
     */
    @Override
    public void setVanishArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff vanish) {
        generatedSetterHelperImpl(vanish, PROPERTY_QNAME[20], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "vanish" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff insertNewVanish(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().insert_element_user(PROPERTY_QNAME[20], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "vanish" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewVanish() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[20]);
            return target;
        }
    }

    /**
     * Removes the ith "vanish" element
     */
    @Override
    public void removeVanish(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[20], i);
        }
    }

    /**
     * Gets a List of "webHidden" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff> getWebHiddenList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getWebHiddenArray,
                this::setWebHiddenArray,
                this::insertNewWebHidden,
                this::removeWebHidden,
                this::sizeOfWebHiddenArray
            );
        }
    }

    /**
     * Gets array of all "webHidden" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] getWebHiddenArray() {
        return getXmlObjectArray(PROPERTY_QNAME[21], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[0]);
    }

    /**
     * Gets ith "webHidden" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getWebHiddenArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[21], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "webHidden" element
     */
    @Override
    public int sizeOfWebHiddenArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[21]);
        }
    }

    /**
     * Sets array of all "webHidden" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setWebHiddenArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] webHiddenArray) {
        check_orphaned();
        arraySetterHelper(webHiddenArray, PROPERTY_QNAME[21]);
    }

    /**
     * Sets ith "webHidden" element
     */
    @Override
    public void setWebHiddenArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff webHidden) {
        generatedSetterHelperImpl(webHidden, PROPERTY_QNAME[21], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "webHidden" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff insertNewWebHidden(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().insert_element_user(PROPERTY_QNAME[21], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "webHidden" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewWebHidden() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[21]);
            return target;
        }
    }

    /**
     * Removes the ith "webHidden" element
     */
    @Override
    public void removeWebHidden(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[21], i);
        }
    }

    /**
     * Gets a List of "color" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColor> getColorList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getColorArray,
                this::setColorArray,
                this::insertNewColor,
                this::removeColor,
                this::sizeOfColorArray
            );
        }
    }

    /**
     * Gets array of all "color" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColor[] getColorArray() {
        return getXmlObjectArray(PROPERTY_QNAME[22], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColor[0]);
    }

    /**
     * Gets ith "color" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColor getColorArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColor target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColor)get_store().find_element_user(PROPERTY_QNAME[22], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "color" element
     */
    @Override
    public int sizeOfColorArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[22]);
        }
    }

    /**
     * Sets array of all "color" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setColorArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColor[] colorArray) {
        check_orphaned();
        arraySetterHelper(colorArray, PROPERTY_QNAME[22]);
    }

    /**
     * Sets ith "color" element
     */
    @Override
    public void setColorArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColor color) {
        generatedSetterHelperImpl(color, PROPERTY_QNAME[22], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "color" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColor insertNewColor(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColor target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColor)get_store().insert_element_user(PROPERTY_QNAME[22], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "color" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColor addNewColor() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColor target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColor)get_store().add_element_user(PROPERTY_QNAME[22]);
            return target;
        }
    }

    /**
     * Removes the ith "color" element
     */
    @Override
    public void removeColor(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[22], i);
        }
    }

    /**
     * Gets a List of "spacing" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedTwipsMeasure> getSpacingList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getSpacingArray,
                this::setSpacingArray,
                this::insertNewSpacing,
                this::removeSpacing,
                this::sizeOfSpacingArray
            );
        }
    }

    /**
     * Gets array of all "spacing" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedTwipsMeasure[] getSpacingArray() {
        return getXmlObjectArray(PROPERTY_QNAME[23], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedTwipsMeasure[0]);
    }

    /**
     * Gets ith "spacing" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedTwipsMeasure getSpacingArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedTwipsMeasure)get_store().find_element_user(PROPERTY_QNAME[23], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "spacing" element
     */
    @Override
    public int sizeOfSpacingArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[23]);
        }
    }

    /**
     * Sets array of all "spacing" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSpacingArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedTwipsMeasure[] spacingArray) {
        check_orphaned();
        arraySetterHelper(spacingArray, PROPERTY_QNAME[23]);
    }

    /**
     * Sets ith "spacing" element
     */
    @Override
    public void setSpacingArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedTwipsMeasure spacing) {
        generatedSetterHelperImpl(spacing, PROPERTY_QNAME[23], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "spacing" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedTwipsMeasure insertNewSpacing(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedTwipsMeasure)get_store().insert_element_user(PROPERTY_QNAME[23], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "spacing" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedTwipsMeasure addNewSpacing() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedTwipsMeasure)get_store().add_element_user(PROPERTY_QNAME[23]);
            return target;
        }
    }

    /**
     * Removes the ith "spacing" element
     */
    @Override
    public void removeSpacing(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[23], i);
        }
    }

    /**
     * Gets a List of "w" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextScale> getWList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getWArray,
                this::setWArray,
                this::insertNewW,
                this::removeW,
                this::sizeOfWArray
            );
        }
    }

    /**
     * Gets array of all "w" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextScale[] getWArray() {
        return getXmlObjectArray(PROPERTY_QNAME[24], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextScale[0]);
    }

    /**
     * Gets ith "w" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextScale getWArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextScale target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextScale)get_store().find_element_user(PROPERTY_QNAME[24], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "w" element
     */
    @Override
    public int sizeOfWArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[24]);
        }
    }

    /**
     * Sets array of all "w" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setWArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextScale[] wArray) {
        check_orphaned();
        arraySetterHelper(wArray, PROPERTY_QNAME[24]);
    }

    /**
     * Sets ith "w" element
     */
    @Override
    public void setWArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextScale w) {
        generatedSetterHelperImpl(w, PROPERTY_QNAME[24], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "w" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextScale insertNewW(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextScale target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextScale)get_store().insert_element_user(PROPERTY_QNAME[24], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "w" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextScale addNewW() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextScale target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextScale)get_store().add_element_user(PROPERTY_QNAME[24]);
            return target;
        }
    }

    /**
     * Removes the ith "w" element
     */
    @Override
    public void removeW(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[24], i);
        }
    }

    /**
     * Gets a List of "kern" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure> getKernList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getKernArray,
                this::setKernArray,
                this::insertNewKern,
                this::removeKern,
                this::sizeOfKernArray
            );
        }
    }

    /**
     * Gets array of all "kern" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure[] getKernArray() {
        return getXmlObjectArray(PROPERTY_QNAME[25], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure[0]);
    }

    /**
     * Gets ith "kern" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure getKernArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure)get_store().find_element_user(PROPERTY_QNAME[25], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "kern" element
     */
    @Override
    public int sizeOfKernArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[25]);
        }
    }

    /**
     * Sets array of all "kern" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setKernArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure[] kernArray) {
        check_orphaned();
        arraySetterHelper(kernArray, PROPERTY_QNAME[25]);
    }

    /**
     * Sets ith "kern" element
     */
    @Override
    public void setKernArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure kern) {
        generatedSetterHelperImpl(kern, PROPERTY_QNAME[25], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "kern" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure insertNewKern(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure)get_store().insert_element_user(PROPERTY_QNAME[25], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "kern" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure addNewKern() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure)get_store().add_element_user(PROPERTY_QNAME[25]);
            return target;
        }
    }

    /**
     * Removes the ith "kern" element
     */
    @Override
    public void removeKern(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[25], i);
        }
    }

    /**
     * Gets a List of "position" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedHpsMeasure> getPositionList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getPositionArray,
                this::setPositionArray,
                this::insertNewPosition,
                this::removePosition,
                this::sizeOfPositionArray
            );
        }
    }

    /**
     * Gets array of all "position" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedHpsMeasure[] getPositionArray() {
        return getXmlObjectArray(PROPERTY_QNAME[26], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedHpsMeasure[0]);
    }

    /**
     * Gets ith "position" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedHpsMeasure getPositionArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedHpsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedHpsMeasure)get_store().find_element_user(PROPERTY_QNAME[26], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "position" element
     */
    @Override
    public int sizeOfPositionArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[26]);
        }
    }

    /**
     * Sets array of all "position" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setPositionArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedHpsMeasure[] positionArray) {
        check_orphaned();
        arraySetterHelper(positionArray, PROPERTY_QNAME[26]);
    }

    /**
     * Sets ith "position" element
     */
    @Override
    public void setPositionArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedHpsMeasure position) {
        generatedSetterHelperImpl(position, PROPERTY_QNAME[26], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "position" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedHpsMeasure insertNewPosition(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedHpsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedHpsMeasure)get_store().insert_element_user(PROPERTY_QNAME[26], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "position" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedHpsMeasure addNewPosition() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedHpsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedHpsMeasure)get_store().add_element_user(PROPERTY_QNAME[26]);
            return target;
        }
    }

    /**
     * Removes the ith "position" element
     */
    @Override
    public void removePosition(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[26], i);
        }
    }

    /**
     * Gets a List of "sz" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure> getSzList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getSzArray,
                this::setSzArray,
                this::insertNewSz,
                this::removeSz,
                this::sizeOfSzArray
            );
        }
    }

    /**
     * Gets array of all "sz" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure[] getSzArray() {
        return getXmlObjectArray(PROPERTY_QNAME[27], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure[0]);
    }

    /**
     * Gets ith "sz" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure getSzArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure)get_store().find_element_user(PROPERTY_QNAME[27], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "sz" element
     */
    @Override
    public int sizeOfSzArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[27]);
        }
    }

    /**
     * Sets array of all "sz" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSzArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure[] szArray) {
        check_orphaned();
        arraySetterHelper(szArray, PROPERTY_QNAME[27]);
    }

    /**
     * Sets ith "sz" element
     */
    @Override
    public void setSzArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure sz) {
        generatedSetterHelperImpl(sz, PROPERTY_QNAME[27], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "sz" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure insertNewSz(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure)get_store().insert_element_user(PROPERTY_QNAME[27], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "sz" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure addNewSz() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure)get_store().add_element_user(PROPERTY_QNAME[27]);
            return target;
        }
    }

    /**
     * Removes the ith "sz" element
     */
    @Override
    public void removeSz(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[27], i);
        }
    }

    /**
     * Gets a List of "szCs" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure> getSzCsList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getSzCsArray,
                this::setSzCsArray,
                this::insertNewSzCs,
                this::removeSzCs,
                this::sizeOfSzCsArray
            );
        }
    }

    /**
     * Gets array of all "szCs" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure[] getSzCsArray() {
        return getXmlObjectArray(PROPERTY_QNAME[28], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure[0]);
    }

    /**
     * Gets ith "szCs" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure getSzCsArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure)get_store().find_element_user(PROPERTY_QNAME[28], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "szCs" element
     */
    @Override
    public int sizeOfSzCsArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[28]);
        }
    }

    /**
     * Sets array of all "szCs" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSzCsArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure[] szCsArray) {
        check_orphaned();
        arraySetterHelper(szCsArray, PROPERTY_QNAME[28]);
    }

    /**
     * Sets ith "szCs" element
     */
    @Override
    public void setSzCsArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure szCs) {
        generatedSetterHelperImpl(szCs, PROPERTY_QNAME[28], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "szCs" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure insertNewSzCs(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure)get_store().insert_element_user(PROPERTY_QNAME[28], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "szCs" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure addNewSzCs() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure)get_store().add_element_user(PROPERTY_QNAME[28]);
            return target;
        }
    }

    /**
     * Removes the ith "szCs" element
     */
    @Override
    public void removeSzCs(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[28], i);
        }
    }

    /**
     * Gets a List of "highlight" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHighlight> getHighlightList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getHighlightArray,
                this::setHighlightArray,
                this::insertNewHighlight,
                this::removeHighlight,
                this::sizeOfHighlightArray
            );
        }
    }

    /**
     * Gets array of all "highlight" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHighlight[] getHighlightArray() {
        return getXmlObjectArray(PROPERTY_QNAME[29], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHighlight[0]);
    }

    /**
     * Gets ith "highlight" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHighlight getHighlightArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHighlight target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHighlight)get_store().find_element_user(PROPERTY_QNAME[29], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "highlight" element
     */
    @Override
    public int sizeOfHighlightArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[29]);
        }
    }

    /**
     * Sets array of all "highlight" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setHighlightArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHighlight[] highlightArray) {
        check_orphaned();
        arraySetterHelper(highlightArray, PROPERTY_QNAME[29]);
    }

    /**
     * Sets ith "highlight" element
     */
    @Override
    public void setHighlightArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHighlight highlight) {
        generatedSetterHelperImpl(highlight, PROPERTY_QNAME[29], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "highlight" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHighlight insertNewHighlight(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHighlight target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHighlight)get_store().insert_element_user(PROPERTY_QNAME[29], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "highlight" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHighlight addNewHighlight() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHighlight target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHighlight)get_store().add_element_user(PROPERTY_QNAME[29]);
            return target;
        }
    }

    /**
     * Removes the ith "highlight" element
     */
    @Override
    public void removeHighlight(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[29], i);
        }
    }

    /**
     * Gets a List of "u" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTUnderline> getUList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getUArray,
                this::setUArray,
                this::insertNewU,
                this::removeU,
                this::sizeOfUArray
            );
        }
    }

    /**
     * Gets array of all "u" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTUnderline[] getUArray() {
        return getXmlObjectArray(PROPERTY_QNAME[30], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTUnderline[0]);
    }

    /**
     * Gets ith "u" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTUnderline getUArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTUnderline target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTUnderline)get_store().find_element_user(PROPERTY_QNAME[30], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "u" element
     */
    @Override
    public int sizeOfUArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[30]);
        }
    }

    /**
     * Sets array of all "u" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setUArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTUnderline[] uArray) {
        check_orphaned();
        arraySetterHelper(uArray, PROPERTY_QNAME[30]);
    }

    /**
     * Sets ith "u" element
     */
    @Override
    public void setUArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTUnderline u) {
        generatedSetterHelperImpl(u, PROPERTY_QNAME[30], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "u" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTUnderline insertNewU(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTUnderline target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTUnderline)get_store().insert_element_user(PROPERTY_QNAME[30], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "u" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTUnderline addNewU() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTUnderline target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTUnderline)get_store().add_element_user(PROPERTY_QNAME[30]);
            return target;
        }
    }

    /**
     * Removes the ith "u" element
     */
    @Override
    public void removeU(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[30], i);
        }
    }

    /**
     * Gets a List of "effect" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextEffect> getEffectList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getEffectArray,
                this::setEffectArray,
                this::insertNewEffect,
                this::removeEffect,
                this::sizeOfEffectArray
            );
        }
    }

    /**
     * Gets array of all "effect" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextEffect[] getEffectArray() {
        return getXmlObjectArray(PROPERTY_QNAME[31], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextEffect[0]);
    }

    /**
     * Gets ith "effect" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextEffect getEffectArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextEffect target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextEffect)get_store().find_element_user(PROPERTY_QNAME[31], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "effect" element
     */
    @Override
    public int sizeOfEffectArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[31]);
        }
    }

    /**
     * Sets array of all "effect" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setEffectArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextEffect[] effectArray) {
        check_orphaned();
        arraySetterHelper(effectArray, PROPERTY_QNAME[31]);
    }

    /**
     * Sets ith "effect" element
     */
    @Override
    public void setEffectArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextEffect effect) {
        generatedSetterHelperImpl(effect, PROPERTY_QNAME[31], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "effect" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextEffect insertNewEffect(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextEffect target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextEffect)get_store().insert_element_user(PROPERTY_QNAME[31], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "effect" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextEffect addNewEffect() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextEffect target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextEffect)get_store().add_element_user(PROPERTY_QNAME[31]);
            return target;
        }
    }

    /**
     * Removes the ith "effect" element
     */
    @Override
    public void removeEffect(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[31], i);
        }
    }

    /**
     * Gets a List of "bdr" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder> getBdrList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getBdrArray,
                this::setBdrArray,
                this::insertNewBdr,
                this::removeBdr,
                this::sizeOfBdrArray
            );
        }
    }

    /**
     * Gets array of all "bdr" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder[] getBdrArray() {
        return getXmlObjectArray(PROPERTY_QNAME[32], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder[0]);
    }

    /**
     * Gets ith "bdr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder getBdrArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder)get_store().find_element_user(PROPERTY_QNAME[32], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "bdr" element
     */
    @Override
    public int sizeOfBdrArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[32]);
        }
    }

    /**
     * Sets array of all "bdr" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setBdrArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder[] bdrArray) {
        check_orphaned();
        arraySetterHelper(bdrArray, PROPERTY_QNAME[32]);
    }

    /**
     * Sets ith "bdr" element
     */
    @Override
    public void setBdrArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder bdr) {
        generatedSetterHelperImpl(bdr, PROPERTY_QNAME[32], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "bdr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder insertNewBdr(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder)get_store().insert_element_user(PROPERTY_QNAME[32], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "bdr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder addNewBdr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder)get_store().add_element_user(PROPERTY_QNAME[32]);
            return target;
        }
    }

    /**
     * Removes the ith "bdr" element
     */
    @Override
    public void removeBdr(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[32], i);
        }
    }

    /**
     * Gets a List of "shd" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd> getShdList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getShdArray,
                this::setShdArray,
                this::insertNewShd,
                this::removeShd,
                this::sizeOfShdArray
            );
        }
    }

    /**
     * Gets array of all "shd" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd[] getShdArray() {
        return getXmlObjectArray(PROPERTY_QNAME[33], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd[0]);
    }

    /**
     * Gets ith "shd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd getShdArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd)get_store().find_element_user(PROPERTY_QNAME[33], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "shd" element
     */
    @Override
    public int sizeOfShdArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[33]);
        }
    }

    /**
     * Sets array of all "shd" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setShdArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd[] shdArray) {
        check_orphaned();
        arraySetterHelper(shdArray, PROPERTY_QNAME[33]);
    }

    /**
     * Sets ith "shd" element
     */
    @Override
    public void setShdArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd shd) {
        generatedSetterHelperImpl(shd, PROPERTY_QNAME[33], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "shd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd insertNewShd(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd)get_store().insert_element_user(PROPERTY_QNAME[33], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "shd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd addNewShd() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd)get_store().add_element_user(PROPERTY_QNAME[33]);
            return target;
        }
    }

    /**
     * Removes the ith "shd" element
     */
    @Override
    public void removeShd(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[33], i);
        }
    }

    /**
     * Gets a List of "fitText" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFitText> getFitTextList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getFitTextArray,
                this::setFitTextArray,
                this::insertNewFitText,
                this::removeFitText,
                this::sizeOfFitTextArray
            );
        }
    }

    /**
     * Gets array of all "fitText" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFitText[] getFitTextArray() {
        return getXmlObjectArray(PROPERTY_QNAME[34], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFitText[0]);
    }

    /**
     * Gets ith "fitText" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFitText getFitTextArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFitText target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFitText)get_store().find_element_user(PROPERTY_QNAME[34], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "fitText" element
     */
    @Override
    public int sizeOfFitTextArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[34]);
        }
    }

    /**
     * Sets array of all "fitText" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setFitTextArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFitText[] fitTextArray) {
        check_orphaned();
        arraySetterHelper(fitTextArray, PROPERTY_QNAME[34]);
    }

    /**
     * Sets ith "fitText" element
     */
    @Override
    public void setFitTextArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFitText fitText) {
        generatedSetterHelperImpl(fitText, PROPERTY_QNAME[34], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "fitText" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFitText insertNewFitText(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFitText target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFitText)get_store().insert_element_user(PROPERTY_QNAME[34], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "fitText" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFitText addNewFitText() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFitText target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFitText)get_store().add_element_user(PROPERTY_QNAME[34]);
            return target;
        }
    }

    /**
     * Removes the ith "fitText" element
     */
    @Override
    public void removeFitText(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[34], i);
        }
    }

    /**
     * Gets a List of "vertAlign" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalAlignRun> getVertAlignList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getVertAlignArray,
                this::setVertAlignArray,
                this::insertNewVertAlign,
                this::removeVertAlign,
                this::sizeOfVertAlignArray
            );
        }
    }

    /**
     * Gets array of all "vertAlign" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalAlignRun[] getVertAlignArray() {
        return getXmlObjectArray(PROPERTY_QNAME[35], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalAlignRun[0]);
    }

    /**
     * Gets ith "vertAlign" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalAlignRun getVertAlignArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalAlignRun target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalAlignRun)get_store().find_element_user(PROPERTY_QNAME[35], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "vertAlign" element
     */
    @Override
    public int sizeOfVertAlignArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[35]);
        }
    }

    /**
     * Sets array of all "vertAlign" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setVertAlignArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalAlignRun[] vertAlignArray) {
        check_orphaned();
        arraySetterHelper(vertAlignArray, PROPERTY_QNAME[35]);
    }

    /**
     * Sets ith "vertAlign" element
     */
    @Override
    public void setVertAlignArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalAlignRun vertAlign) {
        generatedSetterHelperImpl(vertAlign, PROPERTY_QNAME[35], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "vertAlign" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalAlignRun insertNewVertAlign(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalAlignRun target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalAlignRun)get_store().insert_element_user(PROPERTY_QNAME[35], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "vertAlign" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalAlignRun addNewVertAlign() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalAlignRun target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalAlignRun)get_store().add_element_user(PROPERTY_QNAME[35]);
            return target;
        }
    }

    /**
     * Removes the ith "vertAlign" element
     */
    @Override
    public void removeVertAlign(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[35], i);
        }
    }

    /**
     * Gets a List of "rtl" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff> getRtlList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getRtlArray,
                this::setRtlArray,
                this::insertNewRtl,
                this::removeRtl,
                this::sizeOfRtlArray
            );
        }
    }

    /**
     * Gets array of all "rtl" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] getRtlArray() {
        return getXmlObjectArray(PROPERTY_QNAME[36], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[0]);
    }

    /**
     * Gets ith "rtl" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getRtlArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[36], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "rtl" element
     */
    @Override
    public int sizeOfRtlArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[36]);
        }
    }

    /**
     * Sets array of all "rtl" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setRtlArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] rtlArray) {
        check_orphaned();
        arraySetterHelper(rtlArray, PROPERTY_QNAME[36]);
    }

    /**
     * Sets ith "rtl" element
     */
    @Override
    public void setRtlArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff rtl) {
        generatedSetterHelperImpl(rtl, PROPERTY_QNAME[36], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "rtl" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff insertNewRtl(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().insert_element_user(PROPERTY_QNAME[36], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "rtl" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewRtl() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[36]);
            return target;
        }
    }

    /**
     * Removes the ith "rtl" element
     */
    @Override
    public void removeRtl(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[36], i);
        }
    }

    /**
     * Gets a List of "cs" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff> getCsList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCsArray,
                this::setCsArray,
                this::insertNewCs,
                this::removeCs,
                this::sizeOfCsArray
            );
        }
    }

    /**
     * Gets array of all "cs" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] getCsArray() {
        return getXmlObjectArray(PROPERTY_QNAME[37], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[0]);
    }

    /**
     * Gets ith "cs" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getCsArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[37], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "cs" element
     */
    @Override
    public int sizeOfCsArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[37]);
        }
    }

    /**
     * Sets array of all "cs" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCsArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] csArray) {
        check_orphaned();
        arraySetterHelper(csArray, PROPERTY_QNAME[37]);
    }

    /**
     * Sets ith "cs" element
     */
    @Override
    public void setCsArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff cs) {
        generatedSetterHelperImpl(cs, PROPERTY_QNAME[37], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "cs" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff insertNewCs(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().insert_element_user(PROPERTY_QNAME[37], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "cs" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewCs() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[37]);
            return target;
        }
    }

    /**
     * Removes the ith "cs" element
     */
    @Override
    public void removeCs(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[37], i);
        }
    }

    /**
     * Gets a List of "em" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEm> getEmList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getEmArray,
                this::setEmArray,
                this::insertNewEm,
                this::removeEm,
                this::sizeOfEmArray
            );
        }
    }

    /**
     * Gets array of all "em" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEm[] getEmArray() {
        return getXmlObjectArray(PROPERTY_QNAME[38], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEm[0]);
    }

    /**
     * Gets ith "em" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEm getEmArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEm target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEm)get_store().find_element_user(PROPERTY_QNAME[38], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "em" element
     */
    @Override
    public int sizeOfEmArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[38]);
        }
    }

    /**
     * Sets array of all "em" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setEmArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEm[] emArray) {
        check_orphaned();
        arraySetterHelper(emArray, PROPERTY_QNAME[38]);
    }

    /**
     * Sets ith "em" element
     */
    @Override
    public void setEmArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEm em) {
        generatedSetterHelperImpl(em, PROPERTY_QNAME[38], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "em" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEm insertNewEm(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEm target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEm)get_store().insert_element_user(PROPERTY_QNAME[38], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "em" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEm addNewEm() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEm target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEm)get_store().add_element_user(PROPERTY_QNAME[38]);
            return target;
        }
    }

    /**
     * Removes the ith "em" element
     */
    @Override
    public void removeEm(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[38], i);
        }
    }

    /**
     * Gets a List of "lang" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLanguage> getLangList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getLangArray,
                this::setLangArray,
                this::insertNewLang,
                this::removeLang,
                this::sizeOfLangArray
            );
        }
    }

    /**
     * Gets array of all "lang" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLanguage[] getLangArray() {
        return getXmlObjectArray(PROPERTY_QNAME[39], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLanguage[0]);
    }

    /**
     * Gets ith "lang" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLanguage getLangArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLanguage target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLanguage)get_store().find_element_user(PROPERTY_QNAME[39], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "lang" element
     */
    @Override
    public int sizeOfLangArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[39]);
        }
    }

    /**
     * Sets array of all "lang" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setLangArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLanguage[] langArray) {
        check_orphaned();
        arraySetterHelper(langArray, PROPERTY_QNAME[39]);
    }

    /**
     * Sets ith "lang" element
     */
    @Override
    public void setLangArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLanguage lang) {
        generatedSetterHelperImpl(lang, PROPERTY_QNAME[39], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "lang" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLanguage insertNewLang(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLanguage target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLanguage)get_store().insert_element_user(PROPERTY_QNAME[39], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "lang" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLanguage addNewLang() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLanguage target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLanguage)get_store().add_element_user(PROPERTY_QNAME[39]);
            return target;
        }
    }

    /**
     * Removes the ith "lang" element
     */
    @Override
    public void removeLang(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[39], i);
        }
    }

    /**
     * Gets a List of "eastAsianLayout" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEastAsianLayout> getEastAsianLayoutList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getEastAsianLayoutArray,
                this::setEastAsianLayoutArray,
                this::insertNewEastAsianLayout,
                this::removeEastAsianLayout,
                this::sizeOfEastAsianLayoutArray
            );
        }
    }

    /**
     * Gets array of all "eastAsianLayout" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEastAsianLayout[] getEastAsianLayoutArray() {
        return getXmlObjectArray(PROPERTY_QNAME[40], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEastAsianLayout[0]);
    }

    /**
     * Gets ith "eastAsianLayout" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEastAsianLayout getEastAsianLayoutArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEastAsianLayout target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEastAsianLayout)get_store().find_element_user(PROPERTY_QNAME[40], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "eastAsianLayout" element
     */
    @Override
    public int sizeOfEastAsianLayoutArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[40]);
        }
    }

    /**
     * Sets array of all "eastAsianLayout" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setEastAsianLayoutArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEastAsianLayout[] eastAsianLayoutArray) {
        check_orphaned();
        arraySetterHelper(eastAsianLayoutArray, PROPERTY_QNAME[40]);
    }

    /**
     * Sets ith "eastAsianLayout" element
     */
    @Override
    public void setEastAsianLayoutArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEastAsianLayout eastAsianLayout) {
        generatedSetterHelperImpl(eastAsianLayout, PROPERTY_QNAME[40], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "eastAsianLayout" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEastAsianLayout insertNewEastAsianLayout(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEastAsianLayout target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEastAsianLayout)get_store().insert_element_user(PROPERTY_QNAME[40], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "eastAsianLayout" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEastAsianLayout addNewEastAsianLayout() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEastAsianLayout target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEastAsianLayout)get_store().add_element_user(PROPERTY_QNAME[40]);
            return target;
        }
    }

    /**
     * Removes the ith "eastAsianLayout" element
     */
    @Override
    public void removeEastAsianLayout(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[40], i);
        }
    }

    /**
     * Gets a List of "specVanish" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff> getSpecVanishList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getSpecVanishArray,
                this::setSpecVanishArray,
                this::insertNewSpecVanish,
                this::removeSpecVanish,
                this::sizeOfSpecVanishArray
            );
        }
    }

    /**
     * Gets array of all "specVanish" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] getSpecVanishArray() {
        return getXmlObjectArray(PROPERTY_QNAME[41], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[0]);
    }

    /**
     * Gets ith "specVanish" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getSpecVanishArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[41], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "specVanish" element
     */
    @Override
    public int sizeOfSpecVanishArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[41]);
        }
    }

    /**
     * Sets array of all "specVanish" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSpecVanishArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] specVanishArray) {
        check_orphaned();
        arraySetterHelper(specVanishArray, PROPERTY_QNAME[41]);
    }

    /**
     * Sets ith "specVanish" element
     */
    @Override
    public void setSpecVanishArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff specVanish) {
        generatedSetterHelperImpl(specVanish, PROPERTY_QNAME[41], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "specVanish" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff insertNewSpecVanish(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().insert_element_user(PROPERTY_QNAME[41], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "specVanish" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewSpecVanish() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[41]);
            return target;
        }
    }

    /**
     * Removes the ith "specVanish" element
     */
    @Override
    public void removeSpecVanish(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[41], i);
        }
    }

    /**
     * Gets a List of "oMath" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff> getOMathList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getOMathArray,
                this::setOMathArray,
                this::insertNewOMath,
                this::removeOMath,
                this::sizeOfOMathArray
            );
        }
    }

    /**
     * Gets array of all "oMath" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] getOMathArray() {
        return getXmlObjectArray(PROPERTY_QNAME[42], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[0]);
    }

    /**
     * Gets ith "oMath" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getOMathArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[42], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "oMath" element
     */
    @Override
    public int sizeOfOMathArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[42]);
        }
    }

    /**
     * Sets array of all "oMath" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setOMathArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] oMathArray) {
        check_orphaned();
        arraySetterHelper(oMathArray, PROPERTY_QNAME[42]);
    }

    /**
     * Sets ith "oMath" element
     */
    @Override
    public void setOMathArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff oMath) {
        generatedSetterHelperImpl(oMath, PROPERTY_QNAME[42], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "oMath" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff insertNewOMath(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().insert_element_user(PROPERTY_QNAME[42], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "oMath" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewOMath() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[42]);
            return target;
        }
    }

    /**
     * Removes the ith "oMath" element
     */
    @Override
    public void removeOMath(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[42], i);
        }
    }
}
