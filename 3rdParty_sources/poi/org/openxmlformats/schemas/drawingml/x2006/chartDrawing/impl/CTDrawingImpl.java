/*
 * XML Type:  CT_Drawing
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chartDrawing
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTDrawing
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chartDrawing.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Drawing(@http://schemas.openxmlformats.org/drawingml/2006/chartDrawing).
 *
 * This is a complex type.
 */
public class CTDrawingImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTDrawing {
    private static final long serialVersionUID = 1L;

    public CTDrawingImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chartDrawing", "relSizeAnchor"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chartDrawing", "absSizeAnchor"),
    };


    /**
     * Gets a List of "relSizeAnchor" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTRelSizeAnchor> getRelSizeAnchorList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getRelSizeAnchorArray,
                this::setRelSizeAnchorArray,
                this::insertNewRelSizeAnchor,
                this::removeRelSizeAnchor,
                this::sizeOfRelSizeAnchorArray
            );
        }
    }

    /**
     * Gets array of all "relSizeAnchor" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTRelSizeAnchor[] getRelSizeAnchorArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTRelSizeAnchor[0]);
    }

    /**
     * Gets ith "relSizeAnchor" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTRelSizeAnchor getRelSizeAnchorArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTRelSizeAnchor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTRelSizeAnchor)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "relSizeAnchor" element
     */
    @Override
    public int sizeOfRelSizeAnchorArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "relSizeAnchor" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setRelSizeAnchorArray(org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTRelSizeAnchor[] relSizeAnchorArray) {
        check_orphaned();
        arraySetterHelper(relSizeAnchorArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "relSizeAnchor" element
     */
    @Override
    public void setRelSizeAnchorArray(int i, org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTRelSizeAnchor relSizeAnchor) {
        generatedSetterHelperImpl(relSizeAnchor, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "relSizeAnchor" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTRelSizeAnchor insertNewRelSizeAnchor(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTRelSizeAnchor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTRelSizeAnchor)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "relSizeAnchor" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTRelSizeAnchor addNewRelSizeAnchor() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTRelSizeAnchor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTRelSizeAnchor)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "relSizeAnchor" element
     */
    @Override
    public void removeRelSizeAnchor(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets a List of "absSizeAnchor" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTAbsSizeAnchor> getAbsSizeAnchorList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getAbsSizeAnchorArray,
                this::setAbsSizeAnchorArray,
                this::insertNewAbsSizeAnchor,
                this::removeAbsSizeAnchor,
                this::sizeOfAbsSizeAnchorArray
            );
        }
    }

    /**
     * Gets array of all "absSizeAnchor" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTAbsSizeAnchor[] getAbsSizeAnchorArray() {
        return getXmlObjectArray(PROPERTY_QNAME[1], new org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTAbsSizeAnchor[0]);
    }

    /**
     * Gets ith "absSizeAnchor" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTAbsSizeAnchor getAbsSizeAnchorArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTAbsSizeAnchor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTAbsSizeAnchor)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "absSizeAnchor" element
     */
    @Override
    public int sizeOfAbsSizeAnchorArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets array of all "absSizeAnchor" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setAbsSizeAnchorArray(org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTAbsSizeAnchor[] absSizeAnchorArray) {
        check_orphaned();
        arraySetterHelper(absSizeAnchorArray, PROPERTY_QNAME[1]);
    }

    /**
     * Sets ith "absSizeAnchor" element
     */
    @Override
    public void setAbsSizeAnchorArray(int i, org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTAbsSizeAnchor absSizeAnchor) {
        generatedSetterHelperImpl(absSizeAnchor, PROPERTY_QNAME[1], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "absSizeAnchor" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTAbsSizeAnchor insertNewAbsSizeAnchor(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTAbsSizeAnchor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTAbsSizeAnchor)get_store().insert_element_user(PROPERTY_QNAME[1], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "absSizeAnchor" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTAbsSizeAnchor addNewAbsSizeAnchor() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTAbsSizeAnchor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTAbsSizeAnchor)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Removes the ith "absSizeAnchor" element
     */
    @Override
    public void removeAbsSizeAnchor(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], i);
        }
    }
}
