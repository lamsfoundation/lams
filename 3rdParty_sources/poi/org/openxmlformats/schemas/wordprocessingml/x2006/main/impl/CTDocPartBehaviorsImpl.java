/*
 * XML Type:  CT_DocPartBehaviors
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartBehaviors
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_DocPartBehaviors(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTDocPartBehaviorsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartBehaviors {
    private static final long serialVersionUID = 1L;

    public CTDocPartBehaviorsImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "behavior"),
    };


    /**
     * Gets a List of "behavior" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartBehavior> getBehaviorList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getBehaviorArray,
                this::setBehaviorArray,
                this::insertNewBehavior,
                this::removeBehavior,
                this::sizeOfBehaviorArray
            );
        }
    }

    /**
     * Gets array of all "behavior" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartBehavior[] getBehaviorArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartBehavior[0]);
    }

    /**
     * Gets ith "behavior" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartBehavior getBehaviorArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartBehavior target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartBehavior)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "behavior" element
     */
    @Override
    public int sizeOfBehaviorArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "behavior" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setBehaviorArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartBehavior[] behaviorArray) {
        check_orphaned();
        arraySetterHelper(behaviorArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "behavior" element
     */
    @Override
    public void setBehaviorArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartBehavior behavior) {
        generatedSetterHelperImpl(behavior, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "behavior" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartBehavior insertNewBehavior(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartBehavior target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartBehavior)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "behavior" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartBehavior addNewBehavior() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartBehavior target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartBehavior)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "behavior" element
     */
    @Override
    public void removeBehavior(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
