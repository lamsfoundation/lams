/*
 * An XML document type.
 * Localname: presentation
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.PresentationDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one presentation(@http://schemas.openxmlformats.org/presentationml/2006/main) element.
 *
 * This is a complex type.
 */
public class PresentationDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.PresentationDocument {
    private static final long serialVersionUID = 1L;

    public PresentationDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "presentation"),
    };


    /**
     * Gets the "presentation" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTPresentation getPresentation() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTPresentation target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTPresentation)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "presentation" element
     */
    @Override
    public void setPresentation(org.openxmlformats.schemas.presentationml.x2006.main.CTPresentation presentation) {
        generatedSetterHelperImpl(presentation, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "presentation" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTPresentation addNewPresentation() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTPresentation target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTPresentation)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
