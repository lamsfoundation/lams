/*
 * XML Type:  CT_TLAnimVariant
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariant
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TLAnimVariant(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTTLAnimVariantImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariant {
    private static final long serialVersionUID = 1L;

    public CTTLAnimVariantImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "boolVal"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "intVal"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "fltVal"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "strVal"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "clrVal"),
    };


    /**
     * Gets the "boolVal" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariantBooleanVal getBoolVal() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariantBooleanVal target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariantBooleanVal)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "boolVal" element
     */
    @Override
    public boolean isSetBoolVal() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "boolVal" element
     */
    @Override
    public void setBoolVal(org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariantBooleanVal boolVal) {
        generatedSetterHelperImpl(boolVal, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "boolVal" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariantBooleanVal addNewBoolVal() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariantBooleanVal target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariantBooleanVal)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "boolVal" element
     */
    @Override
    public void unsetBoolVal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "intVal" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariantIntegerVal getIntVal() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariantIntegerVal target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariantIntegerVal)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "intVal" element
     */
    @Override
    public boolean isSetIntVal() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "intVal" element
     */
    @Override
    public void setIntVal(org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariantIntegerVal intVal) {
        generatedSetterHelperImpl(intVal, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "intVal" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariantIntegerVal addNewIntVal() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariantIntegerVal target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariantIntegerVal)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "intVal" element
     */
    @Override
    public void unsetIntVal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "fltVal" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariantFloatVal getFltVal() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariantFloatVal target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariantFloatVal)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "fltVal" element
     */
    @Override
    public boolean isSetFltVal() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "fltVal" element
     */
    @Override
    public void setFltVal(org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariantFloatVal fltVal) {
        generatedSetterHelperImpl(fltVal, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "fltVal" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariantFloatVal addNewFltVal() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariantFloatVal target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariantFloatVal)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "fltVal" element
     */
    @Override
    public void unsetFltVal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "strVal" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariantStringVal getStrVal() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariantStringVal target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariantStringVal)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "strVal" element
     */
    @Override
    public boolean isSetStrVal() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "strVal" element
     */
    @Override
    public void setStrVal(org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariantStringVal strVal) {
        generatedSetterHelperImpl(strVal, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "strVal" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariantStringVal addNewStrVal() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariantStringVal target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariantStringVal)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "strVal" element
     */
    @Override
    public void unsetStrVal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "clrVal" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTColor getClrVal() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTColor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTColor)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "clrVal" element
     */
    @Override
    public boolean isSetClrVal() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "clrVal" element
     */
    @Override
    public void setClrVal(org.openxmlformats.schemas.drawingml.x2006.main.CTColor clrVal) {
        generatedSetterHelperImpl(clrVal, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "clrVal" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTColor addNewClrVal() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTColor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTColor)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "clrVal" element
     */
    @Override
    public void unsetClrVal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }
}
