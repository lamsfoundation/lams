/*
 * XML Type:  CT_DocVars
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocVars
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_DocVars(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTDocVarsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocVars {
    private static final long serialVersionUID = 1L;

    public CTDocVarsImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "docVar"),
    };


    /**
     * Gets a List of "docVar" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocVar> getDocVarList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getDocVarArray,
                this::setDocVarArray,
                this::insertNewDocVar,
                this::removeDocVar,
                this::sizeOfDocVarArray
            );
        }
    }

    /**
     * Gets array of all "docVar" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocVar[] getDocVarArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocVar[0]);
    }

    /**
     * Gets ith "docVar" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocVar getDocVarArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocVar target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocVar)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "docVar" element
     */
    @Override
    public int sizeOfDocVarArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "docVar" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setDocVarArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocVar[] docVarArray) {
        check_orphaned();
        arraySetterHelper(docVarArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "docVar" element
     */
    @Override
    public void setDocVarArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocVar docVar) {
        generatedSetterHelperImpl(docVar, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "docVar" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocVar insertNewDocVar(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocVar target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocVar)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "docVar" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocVar addNewDocVar() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocVar target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocVar)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "docVar" element
     */
    @Override
    public void removeDocVar(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
