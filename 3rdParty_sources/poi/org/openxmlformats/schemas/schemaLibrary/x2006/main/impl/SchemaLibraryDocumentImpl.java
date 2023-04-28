/*
 * An XML document type.
 * Localname: schemaLibrary
 * Namespace: http://schemas.openxmlformats.org/schemaLibrary/2006/main
 * Java type: org.openxmlformats.schemas.schemaLibrary.x2006.main.SchemaLibraryDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.schemaLibrary.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one schemaLibrary(@http://schemas.openxmlformats.org/schemaLibrary/2006/main) element.
 *
 * This is a complex type.
 */
public class SchemaLibraryDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.schemaLibrary.x2006.main.SchemaLibraryDocument {
    private static final long serialVersionUID = 1L;

    public SchemaLibraryDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/schemaLibrary/2006/main", "schemaLibrary"),
    };


    /**
     * Gets the "schemaLibrary" element
     */
    @Override
    public org.openxmlformats.schemas.schemaLibrary.x2006.main.CTSchemaLibrary getSchemaLibrary() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.schemaLibrary.x2006.main.CTSchemaLibrary target = null;
            target = (org.openxmlformats.schemas.schemaLibrary.x2006.main.CTSchemaLibrary)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "schemaLibrary" element
     */
    @Override
    public void setSchemaLibrary(org.openxmlformats.schemas.schemaLibrary.x2006.main.CTSchemaLibrary schemaLibrary) {
        generatedSetterHelperImpl(schemaLibrary, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "schemaLibrary" element
     */
    @Override
    public org.openxmlformats.schemas.schemaLibrary.x2006.main.CTSchemaLibrary addNewSchemaLibrary() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.schemaLibrary.x2006.main.CTSchemaLibrary target = null;
            target = (org.openxmlformats.schemas.schemaLibrary.x2006.main.CTSchemaLibrary)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
