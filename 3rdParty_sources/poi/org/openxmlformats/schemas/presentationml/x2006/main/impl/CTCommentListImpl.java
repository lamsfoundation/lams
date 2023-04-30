/*
 * XML Type:  CT_CommentList
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTCommentList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_CommentList(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTCommentListImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTCommentList {
    private static final long serialVersionUID = 1L;

    public CTCommentListImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "cm"),
    };


    /**
     * Gets a List of "cm" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTComment> getCmList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCmArray,
                this::setCmArray,
                this::insertNewCm,
                this::removeCm,
                this::sizeOfCmArray
            );
        }
    }

    /**
     * Gets array of all "cm" elements
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTComment[] getCmArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.presentationml.x2006.main.CTComment[0]);
    }

    /**
     * Gets ith "cm" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTComment getCmArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTComment target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTComment)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "cm" element
     */
    @Override
    public int sizeOfCmArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "cm" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCmArray(org.openxmlformats.schemas.presentationml.x2006.main.CTComment[] cmArray) {
        check_orphaned();
        arraySetterHelper(cmArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "cm" element
     */
    @Override
    public void setCmArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTComment cm) {
        generatedSetterHelperImpl(cm, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "cm" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTComment insertNewCm(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTComment target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTComment)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "cm" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTComment addNewCm() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTComment target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTComment)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "cm" element
     */
    @Override
    public void removeCm(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
