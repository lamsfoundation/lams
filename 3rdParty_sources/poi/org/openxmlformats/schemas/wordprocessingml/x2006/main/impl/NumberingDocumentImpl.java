/*
 * An XML document type.
 * Localname: numbering
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.NumberingDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one numbering(@http://schemas.openxmlformats.org/wordprocessingml/2006/main) element.
 *
 * This is a complex type.
 */
public class NumberingDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.NumberingDocument {
    private static final long serialVersionUID = 1L;

    public NumberingDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "numbering"),
    };


    /**
     * Gets the "numbering" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumbering getNumbering() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumbering target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumbering)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "numbering" element
     */
    @Override
    public void setNumbering(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumbering numbering) {
        generatedSetterHelperImpl(numbering, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "numbering" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumbering addNewNumbering() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumbering target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumbering)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
