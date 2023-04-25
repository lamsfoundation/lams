/*
 * XML Type:  CT_DocParts
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocParts
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_DocParts(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTDocPartsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocParts {
    private static final long serialVersionUID = 1L;

    public CTDocPartsImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "docPart"),
    };


    /**
     * Gets a List of "docPart" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPart> getDocPartList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getDocPartArray,
                this::setDocPartArray,
                this::insertNewDocPart,
                this::removeDocPart,
                this::sizeOfDocPartArray
            );
        }
    }

    /**
     * Gets array of all "docPart" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPart[] getDocPartArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPart[0]);
    }

    /**
     * Gets ith "docPart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPart getDocPartArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPart target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPart)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "docPart" element
     */
    @Override
    public int sizeOfDocPartArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "docPart" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setDocPartArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPart[] docPartArray) {
        check_orphaned();
        arraySetterHelper(docPartArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "docPart" element
     */
    @Override
    public void setDocPartArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPart docPart) {
        generatedSetterHelperImpl(docPart, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "docPart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPart insertNewDocPart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPart target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPart)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "docPart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPart addNewDocPart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPart target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPart)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "docPart" element
     */
    @Override
    public void removeDocPart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
