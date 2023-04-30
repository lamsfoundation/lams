/*
 * XML Type:  CT_Path2DCubicBezierTo
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DCubicBezierTo
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Path2DCubicBezierTo(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTPath2DCubicBezierToImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DCubicBezierTo {
    private static final long serialVersionUID = 1L;

    public CTPath2DCubicBezierToImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "pt"),
    };


    /**
     * Gets a List of "pt" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTAdjPoint2D> getPtList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getPtArray,
                this::setPtArray,
                this::insertNewPt,
                this::removePt,
                this::sizeOfPtArray
            );
        }
    }

    /**
     * Gets array of all "pt" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAdjPoint2D[] getPtArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.drawingml.x2006.main.CTAdjPoint2D[0]);
    }

    /**
     * Gets ith "pt" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAdjPoint2D getPtArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTAdjPoint2D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTAdjPoint2D)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "pt" element
     */
    @Override
    public int sizeOfPtArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "pt" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setPtArray(org.openxmlformats.schemas.drawingml.x2006.main.CTAdjPoint2D[] ptArray) {
        check_orphaned();
        arraySetterHelper(ptArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "pt" element
     */
    @Override
    public void setPtArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTAdjPoint2D pt) {
        generatedSetterHelperImpl(pt, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "pt" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAdjPoint2D insertNewPt(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTAdjPoint2D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTAdjPoint2D)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "pt" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAdjPoint2D addNewPt() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTAdjPoint2D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTAdjPoint2D)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "pt" element
     */
    @Override
    public void removePt(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
