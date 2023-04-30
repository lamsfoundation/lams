/*
 * XML Type:  CT_CommentAuthorList
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTCommentAuthorList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_CommentAuthorList(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTCommentAuthorListImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTCommentAuthorList {
    private static final long serialVersionUID = 1L;

    public CTCommentAuthorListImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "cmAuthor"),
    };


    /**
     * Gets a List of "cmAuthor" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTCommentAuthor> getCmAuthorList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCmAuthorArray,
                this::setCmAuthorArray,
                this::insertNewCmAuthor,
                this::removeCmAuthor,
                this::sizeOfCmAuthorArray
            );
        }
    }

    /**
     * Gets array of all "cmAuthor" elements
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTCommentAuthor[] getCmAuthorArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.presentationml.x2006.main.CTCommentAuthor[0]);
    }

    /**
     * Gets ith "cmAuthor" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTCommentAuthor getCmAuthorArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTCommentAuthor target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTCommentAuthor)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "cmAuthor" element
     */
    @Override
    public int sizeOfCmAuthorArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "cmAuthor" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCmAuthorArray(org.openxmlformats.schemas.presentationml.x2006.main.CTCommentAuthor[] cmAuthorArray) {
        check_orphaned();
        arraySetterHelper(cmAuthorArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "cmAuthor" element
     */
    @Override
    public void setCmAuthorArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTCommentAuthor cmAuthor) {
        generatedSetterHelperImpl(cmAuthor, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "cmAuthor" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTCommentAuthor insertNewCmAuthor(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTCommentAuthor target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTCommentAuthor)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "cmAuthor" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTCommentAuthor addNewCmAuthor() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTCommentAuthor target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTCommentAuthor)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "cmAuthor" element
     */
    @Override
    public void removeCmAuthor(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
