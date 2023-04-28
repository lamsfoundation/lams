/*
 * An XML document type.
 * Localname: Sources
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/bibliography
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.bibliography.SourcesDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.bibliography.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one Sources(@http://schemas.openxmlformats.org/officeDocument/2006/bibliography) element.
 *
 * This is a complex type.
 */
public class SourcesDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.officeDocument.x2006.bibliography.SourcesDocument {
    private static final long serialVersionUID = 1L;

    public SourcesDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "Sources"),
    };


    /**
     * Gets the "Sources" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTSources getSources() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTSources target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTSources)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "Sources" element
     */
    @Override
    public void setSources(org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTSources sources) {
        generatedSetterHelperImpl(sources, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "Sources" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTSources addNewSources() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTSources target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTSources)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
