/*
 * An XML document type.
 * Localname: variant
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.VariantDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one variant(@http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes) element.
 *
 * This is a complex type.
 */
public class VariantDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.VariantDocument {
    private static final long serialVersionUID = 1L;

    public VariantDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes", "variant"),
    };


    /**
     * Gets the "variant" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTVariant getVariant() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTVariant target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTVariant)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "variant" element
     */
    @Override
    public void setVariant(org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTVariant variant) {
        generatedSetterHelperImpl(variant, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "variant" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTVariant addNewVariant() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTVariant target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTVariant)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
