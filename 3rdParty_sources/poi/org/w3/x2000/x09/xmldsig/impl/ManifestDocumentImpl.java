/*
 * An XML document type.
 * Localname: Manifest
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.ManifestDocument
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one Manifest(@http://www.w3.org/2000/09/xmldsig#) element.
 *
 * This is a complex type.
 */
public class ManifestDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.w3.x2000.x09.xmldsig.ManifestDocument {
    private static final long serialVersionUID = 1L;

    public ManifestDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://www.w3.org/2000/09/xmldsig#", "Manifest"),
    };


    /**
     * Gets the "Manifest" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.ManifestType getManifest() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.ManifestType target = null;
            target = (org.w3.x2000.x09.xmldsig.ManifestType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "Manifest" element
     */
    @Override
    public void setManifest(org.w3.x2000.x09.xmldsig.ManifestType manifest) {
        generatedSetterHelperImpl(manifest, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "Manifest" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.ManifestType addNewManifest() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.ManifestType target = null;
            target = (org.w3.x2000.x09.xmldsig.ManifestType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
