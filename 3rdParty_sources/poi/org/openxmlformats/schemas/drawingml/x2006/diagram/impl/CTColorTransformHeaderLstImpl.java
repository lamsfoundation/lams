/*
 * XML Type:  CT_ColorTransformHeaderLst
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.CTColorTransformHeaderLst
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_ColorTransformHeaderLst(@http://schemas.openxmlformats.org/drawingml/2006/diagram).
 *
 * This is a complex type.
 */
public class CTColorTransformHeaderLstImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.diagram.CTColorTransformHeaderLst {
    private static final long serialVersionUID = 1L;

    public CTColorTransformHeaderLstImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "colorsDefHdr"),
    };


    /**
     * Gets a List of "colorsDefHdr" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.diagram.CTColorTransformHeader> getColorsDefHdrList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getColorsDefHdrArray,
                this::setColorsDefHdrArray,
                this::insertNewColorsDefHdr,
                this::removeColorsDefHdr,
                this::sizeOfColorsDefHdrArray
            );
        }
    }

    /**
     * Gets array of all "colorsDefHdr" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTColorTransformHeader[] getColorsDefHdrArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.drawingml.x2006.diagram.CTColorTransformHeader[0]);
    }

    /**
     * Gets ith "colorsDefHdr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTColorTransformHeader getColorsDefHdrArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTColorTransformHeader target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTColorTransformHeader)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "colorsDefHdr" element
     */
    @Override
    public int sizeOfColorsDefHdrArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "colorsDefHdr" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setColorsDefHdrArray(org.openxmlformats.schemas.drawingml.x2006.diagram.CTColorTransformHeader[] colorsDefHdrArray) {
        check_orphaned();
        arraySetterHelper(colorsDefHdrArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "colorsDefHdr" element
     */
    @Override
    public void setColorsDefHdrArray(int i, org.openxmlformats.schemas.drawingml.x2006.diagram.CTColorTransformHeader colorsDefHdr) {
        generatedSetterHelperImpl(colorsDefHdr, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "colorsDefHdr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTColorTransformHeader insertNewColorsDefHdr(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTColorTransformHeader target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTColorTransformHeader)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "colorsDefHdr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTColorTransformHeader addNewColorsDefHdr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTColorTransformHeader target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTColorTransformHeader)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "colorsDefHdr" element
     */
    @Override
    public void removeColorsDefHdr(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
