/*
 * XML Type:  CT_FFData
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFData
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_FFData(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTFFDataImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFData {
    private static final long serialVersionUID = 1L;

    public CTFFDataImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "name"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "label"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "tabIndex"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "enabled"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "calcOnExit"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "entryMacro"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "exitMacro"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "helpText"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "statusText"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "checkBox"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "ddList"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "textInput"),
    };


    /**
     * Gets a List of "name" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFName> getNameList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getNameArray,
                this::setNameArray,
                this::insertNewName,
                this::removeName,
                this::sizeOfNameArray
            );
        }
    }

    /**
     * Gets array of all "name" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFName[] getNameArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFName[0]);
    }

    /**
     * Gets ith "name" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFName getNameArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFName target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFName)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "name" element
     */
    @Override
    public int sizeOfNameArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "name" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setNameArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFName[] nameArray) {
        check_orphaned();
        arraySetterHelper(nameArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "name" element
     */
    @Override
    public void setNameArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFName name) {
        generatedSetterHelperImpl(name, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "name" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFName insertNewName(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFName target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFName)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "name" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFName addNewName() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFName target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFName)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "name" element
     */
    @Override
    public void removeName(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets a List of "label" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber> getLabelList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getLabelArray,
                this::setLabelArray,
                this::insertNewLabel,
                this::removeLabel,
                this::sizeOfLabelArray
            );
        }
    }

    /**
     * Gets array of all "label" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber[] getLabelArray() {
        return getXmlObjectArray(PROPERTY_QNAME[1], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber[0]);
    }

    /**
     * Gets ith "label" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber getLabelArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "label" element
     */
    @Override
    public int sizeOfLabelArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets array of all "label" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setLabelArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber[] labelArray) {
        check_orphaned();
        arraySetterHelper(labelArray, PROPERTY_QNAME[1]);
    }

    /**
     * Sets ith "label" element
     */
    @Override
    public void setLabelArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber label) {
        generatedSetterHelperImpl(label, PROPERTY_QNAME[1], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "label" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber insertNewLabel(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber)get_store().insert_element_user(PROPERTY_QNAME[1], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "label" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber addNewLabel() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Removes the ith "label" element
     */
    @Override
    public void removeLabel(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], i);
        }
    }

    /**
     * Gets a List of "tabIndex" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTUnsignedDecimalNumber> getTabIndexList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getTabIndexArray,
                this::setTabIndexArray,
                this::insertNewTabIndex,
                this::removeTabIndex,
                this::sizeOfTabIndexArray
            );
        }
    }

    /**
     * Gets array of all "tabIndex" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTUnsignedDecimalNumber[] getTabIndexArray() {
        return getXmlObjectArray(PROPERTY_QNAME[2], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTUnsignedDecimalNumber[0]);
    }

    /**
     * Gets ith "tabIndex" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTUnsignedDecimalNumber getTabIndexArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTUnsignedDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTUnsignedDecimalNumber)get_store().find_element_user(PROPERTY_QNAME[2], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "tabIndex" element
     */
    @Override
    public int sizeOfTabIndexArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Sets array of all "tabIndex" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setTabIndexArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTUnsignedDecimalNumber[] tabIndexArray) {
        check_orphaned();
        arraySetterHelper(tabIndexArray, PROPERTY_QNAME[2]);
    }

    /**
     * Sets ith "tabIndex" element
     */
    @Override
    public void setTabIndexArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTUnsignedDecimalNumber tabIndex) {
        generatedSetterHelperImpl(tabIndex, PROPERTY_QNAME[2], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "tabIndex" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTUnsignedDecimalNumber insertNewTabIndex(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTUnsignedDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTUnsignedDecimalNumber)get_store().insert_element_user(PROPERTY_QNAME[2], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "tabIndex" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTUnsignedDecimalNumber addNewTabIndex() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTUnsignedDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTUnsignedDecimalNumber)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Removes the ith "tabIndex" element
     */
    @Override
    public void removeTabIndex(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], i);
        }
    }

    /**
     * Gets a List of "enabled" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff> getEnabledList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getEnabledArray,
                this::setEnabledArray,
                this::insertNewEnabled,
                this::removeEnabled,
                this::sizeOfEnabledArray
            );
        }
    }

    /**
     * Gets array of all "enabled" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] getEnabledArray() {
        return getXmlObjectArray(PROPERTY_QNAME[3], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[0]);
    }

    /**
     * Gets ith "enabled" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getEnabledArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[3], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "enabled" element
     */
    @Override
    public int sizeOfEnabledArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Sets array of all "enabled" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setEnabledArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] enabledArray) {
        check_orphaned();
        arraySetterHelper(enabledArray, PROPERTY_QNAME[3]);
    }

    /**
     * Sets ith "enabled" element
     */
    @Override
    public void setEnabledArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff enabled) {
        generatedSetterHelperImpl(enabled, PROPERTY_QNAME[3], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "enabled" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff insertNewEnabled(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().insert_element_user(PROPERTY_QNAME[3], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "enabled" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewEnabled() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Removes the ith "enabled" element
     */
    @Override
    public void removeEnabled(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], i);
        }
    }

    /**
     * Gets a List of "calcOnExit" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff> getCalcOnExitList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCalcOnExitArray,
                this::setCalcOnExitArray,
                this::insertNewCalcOnExit,
                this::removeCalcOnExit,
                this::sizeOfCalcOnExitArray
            );
        }
    }

    /**
     * Gets array of all "calcOnExit" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] getCalcOnExitArray() {
        return getXmlObjectArray(PROPERTY_QNAME[4], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[0]);
    }

    /**
     * Gets ith "calcOnExit" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getCalcOnExitArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[4], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "calcOnExit" element
     */
    @Override
    public int sizeOfCalcOnExitArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Sets array of all "calcOnExit" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCalcOnExitArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] calcOnExitArray) {
        check_orphaned();
        arraySetterHelper(calcOnExitArray, PROPERTY_QNAME[4]);
    }

    /**
     * Sets ith "calcOnExit" element
     */
    @Override
    public void setCalcOnExitArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff calcOnExit) {
        generatedSetterHelperImpl(calcOnExit, PROPERTY_QNAME[4], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "calcOnExit" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff insertNewCalcOnExit(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().insert_element_user(PROPERTY_QNAME[4], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "calcOnExit" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewCalcOnExit() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Removes the ith "calcOnExit" element
     */
    @Override
    public void removeCalcOnExit(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], i);
        }
    }

    /**
     * Gets a List of "entryMacro" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMacroName> getEntryMacroList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getEntryMacroArray,
                this::setEntryMacroArray,
                this::insertNewEntryMacro,
                this::removeEntryMacro,
                this::sizeOfEntryMacroArray
            );
        }
    }

    /**
     * Gets array of all "entryMacro" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMacroName[] getEntryMacroArray() {
        return getXmlObjectArray(PROPERTY_QNAME[5], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMacroName[0]);
    }

    /**
     * Gets ith "entryMacro" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMacroName getEntryMacroArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMacroName target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMacroName)get_store().find_element_user(PROPERTY_QNAME[5], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "entryMacro" element
     */
    @Override
    public int sizeOfEntryMacroArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Sets array of all "entryMacro" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setEntryMacroArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMacroName[] entryMacroArray) {
        check_orphaned();
        arraySetterHelper(entryMacroArray, PROPERTY_QNAME[5]);
    }

    /**
     * Sets ith "entryMacro" element
     */
    @Override
    public void setEntryMacroArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMacroName entryMacro) {
        generatedSetterHelperImpl(entryMacro, PROPERTY_QNAME[5], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "entryMacro" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMacroName insertNewEntryMacro(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMacroName target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMacroName)get_store().insert_element_user(PROPERTY_QNAME[5], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "entryMacro" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMacroName addNewEntryMacro() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMacroName target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMacroName)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Removes the ith "entryMacro" element
     */
    @Override
    public void removeEntryMacro(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], i);
        }
    }

    /**
     * Gets a List of "exitMacro" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMacroName> getExitMacroList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getExitMacroArray,
                this::setExitMacroArray,
                this::insertNewExitMacro,
                this::removeExitMacro,
                this::sizeOfExitMacroArray
            );
        }
    }

    /**
     * Gets array of all "exitMacro" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMacroName[] getExitMacroArray() {
        return getXmlObjectArray(PROPERTY_QNAME[6], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMacroName[0]);
    }

    /**
     * Gets ith "exitMacro" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMacroName getExitMacroArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMacroName target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMacroName)get_store().find_element_user(PROPERTY_QNAME[6], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "exitMacro" element
     */
    @Override
    public int sizeOfExitMacroArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]);
        }
    }

    /**
     * Sets array of all "exitMacro" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setExitMacroArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMacroName[] exitMacroArray) {
        check_orphaned();
        arraySetterHelper(exitMacroArray, PROPERTY_QNAME[6]);
    }

    /**
     * Sets ith "exitMacro" element
     */
    @Override
    public void setExitMacroArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMacroName exitMacro) {
        generatedSetterHelperImpl(exitMacro, PROPERTY_QNAME[6], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "exitMacro" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMacroName insertNewExitMacro(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMacroName target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMacroName)get_store().insert_element_user(PROPERTY_QNAME[6], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "exitMacro" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMacroName addNewExitMacro() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMacroName target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMacroName)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Removes the ith "exitMacro" element
     */
    @Override
    public void removeExitMacro(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], i);
        }
    }

    /**
     * Gets a List of "helpText" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFHelpText> getHelpTextList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getHelpTextArray,
                this::setHelpTextArray,
                this::insertNewHelpText,
                this::removeHelpText,
                this::sizeOfHelpTextArray
            );
        }
    }

    /**
     * Gets array of all "helpText" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFHelpText[] getHelpTextArray() {
        return getXmlObjectArray(PROPERTY_QNAME[7], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFHelpText[0]);
    }

    /**
     * Gets ith "helpText" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFHelpText getHelpTextArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFHelpText target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFHelpText)get_store().find_element_user(PROPERTY_QNAME[7], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "helpText" element
     */
    @Override
    public int sizeOfHelpTextArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]);
        }
    }

    /**
     * Sets array of all "helpText" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setHelpTextArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFHelpText[] helpTextArray) {
        check_orphaned();
        arraySetterHelper(helpTextArray, PROPERTY_QNAME[7]);
    }

    /**
     * Sets ith "helpText" element
     */
    @Override
    public void setHelpTextArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFHelpText helpText) {
        generatedSetterHelperImpl(helpText, PROPERTY_QNAME[7], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "helpText" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFHelpText insertNewHelpText(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFHelpText target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFHelpText)get_store().insert_element_user(PROPERTY_QNAME[7], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "helpText" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFHelpText addNewHelpText() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFHelpText target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFHelpText)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Removes the ith "helpText" element
     */
    @Override
    public void removeHelpText(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[7], i);
        }
    }

    /**
     * Gets a List of "statusText" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFStatusText> getStatusTextList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getStatusTextArray,
                this::setStatusTextArray,
                this::insertNewStatusText,
                this::removeStatusText,
                this::sizeOfStatusTextArray
            );
        }
    }

    /**
     * Gets array of all "statusText" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFStatusText[] getStatusTextArray() {
        return getXmlObjectArray(PROPERTY_QNAME[8], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFStatusText[0]);
    }

    /**
     * Gets ith "statusText" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFStatusText getStatusTextArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFStatusText target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFStatusText)get_store().find_element_user(PROPERTY_QNAME[8], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "statusText" element
     */
    @Override
    public int sizeOfStatusTextArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[8]);
        }
    }

    /**
     * Sets array of all "statusText" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setStatusTextArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFStatusText[] statusTextArray) {
        check_orphaned();
        arraySetterHelper(statusTextArray, PROPERTY_QNAME[8]);
    }

    /**
     * Sets ith "statusText" element
     */
    @Override
    public void setStatusTextArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFStatusText statusText) {
        generatedSetterHelperImpl(statusText, PROPERTY_QNAME[8], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "statusText" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFStatusText insertNewStatusText(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFStatusText target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFStatusText)get_store().insert_element_user(PROPERTY_QNAME[8], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "statusText" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFStatusText addNewStatusText() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFStatusText target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFStatusText)get_store().add_element_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * Removes the ith "statusText" element
     */
    @Override
    public void removeStatusText(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[8], i);
        }
    }

    /**
     * Gets a List of "checkBox" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFCheckBox> getCheckBoxList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCheckBoxArray,
                this::setCheckBoxArray,
                this::insertNewCheckBox,
                this::removeCheckBox,
                this::sizeOfCheckBoxArray
            );
        }
    }

    /**
     * Gets array of all "checkBox" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFCheckBox[] getCheckBoxArray() {
        return getXmlObjectArray(PROPERTY_QNAME[9], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFCheckBox[0]);
    }

    /**
     * Gets ith "checkBox" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFCheckBox getCheckBoxArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFCheckBox target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFCheckBox)get_store().find_element_user(PROPERTY_QNAME[9], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "checkBox" element
     */
    @Override
    public int sizeOfCheckBoxArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[9]);
        }
    }

    /**
     * Sets array of all "checkBox" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCheckBoxArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFCheckBox[] checkBoxArray) {
        check_orphaned();
        arraySetterHelper(checkBoxArray, PROPERTY_QNAME[9]);
    }

    /**
     * Sets ith "checkBox" element
     */
    @Override
    public void setCheckBoxArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFCheckBox checkBox) {
        generatedSetterHelperImpl(checkBox, PROPERTY_QNAME[9], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "checkBox" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFCheckBox insertNewCheckBox(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFCheckBox target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFCheckBox)get_store().insert_element_user(PROPERTY_QNAME[9], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "checkBox" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFCheckBox addNewCheckBox() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFCheckBox target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFCheckBox)get_store().add_element_user(PROPERTY_QNAME[9]);
            return target;
        }
    }

    /**
     * Removes the ith "checkBox" element
     */
    @Override
    public void removeCheckBox(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[9], i);
        }
    }

    /**
     * Gets a List of "ddList" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFDDList> getDdListList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getDdListArray,
                this::setDdListArray,
                this::insertNewDdList,
                this::removeDdList,
                this::sizeOfDdListArray
            );
        }
    }

    /**
     * Gets array of all "ddList" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFDDList[] getDdListArray() {
        return getXmlObjectArray(PROPERTY_QNAME[10], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFDDList[0]);
    }

    /**
     * Gets ith "ddList" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFDDList getDdListArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFDDList target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFDDList)get_store().find_element_user(PROPERTY_QNAME[10], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "ddList" element
     */
    @Override
    public int sizeOfDdListArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[10]);
        }
    }

    /**
     * Sets array of all "ddList" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setDdListArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFDDList[] ddListArray) {
        check_orphaned();
        arraySetterHelper(ddListArray, PROPERTY_QNAME[10]);
    }

    /**
     * Sets ith "ddList" element
     */
    @Override
    public void setDdListArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFDDList ddList) {
        generatedSetterHelperImpl(ddList, PROPERTY_QNAME[10], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "ddList" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFDDList insertNewDdList(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFDDList target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFDDList)get_store().insert_element_user(PROPERTY_QNAME[10], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "ddList" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFDDList addNewDdList() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFDDList target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFDDList)get_store().add_element_user(PROPERTY_QNAME[10]);
            return target;
        }
    }

    /**
     * Removes the ith "ddList" element
     */
    @Override
    public void removeDdList(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[10], i);
        }
    }

    /**
     * Gets a List of "textInput" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFTextInput> getTextInputList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getTextInputArray,
                this::setTextInputArray,
                this::insertNewTextInput,
                this::removeTextInput,
                this::sizeOfTextInputArray
            );
        }
    }

    /**
     * Gets array of all "textInput" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFTextInput[] getTextInputArray() {
        return getXmlObjectArray(PROPERTY_QNAME[11], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFTextInput[0]);
    }

    /**
     * Gets ith "textInput" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFTextInput getTextInputArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFTextInput target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFTextInput)get_store().find_element_user(PROPERTY_QNAME[11], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "textInput" element
     */
    @Override
    public int sizeOfTextInputArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[11]);
        }
    }

    /**
     * Sets array of all "textInput" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setTextInputArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFTextInput[] textInputArray) {
        check_orphaned();
        arraySetterHelper(textInputArray, PROPERTY_QNAME[11]);
    }

    /**
     * Sets ith "textInput" element
     */
    @Override
    public void setTextInputArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFTextInput textInput) {
        generatedSetterHelperImpl(textInput, PROPERTY_QNAME[11], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "textInput" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFTextInput insertNewTextInput(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFTextInput target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFTextInput)get_store().insert_element_user(PROPERTY_QNAME[11], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "textInput" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFTextInput addNewTextInput() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFTextInput target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFTextInput)get_store().add_element_user(PROPERTY_QNAME[11]);
            return target;
        }
    }

    /**
     * Removes the ith "textInput" element
     */
    @Override
    public void removeTextInput(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[11], i);
        }
    }
}
