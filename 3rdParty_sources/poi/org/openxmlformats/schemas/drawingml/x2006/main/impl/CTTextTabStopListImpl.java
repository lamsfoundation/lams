/*
 * XML Type:  CT_TextTabStopList
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTTextTabStopList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TextTabStopList(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTTextTabStopListImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTTextTabStopList {
    private static final long serialVersionUID = 1L;

    public CTTextTabStopListImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "tab"),
    };


    /**
     * Gets a List of "tab" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTTextTabStop> getTabList() {
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
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextTabStop[] getTabArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.drawingml.x2006.main.CTTextTabStop[0]);
    }

    /**
     * Gets ith "tab" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextTabStop getTabArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextTabStop target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextTabStop)get_store().find_element_user(PROPERTY_QNAME[0], i);
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
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "tab" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setTabArray(org.openxmlformats.schemas.drawingml.x2006.main.CTTextTabStop[] tabArray) {
        check_orphaned();
        arraySetterHelper(tabArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "tab" element
     */
    @Override
    public void setTabArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTTextTabStop tab) {
        generatedSetterHelperImpl(tab, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "tab" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextTabStop insertNewTab(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextTabStop target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextTabStop)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "tab" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextTabStop addNewTab() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextTabStop target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextTabStop)get_store().add_element_user(PROPERTY_QNAME[0]);
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
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
