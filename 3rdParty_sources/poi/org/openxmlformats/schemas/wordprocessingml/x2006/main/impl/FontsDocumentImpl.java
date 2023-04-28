/*
 * An XML document type.
 * Localname: fonts
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.FontsDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one fonts(@http://schemas.openxmlformats.org/wordprocessingml/2006/main) element.
 *
 * This is a complex type.
 */
public class FontsDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.FontsDocument {
    private static final long serialVersionUID = 1L;

    public FontsDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "fonts"),
    };


    /**
     * Gets the "fonts" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontsList getFonts() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontsList target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontsList)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "fonts" element
     */
    @Override
    public void setFonts(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontsList fonts) {
        generatedSetterHelperImpl(fonts, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "fonts" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontsList addNewFonts() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontsList target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontsList)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
