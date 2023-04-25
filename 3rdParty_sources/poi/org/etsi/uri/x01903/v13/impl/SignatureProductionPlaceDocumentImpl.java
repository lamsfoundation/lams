/*
 * An XML document type.
 * Localname: SignatureProductionPlace
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.SignatureProductionPlaceDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one SignatureProductionPlace(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public class SignatureProductionPlaceDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.SignatureProductionPlaceDocument {
    private static final long serialVersionUID = 1L;

    public SignatureProductionPlaceDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "SignatureProductionPlace"),
    };


    /**
     * Gets the "SignatureProductionPlace" element
     */
    @Override
    public org.etsi.uri.x01903.v13.SignatureProductionPlaceType getSignatureProductionPlace() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.SignatureProductionPlaceType target = null;
            target = (org.etsi.uri.x01903.v13.SignatureProductionPlaceType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "SignatureProductionPlace" element
     */
    @Override
    public void setSignatureProductionPlace(org.etsi.uri.x01903.v13.SignatureProductionPlaceType signatureProductionPlace) {
        generatedSetterHelperImpl(signatureProductionPlace, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "SignatureProductionPlace" element
     */
    @Override
    public org.etsi.uri.x01903.v13.SignatureProductionPlaceType addNewSignatureProductionPlace() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.SignatureProductionPlaceType target = null;
            target = (org.etsi.uri.x01903.v13.SignatureProductionPlaceType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
