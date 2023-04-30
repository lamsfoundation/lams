/*
 * XML Type:  CT_Relationships
 * Namespace: http://schemas.openxmlformats.org/package/2006/relationships
 * Java type: org.openxmlformats.schemas.xpackage.x2006.relationships.CTRelationships
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.xpackage.x2006.relationships.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Relationships(@http://schemas.openxmlformats.org/package/2006/relationships).
 *
 * This is a complex type.
 */
public class CTRelationshipsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.xpackage.x2006.relationships.CTRelationships {
    private static final long serialVersionUID = 1L;

    public CTRelationshipsImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/package/2006/relationships", "Relationship"),
    };


    /**
     * Gets a List of "Relationship" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.xpackage.x2006.relationships.CTRelationship> getRelationshipList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getRelationshipArray,
                this::setRelationshipArray,
                this::insertNewRelationship,
                this::removeRelationship,
                this::sizeOfRelationshipArray
            );
        }
    }

    /**
     * Gets array of all "Relationship" elements
     */
    @Override
    public org.openxmlformats.schemas.xpackage.x2006.relationships.CTRelationship[] getRelationshipArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.xpackage.x2006.relationships.CTRelationship[0]);
    }

    /**
     * Gets ith "Relationship" element
     */
    @Override
    public org.openxmlformats.schemas.xpackage.x2006.relationships.CTRelationship getRelationshipArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.xpackage.x2006.relationships.CTRelationship target = null;
            target = (org.openxmlformats.schemas.xpackage.x2006.relationships.CTRelationship)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "Relationship" element
     */
    @Override
    public int sizeOfRelationshipArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "Relationship" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setRelationshipArray(org.openxmlformats.schemas.xpackage.x2006.relationships.CTRelationship[] relationshipArray) {
        check_orphaned();
        arraySetterHelper(relationshipArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "Relationship" element
     */
    @Override
    public void setRelationshipArray(int i, org.openxmlformats.schemas.xpackage.x2006.relationships.CTRelationship relationship) {
        generatedSetterHelperImpl(relationship, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Relationship" element
     */
    @Override
    public org.openxmlformats.schemas.xpackage.x2006.relationships.CTRelationship insertNewRelationship(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.xpackage.x2006.relationships.CTRelationship target = null;
            target = (org.openxmlformats.schemas.xpackage.x2006.relationships.CTRelationship)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "Relationship" element
     */
    @Override
    public org.openxmlformats.schemas.xpackage.x2006.relationships.CTRelationship addNewRelationship() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.xpackage.x2006.relationships.CTRelationship target = null;
            target = (org.openxmlformats.schemas.xpackage.x2006.relationships.CTRelationship)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "Relationship" element
     */
    @Override
    public void removeRelationship(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
