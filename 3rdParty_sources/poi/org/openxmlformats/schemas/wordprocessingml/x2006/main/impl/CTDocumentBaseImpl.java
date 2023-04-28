/*
 * XML Type:  CT_DocumentBase
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocumentBase
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_DocumentBase(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTDocumentBaseImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocumentBase {
    private static final long serialVersionUID = 1L;

    public CTDocumentBaseImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "background"),
    };


    /**
     * Gets the "background" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBackground getBackground() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBackground target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBackground)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "background" element
     */
    @Override
    public boolean isSetBackground() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "background" element
     */
    @Override
    public void setBackground(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBackground background) {
        generatedSetterHelperImpl(background, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "background" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBackground addNewBackground() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBackground target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBackground)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "background" element
     */
    @Override
    public void unsetBackground() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }
}
