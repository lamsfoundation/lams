/*
 * XML Type:  CT_DiagramDefinitionHeaderLst
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.CTDiagramDefinitionHeaderLst
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_DiagramDefinitionHeaderLst(@http://schemas.openxmlformats.org/drawingml/2006/diagram).
 *
 * This is a complex type.
 */
public class CTDiagramDefinitionHeaderLstImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.diagram.CTDiagramDefinitionHeaderLst {
    private static final long serialVersionUID = 1L;

    public CTDiagramDefinitionHeaderLstImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "layoutDefHdr"),
    };


    /**
     * Gets a List of "layoutDefHdr" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.diagram.CTDiagramDefinitionHeader> getLayoutDefHdrList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getLayoutDefHdrArray,
                this::setLayoutDefHdrArray,
                this::insertNewLayoutDefHdr,
                this::removeLayoutDefHdr,
                this::sizeOfLayoutDefHdrArray
            );
        }
    }

    /**
     * Gets array of all "layoutDefHdr" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTDiagramDefinitionHeader[] getLayoutDefHdrArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.drawingml.x2006.diagram.CTDiagramDefinitionHeader[0]);
    }

    /**
     * Gets ith "layoutDefHdr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTDiagramDefinitionHeader getLayoutDefHdrArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTDiagramDefinitionHeader target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTDiagramDefinitionHeader)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "layoutDefHdr" element
     */
    @Override
    public int sizeOfLayoutDefHdrArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "layoutDefHdr" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setLayoutDefHdrArray(org.openxmlformats.schemas.drawingml.x2006.diagram.CTDiagramDefinitionHeader[] layoutDefHdrArray) {
        check_orphaned();
        arraySetterHelper(layoutDefHdrArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "layoutDefHdr" element
     */
    @Override
    public void setLayoutDefHdrArray(int i, org.openxmlformats.schemas.drawingml.x2006.diagram.CTDiagramDefinitionHeader layoutDefHdr) {
        generatedSetterHelperImpl(layoutDefHdr, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "layoutDefHdr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTDiagramDefinitionHeader insertNewLayoutDefHdr(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTDiagramDefinitionHeader target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTDiagramDefinitionHeader)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "layoutDefHdr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTDiagramDefinitionHeader addNewLayoutDefHdr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTDiagramDefinitionHeader target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTDiagramDefinitionHeader)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "layoutDefHdr" element
     */
    @Override
    public void removeLayoutDefHdr(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
