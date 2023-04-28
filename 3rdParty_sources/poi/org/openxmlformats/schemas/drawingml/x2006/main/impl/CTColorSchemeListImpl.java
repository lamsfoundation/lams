/*
 * XML Type:  CT_ColorSchemeList
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTColorSchemeList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_ColorSchemeList(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTColorSchemeListImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTColorSchemeList {
    private static final long serialVersionUID = 1L;

    public CTColorSchemeListImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "extraClrScheme"),
    };


    /**
     * Gets a List of "extraClrScheme" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTColorSchemeAndMapping> getExtraClrSchemeList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getExtraClrSchemeArray,
                this::setExtraClrSchemeArray,
                this::insertNewExtraClrScheme,
                this::removeExtraClrScheme,
                this::sizeOfExtraClrSchemeArray
            );
        }
    }

    /**
     * Gets array of all "extraClrScheme" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTColorSchemeAndMapping[] getExtraClrSchemeArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.drawingml.x2006.main.CTColorSchemeAndMapping[0]);
    }

    /**
     * Gets ith "extraClrScheme" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTColorSchemeAndMapping getExtraClrSchemeArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTColorSchemeAndMapping target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTColorSchemeAndMapping)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "extraClrScheme" element
     */
    @Override
    public int sizeOfExtraClrSchemeArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "extraClrScheme" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setExtraClrSchemeArray(org.openxmlformats.schemas.drawingml.x2006.main.CTColorSchemeAndMapping[] extraClrSchemeArray) {
        check_orphaned();
        arraySetterHelper(extraClrSchemeArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "extraClrScheme" element
     */
    @Override
    public void setExtraClrSchemeArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTColorSchemeAndMapping extraClrScheme) {
        generatedSetterHelperImpl(extraClrScheme, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "extraClrScheme" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTColorSchemeAndMapping insertNewExtraClrScheme(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTColorSchemeAndMapping target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTColorSchemeAndMapping)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "extraClrScheme" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTColorSchemeAndMapping addNewExtraClrScheme() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTColorSchemeAndMapping target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTColorSchemeAndMapping)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "extraClrScheme" element
     */
    @Override
    public void removeExtraClrScheme(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
