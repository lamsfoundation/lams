/*
 * An XML document type.
 * Localname: users
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.UsersDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one users(@http://schemas.openxmlformats.org/spreadsheetml/2006/main) element.
 *
 * This is a complex type.
 */
public class UsersDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.UsersDocument {
    private static final long serialVersionUID = 1L;

    public UsersDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "users"),
    };


    /**
     * Gets the "users" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTUsers getUsers() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTUsers target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTUsers)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "users" element
     */
    @Override
    public void setUsers(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTUsers users) {
        generatedSetterHelperImpl(users, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "users" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTUsers addNewUsers() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTUsers target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTUsers)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
