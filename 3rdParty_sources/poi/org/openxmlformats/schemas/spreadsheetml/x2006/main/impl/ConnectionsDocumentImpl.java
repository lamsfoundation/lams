/*
 * An XML document type.
 * Localname: connections
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.ConnectionsDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one connections(@http://schemas.openxmlformats.org/spreadsheetml/2006/main) element.
 *
 * This is a complex type.
 */
public class ConnectionsDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.ConnectionsDocument {
    private static final long serialVersionUID = 1L;

    public ConnectionsDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "connections"),
    };


    /**
     * Gets the "connections" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConnections getConnections() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConnections target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConnections)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "connections" element
     */
    @Override
    public void setConnections(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConnections connections) {
        generatedSetterHelperImpl(connections, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "connections" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConnections addNewConnections() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConnections target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConnections)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
