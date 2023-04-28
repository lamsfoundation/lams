/*
 * An XML document type.
 * Localname: webSettings
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.WebSettingsDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one webSettings(@http://schemas.openxmlformats.org/wordprocessingml/2006/main) element.
 *
 * This is a complex type.
 */
public class WebSettingsDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.WebSettingsDocument {
    private static final long serialVersionUID = 1L;

    public WebSettingsDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "webSettings"),
    };


    /**
     * Gets the "webSettings" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTWebSettings getWebSettings() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTWebSettings target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTWebSettings)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "webSettings" element
     */
    @Override
    public void setWebSettings(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTWebSettings webSettings) {
        generatedSetterHelperImpl(webSettings, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "webSettings" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTWebSettings addNewWebSettings() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTWebSettings target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTWebSettings)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
