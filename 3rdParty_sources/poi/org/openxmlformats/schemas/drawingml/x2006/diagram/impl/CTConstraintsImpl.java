/*
 * XML Type:  CT_Constraints
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.CTConstraints
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Constraints(@http://schemas.openxmlformats.org/drawingml/2006/diagram).
 *
 * This is a complex type.
 */
public class CTConstraintsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.diagram.CTConstraints {
    private static final long serialVersionUID = 1L;

    public CTConstraintsImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "constr"),
    };


    /**
     * Gets a List of "constr" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.diagram.CTConstraint> getConstrList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getConstrArray,
                this::setConstrArray,
                this::insertNewConstr,
                this::removeConstr,
                this::sizeOfConstrArray
            );
        }
    }

    /**
     * Gets array of all "constr" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTConstraint[] getConstrArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.drawingml.x2006.diagram.CTConstraint[0]);
    }

    /**
     * Gets ith "constr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTConstraint getConstrArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTConstraint target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTConstraint)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "constr" element
     */
    @Override
    public int sizeOfConstrArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "constr" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setConstrArray(org.openxmlformats.schemas.drawingml.x2006.diagram.CTConstraint[] constrArray) {
        check_orphaned();
        arraySetterHelper(constrArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "constr" element
     */
    @Override
    public void setConstrArray(int i, org.openxmlformats.schemas.drawingml.x2006.diagram.CTConstraint constr) {
        generatedSetterHelperImpl(constr, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "constr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTConstraint insertNewConstr(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTConstraint target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTConstraint)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "constr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTConstraint addNewConstr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTConstraint target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTConstraint)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "constr" element
     */
    @Override
    public void removeConstr(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
