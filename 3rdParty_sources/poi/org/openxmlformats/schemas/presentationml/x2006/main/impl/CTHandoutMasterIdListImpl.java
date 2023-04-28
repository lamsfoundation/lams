/*
 * XML Type:  CT_HandoutMasterIdList
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTHandoutMasterIdList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_HandoutMasterIdList(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTHandoutMasterIdListImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTHandoutMasterIdList {
    private static final long serialVersionUID = 1L;

    public CTHandoutMasterIdListImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "handoutMasterId"),
    };


    /**
     * Gets the "handoutMasterId" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTHandoutMasterIdListEntry getHandoutMasterId() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTHandoutMasterIdListEntry target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTHandoutMasterIdListEntry)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "handoutMasterId" element
     */
    @Override
    public boolean isSetHandoutMasterId() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "handoutMasterId" element
     */
    @Override
    public void setHandoutMasterId(org.openxmlformats.schemas.presentationml.x2006.main.CTHandoutMasterIdListEntry handoutMasterId) {
        generatedSetterHelperImpl(handoutMasterId, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "handoutMasterId" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTHandoutMasterIdListEntry addNewHandoutMasterId() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTHandoutMasterIdListEntry target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTHandoutMasterIdListEntry)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "handoutMasterId" element
     */
    @Override
    public void unsetHandoutMasterId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }
}
