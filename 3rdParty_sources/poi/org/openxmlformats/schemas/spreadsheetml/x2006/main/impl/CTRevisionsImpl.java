/*
 * XML Type:  CT_Revisions
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisions
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Revisions(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTRevisionsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisions {
    private static final long serialVersionUID = 1L;

    public CTRevisionsImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "rrc"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "rm"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "rcv"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "rsnm"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "ris"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "rcc"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "rfmt"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "raf"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "rdn"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "rcmt"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "rqt"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "rcft"),
    };


    /**
     * Gets a List of "rrc" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionRowColumn> getRrcList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getRrcArray,
                this::setRrcArray,
                this::insertNewRrc,
                this::removeRrc,
                this::sizeOfRrcArray
            );
        }
    }

    /**
     * Gets array of all "rrc" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionRowColumn[] getRrcArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionRowColumn[0]);
    }

    /**
     * Gets ith "rrc" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionRowColumn getRrcArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionRowColumn target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionRowColumn)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "rrc" element
     */
    @Override
    public int sizeOfRrcArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "rrc" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setRrcArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionRowColumn[] rrcArray) {
        check_orphaned();
        arraySetterHelper(rrcArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "rrc" element
     */
    @Override
    public void setRrcArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionRowColumn rrc) {
        generatedSetterHelperImpl(rrc, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "rrc" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionRowColumn insertNewRrc(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionRowColumn target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionRowColumn)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "rrc" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionRowColumn addNewRrc() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionRowColumn target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionRowColumn)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "rrc" element
     */
    @Override
    public void removeRrc(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets a List of "rm" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionMove> getRmList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getRmArray,
                this::setRmArray,
                this::insertNewRm,
                this::removeRm,
                this::sizeOfRmArray
            );
        }
    }

    /**
     * Gets array of all "rm" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionMove[] getRmArray() {
        return getXmlObjectArray(PROPERTY_QNAME[1], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionMove[0]);
    }

    /**
     * Gets ith "rm" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionMove getRmArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionMove target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionMove)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "rm" element
     */
    @Override
    public int sizeOfRmArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets array of all "rm" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setRmArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionMove[] rmArray) {
        check_orphaned();
        arraySetterHelper(rmArray, PROPERTY_QNAME[1]);
    }

    /**
     * Sets ith "rm" element
     */
    @Override
    public void setRmArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionMove rm) {
        generatedSetterHelperImpl(rm, PROPERTY_QNAME[1], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "rm" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionMove insertNewRm(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionMove target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionMove)get_store().insert_element_user(PROPERTY_QNAME[1], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "rm" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionMove addNewRm() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionMove target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionMove)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Removes the ith "rm" element
     */
    @Override
    public void removeRm(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], i);
        }
    }

    /**
     * Gets a List of "rcv" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionCustomView> getRcvList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getRcvArray,
                this::setRcvArray,
                this::insertNewRcv,
                this::removeRcv,
                this::sizeOfRcvArray
            );
        }
    }

    /**
     * Gets array of all "rcv" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionCustomView[] getRcvArray() {
        return getXmlObjectArray(PROPERTY_QNAME[2], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionCustomView[0]);
    }

    /**
     * Gets ith "rcv" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionCustomView getRcvArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionCustomView target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionCustomView)get_store().find_element_user(PROPERTY_QNAME[2], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "rcv" element
     */
    @Override
    public int sizeOfRcvArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Sets array of all "rcv" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setRcvArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionCustomView[] rcvArray) {
        check_orphaned();
        arraySetterHelper(rcvArray, PROPERTY_QNAME[2]);
    }

    /**
     * Sets ith "rcv" element
     */
    @Override
    public void setRcvArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionCustomView rcv) {
        generatedSetterHelperImpl(rcv, PROPERTY_QNAME[2], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "rcv" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionCustomView insertNewRcv(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionCustomView target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionCustomView)get_store().insert_element_user(PROPERTY_QNAME[2], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "rcv" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionCustomView addNewRcv() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionCustomView target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionCustomView)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Removes the ith "rcv" element
     */
    @Override
    public void removeRcv(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], i);
        }
    }

    /**
     * Gets a List of "rsnm" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionSheetRename> getRsnmList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getRsnmArray,
                this::setRsnmArray,
                this::insertNewRsnm,
                this::removeRsnm,
                this::sizeOfRsnmArray
            );
        }
    }

    /**
     * Gets array of all "rsnm" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionSheetRename[] getRsnmArray() {
        return getXmlObjectArray(PROPERTY_QNAME[3], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionSheetRename[0]);
    }

    /**
     * Gets ith "rsnm" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionSheetRename getRsnmArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionSheetRename target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionSheetRename)get_store().find_element_user(PROPERTY_QNAME[3], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "rsnm" element
     */
    @Override
    public int sizeOfRsnmArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Sets array of all "rsnm" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setRsnmArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionSheetRename[] rsnmArray) {
        check_orphaned();
        arraySetterHelper(rsnmArray, PROPERTY_QNAME[3]);
    }

    /**
     * Sets ith "rsnm" element
     */
    @Override
    public void setRsnmArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionSheetRename rsnm) {
        generatedSetterHelperImpl(rsnm, PROPERTY_QNAME[3], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "rsnm" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionSheetRename insertNewRsnm(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionSheetRename target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionSheetRename)get_store().insert_element_user(PROPERTY_QNAME[3], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "rsnm" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionSheetRename addNewRsnm() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionSheetRename target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionSheetRename)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Removes the ith "rsnm" element
     */
    @Override
    public void removeRsnm(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], i);
        }
    }

    /**
     * Gets a List of "ris" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionInsertSheet> getRisList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getRisArray,
                this::setRisArray,
                this::insertNewRis,
                this::removeRis,
                this::sizeOfRisArray
            );
        }
    }

    /**
     * Gets array of all "ris" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionInsertSheet[] getRisArray() {
        return getXmlObjectArray(PROPERTY_QNAME[4], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionInsertSheet[0]);
    }

    /**
     * Gets ith "ris" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionInsertSheet getRisArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionInsertSheet target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionInsertSheet)get_store().find_element_user(PROPERTY_QNAME[4], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "ris" element
     */
    @Override
    public int sizeOfRisArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Sets array of all "ris" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setRisArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionInsertSheet[] risArray) {
        check_orphaned();
        arraySetterHelper(risArray, PROPERTY_QNAME[4]);
    }

    /**
     * Sets ith "ris" element
     */
    @Override
    public void setRisArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionInsertSheet ris) {
        generatedSetterHelperImpl(ris, PROPERTY_QNAME[4], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "ris" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionInsertSheet insertNewRis(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionInsertSheet target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionInsertSheet)get_store().insert_element_user(PROPERTY_QNAME[4], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "ris" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionInsertSheet addNewRis() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionInsertSheet target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionInsertSheet)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Removes the ith "ris" element
     */
    @Override
    public void removeRis(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], i);
        }
    }

    /**
     * Gets a List of "rcc" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionCellChange> getRccList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getRccArray,
                this::setRccArray,
                this::insertNewRcc,
                this::removeRcc,
                this::sizeOfRccArray
            );
        }
    }

    /**
     * Gets array of all "rcc" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionCellChange[] getRccArray() {
        return getXmlObjectArray(PROPERTY_QNAME[5], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionCellChange[0]);
    }

    /**
     * Gets ith "rcc" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionCellChange getRccArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionCellChange target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionCellChange)get_store().find_element_user(PROPERTY_QNAME[5], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "rcc" element
     */
    @Override
    public int sizeOfRccArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Sets array of all "rcc" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setRccArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionCellChange[] rccArray) {
        check_orphaned();
        arraySetterHelper(rccArray, PROPERTY_QNAME[5]);
    }

    /**
     * Sets ith "rcc" element
     */
    @Override
    public void setRccArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionCellChange rcc) {
        generatedSetterHelperImpl(rcc, PROPERTY_QNAME[5], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "rcc" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionCellChange insertNewRcc(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionCellChange target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionCellChange)get_store().insert_element_user(PROPERTY_QNAME[5], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "rcc" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionCellChange addNewRcc() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionCellChange target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionCellChange)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Removes the ith "rcc" element
     */
    @Override
    public void removeRcc(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], i);
        }
    }

    /**
     * Gets a List of "rfmt" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionFormatting> getRfmtList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getRfmtArray,
                this::setRfmtArray,
                this::insertNewRfmt,
                this::removeRfmt,
                this::sizeOfRfmtArray
            );
        }
    }

    /**
     * Gets array of all "rfmt" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionFormatting[] getRfmtArray() {
        return getXmlObjectArray(PROPERTY_QNAME[6], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionFormatting[0]);
    }

    /**
     * Gets ith "rfmt" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionFormatting getRfmtArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionFormatting target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionFormatting)get_store().find_element_user(PROPERTY_QNAME[6], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "rfmt" element
     */
    @Override
    public int sizeOfRfmtArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]);
        }
    }

    /**
     * Sets array of all "rfmt" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setRfmtArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionFormatting[] rfmtArray) {
        check_orphaned();
        arraySetterHelper(rfmtArray, PROPERTY_QNAME[6]);
    }

    /**
     * Sets ith "rfmt" element
     */
    @Override
    public void setRfmtArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionFormatting rfmt) {
        generatedSetterHelperImpl(rfmt, PROPERTY_QNAME[6], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "rfmt" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionFormatting insertNewRfmt(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionFormatting target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionFormatting)get_store().insert_element_user(PROPERTY_QNAME[6], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "rfmt" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionFormatting addNewRfmt() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionFormatting target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionFormatting)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Removes the ith "rfmt" element
     */
    @Override
    public void removeRfmt(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], i);
        }
    }

    /**
     * Gets a List of "raf" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionAutoFormatting> getRafList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getRafArray,
                this::setRafArray,
                this::insertNewRaf,
                this::removeRaf,
                this::sizeOfRafArray
            );
        }
    }

    /**
     * Gets array of all "raf" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionAutoFormatting[] getRafArray() {
        return getXmlObjectArray(PROPERTY_QNAME[7], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionAutoFormatting[0]);
    }

    /**
     * Gets ith "raf" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionAutoFormatting getRafArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionAutoFormatting target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionAutoFormatting)get_store().find_element_user(PROPERTY_QNAME[7], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "raf" element
     */
    @Override
    public int sizeOfRafArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]);
        }
    }

    /**
     * Sets array of all "raf" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setRafArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionAutoFormatting[] rafArray) {
        check_orphaned();
        arraySetterHelper(rafArray, PROPERTY_QNAME[7]);
    }

    /**
     * Sets ith "raf" element
     */
    @Override
    public void setRafArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionAutoFormatting raf) {
        generatedSetterHelperImpl(raf, PROPERTY_QNAME[7], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "raf" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionAutoFormatting insertNewRaf(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionAutoFormatting target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionAutoFormatting)get_store().insert_element_user(PROPERTY_QNAME[7], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "raf" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionAutoFormatting addNewRaf() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionAutoFormatting target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionAutoFormatting)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Removes the ith "raf" element
     */
    @Override
    public void removeRaf(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[7], i);
        }
    }

    /**
     * Gets a List of "rdn" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionDefinedName> getRdnList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getRdnArray,
                this::setRdnArray,
                this::insertNewRdn,
                this::removeRdn,
                this::sizeOfRdnArray
            );
        }
    }

    /**
     * Gets array of all "rdn" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionDefinedName[] getRdnArray() {
        return getXmlObjectArray(PROPERTY_QNAME[8], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionDefinedName[0]);
    }

    /**
     * Gets ith "rdn" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionDefinedName getRdnArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionDefinedName target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionDefinedName)get_store().find_element_user(PROPERTY_QNAME[8], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "rdn" element
     */
    @Override
    public int sizeOfRdnArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[8]);
        }
    }

    /**
     * Sets array of all "rdn" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setRdnArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionDefinedName[] rdnArray) {
        check_orphaned();
        arraySetterHelper(rdnArray, PROPERTY_QNAME[8]);
    }

    /**
     * Sets ith "rdn" element
     */
    @Override
    public void setRdnArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionDefinedName rdn) {
        generatedSetterHelperImpl(rdn, PROPERTY_QNAME[8], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "rdn" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionDefinedName insertNewRdn(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionDefinedName target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionDefinedName)get_store().insert_element_user(PROPERTY_QNAME[8], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "rdn" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionDefinedName addNewRdn() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionDefinedName target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionDefinedName)get_store().add_element_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * Removes the ith "rdn" element
     */
    @Override
    public void removeRdn(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[8], i);
        }
    }

    /**
     * Gets a List of "rcmt" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionComment> getRcmtList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getRcmtArray,
                this::setRcmtArray,
                this::insertNewRcmt,
                this::removeRcmt,
                this::sizeOfRcmtArray
            );
        }
    }

    /**
     * Gets array of all "rcmt" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionComment[] getRcmtArray() {
        return getXmlObjectArray(PROPERTY_QNAME[9], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionComment[0]);
    }

    /**
     * Gets ith "rcmt" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionComment getRcmtArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionComment target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionComment)get_store().find_element_user(PROPERTY_QNAME[9], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "rcmt" element
     */
    @Override
    public int sizeOfRcmtArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[9]);
        }
    }

    /**
     * Sets array of all "rcmt" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setRcmtArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionComment[] rcmtArray) {
        check_orphaned();
        arraySetterHelper(rcmtArray, PROPERTY_QNAME[9]);
    }

    /**
     * Sets ith "rcmt" element
     */
    @Override
    public void setRcmtArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionComment rcmt) {
        generatedSetterHelperImpl(rcmt, PROPERTY_QNAME[9], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "rcmt" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionComment insertNewRcmt(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionComment target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionComment)get_store().insert_element_user(PROPERTY_QNAME[9], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "rcmt" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionComment addNewRcmt() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionComment target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionComment)get_store().add_element_user(PROPERTY_QNAME[9]);
            return target;
        }
    }

    /**
     * Removes the ith "rcmt" element
     */
    @Override
    public void removeRcmt(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[9], i);
        }
    }

    /**
     * Gets a List of "rqt" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionQueryTableField> getRqtList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getRqtArray,
                this::setRqtArray,
                this::insertNewRqt,
                this::removeRqt,
                this::sizeOfRqtArray
            );
        }
    }

    /**
     * Gets array of all "rqt" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionQueryTableField[] getRqtArray() {
        return getXmlObjectArray(PROPERTY_QNAME[10], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionQueryTableField[0]);
    }

    /**
     * Gets ith "rqt" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionQueryTableField getRqtArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionQueryTableField target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionQueryTableField)get_store().find_element_user(PROPERTY_QNAME[10], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "rqt" element
     */
    @Override
    public int sizeOfRqtArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[10]);
        }
    }

    /**
     * Sets array of all "rqt" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setRqtArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionQueryTableField[] rqtArray) {
        check_orphaned();
        arraySetterHelper(rqtArray, PROPERTY_QNAME[10]);
    }

    /**
     * Sets ith "rqt" element
     */
    @Override
    public void setRqtArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionQueryTableField rqt) {
        generatedSetterHelperImpl(rqt, PROPERTY_QNAME[10], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "rqt" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionQueryTableField insertNewRqt(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionQueryTableField target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionQueryTableField)get_store().insert_element_user(PROPERTY_QNAME[10], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "rqt" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionQueryTableField addNewRqt() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionQueryTableField target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionQueryTableField)get_store().add_element_user(PROPERTY_QNAME[10]);
            return target;
        }
    }

    /**
     * Removes the ith "rqt" element
     */
    @Override
    public void removeRqt(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[10], i);
        }
    }

    /**
     * Gets a List of "rcft" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionConflict> getRcftList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getRcftArray,
                this::setRcftArray,
                this::insertNewRcft,
                this::removeRcft,
                this::sizeOfRcftArray
            );
        }
    }

    /**
     * Gets array of all "rcft" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionConflict[] getRcftArray() {
        return getXmlObjectArray(PROPERTY_QNAME[11], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionConflict[0]);
    }

    /**
     * Gets ith "rcft" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionConflict getRcftArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionConflict target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionConflict)get_store().find_element_user(PROPERTY_QNAME[11], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "rcft" element
     */
    @Override
    public int sizeOfRcftArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[11]);
        }
    }

    /**
     * Sets array of all "rcft" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setRcftArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionConflict[] rcftArray) {
        check_orphaned();
        arraySetterHelper(rcftArray, PROPERTY_QNAME[11]);
    }

    /**
     * Sets ith "rcft" element
     */
    @Override
    public void setRcftArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionConflict rcft) {
        generatedSetterHelperImpl(rcft, PROPERTY_QNAME[11], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "rcft" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionConflict insertNewRcft(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionConflict target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionConflict)get_store().insert_element_user(PROPERTY_QNAME[11], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "rcft" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionConflict addNewRcft() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionConflict target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionConflict)get_store().add_element_user(PROPERTY_QNAME[11]);
            return target;
        }
    }

    /**
     * Removes the ith "rcft" element
     */
    @Override
    public void removeRcft(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[11], i);
        }
    }
}
