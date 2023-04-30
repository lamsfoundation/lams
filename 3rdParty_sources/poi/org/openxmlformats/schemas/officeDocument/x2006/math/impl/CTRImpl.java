/*
 * XML Type:  CT_R
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/math
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.math.CTR
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.math.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_R(@http://schemas.openxmlformats.org/officeDocument/2006/math).
 *
 * This is a complex type.
 */
public class CTRImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.officeDocument.x2006.math.CTR {
    private static final long serialVersionUID = 1L;

    public CTRImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/math", "rPr"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "rPr"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "br"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "t"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "contentPart"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "delText"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "instrText"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "delInstrText"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "noBreakHyphen"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "softHyphen"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "dayShort"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "monthShort"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "yearShort"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "dayLong"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "monthLong"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "yearLong"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "annotationRef"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "footnoteRef"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "endnoteRef"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "separator"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "continuationSeparator"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "sym"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "pgNum"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "cr"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "tab"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "object"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "pict"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "fldChar"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "ruby"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "footnoteReference"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "endnoteReference"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "commentReference"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "drawing"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "ptab"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "lastRenderedPageBreak"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/math", "t"),
    };


    /**
     * Gets the "rPr" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTRPR getRPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTRPR target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTRPR)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "rPr" element
     */
    @Override
    public boolean isSetRPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "rPr" element
     */
    @Override
    public void setRPr(org.openxmlformats.schemas.officeDocument.x2006.math.CTRPR rPr) {
        generatedSetterHelperImpl(rPr, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "rPr" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTRPR addNewRPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTRPR target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTRPR)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "rPr" element
     */
    @Override
    public void unsetRPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "rPr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr getRPr2() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "rPr" element
     */
    @Override
    public boolean isSetRPr2() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "rPr" element
     */
    @Override
    public void setRPr2(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr rPr2) {
        generatedSetterHelperImpl(rPr2, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "rPr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr addNewRPr2() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "rPr" element
     */
    @Override
    public void unsetRPr2() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets a List of "br" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBr> getBrList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getBrArray,
                this::setBrArray,
                this::insertNewBr,
                this::removeBr,
                this::sizeOfBrArray
            );
        }
    }

    /**
     * Gets array of all "br" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBr[] getBrArray() {
        return getXmlObjectArray(PROPERTY_QNAME[2], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBr[0]);
    }

    /**
     * Gets ith "br" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBr getBrArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBr target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBr)get_store().find_element_user(PROPERTY_QNAME[2], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "br" element
     */
    @Override
    public int sizeOfBrArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Sets array of all "br" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setBrArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBr[] brArray) {
        check_orphaned();
        arraySetterHelper(brArray, PROPERTY_QNAME[2]);
    }

    /**
     * Sets ith "br" element
     */
    @Override
    public void setBrArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBr br) {
        generatedSetterHelperImpl(br, PROPERTY_QNAME[2], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "br" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBr insertNewBr(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBr target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBr)get_store().insert_element_user(PROPERTY_QNAME[2], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "br" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBr addNewBr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBr target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBr)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Removes the ith "br" element
     */
    @Override
    public void removeBr(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], i);
        }
    }

    /**
     * Gets a List of "t" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText> getTList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getTArray,
                this::setTArray,
                this::insertNewT,
                this::removeT,
                this::sizeOfTArray
            );
        }
    }

    /**
     * Gets array of all "t" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText[] getTArray() {
        return getXmlObjectArray(PROPERTY_QNAME[3], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText[0]);
    }

    /**
     * Gets ith "t" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText getTArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText)get_store().find_element_user(PROPERTY_QNAME[3], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "t" element
     */
    @Override
    public int sizeOfTArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Sets array of all "t" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setTArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText[] tArray) {
        check_orphaned();
        arraySetterHelper(tArray, PROPERTY_QNAME[3]);
    }

    /**
     * Sets ith "t" element
     */
    @Override
    public void setTArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText t) {
        generatedSetterHelperImpl(t, PROPERTY_QNAME[3], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "t" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText insertNewT(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText)get_store().insert_element_user(PROPERTY_QNAME[3], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "t" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText addNewT() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Removes the ith "t" element
     */
    @Override
    public void removeT(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], i);
        }
    }

    /**
     * Gets a List of "contentPart" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel> getContentPartList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getContentPartArray,
                this::setContentPartArray,
                this::insertNewContentPart,
                this::removeContentPart,
                this::sizeOfContentPartArray
            );
        }
    }

    /**
     * Gets array of all "contentPart" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel[] getContentPartArray() {
        return getXmlObjectArray(PROPERTY_QNAME[4], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel[0]);
    }

    /**
     * Gets ith "contentPart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel getContentPartArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel)get_store().find_element_user(PROPERTY_QNAME[4], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "contentPart" element
     */
    @Override
    public int sizeOfContentPartArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Sets array of all "contentPart" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setContentPartArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel[] contentPartArray) {
        check_orphaned();
        arraySetterHelper(contentPartArray, PROPERTY_QNAME[4]);
    }

    /**
     * Sets ith "contentPart" element
     */
    @Override
    public void setContentPartArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel contentPart) {
        generatedSetterHelperImpl(contentPart, PROPERTY_QNAME[4], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "contentPart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel insertNewContentPart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel)get_store().insert_element_user(PROPERTY_QNAME[4], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "contentPart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel addNewContentPart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Removes the ith "contentPart" element
     */
    @Override
    public void removeContentPart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], i);
        }
    }

    /**
     * Gets a List of "delText" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText> getDelTextList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getDelTextArray,
                this::setDelTextArray,
                this::insertNewDelText,
                this::removeDelText,
                this::sizeOfDelTextArray
            );
        }
    }

    /**
     * Gets array of all "delText" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText[] getDelTextArray() {
        return getXmlObjectArray(PROPERTY_QNAME[5], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText[0]);
    }

    /**
     * Gets ith "delText" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText getDelTextArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText)get_store().find_element_user(PROPERTY_QNAME[5], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "delText" element
     */
    @Override
    public int sizeOfDelTextArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Sets array of all "delText" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setDelTextArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText[] delTextArray) {
        check_orphaned();
        arraySetterHelper(delTextArray, PROPERTY_QNAME[5]);
    }

    /**
     * Sets ith "delText" element
     */
    @Override
    public void setDelTextArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText delText) {
        generatedSetterHelperImpl(delText, PROPERTY_QNAME[5], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "delText" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText insertNewDelText(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText)get_store().insert_element_user(PROPERTY_QNAME[5], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "delText" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText addNewDelText() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Removes the ith "delText" element
     */
    @Override
    public void removeDelText(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], i);
        }
    }

    /**
     * Gets a List of "instrText" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText> getInstrTextList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getInstrTextArray,
                this::setInstrTextArray,
                this::insertNewInstrText,
                this::removeInstrText,
                this::sizeOfInstrTextArray
            );
        }
    }

    /**
     * Gets array of all "instrText" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText[] getInstrTextArray() {
        return getXmlObjectArray(PROPERTY_QNAME[6], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText[0]);
    }

    /**
     * Gets ith "instrText" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText getInstrTextArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText)get_store().find_element_user(PROPERTY_QNAME[6], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "instrText" element
     */
    @Override
    public int sizeOfInstrTextArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]);
        }
    }

    /**
     * Sets array of all "instrText" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setInstrTextArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText[] instrTextArray) {
        check_orphaned();
        arraySetterHelper(instrTextArray, PROPERTY_QNAME[6]);
    }

    /**
     * Sets ith "instrText" element
     */
    @Override
    public void setInstrTextArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText instrText) {
        generatedSetterHelperImpl(instrText, PROPERTY_QNAME[6], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "instrText" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText insertNewInstrText(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText)get_store().insert_element_user(PROPERTY_QNAME[6], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "instrText" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText addNewInstrText() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Removes the ith "instrText" element
     */
    @Override
    public void removeInstrText(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], i);
        }
    }

    /**
     * Gets a List of "delInstrText" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText> getDelInstrTextList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getDelInstrTextArray,
                this::setDelInstrTextArray,
                this::insertNewDelInstrText,
                this::removeDelInstrText,
                this::sizeOfDelInstrTextArray
            );
        }
    }

    /**
     * Gets array of all "delInstrText" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText[] getDelInstrTextArray() {
        return getXmlObjectArray(PROPERTY_QNAME[7], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText[0]);
    }

    /**
     * Gets ith "delInstrText" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText getDelInstrTextArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText)get_store().find_element_user(PROPERTY_QNAME[7], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "delInstrText" element
     */
    @Override
    public int sizeOfDelInstrTextArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]);
        }
    }

    /**
     * Sets array of all "delInstrText" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setDelInstrTextArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText[] delInstrTextArray) {
        check_orphaned();
        arraySetterHelper(delInstrTextArray, PROPERTY_QNAME[7]);
    }

    /**
     * Sets ith "delInstrText" element
     */
    @Override
    public void setDelInstrTextArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText delInstrText) {
        generatedSetterHelperImpl(delInstrText, PROPERTY_QNAME[7], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "delInstrText" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText insertNewDelInstrText(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText)get_store().insert_element_user(PROPERTY_QNAME[7], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "delInstrText" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText addNewDelInstrText() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Removes the ith "delInstrText" element
     */
    @Override
    public void removeDelInstrText(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[7], i);
        }
    }

    /**
     * Gets a List of "noBreakHyphen" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty> getNoBreakHyphenList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getNoBreakHyphenArray,
                this::setNoBreakHyphenArray,
                this::insertNewNoBreakHyphen,
                this::removeNoBreakHyphen,
                this::sizeOfNoBreakHyphenArray
            );
        }
    }

    /**
     * Gets array of all "noBreakHyphen" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty[] getNoBreakHyphenArray() {
        return getXmlObjectArray(PROPERTY_QNAME[8], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty[0]);
    }

    /**
     * Gets ith "noBreakHyphen" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty getNoBreakHyphenArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty)get_store().find_element_user(PROPERTY_QNAME[8], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "noBreakHyphen" element
     */
    @Override
    public int sizeOfNoBreakHyphenArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[8]);
        }
    }

    /**
     * Sets array of all "noBreakHyphen" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setNoBreakHyphenArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty[] noBreakHyphenArray) {
        check_orphaned();
        arraySetterHelper(noBreakHyphenArray, PROPERTY_QNAME[8]);
    }

    /**
     * Sets ith "noBreakHyphen" element
     */
    @Override
    public void setNoBreakHyphenArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty noBreakHyphen) {
        generatedSetterHelperImpl(noBreakHyphen, PROPERTY_QNAME[8], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "noBreakHyphen" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty insertNewNoBreakHyphen(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty)get_store().insert_element_user(PROPERTY_QNAME[8], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "noBreakHyphen" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty addNewNoBreakHyphen() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty)get_store().add_element_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * Removes the ith "noBreakHyphen" element
     */
    @Override
    public void removeNoBreakHyphen(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[8], i);
        }
    }

    /**
     * Gets a List of "softHyphen" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty> getSoftHyphenList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getSoftHyphenArray,
                this::setSoftHyphenArray,
                this::insertNewSoftHyphen,
                this::removeSoftHyphen,
                this::sizeOfSoftHyphenArray
            );
        }
    }

    /**
     * Gets array of all "softHyphen" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty[] getSoftHyphenArray() {
        return getXmlObjectArray(PROPERTY_QNAME[9], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty[0]);
    }

    /**
     * Gets ith "softHyphen" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty getSoftHyphenArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty)get_store().find_element_user(PROPERTY_QNAME[9], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "softHyphen" element
     */
    @Override
    public int sizeOfSoftHyphenArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[9]);
        }
    }

    /**
     * Sets array of all "softHyphen" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSoftHyphenArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty[] softHyphenArray) {
        check_orphaned();
        arraySetterHelper(softHyphenArray, PROPERTY_QNAME[9]);
    }

    /**
     * Sets ith "softHyphen" element
     */
    @Override
    public void setSoftHyphenArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty softHyphen) {
        generatedSetterHelperImpl(softHyphen, PROPERTY_QNAME[9], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "softHyphen" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty insertNewSoftHyphen(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty)get_store().insert_element_user(PROPERTY_QNAME[9], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "softHyphen" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty addNewSoftHyphen() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty)get_store().add_element_user(PROPERTY_QNAME[9]);
            return target;
        }
    }

    /**
     * Removes the ith "softHyphen" element
     */
    @Override
    public void removeSoftHyphen(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[9], i);
        }
    }

    /**
     * Gets a List of "dayShort" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty> getDayShortList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getDayShortArray,
                this::setDayShortArray,
                this::insertNewDayShort,
                this::removeDayShort,
                this::sizeOfDayShortArray
            );
        }
    }

    /**
     * Gets array of all "dayShort" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty[] getDayShortArray() {
        return getXmlObjectArray(PROPERTY_QNAME[10], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty[0]);
    }

    /**
     * Gets ith "dayShort" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty getDayShortArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty)get_store().find_element_user(PROPERTY_QNAME[10], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "dayShort" element
     */
    @Override
    public int sizeOfDayShortArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[10]);
        }
    }

    /**
     * Sets array of all "dayShort" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setDayShortArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty[] dayShortArray) {
        check_orphaned();
        arraySetterHelper(dayShortArray, PROPERTY_QNAME[10]);
    }

    /**
     * Sets ith "dayShort" element
     */
    @Override
    public void setDayShortArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty dayShort) {
        generatedSetterHelperImpl(dayShort, PROPERTY_QNAME[10], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "dayShort" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty insertNewDayShort(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty)get_store().insert_element_user(PROPERTY_QNAME[10], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "dayShort" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty addNewDayShort() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty)get_store().add_element_user(PROPERTY_QNAME[10]);
            return target;
        }
    }

    /**
     * Removes the ith "dayShort" element
     */
    @Override
    public void removeDayShort(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[10], i);
        }
    }

    /**
     * Gets a List of "monthShort" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty> getMonthShortList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getMonthShortArray,
                this::setMonthShortArray,
                this::insertNewMonthShort,
                this::removeMonthShort,
                this::sizeOfMonthShortArray
            );
        }
    }

    /**
     * Gets array of all "monthShort" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty[] getMonthShortArray() {
        return getXmlObjectArray(PROPERTY_QNAME[11], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty[0]);
    }

    /**
     * Gets ith "monthShort" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty getMonthShortArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty)get_store().find_element_user(PROPERTY_QNAME[11], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "monthShort" element
     */
    @Override
    public int sizeOfMonthShortArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[11]);
        }
    }

    /**
     * Sets array of all "monthShort" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setMonthShortArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty[] monthShortArray) {
        check_orphaned();
        arraySetterHelper(monthShortArray, PROPERTY_QNAME[11]);
    }

    /**
     * Sets ith "monthShort" element
     */
    @Override
    public void setMonthShortArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty monthShort) {
        generatedSetterHelperImpl(monthShort, PROPERTY_QNAME[11], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "monthShort" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty insertNewMonthShort(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty)get_store().insert_element_user(PROPERTY_QNAME[11], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "monthShort" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty addNewMonthShort() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty)get_store().add_element_user(PROPERTY_QNAME[11]);
            return target;
        }
    }

    /**
     * Removes the ith "monthShort" element
     */
    @Override
    public void removeMonthShort(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[11], i);
        }
    }

    /**
     * Gets a List of "yearShort" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty> getYearShortList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getYearShortArray,
                this::setYearShortArray,
                this::insertNewYearShort,
                this::removeYearShort,
                this::sizeOfYearShortArray
            );
        }
    }

    /**
     * Gets array of all "yearShort" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty[] getYearShortArray() {
        return getXmlObjectArray(PROPERTY_QNAME[12], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty[0]);
    }

    /**
     * Gets ith "yearShort" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty getYearShortArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty)get_store().find_element_user(PROPERTY_QNAME[12], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "yearShort" element
     */
    @Override
    public int sizeOfYearShortArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[12]);
        }
    }

    /**
     * Sets array of all "yearShort" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setYearShortArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty[] yearShortArray) {
        check_orphaned();
        arraySetterHelper(yearShortArray, PROPERTY_QNAME[12]);
    }

    /**
     * Sets ith "yearShort" element
     */
    @Override
    public void setYearShortArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty yearShort) {
        generatedSetterHelperImpl(yearShort, PROPERTY_QNAME[12], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "yearShort" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty insertNewYearShort(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty)get_store().insert_element_user(PROPERTY_QNAME[12], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "yearShort" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty addNewYearShort() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty)get_store().add_element_user(PROPERTY_QNAME[12]);
            return target;
        }
    }

    /**
     * Removes the ith "yearShort" element
     */
    @Override
    public void removeYearShort(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[12], i);
        }
    }

    /**
     * Gets a List of "dayLong" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty> getDayLongList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getDayLongArray,
                this::setDayLongArray,
                this::insertNewDayLong,
                this::removeDayLong,
                this::sizeOfDayLongArray
            );
        }
    }

    /**
     * Gets array of all "dayLong" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty[] getDayLongArray() {
        return getXmlObjectArray(PROPERTY_QNAME[13], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty[0]);
    }

    /**
     * Gets ith "dayLong" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty getDayLongArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty)get_store().find_element_user(PROPERTY_QNAME[13], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "dayLong" element
     */
    @Override
    public int sizeOfDayLongArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[13]);
        }
    }

    /**
     * Sets array of all "dayLong" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setDayLongArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty[] dayLongArray) {
        check_orphaned();
        arraySetterHelper(dayLongArray, PROPERTY_QNAME[13]);
    }

    /**
     * Sets ith "dayLong" element
     */
    @Override
    public void setDayLongArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty dayLong) {
        generatedSetterHelperImpl(dayLong, PROPERTY_QNAME[13], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "dayLong" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty insertNewDayLong(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty)get_store().insert_element_user(PROPERTY_QNAME[13], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "dayLong" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty addNewDayLong() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty)get_store().add_element_user(PROPERTY_QNAME[13]);
            return target;
        }
    }

    /**
     * Removes the ith "dayLong" element
     */
    @Override
    public void removeDayLong(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[13], i);
        }
    }

    /**
     * Gets a List of "monthLong" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty> getMonthLongList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getMonthLongArray,
                this::setMonthLongArray,
                this::insertNewMonthLong,
                this::removeMonthLong,
                this::sizeOfMonthLongArray
            );
        }
    }

    /**
     * Gets array of all "monthLong" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty[] getMonthLongArray() {
        return getXmlObjectArray(PROPERTY_QNAME[14], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty[0]);
    }

    /**
     * Gets ith "monthLong" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty getMonthLongArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty)get_store().find_element_user(PROPERTY_QNAME[14], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "monthLong" element
     */
    @Override
    public int sizeOfMonthLongArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[14]);
        }
    }

    /**
     * Sets array of all "monthLong" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setMonthLongArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty[] monthLongArray) {
        check_orphaned();
        arraySetterHelper(monthLongArray, PROPERTY_QNAME[14]);
    }

    /**
     * Sets ith "monthLong" element
     */
    @Override
    public void setMonthLongArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty monthLong) {
        generatedSetterHelperImpl(monthLong, PROPERTY_QNAME[14], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "monthLong" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty insertNewMonthLong(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty)get_store().insert_element_user(PROPERTY_QNAME[14], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "monthLong" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty addNewMonthLong() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty)get_store().add_element_user(PROPERTY_QNAME[14]);
            return target;
        }
    }

    /**
     * Removes the ith "monthLong" element
     */
    @Override
    public void removeMonthLong(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[14], i);
        }
    }

    /**
     * Gets a List of "yearLong" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty> getYearLongList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getYearLongArray,
                this::setYearLongArray,
                this::insertNewYearLong,
                this::removeYearLong,
                this::sizeOfYearLongArray
            );
        }
    }

    /**
     * Gets array of all "yearLong" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty[] getYearLongArray() {
        return getXmlObjectArray(PROPERTY_QNAME[15], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty[0]);
    }

    /**
     * Gets ith "yearLong" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty getYearLongArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty)get_store().find_element_user(PROPERTY_QNAME[15], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "yearLong" element
     */
    @Override
    public int sizeOfYearLongArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[15]);
        }
    }

    /**
     * Sets array of all "yearLong" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setYearLongArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty[] yearLongArray) {
        check_orphaned();
        arraySetterHelper(yearLongArray, PROPERTY_QNAME[15]);
    }

    /**
     * Sets ith "yearLong" element
     */
    @Override
    public void setYearLongArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty yearLong) {
        generatedSetterHelperImpl(yearLong, PROPERTY_QNAME[15], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "yearLong" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty insertNewYearLong(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty)get_store().insert_element_user(PROPERTY_QNAME[15], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "yearLong" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty addNewYearLong() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty)get_store().add_element_user(PROPERTY_QNAME[15]);
            return target;
        }
    }

    /**
     * Removes the ith "yearLong" element
     */
    @Override
    public void removeYearLong(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[15], i);
        }
    }

    /**
     * Gets a List of "annotationRef" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty> getAnnotationRefList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getAnnotationRefArray,
                this::setAnnotationRefArray,
                this::insertNewAnnotationRef,
                this::removeAnnotationRef,
                this::sizeOfAnnotationRefArray
            );
        }
    }

    /**
     * Gets array of all "annotationRef" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty[] getAnnotationRefArray() {
        return getXmlObjectArray(PROPERTY_QNAME[16], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty[0]);
    }

    /**
     * Gets ith "annotationRef" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty getAnnotationRefArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty)get_store().find_element_user(PROPERTY_QNAME[16], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "annotationRef" element
     */
    @Override
    public int sizeOfAnnotationRefArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[16]);
        }
    }

    /**
     * Sets array of all "annotationRef" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setAnnotationRefArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty[] annotationRefArray) {
        check_orphaned();
        arraySetterHelper(annotationRefArray, PROPERTY_QNAME[16]);
    }

    /**
     * Sets ith "annotationRef" element
     */
    @Override
    public void setAnnotationRefArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty annotationRef) {
        generatedSetterHelperImpl(annotationRef, PROPERTY_QNAME[16], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "annotationRef" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty insertNewAnnotationRef(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty)get_store().insert_element_user(PROPERTY_QNAME[16], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "annotationRef" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty addNewAnnotationRef() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty)get_store().add_element_user(PROPERTY_QNAME[16]);
            return target;
        }
    }

    /**
     * Removes the ith "annotationRef" element
     */
    @Override
    public void removeAnnotationRef(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[16], i);
        }
    }

    /**
     * Gets a List of "footnoteRef" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty> getFootnoteRefList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getFootnoteRefArray,
                this::setFootnoteRefArray,
                this::insertNewFootnoteRef,
                this::removeFootnoteRef,
                this::sizeOfFootnoteRefArray
            );
        }
    }

    /**
     * Gets array of all "footnoteRef" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty[] getFootnoteRefArray() {
        return getXmlObjectArray(PROPERTY_QNAME[17], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty[0]);
    }

    /**
     * Gets ith "footnoteRef" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty getFootnoteRefArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty)get_store().find_element_user(PROPERTY_QNAME[17], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "footnoteRef" element
     */
    @Override
    public int sizeOfFootnoteRefArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[17]);
        }
    }

    /**
     * Sets array of all "footnoteRef" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setFootnoteRefArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty[] footnoteRefArray) {
        check_orphaned();
        arraySetterHelper(footnoteRefArray, PROPERTY_QNAME[17]);
    }

    /**
     * Sets ith "footnoteRef" element
     */
    @Override
    public void setFootnoteRefArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty footnoteRef) {
        generatedSetterHelperImpl(footnoteRef, PROPERTY_QNAME[17], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "footnoteRef" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty insertNewFootnoteRef(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty)get_store().insert_element_user(PROPERTY_QNAME[17], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "footnoteRef" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty addNewFootnoteRef() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty)get_store().add_element_user(PROPERTY_QNAME[17]);
            return target;
        }
    }

    /**
     * Removes the ith "footnoteRef" element
     */
    @Override
    public void removeFootnoteRef(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[17], i);
        }
    }

    /**
     * Gets a List of "endnoteRef" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty> getEndnoteRefList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getEndnoteRefArray,
                this::setEndnoteRefArray,
                this::insertNewEndnoteRef,
                this::removeEndnoteRef,
                this::sizeOfEndnoteRefArray
            );
        }
    }

    /**
     * Gets array of all "endnoteRef" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty[] getEndnoteRefArray() {
        return getXmlObjectArray(PROPERTY_QNAME[18], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty[0]);
    }

    /**
     * Gets ith "endnoteRef" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty getEndnoteRefArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty)get_store().find_element_user(PROPERTY_QNAME[18], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "endnoteRef" element
     */
    @Override
    public int sizeOfEndnoteRefArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[18]);
        }
    }

    /**
     * Sets array of all "endnoteRef" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setEndnoteRefArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty[] endnoteRefArray) {
        check_orphaned();
        arraySetterHelper(endnoteRefArray, PROPERTY_QNAME[18]);
    }

    /**
     * Sets ith "endnoteRef" element
     */
    @Override
    public void setEndnoteRefArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty endnoteRef) {
        generatedSetterHelperImpl(endnoteRef, PROPERTY_QNAME[18], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "endnoteRef" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty insertNewEndnoteRef(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty)get_store().insert_element_user(PROPERTY_QNAME[18], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "endnoteRef" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty addNewEndnoteRef() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty)get_store().add_element_user(PROPERTY_QNAME[18]);
            return target;
        }
    }

    /**
     * Removes the ith "endnoteRef" element
     */
    @Override
    public void removeEndnoteRef(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[18], i);
        }
    }

    /**
     * Gets a List of "separator" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty> getSeparatorList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getSeparatorArray,
                this::setSeparatorArray,
                this::insertNewSeparator,
                this::removeSeparator,
                this::sizeOfSeparatorArray
            );
        }
    }

    /**
     * Gets array of all "separator" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty[] getSeparatorArray() {
        return getXmlObjectArray(PROPERTY_QNAME[19], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty[0]);
    }

    /**
     * Gets ith "separator" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty getSeparatorArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty)get_store().find_element_user(PROPERTY_QNAME[19], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "separator" element
     */
    @Override
    public int sizeOfSeparatorArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[19]);
        }
    }

    /**
     * Sets array of all "separator" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSeparatorArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty[] separatorArray) {
        check_orphaned();
        arraySetterHelper(separatorArray, PROPERTY_QNAME[19]);
    }

    /**
     * Sets ith "separator" element
     */
    @Override
    public void setSeparatorArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty separator) {
        generatedSetterHelperImpl(separator, PROPERTY_QNAME[19], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "separator" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty insertNewSeparator(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty)get_store().insert_element_user(PROPERTY_QNAME[19], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "separator" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty addNewSeparator() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty)get_store().add_element_user(PROPERTY_QNAME[19]);
            return target;
        }
    }

    /**
     * Removes the ith "separator" element
     */
    @Override
    public void removeSeparator(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[19], i);
        }
    }

    /**
     * Gets a List of "continuationSeparator" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty> getContinuationSeparatorList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getContinuationSeparatorArray,
                this::setContinuationSeparatorArray,
                this::insertNewContinuationSeparator,
                this::removeContinuationSeparator,
                this::sizeOfContinuationSeparatorArray
            );
        }
    }

    /**
     * Gets array of all "continuationSeparator" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty[] getContinuationSeparatorArray() {
        return getXmlObjectArray(PROPERTY_QNAME[20], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty[0]);
    }

    /**
     * Gets ith "continuationSeparator" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty getContinuationSeparatorArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty)get_store().find_element_user(PROPERTY_QNAME[20], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "continuationSeparator" element
     */
    @Override
    public int sizeOfContinuationSeparatorArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[20]);
        }
    }

    /**
     * Sets array of all "continuationSeparator" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setContinuationSeparatorArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty[] continuationSeparatorArray) {
        check_orphaned();
        arraySetterHelper(continuationSeparatorArray, PROPERTY_QNAME[20]);
    }

    /**
     * Sets ith "continuationSeparator" element
     */
    @Override
    public void setContinuationSeparatorArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty continuationSeparator) {
        generatedSetterHelperImpl(continuationSeparator, PROPERTY_QNAME[20], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "continuationSeparator" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty insertNewContinuationSeparator(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty)get_store().insert_element_user(PROPERTY_QNAME[20], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "continuationSeparator" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty addNewContinuationSeparator() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty)get_store().add_element_user(PROPERTY_QNAME[20]);
            return target;
        }
    }

    /**
     * Removes the ith "continuationSeparator" element
     */
    @Override
    public void removeContinuationSeparator(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[20], i);
        }
    }

    /**
     * Gets a List of "sym" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSym> getSymList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getSymArray,
                this::setSymArray,
                this::insertNewSym,
                this::removeSym,
                this::sizeOfSymArray
            );
        }
    }

    /**
     * Gets array of all "sym" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSym[] getSymArray() {
        return getXmlObjectArray(PROPERTY_QNAME[21], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSym[0]);
    }

    /**
     * Gets ith "sym" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSym getSymArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSym target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSym)get_store().find_element_user(PROPERTY_QNAME[21], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "sym" element
     */
    @Override
    public int sizeOfSymArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[21]);
        }
    }

    /**
     * Sets array of all "sym" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSymArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSym[] symArray) {
        check_orphaned();
        arraySetterHelper(symArray, PROPERTY_QNAME[21]);
    }

    /**
     * Sets ith "sym" element
     */
    @Override
    public void setSymArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSym sym) {
        generatedSetterHelperImpl(sym, PROPERTY_QNAME[21], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "sym" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSym insertNewSym(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSym target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSym)get_store().insert_element_user(PROPERTY_QNAME[21], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "sym" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSym addNewSym() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSym target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSym)get_store().add_element_user(PROPERTY_QNAME[21]);
            return target;
        }
    }

    /**
     * Removes the ith "sym" element
     */
    @Override
    public void removeSym(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[21], i);
        }
    }

    /**
     * Gets a List of "pgNum" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty> getPgNumList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getPgNumArray,
                this::setPgNumArray,
                this::insertNewPgNum,
                this::removePgNum,
                this::sizeOfPgNumArray
            );
        }
    }

    /**
     * Gets array of all "pgNum" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty[] getPgNumArray() {
        return getXmlObjectArray(PROPERTY_QNAME[22], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty[0]);
    }

    /**
     * Gets ith "pgNum" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty getPgNumArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty)get_store().find_element_user(PROPERTY_QNAME[22], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "pgNum" element
     */
    @Override
    public int sizeOfPgNumArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[22]);
        }
    }

    /**
     * Sets array of all "pgNum" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setPgNumArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty[] pgNumArray) {
        check_orphaned();
        arraySetterHelper(pgNumArray, PROPERTY_QNAME[22]);
    }

    /**
     * Sets ith "pgNum" element
     */
    @Override
    public void setPgNumArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty pgNum) {
        generatedSetterHelperImpl(pgNum, PROPERTY_QNAME[22], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "pgNum" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty insertNewPgNum(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty)get_store().insert_element_user(PROPERTY_QNAME[22], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "pgNum" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty addNewPgNum() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty)get_store().add_element_user(PROPERTY_QNAME[22]);
            return target;
        }
    }

    /**
     * Removes the ith "pgNum" element
     */
    @Override
    public void removePgNum(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[22], i);
        }
    }

    /**
     * Gets a List of "cr" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty> getCrList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCrArray,
                this::setCrArray,
                this::insertNewCr,
                this::removeCr,
                this::sizeOfCrArray
            );
        }
    }

    /**
     * Gets array of all "cr" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty[] getCrArray() {
        return getXmlObjectArray(PROPERTY_QNAME[23], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty[0]);
    }

    /**
     * Gets ith "cr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty getCrArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty)get_store().find_element_user(PROPERTY_QNAME[23], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "cr" element
     */
    @Override
    public int sizeOfCrArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[23]);
        }
    }

    /**
     * Sets array of all "cr" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCrArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty[] crArray) {
        check_orphaned();
        arraySetterHelper(crArray, PROPERTY_QNAME[23]);
    }

    /**
     * Sets ith "cr" element
     */
    @Override
    public void setCrArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty cr) {
        generatedSetterHelperImpl(cr, PROPERTY_QNAME[23], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "cr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty insertNewCr(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty)get_store().insert_element_user(PROPERTY_QNAME[23], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "cr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty addNewCr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty)get_store().add_element_user(PROPERTY_QNAME[23]);
            return target;
        }
    }

    /**
     * Removes the ith "cr" element
     */
    @Override
    public void removeCr(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[23], i);
        }
    }

    /**
     * Gets a List of "tab" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty> getTabList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getTabArray,
                this::setTabArray,
                this::insertNewTab,
                this::removeTab,
                this::sizeOfTabArray
            );
        }
    }

    /**
     * Gets array of all "tab" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty[] getTabArray() {
        return getXmlObjectArray(PROPERTY_QNAME[24], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty[0]);
    }

    /**
     * Gets ith "tab" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty getTabArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty)get_store().find_element_user(PROPERTY_QNAME[24], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "tab" element
     */
    @Override
    public int sizeOfTabArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[24]);
        }
    }

    /**
     * Sets array of all "tab" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setTabArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty[] tabArray) {
        check_orphaned();
        arraySetterHelper(tabArray, PROPERTY_QNAME[24]);
    }

    /**
     * Sets ith "tab" element
     */
    @Override
    public void setTabArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty tab) {
        generatedSetterHelperImpl(tab, PROPERTY_QNAME[24], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "tab" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty insertNewTab(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty)get_store().insert_element_user(PROPERTY_QNAME[24], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "tab" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty addNewTab() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty)get_store().add_element_user(PROPERTY_QNAME[24]);
            return target;
        }
    }

    /**
     * Removes the ith "tab" element
     */
    @Override
    public void removeTab(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[24], i);
        }
    }

    /**
     * Gets a List of "object" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTObject> getObjectList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getObjectArray,
                this::setObjectArray,
                this::insertNewObject,
                this::removeObject,
                this::sizeOfObjectArray
            );
        }
    }

    /**
     * Gets array of all "object" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTObject[] getObjectArray() {
        return getXmlObjectArray(PROPERTY_QNAME[25], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTObject[0]);
    }

    /**
     * Gets ith "object" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTObject getObjectArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTObject target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTObject)get_store().find_element_user(PROPERTY_QNAME[25], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "object" element
     */
    @Override
    public int sizeOfObjectArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[25]);
        }
    }

    /**
     * Sets array of all "object" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setObjectArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTObject[] objectArray) {
        check_orphaned();
        arraySetterHelper(objectArray, PROPERTY_QNAME[25]);
    }

    /**
     * Sets ith "object" element
     */
    @Override
    public void setObjectArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTObject object) {
        generatedSetterHelperImpl(object, PROPERTY_QNAME[25], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "object" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTObject insertNewObject(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTObject target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTObject)get_store().insert_element_user(PROPERTY_QNAME[25], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "object" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTObject addNewObject() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTObject target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTObject)get_store().add_element_user(PROPERTY_QNAME[25]);
            return target;
        }
    }

    /**
     * Removes the ith "object" element
     */
    @Override
    public void removeObject(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[25], i);
        }
    }

    /**
     * Gets a List of "pict" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPicture> getPictList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getPictArray,
                this::setPictArray,
                this::insertNewPict,
                this::removePict,
                this::sizeOfPictArray
            );
        }
    }

    /**
     * Gets array of all "pict" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPicture[] getPictArray() {
        return getXmlObjectArray(PROPERTY_QNAME[26], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPicture[0]);
    }

    /**
     * Gets ith "pict" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPicture getPictArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPicture target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPicture)get_store().find_element_user(PROPERTY_QNAME[26], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "pict" element
     */
    @Override
    public int sizeOfPictArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[26]);
        }
    }

    /**
     * Sets array of all "pict" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setPictArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPicture[] pictArray) {
        check_orphaned();
        arraySetterHelper(pictArray, PROPERTY_QNAME[26]);
    }

    /**
     * Sets ith "pict" element
     */
    @Override
    public void setPictArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPicture pict) {
        generatedSetterHelperImpl(pict, PROPERTY_QNAME[26], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "pict" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPicture insertNewPict(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPicture target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPicture)get_store().insert_element_user(PROPERTY_QNAME[26], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "pict" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPicture addNewPict() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPicture target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPicture)get_store().add_element_user(PROPERTY_QNAME[26]);
            return target;
        }
    }

    /**
     * Removes the ith "pict" element
     */
    @Override
    public void removePict(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[26], i);
        }
    }

    /**
     * Gets a List of "fldChar" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFldChar> getFldCharList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getFldCharArray,
                this::setFldCharArray,
                this::insertNewFldChar,
                this::removeFldChar,
                this::sizeOfFldCharArray
            );
        }
    }

    /**
     * Gets array of all "fldChar" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFldChar[] getFldCharArray() {
        return getXmlObjectArray(PROPERTY_QNAME[27], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFldChar[0]);
    }

    /**
     * Gets ith "fldChar" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFldChar getFldCharArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFldChar target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFldChar)get_store().find_element_user(PROPERTY_QNAME[27], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "fldChar" element
     */
    @Override
    public int sizeOfFldCharArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[27]);
        }
    }

    /**
     * Sets array of all "fldChar" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setFldCharArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFldChar[] fldCharArray) {
        check_orphaned();
        arraySetterHelper(fldCharArray, PROPERTY_QNAME[27]);
    }

    /**
     * Sets ith "fldChar" element
     */
    @Override
    public void setFldCharArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFldChar fldChar) {
        generatedSetterHelperImpl(fldChar, PROPERTY_QNAME[27], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "fldChar" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFldChar insertNewFldChar(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFldChar target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFldChar)get_store().insert_element_user(PROPERTY_QNAME[27], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "fldChar" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFldChar addNewFldChar() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFldChar target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFldChar)get_store().add_element_user(PROPERTY_QNAME[27]);
            return target;
        }
    }

    /**
     * Removes the ith "fldChar" element
     */
    @Override
    public void removeFldChar(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[27], i);
        }
    }

    /**
     * Gets a List of "ruby" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRuby> getRubyList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getRubyArray,
                this::setRubyArray,
                this::insertNewRuby,
                this::removeRuby,
                this::sizeOfRubyArray
            );
        }
    }

    /**
     * Gets array of all "ruby" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRuby[] getRubyArray() {
        return getXmlObjectArray(PROPERTY_QNAME[28], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRuby[0]);
    }

    /**
     * Gets ith "ruby" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRuby getRubyArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRuby target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRuby)get_store().find_element_user(PROPERTY_QNAME[28], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "ruby" element
     */
    @Override
    public int sizeOfRubyArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[28]);
        }
    }

    /**
     * Sets array of all "ruby" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setRubyArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRuby[] rubyArray) {
        check_orphaned();
        arraySetterHelper(rubyArray, PROPERTY_QNAME[28]);
    }

    /**
     * Sets ith "ruby" element
     */
    @Override
    public void setRubyArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRuby ruby) {
        generatedSetterHelperImpl(ruby, PROPERTY_QNAME[28], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "ruby" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRuby insertNewRuby(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRuby target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRuby)get_store().insert_element_user(PROPERTY_QNAME[28], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "ruby" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRuby addNewRuby() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRuby target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRuby)get_store().add_element_user(PROPERTY_QNAME[28]);
            return target;
        }
    }

    /**
     * Removes the ith "ruby" element
     */
    @Override
    public void removeRuby(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[28], i);
        }
    }

    /**
     * Gets a List of "footnoteReference" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdnRef> getFootnoteReferenceList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getFootnoteReferenceArray,
                this::setFootnoteReferenceArray,
                this::insertNewFootnoteReference,
                this::removeFootnoteReference,
                this::sizeOfFootnoteReferenceArray
            );
        }
    }

    /**
     * Gets array of all "footnoteReference" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdnRef[] getFootnoteReferenceArray() {
        return getXmlObjectArray(PROPERTY_QNAME[29], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdnRef[0]);
    }

    /**
     * Gets ith "footnoteReference" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdnRef getFootnoteReferenceArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdnRef target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdnRef)get_store().find_element_user(PROPERTY_QNAME[29], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "footnoteReference" element
     */
    @Override
    public int sizeOfFootnoteReferenceArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[29]);
        }
    }

    /**
     * Sets array of all "footnoteReference" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setFootnoteReferenceArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdnRef[] footnoteReferenceArray) {
        check_orphaned();
        arraySetterHelper(footnoteReferenceArray, PROPERTY_QNAME[29]);
    }

    /**
     * Sets ith "footnoteReference" element
     */
    @Override
    public void setFootnoteReferenceArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdnRef footnoteReference) {
        generatedSetterHelperImpl(footnoteReference, PROPERTY_QNAME[29], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "footnoteReference" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdnRef insertNewFootnoteReference(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdnRef target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdnRef)get_store().insert_element_user(PROPERTY_QNAME[29], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "footnoteReference" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdnRef addNewFootnoteReference() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdnRef target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdnRef)get_store().add_element_user(PROPERTY_QNAME[29]);
            return target;
        }
    }

    /**
     * Removes the ith "footnoteReference" element
     */
    @Override
    public void removeFootnoteReference(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[29], i);
        }
    }

    /**
     * Gets a List of "endnoteReference" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdnRef> getEndnoteReferenceList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getEndnoteReferenceArray,
                this::setEndnoteReferenceArray,
                this::insertNewEndnoteReference,
                this::removeEndnoteReference,
                this::sizeOfEndnoteReferenceArray
            );
        }
    }

    /**
     * Gets array of all "endnoteReference" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdnRef[] getEndnoteReferenceArray() {
        return getXmlObjectArray(PROPERTY_QNAME[30], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdnRef[0]);
    }

    /**
     * Gets ith "endnoteReference" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdnRef getEndnoteReferenceArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdnRef target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdnRef)get_store().find_element_user(PROPERTY_QNAME[30], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "endnoteReference" element
     */
    @Override
    public int sizeOfEndnoteReferenceArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[30]);
        }
    }

    /**
     * Sets array of all "endnoteReference" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setEndnoteReferenceArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdnRef[] endnoteReferenceArray) {
        check_orphaned();
        arraySetterHelper(endnoteReferenceArray, PROPERTY_QNAME[30]);
    }

    /**
     * Sets ith "endnoteReference" element
     */
    @Override
    public void setEndnoteReferenceArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdnRef endnoteReference) {
        generatedSetterHelperImpl(endnoteReference, PROPERTY_QNAME[30], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "endnoteReference" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdnRef insertNewEndnoteReference(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdnRef target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdnRef)get_store().insert_element_user(PROPERTY_QNAME[30], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "endnoteReference" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdnRef addNewEndnoteReference() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdnRef target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdnRef)get_store().add_element_user(PROPERTY_QNAME[30]);
            return target;
        }
    }

    /**
     * Removes the ith "endnoteReference" element
     */
    @Override
    public void removeEndnoteReference(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[30], i);
        }
    }

    /**
     * Gets a List of "commentReference" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup> getCommentReferenceList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCommentReferenceArray,
                this::setCommentReferenceArray,
                this::insertNewCommentReference,
                this::removeCommentReference,
                this::sizeOfCommentReferenceArray
            );
        }
    }

    /**
     * Gets array of all "commentReference" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup[] getCommentReferenceArray() {
        return getXmlObjectArray(PROPERTY_QNAME[31], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup[0]);
    }

    /**
     * Gets ith "commentReference" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup getCommentReferenceArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup)get_store().find_element_user(PROPERTY_QNAME[31], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "commentReference" element
     */
    @Override
    public int sizeOfCommentReferenceArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[31]);
        }
    }

    /**
     * Sets array of all "commentReference" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCommentReferenceArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup[] commentReferenceArray) {
        check_orphaned();
        arraySetterHelper(commentReferenceArray, PROPERTY_QNAME[31]);
    }

    /**
     * Sets ith "commentReference" element
     */
    @Override
    public void setCommentReferenceArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup commentReference) {
        generatedSetterHelperImpl(commentReference, PROPERTY_QNAME[31], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "commentReference" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup insertNewCommentReference(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup)get_store().insert_element_user(PROPERTY_QNAME[31], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "commentReference" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup addNewCommentReference() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup)get_store().add_element_user(PROPERTY_QNAME[31]);
            return target;
        }
    }

    /**
     * Removes the ith "commentReference" element
     */
    @Override
    public void removeCommentReference(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[31], i);
        }
    }

    /**
     * Gets a List of "drawing" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing> getDrawingList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getDrawingArray,
                this::setDrawingArray,
                this::insertNewDrawing,
                this::removeDrawing,
                this::sizeOfDrawingArray
            );
        }
    }

    /**
     * Gets array of all "drawing" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing[] getDrawingArray() {
        return getXmlObjectArray(PROPERTY_QNAME[32], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing[0]);
    }

    /**
     * Gets ith "drawing" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing getDrawingArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing)get_store().find_element_user(PROPERTY_QNAME[32], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "drawing" element
     */
    @Override
    public int sizeOfDrawingArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[32]);
        }
    }

    /**
     * Sets array of all "drawing" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setDrawingArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing[] drawingArray) {
        check_orphaned();
        arraySetterHelper(drawingArray, PROPERTY_QNAME[32]);
    }

    /**
     * Sets ith "drawing" element
     */
    @Override
    public void setDrawingArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing drawing) {
        generatedSetterHelperImpl(drawing, PROPERTY_QNAME[32], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "drawing" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing insertNewDrawing(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing)get_store().insert_element_user(PROPERTY_QNAME[32], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "drawing" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing addNewDrawing() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing)get_store().add_element_user(PROPERTY_QNAME[32]);
            return target;
        }
    }

    /**
     * Removes the ith "drawing" element
     */
    @Override
    public void removeDrawing(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[32], i);
        }
    }

    /**
     * Gets a List of "ptab" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPTab> getPtabList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getPtabArray,
                this::setPtabArray,
                this::insertNewPtab,
                this::removePtab,
                this::sizeOfPtabArray
            );
        }
    }

    /**
     * Gets array of all "ptab" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPTab[] getPtabArray() {
        return getXmlObjectArray(PROPERTY_QNAME[33], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPTab[0]);
    }

    /**
     * Gets ith "ptab" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPTab getPtabArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPTab target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPTab)get_store().find_element_user(PROPERTY_QNAME[33], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "ptab" element
     */
    @Override
    public int sizeOfPtabArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[33]);
        }
    }

    /**
     * Sets array of all "ptab" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setPtabArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPTab[] ptabArray) {
        check_orphaned();
        arraySetterHelper(ptabArray, PROPERTY_QNAME[33]);
    }

    /**
     * Sets ith "ptab" element
     */
    @Override
    public void setPtabArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPTab ptab) {
        generatedSetterHelperImpl(ptab, PROPERTY_QNAME[33], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "ptab" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPTab insertNewPtab(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPTab target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPTab)get_store().insert_element_user(PROPERTY_QNAME[33], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "ptab" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPTab addNewPtab() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPTab target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPTab)get_store().add_element_user(PROPERTY_QNAME[33]);
            return target;
        }
    }

    /**
     * Removes the ith "ptab" element
     */
    @Override
    public void removePtab(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[33], i);
        }
    }

    /**
     * Gets a List of "lastRenderedPageBreak" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty> getLastRenderedPageBreakList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getLastRenderedPageBreakArray,
                this::setLastRenderedPageBreakArray,
                this::insertNewLastRenderedPageBreak,
                this::removeLastRenderedPageBreak,
                this::sizeOfLastRenderedPageBreakArray
            );
        }
    }

    /**
     * Gets array of all "lastRenderedPageBreak" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty[] getLastRenderedPageBreakArray() {
        return getXmlObjectArray(PROPERTY_QNAME[34], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty[0]);
    }

    /**
     * Gets ith "lastRenderedPageBreak" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty getLastRenderedPageBreakArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty)get_store().find_element_user(PROPERTY_QNAME[34], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "lastRenderedPageBreak" element
     */
    @Override
    public int sizeOfLastRenderedPageBreakArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[34]);
        }
    }

    /**
     * Sets array of all "lastRenderedPageBreak" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setLastRenderedPageBreakArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty[] lastRenderedPageBreakArray) {
        check_orphaned();
        arraySetterHelper(lastRenderedPageBreakArray, PROPERTY_QNAME[34]);
    }

    /**
     * Sets ith "lastRenderedPageBreak" element
     */
    @Override
    public void setLastRenderedPageBreakArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty lastRenderedPageBreak) {
        generatedSetterHelperImpl(lastRenderedPageBreak, PROPERTY_QNAME[34], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "lastRenderedPageBreak" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty insertNewLastRenderedPageBreak(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty)get_store().insert_element_user(PROPERTY_QNAME[34], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "lastRenderedPageBreak" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty addNewLastRenderedPageBreak() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty)get_store().add_element_user(PROPERTY_QNAME[34]);
            return target;
        }
    }

    /**
     * Removes the ith "lastRenderedPageBreak" element
     */
    @Override
    public void removeLastRenderedPageBreak(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[34], i);
        }
    }

    /**
     * Gets a List of "t" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.math.CTText> getT2List() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getT2Array,
                this::setT2Array,
                this::insertNewT2,
                this::removeT2,
                this::sizeOfT2Array
            );
        }
    }

    /**
     * Gets array of all "t" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTText[] getT2Array() {
        return getXmlObjectArray(PROPERTY_QNAME[35], new org.openxmlformats.schemas.officeDocument.x2006.math.CTText[0]);
    }

    /**
     * Gets ith "t" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTText getT2Array(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTText target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTText)get_store().find_element_user(PROPERTY_QNAME[35], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "t" element
     */
    @Override
    public int sizeOfT2Array() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[35]);
        }
    }

    /**
     * Sets array of all "t" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setT2Array(org.openxmlformats.schemas.officeDocument.x2006.math.CTText[] t2Array) {
        check_orphaned();
        arraySetterHelper(t2Array, PROPERTY_QNAME[35]);
    }

    /**
     * Sets ith "t" element
     */
    @Override
    public void setT2Array(int i, org.openxmlformats.schemas.officeDocument.x2006.math.CTText t2) {
        generatedSetterHelperImpl(t2, PROPERTY_QNAME[35], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "t" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTText insertNewT2(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTText target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTText)get_store().insert_element_user(PROPERTY_QNAME[35], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "t" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTText addNewT2() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTText target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTText)get_store().add_element_user(PROPERTY_QNAME[35]);
            return target;
        }
    }

    /**
     * Removes the ith "t" element
     */
    @Override
    public void removeT2(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[35], i);
        }
    }
}
