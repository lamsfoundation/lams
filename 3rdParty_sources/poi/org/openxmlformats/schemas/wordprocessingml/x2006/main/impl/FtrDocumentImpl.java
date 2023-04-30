/*
 * An XML document type.
 * Localname: ftr
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.FtrDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one ftr(@http://schemas.openxmlformats.org/wordprocessingml/2006/main) element.
 *
 * This is a complex type.
 */
public class FtrDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.FtrDocument {
    private static final long serialVersionUID = 1L;

    public FtrDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "ftr"),
    };


    /**
     * Gets the "ftr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHdrFtr getFtr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHdrFtr target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHdrFtr)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "ftr" element
     */
    @Override
    public void setFtr(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHdrFtr ftr) {
        generatedSetterHelperImpl(ftr, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "ftr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHdrFtr addNewFtr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHdrFtr target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHdrFtr)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
