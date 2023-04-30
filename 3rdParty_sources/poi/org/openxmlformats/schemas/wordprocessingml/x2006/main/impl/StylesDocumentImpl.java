/*
 * An XML document type.
 * Localname: styles
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.StylesDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one styles(@http://schemas.openxmlformats.org/wordprocessingml/2006/main) element.
 *
 * This is a complex type.
 */
public class StylesDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.StylesDocument {
    private static final long serialVersionUID = 1L;

    public StylesDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "styles"),
    };


    /**
     * Gets the "styles" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles getStyles() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "styles" element
     */
    @Override
    public void setStyles(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles styles) {
        generatedSetterHelperImpl(styles, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "styles" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles addNewStyles() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
