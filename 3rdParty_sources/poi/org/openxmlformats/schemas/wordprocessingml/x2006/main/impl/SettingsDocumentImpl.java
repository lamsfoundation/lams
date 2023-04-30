/*
 * An XML document type.
 * Localname: settings
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.SettingsDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one settings(@http://schemas.openxmlformats.org/wordprocessingml/2006/main) element.
 *
 * This is a complex type.
 */
public class SettingsDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.SettingsDocument {
    private static final long serialVersionUID = 1L;

    public SettingsDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "settings"),
    };


    /**
     * Gets the "settings" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSettings getSettings() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSettings target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSettings)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "settings" element
     */
    @Override
    public void setSettings(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSettings settings) {
        generatedSetterHelperImpl(settings, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "settings" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSettings addNewSettings() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSettings target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSettings)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
