/*
 * An XML document type.
 * Localname: xml
 * Namespace: urn:schemas-poi-apache-org:vmldrawing
 * Java type: org.apache.poi.schemas.vmldrawing.XmlDocument
 *
 * Automatically generated - do not modify.
 */
package org.apache.poi.schemas.vmldrawing.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one xml(@urn:schemas-poi-apache-org:vmldrawing) element.
 *
 * This is a complex type.
 */
public class XmlDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.apache.poi.schemas.vmldrawing.XmlDocument {
    private static final long serialVersionUID = 1L;

    public XmlDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("urn:schemas-poi-apache-org:vmldrawing", "xml"),
    };


    /**
     * Gets the "xml" element
     */
    @Override
    public org.apache.poi.schemas.vmldrawing.CTXML getXml() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.poi.schemas.vmldrawing.CTXML target = null;
            target = (org.apache.poi.schemas.vmldrawing.CTXML)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "xml" element
     */
    @Override
    public void setXml(org.apache.poi.schemas.vmldrawing.CTXML xml) {
        generatedSetterHelperImpl(xml, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "xml" element
     */
    @Override
    public org.apache.poi.schemas.vmldrawing.CTXML addNewXml() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.poi.schemas.vmldrawing.CTXML target = null;
            target = (org.apache.poi.schemas.vmldrawing.CTXML)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
