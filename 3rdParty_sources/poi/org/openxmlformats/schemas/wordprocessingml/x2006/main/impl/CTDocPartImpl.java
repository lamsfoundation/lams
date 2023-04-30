/*
 * XML Type:  CT_DocPart
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPart
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_DocPart(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTDocPartImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPart {
    private static final long serialVersionUID = 1L;

    public CTDocPartImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "docPartPr"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "docPartBody"),
    };


    /**
     * Gets the "docPartPr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartPr getDocPartPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartPr target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartPr)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "docPartPr" element
     */
    @Override
    public boolean isSetDocPartPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "docPartPr" element
     */
    @Override
    public void setDocPartPr(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartPr docPartPr) {
        generatedSetterHelperImpl(docPartPr, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "docPartPr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartPr addNewDocPartPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartPr target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartPr)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "docPartPr" element
     */
    @Override
    public void unsetDocPartPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "docPartBody" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody getDocPartBody() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "docPartBody" element
     */
    @Override
    public boolean isSetDocPartBody() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "docPartBody" element
     */
    @Override
    public void setDocPartBody(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody docPartBody) {
        generatedSetterHelperImpl(docPartBody, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "docPartBody" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody addNewDocPartBody() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "docPartBody" element
     */
    @Override
    public void unsetDocPartBody() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }
}
