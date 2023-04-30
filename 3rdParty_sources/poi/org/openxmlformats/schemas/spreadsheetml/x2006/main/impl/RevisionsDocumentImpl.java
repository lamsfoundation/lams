/*
 * An XML document type.
 * Localname: revisions
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.RevisionsDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one revisions(@http://schemas.openxmlformats.org/spreadsheetml/2006/main) element.
 *
 * This is a complex type.
 */
public class RevisionsDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.RevisionsDocument {
    private static final long serialVersionUID = 1L;

    public RevisionsDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "revisions"),
    };


    /**
     * Gets the "revisions" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisions getRevisions() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisions target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisions)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "revisions" element
     */
    @Override
    public void setRevisions(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisions revisions) {
        generatedSetterHelperImpl(revisions, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "revisions" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisions addNewRevisions() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisions target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisions)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
