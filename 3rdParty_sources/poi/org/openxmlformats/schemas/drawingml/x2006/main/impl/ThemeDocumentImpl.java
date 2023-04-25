/*
 * An XML document type.
 * Localname: theme
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.ThemeDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one theme(@http://schemas.openxmlformats.org/drawingml/2006/main) element.
 *
 * This is a complex type.
 */
public class ThemeDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.ThemeDocument {
    private static final long serialVersionUID = 1L;

    public ThemeDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "theme"),
    };


    /**
     * Gets the "theme" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeStyleSheet getTheme() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeStyleSheet target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeStyleSheet)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "theme" element
     */
    @Override
    public void setTheme(org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeStyleSheet theme) {
        generatedSetterHelperImpl(theme, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "theme" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeStyleSheet addNewTheme() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeStyleSheet target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeStyleSheet)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
