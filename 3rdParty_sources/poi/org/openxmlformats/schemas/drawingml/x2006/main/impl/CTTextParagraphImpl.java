/*
 * XML Type:  CT_TextParagraph
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TextParagraph(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTTextParagraphImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph {
    private static final long serialVersionUID = 1L;

    public CTTextParagraphImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "pPr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "r"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "br"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "fld"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "endParaRPr"),
    };


    /**
     * Gets the "pPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties getPPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties)get_store().find_element_user(PROPERTY_QNAME[0], 0);
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
    public void setPPr(org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties pPr) {
        generatedSetterHelperImpl(pPr, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "pPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties addNewPPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties)get_store().add_element_user(PROPERTY_QNAME[0]);
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
     * Gets a List of "r" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun> getRList() {
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
    public org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun[] getRArray() {
        return getXmlObjectArray(PROPERTY_QNAME[1], new org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun[0]);
    }

    /**
     * Gets ith "r" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun getRArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun)get_store().find_element_user(PROPERTY_QNAME[1], i);
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
            return get_store().count_elements(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets array of all "r" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setRArray(org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun[] rArray) {
        check_orphaned();
        arraySetterHelper(rArray, PROPERTY_QNAME[1]);
    }

    /**
     * Sets ith "r" element
     */
    @Override
    public void setRArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun r) {
        generatedSetterHelperImpl(r, PROPERTY_QNAME[1], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "r" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun insertNewR(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun)get_store().insert_element_user(PROPERTY_QNAME[1], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "r" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun addNewR() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun)get_store().add_element_user(PROPERTY_QNAME[1]);
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
            get_store().remove_element(PROPERTY_QNAME[1], i);
        }
    }

    /**
     * Gets a List of "br" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTTextLineBreak> getBrList() {
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
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextLineBreak[] getBrArray() {
        return getXmlObjectArray(PROPERTY_QNAME[2], new org.openxmlformats.schemas.drawingml.x2006.main.CTTextLineBreak[0]);
    }

    /**
     * Gets ith "br" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextLineBreak getBrArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextLineBreak target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextLineBreak)get_store().find_element_user(PROPERTY_QNAME[2], i);
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
    public void setBrArray(org.openxmlformats.schemas.drawingml.x2006.main.CTTextLineBreak[] brArray) {
        check_orphaned();
        arraySetterHelper(brArray, PROPERTY_QNAME[2]);
    }

    /**
     * Sets ith "br" element
     */
    @Override
    public void setBrArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTTextLineBreak br) {
        generatedSetterHelperImpl(br, PROPERTY_QNAME[2], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "br" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextLineBreak insertNewBr(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextLineBreak target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextLineBreak)get_store().insert_element_user(PROPERTY_QNAME[2], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "br" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextLineBreak addNewBr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextLineBreak target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextLineBreak)get_store().add_element_user(PROPERTY_QNAME[2]);
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
     * Gets a List of "fld" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTTextField> getFldList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getFldArray,
                this::setFldArray,
                this::insertNewFld,
                this::removeFld,
                this::sizeOfFldArray
            );
        }
    }

    /**
     * Gets array of all "fld" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextField[] getFldArray() {
        return getXmlObjectArray(PROPERTY_QNAME[3], new org.openxmlformats.schemas.drawingml.x2006.main.CTTextField[0]);
    }

    /**
     * Gets ith "fld" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextField getFldArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextField target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextField)get_store().find_element_user(PROPERTY_QNAME[3], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "fld" element
     */
    @Override
    public int sizeOfFldArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Sets array of all "fld" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setFldArray(org.openxmlformats.schemas.drawingml.x2006.main.CTTextField[] fldArray) {
        check_orphaned();
        arraySetterHelper(fldArray, PROPERTY_QNAME[3]);
    }

    /**
     * Sets ith "fld" element
     */
    @Override
    public void setFldArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTTextField fld) {
        generatedSetterHelperImpl(fld, PROPERTY_QNAME[3], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "fld" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextField insertNewFld(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextField target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextField)get_store().insert_element_user(PROPERTY_QNAME[3], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "fld" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextField addNewFld() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextField target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextField)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Removes the ith "fld" element
     */
    @Override
    public void removeFld(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], i);
        }
    }

    /**
     * Gets the "endParaRPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties getEndParaRPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "endParaRPr" element
     */
    @Override
    public boolean isSetEndParaRPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "endParaRPr" element
     */
    @Override
    public void setEndParaRPr(org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties endParaRPr) {
        generatedSetterHelperImpl(endParaRPr, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "endParaRPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties addNewEndParaRPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "endParaRPr" element
     */
    @Override
    public void unsetEndParaRPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }
}
