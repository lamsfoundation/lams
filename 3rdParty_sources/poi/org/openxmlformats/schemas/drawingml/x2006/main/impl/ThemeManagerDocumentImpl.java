/*
 * An XML document type.
 * Localname: themeManager
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.ThemeManagerDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one themeManager(@http://schemas.openxmlformats.org/drawingml/2006/main) element.
 *
 * This is a complex type.
 */
public class ThemeManagerDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.ThemeManagerDocument {
    private static final long serialVersionUID = 1L;

    public ThemeManagerDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "themeManager"),
    };


    /**
     * Gets the "themeManager" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTEmptyElement getThemeManager() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTEmptyElement target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTEmptyElement)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "themeManager" element
     */
    @Override
    public void setThemeManager(org.openxmlformats.schemas.drawingml.x2006.main.CTEmptyElement themeManager) {
        generatedSetterHelperImpl(themeManager, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "themeManager" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTEmptyElement addNewThemeManager() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTEmptyElement target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTEmptyElement)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
