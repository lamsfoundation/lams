/*
 * XML Type:  CT_Endnotes
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEndnotes
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Endnotes(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTEndnotesImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEndnotes {
    private static final long serialVersionUID = 1L;

    public CTEndnotesImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "endnote"),
    };


    /**
     * Gets a List of "endnote" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdn> getEndnoteList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getEndnoteArray,
                this::setEndnoteArray,
                this::insertNewEndnote,
                this::removeEndnote,
                this::sizeOfEndnoteArray
            );
        }
    }

    /**
     * Gets array of all "endnote" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdn[] getEndnoteArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdn[0]);
    }

    /**
     * Gets ith "endnote" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdn getEndnoteArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdn target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdn)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "endnote" element
     */
    @Override
    public int sizeOfEndnoteArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "endnote" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setEndnoteArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdn[] endnoteArray) {
        check_orphaned();
        arraySetterHelper(endnoteArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "endnote" element
     */
    @Override
    public void setEndnoteArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdn endnote) {
        generatedSetterHelperImpl(endnote, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "endnote" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdn insertNewEndnote(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdn target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdn)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "endnote" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdn addNewEndnote() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdn target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdn)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "endnote" element
     */
    @Override
    public void removeEndnote(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
