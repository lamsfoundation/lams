/*
 * An XML document type.
 * Localname: datastoreItem
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/customXml
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.customXml.DatastoreItemDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.customXml.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one datastoreItem(@http://schemas.openxmlformats.org/officeDocument/2006/customXml) element.
 *
 * This is a complex type.
 */
public class DatastoreItemDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.officeDocument.x2006.customXml.DatastoreItemDocument {
    private static final long serialVersionUID = 1L;

    public DatastoreItemDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/customXml", "datastoreItem"),
    };


    /**
     * Gets the "datastoreItem" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.customXml.CTDatastoreItem getDatastoreItem() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.customXml.CTDatastoreItem target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.customXml.CTDatastoreItem)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "datastoreItem" element
     */
    @Override
    public void setDatastoreItem(org.openxmlformats.schemas.officeDocument.x2006.customXml.CTDatastoreItem datastoreItem) {
        generatedSetterHelperImpl(datastoreItem, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "datastoreItem" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.customXml.CTDatastoreItem addNewDatastoreItem() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.customXml.CTDatastoreItem target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.customXml.CTDatastoreItem)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
