/*
 * An XML document type.
 * Localname: volTypes
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.VolTypesDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one volTypes(@http://schemas.openxmlformats.org/spreadsheetml/2006/main) element.
 *
 * This is a complex type.
 */
public class VolTypesDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.VolTypesDocument {
    private static final long serialVersionUID = 1L;

    public VolTypesDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "volTypes"),
    };


    /**
     * Gets the "volTypes" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolTypes getVolTypes() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolTypes target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolTypes)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "volTypes" element
     */
    @Override
    public void setVolTypes(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolTypes volTypes) {
        generatedSetterHelperImpl(volTypes, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "volTypes" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolTypes addNewVolTypes() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolTypes target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolTypes)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
