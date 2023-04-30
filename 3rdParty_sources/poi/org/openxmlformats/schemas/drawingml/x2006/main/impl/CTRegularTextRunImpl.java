/*
 * XML Type:  CT_RegularTextRun
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_RegularTextRun(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTRegularTextRunImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun {
    private static final long serialVersionUID = 1L;

    public CTRegularTextRunImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "rPr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "t"),
    };


    /**
     * Gets the "rPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties getRPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "rPr" element
     */
    @Override
    public boolean isSetRPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "rPr" element
     */
    @Override
    public void setRPr(org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties rPr) {
        generatedSetterHelperImpl(rPr, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "rPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties addNewRPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "rPr" element
     */
    @Override
    public void unsetRPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "t" element
     */
    @Override
    public java.lang.String getT() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "t" element
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetT() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return target;
        }
    }

    /**
     * Sets the "t" element
     */
    @Override
    public void setT(java.lang.String t) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[1]);
            }
            target.setStringValue(t);
        }
    }

    /**
     * Sets (as xml) the "t" element
     */
    @Override
    public void xsetT(org.apache.xmlbeans.XmlString t) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(PROPERTY_QNAME[1]);
            }
            target.set(t);
        }
    }
}
