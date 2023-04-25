/*
 * XML Type:  CT_VectorVariant
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/extended-properties
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.CTVectorVariant
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_VectorVariant(@http://schemas.openxmlformats.org/officeDocument/2006/extended-properties).
 *
 * This is a complex type.
 */
public class CTVectorVariantImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.CTVectorVariant {
    private static final long serialVersionUID = 1L;

    public CTVectorVariantImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes", "vector"),
    };


    /**
     * Gets the "vector" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTVector getVector() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTVector target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTVector)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "vector" element
     */
    @Override
    public void setVector(org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTVector vector) {
        generatedSetterHelperImpl(vector, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "vector" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTVector addNewVector() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTVector target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTVector)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
