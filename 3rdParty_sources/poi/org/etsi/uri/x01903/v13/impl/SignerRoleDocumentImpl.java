/*
 * An XML document type.
 * Localname: SignerRole
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.SignerRoleDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one SignerRole(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public class SignerRoleDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.SignerRoleDocument {
    private static final long serialVersionUID = 1L;

    public SignerRoleDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "SignerRole"),
    };


    /**
     * Gets the "SignerRole" element
     */
    @Override
    public org.etsi.uri.x01903.v13.SignerRoleType getSignerRole() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.SignerRoleType target = null;
            target = (org.etsi.uri.x01903.v13.SignerRoleType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "SignerRole" element
     */
    @Override
    public void setSignerRole(org.etsi.uri.x01903.v13.SignerRoleType signerRole) {
        generatedSetterHelperImpl(signerRole, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "SignerRole" element
     */
    @Override
    public org.etsi.uri.x01903.v13.SignerRoleType addNewSignerRole() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.SignerRoleType target = null;
            target = (org.etsi.uri.x01903.v13.SignerRoleType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
