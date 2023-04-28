/*
 * XML Type:  CT_AutoCaptions
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAutoCaptions
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_AutoCaptions(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTAutoCaptionsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAutoCaptions {
    private static final long serialVersionUID = 1L;

    public CTAutoCaptionsImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "autoCaption"),
    };


    /**
     * Gets a List of "autoCaption" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAutoCaption> getAutoCaptionList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getAutoCaptionArray,
                this::setAutoCaptionArray,
                this::insertNewAutoCaption,
                this::removeAutoCaption,
                this::sizeOfAutoCaptionArray
            );
        }
    }

    /**
     * Gets array of all "autoCaption" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAutoCaption[] getAutoCaptionArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAutoCaption[0]);
    }

    /**
     * Gets ith "autoCaption" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAutoCaption getAutoCaptionArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAutoCaption target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAutoCaption)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "autoCaption" element
     */
    @Override
    public int sizeOfAutoCaptionArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "autoCaption" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setAutoCaptionArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAutoCaption[] autoCaptionArray) {
        check_orphaned();
        arraySetterHelper(autoCaptionArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "autoCaption" element
     */
    @Override
    public void setAutoCaptionArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAutoCaption autoCaption) {
        generatedSetterHelperImpl(autoCaption, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "autoCaption" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAutoCaption insertNewAutoCaption(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAutoCaption target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAutoCaption)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "autoCaption" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAutoCaption addNewAutoCaption() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAutoCaption target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAutoCaption)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "autoCaption" element
     */
    @Override
    public void removeAutoCaption(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
