/*
 * XML Type:  CT_DatastoreItem
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/customXml
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.customXml.CTDatastoreItem
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.customXml.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_DatastoreItem(@http://schemas.openxmlformats.org/officeDocument/2006/customXml).
 *
 * This is a complex type.
 */
public class CTDatastoreItemImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.officeDocument.x2006.customXml.CTDatastoreItem {
    private static final long serialVersionUID = 1L;

    public CTDatastoreItemImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/customXml", "schemaRefs"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/customXml", "itemID"),
    };


    /**
     * Gets the "schemaRefs" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.customXml.CTDatastoreSchemaRefs getSchemaRefs() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.customXml.CTDatastoreSchemaRefs target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.customXml.CTDatastoreSchemaRefs)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "schemaRefs" element
     */
    @Override
    public boolean isSetSchemaRefs() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "schemaRefs" element
     */
    @Override
    public void setSchemaRefs(org.openxmlformats.schemas.officeDocument.x2006.customXml.CTDatastoreSchemaRefs schemaRefs) {
        generatedSetterHelperImpl(schemaRefs, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "schemaRefs" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.customXml.CTDatastoreSchemaRefs addNewSchemaRefs() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.customXml.CTDatastoreSchemaRefs target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.customXml.CTDatastoreSchemaRefs)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "schemaRefs" element
     */
    @Override
    public void unsetSchemaRefs() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "itemID" attribute
     */
    @Override
    public java.lang.String getItemID() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "itemID" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid xgetItemID() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Sets the "itemID" attribute
     */
    @Override
    public void setItemID(java.lang.String itemID) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setStringValue(itemID);
        }
    }

    /**
     * Sets (as xml) the "itemID" attribute
     */
    @Override
    public void xsetItemID(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid itemID) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(itemID);
        }
    }
}
