/*
 * XML Type:  CT_OleLink
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleLink
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_OleLink(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTOleLinkImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleLink {
    private static final long serialVersionUID = 1L;

    public CTOleLinkImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "oleItems"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/relationships", "id"),
        new QName("", "progId"),
    };


    /**
     * Gets the "oleItems" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleItems getOleItems() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleItems target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleItems)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "oleItems" element
     */
    @Override
    public boolean isSetOleItems() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "oleItems" element
     */
    @Override
    public void setOleItems(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleItems oleItems) {
        generatedSetterHelperImpl(oleItems, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "oleItems" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleItems addNewOleItems() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleItems target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleItems)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "oleItems" element
     */
    @Override
    public void unsetOleItems() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "id" attribute
     */
    @Override
    public java.lang.String getId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "id" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId xgetId() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Sets the "id" attribute
     */
    @Override
    public void setId(java.lang.String id) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setStringValue(id);
        }
    }

    /**
     * Sets (as xml) the "id" attribute
     */
    @Override
    public void xsetId(org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId id) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(id);
        }
    }

    /**
     * Gets the "progId" attribute
     */
    @Override
    public java.lang.String getProgId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "progId" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetProgId() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Sets the "progId" attribute
     */
    @Override
    public void setProgId(java.lang.String progId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setStringValue(progId);
        }
    }

    /**
     * Sets (as xml) the "progId" attribute
     */
    @Override
    public void xsetProgId(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring progId) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(progId);
        }
    }
}
