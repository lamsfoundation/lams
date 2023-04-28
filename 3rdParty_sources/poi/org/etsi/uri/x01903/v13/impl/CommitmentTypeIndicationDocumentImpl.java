/*
 * An XML document type.
 * Localname: CommitmentTypeIndication
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.CommitmentTypeIndicationDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one CommitmentTypeIndication(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public class CommitmentTypeIndicationDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.CommitmentTypeIndicationDocument {
    private static final long serialVersionUID = 1L;

    public CommitmentTypeIndicationDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "CommitmentTypeIndication"),
    };


    /**
     * Gets the "CommitmentTypeIndication" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CommitmentTypeIndicationType getCommitmentTypeIndication() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CommitmentTypeIndicationType target = null;
            target = (org.etsi.uri.x01903.v13.CommitmentTypeIndicationType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "CommitmentTypeIndication" element
     */
    @Override
    public void setCommitmentTypeIndication(org.etsi.uri.x01903.v13.CommitmentTypeIndicationType commitmentTypeIndication) {
        generatedSetterHelperImpl(commitmentTypeIndication, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "CommitmentTypeIndication" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CommitmentTypeIndicationType addNewCommitmentTypeIndication() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CommitmentTypeIndicationType target = null;
            target = (org.etsi.uri.x01903.v13.CommitmentTypeIndicationType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
