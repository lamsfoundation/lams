/*
 * XML Type:  CT_NonVisualConnectorProperties
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualConnectorProperties
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_NonVisualConnectorProperties(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTNonVisualConnectorPropertiesImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualConnectorProperties {
    private static final long serialVersionUID = 1L;

    public CTNonVisualConnectorPropertiesImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "cxnSpLocks"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "stCxn"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "endCxn"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "extLst"),
    };


    /**
     * Gets the "cxnSpLocks" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTConnectorLocking getCxnSpLocks() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTConnectorLocking target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTConnectorLocking)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "cxnSpLocks" element
     */
    @Override
    public boolean isSetCxnSpLocks() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "cxnSpLocks" element
     */
    @Override
    public void setCxnSpLocks(org.openxmlformats.schemas.drawingml.x2006.main.CTConnectorLocking cxnSpLocks) {
        generatedSetterHelperImpl(cxnSpLocks, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "cxnSpLocks" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTConnectorLocking addNewCxnSpLocks() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTConnectorLocking target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTConnectorLocking)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "cxnSpLocks" element
     */
    @Override
    public void unsetCxnSpLocks() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "stCxn" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTConnection getStCxn() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTConnection target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTConnection)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "stCxn" element
     */
    @Override
    public boolean isSetStCxn() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "stCxn" element
     */
    @Override
    public void setStCxn(org.openxmlformats.schemas.drawingml.x2006.main.CTConnection stCxn) {
        generatedSetterHelperImpl(stCxn, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "stCxn" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTConnection addNewStCxn() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTConnection target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTConnection)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "stCxn" element
     */
    @Override
    public void unsetStCxn() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "endCxn" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTConnection getEndCxn() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTConnection target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTConnection)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "endCxn" element
     */
    @Override
    public boolean isSetEndCxn() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "endCxn" element
     */
    @Override
    public void setEndCxn(org.openxmlformats.schemas.drawingml.x2006.main.CTConnection endCxn) {
        generatedSetterHelperImpl(endCxn, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "endCxn" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTConnection addNewEndCxn() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTConnection target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTConnection)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "endCxn" element
     */
    @Override
    public void unsetEndCxn() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "extLst" element
     */
    @Override
    public boolean isSetExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "extLst" element
     */
    @Override
    public void unsetExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }
}
