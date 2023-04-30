/*
 * XML Type:  CT_NameType
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/bibliography
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.bibliography.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_NameType(@http://schemas.openxmlformats.org/officeDocument/2006/bibliography).
 *
 * This is a complex type.
 */
public class CTNameTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType {
    private static final long serialVersionUID = 1L;

    public CTNameTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "NameList"),
    };


    /**
     * Gets the "NameList" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameListType getNameList() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameListType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameListType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "NameList" element
     */
    @Override
    public void setNameList(org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameListType nameList) {
        generatedSetterHelperImpl(nameList, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "NameList" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameListType addNewNameList() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameListType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameListType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
