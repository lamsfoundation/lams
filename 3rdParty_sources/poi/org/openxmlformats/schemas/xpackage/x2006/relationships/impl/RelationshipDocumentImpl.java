/*
 * An XML document type.
 * Localname: Relationship
 * Namespace: http://schemas.openxmlformats.org/package/2006/relationships
 * Java type: org.openxmlformats.schemas.xpackage.x2006.relationships.RelationshipDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.xpackage.x2006.relationships.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one Relationship(@http://schemas.openxmlformats.org/package/2006/relationships) element.
 *
 * This is a complex type.
 */
public class RelationshipDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.xpackage.x2006.relationships.RelationshipDocument {
    private static final long serialVersionUID = 1L;

    public RelationshipDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/package/2006/relationships", "Relationship"),
    };


    /**
     * Gets the "Relationship" element
     */
    @Override
    public org.openxmlformats.schemas.xpackage.x2006.relationships.CTRelationship getRelationship() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.xpackage.x2006.relationships.CTRelationship target = null;
            target = (org.openxmlformats.schemas.xpackage.x2006.relationships.CTRelationship)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "Relationship" element
     */
    @Override
    public void setRelationship(org.openxmlformats.schemas.xpackage.x2006.relationships.CTRelationship relationship) {
        generatedSetterHelperImpl(relationship, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "Relationship" element
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
}
