/*
 * XML Type:  CT_Div
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDiv
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Div(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTDivImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDiv {
    private static final long serialVersionUID = 1L;

    public CTDivImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "blockQuote"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "bodyDiv"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "marLeft"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "marRight"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "marTop"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "marBottom"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "divBdr"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "divsChild"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "id"),
    };


    /**
     * Gets the "blockQuote" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getBlockQuote() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "blockQuote" element
     */
    @Override
    public boolean isSetBlockQuote() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "blockQuote" element
     */
    @Override
    public void setBlockQuote(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff blockQuote) {
        generatedSetterHelperImpl(blockQuote, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "blockQuote" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewBlockQuote() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "blockQuote" element
     */
    @Override
    public void unsetBlockQuote() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "bodyDiv" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getBodyDiv() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "bodyDiv" element
     */
    @Override
    public boolean isSetBodyDiv() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "bodyDiv" element
     */
    @Override
    public void setBodyDiv(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff bodyDiv) {
        generatedSetterHelperImpl(bodyDiv, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "bodyDiv" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewBodyDiv() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "bodyDiv" element
     */
    @Override
    public void unsetBodyDiv() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "marLeft" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedTwipsMeasure getMarLeft() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedTwipsMeasure)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "marLeft" element
     */
    @Override
    public void setMarLeft(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedTwipsMeasure marLeft) {
        generatedSetterHelperImpl(marLeft, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "marLeft" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedTwipsMeasure addNewMarLeft() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedTwipsMeasure)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Gets the "marRight" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedTwipsMeasure getMarRight() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedTwipsMeasure)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "marRight" element
     */
    @Override
    public void setMarRight(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedTwipsMeasure marRight) {
        generatedSetterHelperImpl(marRight, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "marRight" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedTwipsMeasure addNewMarRight() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedTwipsMeasure)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Gets the "marTop" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedTwipsMeasure getMarTop() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedTwipsMeasure)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "marTop" element
     */
    @Override
    public void setMarTop(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedTwipsMeasure marTop) {
        generatedSetterHelperImpl(marTop, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "marTop" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedTwipsMeasure addNewMarTop() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedTwipsMeasure)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Gets the "marBottom" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedTwipsMeasure getMarBottom() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedTwipsMeasure)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "marBottom" element
     */
    @Override
    public void setMarBottom(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedTwipsMeasure marBottom) {
        generatedSetterHelperImpl(marBottom, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "marBottom" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedTwipsMeasure addNewMarBottom() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedTwipsMeasure)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Gets the "divBdr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDivBdr getDivBdr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDivBdr target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDivBdr)get_store().find_element_user(PROPERTY_QNAME[6], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "divBdr" element
     */
    @Override
    public boolean isSetDivBdr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]) != 0;
        }
    }

    /**
     * Sets the "divBdr" element
     */
    @Override
    public void setDivBdr(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDivBdr divBdr) {
        generatedSetterHelperImpl(divBdr, PROPERTY_QNAME[6], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "divBdr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDivBdr addNewDivBdr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDivBdr target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDivBdr)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Unsets the "divBdr" element
     */
    @Override
    public void unsetDivBdr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], 0);
        }
    }

    /**
     * Gets a List of "divsChild" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDivs> getDivsChildList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getDivsChildArray,
                this::setDivsChildArray,
                this::insertNewDivsChild,
                this::removeDivsChild,
                this::sizeOfDivsChildArray
            );
        }
    }

    /**
     * Gets array of all "divsChild" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDivs[] getDivsChildArray() {
        return getXmlObjectArray(PROPERTY_QNAME[7], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDivs[0]);
    }

    /**
     * Gets ith "divsChild" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDivs getDivsChildArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDivs target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDivs)get_store().find_element_user(PROPERTY_QNAME[7], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "divsChild" element
     */
    @Override
    public int sizeOfDivsChildArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]);
        }
    }

    /**
     * Sets array of all "divsChild" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setDivsChildArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDivs[] divsChildArray) {
        check_orphaned();
        arraySetterHelper(divsChildArray, PROPERTY_QNAME[7]);
    }

    /**
     * Sets ith "divsChild" element
     */
    @Override
    public void setDivsChildArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDivs divsChild) {
        generatedSetterHelperImpl(divsChild, PROPERTY_QNAME[7], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "divsChild" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDivs insertNewDivsChild(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDivs target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDivs)get_store().insert_element_user(PROPERTY_QNAME[7], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "divsChild" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDivs addNewDivsChild() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDivs target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDivs)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Removes the ith "divsChild" element
     */
    @Override
    public void removeDivsChild(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[7], i);
        }
    }

    /**
     * Gets the "id" attribute
     */
    @Override
    public java.math.BigInteger getId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            return (target == null) ? null : target.getBigIntegerValue();
        }
    }

    /**
     * Gets (as xml) the "id" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber xgetId() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * Sets the "id" attribute
     */
    @Override
    public void setId(java.math.BigInteger id) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.setBigIntegerValue(id);
        }
    }

    /**
     * Sets (as xml) the "id" attribute
     */
    @Override
    public void xsetId(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber id) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.set(id);
        }
    }
}
