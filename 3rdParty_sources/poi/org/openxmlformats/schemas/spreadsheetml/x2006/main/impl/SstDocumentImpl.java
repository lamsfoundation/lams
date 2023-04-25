/*
 * An XML document type.
 * Localname: sst
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.SstDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one sst(@http://schemas.openxmlformats.org/spreadsheetml/2006/main) element.
 *
 * This is a complex type.
 */
public class SstDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.SstDocument {
    private static final long serialVersionUID = 1L;

    public SstDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "sst"),
    };


    /**
     * Gets the "sst" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSst getSst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSst target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSst)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "sst" element
     */
    @Override
    public void setSst(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSst sst) {
        generatedSetterHelperImpl(sst, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "sst" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSst addNewSst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSst target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSst)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
