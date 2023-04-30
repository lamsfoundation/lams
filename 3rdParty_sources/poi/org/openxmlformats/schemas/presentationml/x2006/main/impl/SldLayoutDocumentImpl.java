/*
 * An XML document type.
 * Localname: sldLayout
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.SldLayoutDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one sldLayout(@http://schemas.openxmlformats.org/presentationml/2006/main) element.
 *
 * This is a complex type.
 */
public class SldLayoutDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.SldLayoutDocument {
    private static final long serialVersionUID = 1L;

    public SldLayoutDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "sldLayout"),
    };


    /**
     * Gets the "sldLayout" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTSlideLayout getSldLayout() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTSlideLayout target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTSlideLayout)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "sldLayout" element
     */
    @Override
    public void setSldLayout(org.openxmlformats.schemas.presentationml.x2006.main.CTSlideLayout sldLayout) {
        generatedSetterHelperImpl(sldLayout, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "sldLayout" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTSlideLayout addNewSldLayout() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTSlideLayout target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTSlideLayout)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
