/*
 * An XML document type.
 * Localname: Properties
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/custom-properties
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.customProperties.PropertiesDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.customProperties.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one Properties(@http://schemas.openxmlformats.org/officeDocument/2006/custom-properties) element.
 *
 * This is a complex type.
 */
public class PropertiesDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.officeDocument.x2006.customProperties.PropertiesDocument {
    private static final long serialVersionUID = 1L;

    public PropertiesDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/custom-properties", "Properties"),
    };


    /**
     * Gets the "Properties" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.customProperties.CTProperties getProperties() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.customProperties.CTProperties target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.customProperties.CTProperties)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "Properties" element
     */
    @Override
    public void setProperties(org.openxmlformats.schemas.officeDocument.x2006.customProperties.CTProperties properties) {
        generatedSetterHelperImpl(properties, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "Properties" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.customProperties.CTProperties addNewProperties() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.customProperties.CTProperties target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.customProperties.CTProperties)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
