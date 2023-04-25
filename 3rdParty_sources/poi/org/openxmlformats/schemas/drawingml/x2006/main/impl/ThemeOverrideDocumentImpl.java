/*
 * An XML document type.
 * Localname: themeOverride
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.ThemeOverrideDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one themeOverride(@http://schemas.openxmlformats.org/drawingml/2006/main) element.
 *
 * This is a complex type.
 */
public class ThemeOverrideDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.ThemeOverrideDocument {
    private static final long serialVersionUID = 1L;

    public ThemeOverrideDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "themeOverride"),
    };


    /**
     * Gets the "themeOverride" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTBaseStylesOverride getThemeOverride() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTBaseStylesOverride target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTBaseStylesOverride)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "themeOverride" element
     */
    @Override
    public void setThemeOverride(org.openxmlformats.schemas.drawingml.x2006.main.CTBaseStylesOverride themeOverride) {
        generatedSetterHelperImpl(themeOverride, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "themeOverride" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTBaseStylesOverride addNewThemeOverride() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTBaseStylesOverride target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTBaseStylesOverride)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
