/*
 * XML Type:  TransformsType
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.TransformsType
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML TransformsType(@http://www.w3.org/2000/09/xmldsig#).
 *
 * This is a complex type.
 */
public class TransformsTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.w3.x2000.x09.xmldsig.TransformsType {
    private static final long serialVersionUID = 1L;

    public TransformsTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://www.w3.org/2000/09/xmldsig#", "Transform"),
    };


    /**
     * Gets a List of "Transform" elements
     */
    @Override
    public java.util.List<org.w3.x2000.x09.xmldsig.TransformType> getTransformList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getTransformArray,
                this::setTransformArray,
                this::insertNewTransform,
                this::removeTransform,
                this::sizeOfTransformArray
            );
        }
    }

    /**
     * Gets array of all "Transform" elements
     */
    @Override
    public org.w3.x2000.x09.xmldsig.TransformType[] getTransformArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.w3.x2000.x09.xmldsig.TransformType[0]);
    }

    /**
     * Gets ith "Transform" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.TransformType getTransformArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.TransformType target = null;
            target = (org.w3.x2000.x09.xmldsig.TransformType)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "Transform" element
     */
    @Override
    public int sizeOfTransformArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "Transform" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setTransformArray(org.w3.x2000.x09.xmldsig.TransformType[] transformArray) {
        check_orphaned();
        arraySetterHelper(transformArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "Transform" element
     */
    @Override
    public void setTransformArray(int i, org.w3.x2000.x09.xmldsig.TransformType transform) {
        generatedSetterHelperImpl(transform, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Transform" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.TransformType insertNewTransform(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.TransformType target = null;
            target = (org.w3.x2000.x09.xmldsig.TransformType)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "Transform" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.TransformType addNewTransform() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.TransformType target = null;
            target = (org.w3.x2000.x09.xmldsig.TransformType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "Transform" element
     */
    @Override
    public void removeTransform(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
