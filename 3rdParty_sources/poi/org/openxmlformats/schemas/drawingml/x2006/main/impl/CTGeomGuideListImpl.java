/*
 * XML Type:  CT_GeomGuideList
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuideList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_GeomGuideList(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTGeomGuideListImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuideList {
    private static final long serialVersionUID = 1L;

    public CTGeomGuideListImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "gd"),
    };


    /**
     * Gets a List of "gd" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuide> getGdList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getGdArray,
                this::setGdArray,
                this::insertNewGd,
                this::removeGd,
                this::sizeOfGdArray
            );
        }
    }

    /**
     * Gets array of all "gd" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuide[] getGdArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuide[0]);
    }

    /**
     * Gets ith "gd" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuide getGdArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuide target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuide)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "gd" element
     */
    @Override
    public int sizeOfGdArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "gd" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setGdArray(org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuide[] gdArray) {
        check_orphaned();
        arraySetterHelper(gdArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "gd" element
     */
    @Override
    public void setGdArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuide gd) {
        generatedSetterHelperImpl(gd, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "gd" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuide insertNewGd(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuide target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuide)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "gd" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuide addNewGd() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuide target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuide)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "gd" element
     */
    @Override
    public void removeGd(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
