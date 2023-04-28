/*
 * XML Type:  CT_RunTrackChange
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_RunTrackChange(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTRunTrackChangeImpl extends org.openxmlformats.schemas.wordprocessingml.x2006.main.impl.CTTrackChangeImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange {
    private static final long serialVersionUID = 1L;

    public CTRunTrackChangeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "customXml"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "smartTag"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "sdt"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "dir"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "bdo"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "r"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "proofErr"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "permStart"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "permEnd"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "bookmarkStart"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "bookmarkEnd"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "moveFromRangeStart"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "moveFromRangeEnd"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "moveToRangeStart"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "moveToRangeEnd"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "commentRangeStart"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "commentRangeEnd"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "customXmlInsRangeStart"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "customXmlInsRangeEnd"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "customXmlDelRangeStart"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "customXmlDelRangeEnd"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "customXmlMoveFromRangeStart"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "customXmlMoveFromRangeEnd"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "customXmlMoveToRangeStart"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "customXmlMoveToRangeEnd"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "ins"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "del"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "moveFrom"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "moveTo"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/math", "oMathPara"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/math", "oMath"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/math", "acc"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/math", "bar"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/math", "box"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/math", "borderBox"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/math", "d"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/math", "eqArr"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/math", "f"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/math", "func"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/math", "groupChr"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/math", "limLow"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/math", "limUpp"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/math", "m"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/math", "nary"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/math", "phant"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/math", "rad"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/math", "sPre"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/math", "sSub"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/math", "sSubSup"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/math", "sSup"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/math", "r"),
    };


    /**
     * Gets a List of "customXml" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCustomXmlRun> getCustomXmlList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCustomXmlArray,
                this::setCustomXmlArray,
                this::insertNewCustomXml,
                this::removeCustomXml,
                this::sizeOfCustomXmlArray
            );
        }
    }

    /**
     * Gets array of all "customXml" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCustomXmlRun[] getCustomXmlArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCustomXmlRun[0]);
    }

    /**
     * Gets ith "customXml" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCustomXmlRun getCustomXmlArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCustomXmlRun target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCustomXmlRun)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "customXml" element
     */
    @Override
    public int sizeOfCustomXmlArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "customXml" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCustomXmlArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCustomXmlRun[] customXmlArray) {
        check_orphaned();
        arraySetterHelper(customXmlArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "customXml" element
     */
    @Override
    public void setCustomXmlArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCustomXmlRun customXml) {
        generatedSetterHelperImpl(customXml, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "customXml" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCustomXmlRun insertNewCustomXml(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCustomXmlRun target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCustomXmlRun)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "customXml" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCustomXmlRun addNewCustomXml() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCustomXmlRun target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCustomXmlRun)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "customXml" element
     */
    @Override
    public void removeCustomXml(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets a List of "smartTag" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSmartTagRun> getSmartTagList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getSmartTagArray,
                this::setSmartTagArray,
                this::insertNewSmartTag,
                this::removeSmartTag,
                this::sizeOfSmartTagArray
            );
        }
    }

    /**
     * Gets array of all "smartTag" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSmartTagRun[] getSmartTagArray() {
        return getXmlObjectArray(PROPERTY_QNAME[1], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSmartTagRun[0]);
    }

    /**
     * Gets ith "smartTag" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSmartTagRun getSmartTagArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSmartTagRun target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSmartTagRun)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "smartTag" element
     */
    @Override
    public int sizeOfSmartTagArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets array of all "smartTag" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSmartTagArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSmartTagRun[] smartTagArray) {
        check_orphaned();
        arraySetterHelper(smartTagArray, PROPERTY_QNAME[1]);
    }

    /**
     * Sets ith "smartTag" element
     */
    @Override
    public void setSmartTagArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSmartTagRun smartTag) {
        generatedSetterHelperImpl(smartTag, PROPERTY_QNAME[1], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "smartTag" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSmartTagRun insertNewSmartTag(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSmartTagRun target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSmartTagRun)get_store().insert_element_user(PROPERTY_QNAME[1], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "smartTag" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSmartTagRun addNewSmartTag() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSmartTagRun target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSmartTagRun)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Removes the ith "smartTag" element
     */
    @Override
    public void removeSmartTag(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], i);
        }
    }

    /**
     * Gets a List of "sdt" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtRun> getSdtList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getSdtArray,
                this::setSdtArray,
                this::insertNewSdt,
                this::removeSdt,
                this::sizeOfSdtArray
            );
        }
    }

    /**
     * Gets array of all "sdt" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtRun[] getSdtArray() {
        return getXmlObjectArray(PROPERTY_QNAME[2], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtRun[0]);
    }

    /**
     * Gets ith "sdt" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtRun getSdtArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtRun target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtRun)get_store().find_element_user(PROPERTY_QNAME[2], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "sdt" element
     */
    @Override
    public int sizeOfSdtArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Sets array of all "sdt" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSdtArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtRun[] sdtArray) {
        check_orphaned();
        arraySetterHelper(sdtArray, PROPERTY_QNAME[2]);
    }

    /**
     * Sets ith "sdt" element
     */
    @Override
    public void setSdtArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtRun sdt) {
        generatedSetterHelperImpl(sdt, PROPERTY_QNAME[2], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "sdt" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtRun insertNewSdt(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtRun target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtRun)get_store().insert_element_user(PROPERTY_QNAME[2], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "sdt" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtRun addNewSdt() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtRun target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtRun)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Removes the ith "sdt" element
     */
    @Override
    public void removeSdt(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], i);
        }
    }

    /**
     * Gets a List of "dir" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDirContentRun> getDirList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getDirArray,
                this::setDirArray,
                this::insertNewDir,
                this::removeDir,
                this::sizeOfDirArray
            );
        }
    }

    /**
     * Gets array of all "dir" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDirContentRun[] getDirArray() {
        return getXmlObjectArray(PROPERTY_QNAME[3], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDirContentRun[0]);
    }

    /**
     * Gets ith "dir" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDirContentRun getDirArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDirContentRun target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDirContentRun)get_store().find_element_user(PROPERTY_QNAME[3], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "dir" element
     */
    @Override
    public int sizeOfDirArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Sets array of all "dir" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setDirArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDirContentRun[] dirArray) {
        check_orphaned();
        arraySetterHelper(dirArray, PROPERTY_QNAME[3]);
    }

    /**
     * Sets ith "dir" element
     */
    @Override
    public void setDirArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDirContentRun dir) {
        generatedSetterHelperImpl(dir, PROPERTY_QNAME[3], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "dir" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDirContentRun insertNewDir(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDirContentRun target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDirContentRun)get_store().insert_element_user(PROPERTY_QNAME[3], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "dir" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDirContentRun addNewDir() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDirContentRun target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDirContentRun)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Removes the ith "dir" element
     */
    @Override
    public void removeDir(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], i);
        }
    }

    /**
     * Gets a List of "bdo" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBdoContentRun> getBdoList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getBdoArray,
                this::setBdoArray,
                this::insertNewBdo,
                this::removeBdo,
                this::sizeOfBdoArray
            );
        }
    }

    /**
     * Gets array of all "bdo" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBdoContentRun[] getBdoArray() {
        return getXmlObjectArray(PROPERTY_QNAME[4], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBdoContentRun[0]);
    }

    /**
     * Gets ith "bdo" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBdoContentRun getBdoArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBdoContentRun target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBdoContentRun)get_store().find_element_user(PROPERTY_QNAME[4], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "bdo" element
     */
    @Override
    public int sizeOfBdoArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Sets array of all "bdo" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setBdoArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBdoContentRun[] bdoArray) {
        check_orphaned();
        arraySetterHelper(bdoArray, PROPERTY_QNAME[4]);
    }

    /**
     * Sets ith "bdo" element
     */
    @Override
    public void setBdoArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBdoContentRun bdo) {
        generatedSetterHelperImpl(bdo, PROPERTY_QNAME[4], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "bdo" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBdoContentRun insertNewBdo(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBdoContentRun target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBdoContentRun)get_store().insert_element_user(PROPERTY_QNAME[4], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "bdo" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBdoContentRun addNewBdo() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBdoContentRun target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBdoContentRun)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Removes the ith "bdo" element
     */
    @Override
    public void removeBdo(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], i);
        }
    }

    /**
     * Gets a List of "r" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR> getRList() {
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
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR[] getRArray() {
        return getXmlObjectArray(PROPERTY_QNAME[5], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR[0]);
    }

    /**
     * Gets ith "r" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR getRArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR)get_store().find_element_user(PROPERTY_QNAME[5], i);
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
            return get_store().count_elements(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Sets array of all "r" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setRArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR[] rArray) {
        check_orphaned();
        arraySetterHelper(rArray, PROPERTY_QNAME[5]);
    }

    /**
     * Sets ith "r" element
     */
    @Override
    public void setRArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR r) {
        generatedSetterHelperImpl(r, PROPERTY_QNAME[5], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "r" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR insertNewR(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR)get_store().insert_element_user(PROPERTY_QNAME[5], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "r" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR addNewR() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR)get_store().add_element_user(PROPERTY_QNAME[5]);
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
            get_store().remove_element(PROPERTY_QNAME[5], i);
        }
    }

    /**
     * Gets a List of "proofErr" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTProofErr> getProofErrList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getProofErrArray,
                this::setProofErrArray,
                this::insertNewProofErr,
                this::removeProofErr,
                this::sizeOfProofErrArray
            );
        }
    }

    /**
     * Gets array of all "proofErr" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTProofErr[] getProofErrArray() {
        return getXmlObjectArray(PROPERTY_QNAME[6], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTProofErr[0]);
    }

    /**
     * Gets ith "proofErr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTProofErr getProofErrArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTProofErr target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTProofErr)get_store().find_element_user(PROPERTY_QNAME[6], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "proofErr" element
     */
    @Override
    public int sizeOfProofErrArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]);
        }
    }

    /**
     * Sets array of all "proofErr" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setProofErrArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTProofErr[] proofErrArray) {
        check_orphaned();
        arraySetterHelper(proofErrArray, PROPERTY_QNAME[6]);
    }

    /**
     * Sets ith "proofErr" element
     */
    @Override
    public void setProofErrArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTProofErr proofErr) {
        generatedSetterHelperImpl(proofErr, PROPERTY_QNAME[6], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "proofErr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTProofErr insertNewProofErr(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTProofErr target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTProofErr)get_store().insert_element_user(PROPERTY_QNAME[6], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "proofErr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTProofErr addNewProofErr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTProofErr target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTProofErr)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Removes the ith "proofErr" element
     */
    @Override
    public void removeProofErr(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], i);
        }
    }

    /**
     * Gets a List of "permStart" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPermStart> getPermStartList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getPermStartArray,
                this::setPermStartArray,
                this::insertNewPermStart,
                this::removePermStart,
                this::sizeOfPermStartArray
            );
        }
    }

    /**
     * Gets array of all "permStart" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPermStart[] getPermStartArray() {
        return getXmlObjectArray(PROPERTY_QNAME[7], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPermStart[0]);
    }

    /**
     * Gets ith "permStart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPermStart getPermStartArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPermStart target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPermStart)get_store().find_element_user(PROPERTY_QNAME[7], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "permStart" element
     */
    @Override
    public int sizeOfPermStartArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]);
        }
    }

    /**
     * Sets array of all "permStart" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setPermStartArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPermStart[] permStartArray) {
        check_orphaned();
        arraySetterHelper(permStartArray, PROPERTY_QNAME[7]);
    }

    /**
     * Sets ith "permStart" element
     */
    @Override
    public void setPermStartArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPermStart permStart) {
        generatedSetterHelperImpl(permStart, PROPERTY_QNAME[7], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "permStart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPermStart insertNewPermStart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPermStart target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPermStart)get_store().insert_element_user(PROPERTY_QNAME[7], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "permStart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPermStart addNewPermStart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPermStart target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPermStart)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Removes the ith "permStart" element
     */
    @Override
    public void removePermStart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[7], i);
        }
    }

    /**
     * Gets a List of "permEnd" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPerm> getPermEndList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getPermEndArray,
                this::setPermEndArray,
                this::insertNewPermEnd,
                this::removePermEnd,
                this::sizeOfPermEndArray
            );
        }
    }

    /**
     * Gets array of all "permEnd" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPerm[] getPermEndArray() {
        return getXmlObjectArray(PROPERTY_QNAME[8], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPerm[0]);
    }

    /**
     * Gets ith "permEnd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPerm getPermEndArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPerm target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPerm)get_store().find_element_user(PROPERTY_QNAME[8], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "permEnd" element
     */
    @Override
    public int sizeOfPermEndArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[8]);
        }
    }

    /**
     * Sets array of all "permEnd" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setPermEndArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPerm[] permEndArray) {
        check_orphaned();
        arraySetterHelper(permEndArray, PROPERTY_QNAME[8]);
    }

    /**
     * Sets ith "permEnd" element
     */
    @Override
    public void setPermEndArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPerm permEnd) {
        generatedSetterHelperImpl(permEnd, PROPERTY_QNAME[8], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "permEnd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPerm insertNewPermEnd(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPerm target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPerm)get_store().insert_element_user(PROPERTY_QNAME[8], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "permEnd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPerm addNewPermEnd() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPerm target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPerm)get_store().add_element_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * Removes the ith "permEnd" element
     */
    @Override
    public void removePermEnd(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[8], i);
        }
    }

    /**
     * Gets a List of "bookmarkStart" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark> getBookmarkStartList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getBookmarkStartArray,
                this::setBookmarkStartArray,
                this::insertNewBookmarkStart,
                this::removeBookmarkStart,
                this::sizeOfBookmarkStartArray
            );
        }
    }

    /**
     * Gets array of all "bookmarkStart" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark[] getBookmarkStartArray() {
        return getXmlObjectArray(PROPERTY_QNAME[9], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark[0]);
    }

    /**
     * Gets ith "bookmarkStart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark getBookmarkStartArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark)get_store().find_element_user(PROPERTY_QNAME[9], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "bookmarkStart" element
     */
    @Override
    public int sizeOfBookmarkStartArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[9]);
        }
    }

    /**
     * Sets array of all "bookmarkStart" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setBookmarkStartArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark[] bookmarkStartArray) {
        check_orphaned();
        arraySetterHelper(bookmarkStartArray, PROPERTY_QNAME[9]);
    }

    /**
     * Sets ith "bookmarkStart" element
     */
    @Override
    public void setBookmarkStartArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark bookmarkStart) {
        generatedSetterHelperImpl(bookmarkStart, PROPERTY_QNAME[9], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "bookmarkStart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark insertNewBookmarkStart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark)get_store().insert_element_user(PROPERTY_QNAME[9], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "bookmarkStart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark addNewBookmarkStart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark)get_store().add_element_user(PROPERTY_QNAME[9]);
            return target;
        }
    }

    /**
     * Removes the ith "bookmarkStart" element
     */
    @Override
    public void removeBookmarkStart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[9], i);
        }
    }

    /**
     * Gets a List of "bookmarkEnd" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange> getBookmarkEndList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getBookmarkEndArray,
                this::setBookmarkEndArray,
                this::insertNewBookmarkEnd,
                this::removeBookmarkEnd,
                this::sizeOfBookmarkEndArray
            );
        }
    }

    /**
     * Gets array of all "bookmarkEnd" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange[] getBookmarkEndArray() {
        return getXmlObjectArray(PROPERTY_QNAME[10], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange[0]);
    }

    /**
     * Gets ith "bookmarkEnd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange getBookmarkEndArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange)get_store().find_element_user(PROPERTY_QNAME[10], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "bookmarkEnd" element
     */
    @Override
    public int sizeOfBookmarkEndArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[10]);
        }
    }

    /**
     * Sets array of all "bookmarkEnd" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setBookmarkEndArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange[] bookmarkEndArray) {
        check_orphaned();
        arraySetterHelper(bookmarkEndArray, PROPERTY_QNAME[10]);
    }

    /**
     * Sets ith "bookmarkEnd" element
     */
    @Override
    public void setBookmarkEndArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange bookmarkEnd) {
        generatedSetterHelperImpl(bookmarkEnd, PROPERTY_QNAME[10], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "bookmarkEnd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange insertNewBookmarkEnd(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange)get_store().insert_element_user(PROPERTY_QNAME[10], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "bookmarkEnd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange addNewBookmarkEnd() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange)get_store().add_element_user(PROPERTY_QNAME[10]);
            return target;
        }
    }

    /**
     * Removes the ith "bookmarkEnd" element
     */
    @Override
    public void removeBookmarkEnd(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[10], i);
        }
    }

    /**
     * Gets a List of "moveFromRangeStart" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMoveBookmark> getMoveFromRangeStartList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getMoveFromRangeStartArray,
                this::setMoveFromRangeStartArray,
                this::insertNewMoveFromRangeStart,
                this::removeMoveFromRangeStart,
                this::sizeOfMoveFromRangeStartArray
            );
        }
    }

    /**
     * Gets array of all "moveFromRangeStart" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMoveBookmark[] getMoveFromRangeStartArray() {
        return getXmlObjectArray(PROPERTY_QNAME[11], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMoveBookmark[0]);
    }

    /**
     * Gets ith "moveFromRangeStart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMoveBookmark getMoveFromRangeStartArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMoveBookmark target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMoveBookmark)get_store().find_element_user(PROPERTY_QNAME[11], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "moveFromRangeStart" element
     */
    @Override
    public int sizeOfMoveFromRangeStartArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[11]);
        }
    }

    /**
     * Sets array of all "moveFromRangeStart" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setMoveFromRangeStartArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMoveBookmark[] moveFromRangeStartArray) {
        check_orphaned();
        arraySetterHelper(moveFromRangeStartArray, PROPERTY_QNAME[11]);
    }

    /**
     * Sets ith "moveFromRangeStart" element
     */
    @Override
    public void setMoveFromRangeStartArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMoveBookmark moveFromRangeStart) {
        generatedSetterHelperImpl(moveFromRangeStart, PROPERTY_QNAME[11], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "moveFromRangeStart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMoveBookmark insertNewMoveFromRangeStart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMoveBookmark target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMoveBookmark)get_store().insert_element_user(PROPERTY_QNAME[11], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "moveFromRangeStart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMoveBookmark addNewMoveFromRangeStart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMoveBookmark target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMoveBookmark)get_store().add_element_user(PROPERTY_QNAME[11]);
            return target;
        }
    }

    /**
     * Removes the ith "moveFromRangeStart" element
     */
    @Override
    public void removeMoveFromRangeStart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[11], i);
        }
    }

    /**
     * Gets a List of "moveFromRangeEnd" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange> getMoveFromRangeEndList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getMoveFromRangeEndArray,
                this::setMoveFromRangeEndArray,
                this::insertNewMoveFromRangeEnd,
                this::removeMoveFromRangeEnd,
                this::sizeOfMoveFromRangeEndArray
            );
        }
    }

    /**
     * Gets array of all "moveFromRangeEnd" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange[] getMoveFromRangeEndArray() {
        return getXmlObjectArray(PROPERTY_QNAME[12], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange[0]);
    }

    /**
     * Gets ith "moveFromRangeEnd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange getMoveFromRangeEndArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange)get_store().find_element_user(PROPERTY_QNAME[12], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "moveFromRangeEnd" element
     */
    @Override
    public int sizeOfMoveFromRangeEndArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[12]);
        }
    }

    /**
     * Sets array of all "moveFromRangeEnd" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setMoveFromRangeEndArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange[] moveFromRangeEndArray) {
        check_orphaned();
        arraySetterHelper(moveFromRangeEndArray, PROPERTY_QNAME[12]);
    }

    /**
     * Sets ith "moveFromRangeEnd" element
     */
    @Override
    public void setMoveFromRangeEndArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange moveFromRangeEnd) {
        generatedSetterHelperImpl(moveFromRangeEnd, PROPERTY_QNAME[12], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "moveFromRangeEnd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange insertNewMoveFromRangeEnd(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange)get_store().insert_element_user(PROPERTY_QNAME[12], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "moveFromRangeEnd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange addNewMoveFromRangeEnd() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange)get_store().add_element_user(PROPERTY_QNAME[12]);
            return target;
        }
    }

    /**
     * Removes the ith "moveFromRangeEnd" element
     */
    @Override
    public void removeMoveFromRangeEnd(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[12], i);
        }
    }

    /**
     * Gets a List of "moveToRangeStart" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMoveBookmark> getMoveToRangeStartList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getMoveToRangeStartArray,
                this::setMoveToRangeStartArray,
                this::insertNewMoveToRangeStart,
                this::removeMoveToRangeStart,
                this::sizeOfMoveToRangeStartArray
            );
        }
    }

    /**
     * Gets array of all "moveToRangeStart" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMoveBookmark[] getMoveToRangeStartArray() {
        return getXmlObjectArray(PROPERTY_QNAME[13], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMoveBookmark[0]);
    }

    /**
     * Gets ith "moveToRangeStart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMoveBookmark getMoveToRangeStartArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMoveBookmark target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMoveBookmark)get_store().find_element_user(PROPERTY_QNAME[13], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "moveToRangeStart" element
     */
    @Override
    public int sizeOfMoveToRangeStartArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[13]);
        }
    }

    /**
     * Sets array of all "moveToRangeStart" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setMoveToRangeStartArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMoveBookmark[] moveToRangeStartArray) {
        check_orphaned();
        arraySetterHelper(moveToRangeStartArray, PROPERTY_QNAME[13]);
    }

    /**
     * Sets ith "moveToRangeStart" element
     */
    @Override
    public void setMoveToRangeStartArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMoveBookmark moveToRangeStart) {
        generatedSetterHelperImpl(moveToRangeStart, PROPERTY_QNAME[13], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "moveToRangeStart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMoveBookmark insertNewMoveToRangeStart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMoveBookmark target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMoveBookmark)get_store().insert_element_user(PROPERTY_QNAME[13], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "moveToRangeStart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMoveBookmark addNewMoveToRangeStart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMoveBookmark target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMoveBookmark)get_store().add_element_user(PROPERTY_QNAME[13]);
            return target;
        }
    }

    /**
     * Removes the ith "moveToRangeStart" element
     */
    @Override
    public void removeMoveToRangeStart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[13], i);
        }
    }

    /**
     * Gets a List of "moveToRangeEnd" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange> getMoveToRangeEndList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getMoveToRangeEndArray,
                this::setMoveToRangeEndArray,
                this::insertNewMoveToRangeEnd,
                this::removeMoveToRangeEnd,
                this::sizeOfMoveToRangeEndArray
            );
        }
    }

    /**
     * Gets array of all "moveToRangeEnd" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange[] getMoveToRangeEndArray() {
        return getXmlObjectArray(PROPERTY_QNAME[14], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange[0]);
    }

    /**
     * Gets ith "moveToRangeEnd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange getMoveToRangeEndArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange)get_store().find_element_user(PROPERTY_QNAME[14], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "moveToRangeEnd" element
     */
    @Override
    public int sizeOfMoveToRangeEndArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[14]);
        }
    }

    /**
     * Sets array of all "moveToRangeEnd" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setMoveToRangeEndArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange[] moveToRangeEndArray) {
        check_orphaned();
        arraySetterHelper(moveToRangeEndArray, PROPERTY_QNAME[14]);
    }

    /**
     * Sets ith "moveToRangeEnd" element
     */
    @Override
    public void setMoveToRangeEndArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange moveToRangeEnd) {
        generatedSetterHelperImpl(moveToRangeEnd, PROPERTY_QNAME[14], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "moveToRangeEnd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange insertNewMoveToRangeEnd(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange)get_store().insert_element_user(PROPERTY_QNAME[14], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "moveToRangeEnd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange addNewMoveToRangeEnd() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange)get_store().add_element_user(PROPERTY_QNAME[14]);
            return target;
        }
    }

    /**
     * Removes the ith "moveToRangeEnd" element
     */
    @Override
    public void removeMoveToRangeEnd(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[14], i);
        }
    }

    /**
     * Gets a List of "commentRangeStart" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange> getCommentRangeStartList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCommentRangeStartArray,
                this::setCommentRangeStartArray,
                this::insertNewCommentRangeStart,
                this::removeCommentRangeStart,
                this::sizeOfCommentRangeStartArray
            );
        }
    }

    /**
     * Gets array of all "commentRangeStart" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange[] getCommentRangeStartArray() {
        return getXmlObjectArray(PROPERTY_QNAME[15], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange[0]);
    }

    /**
     * Gets ith "commentRangeStart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange getCommentRangeStartArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange)get_store().find_element_user(PROPERTY_QNAME[15], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "commentRangeStart" element
     */
    @Override
    public int sizeOfCommentRangeStartArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[15]);
        }
    }

    /**
     * Sets array of all "commentRangeStart" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCommentRangeStartArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange[] commentRangeStartArray) {
        check_orphaned();
        arraySetterHelper(commentRangeStartArray, PROPERTY_QNAME[15]);
    }

    /**
     * Sets ith "commentRangeStart" element
     */
    @Override
    public void setCommentRangeStartArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange commentRangeStart) {
        generatedSetterHelperImpl(commentRangeStart, PROPERTY_QNAME[15], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "commentRangeStart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange insertNewCommentRangeStart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange)get_store().insert_element_user(PROPERTY_QNAME[15], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "commentRangeStart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange addNewCommentRangeStart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange)get_store().add_element_user(PROPERTY_QNAME[15]);
            return target;
        }
    }

    /**
     * Removes the ith "commentRangeStart" element
     */
    @Override
    public void removeCommentRangeStart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[15], i);
        }
    }

    /**
     * Gets a List of "commentRangeEnd" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange> getCommentRangeEndList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCommentRangeEndArray,
                this::setCommentRangeEndArray,
                this::insertNewCommentRangeEnd,
                this::removeCommentRangeEnd,
                this::sizeOfCommentRangeEndArray
            );
        }
    }

    /**
     * Gets array of all "commentRangeEnd" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange[] getCommentRangeEndArray() {
        return getXmlObjectArray(PROPERTY_QNAME[16], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange[0]);
    }

    /**
     * Gets ith "commentRangeEnd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange getCommentRangeEndArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange)get_store().find_element_user(PROPERTY_QNAME[16], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "commentRangeEnd" element
     */
    @Override
    public int sizeOfCommentRangeEndArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[16]);
        }
    }

    /**
     * Sets array of all "commentRangeEnd" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCommentRangeEndArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange[] commentRangeEndArray) {
        check_orphaned();
        arraySetterHelper(commentRangeEndArray, PROPERTY_QNAME[16]);
    }

    /**
     * Sets ith "commentRangeEnd" element
     */
    @Override
    public void setCommentRangeEndArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange commentRangeEnd) {
        generatedSetterHelperImpl(commentRangeEnd, PROPERTY_QNAME[16], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "commentRangeEnd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange insertNewCommentRangeEnd(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange)get_store().insert_element_user(PROPERTY_QNAME[16], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "commentRangeEnd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange addNewCommentRangeEnd() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange)get_store().add_element_user(PROPERTY_QNAME[16]);
            return target;
        }
    }

    /**
     * Removes the ith "commentRangeEnd" element
     */
    @Override
    public void removeCommentRangeEnd(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[16], i);
        }
    }

    /**
     * Gets a List of "customXmlInsRangeStart" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange> getCustomXmlInsRangeStartList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCustomXmlInsRangeStartArray,
                this::setCustomXmlInsRangeStartArray,
                this::insertNewCustomXmlInsRangeStart,
                this::removeCustomXmlInsRangeStart,
                this::sizeOfCustomXmlInsRangeStartArray
            );
        }
    }

    /**
     * Gets array of all "customXmlInsRangeStart" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange[] getCustomXmlInsRangeStartArray() {
        return getXmlObjectArray(PROPERTY_QNAME[17], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange[0]);
    }

    /**
     * Gets ith "customXmlInsRangeStart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange getCustomXmlInsRangeStartArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange)get_store().find_element_user(PROPERTY_QNAME[17], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "customXmlInsRangeStart" element
     */
    @Override
    public int sizeOfCustomXmlInsRangeStartArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[17]);
        }
    }

    /**
     * Sets array of all "customXmlInsRangeStart" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCustomXmlInsRangeStartArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange[] customXmlInsRangeStartArray) {
        check_orphaned();
        arraySetterHelper(customXmlInsRangeStartArray, PROPERTY_QNAME[17]);
    }

    /**
     * Sets ith "customXmlInsRangeStart" element
     */
    @Override
    public void setCustomXmlInsRangeStartArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange customXmlInsRangeStart) {
        generatedSetterHelperImpl(customXmlInsRangeStart, PROPERTY_QNAME[17], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "customXmlInsRangeStart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange insertNewCustomXmlInsRangeStart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange)get_store().insert_element_user(PROPERTY_QNAME[17], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "customXmlInsRangeStart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange addNewCustomXmlInsRangeStart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange)get_store().add_element_user(PROPERTY_QNAME[17]);
            return target;
        }
    }

    /**
     * Removes the ith "customXmlInsRangeStart" element
     */
    @Override
    public void removeCustomXmlInsRangeStart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[17], i);
        }
    }

    /**
     * Gets a List of "customXmlInsRangeEnd" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup> getCustomXmlInsRangeEndList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCustomXmlInsRangeEndArray,
                this::setCustomXmlInsRangeEndArray,
                this::insertNewCustomXmlInsRangeEnd,
                this::removeCustomXmlInsRangeEnd,
                this::sizeOfCustomXmlInsRangeEndArray
            );
        }
    }

    /**
     * Gets array of all "customXmlInsRangeEnd" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup[] getCustomXmlInsRangeEndArray() {
        return getXmlObjectArray(PROPERTY_QNAME[18], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup[0]);
    }

    /**
     * Gets ith "customXmlInsRangeEnd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup getCustomXmlInsRangeEndArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup)get_store().find_element_user(PROPERTY_QNAME[18], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "customXmlInsRangeEnd" element
     */
    @Override
    public int sizeOfCustomXmlInsRangeEndArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[18]);
        }
    }

    /**
     * Sets array of all "customXmlInsRangeEnd" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCustomXmlInsRangeEndArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup[] customXmlInsRangeEndArray) {
        check_orphaned();
        arraySetterHelper(customXmlInsRangeEndArray, PROPERTY_QNAME[18]);
    }

    /**
     * Sets ith "customXmlInsRangeEnd" element
     */
    @Override
    public void setCustomXmlInsRangeEndArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup customXmlInsRangeEnd) {
        generatedSetterHelperImpl(customXmlInsRangeEnd, PROPERTY_QNAME[18], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "customXmlInsRangeEnd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup insertNewCustomXmlInsRangeEnd(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup)get_store().insert_element_user(PROPERTY_QNAME[18], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "customXmlInsRangeEnd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup addNewCustomXmlInsRangeEnd() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup)get_store().add_element_user(PROPERTY_QNAME[18]);
            return target;
        }
    }

    /**
     * Removes the ith "customXmlInsRangeEnd" element
     */
    @Override
    public void removeCustomXmlInsRangeEnd(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[18], i);
        }
    }

    /**
     * Gets a List of "customXmlDelRangeStart" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange> getCustomXmlDelRangeStartList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCustomXmlDelRangeStartArray,
                this::setCustomXmlDelRangeStartArray,
                this::insertNewCustomXmlDelRangeStart,
                this::removeCustomXmlDelRangeStart,
                this::sizeOfCustomXmlDelRangeStartArray
            );
        }
    }

    /**
     * Gets array of all "customXmlDelRangeStart" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange[] getCustomXmlDelRangeStartArray() {
        return getXmlObjectArray(PROPERTY_QNAME[19], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange[0]);
    }

    /**
     * Gets ith "customXmlDelRangeStart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange getCustomXmlDelRangeStartArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange)get_store().find_element_user(PROPERTY_QNAME[19], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "customXmlDelRangeStart" element
     */
    @Override
    public int sizeOfCustomXmlDelRangeStartArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[19]);
        }
    }

    /**
     * Sets array of all "customXmlDelRangeStart" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCustomXmlDelRangeStartArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange[] customXmlDelRangeStartArray) {
        check_orphaned();
        arraySetterHelper(customXmlDelRangeStartArray, PROPERTY_QNAME[19]);
    }

    /**
     * Sets ith "customXmlDelRangeStart" element
     */
    @Override
    public void setCustomXmlDelRangeStartArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange customXmlDelRangeStart) {
        generatedSetterHelperImpl(customXmlDelRangeStart, PROPERTY_QNAME[19], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "customXmlDelRangeStart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange insertNewCustomXmlDelRangeStart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange)get_store().insert_element_user(PROPERTY_QNAME[19], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "customXmlDelRangeStart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange addNewCustomXmlDelRangeStart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange)get_store().add_element_user(PROPERTY_QNAME[19]);
            return target;
        }
    }

    /**
     * Removes the ith "customXmlDelRangeStart" element
     */
    @Override
    public void removeCustomXmlDelRangeStart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[19], i);
        }
    }

    /**
     * Gets a List of "customXmlDelRangeEnd" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup> getCustomXmlDelRangeEndList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCustomXmlDelRangeEndArray,
                this::setCustomXmlDelRangeEndArray,
                this::insertNewCustomXmlDelRangeEnd,
                this::removeCustomXmlDelRangeEnd,
                this::sizeOfCustomXmlDelRangeEndArray
            );
        }
    }

    /**
     * Gets array of all "customXmlDelRangeEnd" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup[] getCustomXmlDelRangeEndArray() {
        return getXmlObjectArray(PROPERTY_QNAME[20], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup[0]);
    }

    /**
     * Gets ith "customXmlDelRangeEnd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup getCustomXmlDelRangeEndArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup)get_store().find_element_user(PROPERTY_QNAME[20], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "customXmlDelRangeEnd" element
     */
    @Override
    public int sizeOfCustomXmlDelRangeEndArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[20]);
        }
    }

    /**
     * Sets array of all "customXmlDelRangeEnd" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCustomXmlDelRangeEndArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup[] customXmlDelRangeEndArray) {
        check_orphaned();
        arraySetterHelper(customXmlDelRangeEndArray, PROPERTY_QNAME[20]);
    }

    /**
     * Sets ith "customXmlDelRangeEnd" element
     */
    @Override
    public void setCustomXmlDelRangeEndArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup customXmlDelRangeEnd) {
        generatedSetterHelperImpl(customXmlDelRangeEnd, PROPERTY_QNAME[20], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "customXmlDelRangeEnd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup insertNewCustomXmlDelRangeEnd(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup)get_store().insert_element_user(PROPERTY_QNAME[20], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "customXmlDelRangeEnd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup addNewCustomXmlDelRangeEnd() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup)get_store().add_element_user(PROPERTY_QNAME[20]);
            return target;
        }
    }

    /**
     * Removes the ith "customXmlDelRangeEnd" element
     */
    @Override
    public void removeCustomXmlDelRangeEnd(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[20], i);
        }
    }

    /**
     * Gets a List of "customXmlMoveFromRangeStart" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange> getCustomXmlMoveFromRangeStartList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCustomXmlMoveFromRangeStartArray,
                this::setCustomXmlMoveFromRangeStartArray,
                this::insertNewCustomXmlMoveFromRangeStart,
                this::removeCustomXmlMoveFromRangeStart,
                this::sizeOfCustomXmlMoveFromRangeStartArray
            );
        }
    }

    /**
     * Gets array of all "customXmlMoveFromRangeStart" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange[] getCustomXmlMoveFromRangeStartArray() {
        return getXmlObjectArray(PROPERTY_QNAME[21], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange[0]);
    }

    /**
     * Gets ith "customXmlMoveFromRangeStart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange getCustomXmlMoveFromRangeStartArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange)get_store().find_element_user(PROPERTY_QNAME[21], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "customXmlMoveFromRangeStart" element
     */
    @Override
    public int sizeOfCustomXmlMoveFromRangeStartArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[21]);
        }
    }

    /**
     * Sets array of all "customXmlMoveFromRangeStart" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCustomXmlMoveFromRangeStartArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange[] customXmlMoveFromRangeStartArray) {
        check_orphaned();
        arraySetterHelper(customXmlMoveFromRangeStartArray, PROPERTY_QNAME[21]);
    }

    /**
     * Sets ith "customXmlMoveFromRangeStart" element
     */
    @Override
    public void setCustomXmlMoveFromRangeStartArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange customXmlMoveFromRangeStart) {
        generatedSetterHelperImpl(customXmlMoveFromRangeStart, PROPERTY_QNAME[21], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "customXmlMoveFromRangeStart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange insertNewCustomXmlMoveFromRangeStart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange)get_store().insert_element_user(PROPERTY_QNAME[21], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "customXmlMoveFromRangeStart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange addNewCustomXmlMoveFromRangeStart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange)get_store().add_element_user(PROPERTY_QNAME[21]);
            return target;
        }
    }

    /**
     * Removes the ith "customXmlMoveFromRangeStart" element
     */
    @Override
    public void removeCustomXmlMoveFromRangeStart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[21], i);
        }
    }

    /**
     * Gets a List of "customXmlMoveFromRangeEnd" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup> getCustomXmlMoveFromRangeEndList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCustomXmlMoveFromRangeEndArray,
                this::setCustomXmlMoveFromRangeEndArray,
                this::insertNewCustomXmlMoveFromRangeEnd,
                this::removeCustomXmlMoveFromRangeEnd,
                this::sizeOfCustomXmlMoveFromRangeEndArray
            );
        }
    }

    /**
     * Gets array of all "customXmlMoveFromRangeEnd" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup[] getCustomXmlMoveFromRangeEndArray() {
        return getXmlObjectArray(PROPERTY_QNAME[22], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup[0]);
    }

    /**
     * Gets ith "customXmlMoveFromRangeEnd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup getCustomXmlMoveFromRangeEndArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup)get_store().find_element_user(PROPERTY_QNAME[22], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "customXmlMoveFromRangeEnd" element
     */
    @Override
    public int sizeOfCustomXmlMoveFromRangeEndArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[22]);
        }
    }

    /**
     * Sets array of all "customXmlMoveFromRangeEnd" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCustomXmlMoveFromRangeEndArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup[] customXmlMoveFromRangeEndArray) {
        check_orphaned();
        arraySetterHelper(customXmlMoveFromRangeEndArray, PROPERTY_QNAME[22]);
    }

    /**
     * Sets ith "customXmlMoveFromRangeEnd" element
     */
    @Override
    public void setCustomXmlMoveFromRangeEndArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup customXmlMoveFromRangeEnd) {
        generatedSetterHelperImpl(customXmlMoveFromRangeEnd, PROPERTY_QNAME[22], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "customXmlMoveFromRangeEnd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup insertNewCustomXmlMoveFromRangeEnd(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup)get_store().insert_element_user(PROPERTY_QNAME[22], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "customXmlMoveFromRangeEnd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup addNewCustomXmlMoveFromRangeEnd() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup)get_store().add_element_user(PROPERTY_QNAME[22]);
            return target;
        }
    }

    /**
     * Removes the ith "customXmlMoveFromRangeEnd" element
     */
    @Override
    public void removeCustomXmlMoveFromRangeEnd(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[22], i);
        }
    }

    /**
     * Gets a List of "customXmlMoveToRangeStart" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange> getCustomXmlMoveToRangeStartList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCustomXmlMoveToRangeStartArray,
                this::setCustomXmlMoveToRangeStartArray,
                this::insertNewCustomXmlMoveToRangeStart,
                this::removeCustomXmlMoveToRangeStart,
                this::sizeOfCustomXmlMoveToRangeStartArray
            );
        }
    }

    /**
     * Gets array of all "customXmlMoveToRangeStart" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange[] getCustomXmlMoveToRangeStartArray() {
        return getXmlObjectArray(PROPERTY_QNAME[23], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange[0]);
    }

    /**
     * Gets ith "customXmlMoveToRangeStart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange getCustomXmlMoveToRangeStartArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange)get_store().find_element_user(PROPERTY_QNAME[23], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "customXmlMoveToRangeStart" element
     */
    @Override
    public int sizeOfCustomXmlMoveToRangeStartArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[23]);
        }
    }

    /**
     * Sets array of all "customXmlMoveToRangeStart" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCustomXmlMoveToRangeStartArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange[] customXmlMoveToRangeStartArray) {
        check_orphaned();
        arraySetterHelper(customXmlMoveToRangeStartArray, PROPERTY_QNAME[23]);
    }

    /**
     * Sets ith "customXmlMoveToRangeStart" element
     */
    @Override
    public void setCustomXmlMoveToRangeStartArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange customXmlMoveToRangeStart) {
        generatedSetterHelperImpl(customXmlMoveToRangeStart, PROPERTY_QNAME[23], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "customXmlMoveToRangeStart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange insertNewCustomXmlMoveToRangeStart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange)get_store().insert_element_user(PROPERTY_QNAME[23], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "customXmlMoveToRangeStart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange addNewCustomXmlMoveToRangeStart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange)get_store().add_element_user(PROPERTY_QNAME[23]);
            return target;
        }
    }

    /**
     * Removes the ith "customXmlMoveToRangeStart" element
     */
    @Override
    public void removeCustomXmlMoveToRangeStart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[23], i);
        }
    }

    /**
     * Gets a List of "customXmlMoveToRangeEnd" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup> getCustomXmlMoveToRangeEndList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCustomXmlMoveToRangeEndArray,
                this::setCustomXmlMoveToRangeEndArray,
                this::insertNewCustomXmlMoveToRangeEnd,
                this::removeCustomXmlMoveToRangeEnd,
                this::sizeOfCustomXmlMoveToRangeEndArray
            );
        }
    }

    /**
     * Gets array of all "customXmlMoveToRangeEnd" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup[] getCustomXmlMoveToRangeEndArray() {
        return getXmlObjectArray(PROPERTY_QNAME[24], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup[0]);
    }

    /**
     * Gets ith "customXmlMoveToRangeEnd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup getCustomXmlMoveToRangeEndArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup)get_store().find_element_user(PROPERTY_QNAME[24], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "customXmlMoveToRangeEnd" element
     */
    @Override
    public int sizeOfCustomXmlMoveToRangeEndArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[24]);
        }
    }

    /**
     * Sets array of all "customXmlMoveToRangeEnd" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCustomXmlMoveToRangeEndArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup[] customXmlMoveToRangeEndArray) {
        check_orphaned();
        arraySetterHelper(customXmlMoveToRangeEndArray, PROPERTY_QNAME[24]);
    }

    /**
     * Sets ith "customXmlMoveToRangeEnd" element
     */
    @Override
    public void setCustomXmlMoveToRangeEndArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup customXmlMoveToRangeEnd) {
        generatedSetterHelperImpl(customXmlMoveToRangeEnd, PROPERTY_QNAME[24], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "customXmlMoveToRangeEnd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup insertNewCustomXmlMoveToRangeEnd(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup)get_store().insert_element_user(PROPERTY_QNAME[24], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "customXmlMoveToRangeEnd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup addNewCustomXmlMoveToRangeEnd() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup)get_store().add_element_user(PROPERTY_QNAME[24]);
            return target;
        }
    }

    /**
     * Removes the ith "customXmlMoveToRangeEnd" element
     */
    @Override
    public void removeCustomXmlMoveToRangeEnd(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[24], i);
        }
    }

    /**
     * Gets a List of "ins" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange> getInsList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getInsArray,
                this::setInsArray,
                this::insertNewIns,
                this::removeIns,
                this::sizeOfInsArray
            );
        }
    }

    /**
     * Gets array of all "ins" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange[] getInsArray() {
        return getXmlObjectArray(PROPERTY_QNAME[25], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange[0]);
    }

    /**
     * Gets ith "ins" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange getInsArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange)get_store().find_element_user(PROPERTY_QNAME[25], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "ins" element
     */
    @Override
    public int sizeOfInsArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[25]);
        }
    }

    /**
     * Sets array of all "ins" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setInsArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange[] insArray) {
        check_orphaned();
        arraySetterHelper(insArray, PROPERTY_QNAME[25]);
    }

    /**
     * Sets ith "ins" element
     */
    @Override
    public void setInsArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange ins) {
        generatedSetterHelperImpl(ins, PROPERTY_QNAME[25], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "ins" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange insertNewIns(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange)get_store().insert_element_user(PROPERTY_QNAME[25], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "ins" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange addNewIns() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange)get_store().add_element_user(PROPERTY_QNAME[25]);
            return target;
        }
    }

    /**
     * Removes the ith "ins" element
     */
    @Override
    public void removeIns(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[25], i);
        }
    }

    /**
     * Gets a List of "del" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange> getDelList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getDelArray,
                this::setDelArray,
                this::insertNewDel,
                this::removeDel,
                this::sizeOfDelArray
            );
        }
    }

    /**
     * Gets array of all "del" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange[] getDelArray() {
        return getXmlObjectArray(PROPERTY_QNAME[26], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange[0]);
    }

    /**
     * Gets ith "del" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange getDelArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange)get_store().find_element_user(PROPERTY_QNAME[26], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "del" element
     */
    @Override
    public int sizeOfDelArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[26]);
        }
    }

    /**
     * Sets array of all "del" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setDelArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange[] delArray) {
        check_orphaned();
        arraySetterHelper(delArray, PROPERTY_QNAME[26]);
    }

    /**
     * Sets ith "del" element
     */
    @Override
    public void setDelArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange del) {
        generatedSetterHelperImpl(del, PROPERTY_QNAME[26], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "del" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange insertNewDel(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange)get_store().insert_element_user(PROPERTY_QNAME[26], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "del" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange addNewDel() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange)get_store().add_element_user(PROPERTY_QNAME[26]);
            return target;
        }
    }

    /**
     * Removes the ith "del" element
     */
    @Override
    public void removeDel(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[26], i);
        }
    }

    /**
     * Gets a List of "moveFrom" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange> getMoveFromList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getMoveFromArray,
                this::setMoveFromArray,
                this::insertNewMoveFrom,
                this::removeMoveFrom,
                this::sizeOfMoveFromArray
            );
        }
    }

    /**
     * Gets array of all "moveFrom" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange[] getMoveFromArray() {
        return getXmlObjectArray(PROPERTY_QNAME[27], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange[0]);
    }

    /**
     * Gets ith "moveFrom" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange getMoveFromArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange)get_store().find_element_user(PROPERTY_QNAME[27], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "moveFrom" element
     */
    @Override
    public int sizeOfMoveFromArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[27]);
        }
    }

    /**
     * Sets array of all "moveFrom" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setMoveFromArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange[] moveFromArray) {
        check_orphaned();
        arraySetterHelper(moveFromArray, PROPERTY_QNAME[27]);
    }

    /**
     * Sets ith "moveFrom" element
     */
    @Override
    public void setMoveFromArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange moveFrom) {
        generatedSetterHelperImpl(moveFrom, PROPERTY_QNAME[27], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "moveFrom" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange insertNewMoveFrom(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange)get_store().insert_element_user(PROPERTY_QNAME[27], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "moveFrom" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange addNewMoveFrom() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange)get_store().add_element_user(PROPERTY_QNAME[27]);
            return target;
        }
    }

    /**
     * Removes the ith "moveFrom" element
     */
    @Override
    public void removeMoveFrom(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[27], i);
        }
    }

    /**
     * Gets a List of "moveTo" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange> getMoveToList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getMoveToArray,
                this::setMoveToArray,
                this::insertNewMoveTo,
                this::removeMoveTo,
                this::sizeOfMoveToArray
            );
        }
    }

    /**
     * Gets array of all "moveTo" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange[] getMoveToArray() {
        return getXmlObjectArray(PROPERTY_QNAME[28], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange[0]);
    }

    /**
     * Gets ith "moveTo" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange getMoveToArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange)get_store().find_element_user(PROPERTY_QNAME[28], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "moveTo" element
     */
    @Override
    public int sizeOfMoveToArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[28]);
        }
    }

    /**
     * Sets array of all "moveTo" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setMoveToArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange[] moveToArray) {
        check_orphaned();
        arraySetterHelper(moveToArray, PROPERTY_QNAME[28]);
    }

    /**
     * Sets ith "moveTo" element
     */
    @Override
    public void setMoveToArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange moveTo) {
        generatedSetterHelperImpl(moveTo, PROPERTY_QNAME[28], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "moveTo" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange insertNewMoveTo(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange)get_store().insert_element_user(PROPERTY_QNAME[28], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "moveTo" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange addNewMoveTo() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange)get_store().add_element_user(PROPERTY_QNAME[28]);
            return target;
        }
    }

    /**
     * Removes the ith "moveTo" element
     */
    @Override
    public void removeMoveTo(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[28], i);
        }
    }

    /**
     * Gets a List of "oMathPara" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathPara> getOMathParaList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getOMathParaArray,
                this::setOMathParaArray,
                this::insertNewOMathPara,
                this::removeOMathPara,
                this::sizeOfOMathParaArray
            );
        }
    }

    /**
     * Gets array of all "oMathPara" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathPara[] getOMathParaArray() {
        return getXmlObjectArray(PROPERTY_QNAME[29], new org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathPara[0]);
    }

    /**
     * Gets ith "oMathPara" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathPara getOMathParaArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathPara target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathPara)get_store().find_element_user(PROPERTY_QNAME[29], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "oMathPara" element
     */
    @Override
    public int sizeOfOMathParaArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[29]);
        }
    }

    /**
     * Sets array of all "oMathPara" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setOMathParaArray(org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathPara[] oMathParaArray) {
        check_orphaned();
        arraySetterHelper(oMathParaArray, PROPERTY_QNAME[29]);
    }

    /**
     * Sets ith "oMathPara" element
     */
    @Override
    public void setOMathParaArray(int i, org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathPara oMathPara) {
        generatedSetterHelperImpl(oMathPara, PROPERTY_QNAME[29], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "oMathPara" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathPara insertNewOMathPara(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathPara target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathPara)get_store().insert_element_user(PROPERTY_QNAME[29], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "oMathPara" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathPara addNewOMathPara() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathPara target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathPara)get_store().add_element_user(PROPERTY_QNAME[29]);
            return target;
        }
    }

    /**
     * Removes the ith "oMathPara" element
     */
    @Override
    public void removeOMathPara(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[29], i);
        }
    }

    /**
     * Gets a List of "oMath" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath> getOMathList() {
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
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath[] getOMathArray() {
        return getXmlObjectArray(PROPERTY_QNAME[30], new org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath[0]);
    }

    /**
     * Gets ith "oMath" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath getOMathArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath)get_store().find_element_user(PROPERTY_QNAME[30], i);
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
            return get_store().count_elements(PROPERTY_QNAME[30]);
        }
    }

    /**
     * Sets array of all "oMath" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setOMathArray(org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath[] oMathArray) {
        check_orphaned();
        arraySetterHelper(oMathArray, PROPERTY_QNAME[30]);
    }

    /**
     * Sets ith "oMath" element
     */
    @Override
    public void setOMathArray(int i, org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath oMath) {
        generatedSetterHelperImpl(oMath, PROPERTY_QNAME[30], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "oMath" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath insertNewOMath(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath)get_store().insert_element_user(PROPERTY_QNAME[30], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "oMath" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath addNewOMath() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath)get_store().add_element_user(PROPERTY_QNAME[30]);
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
            get_store().remove_element(PROPERTY_QNAME[30], i);
        }
    }

    /**
     * Gets a List of "acc" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.math.CTAcc> getAccList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getAccArray,
                this::setAccArray,
                this::insertNewAcc,
                this::removeAcc,
                this::sizeOfAccArray
            );
        }
    }

    /**
     * Gets array of all "acc" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTAcc[] getAccArray() {
        return getXmlObjectArray(PROPERTY_QNAME[31], new org.openxmlformats.schemas.officeDocument.x2006.math.CTAcc[0]);
    }

    /**
     * Gets ith "acc" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTAcc getAccArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTAcc target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTAcc)get_store().find_element_user(PROPERTY_QNAME[31], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "acc" element
     */
    @Override
    public int sizeOfAccArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[31]);
        }
    }

    /**
     * Sets array of all "acc" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setAccArray(org.openxmlformats.schemas.officeDocument.x2006.math.CTAcc[] accArray) {
        check_orphaned();
        arraySetterHelper(accArray, PROPERTY_QNAME[31]);
    }

    /**
     * Sets ith "acc" element
     */
    @Override
    public void setAccArray(int i, org.openxmlformats.schemas.officeDocument.x2006.math.CTAcc acc) {
        generatedSetterHelperImpl(acc, PROPERTY_QNAME[31], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "acc" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTAcc insertNewAcc(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTAcc target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTAcc)get_store().insert_element_user(PROPERTY_QNAME[31], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "acc" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTAcc addNewAcc() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTAcc target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTAcc)get_store().add_element_user(PROPERTY_QNAME[31]);
            return target;
        }
    }

    /**
     * Removes the ith "acc" element
     */
    @Override
    public void removeAcc(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[31], i);
        }
    }

    /**
     * Gets a List of "bar" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.math.CTBar> getBarList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getBarArray,
                this::setBarArray,
                this::insertNewBar,
                this::removeBar,
                this::sizeOfBarArray
            );
        }
    }

    /**
     * Gets array of all "bar" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTBar[] getBarArray() {
        return getXmlObjectArray(PROPERTY_QNAME[32], new org.openxmlformats.schemas.officeDocument.x2006.math.CTBar[0]);
    }

    /**
     * Gets ith "bar" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTBar getBarArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTBar target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTBar)get_store().find_element_user(PROPERTY_QNAME[32], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "bar" element
     */
    @Override
    public int sizeOfBarArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[32]);
        }
    }

    /**
     * Sets array of all "bar" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setBarArray(org.openxmlformats.schemas.officeDocument.x2006.math.CTBar[] barArray) {
        check_orphaned();
        arraySetterHelper(barArray, PROPERTY_QNAME[32]);
    }

    /**
     * Sets ith "bar" element
     */
    @Override
    public void setBarArray(int i, org.openxmlformats.schemas.officeDocument.x2006.math.CTBar bar) {
        generatedSetterHelperImpl(bar, PROPERTY_QNAME[32], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "bar" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTBar insertNewBar(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTBar target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTBar)get_store().insert_element_user(PROPERTY_QNAME[32], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "bar" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTBar addNewBar() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTBar target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTBar)get_store().add_element_user(PROPERTY_QNAME[32]);
            return target;
        }
    }

    /**
     * Removes the ith "bar" element
     */
    @Override
    public void removeBar(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[32], i);
        }
    }

    /**
     * Gets a List of "box" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.math.CTBox> getBoxList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getBoxArray,
                this::setBoxArray,
                this::insertNewBox,
                this::removeBox,
                this::sizeOfBoxArray
            );
        }
    }

    /**
     * Gets array of all "box" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTBox[] getBoxArray() {
        return getXmlObjectArray(PROPERTY_QNAME[33], new org.openxmlformats.schemas.officeDocument.x2006.math.CTBox[0]);
    }

    /**
     * Gets ith "box" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTBox getBoxArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTBox target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTBox)get_store().find_element_user(PROPERTY_QNAME[33], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "box" element
     */
    @Override
    public int sizeOfBoxArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[33]);
        }
    }

    /**
     * Sets array of all "box" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setBoxArray(org.openxmlformats.schemas.officeDocument.x2006.math.CTBox[] boxArray) {
        check_orphaned();
        arraySetterHelper(boxArray, PROPERTY_QNAME[33]);
    }

    /**
     * Sets ith "box" element
     */
    @Override
    public void setBoxArray(int i, org.openxmlformats.schemas.officeDocument.x2006.math.CTBox box) {
        generatedSetterHelperImpl(box, PROPERTY_QNAME[33], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "box" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTBox insertNewBox(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTBox target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTBox)get_store().insert_element_user(PROPERTY_QNAME[33], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "box" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTBox addNewBox() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTBox target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTBox)get_store().add_element_user(PROPERTY_QNAME[33]);
            return target;
        }
    }

    /**
     * Removes the ith "box" element
     */
    @Override
    public void removeBox(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[33], i);
        }
    }

    /**
     * Gets a List of "borderBox" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.math.CTBorderBox> getBorderBoxList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getBorderBoxArray,
                this::setBorderBoxArray,
                this::insertNewBorderBox,
                this::removeBorderBox,
                this::sizeOfBorderBoxArray
            );
        }
    }

    /**
     * Gets array of all "borderBox" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTBorderBox[] getBorderBoxArray() {
        return getXmlObjectArray(PROPERTY_QNAME[34], new org.openxmlformats.schemas.officeDocument.x2006.math.CTBorderBox[0]);
    }

    /**
     * Gets ith "borderBox" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTBorderBox getBorderBoxArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTBorderBox target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTBorderBox)get_store().find_element_user(PROPERTY_QNAME[34], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "borderBox" element
     */
    @Override
    public int sizeOfBorderBoxArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[34]);
        }
    }

    /**
     * Sets array of all "borderBox" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setBorderBoxArray(org.openxmlformats.schemas.officeDocument.x2006.math.CTBorderBox[] borderBoxArray) {
        check_orphaned();
        arraySetterHelper(borderBoxArray, PROPERTY_QNAME[34]);
    }

    /**
     * Sets ith "borderBox" element
     */
    @Override
    public void setBorderBoxArray(int i, org.openxmlformats.schemas.officeDocument.x2006.math.CTBorderBox borderBox) {
        generatedSetterHelperImpl(borderBox, PROPERTY_QNAME[34], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "borderBox" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTBorderBox insertNewBorderBox(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTBorderBox target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTBorderBox)get_store().insert_element_user(PROPERTY_QNAME[34], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "borderBox" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTBorderBox addNewBorderBox() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTBorderBox target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTBorderBox)get_store().add_element_user(PROPERTY_QNAME[34]);
            return target;
        }
    }

    /**
     * Removes the ith "borderBox" element
     */
    @Override
    public void removeBorderBox(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[34], i);
        }
    }

    /**
     * Gets a List of "d" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.math.CTD> getDList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getDArray,
                this::setDArray,
                this::insertNewD,
                this::removeD,
                this::sizeOfDArray
            );
        }
    }

    /**
     * Gets array of all "d" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTD[] getDArray() {
        return getXmlObjectArray(PROPERTY_QNAME[35], new org.openxmlformats.schemas.officeDocument.x2006.math.CTD[0]);
    }

    /**
     * Gets ith "d" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTD getDArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTD target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTD)get_store().find_element_user(PROPERTY_QNAME[35], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "d" element
     */
    @Override
    public int sizeOfDArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[35]);
        }
    }

    /**
     * Sets array of all "d" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setDArray(org.openxmlformats.schemas.officeDocument.x2006.math.CTD[] dArray) {
        check_orphaned();
        arraySetterHelper(dArray, PROPERTY_QNAME[35]);
    }

    /**
     * Sets ith "d" element
     */
    @Override
    public void setDArray(int i, org.openxmlformats.schemas.officeDocument.x2006.math.CTD d) {
        generatedSetterHelperImpl(d, PROPERTY_QNAME[35], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "d" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTD insertNewD(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTD target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTD)get_store().insert_element_user(PROPERTY_QNAME[35], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "d" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTD addNewD() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTD target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTD)get_store().add_element_user(PROPERTY_QNAME[35]);
            return target;
        }
    }

    /**
     * Removes the ith "d" element
     */
    @Override
    public void removeD(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[35], i);
        }
    }

    /**
     * Gets a List of "eqArr" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.math.CTEqArr> getEqArrList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getEqArrArray,
                this::setEqArrArray,
                this::insertNewEqArr,
                this::removeEqArr,
                this::sizeOfEqArrArray
            );
        }
    }

    /**
     * Gets array of all "eqArr" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTEqArr[] getEqArrArray() {
        return getXmlObjectArray(PROPERTY_QNAME[36], new org.openxmlformats.schemas.officeDocument.x2006.math.CTEqArr[0]);
    }

    /**
     * Gets ith "eqArr" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTEqArr getEqArrArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTEqArr target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTEqArr)get_store().find_element_user(PROPERTY_QNAME[36], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "eqArr" element
     */
    @Override
    public int sizeOfEqArrArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[36]);
        }
    }

    /**
     * Sets array of all "eqArr" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setEqArrArray(org.openxmlformats.schemas.officeDocument.x2006.math.CTEqArr[] eqArrArray) {
        check_orphaned();
        arraySetterHelper(eqArrArray, PROPERTY_QNAME[36]);
    }

    /**
     * Sets ith "eqArr" element
     */
    @Override
    public void setEqArrArray(int i, org.openxmlformats.schemas.officeDocument.x2006.math.CTEqArr eqArr) {
        generatedSetterHelperImpl(eqArr, PROPERTY_QNAME[36], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "eqArr" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTEqArr insertNewEqArr(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTEqArr target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTEqArr)get_store().insert_element_user(PROPERTY_QNAME[36], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "eqArr" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTEqArr addNewEqArr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTEqArr target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTEqArr)get_store().add_element_user(PROPERTY_QNAME[36]);
            return target;
        }
    }

    /**
     * Removes the ith "eqArr" element
     */
    @Override
    public void removeEqArr(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[36], i);
        }
    }

    /**
     * Gets a List of "f" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.math.CTF> getFList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getFArray,
                this::setFArray,
                this::insertNewF,
                this::removeF,
                this::sizeOfFArray
            );
        }
    }

    /**
     * Gets array of all "f" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTF[] getFArray() {
        return getXmlObjectArray(PROPERTY_QNAME[37], new org.openxmlformats.schemas.officeDocument.x2006.math.CTF[0]);
    }

    /**
     * Gets ith "f" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTF getFArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTF target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTF)get_store().find_element_user(PROPERTY_QNAME[37], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "f" element
     */
    @Override
    public int sizeOfFArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[37]);
        }
    }

    /**
     * Sets array of all "f" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setFArray(org.openxmlformats.schemas.officeDocument.x2006.math.CTF[] fArray) {
        check_orphaned();
        arraySetterHelper(fArray, PROPERTY_QNAME[37]);
    }

    /**
     * Sets ith "f" element
     */
    @Override
    public void setFArray(int i, org.openxmlformats.schemas.officeDocument.x2006.math.CTF f) {
        generatedSetterHelperImpl(f, PROPERTY_QNAME[37], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "f" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTF insertNewF(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTF target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTF)get_store().insert_element_user(PROPERTY_QNAME[37], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "f" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTF addNewF() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTF target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTF)get_store().add_element_user(PROPERTY_QNAME[37]);
            return target;
        }
    }

    /**
     * Removes the ith "f" element
     */
    @Override
    public void removeF(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[37], i);
        }
    }

    /**
     * Gets a List of "func" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.math.CTFunc> getFuncList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getFuncArray,
                this::setFuncArray,
                this::insertNewFunc,
                this::removeFunc,
                this::sizeOfFuncArray
            );
        }
    }

    /**
     * Gets array of all "func" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTFunc[] getFuncArray() {
        return getXmlObjectArray(PROPERTY_QNAME[38], new org.openxmlformats.schemas.officeDocument.x2006.math.CTFunc[0]);
    }

    /**
     * Gets ith "func" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTFunc getFuncArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTFunc target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTFunc)get_store().find_element_user(PROPERTY_QNAME[38], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "func" element
     */
    @Override
    public int sizeOfFuncArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[38]);
        }
    }

    /**
     * Sets array of all "func" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setFuncArray(org.openxmlformats.schemas.officeDocument.x2006.math.CTFunc[] funcArray) {
        check_orphaned();
        arraySetterHelper(funcArray, PROPERTY_QNAME[38]);
    }

    /**
     * Sets ith "func" element
     */
    @Override
    public void setFuncArray(int i, org.openxmlformats.schemas.officeDocument.x2006.math.CTFunc func) {
        generatedSetterHelperImpl(func, PROPERTY_QNAME[38], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "func" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTFunc insertNewFunc(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTFunc target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTFunc)get_store().insert_element_user(PROPERTY_QNAME[38], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "func" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTFunc addNewFunc() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTFunc target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTFunc)get_store().add_element_user(PROPERTY_QNAME[38]);
            return target;
        }
    }

    /**
     * Removes the ith "func" element
     */
    @Override
    public void removeFunc(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[38], i);
        }
    }

    /**
     * Gets a List of "groupChr" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.math.CTGroupChr> getGroupChrList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getGroupChrArray,
                this::setGroupChrArray,
                this::insertNewGroupChr,
                this::removeGroupChr,
                this::sizeOfGroupChrArray
            );
        }
    }

    /**
     * Gets array of all "groupChr" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTGroupChr[] getGroupChrArray() {
        return getXmlObjectArray(PROPERTY_QNAME[39], new org.openxmlformats.schemas.officeDocument.x2006.math.CTGroupChr[0]);
    }

    /**
     * Gets ith "groupChr" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTGroupChr getGroupChrArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTGroupChr target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTGroupChr)get_store().find_element_user(PROPERTY_QNAME[39], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "groupChr" element
     */
    @Override
    public int sizeOfGroupChrArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[39]);
        }
    }

    /**
     * Sets array of all "groupChr" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setGroupChrArray(org.openxmlformats.schemas.officeDocument.x2006.math.CTGroupChr[] groupChrArray) {
        check_orphaned();
        arraySetterHelper(groupChrArray, PROPERTY_QNAME[39]);
    }

    /**
     * Sets ith "groupChr" element
     */
    @Override
    public void setGroupChrArray(int i, org.openxmlformats.schemas.officeDocument.x2006.math.CTGroupChr groupChr) {
        generatedSetterHelperImpl(groupChr, PROPERTY_QNAME[39], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "groupChr" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTGroupChr insertNewGroupChr(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTGroupChr target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTGroupChr)get_store().insert_element_user(PROPERTY_QNAME[39], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "groupChr" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTGroupChr addNewGroupChr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTGroupChr target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTGroupChr)get_store().add_element_user(PROPERTY_QNAME[39]);
            return target;
        }
    }

    /**
     * Removes the ith "groupChr" element
     */
    @Override
    public void removeGroupChr(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[39], i);
        }
    }

    /**
     * Gets a List of "limLow" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.math.CTLimLow> getLimLowList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getLimLowArray,
                this::setLimLowArray,
                this::insertNewLimLow,
                this::removeLimLow,
                this::sizeOfLimLowArray
            );
        }
    }

    /**
     * Gets array of all "limLow" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTLimLow[] getLimLowArray() {
        return getXmlObjectArray(PROPERTY_QNAME[40], new org.openxmlformats.schemas.officeDocument.x2006.math.CTLimLow[0]);
    }

    /**
     * Gets ith "limLow" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTLimLow getLimLowArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTLimLow target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTLimLow)get_store().find_element_user(PROPERTY_QNAME[40], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "limLow" element
     */
    @Override
    public int sizeOfLimLowArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[40]);
        }
    }

    /**
     * Sets array of all "limLow" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setLimLowArray(org.openxmlformats.schemas.officeDocument.x2006.math.CTLimLow[] limLowArray) {
        check_orphaned();
        arraySetterHelper(limLowArray, PROPERTY_QNAME[40]);
    }

    /**
     * Sets ith "limLow" element
     */
    @Override
    public void setLimLowArray(int i, org.openxmlformats.schemas.officeDocument.x2006.math.CTLimLow limLow) {
        generatedSetterHelperImpl(limLow, PROPERTY_QNAME[40], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "limLow" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTLimLow insertNewLimLow(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTLimLow target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTLimLow)get_store().insert_element_user(PROPERTY_QNAME[40], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "limLow" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTLimLow addNewLimLow() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTLimLow target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTLimLow)get_store().add_element_user(PROPERTY_QNAME[40]);
            return target;
        }
    }

    /**
     * Removes the ith "limLow" element
     */
    @Override
    public void removeLimLow(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[40], i);
        }
    }

    /**
     * Gets a List of "limUpp" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.math.CTLimUpp> getLimUppList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getLimUppArray,
                this::setLimUppArray,
                this::insertNewLimUpp,
                this::removeLimUpp,
                this::sizeOfLimUppArray
            );
        }
    }

    /**
     * Gets array of all "limUpp" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTLimUpp[] getLimUppArray() {
        return getXmlObjectArray(PROPERTY_QNAME[41], new org.openxmlformats.schemas.officeDocument.x2006.math.CTLimUpp[0]);
    }

    /**
     * Gets ith "limUpp" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTLimUpp getLimUppArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTLimUpp target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTLimUpp)get_store().find_element_user(PROPERTY_QNAME[41], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "limUpp" element
     */
    @Override
    public int sizeOfLimUppArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[41]);
        }
    }

    /**
     * Sets array of all "limUpp" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setLimUppArray(org.openxmlformats.schemas.officeDocument.x2006.math.CTLimUpp[] limUppArray) {
        check_orphaned();
        arraySetterHelper(limUppArray, PROPERTY_QNAME[41]);
    }

    /**
     * Sets ith "limUpp" element
     */
    @Override
    public void setLimUppArray(int i, org.openxmlformats.schemas.officeDocument.x2006.math.CTLimUpp limUpp) {
        generatedSetterHelperImpl(limUpp, PROPERTY_QNAME[41], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "limUpp" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTLimUpp insertNewLimUpp(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTLimUpp target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTLimUpp)get_store().insert_element_user(PROPERTY_QNAME[41], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "limUpp" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTLimUpp addNewLimUpp() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTLimUpp target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTLimUpp)get_store().add_element_user(PROPERTY_QNAME[41]);
            return target;
        }
    }

    /**
     * Removes the ith "limUpp" element
     */
    @Override
    public void removeLimUpp(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[41], i);
        }
    }

    /**
     * Gets a List of "m" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.math.CTM> getMList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getMArray,
                this::setMArray,
                this::insertNewM,
                this::removeM,
                this::sizeOfMArray
            );
        }
    }

    /**
     * Gets array of all "m" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTM[] getMArray() {
        return getXmlObjectArray(PROPERTY_QNAME[42], new org.openxmlformats.schemas.officeDocument.x2006.math.CTM[0]);
    }

    /**
     * Gets ith "m" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTM getMArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTM target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTM)get_store().find_element_user(PROPERTY_QNAME[42], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "m" element
     */
    @Override
    public int sizeOfMArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[42]);
        }
    }

    /**
     * Sets array of all "m" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setMArray(org.openxmlformats.schemas.officeDocument.x2006.math.CTM[] mArray) {
        check_orphaned();
        arraySetterHelper(mArray, PROPERTY_QNAME[42]);
    }

    /**
     * Sets ith "m" element
     */
    @Override
    public void setMArray(int i, org.openxmlformats.schemas.officeDocument.x2006.math.CTM m) {
        generatedSetterHelperImpl(m, PROPERTY_QNAME[42], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "m" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTM insertNewM(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTM target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTM)get_store().insert_element_user(PROPERTY_QNAME[42], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "m" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTM addNewM() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTM target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTM)get_store().add_element_user(PROPERTY_QNAME[42]);
            return target;
        }
    }

    /**
     * Removes the ith "m" element
     */
    @Override
    public void removeM(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[42], i);
        }
    }

    /**
     * Gets a List of "nary" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.math.CTNary> getNaryList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getNaryArray,
                this::setNaryArray,
                this::insertNewNary,
                this::removeNary,
                this::sizeOfNaryArray
            );
        }
    }

    /**
     * Gets array of all "nary" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTNary[] getNaryArray() {
        return getXmlObjectArray(PROPERTY_QNAME[43], new org.openxmlformats.schemas.officeDocument.x2006.math.CTNary[0]);
    }

    /**
     * Gets ith "nary" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTNary getNaryArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTNary target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTNary)get_store().find_element_user(PROPERTY_QNAME[43], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "nary" element
     */
    @Override
    public int sizeOfNaryArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[43]);
        }
    }

    /**
     * Sets array of all "nary" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setNaryArray(org.openxmlformats.schemas.officeDocument.x2006.math.CTNary[] naryArray) {
        check_orphaned();
        arraySetterHelper(naryArray, PROPERTY_QNAME[43]);
    }

    /**
     * Sets ith "nary" element
     */
    @Override
    public void setNaryArray(int i, org.openxmlformats.schemas.officeDocument.x2006.math.CTNary nary) {
        generatedSetterHelperImpl(nary, PROPERTY_QNAME[43], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "nary" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTNary insertNewNary(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTNary target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTNary)get_store().insert_element_user(PROPERTY_QNAME[43], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "nary" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTNary addNewNary() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTNary target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTNary)get_store().add_element_user(PROPERTY_QNAME[43]);
            return target;
        }
    }

    /**
     * Removes the ith "nary" element
     */
    @Override
    public void removeNary(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[43], i);
        }
    }

    /**
     * Gets a List of "phant" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.math.CTPhant> getPhantList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getPhantArray,
                this::setPhantArray,
                this::insertNewPhant,
                this::removePhant,
                this::sizeOfPhantArray
            );
        }
    }

    /**
     * Gets array of all "phant" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTPhant[] getPhantArray() {
        return getXmlObjectArray(PROPERTY_QNAME[44], new org.openxmlformats.schemas.officeDocument.x2006.math.CTPhant[0]);
    }

    /**
     * Gets ith "phant" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTPhant getPhantArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTPhant target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTPhant)get_store().find_element_user(PROPERTY_QNAME[44], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "phant" element
     */
    @Override
    public int sizeOfPhantArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[44]);
        }
    }

    /**
     * Sets array of all "phant" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setPhantArray(org.openxmlformats.schemas.officeDocument.x2006.math.CTPhant[] phantArray) {
        check_orphaned();
        arraySetterHelper(phantArray, PROPERTY_QNAME[44]);
    }

    /**
     * Sets ith "phant" element
     */
    @Override
    public void setPhantArray(int i, org.openxmlformats.schemas.officeDocument.x2006.math.CTPhant phant) {
        generatedSetterHelperImpl(phant, PROPERTY_QNAME[44], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "phant" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTPhant insertNewPhant(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTPhant target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTPhant)get_store().insert_element_user(PROPERTY_QNAME[44], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "phant" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTPhant addNewPhant() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTPhant target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTPhant)get_store().add_element_user(PROPERTY_QNAME[44]);
            return target;
        }
    }

    /**
     * Removes the ith "phant" element
     */
    @Override
    public void removePhant(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[44], i);
        }
    }

    /**
     * Gets a List of "rad" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.math.CTRad> getRadList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getRadArray,
                this::setRadArray,
                this::insertNewRad,
                this::removeRad,
                this::sizeOfRadArray
            );
        }
    }

    /**
     * Gets array of all "rad" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTRad[] getRadArray() {
        return getXmlObjectArray(PROPERTY_QNAME[45], new org.openxmlformats.schemas.officeDocument.x2006.math.CTRad[0]);
    }

    /**
     * Gets ith "rad" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTRad getRadArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTRad target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTRad)get_store().find_element_user(PROPERTY_QNAME[45], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "rad" element
     */
    @Override
    public int sizeOfRadArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[45]);
        }
    }

    /**
     * Sets array of all "rad" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setRadArray(org.openxmlformats.schemas.officeDocument.x2006.math.CTRad[] radArray) {
        check_orphaned();
        arraySetterHelper(radArray, PROPERTY_QNAME[45]);
    }

    /**
     * Sets ith "rad" element
     */
    @Override
    public void setRadArray(int i, org.openxmlformats.schemas.officeDocument.x2006.math.CTRad rad) {
        generatedSetterHelperImpl(rad, PROPERTY_QNAME[45], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "rad" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTRad insertNewRad(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTRad target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTRad)get_store().insert_element_user(PROPERTY_QNAME[45], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "rad" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTRad addNewRad() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTRad target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTRad)get_store().add_element_user(PROPERTY_QNAME[45]);
            return target;
        }
    }

    /**
     * Removes the ith "rad" element
     */
    @Override
    public void removeRad(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[45], i);
        }
    }

    /**
     * Gets a List of "sPre" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.math.CTSPre> getSPreList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getSPreArray,
                this::setSPreArray,
                this::insertNewSPre,
                this::removeSPre,
                this::sizeOfSPreArray
            );
        }
    }

    /**
     * Gets array of all "sPre" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTSPre[] getSPreArray() {
        return getXmlObjectArray(PROPERTY_QNAME[46], new org.openxmlformats.schemas.officeDocument.x2006.math.CTSPre[0]);
    }

    /**
     * Gets ith "sPre" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTSPre getSPreArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTSPre target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTSPre)get_store().find_element_user(PROPERTY_QNAME[46], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "sPre" element
     */
    @Override
    public int sizeOfSPreArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[46]);
        }
    }

    /**
     * Sets array of all "sPre" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSPreArray(org.openxmlformats.schemas.officeDocument.x2006.math.CTSPre[] sPreArray) {
        check_orphaned();
        arraySetterHelper(sPreArray, PROPERTY_QNAME[46]);
    }

    /**
     * Sets ith "sPre" element
     */
    @Override
    public void setSPreArray(int i, org.openxmlformats.schemas.officeDocument.x2006.math.CTSPre sPre) {
        generatedSetterHelperImpl(sPre, PROPERTY_QNAME[46], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "sPre" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTSPre insertNewSPre(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTSPre target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTSPre)get_store().insert_element_user(PROPERTY_QNAME[46], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "sPre" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTSPre addNewSPre() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTSPre target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTSPre)get_store().add_element_user(PROPERTY_QNAME[46]);
            return target;
        }
    }

    /**
     * Removes the ith "sPre" element
     */
    @Override
    public void removeSPre(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[46], i);
        }
    }

    /**
     * Gets a List of "sSub" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.math.CTSSub> getSSubList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getSSubArray,
                this::setSSubArray,
                this::insertNewSSub,
                this::removeSSub,
                this::sizeOfSSubArray
            );
        }
    }

    /**
     * Gets array of all "sSub" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTSSub[] getSSubArray() {
        return getXmlObjectArray(PROPERTY_QNAME[47], new org.openxmlformats.schemas.officeDocument.x2006.math.CTSSub[0]);
    }

    /**
     * Gets ith "sSub" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTSSub getSSubArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTSSub target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTSSub)get_store().find_element_user(PROPERTY_QNAME[47], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "sSub" element
     */
    @Override
    public int sizeOfSSubArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[47]);
        }
    }

    /**
     * Sets array of all "sSub" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSSubArray(org.openxmlformats.schemas.officeDocument.x2006.math.CTSSub[] sSubArray) {
        check_orphaned();
        arraySetterHelper(sSubArray, PROPERTY_QNAME[47]);
    }

    /**
     * Sets ith "sSub" element
     */
    @Override
    public void setSSubArray(int i, org.openxmlformats.schemas.officeDocument.x2006.math.CTSSub sSub) {
        generatedSetterHelperImpl(sSub, PROPERTY_QNAME[47], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "sSub" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTSSub insertNewSSub(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTSSub target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTSSub)get_store().insert_element_user(PROPERTY_QNAME[47], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "sSub" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTSSub addNewSSub() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTSSub target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTSSub)get_store().add_element_user(PROPERTY_QNAME[47]);
            return target;
        }
    }

    /**
     * Removes the ith "sSub" element
     */
    @Override
    public void removeSSub(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[47], i);
        }
    }

    /**
     * Gets a List of "sSubSup" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.math.CTSSubSup> getSSubSupList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getSSubSupArray,
                this::setSSubSupArray,
                this::insertNewSSubSup,
                this::removeSSubSup,
                this::sizeOfSSubSupArray
            );
        }
    }

    /**
     * Gets array of all "sSubSup" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTSSubSup[] getSSubSupArray() {
        return getXmlObjectArray(PROPERTY_QNAME[48], new org.openxmlformats.schemas.officeDocument.x2006.math.CTSSubSup[0]);
    }

    /**
     * Gets ith "sSubSup" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTSSubSup getSSubSupArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTSSubSup target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTSSubSup)get_store().find_element_user(PROPERTY_QNAME[48], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "sSubSup" element
     */
    @Override
    public int sizeOfSSubSupArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[48]);
        }
    }

    /**
     * Sets array of all "sSubSup" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSSubSupArray(org.openxmlformats.schemas.officeDocument.x2006.math.CTSSubSup[] sSubSupArray) {
        check_orphaned();
        arraySetterHelper(sSubSupArray, PROPERTY_QNAME[48]);
    }

    /**
     * Sets ith "sSubSup" element
     */
    @Override
    public void setSSubSupArray(int i, org.openxmlformats.schemas.officeDocument.x2006.math.CTSSubSup sSubSup) {
        generatedSetterHelperImpl(sSubSup, PROPERTY_QNAME[48], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "sSubSup" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTSSubSup insertNewSSubSup(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTSSubSup target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTSSubSup)get_store().insert_element_user(PROPERTY_QNAME[48], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "sSubSup" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTSSubSup addNewSSubSup() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTSSubSup target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTSSubSup)get_store().add_element_user(PROPERTY_QNAME[48]);
            return target;
        }
    }

    /**
     * Removes the ith "sSubSup" element
     */
    @Override
    public void removeSSubSup(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[48], i);
        }
    }

    /**
     * Gets a List of "sSup" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.math.CTSSup> getSSupList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getSSupArray,
                this::setSSupArray,
                this::insertNewSSup,
                this::removeSSup,
                this::sizeOfSSupArray
            );
        }
    }

    /**
     * Gets array of all "sSup" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTSSup[] getSSupArray() {
        return getXmlObjectArray(PROPERTY_QNAME[49], new org.openxmlformats.schemas.officeDocument.x2006.math.CTSSup[0]);
    }

    /**
     * Gets ith "sSup" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTSSup getSSupArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTSSup target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTSSup)get_store().find_element_user(PROPERTY_QNAME[49], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "sSup" element
     */
    @Override
    public int sizeOfSSupArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[49]);
        }
    }

    /**
     * Sets array of all "sSup" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSSupArray(org.openxmlformats.schemas.officeDocument.x2006.math.CTSSup[] sSupArray) {
        check_orphaned();
        arraySetterHelper(sSupArray, PROPERTY_QNAME[49]);
    }

    /**
     * Sets ith "sSup" element
     */
    @Override
    public void setSSupArray(int i, org.openxmlformats.schemas.officeDocument.x2006.math.CTSSup sSup) {
        generatedSetterHelperImpl(sSup, PROPERTY_QNAME[49], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "sSup" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTSSup insertNewSSup(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTSSup target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTSSup)get_store().insert_element_user(PROPERTY_QNAME[49], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "sSup" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTSSup addNewSSup() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTSSup target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTSSup)get_store().add_element_user(PROPERTY_QNAME[49]);
            return target;
        }
    }

    /**
     * Removes the ith "sSup" element
     */
    @Override
    public void removeSSup(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[49], i);
        }
    }

    /**
     * Gets a List of "r" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.math.CTR> getR2List() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getR2Array,
                this::setR2Array,
                this::insertNewR2,
                this::removeR2,
                this::sizeOfR2Array
            );
        }
    }

    /**
     * Gets array of all "r" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTR[] getR2Array() {
        return getXmlObjectArray(PROPERTY_QNAME[50], new org.openxmlformats.schemas.officeDocument.x2006.math.CTR[0]);
    }

    /**
     * Gets ith "r" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTR getR2Array(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTR target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTR)get_store().find_element_user(PROPERTY_QNAME[50], i);
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
    public int sizeOfR2Array() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[50]);
        }
    }

    /**
     * Sets array of all "r" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setR2Array(org.openxmlformats.schemas.officeDocument.x2006.math.CTR[] r2Array) {
        check_orphaned();
        arraySetterHelper(r2Array, PROPERTY_QNAME[50]);
    }

    /**
     * Sets ith "r" element
     */
    @Override
    public void setR2Array(int i, org.openxmlformats.schemas.officeDocument.x2006.math.CTR r2) {
        generatedSetterHelperImpl(r2, PROPERTY_QNAME[50], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "r" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTR insertNewR2(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTR target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTR)get_store().insert_element_user(PROPERTY_QNAME[50], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "r" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTR addNewR2() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTR target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTR)get_store().add_element_user(PROPERTY_QNAME[50]);
            return target;
        }
    }

    /**
     * Removes the ith "r" element
     */
    @Override
    public void removeR2(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[50], i);
        }
    }
}
