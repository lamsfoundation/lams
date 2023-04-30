/*
 * An XML document type.
 * Localname: styleSheet
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.StyleSheetDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one styleSheet(@http://schemas.openxmlformats.org/spreadsheetml/2006/main) element.
 *
 * This is a complex type.
 */
public class StyleSheetDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.StyleSheetDocument {
    private static final long serialVersionUID = 1L;

    public StyleSheetDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "styleSheet"),
    };


    /**
     * Gets the "styleSheet" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTStylesheet getStyleSheet() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTStylesheet target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTStylesheet)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "styleSheet" element
     */
    @Override
    public void setStyleSheet(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTStylesheet styleSheet) {
        generatedSetterHelperImpl(styleSheet, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "styleSheet" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTStylesheet addNewStyleSheet() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTStylesheet target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTStylesheet)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
