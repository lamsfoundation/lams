/*
 * An XML document type.
 * Localname: sld
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.SldDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one sld(@http://schemas.openxmlformats.org/presentationml/2006/main) element.
 *
 * This is a complex type.
 */
public class SldDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.SldDocument {
    private static final long serialVersionUID = 1L;

    public SldDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "sld"),
    };


    /**
     * Gets the "sld" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTSlide getSld() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTSlide target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTSlide)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "sld" element
     */
    @Override
    public void setSld(org.openxmlformats.schemas.presentationml.x2006.main.CTSlide sld) {
        generatedSetterHelperImpl(sld, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "sld" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTSlide addNewSld() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTSlide target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTSlide)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
