/*
 * XML Type:  CT_Comments
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTComments
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Comments(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTCommentsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTComments {
    private static final long serialVersionUID = 1L;

    public CTCommentsImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "authors"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "commentList"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "extLst"),
    };


    /**
     * Gets the "authors" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTAuthors getAuthors() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTAuthors target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTAuthors)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "authors" element
     */
    @Override
    public void setAuthors(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTAuthors authors) {
        generatedSetterHelperImpl(authors, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "authors" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTAuthors addNewAuthors() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTAuthors target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTAuthors)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Gets the "commentList" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCommentList getCommentList() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCommentList target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCommentList)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "commentList" element
     */
    @Override
    public void setCommentList(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCommentList commentList) {
        generatedSetterHelperImpl(commentList, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "commentList" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCommentList addNewCommentList() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCommentList target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCommentList)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Gets the "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "extLst" element
     */
    @Override
    public boolean isSetExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "extLst" element
     */
    @Override
    public void unsetExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }
}
