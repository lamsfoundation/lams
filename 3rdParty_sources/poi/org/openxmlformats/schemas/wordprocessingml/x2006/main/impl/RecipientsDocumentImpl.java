/*
 * An XML document type.
 * Localname: recipients
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.RecipientsDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one recipients(@http://schemas.openxmlformats.org/wordprocessingml/2006/main) element.
 *
 * This is a complex type.
 */
public class RecipientsDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.RecipientsDocument {
    private static final long serialVersionUID = 1L;

    public RecipientsDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "recipients"),
    };


    /**
     * Gets the "recipients" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRecipients getRecipients() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRecipients target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRecipients)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "recipients" element
     */
    @Override
    public void setRecipients(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRecipients recipients) {
        generatedSetterHelperImpl(recipients, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "recipients" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRecipients addNewRecipients() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRecipients target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRecipients)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
