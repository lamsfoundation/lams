/*
 * XML Type:  CT_SdtText
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtText
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_SdtText(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTSdtTextImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtText {
    private static final long serialVersionUID = 1L;

    public CTSdtTextImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "multiLine"),
    };


    /**
     * Gets the "multiLine" attribute
     */
    @Override
    public java.lang.Object getMultiLine() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "multiLine" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff xgetMultiLine() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * True if has "multiLine" attribute
     */
    @Override
    public boolean isSetMultiLine() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[0]) != null;
        }
    }

    /**
     * Sets the "multiLine" attribute
     */
    @Override
    public void setMultiLine(java.lang.Object multiLine) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setObjectValue(multiLine);
        }
    }

    /**
     * Sets (as xml) the "multiLine" attribute
     */
    @Override
    public void xsetMultiLine(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff multiLine) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(multiLine);
        }
    }

    /**
     * Unsets the "multiLine" attribute
     */
    @Override
    public void unsetMultiLine() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[0]);
        }
    }
}
