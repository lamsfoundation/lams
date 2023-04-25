/*
 * XML Type:  CT_ExtensionList
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_ExtensionList(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public class CTExtensionListImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList {
    private static final long serialVersionUID = 1L;

    public CTExtensionListImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "ext"),
    };


    /**
     * Gets a List of "ext" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.chart.CTExtension> getExtList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getExtArray,
                this::setExtArray,
                this::insertNewExt,
                this::removeExt,
                this::sizeOfExtArray
            );
        }
    }

    /**
     * Gets array of all "ext" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTExtension[] getExtArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.drawingml.x2006.chart.CTExtension[0]);
    }

    /**
     * Gets ith "ext" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTExtension getExtArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTExtension target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTExtension)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "ext" element
     */
    @Override
    public int sizeOfExtArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "ext" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setExtArray(org.openxmlformats.schemas.drawingml.x2006.chart.CTExtension[] extArray) {
        check_orphaned();
        arraySetterHelper(extArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "ext" element
     */
    @Override
    public void setExtArray(int i, org.openxmlformats.schemas.drawingml.x2006.chart.CTExtension ext) {
        generatedSetterHelperImpl(ext, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "ext" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTExtension insertNewExt(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTExtension target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTExtension)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "ext" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTExtension addNewExt() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTExtension target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTExtension)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "ext" element
     */
    @Override
    public void removeExt(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
