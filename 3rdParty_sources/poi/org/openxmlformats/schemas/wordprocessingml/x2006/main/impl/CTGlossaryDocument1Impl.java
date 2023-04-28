/*
 * XML Type:  CT_GlossaryDocument
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTGlossaryDocument1
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_GlossaryDocument(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTGlossaryDocument1Impl extends org.openxmlformats.schemas.wordprocessingml.x2006.main.impl.CTDocumentBaseImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTGlossaryDocument1 {
    private static final long serialVersionUID = 1L;

    public CTGlossaryDocument1Impl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "docParts"),
    };


    /**
     * Gets the "docParts" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocParts getDocParts() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocParts target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocParts)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "docParts" element
     */
    @Override
    public boolean isSetDocParts() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "docParts" element
     */
    @Override
    public void setDocParts(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocParts docParts) {
        generatedSetterHelperImpl(docParts, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "docParts" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocParts addNewDocParts() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocParts target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocParts)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "docParts" element
     */
    @Override
    public void unsetDocParts() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }
}
