/*
 * XML Type:  CT_TrPrBase
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPrBase
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TrPrBase(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTTrPrBaseImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPrBase {
    private static final long serialVersionUID = 1L;

    public CTTrPrBaseImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "cnfStyle"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "divId"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "gridBefore"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "gridAfter"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "wBefore"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "wAfter"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "cantSplit"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "trHeight"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "tblHeader"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "tblCellSpacing"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "jc"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "hidden"),
    };


    /**
     * Gets a List of "cnfStyle" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCnf> getCnfStyleList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCnfStyleArray,
                this::setCnfStyleArray,
                this::insertNewCnfStyle,
                this::removeCnfStyle,
                this::sizeOfCnfStyleArray
            );
        }
    }

    /**
     * Gets array of all "cnfStyle" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCnf[] getCnfStyleArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCnf[0]);
    }

    /**
     * Gets ith "cnfStyle" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCnf getCnfStyleArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCnf target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCnf)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "cnfStyle" element
     */
    @Override
    public int sizeOfCnfStyleArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "cnfStyle" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCnfStyleArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCnf[] cnfStyleArray) {
        check_orphaned();
        arraySetterHelper(cnfStyleArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "cnfStyle" element
     */
    @Override
    public void setCnfStyleArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCnf cnfStyle) {
        generatedSetterHelperImpl(cnfStyle, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "cnfStyle" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCnf insertNewCnfStyle(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCnf target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCnf)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "cnfStyle" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCnf addNewCnfStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCnf target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCnf)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "cnfStyle" element
     */
    @Override
    public void removeCnfStyle(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets a List of "divId" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber> getDivIdList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getDivIdArray,
                this::setDivIdArray,
                this::insertNewDivId,
                this::removeDivId,
                this::sizeOfDivIdArray
            );
        }
    }

    /**
     * Gets array of all "divId" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber[] getDivIdArray() {
        return getXmlObjectArray(PROPERTY_QNAME[1], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber[0]);
    }

    /**
     * Gets ith "divId" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber getDivIdArray(int i) {
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
     * Returns number of "divId" element
     */
    @Override
    public int sizeOfDivIdArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets array of all "divId" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setDivIdArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber[] divIdArray) {
        check_orphaned();
        arraySetterHelper(divIdArray, PROPERTY_QNAME[1]);
    }

    /**
     * Sets ith "divId" element
     */
    @Override
    public void setDivIdArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber divId) {
        generatedSetterHelperImpl(divId, PROPERTY_QNAME[1], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "divId" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber insertNewDivId(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber)get_store().insert_element_user(PROPERTY_QNAME[1], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "divId" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber addNewDivId() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Removes the ith "divId" element
     */
    @Override
    public void removeDivId(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], i);
        }
    }

    /**
     * Gets a List of "gridBefore" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber> getGridBeforeList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getGridBeforeArray,
                this::setGridBeforeArray,
                this::insertNewGridBefore,
                this::removeGridBefore,
                this::sizeOfGridBeforeArray
            );
        }
    }

    /**
     * Gets array of all "gridBefore" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber[] getGridBeforeArray() {
        return getXmlObjectArray(PROPERTY_QNAME[2], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber[0]);
    }

    /**
     * Gets ith "gridBefore" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber getGridBeforeArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber)get_store().find_element_user(PROPERTY_QNAME[2], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "gridBefore" element
     */
    @Override
    public int sizeOfGridBeforeArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Sets array of all "gridBefore" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setGridBeforeArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber[] gridBeforeArray) {
        check_orphaned();
        arraySetterHelper(gridBeforeArray, PROPERTY_QNAME[2]);
    }

    /**
     * Sets ith "gridBefore" element
     */
    @Override
    public void setGridBeforeArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber gridBefore) {
        generatedSetterHelperImpl(gridBefore, PROPERTY_QNAME[2], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "gridBefore" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber insertNewGridBefore(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber)get_store().insert_element_user(PROPERTY_QNAME[2], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "gridBefore" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber addNewGridBefore() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Removes the ith "gridBefore" element
     */
    @Override
    public void removeGridBefore(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], i);
        }
    }

    /**
     * Gets a List of "gridAfter" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber> getGridAfterList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getGridAfterArray,
                this::setGridAfterArray,
                this::insertNewGridAfter,
                this::removeGridAfter,
                this::sizeOfGridAfterArray
            );
        }
    }

    /**
     * Gets array of all "gridAfter" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber[] getGridAfterArray() {
        return getXmlObjectArray(PROPERTY_QNAME[3], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber[0]);
    }

    /**
     * Gets ith "gridAfter" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber getGridAfterArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber)get_store().find_element_user(PROPERTY_QNAME[3], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "gridAfter" element
     */
    @Override
    public int sizeOfGridAfterArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Sets array of all "gridAfter" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setGridAfterArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber[] gridAfterArray) {
        check_orphaned();
        arraySetterHelper(gridAfterArray, PROPERTY_QNAME[3]);
    }

    /**
     * Sets ith "gridAfter" element
     */
    @Override
    public void setGridAfterArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber gridAfter) {
        generatedSetterHelperImpl(gridAfter, PROPERTY_QNAME[3], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "gridAfter" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber insertNewGridAfter(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber)get_store().insert_element_user(PROPERTY_QNAME[3], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "gridAfter" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber addNewGridAfter() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Removes the ith "gridAfter" element
     */
    @Override
    public void removeGridAfter(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], i);
        }
    }

    /**
     * Gets a List of "wBefore" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth> getWBeforeList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getWBeforeArray,
                this::setWBeforeArray,
                this::insertNewWBefore,
                this::removeWBefore,
                this::sizeOfWBeforeArray
            );
        }
    }

    /**
     * Gets array of all "wBefore" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth[] getWBeforeArray() {
        return getXmlObjectArray(PROPERTY_QNAME[4], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth[0]);
    }

    /**
     * Gets ith "wBefore" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth getWBeforeArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth)get_store().find_element_user(PROPERTY_QNAME[4], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "wBefore" element
     */
    @Override
    public int sizeOfWBeforeArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Sets array of all "wBefore" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setWBeforeArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth[] wBeforeArray) {
        check_orphaned();
        arraySetterHelper(wBeforeArray, PROPERTY_QNAME[4]);
    }

    /**
     * Sets ith "wBefore" element
     */
    @Override
    public void setWBeforeArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth wBefore) {
        generatedSetterHelperImpl(wBefore, PROPERTY_QNAME[4], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "wBefore" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth insertNewWBefore(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth)get_store().insert_element_user(PROPERTY_QNAME[4], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "wBefore" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth addNewWBefore() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Removes the ith "wBefore" element
     */
    @Override
    public void removeWBefore(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], i);
        }
    }

    /**
     * Gets a List of "wAfter" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth> getWAfterList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getWAfterArray,
                this::setWAfterArray,
                this::insertNewWAfter,
                this::removeWAfter,
                this::sizeOfWAfterArray
            );
        }
    }

    /**
     * Gets array of all "wAfter" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth[] getWAfterArray() {
        return getXmlObjectArray(PROPERTY_QNAME[5], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth[0]);
    }

    /**
     * Gets ith "wAfter" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth getWAfterArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth)get_store().find_element_user(PROPERTY_QNAME[5], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "wAfter" element
     */
    @Override
    public int sizeOfWAfterArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Sets array of all "wAfter" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setWAfterArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth[] wAfterArray) {
        check_orphaned();
        arraySetterHelper(wAfterArray, PROPERTY_QNAME[5]);
    }

    /**
     * Sets ith "wAfter" element
     */
    @Override
    public void setWAfterArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth wAfter) {
        generatedSetterHelperImpl(wAfter, PROPERTY_QNAME[5], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "wAfter" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth insertNewWAfter(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth)get_store().insert_element_user(PROPERTY_QNAME[5], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "wAfter" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth addNewWAfter() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Removes the ith "wAfter" element
     */
    @Override
    public void removeWAfter(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], i);
        }
    }

    /**
     * Gets a List of "cantSplit" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff> getCantSplitList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCantSplitArray,
                this::setCantSplitArray,
                this::insertNewCantSplit,
                this::removeCantSplit,
                this::sizeOfCantSplitArray
            );
        }
    }

    /**
     * Gets array of all "cantSplit" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] getCantSplitArray() {
        return getXmlObjectArray(PROPERTY_QNAME[6], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[0]);
    }

    /**
     * Gets ith "cantSplit" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getCantSplitArray(int i) {
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
     * Returns number of "cantSplit" element
     */
    @Override
    public int sizeOfCantSplitArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]);
        }
    }

    /**
     * Sets array of all "cantSplit" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCantSplitArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] cantSplitArray) {
        check_orphaned();
        arraySetterHelper(cantSplitArray, PROPERTY_QNAME[6]);
    }

    /**
     * Sets ith "cantSplit" element
     */
    @Override
    public void setCantSplitArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff cantSplit) {
        generatedSetterHelperImpl(cantSplit, PROPERTY_QNAME[6], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "cantSplit" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff insertNewCantSplit(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().insert_element_user(PROPERTY_QNAME[6], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "cantSplit" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewCantSplit() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Removes the ith "cantSplit" element
     */
    @Override
    public void removeCantSplit(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], i);
        }
    }

    /**
     * Gets a List of "trHeight" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHeight> getTrHeightList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getTrHeightArray,
                this::setTrHeightArray,
                this::insertNewTrHeight,
                this::removeTrHeight,
                this::sizeOfTrHeightArray
            );
        }
    }

    /**
     * Gets array of all "trHeight" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHeight[] getTrHeightArray() {
        return getXmlObjectArray(PROPERTY_QNAME[7], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHeight[0]);
    }

    /**
     * Gets ith "trHeight" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHeight getTrHeightArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHeight target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHeight)get_store().find_element_user(PROPERTY_QNAME[7], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "trHeight" element
     */
    @Override
    public int sizeOfTrHeightArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]);
        }
    }

    /**
     * Sets array of all "trHeight" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setTrHeightArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHeight[] trHeightArray) {
        check_orphaned();
        arraySetterHelper(trHeightArray, PROPERTY_QNAME[7]);
    }

    /**
     * Sets ith "trHeight" element
     */
    @Override
    public void setTrHeightArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHeight trHeight) {
        generatedSetterHelperImpl(trHeight, PROPERTY_QNAME[7], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "trHeight" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHeight insertNewTrHeight(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHeight target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHeight)get_store().insert_element_user(PROPERTY_QNAME[7], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "trHeight" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHeight addNewTrHeight() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHeight target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHeight)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Removes the ith "trHeight" element
     */
    @Override
    public void removeTrHeight(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[7], i);
        }
    }

    /**
     * Gets a List of "tblHeader" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff> getTblHeaderList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getTblHeaderArray,
                this::setTblHeaderArray,
                this::insertNewTblHeader,
                this::removeTblHeader,
                this::sizeOfTblHeaderArray
            );
        }
    }

    /**
     * Gets array of all "tblHeader" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] getTblHeaderArray() {
        return getXmlObjectArray(PROPERTY_QNAME[8], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[0]);
    }

    /**
     * Gets ith "tblHeader" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getTblHeaderArray(int i) {
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
     * Returns number of "tblHeader" element
     */
    @Override
    public int sizeOfTblHeaderArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[8]);
        }
    }

    /**
     * Sets array of all "tblHeader" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setTblHeaderArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] tblHeaderArray) {
        check_orphaned();
        arraySetterHelper(tblHeaderArray, PROPERTY_QNAME[8]);
    }

    /**
     * Sets ith "tblHeader" element
     */
    @Override
    public void setTblHeaderArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff tblHeader) {
        generatedSetterHelperImpl(tblHeader, PROPERTY_QNAME[8], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "tblHeader" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff insertNewTblHeader(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().insert_element_user(PROPERTY_QNAME[8], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "tblHeader" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewTblHeader() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * Removes the ith "tblHeader" element
     */
    @Override
    public void removeTblHeader(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[8], i);
        }
    }

    /**
     * Gets a List of "tblCellSpacing" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth> getTblCellSpacingList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getTblCellSpacingArray,
                this::setTblCellSpacingArray,
                this::insertNewTblCellSpacing,
                this::removeTblCellSpacing,
                this::sizeOfTblCellSpacingArray
            );
        }
    }

    /**
     * Gets array of all "tblCellSpacing" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth[] getTblCellSpacingArray() {
        return getXmlObjectArray(PROPERTY_QNAME[9], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth[0]);
    }

    /**
     * Gets ith "tblCellSpacing" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth getTblCellSpacingArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth)get_store().find_element_user(PROPERTY_QNAME[9], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "tblCellSpacing" element
     */
    @Override
    public int sizeOfTblCellSpacingArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[9]);
        }
    }

    /**
     * Sets array of all "tblCellSpacing" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setTblCellSpacingArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth[] tblCellSpacingArray) {
        check_orphaned();
        arraySetterHelper(tblCellSpacingArray, PROPERTY_QNAME[9]);
    }

    /**
     * Sets ith "tblCellSpacing" element
     */
    @Override
    public void setTblCellSpacingArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth tblCellSpacing) {
        generatedSetterHelperImpl(tblCellSpacing, PROPERTY_QNAME[9], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "tblCellSpacing" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth insertNewTblCellSpacing(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth)get_store().insert_element_user(PROPERTY_QNAME[9], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "tblCellSpacing" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth addNewTblCellSpacing() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth)get_store().add_element_user(PROPERTY_QNAME[9]);
            return target;
        }
    }

    /**
     * Removes the ith "tblCellSpacing" element
     */
    @Override
    public void removeTblCellSpacing(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[9], i);
        }
    }

    /**
     * Gets a List of "jc" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJcTable> getJcList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getJcArray,
                this::setJcArray,
                this::insertNewJc,
                this::removeJc,
                this::sizeOfJcArray
            );
        }
    }

    /**
     * Gets array of all "jc" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJcTable[] getJcArray() {
        return getXmlObjectArray(PROPERTY_QNAME[10], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJcTable[0]);
    }

    /**
     * Gets ith "jc" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJcTable getJcArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJcTable target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJcTable)get_store().find_element_user(PROPERTY_QNAME[10], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "jc" element
     */
    @Override
    public int sizeOfJcArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[10]);
        }
    }

    /**
     * Sets array of all "jc" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setJcArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJcTable[] jcArray) {
        check_orphaned();
        arraySetterHelper(jcArray, PROPERTY_QNAME[10]);
    }

    /**
     * Sets ith "jc" element
     */
    @Override
    public void setJcArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJcTable jc) {
        generatedSetterHelperImpl(jc, PROPERTY_QNAME[10], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "jc" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJcTable insertNewJc(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJcTable target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJcTable)get_store().insert_element_user(PROPERTY_QNAME[10], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "jc" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJcTable addNewJc() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJcTable target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJcTable)get_store().add_element_user(PROPERTY_QNAME[10]);
            return target;
        }
    }

    /**
     * Removes the ith "jc" element
     */
    @Override
    public void removeJc(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[10], i);
        }
    }

    /**
     * Gets a List of "hidden" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff> getHiddenList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getHiddenArray,
                this::setHiddenArray,
                this::insertNewHidden,
                this::removeHidden,
                this::sizeOfHiddenArray
            );
        }
    }

    /**
     * Gets array of all "hidden" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] getHiddenArray() {
        return getXmlObjectArray(PROPERTY_QNAME[11], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[0]);
    }

    /**
     * Gets ith "hidden" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getHiddenArray(int i) {
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
     * Returns number of "hidden" element
     */
    @Override
    public int sizeOfHiddenArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[11]);
        }
    }

    /**
     * Sets array of all "hidden" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setHiddenArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] hiddenArray) {
        check_orphaned();
        arraySetterHelper(hiddenArray, PROPERTY_QNAME[11]);
    }

    /**
     * Sets ith "hidden" element
     */
    @Override
    public void setHiddenArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff hidden) {
        generatedSetterHelperImpl(hidden, PROPERTY_QNAME[11], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "hidden" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff insertNewHidden(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().insert_element_user(PROPERTY_QNAME[11], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "hidden" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewHidden() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[11]);
            return target;
        }
    }

    /**
     * Removes the ith "hidden" element
     */
    @Override
    public void removeHidden(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[11], i);
        }
    }
}
