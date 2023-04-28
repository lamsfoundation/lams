/*
 * An XML document type.
 * Localname: presentationPr
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.PresentationPrDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one presentationPr(@http://schemas.openxmlformats.org/presentationml/2006/main) element.
 *
 * This is a complex type.
 */
public class PresentationPrDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.PresentationPrDocument {
    private static final long serialVersionUID = 1L;

    public PresentationPrDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "presentationPr"),
    };


    /**
     * Gets the "presentationPr" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTPresentationProperties getPresentationPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTPresentationProperties target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTPresentationProperties)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "presentationPr" element
     */
    @Override
    public void setPresentationPr(org.openxmlformats.schemas.presentationml.x2006.main.CTPresentationProperties presentationPr) {
        generatedSetterHelperImpl(presentationPr, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "presentationPr" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTPresentationProperties addNewPresentationPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTPresentationProperties target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTPresentationProperties)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
