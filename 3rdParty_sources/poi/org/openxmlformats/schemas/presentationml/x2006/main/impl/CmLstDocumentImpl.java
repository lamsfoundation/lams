/*
 * An XML document type.
 * Localname: cmLst
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CmLstDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one cmLst(@http://schemas.openxmlformats.org/presentationml/2006/main) element.
 *
 * This is a complex type.
 */
public class CmLstDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CmLstDocument {
    private static final long serialVersionUID = 1L;

    public CmLstDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "cmLst"),
    };


    /**
     * Gets the "cmLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTCommentList getCmLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTCommentList target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTCommentList)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "cmLst" element
     */
    @Override
    public void setCmLst(org.openxmlformats.schemas.presentationml.x2006.main.CTCommentList cmLst) {
        generatedSetterHelperImpl(cmLst, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "cmLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTCommentList addNewCmLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTCommentList target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTCommentList)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
