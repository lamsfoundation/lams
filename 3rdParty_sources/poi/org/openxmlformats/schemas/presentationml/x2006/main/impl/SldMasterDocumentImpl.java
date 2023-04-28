/*
 * An XML document type.
 * Localname: sldMaster
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.SldMasterDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one sldMaster(@http://schemas.openxmlformats.org/presentationml/2006/main) element.
 *
 * This is a complex type.
 */
public class SldMasterDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.SldMasterDocument {
    private static final long serialVersionUID = 1L;

    public SldMasterDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "sldMaster"),
    };


    /**
     * Gets the "sldMaster" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMaster getSldMaster() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMaster target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMaster)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "sldMaster" element
     */
    @Override
    public void setSldMaster(org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMaster sldMaster) {
        generatedSetterHelperImpl(sldMaster, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "sldMaster" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMaster addNewSldMaster() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMaster target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMaster)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
