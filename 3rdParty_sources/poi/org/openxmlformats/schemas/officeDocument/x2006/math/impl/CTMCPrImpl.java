/*
 * XML Type:  CT_MCPr
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/math
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.math.CTMCPr
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.math.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_MCPr(@http://schemas.openxmlformats.org/officeDocument/2006/math).
 *
 * This is a complex type.
 */
public class CTMCPrImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.officeDocument.x2006.math.CTMCPr {
    private static final long serialVersionUID = 1L;

    public CTMCPrImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/math", "count"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/math", "mcJc"),
    };


    /**
     * Gets the "count" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTInteger255 getCount() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTInteger255 target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTInteger255)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "count" element
     */
    @Override
    public boolean isSetCount() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "count" element
     */
    @Override
    public void setCount(org.openxmlformats.schemas.officeDocument.x2006.math.CTInteger255 count) {
        generatedSetterHelperImpl(count, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "count" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTInteger255 addNewCount() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTInteger255 target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTInteger255)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "count" element
     */
    @Override
    public void unsetCount() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "mcJc" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTXAlign getMcJc() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTXAlign target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTXAlign)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "mcJc" element
     */
    @Override
    public boolean isSetMcJc() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "mcJc" element
     */
    @Override
    public void setMcJc(org.openxmlformats.schemas.officeDocument.x2006.math.CTXAlign mcJc) {
        generatedSetterHelperImpl(mcJc, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "mcJc" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTXAlign addNewMcJc() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTXAlign target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTXAlign)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "mcJc" element
     */
    @Override
    public void unsetMcJc() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }
}
