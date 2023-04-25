/*
 * XML Type:  CT_Connections
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConnections
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Connections(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTConnectionsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConnections {
    private static final long serialVersionUID = 1L;

    public CTConnectionsImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "connection"),
    };


    /**
     * Gets a List of "connection" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConnection> getConnectionList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getConnectionArray,
                this::setConnectionArray,
                this::insertNewConnection,
                this::removeConnection,
                this::sizeOfConnectionArray
            );
        }
    }

    /**
     * Gets array of all "connection" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConnection[] getConnectionArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConnection[0]);
    }

    /**
     * Gets ith "connection" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConnection getConnectionArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConnection target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConnection)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "connection" element
     */
    @Override
    public int sizeOfConnectionArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "connection" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setConnectionArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConnection[] connectionArray) {
        check_orphaned();
        arraySetterHelper(connectionArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "connection" element
     */
    @Override
    public void setConnectionArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConnection connection) {
        generatedSetterHelperImpl(connection, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "connection" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConnection insertNewConnection(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConnection target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConnection)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "connection" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConnection addNewConnection() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConnection target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConnection)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "connection" element
     */
    @Override
    public void removeConnection(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
