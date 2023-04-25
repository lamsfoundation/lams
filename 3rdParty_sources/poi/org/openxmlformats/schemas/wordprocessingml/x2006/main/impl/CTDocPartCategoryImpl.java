/*
 * XML Type:  CT_DocPartCategory
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartCategory
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_DocPartCategory(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTDocPartCategoryImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartCategory {
    private static final long serialVersionUID = 1L;

    public CTDocPartCategoryImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "name"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "gallery"),
    };


    /**
     * Gets the "name" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString getName() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "name" element
     */
    @Override
    public void setName(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString name) {
        generatedSetterHelperImpl(name, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "name" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString addNewName() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Gets the "gallery" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartGallery getGallery() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartGallery target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartGallery)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "gallery" element
     */
    @Override
    public void setGallery(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartGallery gallery) {
        generatedSetterHelperImpl(gallery, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "gallery" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartGallery addNewGallery() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartGallery target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartGallery)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }
}
