/*
 * XML Type:  CT_StyleDefinitionHeaderLst
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinitionHeaderLst
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_StyleDefinitionHeaderLst(@http://schemas.openxmlformats.org/drawingml/2006/diagram).
 *
 * This is a complex type.
 */
public class CTStyleDefinitionHeaderLstImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinitionHeaderLst {
    private static final long serialVersionUID = 1L;

    public CTStyleDefinitionHeaderLstImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "styleDefHdr"),
    };


    /**
     * Gets a List of "styleDefHdr" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinitionHeader> getStyleDefHdrList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getStyleDefHdrArray,
                this::setStyleDefHdrArray,
                this::insertNewStyleDefHdr,
                this::removeStyleDefHdr,
                this::sizeOfStyleDefHdrArray
            );
        }
    }

    /**
     * Gets array of all "styleDefHdr" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinitionHeader[] getStyleDefHdrArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinitionHeader[0]);
    }

    /**
     * Gets ith "styleDefHdr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinitionHeader getStyleDefHdrArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinitionHeader target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinitionHeader)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "styleDefHdr" element
     */
    @Override
    public int sizeOfStyleDefHdrArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "styleDefHdr" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setStyleDefHdrArray(org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinitionHeader[] styleDefHdrArray) {
        check_orphaned();
        arraySetterHelper(styleDefHdrArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "styleDefHdr" element
     */
    @Override
    public void setStyleDefHdrArray(int i, org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinitionHeader styleDefHdr) {
        generatedSetterHelperImpl(styleDefHdr, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "styleDefHdr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinitionHeader insertNewStyleDefHdr(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinitionHeader target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinitionHeader)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "styleDefHdr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinitionHeader addNewStyleDefHdr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinitionHeader target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinitionHeader)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "styleDefHdr" element
     */
    @Override
    public void removeStyleDefHdr(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
