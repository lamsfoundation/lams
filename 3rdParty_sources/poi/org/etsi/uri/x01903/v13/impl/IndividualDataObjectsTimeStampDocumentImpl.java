/*
 * An XML document type.
 * Localname: IndividualDataObjectsTimeStamp
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.IndividualDataObjectsTimeStampDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one IndividualDataObjectsTimeStamp(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public class IndividualDataObjectsTimeStampDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.IndividualDataObjectsTimeStampDocument {
    private static final long serialVersionUID = 1L;

    public IndividualDataObjectsTimeStampDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "IndividualDataObjectsTimeStamp"),
    };


    /**
     * Gets the "IndividualDataObjectsTimeStamp" element
     */
    @Override
    public org.etsi.uri.x01903.v13.XAdESTimeStampType getIndividualDataObjectsTimeStamp() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.XAdESTimeStampType target = null;
            target = (org.etsi.uri.x01903.v13.XAdESTimeStampType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "IndividualDataObjectsTimeStamp" element
     */
    @Override
    public void setIndividualDataObjectsTimeStamp(org.etsi.uri.x01903.v13.XAdESTimeStampType individualDataObjectsTimeStamp) {
        generatedSetterHelperImpl(individualDataObjectsTimeStamp, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "IndividualDataObjectsTimeStamp" element
     */
    @Override
    public org.etsi.uri.x01903.v13.XAdESTimeStampType addNewIndividualDataObjectsTimeStamp() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.XAdESTimeStampType target = null;
            target = (org.etsi.uri.x01903.v13.XAdESTimeStampType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
