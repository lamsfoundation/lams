/*
 * XML Type:  CT_TLTimeNodeParallel
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeNodeParallel
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TLTimeNodeParallel(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTTLTimeNodeParallelImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeNodeParallel {
    private static final long serialVersionUID = 1L;

    public CTTLTimeNodeParallelImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "cTn"),
    };


    /**
     * Gets the "cTn" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonTimeNodeData getCTn() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonTimeNodeData target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonTimeNodeData)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "cTn" element
     */
    @Override
    public void setCTn(org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonTimeNodeData cTn) {
        generatedSetterHelperImpl(cTn, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "cTn" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonTimeNodeData addNewCTn() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonTimeNodeData target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonTimeNodeData)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
