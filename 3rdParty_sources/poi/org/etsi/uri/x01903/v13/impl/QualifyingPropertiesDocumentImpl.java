/*
 * An XML document type.
 * Localname: QualifyingProperties
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.QualifyingPropertiesDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one QualifyingProperties(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public class QualifyingPropertiesDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.QualifyingPropertiesDocument {
    private static final long serialVersionUID = 1L;

    public QualifyingPropertiesDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "QualifyingProperties"),
    };


    /**
     * Gets the "QualifyingProperties" element
     */
    @Override
    public org.etsi.uri.x01903.v13.QualifyingPropertiesType getQualifyingProperties() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.QualifyingPropertiesType target = null;
            target = (org.etsi.uri.x01903.v13.QualifyingPropertiesType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "QualifyingProperties" element
     */
    @Override
    public void setQualifyingProperties(org.etsi.uri.x01903.v13.QualifyingPropertiesType qualifyingProperties) {
        generatedSetterHelperImpl(qualifyingProperties, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "QualifyingProperties" element
     */
    @Override
    public org.etsi.uri.x01903.v13.QualifyingPropertiesType addNewQualifyingProperties() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.QualifyingPropertiesType target = null;
            target = (org.etsi.uri.x01903.v13.QualifyingPropertiesType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
