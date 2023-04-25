/*
 * An XML document type.
 * Localname: comments
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CommentsDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one comments(@http://schemas.openxmlformats.org/spreadsheetml/2006/main) element.
 *
 * This is a complex type.
 */
public class CommentsDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CommentsDocument {
    private static final long serialVersionUID = 1L;

    public CommentsDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "comments"),
    };


    /**
     * Gets the "comments" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTComments getComments() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTComments target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTComments)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "comments" element
     */
    @Override
    public void setComments(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTComments comments) {
        generatedSetterHelperImpl(comments, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "comments" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTComments addNewComments() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTComments target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTComments)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
