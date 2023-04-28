/*
 * An XML document type.
 * Localname: viewPr
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.ViewPrDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one viewPr(@http://schemas.openxmlformats.org/presentationml/2006/main) element.
 *
 * This is a complex type.
 */
public class ViewPrDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.ViewPrDocument {
    private static final long serialVersionUID = 1L;

    public ViewPrDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "viewPr"),
    };


    /**
     * Gets the "viewPr" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTViewProperties getViewPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTViewProperties target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTViewProperties)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "viewPr" element
     */
    @Override
    public void setViewPr(org.openxmlformats.schemas.presentationml.x2006.main.CTViewProperties viewPr) {
        generatedSetterHelperImpl(viewPr, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "viewPr" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTViewProperties addNewViewPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTViewProperties target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTViewProperties)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
