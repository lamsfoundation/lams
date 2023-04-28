/*
 * XML Type:  CT_Styles
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Styles(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTStylesImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles {
    private static final long serialVersionUID = 1L;

    public CTStylesImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "docDefaults"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "latentStyles"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "style"),
    };


    /**
     * Gets the "docDefaults" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocDefaults getDocDefaults() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocDefaults target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocDefaults)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "docDefaults" element
     */
    @Override
    public boolean isSetDocDefaults() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "docDefaults" element
     */
    @Override
    public void setDocDefaults(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocDefaults docDefaults) {
        generatedSetterHelperImpl(docDefaults, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "docDefaults" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocDefaults addNewDocDefaults() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocDefaults target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocDefaults)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "docDefaults" element
     */
    @Override
    public void unsetDocDefaults() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "latentStyles" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLatentStyles getLatentStyles() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLatentStyles target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLatentStyles)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "latentStyles" element
     */
    @Override
    public boolean isSetLatentStyles() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "latentStyles" element
     */
    @Override
    public void setLatentStyles(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLatentStyles latentStyles) {
        generatedSetterHelperImpl(latentStyles, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "latentStyles" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLatentStyles addNewLatentStyles() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLatentStyles target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLatentStyles)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "latentStyles" element
     */
    @Override
    public void unsetLatentStyles() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets a List of "style" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle> getStyleList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getStyleArray,
                this::setStyleArray,
                this::insertNewStyle,
                this::removeStyle,
                this::sizeOfStyleArray
            );
        }
    }

    /**
     * Gets array of all "style" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle[] getStyleArray() {
        return getXmlObjectArray(PROPERTY_QNAME[2], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle[0]);
    }

    /**
     * Gets ith "style" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle getStyleArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle)get_store().find_element_user(PROPERTY_QNAME[2], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "style" element
     */
    @Override
    public int sizeOfStyleArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Sets array of all "style" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setStyleArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle[] styleArray) {
        check_orphaned();
        arraySetterHelper(styleArray, PROPERTY_QNAME[2]);
    }

    /**
     * Sets ith "style" element
     */
    @Override
    public void setStyleArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle style) {
        generatedSetterHelperImpl(style, PROPERTY_QNAME[2], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "style" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle insertNewStyle(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle)get_store().insert_element_user(PROPERTY_QNAME[2], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "style" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle addNewStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Removes the ith "style" element
     */
    @Override
    public void removeStyle(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], i);
        }
    }
}
