/*
 * An XML document type.
 * Localname: footnotes
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.FootnotesDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one footnotes(@http://schemas.openxmlformats.org/wordprocessingml/2006/main) element.
 *
 * This is a complex type.
 */
public class FootnotesDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.FootnotesDocument {
    private static final long serialVersionUID = 1L;

    public FootnotesDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "footnotes"),
    };


    /**
     * Gets the "footnotes" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFootnotes getFootnotes() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFootnotes target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFootnotes)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "footnotes" element
     */
    @Override
    public void setFootnotes(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFootnotes footnotes) {
        generatedSetterHelperImpl(footnotes, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "footnotes" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFootnotes addNewFootnotes() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFootnotes target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFootnotes)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
