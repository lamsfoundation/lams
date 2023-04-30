/*
 * XML Type:  CT_ColorMRU
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTColorMRU
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_ColorMRU(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTColorMRUImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTColorMRU {
    private static final long serialVersionUID = 1L;

    public CTColorMRUImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "scrgbClr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "srgbClr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "hslClr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "sysClr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "schemeClr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "prstClr"),
    };


    /**
     * Gets a List of "scrgbClr" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTScRgbColor> getScrgbClrList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getScrgbClrArray,
                this::setScrgbClrArray,
                this::insertNewScrgbClr,
                this::removeScrgbClr,
                this::sizeOfScrgbClrArray
            );
        }
    }

    /**
     * Gets array of all "scrgbClr" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTScRgbColor[] getScrgbClrArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.drawingml.x2006.main.CTScRgbColor[0]);
    }

    /**
     * Gets ith "scrgbClr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTScRgbColor getScrgbClrArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTScRgbColor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTScRgbColor)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "scrgbClr" element
     */
    @Override
    public int sizeOfScrgbClrArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "scrgbClr" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setScrgbClrArray(org.openxmlformats.schemas.drawingml.x2006.main.CTScRgbColor[] scrgbClrArray) {
        check_orphaned();
        arraySetterHelper(scrgbClrArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "scrgbClr" element
     */
    @Override
    public void setScrgbClrArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTScRgbColor scrgbClr) {
        generatedSetterHelperImpl(scrgbClr, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "scrgbClr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTScRgbColor insertNewScrgbClr(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTScRgbColor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTScRgbColor)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "scrgbClr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTScRgbColor addNewScrgbClr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTScRgbColor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTScRgbColor)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "scrgbClr" element
     */
    @Override
    public void removeScrgbClr(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets a List of "srgbClr" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor> getSrgbClrList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getSrgbClrArray,
                this::setSrgbClrArray,
                this::insertNewSrgbClr,
                this::removeSrgbClr,
                this::sizeOfSrgbClrArray
            );
        }
    }

    /**
     * Gets array of all "srgbClr" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor[] getSrgbClrArray() {
        return getXmlObjectArray(PROPERTY_QNAME[1], new org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor[0]);
    }

    /**
     * Gets ith "srgbClr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor getSrgbClrArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "srgbClr" element
     */
    @Override
    public int sizeOfSrgbClrArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets array of all "srgbClr" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSrgbClrArray(org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor[] srgbClrArray) {
        check_orphaned();
        arraySetterHelper(srgbClrArray, PROPERTY_QNAME[1]);
    }

    /**
     * Sets ith "srgbClr" element
     */
    @Override
    public void setSrgbClrArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor srgbClr) {
        generatedSetterHelperImpl(srgbClr, PROPERTY_QNAME[1], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "srgbClr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor insertNewSrgbClr(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor)get_store().insert_element_user(PROPERTY_QNAME[1], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "srgbClr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor addNewSrgbClr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Removes the ith "srgbClr" element
     */
    @Override
    public void removeSrgbClr(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], i);
        }
    }

    /**
     * Gets a List of "hslClr" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTHslColor> getHslClrList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getHslClrArray,
                this::setHslClrArray,
                this::insertNewHslClr,
                this::removeHslClr,
                this::sizeOfHslClrArray
            );
        }
    }

    /**
     * Gets array of all "hslClr" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTHslColor[] getHslClrArray() {
        return getXmlObjectArray(PROPERTY_QNAME[2], new org.openxmlformats.schemas.drawingml.x2006.main.CTHslColor[0]);
    }

    /**
     * Gets ith "hslClr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTHslColor getHslClrArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTHslColor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTHslColor)get_store().find_element_user(PROPERTY_QNAME[2], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "hslClr" element
     */
    @Override
    public int sizeOfHslClrArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Sets array of all "hslClr" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setHslClrArray(org.openxmlformats.schemas.drawingml.x2006.main.CTHslColor[] hslClrArray) {
        check_orphaned();
        arraySetterHelper(hslClrArray, PROPERTY_QNAME[2]);
    }

    /**
     * Sets ith "hslClr" element
     */
    @Override
    public void setHslClrArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTHslColor hslClr) {
        generatedSetterHelperImpl(hslClr, PROPERTY_QNAME[2], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "hslClr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTHslColor insertNewHslClr(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTHslColor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTHslColor)get_store().insert_element_user(PROPERTY_QNAME[2], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "hslClr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTHslColor addNewHslClr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTHslColor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTHslColor)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Removes the ith "hslClr" element
     */
    @Override
    public void removeHslClr(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], i);
        }
    }

    /**
     * Gets a List of "sysClr" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTSystemColor> getSysClrList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getSysClrArray,
                this::setSysClrArray,
                this::insertNewSysClr,
                this::removeSysClr,
                this::sizeOfSysClrArray
            );
        }
    }

    /**
     * Gets array of all "sysClr" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTSystemColor[] getSysClrArray() {
        return getXmlObjectArray(PROPERTY_QNAME[3], new org.openxmlformats.schemas.drawingml.x2006.main.CTSystemColor[0]);
    }

    /**
     * Gets ith "sysClr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTSystemColor getSysClrArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTSystemColor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTSystemColor)get_store().find_element_user(PROPERTY_QNAME[3], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "sysClr" element
     */
    @Override
    public int sizeOfSysClrArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Sets array of all "sysClr" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSysClrArray(org.openxmlformats.schemas.drawingml.x2006.main.CTSystemColor[] sysClrArray) {
        check_orphaned();
        arraySetterHelper(sysClrArray, PROPERTY_QNAME[3]);
    }

    /**
     * Sets ith "sysClr" element
     */
    @Override
    public void setSysClrArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTSystemColor sysClr) {
        generatedSetterHelperImpl(sysClr, PROPERTY_QNAME[3], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "sysClr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTSystemColor insertNewSysClr(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTSystemColor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTSystemColor)get_store().insert_element_user(PROPERTY_QNAME[3], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "sysClr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTSystemColor addNewSysClr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTSystemColor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTSystemColor)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Removes the ith "sysClr" element
     */
    @Override
    public void removeSysClr(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], i);
        }
    }

    /**
     * Gets a List of "schemeClr" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTSchemeColor> getSchemeClrList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getSchemeClrArray,
                this::setSchemeClrArray,
                this::insertNewSchemeClr,
                this::removeSchemeClr,
                this::sizeOfSchemeClrArray
            );
        }
    }

    /**
     * Gets array of all "schemeClr" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTSchemeColor[] getSchemeClrArray() {
        return getXmlObjectArray(PROPERTY_QNAME[4], new org.openxmlformats.schemas.drawingml.x2006.main.CTSchemeColor[0]);
    }

    /**
     * Gets ith "schemeClr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTSchemeColor getSchemeClrArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTSchemeColor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTSchemeColor)get_store().find_element_user(PROPERTY_QNAME[4], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "schemeClr" element
     */
    @Override
    public int sizeOfSchemeClrArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Sets array of all "schemeClr" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSchemeClrArray(org.openxmlformats.schemas.drawingml.x2006.main.CTSchemeColor[] schemeClrArray) {
        check_orphaned();
        arraySetterHelper(schemeClrArray, PROPERTY_QNAME[4]);
    }

    /**
     * Sets ith "schemeClr" element
     */
    @Override
    public void setSchemeClrArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTSchemeColor schemeClr) {
        generatedSetterHelperImpl(schemeClr, PROPERTY_QNAME[4], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "schemeClr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTSchemeColor insertNewSchemeClr(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTSchemeColor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTSchemeColor)get_store().insert_element_user(PROPERTY_QNAME[4], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "schemeClr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTSchemeColor addNewSchemeClr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTSchemeColor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTSchemeColor)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Removes the ith "schemeClr" element
     */
    @Override
    public void removeSchemeClr(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], i);
        }
    }

    /**
     * Gets a List of "prstClr" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTPresetColor> getPrstClrList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getPrstClrArray,
                this::setPrstClrArray,
                this::insertNewPrstClr,
                this::removePrstClr,
                this::sizeOfPrstClrArray
            );
        }
    }

    /**
     * Gets array of all "prstClr" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPresetColor[] getPrstClrArray() {
        return getXmlObjectArray(PROPERTY_QNAME[5], new org.openxmlformats.schemas.drawingml.x2006.main.CTPresetColor[0]);
    }

    /**
     * Gets ith "prstClr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPresetColor getPrstClrArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPresetColor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPresetColor)get_store().find_element_user(PROPERTY_QNAME[5], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "prstClr" element
     */
    @Override
    public int sizeOfPrstClrArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Sets array of all "prstClr" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setPrstClrArray(org.openxmlformats.schemas.drawingml.x2006.main.CTPresetColor[] prstClrArray) {
        check_orphaned();
        arraySetterHelper(prstClrArray, PROPERTY_QNAME[5]);
    }

    /**
     * Sets ith "prstClr" element
     */
    @Override
    public void setPrstClrArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTPresetColor prstClr) {
        generatedSetterHelperImpl(prstClr, PROPERTY_QNAME[5], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "prstClr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPresetColor insertNewPrstClr(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPresetColor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPresetColor)get_store().insert_element_user(PROPERTY_QNAME[5], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "prstClr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPresetColor addNewPrstClr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPresetColor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPresetColor)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Removes the ith "prstClr" element
     */
    @Override
    public void removePrstClr(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], i);
        }
    }
}
