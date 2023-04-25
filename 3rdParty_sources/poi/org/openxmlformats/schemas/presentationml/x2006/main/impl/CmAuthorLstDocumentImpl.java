/*
 * An XML document type.
 * Localname: cmAuthorLst
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CmAuthorLstDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one cmAuthorLst(@http://schemas.openxmlformats.org/presentationml/2006/main) element.
 *
 * This is a complex type.
 */
public class CmAuthorLstDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CmAuthorLstDocument {
    private static final long serialVersionUID = 1L;

    public CmAuthorLstDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "cmAuthorLst"),
    };


    /**
     * Gets the "cmAuthorLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTCommentAuthorList getCmAuthorLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTCommentAuthorList target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTCommentAuthorList)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "cmAuthorLst" element
     */
    @Override
    public void setCmAuthorLst(org.openxmlformats.schemas.presentationml.x2006.main.CTCommentAuthorList cmAuthorLst) {
        generatedSetterHelperImpl(cmAuthorLst, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "cmAuthorLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTCommentAuthorList addNewCmAuthorLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTCommentAuthorList target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTCommentAuthorList)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
