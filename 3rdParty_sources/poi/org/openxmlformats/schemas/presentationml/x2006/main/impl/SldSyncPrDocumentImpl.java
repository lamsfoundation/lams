/*
 * An XML document type.
 * Localname: sldSyncPr
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.SldSyncPrDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one sldSyncPr(@http://schemas.openxmlformats.org/presentationml/2006/main) element.
 *
 * This is a complex type.
 */
public class SldSyncPrDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.SldSyncPrDocument {
    private static final long serialVersionUID = 1L;

    public SldSyncPrDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "sldSyncPr"),
    };


    /**
     * Gets the "sldSyncPr" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTSlideSyncProperties getSldSyncPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTSlideSyncProperties target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTSlideSyncProperties)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "sldSyncPr" element
     */
    @Override
    public void setSldSyncPr(org.openxmlformats.schemas.presentationml.x2006.main.CTSlideSyncProperties sldSyncPr) {
        generatedSetterHelperImpl(sldSyncPr, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "sldSyncPr" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTSlideSyncProperties addNewSldSyncPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTSlideSyncProperties target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTSlideSyncProperties)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
