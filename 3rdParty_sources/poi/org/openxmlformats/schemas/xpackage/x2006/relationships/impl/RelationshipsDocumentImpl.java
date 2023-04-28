/*
 * An XML document type.
 * Localname: Relationships
 * Namespace: http://schemas.openxmlformats.org/package/2006/relationships
 * Java type: org.openxmlformats.schemas.xpackage.x2006.relationships.RelationshipsDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.xpackage.x2006.relationships.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one Relationships(@http://schemas.openxmlformats.org/package/2006/relationships) element.
 *
 * This is a complex type.
 */
public class RelationshipsDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.xpackage.x2006.relationships.RelationshipsDocument {
    private static final long serialVersionUID = 1L;

    public RelationshipsDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/package/2006/relationships", "Relationships"),
    };


    /**
     * Gets the "Relationships" element
     */
    @Override
    public org.openxmlformats.schemas.xpackage.x2006.relationships.CTRelationships getRelationships() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.xpackage.x2006.relationships.CTRelationships target = null;
            target = (org.openxmlformats.schemas.xpackage.x2006.relationships.CTRelationships)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "Relationships" element
     */
    @Override
    public void setRelationships(org.openxmlformats.schemas.xpackage.x2006.relationships.CTRelationships relationships) {
        generatedSetterHelperImpl(relationships, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "Relationships" element
     */
    @Override
    public org.openxmlformats.schemas.xpackage.x2006.relationships.CTRelationships addNewRelationships() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.xpackage.x2006.relationships.CTRelationships target = null;
            target = (org.openxmlformats.schemas.xpackage.x2006.relationships.CTRelationships)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
