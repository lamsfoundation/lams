/*
 * XML Type:  CT_Captions
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCaptions
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Captions(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTCaptionsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCaptions {
    private static final long serialVersionUID = 1L;

    public CTCaptionsImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "caption"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "autoCaptions"),
    };


    /**
     * Gets a List of "caption" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCaption> getCaptionList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCaptionArray,
                this::setCaptionArray,
                this::insertNewCaption,
                this::removeCaption,
                this::sizeOfCaptionArray
            );
        }
    }

    /**
     * Gets array of all "caption" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCaption[] getCaptionArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCaption[0]);
    }

    /**
     * Gets ith "caption" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCaption getCaptionArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCaption target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCaption)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "caption" element
     */
    @Override
    public int sizeOfCaptionArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "caption" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCaptionArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCaption[] captionArray) {
        check_orphaned();
        arraySetterHelper(captionArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "caption" element
     */
    @Override
    public void setCaptionArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCaption caption) {
        generatedSetterHelperImpl(caption, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "caption" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCaption insertNewCaption(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCaption target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCaption)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "caption" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCaption addNewCaption() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCaption target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCaption)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "caption" element
     */
    @Override
    public void removeCaption(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets the "autoCaptions" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAutoCaptions getAutoCaptions() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAutoCaptions target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAutoCaptions)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "autoCaptions" element
     */
    @Override
    public boolean isSetAutoCaptions() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "autoCaptions" element
     */
    @Override
    public void setAutoCaptions(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAutoCaptions autoCaptions) {
        generatedSetterHelperImpl(autoCaptions, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "autoCaptions" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAutoCaptions addNewAutoCaptions() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAutoCaptions target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAutoCaptions)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "autoCaptions" element
     */
    @Override
    public void unsetAutoCaptions() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }
}
