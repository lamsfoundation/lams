/*
 * An XML document type.
 * Localname: oleObj
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.OleObjDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one oleObj(@http://schemas.openxmlformats.org/presentationml/2006/main) element.
 *
 * This is a complex type.
 */
public class OleObjDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.OleObjDocument {
    private static final long serialVersionUID = 1L;

    public OleObjDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "oleObj"),
    };


    /**
     * Gets the "oleObj" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTOleObject getOleObj() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTOleObject target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTOleObject)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "oleObj" element
     */
    @Override
    public void setOleObj(org.openxmlformats.schemas.presentationml.x2006.main.CTOleObject oleObj) {
        generatedSetterHelperImpl(oleObj, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "oleObj" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTOleObject addNewOleObj() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTOleObject target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTOleObject)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
