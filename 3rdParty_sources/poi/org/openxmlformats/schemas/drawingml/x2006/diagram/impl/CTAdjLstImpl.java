/*
 * XML Type:  CT_AdjLst
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.CTAdjLst
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_AdjLst(@http://schemas.openxmlformats.org/drawingml/2006/diagram).
 *
 * This is a complex type.
 */
public class CTAdjLstImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.diagram.CTAdjLst {
    private static final long serialVersionUID = 1L;

    public CTAdjLstImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "adj"),
    };


    /**
     * Gets a List of "adj" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.diagram.CTAdj> getAdjList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getAdjArray,
                this::setAdjArray,
                this::insertNewAdj,
                this::removeAdj,
                this::sizeOfAdjArray
            );
        }
    }

    /**
     * Gets array of all "adj" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTAdj[] getAdjArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.drawingml.x2006.diagram.CTAdj[0]);
    }

    /**
     * Gets ith "adj" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTAdj getAdjArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTAdj target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTAdj)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "adj" element
     */
    @Override
    public int sizeOfAdjArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "adj" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setAdjArray(org.openxmlformats.schemas.drawingml.x2006.diagram.CTAdj[] adjArray) {
        check_orphaned();
        arraySetterHelper(adjArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "adj" element
     */
    @Override
    public void setAdjArray(int i, org.openxmlformats.schemas.drawingml.x2006.diagram.CTAdj adj) {
        generatedSetterHelperImpl(adj, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "adj" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTAdj insertNewAdj(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTAdj target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTAdj)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "adj" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTAdj addNewAdj() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTAdj target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTAdj)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "adj" element
     */
    @Override
    public void removeAdj(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
