/*
 * XML Type:  CT_Comments
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTComments
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Comments(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTCommentsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTComments {
    private static final long serialVersionUID = 1L;

    public CTCommentsImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "comment"),
    };


    /**
     * Gets a List of "comment" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTComment> getCommentList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCommentArray,
                this::setCommentArray,
                this::insertNewComment,
                this::removeComment,
                this::sizeOfCommentArray
            );
        }
    }

    /**
     * Gets array of all "comment" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTComment[] getCommentArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTComment[0]);
    }

    /**
     * Gets ith "comment" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTComment getCommentArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTComment target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTComment)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "comment" element
     */
    @Override
    public int sizeOfCommentArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "comment" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCommentArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTComment[] commentArray) {
        check_orphaned();
        arraySetterHelper(commentArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "comment" element
     */
    @Override
    public void setCommentArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTComment comment) {
        generatedSetterHelperImpl(comment, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "comment" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTComment insertNewComment(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTComment target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTComment)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "comment" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTComment addNewComment() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTComment target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTComment)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "comment" element
     */
    @Override
    public void removeComment(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
