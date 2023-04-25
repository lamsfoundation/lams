/*
 * XML Type:  CT_Footnotes
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFootnotes
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Footnotes(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTFootnotesImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFootnotes {
    private static final long serialVersionUID = 1L;

    public CTFootnotesImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "footnote"),
    };


    /**
     * Gets a List of "footnote" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdn> getFootnoteList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getFootnoteArray,
                this::setFootnoteArray,
                this::insertNewFootnote,
                this::removeFootnote,
                this::sizeOfFootnoteArray
            );
        }
    }

    /**
     * Gets array of all "footnote" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdn[] getFootnoteArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdn[0]);
    }

    /**
     * Gets ith "footnote" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdn getFootnoteArray(int i) {
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
     * Returns number of "footnote" element
     */
    @Override
    public int sizeOfFootnoteArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "footnote" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setFootnoteArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdn[] footnoteArray) {
        check_orphaned();
        arraySetterHelper(footnoteArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "footnote" element
     */
    @Override
    public void setFootnoteArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdn footnote) {
        generatedSetterHelperImpl(footnote, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "footnote" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdn insertNewFootnote(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdn target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdn)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "footnote" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdn addNewFootnote() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdn target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdn)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "footnote" element
     */
    @Override
    public void removeFootnote(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
