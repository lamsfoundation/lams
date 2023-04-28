/*
 * An XML document type.
 * Localname: TimeStampValidationData
 * Namespace: http://uri.etsi.org/01903/v1.4.1#
 * Java type: org.etsi.uri.x01903.v14.TimeStampValidationDataDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v14.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one TimeStampValidationData(@http://uri.etsi.org/01903/v1.4.1#) element.
 *
 * This is a complex type.
 */
public class TimeStampValidationDataDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v14.TimeStampValidationDataDocument {
    private static final long serialVersionUID = 1L;

    public TimeStampValidationDataDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.4.1#", "TimeStampValidationData"),
    };


    /**
     * Gets the "TimeStampValidationData" element
     */
    @Override
    public org.etsi.uri.x01903.v14.ValidationDataType getTimeStampValidationData() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v14.ValidationDataType target = null;
            target = (org.etsi.uri.x01903.v14.ValidationDataType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "TimeStampValidationData" element
     */
    @Override
    public void setTimeStampValidationData(org.etsi.uri.x01903.v14.ValidationDataType timeStampValidationData) {
        generatedSetterHelperImpl(timeStampValidationData, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "TimeStampValidationData" element
     */
    @Override
    public org.etsi.uri.x01903.v14.ValidationDataType addNewTimeStampValidationData() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v14.ValidationDataType target = null;
            target = (org.etsi.uri.x01903.v14.ValidationDataType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
