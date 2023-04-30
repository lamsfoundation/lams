/*
 * XML Type:  CT_Drawing
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Drawing(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTDrawingImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing {
    private static final long serialVersionUID = 1L;

    public CTDrawingImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing", "anchor"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing", "inline"),
    };


    /**
     * Gets a List of "anchor" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTAnchor> getAnchorList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getAnchorArray,
                this::setAnchorArray,
                this::insertNewAnchor,
                this::removeAnchor,
                this::sizeOfAnchorArray
            );
        }
    }

    /**
     * Gets array of all "anchor" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTAnchor[] getAnchorArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTAnchor[0]);
    }

    /**
     * Gets ith "anchor" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTAnchor getAnchorArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTAnchor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTAnchor)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "anchor" element
     */
    @Override
    public int sizeOfAnchorArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "anchor" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setAnchorArray(org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTAnchor[] anchorArray) {
        check_orphaned();
        arraySetterHelper(anchorArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "anchor" element
     */
    @Override
    public void setAnchorArray(int i, org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTAnchor anchor) {
        generatedSetterHelperImpl(anchor, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "anchor" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTAnchor insertNewAnchor(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTAnchor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTAnchor)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "anchor" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTAnchor addNewAnchor() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTAnchor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTAnchor)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "anchor" element
     */
    @Override
    public void removeAnchor(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets a List of "inline" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline> getInlineList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getInlineArray,
                this::setInlineArray,
                this::insertNewInline,
                this::removeInline,
                this::sizeOfInlineArray
            );
        }
    }

    /**
     * Gets array of all "inline" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline[] getInlineArray() {
        return getXmlObjectArray(PROPERTY_QNAME[1], new org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline[0]);
    }

    /**
     * Gets ith "inline" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline getInlineArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "inline" element
     */
    @Override
    public int sizeOfInlineArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets array of all "inline" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setInlineArray(org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline[] inlineArray) {
        check_orphaned();
        arraySetterHelper(inlineArray, PROPERTY_QNAME[1]);
    }

    /**
     * Sets ith "inline" element
     */
    @Override
    public void setInlineArray(int i, org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline inline) {
        generatedSetterHelperImpl(inline, PROPERTY_QNAME[1], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "inline" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline insertNewInline(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline)get_store().insert_element_user(PROPERTY_QNAME[1], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "inline" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline addNewInline() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Removes the ith "inline" element
     */
    @Override
    public void removeInline(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], i);
        }
    }
}
