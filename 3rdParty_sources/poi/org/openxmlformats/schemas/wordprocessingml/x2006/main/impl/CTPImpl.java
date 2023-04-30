/*
 * XML Type:  CT_P
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_P(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTPImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP {
    private static final long serialVersionUID = 1L;

    public CTPImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "pPr"),
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
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "fldSimple"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "hyperlink"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "subDoc"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "rsidRPr"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "rsidR"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "rsidDel"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "rsidP"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "rsidRDefault"),
    };


    /**
     * Gets the "pPr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr getPPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "pPr" element
     */
    @Override
    public boolean isSetPPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "pPr" element
     */
    @Override
    public void setPPr(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr pPr) {
        generatedSetterHelperImpl(pPr, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "pPr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr addNewPPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "pPr" element
     */
    @Override
    public void unsetPPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

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
        return getXmlObjectArray(PROPERTY_QNAME[1], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCustomXmlRun[0]);
    }

    /**
     * Gets ith "customXml" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCustomXmlRun getCustomXmlArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCustomXmlRun target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCustomXmlRun)get_store().find_element_user(PROPERTY_QNAME[1], i);
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
            return get_store().count_elements(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets array of all "customXml" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCustomXmlArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCustomXmlRun[] customXmlArray) {
        check_orphaned();
        arraySetterHelper(customXmlArray, PROPERTY_QNAME[1]);
    }

    /**
     * Sets ith "customXml" element
     */
    @Override
    public void setCustomXmlArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCustomXmlRun customXml) {
        generatedSetterHelperImpl(customXml, PROPERTY_QNAME[1], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "customXml" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCustomXmlRun insertNewCustomXml(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCustomXmlRun target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCustomXmlRun)get_store().insert_element_user(PROPERTY_QNAME[1], i);
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
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCustomXmlRun)get_store().add_element_user(PROPERTY_QNAME[1]);
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
            get_store().remove_element(PROPERTY_QNAME[1], i);
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
        return getXmlObjectArray(PROPERTY_QNAME[2], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSmartTagRun[0]);
    }

    /**
     * Gets ith "smartTag" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSmartTagRun getSmartTagArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSmartTagRun target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSmartTagRun)get_store().find_element_user(PROPERTY_QNAME[2], i);
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
            return get_store().count_elements(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Sets array of all "smartTag" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSmartTagArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSmartTagRun[] smartTagArray) {
        check_orphaned();
        arraySetterHelper(smartTagArray, PROPERTY_QNAME[2]);
    }

    /**
     * Sets ith "smartTag" element
     */
    @Override
    public void setSmartTagArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSmartTagRun smartTag) {
        generatedSetterHelperImpl(smartTag, PROPERTY_QNAME[2], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "smartTag" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSmartTagRun insertNewSmartTag(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSmartTagRun target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSmartTagRun)get_store().insert_element_user(PROPERTY_QNAME[2], i);
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
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSmartTagRun)get_store().add_element_user(PROPERTY_QNAME[2]);
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
            get_store().remove_element(PROPERTY_QNAME[2], i);
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
        return getXmlObjectArray(PROPERTY_QNAME[3], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtRun[0]);
    }

    /**
     * Gets ith "sdt" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtRun getSdtArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtRun target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtRun)get_store().find_element_user(PROPERTY_QNAME[3], i);
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
            return get_store().count_elements(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Sets array of all "sdt" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSdtArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtRun[] sdtArray) {
        check_orphaned();
        arraySetterHelper(sdtArray, PROPERTY_QNAME[3]);
    }

    /**
     * Sets ith "sdt" element
     */
    @Override
    public void setSdtArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtRun sdt) {
        generatedSetterHelperImpl(sdt, PROPERTY_QNAME[3], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "sdt" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtRun insertNewSdt(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtRun target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtRun)get_store().insert_element_user(PROPERTY_QNAME[3], i);
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
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtRun)get_store().add_element_user(PROPERTY_QNAME[3]);
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
            get_store().remove_element(PROPERTY_QNAME[3], i);
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
        return getXmlObjectArray(PROPERTY_QNAME[4], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDirContentRun[0]);
    }

    /**
     * Gets ith "dir" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDirContentRun getDirArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDirContentRun target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDirContentRun)get_store().find_element_user(PROPERTY_QNAME[4], i);
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
            return get_store().count_elements(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Sets array of all "dir" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setDirArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDirContentRun[] dirArray) {
        check_orphaned();
        arraySetterHelper(dirArray, PROPERTY_QNAME[4]);
    }

    /**
     * Sets ith "dir" element
     */
    @Override
    public void setDirArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDirContentRun dir) {
        generatedSetterHelperImpl(dir, PROPERTY_QNAME[4], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "dir" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDirContentRun insertNewDir(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDirContentRun target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDirContentRun)get_store().insert_element_user(PROPERTY_QNAME[4], i);
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
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDirContentRun)get_store().add_element_user(PROPERTY_QNAME[4]);
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
            get_store().remove_element(PROPERTY_QNAME[4], i);
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
        return getXmlObjectArray(PROPERTY_QNAME[5], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBdoContentRun[0]);
    }

    /**
     * Gets ith "bdo" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBdoContentRun getBdoArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBdoContentRun target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBdoContentRun)get_store().find_element_user(PROPERTY_QNAME[5], i);
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
            return get_store().count_elements(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Sets array of all "bdo" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setBdoArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBdoContentRun[] bdoArray) {
        check_orphaned();
        arraySetterHelper(bdoArray, PROPERTY_QNAME[5]);
    }

    /**
     * Sets ith "bdo" element
     */
    @Override
    public void setBdoArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBdoContentRun bdo) {
        generatedSetterHelperImpl(bdo, PROPERTY_QNAME[5], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "bdo" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBdoContentRun insertNewBdo(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBdoContentRun target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBdoContentRun)get_store().insert_element_user(PROPERTY_QNAME[5], i);
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
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBdoContentRun)get_store().add_element_user(PROPERTY_QNAME[5]);
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
            get_store().remove_element(PROPERTY_QNAME[5], i);
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
        return getXmlObjectArray(PROPERTY_QNAME[6], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR[0]);
    }

    /**
     * Gets ith "r" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR getRArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR)get_store().find_element_user(PROPERTY_QNAME[6], i);
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
            return get_store().count_elements(PROPERTY_QNAME[6]);
        }
    }

    /**
     * Sets array of all "r" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setRArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR[] rArray) {
        check_orphaned();
        arraySetterHelper(rArray, PROPERTY_QNAME[6]);
    }

    /**
     * Sets ith "r" element
     */
    @Override
    public void setRArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR r) {
        generatedSetterHelperImpl(r, PROPERTY_QNAME[6], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "r" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR insertNewR(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR)get_store().insert_element_user(PROPERTY_QNAME[6], i);
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
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR)get_store().add_element_user(PROPERTY_QNAME[6]);
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
            get_store().remove_element(PROPERTY_QNAME[6], i);
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
        return getXmlObjectArray(PROPERTY_QNAME[7], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTProofErr[0]);
    }

    /**
     * Gets ith "proofErr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTProofErr getProofErrArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTProofErr target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTProofErr)get_store().find_element_user(PROPERTY_QNAME[7], i);
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
            return get_store().count_elements(PROPERTY_QNAME[7]);
        }
    }

    /**
     * Sets array of all "proofErr" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setProofErrArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTProofErr[] proofErrArray) {
        check_orphaned();
        arraySetterHelper(proofErrArray, PROPERTY_QNAME[7]);
    }

    /**
     * Sets ith "proofErr" element
     */
    @Override
    public void setProofErrArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTProofErr proofErr) {
        generatedSetterHelperImpl(proofErr, PROPERTY_QNAME[7], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "proofErr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTProofErr insertNewProofErr(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTProofErr target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTProofErr)get_store().insert_element_user(PROPERTY_QNAME[7], i);
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
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTProofErr)get_store().add_element_user(PROPERTY_QNAME[7]);
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
            get_store().remove_element(PROPERTY_QNAME[7], i);
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
        return getXmlObjectArray(PROPERTY_QNAME[8], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPermStart[0]);
    }

    /**
     * Gets ith "permStart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPermStart getPermStartArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPermStart target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPermStart)get_store().find_element_user(PROPERTY_QNAME[8], i);
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
            return get_store().count_elements(PROPERTY_QNAME[8]);
        }
    }

    /**
     * Sets array of all "permStart" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setPermStartArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPermStart[] permStartArray) {
        check_orphaned();
        arraySetterHelper(permStartArray, PROPERTY_QNAME[8]);
    }

    /**
     * Sets ith "permStart" element
     */
    @Override
    public void setPermStartArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPermStart permStart) {
        generatedSetterHelperImpl(permStart, PROPERTY_QNAME[8], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "permStart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPermStart insertNewPermStart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPermStart target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPermStart)get_store().insert_element_user(PROPERTY_QNAME[8], i);
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
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPermStart)get_store().add_element_user(PROPERTY_QNAME[8]);
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
            get_store().remove_element(PROPERTY_QNAME[8], i);
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
        return getXmlObjectArray(PROPERTY_QNAME[9], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPerm[0]);
    }

    /**
     * Gets ith "permEnd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPerm getPermEndArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPerm target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPerm)get_store().find_element_user(PROPERTY_QNAME[9], i);
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
            return get_store().count_elements(PROPERTY_QNAME[9]);
        }
    }

    /**
     * Sets array of all "permEnd" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setPermEndArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPerm[] permEndArray) {
        check_orphaned();
        arraySetterHelper(permEndArray, PROPERTY_QNAME[9]);
    }

    /**
     * Sets ith "permEnd" element
     */
    @Override
    public void setPermEndArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPerm permEnd) {
        generatedSetterHelperImpl(permEnd, PROPERTY_QNAME[9], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "permEnd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPerm insertNewPermEnd(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPerm target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPerm)get_store().insert_element_user(PROPERTY_QNAME[9], i);
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
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPerm)get_store().add_element_user(PROPERTY_QNAME[9]);
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
            get_store().remove_element(PROPERTY_QNAME[9], i);
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
        return getXmlObjectArray(PROPERTY_QNAME[10], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark[0]);
    }

    /**
     * Gets ith "bookmarkStart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark getBookmarkStartArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark)get_store().find_element_user(PROPERTY_QNAME[10], i);
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
            return get_store().count_elements(PROPERTY_QNAME[10]);
        }
    }

    /**
     * Sets array of all "bookmarkStart" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setBookmarkStartArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark[] bookmarkStartArray) {
        check_orphaned();
        arraySetterHelper(bookmarkStartArray, PROPERTY_QNAME[10]);
    }

    /**
     * Sets ith "bookmarkStart" element
     */
    @Override
    public void setBookmarkStartArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark bookmarkStart) {
        generatedSetterHelperImpl(bookmarkStart, PROPERTY_QNAME[10], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "bookmarkStart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark insertNewBookmarkStart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark)get_store().insert_element_user(PROPERTY_QNAME[10], i);
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
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark)get_store().add_element_user(PROPERTY_QNAME[10]);
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
            get_store().remove_element(PROPERTY_QNAME[10], i);
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
        return getXmlObjectArray(PROPERTY_QNAME[11], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange[0]);
    }

    /**
     * Gets ith "bookmarkEnd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange getBookmarkEndArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange)get_store().find_element_user(PROPERTY_QNAME[11], i);
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
            return get_store().count_elements(PROPERTY_QNAME[11]);
        }
    }

    /**
     * Sets array of all "bookmarkEnd" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setBookmarkEndArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange[] bookmarkEndArray) {
        check_orphaned();
        arraySetterHelper(bookmarkEndArray, PROPERTY_QNAME[11]);
    }

    /**
     * Sets ith "bookmarkEnd" element
     */
    @Override
    public void setBookmarkEndArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange bookmarkEnd) {
        generatedSetterHelperImpl(bookmarkEnd, PROPERTY_QNAME[11], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "bookmarkEnd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange insertNewBookmarkEnd(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange)get_store().insert_element_user(PROPERTY_QNAME[11], i);
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
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange)get_store().add_element_user(PROPERTY_QNAME[11]);
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
            get_store().remove_element(PROPERTY_QNAME[11], i);
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
        return getXmlObjectArray(PROPERTY_QNAME[12], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMoveBookmark[0]);
    }

    /**
     * Gets ith "moveFromRangeStart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMoveBookmark getMoveFromRangeStartArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMoveBookmark target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMoveBookmark)get_store().find_element_user(PROPERTY_QNAME[12], i);
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
            return get_store().count_elements(PROPERTY_QNAME[12]);
        }
    }

    /**
     * Sets array of all "moveFromRangeStart" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setMoveFromRangeStartArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMoveBookmark[] moveFromRangeStartArray) {
        check_orphaned();
        arraySetterHelper(moveFromRangeStartArray, PROPERTY_QNAME[12]);
    }

    /**
     * Sets ith "moveFromRangeStart" element
     */
    @Override
    public void setMoveFromRangeStartArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMoveBookmark moveFromRangeStart) {
        generatedSetterHelperImpl(moveFromRangeStart, PROPERTY_QNAME[12], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "moveFromRangeStart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMoveBookmark insertNewMoveFromRangeStart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMoveBookmark target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMoveBookmark)get_store().insert_element_user(PROPERTY_QNAME[12], i);
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
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMoveBookmark)get_store().add_element_user(PROPERTY_QNAME[12]);
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
            get_store().remove_element(PROPERTY_QNAME[12], i);
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
        return getXmlObjectArray(PROPERTY_QNAME[13], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange[0]);
    }

    /**
     * Gets ith "moveFromRangeEnd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange getMoveFromRangeEndArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange)get_store().find_element_user(PROPERTY_QNAME[13], i);
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
            return get_store().count_elements(PROPERTY_QNAME[13]);
        }
    }

    /**
     * Sets array of all "moveFromRangeEnd" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setMoveFromRangeEndArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange[] moveFromRangeEndArray) {
        check_orphaned();
        arraySetterHelper(moveFromRangeEndArray, PROPERTY_QNAME[13]);
    }

    /**
     * Sets ith "moveFromRangeEnd" element
     */
    @Override
    public void setMoveFromRangeEndArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange moveFromRangeEnd) {
        generatedSetterHelperImpl(moveFromRangeEnd, PROPERTY_QNAME[13], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "moveFromRangeEnd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange insertNewMoveFromRangeEnd(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange)get_store().insert_element_user(PROPERTY_QNAME[13], i);
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
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange)get_store().add_element_user(PROPERTY_QNAME[13]);
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
            get_store().remove_element(PROPERTY_QNAME[13], i);
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
        return getXmlObjectArray(PROPERTY_QNAME[14], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMoveBookmark[0]);
    }

    /**
     * Gets ith "moveToRangeStart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMoveBookmark getMoveToRangeStartArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMoveBookmark target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMoveBookmark)get_store().find_element_user(PROPERTY_QNAME[14], i);
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
            return get_store().count_elements(PROPERTY_QNAME[14]);
        }
    }

    /**
     * Sets array of all "moveToRangeStart" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setMoveToRangeStartArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMoveBookmark[] moveToRangeStartArray) {
        check_orphaned();
        arraySetterHelper(moveToRangeStartArray, PROPERTY_QNAME[14]);
    }

    /**
     * Sets ith "moveToRangeStart" element
     */
    @Override
    public void setMoveToRangeStartArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMoveBookmark moveToRangeStart) {
        generatedSetterHelperImpl(moveToRangeStart, PROPERTY_QNAME[14], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "moveToRangeStart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMoveBookmark insertNewMoveToRangeStart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMoveBookmark target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMoveBookmark)get_store().insert_element_user(PROPERTY_QNAME[14], i);
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
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMoveBookmark)get_store().add_element_user(PROPERTY_QNAME[14]);
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
            get_store().remove_element(PROPERTY_QNAME[14], i);
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
        return getXmlObjectArray(PROPERTY_QNAME[15], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange[0]);
    }

    /**
     * Gets ith "moveToRangeEnd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange getMoveToRangeEndArray(int i) {
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
     * Returns number of "moveToRangeEnd" element
     */
    @Override
    public int sizeOfMoveToRangeEndArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[15]);
        }
    }

    /**
     * Sets array of all "moveToRangeEnd" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setMoveToRangeEndArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange[] moveToRangeEndArray) {
        check_orphaned();
        arraySetterHelper(moveToRangeEndArray, PROPERTY_QNAME[15]);
    }

    /**
     * Sets ith "moveToRangeEnd" element
     */
    @Override
    public void setMoveToRangeEndArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange moveToRangeEnd) {
        generatedSetterHelperImpl(moveToRangeEnd, PROPERTY_QNAME[15], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "moveToRangeEnd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange insertNewMoveToRangeEnd(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange)get_store().insert_element_user(PROPERTY_QNAME[15], i);
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
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange)get_store().add_element_user(PROPERTY_QNAME[15]);
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
            get_store().remove_element(PROPERTY_QNAME[15], i);
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
        return getXmlObjectArray(PROPERTY_QNAME[16], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange[0]);
    }

    /**
     * Gets ith "commentRangeStart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange getCommentRangeStartArray(int i) {
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
     * Returns number of "commentRangeStart" element
     */
    @Override
    public int sizeOfCommentRangeStartArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[16]);
        }
    }

    /**
     * Sets array of all "commentRangeStart" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCommentRangeStartArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange[] commentRangeStartArray) {
        check_orphaned();
        arraySetterHelper(commentRangeStartArray, PROPERTY_QNAME[16]);
    }

    /**
     * Sets ith "commentRangeStart" element
     */
    @Override
    public void setCommentRangeStartArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange commentRangeStart) {
        generatedSetterHelperImpl(commentRangeStart, PROPERTY_QNAME[16], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "commentRangeStart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange insertNewCommentRangeStart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange)get_store().insert_element_user(PROPERTY_QNAME[16], i);
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
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange)get_store().add_element_user(PROPERTY_QNAME[16]);
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
            get_store().remove_element(PROPERTY_QNAME[16], i);
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
        return getXmlObjectArray(PROPERTY_QNAME[17], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange[0]);
    }

    /**
     * Gets ith "commentRangeEnd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange getCommentRangeEndArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange)get_store().find_element_user(PROPERTY_QNAME[17], i);
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
            return get_store().count_elements(PROPERTY_QNAME[17]);
        }
    }

    /**
     * Sets array of all "commentRangeEnd" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCommentRangeEndArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange[] commentRangeEndArray) {
        check_orphaned();
        arraySetterHelper(commentRangeEndArray, PROPERTY_QNAME[17]);
    }

    /**
     * Sets ith "commentRangeEnd" element
     */
    @Override
    public void setCommentRangeEndArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange commentRangeEnd) {
        generatedSetterHelperImpl(commentRangeEnd, PROPERTY_QNAME[17], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "commentRangeEnd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange insertNewCommentRangeEnd(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange)get_store().insert_element_user(PROPERTY_QNAME[17], i);
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
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange)get_store().add_element_user(PROPERTY_QNAME[17]);
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
            get_store().remove_element(PROPERTY_QNAME[17], i);
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
        return getXmlObjectArray(PROPERTY_QNAME[18], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange[0]);
    }

    /**
     * Gets ith "customXmlInsRangeStart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange getCustomXmlInsRangeStartArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange)get_store().find_element_user(PROPERTY_QNAME[18], i);
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
            return get_store().count_elements(PROPERTY_QNAME[18]);
        }
    }

    /**
     * Sets array of all "customXmlInsRangeStart" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCustomXmlInsRangeStartArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange[] customXmlInsRangeStartArray) {
        check_orphaned();
        arraySetterHelper(customXmlInsRangeStartArray, PROPERTY_QNAME[18]);
    }

    /**
     * Sets ith "customXmlInsRangeStart" element
     */
    @Override
    public void setCustomXmlInsRangeStartArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange customXmlInsRangeStart) {
        generatedSetterHelperImpl(customXmlInsRangeStart, PROPERTY_QNAME[18], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "customXmlInsRangeStart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange insertNewCustomXmlInsRangeStart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange)get_store().insert_element_user(PROPERTY_QNAME[18], i);
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
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange)get_store().add_element_user(PROPERTY_QNAME[18]);
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
            get_store().remove_element(PROPERTY_QNAME[18], i);
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
        return getXmlObjectArray(PROPERTY_QNAME[19], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup[0]);
    }

    /**
     * Gets ith "customXmlInsRangeEnd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup getCustomXmlInsRangeEndArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup)get_store().find_element_user(PROPERTY_QNAME[19], i);
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
            return get_store().count_elements(PROPERTY_QNAME[19]);
        }
    }

    /**
     * Sets array of all "customXmlInsRangeEnd" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCustomXmlInsRangeEndArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup[] customXmlInsRangeEndArray) {
        check_orphaned();
        arraySetterHelper(customXmlInsRangeEndArray, PROPERTY_QNAME[19]);
    }

    /**
     * Sets ith "customXmlInsRangeEnd" element
     */
    @Override
    public void setCustomXmlInsRangeEndArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup customXmlInsRangeEnd) {
        generatedSetterHelperImpl(customXmlInsRangeEnd, PROPERTY_QNAME[19], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "customXmlInsRangeEnd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup insertNewCustomXmlInsRangeEnd(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup)get_store().insert_element_user(PROPERTY_QNAME[19], i);
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
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup)get_store().add_element_user(PROPERTY_QNAME[19]);
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
            get_store().remove_element(PROPERTY_QNAME[19], i);
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
        return getXmlObjectArray(PROPERTY_QNAME[20], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange[0]);
    }

    /**
     * Gets ith "customXmlDelRangeStart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange getCustomXmlDelRangeStartArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange)get_store().find_element_user(PROPERTY_QNAME[20], i);
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
            return get_store().count_elements(PROPERTY_QNAME[20]);
        }
    }

    /**
     * Sets array of all "customXmlDelRangeStart" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCustomXmlDelRangeStartArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange[] customXmlDelRangeStartArray) {
        check_orphaned();
        arraySetterHelper(customXmlDelRangeStartArray, PROPERTY_QNAME[20]);
    }

    /**
     * Sets ith "customXmlDelRangeStart" element
     */
    @Override
    public void setCustomXmlDelRangeStartArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange customXmlDelRangeStart) {
        generatedSetterHelperImpl(customXmlDelRangeStart, PROPERTY_QNAME[20], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "customXmlDelRangeStart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange insertNewCustomXmlDelRangeStart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange)get_store().insert_element_user(PROPERTY_QNAME[20], i);
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
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange)get_store().add_element_user(PROPERTY_QNAME[20]);
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
            get_store().remove_element(PROPERTY_QNAME[20], i);
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
        return getXmlObjectArray(PROPERTY_QNAME[21], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup[0]);
    }

    /**
     * Gets ith "customXmlDelRangeEnd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup getCustomXmlDelRangeEndArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup)get_store().find_element_user(PROPERTY_QNAME[21], i);
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
            return get_store().count_elements(PROPERTY_QNAME[21]);
        }
    }

    /**
     * Sets array of all "customXmlDelRangeEnd" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCustomXmlDelRangeEndArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup[] customXmlDelRangeEndArray) {
        check_orphaned();
        arraySetterHelper(customXmlDelRangeEndArray, PROPERTY_QNAME[21]);
    }

    /**
     * Sets ith "customXmlDelRangeEnd" element
     */
    @Override
    public void setCustomXmlDelRangeEndArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup customXmlDelRangeEnd) {
        generatedSetterHelperImpl(customXmlDelRangeEnd, PROPERTY_QNAME[21], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "customXmlDelRangeEnd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup insertNewCustomXmlDelRangeEnd(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup)get_store().insert_element_user(PROPERTY_QNAME[21], i);
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
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup)get_store().add_element_user(PROPERTY_QNAME[21]);
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
            get_store().remove_element(PROPERTY_QNAME[21], i);
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
        return getXmlObjectArray(PROPERTY_QNAME[22], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange[0]);
    }

    /**
     * Gets ith "customXmlMoveFromRangeStart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange getCustomXmlMoveFromRangeStartArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange)get_store().find_element_user(PROPERTY_QNAME[22], i);
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
            return get_store().count_elements(PROPERTY_QNAME[22]);
        }
    }

    /**
     * Sets array of all "customXmlMoveFromRangeStart" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCustomXmlMoveFromRangeStartArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange[] customXmlMoveFromRangeStartArray) {
        check_orphaned();
        arraySetterHelper(customXmlMoveFromRangeStartArray, PROPERTY_QNAME[22]);
    }

    /**
     * Sets ith "customXmlMoveFromRangeStart" element
     */
    @Override
    public void setCustomXmlMoveFromRangeStartArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange customXmlMoveFromRangeStart) {
        generatedSetterHelperImpl(customXmlMoveFromRangeStart, PROPERTY_QNAME[22], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "customXmlMoveFromRangeStart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange insertNewCustomXmlMoveFromRangeStart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange)get_store().insert_element_user(PROPERTY_QNAME[22], i);
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
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange)get_store().add_element_user(PROPERTY_QNAME[22]);
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
            get_store().remove_element(PROPERTY_QNAME[22], i);
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
        return getXmlObjectArray(PROPERTY_QNAME[23], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup[0]);
    }

    /**
     * Gets ith "customXmlMoveFromRangeEnd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup getCustomXmlMoveFromRangeEndArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup)get_store().find_element_user(PROPERTY_QNAME[23], i);
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
            return get_store().count_elements(PROPERTY_QNAME[23]);
        }
    }

    /**
     * Sets array of all "customXmlMoveFromRangeEnd" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCustomXmlMoveFromRangeEndArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup[] customXmlMoveFromRangeEndArray) {
        check_orphaned();
        arraySetterHelper(customXmlMoveFromRangeEndArray, PROPERTY_QNAME[23]);
    }

    /**
     * Sets ith "customXmlMoveFromRangeEnd" element
     */
    @Override
    public void setCustomXmlMoveFromRangeEndArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup customXmlMoveFromRangeEnd) {
        generatedSetterHelperImpl(customXmlMoveFromRangeEnd, PROPERTY_QNAME[23], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "customXmlMoveFromRangeEnd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup insertNewCustomXmlMoveFromRangeEnd(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup)get_store().insert_element_user(PROPERTY_QNAME[23], i);
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
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup)get_store().add_element_user(PROPERTY_QNAME[23]);
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
            get_store().remove_element(PROPERTY_QNAME[23], i);
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
        return getXmlObjectArray(PROPERTY_QNAME[24], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange[0]);
    }

    /**
     * Gets ith "customXmlMoveToRangeStart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange getCustomXmlMoveToRangeStartArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange)get_store().find_element_user(PROPERTY_QNAME[24], i);
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
            return get_store().count_elements(PROPERTY_QNAME[24]);
        }
    }

    /**
     * Sets array of all "customXmlMoveToRangeStart" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCustomXmlMoveToRangeStartArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange[] customXmlMoveToRangeStartArray) {
        check_orphaned();
        arraySetterHelper(customXmlMoveToRangeStartArray, PROPERTY_QNAME[24]);
    }

    /**
     * Sets ith "customXmlMoveToRangeStart" element
     */
    @Override
    public void setCustomXmlMoveToRangeStartArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange customXmlMoveToRangeStart) {
        generatedSetterHelperImpl(customXmlMoveToRangeStart, PROPERTY_QNAME[24], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "customXmlMoveToRangeStart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange insertNewCustomXmlMoveToRangeStart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange)get_store().insert_element_user(PROPERTY_QNAME[24], i);
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
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange)get_store().add_element_user(PROPERTY_QNAME[24]);
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
            get_store().remove_element(PROPERTY_QNAME[24], i);
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
        return getXmlObjectArray(PROPERTY_QNAME[25], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup[0]);
    }

    /**
     * Gets ith "customXmlMoveToRangeEnd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup getCustomXmlMoveToRangeEndArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup)get_store().find_element_user(PROPERTY_QNAME[25], i);
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
            return get_store().count_elements(PROPERTY_QNAME[25]);
        }
    }

    /**
     * Sets array of all "customXmlMoveToRangeEnd" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCustomXmlMoveToRangeEndArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup[] customXmlMoveToRangeEndArray) {
        check_orphaned();
        arraySetterHelper(customXmlMoveToRangeEndArray, PROPERTY_QNAME[25]);
    }

    /**
     * Sets ith "customXmlMoveToRangeEnd" element
     */
    @Override
    public void setCustomXmlMoveToRangeEndArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup customXmlMoveToRangeEnd) {
        generatedSetterHelperImpl(customXmlMoveToRangeEnd, PROPERTY_QNAME[25], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "customXmlMoveToRangeEnd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup insertNewCustomXmlMoveToRangeEnd(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup)get_store().insert_element_user(PROPERTY_QNAME[25], i);
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
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup)get_store().add_element_user(PROPERTY_QNAME[25]);
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
            get_store().remove_element(PROPERTY_QNAME[25], i);
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
        return getXmlObjectArray(PROPERTY_QNAME[26], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange[0]);
    }

    /**
     * Gets ith "ins" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange getInsArray(int i) {
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
     * Returns number of "ins" element
     */
    @Override
    public int sizeOfInsArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[26]);
        }
    }

    /**
     * Sets array of all "ins" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setInsArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange[] insArray) {
        check_orphaned();
        arraySetterHelper(insArray, PROPERTY_QNAME[26]);
    }

    /**
     * Sets ith "ins" element
     */
    @Override
    public void setInsArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange ins) {
        generatedSetterHelperImpl(ins, PROPERTY_QNAME[26], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "ins" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange insertNewIns(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange)get_store().insert_element_user(PROPERTY_QNAME[26], i);
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
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange)get_store().add_element_user(PROPERTY_QNAME[26]);
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
            get_store().remove_element(PROPERTY_QNAME[26], i);
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
        return getXmlObjectArray(PROPERTY_QNAME[27], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange[0]);
    }

    /**
     * Gets ith "del" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange getDelArray(int i) {
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
     * Returns number of "del" element
     */
    @Override
    public int sizeOfDelArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[27]);
        }
    }

    /**
     * Sets array of all "del" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setDelArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange[] delArray) {
        check_orphaned();
        arraySetterHelper(delArray, PROPERTY_QNAME[27]);
    }

    /**
     * Sets ith "del" element
     */
    @Override
    public void setDelArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange del) {
        generatedSetterHelperImpl(del, PROPERTY_QNAME[27], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "del" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange insertNewDel(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange)get_store().insert_element_user(PROPERTY_QNAME[27], i);
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
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange)get_store().add_element_user(PROPERTY_QNAME[27]);
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
            get_store().remove_element(PROPERTY_QNAME[27], i);
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
        return getXmlObjectArray(PROPERTY_QNAME[28], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange[0]);
    }

    /**
     * Gets ith "moveFrom" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange getMoveFromArray(int i) {
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
     * Returns number of "moveFrom" element
     */
    @Override
    public int sizeOfMoveFromArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[28]);
        }
    }

    /**
     * Sets array of all "moveFrom" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setMoveFromArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange[] moveFromArray) {
        check_orphaned();
        arraySetterHelper(moveFromArray, PROPERTY_QNAME[28]);
    }

    /**
     * Sets ith "moveFrom" element
     */
    @Override
    public void setMoveFromArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange moveFrom) {
        generatedSetterHelperImpl(moveFrom, PROPERTY_QNAME[28], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "moveFrom" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange insertNewMoveFrom(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange)get_store().insert_element_user(PROPERTY_QNAME[28], i);
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
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange)get_store().add_element_user(PROPERTY_QNAME[28]);
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
            get_store().remove_element(PROPERTY_QNAME[28], i);
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
        return getXmlObjectArray(PROPERTY_QNAME[29], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange[0]);
    }

    /**
     * Gets ith "moveTo" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange getMoveToArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange)get_store().find_element_user(PROPERTY_QNAME[29], i);
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
            return get_store().count_elements(PROPERTY_QNAME[29]);
        }
    }

    /**
     * Sets array of all "moveTo" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setMoveToArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange[] moveToArray) {
        check_orphaned();
        arraySetterHelper(moveToArray, PROPERTY_QNAME[29]);
    }

    /**
     * Sets ith "moveTo" element
     */
    @Override
    public void setMoveToArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange moveTo) {
        generatedSetterHelperImpl(moveTo, PROPERTY_QNAME[29], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "moveTo" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange insertNewMoveTo(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange)get_store().insert_element_user(PROPERTY_QNAME[29], i);
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
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRunTrackChange)get_store().add_element_user(PROPERTY_QNAME[29]);
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
            get_store().remove_element(PROPERTY_QNAME[29], i);
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
        return getXmlObjectArray(PROPERTY_QNAME[30], new org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathPara[0]);
    }

    /**
     * Gets ith "oMathPara" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathPara getOMathParaArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathPara target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathPara)get_store().find_element_user(PROPERTY_QNAME[30], i);
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
            return get_store().count_elements(PROPERTY_QNAME[30]);
        }
    }

    /**
     * Sets array of all "oMathPara" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setOMathParaArray(org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathPara[] oMathParaArray) {
        check_orphaned();
        arraySetterHelper(oMathParaArray, PROPERTY_QNAME[30]);
    }

    /**
     * Sets ith "oMathPara" element
     */
    @Override
    public void setOMathParaArray(int i, org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathPara oMathPara) {
        generatedSetterHelperImpl(oMathPara, PROPERTY_QNAME[30], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "oMathPara" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathPara insertNewOMathPara(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathPara target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathPara)get_store().insert_element_user(PROPERTY_QNAME[30], i);
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
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathPara)get_store().add_element_user(PROPERTY_QNAME[30]);
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
            get_store().remove_element(PROPERTY_QNAME[30], i);
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
        return getXmlObjectArray(PROPERTY_QNAME[31], new org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath[0]);
    }

    /**
     * Gets ith "oMath" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath getOMathArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath)get_store().find_element_user(PROPERTY_QNAME[31], i);
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
            return get_store().count_elements(PROPERTY_QNAME[31]);
        }
    }

    /**
     * Sets array of all "oMath" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setOMathArray(org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath[] oMathArray) {
        check_orphaned();
        arraySetterHelper(oMathArray, PROPERTY_QNAME[31]);
    }

    /**
     * Sets ith "oMath" element
     */
    @Override
    public void setOMathArray(int i, org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath oMath) {
        generatedSetterHelperImpl(oMath, PROPERTY_QNAME[31], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "oMath" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath insertNewOMath(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath)get_store().insert_element_user(PROPERTY_QNAME[31], i);
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
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath)get_store().add_element_user(PROPERTY_QNAME[31]);
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
            get_store().remove_element(PROPERTY_QNAME[31], i);
        }
    }

    /**
     * Gets a List of "fldSimple" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSimpleField> getFldSimpleList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getFldSimpleArray,
                this::setFldSimpleArray,
                this::insertNewFldSimple,
                this::removeFldSimple,
                this::sizeOfFldSimpleArray
            );
        }
    }

    /**
     * Gets array of all "fldSimple" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSimpleField[] getFldSimpleArray() {
        return getXmlObjectArray(PROPERTY_QNAME[32], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSimpleField[0]);
    }

    /**
     * Gets ith "fldSimple" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSimpleField getFldSimpleArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSimpleField target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSimpleField)get_store().find_element_user(PROPERTY_QNAME[32], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "fldSimple" element
     */
    @Override
    public int sizeOfFldSimpleArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[32]);
        }
    }

    /**
     * Sets array of all "fldSimple" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setFldSimpleArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSimpleField[] fldSimpleArray) {
        check_orphaned();
        arraySetterHelper(fldSimpleArray, PROPERTY_QNAME[32]);
    }

    /**
     * Sets ith "fldSimple" element
     */
    @Override
    public void setFldSimpleArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSimpleField fldSimple) {
        generatedSetterHelperImpl(fldSimple, PROPERTY_QNAME[32], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "fldSimple" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSimpleField insertNewFldSimple(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSimpleField target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSimpleField)get_store().insert_element_user(PROPERTY_QNAME[32], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "fldSimple" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSimpleField addNewFldSimple() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSimpleField target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSimpleField)get_store().add_element_user(PROPERTY_QNAME[32]);
            return target;
        }
    }

    /**
     * Removes the ith "fldSimple" element
     */
    @Override
    public void removeFldSimple(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[32], i);
        }
    }

    /**
     * Gets a List of "hyperlink" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHyperlink> getHyperlinkList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getHyperlinkArray,
                this::setHyperlinkArray,
                this::insertNewHyperlink,
                this::removeHyperlink,
                this::sizeOfHyperlinkArray
            );
        }
    }

    /**
     * Gets array of all "hyperlink" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHyperlink[] getHyperlinkArray() {
        return getXmlObjectArray(PROPERTY_QNAME[33], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHyperlink[0]);
    }

    /**
     * Gets ith "hyperlink" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHyperlink getHyperlinkArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHyperlink target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHyperlink)get_store().find_element_user(PROPERTY_QNAME[33], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "hyperlink" element
     */
    @Override
    public int sizeOfHyperlinkArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[33]);
        }
    }

    /**
     * Sets array of all "hyperlink" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setHyperlinkArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHyperlink[] hyperlinkArray) {
        check_orphaned();
        arraySetterHelper(hyperlinkArray, PROPERTY_QNAME[33]);
    }

    /**
     * Sets ith "hyperlink" element
     */
    @Override
    public void setHyperlinkArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHyperlink hyperlink) {
        generatedSetterHelperImpl(hyperlink, PROPERTY_QNAME[33], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "hyperlink" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHyperlink insertNewHyperlink(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHyperlink target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHyperlink)get_store().insert_element_user(PROPERTY_QNAME[33], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "hyperlink" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHyperlink addNewHyperlink() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHyperlink target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHyperlink)get_store().add_element_user(PROPERTY_QNAME[33]);
            return target;
        }
    }

    /**
     * Removes the ith "hyperlink" element
     */
    @Override
    public void removeHyperlink(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[33], i);
        }
    }

    /**
     * Gets a List of "subDoc" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel> getSubDocList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getSubDocArray,
                this::setSubDocArray,
                this::insertNewSubDoc,
                this::removeSubDoc,
                this::sizeOfSubDocArray
            );
        }
    }

    /**
     * Gets array of all "subDoc" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel[] getSubDocArray() {
        return getXmlObjectArray(PROPERTY_QNAME[34], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel[0]);
    }

    /**
     * Gets ith "subDoc" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel getSubDocArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel)get_store().find_element_user(PROPERTY_QNAME[34], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "subDoc" element
     */
    @Override
    public int sizeOfSubDocArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[34]);
        }
    }

    /**
     * Sets array of all "subDoc" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSubDocArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel[] subDocArray) {
        check_orphaned();
        arraySetterHelper(subDocArray, PROPERTY_QNAME[34]);
    }

    /**
     * Sets ith "subDoc" element
     */
    @Override
    public void setSubDocArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel subDoc) {
        generatedSetterHelperImpl(subDoc, PROPERTY_QNAME[34], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "subDoc" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel insertNewSubDoc(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel)get_store().insert_element_user(PROPERTY_QNAME[34], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "subDoc" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel addNewSubDoc() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel)get_store().add_element_user(PROPERTY_QNAME[34]);
            return target;
        }
    }

    /**
     * Removes the ith "subDoc" element
     */
    @Override
    public void removeSubDoc(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[34], i);
        }
    }

    /**
     * Gets the "rsidRPr" attribute
     */
    @Override
    public byte[] getRsidRPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[35]);
            return (target == null) ? null : target.getByteArrayValue();
        }
    }

    /**
     * Gets (as xml) the "rsidRPr" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber xgetRsidRPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber)get_store().find_attribute_user(PROPERTY_QNAME[35]);
            return target;
        }
    }

    /**
     * True if has "rsidRPr" attribute
     */
    @Override
    public boolean isSetRsidRPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[35]) != null;
        }
    }

    /**
     * Sets the "rsidRPr" attribute
     */
    @Override
    public void setRsidRPr(byte[] rsidRPr) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[35]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[35]);
            }
            target.setByteArrayValue(rsidRPr);
        }
    }

    /**
     * Sets (as xml) the "rsidRPr" attribute
     */
    @Override
    public void xsetRsidRPr(org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber rsidRPr) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber)get_store().find_attribute_user(PROPERTY_QNAME[35]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber)get_store().add_attribute_user(PROPERTY_QNAME[35]);
            }
            target.set(rsidRPr);
        }
    }

    /**
     * Unsets the "rsidRPr" attribute
     */
    @Override
    public void unsetRsidRPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[35]);
        }
    }

    /**
     * Gets the "rsidR" attribute
     */
    @Override
    public byte[] getRsidR() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[36]);
            return (target == null) ? null : target.getByteArrayValue();
        }
    }

    /**
     * Gets (as xml) the "rsidR" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber xgetRsidR() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber)get_store().find_attribute_user(PROPERTY_QNAME[36]);
            return target;
        }
    }

    /**
     * True if has "rsidR" attribute
     */
    @Override
    public boolean isSetRsidR() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[36]) != null;
        }
    }

    /**
     * Sets the "rsidR" attribute
     */
    @Override
    public void setRsidR(byte[] rsidR) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[36]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[36]);
            }
            target.setByteArrayValue(rsidR);
        }
    }

    /**
     * Sets (as xml) the "rsidR" attribute
     */
    @Override
    public void xsetRsidR(org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber rsidR) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber)get_store().find_attribute_user(PROPERTY_QNAME[36]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber)get_store().add_attribute_user(PROPERTY_QNAME[36]);
            }
            target.set(rsidR);
        }
    }

    /**
     * Unsets the "rsidR" attribute
     */
    @Override
    public void unsetRsidR() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[36]);
        }
    }

    /**
     * Gets the "rsidDel" attribute
     */
    @Override
    public byte[] getRsidDel() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[37]);
            return (target == null) ? null : target.getByteArrayValue();
        }
    }

    /**
     * Gets (as xml) the "rsidDel" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber xgetRsidDel() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber)get_store().find_attribute_user(PROPERTY_QNAME[37]);
            return target;
        }
    }

    /**
     * True if has "rsidDel" attribute
     */
    @Override
    public boolean isSetRsidDel() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[37]) != null;
        }
    }

    /**
     * Sets the "rsidDel" attribute
     */
    @Override
    public void setRsidDel(byte[] rsidDel) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[37]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[37]);
            }
            target.setByteArrayValue(rsidDel);
        }
    }

    /**
     * Sets (as xml) the "rsidDel" attribute
     */
    @Override
    public void xsetRsidDel(org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber rsidDel) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber)get_store().find_attribute_user(PROPERTY_QNAME[37]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber)get_store().add_attribute_user(PROPERTY_QNAME[37]);
            }
            target.set(rsidDel);
        }
    }

    /**
     * Unsets the "rsidDel" attribute
     */
    @Override
    public void unsetRsidDel() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[37]);
        }
    }

    /**
     * Gets the "rsidP" attribute
     */
    @Override
    public byte[] getRsidP() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[38]);
            return (target == null) ? null : target.getByteArrayValue();
        }
    }

    /**
     * Gets (as xml) the "rsidP" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber xgetRsidP() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber)get_store().find_attribute_user(PROPERTY_QNAME[38]);
            return target;
        }
    }

    /**
     * True if has "rsidP" attribute
     */
    @Override
    public boolean isSetRsidP() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[38]) != null;
        }
    }

    /**
     * Sets the "rsidP" attribute
     */
    @Override
    public void setRsidP(byte[] rsidP) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[38]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[38]);
            }
            target.setByteArrayValue(rsidP);
        }
    }

    /**
     * Sets (as xml) the "rsidP" attribute
     */
    @Override
    public void xsetRsidP(org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber rsidP) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber)get_store().find_attribute_user(PROPERTY_QNAME[38]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber)get_store().add_attribute_user(PROPERTY_QNAME[38]);
            }
            target.set(rsidP);
        }
    }

    /**
     * Unsets the "rsidP" attribute
     */
    @Override
    public void unsetRsidP() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[38]);
        }
    }

    /**
     * Gets the "rsidRDefault" attribute
     */
    @Override
    public byte[] getRsidRDefault() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[39]);
            return (target == null) ? null : target.getByteArrayValue();
        }
    }

    /**
     * Gets (as xml) the "rsidRDefault" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber xgetRsidRDefault() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber)get_store().find_attribute_user(PROPERTY_QNAME[39]);
            return target;
        }
    }

    /**
     * True if has "rsidRDefault" attribute
     */
    @Override
    public boolean isSetRsidRDefault() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[39]) != null;
        }
    }

    /**
     * Sets the "rsidRDefault" attribute
     */
    @Override
    public void setRsidRDefault(byte[] rsidRDefault) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[39]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[39]);
            }
            target.setByteArrayValue(rsidRDefault);
        }
    }

    /**
     * Sets (as xml) the "rsidRDefault" attribute
     */
    @Override
    public void xsetRsidRDefault(org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber rsidRDefault) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber)get_store().find_attribute_user(PROPERTY_QNAME[39]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber)get_store().add_attribute_user(PROPERTY_QNAME[39]);
            }
            target.set(rsidRDefault);
        }
    }

    /**
     * Unsets the "rsidRDefault" attribute
     */
    @Override
    public void unsetRsidRDefault() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[39]);
        }
    }
}
