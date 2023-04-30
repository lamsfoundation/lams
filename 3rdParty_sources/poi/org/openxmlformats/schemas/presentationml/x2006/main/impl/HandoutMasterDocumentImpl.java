/*
 * An XML document type.
 * Localname: handoutMaster
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.HandoutMasterDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one handoutMaster(@http://schemas.openxmlformats.org/presentationml/2006/main) element.
 *
 * This is a complex type.
 */
public class HandoutMasterDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.HandoutMasterDocument {
    private static final long serialVersionUID = 1L;

    public HandoutMasterDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "handoutMaster"),
    };


    /**
     * Gets the "handoutMaster" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTHandoutMaster getHandoutMaster() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTHandoutMaster target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTHandoutMaster)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "handoutMaster" element
     */
    @Override
    public void setHandoutMaster(org.openxmlformats.schemas.presentationml.x2006.main.CTHandoutMaster handoutMaster) {
        generatedSetterHelperImpl(handoutMaster, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "handoutMaster" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTHandoutMaster addNewHandoutMaster() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTHandoutMaster target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTHandoutMaster)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
